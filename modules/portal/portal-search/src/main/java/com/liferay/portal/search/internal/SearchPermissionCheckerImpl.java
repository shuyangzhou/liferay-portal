/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.search.internal;

import com.liferay.portal.NoSuchResourceException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchPermissionChecker;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.TermsFilter;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.security.permission.UserBag;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.service.ResourceBlockLocalService;
import com.liferay.portal.service.ResourcePermissionLocalService;
import com.liferay.portal.service.RoleLocalService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.util.Portal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Allen Chiang
 * @author Bruno Farache
 * @author Raymond Augé
 * @author Amos Fong
 * @author Preston Crary
 */
@Component(immediate = true, service = SearchPermissionChecker.class)
public class SearchPermissionCheckerImpl implements SearchPermissionChecker {

	@Override
	public void addPermissionFields(long companyId, Document document) {
		try {
			long groupId = GetterUtil.getLong(document.get(Field.GROUP_ID));

			String className = document.get(Field.ENTRY_CLASS_NAME);
			String classPK = document.get(Field.ENTRY_CLASS_PK);

			if (Validator.isNull(className) && Validator.isNull(classPK)) {
				className = document.get(Field.ROOT_ENTRY_CLASS_NAME);
				classPK = document.get(Field.ROOT_ENTRY_CLASS_PK);
			}

			boolean relatedEntry = GetterUtil.getBoolean(
				document.get(Field.RELATED_ENTRY));

			if (relatedEntry) {
				long classNameId = GetterUtil.getLong(
					document.get(Field.CLASS_NAME_ID));

				className = _portal.getClassName(classNameId);
				classPK = document.get(Field.CLASS_PK);
			}

			if (Validator.isNull(className) || Validator.isNull(classPK)) {
				return;
			}

			Indexer<?> indexer = _indexerRegistry.nullSafeGetIndexer(className);

			if (!indexer.isPermissionAware()) {
				return;
			}

			doAddPermissionFields(
				companyId, groupId, className, classPK, document);
		}
		catch (NoSuchResourceException nsre) {
			if (_log.isDebugEnabled()) {
				_log.debug(nsre, nsre);
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	@Override
	public BooleanFilter getPermissionBooleanFilter(
		long companyId, long[] groupIds, long userId, String className,
		BooleanFilter booleanFilter, SearchContext searchContext) {

		try {
			booleanFilter = doGetPermissionBooleanFilter(
				companyId, groupIds, userId, className, booleanFilter,
				searchContext);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return booleanFilter;
	}

	@Override
	public void updatePermissionFields(
		String resourceName, String resourceClassPK) {

		try {
			doUpdatePermissionFields(resourceName, resourceClassPK);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected void doAddPermissionFields(
			long companyId, long groupId, String className, String classPK,
			Document doc)
		throws Exception {

		List<Role> roles = null;

		if (_resourceBlockLocalService.isSupported(className)) {
			roles = _resourceBlockLocalService.getRoles(
				className, Long.valueOf(classPK), ActionKeys.VIEW);
		}
		else {
			roles = _resourcePermissionLocalService.getRoles(
				companyId, className, ResourceConstants.SCOPE_INDIVIDUAL,
				classPK, ActionKeys.VIEW);
		}

		if (roles.isEmpty()) {
			return;
		}

		List<Long> roleIds = new ArrayList<>();
		List<String> groupRoleIds = new ArrayList<>();

		for (Role role : roles) {
			if ((role.getType() == RoleConstants.TYPE_ORGANIZATION) ||
				(role.getType() == RoleConstants.TYPE_SITE)) {

				groupRoleIds.add(getGroupRoleTerm(groupId, role.getRoleId()));
			}
			else {
				roleIds.add(role.getRoleId());
			}
		}

		doc.addKeyword(
			Field.ROLE_ID, roleIds.toArray(new Long[roleIds.size()]));
		doc.addKeyword(
			Field.GROUP_ROLE_ID,
			groupRoleIds.toArray(new String[groupRoleIds.size()]));
	}

	protected BooleanFilter doGetPermissionBooleanFilter(
			long companyId, long[] groupIds, long userId, String className,
			BooleanFilter booleanFilter, SearchContext searchContext)
		throws Exception {

		Indexer<?> indexer = _indexerRegistry.getIndexer(className);

		if (!indexer.isPermissionAware()) {
			return booleanFilter;
		}

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		User user = permissionChecker.getUser();

		if ((user == null) || (user.getUserId() != userId)) {
			user = _userLocalService.fetchUser(userId);

			if (user == null) {
				return booleanFilter;
			}

			permissionChecker = PermissionCheckerFactoryUtil.create(user);
		}

		if (permissionChecker.getUserBag() == null) {
			return booleanFilter;
		}

		if (permissionChecker.isCompanyAdmin(companyId)) {
			return booleanFilter;
		}

		Set<Group> groups = new LinkedHashSet<>();
		Set<Role> roles = new LinkedHashSet<>();
		Map<Long, long[]> groupIdsToRoleIds = new HashMap<>();

		populate(
			companyId, groupIds, userId, permissionChecker, groups, roles,
			groupIdsToRoleIds);

		return doGetPermissionFilter(
			companyId, userId, permissionChecker, className, booleanFilter,
			groups, roles, groupIdsToRoleIds);
	}

	protected BooleanFilter doGetPermissionFilter(
			long companyId, long userId, PermissionChecker permissionChecker,
			String className, BooleanFilter booleanFilter, Set<Group> groups,
			Set<Role> roles, Map<Long, long[]> groupIdsToRoleIds)
		throws Exception {

		BooleanFilter permissionBooleanFilter = new BooleanFilter();

		permissionBooleanFilter.addTerm(Field.USER_ID, userId);

		TermsFilter groupsTermsFilter = new TermsFilter(Field.GROUP_ID);
		TermsFilter groupRolesTermsFilter = new TermsFilter(
			Field.GROUP_ROLE_ID);
		TermsFilter rolesTermsFilter = new TermsFilter(Field.ROLE_ID);

		List<Long> roleIds = new ArrayList<>(roles.size());
		List<Long> regularRoleIds = new ArrayList<>();

		for (Role role : roles) {
			roleIds.add(role.getRoleId());

			if (role.getType() == RoleConstants.TYPE_REGULAR) {
				regularRoleIds.add(role.getRoleId());
			}

			rolesTermsFilter.addValue(String.valueOf(role.getRoleId()));
		}

		long[] roleIdsArray = ArrayUtil.toLongArray(roleIds);

		if (_resourcePermissionLocalService.hasResourcePermission(
				companyId, className, ResourceConstants.SCOPE_COMPANY,
				String.valueOf(companyId), roleIdsArray, ActionKeys.VIEW)) {

			return booleanFilter;
		}

		if (_resourcePermissionLocalService.hasResourcePermission(
				companyId, className, ResourceConstants.SCOPE_GROUP_TEMPLATE,
				String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
				ArrayUtil.toLongArray(regularRoleIds), ActionKeys.VIEW)) {

			return booleanFilter;
		}

		for (Group group : groups) {
			if (permissionChecker.isGroupAdmin(
					group.getGroupId()) ||
				_resourcePermissionLocalService.hasResourcePermission(
					companyId, className, ResourceConstants.SCOPE_GROUP,
					String.valueOf(group.getGroupId()), roleIdsArray,
					ActionKeys.VIEW)) {

				groupsTermsFilter.addValue(String.valueOf(group.getGroupId()));
			}

			long[] groupRoleIds = groupIdsToRoleIds.get(group.getGroupId());

			if (_resourcePermissionLocalService.hasResourcePermission(
					companyId, className,
					ResourceConstants.SCOPE_GROUP_TEMPLATE,
					String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
					groupRoleIds, ActionKeys.VIEW)) {

				groupsTermsFilter.addValue(String.valueOf(group.getGroupId()));
			}

			for (long roleId : groupRoleIds) {
				groupRolesTermsFilter.addValue(
					getGroupRoleTerm(group.getGroupId(), roleId));
			}
		}

		if (!groupsTermsFilter.isEmpty()) {
			permissionBooleanFilter.add(groupsTermsFilter);
		}

		if (!groupRolesTermsFilter.isEmpty()) {
			permissionBooleanFilter.add(groupRolesTermsFilter);
		}

		if (!rolesTermsFilter.isEmpty()) {
			permissionBooleanFilter.add(rolesTermsFilter);
		}

		if (!permissionBooleanFilter.hasClauses()) {
			return booleanFilter;
		}

		BooleanFilter fullBooleanFilter = new BooleanFilter();

		if ((booleanFilter != null) && booleanFilter.hasClauses()) {
			fullBooleanFilter.add(booleanFilter, BooleanClauseOccur.MUST);
		}

		fullBooleanFilter.add(permissionBooleanFilter, BooleanClauseOccur.MUST);

		return fullBooleanFilter;
	}

	protected void doUpdatePermissionFields(
			String resourceName, String resourceClassPK)
		throws Exception {

		Indexer<?> indexer = _indexerRegistry.nullSafeGetIndexer(resourceName);

		if (indexer.isPermissionAware()) {
			indexer.reindex(resourceName, GetterUtil.getLong(resourceClassPK));
		}
	}

	protected String getGroupRoleTerm(long groupId, long roleId) {
		return String.valueOf(groupId).concat(StringPool.DASH).concat(
			String.valueOf(roleId));
	}

	protected void populate(
			long companyId, long[] groupIds, long userId,
			PermissionChecker permissionChecker, Set<Group> groups,
			Set<Role> roles, Map<Long, long[]> groupIdsToRoles)
		throws Exception {

		UserBag userBag = permissionChecker.getUserBag();

		if (ArrayUtil.isEmpty(groupIds)) {
			groups.addAll(userBag.getUserGroups());
			groups.addAll(userBag.getUserOrgGroups());
		}
		else {
			groups.addAll(_groupLocalService.getGroups(groupIds));
		}

		if (permissionChecker.isSignedIn()) {
			roles.addAll(userBag.getRoles());
		}
		else {
			Group guestGroup = _groupLocalService.getGroup(
				companyId, GroupConstants.GUEST);

			roles.addAll(
				_roleLocalService.getUserRelatedRoles(
					userId, Collections.singletonList(guestGroup)));
		}

		if (permissionChecker.isCheckGuest()) {
			roles.add(
				_roleLocalService.getRole(companyId, RoleConstants.GUEST));
		}

		Role organizationUserRole = _roleLocalService.getRole(
			companyId, RoleConstants.ORGANIZATION_USER);
		Role siteMemberRole = _roleLocalService.getRole(
			companyId, RoleConstants.SITE_MEMBER);

		for (Group group : groups) {
			long[] roleIds = permissionChecker.getRoleIds(
				userId, group.getGroupId());

			List<Role> groupRoles = _roleLocalService.getRoles(roleIds);

			ListIterator<Role> listIterator = groupRoles.listIterator();

			while (listIterator.hasNext()) {
				Role role = listIterator.next();

				if ((role.getType() != RoleConstants.TYPE_ORGANIZATION) &&
					(role.getType() != RoleConstants.TYPE_SITE)) {

					listIterator.remove();
				}
			}

			if (!groupRoles.contains(organizationUserRole)) {
				groupRoles.add(organizationUserRole);
			}

			if (!groupRoles.contains(siteMemberRole)) {
				groupRoles.add(siteMemberRole);
			}

			long[] groupRoleIds = ListUtil.toLongArray(
				groupRoles, Role.ROLE_ID_ACCESSOR);

			Arrays.sort(groupRoleIds);

			groupIdsToRoles.put(group.getGroupId(), groupRoleIds);

			roles.addAll(groupRoles);
		}
	}

	@Reference(unbind = "-")
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	@Reference(unbind = "-")
	protected void setIndexerRegistry(IndexerRegistry indexerRegistry) {
		_indexerRegistry = indexerRegistry;
	}

	@Reference(unbind = "-")
	protected void setPortal(Portal portal) {
		_portal = portal;
	}

	@Reference(unbind = "-")
	protected void setResourceBlockLocalService(
		ResourceBlockLocalService resourceBlockLocalService) {

		_resourceBlockLocalService = resourceBlockLocalService;
	}

	@Reference(unbind = "-")
	protected void setResourcePermissionLocalService(
		ResourcePermissionLocalService resourcePermissionLocalService) {

		_resourcePermissionLocalService = resourcePermissionLocalService;
	}

	@Reference(unbind = "-")
	protected void setRoleLocalService(RoleLocalService roleLocalService) {
		_roleLocalService = roleLocalService;
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SearchPermissionCheckerImpl.class);

	private volatile GroupLocalService _groupLocalService;
	private volatile IndexerRegistry _indexerRegistry;
	private volatile Portal _portal;
	private volatile ResourceBlockLocalService _resourceBlockLocalService;
	private volatile ResourcePermissionLocalService
		_resourcePermissionLocalService;
	private volatile RoleLocalService _roleLocalService;
	private volatile UserLocalService _userLocalService;

}
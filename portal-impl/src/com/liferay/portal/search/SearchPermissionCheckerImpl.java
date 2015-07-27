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

package com.liferay.portal.search;

import com.liferay.portal.NoSuchResourceException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchPermissionChecker;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.TermsFilter;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.AdvancedPermissionChecker;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerBag;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.security.permission.UserPermissionCheckerBag;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ResourceBlockLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Allen Chiang
 * @author Bruno Farache
 * @author Raymond Augé
 * @author Amos Fong
 */
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

				className = PortalUtil.getClassName(classNameId);

				classPK = document.get(Field.CLASS_PK);
			}

			if (Validator.isNull(className) || Validator.isNull(classPK)) {
				return;
			}

			Indexer<?> indexer = IndexerRegistryUtil.nullSafeGetIndexer(
				className);

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
				companyId, groupIds, userId, className, booleanFilter);
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
			Indexer<?> indexer = IndexerRegistryUtil.nullSafeGetIndexer(
				resourceName);

			if (indexer.isPermissionAware()) {
				indexer.reindex(
					resourceName, GetterUtil.getLong(resourceClassPK));
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected void addRequiredRoles(long companyId, Set<Role> roles)
		throws Exception {

		Role guestRole = RoleLocalServiceUtil.getRole(
			companyId, RoleConstants.GUEST);

		roles.add(guestRole);

		Role organizationUserRole = RoleLocalServiceUtil.getRole(
			companyId, RoleConstants.ORGANIZATION_USER);

		roles.add(organizationUserRole);

		Role siteMemberRole = RoleLocalServiceUtil.getRole(
			companyId, RoleConstants.SITE_MEMBER);

		roles.add(siteMemberRole);
	}

	protected void doAddPermissionFields(
			long companyId, long groupId, String className, String classPK,
			Document doc)
		throws Exception {

		List<Role> roles = null;

		if (ResourceBlockLocalServiceUtil.isSupported(className)) {
			roles = ResourceBlockLocalServiceUtil.getRoles(
				className, Long.valueOf(classPK), ActionKeys.VIEW);
		}
		else {
			roles = ResourcePermissionLocalServiceUtil.getRoles(
				companyId, className, ResourceConstants.SCOPE_INDIVIDUAL,
				classPK, ActionKeys.VIEW);
		}

		if (roles.isEmpty()) {
			return;
		}

		List<Long> roleIds = new ArrayList<>();
		List<String> groupRoleIds = new ArrayList<>();

		Group group = GroupLocalServiceUtil.fetchGroup(groupId);

		for (Role role : roles) {
			if (role.getType() == RoleConstants.TYPE_REGULAR) {
				roleIds.add(role.getRoleId());
			}
			else if ((group != null) &&
					 (group.isOrganization() || group.isSite())) {

				groupRoleIds.add(groupId + StringPool.DASH + role.getRoleId());
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
			BooleanFilter booleanFilter)
		throws Exception {

		Indexer<?> indexer = IndexerRegistryUtil.nullSafeGetIndexer(className);

		if (!indexer.isPermissionAware()) {
			return booleanFilter;
		}

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (permissionChecker == null) {
			return booleanFilter;
		}

		if (permissionChecker.getUserId() != userId) {
			User user = UserLocalServiceUtil.getUser(userId);

			permissionChecker = PermissionCheckerFactoryUtil.create(user);
		}

		if (permissionChecker.isCompanyAdmin(companyId)) {
			return booleanFilter;
		}

		if (!(permissionChecker instanceof AdvancedPermissionChecker)) {
			return booleanFilter;
		}

		AdvancedPermissionChecker advancedPermissionChecker =
			(AdvancedPermissionChecker)permissionChecker;

		return doGetPermissionFilter(
			companyId, groupIds, advancedPermissionChecker, className,
			booleanFilter);
	}

	protected BooleanFilter doGetPermissionFilter(
			long companyId, long[] groupIds,
			AdvancedPermissionChecker advancedPermissionChecker,
			String className, BooleanFilter booleanFilter)
		throws Exception {

		Set<Group> groups = new LinkedHashSet<>();
		Set<Role> roles = new LinkedHashSet<>();

		populate(companyId, groupIds, advancedPermissionChecker, groups, roles);

		BooleanFilter permissionBooleanFilter = new BooleanFilter();

		permissionBooleanFilter.addTerm(
			Field.USER_ID, advancedPermissionChecker.getUserId());

		TermsFilter groupsTermsFilter = new TermsFilter(Field.GROUP_ID);
		TermsFilter groupRolesTermsFilter = new TermsFilter(
			Field.GROUP_ROLE_ID);
		TermsFilter rolesTermsFilter = new TermsFilter(Field.ROLE_ID);

		List<Role> regularRoles = new ArrayList<>();
		List<Role> groupRoles = new ArrayList<>();

		for (Role role : roles) {
			if (role.getType() == RoleConstants.TYPE_REGULAR) {
				regularRoles.add(role);
			}
			else {
				groupRoles.add(role);
			}

			rolesTermsFilter.addValue(String.valueOf(role.getRoleId()));
		}

		long[] regularRoleIds = ListUtil.toLongArray(
			regularRoles, Role.ROLE_ID_ACCESSOR);
		long[] groupRoleIds = ListUtil.toLongArray(
			groupRoles, Role.ROLE_ID_ACCESSOR);
		long[] roleIds = ArrayUtil.append(regularRoleIds, groupRoleIds);

		if (ResourcePermissionLocalServiceUtil.hasResourcePermission(
				companyId, className, ResourceConstants.SCOPE_COMPANY,
				String.valueOf(companyId), roleIds, ActionKeys.VIEW)) {

			return booleanFilter;
		}

		if (ResourcePermissionLocalServiceUtil.hasResourcePermission(
				companyId, className, ResourceConstants.SCOPE_GROUP_TEMPLATE,
				String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
				regularRoleIds, ActionKeys.VIEW)) {

			return booleanFilter;
		}

		for (Group group : groups) {
			if (advancedPermissionChecker.isGroupAdmin(
					group.getGroupId()) ||
				ResourcePermissionLocalServiceUtil.hasResourcePermission(
					companyId, className, ResourceConstants.SCOPE_GROUP,
					String.valueOf(group.getGroupId()), roleIds,
					ActionKeys.VIEW)) {

				groupsTermsFilter.addValue(String.valueOf(group.getGroupId()));
			}
			else if (ResourcePermissionLocalServiceUtil.hasResourcePermission(
						companyId, className,
						ResourceConstants.SCOPE_GROUP_TEMPLATE,
						String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
						groupRoleIds, ActionKeys.VIEW)) {

				groupsTermsFilter.addValue(String.valueOf(group.getGroupId()));
			}

			for (Role role : groupRoles) {
				groupRolesTermsFilter.addValue(
					group.getGroupId() + StringPool.DASH + role.getRoleId());
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

	protected void populate(
			long companyId, long[] groupIds,
			AdvancedPermissionChecker advancedPermissionChecker,
			Set<Group> groups, Set<Role> roles)
		throws Exception {

		UserPermissionCheckerBag userPermissionCheckerBag =
			advancedPermissionChecker.getUserBag();

		roles.addAll(userPermissionCheckerBag.getRoles());

		if (ArrayUtil.isEmpty(groupIds)) {
			groups.addAll(userPermissionCheckerBag.getUserGroups());
			groups.addAll(userPermissionCheckerBag.getUserOrgGroups());
		}
		else {
			Set<Long> groupIdsSet = SetUtil.fromArray(groupIds);

			for (Group group : userPermissionCheckerBag.getUserGroups()) {
				if (groupIdsSet.contains(group.getGroupId())) {
					groups.add(group);
				}
			}

			for (Group group : userPermissionCheckerBag.getUserOrgGroups()) {
				if (groupIdsSet.contains(group.getGroupId())) {
					groups.add(group);
				}
			}
		}

		for (Group group : groups) {
			PermissionCheckerBag userBag = advancedPermissionChecker.getUserBag(
				advancedPermissionChecker.getUserId(), group.getGroupId());

			roles.addAll(userBag.getRoles());
		}

		addRequiredRoles(companyId, roles);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SearchPermissionCheckerImpl.class);

}
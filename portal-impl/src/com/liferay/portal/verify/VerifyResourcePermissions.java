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

package com.liferay.portal.verify;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.concurrent.ThrowableAwareRunnable;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ContactLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourceLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.verify.model.VerifiableResourcedModel;
import com.liferay.portal.util.PortalInstances;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Raymond Augé
 * @author James Lefeu
 */
public class VerifyResourcePermissions extends VerifyProcess {

	public void verify(VerifiableResourcedModel... verifiableResourcedModels)
		throws Exception {

		long[] companyIds = PortalInstances.getCompanyIdsBySQL();

		for (long companyId : companyIds) {
			Role role = RoleLocalServiceUtil.getRole(
				companyId, RoleConstants.OWNER);

			List<VerifyResourcedModelRunnable> verifyResourcedModelRunnables =
				new ArrayList<>(verifiableResourcedModels.length);

			for (VerifiableResourcedModel verifiableResourcedModel :
					verifiableResourcedModels) {

				VerifyResourcedModelRunnable verifyResourcedModelRunnable =
					new VerifyResourcedModelRunnable(
						role, verifiableResourcedModel);

				verifyResourcedModelRunnables.add(verifyResourcedModelRunnable);
			}

			doVerify(verifyResourcedModelRunnables);

			verifyLayout(role);
		}
	}

	@Override
	protected void doVerify() throws Exception {
		Map<String, VerifiableResourcedModel> verifiableResourcedModelsMap =
			PortalBeanLocatorUtil.locate(VerifiableResourcedModel.class);

		Collection<VerifiableResourcedModel> verifiableResourcedModels =
			verifiableResourcedModelsMap.values();

		verify(
			verifiableResourcedModels.toArray(
				new VerifiableResourcedModel[
					verifiableResourcedModels.size()]));
	}

	protected void verifyLayout(Role role) throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			List<Layout> layouts =
				LayoutLocalServiceUtil.getNoPermissionLayouts(role.getRoleId());

			int total = layouts.size();

			for (int i = 0; i < total; i++) {
				Layout layout = layouts.get(i);

				verifyResourcedModel(
					role.getCompanyId(), Layout.class.getName(),
					layout.getPlid(), role, 0, i, total);
			}
		}
	}

	protected void verifyResourcedModel(
			long companyId, String modelName, long primKey, Role role,
			long ownerId, int cur, int total)
		throws Exception {

		if (_log.isInfoEnabled() && (((cur + 1) % 1000) == 0)) {
			cur++;

			_log.info(
				"Processed " + cur + " of " + total + " resource permissions " +
					"for company = " + companyId + " and model " + modelName);
		}

		ResourcePermission resourcePermission =
			ResourcePermissionLocalServiceUtil.fetchResourcePermission(
				companyId, modelName, ResourceConstants.SCOPE_INDIVIDUAL,
				String.valueOf(primKey), role.getRoleId());

		if (resourcePermission == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"No resource found for {" + companyId + ", " + modelName +
						", " + ResourceConstants.SCOPE_INDIVIDUAL + ", " +
							primKey + ", " + role.getRoleId() + "}");
			}

			ResourceLocalServiceUtil.addResources(
				companyId, 0, ownerId, modelName, String.valueOf(primKey),
				false, false, false);
		}

		if (resourcePermission == null) {
			resourcePermission =
				ResourcePermissionLocalServiceUtil.fetchResourcePermission(
					companyId, modelName, ResourceConstants.SCOPE_INDIVIDUAL,
					String.valueOf(primKey), role.getRoleId());

			if (resourcePermission == null) {
				return;
			}
		}

		if (modelName.equals(User.class.getName())) {
			User user = UserLocalServiceUtil.fetchUserById(ownerId);

			if (user != null) {
				Contact contact = ContactLocalServiceUtil.fetchContact(
					user.getContactId());

				if (contact != null) {
					ownerId = contact.getUserId();
				}
			}
		}

		if (ownerId != resourcePermission.getOwnerId()) {
			resourcePermission.setOwnerId(ownerId);

			ResourcePermissionLocalServiceUtil.updateResourcePermission(
				resourcePermission);
		}
	}

	protected void verifyResourcedModel(
			Role role, VerifiableResourcedModel verifiableResourcedModel)
		throws Exception {

		long companyId = role.getCompanyId();

		Map<Long, Long> ownerIds = null;

		String modelName = verifiableResourcedModel.getModelName();

		if (modelName.equals(User.class.getName())) {
			ownerIds = _getUserOwnerIds(companyId);
		}
		else {
			ownerIds = _getOwnerIds(companyId, verifiableResourcedModel);
		}

		StringBundler sb = new StringBundler(11);

		sb.append("select ");
		sb.append("count(*) ");
		sb.append("from ResourcePermission WHERE (companyId = ");
		sb.append(companyId);
		sb.append(") and (name = '");
		sb.append(modelName);
		sb.append("') and (scope = ");
		sb.append(ResourceConstants.SCOPE_INDIVIDUAL);
		sb.append(") and (roleId = ");
		sb.append(role.getRoleId());
		sb.append(StringPool.CLOSE_PARENTHESIS);

		long total = 0;

		try (Connection con = DataAccess.getUpgradeOptimizedConnection();
			PreparedStatement ps = con.prepareStatement(
				sb.toString(), ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				total = rs.getLong(1);
			}
		}

		sb.setStringAt("resourcePermissionId, primKeyId, ownerId ", 1);

		try (LoggingTimer loggingTimer = new LoggingTimer(
				verifiableResourcedModel.getModelName());
			Connection con = DataAccess.getUpgradeOptimizedConnection();
			PreparedStatement ps = con.prepareStatement(
				sb.toString(), ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = ps.executeQuery()) {

			for (int i = 1; rs.next(); i++) {
				if (_log.isInfoEnabled() && ((i % 1000) == 0)) {
					_log.info(
						"Processed " + i + " of " + total +
							" resource permissions for company = " + companyId +
								" and model " + modelName);
				}

				long primKeyId = rs.getLong("primKeyId");

				Long newOwnerId = ownerIds.remove(primKeyId);

				Long oldOwnerId = rs.getLong("ownerId");

				if (newOwnerId == null) {
					rs.deleteRow();
				}
				else if (!newOwnerId.equals(oldOwnerId)) {
					rs.updateLong("ownerId", newOwnerId);

					rs.updateRow();
				}
			}
		}

		total = ownerIds.size();

		Set<Map.Entry<Long, Long>> entries = ownerIds.entrySet();

		Iterator<Map.Entry<Long, Long>> entryIterator = entries.iterator();

		for (int i = 0; entryIterator.hasNext(); i++) {
			if (_log.isInfoEnabled() && ((i % 1000) == 0)) {
				_log.info(
					"Added " + i + " of " + total +
						" missing resource permissions for company = " +
						companyId + " and model " + modelName);
			}

			Map.Entry<Long, Long> entry = entryIterator.next();

			long ownerId = entry.getValue();
			String primKey = String.valueOf(entry.getKey());

			ResourceLocalServiceUtil.addResources(
				companyId, 0, ownerId, modelName, primKey, false, false, false);
		}
	}

	private Map<Long, Long> _getOwnerIds(
			long companyId, VerifiableResourcedModel verifiableResourcedModel)
		throws Exception {

		StringBundler sb = new StringBundler(8);

		sb.append("select ");
		sb.append(verifiableResourcedModel.getPrimaryKeyColumnName());
		sb.append(", ");
		sb.append(verifiableResourcedModel.getUserIdColumnName());
		sb.append(" from ");
		sb.append(verifiableResourcedModel.getTableName());
		sb.append(" where companyId = ");
		sb.append(companyId);

		Map<Long, Long> ownerIds = new HashMap<>();

		try (LoggingTimer loggingTimer = new LoggingTimer(
				verifiableResourcedModel.getModelName());
			Connection con = DataAccess.getUpgradeOptimizedConnection();
			PreparedStatement ps = con.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				long primKey = rs.getLong(
					verifiableResourcedModel.getPrimaryKeyColumnName());
				long userId = rs.getLong(
					verifiableResourcedModel.getUserIdColumnName());

				ownerIds.put(primKey, userId);
			}
		}

		return ownerIds;
	}

	private Map<Long, Long> _getUserOwnerIds(long companyId) throws Exception {
		StringBundler sb = new StringBundler(5);

		sb.append("select User_.userId as userUserId, Contact_.userId as ");
		sb.append("contactUserId from User_ left join Contact_ on ");
		sb.append("User_.contactId = Contact_.contactId where ");
		sb.append("User_.companyId = ");
		sb.append(companyId);

		Map<Long, Long> ownerIds = new HashMap<>();

		try (LoggingTimer loggingTimer = new LoggingTimer(User.class.getName());
			Connection con = DataAccess.getUpgradeOptimizedConnection();
			PreparedStatement ps = con.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				long userUserId = rs.getLong("userUserId");
				long contactUserId = rs.getLong("contactUserId");

				if (contactUserId > 0) {
					ownerIds.put(userUserId, contactUserId);
				}
				else {
					ownerIds.put(userUserId, userUserId);
				}
			}
		}

		return ownerIds;
	}

	private void _verifyResourcedModel(
			long companyId, String modelName, long primKey, Long oldOwnerId,
			Long newOwnerId, Role role, int cur, int total)
		throws Exception {

		if (_log.isInfoEnabled() && (((cur + 1) % 100) == 0)) {
			cur++;

			_log.info(
				"Processed " + cur + " of " + total + " resource permissions " +
					"for company = " + companyId + " and model " + modelName);
		}

		if (oldOwnerId == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"No resource found for {" + companyId + ", " + modelName +
						", " + ResourceConstants.SCOPE_INDIVIDUAL + ", " +
							primKey + ", " + role.getRoleId() + "}");
			}

			ResourceLocalServiceUtil.addResources(
				companyId, 0, newOwnerId, modelName, String.valueOf(primKey),
				false, false, false);

			return;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		VerifyResourcePermissions.class);

	private class VerifyResourcedModelRunnable extends ThrowableAwareRunnable {

		public VerifyResourcedModelRunnable(
			Role role, VerifiableResourcedModel verifiableResourcedModel) {

			_role = role;
			_verifiableResourcedModel = verifiableResourcedModel;
		}

		@Override
		protected void doRun() throws Exception {
			verifyResourcedModel(_role, _verifiableResourcedModel);
		}

		private final Role _role;
		private final VerifiableResourcedModel _verifiableResourcedModel;

	}

}
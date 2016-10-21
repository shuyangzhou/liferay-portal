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

import com.liferay.portal.dao.orm.common.SQLTransformer;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.concurrent.ThrowableAwareRunnable;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.kernel.verify.model.VerifiableUUIDModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 */
public class VerifyUUID extends VerifyProcess {

	public static void verify(VerifiableUUIDModel... verifiableUUIDModels)
		throws Exception {

		VerifyUUID verifyUUID = new VerifyUUID();

		verifyUUID.doVerify(verifiableUUIDModels);
	}

	@Override
	protected void doVerify() throws Exception {
		Map<String, VerifiableUUIDModel> verifiableUUIDModelsMap =
			PortalBeanLocatorUtil.locate(VerifiableUUIDModel.class);

		Collection<VerifiableUUIDModel> verifiableUUIDModels =
			verifiableUUIDModelsMap.values();

		doVerify(
			verifiableUUIDModels.toArray(
				new VerifiableUUIDModel[verifiableUUIDModels.size()]));
	}

	protected void doVerify(VerifiableUUIDModel... verifiableUUIDModels)
		throws Exception {

		List<VerifyUUIDRunnable> verifyUUIDRunnables = new ArrayList<>(
			verifiableUUIDModels.length);

		for (VerifiableUUIDModel verifiableUUIDModel : verifiableUUIDModels) {
			VerifyUUIDRunnable verifyUUIDRunnable = new VerifyUUIDRunnable(
				verifiableUUIDModel);

			verifyUUIDRunnables.add(verifyUUIDRunnable);
		}

		doVerify(verifyUUIDRunnables);
	}

	protected void verifyUUID(VerifiableUUIDModel verifiableUUIDModel)
		throws Exception {

		long maxPrimKeyValue = _getMaxPrimaryKeyValue(verifiableUUIDModel);

		StringBundler sb = new StringBundler(5);

		sb.append("update ");
		sb.append(verifiableUUIDModel.getTableName());
		sb.append(" set uuid_ = CONCAT(?, cast_text(");
		sb.append(verifiableUUIDModel.getPrimaryKeyColumnName());
		sb.append(")) where (uuid_ is null) and (");
		sb.append(verifiableUUIDModel.getPrimaryKeyColumnName());
		sb.append(" >= ?) and (");
		sb.append(verifiableUUIDModel.getPrimaryKeyColumnName());
		sb.append(" < ?)");

		String updateSQL = sb.toString();

		updateSQL = SQLTransformer.transform(updateSQL);

		try (LoggingTimer loggingTimer = new LoggingTimer(
				verifiableUUIDModel.getTableName());
			Connection con = DataAccess.getUpgradeOptimizedConnection();
			PreparedStatement ps = con.prepareStatement(updateSQL)) {

			List<String> pastPrefixes = new ArrayList<>();

			int primKeyLength = 1;
			long primKeyValue = 1;

			while (primKeyValue <= maxPrimKeyValue) {
				String prefix = _getNonConflictingPrefix(
					pastPrefixes, primKeyLength);

				ps.setString(1, prefix);

				ps.setLong(2, primKeyValue);
				ps.setLong(3, primKeyValue * 10);

				ps.executeUpdate();

				primKeyLength++;
				primKeyValue *= 10;
			}
		}
	}

	private long _getMaxPrimaryKeyValue(VerifiableUUIDModel verifiableUUIDModel)
		throws Exception {

		StringBundler sb = new StringBundler();

		sb.append("select max(");
		sb.append(verifiableUUIDModel.getPrimaryKeyColumnName());
		sb.append(") as maxPrimaryKeyValue from ");
		sb.append(verifiableUUIDModel.getTableName());

		try (Connection con = DataAccess.getUpgradeOptimizedConnection();
			PreparedStatement ps = con.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getLong("maxPrimaryKeyValue");
			}
		}

		return 0;
	}

	private String _getNonConflictingPrefix(
		List<String> pastPrefixes, int suffixLength) {

		String prefix = null;

		boolean unique = false;

		while (!unique) {
			String uuid = PortalUUIDUtil.generate();

			prefix = uuid.substring(0, uuid.length() - suffixLength);

			unique = true;

			for (String pastPrefix : pastPrefixes) {
				if (pastPrefix.length() > prefix.length()) {
					unique &= !pastPrefix.startsWith(prefix);
				}
				else {
					unique &= !prefix.startsWith(pastPrefix);
				}
			}
		}

		pastPrefixes.add(prefix);

		return prefix;
	}

	private class VerifyUUIDRunnable extends ThrowableAwareRunnable {

		public VerifyUUIDRunnable(VerifiableUUIDModel verifiableUUIDModel) {
			_verifiableUUIDModel = verifiableUUIDModel;
		}

		@Override
		protected void doRun() throws Exception {
			verifyUUID(_verifiableUUIDModel);
		}

		private final VerifiableUUIDModel _verifiableUUIDModel;

	}

}
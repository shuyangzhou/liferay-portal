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

package com.liferay.dynamic.data.mapping.internal.upgrade.v1_1_3;

import com.liferay.dynamic.data.mapping.internal.upgrade.v1_1_3.util.DDMStorageLinkTable;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.upgrade.AutoBatchPreparedStatementUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Pedro Queiroz
 */
public class UpgradeDDMStorageLink extends UpgradeProcess {

	public long getLatestStructureVersionId(long ddmStructureId)
		throws Exception {

		try (PreparedStatement ps = connection.prepareStatement(
				"select structureVersionId from DDMStructureVersion where " +
					"structureId = ? order by createDate desc")) {

			ps.setLong(1, ddmStructureId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("structureVersionId");
				}

				return 0;
			}
		}
	}

	@Override
	protected void doUpgrade() throws Exception {
		if (!hasColumn("DDMStorageLink", "structureVersionId")) {
			alter(
				DDMStorageLinkTable.class,
				new AlterTableAddColumn("structureVersionId LONG"));
		}

		try (PreparedStatement ps1 = connection.prepareStatement(
				"select structureId from DDMStorageLink");
			PreparedStatement ps2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update DDMStorageLink set structureVersionId = ? where " +
						"structureId = ?");
			ResultSet rs = ps1.executeQuery()) {

			while (rs.next()) {
				long ddmStructureId = rs.getLong("structureId");

				long ddmStructureVersionId = getLatestStructureVersionId(
					ddmStructureId);

				if (ddmStructureVersionId > 0) {
					ps2.setLong(1, ddmStructureVersionId);
					ps2.setLong(2, ddmStructureId);

					ps2.addBatch();
				}
			}

			ps2.executeBatch();
		}
	}

}
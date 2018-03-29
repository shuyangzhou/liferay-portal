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

package com.liferay.portal.upgrade.v6_1_1;

import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.test.rule.SpringInitializationTestRule;
import com.liferay.portal.upgrade.UpgradeSchemaBase;
import com.liferay.portal.upgrade.UpgradeTestHelper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Tom Wang
 */
public class UpgradeSchemaTest extends UpgradeSchemaBase {

	@ClassRule
	@Rule
	public static final SpringInitializationTestRule
		SPRING_INITIALIZATION_TEST_RULE = new SpringInitializationTestRule(
		PropsKeys.SPRING_INFRASTRUCTURE_CONFIGS);

	@BeforeClass
	public static void setUpClass() throws Exception {
		initDB();

		_db = DBManagerUtil.getDB();

		DBType dbType = _db.getDBType();

		_db.runSQLTemplate("v6_1_1/portal-tables-" + dbType.getName() + ".sql");

		_db.runSQLTemplate("v6_1_1/indexes-" + dbType.getName() + ".sql");
	}

	@Test
	public void testUpgradeSchema() throws Exception {
		UpgradeTestHelper upgradeTestHelper = new UpgradeTestHelper(
			DataAccess.getConnection());

		Assert.assertEquals(false, upgradeTestHelper.hasColumn(
			"DLFileVersion", "uuid_"));
		Assert.assertEquals(false, upgradeTestHelper.hasColumn(
			"DLSync", "description"));
		Assert.assertEquals(false, upgradeTestHelper.hasColumn(
			"LayoutSetBranch", "logo"));

		UpgradeSchema upgradeSchema = new UpgradeSchema();

		upgradeSchema.upgrade();

		Assert.assertEquals(true, upgradeTestHelper.hasColumn(
			"DLFileVersion", "uuid_"));
		Assert.assertEquals(true, upgradeTestHelper.hasColumn(
			"DLSync", "description"));
		Assert.assertEquals(true, upgradeTestHelper.hasColumn(
			"LayoutSetBranch", "logo"));
	}

}
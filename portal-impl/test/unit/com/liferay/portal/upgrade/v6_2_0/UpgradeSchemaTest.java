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

package com.liferay.portal.upgrade.v6_2_0;

import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.upgrade.UpgradeTestUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Tom Wang
 */
public class UpgradeSchemaTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule
		LIFERAY_INTEGRATION_TEST_RULE =
			LiferayIntegrationTestRule.UPGRADE_TESTRULES_INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		UpgradeTestUtil.populateDB(UpgradeSchemaTest.class);
	}

	@Test
	public void testUpgradeSchema() throws Exception {
		Assert.assertEquals(false, UpgradeTestUtil.hasColumn(
			"Address", "uuid_"));
		Assert.assertEquals(false, UpgradeTestUtil.hasColumn(
			"BookmarksEntry", "treePath"));
		Assert.assertEquals(false, UpgradeTestUtil.hasColumn(
			"BookmarksFolder", "treePath"));
		Assert.assertEquals(
			false, UpgradeTestUtil.hasTable("BackgroundTask"));
		Assert.assertEquals(
			true, UpgradeTestUtil.hasColumn("MBMessage", "attachments"));
		Assert.assertEquals(
			false, UpgradeTestUtil.hasTable("SocialActivitySet"));

		Assert.assertEquals(true, UpgradeTestUtil.hasColumn("DLFileEntry", "versionUserId"));
		Assert.assertEquals(false, UpgradeTestUtil.hasColumn("DLFileEntry", "classNameId"));
		Assert.assertEquals(false, UpgradeTestUtil.hasColumn("DLFileRank", "active_"));
		Assert.assertEquals(false, UpgradeTestUtil.hasColumn("DLFileShortcut", "treePath"));
		Assert.assertEquals(false, UpgradeTestUtil.hasColumn("DLFileVersion", "checksum"));
		Assert.assertEquals(false, UpgradeTestUtil.hasColumn("DLFolder", "hidden_"));
		Assert.assertEquals(true, UpgradeTestUtil.hasTable("DLSync"));
		Assert.assertEquals(false, UpgradeTestUtil.hasTable("DLSyncEvent"));

		Assert.assertEquals(false, UpgradeTestUtil.hasColumn("ExpandoRow", "modifiedDate"));

		Assert.assertEquals(false, UpgradeTestUtil.hasColumn("Group_", "uuid_"));

		Assert.assertEquals(false, UpgradeTestUtil.hasColumn("JournalArticle", "folderId"));
		Assert.assertEquals(false, UpgradeTestUtil.hasTable("JournalFolder"));

		Assert.assertEquals(false, UpgradeTestUtil.hasColumn("User_", "ldapServerId"));

		Assert.assertEquals(false, UpgradeTestUtil.hasColumn("WikiNode", "status"));

		UpgradeSchema upgradeSchema = new UpgradeSchema();

		upgradeSchema.upgrade();

		Assert.assertEquals(true, UpgradeTestUtil.hasColumn(
			"Address", "uuid_"));
		Assert.assertEquals(true, UpgradeTestUtil.hasColumn(
			"BookmarksEntry", "treePath"));
		Assert.assertEquals(true, UpgradeTestUtil.hasColumn(
			"BookmarksFolder", "treePath"));
		Assert.assertEquals(
			true, UpgradeTestUtil.hasTable("BackgroundTask"));
		Assert.assertEquals(
			false, UpgradeTestUtil.hasColumn("MBMessage", "attachments"));
		Assert.assertEquals(
			true, UpgradeTestUtil.hasTable("SocialActivitySet"));

		Assert.assertEquals(false, UpgradeTestUtil.hasColumn("DLFileEntry", "versionUserId"));
		Assert.assertEquals(true, UpgradeTestUtil.hasColumn("DLFileEntry", "classNameId"));
		Assert.assertEquals(true, UpgradeTestUtil.hasColumn("DLFileRank", "active_"));
		Assert.assertEquals(true, UpgradeTestUtil.hasColumn("DLFileShortcut", "treePath"));
		Assert.assertEquals(true, UpgradeTestUtil.hasColumn("DLFileVersion", "checksum"));
		Assert.assertEquals(true, UpgradeTestUtil.hasColumn("DLFolder", "hidden_"));
		Assert.assertEquals(false, UpgradeTestUtil.hasTable("DLSync"));
		Assert.assertEquals(true, UpgradeTestUtil.hasTable("DLSyncEvent"));

		Assert.assertEquals(true, UpgradeTestUtil.hasColumn("ExpandoRow", "modifiedDate"));

		Assert.assertEquals(true, UpgradeTestUtil.hasColumn("Group_", "uuid_"));

		Assert.assertEquals(true, UpgradeTestUtil.hasColumn("JournalArticle", "folderId"));
		Assert.assertEquals(true, UpgradeTestUtil.hasTable("JournalFolder"));

		Assert.assertEquals(true, UpgradeTestUtil.hasColumn("User_", "ldapServerId"));

		Assert.assertEquals(true, UpgradeTestUtil.hasColumn("WikiNode", "status"));
	}

}
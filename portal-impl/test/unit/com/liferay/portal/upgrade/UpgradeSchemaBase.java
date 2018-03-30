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

package com.liferay.portal.upgrade;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.UnsecureSAXReaderUtil;
import com.liferay.portal.test.rule.SpringInitializationTestRule;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.xml.SAXReaderImpl;
import jodd.io.FileUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;

import java.io.File;

/**
 * @author Tom Wang
 */
public class UpgradeSchemaBase {

	public static DB initDB() throws Exception {
		DB db = DBManagerUtil.getDB();

		DBType dbType = db.getDBType();

		if (dbType == DBType.HYPERSONIC) {
			File file = new File(
				PropsUtil.get("liferay.home") + "/data/hypersonic");

			if (file.isDirectory()) {
				FileUtil.deleteDir(file);
			}
		}
		else {
			db.runSQLTemplate("init-" + dbType.getName() + ".sql");
		}

		_setUpSAXReaderUtil();

		_db = db;

		return db;
	}

	private static void _setUpSAXReaderUtil() {
		SAXReaderUtil saxReaderUtil = new SAXReaderUtil();

		SAXReaderImpl secureSAXReader = new SAXReaderImpl();

		secureSAXReader.setSecure(true);

		saxReaderUtil.setSAXReader(secureSAXReader);

		UnsecureSAXReaderUtil unsecureSAXReaderUtil =
			new UnsecureSAXReaderUtil();

		SAXReaderImpl unsecureSAXReader = new SAXReaderImpl();

		unsecureSAXReaderUtil.setSAXReader(unsecureSAXReader);
	}

	protected static DB _db;
}
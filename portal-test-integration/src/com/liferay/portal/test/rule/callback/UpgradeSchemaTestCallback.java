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

package com.liferay.portal.test.rule.callback;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.test.rule.callback.BaseTestCallback;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.UnsecureSAXReaderUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.xml.SAXReaderImpl;
import jodd.io.FileUtil;
import org.junit.runner.Description;

import java.io.File;
import java.io.InputStream;

/**
 * @author Tom Wang
 */

public class UpgradeSchemaTestCallback extends BaseTestCallback<Void, Void> {

	public static final UpgradeSchemaTestCallback INSTANCE =
		new UpgradeSchemaTestCallback();

	private UpgradeSchemaTestCallback() {
	}

	@Override
	public Void beforeClass(Description description) throws Exception {
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
			InputStream is = UpgradeSchemaTestCallback.class.getResourceAsStream(
				"dependencies/init-mysql.sql");

			db.runSQLTemplateString(StringUtil.read(is), false, false);
		}

		SAXReaderUtil saxReaderUtil = new SAXReaderUtil();

		SAXReaderImpl secureSAXReader = new SAXReaderImpl();

		secureSAXReader.setSecure(true);

		saxReaderUtil.setSAXReader(secureSAXReader);

		UnsecureSAXReaderUtil unsecureSAXReaderUtil =
			new UnsecureSAXReaderUtil();

		unsecureSAXReaderUtil.setSAXReader(new SAXReaderImpl());

		return null;
	}

}

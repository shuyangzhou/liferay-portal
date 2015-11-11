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

package com.liferay.portal.kernel.test.rule.callback;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;

import java.sql.Connection;
import java.sql.Statement;

import org.junit.runner.Description;

/**
 * @author Shuyang Zhou
 */
public class SybaseDumpTransactionLogTestCallback
	extends BaseTestCallback<Void, Void> {

	public static final SybaseDumpTransactionLogTestCallback INSTANCE =
		new SybaseDumpTransactionLogTestCallback();

	@Override
	public Void beforeClass(Description description) throws Throwable {
		try (Connection connection = DataAccess.getConnection();
			Statement statement = connection.createStatement()) {

			statement.addBatch("use master");
			statement.addBatch("dump transaction master with no_log");

			statement.executeBatch();
		}

		return null;
	}

	private SybaseDumpTransactionLogTestCallback() {
	}

}
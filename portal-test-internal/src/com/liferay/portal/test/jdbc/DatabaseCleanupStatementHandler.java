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

package com.liferay.portal.test.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.sql.Connection;
import java.sql.Statement;

/**
 * @author Shuyang Zhou
 */
public class DatabaseCleanupStatementHandler implements InvocationHandler {

	public DatabaseCleanupStatementHandler(
		Connection connection, Statement statement) {

		_connection = connection;
		_statement = statement;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] arguments)
		throws Throwable {

		try {
			String methodName = method.getName();

			if (methodName.equals("equals")) {
				if (proxy == arguments[0]) {
					return true;
				}
				else {
					return false;
				}
			}

			if (methodName.equals("hashCode")) {
				return System.identityHashCode(proxy);
			}

			boolean processAfterInvoke = false;

			if (methodName.equals("executeBatch") ||
				methodName.equals("execute") ||
				methodName.equals("executeQuery") ||
				methodName.equals("executeUpdate")) {

				processAfterInvoke =
					DatabaseCleanupTestRule.processBeforeInvoke(
						_connection, _statement);
			}

			Object returnValue = method.invoke(_statement, arguments);

			if (processAfterInvoke) {
				DatabaseCleanupTestRule.processAfterInvoke(_connection,
					_statement);
			}

			return returnValue;
		}
		catch (InvocationTargetException ite) {
			throw ite.getTargetException();
		}
	}

	private final Connection _connection;
	private final Statement _statement;

}
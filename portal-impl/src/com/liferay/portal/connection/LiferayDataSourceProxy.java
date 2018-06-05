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

package com.liferay.portal.connection;

import com.liferay.portal.kernel.util.ProxyUtil;

import java.lang.reflect.InvocationTargetException;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.jdbc.datasource.ConnectionProxy;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

/**
 * @author Tom Wang
 */
public class LiferayDataSourceProxy extends LazyConnectionDataSourceProxy {

	public static int getAndResetOpenConnectionCount() {
		AtomicInteger atomicInteger = _openConnectionCountThreadLocal.get();

		return atomicInteger.getAndSet(0);
	}

	public static void setTestThreadThreadLocal(boolean value) {
		_testThreadThreadLocal.set(value);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return getConnection(null, null);
	}

	@Override
	public Connection getConnection(String username, String password)
		throws SQLException {

		Connection connection;

		if ((username != null) && (password != null)) {
			connection = super.getConnection(username, password);
		}
		else {
			connection = super.getConnection();
		}

		if (!_testThreadThreadLocal.get()) {
			return connection;
		}

		AtomicInteger atomicInteger = _openConnectionCountThreadLocal.get();

		atomicInteger.incrementAndGet();

		return (Connection)ProxyUtil.newProxyInstance(
			ConnectionProxy.class.getClassLoader(), _INTERFACES,
			(proxy, method, args) -> {
				if ("close".equals(method.getName())) {
					atomicInteger.decrementAndGet();
				}

				try {
					return method.invoke(connection, args);
				}
				catch (InvocationTargetException ite) {
					throw ite.getTargetException();
				}
			});
	}

	private static final Class<?>[] _INTERFACES =
		new Class<?>[] {ConnectionProxy.class};

	private static final ThreadLocal<AtomicInteger>
		_openConnectionCountThreadLocal = ThreadLocal.withInitial(
			() -> new AtomicInteger(0));
	private static final ThreadLocal<Boolean> _testThreadThreadLocal =
		ThreadLocal.withInitial(() -> Boolean.FALSE);

}
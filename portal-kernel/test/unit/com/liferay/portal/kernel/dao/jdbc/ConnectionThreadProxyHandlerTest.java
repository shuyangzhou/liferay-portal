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

package com.liferay.portal.kernel.dao.jdbc;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Luis Ortiz
 */
@PrepareForTest(DataAccess.class)
@RunWith(PowerMockRunner.class)
public class ConnectionThreadProxyHandlerTest {

	@Before
	public void setUp() throws Exception {
		_setUpDataAccess();
		_setUpConnectionMock();
	}

	@Test
	public void testOneThread() throws SQLException {
		Connection connection1 = _checkNewProxyInstance(0, true);

		Connection connection2 = _checkNewProxyInstance(1, false);

		_checkConnectionClose(connection2);

		_checkConnectionClose(connection1);
	}

	@Test
	public void testSeveralThreads() throws InterruptedException, SQLException {
		Connection connection1 = _checkNewProxyInstance(0, true);

		Thread thread = new Thread(
			() -> {
				try {
					Connection connection2 = _checkNewProxyInstance(1, true);

					_checkConnectionClose(connection2);
				}
				catch (SQLException sqlException) {
					throw new RuntimeException(sqlException);
				}
			});

		thread.start();
		thread.join();

		_checkConnectionClose(connection1);
	}

	private void _checkConnectionClose(Connection connection)
		throws SQLException {

		List<Long> currentThreads = _getCurrentThreadsList(connection);

		int threadConnections = currentThreads.size();

		Map<Long, Connection> connectionMap = _getConnectionMap(connection);

		int totalConnections = connectionMap.size();

		_closeConnectionCalls = 0;
		connection.close();
		Assert.assertEquals(_closeConnectionCalls, threadConnections);

		Assert.assertEquals(
			currentThreads.toString(), 0, currentThreads.size());

		Assert.assertEquals(
			connectionMap.size(), totalConnections - threadConnections);
	}

	private Connection _checkNewProxyInstance(
			int existingConnections, boolean newThread)
		throws SQLException {

		Connection connection = (Connection)ProxyUtil.newProxyInstance(
			ClassLoader.getSystemClassLoader(),
			new Class<?>[] {Connection.class},
			new ConnectionThreadProxyHandler());

		Map<Long, Connection> connectionMap = _getConnectionMap(connection);

		Assert.assertEquals(connectionMap.size(), existingConnections);

		List<Long> currentThreads = _getCurrentThreadsList(connection);

		Assert.assertEquals(
			currentThreads.toString(), 0, currentThreads.size());

		connection.createStatement();

		PowerMockito.verifyStatic(
			Mockito.times(existingConnections + (newThread ? 1 : 0)));
		DataAccess.getConnection();

		Assert.assertEquals(
			connectionMap.size(), existingConnections + (newThread ? 1 : 0));

		Assert.assertEquals(currentThreads.size(), newThread ? 1 : 0);

		return connection;
	}

	private Map<Long, Connection> _getConnectionMap(Connection connection) {
		return ReflectionTestUtil.getFieldValue(
			ProxyUtil.getInvocationHandler(connection), "_connectionMap");
	}

	private List<Long> _getCurrentThreadsList(Connection connection) {
		return ReflectionTestUtil.getFieldValue(
			ProxyUtil.getInvocationHandler(connection), "_currentThreads");
	}

	private void _setUpConnectionMock() throws Exception {
		Mockito.doAnswer(
			invocation -> {
				_closeConnectionCalls++;

				return null;
			}
		).when(
			_connection
		).close();
	}

	private void _setUpDataAccess() throws Exception {
		PowerMockito.mockStatic(DataAccess.class);
		PowerMockito.when(
			DataAccess.getConnection()
		).thenReturn(
			_connection
		);
	}

	private long _closeConnectionCalls;

	@Mock
	private Connection _connection;

}
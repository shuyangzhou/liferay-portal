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

package com.liferay.portal.dao.jdbc.spring;

import com.liferay.portal.kernel.dao.jdbc.CurrentConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/**
 * @author Matthew Tambara
 */
public class JdbcTemplate extends org.springframework.jdbc.core.JdbcTemplate {

	@Override
	public <T> T execute(
			PreparedStatementCreator preparedStatementCreator,
			PreparedStatementCallback<T> preparedStatementCallback)
		throws DataAccessException {

		if (preparedStatementCreator == null) {
			throw new IllegalArgumentException(
				"PreparedStatementCreator is null");
		}

		if (preparedStatementCallback == null) {
			throw new IllegalArgumentException(
				"PreparedStatementCallback is null");
		}

		Connection connection = CurrentConnectionUtil.getConnection(
			getDataSource());

		Boolean newConnection = false;

		if (connection == null) {
			DataSource dataSource = getDataSource();

			try {
				connection = dataSource.getConnection();

				newConnection = true;
			}
			catch (SQLException sqle) {
				throw new CannotGetJdbcConnectionException(
					"Could not get JDBC Connection", sqle);
			}
		}

		PreparedStatement ps = null;

		try {
			NativeJdbcExtractor nativeJdbcExtractor = getNativeJdbcExtractor();

			if ((nativeJdbcExtractor != null) &&
				nativeJdbcExtractor.
					isNativeConnectionNecessaryForNativePreparedStatements()) {

				connection = nativeJdbcExtractor.getNativeConnection(
					connection);
			}

			ps = preparedStatementCreator.createPreparedStatement(connection);

			applyStatementSettings(ps);

			if (nativeJdbcExtractor != null) {
				ps = nativeJdbcExtractor.getNativePreparedStatement(ps);
			}

			T result = preparedStatementCallback.doInPreparedStatement(ps);

			handleWarnings(ps);

			return result;
		}
		catch (SQLException sqle) {
			if (preparedStatementCreator instanceof ParameterDisposer) {
				((ParameterDisposer)preparedStatementCreator).
					cleanupParameters();
			}

			String sql = null;

			if (preparedStatementCreator instanceof SqlProvider) {
				sql = ((SqlProvider)preparedStatementCreator).getSql();
			}

			SQLExceptionTranslator sqlExceptionTranslator =
				getExceptionTranslator();

			throw sqlExceptionTranslator.translate(
				"PreparedStatementCallback", sql, sqle);
		}
		finally {
			if (preparedStatementCreator instanceof ParameterDisposer) {
				((ParameterDisposer)preparedStatementCreator).
					cleanupParameters();
			}

			JdbcUtils.closeStatement(ps);

			if (newConnection) {
				try {
					connection.close();
				}
				catch (SQLException sqle) {
				}
			}
		}
	}

}
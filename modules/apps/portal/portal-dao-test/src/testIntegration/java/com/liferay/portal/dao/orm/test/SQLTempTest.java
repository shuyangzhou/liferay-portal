/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.dao.orm.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.model.ReleaseConstants;
import com.liferay.portal.kernel.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Shuyang Zhou
 */
@RunWith(Arquillian.class)
public class SQLTempTest {

	@BeforeClass
	public static void setUpClass() {
		System.out.println(
			"####Running on " + DBManagerUtil.getDB().getDBType());
	}

	@Test
	public void testProjectOnCompare() throws SQLException {
		try (Connection connection = DataAccess.getConnection();
			PreparedStatement preparedStatement =
				connection.prepareStatement(
					StringBundler.concat(
						"select servletContextName, (testString = ",
						"?) as caseSensitive from Release_ where ",
						"releaseId = 1"))) {

			preparedStatement.setString(1, ReleaseConstants.TEST_STRING);

			try (ResultSet resultSet =
					preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					System.out.println(
						"####testProjectOnCompare exact case : " +
							resultSet.getBoolean("caseSensitive"));
				}
			}
			
			preparedStatement.setString(
				1, StringUtil.toUpperCase(ReleaseConstants.TEST_STRING));

			try (ResultSet resultSet =
					preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					System.out.println(
						"####testProjectOnCompare upper case : " +
							resultSet.getBoolean("caseSensitive"));
				}
			}
		}
	}

	@Test
	public void testProjectOnSubquery() throws SQLException {
		try (Connection connection = DataAccess.getConnection();
			PreparedStatement preparedStatement =
				connection.prepareStatement(
					StringBundler.concat(
						"select servletContextName, (select count(*) from ",
						"Release_ where releaseId = 1 and testString = ?) as ",
						"matchCount from Release_ where releaseId = 1"))) {

			preparedStatement.setString(1, ReleaseConstants.TEST_STRING);

			try (ResultSet resultSet =
					preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					System.out.println(
						"####testProjectOnSubquery exact case : " +
							resultSet.getInt("matchCount"));
				}
			}

			preparedStatement.setString(
				1, StringUtil.toUpperCase(ReleaseConstants.TEST_STRING));

			try (ResultSet resultSet =
					preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					System.out.println(
						"####testProjectOnSubquery upper case : " +
							resultSet.getInt("matchCount"));
				}
			}
		}
	}

	@Test
	public void testUnionALL() throws SQLException {
		try (Connection connection = DataAccess.getConnection();
			PreparedStatement preparedStatement =
				connection.prepareStatement(
					StringBundler.concat(
						"select servletContextName from Release_ where ",
						"releaseId = 1 UNION ALL select servletContextName ",
						"from Release_ where releaseId = 1 and testString = ",
						"?"))) {

			preparedStatement.setString(
				1, StringUtil.toUpperCase(ReleaseConstants.TEST_STRING));

			try (ResultSet resultSet =
					preparedStatement.executeQuery()) {

				int count = 0;

				while (resultSet.next()) {
					count++;
				}

				System.out.println("####testUnionALL : " + count);
			}
		}
	}

}
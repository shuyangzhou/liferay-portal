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

package com.liferay.portal.dao.orm;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.TransactionalTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class SQLDateTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE,
			TransactionalTestRule.INSTANCE);

	@Test
	public void testMillisecondsProcessing() throws SQLException {
		DB db = DBFactoryUtil.getDB();

		String dbType = db.getType();

		System.out.println("Running on " + dbType);

		long time = _readTime() / 1000 * 1000;

		if (dbType.equals(DB.TYPE_SYBASE)) {
			for (int i = 0; i < 1000; i++) {
				_writeTime(time);

				long readTime = _readTime();

				if (time++ != readTime) {
					System.out.println(
						"#####For sybase write out : " + time + ", read in : " +
							readTime);
				}
			}
		}
		else {
			for (int i = 0; i < 1000; i++) {
				_writeTime(time);

				Assert.assertEquals(time++, _readTime());
			}
		}
	}

	private long _readTime() throws SQLException {
		try (Connection con = DataAccess.getConnection();
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(
				_READ_RELEASE_MODIFIED_DATE)) {

			Assert.assertTrue(rs.next());

			Timestamp timestamp = rs.getTimestamp("modifiedDate");

			Assert.assertFalse(rs.next());

			return timestamp.getTime();
		}
	}

	private void _writeTime(long time) throws SQLException {
		try (Connection con = DataAccess.getConnection();
			PreparedStatement ps = con.prepareStatement(
				_WRITE_RELEASE_MODIFIED_DATE)) {

			ps.setTimestamp(1, new Timestamp(time));

			Assert.assertEquals(1, ps.executeUpdate());
		}
	}

	private static final String _READ_RELEASE_MODIFIED_DATE =
		"select modifiedDate from Release_ where releaseId = 1";

	private static final String _WRITE_RELEASE_MODIFIED_DATE =
		"update Release_ set modifiedDate=? where releaseId = 1";

}
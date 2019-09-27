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

package com.liferay.portlet.preferences.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.test.log.CaptureAppender;
import com.liferay.portal.test.log.Log4JLoggerTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.sql.SQLException;

import org.apache.log4j.Level;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Matthew Tambara
 */
@RunWith(Arquillian.class)
public class SQLStateTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testGroup() throws Exception {
		_group = _groupLocalService.createGroup(RandomTestUtil.nextLong());

		Group group2 = _groupLocalService.createGroup(
			RandomTestUtil.nextLong());

		_groupLocalService.addGroup(_group);

		group2.setPrimaryKey(_group.getPrimaryKey());

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					"org.hibernate.util.JDBCExceptionReporter", Level.OFF)) {

			_groupLocalService.addGroup(group2);

			Assert.fail();
		}
		catch (Exception e) {
			SQLException sqle = _getSQLException(e);

			Assert.assertNotNull(e.toString(), sqle);

			String sqlState = sqle.getSQLState();

			Assert.assertTrue(sqlState, sqlState.startsWith("23"));
		}
	}

	private SQLException _getSQLException(Throwable t) {
		while (true) {
			Class<?> clazz = t.getClass();

			ClassLoader classLoader = clazz.getClassLoader();

			if (classLoader == null) {
				classLoader = ClassLoader.getSystemClassLoader();
			}

			try {
				Class<?> exceptionClass = classLoader.loadClass(
					SQLException.class.getName());

				if (exceptionClass.isInstance(t)) {
					return (SQLException)t;
				}
			}
			catch (ClassNotFoundException cnfe) {
			}

			Throwable cause = t.getCause();

			if ((t == cause) || (cause == null)) {
				break;
			}

			t = cause;
		}

		return null;
	}

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private GroupLocalService _groupLocalService;

}
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

package com.liferay.portal.security.pacl.checker;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.security.pacl.PACLPolicy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.util.Properties;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Raymond Augé
 */
public class SQLCheckerTest {

	@Test
	public void testHasSQL() throws Exception {
		SQLChecker sqlChecker = _getSQLChecker();

		// Test Create

		Assert.assertFalse(
			sqlChecker.hasSQL(
				"create table TestPACL_CreateFailure (userId bigint)"));

		Assert.assertTrue(
			sqlChecker.hasSQL(
				"create table TestPACL_CreateSuccess (userId bigint)"));

		// Test Drop

		Assert.assertFalse(
			sqlChecker.hasSQL("drop table TestPACL_DropFailure"));

		Assert.assertTrue(
			sqlChecker.hasSQL("drop table TestPACL_CreateSuccess"));

		// Test Create Index

		Assert.assertFalse(
			sqlChecker.hasSQL(
				"create index index1 ON TestPACL_CreateFailure (userId)"));

		Assert.assertTrue(
			sqlChecker.hasSQL(
				"create index index1 ON TestPACL_CreateSuccess (userId)"));

		// Test Insert

		Assert.assertFalse(
			sqlChecker.hasSQL("insert into TestPACL_InsertFailure values (1)"));

		Assert.assertTrue(
			sqlChecker.hasSQL("insert into TestPACL_InsertSuccess values (1)"));

		// Test Replace

		Assert.assertFalse(
			sqlChecker.hasSQL(
				"replace TestPACL_ReplaceFailure (userId) values (1)"));

		Assert.assertTrue(
			sqlChecker.hasSQL(
				"replace TestPACL_ReplaceSuccess (userId) values (1)"));

		// Test Select

		Assert.assertFalse(
			sqlChecker.hasSQL(
				"select * from Counter inner join User_ on User_.userId = " +
					"Counter.currentId"));

		Assert.assertTrue(sqlChecker.hasSQL("select * from Counter"));

		Assert.assertTrue(sqlChecker.hasSQL("select * from TestPACL_Bar"));

		Assert.assertFalse(sqlChecker.hasSQL("select * from User_"));

		// Test Truncate

		Assert.assertFalse(
			sqlChecker.hasSQL("truncate table TestPACL_TruncateFailure"));

		Assert.assertTrue(
			sqlChecker.hasSQL("truncate table TestPACL_TruncateSuccess"));

		// Test Update

		Assert.assertTrue(
			sqlChecker.hasSQL(
				"update ListType set name = 'Test PACL' where listTypeId = " +
					"-123"));

		Assert.assertFalse(
			sqlChecker.hasSQL(
				"update ListType set name = 'Test PACL' where listTypeId = " +
					"(select userId from User_)"));

		Assert.assertFalse(
			sqlChecker.hasSQL(
				"update User_ set firstName = 'Test PACL' where userId = " +
					"-123"));
	}

	private SQLChecker _getSQLChecker() throws Exception {
		final Properties properties = new Properties();

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		properties.load(
			classLoader.getResourceAsStream(
				"com/liferay/portal/security/pacl/checker/dependencies" +
					"/pacl-sql-checker.properties"));

		SQLChecker sqlChecker = new SQLChecker();

		sqlChecker.setPACLPolicy(
			(PACLPolicy)ProxyUtil.newProxyInstance(
				PACLPolicy.class.getClassLoader(),
				new Class<?>[] {PACLPolicy.class},
				new InvocationHandler() {

					@Override
					public Object invoke(
							Object proxy, Method method, Object[] args)
						throws Throwable {

						String methodName = method.getName();

						if (methodName.equals("getProperties")) {
							return properties;
						}
						else if (methodName.equals("getProperty")) {
							return properties.getProperty((String)args[0]);
						}
						else if (methodName.equals("getPropertyArray")) {
							return StringUtil.split(
								properties.getProperty((String)args[0]));
						}
						else if (methodName.equals("getPropertyBoolean")) {
							return GetterUtil.getBoolean(
								properties.getProperty((String)args[0]));
						}
						else if (methodName.equals("getPropertySet")) {
							return new TreeSet<>(
								SetUtil.fromArray(
									StringUtil.split(
										properties.getProperty(
											(String)args[0]))));
						}

						return method.invoke(proxy, args);
					}

				}));

		sqlChecker.afterPropertiesSet();

		return sqlChecker;
	}

}
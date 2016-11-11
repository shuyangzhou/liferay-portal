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

package com.liferay.aspectj.hibernate.unexpected.row.count;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.sql.PreparedStatement;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.SuppressAjWarnings;

/**
 * @author Preston Crary
 */
@Aspect
@SuppressAjWarnings("adviceDidNotMatch")
public class HibernateUnexpectedRowCountAspect {

	@AfterThrowing(
		throwing = "exception",
		value = "execution(void org.hibernate.jdbc.BatchingBatcher.doExecuteBatch(java.sql.PreparedStatement)) && args(preparedStatement) && this(batchingBatcher)"
	)
	public void logUpdateSQL(
			Object batchingBatcher, PreparedStatement preparedStatement,
			Exception exception)
		throws Exception {

		Class<?> clazz = exception.getClass();

		if (_STALE_STATE_EXCEPTION_NAME.equals(clazz.getName())) {
			Class<?> batchingBatcherClass = batchingBatcher.getClass();

			Class<?> abstractBatcherClass =
				batchingBatcherClass.getSuperclass();

			Field batchUpdateSQLField = abstractBatcherClass.getDeclaredField(
				"batchUpdateSQL");

			batchUpdateSQLField.setAccessible(true);

			String batchUpdateSQL = (String)batchUpdateSQLField.get(
				batchingBatcher);

			Field logField = abstractBatcherClass.getDeclaredField("log");

			logField.setAccessible(true);

			Class<?> logClass = logField.getType();

			Method errorMethod = logClass.getMethod("error", String.class);

			Object log = logField.get(batchingBatcher);

			StringBuilder sb = new StringBuilder();

			sb.append("{preparedStatement=");
			sb.append(preparedStatement);
			sb.append(", batchUpdateSQL=");
			sb.append(batchUpdateSQL);
			sb.append("}");

			errorMethod.invoke(log, sb.toString());
		}
	}

	private static final String _STALE_STATE_EXCEPTION_NAME =
		"org.hibernate.StaleStateException";

}
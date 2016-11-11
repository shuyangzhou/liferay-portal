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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.SuppressAjWarnings;

/**
 * @author Preston Crary
 */
@Aspect
@SuppressAjWarnings("adviceDidNotMatch")
public class HibernateUnexpectedRowCountAspect {

	@Around(
		"execution(void org.hibernate.jdbc.BatchingBatcher.doExecuteBatch(" +
			"java.sql.PreparedStatement))"
	)
	public void addPreparedStatementString(
			ProceedingJoinPoint proceedingJoinPoint)
		throws Throwable {

		try {
			proceedingJoinPoint.proceed();
		}
		catch (Exception e) {
			Class<?> clazz = e.getClass();

			if (_STALE_STATE_EXCEPTION_NAME.equals(clazz.getName())) {
				Object target = proceedingJoinPoint.getTarget();

				Class<?> batchingBatcherClass = target.getClass();

				Class<?> abstractBatcherClass =
					batchingBatcherClass.getSuperclass();

				Field logField = abstractBatcherClass.getDeclaredField("log");

				logField.setAccessible(true);

				Class<?> logClass = logField.getType();

				Method errorMethod = logClass.getMethod("error", String.class);

				Object[] args = proceedingJoinPoint.getArgs();

				Object log = logField.get(target);

				errorMethod.invoke(
					log, "PreparedStatement: " + args[0].toString());
			}

			throw e;
		}
	}

	private static final String _STALE_STATE_EXCEPTION_NAME =
		"org.hibernate.StaleStateException";

}
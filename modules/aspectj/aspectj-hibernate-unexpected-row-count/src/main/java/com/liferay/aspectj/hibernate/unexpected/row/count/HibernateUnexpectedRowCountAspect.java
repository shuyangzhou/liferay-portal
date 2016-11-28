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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.lang.reflect.Field;

import java.sql.PreparedStatement;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import org.hibernate.StaleStateException;
import org.hibernate.jdbc.AbstractBatcher;
import org.hibernate.jdbc.BatchingBatcher;

/**
 * @author Preston Crary
 */
@Aspect
@SuppressAjWarnings("adviceDidNotMatch")
public class HibernateUnexpectedRowCountAspect {

	@AfterThrowing(
		argNames = "batchingBatcher,preparedStatement,sse",
		throwing = "sse",
		value = "execution(void org.hibernate.jdbc.BatchingBatcher.doExecuteBatch(java.sql.PreparedStatement)) && args(preparedStatement) && this(batchingBatcher)"
	)
	public void logUpdateSQL(
			BatchingBatcher batchingBatcher,
			PreparedStatement preparedStatement, StaleStateException sse)
		throws ReflectiveOperationException {

		StringBundler sb = new StringBundler(5);

		sb.append("{preparedStatement=");
		sb.append(preparedStatement);
		sb.append(", batchUpdateSQL=");
		sb.append(_batchUpdateSQLField.get(batchingBatcher));
		sb.append("}");

		_log.error(sb.toString(), sse);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		HibernateUnexpectedRowCountAspect.class);

	private static final Field _batchUpdateSQLField;

	static {
		try {
			_batchUpdateSQLField = ReflectionUtil.getDeclaredField(
				AbstractBatcher.class, "batchUpdateSQL");
		}
		catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

}
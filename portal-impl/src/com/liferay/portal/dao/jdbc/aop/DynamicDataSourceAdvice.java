/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.dao.jdbc.aop;

import com.liferay.portal.kernel.dao.jdbc.UseDefaultDataSource;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;
import com.liferay.portal.spring.transaction.TransactionInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author László Csontos
 */
public class DynamicDataSourceAdvice
	extends AnnotationChainableMethodAdvice<UseDefaultDataSource> {

	@Override
	public Object before(MethodInvocation methodInvocation) throws Throwable {
		if (skipAdvice(methodInvocation)) {
			return null;
		}

		Operation operation = Operation.WRITE;

		UseDefaultDataSource useDefaultDataSource = findAnnotation(
			methodInvocation);

		if (_transactionInterceptor.isReadOnlyMethod(methodInvocation) &&
			(useDefaultDataSource == _nullUseDefaultDataSource)) {

			operation = Operation.READ;
		}

		DynamicDataSourceTargetSource dynamicDataSourceTargetSource =
			getDynamicDataSourceTargetSource();

		if (_log.isDebugEnabled()) {
			StringBundler sb = new StringBundler(4);

			sb.append("Changing dynamic data source from ");
			sb.append(dynamicDataSourceTargetSource.getOperation());
			sb.append(" to ");
			sb.append(operation);
			sb.append(" for ");
			sb.append(methodInvocation.toString());

			_log.debug(sb.toString());
		}

		dynamicDataSourceTargetSource.setOperation(operation);

		Class<?> targetClass = null;

		if (methodInvocation.getThis() != null) {
			Object thisObject = methodInvocation.getThis();

			targetClass = thisObject.getClass();
		}

		Method targetMethod = methodInvocation.getMethod();

		dynamicDataSourceTargetSource.pushMethod(
			targetClass.getName().concat(StringPool.PERIOD).concat(
				targetMethod.getName()));

		return null;
	}

	@Override
	public void duringFinally(MethodInvocation methodInvocation) {
		DynamicDataSourceTargetSource dynamicDataSourceTargetSource =
			getDynamicDataSourceTargetSource();

		if (dynamicDataSourceTargetSource != null) {
			dynamicDataSourceTargetSource.popMethod();
		}
	}

	@Override
	public UseDefaultDataSource getNullAnnotation() {
		return _nullUseDefaultDataSource;
	}

	public void setTransactionInterceptor(
		TransactionInterceptor transactionInterceptor) {

		_transactionInterceptor = transactionInterceptor;
	}

	protected DynamicDataSourceTargetSource getDynamicDataSourceTargetSource() {
		if (_dynamicDataSourceTargetSource == null) {
			_dynamicDataSourceTargetSource =
				(DynamicDataSourceTargetSource)InfrastructureUtil.
					getDynamicDataSourceTargetSource();
		}

		return _dynamicDataSourceTargetSource;
	}

	protected boolean skipAdvice(MethodInvocation methodInvocation) {
		boolean skip = false;

		DynamicDataSourceTargetSource dynamicDataSourceTargetSource =
			getDynamicDataSourceTargetSource();

		if (dynamicDataSourceTargetSource == null) {
			skip = true;

			serviceBeanAopCacheManager.removeMethodInterceptor(
				methodInvocation, this);

			if (_log.isDebugEnabled()) {
				StringBundler sb = new StringBundler(4);

				sb.append(this.getClass());
				sb.append(" for ");
				sb.append(methodInvocation.toString());
				sb.append(" has been removed");

				_log.debug(sb.toString());
			}
		}

		return skip;
	}

	private static Log _log = LogFactoryUtil.getLog(
		DynamicDataSourceAdvice.class);

	private static UseDefaultDataSource _nullUseDefaultDataSource =
		new UseDefaultDataSource() {

		@Override
		public Class<? extends Annotation> annotationType() {
			return UseDefaultDataSource.class;
		}
	};

	private DynamicDataSourceTargetSource _dynamicDataSourceTargetSource;
	private TransactionInterceptor _transactionInterceptor;

}
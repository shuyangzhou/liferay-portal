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

package com.liferay.portal.dao.jdbc.aop;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AnnotatedMethodInterceptor;
import com.liferay.portal.kernel.dao.jdbc.aop.DynamicDataSourceTargetSource;
import com.liferay.portal.kernel.dao.jdbc.aop.MasterDataSource;
import com.liferay.portal.kernel.dao.jdbc.aop.Operation;
import com.liferay.portal.spring.transaction.TransactionInterceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

import org.springframework.transaction.interceptor.TransactionAttribute;

/**
 * @author Shuyang Zhou
 * @author László Csontos
 */
public class DynamicDataSourceAdvice
	extends AnnotatedMethodInterceptor<MasterDataSource> {

	public void setDynamicDataSourceTargetSource(
		DynamicDataSourceTargetSource dynamicDataSourceTargetSource) {

		_dynamicDataSourceTargetSource = dynamicDataSourceTargetSource;
	}

	public void setTransactionInterceptor(
		TransactionInterceptor transactionInterceptor) {

		_transactionInterceptor = transactionInterceptor;
	}

	@Override
	protected Object before(MethodInvocation methodInvocation)
		throws Throwable {

		Operation operation = Operation.WRITE;

		Object targetBean = methodInvocation.getThis();

		Class<?> targetClass = targetBean.getClass();

		Method targetMethod = methodInvocation.getMethod();

		MasterDataSource masterDataSource = findAnnotation(methodInvocation);

		if (masterDataSource == null) {
			TransactionAttribute transactionAttribute =
				_transactionInterceptor.getTransactionAttribute(
					methodInvocation);

			if ((transactionAttribute != null) &&
				transactionAttribute.isReadOnly()) {

				operation = Operation.READ;
			}
		}

		_dynamicDataSourceTargetSource.setOperation(operation);

		String targetClassName = targetClass.getName();

		_dynamicDataSourceTargetSource.pushMethod(
			targetClassName.concat(StringPool.PERIOD).concat(
				targetMethod.getName()));

		return null;
	}

	@Override
	protected void duringFinally(MethodInvocation methodInvocation) {
		_dynamicDataSourceTargetSource.popMethod();
	}

	@Override
	protected MasterDataSource findAnnotation(
		MethodInvocation methodInvocation) {

		return methodInterceptorCache.findAnnotation(
			methodInvocation, MasterDataSource.class, null);
	}

	private DynamicDataSourceTargetSource _dynamicDataSourceTargetSource;
	private TransactionInterceptor _transactionInterceptor;

}
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
import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.aop.retry.RetryMethodInterceptorFactory;
import com.liferay.portal.kernel.dao.jdbc.aop.DynamicDataSourceTargetSource;
import com.liferay.portal.kernel.dao.jdbc.aop.MasterDataSource;
import com.liferay.portal.kernel.dao.jdbc.aop.Operation;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.spring.transaction.TransactionExecutor;
import com.liferay.portal.spring.transaction.TransactionInterceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.transaction.interceptor.TransactionAttribute;

/**
 * @author Shuyang Zhou
 * @author László Csontos
 * @author Preston Crary
 */
public class DynamicDataSourceMethodInterceptorFactory
	implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorContext methodInterceptorContext) {

		DynamicDataSourceTargetSource dynamicDataSourceTargetSource =
			InfrastructureUtil.getDynamicDataSourceTargetSource();

		TransactionExecutor transactionExecutor =
			methodInterceptorContext.getService(TransactionExecutor.class);

		TransactionInterceptor transactionInterceptor =
			new TransactionInterceptor();

		transactionInterceptor.setTransactionExecutor(transactionExecutor);

		DynamicDataSourceMethodInterceptor dynamicDataSourceMethodInterceptor =
			new DynamicDataSourceMethodInterceptor();

		dynamicDataSourceMethodInterceptor.setDynamicDataSourceTargetSource(
			dynamicDataSourceTargetSource);

		dynamicDataSourceMethodInterceptor.setTransactionInterceptor(
			transactionInterceptor);

		return dynamicDataSourceMethodInterceptor;
	}

	@Override
	public Class<MasterDataSource> getAnnotationClass() {
		return MasterDataSource.class;
	}

	@Override
	public Class<? extends MethodInterceptorFactory> getParentClass() {
		return RetryMethodInterceptorFactory.class;
	}

	@Override
	public boolean isEnabled() {
		DynamicDataSourceTargetSource dynamicDataSourceTargetSource =
			InfrastructureUtil.getDynamicDataSourceTargetSource();

		if (dynamicDataSourceTargetSource == null) {
			return false;
		}

		return true;
	}

	private static class DynamicDataSourceMethodInterceptor
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

			MasterDataSource masterDataSource = findAnnotation(
				methodInvocation);

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

}
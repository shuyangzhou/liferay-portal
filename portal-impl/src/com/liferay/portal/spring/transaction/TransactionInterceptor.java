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

package com.liferay.portal.spring.transaction;

import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;
import com.liferay.portal.spring.aop.ServiceBeanMethodInvocation;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

import org.springframework.transaction.interceptor.TransactionAttribute;

/**
 * @author Shuyang Zhou
 */
public class TransactionInterceptor
	extends AnnotationChainableMethodAdvice<Transactional> {

	public TransactionInterceptor() {
		super(Transactional.class);
	}

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		ServiceBeanMethodInvocation serviceBeanMethodInvocation =
			(ServiceBeanMethodInvocation)methodInvocation;

		TransactionAttributeAdapter transactionAttributeAdapter =
			serviceBeanMethodInvocation.getCurrentAdviceMethodContext();

		return transactionExecutor.execute(
			transactionAttributeAdapter, methodInvocation);
	}

	@Override
	public boolean isEnabled(
		Class<?> targetClass, Method method,
		MethodContextHelper methodContextHelper) {

		Transactional transactional = methodContextHelper.findAnnotation(
			Transactional.class);

		TransactionAttribute transactionAttribute =
			TransactionAttributeBuilder.build(transactional);

		if (transactionAttribute == null) {
			return false;
		}

		TransactionAttributeAdapter transactionAttributeAdapter =
			new TransactionAttributeAdapter(transactionAttribute);

		methodContextHelper.setCurrentAdviceMethodContext(
			transactionAttributeAdapter);

		return true;
	}

	public void setTransactionExecutor(
		TransactionExecutor transactionExecutor) {

		this.transactionExecutor = transactionExecutor;
	}

	protected TransactionExecutor transactionExecutor;

}
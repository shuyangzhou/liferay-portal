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

package com.liferay.portal.spring.aop;

import com.liferay.portal.kernel.spring.aop.Retry;
import com.liferay.portal.service.RetryAdviceAcceptor;

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Matthew Tambara
 */
public class RetryAdvice extends AnnotationChainableMethodAdvice<Retry> {

	@Override
	public void afterReturning(
			MethodInvocation methodInvocation, Object returnValue)
		throws Throwable {

		RetryAdviceAcceptor retryAdviceAcceptor =
			_retryAdviceAcceptor.newInstance();

		_retry.set(retryAdviceAcceptor.accept(returnValue, null));
	}

	@Override
	public void afterThrowing(
			MethodInvocation methodInvocation, Throwable throwable)
		throws Throwable {

		RetryAdviceAcceptor retryAdviceAcceptor =
			_retryAdviceAcceptor.newInstance();

			_retry.set(retryAdviceAcceptor.accept(null, throwable));
	}

	@Override
	public Retry getNullAnnotation() {
		return _nullRetry;
	}

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Retry retry = findAnnotation(methodInvocation);

		if (retry == _nullRetry) {
			return methodInvocation.proceed();
		}

		int numberOfRetries = retry.retries();

		_retryAdviceAcceptor = retry.acceptor();

		Object returnValue = null;

		ServiceBeanMethodInvocation serviceBeanMethodInvocation =
			(ServiceBeanMethodInvocation)methodInvocation;

		serviceBeanMethodInvocation.mark();

		while (true) {
			try {
				returnValue = serviceBeanMethodInvocation.proceed();

				afterReturning(serviceBeanMethodInvocation, returnValue);
			}
			catch (Throwable t) {
				afterThrowing(serviceBeanMethodInvocation, t);

				if (!_retry.get()) {
					throw t;
				}
			}

			if (numberOfRetries-- == 0) {
				break;
			}

			serviceBeanMethodInvocation.reset();

			if (_retry.get()) {
				_retry.set(false);

				continue;
			}

			break;
		}

		return returnValue;
	}

	private static final Retry _nullRetry = new Retry() {

		@Override
		public Class<? extends RetryAdviceAcceptor> acceptor() {
			return null;
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return Retry.class;
		}

		@Override
		public int retries() {
			return 0;
		}

	};

	private static Class<? extends RetryAdviceAcceptor> _retryAdviceAcceptor;

	private final ThreadLocal<Boolean> _retry = new ThreadLocal<>();

}
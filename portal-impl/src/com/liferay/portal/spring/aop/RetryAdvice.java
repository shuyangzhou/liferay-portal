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

import com.liferay.portal.kernel.spring.aop.Property;
import com.liferay.portal.kernel.spring.aop.Retry;
import com.liferay.portal.service.RetryAdviceAcceptor;

import java.lang.annotation.Annotation;

import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Matthew Tambara
 */
public class RetryAdvice extends AnnotationChainableMethodAdvice<Retry> {

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

		Property[] properties = retry.properties();

		Map<String, String> propertyMap = new HashMap<>();

		for (Property property : properties) {
			propertyMap.put(property.propName(), property.propValue());
		}

		Class<? extends RetryAdviceAcceptor> clazz = retry.acceptor();

		RetryAdviceAcceptor retryAdviceAcceptor = clazz.newInstance();

		Object returnValue = null;

		ServiceBeanMethodInvocation serviceBeanMethodInvocation =
			(ServiceBeanMethodInvocation)methodInvocation;

		serviceBeanMethodInvocation.mark();

		while (true) {
			try {
				returnValue = serviceBeanMethodInvocation.proceed();

				if (!retryAdviceAcceptor.accept(
						returnValue, null, propertyMap)) {

					return returnValue;
				}
			}
			catch (Throwable t) {
				if (!retryAdviceAcceptor.accept(null, t, propertyMap)) {
					throw t;
				}
			}

			if (numberOfRetries-- == 0) {
				throw new RetryException();
			}

			serviceBeanMethodInvocation.reset();
		}
	}

	protected class RetryException extends Exception {
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
		public Property[] properties() {
			return null;
		}

		@Override
		public int retries() {
			return 0;
		}

	};

}
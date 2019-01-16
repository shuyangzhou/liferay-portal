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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ExceptionRetryAcceptor;
import com.liferay.portal.kernel.spring.aop.ExceptionRetry;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsValues;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tina Tian
 */
public class ExceptionRetryAdvice extends ChainableMethodAdvice {

	@Override
	public Object createMethodContext(
		Class<?> targetClass, Method method,
		Map<Class<? extends Annotation>, Annotation> annotations) {

		ExceptionRetry exceptionRetry = (ExceptionRetry)annotations.get(
			ExceptionRetry.class);

		if (exceptionRetry == null) {
			return null;
		}

		String exceptionName = exceptionRetry.exceptionName();

		if (Validator.isNull(exceptionName)) {
			return null;
		}

		int retries = exceptionRetry.retries();

		if (retries < 0) {
			retries = PropsValues.RETRY_ADVICE_MAX_RETRIES;
		}

		Map<String, String> properties = new HashMap<>();

		properties.put(ExceptionRetryAcceptor.EXCEPTION_NAME, exceptionName);

		return new RetryContext(properties, retries);
	}

	@Override
	public Object invoke(
			AopMethodInvocation aopMethodInvocation, Object[] arguments)
		throws Throwable {

		RetryContext retryContext =
			aopMethodInvocation.getAdviceMethodContext();

		Map<String, String> properties = retryContext._properties;

		int retries = retryContext._retries;

		int totalRetries = retries;

		if (retries >= 0) {
			retries++;
		}

		Throwable throwable = null;

		while ((retries < 0) || (retries-- > 0)) {
			try {
				return aopMethodInvocation.proceed(arguments);
			}
			catch (Throwable t) {
				throwable = t;

				if (!_acceptException(t, properties)) {
					throw t;
				}

				if (_log.isWarnEnabled() && (retries != 0)) {
					String number = String.valueOf(retries);

					if (retries < 0) {
						number = "unlimited";
					}

					_log.warn(
						StringBundler.concat(
							"Retry on ", String.valueOf(aopMethodInvocation),
							" for ", number, " more times due to exception ",
							String.valueOf(throwable)),
						throwable);
				}
			}
		}

		if (_log.isWarnEnabled()) {
			_log.warn(
				StringBundler.concat(
					"Give up retrying on ", String.valueOf(aopMethodInvocation),
					" after ", String.valueOf(totalRetries),
					" retries and rethrow last retry's exception ",
					String.valueOf(throwable)),
				throwable);
		}

		throw throwable;
	}

	private boolean _acceptException(
		Throwable throwable, Map<String, String> propertyMap) {

		String name = propertyMap.get(ExceptionRetryAcceptor.EXCEPTION_NAME);

		if (name == null) {
			throw new IllegalArgumentException(
				"Missing property " + ExceptionRetryAcceptor.EXCEPTION_NAME);
		}

		while (true) {
			Class<?> clazz = throwable.getClass();

			ClassLoader classLoader = clazz.getClassLoader();

			if (classLoader == null) {
				classLoader = ClassLoader.getSystemClassLoader();
			}

			try {
				Class<?> exceptionClass = classLoader.loadClass(name);

				if (exceptionClass.isInstance(throwable)) {
					return true;
				}
			}
			catch (ClassNotFoundException cnfe) {
			}

			Throwable cause = throwable.getCause();

			if ((throwable == cause) || (cause == null)) {
				break;
			}

			throwable = cause;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ExceptionRetryAdvice.class);

	private static class RetryContext {

		private RetryContext(Map<String, String> properties, int retries) {
			_properties = properties;
			_retries = retries;
		}

		private final Map<String, String> _properties;
		private final int _retries;

	}

}
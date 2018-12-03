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

package com.liferay.portal.messaging.async;

import com.liferay.portal.internal.messaging.async.AsyncInvokeThreadLocal;
import com.liferay.portal.internal.messaging.async.AsyncProcessCallable;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.async.Async;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;
import com.liferay.portal.spring.aop.ServiceBeanMethodInvocation;

import java.lang.reflect.Method;

import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 * @author Brian Wing Shun Chan
 */
public class AsyncAdvice extends AnnotationChainableMethodAdvice<Async> {

	public AsyncAdvice() {
		super(Async.class);
	}

	@Override
	public Object before(MethodInvocation methodInvocation) {
		if (AsyncInvokeThreadLocal.isEnabled()) {
			return null;
		}

		ServiceBeanMethodInvocation serviceBeanMethodInvocation =
			(ServiceBeanMethodInvocation)methodInvocation;

		String callbackDestinationName =
			serviceBeanMethodInvocation.getCurrentAdviceMethodContext();

		TransactionCommitCallbackUtil.registerCallback(
			() -> {
				MessageBusUtil.sendMessage(
					callbackDestinationName,
					new AsyncProcessCallable(methodInvocation));

				return null;
			});

		return nullResult;
	}

	public String getDefaultDestinationName() {
		return _defaultDestinationName;
	}

	@Override
	public boolean isEnabled(
		Class<?> targetClass, Method method,
		MethodContextHelper methodContextHelper) {

		if (!super.isEnabled(targetClass, method, methodContextHelper)) {
			return false;
		}

		if (method.getReturnType() != void.class) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Async annotation on method " + method.getName() +
						" does not return void");
			}

			return false;
		}

		String destinationName = null;

		if ((_destinationNames != null) && !_destinationNames.isEmpty()) {
			destinationName = _destinationNames.get(targetClass);
		}

		if (destinationName == null) {
			destinationName = _defaultDestinationName;
		}

		methodContextHelper.setCurrentAdviceMethodContext(destinationName);

		return true;
	}

	public void setDefaultDestinationName(String defaultDestinationName) {
		_defaultDestinationName = defaultDestinationName;
	}

	public void setDestinationNames(Map<Class<?>, String> destinationNames) {
		_destinationNames = destinationNames;
	}

	private static final Log _log = LogFactoryUtil.getLog(AsyncAdvice.class);

	private String _defaultDestinationName;
	private Map<Class<?>, String> _destinationNames;

}
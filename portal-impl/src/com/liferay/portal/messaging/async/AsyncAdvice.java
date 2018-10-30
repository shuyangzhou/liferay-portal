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

import com.liferay.portal.aop.AnnotatedMethodInterceptor;
import com.liferay.portal.internal.messaging.async.AsyncInvokeThreadLocal;
import com.liferay.portal.internal.messaging.async.AsyncProcessCallable;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.async.Async;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 * @author Brian Wing Shun Chan
 */
public class AsyncAdvice extends AnnotatedMethodInterceptor<Async> {

	@Override
	protected Object before(MethodInvocation methodInvocation)
		throws Throwable {

		if (AsyncInvokeThreadLocal.isEnabled()) {
			return null;
		}

		Async async = findAnnotation(methodInvocation);

		if (async == null) {
			return null;
		}

		Method method = methodInvocation.getMethod();

		if (method.getReturnType() != void.class) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Async annotation on method " + method.getName() +
						" does not return void");
			}

			return null;
		}

		TransactionCommitCallbackUtil.registerCallback(
			() -> {
				MessageBusUtil.sendMessage(
					DestinationNames.ASYNC_SERVICE,
					new AsyncProcessCallable(methodInvocation));

				return null;
			});

		return nullResult;
	}

	private static final Log _log = LogFactoryUtil.getLog(AsyncAdvice.class);

}
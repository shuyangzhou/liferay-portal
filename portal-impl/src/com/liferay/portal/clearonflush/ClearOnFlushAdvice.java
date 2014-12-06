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

package com.liferay.portal.clearonflush;

import com.liferay.portal.kernel.clearonflush.ClearOnFlush;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Preston Crary
 */
public class ClearOnFlushAdvice
	extends AnnotationChainableMethodAdvice<ClearOnFlush> {

	@Override
	public ClearOnFlush getNullAnnotation() {
		return NullClearOnFlush.NULL_CLEAR_ON_FLUSH;
	}

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		boolean clearOnFlushEnabled = ClearOnFlushThreadLocal.isEnabled();

		ClearOnFlushThreadLocal.setEnabled(true);

		try {
			return methodInvocation.proceed();
		}
		finally {
			ClearOnFlushThreadLocal.setEnabled(clearOnFlushEnabled);
		}
	}

}
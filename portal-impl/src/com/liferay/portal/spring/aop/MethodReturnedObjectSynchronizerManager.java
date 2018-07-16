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

import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Mariano Alvaro Saiz
 */
public class MethodReturnedObjectSynchronizerManager
	extends ChainableMethodAdvice {

	public static ChainableMethodAdvice
		getMethodReturnedObjectSynchronizerAdvice() {

		return new ChainableMethodAdvice() {

			@Override
			public Object invoke(MethodInvocation methodInvocation)
				throws Throwable {

				Object returnValue = methodInvocation.proceed();

				return _getUpdatedReturnValue(returnValue);
			}

			private Object _getUpdatedReturnValue(final Object returnValue) {
				Object updatedReturnValue = returnValue;

				for (MethodReturnedObjectSynchronizer synchronizer :
						_synchronizers) {

					updatedReturnValue = synchronizer.updateResult(
						updatedReturnValue);
				}

				return updatedReturnValue;
			}

		};
	}

	public abstract static class MethodReturnedObjectSynchronizer {

		public MethodReturnedObjectSynchronizer() {
			_synchronizers.add(this);
		}

		public abstract Object updateResult(Object result);

	}

	private static final List<MethodReturnedObjectSynchronizer> _synchronizers =
		new ArrayList<>();

}
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import java.util.Objects;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 * @author Brian Wing Shun Chan
 */
public abstract class AnnotationChainableMethodAdvice<T extends Annotation>
	extends ChainableMethodAdvice {

	public AnnotationChainableMethodAdvice(Class<T> annotationClass) {
		_annotationClass = Objects.requireNonNull(annotationClass);
	}

	public Class<T> getAnnotationClass() {
		return _annotationClass;
	}

	@Override
	public boolean isEnabled(
		Class<?> targetClass, Method method,
		AnnotationHelper annotationHelper) {

		T annotation = annotationHelper.findAnnotation(_annotationClass);

		if (annotation == null) {
			return false;
		}

		return true;
	}

	protected T findAnnotation(MethodInvocation methodInvocation) {
		ServiceBeanMethodInvocation serviceBeanMethodInvocation =
			(ServiceBeanMethodInvocation)methodInvocation;

		return serviceBeanMethodInvocation.getAnnotation();
	}

	private final Class<T> _annotationClass;

}
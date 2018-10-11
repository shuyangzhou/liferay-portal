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

import java.util.Objects;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Preston Crary
 */
public abstract class AnnotatedMethodInterceptor<T extends Annotation>
	extends BaseMethodInterceptor {

	protected T findAnnotation(MethodInvocation methodInvocation) {
		T annotation = methodInterceptorCache.findAnnotation(
			methodInvocation, _annotationClass, null);

		if (annotation == null) {
			methodInterceptorCache.removeMethodInterceptor(
				methodInvocation, this);
		}

		return annotation;
	}

	protected void setAnnotationClass(
		Class<? extends Annotation> annotationClass) {

		_annotationClass = Objects.requireNonNull((Class<T>)annotationClass);
	}

	private Class<T> _annotationClass;

}
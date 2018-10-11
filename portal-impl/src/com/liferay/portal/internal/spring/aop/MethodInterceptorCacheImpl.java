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

package com.liferay.portal.internal.spring.aop;

import com.liferay.petra.reflect.AnnotationLocator;
import com.liferay.portal.spring.aop.MethodInterceptorCache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Preston Crary
 */
public class MethodInterceptorCacheImpl implements MethodInterceptorCache {

	@Override
	public void clear() {
		_methodAnnotations.clear();
		_methodInterceptors.clear();
	}

	@Override
	public <T> T findAnnotation(
		MethodInvocation methodInvocation, Class<T> annotationType,
		T defaultValue) {

		Annotation[] annotationsArray = _methodAnnotations.get(
			methodInvocation.getMethod());

		if (annotationsArray == _nullAnnotations) {
			return defaultValue;
		}

		T annotation = defaultValue;

		if (annotationsArray == null) {
			Object target = methodInvocation.getThis();

			List<Annotation> annotations = AnnotationLocator.locate(
				methodInvocation.getMethod(), target.getClass());

			Iterator<Annotation> iterator = annotations.iterator();

			while (iterator.hasNext()) {
				Annotation curAnnotation = iterator.next();

				Class<? extends Annotation> curAnnotationType =
					curAnnotation.annotationType();

				if (!_annotationTypes.contains(curAnnotationType)) {
					iterator.remove();
				}
				else if (annotationType == curAnnotationType) {
					annotation = (T)curAnnotation;
				}
			}

			annotationsArray = _nullAnnotations;

			if (!annotations.isEmpty()) {
				annotationsArray = annotations.toArray(
					new Annotation[annotations.size()]);
			}

			_methodAnnotations.put(
				methodInvocation.getMethod(), annotationsArray);
		}
		else {
			for (Annotation curAnnotation : annotationsArray) {
				if (curAnnotation.annotationType() == annotationType) {
					return (T)curAnnotation;
				}
			}
		}

		return annotation;
	}

	@Override
	public MethodInterceptor[] getMethodInterceptors(
		MethodInvocation methodInvocation) {

		MethodInterceptor[] methodInterceptors = _methodInterceptors.get(
			methodInvocation.getMethod());

		if (methodInterceptors == null) {
			methodInterceptors = _fullMethodInterceptors;

			_methodInterceptors.put(
				methodInvocation.getMethod(), methodInterceptors);
		}

		return methodInterceptors;
	}

	@Override
	public void removeMethodInterceptor(
		MethodInvocation methodInvocation,
		MethodInterceptor methodInterceptor) {

		Method method = methodInvocation.getMethod();

		MethodInterceptor[] methodInterceptors = _methodInterceptors.get(
			method);

		if (methodInterceptors == null) {
			return;
		}

		int index = -1;

		for (int i = 0; i < methodInterceptors.length; i++) {
			if (methodInterceptors[i].equals(methodInterceptor)) {
				index = i;

				break;
			}
		}

		if (index < 0) {
			return;
		}

		int newLength = methodInterceptors.length - 1;

		MethodInterceptor[] newMethodInterceptors = new MethodInterceptor[
			newLength];

		if (index > 0) {
			System.arraycopy(
				methodInterceptors, 0, newMethodInterceptors, 0, index);
		}

		if (index < newLength) {
			System.arraycopy(
				methodInterceptors, index + 1, newMethodInterceptors, index,
				newLength - index);
		}

		if (Arrays.equals(
				newMethodInterceptors, _classLevelMethodInterceptors)) {

			newMethodInterceptors = _classLevelMethodInterceptors;
		}

		_methodInterceptors.put(method, newMethodInterceptors);
	}

	@Override
	public void setMethodInterceptors(
		MethodInvocation methodInvocation,
		MethodInterceptor[] methodInterceptors) {

		_methodInterceptors.put(
			methodInvocation.getMethod(), methodInterceptors);
	}

	public void setMethodInterceptorsAndAnnotationTypes(
		MethodInterceptor[] classLevelMethodInterceptors,
		MethodInterceptor[] fullMethodInterceptors,
		Set<Class<? extends Annotation>> annotationTypes) {

		_classLevelMethodInterceptors = classLevelMethodInterceptors;
		_fullMethodInterceptors = fullMethodInterceptors;
		_annotationTypes = annotationTypes;
	}

	private static final Annotation[] _nullAnnotations = new Annotation[0];

	private Set<Class<? extends Annotation>> _annotationTypes;
	private MethodInterceptor[] _classLevelMethodInterceptors;
	private MethodInterceptor[] _fullMethodInterceptors;
	private final Map<Method, Annotation[]> _methodAnnotations =
		new ConcurrentHashMap<>();
	private final Map<Method, MethodInterceptor[]> _methodInterceptors =
		new ConcurrentHashMap<>();

}
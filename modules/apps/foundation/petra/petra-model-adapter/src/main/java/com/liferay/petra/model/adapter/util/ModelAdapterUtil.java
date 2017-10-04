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

package com.liferay.petra.model.adapter.util;

import com.liferay.portal.kernel.dao.orm.QueryDefinition;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorAdapter;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Shuyang Zhou
 */
public class ModelAdapterUtil {

	public static <T> List<T> adapt(Class<T> clazz, List<?> delegateObjects) {
		List<T> adaptedObjects = new ArrayList<>(delegateObjects.size());

		for (Object delegateObject : delegateObjects) {
			adaptedObjects.add(adapt(clazz, delegateObject));
		}

		return adaptedObjects;
	}

	public static <T> T adapt(Class<T> clazz, Object delegateObject) {
		if (delegateObject == null) {
			return null;
		}

		Class<?> delegateClass = delegateObject.getClass();

		T proxyObject = (T)ProxyUtil.newProxyInstance(
			clazz.getClassLoader(), new Class<?>[] {clazz},
			new InvocationHandler() {

				@Override
				public Object invoke(Object proxy, Method method, Object[] args)
					throws ReflectiveOperationException {

					method = delegateClass.getMethod(
						method.getName(), method.getParameterTypes());

					if (_isEquals(method)) {
						args = new Object[] {
							_delegateObjects.computeIfAbsent(
								args[0], Function.identity())
						};
					}

					return method.invoke(delegateObject, args);
				}

			});

		_delegateObjects.put(proxyObject, delegateObject);

		return proxyObject;
	}

	public static <T> T[] adapt(Class<T> clazz, Object[] delegateObjects) {
		T[] adaptedObjects = (T[])Array.newInstance(
			clazz, delegateObjects.length);

		for (int i = 0; i < delegateObjects.length; i++) {
			adaptedObjects[i] = adapt(clazz, delegateObjects[i]);
		}

		return adaptedObjects;
	}

	public static <T, V> OrderByComparator<T> adapt(
		Class<V> clazz, OrderByComparator<V> orderByComparator) {

		if (orderByComparator == null) {
			return null;
		}

		return new OrderByComparatorAdapter<T, V>(orderByComparator) {

			@Override
			public V adapt(T t) {
				return ModelAdapterUtil.adapt(clazz, t);
			}

		};
	}

	public static <T, V> QueryDefinition<T> adapt(
		Class<V> clazz, QueryDefinition<V> queryDefinition) {

		if (queryDefinition == null) {
			return null;
		}

		QueryDefinition<T> adaptedQueryDefinition = new QueryDefinition<>(
			queryDefinition.getStatus(), queryDefinition.isExcludeStatus(),
			queryDefinition.getOwnerUserId(), queryDefinition.isIncludeOwner(),
			queryDefinition.getStart(), queryDefinition.getEnd(),
			adapt(clazz, queryDefinition.getOrderByComparator()));

		adaptedQueryDefinition.setAttributes(queryDefinition.getAttributes());

		return adaptedQueryDefinition;
	}

	private static boolean _isEquals(Method method) {
		Parameter[] parameters = method.getParameters();
		Class<?> returnType = method.getReturnType();

		if (method.getName().equals("equals") && returnType.isPrimitive() &&
			returnType.getName().equals("boolean") &&
			(parameters.length == 1) &&
			Object.class.equals(parameters[0].getType())) {

			return true;
		}

		return false;
	}

	private static final Map<Object, Object> _delegateObjects = new HashMap<>();

}
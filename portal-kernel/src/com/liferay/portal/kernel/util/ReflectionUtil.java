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

package com.liferay.portal.kernel.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Brian Wing Shun Chan
 * @author Miguel Pastor
 * @author Shuyang Zhou
 * @deprecated As of 7.0.0, replaced by {@link
 *             com.liferay.petra.reflect.ReflectionUtil}
 */
@Deprecated
public class ReflectionUtil {

	public static Object arrayClone(Object array) {
		return com.liferay.petra.reflect.ReflectionUtil.arrayClone(array);
	}

	public static Field getDeclaredField(Class<?> clazz, String name)
		throws Exception {

		return com.liferay.petra.reflect.ReflectionUtil.getDeclaredField(
			clazz, name);
	}

	public static Field[] getDeclaredFields(Class<?> clazz) throws Exception {
		return com.liferay.petra.reflect.ReflectionUtil.getDeclaredFields(
			clazz);
	}

	public static Method getDeclaredMethod(
			Class<?> clazz, String name, Class<?>... parameterTypes)
		throws Exception {

		return com.liferay.petra.reflect.ReflectionUtil.getDeclaredMethod(
			clazz, name, parameterTypes);
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public static Type getGenericInterface(
		Object object, Class<?> interfaceClass) {

		Class<?> clazz = object.getClass();

		Type genericInterface = _getGenericInterface(clazz, interfaceClass);

		if (genericInterface != null) {
			return genericInterface;
		}

		Class<?> superClass = clazz.getSuperclass();

		while (superClass != null) {
			genericInterface = _getGenericInterface(superClass, interfaceClass);

			if (genericInterface != null) {
				return genericInterface;
			}

			superClass = superClass.getSuperclass();
		}

		return null;
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public static Class<?> getGenericSuperType(Class<?> clazz) {
		try {
			ParameterizedType parameterizedType =
				(ParameterizedType)clazz.getGenericSuperclass();

			Type[] types = parameterizedType.getActualTypeArguments();

			if (types.length > 0) {
				return (Class<?>)types[0];
			}
		}
		catch (Throwable t) {
		}

		return null;
	}

	public static Class<?>[] getInterfaces(Object object) {
		return com.liferay.petra.reflect.ReflectionUtil.getInterfaces(object);
	}

	public static Class<?>[] getInterfaces(
		Object object, ClassLoader classLoader) {

		return com.liferay.petra.reflect.ReflectionUtil.getInterfaces(
			object, classLoader);
	}

	public static Class<?>[] getInterfaces(
		Object object, ClassLoader classLoader,
		Consumer<ClassNotFoundException> classNotFoundHandler) {

		return com.liferay.petra.reflect.ReflectionUtil.getInterfaces(
			object, classLoader, classNotFoundHandler);
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public static Class<?>[] getParameterTypes(Object[] arguments) {
		if (arguments == null) {
			return null;
		}

		Class<?>[] parameterTypes = new Class<?>[arguments.length];

		for (int i = 0; i < arguments.length; i++) {
			if (arguments[i] == null) {
				parameterTypes[i] = null;
			}
			else if (arguments[i] instanceof Boolean) {
				parameterTypes[i] = Boolean.TYPE;
			}
			else if (arguments[i] instanceof Byte) {
				parameterTypes[i] = Byte.TYPE;
			}
			else if (arguments[i] instanceof Character) {
				parameterTypes[i] = Character.TYPE;
			}
			else if (arguments[i] instanceof Double) {
				parameterTypes[i] = Double.TYPE;
			}
			else if (arguments[i] instanceof Float) {
				parameterTypes[i] = Float.TYPE;
			}
			else if (arguments[i] instanceof Integer) {
				parameterTypes[i] = Integer.TYPE;
			}
			else if (arguments[i] instanceof Long) {
				parameterTypes[i] = Long.TYPE;
			}
			else if (arguments[i] instanceof Short) {
				parameterTypes[i] = Short.TYPE;
			}
			else {
				parameterTypes[i] = arguments[i].getClass();
			}
		}

		return parameterTypes;
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public static Set<Method> getVisibleMethods(Class<?> clazz) {
		Set<Method> visibleMethods = new HashSet<>(
			Arrays.asList(clazz.getMethods()));

		Collections.addAll(visibleMethods, clazz.getDeclaredMethods());

		while ((clazz = clazz.getSuperclass()) != null) {
			for (Method method : clazz.getDeclaredMethods()) {
				int modifiers = method.getModifiers();

				if (!Modifier.isPrivate(modifiers) &
					!Modifier.isPublic(modifiers)) {

					visibleMethods.add(method);
				}
			}
		}

		return visibleMethods;
	}

	public static <T> T throwException(Throwable throwable) {
		return com.liferay.petra.reflect.ReflectionUtil.throwException(
			throwable);
	}

	public static Field unfinalField(Field field) throws Exception {
		return com.liferay.petra.reflect.ReflectionUtil.unfinalField(field);
	}

	private static Type _getGenericInterface(
		Class<?> clazz, Class<?> interfaceClass) {

		Type[] genericInterfaces = clazz.getGenericInterfaces();

		for (Type genericInterface : genericInterfaces) {
			if (!(genericInterface instanceof ParameterizedType)) {
				continue;
			}

			ParameterizedType parameterizedType =
				(ParameterizedType)genericInterface;

			Type rawType = parameterizedType.getRawType();

			if (rawType.equals(interfaceClass)) {
				return parameterizedType;
			}
		}

		return null;
	}

}
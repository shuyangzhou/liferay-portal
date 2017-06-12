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

package com.liferay.portal.configuration.module.configuration.internal;

import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.settings.Settings;
import com.liferay.portal.kernel.settings.TypedSettings;
import com.liferay.portal.kernel.util.StringBundler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Preston Crary
 */
public class ConfigurationOverrideInstance {

	public static ConfigurationOverrideInstance
			getConfigurationOverrideInstance(
				Class<?> clazz, TypedSettings typedSettings)
		throws ConfigurationException, ReflectiveOperationException {

		Class<?> configurationOverrideClass = _getOverrideClass(clazz);

		if (configurationOverrideClass == null) {
			return null;
		}

		ConfigurationOverrideInstance configurationOverrideInstance =
			_configurationOverrideInstances.get(configurationOverrideClass);

		if (configurationOverrideInstance == null) {
			configurationOverrideInstance = new ConfigurationOverrideInstance(
				configurationOverrideClass, typedSettings);

			_configurationOverrideInstances.put(
				configurationOverrideClass, configurationOverrideInstance);
		}

		return configurationOverrideInstance;
	}

	public Object invoke(String methodName)
		throws ReflectiveOperationException {

		Method method = _methods.get(methodName);

		if (method == null) {
			return null;
		}

		return method.invoke(_configurationOverrideInstance);
	}

	private static Class<?> _getOverrideClass(Class<?> clazz) {
		Settings.OverrideClass overrideClass = clazz.getAnnotation(
			Settings.OverrideClass.class);

		if (overrideClass == null) {
			return null;
		}

		if (overrideClass.value() == Object.class) {
			return null;
		}

		return overrideClass.value();
	}

	private ConfigurationOverrideInstance(
			Class<?> configurationOverrideClass, TypedSettings typedSettings)
		throws ConfigurationException, ReflectiveOperationException {

		Constructor<?> constructor = configurationOverrideClass.getConstructor(
			TypedSettings.class);

		_configurationOverrideInstance = constructor.newInstance(typedSettings);

		for (Method method : configurationOverrideClass.getMethods()) {
			if (!_objectMethodSet.contains(method)) {
				Method previousMethod = _methods.put(method.getName(), method);

				if (previousMethod != null) {
					StringBundler sb = new StringBundler(5);

					sb.append("Unexpected duplicate method ");
					sb.append(method.getName());
					sb.append(" for class ");
					sb.append(configurationOverrideClass);
					sb.append(" method arguments are not supported");

					throw new ConfigurationException(sb.toString());
				}
			}
		}
	}

	private static final Map<Class<?>, ConfigurationOverrideInstance>
		_configurationOverrideInstances = new ConcurrentHashMap<>();
	private static final Set<Method> _objectMethodSet = new HashSet<>();

	static {
		Collections.addAll(_objectMethodSet, Object.class.getMethods());
	}

	private final Object _configurationOverrideInstance;
	private final Map<String, Method> _methods = new HashMap<>();

}
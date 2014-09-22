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

package com.liferay.portal.kernel.cache.configuration;

import com.liferay.portal.kernel.util.HashUtil;

import java.util.Properties;

/**
 * @author Tina Tian
 */
public class FactoryConfiguration {

	public FactoryConfiguration(
		String factoryClassName, Properties properties) {

		if (factoryClassName == null) {
			throw new NullPointerException("Factory class name is null");
		}

		if (properties == null) {
			throw new NullPointerException("Properties is null");
		}

		_factoryClassName = factoryClassName;
		_properties = properties;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof FactoryConfiguration)) {
			return false;
		}

		FactoryConfiguration factoryConfiguration =
			(FactoryConfiguration)object;

		return _factoryClassName.equals(
				factoryConfiguration._factoryClassName) &&
			_properties.equals(factoryConfiguration._properties);
	}

	public String getFactoryClassName() {
		return _factoryClassName;
	}

	public Properties getProperties() {
		return _properties;
	}

	@Override
	public int hashCode() {
		int hash = HashUtil.hash(0, _factoryClassName);

		return HashUtil.hash(hash, _properties);
	}

	private final String _factoryClassName;
	private final Properties _properties;

}
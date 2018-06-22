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

package com.liferay.portal.configuration.upgrade;

/**
 * @author Drew Brokke
 */
public interface PrefsPropsToConfigurationUpgradeHelper {

	public <T> void mapConfigurations(
			Class<T> configurationClass,
			ConfigurationMapper<T> configurationMapper)
		throws Exception;

	@FunctionalInterface
	public interface ConfigurationMapper<T> {

		public void map(
			T defaultConfiguration,
			ConfigurationMappingCollector configurationMappingCollector);

	}

	public interface ConfigurationMappingCollector {

		public void mapConfiguration(
			String oldKey, String newKey, boolean defaultValue);

		public void mapConfiguration(
			String oldKey, String newKey, double defaultValue);

		public void mapConfiguration(
			String oldKey, String newKey, float defaultValue);

		public void mapConfiguration(
			String oldKey, String newKey, int defaultValue);

		public void mapConfiguration(
			String oldKey, String newKey, long defaultValue);

		public void mapConfiguration(
			String oldKey, String newKey, short defaultValue);

		public void mapConfiguration(
			String oldKey, String newKey, String defaultValue);

		public void mapConfiguration(
			String oldKey, String newKey, String[] defaultValue);

	}

}
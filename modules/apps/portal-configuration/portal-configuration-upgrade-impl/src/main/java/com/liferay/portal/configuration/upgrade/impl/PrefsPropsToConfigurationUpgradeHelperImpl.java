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

package com.liferay.portal.configuration.upgrade.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.configuration.upgrade.PrefsPropsToConfigurationUpgradeHelper;
import com.liferay.portal.kernel.util.Function;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.PrefsProps;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Objects;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;

import org.osgi.framework.Constants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Drew Brokke
 */
@Component(
	immediate = true, service = PrefsPropsToConfigurationUpgradeHelper.class
)
public class PrefsPropsToConfigurationUpgradeHelperImpl
	implements PrefsPropsToConfigurationUpgradeHelper {

	@Override
	public <T> void mapConfigurations(
			Class<T> configurationClass,
			ConfigurationMapper<T> configurationMapper)
		throws Exception {

		String filterString = StringBundler.concat(
			"(", Constants.SERVICE_PID, "=", configurationClass.getName(), ")");

		Configuration[] configurations = _configurationAdmin.listConfigurations(
			filterString);

		if (configurations != null) {
			return;
		}

		Dictionary<String, Object> properties = new HashMapDictionary<>();

		PortletPreferences portletPreferences = _prefsProps.getPreferences();

		T defaultConfiguration = ConfigurableUtil.createConfigurable(
			configurationClass, new HashMapDictionary<>());

		configurationMapper.map(
			defaultConfiguration,
			new ConfigurationMappingCollector() {

				@Override
				public void mapConfiguration(
					String oldKey, String newKey, boolean defaultValue) {

					_mapConfiguration(
						oldKey, newKey, defaultValue, GetterUtil::getBoolean);
				}

				@Override
				public void mapConfiguration(
					String oldKey, String newKey, double defaultValue) {

					_mapConfiguration(
						oldKey, newKey, defaultValue, GetterUtil::getDouble);
				}

				@Override
				public void mapConfiguration(
					String oldKey, String newKey, int defaultValue) {

					_mapConfiguration(
						oldKey, newKey, defaultValue, GetterUtil::getInteger);
				}

				@Override
				public void mapConfiguration(
					String oldKey, String newKey, long defaultValue) {

					_mapConfiguration(
						oldKey, newKey, defaultValue, GetterUtil::getLong);
				}

				@Override
				public void mapConfiguration(
					String oldKey, String newKey, short defaultValue) {

					_mapConfiguration(
						oldKey, newKey, defaultValue, GetterUtil::getShort);
				}

				@Override
				public void mapConfiguration(
					String oldKey, String newKey, String defaultValue) {

					_mapConfiguration(
						oldKey, newKey, defaultValue, GetterUtil::getString);
				}

				@Override
				public void mapConfiguration(
					String oldKey, String newKey, String[] defaultValue) {

					String valueString = _prefsProps.getString(oldKey, null);

					if (Validator.isNull(valueString)) {
						return;
					}

					String[] value = StringUtil.split(valueString);

					if (!Arrays.equals(value, defaultValue)) {
						properties.put(newKey, value);

						if (!portletPreferences.isReadOnly(oldKey)) {
							try {
								portletPreferences.reset(oldKey);
							}
							catch (ReadOnlyException roe) {
								throw new RuntimeException(roe);
							}
						}
					}
				}

				private void _mapConfiguration(
					String oldKey, String newKey, Object defaultValue,
					Function<String, Object> getterFunction) {

					String valueString = _prefsProps.getString(oldKey, null);

					if (Validator.isNull(valueString)) {
						return;
					}

					Object value = getterFunction.apply(valueString);

					if (!Objects.equals(value, defaultValue)) {
						properties.put(newKey, value);

						if (!portletPreferences.isReadOnly(oldKey)) {
							try {
								portletPreferences.reset(oldKey);
							}
							catch (ReadOnlyException roe) {
								throw new RuntimeException(roe);
							}
						}
					}
				}

			});

		if (properties.isEmpty()) {
			return;
		}

		Configuration configuration = _configurationAdmin.getConfiguration(
			configurationClass.getName(), StringPool.QUESTION);

		configuration.update(properties);

		portletPreferences.store();
	}

	@Reference
	private ConfigurationAdmin _configurationAdmin;

	@Reference
	private PrefsProps _prefsProps;

}
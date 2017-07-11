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

package com.liferay.portal.bundle.blacklist.internal;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.util.PropsValues;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Dictionary;
import java.util.List;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Matthew Tambara
 */
@Component(
	configurationPid = "com.liferay.portal.bundle.blacklist.internal.BundleBlacklistConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true
)
public class BundleBlacklist {

	@Activate
	protected void activate(ComponentContext componentContext)
		throws Exception {

		_bundleContext = componentContext.getBundleContext();

		Dictionary<String, Object> properties =
			componentContext.getProperties();

		_bundleBlacklistConfiguration = ConfigurableUtil.createConfigurable(
			BundleBlacklistConfiguration.class, properties);

		for (Bundle bundle : _bundleContext.getBundles()) {
			_processBundle(bundle);
		}

		_bundleContext.addBundleListener(_bundleListener);

		List<String> blacklistBundles =
			_bundleBlacklistConfiguration.blacklistBundles();

		Properties blacklistBundleProperties = _loadBlacklistBundlesProperties(
			_bundleContext);

		if (blacklistBundleProperties == null) {
			return;
		}

		for (String symbolicName : blacklistBundleProperties.
				stringPropertyNames()) {

			if (!blacklistBundles.contains(symbolicName)) {
				if (_log.isInfoEnabled()) {
					_log.info("Reinstalling bundle " + symbolicName);
				}

				Bundle bundle = _bundleContext.installBundle(
					blacklistBundleProperties.getProperty(symbolicName));

				BundleStartLevel bundleStartLevel = bundle.adapt(
					BundleStartLevel.class);

				bundleStartLevel.setStartLevel(
					PropsValues.MODULE_FRAMEWORK_DYNAMIC_INSTALL_START_LEVEL);

				bundle.start();

				blacklistBundleProperties.remove(symbolicName);

				_saveBlacklistBundlesProperties(
					_bundleContext, blacklistBundleProperties);
			}
		}
	}

	@Deactivate
	protected void deactivate(BundleContext bundleContext) {
		bundleContext.removeBundleListener(_bundleListener);
	}

	private Properties _loadBlacklistBundlesProperties(
			BundleContext bundleContext)
		throws IOException {

		Bundle bundle = bundleContext.getBundle(0);

		BundleContext systemBundleContext = bundle.getBundleContext();

		File blacklistBundlesPropertiesFile = systemBundleContext.getDataFile(
			"blacklist-bundles.properties");

		Properties blacklistBundles = new Properties();

		if (blacklistBundlesPropertiesFile.exists()) {
			try (InputStream inputStream = new FileInputStream(
					blacklistBundlesPropertiesFile)) {

				blacklistBundles.load(inputStream);
			}
		}

		return blacklistBundles;
	}

	private void _processBundle(Bundle bundle) throws Exception {
		List<String> blacklistBundles =
			_bundleBlacklistConfiguration.blacklistBundles();

		if (blacklistBundles == null) {
			return;
		}

		String symbolicName = bundle.getSymbolicName();

		if (blacklistBundles.contains(symbolicName)) {
			if (_log.isInfoEnabled()) {
				_log.info("Stopping blacklisted bundle " + bundle);
			}

			Properties properties = _loadBlacklistBundlesProperties(
				_bundleContext);

			properties.put(symbolicName, bundle.getLocation());

			_saveBlacklistBundlesProperties(_bundleContext, properties);

			bundle.uninstall();
		}
	}

	private void _saveBlacklistBundlesProperties(
			BundleContext bundleContext, Properties properties)
		throws IOException {

		Bundle bundle = bundleContext.getBundle(0);

		BundleContext systemBundleContext = bundle.getBundleContext();

		File blacklistBundlesPropertiesFile = systemBundleContext.getDataFile(
			"blacklist-bundles.properties");

		try (OutputStream outputStream = new FileOutputStream(
				blacklistBundlesPropertiesFile)) {

			properties.store(outputStream, null);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BundleBlacklist.class);

	private BundleBlacklistConfiguration _bundleBlacklistConfiguration;
	private BundleContext _bundleContext;

	private final BundleListener _bundleListener = new BundleListener() {

		@Override
		public void bundleChanged(BundleEvent bundleEvent) {
			if (bundleEvent.getType() != BundleEvent.RESOLVED) {
				return;
			}

			try {
				_processBundle(bundleEvent.getBundle());
			}
			catch (Exception e) {
				ReflectionUtil.throwException(e);
			}
		}

	};

}
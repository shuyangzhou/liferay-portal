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

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		for (String symbolicName : _uninstalledBundles.keySet()) {
			if (!blacklistBundles.contains(symbolicName)) {
				if (_log.isInfoEnabled()) {
					_log.info("Reinstalling bundle " + symbolicName);
				}

				Bundle bundle = _bundleContext.installBundle(
					_uninstalledBundles.get(symbolicName));

				BundleStartLevel bundleStartLevel = bundle.adapt(
					BundleStartLevel.class);

				bundleStartLevel.setStartLevel(
					PropsValues.MODULE_FRAMEWORK_DYNAMIC_INSTALL_START_LEVEL);

				bundle.start();

				_uninstalledBundles.remove(symbolicName);
			}
		}
	}

	@Deactivate
	protected void deactivate(BundleContext bundleContext) {
		bundleContext.removeBundleListener(_bundleListener);
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

			_uninstalledBundles.put(symbolicName, bundle.getLocation());

			bundle.uninstall();
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

	private final Map<String, String> _uninstalledBundles = new HashMap<>();

}
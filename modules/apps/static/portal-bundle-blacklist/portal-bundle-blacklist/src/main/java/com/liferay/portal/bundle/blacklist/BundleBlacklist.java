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

package com.liferay.portal.bundle.blacklist;

import com.liferay.portal.bundle.blacklist.internal.BundleBlacklistConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.concurrent.DefaultNoticeableFuture;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.lpkg.deployer.LPKGDeployer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.framework.startlevel.FrameworkStartLevel;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Tambara
 */
@Component(
	configurationPid = "com.liferay.portal.bundle.blacklist.internal.BundleBlacklistConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true,
	service = BundleBlacklist.class
)
public class BundleBlacklist {

	@Activate
	@Modified
	protected void activate(
			BundleContext bundleContext, Map<String, String> properties)
		throws Throwable {

		_bundleContext = bundleContext;

		Bundle systemBundle = bundleContext.getBundle(0);

		_frameworkWiring = systemBundle.adapt(FrameworkWiring.class);

		_bundleBlacklistConfiguration = ConfigurableUtil.createConfigurable(
			BundleBlacklistConfiguration.class, properties);

		bundleContext.addBundleListener(_bundleListener);

		_scanBundles();

		_serviceReference = bundleContext.getServiceReference(
			LPKGDeployer.class);

		String[] blacklistBundles =
			_bundleBlacklistConfiguration.blacklistBundles();

		Set<Entry<String, UninstalledBundleData>> entrySet =
			_uninstalledBundles.entrySet();

		Iterator<Entry<String, UninstalledBundleData>> iterator =
			entrySet.iterator();

		while (iterator.hasNext()) {
			Entry<String, UninstalledBundleData> entry = iterator.next();

			String symbolicName = entry.getKey();

			if (!ArrayUtil.contains(blacklistBundles, symbolicName)) {
				if (_log.isInfoEnabled()) {
					_log.info("Reinstalling bundle " + symbolicName);
				}

				_installBundle(entry.getValue());

				iterator.remove();
			}
		}
	}

	@Deactivate
	protected void deactivate(BundleContext bundleContext) throws Throwable {
		bundleContext.removeBundleListener(_bundleListener);

		for (UninstalledBundleData uninstalledBundleData :
				_uninstalledBundles.values()) {

			_installBundle(uninstalledBundleData);
		}
	}

	private static void _startBundle(Bundle bundle) throws BundleException {
		Dictionary<String, String> headers = bundle.getHeaders();

		String fragmentHost = headers.get(Constants.FRAGMENT_HOST);

		if (fragmentHost == null) {
			bundle.start();
		}
	}

	private void _installBundle(UninstalledBundleData uninstalledBundleData)
		throws Throwable {

		Bundle bundle = null;

		String location = uninstalledBundleData.getLocation();

		Map<String, String[]> parameters = _http.getParameterMap(
			_http.getQueryString(location));

		String[] lpkgPath = parameters.get("lpkgPath");

		String[] protocol = parameters.get("protocol");

		String[] webContextPath = parameters.get("Web-ContextPath");

		if (parameters.isEmpty() && location.endsWith(".lpkg")) {
			bundle = _bundleContext.installBundle(
				location, _lpkgToBundle(new File(location)));
		}
		else if (ArrayUtil.isNotEmpty(lpkgPath)) {
			bundle = _bundleContext.getBundle(lpkgPath[0]);

			_refreshBundles(Collections.<Bundle>singletonList(bundle));

			return;
		}
		else if (ArrayUtil.isNotEmpty(protocol) && protocol[0].equals("lpkg") &&
				 ArrayUtil.isNotEmpty(webContextPath)) {

			String contextName = webContextPath[0].substring(1);

			for (Bundle installedBundle : _bundleContext.getBundles()) {
				Dictionary<String, String> headers =
					installedBundle.getHeaders();

				if (contextName.equals(
						headers.get("Liferay-WAB-Context-Name"))) {

					_refreshBundles(
						Collections.<Bundle>singletonList(installedBundle));
				}
			}

			return;
		}
		else {
			bundle = _bundleContext.installBundle(location);
		}

		int startLevel = uninstalledBundleData.getStartLevel();

		Bundle systemBundle = _bundleContext.getBundle(0);

		FrameworkStartLevel frameworkStartLevel = systemBundle.adapt(
			FrameworkStartLevel.class);

		BundleStartLevel bundleStartLevel = bundle.adapt(
			BundleStartLevel.class);

		if (frameworkStartLevel.getStartLevel() >= startLevel) {
			_startBundle(bundle);

			bundleStartLevel.setStartLevel(startLevel);
		}
		else {
			bundleStartLevel.setStartLevel(startLevel);

			_startBundle(bundle);
		}
	}

	private InputStream _lpkgToBundle(File lpkgFile) throws IOException {
		try (UnsyncByteArrayOutputStream unsyncByteArrayOutputStream =
				new UnsyncByteArrayOutputStream()) {

			try (ZipFile zipFile = new ZipFile(lpkgFile);
				JarOutputStream jarOutputStream = new JarOutputStream(
					unsyncByteArrayOutputStream)) {

				Manifest manifest = new Manifest();

				Attributes attributes = manifest.getMainAttributes();

				Properties properties = new Properties();

				properties.load(
					zipFile.getInputStream(
						zipFile.getEntry("liferay-marketplace.properties")));

				attributes.putValue(
					Constants.BUNDLE_DESCRIPTION,
					properties.getProperty("description"));

				attributes.putValue(Constants.BUNDLE_MANIFESTVERSION, "2");
				attributes.putValue(
					Constants.BUNDLE_SYMBOLICNAME,
					properties.getProperty("title"));
				attributes.putValue(
					Constants.BUNDLE_VERSION,
					properties.getProperty("version"));
				attributes.putValue("Liferay-Releng-Bundle-Type", "lpkg");
				attributes.putValue("Manifest-Version", "2");

				jarOutputStream.putNextEntry(
					new ZipEntry(JarFile.MANIFEST_NAME));

				manifest.write(jarOutputStream);

				jarOutputStream.closeEntry();

				Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();

				while (zipEntries.hasMoreElements()) {
					ZipEntry zipEntry = zipEntries.nextElement();

					jarOutputStream.putNextEntry(
						new ZipEntry(zipEntry.getName()));

					StreamUtil.transfer(
						zipFile.getInputStream(zipEntry), jarOutputStream,
						false);

					jarOutputStream.closeEntry();
				}
			}

			return new UnsyncByteArrayInputStream(
				unsyncByteArrayOutputStream.unsafeGetByteArray(), 0,
				unsyncByteArrayOutputStream.size());
		}
	}

	private boolean _processBundle(Bundle bundle) throws Exception {
		String[] blacklistBundles =
			_bundleBlacklistConfiguration.blacklistBundles();

		String symbolicName = bundle.getSymbolicName();

		if (ArrayUtil.contains(blacklistBundles, symbolicName)) {
			if (_log.isInfoEnabled()) {
				_log.info("Stopping blacklisted bundle " + bundle);
			}

			BundleStartLevel bundleStartLevel = bundle.adapt(
				BundleStartLevel.class);

			_uninstalledBundles.put(
				symbolicName,
				new UninstalledBundleData(
					bundle.getLocation(), bundleStartLevel.getStartLevel()));

			try {
				bundle.uninstall();
			}
			catch (Exception e) {
				_log.error("Unable to uninstall " + bundle, e);

				_uninstalledBundles.remove(symbolicName);
			}

			return true;
		}

		return false;
	}

	private void _refreshBundles(List<Bundle> refreshBundles) {
		final DefaultNoticeableFuture<FrameworkEvent> defaultNoticeableFuture =
			new DefaultNoticeableFuture<>();

		_frameworkWiring.refreshBundles(
			refreshBundles,
			new FrameworkListener() {

				@Override
				public void frameworkEvent(FrameworkEvent frameworkEvent) {
					defaultNoticeableFuture.set(frameworkEvent);
				}

			});

		try {
			FrameworkEvent frameworkEvent = defaultNoticeableFuture.get();

			if (frameworkEvent.getType() != FrameworkEvent.PACKAGES_REFRESHED) {
				throw frameworkEvent.getThrowable();
			}
		}
		catch (Throwable t) {
			ReflectionUtil.throwException(t);
		}
	}

	private void _scanBundles() throws Exception {
		List<Bundle> uninstalledBundles = new ArrayList<>();

		for (Bundle bundle : _bundleContext.getBundles()) {
			if ((bundle.getState() != Bundle.UNINSTALLED) &&
				_processBundle(bundle)) {

				uninstalledBundles.add(bundle);
			}
		}

		if (!uninstalledBundles.isEmpty()) {
			_refreshBundles(uninstalledBundles);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BundleBlacklist.class);

	private BundleBlacklistConfiguration _bundleBlacklistConfiguration;
	private BundleContext _bundleContext;

	private final BundleListener _bundleListener =
		new SynchronousBundleListener() {

			@Override
			public void bundleChanged(BundleEvent bundleEvent) {
				if (bundleEvent.getType() != BundleEvent.INSTALLED) {
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

	private FrameworkWiring _frameworkWiring;

	@Reference
	private Http _http;

	private ServiceReference<LPKGDeployer> _serviceReference;
	private final Map<String, UninstalledBundleData> _uninstalledBundles =
		new HashMap<>();

	private class UninstalledBundleData {

		public UninstalledBundleData(String location, int startLevel) {
			_location = location;

			_startLevel = startLevel;
		}

		public String getLocation() {
			return _location;
		}

		public int getStartLevel() {
			return _startLevel;
		}

		private final String _location;
		private final int _startLevel;

	}

}
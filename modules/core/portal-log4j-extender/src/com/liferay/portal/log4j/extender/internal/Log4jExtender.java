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

package com.liferay.portal.log4j.extender.internal;

import java.io.IOException;

import java.net.URL;

import java.util.Arrays;
import java.util.Enumeration;

import java.util.logging.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.SynchronousConfigurationListener;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * @author Shuyang Zhou
 */
public class Log4jExtender implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ServiceTracker<ConfigurationAdmin, ConfigurationAdmin>
			configurationAdminServiceTracker = new ServiceTracker<>(
				bundleContext, ConfigurationAdmin.class, null);

		configurationAdminServiceTracker.open();

		final ConfigurationAdmin configurationAdmin =
			configurationAdminServiceTracker.waitForService(5000);

		bundleContext.registerService(
			SynchronousConfigurationListener.class,
			new SynchronousConfigurationListener() {

			@Override
			public void configurationEvent(
				ConfigurationEvent configurationEvent) {

				System.out.println(
					"########## pid : " + configurationEvent.getPid() +
						", factoryPid :" + configurationEvent.getFactoryPid() +
							", type : " + configurationEvent.getType());

				if (configurationEvent.getType() ==
					ConfigurationEvent.CM_DELETED || configurationEvent.getPid().contains("com.liferay.portal.store.file.system.configuration.FileSystemStoreConfiguration")) {

					new Exception().printStackTrace(System.out);
				}

				try {
					System.out.println(
						"%%%%%%%%%%%%%" + Arrays.toString(configurationAdmin.listConfigurations(null)));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, null);

		System.out.println(
			"##############" + Arrays.toString(configurationAdmin.listConfigurations(null)));

		_tracker = new BundleTracker<Void>(
			bundleContext, Bundle.STARTING, null) {

			@Override
			public Void addingBundle(Bundle bundle, BundleEvent bundleEvent) {
				try {
					_configureLog4j(bundle, "META-INF/module-log4j.xml");
					_configureLog4j(bundle, "META-INF/module-log4j-ext.xml");
				}
				catch (IOException ioe) {
					_logger.error(
						"Unable to configure Log4j for bundle " +
							bundle.getSymbolicName(),
						ioe);
				}

				return null;
			}

		};

		_tracker.open();
	}

	@Override
	public void stop(BundleContext context) {
		_tracker.close();
	}

	private void _configureLog4j(Bundle bundle, String resourcePath)
		throws IOException {

		Enumeration<URL> enumeration = bundle.getResources(resourcePath);

		if (enumeration != null) {
			while (enumeration.hasMoreElements()) {
				DOMConfigurator domConfigurator = new DOMConfigurator();

				domConfigurator.doConfigure(
					enumeration.nextElement(),
					LogManager.getLoggerRepository());
			}
		}
	}

	private static final Logger _logger = Logger.getLogger(Log4jExtender.class);

	private volatile BundleTracker<Void> _tracker;

}
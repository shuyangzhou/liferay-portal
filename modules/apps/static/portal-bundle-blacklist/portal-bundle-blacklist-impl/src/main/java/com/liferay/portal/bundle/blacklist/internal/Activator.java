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

import com.liferay.portal.util.PropsValues;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Dictionary;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * @author Shuyang Zhou
 */
public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("***********Starting " + context.getBundle());

		ServiceReference<ConfigurationAdmin> serviceReference =
			context.getServiceReference(ConfigurationAdmin.class);

		ConfigurationAdmin configurationAdmin = context.getService(
			serviceReference);

		Configuration configuration = configurationAdmin.getConfiguration(
			"com.liferay.portal.bundle.blacklist.internal." +
				"BundleBlacklistConfiguration");

		Dictionary<?, ?> dictionary = configuration.getProperties();

		if (dictionary == null) {
			System.out.println("^^^^^^No configuration for blacklist");
		}
		else {
			String[] values = (String[])dictionary.get("blacklistBundleSymbolicNames");

			System.out.println("^^^^^^" + Arrays.toString(values));
		}

		for (File file : new File(PropsValues.MODULE_FRAMEWORK_CONFIGS_DIR).listFiles()) {
			System.out.println("#####Config file : " + file.getAbsolutePath() + "\ncontent : " + new String(Files.readAllBytes(file.toPath())));
		}

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("***********Stopping " + context.getBundle());
	}

}

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

package com.liferay.portal.lang.internal.activator;

import com.liferay.portal.kernel.language.UTF8Control;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;

import java.net.URL;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Preston Crary
 */
public class PortalLangBundleActivator implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) {
		Bundle bundle = bundleContext.getBundle();

		Enumeration<URL> enumeration = bundle.findEntries(
			"content", "Language_*.properties", false);

		while (enumeration.hasMoreElements()) {
			URL url = enumeration.nextElement();

			String path = url.getPath();

			String languageId = path.substring(
				"/content/Language_".length(),
				path.length() - ".properties".length());

			Locale locale = LocaleUtil.fromLanguageId(languageId, false);

			ResourceBundle resourceBundle = ResourceBundle.getBundle(
				"content.Language", locale,
				PortalLangBundleActivator.class.getClassLoader(),
				UTF8Control.INSTANCE);

			ServiceRegistration<?> serviceRegistration =
				bundleContext.registerService(
					ResourceBundle.class, resourceBundle,
					MapUtil.singletonDictionary("language.id", languageId));

			_serviceRegistrations.add(serviceRegistration);
		}
	}

	@Override
	public void stop(BundleContext bundleContext) {
		for (ServiceRegistration<?> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	private final List<ServiceRegistration<?>> _serviceRegistrations =
		new ArrayList<>();

}
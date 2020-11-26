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

package com.liferay.portal.security.auth.verifier.internal;

import com.liferay.portal.kernel.security.auth.verifier.AuthVerifier;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierConfiguration;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.security.auth.AuthVerifierPipeline;

import java.util.Map;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Tomas Polesovsky
 * @author Arthur Chan
 */
public abstract class BaseAuthVerifierPipelineConfigurator {

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		Boolean enabled = GetterUtil.getBoolean(properties.get("enabled"));

		if ((enabled == null) || !enabled) {
			return;
		}

		Class<?> clazz = getAuthVerifierClass();

		String authVerifierPropertyName =
			AuthVerifierPipeline.getAuthVerifierPropertyName(clazz.getName());

		Properties translatedProperties = new Properties();

		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			translatedProperties.setProperty(
				translateKey(authVerifierPropertyName, entry.getKey()),
				String.valueOf(entry.getValue()));
		}

		_authVerifierConfiguration = new AuthVerifierConfiguration();

		_authVerifierConfiguration.setAuthVerifierClassName(clazz.getName());
		_authVerifierConfiguration.setProperties(translatedProperties);

		_serviceRegistration = bundleContext.registerService(
			AuthVerifierConfiguration.class, _authVerifierConfiguration,
			new HashMapDictionary<>());
	}

	@Deactivate
	protected void deactivate() {
		_serviceRegistration.unregister();
	}

	protected abstract Class<? extends AuthVerifier> getAuthVerifierClass();

	@Modified
	protected void modified(
		BundleContext bundleContext, Map<String, Object> properties) {

		deactivate();

		activate(bundleContext, properties);
	}

	protected String translateKey(String authVerifierPropertyName, String key) {
		if (key.equals("hostsAllowed")) {
			key = "hosts.allowed";
		}
		else if (key.equals("urlsExcludes")) {
			key = "urls.excludes";
		}
		else if (key.equals("urlsIncludes")) {
			key = "urls.includes";
		}

		return key;
	}

	private AuthVerifierConfiguration _authVerifierConfiguration;
	private ServiceRegistration<AuthVerifierConfiguration> _serviceRegistration;

}
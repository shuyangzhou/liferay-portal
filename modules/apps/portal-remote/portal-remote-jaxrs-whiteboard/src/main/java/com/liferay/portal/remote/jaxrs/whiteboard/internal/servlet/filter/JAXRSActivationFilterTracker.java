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

package com.liferay.portal.remote.jaxrs.whiteboard.internal.servlet.filter;

import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.MapUtil;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.servlet.Filter;
import javax.servlet.ServletException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.http.runtime.HttpServiceRuntime;

/**
 * @author Shuyang Zhou
 */
@Component(service = {})
public class JAXRSActivationFilterTracker {

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_countDownLatch = new CountDownLatch(1);

		_filterServiceRegistration = bundleContext.registerService(
			Filter.class, new JAXRSActivationFilter(bundleContext, this),
			HashMapDictionaryBuilder.<String, Object>put(
				"dispatcher", new String[] {"FORWARD", "REQUEST"}
			).put(
				"servlet-context-name", ""
			).put(
				"servlet-filter-name", "Activation Filter"
			).put(
				"url-pattern", "/o/*"
			).build());

		_countDownLatch.countDown();
	}

	@Deactivate
	protected synchronized void deactivate() {
		_unregister();

		if (_readyServiceRegistration != null) {
			_readyServiceRegistration.unregister();

			_readyServiceRegistration = null;
		}

		if (_httpServiceRuntimeServiceRegistration != null) {
			_httpServiceRuntimeServiceRegistration.unregister();

			_httpServiceRuntimeServiceRegistration = null;
		}
	}

	protected synchronized void setReady() throws ServletException {
		try {
			_countDownLatch.await();
		}
		catch (InterruptedException interruptedException) {
			throw new ServletException(interruptedException);
		}

		if (_readyServiceRegistration == null) {
			ServiceReference<HttpServiceRuntime> serviceReference =
				_bundleContext.getServiceReference(HttpServiceRuntime.class);

			Dictionary<String, Object> dictionary = new HashMapDictionary<>();

			for (String propertyKey : serviceReference.getPropertyKeys()) {
				if (_frameworkKeys.contains(propertyKey)) {
					continue;
				}

				dictionary.put(
					propertyKey, serviceReference.getProperty(propertyKey));
			}

			dictionary.put("liferay.jaxrs.whiteboard.ready", true);

			_httpServiceRuntimeServiceRegistration =
				_bundleContext.registerService(
					HttpServiceRuntime.class,
					_bundleContext.getService(serviceReference), dictionary);
			_readyServiceRegistration = _bundleContext.registerService(
				Object.class, new Object(),
				MapUtil.singletonDictionary(
					"liferay.jaxrs.whiteboard.ready", true));

			_unregister();
		}
	}

	private void _unregister() {
		if (_filterServiceRegistration != null) {
			_filterServiceRegistration.unregister();

			_filterServiceRegistration = null;
		}
	}

	private static final Set<String> _frameworkKeys = new HashSet<>(
		Arrays.asList(
			Constants.OBJECTCLASS, Constants.SERVICE_BUNDLEID,
			Constants.SERVICE_ID, Constants.SERVICE_SCOPE));

	private BundleContext _bundleContext;
	private CountDownLatch _countDownLatch;
	private ServiceRegistration<Filter> _filterServiceRegistration;
	private ServiceRegistration<?> _httpServiceRuntimeServiceRegistration;
	private ServiceRegistration<?> _readyServiceRegistration;

}
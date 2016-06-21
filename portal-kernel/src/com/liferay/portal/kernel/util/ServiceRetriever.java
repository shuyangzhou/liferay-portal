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

package com.liferay.portal.kernel.util;

import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

/**
 * @author Tina Tian
 */
public class ServiceRetriever<T> implements ServiceTrackerCustomizer<T, T> {

	public ServiceRetriever(Class<T> clazz) {
		_dummyService = ProxyFactory.newDummyInstance(clazz);

		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(clazz, this);

		_serviceTracker.open();
	}

	@Override
	public T addingService(ServiceReference<T> serviceReference) {
		Registry registry = RegistryUtil.getRegistry();

		T service = registry.getService(serviceReference);

		_service = service;

		return service;
	}

	public void close() {
		if (_serviceTracker != null) {
			_serviceTracker.close();
		}
	}

	public T getService() {
		if (_service != null) {
			return _service;
		}

		return _dummyService;
	}

	@Override
	public void modifiedService(
		ServiceReference<T> serviceReference, T service) {

		_service = service;
	}

	@Override
	public void removedService(
		ServiceReference<T> serviceReference, T service) {

		_service = null;
	}

	private final T _dummyService;
	private volatile T _service;
	private final ServiceTracker<T, T> _serviceTracker;

}
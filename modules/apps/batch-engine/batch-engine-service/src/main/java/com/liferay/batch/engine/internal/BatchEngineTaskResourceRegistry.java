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

package com.liferay.batch.engine.internal;

import com.liferay.batch.engine.BatchEngineTaskMethod;
import com.liferay.batch.engine.BatchEngineTaskOperation;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;

import java.lang.reflect.Method;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Shuyang Zhou
 * @author Ivica Cardic
 */
@Component(service = BatchEngineTaskResourceRegistry.class)
public class BatchEngineTaskResourceRegistry {

	@Activate
	public void activate(BundleContext bundleContext) throws Exception {
		_serviceTracker = new ServiceTracker<>(
			bundleContext,
			bundleContext.createFilter(
				"(&(api.version=*)(osgi.jaxrs.resource=true))"),
			new ItemClassServiceTrackerCustomizer(bundleContext));

		_serviceTracker.open();
	}

	@Deactivate
	public void deactivate(BundleContext bundleContext) {
		_serviceTracker.close();
	}

	public ServiceReference<Object> getServiceReference(
		BatchEngineTaskOperation batchEngineTaskOperation, String itemClassName,
		String version) {

		return _resourceServiceReferenceMap.get(
			StringBundler.concat(
				batchEngineTaskOperation, StringPool.POUND, itemClassName,
				StringPool.POUND, version));
	}

	private final Map<String, ServiceReference<Object>>
		_resourceServiceReferenceMap = new ConcurrentHashMap<>();
	private ServiceTracker<Object, Object> _serviceTracker;

	private class ItemClassServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<Object, Object> {

		@Override
		public Object addingService(ServiceReference<Object> serviceReference) {
			Object resource = _bundleContext.getService(serviceReference);

			Class<?> resourceClass = resource.getClass();

			for (Method resourceMethod : resourceClass.getMethods()) {
				BatchEngineTaskMethod batchEngineTaskMethod =
					resourceMethod.getAnnotation(BatchEngineTaskMethod.class);

				if (batchEngineTaskMethod == null) {
					continue;
				}

				Class<?> itemClass = batchEngineTaskMethod.itemClass();

				_resourceServiceReferenceMap.put(
					StringBundler.concat(
						batchEngineTaskMethod.batchEngineTaskOperation(),
						StringPool.POUND, itemClass.getName(), StringPool.POUND,
						serviceReference.getProperty("api.version")),
					serviceReference);
			}

			return resource;
		}

		@Override
		public void modifiedService(
			ServiceReference<Object> serviceReference, Object resource) {
		}

		@Override
		public void removedService(
			ServiceReference<Object> serviceReference, Object resource) {

			for (Map.Entry<String, ServiceReference<Object>> entry :
					_resourceServiceReferenceMap.entrySet()) {

				if (!Objects.equals(serviceReference, entry.getValue())) {
					continue;
				}

				_resourceServiceReferenceMap.remove(entry.getKey());
			}
		}

		private ItemClassServiceTrackerCustomizer(BundleContext bundleContext) {
			_bundleContext = bundleContext;
		}

		private final BundleContext _bundleContext;

	}

}
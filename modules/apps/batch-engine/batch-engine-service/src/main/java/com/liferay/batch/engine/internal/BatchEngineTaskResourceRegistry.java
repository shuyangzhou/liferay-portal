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

import com.liferay.batch.engine.BatchEngineTaskField;
import com.liferay.batch.engine.BatchEngineTaskMethod;
import com.liferay.batch.engine.BatchEngineTaskOperation;
import com.liferay.portal.kernel.exception.SystemException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import javax.ws.rs.PathParam;

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

	public Class<?> getItemClass(String itemClasName) {
		Stream<Class<?>> itemClassStream = _itemClasses.stream();

		return itemClassStream.filter(
			itemClass -> Objects.equals(itemClass.getName(), itemClasName)
		).findFirst(
		).orElse(
			null
		);
	}

	public ResourceMethodServiceReferenceTuple
		getResourceServiceReferenceMethodTuple(
			BatchEngineTaskOperation batchEngineTaskOperation,
			String itemClassName) {

		for (Map.Entry
				<BatchEngineTaskMethod, ResourceMethodServiceReferenceTuple>
					entry :
						_resourceServiceReferenceMethodTupleMap.entrySet()) {

			BatchEngineTaskMethod batchEngineTaskMethod = entry.getKey();

			Class<?> itemClass = batchEngineTaskMethod.itemClass();

			if ((batchEngineTaskMethod.batchEngineTaskOperation() ==
					batchEngineTaskOperation) &&
				Objects.equals(itemClass.getName(), itemClassName)) {

				return entry.getValue();
			}
		}

		return null;
	}

	public boolean isResourceMethodRegistered(
		BatchEngineTaskOperation batchEngineTaskOperation,
		String itemClassName) {

		Set<BatchEngineTaskMethod> batchEngineTaskMethods =
			_resourceServiceReferenceMethodTupleMap.keySet();

		Stream<BatchEngineTaskMethod> batchEngineTaskMethodsStream =
			batchEngineTaskMethods.stream();

		return batchEngineTaskMethodsStream.anyMatch(
			batchEngineTaskMethod -> {
				Class<?> itemClass = batchEngineTaskMethod.itemClass();

				if ((batchEngineTaskMethod.batchEngineTaskOperation() ==
						batchEngineTaskOperation) &&
					Objects.equals(itemClass.getName(), itemClassName)) {

					return true;
				}

				return false;
			});
	}

	private final Set<Class<?>> _itemClasses = Collections.synchronizedSet(
		new HashSet<>());
	private final Map
		<BatchEngineTaskMethod, ResourceMethodServiceReferenceTuple>
			_resourceServiceReferenceMethodTupleMap = new ConcurrentHashMap<>();
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

				_itemClasses.add(batchEngineTaskMethod.itemClass());

				try {
					Class<?> resourceSuperclass = resourceClass.getSuperclass();

					ResourceMethodServiceReferenceTuple
						resourceMethodServiceReferenceTuple =
							new ResourceMethodServiceReferenceTuple(
								resourceMethod,
								_getMethodParameterNames(
									resourceSuperclass.getMethod(
										resourceMethod.getName(),
										resourceMethod.getParameterTypes()),
									resourceMethod),
								serviceReference);

					_resourceServiceReferenceMethodTupleMap.put(
						batchEngineTaskMethod,
						resourceMethodServiceReferenceTuple);
				}
				catch (NoSuchMethodException nsme) {
					throw new SystemException(nsme);
				}
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

			Set<Class> removedItemClasses = new HashSet<>();

			for (Map.Entry
					<BatchEngineTaskMethod, ResourceMethodServiceReferenceTuple>
						entry :
							_resourceServiceReferenceMethodTupleMap.
								entrySet()) {

				ResourceMethodServiceReferenceTuple
					resourceMethodServiceReferenceTuple = entry.getValue();

				if (!Objects.equals(
						serviceReference,
						resourceMethodServiceReferenceTuple.
							getServiceReference())) {

					continue;
				}

				BatchEngineTaskMethod batchEngineTaskMethod = entry.getKey();

				removedItemClasses.add(batchEngineTaskMethod.itemClass());

				_resourceServiceReferenceMethodTupleMap.remove(
					batchEngineTaskMethod);
			}

			for (Class itemClass : removedItemClasses) {
				boolean remove = true;

				for (BatchEngineTaskMethod batchEngineTaskMethod :
						_resourceServiceReferenceMethodTupleMap.keySet()) {

					if (batchEngineTaskMethod.itemClass() == itemClass) {
						remove = false;

						break;
					}
				}

				if (remove) {
					_itemClasses.remove(itemClass);
				}
			}
		}

		private ItemClassServiceTrackerCustomizer(BundleContext bundleContext) {
			_bundleContext = bundleContext;
		}

		private String[] _getMethodParameterNames(
			Method parentResourceMethod, Method resourceMethod) {

			Parameter[] parentResourceMethodParameters =
				parentResourceMethod.getParameters();

			Parameter[] resourceMethodParameters =
				resourceMethod.getParameters();

			String[] parameterNames =
				new String[resourceMethodParameters.length];

			for (int i = 0; i < resourceMethodParameters.length; i++) {
				Parameter parameter = resourceMethodParameters[i];

				BatchEngineTaskField batchEngineTaskField =
					parameter.getAnnotation(BatchEngineTaskField.class);

				if (batchEngineTaskField == null) {
					parameter = parentResourceMethodParameters[i];

					PathParam pathParam = parameter.getAnnotation(
						PathParam.class);

					if (pathParam != null) {
						parameterNames[i] = pathParam.value();
					}
				}
				else {
					parameterNames[i] = batchEngineTaskField.value();
				}
			}

			return parameterNames;
		}

		private final BundleContext _bundleContext;

	}

}
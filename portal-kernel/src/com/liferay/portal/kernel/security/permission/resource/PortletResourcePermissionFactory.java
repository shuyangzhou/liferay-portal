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

package com.liferay.portal.kernel.security.permission.resource;

import com.liferay.portal.kernel.internal.security.permission.resource.DefaultPortletResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.definition.PortletResourcePermissionDefinition;
import com.liferay.portal.kernel.service.BaseService;
import com.liferay.portal.kernel.util.ServiceProxyFactory;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceRegistration;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Preston Crary
 */
public class PortletResourcePermissionFactory {

	public static PortletResourcePermission create(
		String resourceName,
		PortletResourcePermissionLogic... portletResourcePermissionLogics) {

		return new DefaultPortletResourcePermission(
			resourceName, portletResourcePermissionLogics);
	}

	public static PortletResourcePermission getInstance(
		Class<? extends BaseService> declaringServiceClass, String fieldName,
		String resourceName) {

		return ServiceProxyFactory.newServiceTrackedInstance(
			PortletResourcePermission.class, declaringServiceClass, fieldName,
			"(resource.name=" + resourceName + ")", true);
	}

	private static final Map<String, ServiceRegistration<?>>
		_serviceRegistrations = new ConcurrentHashMap<>();
	private static final ServiceTracker
		<PortletResourcePermissionDefinition,
			PortletResourcePermissionDefinition> _serviceTracker;

	private static class
		PortletResourcePermissionDefinitionServiceTrackerCustomizer
			implements ServiceTrackerCustomizer
				<PortletResourcePermissionDefinition,
					PortletResourcePermissionDefinition> {

		@Override
		public PortletResourcePermissionDefinition addingService(
			ServiceReference<PortletResourcePermissionDefinition>
				serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			PortletResourcePermissionDefinition
				portletResourcePermissionDefinition = registry.getService(
					serviceReference);

			PortletResourcePermission portletResourcePermission =
				new DefaultPortletResourcePermission(
					portletResourcePermissionDefinition.getResourceName(),
					portletResourcePermissionDefinition.
						getPortletResourcePermissionLogics());

			Map<String, Object> properties = new HashMap<>();

			String resourceName =
				portletResourcePermissionDefinition.getResourceName();

			properties.put("resource.name", resourceName);

			Object serviceRanking = serviceReference.getProperty(
				"service.ranking");

			if (serviceRanking != null) {
				properties.put("service.ranking", serviceRanking);
			}

			ServiceRegistration<PortletResourcePermission> serviceRegistration =
				registry.registerService(
					PortletResourcePermission.class, portletResourcePermission,
					properties);

			Class<?> clazz = portletResourcePermissionDefinition.getClass();

			_serviceRegistrations.put(clazz.getName(), serviceRegistration);

			return portletResourcePermissionDefinition;
		}

		@Override
		public void modifiedService(
			ServiceReference<PortletResourcePermissionDefinition>
				serviceReference,
			PortletResourcePermissionDefinition
				portletResourcePermissionDefinition) {
		}

		@Override
		public void removedService(
			ServiceReference<PortletResourcePermissionDefinition>
				serviceReference,
			PortletResourcePermissionDefinition
				portletResourcePermissionDefinition) {

			Class<?> clazz = portletResourcePermissionDefinition.getClass();

			ServiceRegistration<?> serviceRegistration =
				_serviceRegistrations.remove(clazz.getName());

			if (serviceRegistration != null) {
				serviceRegistration.unregister();
			}

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);
		}

	}

	static {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			PortletResourcePermissionDefinition.class,
			new PortletResourcePermissionDefinitionServiceTrackerCustomizer());

		_serviceTracker.open();
	}

}
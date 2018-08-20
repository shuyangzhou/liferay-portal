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

package com.liferay.portal.kernel.cache;

import com.liferay.petra.memory.FinalizeAction;
import com.liferay.petra.memory.FinalizeManager;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.io.Serializable;

import java.lang.ref.Reference;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tina Tian
 */
public class PortalCacheManagerProvider<K extends Serializable, V> {

	public static PortalCacheManager<? extends Serializable, ?>
		getPortalCacheManager(String portalCacheManagerName) {

		return _instance._getDynamicPortalCacheManager(portalCacheManagerName);
	}

	public static Collection<PortalCacheManager<? extends Serializable, ?>>
		getPortalCacheManagers() {

		return (Collection<PortalCacheManager<? extends Serializable, ?>>)
			(Collection<?>)_instance._getDynamicPortalCacheManagers();
	}

	private PortalCacheManagerProvider() {
		_dynamicPortalCacheManagers = new ConcurrentHashMap<>();

		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			(Class<PortalCacheManager<K, V>>)(Class<?>)PortalCacheManager.class,
			new PortalCacheProviderServiceTrackerCustomizer());

		_serviceTracker.open();

		FinalizeManager.register(
			this,
			new FinalizeAction() {

				@Override
				public void doFinalize(Reference<?> reference) {
					_serviceTracker.close();

					for (DynamicPortalCacheManager<K, V>
							dynamicPortalCacheManager :
								_dynamicPortalCacheManagers.values()) {

						dynamicPortalCacheManager.destroy();
					}

					_dynamicPortalCacheManagers.clear();
				}

			},
			FinalizeManager.PHANTOM_REFERENCE_FACTORY);
	}

	private DynamicPortalCacheManager<K, V> _getDynamicPortalCacheManager(
		String portalCacheManagerName) {

		return _dynamicPortalCacheManagers.computeIfAbsent(
			portalCacheManagerName,
			key -> new DynamicPortalCacheManager<>(key));
	}

	private Collection<DynamicPortalCacheManager<K, V>>
		_getDynamicPortalCacheManagers() {

		return Collections.unmodifiableCollection(
			_dynamicPortalCacheManagers.values());
	}

	private static final PortalCacheManagerProvider<? extends Serializable, ?>
		_instance = new PortalCacheManagerProvider<>();

	private final Map<String, DynamicPortalCacheManager<K, V>>
		_dynamicPortalCacheManagers;
	private final ServiceTracker
		<PortalCacheManager<K, V>, DynamicPortalCacheManager<K, V>>
			_serviceTracker;

	private class PortalCacheProviderServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<PortalCacheManager<K, V>, DynamicPortalCacheManager<K, V>> {

		@Override
		public DynamicPortalCacheManager<K, V> addingService(
			ServiceReference<PortalCacheManager<K, V>> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			PortalCacheManager<K, V> portalCacheManager = registry.getService(
				serviceReference);

			DynamicPortalCacheManager<K, V> dynamicPortalCacheManager =
				_getDynamicPortalCacheManager(
					portalCacheManager.getPortalCacheManagerName());

			dynamicPortalCacheManager.setPortalCacheManager(portalCacheManager);

			return dynamicPortalCacheManager;
		}

		@Override
		public void modifiedService(
			ServiceReference<PortalCacheManager<K, V>> serviceReference,
			DynamicPortalCacheManager<K, V> dynamicPortalCacheManager) {
		}

		@Override
		public void removedService(
			ServiceReference<PortalCacheManager<K, V>> serviceReference,
			DynamicPortalCacheManager<K, V> dynamicPortalCacheManager) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			dynamicPortalCacheManager.setPortalCacheManager(null);
		}

	}

}
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

import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRankingUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.io.Serializable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tina Tian
 */
public class PortalCacheManagerProvider {

	public static PortalCacheManager<? extends Serializable, ?>
		getPortalCacheManager(String portalCacheManagerName) {

		return _dynamicPortalCacheManagers.computeIfAbsent(
			portalCacheManagerName,
			key -> new DynamicPortalCacheManager<>(key));
	}

	public static Collection<PortalCacheManager<? extends Serializable, ?>>
		getPortalCacheManagers() {

		return Collections.unmodifiableCollection(
			_dynamicPortalCacheManagers.values());
	}

	private static final Map
		<String, DynamicPortalCacheManager<? extends Serializable, ?>>
			_dynamicPortalCacheManagers = new ConcurrentHashMap<>();
	private static final ServiceTracker
		<PortalCacheManager<? extends Serializable, ?>,
			PortalCacheManager<? extends Serializable, ?>> _serviceTracker;
	private static final Map
		<ServiceReference<PortalCacheManager<? extends Serializable, ?>>,
			PortalCacheManager<? extends Serializable, ?>>
				_trackedServices = new ConcurrentHashMap<>();

	static {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			(Class<PortalCacheManager<? extends Serializable, ?>>)(Class<?>)
				PortalCacheManager.class,
			new PortalCacheProviderServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	private static class PortalCacheProviderServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<PortalCacheManager<? extends Serializable, ?>,
				PortalCacheManager<? extends Serializable, ?>> {

		@Override
		public PortalCacheManager<? extends Serializable, ?> addingService(
			ServiceReference<PortalCacheManager<? extends Serializable, ?>>
				serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			PortalCacheManager<? extends Serializable, ?> portalCacheManager =
				registry.getService(serviceReference);

			_trackedServices.put(serviceReference, portalCacheManager);

			_updateDynamicPortalCacheManager(
				portalCacheManager.getPortalCacheManagerName());

			return portalCacheManager;
		}

		@Override
		public void modifiedService(
			ServiceReference<PortalCacheManager<? extends Serializable, ?>>
				serviceReference,
			PortalCacheManager<? extends Serializable, ?> portalCacheManager) {

			_updateDynamicPortalCacheManager(
				portalCacheManager.getPortalCacheManagerName());
		}

		@Override
		public void removedService(
			ServiceReference<PortalCacheManager<? extends Serializable, ?>>
				serviceReference,
			PortalCacheManager<? extends Serializable, ?> portalCacheManager) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			if (_trackedServices.remove(serviceReference, portalCacheManager)) {
				_updateDynamicPortalCacheManager(
					portalCacheManager.getPortalCacheManagerName());
			}
		}

		private void _updateDynamicPortalCacheManager(
			String portalCacheManagerName) {

			Optional
				<Map.Entry
					<ServiceReference
						<PortalCacheManager<? extends Serializable, ?>>,
							PortalCacheManager<? extends Serializable, ?>>>
								optionalEntry =
									ServiceRankingUtil.getHighestRankingEntry(
										_trackedServices);

			Optional<PortalCacheManager<? extends Serializable, ?>>
				optionalService = optionalEntry.map(Map.Entry::getValue);

			DynamicPortalCacheManager<? extends Serializable, ?>
				dynamicPortalCacheManager =
					_dynamicPortalCacheManagers.computeIfAbsent(
						portalCacheManagerName,
						key -> new DynamicPortalCacheManager<>(key));

			dynamicPortalCacheManager.setPortalCacheManager(
				optionalService.orElse(null));
		}

	}

}
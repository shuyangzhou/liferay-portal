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

package com.liferay.apio.architect.wiring.osgi.internal.manager.router;

import static com.liferay.apio.architect.alias.ProvideFunction.curry;
import static com.liferay.apio.architect.unsafe.Unsafe.unsafeCast;
import static com.liferay.apio.architect.wiring.osgi.internal.manager.ManagerCache.INSTANCE;

import com.liferay.apio.architect.logger.ApioLogger;
import com.liferay.apio.architect.router.CollectionRouter;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.routes.CollectionRoutes.Builder;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.unsafe.Unsafe;
import com.liferay.apio.architect.wiring.osgi.internal.manager.base.SimpleBaseManager;
import com.liferay.apio.architect.wiring.osgi.manager.ProviderManager;
import com.liferay.apio.architect.wiring.osgi.manager.representable.NameManager;
import com.liferay.apio.architect.wiring.osgi.manager.router.CollectionRouterManager;
import com.liferay.apio.architect.wiring.osgi.manager.router.ItemRouterManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Hernández
 */
@Component(immediate = true)
public class CollectionRouterManagerImpl
	extends SimpleBaseManager<CollectionRouter>
	implements CollectionRouterManager {

	public CollectionRouterManagerImpl() {
		super(CollectionRouter.class);
	}

	@Override
	public <T> Optional<CollectionRoutes<T>> getCollectionRoutesOptional(
		String name) {

		if (!INSTANCE.hasCollectionRoutes()) {
			_generateCollectionRoutes();
		}

		Optional<Map<String, CollectionRoutes>> optional =
			INSTANCE.getCollectionRoutesOptional();

		return optional.map(
			map -> map.get(name)
		).map(
			Unsafe::unsafeCast
		);
	}

	@Override
	public Integer getPrincipalTypeParamPosition() {
		return 1;
	}

	@Override
	public List<String> getResourceNames() {
		if (!INSTANCE.hasRootResourceNames()) {
			_generateCollectionRoutes();
		}

		Optional<List<String>> optional =
			INSTANCE.getRootResourceNamesOptional();

		return optional.orElseGet(Collections::emptyList);
	}

	private void _generateCollectionRoutes() {
		Stream<String> stream = getKeyStream();

		List<String> resourceNames = new ArrayList<>();

		Map<String, CollectionRoutes> collectionRoutes = new HashMap<>();

		stream.forEach(
			className -> {
				Optional<String> nameOptional = _nameManager.getNameOptional(
					className);

				if (!nameOptional.isPresent()) {
					_apioLogger.warning(
						"Could not found a Representable for classname " +
							className);

					return;
				}

				String name = nameOptional.get();

				CollectionRouter<Object, ?> collectionRouter = unsafeCast(
					serviceTrackerMap.getService(className));

				Set<String> neededProviders = new TreeSet<>();

				Builder<Object> builder = new Builder<>(
					name, curry(_providerManager::provideMandatory),
					neededProviders::add);

				List<String> missingProviders =
					_providerManager.getMissingProviders(neededProviders);

				if (!missingProviders.isEmpty()) {
					_apioLogger.warning(
						"Missing providers for classes: " + missingProviders);

					return;
				}

				Optional<ItemRoutes<Object, Object>> optional =
					_itemRouterManager.getItemRoutesOptional(name);

				if (!optional.isPresent()) {
					_apioLogger.warning(
						"Missing item router for resource with name " + name);

					return;
				}

				resourceNames.add(name);
				collectionRoutes.put(
					name, collectionRouter.collectionRoutes(builder));
			});

		INSTANCE.setRootResourceNames(resourceNames);
		INSTANCE.setCollectionRoutes(collectionRoutes);
	}

	@Reference
	private ApioLogger _apioLogger;

	@Reference
	private ItemRouterManager _itemRouterManager;

	@Reference
	private NameManager _nameManager;

	@Reference
	private ProviderManager _providerManager;

}
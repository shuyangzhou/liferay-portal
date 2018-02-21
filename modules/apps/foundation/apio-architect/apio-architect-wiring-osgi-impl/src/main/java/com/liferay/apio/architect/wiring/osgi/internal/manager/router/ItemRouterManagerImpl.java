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
import com.liferay.apio.architect.router.ItemRouter;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.ItemRoutes.Builder;
import com.liferay.apio.architect.unsafe.Unsafe;
import com.liferay.apio.architect.wiring.osgi.internal.manager.base.SimpleBaseManager;
import com.liferay.apio.architect.wiring.osgi.manager.PathIdentifierMapperManager;
import com.liferay.apio.architect.wiring.osgi.manager.ProviderManager;
import com.liferay.apio.architect.wiring.osgi.manager.representable.NameManager;
import com.liferay.apio.architect.wiring.osgi.manager.router.ItemRouterManager;

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
public class ItemRouterManagerImpl
	extends SimpleBaseManager<ItemRouter> implements ItemRouterManager {

	public ItemRouterManagerImpl() {
		super(ItemRouter.class);
	}

	@Override
	public <T, S> Optional<ItemRoutes<T, S>> getItemRoutesOptional(
		String name) {

		if (!INSTANCE.hasItemRoutes()) {
			_generateItemRoutes();
		}

		Optional<Map<String, ItemRoutes>> optional =
			INSTANCE.getItemRoutesOptional();

		return optional.map(
			map -> map.get(name)
		).map(
			Unsafe::unsafeCast
		);
	}

	private void _generateItemRoutes() {
		Stream<String> stream = getKeyStream();

		Map<String, ItemRoutes> itemRoutes = new HashMap<>();

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

				ItemRouter<Object, Object, ?> itemRouter = unsafeCast(
					serviceTrackerMap.getService(className));

				Set<String> neededProviders = new TreeSet<>();

				Builder<Object, Object> builder = new Builder<>(
					name, curry(_providerManager::provideMandatory),
					neededProviders::add);

				List<String> missingProviders =
					_providerManager.getMissingProviders(neededProviders);

				if (!missingProviders.isEmpty()) {
					_apioLogger.warning(
						"Missing providers for classes: " + missingProviders);

					return;
				}

				boolean hasPathIdentifierMapper =
					_pathIdentifierMapperManager.hasPathIdentifierMapper(name);

				if (!hasPathIdentifierMapper) {
					_apioLogger.warning(
						"Missing path identifier mapper for resource with " +
							"name " + name);

					return;
				}

				itemRoutes.put(name, itemRouter.itemRoutes(builder));
			});

		INSTANCE.setItemRoutes(itemRoutes);
	}

	@Reference
	private ApioLogger _apioLogger;

	@Reference
	private NameManager _nameManager;

	@Reference
	private PathIdentifierMapperManager _pathIdentifierMapperManager;

	@Reference
	private ProviderManager _providerManager;

}
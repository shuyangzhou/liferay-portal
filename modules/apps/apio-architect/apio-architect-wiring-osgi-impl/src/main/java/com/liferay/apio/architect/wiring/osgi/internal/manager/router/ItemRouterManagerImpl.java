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
import static com.liferay.apio.architect.wiring.osgi.internal.manager.cache.ManagerCache.INSTANCE;

import com.liferay.apio.architect.router.ItemRouter;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.ItemRoutes.Builder;
import com.liferay.apio.architect.wiring.osgi.internal.manager.base.ClassNameBaseManager;
import com.liferay.apio.architect.wiring.osgi.manager.PathIdentifierMapperManager;
import com.liferay.apio.architect.wiring.osgi.manager.ProviderManager;
import com.liferay.apio.architect.wiring.osgi.manager.representable.NameManager;
import com.liferay.apio.architect.wiring.osgi.manager.router.ItemRouterManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Hernández
 */
@Component
public class ItemRouterManagerImpl
	extends ClassNameBaseManager<ItemRouter> implements ItemRouterManager {

	public ItemRouterManagerImpl() {
		super(ItemRouter.class, 2);
	}

	@Override
	public Map<String, ItemRoutes> getItemRoutes() {
		return INSTANCE.getItemRoutesMap(this::_computeItemRoutes);
	}

	@Override
	public <T, S> Optional<ItemRoutes<T, S>> getItemRoutesOptional(
		String name) {

		return INSTANCE.getItemRoutesOptional(name, this::_computeItemRoutes);
	}

	private void _computeItemRoutes() {
		forEachService(
			(className, itemRouter) -> {
				Optional<String> nameOptional = _nameManager.getNameOptional(
					className);

				if (!nameOptional.isPresent()) {
					warning(
						"Unable to find a Representable for class name " +
							className);

					return;
				}

				String name = nameOptional.get();

				Set<String> neededProviders = new TreeSet<>();

				Builder builder = new Builder<>(
					name, curry(_providerManager::provideMandatory),
					neededProviders::add);

				@SuppressWarnings("unchecked")
				ItemRoutes itemRoutes = itemRouter.itemRoutes(builder);

				List<String> missingProviders =
					_providerManager.getMissingProviders(neededProviders);

				if (!missingProviders.isEmpty()) {
					warning(
						"Missing providers for classes: " + missingProviders);

					return;
				}

				boolean hasPathIdentifierMapper =
					_pathIdentifierMapperManager.hasPathIdentifierMapper(name);

				if (!hasPathIdentifierMapper) {
					warning(
						"Missing path identifier mapper for resource with " +
							"name " + name);

					return;
				}

				INSTANCE.putItemRoutes(name, itemRoutes);
			});
	}

	@Reference
	private NameManager _nameManager;

	@Reference
	private PathIdentifierMapperManager _pathIdentifierMapperManager;

	@Reference
	private ProviderManager _providerManager;

}
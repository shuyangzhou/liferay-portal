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

package com.liferay.apio.architect.internal.wiring.osgi.manager.router;

import static com.liferay.apio.architect.internal.alias.ProvideFunction.curry;
import static com.liferay.apio.architect.internal.wiring.osgi.manager.cache.ManagerCache.INSTANCE;

import static org.slf4j.LoggerFactory.getLogger;

import com.liferay.apio.architect.internal.routes.NestedCollectionRoutesImpl;
import com.liferay.apio.architect.internal.wiring.osgi.manager.base.ClassNameBaseManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.provider.ProviderManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.representable.NameManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.representable.RepresentableManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.uri.mapper.PathIdentifierMapperManager;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.router.ReusableNestedCollectionRouter;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes.Builder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.slf4j.Logger;

/**
 * @author Alejandro Hernández
 */
@Component(service = ReusableNestedCollectionRouterManager.class)
public class ReusableNestedCollectionRouterManagerImpl
	extends ClassNameBaseManager<ReusableNestedCollectionRouter>
	implements ReusableNestedCollectionRouterManager {

	public ReusableNestedCollectionRouterManagerImpl() {
		super(ReusableNestedCollectionRouter.class, 2);
	}

	@Override
	public Map<String, NestedCollectionRoutes> getReusableCollectionRoutes() {
		return INSTANCE.getReusableCollectionRoutesMap(
			this::_computeNestedCollectionRoutes);
	}

	@Override
	public Optional<NestedCollectionRoutes> getReusableCollectionRoutesOptional(
		String name) {

		return INSTANCE.getReusableNestedCollectionRoutesOptional(
			name, this::_computeNestedCollectionRoutes);
	}

	private void _computeNestedCollectionRoutes() {
		forEachService(
			(className, reusableNestedCollectionRouter) -> {
				Optional<String> nameOptional = _nameManager.getNameOptional(
					className);

				if (!nameOptional.isPresent()) {
					if (_logger.isWarnEnabled()) {
						_logger.warn(
							"Unable to find a Representable for class name " +
								className);
					}

					return;
				}

				String name = nameOptional.get();

				Set<String> neededProviders = new TreeSet<>();

				Optional<Representor<Object>> representorOptional =
					_representableManager.getRepresentorOptional(name);

				Representor<Object> representor = representorOptional.get();

				Builder builder = new NestedCollectionRoutesImpl.BuilderImpl<>(
					"r", name, curry(_providerManager::provideMandatory),
					neededProviders::add,
					path -> _pathIdentifierMapperManager.mapToIdentifierOrFail(
						path),
					identifier -> _pathIdentifierMapperManager.mapToPath(
						name, identifier, this),
					representor::getIdentifier);

				NestedCollectionRoutes nestedCollectionRoutes =
					reusableNestedCollectionRouter.collectionRoutes(builder);

				List<String> missingProviders =
					_providerManager.getMissingProviders(neededProviders);

				if (!missingProviders.isEmpty()) {
					if (_logger.isWarnEnabled()) {
						_logger.warn(
							"Missing providers for classes: " +
								missingProviders);
					}

					return;
				}

				Optional<ItemRoutes<Object, Object>> optional =
					_itemRouterManager.getItemRoutesOptional(name);

				if (!optional.isPresent()) {
					if (_logger.isWarnEnabled()) {
						_logger.warn(
							"Missing item router for resource with name " +
								name);
					}

					return;
				}

				INSTANCE.putReusableNestedCollectionRoutes(
					name, nestedCollectionRoutes);
			});
	}

	@Reference
	private ItemRouterManager _itemRouterManager;

	private Logger _logger = getLogger(getClass());

	@Reference
	private NameManager _nameManager;

	@Reference
	private PathIdentifierMapperManager _pathIdentifierMapperManager;

	@Reference
	private ProviderManager _providerManager;

	@Reference
	private RepresentableManager _representableManager;

}
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
import static com.liferay.apio.architect.wiring.osgi.internal.manager.TypeArgumentProperties.KEY_PARENT_IDENTIFIER_CLASS;
import static com.liferay.apio.architect.wiring.osgi.internal.manager.util.ManagerUtil.getGenericClassFromPropertyOrElse;
import static com.liferay.apio.architect.wiring.osgi.internal.manager.util.ManagerUtil.getTypeParamOrFail;

import com.liferay.apio.architect.logger.ApioLogger;
import com.liferay.apio.architect.router.NestedCollectionRouter;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes.Builder;
import com.liferay.apio.architect.unsafe.Unsafe;
import com.liferay.apio.architect.wiring.osgi.internal.manager.base.SimpleBaseManager;
import com.liferay.apio.architect.wiring.osgi.internal.service.reference.mapper.CustomServiceReferenceMapper;
import com.liferay.apio.architect.wiring.osgi.manager.ProviderManager;
import com.liferay.apio.architect.wiring.osgi.manager.representable.NameManager;
import com.liferay.apio.architect.wiring.osgi.manager.router.ItemRouterManager;
import com.liferay.apio.architect.wiring.osgi.manager.router.NestedCollectionRouterManager;
import com.liferay.osgi.service.tracker.collections.map.ServiceReferenceMapper.Emitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Hernández
 */
@Component(immediate = true)
public class NestedCollectionRouterManagerImpl
	extends SimpleBaseManager<NestedCollectionRouter>
	implements NestedCollectionRouterManager {

	public NestedCollectionRouterManagerImpl() {
		super(NestedCollectionRouter.class);
	}

	@Override
	public <T, S> Optional<NestedCollectionRoutes<T, S>>
		getNestedCollectionRoutesOptional(String name, String nestedName) {

		if (!INSTANCE.hasNestedCollectionRoutes()) {
			_generateNestedCollectionRoutes();
		}

		Optional<Map<String, NestedCollectionRoutes>> optional =
			INSTANCE.getNestedCollectionRoutesOptional();

		return optional.map(
			map -> map.get(name + "-" + nestedName)
		).map(
			Unsafe::unsafeCast
		);
	}

	@Override
	protected void emit(
		ServiceReference<NestedCollectionRouter> serviceReference,
		Emitter<String> emitter) {

		Bundle bundle = FrameworkUtil.getBundle(
			NestedCollectionRouterManagerImpl.class);

		BundleContext bundleContext = bundle.getBundleContext();

		CustomServiceReferenceMapper<NestedCollectionRouter>
			customServiceReferenceMapper = new CustomServiceReferenceMapper<>(
				bundleContext, NestedCollectionRouter.class, 1);

		NestedCollectionRouter nestedCollectionRouter =
			bundleContext.getService(serviceReference);

		Class<?> clazz = getGenericClassFromPropertyOrElse(
			serviceReference, KEY_PARENT_IDENTIFIER_CLASS,
			() -> getTypeParamOrFail(
				nestedCollectionRouter, NestedCollectionRouter.class, 3));

		customServiceReferenceMapper.map(
			serviceReference, key -> emitter.emit(clazz.getName() + "-" + key));
	}

	private void _generateNestedCollectionRoutes() {
		Stream<String> stream = getKeyStream();

		Map<String, NestedCollectionRoutes> nestedCollectionRoutes =
			new HashMap<>();

		stream.forEach(
			key -> {
				String[] classNames = key.split("-");

				if (classNames.length != 2) {
					return;
				}

				String parentClassName = classNames[0];
				String nestedClassName = classNames[1];

				Optional<String> nameOptional = _nameManager.getNameOptional(
					parentClassName);

				if (!nameOptional.isPresent()) {
					_apioLogger.warning(
						"Could not found a Representable for classname " +
							parentClassName);

					return;
				}

				String name = nameOptional.get();

				Optional<String> nestedNameOptional =
					_nameManager.getNameOptional(nestedClassName);

				if (!nestedNameOptional.isPresent()) {
					_apioLogger.warning(
						"Could not found a Representable for classname " +
							nestedClassName);

					return;
				}

				String nestedName = nestedNameOptional.get();

				NestedCollectionRouter<Object, ?, Object, ?>
					nestedCollectionRouter = unsafeCast(
						serviceTrackerMap.getService(key));

				Set<String> neededProviders = new TreeSet<>();

				Builder<Object, Object> builder = new Builder<>(
					name, nestedName, curry(_providerManager::provideMandatory),
					neededProviders::add);

				List<String> missingProviders =
					_providerManager.getMissingProviders(neededProviders);

				if (!missingProviders.isEmpty()) {
					_apioLogger.warning(
						"Missing providers for classes: " + missingProviders);

					return;
				}

				Optional<ItemRoutes<Object, Object>> nestedItemRoutes =
					_itemRouterManager.getItemRoutesOptional(nestedName);

				if (!nestedItemRoutes.isPresent()) {
					_apioLogger.warning(
						"Missing item router for resource with name " +
							nestedName);

					return;
				}

				Optional<ItemRoutes<Object, Object>> parentItemRoutes =
					_itemRouterManager.getItemRoutesOptional(name);

				if (!parentItemRoutes.isPresent()) {
					_apioLogger.warning(
						"Missing item router for resource with name " + name);

					return;
				}

				nestedCollectionRoutes.put(
					name + "-" + nestedName,
					nestedCollectionRouter.collectionRoutes(builder));
			});

		INSTANCE.setNestedCollectionRoutes(nestedCollectionRoutes);
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
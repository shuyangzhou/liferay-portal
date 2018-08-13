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

package com.liferay.apio.architect.impl.endpoint;

import static com.liferay.apio.architect.impl.endpoint.ExceptionSupplierUtil.notFound;

import com.liferay.apio.architect.documentation.APIDescription;
import com.liferay.apio.architect.documentation.APITitle;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.impl.documentation.Documentation;
import com.liferay.apio.architect.impl.entrypoint.EntryPoint;
import com.liferay.apio.architect.impl.url.ApplicationURL;
import com.liferay.apio.architect.impl.wiring.osgi.manager.provider.ProviderManager;
import com.liferay.apio.architect.impl.wiring.osgi.manager.representable.RepresentableManager;
import com.liferay.apio.architect.impl.wiring.osgi.manager.router.CollectionRouterManager;
import com.liferay.apio.architect.impl.wiring.osgi.manager.router.ItemRouterManager;
import com.liferay.apio.architect.impl.wiring.osgi.manager.router.NestedCollectionRouterManager;
import com.liferay.apio.architect.impl.wiring.osgi.manager.uri.mapper.PathIdentifierMapperManager;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.apio.architect.single.model.SingleModel;
import com.liferay.apio.architect.uri.Path;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.core.Context;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@Component
public class RootEndpointImpl implements RootEndpoint {

	@Activate
	public void activate() {
		_documentation = new Documentation(
			() -> _provide(APITitle.class),
			() -> _provide(APIDescription.class),
			() -> _provide(ApplicationURL.class),
			() -> _representableManager.getRepresentors(),
			() -> _collectionRouterManager.getCollectionRoutes(),
			() -> _itemRouterManager.getItemRoutes(),
			() -> _nestedCollectionRouterManager.getNestedCollectionRoutes());
	}

	@Override
	public BatchEndpoint batchEndpoint(String name) {
		return BatchEndpointBuilder.name(
			name
		).httpServletRequest(
			_httpServletRequest
		).singleModelFunction(
			id -> _getSingleModelTry(name, id)
		).representorSupplier(
			() -> _getRepresentorOrFail(name)
		).collectionRoutesSupplier(
			() -> _getCollectionRoutesOrFail(name)
		).nestedCollectionRoutesFunction(
			nestedName -> _getNestedCollectionRoutesOrFail(name, nestedName)
		).build();
	}

	@Override
	public BinaryEndpoint binaryEndpoint() {
		return new BinaryEndpoint(
			this::_getRepresentorOrFail, this::_getSingleModelTry);
	}

	@Override
	public Documentation documentation() {
		return _documentation;
	}

	@Override
	public FormEndpoint formEndpoint() {
		return new FormEndpoint(
			this::_getCollectionRoutesOrFail, this::_getItemRoutesOrFail,
			this::_getNestedCollectionRoutesOrFail);
	}

	@Override
	public EntryPoint home() {
		return () -> _collectionRouterManager.getResourceNames();
	}

	@Override
	public PageEndpointImpl pageEndpoint(String name) {
		return new PageEndpointImpl<>(
			name, _httpServletRequest, id -> _getSingleModelTry(name, id),
			() -> _getCollectionRoutesOrFail(name),
			() -> _getRepresentorOrFail(name), () -> _getItemRoutesOrFail(name),
			nestedName -> _getNestedCollectionRoutesOrFail(name, nestedName),
			_pathIdentifierMapperManager::mapToIdentifierOrFail);
	}

	private CollectionRoutes<Object, Object> _getCollectionRoutesOrFail(
		String name) {

		Optional<CollectionRoutes<Object, Object>> optional =
			_collectionRouterManager.getCollectionRoutesOptional(name);

		return optional.orElseThrow(notFound(name));
	}

	private ItemRoutes<Object, Object> _getItemRoutesOrFail(String name) {
		Optional<ItemRoutes<Object, Object>> optional =
			_itemRouterManager.getItemRoutesOptional(name);

		return optional.orElseThrow(notFound(name));
	}

	private NestedCollectionRoutes<Object, Object, Object>
		_getNestedCollectionRoutesOrFail(String name, String nestedName) {

		Optional<NestedCollectionRoutes<Object, Object, Object>> optional =
			_nestedCollectionRouterManager.getNestedCollectionRoutesOptional(
				name, nestedName);

		return optional.orElseThrow(notFound(name, "{id}", nestedName));
	}

	private Representor<Object> _getRepresentorOrFail(String name) {
		Optional<Representor<Object>> optional =
			_representableManager.getRepresentorOptional(name);

		return optional.orElseThrow(notFound(name));
	}

	private Try<SingleModel<Object>> _getSingleModelTry(
		String name, String id) {

		return Try.fromFallible(
			() -> _getItemRoutesOrFail(name)
		).mapOptional(
			ItemRoutes::getItemFunctionOptional, notFound(name, id)
		).map(
			requestFunction -> requestFunction.apply(_httpServletRequest)
		).map(
			identifierFunction -> identifierFunction.compose(
				(Path path) ->
					_pathIdentifierMapperManager.mapToIdentifierOrFail(path))
		).flatMap(
			pathFunction -> pathFunction.apply(new Path(name, id))
		);
	}

	private <T> Optional<T> _provide(Class<T> clazz) {
		return _providerManager.provideOptional(_httpServletRequest, clazz);
	}

	@Reference
	private CollectionRouterManager _collectionRouterManager;

	private Documentation _documentation;

	@Context
	private HttpServletRequest _httpServletRequest;

	@Reference
	private ItemRouterManager _itemRouterManager;

	@Reference
	private NestedCollectionRouterManager _nestedCollectionRouterManager;

	@Reference
	private PathIdentifierMapperManager _pathIdentifierMapperManager;

	@Reference
	private ProviderManager _providerManager;

	@Reference
	private RepresentableManager _representableManager;

}
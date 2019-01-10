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

package com.liferay.apio.architect.internal.annotation;

import static com.liferay.apio.architect.internal.action.Predicates.isAction;
import static com.liferay.apio.architect.internal.action.Predicates.isActionFor;
import static com.liferay.apio.architect.internal.action.Predicates.isCreateAction;
import static com.liferay.apio.architect.internal.action.Predicates.isRemoveAction;
import static com.liferay.apio.architect.internal.action.Predicates.isReplaceAction;
import static com.liferay.apio.architect.internal.action.Predicates.isRetrieveAction;
import static com.liferay.apio.architect.internal.action.Predicates.isRootCollectionAction;
import static com.liferay.apio.architect.internal.action.Predicates.isUpdateAction;
import static com.liferay.apio.architect.internal.action.converter.EntryPointConverter.getEntryPointFrom;
import static com.liferay.apio.architect.internal.body.JSONToBodyConverter.jsonToBody;
import static com.liferay.apio.architect.internal.body.MultipartToBodyConverter.multipartToBody;

import static io.vavr.Predicates.instanceOf;
import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

import static java.util.function.Function.identity;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA_TYPE;

import com.liferay.apio.architect.annotation.GenericParentId;
import com.liferay.apio.architect.annotation.Id;
import com.liferay.apio.architect.annotation.ParentId;
import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.documentation.APIDescription;
import com.liferay.apio.architect.documentation.APITitle;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.internal.action.ActionSemantics;
import com.liferay.apio.architect.internal.annotation.Action.Error;
import com.liferay.apio.architect.internal.annotation.Action.Error.NotFound;
import com.liferay.apio.architect.internal.documentation.Documentation;
import com.liferay.apio.architect.internal.entrypoint.EntryPoint;
import com.liferay.apio.architect.internal.url.ApplicationURL;
import com.liferay.apio.architect.internal.wiring.osgi.manager.documentation.contributor.CustomDocumentationManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.provider.ProviderManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.representable.RepresentableManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.router.CollectionRouterManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.router.ItemRouterManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.router.NestedCollectionRouterManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.router.ReusableNestedCollectionRouterManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.uri.mapper.PathIdentifierMapperManager;
import com.liferay.apio.architect.resource.Resource;
import com.liferay.apio.architect.resource.Resource.GenericParent;
import com.liferay.apio.architect.resource.Resource.Item;
import com.liferay.apio.architect.resource.Resource.Nested;
import com.liferay.apio.architect.resource.Resource.Paged;
import com.liferay.apio.architect.single.model.SingleModel;
import com.liferay.apio.architect.uri.Path;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides methods to get the different actions provided by the different
 * routers.
 *
 * @author Javier Gamarra
 * @review
 */
@Component(service = ActionManager.class)
public class ActionManagerImpl implements ActionManager {

	/**
	 * Returns all of the action semantics collected by the different routers.
	 *
	 * @review
	 */
	public Stream<ActionSemantics> actionSemantics() {
		return Stream.of(
			_actionRouterManager.getActionSemantics(),
			_itemRouterManager.getActionSemantics(),
			_collectionRouterManager.getActionSemantics(),
			_reusableNestedCollectionRouterManager.getActionSemantics(),
			_nestedCollectionRouterManager.getActionSemantics()
		).flatMap(
			identity()
		);
	}

	@Override
	public Either<Action.Error, Action> getAction(
		String method, List<String> params) {

		int numberOfParams = params.size();

		if (numberOfParams == 1) {
			Paged paged = Paged.of(params.get(0));

			if ("GET".equals(method)) {
				return _getAction(paged, isRootCollectionAction);
			}
			else if ("POST".equals(method)) {
				return _getAction(paged, isCreateAction);
			}
		}
		else if (numberOfParams == 2) {
			Paged paged = Paged.of(params.get(0));
			String actionName = params.get(1);

			Either<Error, Action> pagedActionEither = _getAction(
				paged, isAction(actionName, method));

			if (pagedActionEither.isRight()) {
				return pagedActionEither;
			}

			Item item = _getItem(params.get(0), params.get(1));

			if (item != null) {
				if ("DELETE".equals(method)) {
					return _getAction(item, isRemoveAction);
				}
				else if ("PATCH".equals(method)) {
					return _getAction(item, isUpdateAction);
				}
				else if ("PUT".equals(method)) {
					return _getAction(item, isReplaceAction);
				}
				else if ("GET".equals(method)) {
					return _getAction(item, isRetrieveAction);
				}
			}
		}
		else if (numberOfParams == 3) {
			GenericParent genericParent = _getGenericParent(
				params.get(0), params.get(1), params.get(2));

			if (genericParent != null) {
				if ("GET".equals(method)) {
					return _getAction(genericParent, isRetrieveAction);
				}
				else if ("POST".equals(method)) {
					return _getAction(genericParent, isCreateAction);
				}
			}
			else {
				Item item = _getItem(params.get(0), params.get(1));

				if (item != null) {
					Either<Error, Action> binaryFileActionEither =
						_getBinaryFileAction(item, params.get(2));

					if (binaryFileActionEither.isRight()) {
						return binaryFileActionEither;
					}

					Either<Error, Action> itemEither = _getAction(
						item, isAction(params.get(2), method));

					if (itemEither.isRight()) {
						return itemEither;
					}

					Nested nested = Nested.of(item, params.get(2));

					if ("GET".equals(method)) {
						return _getAction(nested, isRetrieveAction);
					}
					else if ("POST".equals(method)) {
						return _getAction(nested, isCreateAction);
					}
				}
			}
		}
		else if (numberOfParams == 4) {
			GenericParent genericParent = _getGenericParent(
				params.get(0), params.get(1), params.get(2));

			if (genericParent != null) {
				return _getAction(
					genericParent, isAction(params.get(3), method));
			}

			Item item = _getItem(params.get(0), params.get(1));

			if (item != null) {
				Nested nested = Nested.of(item, params.get(2));

				return _getAction(nested, isAction(params.get(3), method));
			}
		}

		return left(_notFound);
	}

	@Override
	public Stream<ActionSemantics> getActionSemantics(
		Resource resource, Credentials credentials,
		HttpServletRequest httpServletRequest) {

		Stream<ActionSemantics> stream = actionSemantics();

		return stream.filter(
			isActionFor(resource)
		).map(
			actionSemantics -> actionSemantics.withResource(resource)
		).filter(
			actionSemantics -> Try.of(
				() -> {
					List<Object> params = actionSemantics.getPermissionParams(
						clazz -> _provide(
							actionSemantics, httpServletRequest, clazz));

					return actionSemantics.checkPermissions(params);
				}
			).getOrElse(
				false
			)
		);
	}

	@Override
	public Documentation getDocumentation(
		HttpServletRequest httpServletRequest) {

		Supplier<Optional<APITitle>> apiTitleSupplier =
			() -> providerManager.provideOptional(
				httpServletRequest, APITitle.class);

		Supplier<Optional<APIDescription>> apiDescriptionSupplier =
			() -> providerManager.provideOptional(
				httpServletRequest, APIDescription.class);

		Supplier<Optional<ApplicationURL>> applicationUrlSupplier =
			() -> providerManager.provideOptional(
				httpServletRequest, ApplicationURL.class);

		Stream<ActionSemantics> stream = actionSemantics();

		Stream<Resource> resourceStream = stream.map(
			ActionSemantics::getResource
		).distinct();

		return new Documentation(
			apiTitleSupplier, apiDescriptionSupplier, applicationUrlSupplier,
			() -> _representableManager.getRepresentors(), resourceStream,
			resource -> getActionSemantics(resource, null, null),
			() -> _customDocumentationManager.getCustomDocumentation());
	}

	@Override
	public EntryPoint getEntryPoint() {
		return getEntryPointFrom(actionSemantics());
	}

	@Override
	public Optional<SingleModel> getItemSingleModel(
		Item item, HttpServletRequest request) {

		return Either.narrow(
			_getAction(item, isRetrieveAction)
		).map(
			action -> action.apply(request)
		).map(
			object -> object instanceof Try ? ((Try)object).get() : object
		).toJavaOptional(
		).filter(
			instanceOf(SingleModel.class)
		).map(
			SingleModel.class::cast
		);
	}

	@Reference
	protected PathIdentifierMapperManager pathIdentifierMapperManager;

	@Reference
	protected ProviderManager providerManager;

	private Either<Action.Error, Action> _getAction(
		Resource resource, Predicate<ActionSemantics> predicate) {

		Stream<ActionSemantics> actionSemanticsStream = actionSemantics();

		Optional<ActionSemantics> optionalActionSemantics =
			actionSemanticsStream.filter(
				predicate.and(isActionFor(resource))
			).findFirst();

		if (!optionalActionSemantics.isPresent()) {
			return left(_notFound);
		}

		ActionSemantics actionSemantics = optionalActionSemantics.get();

		ActionSemantics updatedActionSemantics = actionSemantics.withResource(
			resource);

		return right(updatedActionSemantics.toAction(this::_provide));
	}

	private Either<Action.Error, Action> _getBinaryFileAction(
		Item item, String binaryId) {

		return Option.ofOptional(
			_representableManager.getRepresentorOptional(item.getName())
		).flatMap(
			representor -> Option.ofOptional(
				representor.getBinaryFunction(binaryId))
		).<Action.Error>toEither(
			() -> _notFound
		).map(
			function -> request -> Option.ofOptional(
				getItemSingleModel(item, request)
			).map(
				SingleModel::getModel
			).map(
				function
			).getOrElseThrow(
				NotFoundException::new
			)
		);
	}

	private Body _getBody(HttpServletRequest request) {
		MediaType mediaType = Try.of(
			() -> MediaType.valueOf(request.getContentType())
		).getOrElseThrow(
			t -> new BadRequestException("Invalid Content-Type header", t)
		);

		if (APPLICATION_JSON_TYPE.isCompatible(mediaType)) {
			return jsonToBody(request);
		}

		if (MULTIPART_FORM_DATA_TYPE.isCompatible(mediaType)) {
			return multipartToBody(request);
		}

		throw new NotSupportedException();
	}

	private GenericParent _getGenericParent(
		String name, String genericParentName, String genericParentStringId) {

		return Optional.ofNullable(
			_getId(name, genericParentStringId)
		).map(
			id -> GenericParent.of(genericParentName, id, name)
		).orElse(
			null
		);
	}

	private Resource.Id _getId(String name, String id) {
		return Try.success(
			new Path(name, id)
		).mapTry(
			pathIdentifierMapperManager::mapToIdentifierOrFail
		).map(
			object -> Resource.Id.of(object, id)
		).getOrNull();
	}

	private Item _getItem(String name, String stringId) {
		return Optional.ofNullable(
			_getId(name, stringId)
		).map(
			id -> Item.of(name, id)
		).orElse(
			null
		);
	}

	private Object _provide(
		ActionSemantics actionSemantics, HttpServletRequest request,
		Class<?> clazz) {

		if (Void.class.equals(clazz)) {
			return null;
		}

		if (Body.class.equals(clazz)) {
			return actionSemantics.getBodyValue(_getBody(request));
		}

		if (Id.class.equals(clazz)) {
			return Optional.of(
				actionSemantics.getResource()
			).filter(
				instanceOf(Item.class)
			).map(
				Item.class::cast
			).flatMap(
				Item::getIdOptional
			).orElseThrow(
				NotFoundException::new
			);
		}

		if (ParentId.class.equals(clazz)) {
			return Optional.of(
				actionSemantics.getResource()
			).filter(
				instanceOf(Nested.class)
			).map(
				Nested.class::cast
			).map(
				Nested::getParentItem
			).flatMap(
				Item::getIdOptional
			).orElseThrow(
				NotFoundException::new
			);
		}

		if (GenericParentId.class.equals(clazz)) {
			return Optional.of(
				actionSemantics.getResource()
			).filter(
				instanceOf(GenericParent.class)
			).map(
				GenericParent.class::cast
			).flatMap(
				GenericParent::getParentIdOptional
			).orElseThrow(
				NotFoundException::new
			);
		}

		return providerManager.provideMandatory(request, clazz);
	}

	private static final NotFound _notFound = new NotFound() {
	};

	@Reference
	private ActionRouterManager _actionRouterManager;

	@Reference
	private CollectionRouterManager _collectionRouterManager;

	@Reference
	private CustomDocumentationManager _customDocumentationManager;

	@Reference
	private ItemRouterManager _itemRouterManager;

	@Reference
	private NestedCollectionRouterManager _nestedCollectionRouterManager;

	@Reference
	private RepresentableManager _representableManager;

	@Reference
	private ReusableNestedCollectionRouterManager
		_reusableNestedCollectionRouterManager;

}
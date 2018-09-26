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

package com.liferay.apio.architect.internal.routes;

import static com.liferay.apio.architect.internal.routes.RoutesBuilderUtil.provide;

import com.liferay.apio.architect.alias.IdentifierFunction;
import com.liferay.apio.architect.alias.form.FormBuilderFunction;
import com.liferay.apio.architect.alias.routes.BatchCreateItemFunction;
import com.liferay.apio.architect.alias.routes.CreateItemFunction;
import com.liferay.apio.architect.alias.routes.CustomPageFunction;
import com.liferay.apio.architect.alias.routes.GetPageFunction;
import com.liferay.apio.architect.alias.routes.permission.HasAddingPermissionFunction;
import com.liferay.apio.architect.batch.BatchResult;
import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.custom.actions.CustomRoute;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.function.throwable.ThrowableBiFunction;
import com.liferay.apio.architect.function.throwable.ThrowableFunction;
import com.liferay.apio.architect.function.throwable.ThrowableHexaFunction;
import com.liferay.apio.architect.function.throwable.ThrowablePentaFunction;
import com.liferay.apio.architect.function.throwable.ThrowableTetraFunction;
import com.liferay.apio.architect.function.throwable.ThrowableTriFunction;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.internal.alias.ProvideFunction;
import com.liferay.apio.architect.internal.form.FormImpl;
import com.liferay.apio.architect.internal.operation.BatchCreateOperation;
import com.liferay.apio.architect.internal.operation.CreateOperation;
import com.liferay.apio.architect.internal.operation.DeleteOperation;
import com.liferay.apio.architect.internal.operation.RetrieveOperation;
import com.liferay.apio.architect.internal.operation.UpdateOperation;
import com.liferay.apio.architect.internal.pagination.PageImpl;
import com.liferay.apio.architect.internal.single.model.SingleModelImpl;
import com.liferay.apio.architect.operation.HTTPMethod;
import com.liferay.apio.architect.operation.Operation;
import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.uri.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Alejandro Hernández
 */
public class CollectionRoutesImpl<T, S> implements CollectionRoutes<T, S> {

	public CollectionRoutesImpl(BuilderImpl<T, S> builderImpl) {
		_batchCreateItemFunction = builderImpl._batchCreateItemFunction;
		_createItemFunction = builderImpl._createItemFunction;
		_form = builderImpl._form;
		_getPageFunction = builderImpl._getPageFunction;

		_customRoutes = builderImpl._customRoutes;
		_customPageFunctions = builderImpl._customRouteFunctions;
	}

	@Override
	public Optional<BatchCreateItemFunction<S>>
		getBatchCreateItemFunctionOptional() {

		return Optional.ofNullable(_batchCreateItemFunction);
	}

	@Override
	public Optional<CreateItemFunction<T>> getCreateItemFunctionOptional() {
		return Optional.ofNullable(_createItemFunction);
	}

	@Override
	public Optional<Map<String, CustomPageFunction<?>>>
		getCustomPageFunctionsOptional() {

		return Optional.of(_customPageFunctions);
	}

	@Override
	public Map<String, CustomRoute> getCustomRoutes() {
		return _customRoutes;
	}

	@Override
	public Optional<Form> getFormOptional() {
		return Optional.ofNullable(_form);
	}

	@Override
	public Optional<GetPageFunction<T>> getGetPageFunctionOptional() {
		return Optional.ofNullable(_getPageFunction);
	}

	public static class BuilderImpl<T, S> implements Builder<T, S> {

		public BuilderImpl(
			String name, ProvideFunction provideFunction,
			Consumer<String> neededProviderConsumer,
			Function<Path, ?> pathToIdentifierFunction,
			Function<T, S> modelToIdentifierFunction,
			Function<String, Optional<String>> nameFunction) {

			_name = name;
			_provideFunction = provideFunction;
			_neededProviderConsumer = neededProviderConsumer;

			_pathToIdentifierFunction = pathToIdentifierFunction::apply;
			_modelToIdentifierFunction = modelToIdentifierFunction;
			_nameFunction = nameFunction;
		}

		@Override
		public <A, R> Builder<T, S> addCreator(
			ThrowableBiFunction<R, A, T> creatorThrowableBiFunction,
			Class<A> aClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			ThrowableBiFunction<List<R>, A, List<S>>
				batchCreatorThrowableBiFunction = (formList, a) ->
					_transformList(
						formList, r -> creatorThrowableBiFunction.apply(r, a));

			return addCreator(
				creatorThrowableBiFunction, batchCreatorThrowableBiFunction,
				aClass, hasAddingPermissionFunction, formBuilderFunction);
		}

		@Override
		public <A, R> Builder<T, S> addCreator(
			ThrowableBiFunction<R, A, T> creatorThrowableBiFunction,
			ThrowableBiFunction<List<R>, A, List<S>>
				batchCreatorThrowableBiFunction,
			Class<A> aClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());

			_hasAddingPermissionFunction = hasAddingPermissionFunction;

			Form<R> form = formBuilderFunction.apply(
				new FormImpl.BuilderImpl<>(
					Arrays.asList("c", _name), _pathToIdentifierFunction));

			_form = form;

			_createItemFunction = httpServletRequest -> body -> provide(
				_provideFunction.apply(httpServletRequest), aClass,
				a -> creatorThrowableBiFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _name, Collections.emptyList())
				).apply(
					form.get(body), a
				));

			_batchCreateItemFunction = httpServletRequest -> body -> provide(
				_provideFunction.apply(httpServletRequest), aClass,
				a -> batchCreatorThrowableBiFunction.andThen(
					t -> new BatchResult<>(t, _name)
				).apply(
					form.getList(body), a
				));

			return this;
		}

		@Override
		public <R> Builder<T, S> addCreator(
			ThrowableFunction<R, T> creatorThrowableFunction,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			ThrowableFunction<List<R>, List<S>> batchCreatorThrowableFunction =
				formList -> _transformList(formList, creatorThrowableFunction);

			return addCreator(
				creatorThrowableFunction, batchCreatorThrowableFunction,
				hasAddingPermissionFunction, formBuilderFunction);
		}

		@Override
		public <R> Builder<T, S> addCreator(
			ThrowableFunction<R, T> creatorThrowableFunction,
			ThrowableFunction<List<R>, List<S>> batchCreatorThrowableFunction,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			_hasAddingPermissionFunction = hasAddingPermissionFunction;

			Form<R> form = formBuilderFunction.apply(
				new FormImpl.BuilderImpl<>(
					Arrays.asList("c", _name), _pathToIdentifierFunction));

			_form = form;

			_createItemFunction = httpServletRequest -> body ->
				Try.fromFallible(
					() -> creatorThrowableFunction.andThen(
						t -> new SingleModelImpl<>(
							t, _name, Collections.emptyList())
					).apply(
						form.get(body)
					));

			_batchCreateItemFunction = httpServletRequest -> body ->
				Try.fromFallible(
					() -> batchCreatorThrowableFunction.andThen(
						t -> new BatchResult<>(t, _name)
					).apply(
						form.getList(body)
					));

			return this;
		}

		@Override
		public <A, B, C, D, R> Builder<T, S> addCreator(
			ThrowablePentaFunction<R, A, B, C, D, T>
				creatorThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			ThrowablePentaFunction<List<R>, A, B, C, D, List<S>>
				batchCreatorThrowablePentaFunction = (formList, a, b, c, d) ->
					_transformList(
						formList,
						r -> creatorThrowablePentaFunction.apply(
							r, a, b, c, d));

			return addCreator(
				creatorThrowablePentaFunction,
				batchCreatorThrowablePentaFunction, aClass, bClass, cClass,
				dClass, hasAddingPermissionFunction, formBuilderFunction);
		}

		@Override
		public <A, B, C, D, R> Builder<T, S> addCreator(
			ThrowablePentaFunction<R, A, B, C, D, T>
				creatorThrowablePentaFunction,
			ThrowablePentaFunction<List<R>, A, B, C, D, List<S>>
				batchCreatorThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());
			_neededProviderConsumer.accept(dClass.getName());

			_hasAddingPermissionFunction = hasAddingPermissionFunction;

			Form<R> form = formBuilderFunction.apply(
				new FormImpl.BuilderImpl<>(
					Arrays.asList("c", _name), _pathToIdentifierFunction));

			_form = form;

			_createItemFunction = httpServletRequest -> body -> provide(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				cClass, dClass,
				(a, b, c, d) -> creatorThrowablePentaFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _name, Collections.emptyList())
				).apply(
					form.get(body), a, b, c, d
				));

			_batchCreateItemFunction = httpServletRequest -> body -> provide(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				cClass, dClass,
				(a, b, c, d) -> batchCreatorThrowablePentaFunction.andThen(
					t -> new BatchResult<>(t, _name)
				).apply(
					form.getList(body), a, b, c, d
				));

			return this;
		}

		@Override
		public <A, B, C, R> Builder<T, S> addCreator(
			ThrowableTetraFunction<R, A, B, C, T> creatorThrowableTetraFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			ThrowableTetraFunction<List<R>, A, B, C, List<S>>
				batchCreatorThrowableTetraFunction = (formList, a, b, c) ->
					_transformList(
						formList,
						r -> creatorThrowableTetraFunction.apply(r, a, b, c));

			return addCreator(
				creatorThrowableTetraFunction,
				batchCreatorThrowableTetraFunction, aClass, bClass, cClass,
				hasAddingPermissionFunction, formBuilderFunction);
		}

		@Override
		public <A, B, C, R> Builder<T, S> addCreator(
			ThrowableTetraFunction<R, A, B, C, T> creatorThrowableTetraFunction,
			ThrowableTetraFunction<List<R>, A, B, C, List<S>>
				batchCreatorThrowableTetraFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());

			_hasAddingPermissionFunction = hasAddingPermissionFunction;

			Form<R> form = formBuilderFunction.apply(
				new FormImpl.BuilderImpl<>(
					Arrays.asList("c", _name), _pathToIdentifierFunction));

			_form = form;

			_createItemFunction = httpServletRequest -> body -> provide(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				cClass,
				(a, b, c) -> creatorThrowableTetraFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _name, Collections.emptyList())
				).apply(
					form.get(body), a, b, c
				));

			_batchCreateItemFunction = httpServletRequest -> body -> provide(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				cClass,
				(a, b, c) -> batchCreatorThrowableTetraFunction.andThen(
					t -> new BatchResult<>(t, _name)
				).apply(
					form.getList(body), a, b, c
				));

			return this;
		}

		@Override
		public <A, B, R> Builder<T, S> addCreator(
			ThrowableTriFunction<R, A, B, T> creatorThrowableTriFunction,
			Class<A> aClass, Class<B> bClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			ThrowableTriFunction<List<R>, A, B, List<S>>
				batchCreatorThrowableTriFunction = (formList, a, b) ->
					_transformList(
						formList,
						r -> creatorThrowableTriFunction.apply(r, a, b));

			return addCreator(
				creatorThrowableTriFunction, batchCreatorThrowableTriFunction,
				aClass, bClass, hasAddingPermissionFunction,
				formBuilderFunction);
		}

		@Override
		public <A, B, R> Builder<T, S> addCreator(
			ThrowableTriFunction<R, A, B, T> creatorThrowableTriFunction,
			ThrowableTriFunction<List<R>, A, B, List<S>>
				batchCreatorThrowableTriFunction,
			Class<A> aClass, Class<B> bClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());

			_hasAddingPermissionFunction = hasAddingPermissionFunction;

			Form<R> form = formBuilderFunction.apply(
				new FormImpl.BuilderImpl<>(
					Arrays.asList("c", _name), _pathToIdentifierFunction));

			_form = form;

			_createItemFunction = httpServletRequest -> body -> provide(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				(a, b) -> creatorThrowableTriFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _name, Collections.emptyList())
				).apply(
					form.get(body), a, b
				));

			_batchCreateItemFunction = httpServletRequest -> body -> provide(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				(a, b) -> batchCreatorThrowableTriFunction.andThen(
					t -> new BatchResult<>(t, _name)
				).apply(
					form.getList(body), a, b
				));

			return this;
		}

		@Override
		public <R, U, I extends Identifier> CollectionRoutes.Builder<T, S>
			addCustomRoute(
				CustomRoute customRoute,
				ThrowableBiFunction<Pagination, R, U> throwableBiFunction,
				Class<I> supplier,
				Function<Credentials, Boolean> permissionFunction,
				FormBuilderFunction<R> formBuilderFunction) {

			String name = customRoute.getName();

			_calculateForm(customRoute, formBuilderFunction, name);

			_customRoutes.put(name, customRoute);
			_customPermissionFunctions.put(name, permissionFunction);

			CustomPageFunction<U> requestFunction =
				httpServletRequest -> body -> provide(
					_provideFunction.apply(httpServletRequest),
					Pagination.class,
					pagination -> throwableBiFunction.andThen(
						model -> new SingleModelImpl(
							model, _getResourceName(supplier))
					).apply(
						pagination, _getModel(customRoute, body)
					));

			_customRouteFunctions.put(name, requestFunction);

			return this;
		}

		@Override
		public <A, B, C, D, R, U, I extends Identifier>
			CollectionRoutes.Builder<T, S> addCustomRoute(
				CustomRoute customRoute,
				ThrowableHexaFunction<Pagination, R, A, B, C, D, U>
					throwableHexaFunction,
				Class<A> aClass, Class<B> bClass, Class<C> cClass,
				Class<D> dClass, Class<I> supplier,
				Function<Credentials, Boolean> permissionFunction,
				FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());
			_neededProviderConsumer.accept(dClass.getName());

			String name = customRoute.getName();

			_calculateForm(customRoute, formBuilderFunction, name);

			_customRoutes.put(name, customRoute);
			_customPermissionFunctions.put(name, permissionFunction);

			CustomPageFunction<U> requestFunction =
				httpServletRequest -> body -> provide(
					_provideFunction.apply(httpServletRequest),
					Pagination.class, aClass, bClass, cClass, dClass,
					(pagination, a, b, c, d) -> throwableHexaFunction.andThen(
						model -> new SingleModelImpl(
							model, _getResourceName(supplier))
					).apply(
						pagination, _getModel(customRoute, body), a, b, c, d
					));

			_customRouteFunctions.put(name, requestFunction);

			return this;
		}

		@Override
		public <A, B, C, R, U, I extends Identifier>
			CollectionRoutes.Builder<T, S> addCustomRoute(
				CustomRoute customRoute,
				ThrowablePentaFunction<Pagination, R, A, B, C, U>
					throwablePentaFunction,
				Class<A> aClass, Class<B> bClass, Class<C> cClass,
				Class<I> supplier,
				Function<Credentials, Boolean> permissionFunction,
				FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());

			String name = customRoute.getName();

			_calculateForm(customRoute, formBuilderFunction, name);

			_customRoutes.put(name, customRoute);
			_customPermissionFunctions.put(name, permissionFunction);

			CustomPageFunction<U> requestFunction =
				httpServletRequest -> body -> provide(
					_provideFunction.apply(httpServletRequest),
					Pagination.class, aClass, bClass, cClass,
					(pagination, a, b, c) -> throwablePentaFunction.andThen(
						model -> new SingleModelImpl(
							model, _getResourceName(supplier))
					).apply(
						pagination, _getModel(customRoute, body), a, b, c
					));

			_customRouteFunctions.put(name, requestFunction);

			return this;
		}

		@Override
		public <A, B, R, U, I extends Identifier> CollectionRoutes.Builder<T, S>
			addCustomRoute(
				CustomRoute customRoute,
				ThrowableTetraFunction<Pagination, R, A, B, U>
					throwableTetraFunction,
				Class<A> aClass, Class<B> bClass, Class<I> supplier,
				Function<Credentials, Boolean> permissionFunction,
				FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());

			String name = customRoute.getName();

			_calculateForm(customRoute, formBuilderFunction, name);

			_customRoutes.put(name, customRoute);
			_customPermissionFunctions.put(name, permissionFunction);

			CustomPageFunction<R> requestFunction =
				httpServletRequest -> body -> provide(
					_provideFunction.apply(httpServletRequest),
					Pagination.class, aClass, bClass,
					(pagination, a, b) -> throwableTetraFunction.andThen(
						model -> new SingleModelImpl(
							model, _getResourceName(supplier))
					).apply(
						pagination, _getModel(customRoute, body), a, b
					));

			_customRouteFunctions.put(name, requestFunction);

			return this;
		}

		@Override
		public <A, R, U, I extends Identifier> CollectionRoutes.Builder<T, S>
			addCustomRoute(
				CustomRoute customRoute,
				ThrowableTriFunction<Pagination, R, A, U> throwableTriFunction,
				Class<A> aClass, Class<I> supplier,
				Function<Credentials, Boolean> permissionFunction,
				FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());

			String name = customRoute.getName();

			_calculateForm(customRoute, formBuilderFunction, name);

			_customRoutes.put(name, customRoute);
			_customPermissionFunctions.put(name, permissionFunction);

			CustomPageFunction<U> requestFunction =
				httpServletRequest -> body -> provide(
					_provideFunction.apply(httpServletRequest),
					Pagination.class, aClass,
					(pagination, a) -> throwableTriFunction.andThen(
						model -> new SingleModelImpl(
							model, _getResourceName(supplier))
					).apply(
						pagination, _getModel(customRoute, body), a
					));

			_customRouteFunctions.put(name, requestFunction);

			return this;
		}

		@Override
		public <A> Builder<T, S> addGetter(
			ThrowableBiFunction<Pagination, A, PageItems<T>>
				getterThrowableBiFunction,
			Class<A> aClass) {

			_neededProviderConsumer.accept(aClass.getName());

			_getPageFunction = httpServletRequest -> provide(
				_provideFunction.apply(httpServletRequest), Pagination.class,
				aClass, Credentials.class,
				(pagination, a, credentials) ->
					getterThrowableBiFunction.andThen(
						items -> new PageImpl<>(
							_name, items, pagination,
							_getOperations(credentials))
					).apply(
						pagination, a
					));

			return this;
		}

		@Override
		public Builder<T, S> addGetter(
			ThrowableFunction<Pagination, PageItems<T>>
				getterThrowableFunction) {

			_getPageFunction = httpServletRequest -> provide(
				_provideFunction.apply(httpServletRequest), Pagination.class,
				Credentials.class,
				(pagination, credentials) -> getterThrowableFunction.andThen(
					items -> new PageImpl<>(
						_name, items, pagination, _getOperations(credentials))
				).apply(
					pagination
				));

			return this;
		}

		@Override
		public <A, B, C, D> Builder<T, S> addGetter(
			ThrowablePentaFunction<Pagination, A, B, C, D, PageItems<T>>
				getterThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			Class<D> dClass) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());
			_neededProviderConsumer.accept(dClass.getName());

			_getPageFunction = httpServletRequest -> provide(
				_provideFunction.apply(httpServletRequest), Pagination.class,
				aClass, bClass, cClass, dClass, Credentials.class,
				(pagination, a, b, c, d, credentials) ->
					getterThrowablePentaFunction.andThen(
						items -> new PageImpl<>(
							_name, items, pagination,
							_getOperations(credentials))
					).apply(
						pagination, a, b, c, d
					));

			return this;
		}

		@Override
		public <A, B, C> Builder<T, S> addGetter(
			ThrowableTetraFunction<Pagination, A, B, C, PageItems<T>>
				getterThrowableTetraFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());

			_getPageFunction = httpServletRequest -> provide(
				_provideFunction.apply(httpServletRequest), Pagination.class,
				aClass, bClass, cClass, Credentials.class,
				(pagination, a, b, c, credentials) ->
					getterThrowableTetraFunction.andThen(
						items -> new PageImpl<>(
							_name, items, pagination,
							_getOperations(credentials))
					).apply(
						pagination, a, b, c
					));

			return this;
		}

		@Override
		public <A, B> Builder<T, S> addGetter(
			ThrowableTriFunction<Pagination, A, B, PageItems<T>>
				getterThrowableTriFunction,
			Class<A> aClass, Class<B> bClass) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());

			_getPageFunction = httpServletRequest -> provide(
				_provideFunction.apply(httpServletRequest), Pagination.class,
				aClass, bClass, Credentials.class,
				(pagination, a, b, credentials) ->
					getterThrowableTriFunction.andThen(
						items -> new PageImpl<>(
							_name, items, pagination,
							_getOperations(credentials))
					).apply(
						pagination, a, b
					));

			return this;
		}

		@Override
		public CollectionRoutes<T, S> build() {
			return new CollectionRoutesImpl<>(this);
		}

		private <R> void _calculateForm(
			CustomRoute customRoute, FormBuilderFunction<R> formBuilderFunction,
			String name) {

			if (formBuilderFunction != null) {
				Form<R> form = formBuilderFunction.apply(
					new FormImpl.BuilderImpl<>(
						Arrays.asList("p", _name, name),
						_pathToIdentifierFunction));

				customRoute.setForm(form);
			}
		}

		private List<Operation> _createCustomOperations(
			Credentials credentials) {

			List<Operation> operations = new ArrayList<>();

			Set<String> customPermissionKeys =
				_customPermissionFunctions.keySet();

			Stream<String> customPermissionKeysStream =
				customPermissionKeys.stream();

			customPermissionKeysStream.filter(
				key -> _getPermissionFunction(credentials, key)
			).forEach(
				routeEntry -> {
					CustomRoute customRoute = _customRoutes.get(routeEntry);

					Optional<Form<?>> formOptional =
						customRoute.getFormOptional();

					Form form = formOptional.orElse(null);

					Operation operation = _createOperation(
						form, customRoute.getMethod(), _name, _name,
						routeEntry);

					operations.add(operation);
				}
			);

			return operations;
		}

		private Operation _createOperation(
			Form form, HTTPMethod method, String name, String path,
			String custom) {

			if (method == HTTPMethod.GET) {
				return new RetrieveOperation(name, true, path, custom);
			}
			else if (method == HTTPMethod.POST) {
				return new CreateOperation(form, name, path, custom);
			}
			else if (method == HTTPMethod.DELETE) {
				return new DeleteOperation(name, path, custom);
			}
			else if (method == HTTPMethod.PUT) {
				return new UpdateOperation(form, name, path, custom);
			}

			return null;
		}

		private <R> R _getModel(CustomRoute customRoute, Body body) {
			Optional<Form<?>> formOptional = customRoute.getFormOptional();

			return (R)formOptional.map(
				form -> form.get(body)
			).orElse(
				null
			);
		}

		private List<Operation> _getOperations(Credentials credentials) {
			Boolean canAdd = Try.fromFallible(
				() -> _hasAddingPermissionFunction.apply(credentials)
			).orElse(
				false
			);

			if (!canAdd) {
				return Collections.emptyList();
			}

			List<Operation> operations = new ArrayList<>();

			CreateOperation createOperation = new CreateOperation(
				_form, _name, _name);

			operations.add(createOperation);

			BatchCreateOperation batchCreateOperation =
				new BatchCreateOperation(_form, _name, _name);

			operations.add(batchCreateOperation);

			operations.addAll(_createCustomOperations(credentials));

			return operations;
		}

		private Boolean _getPermissionFunction(
			Credentials credentials, String key) {

			return Try.fromFallible(
				() -> _customPermissionFunctions.get(key)
			).map(
				function -> function.apply(credentials)
			).orElse(
				false
			);
		}

		private <I extends Identifier> String _getResourceName(
			Class<I> supplier) {

			return _nameFunction.apply(
				supplier.getName()
			).orElse(
				null
			);
		}

		private <U> List<S> _transformList(
				List<U> list,
				ThrowableFunction<U, T> transformThrowableFunction)
			throws Exception {

			List<S> newList = new ArrayList<>();

			for (U u : list) {
				S s = transformThrowableFunction.andThen(
					_modelToIdentifierFunction::apply
				).apply(
					u
				);

				newList.add(s);
			}

			return newList;
		}

		private BatchCreateItemFunction<S> _batchCreateItemFunction;
		private CreateItemFunction<T> _createItemFunction;
		private final Map<String, Function<Credentials, Boolean>>
			_customPermissionFunctions = new HashMap<>();
		private Map<String, CustomPageFunction<?>> _customRouteFunctions =
			new HashMap<>();
		private final Map<String, CustomRoute> _customRoutes = new HashMap<>();
		private Form _form;
		private GetPageFunction<T> _getPageFunction;
		private HasAddingPermissionFunction _hasAddingPermissionFunction;
		private final Function<T, S> _modelToIdentifierFunction;
		private final String _name;
		private final Function<String, Optional<String>> _nameFunction;
		private final Consumer<String> _neededProviderConsumer;
		private final IdentifierFunction<?> _pathToIdentifierFunction;
		private final ProvideFunction _provideFunction;

	}

	private final BatchCreateItemFunction<S> _batchCreateItemFunction;
	private final CreateItemFunction<T> _createItemFunction;
	private final Map<String, CustomPageFunction<?>> _customPageFunctions;
	private final Map<String, CustomRoute> _customRoutes;
	private final Form _form;
	private final GetPageFunction<T> _getPageFunction;

}
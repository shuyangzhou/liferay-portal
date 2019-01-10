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

import static com.liferay.apio.architect.internal.unsafe.Unsafe.unsafeCast;

import static java.util.Collections.unmodifiableList;

import com.liferay.apio.architect.alias.form.FormBuilderFunction;
import com.liferay.apio.architect.alias.routes.BatchCreateItemFunction;
import com.liferay.apio.architect.alias.routes.CreateItemFunction;
import com.liferay.apio.architect.alias.routes.CustomPageFunction;
import com.liferay.apio.architect.alias.routes.GetPageFunction;
import com.liferay.apio.architect.alias.routes.permission.HasAddingPermissionFunction;
import com.liferay.apio.architect.annotation.EntryPoint;
import com.liferay.apio.architect.batch.BatchResult;
import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.custom.actions.CustomRoute;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.function.throwable.ThrowableFunction;
import com.liferay.apio.architect.function.throwable.ThrowableHexaFunction;
import com.liferay.apio.architect.function.throwable.ThrowablePentaFunction;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.internal.action.ActionSemantics;
import com.liferay.apio.architect.internal.pagination.PageImpl;
import com.liferay.apio.architect.internal.single.model.SingleModelImpl;
import com.liferay.apio.architect.pagination.Page;
import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.resource.Resource.Paged;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Alejandro Hernández
 */
public class CollectionRoutesImpl<T, S> implements CollectionRoutes<T, S> {

	public CollectionRoutesImpl(BuilderImpl<T, S> builderImpl) {
		_actionSemantics = unmodifiableList(builderImpl._actionSemantics);
	}

	/**
	 * Returns the list of {@link ActionSemantics} created by a {@link Builder}.
	 *
	 * @review
	 */
	public List<ActionSemantics> getActionSemantics() {
		return _actionSemantics;
	}

	@Override
	public Optional<BatchCreateItemFunction<S>>
		getBatchCreateItemFunctionOptional() {

		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<CreateItemFunction<T>> getCreateItemFunctionOptional() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<Map<String, CustomPageFunction<?>>>
		getCustomPageFunctionsOptional() {

		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, CustomRoute> getCustomRoutes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<Form> getFormOptional() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<GetPageFunction<T>> getGetPageFunctionOptional() {
		throw new UnsupportedOperationException();
	}

	public static class BuilderImpl<T, S> implements Builder<T, S> {

		public BuilderImpl(
			Paged paged, Supplier<Form.Builder> formBuilderSupplier,
			Function<T, S> modelToIdentifierFunction,
			Function<String, Optional<String>> nameFunction) {

			_paged = paged;
			_formBuilderSupplier = formBuilderSupplier;
			_modelToIdentifierFunction = modelToIdentifierFunction;
			_nameFunction = nameFunction;
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

			Form<?> form = formBuilderFunction.apply(
				unsafeCast(_formBuilderSupplier.get()));

			ActionSemantics batchCreateActionSemantics =
				ActionSemantics.ofResource(
					_paged
				).name(
					"batch-create"
				).method(
					"POST"
				).returns(
					BatchResult.class
				).permissionFunction(
					params -> hasAddingPermissionFunction.apply(
						unsafeCast(params.get(0)))
				).permissionProvidedClasses(
					Credentials.class
				).executeFunction(
					params -> batchCreatorThrowablePentaFunction.andThen(
						t -> new BatchResult<>(t, _paged.getName())
					).apply(
						unsafeCast(params.get(0)), unsafeCast(params.get(1)),
						unsafeCast(params.get(2)), unsafeCast(params.get(3)),
						unsafeCast(params.get(4))
					)
				).bodyFunction(
					form::getList
				).receivesParams(
					Body.class, aClass, bClass, cClass, dClass
				).build();

			_actionSemantics.add(batchCreateActionSemantics);

			ActionSemantics createActionSemantics = ActionSemantics.ofResource(
				_paged
			).name(
				"create"
			).method(
				"POST"
			).returns(
				SingleModel.class
			).permissionFunction(
				params -> hasAddingPermissionFunction.apply(
					unsafeCast(params.get(0)))
			).permissionProvidedClasses(
				Credentials.class
			).executeFunction(
				params -> creatorThrowablePentaFunction.andThen(
					t -> new SingleModelImpl<>(t, _paged.getName())
				).apply(
					unsafeCast(params.get(0)), unsafeCast(params.get(1)),
					unsafeCast(params.get(2)), unsafeCast(params.get(3)),
					unsafeCast(params.get(4))
				)
			).bodyFunction(
				form::get
			).receivesParams(
				Body.class, aClass, bClass, cClass, dClass
			).build();

			_actionSemantics.add(createActionSemantics);

			return this;
		}

		@Override
		public <A, B, C, D, R, U, I extends Identifier>
			Builder<T, S> addCustomRoute(
				CustomRoute customRoute,
				ThrowableHexaFunction<Pagination, R, A, B, C, D, U>
					throwableHexaFunction,
				Class<A> aClass, Class<B> bClass, Class<C> cClass,
				Class<D> dClass, Class<I> identifierClass,
				Function<Credentials, Boolean> permissionFunction,
				FormBuilderFunction<R> formBuilderFunction) {

			Optional<Form> optionalForm = Optional.ofNullable(
				formBuilderFunction
			).map(
				function -> function.apply(
					unsafeCast(_formBuilderSupplier.get()))
			);

			Class<?> bodyClass = optionalForm.<Class<?>>map(
				__ -> Body.class
			).orElse(
				Void.class
			);

			ActionSemantics createActionSemantics = ActionSemantics.ofResource(
				_paged
			).name(
				customRoute.getName()
			).method(
				customRoute.getMethod()
			).returns(
				SingleModel.class
			).permissionFunction(
				params -> permissionFunction.apply(unsafeCast(params.get(0)))
			).permissionProvidedClasses(
				Credentials.class
			).executeFunction(
				params -> throwableHexaFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _getResourceName(identifierClass))
				).apply(
					(Pagination)params.get(0), unsafeCast(params.get(1)),
					unsafeCast(params.get(2)), unsafeCast(params.get(3)),
					unsafeCast(params.get(4)), unsafeCast(params.get(5))
				)
			).bodyFunction(
				body -> optionalForm.map(
					form -> form.get(body)
				).orElse(
					null
				)
			).receivesParams(
				Pagination.class, bodyClass, aClass, bClass, cClass, dClass
			).build();

			_actionSemantics.add(createActionSemantics);

			return this;
		}

		@Override
		public <A, B, C, D> Builder<T, S> addGetter(
			ThrowablePentaFunction<Pagination, A, B, C, D, PageItems<T>>
				getterThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			Class<D> dClass) {

			ActionSemantics actionSemantics = ActionSemantics.ofResource(
				_paged
			).name(
				"retrieve"
			).method(
				"GET"
			).returns(
				Page.class
			).permissionFunction(
			).executeFunction(
				params -> getterThrowablePentaFunction.andThen(
					pageItems -> new PageImpl<>(
						_paged, pageItems, (Pagination)params.get(0))
				).apply(
					(Pagination)params.get(0), unsafeCast(params.get(1)),
					unsafeCast(params.get(2)), unsafeCast(params.get(3)),
					unsafeCast(params.get(4))
				)
			).annotatedWith(
				() -> EntryPoint.class
			).receivesParams(
				Pagination.class, aClass, bClass, cClass, dClass
			).build();

			_actionSemantics.add(actionSemantics);

			return this;
		}

		@Override
		public CollectionRoutes<T, S> build() {
			return new CollectionRoutesImpl<>(this);
		}

		private <I extends Identifier> String _getResourceName(Class<I> clazz) {
			return _nameFunction.apply(
				clazz.getName()
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

		private final List<ActionSemantics> _actionSemantics =
			new ArrayList<>();
		private final Supplier<Form.Builder> _formBuilderSupplier;
		private final Function<T, S> _modelToIdentifierFunction;
		private final Function<String, Optional<String>> _nameFunction;
		private final Paged _paged;

	}

	private final List<ActionSemantics> _actionSemantics;

}
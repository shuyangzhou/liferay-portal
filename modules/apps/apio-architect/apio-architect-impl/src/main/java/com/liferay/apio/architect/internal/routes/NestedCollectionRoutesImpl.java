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

import com.liferay.apio.architect.alias.form.FormBuilderFunction;
import com.liferay.apio.architect.alias.routes.NestedBatchCreateItemFunction;
import com.liferay.apio.architect.alias.routes.NestedCreateItemFunction;
import com.liferay.apio.architect.alias.routes.NestedGetPageFunction;
import com.liferay.apio.architect.alias.routes.permission.HasNestedAddingPermissionFunction;
import com.liferay.apio.architect.annotation.GenericParentId;
import com.liferay.apio.architect.annotation.ParentId;
import com.liferay.apio.architect.batch.BatchResult;
import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.function.throwable.ThrowableFunction;
import com.liferay.apio.architect.function.throwable.ThrowableHexaFunction;
import com.liferay.apio.architect.internal.action.ActionSemantics;
import com.liferay.apio.architect.internal.pagination.PageImpl;
import com.liferay.apio.architect.internal.single.model.SingleModelImpl;
import com.liferay.apio.architect.pagination.Page;
import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.resource.Resource;
import com.liferay.apio.architect.resource.Resource.GenericParent;
import com.liferay.apio.architect.resource.Resource.Id;
import com.liferay.apio.architect.resource.Resource.Nested;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Alejandro Hernández
 */
public class NestedCollectionRoutesImpl<T, S, U>
	implements NestedCollectionRoutes<T, S, U> {

	public NestedCollectionRoutesImpl(BuilderImpl<T, S, U> builderImpl) {
		_actionSemantics = builderImpl._actionSemantics;
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
	public Optional<Form> getFormOptional() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<NestedBatchCreateItemFunction<S, U>>
		getNestedBatchCreateItemFunctionOptional() {

		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<NestedCreateItemFunction<T, U>>
		getNestedCreateItemFunctionOptional() {

		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<NestedGetPageFunction<T, U>>
		getNestedGetPageFunctionOptional() {

		throw new UnsupportedOperationException();
	}

	public static class BuilderImpl<T, S, U> implements Builder<T, S, U> {

		public BuilderImpl(
			Resource resource, Supplier<Form.Builder> formBuilderSupplier,
			Function<T, S> modelToIdentifierFunction) {

			if (!(resource instanceof Nested) &&
				!(resource instanceof GenericParent)) {

				throw new IllegalArgumentException(
					"Resource must be either Nested or GenericParent");
			}

			_resource = resource;
			_formBuilderSupplier = formBuilderSupplier;
			_modelToIdentifierFunction = modelToIdentifierFunction;
		}

		@Override
		public <A, B, C, D, R> Builder<T, S, U> addCreator(
			ThrowableHexaFunction<U, R, A, B, C, D, T>
				creatorThrowableHexaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasNestedAddingPermissionFunction<U>
				hasNestedAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			ThrowableHexaFunction<U, List<R>, A, B, C, D, List<S>>
				batchCreatorThrowableHexaFunction = (u, formList, a, b, c, d) ->
					_transformList(
						formList,
						r -> creatorThrowableHexaFunction.apply(
							u, r, a, b, c, d));

			return addCreator(
				creatorThrowableHexaFunction, batchCreatorThrowableHexaFunction,
				aClass, bClass, cClass, dClass,
				hasNestedAddingPermissionFunction, formBuilderFunction);
		}

		@Override
		public <A, B, C, D, R> Builder<T, S, U> addCreator(
			ThrowableHexaFunction<U, R, A, B, C, D, T>
				creatorThrowableHexaFunction,
			ThrowableHexaFunction<U, List<R>, A, B, C, D, List<S>>
				batchCreatorThrowableHexaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasNestedAddingPermissionFunction<U>
				hasNestedAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			Form form = formBuilderFunction.apply(
				unsafeCast(_formBuilderSupplier.get()));

			ActionSemantics batchCreateActionSemantics =
				ActionSemantics.ofResource(
					_resource
				).name(
					"batch-create"
				).method(
					"POST"
				).returns(
					BatchResult.class
				).permissionFunction(
					params ->
						hasNestedAddingPermissionFunction.apply(
							unsafeCast(params.get(0)),
							unsafeCast(params.get(1)))
				).permissionProvidedClasses(
					Credentials.class, _getIdClass()
				).executeFunction(
					params -> batchCreatorThrowableHexaFunction.andThen(
						t -> new BatchResult<>(t, _resource.getName())
					).apply(
						_getId(params.get(0)), unsafeCast(params.get(1)),
						unsafeCast(params.get(2)), unsafeCast(params.get(3)),
						unsafeCast(params.get(4)), unsafeCast(params.get(5))
					)
				).bodyFunction(
					form::getList
				).receivesParams(
					_getIdClass(), Body.class, aClass, bClass, cClass, dClass
				).build();

			_actionSemantics.add(batchCreateActionSemantics);

			ActionSemantics createActionSemantics = ActionSemantics.ofResource(
				_resource
			).name(
				"create"
			).method(
				"POST"
			).returns(
				SingleModel.class
			).permissionFunction(
				params -> hasNestedAddingPermissionFunction.apply(
					unsafeCast(params.get(0)), unsafeCast(params.get(1)))
			).permissionProvidedClasses(
				Credentials.class, _getIdClass()
			).executeFunction(
				params -> creatorThrowableHexaFunction.andThen(
					t -> new SingleModelImpl<>(t, _resource.getName())
				).apply(
					_getId(params.get(0)), unsafeCast(params.get(1)),
					unsafeCast(params.get(2)), unsafeCast(params.get(3)),
					unsafeCast(params.get(4)), unsafeCast(params.get(5))
				)
			).bodyFunction(
				form::get
			).receivesParams(
				_getIdClass(), Body.class, aClass, bClass, cClass, dClass
			).build();

			_actionSemantics.add(createActionSemantics);

			return this;
		}

		@Override
		public <A, B, C, D> Builder<T, S, U> addGetter(
			ThrowableHexaFunction<Pagination, U, A, B, C, D, PageItems<T>>
				getterThrowableHexaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			Class<D> dClass) {

			ActionSemantics actionSemantics = ActionSemantics.ofResource(
				_resource
			).name(
				"retrieve"
			).method(
				"GET"
			).returns(
				Page.class
			).permissionFunction(
			).executeFunction(
				params -> getterThrowableHexaFunction.andThen(
					pageItems -> new PageImpl<>(
						_resourceWithParentId((Id)params.get(1)), pageItems,
						(Pagination)params.get(0))
				).apply(
					(Pagination)params.get(0), _getId(params.get(1)),
					unsafeCast(params.get(2)), unsafeCast(params.get(3)),
					unsafeCast(params.get(4)), unsafeCast(params.get(5))
				)
			).receivesParams(
				Pagination.class, _getIdClass(), aClass, bClass, cClass, dClass
			).build();

			_actionSemantics.add(actionSemantics);

			return this;
		}

		@Override
		public NestedCollectionRoutes<T, S, U> build() {
			return new NestedCollectionRoutesImpl<>(this);
		}

		private U _getId(Object object) {
			Resource.Id id = (Resource.Id)object;

			return unsafeCast(id.asObject());
		}

		private Class<?> _getIdClass() {
			if (_resource instanceof Nested) {
				return ParentId.class;
			}

			return GenericParentId.class;
		}

		private Resource _resourceWithParentId(Id id) {
			if (_resource instanceof Nested) {
				Nested nested = (Nested)_resource;

				return nested.withParentId(id);
			}

			GenericParent genericParent = (GenericParent)_resource;

			return genericParent.withParentId(id);
		}

		private <V> List<S> _transformList(
				List<V> list,
				ThrowableFunction<V, T> transformThrowableFunction)
			throws Exception {

			List<S> newList = new ArrayList<>();

			for (V v : list) {
				S s = transformThrowableFunction.andThen(
					_modelToIdentifierFunction::apply
				).apply(
					v
				);

				newList.add(s);
			}

			return newList;
		}

		private final List<ActionSemantics> _actionSemantics =
			new ArrayList<>();
		private final Supplier<Form.Builder> _formBuilderSupplier;
		private final Function<T, S> _modelToIdentifierFunction;
		private final Resource _resource;

	}

	private final List<ActionSemantics> _actionSemantics;

}
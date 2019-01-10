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
import com.liferay.apio.architect.alias.routes.CustomItemFunction;
import com.liferay.apio.architect.alias.routes.DeleteItemConsumer;
import com.liferay.apio.architect.alias.routes.GetItemFunction;
import com.liferay.apio.architect.alias.routes.UpdateItemFunction;
import com.liferay.apio.architect.alias.routes.permission.HasRemovePermissionFunction;
import com.liferay.apio.architect.alias.routes.permission.HasUpdatePermissionFunction;
import com.liferay.apio.architect.annotation.Id;
import com.liferay.apio.architect.consumer.throwable.ThrowablePentaConsumer;
import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.custom.actions.CustomRoute;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.function.throwable.ThrowableHexaFunction;
import com.liferay.apio.architect.function.throwable.ThrowablePentaFunction;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.internal.action.ActionSemantics;
import com.liferay.apio.architect.internal.single.model.SingleModelImpl;
import com.liferay.apio.architect.resource.Resource;
import com.liferay.apio.architect.resource.Resource.Item;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.single.model.SingleModel;

import io.vavr.CheckedRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Alejandro Hernández
 */
public class ItemRoutesImpl<T, S> implements ItemRoutes<T, S> {

	public ItemRoutesImpl(BuilderImpl<T, S> builderImpl) {
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
	public Optional<Map<String, CustomItemFunction<?, S>>>
		getCustomItemFunctionsOptional() {

		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, CustomRoute> getCustomRoutes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<DeleteItemConsumer<S>> getDeleteConsumerOptional() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<Form> getFormOptional() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<GetItemFunction<T, S>> getItemFunctionOptional() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<UpdateItemFunction<T, S>> getUpdateItemFunctionOptional() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the {@code ItemRoutes} of an {@link
	 * com.liferay.apio.architect.router.ItemRouter}.
	 *
	 * @param <T> the model's type
	 * @param <S> the type of the model's identifier (e.g., {@code Long}, {@code
	 *        String}, etc.)
	 */
	public static class BuilderImpl<T, S> implements Builder<T, S> {

		public BuilderImpl(
			Item item, Supplier<Form.Builder> formBuilderSupplier,
			Function<String, Optional<String>> nameFunction) {

			_item = item;
			_formBuilderSupplier = formBuilderSupplier;
			_nameFunction = nameFunction;
		}

		@Override
		public <A, B, C, D, R, U, I extends Identifier<?>> Builder<T, S>
			addCustomRoute(
				CustomRoute customRoute,
				ThrowableHexaFunction<S, R, A, B, C, D, U>
					throwableHexaFunction,
				Class<A> aClass, Class<B> bClass, Class<C> cClass,
				Class<D> dClass, Class<I> identifierClass,
				BiFunction<Credentials, S, Boolean> permissionBiFunction,
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
				_item
			).name(
				customRoute.getName()
			).method(
				customRoute.getMethod()
			).returns(
				SingleModel.class
			).permissionFunction(
				params -> permissionBiFunction.apply(
					unsafeCast(params.get(0)), unsafeCast(params.get(1)))
			).permissionProvidedClasses(
				Credentials.class, Id.class
			).executeFunction(
				params -> throwableHexaFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _getResourceName(identifierClass))
				).apply(
					_getId(params.get(0)), unsafeCast(params.get(1)),
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
				Id.class, bodyClass, aClass, bClass, cClass, dClass
			).build();

			_actionSemantics.add(createActionSemantics);

			return this;
		}

		@Override
		public <A, B, C, D> Builder<T, S> addGetter(
			ThrowablePentaFunction<S, A, B, C, D, T>
				getterThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			Class<D> dClass) {

			ActionSemantics actionSemantics = ActionSemantics.ofResource(
				_item
			).name(
				"retrieve"
			).method(
				"GET"
			).returns(
				SingleModel.class
			).permissionFunction(
			).executeFunction(
				params -> getterThrowablePentaFunction.andThen(
					t -> new SingleModelImpl<>(t, _item.getName())
				).apply(
					_getId(params.get(0)), unsafeCast(params.get(1)),
					unsafeCast(params.get(2)), unsafeCast(params.get(3)),
					unsafeCast(params.get(4))
				)
			).receivesParams(
				Id.class, aClass, bClass, cClass, dClass
			).build();

			_actionSemantics.add(actionSemantics);

			return this;
		}

		@Override
		public <A, B, C, D> Builder<T, S> addRemover(
			ThrowablePentaConsumer<S, A, B, C, D> removerThrowablePentaConsumer,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasRemovePermissionFunction<S> hasRemovePermissionFunction) {

			ActionSemantics actionSemantics = ActionSemantics.ofResource(
				_item
			).name(
				"remove"
			).method(
				"DELETE"
			).returns(
				Void.class
			).permissionFunction(
				params -> hasRemovePermissionFunction.apply(
					unsafeCast(params.get(0)), unsafeCast(params.get(1)))
			).permissionProvidedClasses(
				Credentials.class, Id.class
			).executeFunction(
				params -> _run(
					() -> removerThrowablePentaConsumer.accept(
						_getId(params.get(0)), unsafeCast(params.get(1)),
						unsafeCast(params.get(2)), unsafeCast(params.get(3)),
						unsafeCast(params.get(4))))
			).receivesParams(
				Id.class, aClass, bClass, cClass, dClass
			).build();

			_actionSemantics.add(actionSemantics);

			return this;
		}

		@Override
		public <A, B, C, D, R> Builder<T, S> addUpdater(
			ThrowableHexaFunction<S, R, A, B, C, D, T>
				updaterThrowableHexaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasUpdatePermissionFunction<S> hasUpdatePermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			Form form = formBuilderFunction.apply(
				unsafeCast(_formBuilderSupplier.get()));

			ActionSemantics actionSemantics = ActionSemantics.ofResource(
				_item
			).name(
				"replace"
			).method(
				"PUT"
			).returns(
				SingleModel.class
			).permissionFunction(
				params -> hasUpdatePermissionFunction.apply(
					unsafeCast(params.get(0)), unsafeCast(params.get(1)))
			).permissionProvidedClasses(
				Credentials.class, Id.class
			).executeFunction(
				params -> updaterThrowableHexaFunction.andThen(
					t -> new SingleModelImpl<>(t, _item.getName())
				).apply(
					_getId(params.get(0)), unsafeCast(params.get(1)),
					unsafeCast(params.get(2)), unsafeCast(params.get(3)),
					unsafeCast(params.get(4)), unsafeCast(params.get(5))
				)
			).bodyFunction(
				form::get
			).receivesParams(
				Id.class, Body.class, aClass, bClass, cClass, dClass
			).build();

			_actionSemantics.add(actionSemantics);

			return this;
		}

		@Override
		public ItemRoutes<T, S> build() {
			return new ItemRoutesImpl<>(this);
		}

		private S _getId(Object object) {
			Resource.Id id = (Resource.Id)object;

			return unsafeCast(id.asObject());
		}

		private <I extends Identifier<?>> String _getResourceName(
			Class<I> identifierClass) {

			return _nameFunction.apply(
				identifierClass.getName()
			).orElse(
				null
			);
		}

		private Void _run(CheckedRunnable checkedRunnable) throws Throwable {
			checkedRunnable.run();

			return null;
		}

		private final List<ActionSemantics> _actionSemantics =
			new ArrayList<>();
		private final Supplier<Form.Builder> _formBuilderSupplier;
		private final Item _item;
		private final Function<String, Optional<String>> _nameFunction;

	}

	private final List<ActionSemantics> _actionSemantics;

}
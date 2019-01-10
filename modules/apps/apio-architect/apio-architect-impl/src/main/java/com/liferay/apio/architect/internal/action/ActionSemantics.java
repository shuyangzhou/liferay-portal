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

package com.liferay.apio.architect.internal.action;

import static java.util.Collections.unmodifiableList;

import com.liferay.apio.architect.annotation.Id;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.internal.alias.ProvideFunction;
import com.liferay.apio.architect.internal.annotation.Action;
import com.liferay.apio.architect.operation.HTTPMethod;
import com.liferay.apio.architect.resource.Resource;

import io.vavr.CheckedFunction1;
import io.vavr.control.Try;

import java.lang.annotation.Annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.ForbiddenException;

/**
 * Instances of this class contains semantic information about an action like
 *
 * @author Alejandro Hernández
 * @review
 */
public final class ActionSemantics {

	/**
	 * Starts creating a new {@link ActionSemantics} by providing information
	 * about the action's resource.
	 *
	 * @review
	 */
	public static NameStep ofResource(Resource resource) {
		ActionSemantics actionSemantics = new ActionSemantics();

		actionSemantics._resource = resource;

		return new Builder(actionSemantics);
	}

	/**
	 * Executes the permission function with the provided params to check if we
	 * have permissions to execute an action
	 *
	 * @review
	 */
	public Boolean checkPermissions(List<?> params) throws Throwable {
		return _permissionFunction.apply(params);
	}

	/**
	 * Executes the action with the provided params.
	 *
	 * @review
	 */
	public Object execute(List<?> params) throws Throwable {
		return _executeFunction.apply(params);
	}

	/**
	 * The action's name.
	 *
	 * @review
	 */
	public String getActionName() {
		return _name;
	}

	/**
	 * The list of annotations.
	 *
	 * @review
	 */
	public List<Annotation> getAnnotations() {
		return unmodifiableList(_annotations);
	}

	/**
	 * Returns the transformed body value needed for the action, if needed.
	 * Returns {@code null} otherwise.
	 *
	 * @review
	 */
	public Object getBodyValue(Body body) {
		if (_bodyFunction == null) {
			return null;
		}

		return _bodyFunction.apply(body);
	}

	/**
	 * The method in which the action is executed.
	 *
	 * @review
	 */
	public String getHTTPMethod() {
		return _method;
	}

	/**
	 * The list of param classes.
	 *
	 * @review
	 */
	public List<Class<?>> getParamClasses() {
		return unmodifiableList(_paramClasses);
	}

	public List<Object> getParams(Function<Class<?>, Object> provideFunction) {
		Stream<Class<?>> stream = getParamClasses().stream();

		return stream.map(
			provideFunction
		).collect(
			Collectors.toList()
		);
	}

	public List<Object> getPermissionParams(
		Function<Class<?>, Object> provideFunction) {

		Stream<Class<?>> stream = getPermissionProvidedClasses().stream();

		return stream.map(
			provideFunction
		).map(
			param -> {
				if (param instanceof Resource.Id) {
					return ((Resource.Id)param).asObject();
				}

				return param;
			}
		).collect(
			Collectors.toList()
		);
	}

	/**
	 * The list of permission classes.
	 *
	 * @review
	 */
	public List<Class<?>> getPermissionProvidedClasses() {
		return unmodifiableList(_permissionProvidedClasses);
	}

	/**
	 * The action's resource.
	 *
	 * @review
	 */
	public Resource getResource() {
		return _resource;
	}

	/**
	 * The class returned by the action.
	 *
	 * @review
	 */
	public Class<?> getReturnClass() {
		return _returnClass;
	}

	/**
	 * Transforms this {@link ActionSemantics} instance into its {@link Action}.
	 *
	 * @param  provideFunction the function used to provide instances of action
	 *         params
	 * @review
	 */
	public Action toAction(ProvideFunction provideFunction) {
		return request -> Try.of(
			() -> getPermissionParams(provideFunction.apply(this, request))
		).mapTry(
			this::checkPermissions
		).filter(
			aBoolean -> aBoolean
		).<Try<List<?>>>transform(
			aTry -> {
				if (aTry.isSuccess()) {
					return Try.of(
						() -> getParams(provideFunction.apply(this, request)));
				}

				return Try.failure(new ForbiddenException());
			}
		).mapTry(
			this::execute
		);
	}

	/**
	 * Copies the current {@link ActionSemantics} by setting a value for the
	 * {@link ActionSemantics#getAnnotations()} annotations} attribute. A
	 * shallow reference equality check is used to prevent copying of the same
	 * value by returning {@code this}.
	 *
	 * @param  annotations the new annotations list
	 * @return A modified copy of {@code this} object
	 * @review
	 */
	public ActionSemantics withAnnotations(List<Annotation> annotations) {
		if (_annotations.equals(annotations)) {
			return this;
		}

		ActionSemantics actionSemantics = new ActionSemantics();

		actionSemantics._annotations = annotations;
		actionSemantics._bodyFunction = _bodyFunction;
		actionSemantics._executeFunction = _executeFunction;
		actionSemantics._method = _method;
		actionSemantics._name = _name;
		actionSemantics._paramClasses = _paramClasses;
		actionSemantics._permissionFunction = _permissionFunction;
		actionSemantics._permissionProvidedClasses = _permissionProvidedClasses;
		actionSemantics._resource = _resource;
		actionSemantics._returnClass = _returnClass;

		return actionSemantics;
	}

	/**
	 * Copies the current {@link ActionSemantics} by setting a value for the
	 * {@link ActionSemantics#getHTTPMethod()} method} attribute. A shallow
	 * reference equality check is used to prevent copying of the same value by
	 * returning {@code this}.
	 *
	 * @param  method the new method
	 * @return A modified copy of {@code this} object
	 * @review
	 */
	public ActionSemantics withMethod(String method) {
		if (_method.equals(method)) {
			return this;
		}

		ActionSemantics actionSemantics = new ActionSemantics();

		actionSemantics._annotations = _annotations;
		actionSemantics._bodyFunction = _bodyFunction;
		actionSemantics._executeFunction = _executeFunction;
		actionSemantics._method = method;
		actionSemantics._name = _name;
		actionSemantics._paramClasses = _paramClasses;
		actionSemantics._permissionFunction = _permissionFunction;
		actionSemantics._permissionProvidedClasses = _permissionProvidedClasses;
		actionSemantics._resource = _resource;
		actionSemantics._returnClass = _returnClass;

		return actionSemantics;
	}

	/**
	 * Copies the current {@link ActionSemantics} by setting a value for the
	 * {@link ActionSemantics#getActionName()} name} attribute. A shallow
	 * reference equality check is used to prevent copying of the same value by
	 * returning {@code this}.
	 *
	 * @param  name the new name
	 * @return A modified copy of {@code this} object
	 * @review
	 */
	public ActionSemantics withName(String name) {
		if (_name.equals(name)) {
			return this;
		}

		ActionSemantics actionSemantics = new ActionSemantics();

		actionSemantics._annotations = _annotations;
		actionSemantics._bodyFunction = _bodyFunction;
		actionSemantics._executeFunction = _executeFunction;
		actionSemantics._method = _method;
		actionSemantics._name = name;
		actionSemantics._paramClasses = _paramClasses;
		actionSemantics._permissionFunction = _permissionFunction;
		actionSemantics._permissionProvidedClasses = _permissionProvidedClasses;
		actionSemantics._resource = _resource;
		actionSemantics._returnClass = _returnClass;

		return actionSemantics;
	}

	/**
	 * Copies the current {@link ActionSemantics} by setting a value for the
	 * {@link ActionSemantics#getResource() resource} attribute. A shallow
	 * reference equality check is used to prevent copying of the same value by
	 * returning {@code this}.
	 *
	 * @param  resource the new resource
	 * @return A modified copy of {@code this} object
	 * @review
	 */
	public ActionSemantics withResource(Resource resource) {
		ActionSemantics actionSemantics = new ActionSemantics();

		actionSemantics._annotations = _annotations;
		actionSemantics._bodyFunction = _bodyFunction;
		actionSemantics._executeFunction = _executeFunction;
		actionSemantics._method = _method;
		actionSemantics._name = _name;
		actionSemantics._paramClasses = _paramClasses;
		actionSemantics._permissionFunction = _permissionFunction;
		actionSemantics._permissionProvidedClasses = _permissionProvidedClasses;
		actionSemantics._resource = resource;
		actionSemantics._returnClass = _returnClass;

		return actionSemantics;
	}

	/**
	 * Copies the current {@link ActionSemantics} by setting a value for the
	 * {@link ActionSemantics#getReturnClass()} return class} attribute. A
	 * shallow reference equality check is used to prevent copying of the same
	 * value by returning {@code this}.
	 *
	 * @param  returnClass the new return class
	 * @return A modified copy of {@code this} object
	 * @review
	 */
	public ActionSemantics withReturnClass(Class<?> returnClass) {
		if (_returnClass.equals(returnClass)) {
			return this;
		}

		ActionSemantics actionSemantics = new ActionSemantics();

		actionSemantics._annotations = _annotations;
		actionSemantics._bodyFunction = _bodyFunction;
		actionSemantics._executeFunction = _executeFunction;
		actionSemantics._method = _method;
		actionSemantics._name = _name;
		actionSemantics._paramClasses = _paramClasses;
		actionSemantics._permissionFunction = _permissionFunction;
		actionSemantics._permissionProvidedClasses = _permissionProvidedClasses;
		actionSemantics._resource = _resource;
		actionSemantics._returnClass = returnClass;

		return actionSemantics;
	}

	public static class Builder
		implements NameStep, MethodStep, ReturnStep, PermissionStep,
				   ExecuteStep, FinalStep {

		public Builder(ActionSemantics actionSemantics) {
			_actionSemantics = actionSemantics;
		}

		@Override
		public FinalStep annotatedWith(Annotation annotation) {
			_actionSemantics._annotations.add(annotation);

			return this;
		}

		@Override
		public FinalStep annotatedWith(Annotation... annotations) {
			_actionSemantics._annotations = Arrays.asList(annotations);

			return this;
		}

		@Override
		public FinalStep bodyFunction(Function<Body, Object> bodyFunction) {
			_actionSemantics._bodyFunction = bodyFunction;

			return this;
		}

		@Override
		public ActionSemantics build() {
			return _actionSemantics;
		}

		@Override
		public FinalStep executeFunction(
			CheckedFunction1<List<?>, ?> executeFunction) {

			_actionSemantics._executeFunction = executeFunction;

			return this;
		}

		@Override
		public ReturnStep method(String method) {
			_actionSemantics._method = method;

			return this;
		}

		@Override
		public MethodStep name(String name) {
			_actionSemantics._name = name;

			return this;
		}

		@Override
		public ExecuteStep permissionFunction() {
			_actionSemantics._permissionFunction = params -> true;

			return this;
		}

		@Override
		public ExecuteStep permissionFunction(
			CheckedFunction1<List<?>, Boolean> permissionFunction) {

			_actionSemantics._permissionFunction = permissionFunction;

			return this;
		}

		@Override
		public ExecuteStep permissionProvidedClasses(Class<?>... classes) {
			_actionSemantics._permissionProvidedClasses = Arrays.asList(
				classes);

			return this;
		}

		@Override
		public FinalStep receivesParams(Class<?>... classes) {
			_actionSemantics._paramClasses = Arrays.asList(classes);

			return this;
		}

		@Override
		public PermissionStep returns(Class<?> returnClass) {
			_actionSemantics._returnClass = returnClass;

			return this;
		}

		private final ActionSemantics _actionSemantics;

	}

	public interface ExecuteStep {

		/**
		 * Provides information about the function action's execute function.
		 * This function receives the list of params in the order provided in
		 * the {@link FinalStep#receivesParams(Class[])} method.
		 *
		 * @review
		 */
		public FinalStep executeFunction(
			CheckedFunction1<List<?>, ?> executeFunction);

		/**
		 * Provides information about the permission method arguments. This
		 * function receives the list of param classes to provide to the
		 * permissionFunction
		 *
		 * @review
		 */
		public ExecuteStep permissionProvidedClasses(Class<?>... classes);

	}

	public interface FinalStep {

		/**
		 * Provides information about the params needed by the action.
		 *
		 * <p>
		 * The param instances will be provided in the {@link #execute(List)} in
		 * the same order as their classes in this method. {@link Void} classes
		 * will be ignored (will be provided as {@code null}. For the {@link Id}
		 * or {@link com.liferay.apio.architect.annotation.ParentId} params, the
		 * annotation class should be provided to the list.
		 * </p>
		 *
		 * @review
		 */
		public FinalStep annotatedWith(Annotation annotations);

		/**
		 * Provides information about the params needed by the action.
		 *
		 * <p>
		 * The param instances will be provided in the {@link #execute(List)} in
		 * the same order as their classes in this method. {@link Void} classes
		 * will be ignored (will be provided as {@code null}. For the {@link Id}
		 * or {@link com.liferay.apio.architect.annotation.ParentId} params, the
		 * annotation class should be provided to the list.
		 * </p>
		 *
		 * @review
		 */
		public FinalStep annotatedWith(Annotation... annotations);

		/**
		 * Provides information about the function used to transform the body
		 * value into the object needed by the action.
		 *
		 * <p>
		 * If the action does not need information from the body, this method
		 * shouldn't be called.
		 * </p>
		 *
		 * @review
		 */
		public FinalStep bodyFunction(Function<Body, Object> bodyFunction);

		/**
		 * Creates the {@link ActionSemantics} object with the information
		 * provided to the builder.
		 *
		 * @review
		 */
		public ActionSemantics build();

		/**
		 * Provides information about the params needed by the action.
		 *
		 * <p>
		 * The param instances will be provided in the {@link #execute(List)} in
		 * the same order as their classes in this method. {@link Void} classes
		 * will be ignored (will be provided as {@code null}. For the {@link Id}
		 * or {@link com.liferay.apio.architect.annotation.ParentId} params, the
		 * annotation class should be provided to the list.
		 * </p>
		 *
		 * @review
		 */
		public FinalStep receivesParams(Class<?>... classes);

	}

	public interface MethodStep {

		/**
		 * Provides information about the method in which the action is
		 * executed.
		 *
		 * @review
		 */
		public default ReturnStep method(HTTPMethod httpMethod) {
			return method(httpMethod.name());
		}

		/**
		 * Provides information about the method in which the action is
		 * executed.
		 *
		 * @review
		 */
		public ReturnStep method(String method);

	}

	public interface NameStep {

		/**
		 * Provides information about the the action's name.
		 *
		 * @review
		 */
		public MethodStep name(String name);

	}

	public interface PermissionStep {

		/**
		 * Default empty implementation of the permission function.
		 *
		 * @review
		 */
		public ExecuteStep permissionFunction();

		/**
		 * Provides information about the permission function to check if the
		 * execute function has permissions to be executed.
		 *
		 * @review
		 */
		public ExecuteStep permissionFunction(
			CheckedFunction1<List<?>, Boolean> permissionFunction);

	}

	public interface ReturnStep {

		/**
		 * Provides information about the class returned by the action.
		 *
		 * @review
		 */
		public PermissionStep returns(Class<?> returnClass);

	}

	private List<Annotation> _annotations = new ArrayList<>();
	private Function<Body, Object> _bodyFunction;
	private CheckedFunction1<List<?>, ?> _executeFunction;
	private String _method;
	private String _name;
	private List<Class<?>> _paramClasses = new ArrayList<>();
	private CheckedFunction1<List<?>, Boolean> _permissionFunction;
	private List<Class<?>> _permissionProvidedClasses = new ArrayList<>();
	private Resource _resource;
	private Class<?> _returnClass;

}
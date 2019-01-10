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

package com.liferay.apio.architect.internal.annotation.util;

import static com.liferay.apio.architect.internal.annotation.representor.StringUtil.toLowercaseSlug;
import static com.liferay.apio.architect.internal.annotation.util.AnnotationUtil.findAnnotationInAnyParameter;
import static com.liferay.apio.architect.internal.annotation.util.AnnotationUtil.findAnnotationInMethodOrInItsAnnotations;

import static io.leangen.geantyref.GenericTypeReflector.getTypeParameter;

import static java.util.Objects.nonNull;

import com.liferay.apio.architect.annotation.GenericParentId;
import com.liferay.apio.architect.annotation.Id;
import com.liferay.apio.architect.annotation.ParentId;
import com.liferay.apio.architect.annotation.Permissions.HasPermission;
import com.liferay.apio.architect.annotation.Vocabulary.Type;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.internal.pagination.PageImpl;
import com.liferay.apio.architect.internal.pagination.PaginationImpl;
import com.liferay.apio.architect.internal.single.model.SingleModelImpl;
import com.liferay.apio.architect.pagination.Page;
import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.resource.Resource;
import com.liferay.apio.architect.resource.Resource.GenericParent;
import com.liferay.apio.architect.resource.Resource.Item;
import com.liferay.apio.architect.resource.Resource.Nested;
import com.liferay.apio.architect.resource.Resource.Paged;
import com.liferay.apio.architect.single.model.SingleModel;

import io.vavr.CheckedFunction1;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Provides utility functions for transforming classes annotated with actions
 * (Retrieve, Delete...) into {@link
 * com.liferay.apio.architect.internal.action.ActionSemantics}.
 *
 * <p>This class should not be instantiated.
 *
 * @author Alejandro Hernández
 * @author Javier Gamarra
 * @review
 */
public final class ActionRouterUtil {

	/**
	 * Executes the provided {@code actionExecuteFunction} and returns its
	 * result. An updated version of the provided param list is used, with all
	 * {@link Resource.Id} values transformed with {@link
	 * Resource.Id#asObject()}. After executing the function an updated result
	 * is returned:
	 *
	 * <p>
	 * If the element is {@code null}, {@code null} will be returned.
	 * </p>
	 *
	 * <p>
	 * If the element is a {@code List} or a {@code PageItems}, it will be
	 * transformed to a {@link Page}.
	 * </p>
	 *
	 * <p>
	 * Otherwise, it gets wrapped in a {@code SingleModel}.
	 * </p>
	 *
	 * @param  resource the action's resource
	 * @param  params the action's params
	 * @param  actionExecuteFunction the function used to execute the action
	 * @return the action's result updated to a type that Apio understands
	 * @throws Throwable if the action throws any exception, its cause is thrown
	 *         instead
	 * @review
	 */
	public static Object execute(
			Resource resource, List<?> params,
			CheckedFunction1<Object[], Object> actionExecuteFunction)
		throws Throwable {

		Object[] updatedParams = params.toArray(new Object[0]);

		for (int i = 0; i < params.size(); i++) {
			if (updatedParams[i] instanceof Resource.Id) {
				Resource.Id id = (Resource.Id)updatedParams[i];

				updatedParams[i] = id.asObject();

				if (resource instanceof Item) {
					resource = ((Item)resource).withId(id);
				}

				if (resource instanceof Nested) {
					resource = ((Nested)resource).withParentId(id);
				}

				if (resource instanceof GenericParent) {
					resource = ((GenericParent)resource).withParentId(id);
				}
			}
		}

		try {
			Object result = actionExecuteFunction.apply(updatedParams);

			if (result == null) {
				return null;
			}

			if (result instanceof List) {
				List<?> list = (List<?>)result;

				PageItems<?> pageItems = new PageItems<>(list, list.size());

				Pagination pagination = new PaginationImpl(list.size(), 1);

				return new PageImpl<>(resource, pageItems, pagination);
			}

			if (result instanceof PageItems) {
				PageItems<?> pageItems = (PageItems<?>)result;

				for (Object param : params) {
					if (param instanceof Pagination) {
						return new PageImpl<>(
							resource, pageItems, (Pagination)param);
					}
				}

				Pagination pagination = new PaginationImpl(
					pageItems.getTotalCount(), 1);

				return new PageImpl<>(resource, pageItems, pagination);
			}

			return new SingleModelImpl<>(result, resource.getName());
		}
		catch (Throwable throwable) {
			if (nonNull(throwable.getCause())) {
				throw throwable.getCause();
			}

			throw throwable;
		}
	}

	/**
	 * Finds the permission method associated with a an action
	 *
	 * @param  actionRouterClass the class to find the permission method
	 * @param  resourceClass the class of resource
	 * @param  actionName the name of the action to search
	 * @param  httpMethod the http method of the action to search
	 * @return an optional containing the method or empty otherwise
	 * @review
	 */
	public static Optional<Method> findPermissionMethodOptional(
		Class actionRouterClass, Class<? extends Resource> resourceClass,
		String actionName, String httpMethod) {

		return Stream.of(
			actionRouterClass.getMethods()
		).filter(
			method -> {
				HasPermission hasPermission =
					findAnnotationInMethodOrInItsAnnotations(
						method, HasPermission.class);

				return _matchesPermission(
					actionName, httpMethod, hasPermission, resourceClass,
					method);
			}
		).findFirst();
	}

	/**
	 * Returns the name of the class that must be provided from the HTTP body.
	 *
	 * <p>
	 * This class is requested by annotating with {@link
	 * com.liferay.apio.architect.annotation.Body} a parameter that must be
	 * either of a class annotated with {@link Type} or a list of elements of a
	 * class annotated with {@link Type}.
	 *
	 * <p>
	 * For example, having a class {@code MyType}, annotated with {@link Type},
	 * executing this function for this two methods:
	 * <pre>
	 * {@code
	 *    public MyType create(@Body MyType myType);
	 *    public List<MyType> createList(@Body List<MyType> myType);
	 * }
	 * </pre><p> Will return the class name of {@code MyType}.
	 *
	 * <p>
	 * Otherwise, if a parameter annotated with {@link
	 * com.liferay.apio.architect.annotation.Body}
	 * cannot be found or its class is not annotated with {@link Type}, {@code
	 * <code>null</code>} will be returned.
	 *
	 * @param  method the method being analyzed. It must contain a parameter
	 *         annotated with {@link com.liferay.apio.architect.annotation.Body}
	 * @return the name of the class that must be provided from the HTTP body,
	 *         if present; {@code null} otherwise
	 * @review
	 */
	public static String getBodyResourceClassName(Method method) {
		for (Parameter parameter : method.getParameters()) {
			if (parameter.isAnnotationPresent(_BODY_ANNOTATION)) {
				Class<?> parameterType = parameter.getType();

				if (!List.class.isAssignableFrom(parameterType)) {
					return parameterType.getName();
				}

				AnnotatedType typeParameter = getTypeParameter(
					parameter.getAnnotatedType(),
					List.class.getTypeParameters()[0]);

				if (Class.class.isInstance(typeParameter.getType())) {
					return ((Class)typeParameter.getType()).getName();
				}
			}
		}

		return null;
	}

	/**
	 * Returns an array containing the classes that must be provided as an
	 * action's method parameters. The parameter type is stored in the same
	 * position and with the same class as the method declaration, with the
	 * following exceptions:
	 *
	 * <p>If the parameter is annotated with {@link Id}, the {@link Id} class is
	 * stored.
	 * <p>If the parameter is annotated with {@link ParentId}, the {@link
	 * ParentId} class is stored.
	 * <p>If the parameter is annotated with {@link
	 * com.liferay.apio.architect.annotation.Body}, the {@link Body} class is
	 * stored.
	 *
	 * @param  method the method being analyzed
	 * @return an array with the classes that must be provided as an action's
	 *         method parameters
	 * @review
	 */
	public static Class<?>[] getParamClasses(Method method) {
		return Stream.of(
			method.getParameters()
		).map(
			parameter -> {
				if (parameter.getAnnotation(Id.class) != null) {
					return Id.class;
				}
				else if (parameter.getAnnotation(ParentId.class) != null) {
					return ParentId.class;
				}
				else if (parameter.getAnnotation(GenericParentId.class) !=
							null) {

					return GenericParentId.class;
				}
				else if (parameter.getAnnotation(_BODY_ANNOTATION) != null) {
					return Body.class;
				}

				return parameter.getType();
			}
		).toArray(
			Class[]::new
		);
	}

	/**
	 * Returns the {@link Resource} to which the action described by the
	 * provided method belongs to.
	 *
	 * @param  method the action's method
	 * @param  name the name of the resource, extracted from the {@link
	 *         ActionRouter}
	 * @return the action's {@link Resource}
	 * @review
	 */
	public static Resource getResource(Method method, String name) {
		Optional<Parameter> optionalParameter = Stream.of(
			method.getParameters()
		).filter(
			parameter -> parameter.isAnnotationPresent(GenericParentId.class)
		).findFirst();

		if (optionalParameter.isPresent()) {
			Parameter parameter = optionalParameter.get();

			Class<?> identifierClass = parameter.getType();

			String parentName = toLowercaseSlug(
				identifierClass.getSimpleName());

			return GenericParent.of(parentName, name);
		}

		ParentId parentId = findAnnotationInAnyParameter(
			method, ParentId.class);

		if (parentId != null) {
			Class<? extends Identifier<?>> typeClass = parentId.value();

			Type type = typeClass.getAnnotation(Type.class);

			Item parent = Item.of(toLowercaseSlug(type.value()));

			return Nested.of(parent, name);
		}

		Id id = findAnnotationInAnyParameter(method, Id.class);

		if (id != null) {
			return Item.of(name);
		}

		return Paged.of(name);
	}

	/**
	 * Returns the class of the method's return, updated so JAX-RS writers can
	 * understand it.
	 *
	 * <p>{@link PageItems} and {@link List} are translated to {@link Page}.
	 * <p>{@code void} is translated to {@link Void}.
	 * <p>A class annotated with {@link Type} is translated to {@link
	 * SingleModel}.
	 * <p>Otherwise, the return from {@link Method#getReturnType()} is returned.
	 *
	 * @param  method the method being analyzed
	 * @return the class of the method's return
	 * @review
	 */
	public static Class<?> getReturnClass(Method method) {
		Class<?> returnType = method.getReturnType();

		if (PageItems.class.equals(returnType)) {
			return Page.class;
		}

		if (List.class.equals(returnType)) {
			return Page.class;
		}

		if (returnType.getAnnotation(Type.class) != null) {
			return SingleModel.class;
		}

		if (void.class.equals(returnType)) {
			return Void.class;
		}

		return returnType;
	}

	/**
	 * Checks if a method needing information from body (previously checked with
	 * {@link #needsParameterFromBody(Method)}) needs the body information as a
	 * list of elements or as a single element.
	 *
	 * @param  method the method to check
	 * @return {@code true} if the method needs the body as a list, {@code
	 *         false} otherwise
	 */
	public static boolean isListBody(Method method) {
		for (Parameter parameter : method.getParameters()) {
			if (parameter.isAnnotationPresent(_BODY_ANNOTATION)) {
				return List.class.isAssignableFrom(parameter.getType());
			}
		}

		return false;
	}

	/**
	 * Checks if a method needs a parameter provided from the information in the
	 * HTTP body, by checking if any of its parameters is annotated with {@link
	 * com.liferay.apio.architect.annotation.Body}.
	 *
	 * @param  method the method to check
	 * @return {@code true} if the method needs a parameter provided from the
	 *         HTTP body information; {@code false} otherwise
	 * @review
	 */
	public static boolean needsParameterFromBody(Method method) {
		return nonNull(findAnnotationInAnyParameter(method, _BODY_ANNOTATION));
	}

	private static <A extends Annotation> boolean _isResourceWithAnnotation(
		Class<? extends Resource> resourceClass,
		Class<? extends Resource> routerClass, Method method,
		Class<A> annotationClass) {

		A annotationInAnyParameter = findAnnotationInAnyParameter(
			method, annotationClass);

		if ((annotationInAnyParameter == null) ||
			routerClass.isAssignableFrom(resourceClass)) {

			return true;
		}

		return false;
	}

	private static boolean _matchesPermission(
		String actionName, String httpMethod, HasPermission hasPermission,
		Class<? extends Resource> resourceClass, Method method) {

		if (hasPermission != null) {
			boolean validIdAnnotation = _isResourceWithAnnotation(
				resourceClass, Item.class, method, Id.class);

			boolean validGenericParentIdAnnotation = _isResourceWithAnnotation(
				resourceClass, GenericParent.class, method,
				GenericParentId.class);

			boolean validParentIdAnnotation = _isResourceWithAnnotation(
				resourceClass, Nested.class, method, ParentId.class);

			if (actionName.equals(hasPermission.name()) &&
				httpMethod.equals(hasPermission.httpMethod()) &&
				validIdAnnotation && validGenericParentIdAnnotation &&
				validParentIdAnnotation) {

				return true;
			}
		}

		return false;
	}

	private ActionRouterUtil() {
	}

	private static final Class<com.liferay.apio.architect.annotation.Body>
		_BODY_ANNOTATION = com.liferay.apio.architect.annotation.Body.class;

}
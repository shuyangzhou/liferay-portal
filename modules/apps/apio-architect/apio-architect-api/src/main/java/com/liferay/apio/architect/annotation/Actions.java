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

package com.liferay.apio.architect.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines an annotation that namespaces action annotations. Use one of this
 * interface's nested annotations.
 *
 * @author Alejandro Hernández
 */
public @interface Actions {

	/**
	 * Defines an annotation that indicates a method performs a generic action.
	 * That method must live inside a class implementing {@link
	 * com.liferay.apio.architect.router.ActionRouter}. This annotation can also
	 * be used on other annotations for creating aliases for semantic operations
	 * (see {@link Retrieve}).
	 *
	 * <p>
	 * This annotation has attributes for setting the action's name and HTTP
	 * verb used to execute the action.
	 * </p>
	 *
	 * <p>
	 * If the action will be added to a root collection, the method in which the
	 * annotation is applied should not include an argument annotated with
	 * {@link ID}.
	 * </p>
	 *
	 * <p>
	 * If the action will be added to a nested collection, the method in which
	 * the annotation is applied must include an argument annotated with the
	 * type of the parent's ID via {@link ParentId}.
	 * </p>
	 *
	 * <p>
	 * If the action will be added to an individual item, the method in which
	 * the annotation is applied must include an argument annotated with the
	 * type of the resource's ID via {@link ID}.
	 * </p>
	 *
	 * <p>
	 * If one of the action parameters represents information obtained from the
	 * HTTP request body, that parameter must be annotated with {@link Body}.
	 * </p>
	 *
	 * <p>
	 * The rest of the action parameters will be provided from the request using
	 * the appropriate {@link com.liferay.apio.architect.provider.Provider}.
	 * </p>
	 */
	@Retention(RUNTIME)
	@Target({METHOD, ANNOTATION_TYPE})
	public @interface Action {

		/**
		 * Returns the HTTP method for executing this action.
		 *
		 * @return the HTTP method
		 */
		public String httpMethod();

		/**
		 * Returns the action's name.
		 *
		 * @return the action's name
		 */
		public String name();

	}

	/**
	 * Defines an annotation that indicates a method creates elements. That
	 * method must live inside a class that implements {@link
	 * com.liferay.apio.architect.router.ActionRouter}.
	 */
	@Action(httpMethod = "POST", name = "create")
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Create {
	}

	/**
	 * Indicates that a method performs the action of removing elements.
	 *
	 * <p>
	 * This annotation must be used on a method that lives inside a class
	 * implementing {@link com.liferay.apio.architect.router.ActionRouter}.
	 * </p>
	 *
	 * @review
	 */
	@Action(httpMethod = "DELETE", name = "remove")
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Remove {
	}

	/**
	 * Defines an annotation that indicates a method replaces an element. That
	 * method must live inside a class that implements {@link
	 * com.liferay.apio.architect.router.ActionRouter}.
	 */
	@Action(httpMethod = "PUT", name = "replace")
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Replace {
	}

	/**
	 * Defines an annotation that indicates a method retrieves elements. That
	 * method must live inside a class that implements {@link
	 * com.liferay.apio.architect.router.ActionRouter}.
	 *
	 * <p>
	 * This annotation can be used on methods for retrieving single resources
	 * (e.g., types annotated with {@link Vocabulary.Type}, {@code
	 * java.util.List}, etc.) or paginated resources (e.g., types annotated with
	 * {@link com.liferay.apio.architect.pagination.PageItems}). In the latter
	 * case, the method must contain a {@link
	 * com.liferay.apio.architect.pagination.Pagination} parameter that must be
	 * used to create the {@code PageItems} instance.
	 * </p>
	 */
	@Action(httpMethod = "GET", name = "retrieve")
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Retrieve {
	}

	/**
	 * Defines an annotation that indicates a method updates an element. That
	 * method must live inside a class that implements {@link
	 * com.liferay.apio.architect.router.ActionRouter}.
	 *
	 * @review
	 */
	@Action(httpMethod = "PATCH", name = "update")
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Update {
	}

}
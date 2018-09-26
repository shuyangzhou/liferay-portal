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
 * This annotation provides namespace for action annotations. Use one of the
 * nested annotations.
 *
 * @author Alejandro Hernández
 * @review
 */
public @interface Actions {

	/**
	 * Indicates that a method performs a generic action.
	 *
	 * <p>
	 * The annotation can also be used on another annotations for creating alias
	 * for semantic operations (see {@link Retrieve}).
	 * </p>
	 *
	 * <p>
	 * If used on a method, it must live inside a class implementing {@link
	 * com.liferay.apio.architect.router.ActionRouter}.
	 * </p>
	 *
	 * <p>
	 * Annotation has attributes for setting the name for the action as well as
	 * indicating the HTTP verb that must be used to execute this action.
	 * </p>
	 *
	 * <p>
	 * If the action is meant to be added on a root collection the method in
	 * which the annotation is applied should not include an argument annotated
	 * with {@link ID}.
	 * </p>
	 *
	 * <p>
	 * If the action is meant to be added on a nested collection the method in
	 * which the annotation is applied must include an argument annotated with
	 * {@link ParentId} with the type of the parent's ID.
	 * </p>
	 *
	 * <p>
	 * If the action is meant to be added on an individual item the method in
	 * which the annotation is applied must include an argument annotated with
	 * {@link ID} with the type of the resource's ID.
	 * </p>
	 *
	 * <p>
	 * If one of the action parameters represent information obtained from the
	 * HTTP request body, that parameter must be annotated with {@link Body}.
	 * </p>
	 *
	 * <p>
	 * The rest of the action parameters will be provided from the request using
	 * the appropriate {@link com.liferay.apio.architect.provider.Provider}.
	 * </p>
	 *
	 * @review
	 */
	@Retention(RUNTIME)
	@Target({METHOD, ANNOTATION_TYPE})
	public @interface Action {

		/**
		 * Returns the HTTP method for executing this action.
		 *
		 * @return the HTTP method for executing this action
		 * @review
		 */
		public String httpMethod();

		/**
		 * Returns the name of the action.
		 *
		 * @return the name of the action
		 * @review
		 */
		public String name();

	}

	/**
	 * Indicates that a method performs the action of creating elements.
	 *
	 * <p>
	 * This annotation must be used on a method that lives inside a class
	 * implementing {@link com.liferay.apio.architect.router.ActionRouter}.
	 * </p>
	 *
	 * @review
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
	 * Indicates that a method performs the action of replacing an element.
	 *
	 * <p>
	 * This annotation must be used on a method that lives inside a class
	 * implementing {@link com.liferay.apio.architect.router.ActionRouter}.
	 * </p>
	 *
	 * @review
	 */
	@Action(httpMethod = "PUT", name = "replace")
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Replace {
	}

	/**
	 * Indicates that a method performs the action of retrieving elements.
	 *
	 * <p>
	 * The annotation can be used on methods for retrieving single resources
	 * (types annotated with {@link Vocabulary.Type}, {@link java.util.List},
	 * or, for paginated resources, {@link
	 * com.liferay.apio.architect.pagination.PageItems}. In the latter case, the
	 * method must contain a {@link
	 * com.liferay.apio.architect.pagination.Pagination} parameter that must be
	 * used to create the {@code PageItems} instance.
	 * </p>
	 *
	 * <p>
	 * This annotation must be used on a method that lives inside a class
	 * implementing {@link com.liferay.apio.architect.router.ActionRouter}.
	 * </p>
	 *
	 * @review
	 */
	@Action(httpMethod = "GET", name = "retrieve")
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Retrieve {
	}

}
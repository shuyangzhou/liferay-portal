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
 * Defines an annotation that namespaces permission annotations. Use one of this
 * interface's permission annotations.
 *
 * @author Javier Gamarra
 * @review
 */
public @interface Permissions {

	/**
	 * Defines a permission to check a method that creates elements. That method
	 * must live inside a class that implements {@link
	 * com.liferay.apio.architect.router.ActionRouter}.
	 *
	 * @review
	 */
	@HasPermission(httpMethod = "POST", name = "create")
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface CanCreate {
	}

	/**
	 * Defines a permission to check a method that removes elements. That method
	 * must live inside a class that implements {@link
	 * com.liferay.apio.architect.router.ActionRouter}.
	 *
	 * @review
	 */
	@HasPermission(httpMethod = "DELETE", name = "remove")
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface CanRemove {
	}

	/**
	 * Defines a permission to check a method that replaces an element. That
	 * method must live inside a class that implements {@link
	 * com.liferay.apio.architect.router.ActionRouter}.
	 *
	 * @review
	 */
	@HasPermission(httpMethod = "PUT", name = "replace")
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface CanReplace {
	}

	/**
	 * Defines a permission to check a method that retrieves elements. That
	 * method must live inside a class that implements {@link
	 * com.liferay.apio.architect.router.ActionRouter}.
	 *
	 * @review
	 */
	@HasPermission(httpMethod = "GET", name = "retrieve")
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface CanRetrieve {
	}

	/**
	 * Defines a permission to check a method that updates elements. That method
	 * must live inside a class that implements {@link
	 * com.liferay.apio.architect.router.ActionRouter}.
	 *
	 * @review
	 */
	@HasPermission(httpMethod = "PATCH", name = "update")
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface CanUpdate {
	}

	/**
	 * Defines an annotation that indicates a method performs a permission check
	 * before an action.
	 *
	 * That method must live inside a class implementing {@link
	 * com.liferay.apio.architect.router.ActionRouter}. This annotation can also
	 * be used on other annotations for creating aliases for semantic permission
	 * checks
	 * (see {@link CanRetrieve}).
	 *
	 * @review
	 */
	@Retention(RUNTIME)
	@Target({METHOD, ANNOTATION_TYPE})
	public @interface HasPermission {

		/**
		 * Returns the HTTP method for executing this action.
		 *
		 * @return the HTTP method
		 */
		public String httpMethod();

		/**
		 * Returns the permission's name.
		 *
		 * @return the permission's name
		 */
		public String name();

	}

}
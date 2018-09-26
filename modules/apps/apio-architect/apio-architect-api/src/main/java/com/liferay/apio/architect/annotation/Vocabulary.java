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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.liferay.apio.architect.identifier.Identifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation provides namespace for vocabulary annotations. Use one of the
 * nested annotation.
 *
 * @author Alejandro Hernández
 */
public @interface Vocabulary {

	/**
	 * Provides information about a field. This annotation should always be used
	 * on an interface method.
	 *
	 * <p>
	 * Annotation has attributes to customize the schema URL (<a
	 * href="https://schema.org">schema.org</a> by default) and the description
	 * of the field (in the case the field is a custom one).
	 * </p>
	 *
	 * @review
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Field {
		/**
		 * Returns the field description, if it is a custom one. Returns empty
		 * string otherwise.
		 *
		 * @return the field description, if it is a custom one; empty string
		 *         otherwise
		 * @review
		 */
		public String description() default "";

		/**
		 * Returns {@code true} if a field should only be used when representing
		 * the type.
		 *
		 * <p>
		 * If this attribute is {@code true}, it will be ignored when
		 * instantiating the interface out of the HTTP request body.
		 * </p>
		 *
		 * <p>
		 * Opposite attribute to {@link #writeOnly()} ()}.
		 * </p>
		 *
		 * @see    #writeOnly()
		 * @review
		 */
		public boolean readOnly() default false;

		/**
		 * Returns the field's schema URL
		 *
		 * @return the field's schema URL
		 * @review
		 */
		public String schemaURL() default "https://www.schema.org/";

		/**
		 * Returns the field's name
		 *
		 * @return the field's name
		 * @review
		 */
		public String value();

		/**
		 * Returns {@code true} if a field should only be used when
		 * instantiating the interface out of the HTTP request body.
		 *
		 * <p>
		 * If this attribute is {@code true}, it will be ignored when
		 * representing the type in any format.
		 * </p>
		 *
		 * <p>
		 * Opposite attribute to {@link #readOnly()}.
		 * </p>
		 *
		 * @see    #readOnly()
		 * @review
		 */
		public boolean writeOnly() default false;

	}

	/**
	 * Indicates that a field should be expressed as a link to another resource.
	 * For this to be possible, the method must provide information about
	 * another resource's ID.
	 *
	 * <p>
	 * Annotation has an attribute to indicate the type of the resource being
	 * linked to.
	 * </p>
	 *
	 * @review
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface LinkedModel {

		/**
		 * Returns the class of the resource being linked to.
		 *
		 * @return the class of the resource being linked to
		 * @review
		 */
		public Class<? extends Identifier<?>> value();

	}

	/**
	 * Indicates that a type has a link to another resource's page.
	 *
	 * <p>
	 * Annotation has an attribute to indicate the type of the resource being
	 * linked to.
	 * </p>
	 *
	 * @review
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface RelatedCollection {

		/**
		 * Returns the class of the resource being linked to.
		 *
		 * @return the class of the resource being linked to
		 * @review
		 */
		public Class<? extends Identifier<?>> value();

	}

	/**
	 * Indicates that a field contains a relative URL that should be expressed
	 * as an absolute one.
	 *
	 * <p>
	 * The annotated method must return the relative URL in the form of a {@code
	 * String}. <p> <p> Annotation has an attribute to indicate if the URL is
	 * relative to the JAX-RS application. By default, the URL is supposedly
	 * relative to the server.
	 * </p>
	 *
	 * @review
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface RelativeURL {

		/**
		 * Returns {@code true} if the provided URL is relative to the JAX-RS
		 * application.
		 *
		 * @return {@code true} if the provided URL is relative to the JAX-RS
		 *         application; {@code false} otherwise
		 * @review
		 */
		public boolean fromApplication() default false;

	}

	/**
	 * Provides information about a type. This annotation should always be used
	 * on an interface.
	 *
	 * <p>
	 * Annotation has attributes to customize the schema URL (<a
	 * href="https://schema.org">schema.org</a> by default) and the description
	 * of the type (in the case the type is a custom one).
	 * </p>
	 *
	 * @review
	 */
	@Retention(RUNTIME)
	@Target(TYPE)
	public @interface Type {

		/**
		 * Returns the type description, if it is a custom one. Returns empty
		 * string otherwise.
		 *
		 * @return the type description, if it is a custom one; empty string
		 *         otherwise
		 * @review
		 */
		public String description() default "";

		/**
		 * Returns the type's schema URL
		 *
		 * @return the type's schema URL
		 * @review
		 */
		public String schemaURL() default "https://www.schema.org/";

		/**
		 * Returns the type's name
		 *
		 * @return the type's name
		 * @review
		 */
		public String value();

	}

}
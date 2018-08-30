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

package com.liferay.apio.architect.documentation.contributor;

import aQute.bnd.annotation.ProviderType;

import java.util.Locale;
import java.util.function.Function;

/**
 * Holds information about the documentation of the properties and operations of
 * the Resources.
 *
 * @author Víctor Galán
 * @review
 */
public interface CustomDocumentation {

	/**
	 * Returns a function that will return the description of a property or
	 * operation passing the desired locale.
	 *
	 * @param  name the name of the property or operation to get its
	 *         description.
	 * @review
	 */
	public Function<Locale, String> getDescriptionFunction(String name);

	/**
	 * Creates a custom documentation
	 *
	 * @review
	 */
	@ProviderType
	public interface Builder {

		/**
		 * Adds a description for the name.
		 *
		 * @param  name the name to add the description to.
		 * @param  description the description to be added to the name.
		 * @review
		 */
		public Builder addDescription(String name, String description);

		/**
		 * Adds a localizable description of the name.
		 *
		 * @param  name the name to add the description to.
		 * @param  stringFunction the fucntion that takes a locale a return a
		 *         description
		 * @review
		 */
		public Builder addLocalizedDescription(
			String name, Function<Locale, String> stringFunction);

		/**
		 * Constructs and returns a {@link CustomDocumentation} instance with
		 * the information provided to the builder.
		 *
		 * @review
		 */
		public CustomDocumentation build();

	}

}
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

package com.liferay.apio.architect.impl.documentation.contributor;

import com.liferay.apio.architect.documentation.contributor.CustomDocumentation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Víctor Galán
 */
public class CustomDocumentationImpl implements CustomDocumentation {

	@Override
	public Function<Locale, String> getDescriptionFunction(String name) {
		return _descriptionFunctions.get(name);
	}

	public static class BuilderImpl implements Builder {

		public BuilderImpl() {
		}

		@Override
		public Builder addDescription(String name, String description) {
			_documentationContributor._descriptionFunctions.put(
				name, __ -> description);

			return this;
		}

		@Override
		public Builder addLocalizedDescription(
			String name, Function<Locale, String> stringFunction) {

			_documentationContributor._descriptionFunctions.put(
				name, stringFunction);

			return this;
		}

		@Override
		public CustomDocumentation build() {
			return _documentationContributor;
		}

		private CustomDocumentationImpl _documentationContributor =
			new CustomDocumentationImpl();

	}

	private final Map<String, Function<Locale, String>> _descriptionFunctions =
		new HashMap<>();

}
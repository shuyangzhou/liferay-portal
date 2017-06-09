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

package com.liferay.vulcan.response.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
public class EmbeddedRetriever {

	public static Embedded getEmbedded(HttpServletRequest httpServletRequest) {
		Map<String, String[]> parameterMap =
			httpServletRequest.getParameterMap();

		String[] embeddedParams = parameterMap.get("embedded");

		if ((embeddedParams != null) && (embeddedParams.length == 1)) {
			String[] embeddedKeys = _EMBEDDED_SEPARATOR_PATTERN.split(
				embeddedParams[0]);

			return new EmbeddedImpl(Arrays.asList(embeddedKeys));
		}

		return new EmbeddedImpl(new ArrayList<>());
	}

	public static class EmbeddedImpl implements Embedded {

		public EmbeddedImpl(List<String> embedded) {
			_embedded = embedded;
		}

		@Override
		public Predicate<String> getEmbeddedPredicate() {
			return field -> _embedded.contains(field);
		}

		private final List<String> _embedded;

	}

	private static final Pattern _EMBEDDED_SEPARATOR_PATTERN = Pattern.compile(
		"\\s*,\\s*");

}
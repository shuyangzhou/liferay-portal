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

package com.liferay.vulcan.message.json;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.json.JSONObject;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@ProviderType
public interface JSONObjectBuilder {

	public JSONObject build();

	public FieldStep field(String name);

	public FieldStep ifElseCondition(
		boolean condition, Function<JSONObjectBuilder, FieldStep> ifFunction,
		Function<JSONObjectBuilder, FieldStep> elseFunction);

	public FieldStep nestedField(String parentName, String... nestedNames);

	public FieldStep nestedPrefixedField(
		String prefix, String parentName, String... nestedNames);

	public FieldStep nestedSuffixedField(
		String suffix, String parentName, String... nestedNames);

	public interface ArrayValueStep {

		public void add(Consumer<JSONObjectBuilder> consumer);

		public void add(JSONObjectBuilder jsonObjectBuilder);

		public void add(Object value);

		public <T> void addAll(Collection<T> collection);

	}

	public interface FieldStep {

		public ArrayValueStep arrayValue();

		public FieldStep field(String name);

		public FieldStep ifCondition(
			boolean condition, Function<FieldStep, FieldStep> ifFunction);

		public FieldStep ifElseCondition(
			boolean condition, Function<FieldStep, FieldStep> ifFunction,
			Function<FieldStep, FieldStep> elseFunction);

		public FieldStep nestedField(String parentName, String... nestedNames);

		public FieldStep nestedPrefixedField(
			String prefix, String parentName, String... nestedNames);

		public FieldStep nestedSuffixedField(
			String suffix, String parentName, String... nestedNames);

		public void value(Object value);

	}

}
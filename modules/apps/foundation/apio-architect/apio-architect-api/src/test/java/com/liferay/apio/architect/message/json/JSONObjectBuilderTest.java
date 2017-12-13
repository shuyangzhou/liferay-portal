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

package com.liferay.apio.architect.message.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.liferay.apio.architect.test.json.JsonMatchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class JSONObjectBuilderTest {

	@Test
	public void testInvokingAddAllOnAnArrayValueCreatesAValidJsonArray() {
		JSONObjectBuilder.ArrayValueStep arrayValueStep =
			_jsonObjectBuilder.field(
				"array"
			).arrayValue();

		List<Boolean> booleanList = new ArrayList<Boolean>() {
			{
				add(false);
				add(true);
			}
		};

		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("solution", 42);

		Collection<JsonObject> jsonObjectList = new ArrayList<JsonObject>() {
			{
				add(jsonObject);
				add(jsonObject);
			}
		};

		List<Number> numberList = new ArrayList<Number>() {
			{
				add(21);
				add(42);
			}
		};

		List<String> stringList = new ArrayList<String>() {
			{
				add("api");
				add("apio");
			}
		};

		arrayValueStep.addAllBooleans(booleanList);
		arrayValueStep.addAllJsonObjects(jsonObjectList);
		arrayValueStep.addAllNumbers(numberList);
		arrayValueStep.addAllStrings(stringList);

		@SuppressWarnings("unchecked")
		Matcher<JsonElement> isAJsonArrayWithElements = Matchers.is(
			JsonMatchers.aJsonArrayThat(
				Matchers.contains(
					JsonMatchers.aJsonBoolean(false),
					JsonMatchers.aJsonBoolean(true),
					_aJsonObjectWithTheSolution, _aJsonObjectWithTheSolution,
					JsonMatchers.aJsonInt(equalTo(21)),
					JsonMatchers.aJsonInt(equalTo(42)),
					JsonMatchers.aJsonString(equalTo("api")),
					JsonMatchers.aJsonString(equalTo("apio")))));

		Matcher<JsonElement> isAJsonObjectWithAnArray = Matchers.is(
			JsonMatchers.aJsonObjectWhere("array", isAJsonArrayWithElements));

		assertThat(getJsonObject(), isAJsonObjectWithAnArray);
	}

	@Test
	public void testInvokingAddConsumerCreatesAValidJsonArray() {
		_jsonObjectBuilder.field(
			"array"
		).arrayValue(
		).add(
			jsonObjectBuilder -> jsonObjectBuilder.field(
				"solution"
			).numberValue(
				42
			)
		);

		@SuppressWarnings("unchecked")
		Matcher<JsonElement> isAJsonArrayWithElements = Matchers.is(
			JsonMatchers.aJsonArrayThat(contains(_aJsonObjectWithTheSolution)));

		Matcher<JsonElement> isAJsonObjectWithAnArray = Matchers.is(
			JsonMatchers.aJsonObjectWhere("array", isAJsonArrayWithElements));

		assertThat(getJsonObject(), isAJsonObjectWithAnArray);
	}

	@Test
	public void testInvokingAddJsonObjectBuilderCreatesAValidJsonArray() {
		JSONObjectBuilder jsonObjectBuilder = new JSONObjectBuilder();

		jsonObjectBuilder.field(
			"solution"
		).numberValue(
			42
		);

		_jsonObjectBuilder.field(
			"array"
		).arrayValue(
		).add(
			jsonObjectBuilder
		);

		@SuppressWarnings("unchecked")
		Matcher<JsonElement> isAJsonArrayWithElements = Matchers.is(
			JsonMatchers.aJsonArrayThat(contains(_aJsonObjectWithTheSolution)));

		Matcher<JsonElement> isAJsonObjectWithAnArray = Matchers.is(
			JsonMatchers.aJsonObjectWhere("array", isAJsonArrayWithElements));

		assertThat(getJsonObject(), isAJsonObjectWithAnArray);
	}

	@Test
	public void testInvokingAddOnAnArrayValueCreatesAValidJsonArray() {
		JSONObjectBuilder.ArrayValueStep arrayValueStep =
			_jsonObjectBuilder.field(
				"array"
			).arrayValue();

		arrayValueStep.addBoolean(true);
		arrayValueStep.addNumber(42);
		arrayValueStep.addString("apio");

		@SuppressWarnings("unchecked")
		Matcher<JsonElement> isAJsonArrayWithElements = Matchers.is(
			JsonMatchers.aJsonArrayThat(
				Matchers.contains(
					JsonMatchers.aJsonBoolean(true),
					JsonMatchers.aJsonInt(equalTo(42)),
					JsonMatchers.aJsonString(equalTo("apio")))));

		Matcher<JsonElement> isAJsonObjectWithAnArray = Matchers.is(
			JsonMatchers.aJsonObjectWhere("array", isAJsonArrayWithElements));

		assertThat(getJsonObject(), isAJsonObjectWithAnArray);
	}

	@Test
	public void testInvokingArrayValueCreatesAJsonArray() {
		_jsonObjectBuilder.field(
			"array"
		).arrayValue();

		Matcher<JsonElement> isAJsonObjectWithAnArray = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"array",
				Matchers.is(
					JsonMatchers.aJsonArrayThat(not(contains(anything()))))));

		assertThat(getJsonObject(), isAJsonObjectWithAnArray);
	}

	@Test
	public void testInvokingBooleanValueCreatesABoolean() {
		_jsonObjectBuilder.field(
			"solution"
		).booleanValue(
			true
		);

		Matcher<JsonElement> isAJsonObjectWithTheSolution = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"solution", Matchers.is(JsonMatchers.aJsonBoolean(true))));

		assertThat(getJsonObject(), isAJsonObjectWithTheSolution);
	}

	@Test
	public void testInvokingFalseIfElseConditionCreatesACorrectField() {
		_jsonObjectBuilder.ifElseCondition(
			false, builder -> builder.field("true"),
			builder -> builder.field("solution")
		).numberValue(
			42
		);

		assertThat(getJsonObject(), is(_aJsonObjectWithTheSolution));
	}

	@Test
	public void testInvokingFieldAndFalseIfConditionCreatesACorrectField() {
		_jsonObjectBuilder.field(
			"first"
		).ifCondition(
			false, builder -> builder.field("solution")
		).numberValue(
			42
		);

		Matcher<JsonElement> isAJsonObjectWithTheFirst = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"first", Matchers.is(JsonMatchers.aJsonInt(equalTo(42)))));

		assertThat(getJsonObject(), isAJsonObjectWithTheFirst);
	}

	@Test
	public void testInvokingFieldAndFalseIfElseConditionCreatesACorrectField() {
		_jsonObjectBuilder.field(
			"first"
		).ifElseCondition(
			false, builder -> builder.field("true"),
			builder -> builder.field("solution")
		).numberValue(
			42
		);

		Matcher<JsonElement> isAJsonObjectWithTheFirst = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"first", is(is(_aJsonObjectWithTheSolution))));

		assertThat(getJsonObject(), isAJsonObjectWithTheFirst);
	}

	@Test
	public void testInvokingFieldAndNestedPrefixedFieldCreatesACorrectField() {
		_jsonObjectBuilder.field(
			"solution"
		).nestedPrefixedField(
			"prefix", "first", "second", "third"
		).numberValue(
			42
		);

		Matcher<JsonElement> isAJsonObjectWithTheSolution = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"solution", isAJsonObjectWithTheFirstPrefix()));

		assertThat(getJsonObject(), isAJsonObjectWithTheSolution);
	}

	@Test
	public void testInvokingFieldAndNestedSuffixedFieldCreatesACorrectField() {
		_jsonObjectBuilder.field(
			"solution"
		).nestedSuffixedField(
			"suffix", "first", "second", "third"
		).numberValue(
			42
		);

		MatcherAssert.assertThat(
			getJsonObject(),
			Matchers.is(
				JsonMatchers.aJsonObjectWhere(
					"solution", isAJsonObjectWithTheFirst())));
	}

	@Test
	public void testInvokingFieldAndTrueIfConditionCreatesACorrectField() {
		_jsonObjectBuilder.field(
			"first"
		).ifCondition(
			true, builder -> builder.field("solution")
		).numberValue(
			42
		);

		Matcher<JsonElement> isAJsonObjectWithTheFirst = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"first", is(is(_aJsonObjectWithTheSolution))));

		assertThat(getJsonObject(), isAJsonObjectWithTheFirst);
	}

	@Test
	public void testInvokingFieldAndTrueIfElseConditionCreatesACorrectField() {
		_jsonObjectBuilder.field(
			"first"
		).ifElseCondition(
			true, builder -> builder.field("solution"),
			builder -> builder.field("false")
		).numberValue(
			42
		);

		Matcher<JsonElement> isAJsonObjectWithTheFirst = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"first", is(is(_aJsonObjectWithTheSolution))));

		assertThat(getJsonObject(), isAJsonObjectWithTheFirst);
	}

	@Test
	public void testInvokingFieldCreatesACorrectField() {
		_jsonObjectBuilder.field(
			"solution"
		).numberValue(
			42
		);

		assertThat(getJsonObject(), is(_aJsonObjectWithTheSolution));
	}

	@Test
	public void testInvokingNestedFieldCreatesACorrectNestedField() {
		_jsonObjectBuilder.nestedField(
			"the", "solution"
		).numberValue(
			42
		);

		MatcherAssert.assertThat(
			getJsonObject(),
			Matchers.is(
				JsonMatchers.aJsonObjectWhere(
					"the", is(_aJsonObjectWithTheSolution))));
	}

	@Test
	public void testInvokingNestedPrefixedFieldCreatesACorrectField() {
		_jsonObjectBuilder.nestedPrefixedField(
			"prefix", "first", "second", "third"
		).numberValue(
			42
		);

		assertThat(getJsonObject(), isAJsonObjectWithTheFirstPrefix());
	}

	@Test
	public void testInvokingNestedSuffixedFieldCreatesACorrectField() {
		_jsonObjectBuilder.nestedSuffixedField(
			"suffix", "first", "second", "third"
		).numberValue(
			42
		);

		assertThat(getJsonObject(), isAJsonObjectWithTheFirst());
	}

	@Test
	public void testInvokingNumberValueCreatesANumber() {
		_jsonObjectBuilder.field(
			"solution"
		).numberValue(
			42
		);

		assertThat(getJsonObject(), is(_aJsonObjectWithTheSolution));
	}

	@Test
	public void testInvokingStringValueCreatesAString() {
		_jsonObjectBuilder.field(
			"solution"
		).stringValue(
			"forty-two"
		);

		Matcher<JsonElement> isAJsonObjectWithTheSolution = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"solution",
				Matchers.is(JsonMatchers.aJsonString(equalTo("forty-two")))));

		assertThat(getJsonObject(), isAJsonObjectWithTheSolution);
	}

	@Test
	public void testInvokingTrueIfElseConditionCreatesACorrectField() {
		_jsonObjectBuilder.ifElseCondition(
			true, builder -> builder.field("solution"),
			builder -> builder.field("false")
		).numberValue(
			42
		);

		assertThat(getJsonObject(), is(_aJsonObjectWithTheSolution));
	}

	protected JsonObject getJsonObject() {
		return _jsonObjectBuilder.build();
	}

	protected Matcher<JsonElement> isAJsonObjectWithTheFirst() {
		Matcher<JsonElement> isAJsonObjectWithTheThirdSuffix = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"suffix", Matchers.is(JsonMatchers.aJsonInt(equalTo(42)))));

		Matcher<JsonElement> isAJsonObjectWithTheThird = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"third", isAJsonObjectWithTheThirdSuffix));

		Matcher<JsonElement> isAJsonObjectWithTheSecondSuffix = Matchers.is(
			JsonMatchers.aJsonObjectWhere("suffix", isAJsonObjectWithTheThird));

		Matcher<JsonElement> isAJsonObjectWithTheSecond = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"second", isAJsonObjectWithTheSecondSuffix));

		Matcher<JsonElement> isAJsonObjectWithTheFirstSuffix = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"suffix", isAJsonObjectWithTheSecond));

		return Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"first", isAJsonObjectWithTheFirstSuffix));
	}

	protected Matcher<JsonElement> isAJsonObjectWithTheFirstPrefix() {
		Matcher<JsonElement> isAJsonObjectWithTheThird = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"third", Matchers.is(JsonMatchers.aJsonInt(equalTo(42)))));

		Matcher<JsonElement> isAJsonObjectWithTheThirdPrefix = Matchers.is(
			JsonMatchers.aJsonObjectWhere("prefix", isAJsonObjectWithTheThird));

		Matcher<JsonElement> isAJsonObjectWithTheSecond = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"second", isAJsonObjectWithTheThirdPrefix));

		Matcher<JsonElement> isAJsonObjectWithTheSecondPrefix = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"prefix", isAJsonObjectWithTheSecond));

		Matcher<JsonElement> isAJsonObjectWithTheFirst = Matchers.is(
			JsonMatchers.aJsonObjectWhere(
				"first", isAJsonObjectWithTheSecondPrefix));

		return Matchers.is(
			JsonMatchers.aJsonObjectWhere("prefix", isAJsonObjectWithTheFirst));
	}

	private final Matcher<JsonElement> _aJsonObjectWithTheSolution =
		JsonMatchers.aJsonObjectWhere(
			"solution", Matchers.is(JsonMatchers.aJsonInt(equalTo(42))));
	private final JSONObjectBuilder _jsonObjectBuilder =
		new JSONObjectBuilder();

}
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

package com.liferay.apio.architect.form;

import static com.liferay.apio.architect.form.FormUtil.getOptionalBoolean;
import static com.liferay.apio.architect.form.FormUtil.getOptionalDate;
import static com.liferay.apio.architect.form.FormUtil.getOptionalDouble;
import static com.liferay.apio.architect.form.FormUtil.getOptionalLong;
import static com.liferay.apio.architect.form.FormUtil.getOptionalString;
import static com.liferay.apio.architect.form.FormUtil.getRequiredBoolean;
import static com.liferay.apio.architect.form.FormUtil.getRequiredDate;
import static com.liferay.apio.architect.form.FormUtil.getRequiredDouble;
import static com.liferay.apio.architect.form.FormUtil.getRequiredLong;
import static com.liferay.apio.architect.form.FormUtil.getRequiredString;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;

import com.liferay.apio.architect.alias.form.FieldFormConsumer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.BadRequestException;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class FormUtilTest {

	@Test(expected = UnsupportedOperationException.class)
	public void testConstructorThrowsException() throws Throwable {
		Constructor<?> constructor =
			FormUtil.class.getDeclaredConstructors()[0];

		constructor.setAccessible(true);

		try {
			constructor.newInstance();
		}
		catch (InvocationTargetException ite) {
			throw ite.getTargetException();
		}
	}

	@Test
	public void testGetOptionalBooleanDoesNotFailIfNotPresent() {
		List<Boolean> list = new ArrayList<>();

		FieldFormConsumer<List<Boolean>, Boolean> fieldFormConsumer =
			getOptionalBoolean(emptyMap(), list);

		fieldFormConsumer.accept("boolean", booleanList -> booleanList::add);

		assertThat(list, is(empty()));
	}

	@Test
	public void testGetOptionalBooleanExtractsBoolean() {
		List<Boolean> list = new ArrayList<>();

		FieldFormConsumer<List<Boolean>, Boolean> fieldFormConsumer =
			getOptionalBoolean(singletonMap("boolean", true), list);

		fieldFormConsumer.accept("boolean", booleanList -> booleanList::add);

		assertThat(list, hasSize(1));

		Boolean aBoolean = list.get(0);

		assertThat(aBoolean, is(true));
	}

	@Test(expected = BadRequestException.class)
	public void testGetOptionalBooleanFailsIfNotABoolean() {
		List<Boolean> list = new ArrayList<>();

		FieldFormConsumer<List<Boolean>, Boolean> fieldFormConsumer =
			getOptionalBoolean(singletonMap("boolean", "Apio"), list);

		fieldFormConsumer.accept("boolean", booleanList -> booleanList::add);
	}

	@Test
	public void testGetOptionalDateDoesNotFailIfNotPresent() {
		List<Date> list = new ArrayList<>();

		FieldFormConsumer<List<Date>, Date> fieldFormConsumer = getOptionalDate(
			emptyMap(), list);

		fieldFormConsumer.accept("date", dateList -> dateList::add);

		assertThat(list, is(empty()));
	}

	@Test
	public void testGetOptionalDateExtractsDate() {
		List<Date> list = new ArrayList<>();

		FieldFormConsumer<List<Date>, Date> fieldFormConsumer = getOptionalDate(
			singletonMap("date", "2017-04-03T18:36Z"), list);

		fieldFormConsumer.accept("date", dateList -> dateList::add);

		assertThat(list, hasSize(1));

		Date date = list.get(0);

		assertThat(date, is(equalTo(new Date(1491244560000L))));
	}

	@Test(expected = BadRequestException.class)
	public void testGetOptionalDateFailsIfNotAnISO8601Date() {
		List<Date> list = new ArrayList<>();

		FieldFormConsumer<List<Date>, Date> fieldFormConsumer = getOptionalDate(
			singletonMap("date", "2017-04-03"), list);

		fieldFormConsumer.accept("date", dateList -> dateList::add);
	}

	@Test
	public void testGetOptionalDoubleDoesNotFailIfNotPresent() {
		List<Double> list = new ArrayList<>();

		FieldFormConsumer<List<Double>, Double> fieldFormConsumer =
			getOptionalDouble(emptyMap(), list);

		fieldFormConsumer.accept("double", doubleList -> doubleList::add);

		assertThat(list, is(empty()));
	}

	@Test
	public void testGetOptionalDoubleExtractsDouble() {
		List<Double> list = new ArrayList<>();

		FieldFormConsumer<List<Double>, Double> fieldFormConsumer =
			getOptionalDouble(singletonMap("double", 42.3D), list);

		fieldFormConsumer.accept("double", doubleList -> doubleList::add);

		assertThat(list, hasSize(1));

		Double aDouble = list.get(0);

		assertThat(aDouble, is(equalTo(42.3D)));
	}

	@Test(expected = BadRequestException.class)
	public void testGetOptionalDoubleFailsIfNotADouble() {
		List<Double> list = new ArrayList<>();

		FieldFormConsumer<List<Double>, Double> fieldFormConsumer =
			getOptionalDouble(singletonMap("double", "Apio"), list);

		fieldFormConsumer.accept("double", doubleList -> doubleList::add);
	}

	@Test
	public void testGetOptionalLongDoesNotFailIfNotPresent() {
		List<Long> list = new ArrayList<>();

		FieldFormConsumer<List<Long>, Long> fieldFormConsumer = getOptionalLong(
			emptyMap(), list);

		fieldFormConsumer.accept("long", longList -> longList::add);

		assertThat(list, is(empty()));
	}

	@Test
	public void testGetOptionalLongExtractsLong() {
		List<Long> list = new ArrayList<>();

		FieldFormConsumer<List<Long>, Long> fieldFormConsumer = getOptionalLong(
			singletonMap("long", 42L), list);

		fieldFormConsumer.accept("long", longList -> longList::add);

		assertThat(list, hasSize(1));

		Long aLong = list.get(0);

		assertThat(aLong, is(equalTo(42L)));
	}

	@Test(expected = BadRequestException.class)
	public void testGetOptionalLongFailsIfNotALong() {
		List<Long> list = new ArrayList<>();

		FieldFormConsumer<List<Long>, Long> fieldFormConsumer = getOptionalLong(
			singletonMap("long", "Apio"), list);

		fieldFormConsumer.accept("long", longList -> longList::add);
	}

	@Test
	public void testGetOptionalStringDoesNotFailIfNotPresent() {
		List<String> list = new ArrayList<>();

		FieldFormConsumer<List<String>, String> fieldFormConsumer =
			getOptionalString(emptyMap(), list);

		fieldFormConsumer.accept("string", stringList -> stringList::add);

		assertThat(list, is(empty()));
	}

	@Test
	public void testGetOptionalStringExtractsString() {
		List<String> list = new ArrayList<>();

		FieldFormConsumer<List<String>, String> fieldFormConsumer =
			getOptionalString(singletonMap("string", "Apio"), list);

		fieldFormConsumer.accept("string", stringList -> stringList::add);

		assertThat(list, hasSize(1));

		String string = list.get(0);

		assertThat(string, is(equalTo("Apio")));
	}

	@Test(expected = BadRequestException.class)
	public void testGetOptionalStringFailsIfNotAString() {
		List<String> list = new ArrayList<>();

		FieldFormConsumer<List<String>, String> fieldFormConsumer =
			getOptionalString(singletonMap("string", 42), list);

		fieldFormConsumer.accept("string", stringList -> stringList::add);
	}

	@Test
	public void testGetRequiredBooleanExtractsBoolean() {
		List<Boolean> list = new ArrayList<>();

		FieldFormConsumer<List<Boolean>, Boolean> fieldFormConsumer =
			getRequiredBoolean(singletonMap("boolean", true), list);

		fieldFormConsumer.accept("boolean", booleanList -> booleanList::add);

		assertThat(list, hasSize(1));

		Boolean aBoolean = list.get(0);

		assertThat(aBoolean, is(true));
	}

	@Test(expected = BadRequestException.class)
	public void testGetRequiredBooleanFailsIfNotABoolean() {
		List<Boolean> list = new ArrayList<>();

		FieldFormConsumer<List<Boolean>, Boolean> fieldFormConsumer =
			getRequiredBoolean(singletonMap("boolean", "Apio"), list);

		fieldFormConsumer.accept("boolean", booleanList -> booleanList::add);
	}

	@Test(expected = BadRequestException.class)
	public void testGetRequiredBooleanFailsIfNotPresent() {
		List<Boolean> list = new ArrayList<>();

		FieldFormConsumer<List<Boolean>, Boolean> fieldFormConsumer =
			getRequiredBoolean(emptyMap(), list);

		fieldFormConsumer.accept("boolean", booleanList -> booleanList::add);
	}

	@Test
	public void testGetRequiredDateExtractsDate() {
		List<Date> list = new ArrayList<>();

		FieldFormConsumer<List<Date>, Date> fieldFormConsumer = getRequiredDate(
			singletonMap("date", "2017-04-03T18:36Z"), list);

		fieldFormConsumer.accept("date", dateList -> dateList::add);

		assertThat(list, hasSize(1));

		Date date = list.get(0);

		assertThat(date, is(equalTo(new Date(1491244560000L))));
	}

	@Test(expected = BadRequestException.class)
	public void testGetRequiredDateFailsIfNotAnISO8601Date() {
		List<Date> list = new ArrayList<>();

		FieldFormConsumer<List<Date>, Date> fieldFormConsumer = getRequiredDate(
			singletonMap("date", "2017-04-03"), list);

		fieldFormConsumer.accept("date", dateList -> dateList::add);
	}

	@Test(expected = BadRequestException.class)
	public void testGetRequiredDateFailsIfNotPresent() {
		List<Date> list = new ArrayList<>();

		FieldFormConsumer<List<Date>, Date> fieldFormConsumer = getRequiredDate(
			emptyMap(), list);

		fieldFormConsumer.accept("date", dateList -> dateList::add);
	}

	@Test
	public void testGetRequiredDoubleExtractsDouble() {
		List<Double> list = new ArrayList<>();

		FieldFormConsumer<List<Double>, Double> fieldFormConsumer =
			getRequiredDouble(singletonMap("double", 42.3D), list);

		fieldFormConsumer.accept("double", doubleList -> doubleList::add);

		assertThat(list, hasSize(1));

		Double aDouble = list.get(0);

		assertThat(aDouble, is(equalTo(42.3D)));
	}

	@Test(expected = BadRequestException.class)
	public void testGetRequiredDoubleFailsIfNotADouble() {
		List<Double> list = new ArrayList<>();

		FieldFormConsumer<List<Double>, Double> fieldFormConsumer =
			getRequiredDouble(singletonMap("double", "Apio"), list);

		fieldFormConsumer.accept("double", doubleList -> doubleList::add);
	}

	@Test(expected = BadRequestException.class)
	public void testGetRequiredDoubleFailsIfNotPresent() {
		List<Double> list = new ArrayList<>();

		FieldFormConsumer<List<Double>, Double> fieldFormConsumer =
			getRequiredDouble(emptyMap(), list);

		fieldFormConsumer.accept("double", doubleList -> doubleList::add);
	}

	@Test
	public void testGetRequiredLongExtractsLong() {
		List<Long> list = new ArrayList<>();

		FieldFormConsumer<List<Long>, Long> fieldFormConsumer = getRequiredLong(
			singletonMap("long", 42L), list);

		fieldFormConsumer.accept("long", longList -> longList::add);

		assertThat(list, hasSize(1));

		Long aLong = list.get(0);

		assertThat(aLong, is(equalTo(42L)));
	}

	@Test(expected = BadRequestException.class)
	public void testGetRequiredLongFailsIfNotALong() {
		List<Long> list = new ArrayList<>();

		FieldFormConsumer<List<Long>, Long> fieldFormConsumer = getRequiredLong(
			singletonMap("long", "Apio"), list);

		fieldFormConsumer.accept("long", longList -> longList::add);
	}

	@Test(expected = BadRequestException.class)
	public void testGetRequiredLongFailsIfNotPresent() {
		List<Long> list = new ArrayList<>();

		FieldFormConsumer<List<Long>, Long> fieldFormConsumer = getRequiredLong(
			emptyMap(), list);

		fieldFormConsumer.accept("long", longList -> longList::add);
	}

	@Test
	public void testGetRequiredStringExtractsString() {
		List<String> list = new ArrayList<>();

		FieldFormConsumer<List<String>, String> fieldFormConsumer =
			getRequiredString(singletonMap("string", "Apio"), list);

		fieldFormConsumer.accept("string", stringList -> stringList::add);

		assertThat(list, hasSize(1));

		String string = list.get(0);

		assertThat(string, is(equalTo("Apio")));
	}

	@Test(expected = BadRequestException.class)
	public void testGetRequiredStringFailsIfNotAString() {
		List<String> list = new ArrayList<>();

		FieldFormConsumer<List<String>, String> fieldFormConsumer =
			getRequiredString(singletonMap("string", 42), list);

		fieldFormConsumer.accept("string", stringList -> stringList::add);
	}

	@Test(expected = BadRequestException.class)
	public void testGetRequiredStringFailsIfNotPresent() {
		List<String> list = new ArrayList<>();

		FieldFormConsumer<List<String>, String> fieldFormConsumer =
			getRequiredString(emptyMap(), list);

		fieldFormConsumer.accept("string", stringList -> stringList::add);
	}

}
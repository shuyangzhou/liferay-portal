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

package com.liferay.apio.architect.internal.action;

import static com.liferay.apio.architect.operation.HTTPMethod.GET;

import static java.lang.String.join;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.liferay.apio.architect.annotation.Id;
import com.liferay.apio.architect.internal.annotation.Action;
import com.liferay.apio.architect.internal.unsafe.Unsafe;
import com.liferay.apio.architect.pagination.Page;
import com.liferay.apio.architect.resource.Resource;
import com.liferay.apio.architect.resource.Resource.Item;
import com.liferay.apio.architect.resource.Resource.Paged;

import io.vavr.CheckedFunction1;
import io.vavr.control.Try;

import java.lang.annotation.Annotation;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class ActionSemanticsTest {

	@Test
	public void testBuilderCreatesActionSemantics() throws Throwable {
		ActionSemantics actionSemantics = ActionSemantics.ofResource(
			Paged.of("name")
		).name(
			"action"
		).method(
			GET
		).returns(
			Long.class
		).permissionFunction(
		).executeFunction(
			_join
		).receivesParams(
			String.class, Long.class
		).annotatedWith(
			_myAnnotation
		).build();

		assertThat(actionSemantics.getAnnotations(), contains(_myAnnotation));
		assertThat(actionSemantics.getActionName(), is("action"));
		assertThat(actionSemantics.getHTTPMethod(), is("GET"));

		List<Class<?>> paramClasses = actionSemantics.getParamClasses();

		assertThat(paramClasses, contains(String.class, Long.class));

		assertThat(actionSemantics.getResource(), is(Paged.of("name")));
		assertThat(actionSemantics.getReturnClass(), is(equalTo(Long.class)));

		String result = (String)actionSemantics.execute(asList("1", "2"));

		assertThat(result, is("1-2"));

		boolean permission = actionSemantics.checkPermissions(null);

		assertTrue(permission);
	}

	@Test
	public void testBuilderWithoutAnnotationsCreatesActionSemantics()
		throws Throwable {

		ActionSemantics actionSemantics = ActionSemantics.ofResource(
			Paged.of("name")
		).name(
			"action"
		).method(
			GET
		).returns(
			Void.class
		).permissionFunction(
		).executeFunction(
			_join
		).build();

		assertThat(actionSemantics.getResource(), is(Paged.of("name")));
		assertThat(actionSemantics.getActionName(), is("action"));
		assertThat(actionSemantics.getHTTPMethod(), is("GET"));
		assertThat(actionSemantics.getParamClasses(), is(empty()));
		assertThat(actionSemantics.getReturnClass(), is(equalTo(Void.class)));
		assertThat(actionSemantics.getAnnotations(), is(empty()));

		String result = (String)actionSemantics.execute(asList("1", "2"));

		assertThat(result, is("1-2"));
	}

	@Test
	public void testBuilderWithoutParamsCreatesActionSemantics()
		throws Throwable {

		ActionSemantics actionSemantics = ActionSemantics.ofResource(
			Paged.of("name")
		).name(
			"action"
		).method(
			GET
		).returns(
			Long.class
		).permissionFunction(
		).executeFunction(
			_join
		).annotatedWith(
			_myAnnotation
		).build();

		assertThat(actionSemantics.getResource(), is(Paged.of("name")));
		assertThat(actionSemantics.getActionName(), is("action"));
		assertThat(actionSemantics.getHTTPMethod(), is("GET"));
		assertThat(actionSemantics.getParamClasses(), is(empty()));
		assertThat(actionSemantics.getReturnClass(), is(equalTo(Long.class)));
		assertThat(actionSemantics.getAnnotations(), contains(_myAnnotation));

		String result = (String)actionSemantics.execute(asList("1", "2"));

		assertThat(result, is("1-2"));
	}

	@Test
	public void testBuilderWithPermissionFunctionCreatesActionSemantics()
		throws Throwable {

		ActionSemantics actionSemantics = ActionSemantics.ofResource(
			Paged.of("name")
		).name(
			"action"
		).method(
			GET
		).returns(
			Long.class
		).permissionFunction(
			params -> Unsafe.unsafeCast(params.get(0)).equals(0L)
		).permissionProvidedClasses(
			Id.class
		).executeFunction(
			_join
		).annotatedWith(
			_myAnnotation
		).build();

		boolean validParam = actionSemantics.checkPermissions(
			Collections.singletonList(0L));

		assertTrue(validParam);

		boolean invalidParam = actionSemantics.checkPermissions(
			Collections.singletonList(1L));

		assertFalse(invalidParam);
	}

	@Test
	public void testBuilderWithStringMethodCreatesActionSemantics()
		throws Throwable {

		ActionSemantics actionSemantics = ActionSemantics.ofResource(
			Paged.of("name")
		).name(
			"action"
		).method(
			"POST"
		).returns(
			Void.class
		).permissionFunction(
		).executeFunction(
			_join
		).annotatedWith(
			_myAnnotation
		).build();

		assertThat(actionSemantics.getResource(), is(Paged.of("name")));
		assertThat(actionSemantics.getActionName(), is("action"));
		assertThat(actionSemantics.getHTTPMethod(), is("POST"));
		assertThat(actionSemantics.getParamClasses(), is(empty()));
		assertThat(actionSemantics.getReturnClass(), is(equalTo(Void.class)));
		assertThat(actionSemantics.getAnnotations(), contains(_myAnnotation));

		String result = (String)actionSemantics.execute(asList("1", "2"));

		assertThat(result, is("1-2"));
	}

	@Test
	public void testToActionTransformsAnActionSemanticsIntoAnAction() {
		ActionSemantics actionSemantics = ActionSemantics.ofResource(
			Resource.Paged.of("name")
		).name(
			"action"
		).method(
			GET
		).returns(
			String.class
		).permissionFunction(
		).executeFunction(
			_join
		).receivesParams(
			String.class, Long.class
		).annotatedWith(
			_myAnnotation
		).build();

		Action action = actionSemantics.toAction(
			(semantics, request, clazz) -> clazz.getSimpleName());

		Object object = action.apply(null);

		assertThat(object, is(instanceOf(Try.class)));

		@SuppressWarnings("unchecked")
		Try<String> stringTry = (Try<String>)object;

		assertThat(stringTry.get(), is("String-Long"));
	}

	@Test
	public void testWithAnnotationsReturnsActionSemanticsWithAnnotations() {
		ActionSemantics actionSemantics = ActionSemantics.ofResource(
			Paged.of("name")
		).name(
			"retrieve"
		).method(
			"GET"
		).returns(
			Page.class
		).permissionFunction(
		).executeFunction(
			__ -> null
		).annotatedWith(
			_myAnnotation
		).build();

		ActionSemantics newActionSemantics = actionSemantics.withAnnotations(
			Collections.emptyList());

		assertEquals(
			actionSemantics,
			actionSemantics.withAnnotations(singletonList(_myAnnotation)));
		assertThat(actionSemantics.getAnnotations(), contains(_myAnnotation));

		assertThat(newActionSemantics.getAnnotations(), is(empty()));
	}

	@Test
	public void testWithMethodReturnsActionSemanticsWithDifferentMethod() {
		ActionSemantics actionSemantics = ActionSemantics.ofResource(
			Paged.of("name")
		).name(
			"retrieve"
		).method(
			"GET"
		).returns(
			Page.class
		).permissionFunction(
		).executeFunction(
			__ -> null
		).build();

		ActionSemantics newActionSemantics = actionSemantics.withMethod(
			"DELETE");

		assertEquals(actionSemantics, actionSemantics.withMethod("GET"));
		assertThat(actionSemantics.getHTTPMethod(), is("GET"));

		assertThat(newActionSemantics.getHTTPMethod(), is("DELETE"));
	}

	@Test
	public void testWithNameReturnsActionSemanticsWithDifferentName() {
		ActionSemantics actionSemantics = ActionSemantics.ofResource(
			Paged.of("name")
		).name(
			"retrieve"
		).method(
			"GET"
		).returns(
			Page.class
		).permissionFunction(
		).executeFunction(
			__ -> null
		).build();

		ActionSemantics newActionSemantics = actionSemantics.withName("create");

		assertEquals(actionSemantics, actionSemantics.withName("retrieve"));
		assertThat(actionSemantics.getActionName(), is("retrieve"));

		assertThat(newActionSemantics.getActionName(), is("create"));
	}

	@Test
	public void testWithResourceReturnsActionSemanticsWithDifferentResource() {
		ActionSemantics actionSemantics = ActionSemantics.ofResource(
			Paged.of("name")
		).name(
			"retrieve"
		).method(
			"GET"
		).returns(
			Page.class
		).permissionFunction(
		).executeFunction(
			__ -> null
		).build();

		ActionSemantics newActionSemantics = actionSemantics.withResource(
			Item.of("name"));

		assertThat(actionSemantics.getResource(), is(Paged.of("name")));

		assertThat(newActionSemantics.getResource(), is(Item.of("name")));
	}

	@Test
	public void testWithReturnClassReturnsActionSemanticsWithReturnClass() {
		ActionSemantics actionSemantics = ActionSemantics.ofResource(
			Paged.of("name")
		).name(
			"retrieve"
		).method(
			"GET"
		).returns(
			Page.class
		).permissionFunction(
		).executeFunction(
			__ -> null
		).build();

		ActionSemantics newActionSemantics = actionSemantics.withReturnClass(
			Void.class);

		assertEquals(
			actionSemantics, actionSemantics.withReturnClass(Page.class));
		assertThat(actionSemantics.getReturnClass(), is(equalTo(Page.class)));

		assertThat(
			newActionSemantics.getReturnClass(), is(equalTo(Void.class)));
	}

	public static @interface MyAnnotation {
	}

	@SuppressWarnings("unchecked")
	private static final CheckedFunction1<List<?>, Object> _join = list -> join(
		"-", (List<String>)list);

	private static final MyAnnotation _myAnnotation = new MyAnnotation() {

		@Override
		public Class<? extends Annotation> annotationType() {
			return MyAnnotation.class;
		}

	};

}
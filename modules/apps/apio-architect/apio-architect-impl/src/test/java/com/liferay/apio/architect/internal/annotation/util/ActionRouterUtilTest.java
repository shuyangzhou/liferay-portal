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

package com.liferay.apio.architect.internal.annotation.util;

import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.execute;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.findPermissionMethodOptional;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.getBodyResourceClassName;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.getParamClasses;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.getResource;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.getReturnClass;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.isListBody;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.needsParameterFromBody;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.liferay.apio.architect.annotation.Id;
import com.liferay.apio.architect.annotation.ParentId;
import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.internal.annotation.util.MyAnnotatedInterface.MyType;
import com.liferay.apio.architect.internal.pagination.PaginationImpl;
import com.liferay.apio.architect.pagination.Page;
import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.resource.Resource;
import com.liferay.apio.architect.resource.Resource.Item;
import com.liferay.apio.architect.resource.Resource.Nested;
import com.liferay.apio.architect.resource.Resource.Paged;
import com.liferay.apio.architect.single.model.SingleModel;

import java.lang.reflect.Method;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 * @author Javier Gamarra
 */
public class ActionRouterUtilTest {

	@Test
	public void testCanCreateAnnotationReturnsMethod() {
		Optional<Method> createMethodOptional = findPermissionMethodOptional(
			MyAnnotatedInterface.class, Paged.class, "create", "POST");
		Optional<Method> notExistingMethodOptional =
			findPermissionMethodOptional(
				MyAnnotatedInterface.class, Paged.class, "update", "POST");
		Optional<Method> retrieveItemMethodOptional =
			findPermissionMethodOptional(
				MyAnnotatedInterface.class, Item.class, "retrieve", "GET");

		assertTrue(createMethodOptional.isPresent());
		assertFalse(notExistingMethodOptional.isPresent());
		assertTrue(retrieveItemMethodOptional.isPresent());
	}

	@Test
	public void testExecuteLeavesNullAsNull() throws Throwable {
		Object result = execute(Paged.of("name"), emptyList(), __ -> null);

		assertNull(result);
	}

	@Test
	public void testExecuteTransformsListIntoPage() throws Throwable {
		Object object = execute(
			Paged.of("name"), emptyList(), __ -> singletonList("1"));

		assertThat(object, is(notNullValue()));
		assertThat(object, is(instanceOf(Page.class)));

		Page<?> page = (Page<?>)object;

		assertThat(page.getItems(), contains("1"));
		assertThat(page.getItemsPerPage(), is(1));
		assertThat(page.getLastPageNumber(), is(1));
		assertThat(page.hasNext(), is(false));
		assertThat(page.getPageNumber(), is(1));
		assertThat(page.hasPrevious(), is(false));
		assertThat(page.getResourceName(), is("name"));
		assertThat(page.getTotalCount(), is(1));
	}

	@Test
	public void testExecuteTransformsPageItemsIntoPageWithoutParam()
		throws Throwable {

		PageItems<?> pageItems = new PageItems<>(singletonList("1"), 31);

		Object result = execute(Paged.of("name"), emptyList(), __ -> pageItems);

		assertThat(result, is(notNullValue()));
		assertThat(result, is(instanceOf(Page.class)));

		Page<?> page = (Page<?>)result;

		assertThat(page.getItems(), contains("1"));
		assertThat(page.getItemsPerPage(), is(31));
		assertThat(page.getLastPageNumber(), is(1));
		assertThat(page.hasNext(), is(false));
		assertThat(page.getPageNumber(), is(1));
		assertThat(page.hasPrevious(), is(false));
		assertThat(page.getResourceName(), is("name"));
		assertThat(page.getTotalCount(), is(31));
	}

	@Test
	public void testExecuteTransformsPageItemsIntoPageWithParam()
		throws Throwable {

		PageItems<?> pageItems = new PageItems<>(singletonList("1"), 31);

		Pagination pagination = new PaginationImpl(30, 2);

		Object result = execute(
			Paged.of("name"), asList("1", pagination), __ -> pageItems);

		assertThat(result, is(notNullValue()));
		assertThat(result, is(instanceOf(Page.class)));

		Page<?> page = (Page<?>)result;

		assertThat(page.getItems(), contains("1"));
		assertThat(page.getItemsPerPage(), is(30));
		assertThat(page.getLastPageNumber(), is(2));
		assertThat(page.hasNext(), is(false));
		assertThat(page.getPageNumber(), is(2));
		assertThat(page.hasPrevious(), is(true));
		assertThat(page.getResourceName(), is("name"));
		assertThat(page.getTotalCount(), is(31));
	}

	@Test
	public void testExecuteUnwrapsIdAsObjectAndReturnsSingleModel()
		throws Throwable {

		Object result = execute(
			Paged.of("name"), singletonList(Resource.Id.of(4, "4")),
			list -> list[0]);

		assertThat(result, is(notNullValue()));
		assertThat(result, is(instanceOf(SingleModel.class)));

		SingleModel<?> singleModel = (SingleModel<?>)result;

		assertThat(singleModel.getResourceName(), is("name"));
		assertThat(singleModel.getModel(), is(4));
		assertThat(singleModel.getOperations(), is(emptyList()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailingExecuteThrowsExceptionCauseIfPresent()
		throws Throwable {

		Object result = execute(
			Paged.of("name"), emptyList(),
			__ -> {
				throw new IllegalArgumentException();
			});

		assertNull(result);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFailingExecuteThrowsExceptionIfCauseNotPresent()
		throws Throwable {

		Object result = execute(
			Paged.of("name"), emptyList(),
			__ -> {
				UnsupportedOperationException cause =
					new UnsupportedOperationException();

				throw new IllegalArgumentException(cause);
			});

		assertNull(result);
	}

	@Test
	public void testGetBodyResourceClass() throws NoSuchMethodException {
		Method listBodyMethod = MyAnnotatedInterface.class.getMethod(
			"withListBodyParameters", List.class);
		Method notAnnotatedMethod = MyAnnotatedInterface.class.getMethod(
			"notAnnotated");
		Method singleBodyMethod = MyAnnotatedInterface.class.getMethod(
			"withIdAndBodyParameters", Pagination.class, Credentials.class,
			Long.class, MyType.class);

		String myType = MyType.class.getName();

		assertThat(getBodyResourceClassName(listBodyMethod), is(myType));

		assertNull(getBodyResourceClassName(notAnnotatedMethod));

		assertThat(getBodyResourceClassName(singleBodyMethod), is(myType));
	}

	@Test
	public void testGetParamClassesFromMethodWithIdAndBody()
		throws NoSuchMethodException {

		Method method = MyAnnotatedInterface.class.getMethod(
			"withIdAndBodyParameters", Pagination.class, Credentials.class,
			Long.class, MyType.class);

		Class<?>[] providers = getParamClasses(method);

		Class<?>[] expected =
			{Pagination.class, Credentials.class, Id.class, Body.class};

		assertThat(providers, is(arrayContaining(expected)));
	}

	@Test
	public void testGetParamClassesFromMethodWithParentId()
		throws NoSuchMethodException {

		Method method = MyAnnotatedInterface.class.getMethod(
			"withParentId", long.class);

		Class<?>[] providers = getParamClasses(method);

		assertThat(providers, is(arrayContaining(ParentId.class)));
	}

	@Test
	public void testGetResourceReturnsItemResource()
		throws NoSuchMethodException {

		Method method = MyAnnotatedInterface.class.getMethod(
			"withIdAndBodyParameters", Pagination.class, Credentials.class,
			Long.class, MyType.class);

		Resource resource = getResource(method, "blog");

		assertThat(resource, is(Item.of("blog")));
	}

	@Test
	public void testGetResourceReturnsNestedResource()
		throws NoSuchMethodException {

		Method method = MyAnnotatedInterface.class.getMethod(
			"withParentId", long.class);

		Resource resource = getResource(method, "blog");

		assertThat(resource, is(Nested.of(Item.of("my-type"), "blog")));
	}

	@Test
	public void testGetResourceReturnsPagedResource()
		throws NoSuchMethodException {

		Method method = MyAnnotatedInterface.class.getMethod("notAnnotated");

		Resource resource = getResource(method, "blog");

		assertThat(resource, is(Paged.of("blog")));
	}

	@Test
	public void testGetReturnClass() throws NoSuchMethodException {
		Method returningListMethod = MyAnnotatedInterface.class.getMethod(
			"returningList");
		Method returningMyTypeMethod = MyAnnotatedInterface.class.getMethod(
			"returningMyType");
		Method returningPageItemsMethod = MyAnnotatedInterface.class.getMethod(
			"returningPageItems");
		Method returningStringMethod = MyAnnotatedInterface.class.getMethod(
			"returningString");
		Method returningVoidMethod = MyAnnotatedInterface.class.getMethod(
			"returningVoid");

		assertThat(
			getReturnClass(returningListMethod), is(equalTo(Page.class)));
		assertThat(
			getReturnClass(returningMyTypeMethod),
			is(equalTo(SingleModel.class)));
		assertThat(
			getReturnClass(returningPageItemsMethod), is(equalTo(Page.class)));
		assertThat(
			getReturnClass(returningStringMethod), is(equalTo(String.class)));
		assertThat(
			getReturnClass(returningVoidMethod), is(equalTo(Void.class)));
	}

	@Test
	public void testIsListBody() throws NoSuchMethodException {
		Method notAnnotatedMethod = MyAnnotatedInterface.class.getMethod(
			"notAnnotated");
		Method singleBodyMethod = MyAnnotatedInterface.class.getMethod(
			"withIdAndBodyParameters", Pagination.class, Credentials.class,
			Long.class, MyType.class);
		Method listBodyMethod = MyAnnotatedInterface.class.getMethod(
			"withListBodyParameters", List.class);

		assertFalse(isListBody(notAnnotatedMethod));
		assertFalse(isListBody(singleBodyMethod));
		assertTrue(isListBody(listBodyMethod));
	}

	@Test
	public void testNeedsParameterFromBody() throws NoSuchMethodException {
		Method notAnnotatedMethod = MyAnnotatedInterface.class.getMethod(
			"notAnnotated");
		Method listBodyMethod = MyAnnotatedInterface.class.getMethod(
			"withListBodyParameters", List.class);
		Method singleBodyMethod = MyAnnotatedInterface.class.getMethod(
			"withIdAndBodyParameters", Pagination.class, Credentials.class,
			Long.class, MyType.class);

		assertFalse(needsParameterFromBody(notAnnotatedMethod));
		assertTrue(needsParameterFromBody(listBodyMethod));
		assertTrue(needsParameterFromBody(singleBodyMethod));
	}

}
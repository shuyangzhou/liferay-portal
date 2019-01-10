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

import static com.liferay.apio.architect.internal.action.Predicates.areEquals;
import static com.liferay.apio.architect.internal.action.Predicates.hasAnnotation;
import static com.liferay.apio.architect.internal.action.Predicates.isAction;
import static com.liferay.apio.architect.internal.action.Predicates.isActionBy;
import static com.liferay.apio.architect.internal.action.Predicates.isActionByDELETE;
import static com.liferay.apio.architect.internal.action.Predicates.isActionByGET;
import static com.liferay.apio.architect.internal.action.Predicates.isActionByPATCH;
import static com.liferay.apio.architect.internal.action.Predicates.isActionByPOST;
import static com.liferay.apio.architect.internal.action.Predicates.isActionByPUT;
import static com.liferay.apio.architect.internal.action.Predicates.isActionFor;
import static com.liferay.apio.architect.internal.action.Predicates.isActionNamed;
import static com.liferay.apio.architect.internal.action.Predicates.isCreateAction;
import static com.liferay.apio.architect.internal.action.Predicates.isRemoveAction;
import static com.liferay.apio.architect.internal.action.Predicates.isReplaceAction;
import static com.liferay.apio.architect.internal.action.Predicates.isRetrieveAction;
import static com.liferay.apio.architect.internal.action.Predicates.isRootCollectionAction;
import static com.liferay.apio.architect.internal.action.Predicates.isUpdateAction;
import static com.liferay.apio.architect.internal.action.Predicates.returnsAnyOf;

import static java.util.Collections.singletonList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.liferay.apio.architect.annotation.Actions.Retrieve;
import com.liferay.apio.architect.annotation.EntryPoint;
import com.liferay.apio.architect.pagination.Page;
import com.liferay.apio.architect.resource.Resource.Item;
import com.liferay.apio.architect.resource.Resource.Paged;

import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class PredicatesTest {

	@Test
	public void testEquals() {
		List<String> list = singletonList("Apio");

		Predicate<List<String>> truePredicate = areEquals(List::size, 1);
		Predicate<List<String>> falsePredicate = areEquals(List::size, 2);

		assertTrue(truePredicate.test(list));
		assertFalse(falsePredicate.test(list));
	}

	@Test
	public void testHasAnnotation() {
		Predicate<ActionSemantics> truePredicate = hasAnnotation(
			EntryPoint.class);
		Predicate<ActionSemantics> falsePredicate = hasAnnotation(
			Retrieve.class);

		assertTrue(truePredicate.test(_actionSemantics));
		assertFalse(falsePredicate.test(_actionSemantics));
	}

	@Test
	public void testIsAction() {
		Predicate<ActionSemantics> truePredicate = isAction("name", "GET");
		Predicate<ActionSemantics> falsePredicate = isAction("name", "DELETE");
		Predicate<ActionSemantics> anotherFalsePredicate = isAction(
			"retrieve", "GET");

		assertTrue(truePredicate.test(_actionSemantics));
		assertFalse(falsePredicate.test(_actionSemantics));
		assertFalse(anotherFalsePredicate.test(_actionSemantics));
	}

	@Test
	public void testIsActionBy() {
		assertTrue(isActionBy("GET").test(_actionSemantics));
		assertFalse(isActionBy("DELETE").test(_actionSemantics));
	}

	@Test
	public void testIsActionByDELETE() {
		assertTrue(
			isActionByDELETE.test(_actionSemantics.withMethod("DELETE")));
		assertFalse(isActionByDELETE.test(_actionSemantics));
	}

	@Test
	public void testIsActionByGET() {
		assertTrue(isActionByGET.test(_actionSemantics));
		assertFalse(isActionByGET.test(_actionSemantics.withMethod("DELETE")));
	}

	@Test
	public void testIsActionByPATCH() {
		assertTrue(isActionByPATCH.test(_actionSemantics.withMethod("PATCH")));
		assertFalse(isActionByPATCH.test(_actionSemantics));
	}

	@Test
	public void testIsActionByPOST() {
		assertTrue(isActionByPOST.test(_actionSemantics.withMethod("POST")));
		assertFalse(isActionByPOST.test(_actionSemantics));
	}

	@Test
	public void testIsActionByPUT() {
		assertTrue(isActionByPUT.test(_actionSemantics.withMethod("PUT")));
		assertFalse(isActionByPUT.test(_actionSemantics));
	}

	@Test
	public void testIsActionForResource() {
		Predicate<ActionSemantics> truePredicate = isActionFor(
			Paged.of("Name"));

		Predicate<ActionSemantics> falsePredicate = isActionFor(
			Paged.of("NotName"));

		assertTrue(truePredicate.test(_actionSemantics));
		assertFalse(falsePredicate.test(_actionSemantics));
	}

	@Test
	public void testIsActionForResourceClass() {
		Predicate<ActionSemantics> truePredicate = isActionFor(Paged.class);
		Predicate<ActionSemantics> falsePredicate = isActionFor(Item.class);

		assertTrue(truePredicate.test(_actionSemantics));
		assertFalse(falsePredicate.test(_actionSemantics));
	}

	@Test
	public void testIsActionNamed() {
		Predicate<ActionSemantics> truePredicate = isActionNamed("name");
		Predicate<ActionSemantics> falsePredicate = isActionNamed("notName");

		assertTrue(truePredicate.test(_actionSemantics));
		assertFalse(falsePredicate.test(_actionSemantics));
	}

	@Test
	public void testIsCreateAction() {
		ActionSemantics actionSemantics = _actionSemantics.withMethod("POST");

		assertTrue(isCreateAction.test(actionSemantics.withName("create")));

		assertFalse(isCreateAction.test(_actionSemantics));
	}

	@Test
	public void testIsRemoveAction() {
		ActionSemantics actionSemantics = _actionSemantics.withMethod("DELETE");

		assertTrue(isRemoveAction.test(actionSemantics.withName("remove")));

		assertFalse(isRemoveAction.test(_actionSemantics));
	}

	@Test
	public void testIsReplaceAction() {
		ActionSemantics actionSemantics = _actionSemantics.withMethod("PUT");

		assertTrue(isReplaceAction.test(actionSemantics.withName("replace")));

		assertFalse(isReplaceAction.test(_actionSemantics));
	}

	@Test
	public void testIsRetrieveAction() {
		ActionSemantics actionSemantics = _actionSemantics.withMethod("GET");

		assertTrue(isRetrieveAction.test(actionSemantics.withName("retrieve")));

		assertFalse(isRetrieveAction.test(_actionSemantics));
	}

	@Test
	public void testIsRootCollectionAction() {
		ActionSemantics actionSemantics = _actionSemantics.withMethod("GET");

		assertTrue(
			isRootCollectionAction.test(actionSemantics.withName("retrieve")));

		assertFalse(isRootCollectionAction.test(_actionSemantics));
	}

	@Test
	public void testIsUpdateAction() {
		ActionSemantics actionSemantics = _actionSemantics.withMethod("PATCH");

		assertTrue(isUpdateAction.test(actionSemantics.withName("update")));

		assertFalse(isUpdateAction.test(_actionSemantics));
	}

	@Test
	public void testReturnsAnyOf() {
		Predicate<ActionSemantics> truePredicate = returnsAnyOf(
			String.class, Page.class);
		Predicate<ActionSemantics> anotherTruePredicate = returnsAnyOf(
			Character.class, Page.class);
		Predicate<ActionSemantics> falsePredicate = returnsAnyOf(Void.class);

		assertTrue(truePredicate.test(_actionSemantics));
		assertTrue(anotherTruePredicate.test(_actionSemantics));
		assertFalse(falsePredicate.test(_actionSemantics));
	}

	private static final ActionSemantics _actionSemantics =
		ActionSemantics.ofResource(
			Paged.of("Name")
		).name(
			"name"
		).method(
			"GET"
		).returns(
			Page.class
		).permissionFunction(
		).executeFunction(
			__ -> null
		).receivesParams(
			String.class, Integer.class
		).annotatedWith(
			() -> EntryPoint.class
		).build();

}
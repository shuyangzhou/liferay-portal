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

package com.liferay.apio.architect.impl.endpoint;

import static com.liferay.apio.architect.impl.routes.RoutesTestUtil.FORM_BUILDER_FUNCTION;
import static com.liferay.apio.architect.impl.routes.RoutesTestUtil.HAS_ADDING_PERMISSION_FUNCTION;
import static com.liferay.apio.architect.impl.routes.RoutesTestUtil.REQUEST_PROVIDE_FUNCTION;
import static com.liferay.apio.architect.impl.routes.RoutesTestUtil.hasNestedAddingPermissionFunction;

import static java.util.function.Function.identity;

import com.liferay.apio.architect.impl.routes.CollectionRoutesImpl;
import com.liferay.apio.architect.impl.routes.ItemRoutesImpl;
import com.liferay.apio.architect.impl.routes.NestedCollectionRoutesImpl;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;

import java.util.Optional;

/**
 * @author Alejandro Hernández
 */
public class EndpointsTestUtil {

	public static CollectionRoutes<Object, Object> collectionRoutes() {
		CollectionRoutes.Builder<Object, Object> builder =
			new CollectionRoutesImpl.BuilderImpl<>(
				"name", REQUEST_PROVIDE_FUNCTION,
				__ -> {
				},
				__ -> null, identity());

		return builder.addCreator(
			__ -> __.get("key"), HAS_ADDING_PERMISSION_FUNCTION,
			FORM_BUILDER_FUNCTION
		).build();
	}

	public static <T, S> CollectionRoutes<T, S> emptyCollectionRoutes() {
		return new CollectionRoutesImpl<>(
			new CollectionRoutesImpl.BuilderImpl<>(
				"", httpServletRequest -> aClass -> Optional.empty(),
				__ -> {
				},
				__ -> null, __ -> null));
	}

	public static <T, S> ItemRoutes<T, S> emptyItemRoutes() {
		return new ItemRoutesImpl<>(
			new ItemRoutesImpl.BuilderImpl<>(
				"", httpServletRequest -> aClass -> Optional.empty(),
				__ -> {
				},
				__ -> null, __ -> Optional.empty()));
	}

	public static <T, S, U> NestedCollectionRoutes<T, S, U>
		emptyNestedCollectionRoutes() {

		return new NestedCollectionRoutesImpl<>(
			new NestedCollectionRoutesImpl.BuilderImpl<>(
				"", "", httpServletRequest -> aClass -> Optional.empty(),
				__ -> {
				},
				__ -> null, __ -> Optional.empty(), __ -> null));
	}

	public static <T, S> ItemRoutes<T, S> itemRoutes() {
		ItemRoutes.Builder<T, S> builder = new ItemRoutesImpl.BuilderImpl<>(
			"name", REQUEST_PROVIDE_FUNCTION,
			__ -> {
			},
			__ -> null, __ -> Optional.empty());

		return builder.addUpdater(
			(aLong, body) -> null, (credentials, s) -> true,
			FORM_BUILDER_FUNCTION
		).build();
	}

	public static NestedCollectionRoutes<Object, Object, Object>
		nestedCollectionRoutes() {

		NestedCollectionRoutes.Builder<Object, Object, Object> builder =
			new NestedCollectionRoutesImpl.BuilderImpl<>(
				"name", "nestedName", REQUEST_PROVIDE_FUNCTION,
				__ -> {
				},
				__ -> null, __ -> Optional.empty(), identity());

		return builder.addCreator(
			(parentId, map) -> map.get("key"),
			hasNestedAddingPermissionFunction(), FORM_BUILDER_FUNCTION
		).build();
	}

}
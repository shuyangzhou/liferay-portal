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

package com.liferay.apio.architect.internal.annotation;

import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.internal.action.ActionSemantics;
import com.liferay.apio.architect.internal.documentation.Documentation;
import com.liferay.apio.architect.internal.entrypoint.EntryPoint;
import com.liferay.apio.architect.resource.Resource;
import com.liferay.apio.architect.resource.Resource.Item;
import com.liferay.apio.architect.single.model.SingleModel;

import io.vavr.control.Either;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides methods to get the different actions provided by the different
 * routers.
 *
 * <p>
 * It also contains special methods for obtaining the API {@link EntryPoint} and
 * {@link Documentation}.
 * </p>
 *
 * @author Alejandro Hernández
 * @see    com.liferay.apio.architect.router.ActionRouter
 * @see    com.liferay.apio.architect.router.CollectionRouter
 * @see    com.liferay.apio.architect.router.ItemRouter
 * @see    com.liferay.apio.architect.router.NestedCollectionRouter
 * @review
 */
public interface ActionManager {

	/**
	 * Returns the action for the provided combination of parameters and method,
	 * if found. Returns an {@link Action.Error} if an action couldn't be
	 * provided.
	 *
	 * @param  method the HTTP method of the action
	 * @param  params the parameters
	 * @return the action, if found; an {@link Action.Error} otherwise
	 * @review
	 */
	public Either<Action.Error, Action> getAction(
		String method, List<String> params);

	/**
	 * Returns the list of action semantics available for a resource.
	 *
	 * <p>
	 * A {@link Credentials} instance can be provided to filter only those for
	 * which the user has permission.
	 * </p>
	 *
	 * @param  resource the resource for which to obtain the actions
	 * @param  credentials the user credentials (can be {@code null}). Providing
	 *         this value will filter the actions to those allowed to the user.
	 * @return the list of action semantics for the resource
	 * @review
	 */
	public Stream<ActionSemantics> getActionSemantics(
		Resource resource, Credentials credentials,
		HttpServletRequest httpServletRequest);

	/**
	 * The API documentation with the list of actions and resources.
	 *
	 * @review
	 */
	public Documentation getDocumentation(
		HttpServletRequest httpServletRequest);

	/**
	 * The API entry point with the root resources.
	 *
	 * @review
	 */
	public EntryPoint getEntryPoint();

	/**
	 * Returns a {@link SingleModel} instance for the supplied {@link Item};
	 * returns {@code Optional#empty()} otherwise.
	 *
	 * @param  item the item for which to look for the {@link SingleModel single
	 *         model}
	 * @param  request the current HTTP request
	 * @return the {@link SingleModel} instance for the supplied {@link Item};
	 *         {@code Optional#empty()} otherwise.
	 * @review
	 */
	public Optional<SingleModel> getItemSingleModel(
		Item item, HttpServletRequest request);

}
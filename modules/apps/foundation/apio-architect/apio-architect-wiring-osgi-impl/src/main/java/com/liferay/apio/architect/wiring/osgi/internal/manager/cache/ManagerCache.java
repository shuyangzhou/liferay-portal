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

package com.liferay.apio.architect.wiring.osgi.internal.manager.cache;

import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.apio.architect.unsafe.Unsafe;
import com.liferay.apio.architect.wiring.osgi.internal.alias.EmptyFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class acts as a central cache for most of the managers.
 *
 * <p>
 * There should only be one instance of this class, accessible through {@link
 * #INSTANCE}.
 * </p>
 *
 * @author Alejandro Hernández
 * @review
 */
public class ManagerCache {

	/**
	 * The {@link ManagerCache} instance.
	 *
	 * @review
	 */
	public static final ManagerCache INSTANCE = new ManagerCache();

	/**
	 * Clears the cache.
	 *
	 * @review
	 */
	public void clear() {
		_collectionRoutes = null;
		_identifierClasses = null;
		_itemRoutes = null;
		_names = null;
		_nestedCollectionRoutes = null;
		_representors = null;
		_reusableNestedCollectionRoutes = null;
		_rootResourceNames = null;
	}

	/**
	 * Returns the collection routes for the collection resource's name.
	 *
	 * @param  name the collection resource's name
	 * @param  computeEmptyFunction the function that can be called to compute
	 *         the data
	 * @return the collection routes
	 * @review
	 */
	public <T> Optional<CollectionRoutes<T>> getCollectionRoutesOptional(
		String name, EmptyFunction computeEmptyFunction) {

		if (_collectionRoutes == null) {
			computeEmptyFunction.invoke();
		}

		Optional<Map<String, CollectionRoutes>> optional = Optional.ofNullable(
			INSTANCE._collectionRoutes);

		return optional.map(
			map -> map.get(name)
		).map(
			Unsafe::unsafeCast
		);
	}

	/**
	 * Returns the resource name's identifier class.
	 *
	 * @param  name the resource name
	 * @param  computeEmptyFunction the function that can be called to compute
	 *         the data
	 * @return the resource name's identifier class
	 */
	public <T extends Identifier> Optional<Class<T>> getIdentifierClassOptional(
		String name, EmptyFunction computeEmptyFunction) {

		if (_identifierClasses == null) {
			computeEmptyFunction.invoke();
		}

		Optional<Map<String, Class<Identifier>>> optional = Optional.ofNullable(
			_identifierClasses);

		return optional.map(
			map -> map.get(name)
		).map(
			Unsafe::unsafeCast
		);
	}

	/**
	 * Returns the item routes for the item resource's name.
	 *
	 * @param  name the item resource's name
	 * @param  computeEmptyFunction the function that can be called to compute
	 *         the data
	 * @return the item routes
	 * @review
	 */
	public <T, S> Optional<ItemRoutes<T, S>> getItemRoutesOptional(
		String name, EmptyFunction computeEmptyFunction) {

		if (_itemRoutes == null) {
			computeEmptyFunction.invoke();
		}

		Optional<Map<String, ItemRoutes>> optional = Optional.ofNullable(
			_itemRoutes);

		return optional.map(
			map -> map.get(name)
		).map(
			Unsafe::unsafeCast
		);
	}

	/**
	 * Returns the name of a collection resource that matches the specified
	 * class name.
	 *
	 * @param  className the collection resource's class name
	 * @param  computeEmptyFunction the function that can be called to compute
	 *         the data
	 * @return the collection resource's name
	 */
	public Optional<String> getNameOptional(
		String className, EmptyFunction computeEmptyFunction) {

		if (_names == null) {
			computeEmptyFunction.invoke();
		}

		Optional<Map<String, String>> optional = INSTANCE.getNamesOptional();

		return optional.map(map -> map.get(className));
	}

	/**
	 * Returns the map containing the names for the different resource
	 * identifier classes if they have been set. Returns {@code
	 * Optional#empty()} otherwise.
	 *
	 * @return the map containing the names for the different resource
	 *         identifier classes if they have been set; returns {@code
	 *         Optional#empty()} otherwise
	 * @review
	 */
	public Optional<Map<String, String>> getNamesOptional() {
		return Optional.ofNullable(_names);
	}

	/**
	 * Returns the nested collection routes for the nested collection resource's
	 * name.
	 *
	 * @param  name the parent resource's name
	 * @param  nestedName the nested collection resource's name
	 * @param  computeEmptyFunction the function that can be called to compute
	 *         the data
	 * @return the nested collection routes
	 * @review
	 */
	public <S, T> Optional<NestedCollectionRoutes<T, S>>
		getNestedCollectionRoutesOptional(
			String name, String nestedName,
			EmptyFunction computeEmptyFunction) {

		if (_nestedCollectionRoutes == null) {
			computeEmptyFunction.invoke();
		}

		Optional<Map<String, NestedCollectionRoutes>> optional =
			Optional.ofNullable(_nestedCollectionRoutes);

		return optional.map(
			map -> map.get(name + "-" + nestedName)
		).map(
			Unsafe::unsafeCast
		);
	}

	/**
	 * Returns the representor of the collection resource's model class, if that
	 * representor exists. Returns {@code Optional#empty()} otherwise.
	 *
	 * @param  name the representor's name
	 * @param  computeEmptyFunction the function that can be called to compute
	 *         the data
	 * @return the model class's representor, if present; {@code
	 *         Optional#empty()} otherwise
	 */
	public <U, T> Optional<Representor<T, U>> getRepresentorOptional(
		String name, EmptyFunction computeEmptyFunction) {

		if (_representors == null) {
			computeEmptyFunction.invoke();
		}

		Optional<Map<String, Representor>> optional = Optional.ofNullable(
			_representors);

		return optional.map(
			map -> map.get(name)
		).map(
			Unsafe::unsafeCast
		);
	}

	/**
	 * Returns the nested collection routes for the reusable nested collection
	 * resource's name.
	 *
	 * @param  name the reusable nested collection resource's name
	 * @param  computeEmptyFunction the function that can be called to compute
	 *         the data
	 * @return the nested collection routes
	 */
	public <S, T> Optional<NestedCollectionRoutes<T, S>>
		getReusableNestedCollectionRoutesOptional(
			String name, EmptyFunction computeEmptyFunction) {

		if (_reusableNestedCollectionRoutes == null) {
			computeEmptyFunction.invoke();
		}

		Optional<Map<String, NestedCollectionRoutes>> optional =
			Optional.ofNullable(_reusableNestedCollectionRoutes);

		return optional.map(
			map -> map.get(name)
		).map(
			Unsafe::unsafeCast
		);
	}

	/**
	 * Returns a list containing the names of the root resources with routes.
	 *
	 * @param  computeEmptyFunction the function that can be called to compute
	 *         the data
	 * @return a list containing the names of the root resources with routes.
	 * @review
	 */
	public List<String> getRootResourceNames(
		EmptyFunction computeEmptyFunction) {

		if (_rootResourceNames == null) {
			computeEmptyFunction.invoke();
		}

		Optional<List<String>> optional = Optional.ofNullable(
			_rootResourceNames);

		return optional.orElseGet(Collections::emptyList);
	}

	/**
	 * Adds a collection routes
	 *
	 * @param  key the key
	 * @param  collectionRoutes the collection routes
	 * @review
	 */
	public void putCollectionRoutes(
		String key, CollectionRoutes collectionRoutes) {

		if (_collectionRoutes == null) {
			_collectionRoutes = new HashMap<>();
		}

		_collectionRoutes.put(key, collectionRoutes);
	}

	/**
	 * Adds an identifier class
	 *
	 * @param  key the key
	 * @param  identifierClass the identifier class
	 * @review
	 */
	public void putIdentifierClass(
		String key, Class<Identifier> identifierClass) {

		if (_identifierClasses == null) {
			_identifierClasses = new HashMap<>();
		}

		_identifierClasses.put(key, identifierClass);
	}

	/**
	 * Adds an item routes
	 *
	 * @param  key the key
	 * @param  itemRoutes the item routes
	 * @review
	 */
	public void putItemRoutes(String key, ItemRoutes itemRoutes) {
		if (_itemRoutes == null) {
			_itemRoutes = new HashMap<>();
		}

		_itemRoutes.put(key, itemRoutes);
	}

	/**
	 * Adds a resource name
	 *
	 * @param  key the key
	 * @param  name the resource name
	 * @review
	 */
	public void putName(String key, String name) {
		if (_names == null) {
			_names = new HashMap<>();
		}

		_names.put(key, name);
	}

	/**
	 * Adds a nested collection routes
	 *
	 * @param  key the key
	 * @param  nestedCollectionRoutes the nested collection routes
	 * @review
	 */
	public void putNestedCollectionRoutes(
		String key, NestedCollectionRoutes nestedCollectionRoutes) {

		if (_nestedCollectionRoutes == null) {
			_nestedCollectionRoutes = new HashMap<>();
		}

		_nestedCollectionRoutes.put(key, nestedCollectionRoutes);
	}

	/**
	 * Adds a representors
	 *
	 * @param  key the key
	 * @param  representor the representor
	 * @review
	 */
	public void putRepresentor(String key, Representor representor) {
		if (_representors == null) {
			_representors = new HashMap<>();
		}

		_representors.put(key, representor);
	}

	/**
	 * Adds a reusable nested collection routes
	 *
	 * @param  key the key
	 * @param  reusableNestedCollectionRoutes the reusable nested collection
	 *         routes
	 * @review
	 */
	public void putReusableNestedCollectionRoutes(
		String key, NestedCollectionRoutes reusableNestedCollectionRoutes) {

		if (_reusableNestedCollectionRoutes == null) {
			_reusableNestedCollectionRoutes = new HashMap<>();
		}

		_reusableNestedCollectionRoutes.put(
			key, reusableNestedCollectionRoutes);
	}

	/**
	 * Adds a root resource name
	 *
	 * @param  rootResourceName the root resource name
	 * @review
	 */
	public void putRootResourceName(String rootResourceName) {
		if (_rootResourceNames == null) {
			_rootResourceNames = new ArrayList<>();
		}

		_rootResourceNames.add(rootResourceName);
	}

	private ManagerCache() {
	}

	private Map<String, CollectionRoutes> _collectionRoutes;
	private Map<String, Class<Identifier>> _identifierClasses;
	private Map<String, ItemRoutes> _itemRoutes;
	private Map<String, String> _names;
	private Map<String, NestedCollectionRoutes> _nestedCollectionRoutes;
	private Map<String, Representor> _representors;
	private Map<String, NestedCollectionRoutes> _reusableNestedCollectionRoutes;
	private List<String> _rootResourceNames;

}
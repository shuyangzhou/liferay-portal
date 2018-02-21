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

package com.liferay.apio.architect.wiring.osgi.internal.manager;

import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;

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
	 * Returns the map containing the collection routes if they have been set.
	 * Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the map containing the collection routes if they have been set;
	 *         returns {@code Optional#empty()} otherwise
	 * @review
	 */
	public Optional<Map<String, CollectionRoutes>>
		getCollectionRoutesOptional() {

		return Optional.ofNullable(_collectionRoutes);
	}

	/**
	 * Returns the map containing the classes for the different resource names
	 * if they have been set. Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the map containing the classes for the different resource names
	 *         if they have been set; returns {@code Optional#empty()} otherwise
	 * @review
	 */
	public Optional<Map<String, Class<Identifier>>>
		getIdentifierClassesOptional() {

		return Optional.ofNullable(_identifierClasses);
	}

	/**
	 * Returns the map containing the item routes if they have been set. Returns
	 * {@code Optional#empty()} otherwise.
	 *
	 * @return the map containing the item routes if they have been set; returns
	 *         {@code Optional#empty()} otherwise
	 * @review
	 */
	public Optional<Map<String, ItemRoutes>> getItemRoutesOptional() {
		return Optional.ofNullable(_itemRoutes);
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
	 * Returns the map containing the nested collection routes if they have been
	 * set. Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the map containing the nested collection routes if they have been
	 *         set; returns {@code Optional#empty()} otherwise
	 * @review
	 */
	public Optional<Map<String, NestedCollectionRoutes>>
		getNestedCollectionRoutesOptional() {

		return Optional.ofNullable(_nestedCollectionRoutes);
	}

	/**
	 * Returns the map containing the representors for the different resource
	 * names if they have been set. Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the map containing the representors for the different resource
	 *         names if they have been set; returns {@code Optional#empty()}
	 *         otherwise
	 * @review
	 */
	public Optional<Map<String, Representor>> getRepresentorsOptional() {
		return Optional.ofNullable(_representors);
	}

	/**
	 * Returns the map containing the reusable nested collection routes if they
	 * have been set. Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the map containing the reusable nested collection routes if they
	 *         have been set; returns {@code Optional#empty()} otherwise
	 * @review
	 */
	public Optional<Map<String, NestedCollectionRoutes>>
		getReusableNestedCollectionRoutesOptional() {

		return Optional.ofNullable(_reusableNestedCollectionRoutes);
	}

	/**
	 * Returns the list of root resource names if they have been set. Returns
	 * {@code Optional#empty()} otherwise.
	 *
	 * @return the list of root resource names if they have been set; {@code
	 *         Optional#empty()} otherwise
	 * @review
	 */
	public Optional<List<String>> getRootResourceNamesOptional() {
		return Optional.ofNullable(_rootResourceNames);
	}

	/**
	 * Returns {@code true} if the collection routes have been set in the cache.
	 *
	 * @return {@code true} if the collection routes have been set
	 * @review
	 */
	public boolean hasCollectionRoutes() {
		if (_collectionRoutes != null) {
			return true;
		}

		return false;
	}

	/**
	 * Returns {@code true} if the identifier classes have been set in the
	 * cache.
	 *
	 * @return {@code true} if the identifier classes have been set
	 * @review
	 */
	public boolean hasIdentifierClasses() {
		if (_identifierClasses != null) {
			return true;
		}

		return false;
	}

	/**
	 * Returns {@code true} if the item routes have been set in the cache.
	 *
	 * @return {@code true} if the item routes have been set
	 * @review
	 */
	public boolean hasItemRoutes() {
		if (_itemRoutes != null) {
			return true;
		}

		return false;
	}

	/**
	 * Returns {@code true} if the resource names have been set in the cache.
	 *
	 * @return {@code true} if the resource names have been set
	 * @review
	 */
	public boolean hasNames() {
		if (_names != null) {
			return true;
		}

		return false;
	}

	/**
	 * Returns {@code true} if the nested collection routes have been set in the
	 * cache.
	 *
	 * @return {@code true} if the nested collection routes have been set
	 * @review
	 */
	public boolean hasNestedCollectionRoutes() {
		if (_nestedCollectionRoutes != null) {
			return true;
		}

		return false;
	}

	/**
	 * Returns {@code true} if the representors have been set in the cache.
	 *
	 * @return {@code true} if the representors have been set
	 * @review
	 */
	public boolean hasRepresentors() {
		if (_representors != null) {
			return true;
		}

		return false;
	}

	/**
	 * Returns {@code true} if the reusable nested collection routes have been
	 * set in the cache.
	 *
	 * @return {@code true} if the reusable nested collection routes have been
	 *         set
	 * @review
	 */
	public boolean hasReusableNestedCollectionRoutes() {
		if (_reusableNestedCollectionRoutes != null) {
			return true;
		}

		return false;
	}

	/**
	 * Returns {@code true} if the root resource names have been set in the
	 * cache.
	 *
	 * @return {@code true} if the root resource names have been set
	 * @review
	 */
	public boolean hasRootResourceNames() {
		if (_rootResourceNames != null) {
			return true;
		}

		return false;
	}

	/**
	 * Sets the collection routes
	 *
	 * @param  collectionRoutes the collection routes
	 * @review
	 */
	public void setCollectionRoutes(
		Map<String, CollectionRoutes> collectionRoutes) {

		_collectionRoutes = collectionRoutes;
	}

	/**
	 * Sets the identifier classes
	 *
	 * @param  identifierClasses the identifier classes
	 * @review
	 */
	public void setIdentifierClasses(
		Map<String, Class<Identifier>> identifierClasses) {

		_identifierClasses = identifierClasses;
	}

	/**
	 * Sets the item routes
	 *
	 * @param  itemRoutes the item routes
	 * @review
	 */
	public void setItemRoutes(Map<String, ItemRoutes> itemRoutes) {
		_itemRoutes = itemRoutes;
	}

	/**
	 * Sets the resource names
	 *
	 * @param  names the resource names
	 * @review
	 */
	public void setNames(Map<String, String> names) {
		_names = names;
	}

	/**
	 * Sets the nested collection routes
	 *
	 * @param  nestedCollectionRoutes the nested collection routes
	 * @review
	 */
	public void setNestedCollectionRoutes(
		Map<String, NestedCollectionRoutes> nestedCollectionRoutes) {

		_nestedCollectionRoutes = nestedCollectionRoutes;
	}

	/**
	 * Sets the representors
	 *
	 * @param  representors the representors
	 * @review
	 */
	public void setRepresentors(Map<String, Representor> representors) {
		_representors = representors;
	}

	/**
	 * Sets the reusable nested collection routes
	 *
	 * @param  reusableNestedCollectionRoutes the reusable nested collection
	 *         routes
	 * @review
	 */
	public void setReusableNestedCollectionRoutes(
		Map<String, NestedCollectionRoutes> reusableNestedCollectionRoutes) {

		_reusableNestedCollectionRoutes = reusableNestedCollectionRoutes;
	}

	/**
	 * Sets the root resource names
	 *
	 * @param  rootResourceNames the root resource names
	 * @review
	 */
	public void setRootResourceNames(List<String> rootResourceNames) {
		_rootResourceNames = rootResourceNames;
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
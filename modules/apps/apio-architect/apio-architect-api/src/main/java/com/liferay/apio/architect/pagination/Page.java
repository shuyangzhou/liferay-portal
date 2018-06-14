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

package com.liferay.apio.architect.pagination;

import aQute.bnd.annotation.ProviderType;

import com.liferay.apio.architect.operation.Operation;
import com.liferay.apio.architect.uri.Path;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Represents a page in a collection. Writers can use instances of this
 * interface to create hypermedia representations.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 * @param  <T> the model's type
 */
@ProviderType
public interface Page<T> {

	/**
	 * Returns the page's items.
	 *
	 * @return the page's items
	 */
	public Collection<T> getItems();

	/**
	 * Returns the number of items the user selected on the page.
	 *
	 * @return the number of items the user selected on the page
	 */
	public int getItemsPerPage();

	/**
	 * Returns the number of the collection's last page.
	 *
	 * @return the number of the collection's last page
	 */
	public int getLastPageNumber();

	/**
	 * Returns the list of operations for the page.
	 *
	 * @return the list of operations
	 */
	public List<Operation> getOperations();

	/**
	 * Returns the page number in the collection.
	 *
	 * @return the page number in the collection
	 */
	public int getPageNumber();

	/**
	 * Returns the page path, if present. Returns {@code Optional#empty()}
	 * otherwise.
	 *
	 * @return the path, if present; {@code Optional#empty()} otherwise
	 */
	public Optional<Path> getPathOptional();

	/**
	 * Returns the resource's name.
	 *
	 * @return the resource's name
	 */
	public String getResourceName();

	/**
	 * Returns the total number of elements in the collection.
	 *
	 * @return the total number of elements in the collection
	 */
	public int getTotalCount();

	/**
	 * Returns {@code true} if another page follows this page in the collection.
	 *
	 * @return {@code true} if another page follows this page in the collection;
	 *         {@code false} otherwise
	 */
	public boolean hasNext();

	/**
	 * Returns {@code true} if another page precedes this page in the
	 * collection.
	 *
	 * @return {@code true} if another page precedes this page in the
	 *         collection; {@code false} otherwise
	 */
	public boolean hasPrevious();

}
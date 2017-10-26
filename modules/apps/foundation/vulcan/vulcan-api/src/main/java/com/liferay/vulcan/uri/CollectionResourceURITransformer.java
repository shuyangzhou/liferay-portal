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

package com.liferay.vulcan.uri;

import aQute.bnd.annotation.ConsumerType;

import com.liferay.vulcan.pagination.Page;
import com.liferay.vulcan.pagination.SingleModel;

/**
 * Customizes the URIs of a resource that follows the collection pattern. For
 * example, an instance of this interface can be used to add a prefix to each
 * URI.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@ConsumerType
public interface CollectionResourceURITransformer {

	/**
	 * Returns the binary endpoint's transformed URI.
	 *
	 * @param  uri the binary endpoint's URI
	 * @param  singleModel the single model
	 * @param  binaryId the binary endpoint's ID
	 * @return the transformed URI
	 */
	public <T> String transformBinaryURI(
		String uri, SingleModel<T> singleModel, String binaryId);

	/**
	 * Returns a collection item endpoint's transformed URI.
	 *
	 * @param  uri the collection item's URI
	 * @param  singleModel the single model
	 * @return the transformed URI
	 */
	public <T> String transformCollectionItemSingleResourceURI(
		String uri, SingleModel<T> singleModel);

	/**
	 * Returns the page endpoint's transformed URI.
	 *
	 * @param  uri the page's URI
	 * @param  page the page
	 * @return the page endpoint's transformed URI
	 */
	public <T> String transformPageURI(String uri, Page<T> page);

}
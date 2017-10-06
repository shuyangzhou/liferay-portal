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

package com.liferay.vulcan.resource.identifier;

/**
 * Instances of this identifier represents a simple identifier with a
 * <code>long</code> value ID. This value can be retrieved with the {@link
 * #getId()} method.
 *
 * @author Alejandro Hernández
 */
public interface LongIdentifier extends Identifier {

	/**
	 * Returns the <code>long</code> ID of this identifier.
	 *
	 * <p>
	 * For example with a resource obtained in the URL:
	 * <code>/p/product/100</code> this method will return <code>100</code> as a
	 * <code>long</code> value.
	 * </p>
	 *
	 * @return the ID of the identifier.
	 */
	public long getId();

}
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

package com.liferay.apio.architect.sample.internal.type;

import com.liferay.apio.architect.annotation.Vocabulary.Field;
import com.liferay.apio.architect.annotation.Vocabulary.Type;

/**
 * Instances of this interface represent a postal address exposed through the
 * API.
 *
 * @author Alejandro Hernández
 * @see    <a href="https://schema.org/PostalAddress">PostalAddress</a>
 * @review
 */
@Type("PostalAddress")
public interface PostalAddress {

	/**
	 * Returns the address's country.
	 *
	 * @return the address's country
	 * @see    <a href="https://schema.org/addressCountry">addressCountry</a>
	 * @review
	 */
	@Field("addressCountry")
	public String getAddressCountry();

	/**
	 * Returns the address's locality.
	 *
	 * @return the address's locality
	 * @see    <a href="https://schema.org/addressLocality">addressLocality</a>
	 * @review
	 */
	@Field("addressLocality")
	public String getAddressLocality();

	/**
	 * Returns the address's region.
	 *
	 * @return the address's region
	 * @see    <a href="https://schema.org/addressRegion">addressRegion</a>
	 * @review
	 */
	@Field("addressRegion")
	public String getAddressRegion();

	/**
	 * Returns the address's postal code.
	 *
	 * @return the address's postal code
	 * @see    <a href="https://schema.org/postalCode">postalCode</a>
	 * @review
	 */
	@Field("postalCode")
	public String getPostalCode();

	/**
	 * Returns the address's street.
	 *
	 * @return the address's street
	 * @see    <a href="https://schema.org/streetAddress">streetAddress</a>
	 * @review
	 */
	@Field("streetAddress")
	public String getStreetAddress();

}
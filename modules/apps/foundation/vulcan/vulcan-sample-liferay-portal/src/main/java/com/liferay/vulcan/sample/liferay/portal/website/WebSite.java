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

package com.liferay.vulcan.sample.liferay.portal.website;

import aQute.bnd.annotation.ProviderType;

import com.liferay.vulcan.language.Language;
import com.liferay.vulcan.resource.identifier.LongIdentifier;

/**
 * An instance of this interface represents a website.
 *
 * <p>
 * Conforms with the <a href="http://schema.org/WebSite">WebSite</a> type from
 * schema.org
 * </p>
 *
 * @author Victor Oliveira
 * @author Alejandro Hernández
 * @review
 */
@ProviderType
public interface WebSite {

	/**
	 * Returns the description of the {@code WebSite}.
	 *
	 * @return the description of the {@code WebSite}.
	 * @review
	 */
	public String getDescription();

	/**
	 * Returns the name of the {@code WebSite}.
	 *
	 * @param  language the requested language.
	 * @return the name of the {@code WebSite}.
	 * @review
	 */
	public String getName(Language language);

	/**
	 * Returns the identifier of the {@code WebSite}.
	 *
	 * @return the identifier of the {@code WebSite}.
	 * @review
	 */
	public LongIdentifier getWebSiteLongIdentifier();

}
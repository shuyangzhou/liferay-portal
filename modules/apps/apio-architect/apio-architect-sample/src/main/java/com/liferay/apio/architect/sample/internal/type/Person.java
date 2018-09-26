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

import com.liferay.apio.architect.annotation.Id;
import com.liferay.apio.architect.annotation.Vocabulary.Field;
import com.liferay.apio.architect.annotation.Vocabulary.RelativeURL;
import com.liferay.apio.architect.annotation.Vocabulary.Type;
import com.liferay.apio.architect.identifier.Identifier;

import java.util.Date;
import java.util.List;

/**
 * Instances of this interface represent a person exposed through the API.
 *
 * @author Alejandro Hernández
 * @see    <a href="https://schema.org/Person">Person</a>
 * @review
 */
@Type("Person")
public interface Person extends Identifier<Long> {

	/**
	 * Returns the person's birth date.
	 *
	 * @return the person's birth date
	 * @see    <a href="https://schema.org/birthDate">birthDate</a>
	 * @review
	 */
	@Field("birthDate")
	public Date getBirthDate();

	/**
	 * Returns the person's email.
	 *
	 * @return the person's email
	 * @see    <a href="https://schema.org/email">email</a>
	 * @review
	 */
	@Field("email")
	public String getEmail();

	/**
	 * Returns the person's family name.
	 *
	 * @return the person's family name
	 * @see    <a href="https://schema.org/familyName">familyName</a>
	 * @review
	 */
	@Field("familyName")
	public String getFamilyName();

	/**
	 * Returns the person's given name.
	 *
	 * @return the person's given name
	 * @see    <a href="https://schema.org/givenName">givenName</a>
	 * @review
	 */
	@Field("givenName")
	public String getGivenName();

	/**
	 * Returns the person's ID.
	 *
	 * @return the person's ID
	 * @review
	 */
	@Id
	public Long getId();

	/**
	 * Returns the person's image URL.
	 *
	 * @return the person's image URL
	 * @see    <a href="https://schema.org/image">image</a>
	 * @review
	 */
	@Field("image")
	@RelativeURL(fromApplication = true)
	public String getImage();

	/**
	 * Returns the list of the person's job titles.
	 *
	 * @return the list of the person's job titles.
	 * @see    <a href="https://schema.org/jobTitle">jobTitle</a>
	 * @review
	 */
	@Field("jobTitle")
	public List<String> getJobTitles();

	/**
	 * Returns the person's full name.
	 *
	 * @return the person's full name
	 * @see    <a href="https://schema.org/name">name</a>
	 * @review
	 */
	@Field("name")
	public String getName();

	/**
	 * Returns the person's postal address.
	 *
	 * @return the person's postal address
	 * @see    <a href="https://schema.org/postalAddress">postalAddress</a>
	 * @review
	 */
	@Field("postalAddress")
	public PostalAddress getPostalAddress();

}
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

package com.liferay.apio.architect.sample.internal.converter;

import com.liferay.apio.architect.sample.internal.dto.PersonModel;
import com.liferay.apio.architect.sample.internal.dto.PostalAddressModel;
import com.liferay.apio.architect.sample.internal.jaxrs.AvatarResource;
import com.liferay.apio.architect.sample.internal.type.Person;
import com.liferay.apio.architect.sample.internal.type.PostalAddress;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

/**
 * Provides methods for creating {@link Person} instances out of other DTOs.
 *
 * @author Alejandro Hernández
 * @review
 */
public class PersonConverter {

	/**
	 * Converts a {@link PersonModel} to a {@link Person}.
	 *
	 * @param  personModel the internal person model
	 * @return a {@link Person}
	 * @review
	 */
	public static Person toPerson(PersonModel personModel) {
		return new Person() {

			@Override
			public Date getBirthDate() {
				Date birthDate = personModel.getBirthDate();

				return new Date(birthDate.getTime());
			}

			@Override
			public String getEmail() {
				return personModel.getEmail();
			}

			@Override
			public String getFamilyName() {
				return personModel.getLastName();
			}

			@Override
			public String getGivenName() {
				return personModel.getFirstName();
			}

			@Override
			public Long getId() {
				return personModel.getId();
			}

			@Override
			public String getImage() {
				return UriBuilder.fromResource(
					AvatarResource.class
				).path(
					AvatarResource.class, "getAvatar"
				).build(
					getId()
				).toString();
			}

			@Override
			public List<String> getJobTitles() {
				return personModel.getJobTitles();
			}

			@Override
			public String getName() {
				return personModel.getFirstName() + " " +
					personModel.getLastName();
			}

			@Override
			public PostalAddress getPostalAddress() {
				return _toPostalAddress(personModel.getPostalAddressModel());
			}

		};
	}

	/**
	 * Converts a {@link PostalAddressModel} to a {@link PostalAddress}.
	 *
	 * @param  postalAddressModel the internal postal address model
	 * @return a {@link PostalAddress}
	 * @review
	 */
	private static PostalAddress _toPostalAddress(
		PostalAddressModel postalAddressModel) {

		return new PostalAddress() {

			@Override
			public String getAddressCountry() {
				return postalAddressModel.getCountryCode();
			}

			@Override
			public String getAddressLocality() {
				return postalAddressModel.getCity();
			}

			@Override
			public String getAddressRegion() {
				return postalAddressModel.getState();
			}

			@Override
			public String getPostalCode() {
				return postalAddressModel.getZipCode();
			}

			@Override
			public String getStreetAddress() {
				return postalAddressModel.getStreetAddress();
			}

		};
	}

}
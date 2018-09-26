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

import static com.liferay.apio.architect.sample.internal.converter.DemoDataUtil.PERSON_MODEL;
import static com.liferay.apio.architect.sample.internal.converter.PersonConverter.toPerson;

import static org.exparity.hamcrest.date.DateMatchers.isToday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import com.liferay.apio.architect.sample.internal.type.Person;
import com.liferay.apio.architect.sample.internal.type.PostalAddress;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class PersonConverterTest {

	@Test
	public void testToPerson() {
		Person person = toPerson(PERSON_MODEL);

		assertThat(person.getBirthDate(), isToday());
		assertThat(person.getEmail(), is("email@liferay.com"));
		assertThat(person.getFamilyName(), is("Family"));
		assertThat(person.getGivenName(), is("Given"));
		assertThat(person.getId(), is(84L));
		assertThat(person.getImage(), is("/images/84"));
		assertThat(person.getJobTitles(), contains("Job 1", "Job 2"));
		assertThat(person.getName(), is("Given Family"));

		PostalAddress postalAddress = person.getPostalAddress();

		assertThat(postalAddress.getAddressCountry(), is("country"));
		assertThat(postalAddress.getAddressLocality(), is("city"));
		assertThat(postalAddress.getAddressRegion(), is("state"));
		assertThat(postalAddress.getPostalCode(), is("zip"));
		assertThat(postalAddress.getStreetAddress(), is("street"));
	}

}
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

package com.liferay.apio.architect.sample.internal.router;

import static com.liferay.apio.architect.sample.internal.auth.PermissionChecker.hasPermission;
import static com.liferay.apio.architect.sample.internal.converter.PersonConverter.toPerson;

import com.liferay.apio.architect.annotation.Actions.Create;
import com.liferay.apio.architect.annotation.Actions.Remove;
import com.liferay.apio.architect.annotation.Actions.Replace;
import com.liferay.apio.architect.annotation.Actions.Retrieve;
import com.liferay.apio.architect.annotation.Body;
import com.liferay.apio.architect.annotation.Id;
import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.router.ActionRouter;
import com.liferay.apio.architect.sample.internal.converter.PersonConverter;
import com.liferay.apio.architect.sample.internal.dao.PersonModelService;
import com.liferay.apio.architect.sample.internal.dto.PersonModel;
import com.liferay.apio.architect.sample.internal.dto.PostalAddressModel;
import com.liferay.apio.architect.sample.internal.type.Person;
import com.liferay.apio.architect.sample.internal.type.PostalAddress;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides all the information necessary to expose <a
 * href="http://schema.org/Person">Person</a> resources through a web API.
 *
 * @author Alejandro Hernández
 */
@Component(
	immediate = true, service = {ActionRouter.class, PersonActionRouter.class}
)
public class PersonActionRouter implements ActionRouter<Person> {

	@Create
	public Person create(@Body Person person, Credentials credentials) {
		if (!hasPermission(credentials)) {
			throw new ForbiddenException();
		}

		PostalAddress postalAddress = person.getPostalAddress();

		PostalAddressModel postalAddressModel = new PostalAddressModel(
			postalAddress.getAddressCountry(), postalAddress.getAddressRegion(),
			postalAddress.getAddressLocality(), postalAddress.getPostalCode(),
			postalAddress.getStreetAddress());

		PersonModel personModel = _personModelService.create(
			postalAddressModel, person.getImage(), person.getBirthDate(),
			person.getEmail(), person.getGivenName(), person.getJobTitles(),
			person.getFamilyName());

		return toPerson(personModel);
	}

	@Remove
	public void remove(@Id long id, Credentials credentials) {
		if (!hasPermission(credentials)) {
			throw new ForbiddenException();
		}

		_personModelService.remove(id);
	}

	@Replace
	public Person replace(
		@Id long id, @Body Person person, Credentials credentials) {

		if (!hasPermission(credentials)) {
			throw new ForbiddenException();
		}

		PostalAddress postalAddress = person.getPostalAddress();

		PostalAddressModel postalAddressModel = new PostalAddressModel(
			postalAddress.getAddressCountry(), postalAddress.getAddressRegion(),
			postalAddress.getAddressLocality(), postalAddress.getPostalCode(),
			postalAddress.getStreetAddress());

		Optional<PersonModel> optional = _personModelService.update(
			postalAddressModel, person.getImage(), person.getBirthDate(),
			person.getEmail(), person.getGivenName(), person.getJobTitles(),
			person.getFamilyName(), id);

		return optional.map(
			PersonConverter::toPerson
		).orElseThrow(
			() -> new NotFoundException("Unable to get person " + id)
		);
	}

	@Retrieve
	public Person retrieve(@Id long id) {
		Optional<PersonModel> optional = _personModelService.get(id);

		return optional.map(
			PersonConverter::toPerson
		).orElseThrow(
			() -> new NotFoundException("Unable to get person " + id)
		);
	}

	@Retrieve
	public PageItems<Person> retrieveCollection(Pagination pagination) {
		List<PersonModel> personModels = _personModelService.getPage(
			pagination.getStartPosition(), pagination.getEndPosition());
		int count = _personModelService.getCount();

		Stream<PersonModel> stream = personModels.stream();

		List<Person> persons = stream.map(
			PersonConverter::toPerson
		).collect(
			Collectors.toList()
		);

		return new PageItems<>(persons, count);
	}

	@Reference
	private PersonModelService _personModelService;

}
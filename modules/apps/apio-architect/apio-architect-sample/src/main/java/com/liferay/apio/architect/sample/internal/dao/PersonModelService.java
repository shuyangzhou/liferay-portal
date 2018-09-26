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

package com.liferay.apio.architect.sample.internal.dao;

import static java.util.Calendar.YEAR;
import static java.util.concurrent.TimeUnit.DAYS;

import com.github.javafaker.Address;
import com.github.javafaker.DateAndTime;
import com.github.javafaker.Faker;
import com.github.javafaker.Internet;
import com.github.javafaker.Name;
import com.github.javafaker.service.RandomService;

import com.liferay.apio.architect.sample.internal.dto.PersonModel;
import com.liferay.apio.architect.sample.internal.dto.PostalAddressModel;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * Contains methods for creating, retrieving, updating, and deleting persons in
 * an in-memory database with fake data.
 *
 * @author Alejandro Hernández
 */
@Component(immediate = true, service = PersonModelService.class)
public class PersonModelService {

	/**
	 * Computes the fake data for this model class.
	 */
	@Activate
	public void activate() {
		for (long index = 0; index < 10; index++) {
			Faker faker = new Faker();

			Internet internet = faker.internet();

			DateAndTime dateAndTime = faker.date();

			Calendar calendar = Calendar.getInstance();

			calendar.add(YEAR, -21);

			Date birthDate = dateAndTime.past(10000, DAYS, calendar.getTime());

			Name name = faker.name();

			RandomService randomService = faker.random();

			List<String> jobTitles = IntStream.range(
				0, randomService.nextInt(5)
			).mapToObj(
				__ -> name.title()
			).collect(
				Collectors.toList()
			);

			Address address = faker.address();

			PostalAddressModel postalAddressModel = new PostalAddressModel(
				address.countryCode(), address.state(), address.city(),
				address.zipCode(), address.streetAddress());

			PersonModel personModel = new PersonModel(
				internet.avatar(), birthDate, internet.safeEmailAddress(),
				name.firstName(), jobTitles, name.lastName(),
				postalAddressModel, _count.get());

			_personModels.put(_count.getAndIncrement(), personModel);
		}
	}

	/**
	 * Adds a new person.
	 *
	 * @param  postalAddressModel the person's address
	 * @param  avatar the person's avatar
	 * @param  birthDate the person's birth date
	 * @param  email the person's email
	 * @param  firstName the person's first name
	 * @param  jobTitles the person's job titles
	 * @param  lastName the person's last name
	 * @return the new person
	 */
	public PersonModel create(
		PostalAddressModel postalAddressModel, String avatar, Date birthDate,
		String email, String firstName, List<String> jobTitles,
		String lastName) {

		PersonModel personModel = new PersonModel(
			avatar, birthDate, email, firstName, jobTitles, lastName,
			postalAddressModel, _count.get());

		_personModels.put(_count.getAndIncrement(), personModel);

		return personModel;
	}

	/**
	 * Returns the person that matches the specified ID, if that person exists;
	 * returns {@code Optional#empty()} otherwise.
	 *
	 * @param  id the person's ID
	 * @return the person, if present; {@code Optional#empty()} otherwise
	 */
	public Optional<PersonModel> get(long id) {
		return Optional.ofNullable(_personModels.get(id));
	}

	/**
	 * Returns the total number of persons.
	 *
	 * @return the total number of persons
	 */
	public int getCount() {
		return _personModels.size();
	}

	/**
	 * Returns a page of persons, as specified by the page's start and end
	 * positions.
	 *
	 * @param  start the page's start position
	 * @param  end the page's end position
	 * @return the page of persons
	 */
	public List<PersonModel> getPage(int start, int end) {
		Collection<PersonModel> personModels = _personModels.values();

		Stream<PersonModel> stream = personModels.stream();

		return stream.skip(
			start
		).limit(
			end
		).collect(
			Collectors.toList()
		);
	}

	/**
	 * Deletes a person that matches the specified ID.
	 *
	 * @param id the person's ID
	 */
	public void remove(long id) {
		_personModels.remove(id);
	}

	/**
	 * Updates the person that matches the specified ID, if that person exists;
	 * returns {@code Optional#empty()} otherwise.
	 *
	 * @param  postalAddressModel the person's address
	 * @param  avatar the person's avatar
	 * @param  birthDate the person's birth date
	 * @param  email the person's email
	 * @param  firstName the person's first name
	 * @param  jobTitles the person's job titles
	 * @param  lastName the person's last name
	 * @param  id the person's ID
	 * @return the updated person, if present; {@code Optional#empty()}
	 *         otherwise
	 */
	public Optional<PersonModel> update(
		PostalAddressModel postalAddressModel, String avatar, Date birthDate,
		String email, String firstName, List<String> jobTitles, String lastName,
		long id) {

		PersonModel personModel = _personModels.get(id);

		if (personModel == null) {
			return Optional.empty();
		}

		personModel = new PersonModel(
			avatar, birthDate, email, firstName, jobTitles, lastName,
			postalAddressModel, id);

		_personModels.put(id, personModel);

		return Optional.of(personModel);
	}

	private final AtomicLong _count = new AtomicLong(0);
	private final Map<Long, PersonModel> _personModels =
		new ConcurrentHashMap<>();

}
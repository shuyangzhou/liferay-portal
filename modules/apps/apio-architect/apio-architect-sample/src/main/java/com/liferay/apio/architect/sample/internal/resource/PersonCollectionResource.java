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

package com.liferay.apio.architect.sample.internal.resource;

import static com.liferay.apio.architect.sample.internal.auth.PermissionChecker.hasPermission;

import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.resource.CollectionResource;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.sample.internal.auth.PermissionChecker;
import com.liferay.apio.architect.sample.internal.router.PersonActionRouter;
import com.liferay.apio.architect.sample.internal.type.Person;
import com.liferay.apio.architect.sample.internal.type.PostalAddress;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Hernández
 */
@Component(service = CollectionResource.class)
public class PersonCollectionResource
	implements CollectionResource
		<Person, Long, PersonCollectionResource.PersonIdentifier> {

	@Override
	public CollectionRoutes<Person, Long> collectionRoutes(
		CollectionRoutes.Builder<Person, Long> builder) {

		return builder.addGetter(
			_personActionRouter::retrieveCollection
		).addCreator(
			_personActionRouter::create, Credentials.class,
			PermissionChecker::hasPermission,
			PersonCollectionResource::_buildPersonForm
		).build();
	}

	@Override
	public String getName() {
		return "people-dsl";
	}

	@Override
	public ItemRoutes<Person, Long> itemRoutes(
		ItemRoutes.Builder<Person, Long> builder) {

		return builder.addGetter(
			_personActionRouter::retrieve
		).addRemover(
			_personActionRouter::remove, Credentials.class,
			(credentials, id) -> hasPermission(credentials)
		).addUpdater(
			_personActionRouter::replace, Credentials.class,
			(credentials, id) -> hasPermission(credentials),
			PersonCollectionResource::_buildPersonForm
		).build();
	}

	@Override
	public Representor<Person> representor(
		Representor.Builder<Person, Long> builder) {

		return builder.types(
			"PersonDSL"
		).identifier(
			Person::getId
		).addDate(
			"birthDate", Person::getBirthDate
		).addNested(
			"address", Person::getPostalAddress,
			nestedBuilder -> nestedBuilder.types(
				"PostalAddressDSL"
			).addString(
				"addressCountry", PostalAddress::getAddressCountry
			).addString(
				"addressLocality", PostalAddress::getAddressLocality
			).addString(
				"addressRegion", PostalAddress::getAddressRegion
			).addString(
				"postalCode", PostalAddress::getPostalCode
			).addString(
				"streetAddress", PostalAddress::getStreetAddress
			).build()
		).addApplicationRelativeURL(
			"image", Person::getImage
		).addString(
			"email", Person::getEmail
		).addString(
			"familyName", Person::getFamilyName
		).addString(
			"givenName", Person::getGivenName
		).addStringList(
			"jobTitle", Person::getJobTitles
		).addString(
			"name", Person::getName
		).build();
	}

	public interface PersonIdentifier extends Identifier<Long> {
	}

	private static Form<PersonForm> _buildPersonForm(
		Form.Builder<PersonForm> formBuilder) {

		return formBuilder.title(
			__ -> "The person form"
		).description(
			__ -> "This form can be used to create or update a person"
		).constructor(
			PersonForm::new
		).addRequiredDate(
			"birthDate", PersonForm::_setBirthDate
		).addOptionalStringList(
			"jobTitle", PersonForm::_setJobTitle
		).addRequiredNestedModel(
			"postalAddress", PersonCollectionResource::_buildPostalAddressForm,
			PersonForm::_setPostalAddress
		).addRequiredString(
			"givenName", PersonForm::_setGivenName
		).addRequiredString(
			"image", PersonForm::_setImage
		).addRequiredString(
			"email", PersonForm::_setEmail
		).addRequiredString(
			"familyName", PersonForm::_setFamilyName
		).build();
	}

	private static Form<PostalAddressForm> _buildPostalAddressForm(
		Form.Builder<PostalAddressForm> postalAddressFormBuilder) {

		return postalAddressFormBuilder.title(
			__ -> "The postal address form"
		).description(
			__ -> "This form can be used to create a postal address"
		).constructor(
			PostalAddressForm::new
		).addRequiredString(
			"addressCountry", PostalAddressForm::_setAddressCountry
		).addRequiredString(
			"addressLocality", PostalAddressForm::_setAddressLocality
		).addRequiredString(
			"addressRegion", PostalAddressForm::_setAddressRegion
		).addRequiredString(
			"postalCode", PostalAddressForm::_setPostalCode
		).addRequiredString(
			"streetAddress", PostalAddressForm::_setStreetAddress
		).build();
	}

	@Reference
	private PersonActionRouter _personActionRouter;

	private static class PersonForm implements Person {

		@Override
		public Date getBirthDate() {
			return _birthDate;
		}

		@Override
		public String getEmail() {
			return _email;
		}

		@Override
		public String getFamilyName() {
			return _familyName;
		}

		@Override
		public String getGivenName() {
			return _givenName;
		}

		@Override
		public Long getId() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getImage() {
			return _image;
		}

		@Override
		public List<String> getJobTitles() {
			return _jobTitle;
		}

		@Override
		public String getName() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PostalAddress getPostalAddress() {
			return _postalAddress;
		}

		private void _setBirthDate(Date birthDate) {
			_birthDate = birthDate;
		}

		private void _setEmail(String email) {
			_email = email;
		}

		private void _setFamilyName(String familyName) {
			_familyName = familyName;
		}

		private void _setGivenName(String givenName) {
			_givenName = givenName;
		}

		private void _setImage(String image) {
			_image = image;
		}

		private void _setJobTitle(List<String> jobTitle) {
			_jobTitle = jobTitle;
		}

		private void _setPostalAddress(PostalAddress postalAddress) {
			_postalAddress = postalAddress;
		}

		private Date _birthDate;
		private String _email;
		private String _familyName;
		private String _givenName;
		private String _image;
		private List<String> _jobTitle;
		private PostalAddress _postalAddress;

	}

	private static class PostalAddressForm implements PostalAddress {

		@Override
		public String getAddressCountry() {
			return _addressCountry;
		}

		@Override
		public String getAddressLocality() {
			return _addressLocality;
		}

		@Override
		public String getAddressRegion() {
			return _addressRegion;
		}

		@Override
		public String getPostalCode() {
			return _postalCode;
		}

		@Override
		public String getStreetAddress() {
			return _streetAddress;
		}

		private void _setAddressCountry(String addressCountry) {
			_addressCountry = addressCountry;
		}

		private void _setAddressLocality(String addressLocality) {
			_addressLocality = addressLocality;
		}

		private void _setAddressRegion(String addressRegion) {
			_addressRegion = addressRegion;
		}

		private void _setPostalCode(String postalCode) {
			_postalCode = postalCode;
		}

		private void _setStreetAddress(String streetAddress) {
			_streetAddress = streetAddress;
		}

		private String _addressCountry;
		private String _addressLocality;
		private String _addressRegion;
		private String _postalCode;
		private String _streetAddress;

	}

}
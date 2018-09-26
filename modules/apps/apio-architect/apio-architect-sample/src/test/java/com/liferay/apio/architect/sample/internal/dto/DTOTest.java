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

package com.liferay.apio.architect.sample.internal.dto;

import static com.openpojo.reflection.impl.PojoClassFactory.getPojoClass;

import com.openpojo.reflection.utils.AttributeHelper;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.NoFieldShadowingRule;
import com.openpojo.validation.rule.impl.NoPrimitivesRule;
import com.openpojo.validation.rule.impl.NoPublicFieldsRule;
import com.openpojo.validation.rule.impl.NoStaticExceptFinalRule;
import com.openpojo.validation.test.impl.DefaultValuesNullTester;
import com.openpojo.validation.test.impl.GetterTester;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class DTOTest {

	@Before
	public void setUp() {
		AttributeHelper.registerFieldPrefix("_");

		_validator = ValidatorBuilder.create(
		).with(
			new NoStaticExceptFinalRule()
		).with(
			new NoFieldShadowingRule()
		).with(
			new NoPublicFieldsRule()
		).with(
			new NoPrimitivesRule()
		).with(
			new GetterTester()
		).with(
			new DefaultValuesNullTester()
		).build();
	}

	@Test
	public void testBlogPostingCommentModel() {
		_validator.validate(getPojoClass(BlogPostingCommentModel.class));
	}

	@Test
	public void testBlogPostingModel() {
		_validator.validate(getPojoClass(BlogPostingModel.class));
	}

	@Test
	public void testBlogSubscriptionModel() {
		_validator.validate(getPojoClass(BlogSubscriptionModel.class));
	}

	@Test
	public void testPersonModel() {
		_validator.validate(getPojoClass(PersonModel.class));
	}

	@Test
	public void testPostalAddressModel() {
		_validator.validate(getPojoClass(PostalAddressModel.class));
	}

	@Test
	public void testRatingModel() {
		_validator.validate(getPojoClass(RatingModel.class));
	}

	@Test
	public void testReviewModel() {
		_validator.validate(getPojoClass(ReviewModel.class));
	}

	private Validator _validator;

}
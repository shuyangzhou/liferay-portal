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

import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.representor.Representable;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.sample.internal.resource.BlogPostingCollectionResource.BlogPostingIdentifier;
import com.liferay.apio.architect.sample.internal.resource.PersonCollectionResource.PersonIdentifier;
import com.liferay.apio.architect.sample.internal.type.BlogSubscription;

import org.osgi.service.component.annotations.Component;

/**
 * @author Alejandro Hernández
 */
@Component(service = Representable.class)
public class BlogSubscriptionRepresentable
	implements Representable
		<BlogSubscription, Long,
		 BlogSubscriptionRepresentable.BlogSubscriptionIdentifier> {

	public static Form<BlogSubscriptionForm> buildForm(
		Form.Builder<BlogSubscriptionForm> formBuilder) {

		return formBuilder.title(
			__ -> "The blog subscription form"
		).description(
			__ ->
				"This form can be used to create or update a blog subscription"
		).constructor(
			BlogSubscriptionForm::new
		).addRequiredLinkedModel(
			"person", PersonIdentifier.class, BlogSubscriptionForm::_setPersonId
		).build();
	}

	@Override
	public String getName() {
		return "blog-subscription-dsl";
	}

	@Override
	public Representor<BlogSubscription> representor(
		Representor.Builder<BlogSubscription, Long> builder) {

		return builder.types(
			"BlogSubscriptionDSL"
		).identifier(
			BlogSubscription::getId
		).addLinkedModel(
			"blog", BlogPostingIdentifier.class,
			BlogSubscription::getBlogPostingId
		).addLinkedModel(
			"person", PersonIdentifier.class, BlogSubscription::getPersonId
		).build();
	}

	public static class BlogSubscriptionForm implements BlogSubscription {

		@Override
		public Long getBlogPostingId() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Long getId() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Long getPersonId() {
			return _personId;
		}

		private void _setPersonId(Long personId) {
			_personId = personId;
		}

		private Long _personId;

	}

	public interface BlogSubscriptionIdentifier extends Identifier<Long> {
	}

}
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
import com.liferay.apio.architect.custom.actions.CustomRoute;
import com.liferay.apio.architect.custom.actions.PostRoute;
import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.resource.CollectionResource;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.sample.internal.auth.PermissionChecker;
import com.liferay.apio.architect.sample.internal.resource.BlogPostingCommentNestedCollectionResource.BlogPostingCommentIdentifier;
import com.liferay.apio.architect.sample.internal.resource.BlogSubscriptionRepresentable.BlogSubscriptionIdentifier;
import com.liferay.apio.architect.sample.internal.resource.PersonCollectionResource.PersonIdentifier;
import com.liferay.apio.architect.sample.internal.router.BlogPostingActionRouter;
import com.liferay.apio.architect.sample.internal.type.BlogPosting;
import com.liferay.apio.architect.sample.internal.type.Rating;
import com.liferay.apio.architect.sample.internal.type.Review;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Hernández
 */
@Component(service = CollectionResource.class)
public class BlogPostingCollectionResource
	implements CollectionResource
		<BlogPosting, Long,
		 BlogPostingCollectionResource.BlogPostingIdentifier> {

	@Override
	public CollectionRoutes<BlogPosting, Long> collectionRoutes(
		CollectionRoutes.Builder<BlogPosting, Long> builder) {

		return builder.addGetter(
			_blogPostingActionRouter::retrievePage
		).addCreator(
			_blogPostingActionRouter::create, Credentials.class,
			PermissionChecker::hasPermission,
			BlogPostingCollectionResource::_buildBlogPostingForm
		).build();
	}

	@Override
	public String getName() {
		return "blog-postings-dsl";
	}

	@Override
	public ItemRoutes<BlogPosting, Long> itemRoutes(
		ItemRoutes.Builder<BlogPosting, Long> builder) {

		return builder.addGetter(
			_blogPostingActionRouter::retrieve
		).addCustomRoute(
			_SUBSCRIBE_CUSTOM_ROUTE, _blogPostingActionRouter::subscribe,
			BlogSubscriptionIdentifier.class,
			(credentials, id) -> hasPermission(credentials),
			BlogSubscriptionRepresentable::buildForm
		).addRemover(
			_blogPostingActionRouter::remove, Credentials.class,
			(credentials, id) -> hasPermission(credentials)
		).addUpdater(
			_blogPostingActionRouter::replace, Credentials.class,
			(credentials, id) -> hasPermission(credentials),
			BlogPostingCollectionResource::_buildBlogPostingForm
		).build();
	}

	@Override
	public Representor<BlogPosting> representor(
		Representor.Builder<BlogPosting, Long> builder) {

		return builder.types(
			"BlogPostingDSL"
		).identifier(
			BlogPosting::getId
		).addDate(
			"dateCreated", BlogPosting::getDateCreated
		).addDate(
			"dateModified", BlogPosting::getDateModified
		).addLinkedModel(
			"creator", PersonIdentifier.class, BlogPosting::getCreatorId
		).addNestedList(
			"review", BlogPosting::getReviews,
			reviewBuilder -> reviewBuilder.types(
				"ReviewDSL"
			).addString(
				"reviewBody", Review::getReviewBody
			).addNested(
				"reviewRating", Review::getRating,
				ratingBuilder -> ratingBuilder.types(
					"RatingDSL"
				).addLinkedModel(
					"author", PersonIdentifier.class, Rating::getAuthorId
				).addNumber(
					"bestRating", Rating::getBestRating
				).addNumber(
					"ratingValue", Rating::getRatingValue
				).addNumber(
					"worstRating", Rating::getWorstRating
				).build()
			).build()
		).addRelatedCollection(
			"comment", BlogPostingCommentIdentifier.class
		).addString(
			"alternativeHeadline", BlogPosting::getAlternativeHeadline
		).addString(
			"articleBody", BlogPosting::getArticleBody
		).addString(
			"fileFormat", __ -> "text/html"
		).addString(
			"headline", BlogPosting::getHeadline
		).build();
	}

	public interface BlogPostingIdentifier extends Identifier<Long> {
	}

	private static Form<BlogPostingForm> _buildBlogPostingForm(
		Form.Builder<BlogPostingForm> formBuilder) {

		return formBuilder.title(
			__ -> "The blog posting form"
		).description(
			__ -> "This form can be used to create or update a blog posting"
		).constructor(
			BlogPostingForm::new
		).addRequiredLinkedModel(
			"creator", PersonIdentifier.class, BlogPostingForm::_setCreatorId
		).addRequiredString(
			"articleBody", BlogPostingForm::_setArticleBody
		).addRequiredString(
			"alternativeHeadline", BlogPostingForm::_setAlternativeHeadline
		).addRequiredString(
			"headline", BlogPostingForm::_setHeadline
		).addRequiredNestedModelList(
			"review", BlogPostingCollectionResource::_buildReviewForm,
			BlogPostingForm::_setReviews
		).build();
	}

	private static Form<RatingForm> _buildRatingForm(
		Form.Builder<RatingForm> ratingFormBuilder) {

		return ratingFormBuilder.title(
			__ -> "The rating form"
		).description(
			__ -> "This form can be used to create a rating"
		).constructor(
			RatingForm::new
		).addRequiredLinkedModel(
			"author", PersonIdentifier.class, RatingForm::_setAuthorId
		).addRequiredLong(
			"ratingValue", RatingForm::_setRatingValue
		).build();
	}

	private static Form<ReviewForm> _buildReviewForm(
		Form.Builder<ReviewForm> reviewFormBuilder) {

		return reviewFormBuilder.title(
			__ -> "The review form"
		).description(
			__ -> "This form can be used to create a review"
		).constructor(
			ReviewForm::new
		).addRequiredString(
			"reviewBody", ReviewForm::_setReviewBody
		).addRequiredNestedModel(
			"rating", BlogPostingCollectionResource::_buildRatingForm,
			ReviewForm::_setRating
		).build();
	}

	private static final CustomRoute _SUBSCRIBE_CUSTOM_ROUTE = new PostRoute() {

		@Override
		public String getName() {
			return "subscribe";
		}

	};

	@Reference
	private BlogPostingActionRouter _blogPostingActionRouter;

	private static class BlogPostingForm implements BlogPosting {

		@Override
		public String getAlternativeHeadline() {
			return _alternativeHeadline;
		}

		@Override
		public String getArticleBody() {
			return _articleBody;
		}

		@Override
		public Long getCreatorId() {
			return _creatorId;
		}

		@Override
		public Date getDateCreated() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Date getDateModified() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getFileFormat() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getHeadline() {
			return _headline;
		}

		@Override
		public Long getId() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Review> getReviews() {
			return _reviews;
		}

		private void _setAlternativeHeadline(String alternativeHeadline) {
			_alternativeHeadline = alternativeHeadline;
		}

		private void _setArticleBody(String articleBody) {
			_articleBody = articleBody;
		}

		private void _setCreatorId(Long creator) {
			_creatorId = creator;
		}

		private void _setHeadline(String headline) {
			_headline = headline;
		}

		private void _setReviews(List<ReviewForm> reviews) {
			Stream<ReviewForm> stream = reviews.stream();

			_reviews = stream.map(
				Review.class::cast
			).collect(
				Collectors.toList()
			);
		}

		private String _alternativeHeadline;
		private String _articleBody;
		private Long _creatorId;
		private String _headline;
		private List<Review> _reviews;

	}

	private static class RatingForm implements Rating {

		@Override
		public Long getAuthorId() {
			return _authorId;
		}

		@Override
		public Long getRatingValue() {
			return _ratingValue;
		}

		private void _setAuthorId(Long authorId) {
			_authorId = authorId;
		}

		private void _setRatingValue(Long ratingValue) {
			_ratingValue = ratingValue;
		}

		private Long _authorId;
		private Long _ratingValue;

	}

	private static class ReviewForm implements Review {

		@Override
		public Rating getRating() {
			return _rating;
		}

		@Override
		public String getReviewBody() {
			return _reviewBody;
		}

		private void _setRating(Rating rating) {
			_rating = rating;
		}

		private void _setReviewBody(String reviewBody) {
			_reviewBody = reviewBody;
		}

		private Rating _rating;
		private String _reviewBody;

	}

}
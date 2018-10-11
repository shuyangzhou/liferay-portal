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

import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.router.ReusableNestedCollectionRouter;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.apio.architect.sample.internal.converter.BlogPostingConverter;
import com.liferay.apio.architect.sample.internal.dao.BlogPostingModelService;
import com.liferay.apio.architect.sample.internal.dto.BlogPostingModel;
import com.liferay.apio.architect.sample.internal.identifier.RatingIdentifier;
import com.liferay.apio.architect.sample.internal.resource.BlogPostingCollectionResource.BlogPostingIdentifier;
import com.liferay.apio.architect.sample.internal.type.BlogPosting;
import com.liferay.apio.architect.sample.internal.type.Rating;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Javier Gamarra
 */
@Component(service = ReusableNestedCollectionRouter.class)
public class BlogPostingRatingReusableNestedCollectionRouter
	implements ReusableNestedCollectionRouter
		<BlogPosting, Long, BlogPostingIdentifier, RatingIdentifier> {

	@Override
	public NestedCollectionRoutes
		<BlogPosting, Long, RatingIdentifier> collectionRoutes(
			NestedCollectionRoutes.Builder
				<BlogPosting, Long, RatingIdentifier> builder) {

		return builder.addGetter(
			(pagination, id) -> {
				Rating rating = id.getRating();

				List<BlogPostingModel> page = _blogPostingModelService.getPage(
					pagination.getStartPosition(), pagination.getEndPosition());

				Stream<BlogPostingModel> stream = page.stream();

				List<BlogPosting> blogPostings = stream.filter(
					blogPostingModel -> {
						Long creatorId = blogPostingModel.getCreatorId();

						return creatorId.equals(rating.getCreatorId());
					}
				).map(
					BlogPostingConverter::toBlogPosting
				).collect(
					Collectors.toList()
				);

				return new PageItems<>(blogPostings, blogPostings.size());
			}).build();
	}

	@Reference
	private BlogPostingModelService _blogPostingModelService;

}
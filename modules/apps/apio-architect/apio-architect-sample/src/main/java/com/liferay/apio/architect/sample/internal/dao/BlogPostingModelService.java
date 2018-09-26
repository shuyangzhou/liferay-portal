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

import static java.util.concurrent.TimeUnit.DAYS;

import com.github.javafaker.Book;
import com.github.javafaker.DateAndTime;
import com.github.javafaker.Faker;
import com.github.javafaker.Lorem;
import com.github.javafaker.service.RandomService;

import com.liferay.apio.architect.sample.internal.dto.BlogPostingModel;
import com.liferay.apio.architect.sample.internal.dto.RatingModel;
import com.liferay.apio.architect.sample.internal.dto.ReviewModel;

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
import org.osgi.service.component.annotations.Reference;

/**
 * Contains methods for creating, retrieving, updating, and deleting blog posts
 * in an in-memory database with fake data.
 *
 * @author Alejandro Hernández
 * @review
 */
@Component(immediate = true, service = BlogPostingModelService.class)
public class BlogPostingModelService {

	/**
	 * Computes the fake data for this model class.
	 */
	@Activate
	public void activate() {
		for (long index = 0; index < 42; index++) {
			Faker faker = new Faker();

			Book book = faker.book();

			Lorem lorem = faker.lorem();

			RandomService randomService = faker.random();

			long creatorId = randomService.nextInt(
				_personModelService.getCount());

			DateAndTime dateAndTime = faker.date();

			Date date = dateAndTime.past(400, DAYS);

			int reviewNumber = randomService.nextInt(5);

			List<ReviewModel> reviewModels = IntStream.range(
				0, reviewNumber
			).mapToObj(
				__ -> randomService.nextInt(_personModelService.getCount())
			).map(
				authorId -> new RatingModel(
					(long)authorId, (long)randomService.nextInt(6))
			).map(
				ratingModel -> new ReviewModel(lorem.sentence(), ratingModel)
			).collect(
				Collectors.toList()
			);

			BlogPostingModel blogPostingModel = new BlogPostingModel(
				_count.get(), lorem.paragraph(), date, creatorId, date,
				reviewModels, lorem.sentence(), book.title());

			_blogPostingModels.put(_count.getAndIncrement(), blogPostingModel);
		}
	}

	/**
	 * Adds a new blog posting to the database.
	 *
	 * @param  content the blog posting's content
	 * @param  creatorId the ID of the blog posting's creator
	 * @param  subtitle the blog posting's subtitle
	 * @param  title the blog posting's title
	 * @param  reviewModels the blog posting's reviews
	 * @return the new blog posting
	 * @review
	 */
	public BlogPostingModel create(
		String content, long creatorId, String subtitle, String title,
		List<ReviewModel> reviewModels) {

		BlogPostingModel blogPostingModel = new BlogPostingModel(
			_count.get(), content, new Date(), creatorId, new Date(),
			reviewModels, subtitle, title);

		_blogPostingModels.put(_count.getAndIncrement(), blogPostingModel);

		return blogPostingModel;
	}

	/**
	 * Returns the blog posting that matches the specified ID, if that blog
	 * posting exists; returns {@code Optional#empty()} otherwise.
	 *
	 * @param  id the blog posting's ID
	 * @return the blog posting, if present; {@code Optional#empty()} otherwise
	 */
	public Optional<BlogPostingModel> get(long id) {
		return Optional.ofNullable(_blogPostingModels.get(id));
	}

	/**
	 * Returns the total number of blog postings.
	 *
	 * @return the total number of blog postings
	 */
	public int getCount() {
		return _blogPostingModels.size();
	}

	/**
	 * Returns the page of blog postings specified by the page's start and end
	 * positions.
	 *
	 * @param  start the page's start position
	 * @param  end the page's end position
	 * @return the page of blog postings
	 */
	public List<BlogPostingModel> getPage(int start, int end) {
		Collection<BlogPostingModel> blogPostingModels =
			_blogPostingModels.values();

		Stream<BlogPostingModel> stream = blogPostingModels.stream();

		return stream.skip(
			start
		).limit(
			end
		).collect(
			Collectors.toList()
		);
	}

	/**
	 * Deletes the blog posting that matches the specified ID.
	 *
	 * @param id the blog posting's ID
	 */
	public void remove(long id) {
		_blogPostingModels.remove(id);
	}

	/**
	 * Updates the blog posting that matches the specified ID, if that blog
	 * posting exists; returns {@code Optional#empty()} otherwise.
	 *
	 * @param  id the blog posting's ID
	 * @param  content the blog posting's new content
	 * @param  creatorId the ID of the user updating the blog posting
	 * @param  subtitle the blog posting's new subtitle
	 * @param  title the blog posting's new title
	 * @param  reviewModels the blog posting's reviews
	 * @return the updated blog posting, if present; {@code Optional#empty()}
	 *         otherwise
	 * @review
	 */
	public Optional<BlogPostingModel> update(
		long id, String content, long creatorId, String subtitle, String title,
		List<ReviewModel> reviewModels) {

		BlogPostingModel blogPostingModel = _blogPostingModels.get(id);

		if (blogPostingModel == null) {
			return Optional.empty();
		}

		Date createDate = blogPostingModel.getCreateDate();

		blogPostingModel = new BlogPostingModel(
			id, content, createDate, creatorId, new Date(), reviewModels,
			subtitle, title);

		_blogPostingModels.put(id, blogPostingModel);

		return Optional.of(blogPostingModel);
	}

	private final Map<Long, BlogPostingModel> _blogPostingModels =
		new ConcurrentHashMap<>();
	private final AtomicLong _count = new AtomicLong(0);

	@Reference
	private PersonModelService _personModelService;

}
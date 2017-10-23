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

package com.liferay.vulcan.sample.internal.model;

import com.github.javafaker.Book;
import com.github.javafaker.DateAndTime;
import com.github.javafaker.Faker;
import com.github.javafaker.Lorem;
import com.github.javafaker.service.RandomService;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Instances of this class represents a blog posting. This is a mock class for
 * sample purposes. It contains methods for retrieving/updating/deleting blog
 * posts and a in-memory database with fake data.
 *
 * @author Alejandro Hernández
 * @review
 */
public class BlogPosting {

	/**
	 * Adds a new {@code BlogPosting} to the database.
	 *
	 * @param  content the content of the blog posting.
	 * @param  creatorId the ID of the creator of this blog posting.
	 * @param  subtitle the subtitle of the blog posting.
	 * @param  title the title of the blog posting.
	 * @return the added {@code BlogPosting}.
	 * @review
	 */
	public static BlogPosting addBlogPosting(
		String content, long creatorId, String subtitle, String title) {

		long blogPostingId = _count.incrementAndGet();

		BlogPosting blogPosting = new BlogPosting(
			blogPostingId, content, new Date(), creatorId, new Date(), subtitle,
			title);

		_blogPostings.put(blogPostingId, blogPosting);

		return blogPosting;
	}

	/**
	 * Deletes a {@code BlogPosting} with a certain {@code ID} from the
	 * database.
	 *
	 * @param  blogPostingId the ID of the blog posting to delete.
	 * @review
	 */
	public static void deleteBlogPosting(long blogPostingId) {
		_blogPostings.remove(blogPostingId);
	}

	/**
	 * Returns a {@code BlogPosting} with a certain {@code ID} from the database
	 * if present. Returns {@code Optional#empty()} otherwise.
	 *
	 * @param  blogPostingId the ID of the blog posting to retrieve.
	 * @return the {@code BlogPosting} for the requested ID if present; {@code
	 *         Optional#empty()} otherwise.
	 * @review
	 */
	public static Optional<BlogPosting> getBlogPosting(long blogPostingId) {
		BlogPosting blogPosting = _blogPostings.get(blogPostingId);

		return Optional.ofNullable(blogPosting);
	}

	/**
	 * Return the total number of blog postings in the database.
	 *
	 * @return the total number of blog postings in the database.
	 * @review
	 */
	public static int getBlogPostingCount() {
		return _blogPostings.size();
	}

	/**
	 * Returns a page of {@code BlogPosting} from the database.
	 *
	 * @param  start the start position.
	 * @param  end the end position.
	 * @return the list of blog postings between {@code start} and {@code end}.
	 * @review
	 */
	public static List<BlogPosting> getBlogPostings(int start, int end) {
		Collection<BlogPosting> blogPostings = _blogPostings.values();

		Stream<BlogPosting> stream = blogPostings.stream();

		return stream.skip(
			start
		).limit(
			end
		).collect(
			Collectors.toList()
		);
	}

	/**
	 * Updates a {@code BlogPosting} with a certain {@code ID} in the database,
	 * if present.
	 *
	 * @param  blogPostingId the ID of the blog posting to update.
	 * @param  content the new content for the blog posting.
	 * @param  creatorId the ID of the new creator for the blog posting.
	 * @param  subtitle the new subtitle for the blog posting.
	 * @param  title the new title for the blog posting.
	 * @return the updated {@code BlogPosting} if present; {@code
	 *         Optional#empty()} otherwise.
	 * @review
	 */
	public static Optional<BlogPosting> updateBlogPosting(
		long blogPostingId, String content, long creatorId, String subtitle,
		String title) {

		BlogPosting blogPosting = _blogPostings.get(blogPostingId);

		if (blogPosting == null) {
			return Optional.empty();
		}

		Date createDate = blogPosting.getCreateDate();

		blogPosting = new BlogPosting(
			blogPostingId, content, createDate, creatorId, new Date(), subtitle,
			title);

		_blogPostings.put(blogPostingId, blogPosting);

		return Optional.of(blogPosting);
	}

	/**
	 * Returns the ID of this {@code BlogPosting}.
	 *
	 * @return the ID of the blog posting.
	 * @review
	 */
	public long getBlogPostingId() {
		return _blogPostingId;
	}

	/**
	 * Returns the content of this {@code BlogPosting}.
	 *
	 * @return the content of the blog posting.
	 * @review
	 */
	public String getContent() {
		return _content;
	}

	/**
	 * Returns the create date of this {@code BlogPosting}.
	 *
	 * @return the create date of the blog posting.
	 * @review
	 */
	public Date getCreateDate() {
		return new Date(_createDate.getTime());
	}

	/**
	 * Returns the ID of the creator of this {@code BlogPosting}.
	 *
	 * @return the ID of the creator the blog posting.
	 * @review
	 */
	public long getCreatorId() {
		return _creatorId;
	}

	/**
	 * Returns the modified date of this {@code BlogPosting}.
	 *
	 * @return the modified date of the blog posting.
	 * @review
	 */
	public Date getModifiedDate() {
		return new Date(_modifiedDate.getTime());
	}

	/**
	 * Returns the subtitle of this {@code BlogPosting}.
	 *
	 * @return the subtitle of the blog posting.
	 * @review
	 */
	public String getSubtitle() {
		return _subtitle;
	}

	/**
	 * Returns the title of this {@code BlogPosting}.
	 *
	 * @return the title of the blog posting.
	 * @review
	 */
	public String getTitle() {
		return _title;
	}

	private BlogPosting(
		long blogPostingId, String content, Date createDate, long creatorId,
		Date modifiedDate, String subtitle, String title) {

		_blogPostingId = blogPostingId;
		_content = content;
		_createDate = createDate;
		_creatorId = creatorId;
		_modifiedDate = modifiedDate;
		_subtitle = subtitle;
		_title = title;
	}

	private static Map<Long, BlogPosting> _blogPostings =
		_blogPostings = new HashMap<>();
	private static final AtomicLong _count = new AtomicLong(30);

	static {
		for (long blogPostingId = 0; blogPostingId < 42; blogPostingId++) {
			Faker faker = new Faker();

			Book book = faker.book();

			Lorem lorem = faker.lorem();

			List<String> paragraphs = lorem.paragraphs(5);

			Stream<String> stream = paragraphs.stream();

			String content = stream.map(
				paragraph -> "<p>" + paragraph + "</p>"
			).collect(
				Collectors.joining()
			);

			RandomService randomService = faker.random();

			int creatorId = randomService.nextInt(Person.getPeopleCount());

			DateAndTime dateAndTime = faker.date();

			Date date = dateAndTime.past(400, TimeUnit.DAYS);

			BlogPosting blogPosting = new BlogPosting(
				blogPostingId, content, date, creatorId, date, lorem.sentence(),
				book.title());

			_blogPostings.put(blogPostingId, blogPosting);
		}
	}

	private final long _blogPostingId;
	private final String _content;
	private final Date _createDate;
	private final long _creatorId;
	private final Date _modifiedDate;
	private final String _subtitle;
	private final String _title;

}
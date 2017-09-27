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

package com.liferay.vulcan.sample.liferay.portal.internal.resource;

import com.liferay.blogs.kernel.exception.NoSuchEntryException;
import com.liferay.blogs.kernel.model.BlogsEntry;
import com.liferay.blogs.kernel.service.BlogsEntryService;
import com.liferay.portal.kernel.comment.Comment;
import com.liferay.portal.kernel.exception.NoSuchGroupException;
import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserService;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.vulcan.pagination.PageItems;
import com.liferay.vulcan.pagination.Pagination;
import com.liferay.vulcan.resource.CollectionResource;
import com.liferay.vulcan.resource.Representor;
import com.liferay.vulcan.resource.Routes;
import com.liferay.vulcan.resource.builder.RepresentorBuilder;
import com.liferay.vulcan.resource.builder.RoutesBuilder;
import com.liferay.vulcan.resource.identifier.LongIdentifier;
import com.liferay.vulcan.result.Try;
import com.liferay.vulcan.sample.liferay.portal.rating.AggregateRating;
import com.liferay.vulcan.sample.liferay.portal.rating.AggregateRatingService;
import com.liferay.vulcan.sample.liferay.portal.resource.identifier.AggregateRatingIdentifier;
import com.liferay.vulcan.sample.liferay.portal.resource.identifier.CommentableIdentifier;

import java.text.DateFormat;
import java.text.ParseException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServerErrorException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides all the necessary information to expose <a
 * href="http://schema.org/BlogPosting">BlogPosting</a> resources through a web
 * API.
 *
 * The resources are mapped from the internal {@link BlogsEntry} model.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@Component(immediate = true)
public class BlogPostingCollectionResource
	implements CollectionResource<BlogsEntry, LongIdentifier> {

	@Override
	public Representor<BlogsEntry, LongIdentifier> buildRepresentor(
		RepresentorBuilder<BlogsEntry, LongIdentifier> representorBuilder) {

		Function<Date, Object> formatFunction = date -> {
			if (date == null) {
				return null;
			}

			DateFormat dateFormat = DateUtil.getISO8601Format();

			return dateFormat.format(date);
		};

		return representorBuilder.identifier(
			blogsEntry -> blogsEntry::getEntryId
		).addBidirectionalModel(
			"group", "blogs", Group.class, this::_getGroupOptional,
			group -> (LongIdentifier)group::getGroupId
		).addEmbeddedModel(
			"aggregateRating", AggregateRating.class,
			this::_getAggregateRatingOptional
		).addEmbeddedModel(
			"creator", User.class, this::_getUserOptional
		).addField(
			"alternativeHeadline", BlogsEntry::getSubtitle
		).addField(
			"articleBody", BlogsEntry::getContent
		).addField(
			"createDate",
			blogsEntry -> formatFunction.apply(blogsEntry.getCreateDate())
		).addField(
			"description", BlogsEntry::getDescription
		).addField(
			"displayDate",
			blogsEntry -> formatFunction.apply(blogsEntry.getDisplayDate())
		).addField(
			"fileFormat", blogsEntry -> "text/html"
		).addField(
			"headline", BlogsEntry::getTitle
		).addField(
			"modifiedDate",
			blogsEntry -> formatFunction.apply(blogsEntry.getModifiedDate())
		).addField(
			"publishedDate",
			blogsEntry -> formatFunction.apply(blogsEntry.getLastPublishDate())
		).addLink(
			"license", "https://creativecommons.org/licenses/by/4.0"
		).addLinkedModel(
			"author", User.class, this::_getUserOptional
		).addRelatedCollection(
			"comment", Comment.class, CommentableIdentifier::create
		).addType(
			"BlogPosting"
		).build();
	}

	@Override
	public String getName() {
		return "blogs";
	}

	@Override
	public Routes<BlogsEntry> routes(
		RoutesBuilder<BlogsEntry, LongIdentifier> routesBuilder) {

		return routesBuilder.addCollectionPageGetter(
			this::_getPageItems, LongIdentifier.class
		).addCollectionPageItemCreator(
			this::_addBlogsEntry, LongIdentifier.class
		).addCollectionPageItemGetter(
			this::_getBlogsEntry
		).addCollectionPageItemRemover(
			this::_deleteBlogsEntry
		).addCollectionPageItemUpdater(
			this::_updateBlogsEntry
		).build();
	}

	private BlogsEntry _addBlogsEntry(
		LongIdentifier groupLongIdentifier, Map<String, Object> body) {

		String title = (String)body.get("headline");
		String subtitle = (String)body.get("alternativeHeadline");
		String description = (String)body.get("description");
		String content = (String)body.get("articleBody");
		String displayDateString = (String)body.get("displayDate");

		Supplier<BadRequestException> invalidBodyExceptionSupplier =
			() -> new BadRequestException("Invalid body");

		if (Validator.isNull(title) || Validator.isNull(subtitle) ||
			Validator.isNull(description) || Validator.isNull(content) ||
			Validator.isNull(displayDateString)) {

			throw invalidBodyExceptionSupplier.get();
		}

		Calendar calendar = Calendar.getInstance();

		Try<DateFormat> dateFormatTry = Try.success(
			DateUtil.getISO8601Format());

		Date displayDate = dateFormatTry.map(
			dateFormat -> dateFormat.parse(displayDateString)
		).mapFailMatching(
			ParseException.class, invalidBodyExceptionSupplier
		).getUnchecked();

		calendar.setTime(displayDate);

		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int year = calendar.get(Calendar.YEAR);
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setScopeGroupId(groupLongIdentifier.getId());

		Try<BlogsEntry> blogsEntryTry = Try.fromFallible(
			() -> _blogsService.addEntry(
				title, subtitle, description, content, month, day, year, hour,
				minute, false, false, null, null, null, null, serviceContext));

		return blogsEntryTry.getUnchecked();
	}

	private void _deleteBlogsEntry(LongIdentifier blogsEntryLongIdentifier) {
		try {
			_blogsService.deleteEntry(blogsEntryLongIdentifier.getId());
		}
		catch (PortalException pe) {
			throw new ServerErrorException(500, pe);
		}
	}

	private Optional<AggregateRating> _getAggregateRatingOptional(
		BlogsEntry blogsEntry) {

		AggregateRatingIdentifier aggregateRatingIdentifier =
			AggregateRatingIdentifier.create(blogsEntry);

		return Optional.of(
			_aggregateRatingService.getAggregateRating(
				aggregateRatingIdentifier));
	}

	private BlogsEntry _getBlogsEntry(LongIdentifier blogsEntryLongIdentifier) {
		try {
			return _blogsService.getEntry(blogsEntryLongIdentifier.getId());
		}
		catch (NoSuchEntryException | PrincipalException e) {
			throw new NotFoundException(
				"Unable to get blogs entry " + blogsEntryLongIdentifier.getId(),
				e);
		}
		catch (PortalException pe) {
			throw new ServerErrorException(500, pe);
		}
	}

	private Optional<Group> _getGroupOptional(BlogsEntry blogsEntry) {
		try {
			return Optional.of(
				_groupLocalService.getGroup(blogsEntry.getGroupId()));
		}
		catch (NoSuchGroupException nsge) {
			throw new NotFoundException(
				"Unable to get group " + blogsEntry.getGroupId(), nsge);
		}
		catch (PortalException pe) {
			throw new ServerErrorException(500, pe);
		}
	}

	private PageItems<BlogsEntry> _getPageItems(
		Pagination pagination, LongIdentifier groupLongIdentifier) {

		List<BlogsEntry> blogsEntries = _blogsService.getGroupEntries(
			groupLongIdentifier.getId(), 0, pagination.getStartPosition(),
			pagination.getEndPosition());
		int count = _blogsService.getGroupEntriesCount(
			groupLongIdentifier.getId(), 0);

		return new PageItems<>(blogsEntries, count);
	}

	private Optional<User> _getUserOptional(BlogsEntry blogsEntry) {
		try {
			return Optional.ofNullable(
				_userService.getUserById(blogsEntry.getUserId()));
		}
		catch (NoSuchUserException | PrincipalException e) {
			throw new NotFoundException(
				"Unable to get user " + blogsEntry.getUserId(), e);
		}
		catch (PortalException pe) {
			throw new ServerErrorException(500, pe);
		}
	}

	private BlogsEntry _updateBlogsEntry(
		LongIdentifier blogsEntryLongIdentifier, Map<String, Object> body) {

		String title = (String)body.get("headline");
		String subtitle = (String)body.get("alternativeHeadline");
		String description = (String)body.get("description");
		String content = (String)body.get("articleBody");
		String displayDateString = (String)body.get("displayDate");

		Supplier<BadRequestException> invalidBodyExceptionSupplier =
			() -> new BadRequestException("Invalid body");

		if (Validator.isNull(title) || Validator.isNull(subtitle) ||
			Validator.isNull(description) || Validator.isNull(content) ||
			Validator.isNull(displayDateString)) {

			throw invalidBodyExceptionSupplier.get();
		}

		Calendar calendar = Calendar.getInstance();

		Try<DateFormat> dateFormatTry = Try.success(
			DateUtil.getISO8601Format());

		Date displayDate = dateFormatTry.map(
			dateFormat -> dateFormat.parse(displayDateString)
		).mapFailMatching(
			ParseException.class, invalidBodyExceptionSupplier
		).getUnchecked();

		calendar.setTime(displayDate);

		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int year = calendar.get(Calendar.YEAR);
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		BlogsEntry blogsEntry = _getBlogsEntry(blogsEntryLongIdentifier);

		serviceContext.setScopeGroupId(blogsEntry.getGroupId());

		Try<BlogsEntry> blogsEntryTry = Try.fromFallible(
			() -> _blogsService.updateEntry(
				blogsEntryLongIdentifier.getId(), title, subtitle, description,
				content, month, day, year, hour, minute, false, false, null,
				null, null, null, serviceContext));

		return blogsEntryTry.getUnchecked();
	}

	@Reference
	private AggregateRatingService _aggregateRatingService;

	@Reference
	private BlogsEntryService _blogsService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private UserService _userService;

}
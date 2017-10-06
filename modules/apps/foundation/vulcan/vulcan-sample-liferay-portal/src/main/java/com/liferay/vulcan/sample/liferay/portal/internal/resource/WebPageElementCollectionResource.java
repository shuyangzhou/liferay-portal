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

import com.liferay.document.library.kernel.service.DLFolderService;
import com.liferay.journal.exception.NoSuchArticleException;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.portal.kernel.exception.NoSuchGroupException;
import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.GroupService;
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

import java.text.DateFormat;
import java.text.ParseException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
 * href="http://schema.org/WebPageElement">WebPageElement</a> resources through
 * a web API. <p> The resources are mapped from the internal {@link
 * JournalArticle} model.
 *
 * @author Javier Gamarra
 */
@Component(immediate = true)
public class WebPageElementCollectionResource
	implements CollectionResource<JournalArticle, LongIdentifier> {

	@Override
	public Representor<JournalArticle, LongIdentifier> buildRepresentor(
		RepresentorBuilder<JournalArticle, LongIdentifier> representorBuilder) {

		Function<Date, Object> formatFunction = date -> {
			if (date == null) {
				return null;
			}

			DateFormat dateFormat = DateUtil.getISO8601Format();

			return dateFormat.format(date);
		};

		return representorBuilder.identifier(
			journalArticle -> journalArticle::getId
		).addBidirectionalModel(
			"group", "blogs", Group.class, this::_getGroupOptional,
			group -> (LongIdentifier)group::getGroupId
		).addEmbeddedModel(
			"creator", User.class, this::_getUserOptional
		).addField(
			"dateCreated",
			journalArticle -> formatFunction.apply(
				journalArticle.getCreateDate())
		).addField(
			"dateModified",
			journalArticle -> formatFunction.apply(
				journalArticle.getModifiedDate())
		).addField(
			"datePublished",
			journalArticle -> formatFunction.apply(
				journalArticle.getLastPublishDate())
		).addField(
			"description", JournalArticle::getDescription
		).addField(
			"lastReviewed",
			journalArticle -> formatFunction.apply(
				journalArticle.getReviewDate())
		).addField(
			"text", JournalArticle::getContent
		).addField(
			"title", JournalArticle::getTitle
		).addLinkedModel(
			"author", User.class, this::_getUserOptional
		).addType(
			"WebPageElement"
		).build();
	}

	@Override
	public String getName() {
		return "web-page-elements";
	}

	@Override
	public Routes<JournalArticle> routes(
		RoutesBuilder<JournalArticle, LongIdentifier> routesBuilder) {

		return routesBuilder.addCollectionPageGetter(
			this::_getPageItems, LongIdentifier.class
		).addCollectionPageItemCreator(
			this::_addJournalArticle, LongIdentifier.class
		).addCollectionPageItemGetter(
			this::_getJournalArticle
		).addCollectionPageItemRemover(
			this::_deleteJournalArticle
		).addCollectionPageItemUpdater(
			this::_updateJournalArticle
		).build();
	}

	private JournalArticle _addJournalArticle(
		LongIdentifier groupIdLongIdentifier, Map<String, Object> body) {

		String content = (String)body.get("text");
		String ddmStructureKey = (String)body.get("structure");
		String ddmTemplateKey = (String)body.get("template");
		String description = (String)body.get("description");
		String displayDateString = (String)body.get("dateDisplayed");
		String folderString = (String)body.get("folder");
		String title = (String)body.get("title");

		Supplier<BadRequestException> incorrectBodyExceptionSupplier =
			() -> new BadRequestException("Invalid body");

		if (Validator.isNull(content) || Validator.isNull(ddmStructureKey) ||
			Validator.isNull(ddmTemplateKey) || Validator.isNull(description) ||
			Validator.isNull(displayDateString) || Validator.isNull(title)) {

			throw incorrectBodyExceptionSupplier.get();
		}

		Calendar calendar = Calendar.getInstance();

		Try<DateFormat> dateFormatTry = Try.success(
			DateUtil.getISO8601Format());

		Date displayDate = dateFormatTry.map(
			dateFormat -> dateFormat.parse(displayDateString)
		).mapFailMatching(
			ParseException.class, incorrectBodyExceptionSupplier
		).getUnchecked();

		calendar.setTime(displayDate);

		int displayDateMonth = calendar.get(Calendar.MONTH);
		int displayDateDay = calendar.get(Calendar.DATE);
		int displayDateYear = calendar.get(Calendar.YEAR);
		int displayDateHour = calendar.get(Calendar.HOUR);
		int displayDateMinute = calendar.get(Calendar.MINUTE);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		long groupId = groupIdLongIdentifier.getId();

		serviceContext.setScopeGroupId(groupId);

		Try<Long> longTry = Try.fromFallible(() -> Long.valueOf(folderString));

		long folderId = longTry.orElse(0L);

		Map<Locale, String> titleMap = new HashMap<>();

		titleMap.put(Locale.getDefault(), title);

		Map<Locale, String> descriptionMap = new HashMap<>();

		descriptionMap.put(Locale.getDefault(), description);

		Try<JournalArticle> journalArticleTry = Try.fromFallible(() ->
			_journalArticleService.addArticle(
				groupId, folderId, 0, 0, null, true, titleMap, descriptionMap,
				content, ddmStructureKey, ddmTemplateKey, null,
				displayDateMonth, displayDateDay, displayDateYear,
				displayDateHour, displayDateMinute, 0, 0, 0, 0, 0, true, 0, 0,
				0, 0, 0, true, true, null, serviceContext));

		return journalArticleTry.getUnchecked();
	}

	private void _deleteJournalArticle(
		LongIdentifier journalArticleLongIdentifier) {

		try {
			JournalArticle article = _journalArticleService.getArticle(
				journalArticleLongIdentifier.getId());

			_journalArticleService.deleteArticle(
				article.getGroupId(), article.getArticleId(),
				article.getArticleResourceUuid(), new ServiceContext());
		}
		catch (NoSuchArticleException nsae) {
		}
		catch (PortalException pe) {
			throw new ServerErrorException(500, pe);
		}
	}

	private Optional<Group> _getGroupOptional(JournalArticle journalArticle) {
		try {
			return Optional.of(
				_groupService.getGroup(journalArticle.getGroupId()));
		}
		catch (NoSuchGroupException nsge) {
			throw new NotFoundException(
				"Unable to get group " + journalArticle.getGroupId(), nsge);
		}
		catch (PortalException pe) {
			throw new ServerErrorException(500, pe);
		}
	}

	private JournalArticle _getJournalArticle(
		LongIdentifier journalArticleLongIdentifier) {

		try {
			return _journalArticleService.getArticle(
				journalArticleLongIdentifier.getId());
		}
		catch (NoSuchArticleException nsae) {
			throw new NotFoundException(
				"Unable to get article " + journalArticleLongIdentifier.getId(),
				nsae);
		}
		catch (PortalException pe) {
			throw new ServerErrorException(500, pe);
		}
	}

	private PageItems<JournalArticle> _getPageItems(
		Pagination pagination, LongIdentifier groupIdLongIdentifier) {

		long groupId = groupIdLongIdentifier.getId();

		List<JournalArticle> journalArticles =
			_journalArticleService.getArticles(
				groupId, 0, pagination.getStartPosition(),
				pagination.getEndPosition(), null);
		int count = _journalArticleService.getArticlesCount(groupId, 0);

		return new PageItems<>(journalArticles, count);
	}

	private Optional<User> _getUserOptional(JournalArticle journalArticle) {
		try {
			return Optional.ofNullable(
				_userService.getUserById(journalArticle.getUserId()));
		}
		catch (NoSuchUserException | PrincipalException e) {
			throw new NotFoundException(
				"Unable to get user " + journalArticle.getUserId(), e);
		}
		catch (PortalException pe) {
			throw new ServerErrorException(500, pe);
		}
	}

	private JournalArticle _updateJournalArticle(
		LongIdentifier journalArticleLongIdentifier, Map<String, Object> body) {

		String content = (String)body.get("text");
		String title = (String)body.get("title");
		String description = (String)body.get("description");
		String folderString = (String)body.get("folder");
		Double group = (Double)body.get("group");
		Double userId = (Double)body.get("user");
		Double version = (Double)body.get("version");

		Supplier<BadRequestException> incorrectBodyExceptionSupplier =
			() -> new BadRequestException("Invalid body");

		if (Validator.isNull(content) || Validator.isNull(title) ||
			Validator.isNull(description) || Validator.isNull(group) ||
			Validator.isNull(userId) || Validator.isNull(version)) {

			throw incorrectBodyExceptionSupplier.get();
		}

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		Try<Long> groupIdLongTry = Try.fromFallible(
			() -> Long.valueOf(folderString));

		long groupId = groupIdLongTry.orElseThrow(
			incorrectBodyExceptionSupplier);

		serviceContext.setScopeGroupId(groupId);

		Map<Locale, String> titleMap = new HashMap<>();

		titleMap.put(Locale.getDefault(), title);

		Map<Locale, String> descriptionMap = new HashMap<>();

		descriptionMap.put(Locale.getDefault(), description);

		Try<Long> folderIdLongTry = Try.fromFallible(
			() -> Long.valueOf(folderString));

		long folderId = folderIdLongTry.orElse(0L);

		long userIdLong = userId.longValue();

		long id = journalArticleLongIdentifier.getId();

		Try<JournalArticle> journalArticleTry = Try.fromFallible(() ->
			_journalArticleService.updateArticle(
				userIdLong, groupId, folderId, String.valueOf(id), version,
				titleMap, descriptionMap, content, null, serviceContext));

		return journalArticleTry.getUnchecked();
	}

	@Reference
	private DLFolderService _dlFolderService;

	@Reference
	private GroupService _groupService;

	@Reference
	private JournalArticleService _journalArticleService;

	@Reference
	private UserService _userService;

}
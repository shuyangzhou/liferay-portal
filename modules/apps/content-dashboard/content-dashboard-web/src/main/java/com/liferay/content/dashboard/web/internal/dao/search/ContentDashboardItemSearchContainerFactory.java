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

package com.liferay.content.dashboard.web.internal.dao.search;

import com.liferay.content.dashboard.web.internal.item.ContentDashboardItem;
import com.liferay.content.dashboard.web.internal.item.ContentDashboardItemFactory;
import com.liferay.content.dashboard.web.internal.item.ContentDashboardItemFactoryTracker;
import com.liferay.content.dashboard.web.internal.search.ContentDashboardSearcher;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchResult;
import com.liferay.portal.kernel.search.SearchResultUtil;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.TermsFilter;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Cristina González
 */
public class ContentDashboardItemSearchContainerFactory {

	public static ContentDashboardItemSearchContainerFactory getInstance(
		ContentDashboardItemFactoryTracker contentDashboardItemFactoryTracker,
		Portal portal, RenderRequest renderRequest,
		RenderResponse renderResponse) {

		return new ContentDashboardItemSearchContainerFactory(
			contentDashboardItemFactoryTracker, portal, renderRequest,
			renderResponse);
	}

	public SearchContainer<ContentDashboardItem<?>> create()
		throws PortletException {

		SearchContainer<ContentDashboardItem<?>> searchContainer =
			new SearchContainer<>(
				_renderRequest, _getPortletURL(), null, "there-is-no-content");

		searchContainer.setOrderByCol(_getOrderByCol());

		searchContainer.setOrderByType(_getOrderByType());

		Hits hits = _getHits(
			_getOrderByCol(), _getOrderByType(),
			_portal.getLocale(_renderRequest), searchContainer.getEnd(),
			searchContainer.getStart());

		searchContainer.setResults(
			_getContentDashboardItems(hits, _portal.getLocale(_renderRequest)));
		searchContainer.setTotal(hits.getLength());

		return searchContainer;
	}

	private ContentDashboardItemSearchContainerFactory(
		ContentDashboardItemFactoryTracker contentDashboardItemFactoryTracker,
		Portal portal, RenderRequest renderRequest,
		RenderResponse renderResponse) {

		_contentDashboardItemFactoryTracker =
			contentDashboardItemFactoryTracker;
		_portal = portal;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
	}

	private long[] _getAuthorIds() {
		return ParamUtil.getLongValues(_renderRequest, "authorIds");
	}

	private BooleanClause[] _getBooleanClauses(long[] authorIds) {
		if (ArrayUtil.isEmpty(authorIds)) {
			return new BooleanClause[0];
		}

		BooleanQueryImpl booleanQueryImpl = new BooleanQueryImpl();

		BooleanFilter booleanFilter = new BooleanFilter();

		TermsFilter termsFilter = new TermsFilter(Field.USER_ID);

		for (long authorId : authorIds) {
			termsFilter.addValue(String.valueOf(authorId));
		}

		booleanFilter.add(termsFilter, BooleanClauseOccur.MUST);

		booleanQueryImpl.setPreBooleanFilter(booleanFilter);

		return new BooleanClause[] {
			BooleanClauseFactoryUtil.create(
				booleanQueryImpl, BooleanClauseOccur.MUST.getName())
		};
	}

	private List<ContentDashboardItem<?>> _getContentDashboardItems(
		Hits hits, Locale locale) {

		List<SearchResult> searchResults = SearchResultUtil.getSearchResults(
			hits, locale);

		Stream<SearchResult> stream = searchResults.stream();

		return stream.map(
			this::_toContentDashboardItemOptional
		).filter(
			optional -> optional.isPresent()
		).map(
			Optional::get
		).collect(
			Collectors.toList()
		);
	}

	private Hits _getHits(
			String orderByCol, String orderByType, Locale locale, int end,
			int start)
		throws PortletException {

		Indexer<?> indexer = ContentDashboardSearcher.getInstance(
			_contentDashboardItemFactoryTracker.getClassNames());

		SearchContext searchContext = SearchContextFactory.getInstance(
			_portal.getHttpServletRequest(_renderRequest));

		Integer status = _getStatus();

		if (status == WorkflowConstants.STATUS_APPROVED) {
			searchContext.setAttribute("head", Boolean.TRUE);
		}
		else {
			searchContext.setAttribute("latest", Boolean.TRUE);
		}

		searchContext.setAttribute("status", status);
		searchContext.setBooleanClauses(_getBooleanClauses(_getAuthorIds()));
		searchContext.setEnd(end);
		searchContext.setGroupIds(null);
		searchContext.setKeywords(_getKeywords());
		searchContext.setSorts(_getSort(orderByCol, orderByType, locale));
		searchContext.setStart(start);

		try {
			return indexer.search(searchContext);
		}
		catch (PortalException portalException) {
			throw new PortletException(portalException);
		}
	}

	private String _getKeywords() {
		return ParamUtil.getString(_renderRequest, "keywords");
	}

	private String _getOrderByCol() {
		return ParamUtil.getString(
			_renderRequest, "orderByCol", "modified-date");
	}

	private String _getOrderByType() {
		return ParamUtil.getString(_renderRequest, "orderByType", "desc");
	}

	private PortletURL _getPortletURL() throws PortletException {
		PortletURL portletURL = PortletURLUtil.clone(
			_renderResponse.createRenderURL(), _renderResponse);

		String delta = ParamUtil.getString(_renderRequest, "delta");

		if (Validator.isNotNull(delta)) {
			portletURL.setParameter("delta", delta);
		}

		String deltaEntry = ParamUtil.getString(_renderRequest, "deltaEntry");

		if (Validator.isNotNull(deltaEntry)) {
			portletURL.setParameter("deltaEntry", deltaEntry);
		}

		String keywords = _getKeywords();

		if (Validator.isNotNull(keywords)) {
			portletURL.setParameter("keywords", keywords);
		}

		String orderByCol = _getOrderByCol();

		if (Validator.isNotNull(orderByCol)) {
			portletURL.setParameter("orderByCol", orderByCol);
		}

		String orderByType = _getOrderByType();

		if (Validator.isNotNull(orderByType)) {
			portletURL.setParameter("orderByType", orderByType);
		}

		portletURL.setParameter("status", String.valueOf(_getStatus()));

		return portletURL;
	}

	private Sort _getSort(
		String orderByCol, String orderByType, Locale locale) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		if (orderByCol.equals("title")) {
			String sortFieldName = Field.getSortableFieldName(
				"localized_title_".concat(LocaleUtil.toLanguageId(locale)));

			return new Sort(sortFieldName, Sort.STRING_TYPE, !orderByAsc);
		}

		return new Sort(Field.MODIFIED_DATE, Sort.LONG_TYPE, !orderByAsc);
	}

	private int _getStatus() {
		return GetterUtil.getInteger(
			ParamUtil.getInteger(
				_renderRequest, "status", WorkflowConstants.STATUS_ANY));
	}

	private Optional<ContentDashboardItem<?>> _toContentDashboardItemOptional(
		ContentDashboardItemFactory<?> contentDashboardItemFactory,
		SearchResult searchResult) {

		try {
			return Optional.of(
				contentDashboardItemFactory.create(searchResult.getClassPK()));
		}
		catch (PortalException portalException) {
			_log.error(portalException, portalException);

			return Optional.empty();
		}
	}

	private Optional<ContentDashboardItem<?>> _toContentDashboardItemOptional(
		SearchResult searchResult) {

		Optional<ContentDashboardItemFactory<?>>
			contentDashboardItemFactoryOptional =
				_contentDashboardItemFactoryTracker.
					getContentDashboardItemFactoryOptional(
						searchResult.getClassName());

		return contentDashboardItemFactoryOptional.map(
			contentDashboardItemFactory -> _toContentDashboardItemOptional(
				contentDashboardItemFactory, searchResult)
		).orElse(
			Optional.empty()
		);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ContentDashboardItemSearchContainerFactory.class);

	private final ContentDashboardItemFactoryTracker
		_contentDashboardItemFactoryTracker;
	private final Portal _portal;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;

}
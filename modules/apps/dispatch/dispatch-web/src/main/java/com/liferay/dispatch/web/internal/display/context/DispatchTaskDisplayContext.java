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

package com.liferay.dispatch.web.internal.display.context;

import com.liferay.dispatch.model.DispatchTask;
import com.liferay.dispatch.service.DispatchTaskLocalService;
import com.liferay.dispatch.web.internal.display.context.util.DispatchRequestHelper;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import java.text.Format;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;

/**
 * @author guywandji
 * @author Alessio Antonio Rendina
 */
public class DispatchTaskDisplayContext {

	public DispatchTaskDisplayContext(
		DispatchTaskLocalService dispatchTaskLocalService,
		RenderRequest renderRequest) {

		_dispatchTaskLocalService = dispatchTaskLocalService;

		_dispatchRequestHelper = new DispatchRequestHelper(renderRequest);

		_dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(
			_dispatchRequestHelper.getLocale());
	}

	public DispatchTask getDispatchTask() {
		return _dispatchRequestHelper.getDispatchTask();
	}

	public List<String> getDispatchTaskTypes() {
		return Arrays.asList("111", "222", "333");
	}

	public String getNextFireDate(long dispatchTaskId) {
		Date nextRunDate = _dispatchTaskLocalService.getNextFireDate(
			dispatchTaskId);

		if (nextRunDate != null) {
			return _dateFormatDateTime.format(nextRunDate);
		}

		return StringPool.BLANK;
	}

	public String getOrderByCol() {
		return ParamUtil.getString(
			_dispatchRequestHelper.getRequest(),
			SearchContainer.DEFAULT_ORDER_BY_COL_PARAM, "modified-date");
	}

	public String getOrderByType() {
		return ParamUtil.getString(
			_dispatchRequestHelper.getRequest(),
			SearchContainer.DEFAULT_ORDER_BY_TYPE_PARAM, "desc");
	}

	public PortletURL getPortletURL() throws PortalException {
		LiferayPortletResponse liferayPortletResponse =
			_dispatchRequestHelper.getLiferayPortletResponse();

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		String delta = ParamUtil.getString(
			_dispatchRequestHelper.getRequest(), "delta");

		if (Validator.isNotNull(delta)) {
			portletURL.setParameter("delta", delta);
		}

		String deltaEntry = ParamUtil.getString(
			_dispatchRequestHelper.getRequest(), "deltaEntry");

		if (Validator.isNotNull(deltaEntry)) {
			portletURL.setParameter("deltaEntry", deltaEntry);
		}

		return portletURL;
	}

	public RowChecker getRowChecker() {
		if (_rowChecker == null) {
			_rowChecker = new EmptyOnClickRowChecker(
				_dispatchRequestHelper.getLiferayPortletResponse());
		}

		return _rowChecker;
	}

	public SearchContainer<DispatchTask> getSearchContainer()
		throws PortalException {

		if (_searchContainer != null) {
			return _searchContainer;
		}

		_searchContainer = new SearchContainer<>(
			_dispatchRequestHelper.getLiferayPortletRequest(), getPortletURL(),
			null, null);

		_searchContainer.setEmptyResultsMessage("no-items-were-found");

		_searchContainer.setOrderByCol(getOrderByCol());
		_searchContainer.setOrderByComparator(null);
		_searchContainer.setOrderByType(getOrderByType());
		_searchContainer.setRowChecker(getRowChecker());

		int total = _dispatchTaskLocalService.getDispatchTasksCount(
			_dispatchRequestHelper.getCompanyId());

		_searchContainer.setTotal(total);

		List<DispatchTask> results = _dispatchTaskLocalService.getDispatchTasks(
			_dispatchRequestHelper.getCompanyId(), _searchContainer.getStart(),
			_searchContainer.getEnd());

		_searchContainer.setResults(results);

		return _searchContainer;
	}

	private final Format _dateFormatDateTime;
	private final DispatchRequestHelper _dispatchRequestHelper;
	private final DispatchTaskLocalService _dispatchTaskLocalService;
	private RowChecker _rowChecker;
	private SearchContainer<DispatchTask> _searchContainer;

}
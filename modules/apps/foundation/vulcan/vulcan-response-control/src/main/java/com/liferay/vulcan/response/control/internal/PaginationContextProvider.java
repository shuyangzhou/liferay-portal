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

package com.liferay.vulcan.response.control.internal;

import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.vulcan.pagination.Page;
import com.liferay.vulcan.pagination.Pagination;

import java.util.Collection;
import java.util.Map;

import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.ext.ContextProvider;
import org.apache.cxf.message.Message;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@Component(immediate = true, property = "liferay.vulcan.context.provider=true")
@Provider
public class PaginationContextProvider implements ContextProvider<Pagination> {

	@Override
	public Pagination createContext(Message message) {
		String queryString = (String)message.getContextualProperty(
			Message.QUERY_STRING);

		Map<String, String[]> parameterMap = _http.getParameterMap(queryString);

		int itemsPerPage = MapUtil.getInteger(
			parameterMap, "per_page", _ITEMS_PER_PAGE_DEFAULT);
		int pageNumber = MapUtil.getInteger(
			parameterMap, "page", _PAGE_NUMBER_DEFAULT);

		return new DefaultPagination(itemsPerPage, pageNumber);
	}

	private static final int _ITEMS_PER_PAGE_DEFAULT = 30;

	private static final int _PAGE_NUMBER_DEFAULT = 1;

	@Reference
	private Http _http;

	private static class DefaultPage<T> implements Page<T> {

		public DefaultPage(
			Collection<T> items, int itemsPerPage, int pageNumber,
			int totalCount) {

			_items = items;
			_itemsPerPage = itemsPerPage;
			_pageNumber = pageNumber;
			_totalCount = totalCount;
		}

		@Override
		public Collection<T> getItems() {
			return _items;
		}

		@Override
		public int getItemsPerPage() {
			return _itemsPerPage;
		}

		@Override
		public int getLastPageNumber() {
			return -Math.floorDiv(-_totalCount, _itemsPerPage);
		}

		@Override
		public int getPageNumber() {
			return _pageNumber;
		}

		@Override
		public int getTotalCount() {
			return _totalCount;
		}

		@Override
		public boolean hasNext() {
			if (getLastPageNumber() > _pageNumber) {
				return true;
			}

			return false;
		}

		@Override
		public boolean hasPrevious() {
			if (_pageNumber > 1) {
				return true;
			}

			return false;
		}

		private final Collection<T> _items;
		private final int _itemsPerPage;
		private final int _pageNumber;
		private final int _totalCount;

	}

	private static class DefaultPagination implements Pagination {

		public DefaultPagination(int itemsPerPage, int pageNumber) {
			_itemsPerPage = itemsPerPage;
			_pageNumber = pageNumber;
		}

		@Override
		public <T> Page<T> createPage(Collection<T> items, int totalCount) {
			return new DefaultPage<>(
				items, getItemsPerPage(), getPageNumber(), totalCount);
		}

		@Override
		public int getEndPosition() {
			return _pageNumber * _itemsPerPage;
		}

		@Override
		public int getItemsPerPage() {
			return _itemsPerPage;
		}

		@Override
		public int getPageNumber() {
			return _pageNumber;
		}

		@Override
		public int getStartPosition() {
			return (_pageNumber - 1) * _itemsPerPage;
		}

		private final int _itemsPerPage;
		private final int _pageNumber;

	}

}
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

package com.liferay.portal.kernel.search;

import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.util.ServiceRetriever;

import java.util.Collection;
import java.util.Set;

/**
 * @author Michael C. Han
 */
public class SearchEngineHelperUtil {

	public static void flushQueuedSearchEngine() {
		_getSearchEngineHelper().flushQueuedSearchEngine();
	}

	public static void flushQueuedSearchEngine(String searchEngineId) {
		_getSearchEngineHelper().flushQueuedSearchEngine(searchEngineId);
	}

	public static String getDefaultSearchEngineId() {
		return _getSearchEngineHelper().getDefaultSearchEngineId();
	}

	public static String[] getEntryClassNames() {
		return _getSearchEngineHelper().getEntryClassNames();
	}

	public static SearchEngine getSearchEngine(String searchEngineId) {
		return _getSearchEngineHelper().getSearchEngine(searchEngineId);
	}

	public static SearchEngineHelper getSearchEngineHelper() {
		return _getSearchEngineHelper();
	}

	public static String getSearchEngineId(Collection<Document> documents) {
		return _getSearchEngineHelper().getSearchEngineId(documents);
	}

	public static String getSearchEngineId(Document document) {
		return _getSearchEngineHelper().getSearchEngineId(document);
	}

	public static Set<String> getSearchEngineIds() {
		return _getSearchEngineHelper().getSearchEngineIds();
	}

	public static SearchEngine getSearchEngineSilent(String searchEngineId) {
		return _getSearchEngineHelper().getSearchEngineSilent(searchEngineId);
	}

	public static SearchPermissionChecker getSearchPermissionChecker() {
		return _searchPermissionCheckerServiceRetriever.getService();
	}

	public static String getSearchReaderDestinationName(String searchEngineId) {
		return _getSearchEngineHelper().getSearchReaderDestinationName(
			searchEngineId);
	}

	public static String getSearchWriterDestinationName(String searchEngineId) {
		return _getSearchEngineHelper().getSearchWriterDestinationName(
			searchEngineId);
	}

	public static void initialize(long companyId) {
		_getSearchEngineHelper().initialize(companyId);
	}

	public static void removeCompany(long companyId) {
		_getSearchEngineHelper().removeCompany(companyId);
	}

	public static SearchEngine removeSearchEngine(String searchEngineId) {
		return _getSearchEngineHelper().removeSearchEngine(searchEngineId);
	}

	public static void setDefaultSearchEngineId(String defaultSearchEngineId) {
		_getSearchEngineHelper().setDefaultSearchEngineId(
			defaultSearchEngineId);
	}

	public static void setQueueCapacity(int queueCapacity) {
		_getSearchEngineHelper().setQueueCapacity(queueCapacity);
	}

	public static void setSearchEngine(
		String searchEngineId, SearchEngine searchEngine) {

		_getSearchEngineHelper().setSearchEngine(searchEngineId, searchEngine);

		searchEngine.initialize(CompanyConstants.SYSTEM);
	}

	private static SearchEngineHelper _getSearchEngineHelper() {
		return _searchEngineHelperServiceRetriever.getService();
	}

	private static final ServiceRetriever<SearchEngineHelper>
		_searchEngineHelperServiceRetriever = new ServiceRetriever<>(
			SearchEngineHelper.class);
	private static final ServiceRetriever<SearchPermissionChecker>
		_searchPermissionCheckerServiceRetriever = new ServiceRetriever<>(
			SearchPermissionChecker.class);

}
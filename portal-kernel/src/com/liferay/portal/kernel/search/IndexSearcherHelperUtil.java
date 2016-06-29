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

import com.liferay.portal.kernel.util.ServiceRetriever;

import java.util.List;
import java.util.Map;

/**
 * @author Michael C. Han
 */
public class IndexSearcherHelperUtil {

	public static String getQueryString(
		SearchContext searchContext, Query query) {

		return _getIndexSearcherHelper().getQueryString(searchContext, query);
	}

	public static Hits search(SearchContext searchContext, Query query)
		throws SearchException {

		return _getIndexSearcherHelper().search(searchContext, query);
	}

	public static long searchCount(SearchContext searchContext, Query query)
		throws SearchException {

		return _getIndexSearcherHelper().searchCount(searchContext, query);
	}

	public static String spellCheckKeywords(SearchContext searchContext)
		throws SearchException {

		return _getIndexSearcherHelper().spellCheckKeywords(searchContext);
	}

	public static Map<String, List<String>> spellCheckKeywords(
			SearchContext searchContext, int max)
		throws SearchException {

		return _getIndexSearcherHelper().spellCheckKeywords(searchContext, max);
	}

	public static String[] suggestKeywordQueries(
			SearchContext searchContext, int max)
		throws SearchException {

		return _getIndexSearcherHelper().suggestKeywordQueries(
			searchContext, max);
	}

	private static IndexSearcherHelper _getIndexSearcherHelper() {
		return _serviceRetriever.getService();
	}

	private static final ServiceRetriever<IndexSearcherHelper>
		_serviceRetriever = new ServiceRetriever<>(IndexSearcherHelper.class);

}
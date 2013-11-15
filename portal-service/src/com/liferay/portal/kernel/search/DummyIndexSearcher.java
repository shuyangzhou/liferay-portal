/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

import java.util.List;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public class DummyIndexSearcher implements IndexSearcher {

	@Override
	public Hits search(SearchContext searchContext, Query query) {
		return null;
	}

	@Override
	public Hits search(
		String searchEngineId, long companyId, Query query, Sort[] sort,
		int start, int end) {

		return null;
	}

	@Override
	public String spellCheckKeywords(SearchContext searchContext) {
		return null;
	}

	@Override
	public Map<String, List<String>> spellCheckKeywords(
		SearchContext searchContext, int max) {

		return null;
	}

	@Override
	public String[] suggestKeywordQueries(
		SearchContext searchContext, int max) {

		return null;
	}

}
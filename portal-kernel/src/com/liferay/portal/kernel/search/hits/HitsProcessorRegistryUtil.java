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

package com.liferay.portal.kernel.search.hits;

import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.ServiceRetriever;

/**
 * @author Michael C. Han
 */
public class HitsProcessorRegistryUtil {

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #_getHitsProcessorRegistry()}
	 */
	@Deprecated
	public static HitsProcessorRegistry getHitsProcessorRegistry() {
		return _getHitsProcessorRegistry();
	}

	public static boolean process(SearchContext searchContext, Hits hits)
		throws SearchException {

		return _getHitsProcessorRegistry().process(searchContext, hits);
	}

	private static HitsProcessorRegistry _getHitsProcessorRegistry() {
		return _serviceRetriever.getService();
	}

	private static final ServiceRetriever<HitsProcessorRegistry>
		_serviceRetriever = new ServiceRetriever<>(HitsProcessorRegistry.class);

}
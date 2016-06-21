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

package com.liferay.portal.kernel.search.query;

import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.util.ServiceRetriever;

/**
 * @author Michael C. Han
 */
public class FieldQueryFactoryUtil {

	public static Query createQuery(
		String field, String value, boolean like, boolean splitKeywords) {

		return _getFieldQueryFactory().createQuery(
			field, value, like, splitKeywords);
	}

	private static FieldQueryFactory _getFieldQueryFactory() {
		return _serviceRetriever.getService();
	}

	private static final ServiceRetriever<FieldQueryFactory> _serviceRetriever =
		new ServiceRetriever<>(FieldQueryFactory.class);

}
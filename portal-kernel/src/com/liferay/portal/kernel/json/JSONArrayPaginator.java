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

package com.liferay.portal.kernel.json;

/**
 * @author Brian Wing Shun Chan
 */
public abstract class JSONArrayPaginator {

	public JSONArrayPaginator() throws Exception {
		this(_DELTA);
	}

	public JSONArrayPaginator(int delta) throws Exception {
		if (delta <= 0) {
			delta = _DELTA;
		}

		int start = 0;
		int end = delta;

		while (true) {
			JSONArray jsonArray = paginate(start, end);

			if (jsonArray.length() == 0) {
				break;
			}

			start = end;

			end += delta;
		}
	}

	protected abstract JSONArray paginate(int start, int end) throws Exception;

	private static final int _DELTA = 500;

}
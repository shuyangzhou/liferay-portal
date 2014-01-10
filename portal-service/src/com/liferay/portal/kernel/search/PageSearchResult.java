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

/**
 * @author Tina Tian
 */
public class PageSearchResult<T> {

	public PageSearchResult(List<T> entries, int length) {
		_entries = entries;
		_length = length;
	}

	public List<T> getEntries() {
		return _entries;
	}

	public int getLength() {
		return _length;
	}

	private List<T> _entries;
	private int _length;

}
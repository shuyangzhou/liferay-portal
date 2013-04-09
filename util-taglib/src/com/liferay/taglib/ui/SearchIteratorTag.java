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

package com.liferay.taglib.ui;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author Brian Wing Shun Chan
 */
public class SearchIteratorTag<R> extends SearchPaginatorTag<R> {

	public void setPaginate(boolean paginate) {
		_paginate = paginate;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_paginate = true;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void include(String page) throws Exception {
		StopWatch stopWatch = null;

		if (_log.isDebugEnabled()) {
			stopWatch = new StopWatch();
			stopWatch.start();
		}

		try {
			super.include(page);
		}
		finally {
			if (stopWatch != null) {
				stopWatch.stop();

				_log.debug("Elapsed time: " + stopWatch.getTime());
			}
		}
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		super.setAttributes(request);

		request.setAttribute(
			"liferay-ui:search-iterator:paginate", String.valueOf(_paginate));
	}

	private static final String _PAGE =
		"/html/taglib/ui/search_iterator/page.jsp";

	private static Log _log = LogFactoryUtil.getLog(SearchIteratorTag.class);

	private boolean _paginate = true;

}
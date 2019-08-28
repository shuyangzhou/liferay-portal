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

package com.liferay.portal.servlet.filters.request;

import com.liferay.portal.kernel.servlet.TryFinallyFilter;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.liferay.taglib.util.ClearRequestUtil;
import com.liferay.taglib.util.ParamAndPropertyAncestorTagImpl;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Matthew Tambara
 */
public class ClearRequestFilter
	extends BasePortalFilter implements TryFinallyFilter {

	@Override
	public void doFilterFinally(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, Object object) {

		Set<ParamAndPropertyAncestorTagImpl> paramAndPropertyAncestorTagImpls =
			ClearRequestUtil.getTags();

		Iterator<ParamAndPropertyAncestorTagImpl> iterator =
			paramAndPropertyAncestorTagImpls.iterator();

		while (iterator.hasNext()) {
			ParamAndPropertyAncestorTagImpl paramAndPropertyAncestorTagImpl =
				iterator.next();

			paramAndPropertyAncestorTagImpl.clearRequest();

			iterator.remove();
		}
	}

	@Override
	public Object doFilterTry(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		return null;
	}

}
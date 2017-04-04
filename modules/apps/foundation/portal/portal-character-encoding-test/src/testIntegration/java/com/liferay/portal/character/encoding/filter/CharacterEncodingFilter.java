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

package com.liferay.portal.character.encoding.filter;

import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.io.OutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Matthew Tambara
 */
@Component(
	enabled = true, immediate = true,
	property = {
		"after-filter=Absolute Redirects Filter", "dispatcher=FORWARD",
		"dispatcher=REQUEST", "servlet-context-name=",
		"servlet-filter-name=Character Encoding Filter", "url-pattern=/*"
	},
	service = Filter.class
)
public class CharacterEncodingFilter extends BaseFilter {

	@Override
	protected Log getLog() {
		return _log;
	}

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		StringBundler sb = new StringBundler();

		try (UnsyncBufferedReader bufferedReader =
				new UnsyncBufferedReader(request.getReader())) {

			String output = null;

			while ((output = bufferedReader.readLine()) != null) {
				sb.append(output);
				sb.append(StringPool.NEW_LINE);
			}
		}

		String body = sb.toString();

		try (OutputStream outputStream = response.getOutputStream()) {
			outputStream.write(body.getBytes(request.getCharacterEncoding()));
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CharacterEncodingFilter.class);

}
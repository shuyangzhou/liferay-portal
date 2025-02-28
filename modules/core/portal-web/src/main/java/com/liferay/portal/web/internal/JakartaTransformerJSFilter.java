/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.web.internal;

import com.liferay.portal.kernel.servlet.BufferCacheServletResponse;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shuyang Zhou
 */
public class JakartaTransformerJSFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(
			ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain)
		throws IOException, ServletException {

		HttpServletResponse httpServletResponse =
			(HttpServletResponse)servletResponse;

		BufferCacheServletResponse bufferCacheServletResponse =
			new BufferCacheServletResponse(httpServletResponse);

		filterChain.doFilter(servletRequest, bufferCacheServletResponse);

		ServletResponseUtil.write(
			httpServletResponse,
			StringUtil.replace(
				bufferCacheServletResponse.getString(),
				new String[] {"javax.portlet", "javax-portlet"},
				new String[] {"jakarta.portlet", "jakarta-portlet"}));
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

}
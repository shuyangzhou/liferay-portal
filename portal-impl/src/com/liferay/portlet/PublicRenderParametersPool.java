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

package com.liferay.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.servlet.PortletSessionListenerManager;
import com.liferay.portal.util.PropsValues;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author Michael Young
 */
public class PublicRenderParametersPool {

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             #get(HttpServletRequest, long)}
	 */
	@Deprecated
	public static Map<String, String[]> get(
		HttpServletRequest request, long plid, boolean warFile) {

		return get(request, plid);
	}

	protected static Map<String, String[]> get(
		HttpServletRequest request, long plid) {

		if (PropsValues.PORTLET_PUBLIC_RENDER_PARAMETER_DISTRIBUTION_LAYOUT) {
			return RenderParametersPool.getOrCreate(
				request, plid, _PUBLIC_RENDER_PARAMETERS);
		}

		HttpSession session = request.getSession();

		Map<Long, Map<String, String[]>> publicRenderParametersPool =
			_publicRenderParametersPoolMap.computeIfAbsent(
				session.getId(), id -> new ConcurrentHashMap<>());

		try {
			Layout layout = LayoutLocalServiceUtil.getLayout(plid);

			LayoutSet layoutSet = layout.getLayoutSet();

			return publicRenderParametersPool.computeIfAbsent(
				layoutSet.getLayoutSetId(), key -> new HashMap<>());
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}

			return new HashMap<>();
		}
	}

	private static final String _PUBLIC_RENDER_PARAMETERS =
		"PUBLIC_RENDER_PARAMETERS";

	private static final Log _log = LogFactoryUtil.getLog(
		PublicRenderParametersPool.class);

	private static final Map<String, Map<Long, Map<String, String[]>>>
		_publicRenderParametersPoolMap = new ConcurrentHashMap<>();

	static {
		PortletSessionListenerManager.addHttpSessionListener(
			new HttpSessionListener() {

				@Override
				public void sessionCreated(HttpSessionEvent httpSessionEvent) {
				}

				@Override
				public void sessionDestroyed(
					HttpSessionEvent httpSessionEvent) {

					HttpSession session = httpSessionEvent.getSession();

					_publicRenderParametersPoolMap.remove(session.getId());
				}

			});
	}

}
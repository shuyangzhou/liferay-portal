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

package com.liferay.portal.kernel.security.auth;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Amos Fong
 * @author Peter Fellwock
 * @author Raymond Augé
 */
public class AuthTokenUtil {

	public static void addCSRFToken(
		HttpServletRequest request, LiferayPortletURL liferayPortletURL) {

		AuthToken authToken = _authToken;

		if (authToken == null) {
			return;
		}

		authToken.addCSRFToken(request, liferayPortletURL);
	}

	public static void addPortletInvocationToken(
		HttpServletRequest request, LiferayPortletURL liferayPortletURL) {

		AuthToken authToken = _authToken;

		if (authToken == null) {
			return;
		}

		authToken.addPortletInvocationToken(request, liferayPortletURL);
	}

	/**
	 * @deprecated As of 6.2.0, replaced by {@link
	 *             #checkCSRFToken(HttpServletRequest, String)}
	 */
	@Deprecated
	public static void check(HttpServletRequest request)
		throws PortalException {

		AuthToken authToken = _authToken;

		if (authToken == null) {
			return;
		}

		authToken.check(request);
	}

	public static void checkCSRFToken(HttpServletRequest request, String origin)
		throws PrincipalException {

		AuthToken authToken = _authToken;

		if (authToken == null) {
			return;
		}

		authToken.checkCSRFToken(request, origin);
	}

	public static String getToken(HttpServletRequest request) {
		AuthToken authToken = _authToken;

		if (authToken == null) {
			return null;
		}

		return authToken.getToken(request);
	}

	public static String getToken(
		HttpServletRequest request, long plid, String portletId) {

		AuthToken authToken = _authToken;

		if (authToken == null) {
			return null;
		}

		return authToken.getToken(request, plid, portletId);
	}

	public static boolean isValidPortletInvocationToken(
		HttpServletRequest request, Layout layout, Portlet portlet) {

		AuthToken authToken = _authToken;

		if (authToken == null) {
			return false;
		}

		return authToken.isValidPortletInvocationToken(
			request, layout, portlet);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             #isValidPortletInvocationToken(HttpServletRequest, Layout,
	 *             Portlet)}
	 */
	@Deprecated
	public static boolean isValidPortletInvocationToken(
		HttpServletRequest request, long plid, String portletId,
		String strutsAction, String tokenValue) {

		AuthToken authToken = _authToken;

		if (authToken == null) {
			return false;
		}

		return authToken.isValidPortletInvocationToken(
			request, plid, portletId, strutsAction, tokenValue);
	}

	private static volatile AuthToken _authToken;

	static {
		Registry registry = RegistryUtil.getRegistry();

		AuthTokenServiceTrackerCustomizer authTokenServiceTrackerCustomizer =
			new AuthTokenServiceTrackerCustomizer(registry);

		ServiceTracker<?, AuthToken> serviceTracker = registry.trackServices(
			AuthToken.class.getName(), authTokenServiceTrackerCustomizer);

		authTokenServiceTrackerCustomizer.setServiceTracker(serviceTracker);

		serviceTracker.open();
	}

	private static class AuthTokenServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<AuthToken, AuthToken> {

		@Override
		public AuthToken addingService(
			ServiceReference<AuthToken> serviceReference) {

			_authToken = _serviceTracker.getService();

			return _registry.getService(serviceReference);
		}

		@Override
		public void modifiedService(
			ServiceReference<AuthToken> serviceReference, AuthToken authToken) {

			_authToken = _serviceTracker.getService();
		}

		@Override
		public void removedService(
			ServiceReference<AuthToken> serviceReference, AuthToken authToken) {

			_authToken = _serviceTracker.getService();
		}

		public void setServiceTracker(
			ServiceTracker<?, AuthToken> serviceTracker) {

			_serviceTracker = serviceTracker;
		}

		private AuthTokenServiceTrackerCustomizer(Registry registry) {
			_registry = registry;
		}

		private final Registry _registry;
		private ServiceTracker<?, AuthToken> _serviceTracker;

	}

}
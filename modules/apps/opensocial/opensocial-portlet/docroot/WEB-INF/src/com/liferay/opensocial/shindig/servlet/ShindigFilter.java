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

package com.liferay.opensocial.shindig.servlet;

import com.google.inject.Injector;

import com.liferay.opensocial.shindig.util.HttpServletRequestThreadLocal;
import com.liferay.opensocial.shindig.util.ShindigUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.AuthenticatedUserUUIDStoreUtil;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.CookieKeys;
import com.liferay.portal.kernel.util.Digester;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import java.security.Key;
import java.security.Provider;
import java.security.Security;

import javax.crypto.Cipher;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shindig.common.servlet.GuiceServletContextListener;
import org.apache.shindig.common.servlet.InjectedFilter;

/**
 * @author Michael Young
 * @author Dennis Ju
 */
public class ShindigFilter extends InjectedFilter {

	public void destroy() {
	}

	public void doFilter(
			ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain)
		throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)servletRequest;

		if (injector == null) {
			HttpSession session = request.getSession();

			_init(session.getServletContext());
		}

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (permissionChecker == null) {
			setPermissionChecker(servletRequest);
		}

		ShindigUtil.setScheme(servletRequest.getScheme());

		String serverName = servletRequest.getServerName();

		String host = serverName.concat(StringPool.COLON).concat(
			String.valueOf(servletRequest.getServerPort()));

		ShindigUtil.setHost(host);

		HttpServletRequestThreadLocal.setHttpServletRequest(request);

		try {
			filterChain.doFilter(servletRequest, servletResponse);
		}
		finally {
			HttpServletRequestThreadLocal.setHttpServletRequest(null);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		// LPS-23577 and LPS-41715

		injector = null;
	}

	protected boolean setPermissionChecker(ServletRequest servletRequest) {
		String companyIdString = CookieKeys.getCookie(
			(HttpServletRequest)servletRequest, CookieKeys.COMPANY_ID);

		if (Validator.isNull(companyIdString)) {
			return false;
		}

		long companyId = GetterUtil.getLong(companyIdString);

		String userUUID = StringPool.BLANK;

		try {
			Company company = CompanyLocalServiceUtil.fetchCompany(companyId);

			if (company == null) {
				return false;
			}

			String userUUIDString = CookieKeys.getCookie(
				(HttpServletRequest)servletRequest, CookieKeys.USER_UUID);

			if (Validator.isNull(userUUIDString)) {
				return false;
			}

			try {
				userUUID = GetterUtil.getString(
					_decrypt(company.getKeyObj(), userUUIDString));
			}
			catch (Exception e) {
				return false;
			}
		}
		catch (Exception e) {
			_log.error(e, e);

			return false;
		}

		if (!AuthenticatedUserUUIDStoreUtil.exists(userUUID)) {
			return false;
		}

		String userIdString = userUUID.substring(
			0, userUUID.indexOf(StringPool.PERIOD));

		long userId = GetterUtil.getLong(userIdString);

		try {
			User user = UserLocalServiceUtil.getUserById(userId);

			PrincipalThreadLocal.setName(userIdString);

			PermissionChecker permissionChecker =
				PermissionCheckerFactoryUtil.create(user);

			PermissionThreadLocal.setPermissionChecker(permissionChecker);
		}
		catch (Exception e) {
			_log.error(e, e);

			return false;
		}

		return true;
	}

	private String _decrypt(Key key, String encryptedString) throws Exception {
		byte[] encryptedBytes = Base64.decode(encryptedString);

		String algorithm = key.getAlgorithm();

		Security.addProvider(_getProvider());

		Cipher cipher = Cipher.getInstance(algorithm);

		cipher.init(Cipher.DECRYPT_MODE, key);

		return new String(cipher.doFinal(encryptedBytes), Digester.ENCODING);
	}

	private Provider _getProvider()
		throws ClassNotFoundException, IllegalAccessException,
			   InstantiationException {

		Class<?> providerClass = null;

		try {
			providerClass = Class.forName(_PROVIDER_CLASS);
		}
		catch (ClassNotFoundException cnfe) {
			if (ServerDetector.isWebSphere() &&
				_PROVIDER_CLASS.equals(_SUN_PROVIDER_CLASS)) {

				if (_log.isWarnEnabled()) {
					_log.warn(
						"WebSphere does not have " + _SUN_PROVIDER_CLASS +
							", using " + _IBM_PROVIDER_CLASS + " instead");
				}

				providerClass = Class.forName(_IBM_PROVIDER_CLASS);
			}
			else if (System.getProperty("java.vm.vendor").equals(
						"IBM Corporation")) {

				if (_log.isWarnEnabled()) {
					_log.warn(
						"IBM JVM does not have " + _SUN_PROVIDER_CLASS +
							", using " + _IBM_PROVIDER_CLASS + " instead");
				}

				providerClass = Class.forName(_IBM_PROVIDER_CLASS);
			}
			else {
				throw cnfe;
			}
		}

		return (Provider)providerClass.newInstance();
	}

	private void _init(ServletContext servletContext) throws ServletException {
		injector = (Injector)servletContext.getAttribute(
			GuiceServletContextListener.INJECTOR_ATTRIBUTE);

		if (injector == null) {
			injector = (Injector)servletContext.getAttribute(
				GuiceServletContextListener.INJECTOR_NAME);

			if (injector == null) {
				throw new UnavailableException(
					"Guice injector is not available. Please register " +
						GuiceServletContextListener.class.getName() + ".");
			}
		}

		injector.injectMembers(this);
	}

	private static final String _IBM_PROVIDER_CLASS =
		"com.ibm.crypto.provider.IBMJCE";

	private static final String _PROVIDER_CLASS = GetterUtil.getString(
		SystemProperties.get(ShindigFilter.class.getName() + ".provider.class"),
		"com.sun.crypto.provider.SunJCE");

	private static final String _SUN_PROVIDER_CLASS =
		"com.sun.crypto.provider.SunJCE";

	private static final Log _log = LogFactoryUtil.getLog(ShindigFilter.class);

}
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

package com.liferay.portal.messaging.internal;

import com.liferay.portal.kernel.cluster.ClusterInvokeThreadLocal;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GroupThreadLocal;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Tina Tian
 */
public class MessageBusThreadLocalUtil {

	public static void populateMessageFromThreadLocals(Message message) {
		if (!message.contains("companyId")) {
			message.put("companyId", CompanyThreadLocal.getCompanyId());
		}

		if (!ClusterInvokeThreadLocal.isEnabled()) {
			message.put("clusterInvoke", Boolean.FALSE);
		}

		if (!message.contains("defaultLocale")) {
			message.put("defaultLocale", LocaleThreadLocal.getDefaultLocale());
		}

		if (!message.contains("groupId")) {
			message.put("groupId", GroupThreadLocal.getGroupId());
		}

		if (!message.contains("permissionChecker")) {
			message.put(
				"permissionChecker",
				PermissionThreadLocal.getPermissionChecker());
		}

		if (!message.contains("principalName")) {
			message.put("principalName", PrincipalThreadLocal.getName());
		}

		if (!message.contains("principalPassword")) {
			message.put(
				"principalPassword", PrincipalThreadLocal.getPassword());
		}

		if (!message.contains("siteDefaultLocale")) {
			message.put(
				"siteDefaultLocale", LocaleThreadLocal.getSiteDefaultLocale());
		}

		if (!message.contains("themeDisplayLocale")) {
			message.put(
				"themeDisplayLocale",
				LocaleThreadLocal.getThemeDisplayLocale());
		}
	}

	public static void populateThreadLocalsFromMessage(Message message) {
		long companyId = message.getLong("companyId");

		if (companyId > 0) {
			CompanyThreadLocal.setCompanyId(companyId);
		}

		Boolean clusterInvoke = (Boolean)message.get("clusterInvoke");

		if (clusterInvoke != null) {
			ClusterInvokeThreadLocal.setEnabled(clusterInvoke);
		}

		Locale defaultLocale = (Locale)message.get("defaultLocale");

		if (defaultLocale != null) {
			LocaleThreadLocal.setDefaultLocale(defaultLocale);
		}

		long groupId = message.getLong("groupId");

		if (groupId > 0) {
			GroupThreadLocal.setGroupId(groupId);
		}

		PermissionChecker permissionChecker = (PermissionChecker)message.get(
			"permissionChecker");

		String principalName = message.getString("principalName");

		if (Validator.isNotNull(principalName)) {
			PrincipalThreadLocal.setName(principalName);
		}

		if ((permissionChecker == null) && Validator.isNotNull(principalName)) {
			try {
				UserLocalService userLocalService =
					_userLocalServiceServiceTracker.getService();

				User user = userLocalService.fetchUser(
					PrincipalThreadLocal.getUserId());

				PermissionCheckerFactory permissionCheckerFactory =
					_permissionCheckerFactoryServiceTracker.getService();

				permissionChecker = permissionCheckerFactory.create(user);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		if (permissionChecker != null) {
			PermissionThreadLocal.setPermissionChecker(permissionChecker);
		}

		String principalPassword = message.getString("principalPassword");

		if (Validator.isNotNull(principalPassword)) {
			PrincipalThreadLocal.setPassword(principalPassword);
		}

		Locale siteDefaultLocale = (Locale)message.get("siteDefaultLocale");

		if (siteDefaultLocale != null) {
			LocaleThreadLocal.setSiteDefaultLocale(siteDefaultLocale);
		}

		Locale themeDisplayLocale = (Locale)message.get("themeDisplayLocale");

		if (themeDisplayLocale != null) {
			LocaleThreadLocal.setThemeDisplayLocale(themeDisplayLocale);
		}
	}

	private static final ServiceTracker
		<PermissionCheckerFactory, PermissionCheckerFactory>
			_permissionCheckerFactoryServiceTracker;
	private static final ServiceTracker<UserLocalService, UserLocalService>
		_userLocalServiceServiceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			MessageBusThreadLocalUtil.class);

		ServiceTracker<PermissionCheckerFactory, PermissionCheckerFactory>
			permissionCheckerFactoryServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), PermissionCheckerFactory.class,
				null);

		permissionCheckerFactoryServiceTracker.open();

		_permissionCheckerFactoryServiceTracker =
			permissionCheckerFactoryServiceTracker;

		ServiceTracker<UserLocalService, UserLocalService>
			userLocalServiceServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), UserLocalService.class, null);

		userLocalServiceServiceTracker.open();

		_userLocalServiceServiceTracker = userLocalServiceServiceTracker;
	}

}
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

package com.liferay.portal.template.freemarker.internal;

import com.liferay.portal.kernel.cluster.ClusterInvokeThreadLocal;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.GroupThreadLocal;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;
import java.util.Map;

/**
 * @author Tina Tian
 */
public class FreemarkerThreadLocalUtil {

	public static Map<String, Object> collectThreadLocals() {
		return HashMapBuilder.<String, Object>put(
			"clusterInvoke", ClusterInvokeThreadLocal.isEnabled()
		).put(
			"companyId", CompanyThreadLocal.getCompanyId()
		).put(
			"defaultLocale", LocaleThreadLocal.getDefaultLocale()
		).put(
			"groupId", GroupThreadLocal.getGroupId()
		).put(
			"permissionChecker", PermissionThreadLocal.getPermissionChecker()
		).put(
			"principalName", PrincipalThreadLocal.getName()
		).put(
			"principalPassword", PrincipalThreadLocal.getPassword()
		).put(
			"serviceContext", ServiceContextThreadLocal.getServiceContext()
		).put(
			"siteDefaultLocale", LocaleThreadLocal.getSiteDefaultLocale()
		).put(
			"themeDisplayLocale", LocaleThreadLocal.getThemeDisplayLocale()
		).build();
	}

	public static void populateThreadLocals(
		Map<String, Object> threadLocals,
		PermissionCheckerFactory permissionCheckerFactory,
		UserLocalService userLocalService) {

		long companyId = GetterUtil.getLong(threadLocals.get("companyId"));

		if (companyId > 0) {
			CompanyThreadLocal.setCompanyId(companyId);
		}

		Boolean clusterInvoke = (Boolean)threadLocals.get("clusterInvoke");

		if (clusterInvoke != null) {
			ClusterInvokeThreadLocal.setEnabled(clusterInvoke);
		}

		Locale defaultLocale = (Locale)threadLocals.get("defaultLocale");

		if (defaultLocale != null) {
			LocaleThreadLocal.setDefaultLocale(defaultLocale);
		}

		long groupId = GetterUtil.getLong(threadLocals.get("groupId"));

		if (groupId > 0) {
			GroupThreadLocal.setGroupId(groupId);
		}

		PermissionChecker permissionChecker =
			(PermissionChecker)threadLocals.get("permissionChecker");

		String principalName = (String)threadLocals.get("principalName");

		if (Validator.isNotNull(principalName)) {
			PrincipalThreadLocal.setName(principalName);
		}

		if ((permissionChecker == null) && Validator.isNotNull(principalName)) {
			try {
				User user = userLocalService.fetchUser(
					PrincipalThreadLocal.getUserId());

				permissionChecker = permissionCheckerFactory.create(user);
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		}

		if (permissionChecker != null) {
			PermissionThreadLocal.setPermissionChecker(permissionChecker);
		}

		String principalPassword = (String)threadLocals.get(
			"principalPassword");

		if (Validator.isNotNull(principalPassword)) {
			PrincipalThreadLocal.setPassword(principalPassword);
		}

		ServiceContext serviceContext = (ServiceContext)threadLocals.get(
			"serviceContext");

		if (serviceContext != null) {
			ServiceContextThreadLocal.pushServiceContext(serviceContext);
		}

		Locale siteDefaultLocale = (Locale)threadLocals.get(
			"siteDefaultLocale");

		if (siteDefaultLocale != null) {
			LocaleThreadLocal.setSiteDefaultLocale(siteDefaultLocale);
		}

		Locale themeDisplayLocale = (Locale)threadLocals.get(
			"themeDisplayLocale");

		if (themeDisplayLocale != null) {
			LocaleThreadLocal.setThemeDisplayLocale(themeDisplayLocale);
		}
	}

}
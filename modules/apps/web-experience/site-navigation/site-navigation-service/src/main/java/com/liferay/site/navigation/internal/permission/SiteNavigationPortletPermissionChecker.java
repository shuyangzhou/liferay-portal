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

package com.liferay.site.navigation.internal.permission;

import com.liferay.portal.kernel.security.permission.checker.PortletResourcePermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.StagedPortletPermissionCheck;
import com.liferay.site.navigation.admin.constants.SiteNavigationAdminPortletKeys;
import com.liferay.site.navigation.constants.SiteNavigationConstants;

/**
 * @author Preston Crary
 */
public class SiteNavigationPortletPermissionChecker
	extends PortletResourcePermissionChecker {

	public SiteNavigationPortletPermissionChecker() {
		super(
			SiteNavigationConstants.RESOURCE_NAME,
			new StagedPortletPermissionCheck(
				SiteNavigationAdminPortletKeys.SITE_NAVIGATION_ADMIN));
	}

}
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

package com.liferay.bookmarks.internal.permission;

import com.liferay.bookmarks.constants.BookmarksConstants;
import com.liferay.bookmarks.constants.BookmarksPortletKeys;
import com.liferay.portal.kernel.security.permission.checker.PortletPermission;
import com.liferay.portal.kernel.security.permission.checker.PortletPermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.StagedPortletPermissionCheck;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;

/**
 * @author Preston Crary
 */
@OSGiBeanProperties(
	property = {"resource.name=" + BookmarksConstants.RESOURCE_NAME},
	service = PortletPermission.class
)
public class BookmarksPortletPermissionChecker
	extends PortletPermissionChecker {

	public BookmarksPortletPermissionChecker() {
		super(
			BookmarksConstants.RESOURCE_NAME,
			new StagedPortletPermissionCheck(BookmarksPortletKeys.BOOKMARKS));
	}

}
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

package com.liferay.blogs.web.internal.permission;

import com.liferay.blogs.constants.BlogsConstants;
import com.liferay.blogs.web.constants.BlogsPortletKeys;
import com.liferay.portal.kernel.security.permission.checker.PortletPermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.StagedPortletPermissionCheck;

import org.osgi.service.component.annotations.Component;

/**
 * @author Preston Crary
 */
@Component(
	immediate = true,
	property = {
		"resource.name=" + BlogsConstants.RESOURCE_NAME,
		"service.ranking:Integer=100"
	},
	service = PortletPermissionChecker.class
)
public class BlogsPortletPermissionChecker extends PortletPermissionChecker {

	public BlogsPortletPermissionChecker() {
		super(
			BlogsConstants.RESOURCE_NAME,
			new StagedPortletPermissionCheck(BlogsPortletKeys.BLOGS));
	}

}
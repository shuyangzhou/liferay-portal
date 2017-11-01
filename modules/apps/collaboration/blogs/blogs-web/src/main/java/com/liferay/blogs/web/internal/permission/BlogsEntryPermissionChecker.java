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
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.service.BlogsEntryLocalService;
import com.liferay.blogs.web.constants.BlogsPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.checker.ModelPermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.StagedModelPermissionCheck;
import com.liferay.portal.kernel.security.permission.checker.WorkflowedModelPermissionCheck;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Preston Crary
 */
@Component(
	immediate = true,
	property = {
		"resource.name=" + BlogsConstants.RESOURCE_NAME,
		"service.ranking:Integer=100"
	},
	service = ModelPermissionChecker.class
)
public class BlogsEntryPermissionChecker
	extends ModelPermissionChecker<BlogsEntry> {

	public BlogsEntryPermissionChecker() {
		super(
			BlogsEntry.class.getName(),
			new StagedModelPermissionCheck<>(BlogsPortletKeys.BLOGS),
			new WorkflowedModelPermissionCheck<>());
	}

	@Override
	protected BlogsEntry getModel(long entryId) throws PortalException {
		return _blogsEntryLocalService.getEntry(entryId);
	}

	@Reference
	private BlogsEntryLocalService _blogsEntryLocalService;

}
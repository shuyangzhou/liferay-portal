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

package com.liferay.blogs.internal.permission;

import com.liferay.blogs.constants.BlogsPortletKeys;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.service.BlogsEntryLocalService;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.checker.EntryModelPermission;
import com.liferay.portal.kernel.security.permission.checker.EntryModelPermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.StagedModelPermissionCheck;
import com.liferay.portal.kernel.security.permission.checker.WorkflowedModelPermissionCheck;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;

/**
 * @author Preston Crary
 */
@OSGiBeanProperties(
	property = {"model.class.name=com.liferay.blogs.model.BlogsEntry"},
	service = EntryModelPermission.class
)
public class BlogsEntryPermissionChecker
	extends EntryModelPermissionChecker<BlogsEntry> {

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

	@BeanReference(type = BlogsEntryLocalService.class)
	private BlogsEntryLocalService _blogsEntryLocalService;

}
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

import com.liferay.bookmarks.constants.BookmarksPortletKeys;
import com.liferay.bookmarks.model.BookmarksFolder;
import com.liferay.bookmarks.model.BookmarksFolderConstants;
import com.liferay.bookmarks.service.BookmarksFolderLocalService;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.ContainerModelPermission;
import com.liferay.portal.kernel.security.permission.checker.ContainerModelPermissionCheck;
import com.liferay.portal.kernel.security.permission.checker.ContainerModelPermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.PortletPermission;
import com.liferay.portal.kernel.security.permission.checker.StagedModelPermissionCheck;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.util.PropsValues;

/**
 * @author Preston Crary
 */
@OSGiBeanProperties(
	property = {"model.class.name=com.liferay.bookmarks.model.BookmarksFolder"},
	service = ContainerModelPermission.class
)
public class BookmarksFolderPermissionChecker
	extends ContainerModelPermissionChecker<BookmarksFolder> {

	public BookmarksFolderPermissionChecker() {
		super(BookmarksFolder.class.getName());

		if (PropsValues.PERMISSIONS_VIEW_DYNAMIC_INHERITANCE) {
			setModelPermissionChecks(
				new StagedModelPermissionCheck<>(
					BookmarksPortletKeys.BOOKMARKS),
				new DynamicInheritanceCheck());
		}
		else {
			setModelPermissionChecks(
				new StagedModelPermissionCheck<>(
					BookmarksPortletKeys.BOOKMARKS));
		}
	}

	@Override
	protected boolean doContains(
			PermissionChecker permissionChecker, BookmarksFolder folder,
			String actionId)
		throws PortalException {

		if (ActionKeys.ADD_FOLDER.equals(actionId)) {
			actionId = ActionKeys.ADD_SUBFOLDER;
		}

		return super.doContains(permissionChecker, folder, actionId);
	}

	@Override
	protected BookmarksFolder getModel(long folderId) throws PortalException {
		return _bookmarksFolderLocalService.getFolder(folderId);
	}

	@Override
	protected PortletPermission getPortletPermission() {
		return _portletPermission;
	}

	@BeanReference(type = BookmarksFolderLocalService.class)
	private BookmarksFolderLocalService _bookmarksFolderLocalService;

	@BeanReference(
		name = "com.liferay.bookmarks.internal.permission.BookmarksPortletPermissionChecker"
	)
	private PortletPermission _portletPermission;

	private class DynamicInheritanceCheck
		extends ContainerModelPermissionCheck<BookmarksFolder> {

		@Override
		protected BookmarksFolder fetchParent(BookmarksFolder folder)
			throws PortalException {

			long folderId = folder.getParentFolderId();

			if (folderId == BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
				return null;
			}

			if (folder.isInTrash()) {
				return _bookmarksFolderLocalService.fetchBookmarksFolder(
					folderId);
			}

			return _bookmarksFolderLocalService.getFolder(folderId);
		}

		@Override
		protected PortletPermission getPortletPermission() {
			return _portletPermission;
		}

	}

}
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
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.ContainerDynamicInheritanceCheck;
import com.liferay.portal.kernel.security.permission.checker.ContainerModelPermission;
import com.liferay.portal.kernel.security.permission.checker.ContainerModelPermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.ModelPermissionCheck;
import com.liferay.portal.kernel.security.permission.checker.PortletPermission;
import com.liferay.portal.kernel.security.permission.checker.StagedModelPermissionCheck;
import com.liferay.portal.util.PropsValues;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Preston Crary
 */
public class BookmarksFolderPermissionCheckerFactory {

	public static ContainerModelPermission<BookmarksFolder> create(
		BookmarksFolderLocalService bookmarksFolderLocalService,
		PortletPermission portletPermission) {

		List<ModelPermissionCheck<BookmarksFolder>> modelPermissionChecks =
			new ArrayList<>(2);

		modelPermissionChecks.add(
			new StagedModelPermissionCheck<>(BookmarksPortletKeys.BOOKMARKS));

		if (PropsValues.PERMISSIONS_VIEW_DYNAMIC_INHERITANCE) {
			modelPermissionChecks.add(
				new ContainerDynamicInheritanceCheck<>(
					_getFetchParentFunction(bookmarksFolderLocalService),
					BookmarksFolder::getFolderId, portletPermission));
		}

		return new ContainerModelPermissionChecker<BookmarksFolder>(
			BookmarksFolder.class.getName(),
			bookmarksFolderLocalService::getFolder,
			BookmarksFolder::getFolderId, modelPermissionChecks,
			portletPermission) {

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

		};
	}

	private static UnsafeFunction
		<BookmarksFolder, BookmarksFolder, ? extends PortalException>
			_getFetchParentFunction(
				BookmarksFolderLocalService bookmarksFolderLocalService) {

		return folder -> {
			long folderId = folder.getParentFolderId();

			if (BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID == folderId) {
				return null;
			}

			if (folder.isInTrash()) {
				return bookmarksFolderLocalService.fetchBookmarksFolder(
					folderId);
			}

			return bookmarksFolderLocalService.getFolder(folderId);
		};
	}

}
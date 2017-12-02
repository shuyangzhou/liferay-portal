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
import com.liferay.bookmarks.model.BookmarksEntry;
import com.liferay.bookmarks.model.BookmarksFolder;
import com.liferay.bookmarks.model.BookmarksFolderConstants;
import com.liferay.bookmarks.service.BookmarksEntryLocalService;
import com.liferay.bookmarks.service.BookmarksFolderLocalService;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.checker.ContainerModelPermission;
import com.liferay.portal.kernel.security.permission.checker.EntryDynamicInheritanceCheck;
import com.liferay.portal.kernel.security.permission.checker.EntryModelPermission;
import com.liferay.portal.kernel.security.permission.checker.EntryModelPermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.ModelPermissionCheck;
import com.liferay.portal.kernel.security.permission.checker.PortletPermission;
import com.liferay.portal.kernel.security.permission.checker.StagedModelPermissionCheck;
import com.liferay.portal.util.PropsValues;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Preston Crary
 */
public class BookmarksEntryPermissionCheckerFactory {

	public static EntryModelPermission<BookmarksEntry> create(
		BookmarksEntryLocalService bookmarksEntryLocalService,
		BookmarksFolderLocalService bookmarksFolderLocalService,
		ContainerModelPermission<BookmarksFolder> containerModelPermission,
		PortletPermission portletPermission) {

		List<ModelPermissionCheck<BookmarksEntry>> modelPermissionChecks =
			new ArrayList<>(2);

		modelPermissionChecks.add(
			new StagedModelPermissionCheck<>(BookmarksPortletKeys.BOOKMARKS));

		if (PropsValues.PERMISSIONS_VIEW_DYNAMIC_INHERITANCE) {
			modelPermissionChecks.add(
				new EntryDynamicInheritanceCheck<>(
					_getFetchContainerFunction(bookmarksFolderLocalService),
					portletPermission, containerModelPermission));
		}

		return new EntryModelPermissionChecker<>(
			BookmarksEntry.class.getName(),
			bookmarksEntryLocalService::getEntry, BookmarksEntry::getEntryId,
			modelPermissionChecks);
	}

	private static UnsafeFunction
		<BookmarksEntry, BookmarksFolder, PortalException>
			_getFetchContainerFunction(
				BookmarksFolderLocalService bookmarksFolderLocalService) {

		return entry -> {
			long folderId = entry.getFolderId();

			if (BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID == folderId) {
				return null;
			}

			if (entry.isInTrash()) {
				return bookmarksFolderLocalService.fetchBookmarksFolder(
					folderId);
			}

			return bookmarksFolderLocalService.getFolder(folderId);
		};
	}

}
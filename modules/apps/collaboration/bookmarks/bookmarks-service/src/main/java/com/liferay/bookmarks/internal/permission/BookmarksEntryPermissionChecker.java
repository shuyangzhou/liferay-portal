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
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.checker.ContainerModelPermission;
import com.liferay.portal.kernel.security.permission.checker.EntryModelPermission;
import com.liferay.portal.kernel.security.permission.checker.EntryModelPermissionCheck;
import com.liferay.portal.kernel.security.permission.checker.EntryModelPermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.ModelPermission;
import com.liferay.portal.kernel.security.permission.checker.PortletPermission;
import com.liferay.portal.kernel.security.permission.checker.StagedModelPermissionCheck;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.util.PropsValues;

/**
 * @author Preston Crary
 */
@OSGiBeanProperties(
	property = {"model.class.name=com.liferay.bookmarks.model.BookmarksEntry"},
	service = EntryModelPermission.class
)
public class BookmarksEntryPermissionChecker
	extends EntryModelPermissionChecker<BookmarksEntry> {

	public BookmarksEntryPermissionChecker() {
		super(BookmarksEntry.class.getName());

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
	protected BookmarksEntry getModel(long entryId) throws PortalException {
		return _bookmarksEntryLocalService.getEntry(entryId);
	}

	@BeanReference(type = BookmarksEntryLocalService.class)
	private BookmarksEntryLocalService _bookmarksEntryLocalService;

	@BeanReference(type = BookmarksFolderLocalService.class)
	private BookmarksFolderLocalService _bookmarksFolderLocalService;

	@BeanReference(
		name = "com.liferay.bookmarks.internal.permission.BookmarksFolderPermissionChecker"
	)
	private ContainerModelPermission<BookmarksFolder> _containerModelPermission;

	@BeanReference(
		name = "com.liferay.bookmarks.internal.permission.BookmarksPortletPermissionChecker"
	)
	private PortletPermission _portletPermission;

	private class DynamicInheritanceCheck
		extends EntryModelPermissionCheck<BookmarksEntry, BookmarksFolder> {

		@Override
		protected BookmarksFolder fetchContainer(BookmarksEntry entry)
			throws PortalException {

			long folderId = entry.getFolderId();

			if (folderId == BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
				return null;
			}

			if (entry.isInTrash()) {
				return _bookmarksFolderLocalService.fetchBookmarksFolder(
					folderId);
			}

			return _bookmarksFolderLocalService.getFolder(folderId);
		}

		@Override
		protected ModelPermission<BookmarksFolder>
			getContainerModelPermission() {

			return _containerModelPermission;
		}

		@Override
		protected PortletPermission getPortletPermission() {
			return _portletPermission;
		}

	}

}
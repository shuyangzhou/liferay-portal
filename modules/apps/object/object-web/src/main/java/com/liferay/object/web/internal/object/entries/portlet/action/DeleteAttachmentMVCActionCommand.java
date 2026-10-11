/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.web.internal.object.entries.portlet.action;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.object.constants.ObjectActionKeys;
import com.liferay.object.definition.security.permission.resource.ObjectDefinitionPortletResourcePermissionRegistryUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TempFileEntryUtil;
import com.liferay.portal.kernel.util.WebKeys;

import jakarta.portlet.ActionRequest;
import jakarta.portlet.ActionResponse;

/**
 * @author Carolina Barbosa
 */
public class DeleteAttachmentMVCActionCommand extends BaseMVCActionCommand {

	public DeleteAttachmentMVCActionCommand(
		DLFileEntryLocalService dlFileEntryLocalService,
		ObjectDefinition objectDefinition) {

		_dlFileEntryLocalService = dlFileEntryLocalService;
		_objectDefinition = objectDefinition;
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long fileEntryId = ParamUtil.getLong(actionRequest, "fileEntryId");

		if (fileEntryId == 0) {
			return;
		}

		DLFileEntry dlFileEntry = _dlFileEntryLocalService.fetchDLFileEntry(
			fileEntryId);

		if (dlFileEntry == null) {
			return;
		}

		DLFolder dlFolder = dlFileEntry.getFolder();

		if (!StringUtil.equals(
				dlFolder.getName(), _objectDefinition.getPortletId())) {

			return;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (dlFileEntry.getUserId() != themeDisplay.getUserId()) {
			return;
		}

		PortletResourcePermission portletResourcePermission =
			ObjectDefinitionPortletResourcePermissionRegistryUtil.getService(
				_objectDefinition.getResourceName());

		if (!portletResourcePermission.contains(
				themeDisplay.getPermissionChecker(), dlFileEntry.getGroupId(),
				ObjectActionKeys.ADD_OBJECT_ENTRY)) {

			return;
		}

		TempFileEntryUtil.deleteTempFileEntry(fileEntryId);
	}

	private final DLFileEntryLocalService _dlFileEntryLocalService;
	private final ObjectDefinition _objectDefinition;

}
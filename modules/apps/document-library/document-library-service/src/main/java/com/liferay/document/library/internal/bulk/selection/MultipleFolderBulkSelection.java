/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.internal.bulk.selection;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.bulk.selection.BaseMultipleEntryBulkSelection;
import com.liferay.bulk.selection.BulkSelection;
import com.liferay.bulk.selection.BulkSelectionFactory;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.Folder;

import java.util.Map;

/**
 * @author Adolfo Pérez
 */
public class MultipleFolderBulkSelection
	extends BaseMultipleEntryBulkSelection<Folder> {

	public MultipleFolderBulkSelection(
		long[] folderIds, Map<String, String[]> parameterMap,
		DLAppService dlAppService) {

		super(folderIds, parameterMap);

		_dlAppService = dlAppService;
	}

	@Override
	public Class<? extends BulkSelectionFactory>
		getBulkSelectionFactoryClass() {

		return FolderBulkSelectionFactory.class;
	}

	@Override
	public BulkSelection<AssetEntry> toAssetEntryBulkSelection() {
		throw new UnsupportedOperationException("Folder is not an asset");
	}

	@Override
	protected Folder fetchEntry(long folderId) throws PortalException {
		return _dlAppService.fetchFolder(folderId);
	}

	private final DLAppService _dlAppService;

}
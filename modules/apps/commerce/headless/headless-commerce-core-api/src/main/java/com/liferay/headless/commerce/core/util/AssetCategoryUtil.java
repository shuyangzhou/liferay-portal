/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.commerce.core.util;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetCategoryConstants;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetCategoryService;
import com.liferay.asset.kernel.service.AssetVocabularyService;
import com.liferay.exportimport.kernel.empty.model.EmptyModelManagerUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Collections;

/**
 * @author Alessio Antonio Rendina
 */
public class AssetCategoryUtil {

	public static AssetCategory getOrAddEmptyAssetCategory(
			AssetCategoryLocalService assetCategoryLocalService,
			AssetCategoryService assetCategoryService,
			AssetVocabularyService assetVocabularyService,
			String externalReferenceCode, long groupId,
			String vocabularyExternalReferenceCode)
		throws PortalException {

		if (Validator.isNull(vocabularyExternalReferenceCode)) {
			return assetCategoryService.getOrAddEmptyCategory(
				externalReferenceCode, groupId);
		}

		AssetVocabulary assetVocabulary =
			assetVocabularyService.getOrAddEmptyVocabulary(
				vocabularyExternalReferenceCode, groupId);

		return EmptyModelManagerUtil.getOrAddEmptyModel(
			AssetCategory.class,
			() -> assetCategoryService.addCategory(
				externalReferenceCode, groupId,
				AssetCategoryConstants.EMPTY_PARENT_CATEGORY_ID,
				Collections.singletonMap(
					LocaleUtil.getSiteDefault(), externalReferenceCode),
				null, assetVocabulary.getVocabularyId(), false, new String[0],
				new ServiceContext()),
			externalReferenceCode,
			assetCategoryLocalService::
				fetchAssetCategoryByExternalReferenceCode,
			assetCategoryLocalService::getAssetCategoryByExternalReferenceCode,
			groupId, "category");
	}

}
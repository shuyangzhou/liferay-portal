/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.commerce.admin.pricing.internal.util.v2_0;

import com.liferay.asset.kernel.exception.NoSuchCategoryException;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetCategoryService;
import com.liferay.asset.kernel.service.AssetVocabularyService;
import com.liferay.commerce.pricing.model.CommercePriceModifier;
import com.liferay.commerce.pricing.model.CommercePriceModifierRel;
import com.liferay.commerce.pricing.service.CommercePriceModifierRelService;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.PriceModifierCategory;
import com.liferay.headless.commerce.core.helper.ServiceContextHelper;
import com.liferay.headless.commerce.core.util.AssetCategoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.lazy.referencing.LazyReferencingThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author Riccardo Alberti
 */
public class PriceModifierCategoryUtil {

	public static CommercePriceModifierRel addCommercePriceModifierRel(
			AssetCategoryLocalService assetCategoryLocalService,
			AssetCategoryService assetCategoryService,
			AssetVocabularyService assetVocabularyService,
			CommercePriceModifier commercePriceModifier,
			CommercePriceModifierRelService commercePriceModifierRelService,
			long groupId, PriceModifierCategory priceModifierCategory,
			ServiceContextHelper serviceContextHelper)
		throws PortalException {

		ServiceContext serviceContext =
			serviceContextHelper.getServiceContext();

		AssetCategory assetCategory = _getAssetCategory(
			assetCategoryLocalService, assetCategoryService,
			assetVocabularyService, groupId, priceModifierCategory,
			serviceContext);

		CommercePriceModifierRel commercePriceModifierRel =
			commercePriceModifierRelService.fetchCommercePriceModifierRel(
				commercePriceModifier.getCommercePriceModifierId(),
				AssetCategory.class.getName(), assetCategory.getCategoryId());

		if (commercePriceModifierRel != null) {
			return commercePriceModifierRel;
		}

		return commercePriceModifierRelService.addCommercePriceModifierRel(
			commercePriceModifier.getCommercePriceModifierId(),
			AssetCategory.class.getName(), assetCategory.getCategoryId(),
			serviceContext);
	}

	private static AssetCategory _getAssetCategory(
			AssetCategoryLocalService assetCategoryLocalService,
			AssetCategoryService assetCategoryService,
			AssetVocabularyService assetVocabularyService, long groupId,
			PriceModifierCategory priceModifierCategory,
			ServiceContext serviceContext)
		throws PortalException {

		String categoryExternalReferenceCode =
			priceModifierCategory.getCategoryExternalReferenceCode();

		if (Validator.isNull(categoryExternalReferenceCode)) {
			return assetCategoryLocalService.getCategory(
				GetterUtil.getLong(priceModifierCategory.getCategoryId()));
		}

		AssetCategory assetCategory =
			assetCategoryLocalService.fetchAssetCategoryByExternalReferenceCode(
				categoryExternalReferenceCode, groupId);

		if (assetCategory != null) {
			return assetCategory;
		}

		long categoryId = GetterUtil.getLong(
			priceModifierCategory.getCategoryId());

		if (categoryId > 0) {
			assetCategory = assetCategoryLocalService.fetchAssetCategory(
				categoryId);

			if ((assetCategory != null) &&
				(assetCategory.getCompanyId() ==
					serviceContext.getCompanyId())) {

				return assetCategory;
			}
		}

		if (!LazyReferencingThreadLocal.isEnabled()) {
			throw new NoSuchCategoryException(
				"Unable to find category with external reference code " +
					categoryExternalReferenceCode);
		}

		return AssetCategoryUtil.getOrAddEmptyAssetCategory(
			assetCategoryLocalService, assetCategoryService,
			assetVocabularyService, categoryExternalReferenceCode, groupId,
			priceModifierCategory.getVocabularyExternalReferenceCode());
	}

}
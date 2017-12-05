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

package com.liferay.asset.display.template.internal.permission;

import com.liferay.asset.display.template.model.AssetDisplayTemplate;
import com.liferay.asset.display.template.service.AssetDisplayTemplateLocalService;
import com.liferay.portal.kernel.security.permission.checker.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.checker.ModelResourcePermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.PortletResourcePermission;

/**
 * @author Preston Crary
 */
public class AssetDisplayTemplatePermissionCheckerFactory {

	public static ModelResourcePermission<AssetDisplayTemplate> create(
		AssetDisplayTemplateLocalService assetDisplayTemplateLocalService,
		PortletResourcePermission portletResourcePermission) {

		return ModelResourcePermissionChecker.create(
			AssetDisplayTemplate.class.getName(),
			AssetDisplayTemplate::getAssetDisplayTemplateId,
			assetDisplayTemplateLocalService::getAssetDisplayTemplate,
			portletResourcePermission,
			(modelResourcePermission, checksCollector) -> {
			});
	}

}
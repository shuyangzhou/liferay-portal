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

package com.liferay.layout.page.template.internal.permission;

import com.liferay.layout.page.template.model.LayoutPageTemplateCollection;
import com.liferay.layout.page.template.service.LayoutPageTemplateCollectionLocalService;
import com.liferay.portal.kernel.security.permission.checker.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.checker.ModelResourcePermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.PortletResourcePermission;

/**
 * @author Preston Crary
 */
public class LayoutPageTemplateCollectionPermissionCheckerFactory {

	public static ModelResourcePermission<LayoutPageTemplateCollection> create(
		LayoutPageTemplateCollectionLocalService
			layoutPageTemplateCollectionLocalService,
		PortletResourcePermission portletResourcePermission) {

		return ModelResourcePermissionChecker.create(
			LayoutPageTemplateCollection.class.getName(),
			LayoutPageTemplateCollection::getLayoutPageTemplateCollectionId,
			layoutPageTemplateCollectionLocalService::
				getLayoutPageTemplateCollection,
			portletResourcePermission,
			(modelResourcePermission, checksCollector) -> {
			});
	}

}
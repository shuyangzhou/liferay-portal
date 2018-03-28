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

package com.liferay.application.list;

import com.liferay.application.list.display.context.logic.PanelCategoryHelper;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import java.util.List;

/**
 * Provides methods for retrieving application category instances defined by
 * {@link PanelCategory} implementations. The Application Categories Registry is
 * an OSGi component. Application categories used within the registry should
 * also be OSGi components in order to be registered.
 *
 * @author Adolfo Pérez
 */
public interface PanelCategoryRegistry {

	public List<PanelCategory> getChildPanelCategories(
		PanelCategory panelCategory);

	public List<PanelCategory> getChildPanelCategories(
		PanelCategory panelCategory, final PermissionChecker permissionChecker,
		final Group group);

	public List<PanelCategory> getChildPanelCategories(String panelCategoryKey);

	public List<PanelCategory> getChildPanelCategories(
		String panelCategoryKey, final PermissionChecker permissionChecker,
		final Group group);

	public int getChildPanelCategoriesNotificationsCount(
		PanelCategoryHelper panelCategoryHelper, String panelCategoryKey,
		PermissionChecker permissionChecker, Group group, User user);

	public PanelCategory getFirstChildPanelCategory(
		String panelCategoryKey, PermissionChecker permissionChecker,
		Group group);

	public PanelCategory getPanelCategory(String panelCategoryKey);

}
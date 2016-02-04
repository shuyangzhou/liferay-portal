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

package com.liferay.asset.kernel.service.persistence;

import com.liferay.asset.kernel.model.AssetTagStats;
import com.liferay.asset.kernel.service.AssetTagStatsLocalServiceUtil;

import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;

/**
 * @author Brian Wing Shun Chan
 * @deprecated As of 7.0.0, replaced by {@link AssetTagStatsLocalServiceUtil#getActionableDynamicQuery()}
 * @generated
 */
@Deprecated
public abstract class AssetTagStatsActionableDynamicQuery
	extends DefaultActionableDynamicQuery {
	public AssetTagStatsActionableDynamicQuery() {
		setBaseLocalService(AssetTagStatsLocalServiceUtil.getService());

		setClassLoader(PortalClassLoaderUtil.getClassLoader());

		setModelClass(AssetTagStats.class);

		setPrimaryKeyPropertyName("tagStatsId");
	}
}
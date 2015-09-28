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

package com.liferay.portlet.asset.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
@ProviderType
public class AssetTagStatsFinderUtil {
	public static int updateAssetCountByDLFileEntryC_T_V(long classNameId,
		java.lang.String treePath, boolean visible) {
		return getFinder()
				   .updateAssetCountByDLFileEntryC_T_V(classNameId, treePath,
			visible);
	}

	public static int updateAssetCountByDLFolderC_T_V(long classNameId,
		java.lang.String treePath, boolean visible) {
		return getFinder()
				   .updateAssetCountByDLFolderC_T_V(classNameId, treePath,
			visible);
	}

	public static AssetTagStatsFinder getFinder() {
		if (_finder == null) {
			_finder = (AssetTagStatsFinder)PortalBeanLocatorUtil.locate(AssetTagStatsFinder.class.getName());

			ReferenceRegistry.registerReference(AssetTagStatsFinderUtil.class,
				"_finder");
		}

		return _finder;
	}

	public void setFinder(AssetTagStatsFinder finder) {
		_finder = finder;

		ReferenceRegistry.registerReference(AssetTagStatsFinderUtil.class,
			"_finder");
	}

	private static AssetTagStatsFinder _finder;
}
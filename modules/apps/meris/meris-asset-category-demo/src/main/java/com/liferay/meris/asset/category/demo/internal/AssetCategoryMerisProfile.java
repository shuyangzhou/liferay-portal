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

package com.liferay.meris.asset.category.demo.internal;

import com.liferay.meris.MerisProfile;
import com.liferay.portal.kernel.model.User;

/**
 * @author Eduardo Garcia
 */
public class AssetCategoryMerisProfile
	implements MerisProfile, Comparable<AssetCategoryMerisProfile> {

	public AssetCategoryMerisProfile(User user, long[] assetCategoryIds) {
		_user = user;
		_assetCategoryIds = assetCategoryIds;
	}

	@Override
	public int compareTo(AssetCategoryMerisProfile assetCategoryMerisProfile) {
		String merisProfileId = getMerisProfileId();

		return merisProfileId.compareTo(
			assetCategoryMerisProfile.getMerisProfileId());
	}

	public long[] getAssetCategoryIds() {
		return _assetCategoryIds;
	}

	@Override
	public String getMerisProfileId() {
		return String.valueOf(_user.getUserId());
	}

	private final long[] _assetCategoryIds;
	private final User _user;

}
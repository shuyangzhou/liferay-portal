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

package com.liferay.sites.kernel.util;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ServiceProxyFactory;
import com.liferay.portal.kernel.util.StringPool;

/**
 * @author Pavel Savinov
 */
public class SitesFriendlyURLAdapterUtil {

	public static Group getGroup(long companyId, String friendlyURL) {
		SitesFriendlyURLAdapter sitesFriendlyURLAdapter =
			_sitesFriendlyURLAdapter;

		if (sitesFriendlyURLAdapter != null) {
			String normalizedFriendlyURL =
				FriendlyURLNormalizerUtil.normalizeWithEncoding(
					HttpUtil.decodePath(friendlyURL));

			return sitesFriendlyURLAdapter.getGroup(
				companyId, normalizedFriendlyURL);
		}

		return null;
	}

	public static String getSiteFriendlyURL(Group group, String languageId) {
		SitesFriendlyURLAdapter sitesFriendlyURLAdapter =
			_sitesFriendlyURLAdapter;

		if (sitesFriendlyURLAdapter != null) {
			return sitesFriendlyURLAdapter.getSiteFriendlyURL(
				group, languageId);
		}

		if (group != null) {
			return group.getFriendlyURL();
		}

		return StringPool.BLANK;
	}

	private static volatile SitesFriendlyURLAdapter _sitesFriendlyURLAdapter =
		ServiceProxyFactory.newServiceTrackedInstance(
			SitesFriendlyURLAdapter.class, SitesFriendlyURLAdapterUtil.class,
			"_sitesFriendlyURLAdapter", false, true);

}
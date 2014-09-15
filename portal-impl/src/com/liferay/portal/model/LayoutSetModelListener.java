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

package com.liferay.portal.model;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.model.impl.LayoutSetModelImpl;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutSetPrototypeLocalServiceUtil;
import com.liferay.portal.servlet.filters.cache.CacheUtil;
import com.liferay.portlet.sites.util.Sites;

import java.util.Date;
import java.util.List;

/**
 * @author Alexander Chow
 * @author Raymond Augé
 * @author Will Newbury
 */
public class LayoutSetModelListener extends BaseModelListener<LayoutSet> {

	@Override
	public void onAfterRemove(LayoutSet layoutSet) {
		clearCache(layoutSet);
	}

	@Override
	public void onAfterUpdate(LayoutSet layoutSet) {
		clearCache(layoutSet);
	}

	@Override
	public void onBeforeCreate(LayoutSet layoutSet) {
		updateModifiedDate(layoutSet);
	}

	@Override
	public void onBeforeUpdate(LayoutSet layoutSet) {
		updateModifiedDate(layoutSet);
	}

	protected void clearCache(LayoutSet layoutSet) {
		if (layoutSet == null) {
			return;
		}

		if (!layoutSet.isPrivateLayout()) {
			CacheUtil.clearCache(layoutSet.getCompanyId());
		}
	}

	private void updateModifiedDate(LayoutSet layoutSet) {
		LayoutSetModelImpl layoutSetModelImpl = (LayoutSetModelImpl)layoutSet;

		Date modifiedDate = layoutSet.getModifiedDate();

		if ((modifiedDate == null) ||
			modifiedDate.equals(layoutSetModelImpl.getOriginalModifiedDate())) {

			return;
		}

		try {
			modifiedDate = DateUtil.getDBSafeDate(modifiedDate);

			long maxLastMergeTime = GetterUtil.getLong(
				layoutSet.getSettingsProperty(Sites.LAST_MERGE_TIME));

			List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(
				layoutSet.getGroupId(), layoutSet.isPrivateLayout());

			maxLastMergeTime = MaxMergeTimeUtil.findMaxMergeTimeInLayouts(
				layouts, maxLastMergeTime);

			long layoutSetPrototypeId = layoutSet.getLayoutSetPrototypeId();

			if (layoutSetPrototypeId != 0) {
				LayoutSetPrototype layoutSetPrototype =
					LayoutSetPrototypeLocalServiceUtil.getLayoutSetPrototype(
						layoutSetPrototypeId);

				Date layoutPrototypeModifiedDate =
					layoutSetPrototype.getModifiedDate();

				long layoutPrototypeModifiedTime =
					layoutPrototypeModifiedDate.getTime();

				if (layoutPrototypeModifiedTime > maxLastMergeTime) {
					maxLastMergeTime = layoutPrototypeModifiedTime;
				}
			}

			if (maxLastMergeTime >= modifiedDate.getTime()) {
				layoutSet.setModifiedDate(
					new Date(maxLastMergeTime + Time.SECOND));
			}
		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}
	}

}
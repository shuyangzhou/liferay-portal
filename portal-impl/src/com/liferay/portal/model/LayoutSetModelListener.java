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
import com.liferay.portal.kernel.exception.SystemException;
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
 * Will Newbury
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
	public void onBeforeCreate(LayoutSet layoutSet)
		throws ModelListenerException {

		updateModifiedDate(layoutSet);

		super.onBeforeCreate(layoutSet);
	}

	@Override
	public void onBeforeUpdate(LayoutSet layoutSet)
		throws ModelListenerException {

		updateModifiedDate(layoutSet);

		super.onBeforeUpdate(layoutSet);
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

		Date originalModifiedDate =
			layoutSetModelImpl.getOriginalModifiedDate();

		Date currentModifiedDate = layoutSet.getModifiedDate();

		if ((currentModifiedDate != null) &&
			!currentModifiedDate.equals(originalModifiedDate)) {

			currentModifiedDate = DateUtil.getDBSafeDate(currentModifiedDate);

			long maxLastMergedTime = GetterUtil.getLong(
				layoutSet.getSettingsProperty(Sites.LAST_MERGE_TIME));

			List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(
				layoutSet.getGroupId(), layoutSet.isPrivateLayout());

			for (Layout layout : layouts) {
				String layoutLastMergedTimeString =
					layout.getTypeSettingsProperty(Sites.LAST_MERGE_TIME);

				if (layoutLastMergedTimeString != null) {
					long layoutLastMergedTime = GetterUtil.getLong(
						layoutLastMergedTimeString);

					if (layoutLastMergedTime > maxLastMergedTime) {
						maxLastMergedTime = layoutLastMergedTime;
					}
				}
			}

			LayoutSetPrototype layoutSetPrototype = null;

			try {
				long layoutSetPrototypeId = layoutSet.getLayoutSetPrototypeId();

				if (layoutSetPrototypeId != 0) {
					layoutSetPrototype =
						LayoutSetPrototypeLocalServiceUtil.
							getLayoutSetPrototype(
								layoutSet.getLayoutSetPrototypeId());
				}
			}
			catch (Exception e) {
				throw new SystemException(e);
			}

			if (layoutSetPrototype != null) {
				Date prototypeModifiedDate =
					layoutSetPrototype.getModifiedDate();

				long prototypeModifiedTime = prototypeModifiedDate.getTime();

				if (prototypeModifiedTime > maxLastMergedTime) {
					maxLastMergedTime = prototypeModifiedTime;
				}
			}

			if (maxLastMergedTime >= currentModifiedDate.getTime()) {
				currentModifiedDate = new Date(maxLastMergedTime + Time.SECOND);

				layoutSet.setModifiedDate(currentModifiedDate);
			}
		}
	}

}
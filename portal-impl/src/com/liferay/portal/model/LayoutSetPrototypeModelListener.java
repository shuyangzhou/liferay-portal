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
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.model.impl.LayoutSetPrototypeModelImpl;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portlet.sites.util.Sites;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Will Newbury
 */
public class LayoutSetPrototypeModelListener
	extends BaseModelListener<LayoutSetPrototype> {

	@Override
	public void onBeforeCreate(LayoutSetPrototype layoutSetPrototype)
		throws ModelListenerException {

		updateModifiedDate(layoutSetPrototype);

		super.onBeforeCreate(layoutSetPrototype);
	}

	@Override
	public void onBeforeUpdate(LayoutSetPrototype layoutSetPrototype)
		throws ModelListenerException {

		updateModifiedDate(layoutSetPrototype);
		super.onBeforeUpdate(layoutSetPrototype);
	}

	private void updateModifiedDate(LayoutSetPrototype layoutSetPrototype) {
		LayoutSetPrototypeModelImpl layoutSetPrototypeImpl =
			(LayoutSetPrototypeModelImpl)layoutSetPrototype;

		Date originalModifiedDate =
			layoutSetPrototypeImpl.getOriginalModifiedDate();

		Date currentModifiedDate = layoutSetPrototype.getModifiedDate();

		if ((currentModifiedDate != null) &&
			!currentModifiedDate.equals(originalModifiedDate)) {

			currentModifiedDate = DateUtil.getDBSafeDate(currentModifiedDate);

			List<LayoutSet> layoutSets = new ArrayList(
				LayoutSetLocalServiceUtil.getLayoutSetsByLayoutSetPrototypeUuid(
					layoutSetPrototype.getUuid()));

			LayoutSet privateLayoutSet = null;

			try {
				privateLayoutSet = layoutSetPrototype.getLayoutSet();
			}
			catch (PortalException e) {
				throw new SystemException(e);
			}

			if (privateLayoutSet != null) {
				layoutSets.add(privateLayoutSet);
			}

			long maxLastMergedTime = 0;

			for (LayoutSet layoutSet : layoutSets) {
				String lastMergedTimeString = layoutSet.getSettingsProperty(
					Sites.LAST_MERGE_TIME);

				if (lastMergedTimeString != null) {
					long lastMergedTime = GetterUtil.getLong(
						lastMergedTimeString);

					if (lastMergedTime > maxLastMergedTime) {
						maxLastMergedTime = lastMergedTime;
					}
				}
			}

			if (maxLastMergedTime >= currentModifiedDate.getTime()) {
				currentModifiedDate = new Date(maxLastMergedTime + Time.SECOND);

				layoutSetPrototype.setModifiedDate(currentModifiedDate);
			}
		}
	}

}
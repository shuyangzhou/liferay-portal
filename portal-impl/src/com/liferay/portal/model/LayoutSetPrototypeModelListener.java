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
import com.liferay.portal.model.impl.LayoutSetPrototypeModelImpl;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portlet.sites.util.Sites;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Will Newbury
 */
public class LayoutSetPrototypeModelListener
	extends BaseModelListener<LayoutSetPrototype> {

	@Override
	public void onBeforeUpdate(LayoutSetPrototype layoutSetPrototype)
		throws ModelListenerException {

		updateModifiedDate(layoutSetPrototype);
	}

	private void updateModifiedDate(LayoutSetPrototype layoutSetPrototype) {
		LayoutSetPrototypeModelImpl layoutSetPrototypeImpl =
			(LayoutSetPrototypeModelImpl)layoutSetPrototype;

		Date currentModifiedDate = layoutSetPrototype.getModifiedDate();

		if ((currentModifiedDate == null) ||
			currentModifiedDate.equals(
				layoutSetPrototypeImpl.getOriginalModifiedDate())) {

			return;
		}

		try {
			currentModifiedDate = DateUtil.getDBSafeDate(currentModifiedDate);

			List<LayoutSet> layoutSetList =
				LayoutSetLocalServiceUtil.getLayoutSetsByLayoutSetPrototypeUuid(
					layoutSetPrototype.getUuid());

			Set<LayoutSet> layoutSets = new HashSet(layoutSetList);

			LayoutSet privateLayoutSet = layoutSetPrototype.getLayoutSet();

			if (privateLayoutSet != null) {
				layoutSets.add(privateLayoutSet);
			}

			long maxLastMergeTime = 0;

			for (LayoutSet layoutSet : layoutSets) {
				String lastMergedTimeString = layoutSet.getSettingsProperty(
					Sites.LAST_MERGE_TIME);

				if (lastMergedTimeString != null) {
					long lastMergedTime = GetterUtil.getLong(
						lastMergedTimeString);

					if (lastMergedTime > maxLastMergeTime) {
						maxLastMergeTime = lastMergedTime;
					}
				}
			}

			if (maxLastMergeTime >= currentModifiedDate.getTime()) {
				currentModifiedDate = new Date(maxLastMergeTime + Time.SECOND);

				layoutSetPrototype.setModifiedDate(currentModifiedDate);
			}
		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}
	}

}
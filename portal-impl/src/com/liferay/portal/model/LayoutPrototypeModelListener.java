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
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.model.impl.LayoutPrototypeModelImpl;
import com.liferay.portal.service.LayoutLocalServiceUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Will Newbury
 */
public class LayoutPrototypeModelListener
	extends BaseModelListener<LayoutPrototype> {

	@Override
	public void onBeforeCreate(LayoutPrototype layoutPrototype)
		throws ModelListenerException {

		updateModifiedDate(layoutPrototype);
	}

	@Override
	public void onBeforeUpdate(LayoutPrototype layoutPrototype)
		throws ModelListenerException {

		updateModifiedDate(layoutPrototype);
	}

	private void updateModifiedDate(LayoutPrototype layoutPrototype) {
		LayoutPrototypeModelImpl layoutPrototypeModelImpl =
			(LayoutPrototypeModelImpl)layoutPrototype;

		Date originalModifiedDate =
			layoutPrototypeModelImpl.getOriginalModifiedDate();

		Date currentModifiedDate = layoutPrototype.getModifiedDate();

		if ((currentModifiedDate == null) || (originalModifiedDate == null) ||
			currentModifiedDate.equals(originalModifiedDate)) {

			return;
		}

		try {
			currentModifiedDate = DateUtil.getDBSafeDate(currentModifiedDate);

			List<Layout> layoutList =
				LayoutLocalServiceUtil.getLayoutsByLayoutPrototypeUuid(
					layoutPrototype.getUuid());

			Set<Layout> layouts = new HashSet(layoutList);

			Layout privateLayout = layoutPrototype.getLayout();

			if (privateLayout != null) {
				layouts.add(privateLayout);
			}

			long maxLastMergeTime = 0;

			MaxMergeTimeUtil.findMaxMergeTimeInLayouts(
				layouts, maxLastMergeTime);

			if (maxLastMergeTime >= currentModifiedDate.getTime()) {
				currentModifiedDate = new Date(maxLastMergeTime + Time.SECOND);

				layoutPrototype.setModifiedDate(currentModifiedDate);
			}
		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}
	}

}
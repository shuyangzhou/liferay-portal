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

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * @author Will Newbury
 */
public class LayoutPrototypeModelListener
	extends BaseModelListener<LayoutPrototype> {

	@Override
	public void onBeforeUpdate(LayoutPrototype layoutPrototype) {
		updateModifiedDate(layoutPrototype);
	}

	private void updateModifiedDate(LayoutPrototype layoutPrototype) {
		LayoutPrototypeModelImpl layoutPrototypeModelImpl =
			(LayoutPrototypeModelImpl)layoutPrototype;

		Date modifiedDate = layoutPrototype.getModifiedDate();

		if ((modifiedDate == null) ||
			modifiedDate.equals(
				layoutPrototypeModelImpl.getOriginalModifiedDate())) {

			return;
		}

		try {
			modifiedDate = DateUtil.getDBSafeDate(modifiedDate);

			Collection<Layout> layouts =
				LayoutLocalServiceUtil.getLayoutsByLayoutPrototypeUuid(
					layoutPrototype.getUuid());

			Layout privateLayout = layoutPrototype.getLayout();

			if (privateLayout != null) {
				layouts = new HashSet<Layout>(layouts);

				layouts.add(privateLayout);
			}

			long maxLastMergeTime = MaxMergeTimeUtil.findMaxMergeTimeInLayouts(
				layouts, 0);

			if (maxLastMergeTime >= modifiedDate.getTime()) {
				layoutPrototype.setModifiedDate(
					new Date(maxLastMergeTime + Time.SECOND));
			}
		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}
	}

}
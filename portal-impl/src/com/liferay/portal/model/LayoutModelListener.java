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
import com.liferay.portal.kernel.staging.LayoutStagingUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.impl.LayoutModelImpl;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.LayoutRevisionLocalServiceUtil;
import com.liferay.portal.servlet.filters.cache.CacheUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Chow
 * @author Raymond Augé
 * @author Will Newbury
 */
public class LayoutModelListener extends BaseModelListener<Layout> {

	@Override
	public void onAfterCreate(Layout layout) {
		clearCache(layout);
	}

	@Override
	public void onAfterRemove(Layout layout) {
		clearCache(layout);
	}

	@Override
	public void onAfterUpdate(Layout layout) {
		clearCache(layout);
	}

	@Override
	public void onBeforeCreate(Layout layout) throws ModelListenerException {
		updateModifiedDate(layout);
	}

	@Override
	public void onBeforeRemove(Layout layout) throws ModelListenerException {
		try {
			if (!LayoutStagingUtil.isBranchingLayout(layout)) {
				return;
			}

			LayoutRevisionLocalServiceUtil.deleteLayoutLayoutRevisions(
				layout.getPlid());
		}
		catch (IllegalStateException ise) {

			// This is only needed because of LayoutPersistenceTest but should
			// never happen in a deployed environment

		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}
		catch (SystemException se) {
			throw new ModelListenerException(se);
		}
	}

	@Override
	public void onBeforeUpdate(Layout layout) throws ModelListenerException {
		updateModifiedDate(layout);
	}

	protected void clearCache(Layout layout) {
		if (layout == null) {
			return;
		}

		if (!layout.isPrivateLayout()) {
			CacheUtil.clearCache(layout.getCompanyId());
		}
	}

	private void updateModifiedDate(Layout layout) {
		LayoutModelImpl layoutModelImpl = (LayoutModelImpl)layout;

		Date originalModifiedDate = layoutModelImpl.getOriginalModifiedDate();

		Date currentModifiedDate = layout.getModifiedDate();

		if ((currentModifiedDate == null) ||
			currentModifiedDate.equals(originalModifiedDate)) {

			return;
		}

		try {
			currentModifiedDate = DateUtil.getDBSafeDate(currentModifiedDate);

			LayoutPrototype layoutPrototype = null;

			String layoutPrototypeUuid = layout.getLayoutPrototypeUuid();

			if (!Validator.isNull(layoutPrototypeUuid)) {
				layoutPrototype =
					LayoutPrototypeLocalServiceUtil.
						getLayoutPrototypeByUuidAndCompanyId(
							layoutPrototypeUuid, layout.getCompanyId());
			}
			else {
				Group group = layout.getGroup();

				if (group.getClassNameId() ==
						ClassNameLocalServiceUtil.getClassNameId(
							LayoutPrototype.class)) {

					layoutPrototype =
						LayoutPrototypeLocalServiceUtil.getLayoutPrototype(
							group.getClassPK());
				}
			}

			long maxLastMergeTime = 0;
			Set<Layout> layouts = new HashSet();

			if (layoutPrototype != null) {
				Date prototypeModifiedDate = layoutPrototype.getModifiedDate();

				layouts.addAll(
					LayoutLocalServiceUtil.getLayoutsByLayoutPrototypeUuid(
						layoutPrototype.getUuid()));

				maxLastMergeTime = prototypeModifiedDate.getTime();
			}

			layouts.add(layout);

			maxLastMergeTime = MaxMergeTimeUtil.findMaxMergeTimeInLayouts(
				layouts, maxLastMergeTime);

			if (maxLastMergeTime >= currentModifiedDate.getTime()) {
				currentModifiedDate = new Date(maxLastMergeTime + Time.SECOND);

				layout.setModifiedDate(currentModifiedDate);
			}
		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}
	}

}
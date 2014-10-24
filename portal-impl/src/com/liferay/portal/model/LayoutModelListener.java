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
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.LayoutRevisionLocalServiceUtil;
import com.liferay.portal.servlet.filters.cache.CacheUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

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
	public void onBeforeCreate(Layout layout) {
		updateModifiedDate(layout);
	}

	@Override
	public void onBeforeRemove(Layout layout) {
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
	public void onBeforeUpdate(Layout layout) {
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

		Date modifiedDate = layout.getModifiedDate();

		if ((modifiedDate == null) ||
			modifiedDate.equals(layoutModelImpl.getOriginalModifiedDate())) {

			return;
		}

		try {
			modifiedDate = DateUtil.getDBSafeDate(modifiedDate);

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

			Collection<Layout> layouts = new ArrayList<Layout>();

			layouts.add(layout);

			if (layoutPrototype != null) {
				layouts = new HashSet<Layout>(layouts);

				layouts.addAll(
					LayoutLocalServiceUtil.getLayoutsByLayoutPrototypeUuid(
						layoutPrototype.getUuid()));

				Date layoutPrototypeModifiedDate =
					layoutPrototype.getModifiedDate();

				maxLastMergeTime = layoutPrototypeModifiedDate.getTime();
			}

			String sourcePrototypeLayoutUuid =
				layout.getSourcePrototypeLayoutUuid();

			LayoutSet layoutSet = layout.getLayoutSet();

			long LayoutSetModifiedDate = layoutSet.getModifiedDate().getTime();

			if (LayoutSetModifiedDate > maxLastMergeTime) {
				maxLastMergeTime = LayoutSetModifiedDate;
			}

			long layoutSetPrototypeId = layoutSet.getLayoutSetPrototypeId();

			if (layoutSetPrototypeId > 0) {
				Group layoutSetPrototypeGroup =
					GroupLocalServiceUtil.getLayoutSetPrototypeGroup(
						layout.getCompanyId(), layoutSetPrototypeId);

				Layout sourcePrototypeLayout =
					LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
						sourcePrototypeLayoutUuid,
						layoutSetPrototypeGroup.getGroupId(), true);

				if (sourcePrototypeLayout != null) {
					layouts.add(sourcePrototypeLayout);
				}
			}

			maxLastMergeTime = MaxMergeTimeUtil.findMaxMergeTimeInLayouts(
				layouts, maxLastMergeTime);

			if (maxLastMergeTime >= modifiedDate.getTime()) {
				layout.setModifiedDate(
					new Date(maxLastMergeTime + Time.SECOND));
			}
		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}
	}

}
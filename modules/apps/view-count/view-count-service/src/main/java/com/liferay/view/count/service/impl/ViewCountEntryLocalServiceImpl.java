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

package com.liferay.view.count.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.increment.BufferedIncrement;
import com.liferay.portal.kernel.increment.NumberIncrement;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.view.count.model.ViewCountEntry;
import com.liferay.view.count.service.base.ViewCountEntryLocalServiceBaseImpl;

import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Preston Crary
 */
@Component(
	property = "model.class.name=com.liferay.view.count.model.ViewCountEntry",
	service = AopService.class
)
public class ViewCountEntryLocalServiceImpl
	extends ViewCountEntryLocalServiceBaseImpl {

	@Override
	public ViewCountEntry addViewCountEntry(
		long companyId, long classNameId, long classPK) {

		long viewCountEntryId = counterLocalService.increment(
			ViewCountEntry.class.getName());

		ViewCountEntry viewCountEntry = viewCountEntryPersistence.create(
			viewCountEntryId);

		viewCountEntry.setCompanyId(companyId);
		viewCountEntry.setClassNameId(classNameId);
		viewCountEntry.setClassPK(classPK);

		return viewCountEntryPersistence.update(viewCountEntry);
	}

	@Override
	public long getViewCount(long companyId, long classNameId, long classPK) {
		ViewCountEntry viewCountEntry = viewCountEntryPersistence.fetchByC_C_C(
			companyId, classNameId, classPK);

		if (viewCountEntry == null) {
			return 0;
		}

		return viewCountEntry.getViewCount();
	}

	@Override
	public List<ViewCountEntry> getViewCountEntries(
		long companyId, long[] classNameIds, int start, int end,
		OrderByComparator<ViewCountEntry> obc) {

		return viewCountEntryPersistence.findByC_C(
			companyId, classNameIds, start, end, obc);
	}

	@Override
	public int getViewCountEntriesCount(long companyId, long[] classNameIds) {
		return viewCountEntryPersistence.countByC_C(companyId, classNameIds);
	}

	@Override
	@Transactional(enabled = false)
	public void incrementViewCount(
		long companyId, long classNameId, long classPK) {

		viewCountEntryLocalService.incrementViewCount(
			companyId, classNameId, classPK, 1);
	}

	@BufferedIncrement(incrementClass = NumberIncrement.class)
	@Override
	public void incrementViewCount(
		long companyId, long classNameId, long classPK, int increment) {

		viewCountEntryFinder.incrementViewCount(
			companyId, classNameId, classPK, increment);
	}

	@Override
	public void removeViewCount(long companyId, long classNameId, long classPK)
		throws PortalException {

		viewCountEntryPersistence.removeByC_C_C(
			companyId, classNameId, classPK);
	}

}
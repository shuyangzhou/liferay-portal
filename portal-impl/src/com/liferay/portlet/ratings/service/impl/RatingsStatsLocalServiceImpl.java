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

package com.liferay.portlet.ratings.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portlet.ratings.service.base.RatingsStatsLocalServiceBaseImpl;
import com.liferay.ratings.kernel.exception.NoSuchStatsException;
import com.liferay.ratings.kernel.model.RatingsStats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 */
public class RatingsStatsLocalServiceImpl
	extends RatingsStatsLocalServiceBaseImpl {

	@Override
	public RatingsStats addStats(long classNameId, long classPK) {
		long statsId = counterLocalService.increment();

		RatingsStats stats = ratingsStatsPersistence.create(statsId);

		stats.setClassNameId(classNameId);
		stats.setClassPK(classPK);
		stats.setTotalEntries(0);
		stats.setTotalScore(0.0);
		stats.setAverageScore(0.0);

		try {
			ratingsStatsPersistence.update(stats);
		}
		catch (SystemException se) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Add failed, fetch {classNameId=" + classNameId +
						", classPK=" + classPK + "}");
			}

			stats = ratingsStatsPersistence.fetchByC_C(
				classNameId, classPK, false);

			if (stats == null) {
				throw se;
			}
		}

		return stats;
	}

	@Override
	public void deleteStats(String className, long classPK) {
		long classNameId = classNameLocalService.getClassNameId(className);

		try {
			ratingsStatsPersistence.removeByC_C(classNameId, classPK);
		}
		catch (NoSuchStatsException nsse) {
			if (_log.isWarnEnabled()) {
				_log.warn(nsse);
			}
		}

		ratingsEntryPersistence.removeByC_C(classNameId, classPK);
	}

	@Override
	public RatingsStats fetchStats(String className, long classPK) {
		long classNameId = classNameLocalService.getClassNameId(className);

		RatingsStats stats = ratingsStatsPersistence.fetchByC_C(
			classNameId, classPK);

		return stats;
	}

	@Override
	public RatingsStats getStats(long statsId) throws PortalException {
		return ratingsStatsPersistence.findByPrimaryKey(statsId);
	}

	@Override
	public List<RatingsStats> getStats(String className, List<Long> classPKs) {
		long classNameId = classNameLocalService.getClassNameId(className);

		return ratingsStatsFinder.findByC_C(classNameId, classPKs);
	}

	@Override
	public RatingsStats getStats(String className, long classPK) {
		long classNameId = classNameLocalService.getClassNameId(className);

		RatingsStats stats = ratingsStatsPersistence.fetchByC_C(
			classNameId, classPK);

		if (stats == null) {
			stats = ratingsStatsLocalService.addStats(classNameId, classPK);
		}

		return stats;
	}

	@Override
	public Map<Long, RatingsStats> getStats(String className, long[] classPKs) {
		long classNameId = classNameLocalService.getClassNameId(className);

		List<RatingsStats> stats = ratingsStatsPersistence.findByC_C(
			classNameId, classPKs);

		Map<Long, RatingsStats> map = new HashMap<>();

		iterate:
		for (long classPK : classPKs) {
			for (RatingsStats ratingsStats : stats) {
				if (ratingsStats.getClassPK() == classPK) {
					map.put(classPK, ratingsStats);

					continue iterate;
				}
			}

			RatingsStats ratingsStats = ratingsStatsLocalService.addStats(
				classNameId, classPK);

			map.put(classPK, ratingsStats);
		}

		return map;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		RatingsStatsLocalServiceImpl.class);

}
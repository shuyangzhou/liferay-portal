/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.trash;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.trash.model.TrashEntry;
import com.liferay.portlet.trash.model.TrashVersion;
import com.liferay.portlet.trash.service.TrashEntryLocalServiceUtil;
import com.liferay.portlet.trash.service.TrashVersionLocalServiceUtil;
import com.liferay.portlet.trash.util.TrashUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author Zsolt Berentey
 */
public class TrashContext extends ServiceContext {

	public void addDependentStatuses(long trashEntryId) throws SystemException {
		HashMap<String, HashMap<Long, Integer>> dependentStatusMaps =
			(HashMap<String, HashMap<Long, Integer>>)getAttribute(
				TrashConstants.TRASH_DEPENDENT_STATUS_MAP);

		if (dependentStatusMaps == null) {
			dependentStatusMaps = new HashMap<String, HashMap<Long, Integer>>();

			setAttribute(
				TrashConstants.TRASH_DEPENDENT_STATUS_MAP, dependentStatusMaps);
		}

		List<TrashVersion> trashVersions =
			TrashVersionLocalServiceUtil.getVersions(trashEntryId);

		for (TrashVersion trashVersion : trashVersions) {
			HashMap<Long, Integer> dependentStatusMap = dependentStatusMaps.get(
				trashVersion.getClassName());

			if (dependentStatusMap == null) {
				dependentStatusMap = new HashMap<Long, Integer>();

				dependentStatusMaps.put(
					trashVersion.getClassName(), dependentStatusMap);
			}

			dependentStatusMap.put(
				trashVersion.getClassPK(), trashVersion.getStatus());
		}
	}

	public void addDependentStatuses(long trashEntryId, String className)
		throws SystemException {

		HashMap<String, HashMap<Long, Integer>> dependentStatusMaps =
			(HashMap<String, HashMap<Long, Integer>>)getAttribute(
				TrashConstants.TRASH_DEPENDENT_STATUS_MAP);

		if (dependentStatusMaps == null) {
			dependentStatusMaps = new HashMap<String, HashMap<Long, Integer>>();

			setAttribute(
				TrashConstants.TRASH_DEPENDENT_STATUS_MAP, dependentStatusMaps);
		}

		HashMap<Long, Integer> dependentStatusMap =
			TrashUtil.getDependentStatuses(trashEntryId, className);

		dependentStatusMaps.put(className, dependentStatusMap);
	}

	public void addTrashEntries(long groupId, String className)
		throws SystemException {

		HashSet<Long> trashEntryClassPKs = new HashSet<Long>();

		List<TrashEntry> trashEntries = TrashEntryLocalServiceUtil.getEntries(
			groupId, className);

		if (trashEntries.isEmpty()) {
			return;
		}

		for (TrashEntry trashEntry : trashEntries) {
			trashEntryClassPKs.add(trashEntry.getClassPK());
		}

		HashMap<String, HashSet<Long>> trashEntryClassPKMap =
			(HashMap<String, HashSet<Long>>)getAttribute(
				TrashConstants.TRASH_ENTRY_CLASS_PK_MAP);

		if (trashEntryClassPKMap == null) {
			trashEntryClassPKMap = new HashMap<String, HashSet<Long>>();

			setAttribute(
				TrashConstants.TRASH_ENTRY_CLASS_PK_MAP, trashEntryClassPKMap);
		}

		trashEntryClassPKMap.put(className, trashEntryClassPKs);
	}

	public int getDependentStatus(String className, long classPK) {
		HashMap<String, HashMap<Long, Integer>> dependentStatusMaps =
			(HashMap<String, HashMap<Long, Integer>>)getAttribute(
				TrashConstants.TRASH_DEPENDENT_STATUS_MAP);

		if (dependentStatusMaps == null) {
			return WorkflowConstants.STATUS_APPROVED;
		}

		HashMap<Long, Integer> dependentStatusMap = dependentStatusMaps.get(
			className);

		if (dependentStatusMap == null) {
			return WorkflowConstants.STATUS_APPROVED;
		}

		Integer status = dependentStatusMap.get(classPK);

		if (status == null) {
			return WorkflowConstants.STATUS_APPROVED;
		}

		return status;
	}

	public boolean hasTrashEntry(String className, long classPK) {
		HashMap<String, HashSet<Long>> trashEntryClassPKMap =
			(HashMap<String, HashSet<Long>>)getAttribute(
				TrashConstants.TRASH_ENTRY_CLASS_PK_MAP);

		if (trashEntryClassPKMap == null) {
			return false;
		}

		HashSet<Long> trashEntryClassPKs = trashEntryClassPKMap.get(className);

		if (trashEntryClassPKs == null) {
			trashEntryClassPKs = new HashSet<Long>();

			trashEntryClassPKMap.put(className, trashEntryClassPKs);
		}

		return trashEntryClassPKs.contains(classPK);
	}

}
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
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.trash.model.TrashEntry;
import com.liferay.portlet.trash.model.TrashVersion;
import com.liferay.portlet.trash.service.TrashEntryLocalServiceUtil;
import com.liferay.portlet.trash.service.TrashVersionLocalServiceUtil;
import com.liferay.portlet.trash.util.TrashUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Zsolt Berentey
 */
public class TrashContext extends ServiceContext {

	public boolean containsEntityId(String className, long entityId) {
		Set<Long> entityIds = getEntityIds(className);

		return entityIds.contains(entityId);
	}

	public int getDependentStatus(String className, long classPK) {
		Map<Long, Integer> dependentStatuses = getDependentStatuses(className);

		Integer status = dependentStatuses.get(classPK);

		if (status == null) {
			return WorkflowConstants.STATUS_APPROVED;
		}

		return status;
	}

	/**
	 * Returns a map of trashed entity statuses for the given className. The
	 * keys of the map are usually primary keys of child entities that were
	 * moved to the trash as a result of trashing one of their ancestors. If the
	 * map does not exist, it will be created.
	 *
	 * @param  className the class name
	 * @return the map of trashed entity statuses
	 */
	public Map<Long, Integer> getDependentStatuses(String className) {
		HashMap<String, HashMap<Long, Integer>> dependentStatusMaps =
			(HashMap<String, HashMap<Long, Integer>>)getAttribute(
				TrashConstants.TRASH_DEPENDENT_STATUS_MAP);

		if (dependentStatusMaps == null) {
			dependentStatusMaps = new HashMap<String, HashMap<Long, Integer>>();

			setAttribute(
				TrashConstants.TRASH_DEPENDENT_STATUS_MAP, dependentStatusMaps);
		}

		HashMap<Long, Integer> dependentStatuses = dependentStatusMaps.get(
			className);

		if (dependentStatuses == null) {
			dependentStatuses = new HashMap<Long, Integer>();

			dependentStatusMaps.put(className, dependentStatuses);
		}

		return dependentStatuses;
	}

	/**
	 * Returns a set of trashed entity IDs for the given className. If the set
	 * does not exist, it will be created.
	 *
	 * @param  className the class name
	 * @return the set of trashed entity IDs
	 */
	public Set<Long> getEntityIds(String className) {
		HashMap<String, HashSet<Long>> entityIdsMap =
			(HashMap<String, HashSet<Long>>)getAttribute(
				TrashConstants.TRASH_ENTITY_IDS_MAP);

		if (entityIdsMap == null) {
			entityIdsMap = new HashMap<String, HashSet<Long>>();

			setAttribute(TrashConstants.TRASH_ENTITY_IDS_MAP, entityIdsMap);
		}

		HashSet<Long> entityIds = entityIdsMap.get(className);

		if (entityIds == null) {
			entityIds = new HashSet<Long>();

			entityIdsMap.put(className, entityIds);
		}

		return entityIds;
	}

	public void setDependentStatuses(long trashEntryId) throws SystemException {
		List<TrashVersion> trashVersions =
			TrashVersionLocalServiceUtil.getVersions(trashEntryId);

		for (TrashVersion trashVersion : trashVersions) {
			Map<Long, Integer> dependentStatuses = getDependentStatuses(
				trashVersion.getClassName());

			dependentStatuses.put(
				trashVersion.getClassPK(), trashVersion.getStatus());
		}
	}

	public void setDependentStatuses(long trashEntryId, String className)
		throws SystemException {

		HashMap<Long, Integer> dependentStatuses =
			TrashUtil.getDependentStatuses(
				trashEntryId, MBMessage.class.getName());

		setDependentStatuses(className, dependentStatuses);
	}

	/**
	 * Stores a map of trashed entity statuses for the given class name. The
	 * keys of the map are usually primary keys of child entities that were
	 * moved to the trash as a result of trashing one of their ancestors. If the
	 * map does not exist, it will be created.
	 *
	 * @param className the class name
	 */
	public void setDependentStatuses(
		String className, HashMap<Long, Integer> dependentStatuses) {

		if (dependentStatuses == null) {
			return;
		}

		HashMap<String, HashMap<Long, Integer>> dependentStatusMaps =
			(HashMap<String, HashMap<Long, Integer>>)getAttribute(
				TrashConstants.TRASH_DEPENDENT_STATUS_MAP);

		if (dependentStatusMaps == null) {
			dependentStatusMaps = new HashMap<String, HashMap<Long, Integer>>();

			setAttribute(
				TrashConstants.TRASH_DEPENDENT_STATUS_MAP, dependentStatusMaps);
		}

		dependentStatusMaps.put(className, dependentStatuses);
	}

	public void setEntityIds(long groupId, String className)
		throws SystemException {

		HashSet<Long> entityIds = new HashSet<Long>();

		List<TrashEntry> trashEntries = TrashEntryLocalServiceUtil.getEntries(
			groupId, className);

		for (TrashEntry trashEntry : trashEntries) {
			entityIds.add(trashEntry.getClassPK());
		}

		setEntityIds(className, entityIds);
	}

	/**
	 * Stores a set of trashed entity IDs for the given className.
	 *
	 * @param className the class name
	 */
	public void setEntityIds(String className, HashSet<Long> entityIds) {
		if (entityIds == null) {
			return;
		}

		HashMap<String, HashSet<Long>> entityIdsMap =
			(HashMap<String, HashSet<Long>>)getAttribute(
				TrashConstants.TRASH_ENTITY_IDS_MAP);

		if (entityIdsMap == null) {
			entityIdsMap = new HashMap<String, HashSet<Long>>();

			setAttribute(TrashConstants.TRASH_ENTITY_IDS_MAP, entityIdsMap);
		}

		entityIdsMap.put(className, entityIds);
	}

}
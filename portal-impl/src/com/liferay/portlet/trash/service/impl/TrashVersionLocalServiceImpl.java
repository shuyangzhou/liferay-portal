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

package com.liferay.portlet.trash.service.impl;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.trash.model.TrashVersion;
import com.liferay.portlet.trash.service.base.TrashVersionLocalServiceBaseImpl;

import java.util.List;

/**
 * @author Zsolt Berentey
 */
public class TrashVersionLocalServiceImpl
	extends TrashVersionLocalServiceBaseImpl {

	/**
	 * Adds a trash version for a trash entry. This method is used to store the
	 * original statuses for dependent entities when their parent is moved to
	 * the trash.
	 *
	 * @param  trashEntryId the primary key of the trash entry associated with
	 *         the parent entity
	 * @param  className the class name of the dependent entity
	 * @param  classPK the primary key of the dependent entity
	 * @param  status the status of the dependent entity prior to being moved to
	 *         trash
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void addTrashVersion(
			long trashEntryId, String className, long classPK, int status)
		throws SystemException {

		long versionId = counterLocalService.increment();

		TrashVersion trashVersion = trashVersionPersistence.create(versionId);

		trashVersion.setEntryId(trashEntryId);
		trashVersion.setClassName(className);
		trashVersion.setClassPK(classPK);
		trashVersion.setStatus(status);

		trashVersionPersistence.update(trashVersion);
	}

	/**
	 * Returns the trash version for a trashed child entity identified by the
	 * given class name and primary key that belongs to the trash entry
	 *
	 * @param  entryId the primary key of the trash entry
	 * @param  className the class name of the trash version
	 * @param  classPK the primary key of the trash version
	 * @return the trash version
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public TrashVersion fetchVersion(
			long entryId, String className, long classPK)
		throws SystemException {

		long classNameId = PortalUtil.getClassNameId(className);

		TrashVersion version = trashVersionPersistence.fetchByE_C_C(
			entryId, classNameId, classPK);

		return version;
	}

	/**
	 * Returns all the trash versions associated with the trash entry.
	 *
	 * @param  entryId the primary key of the trash entry
	 * @return all the trash versions associated with the trash entry
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<TrashVersion> getVersions(long entryId) throws SystemException {
		return trashVersionPersistence.findByEntryId(entryId);
	}

	/**
	 * Returns all the trash versions associated with the trash entry filtered
	 * by the given class name.
	 *
	 * @param  entryId the primary key of the trash entry
	 * @param  className the class name of the trash version
	 * @return all the trash versions associated with the trash entry
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<TrashVersion> getVersions(long entryId, String className)
		throws SystemException {

		if (Validator.isNull(className)) {
			return trashVersionPersistence.findByEntryId(entryId);
		}
		else {
			long classNameId = PortalUtil.getClassNameId(className);

			return trashVersionPersistence.findByE_C(entryId, classNameId);
		}
	}

	/**
	 * Returns all the trash versions associated with the trash entry.
	 *
	 * @param  className the class name of the trash entity
	 * @param  classPK the primary key of the trash entity
	 * @return all the trash versions associated with the trash entry
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<TrashVersion> getVersions(String className, long classPK)
		throws SystemException {

		long classNameId = PortalUtil.getClassNameId(className);

		return trashVersionPersistence.findByC_C(classNameId, classPK);
	}

}
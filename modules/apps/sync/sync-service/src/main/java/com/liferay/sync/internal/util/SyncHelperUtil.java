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

package com.liferay.sync.internal.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.sync.model.SyncDLObject;
import com.liferay.sync.util.SyncHelper;

import java.io.File;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Tambara
 */
@Component(immediate = true)
public class SyncHelperUtil {

	public static void addChecksum(
		long modifiedTime, long typePK, String checksum) {

		_syncHelper.addChecksum(modifiedTime, typePK, checksum);
	}

	public static String buildExceptionMessage(Throwable throwable) {
		return _syncHelper.buildExceptionMessage(throwable);
	}

	public static void checkSyncEnabled(long groupId) throws PortalException {
		_syncHelper.checkSyncEnabled(groupId);
	}

	public static String getLanTokenKey(
		long modifiedTime, long typePK, boolean addToMap) {

		return _syncHelper.getLanTokenKey(modifiedTime, typePK, addToMap);
	}

	public static boolean isSupportedFolder(Folder folder) {
		return _syncHelper.isSupportedFolder(folder);
	}

	public static boolean isSyncEnabled(Group group) {
		return _syncHelper.isSyncEnabled(group);
	}

	public static void patchFile(
			File originalFile, File deltaFile, File patchedFile)
		throws PortalException {

		_syncHelper.patchFile(originalFile, deltaFile, patchedFile);
	}

	public static void setFilePermissions(
		Group group, boolean folder, ServiceContext serviceContext) {

		_syncHelper.setFilePermissions(group, folder, serviceContext);
	}

	public static SyncDLObject toSyncDLObject(FileEntry fileEntry, String event)
		throws PortalException {

		return _syncHelper.toSyncDLObject(fileEntry, event);
	}

	public static SyncDLObject toSyncDLObject(Folder folder, String event)
		throws PortalException {

		return _syncHelper.toSyncDLObject(folder, event);
	}

	@Reference(unbind = "-")
	protected void setSyncHelper(SyncHelper syncHelper) {
		_syncHelper = syncHelper;
	}

	private static SyncHelper _syncHelper;

}
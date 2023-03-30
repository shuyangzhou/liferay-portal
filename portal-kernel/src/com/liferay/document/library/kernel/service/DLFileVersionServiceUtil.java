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

package com.liferay.document.library.kernel.service;

import com.liferay.document.library.kernel.model.DLFileVersion;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for DLFileVersion. This utility wraps
 * <code>com.liferay.portlet.documentlibrary.service.impl.DLFileVersionServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see DLFileVersionService
 * @generated
 */
public class DLFileVersionServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portlet.documentlibrary.service.impl.DLFileVersionServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static DLFileVersion getFileVersion(long fileVersionId)
		throws PortalException {

		return getService().getFileVersion(fileVersionId);
	}

	public static List<DLFileVersion> getFileVersions(
			long fileEntryId, int status)
		throws PortalException {

		return getService().getFileVersions(fileEntryId, status);
	}

	public static int getFileVersionsCount(long fileEntryId, int status)
		throws PortalException {

		return getService().getFileVersionsCount(fileEntryId, status);
	}

	public static DLFileVersion getLatestFileVersion(long fileEntryId)
		throws PortalException {

		return getService().getLatestFileVersion(fileEntryId);
	}

	public static DLFileVersion getLatestFileVersion(
			long fileEntryId, boolean excludeWorkingCopy)
		throws PortalException {

		return getService().getLatestFileVersion(
			fileEntryId, excludeWorkingCopy);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static DLFileVersionService getService() {
		return _serviceDCLSingleton.getSingleton(
			DLFileVersionServiceUtil::_getService);
	}

	private static DLFileVersionService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(DLFileVersionServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<DLFileVersionService> serviceReference =
			bundleContext.getServiceReference(DLFileVersionService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<DLFileVersionService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
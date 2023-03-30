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

package com.liferay.dynamic.data.lists.service;

import com.liferay.dynamic.data.lists.model.DDLRecordSetVersion;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for DDLRecordSetVersion. This utility wraps
 * <code>com.liferay.dynamic.data.lists.service.impl.DDLRecordSetVersionServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see DDLRecordSetVersionService
 * @generated
 */
public class DDLRecordSetVersionServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.dynamic.data.lists.service.impl.DDLRecordSetVersionServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static DDLRecordSetVersion getLatestRecordSetVersion(
			long recordSetId)
		throws PortalException {

		return getService().getLatestRecordSetVersion(recordSetId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static DDLRecordSetVersion getRecordSetVersion(
			long recordSetVersionId)
		throws PortalException {

		return getService().getRecordSetVersion(recordSetVersionId);
	}

	public static List<DDLRecordSetVersion> getRecordSetVersions(
			long recordSetId, int start, int end,
			OrderByComparator<DDLRecordSetVersion> orderByComparator)
		throws PortalException {

		return getService().getRecordSetVersions(
			recordSetId, start, end, orderByComparator);
	}

	public static int getRecordSetVersionsCount(long recordSetId)
		throws PortalException {

		return getService().getRecordSetVersionsCount(recordSetId);
	}

	public static DDLRecordSetVersionService getService() {
		return _serviceDCLSingleton.getSingleton(
			DDLRecordSetVersionServiceUtil::_getService);
	}

	private static DDLRecordSetVersionService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			DDLRecordSetVersionServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<DDLRecordSetVersionService> serviceReference =
			bundleContext.getServiceReference(DDLRecordSetVersionService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<DDLRecordSetVersionService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
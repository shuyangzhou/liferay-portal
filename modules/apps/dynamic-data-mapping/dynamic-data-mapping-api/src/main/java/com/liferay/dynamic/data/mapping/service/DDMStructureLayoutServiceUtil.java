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

package com.liferay.dynamic.data.mapping.service;

import com.liferay.dynamic.data.mapping.model.DDMStructureLayout;
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
 * Provides the remote service utility for DDMStructureLayout. This utility wraps
 * <code>com.liferay.dynamic.data.mapping.service.impl.DDMStructureLayoutServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see DDMStructureLayoutService
 * @generated
 */
public class DDMStructureLayoutServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.dynamic.data.mapping.service.impl.DDMStructureLayoutServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static List<DDMStructureLayout> getStructureLayouts(
			long groupId, int start, int end)
		throws PortalException {

		return getService().getStructureLayouts(groupId, start, end);
	}

	public static int getStructureLayoutsCount(long groupId) {
		return getService().getStructureLayoutsCount(groupId);
	}

	public static List<DDMStructureLayout> search(
			long companyId, long[] groupIds, long classNameId, String keywords,
			int start, int end,
			OrderByComparator<DDMStructureLayout> orderByComparator)
		throws PortalException {

		return getService().search(
			companyId, groupIds, classNameId, keywords, start, end,
			orderByComparator);
	}

	public static DDMStructureLayoutService getService() {
		return _serviceDCLSingleton.getSingleton(
			DDMStructureLayoutServiceUtil::_getService);
	}

	private static DDMStructureLayoutService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			DDMStructureLayoutServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<DDMStructureLayoutService> serviceReference =
			bundleContext.getServiceReference(DDMStructureLayoutService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<DDMStructureLayoutService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
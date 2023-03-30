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

package com.liferay.blogs.service;

import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the local service utility for BlogsStatsUser. This utility wraps
 * <code>com.liferay.blogs.service.impl.BlogsStatsUserLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see BlogsStatsUserLocalService
 * @generated
 */
public class BlogsStatsUserLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.blogs.service.impl.BlogsStatsUserLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static List<com.liferay.blogs.model.BlogsStatsUser>
		getGroupsStatsUsers(long companyId, long groupId, int start, int end) {

		return getService().getGroupsStatsUsers(companyId, groupId, start, end);
	}

	public static List<com.liferay.blogs.model.BlogsStatsUser>
		getGroupStatsUsers(long groupId, int start, int end) {

		return getService().getGroupStatsUsers(groupId, start, end);
	}

	public static List<com.liferay.blogs.model.BlogsStatsUser>
		getOrganizationStatsUsers(long organizationId, int start, int end) {

		return getService().getOrganizationStatsUsers(
			organizationId, start, end);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.blogs.model.BlogsStatsUser getStatsUser(
			long groupId, long userId)
		throws PortalException {

		return getService().getStatsUser(groupId, userId);
	}

	public static BlogsStatsUserLocalService getService() {
		return _serviceDCLSingleton.getSingleton(
			BlogsStatsUserLocalServiceUtil::_getService);
	}

	private static BlogsStatsUserLocalService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			BlogsStatsUserLocalServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<BlogsStatsUserLocalService> serviceReference =
			bundleContext.getServiceReference(BlogsStatsUserLocalService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<BlogsStatsUserLocalService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
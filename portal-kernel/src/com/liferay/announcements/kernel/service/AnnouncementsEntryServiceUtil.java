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

package com.liferay.announcements.kernel.service;

import com.liferay.announcements.kernel.model.AnnouncementsEntry;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for AnnouncementsEntry. This utility wraps
 * <code>com.liferay.portlet.announcements.service.impl.AnnouncementsEntryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see AnnouncementsEntryService
 * @generated
 */
public class AnnouncementsEntryServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portlet.announcements.service.impl.AnnouncementsEntryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static AnnouncementsEntry addEntry(
			long classNameId, long classPK, String title, String content,
			String url, String type, java.util.Date displayDate,
			java.util.Date expirationDate, int priority, boolean alert)
		throws PortalException {

		return getService().addEntry(
			classNameId, classPK, title, content, url, type, displayDate,
			expirationDate, priority, alert);
	}

	public static void deleteEntry(long entryId) throws PortalException {
		getService().deleteEntry(entryId);
	}

	public static AnnouncementsEntry getEntry(long entryId)
		throws PortalException {

		return getService().getEntry(entryId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static AnnouncementsEntry updateEntry(
			long entryId, String title, String content, String url, String type,
			java.util.Date displayDate, java.util.Date expirationDate,
			int priority)
		throws PortalException {

		return getService().updateEntry(
			entryId, title, content, url, type, displayDate, expirationDate,
			priority);
	}

	public static AnnouncementsEntryService getService() {
		return _serviceDCLSingleton.getSingleton(
			AnnouncementsEntryServiceUtil::_getService);
	}

	private static AnnouncementsEntryService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			AnnouncementsEntryServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<AnnouncementsEntryService> serviceReference =
			bundleContext.getServiceReference(AnnouncementsEntryService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<AnnouncementsEntryService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
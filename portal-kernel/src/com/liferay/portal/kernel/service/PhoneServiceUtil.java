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

package com.liferay.portal.kernel.service;

import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Phone;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for Phone. This utility wraps
 * <code>com.liferay.portal.service.impl.PhoneServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see PhoneService
 * @generated
 */
public class PhoneServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.service.impl.PhoneServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static Phone addPhone(
			String className, long classPK, String number, String extension,
			long typeId, boolean primary, ServiceContext serviceContext)
		throws PortalException {

		return getService().addPhone(
			className, classPK, number, extension, typeId, primary,
			serviceContext);
	}

	public static void deletePhone(long phoneId) throws PortalException {
		getService().deletePhone(phoneId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static Phone getPhone(long phoneId) throws PortalException {
		return getService().getPhone(phoneId);
	}

	public static List<Phone> getPhones(String className, long classPK)
		throws PortalException {

		return getService().getPhones(className, classPK);
	}

	public static Phone updatePhone(
			long phoneId, String number, String extension, long typeId,
			boolean primary)
		throws PortalException {

		return getService().updatePhone(
			phoneId, number, extension, typeId, primary);
	}

	public static PhoneService getService() {
		return _serviceDCLSingleton.getSingleton(PhoneServiceUtil::_getService);
	}

	private static PhoneService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(PhoneServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<PhoneService> serviceReference =
			bundleContext.getServiceReference(PhoneService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<PhoneService> _serviceDCLSingleton =
		new DCLSingleton<>();

}
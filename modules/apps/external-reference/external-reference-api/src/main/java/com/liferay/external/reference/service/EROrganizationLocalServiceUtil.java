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

package com.liferay.external.reference.service;

import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the local service utility for EROrganization. This utility wraps
 * <code>com.liferay.external.reference.service.impl.EROrganizationLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see EROrganizationLocalService
 * @generated
 */
public class EROrganizationLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.external.reference.service.impl.EROrganizationLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portal.kernel.model.Organization
			addOrUpdateOrganization(
				String externalReferenceCode, long userId,
				long parentOrganizationId, String name, String type,
				long regionId, long countryId, long statusId, String comments,
				boolean site, boolean hasLogo, byte[] logoBytes,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addOrUpdateOrganization(
			externalReferenceCode, userId, parentOrganizationId, name, type,
			regionId, countryId, statusId, comments, site, hasLogo, logoBytes,
			serviceContext);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static EROrganizationLocalService getService() {
		return _serviceDCLSingleton.getSingleton(
			EROrganizationLocalServiceUtil::_getService);
	}

	private static EROrganizationLocalService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			EROrganizationLocalServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<EROrganizationLocalService> serviceReference =
			bundleContext.getServiceReference(EROrganizationLocalService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<EROrganizationLocalService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
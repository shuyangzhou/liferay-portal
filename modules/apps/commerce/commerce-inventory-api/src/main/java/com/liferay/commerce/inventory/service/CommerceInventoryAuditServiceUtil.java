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

package com.liferay.commerce.inventory.service;

import com.liferay.commerce.inventory.model.CommerceInventoryAudit;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for CommerceInventoryAudit. This utility wraps
 * <code>com.liferay.commerce.inventory.service.impl.CommerceInventoryAuditServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Luca Pellizzon
 * @see CommerceInventoryAuditService
 * @generated
 */
public class CommerceInventoryAuditServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.inventory.service.impl.CommerceInventoryAuditServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static List<CommerceInventoryAudit> getCommerceInventoryAudits(
			long companyId, String sku, int start, int end)
		throws PortalException {

		return getService().getCommerceInventoryAudits(
			companyId, sku, start, end);
	}

	public static int getCommerceInventoryAuditsCount(
			long companyId, String sku)
		throws PortalException {

		return getService().getCommerceInventoryAuditsCount(companyId, sku);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static CommerceInventoryAuditService getService() {
		return _serviceDCLSingleton.getSingleton(
			CommerceInventoryAuditServiceUtil::_getService);
	}

	private static CommerceInventoryAuditService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			CommerceInventoryAuditServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<CommerceInventoryAuditService> serviceReference =
			bundleContext.getServiceReference(
				CommerceInventoryAuditService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<CommerceInventoryAuditService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
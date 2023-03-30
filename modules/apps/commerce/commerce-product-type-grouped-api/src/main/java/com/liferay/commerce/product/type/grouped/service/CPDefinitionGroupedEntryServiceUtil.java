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

package com.liferay.commerce.product.type.grouped.service;

import com.liferay.commerce.product.type.grouped.model.CPDefinitionGroupedEntry;
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
 * Provides the remote service utility for CPDefinitionGroupedEntry. This utility wraps
 * <code>com.liferay.commerce.product.type.grouped.service.impl.CPDefinitionGroupedEntryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Andrea Di Giorgi
 * @see CPDefinitionGroupedEntryService
 * @generated
 */
public class CPDefinitionGroupedEntryServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.product.type.grouped.service.impl.CPDefinitionGroupedEntryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static void addCPDefinitionGroupedEntries(
			long cpDefinitionId, long[] entryCPDefinitionIds,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		getService().addCPDefinitionGroupedEntries(
			cpDefinitionId, entryCPDefinitionIds, serviceContext);
	}

	public static CPDefinitionGroupedEntry addCPDefinitionGroupedEntry(
			long cpDefinitionId, long entryCProductId, double priority,
			int quantity,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addCPDefinitionGroupedEntry(
			cpDefinitionId, entryCProductId, priority, quantity,
			serviceContext);
	}

	public static CPDefinitionGroupedEntry deleteCPDefinitionGroupedEntry(
			long cpDefinitionGroupedEntryId)
		throws PortalException {

		return getService().deleteCPDefinitionGroupedEntry(
			cpDefinitionGroupedEntryId);
	}

	public static List<CPDefinitionGroupedEntry> getCPDefinitionGroupedEntries(
			long cpDefinitionId, int start, int end,
			OrderByComparator<CPDefinitionGroupedEntry> orderByComparator)
		throws PortalException {

		return getService().getCPDefinitionGroupedEntries(
			cpDefinitionId, start, end, orderByComparator);
	}

	public static int getCPDefinitionGroupedEntriesCount(long cpDefinitionId)
		throws PortalException {

		return getService().getCPDefinitionGroupedEntriesCount(cpDefinitionId);
	}

	public static CPDefinitionGroupedEntry getCPDefinitionGroupedEntry(
			long cpDefinitionGroupedEntryId)
		throws PortalException {

		return getService().getCPDefinitionGroupedEntry(
			cpDefinitionGroupedEntryId);
	}

	public static List<CPDefinitionGroupedEntry>
			getEntryCProductCPDefinitionGroupedEntries(
				long entryCProductId, int start, int end,
				OrderByComparator<CPDefinitionGroupedEntry> orderByComparator)
		throws PortalException {

		return getService().getEntryCProductCPDefinitionGroupedEntries(
			entryCProductId, start, end, orderByComparator);
	}

	public static int getEntryCProductCPDefinitionGroupedEntriesCount(
			long entryCProductId)
		throws PortalException {

		return getService().getEntryCProductCPDefinitionGroupedEntriesCount(
			entryCProductId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static CPDefinitionGroupedEntry updateCPDefinitionGroupedEntry(
			long cpDefinitionGroupedEntryId, double priority, int quantity)
		throws PortalException {

		return getService().updateCPDefinitionGroupedEntry(
			cpDefinitionGroupedEntryId, priority, quantity);
	}

	public static CPDefinitionGroupedEntryService getService() {
		return _serviceDCLSingleton.getSingleton(
			CPDefinitionGroupedEntryServiceUtil::_getService);
	}

	private static CPDefinitionGroupedEntryService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			CPDefinitionGroupedEntryServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<CPDefinitionGroupedEntryService> serviceReference =
			bundleContext.getServiceReference(
				CPDefinitionGroupedEntryService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<CPDefinitionGroupedEntryService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
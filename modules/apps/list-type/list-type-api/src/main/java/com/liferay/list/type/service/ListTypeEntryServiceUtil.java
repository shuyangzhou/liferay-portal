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

package com.liferay.list.type.service;

import com.liferay.list.type.model.ListTypeEntry;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for ListTypeEntry. This utility wraps
 * <code>com.liferay.list.type.service.impl.ListTypeEntryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Gabriel Albuquerque
 * @see ListTypeEntryService
 * @generated
 */
public class ListTypeEntryServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.list.type.service.impl.ListTypeEntryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static ListTypeEntry addListTypeEntry(
			String externalReferenceCode, long listTypeDefinitionId, String key,
			Map<java.util.Locale, String> nameMap)
		throws PortalException {

		return getService().addListTypeEntry(
			externalReferenceCode, listTypeDefinitionId, key, nameMap);
	}

	public static ListTypeEntry deleteListTypeEntry(long listTypeEntryId)
		throws PortalException {

		return getService().deleteListTypeEntry(listTypeEntryId);
	}

	public static List<ListTypeEntry> getListTypeEntries(
			long listTypeDefinitionId, int start, int end)
		throws PortalException {

		return getService().getListTypeEntries(
			listTypeDefinitionId, start, end);
	}

	public static int getListTypeEntriesCount(long listTypeDefinitionId)
		throws PortalException {

		return getService().getListTypeEntriesCount(listTypeDefinitionId);
	}

	public static ListTypeEntry getListTypeEntry(long listTypeEntryId)
		throws PortalException {

		return getService().getListTypeEntry(listTypeEntryId);
	}

	public static ListTypeEntry getListTypeEntryByExternalReferenceCode(
			String externalReferenceCode, long companyId,
			long listTypeDefinitionId)
		throws PortalException {

		return getService().getListTypeEntryByExternalReferenceCode(
			externalReferenceCode, companyId, listTypeDefinitionId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static ListTypeEntry updateListTypeEntry(
			String externalReferenceCode, long listTypeEntryId,
			Map<java.util.Locale, String> nameMap)
		throws PortalException {

		return getService().updateListTypeEntry(
			externalReferenceCode, listTypeEntryId, nameMap);
	}

	public static ListTypeEntryService getService() {
		return _serviceDCLSingleton.getSingleton(
			ListTypeEntryServiceUtil::_getService);
	}

	private static ListTypeEntryService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(ListTypeEntryServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<ListTypeEntryService> serviceReference =
			bundleContext.getServiceReference(ListTypeEntryService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<ListTypeEntryService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
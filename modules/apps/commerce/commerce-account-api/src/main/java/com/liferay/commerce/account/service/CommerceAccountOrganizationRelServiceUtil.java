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

package com.liferay.commerce.account.service;

import com.liferay.commerce.account.model.CommerceAccountOrganizationRel;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for CommerceAccountOrganizationRel. This utility wraps
 * <code>com.liferay.commerce.account.service.impl.CommerceAccountOrganizationRelServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Marco Leo
 * @see CommerceAccountOrganizationRelService
 * @generated
 */
public class CommerceAccountOrganizationRelServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.account.service.impl.CommerceAccountOrganizationRelServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static CommerceAccountOrganizationRel
			addCommerceAccountOrganizationRel(
				long commerceAccountId, long organizationId,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addCommerceAccountOrganizationRel(
			commerceAccountId, organizationId, serviceContext);
	}

	public static void addCommerceAccountOrganizationRels(
			long commerceAccountId, long[] organizationIds,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		getService().addCommerceAccountOrganizationRels(
			commerceAccountId, organizationIds, serviceContext);
	}

	public static void deleteCommerceAccountOrganizationRel(
			long commerceAccountId, long organizationId)
		throws PortalException {

		getService().deleteCommerceAccountOrganizationRel(
			commerceAccountId, organizationId);
	}

	public static void deleteCommerceAccountOrganizationRels(
			long commerceAccountId, long[] organizationIds)
		throws PortalException {

		getService().deleteCommerceAccountOrganizationRels(
			commerceAccountId, organizationIds);
	}

	public static CommerceAccountOrganizationRel
			fetchCommerceAccountOrganizationRel(
				com.liferay.commerce.account.service.persistence.
					CommerceAccountOrganizationRelPK
						commerceAccountOrganizationRelPK)
		throws PortalException {

		return getService().fetchCommerceAccountOrganizationRel(
			commerceAccountOrganizationRelPK);
	}

	public static CommerceAccountOrganizationRel
			getCommerceAccountOrganizationRel(
				com.liferay.commerce.account.service.persistence.
					CommerceAccountOrganizationRelPK
						commerceAccountOrganizationRelPK)
		throws PortalException {

		return getService().getCommerceAccountOrganizationRel(
			commerceAccountOrganizationRelPK);
	}

	public static List<CommerceAccountOrganizationRel>
			getCommerceAccountOrganizationRels(long commerceAccountId)
		throws PortalException {

		return getService().getCommerceAccountOrganizationRels(
			commerceAccountId);
	}

	public static List<CommerceAccountOrganizationRel>
			getCommerceAccountOrganizationRels(
				long commerceAccountId, int start, int end)
		throws PortalException {

		return getService().getCommerceAccountOrganizationRels(
			commerceAccountId, start, end);
	}

	public static List<CommerceAccountOrganizationRel>
			getCommerceAccountOrganizationRelsByOrganizationId(
				long organizationId, int start, int end)
		throws PortalException {

		return getService().getCommerceAccountOrganizationRelsByOrganizationId(
			organizationId, start, end);
	}

	public static int getCommerceAccountOrganizationRelsByOrganizationIdCount(
			long organizationId)
		throws PortalException {

		return getService().
			getCommerceAccountOrganizationRelsByOrganizationIdCount(
				organizationId);
	}

	public static int getCommerceAccountOrganizationRelsCount(
			long commerceAccountId)
		throws PortalException {

		return getService().getCommerceAccountOrganizationRelsCount(
			commerceAccountId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static CommerceAccountOrganizationRelService getService() {
		return _serviceDCLSingleton.getSingleton(
			CommerceAccountOrganizationRelServiceUtil::_getService);
	}

	private static CommerceAccountOrganizationRelService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			CommerceAccountOrganizationRelServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<CommerceAccountOrganizationRelService>
			serviceReference = bundleContext.getServiceReference(
				CommerceAccountOrganizationRelService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<CommerceAccountOrganizationRelService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
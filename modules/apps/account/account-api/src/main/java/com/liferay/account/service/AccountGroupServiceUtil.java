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

package com.liferay.account.service;

import com.liferay.account.model.AccountGroup;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for AccountGroup. This utility wraps
 * <code>com.liferay.account.service.impl.AccountGroupServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see AccountGroupService
 * @generated
 */
public class AccountGroupServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.account.service.impl.AccountGroupServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static AccountGroup addAccountGroup(
			long userId, String description, String name,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addAccountGroup(
			userId, description, name, serviceContext);
	}

	public static AccountGroup deleteAccountGroup(long accountGroupId)
		throws PortalException {

		return getService().deleteAccountGroup(accountGroupId);
	}

	public static void deleteAccountGroups(long[] accountGroupIds)
		throws PortalException {

		getService().deleteAccountGroups(accountGroupIds);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.portal.kernel.search.BaseModelSearchResult
		<AccountGroup> searchAccountGroups(
				long companyId, String keywords, int start, int end,
				OrderByComparator<AccountGroup> orderByComparator)
			throws PortalException {

		return getService().searchAccountGroups(
			companyId, keywords, start, end, orderByComparator);
	}

	public static AccountGroup updateAccountGroup(
			long accountGroupId, String description, String name,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().updateAccountGroup(
			accountGroupId, description, name, serviceContext);
	}

	public static AccountGroup updateExternalReferenceCode(
			long accountGroupId, String externalReferenceCode)
		throws PortalException {

		return getService().updateExternalReferenceCode(
			accountGroupId, externalReferenceCode);
	}

	public static AccountGroupService getService() {
		return _serviceDCLSingleton.getSingleton(
			AccountGroupServiceUtil::_getService);
	}

	private static AccountGroupService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(AccountGroupServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<AccountGroupService> serviceReference =
			bundleContext.getServiceReference(AccountGroupService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<AccountGroupService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
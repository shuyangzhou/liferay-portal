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

package com.liferay.asset.category.property.service;

import com.liferay.asset.category.property.model.AssetCategoryProperty;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for AssetCategoryProperty. This utility wraps
 * <code>com.liferay.asset.category.property.service.impl.AssetCategoryPropertyServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see AssetCategoryPropertyService
 * @generated
 */
public class AssetCategoryPropertyServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.asset.category.property.service.impl.AssetCategoryPropertyServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static AssetCategoryProperty addCategoryProperty(
			long entryId, String key, String value)
		throws PortalException {

		return getService().addCategoryProperty(entryId, key, value);
	}

	public static void deleteCategoryProperty(long categoryPropertyId)
		throws PortalException {

		getService().deleteCategoryProperty(categoryPropertyId);
	}

	public static List<AssetCategoryProperty> getCategoryProperties(
		long entryId) {

		return getService().getCategoryProperties(entryId);
	}

	public static List<AssetCategoryProperty> getCategoryPropertyValues(
		long companyId, String key) {

		return getService().getCategoryPropertyValues(companyId, key);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static AssetCategoryProperty updateCategoryProperty(
			long userId, long categoryPropertyId, String key, String value)
		throws PortalException {

		return getService().updateCategoryProperty(
			userId, categoryPropertyId, key, value);
	}

	public static AssetCategoryProperty updateCategoryProperty(
			long categoryPropertyId, String key, String value)
		throws PortalException {

		return getService().updateCategoryProperty(
			categoryPropertyId, key, value);
	}

	public static AssetCategoryPropertyService getService() {
		return _serviceDCLSingleton.getSingleton(
			AssetCategoryPropertyServiceUtil::_getService);
	}

	private static AssetCategoryPropertyService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			AssetCategoryPropertyServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<AssetCategoryPropertyService> serviceReference =
			bundleContext.getServiceReference(
				AssetCategoryPropertyService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<AssetCategoryPropertyService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
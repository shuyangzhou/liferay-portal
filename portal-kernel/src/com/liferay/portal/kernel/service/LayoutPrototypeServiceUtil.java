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
import com.liferay.portal.kernel.model.LayoutPrototype;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for LayoutPrototype. This utility wraps
 * <code>com.liferay.portal.service.impl.LayoutPrototypeServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see LayoutPrototypeService
 * @generated
 */
public class LayoutPrototypeServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.service.impl.LayoutPrototypeServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static LayoutPrototype addLayoutPrototype(
			Map<java.util.Locale, String> nameMap,
			Map<java.util.Locale, String> descriptionMap, boolean active,
			ServiceContext serviceContext)
		throws PortalException {

		return getService().addLayoutPrototype(
			nameMap, descriptionMap, active, serviceContext);
	}

	public static void deleteLayoutPrototype(long layoutPrototypeId)
		throws PortalException {

		getService().deleteLayoutPrototype(layoutPrototypeId);
	}

	public static LayoutPrototype fetchLayoutPrototype(long layoutPrototypeId)
		throws PortalException {

		return getService().fetchLayoutPrototype(layoutPrototypeId);
	}

	public static LayoutPrototype getLayoutPrototype(long layoutPrototypeId)
		throws PortalException {

		return getService().getLayoutPrototype(layoutPrototypeId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static List<LayoutPrototype> search(
			long companyId, Boolean active,
			OrderByComparator<LayoutPrototype> orderByComparator)
		throws PortalException {

		return getService().search(companyId, active, orderByComparator);
	}

	public static LayoutPrototype updateLayoutPrototype(
			long layoutPrototypeId, Map<java.util.Locale, String> nameMap,
			Map<java.util.Locale, String> descriptionMap, boolean active,
			ServiceContext serviceContext)
		throws PortalException {

		return getService().updateLayoutPrototype(
			layoutPrototypeId, nameMap, descriptionMap, active, serviceContext);
	}

	public static LayoutPrototypeService getService() {
		return _serviceDCLSingleton.getSingleton(
			LayoutPrototypeServiceUtil::_getService);
	}

	private static LayoutPrototypeService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			LayoutPrototypeServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<LayoutPrototypeService> serviceReference =
			bundleContext.getServiceReference(LayoutPrototypeService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<LayoutPrototypeService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
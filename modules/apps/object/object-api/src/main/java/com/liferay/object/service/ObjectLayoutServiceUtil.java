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

package com.liferay.object.service;

import com.liferay.object.model.ObjectLayout;
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
 * Provides the remote service utility for ObjectLayout. This utility wraps
 * <code>com.liferay.object.service.impl.ObjectLayoutServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Marco Leo
 * @see ObjectLayoutService
 * @generated
 */
public class ObjectLayoutServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.object.service.impl.ObjectLayoutServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static ObjectLayout addObjectLayout(
			long objectDefinitionId, boolean defaultObjectLayout,
			Map<java.util.Locale, String> nameMap,
			List<com.liferay.object.model.ObjectLayoutTab> objectLayoutTabs)
		throws PortalException {

		return getService().addObjectLayout(
			objectDefinitionId, defaultObjectLayout, nameMap, objectLayoutTabs);
	}

	public static ObjectLayout deleteObjectLayout(long objectLayoutId)
		throws PortalException {

		return getService().deleteObjectLayout(objectLayoutId);
	}

	public static ObjectLayout getObjectLayout(long objectLayoutId)
		throws PortalException {

		return getService().getObjectLayout(objectLayoutId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static ObjectLayout updateObjectLayout(
			long objectLayoutId, boolean defaultObjectLayout,
			Map<java.util.Locale, String> nameMap,
			List<com.liferay.object.model.ObjectLayoutTab> objectLayoutTabs)
		throws PortalException {

		return getService().updateObjectLayout(
			objectLayoutId, defaultObjectLayout, nameMap, objectLayoutTabs);
	}

	public static ObjectLayoutService getService() {
		return _serviceDCLSingleton.getSingleton(
			ObjectLayoutServiceUtil::_getService);
	}

	private static ObjectLayoutService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(ObjectLayoutServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<ObjectLayoutService> serviceReference =
			bundleContext.getServiceReference(ObjectLayoutService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<ObjectLayoutService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
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

import com.liferay.object.model.ObjectValidationRule;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for ObjectValidationRule. This utility wraps
 * <code>com.liferay.object.service.impl.ObjectValidationRuleServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Marco Leo
 * @see ObjectValidationRuleService
 * @generated
 */
public class ObjectValidationRuleServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.object.service.impl.ObjectValidationRuleServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static ObjectValidationRule addObjectValidationRule(
			long objectDefinitionId, boolean active, String engine,
			Map<java.util.Locale, String> errorLabelMap,
			Map<java.util.Locale, String> nameMap, String script)
		throws PortalException {

		return getService().addObjectValidationRule(
			objectDefinitionId, active, engine, errorLabelMap, nameMap, script);
	}

	public static ObjectValidationRule deleteObjectValidationRule(
			long objectValidationRuleId)
		throws PortalException {

		return getService().deleteObjectValidationRule(objectValidationRuleId);
	}

	public static ObjectValidationRule getObjectValidationRule(
			long objectValidationRuleId)
		throws PortalException {

		return getService().getObjectValidationRule(objectValidationRuleId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static ObjectValidationRule updateObjectValidationRule(
			long objectValidationRuleId, boolean active, String engine,
			Map<java.util.Locale, String> errorLabelMap,
			Map<java.util.Locale, String> nameMap, String script)
		throws PortalException {

		return getService().updateObjectValidationRule(
			objectValidationRuleId, active, engine, errorLabelMap, nameMap,
			script);
	}

	public static ObjectValidationRuleService getService() {
		return _serviceDCLSingleton.getSingleton(
			ObjectValidationRuleServiceUtil::_getService);
	}

	private static ObjectValidationRuleService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			ObjectValidationRuleServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<ObjectValidationRuleService> serviceReference =
			bundleContext.getServiceReference(
				ObjectValidationRuleService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<ObjectValidationRuleService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
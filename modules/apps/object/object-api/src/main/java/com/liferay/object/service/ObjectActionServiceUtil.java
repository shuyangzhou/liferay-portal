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

import com.liferay.object.model.ObjectAction;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for ObjectAction. This utility wraps
 * <code>com.liferay.object.service.impl.ObjectActionServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Marco Leo
 * @see ObjectActionService
 * @generated
 */
public class ObjectActionServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.object.service.impl.ObjectActionServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static ObjectAction addObjectAction(
			String externalReferenceCode, long objectDefinitionId,
			boolean active, String conditionExpression, String description,
			Map<java.util.Locale, String> errorMessageMap,
			Map<java.util.Locale, String> labelMap, String name,
			String objectActionExecutorKey, String objectActionTriggerKey,
			com.liferay.portal.kernel.util.UnicodeProperties
				parametersUnicodeProperties)
		throws PortalException {

		return getService().addObjectAction(
			externalReferenceCode, objectDefinitionId, active,
			conditionExpression, description, errorMessageMap, labelMap, name,
			objectActionExecutorKey, objectActionTriggerKey,
			parametersUnicodeProperties);
	}

	public static ObjectAction deleteObjectAction(long objectActionId)
		throws PortalException {

		return getService().deleteObjectAction(objectActionId);
	}

	public static ObjectAction getObjectAction(long objectActionId)
		throws PortalException {

		return getService().getObjectAction(objectActionId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static ObjectAction updateObjectAction(
			String externalReferenceCode, long objectActionId, boolean active,
			String conditionExpression, String description,
			Map<java.util.Locale, String> errorMessageMap,
			Map<java.util.Locale, String> labelMap, String name,
			String objectActionExecutorKey, String objectActionTriggerKey,
			com.liferay.portal.kernel.util.UnicodeProperties
				parametersUnicodeProperties)
		throws PortalException {

		return getService().updateObjectAction(
			externalReferenceCode, objectActionId, active, conditionExpression,
			description, errorMessageMap, labelMap, name,
			objectActionExecutorKey, objectActionTriggerKey,
			parametersUnicodeProperties);
	}

	public static ObjectActionService getService() {
		return _serviceDCLSingleton.getSingleton(
			ObjectActionServiceUtil::_getService);
	}

	private static ObjectActionService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(ObjectActionServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<ObjectActionService> serviceReference =
			bundleContext.getServiceReference(ObjectActionService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<ObjectActionService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
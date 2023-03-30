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

import com.liferay.object.model.ObjectField;
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
 * Provides the remote service utility for ObjectField. This utility wraps
 * <code>com.liferay.object.service.impl.ObjectFieldServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Marco Leo
 * @see ObjectFieldService
 * @generated
 */
public class ObjectFieldServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.object.service.impl.ObjectFieldServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static ObjectField addCustomObjectField(
			String externalReferenceCode, long listTypeDefinitionId,
			long objectDefinitionId, String businessType, String dbType,
			boolean indexed, boolean indexedAsKeyword, String indexedLanguageId,
			Map<java.util.Locale, String> labelMap, String name,
			boolean required, boolean state,
			List<com.liferay.object.model.ObjectFieldSetting>
				objectFieldSettings)
		throws PortalException {

		return getService().addCustomObjectField(
			externalReferenceCode, listTypeDefinitionId, objectDefinitionId,
			businessType, dbType, indexed, indexedAsKeyword, indexedLanguageId,
			labelMap, name, required, state, objectFieldSettings);
	}

	public static ObjectField deleteObjectField(long objectFieldId)
		throws Exception {

		return getService().deleteObjectField(objectFieldId);
	}

	public static ObjectField getObjectField(long objectFieldId)
		throws PortalException {

		return getService().getObjectField(objectFieldId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static ObjectField updateObjectField(
			String externalReferenceCode, long objectFieldId,
			long listTypeDefinitionId, String businessType, String dbType,
			boolean indexed, boolean indexedAsKeyword, String indexedLanguageId,
			Map<java.util.Locale, String> labelMap, String name,
			boolean required, boolean state,
			List<com.liferay.object.model.ObjectFieldSetting>
				objectFieldSettings)
		throws PortalException {

		return getService().updateObjectField(
			externalReferenceCode, objectFieldId, listTypeDefinitionId,
			businessType, dbType, indexed, indexedAsKeyword, indexedLanguageId,
			labelMap, name, required, state, objectFieldSettings);
	}

	public static ObjectFieldService getService() {
		return _serviceDCLSingleton.getSingleton(
			ObjectFieldServiceUtil::_getService);
	}

	private static ObjectFieldService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(ObjectFieldServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<ObjectFieldService> serviceReference =
			bundleContext.getServiceReference(ObjectFieldService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<ObjectFieldService> _serviceDCLSingleton =
		new DCLSingleton<>();

}
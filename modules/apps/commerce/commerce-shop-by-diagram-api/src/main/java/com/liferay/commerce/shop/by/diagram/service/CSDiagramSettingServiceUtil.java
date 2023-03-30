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

package com.liferay.commerce.shop.by.diagram.service;

import com.liferay.commerce.shop.by.diagram.model.CSDiagramSetting;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for CSDiagramSetting. This utility wraps
 * <code>com.liferay.commerce.shop.by.diagram.service.impl.CSDiagramSettingServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Alessio Antonio Rendina
 * @see CSDiagramSettingService
 * @generated
 */
public class CSDiagramSettingServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.shop.by.diagram.service.impl.CSDiagramSettingServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static CSDiagramSetting addCSDiagramSetting(
			long cpDefinitionId, long cpAttachmentFileEntryId, String color,
			double radius, String type)
		throws PortalException {

		return getService().addCSDiagramSetting(
			cpDefinitionId, cpAttachmentFileEntryId, color, radius, type);
	}

	public static CSDiagramSetting fetchCSDiagramSettingByCPDefinitionId(
			long cpDefinitionId)
		throws PortalException {

		return getService().fetchCSDiagramSettingByCPDefinitionId(
			cpDefinitionId);
	}

	public static CSDiagramSetting getCSDiagramSetting(long csDiagramSettingId)
		throws PortalException {

		return getService().getCSDiagramSetting(csDiagramSettingId);
	}

	public static CSDiagramSetting getCSDiagramSettingByCPDefinitionId(
			long cpDefinitionId)
		throws PortalException {

		return getService().getCSDiagramSettingByCPDefinitionId(cpDefinitionId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static CSDiagramSetting updateCSDiagramSetting(
			long csDiagramSettingId, long cpAttachmentFileEntryId, String color,
			double radius, String type)
		throws PortalException {

		return getService().updateCSDiagramSetting(
			csDiagramSettingId, cpAttachmentFileEntryId, color, radius, type);
	}

	public static CSDiagramSettingService getService() {
		return _serviceDCLSingleton.getSingleton(
			CSDiagramSettingServiceUtil::_getService);
	}

	private static CSDiagramSettingService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			CSDiagramSettingServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<CSDiagramSettingService> serviceReference =
			bundleContext.getServiceReference(CSDiagramSettingService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<CSDiagramSettingService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
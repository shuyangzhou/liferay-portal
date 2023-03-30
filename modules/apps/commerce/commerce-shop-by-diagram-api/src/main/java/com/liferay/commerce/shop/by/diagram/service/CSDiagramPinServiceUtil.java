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

import com.liferay.commerce.shop.by.diagram.model.CSDiagramPin;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for CSDiagramPin. This utility wraps
 * <code>com.liferay.commerce.shop.by.diagram.service.impl.CSDiagramPinServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Alessio Antonio Rendina
 * @see CSDiagramPinService
 * @generated
 */
public class CSDiagramPinServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.shop.by.diagram.service.impl.CSDiagramPinServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static CSDiagramPin addCSDiagramPin(
			long cpDefinitionId, double positionX, double positionY,
			String sequence)
		throws PortalException {

		return getService().addCSDiagramPin(
			cpDefinitionId, positionX, positionY, sequence);
	}

	public static void deleteCSDiagramPin(CSDiagramPin csDiagramPin)
		throws PortalException {

		getService().deleteCSDiagramPin(csDiagramPin);
	}

	public static void deleteCSDiagramPins(long cpDefinitionId)
		throws PortalException {

		getService().deleteCSDiagramPins(cpDefinitionId);
	}

	public static CSDiagramPin fetchCSDiagramPin(long csDiagramPinId) {
		return getService().fetchCSDiagramPin(csDiagramPinId);
	}

	public static CSDiagramPin getCSDiagramPin(long csDiagramPinId)
		throws PortalException {

		return getService().getCSDiagramPin(csDiagramPinId);
	}

	public static List<CSDiagramPin> getCSDiagramPins(
			long cpDefinitionId, int start, int end)
		throws PortalException {

		return getService().getCSDiagramPins(cpDefinitionId, start, end);
	}

	public static int getCSDiagramPinsCount(long cpDefinitionId)
		throws PortalException {

		return getService().getCSDiagramPinsCount(cpDefinitionId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static CSDiagramPin updateCSDiagramPin(
			long csDiagramPinId, double positionX, double positionY,
			String sequence)
		throws PortalException {

		return getService().updateCSDiagramPin(
			csDiagramPinId, positionX, positionY, sequence);
	}

	public static CSDiagramPinService getService() {
		return _serviceDCLSingleton.getSingleton(
			CSDiagramPinServiceUtil::_getService);
	}

	private static CSDiagramPinService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(CSDiagramPinServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<CSDiagramPinService> serviceReference =
			bundleContext.getServiceReference(CSDiagramPinService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<CSDiagramPinService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
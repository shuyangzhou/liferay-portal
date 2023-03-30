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

package com.liferay.segments.service;

import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.segments.model.SegmentsExperimentRel;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for SegmentsExperimentRel. This utility wraps
 * <code>com.liferay.segments.service.impl.SegmentsExperimentRelServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Eduardo Garcia
 * @see SegmentsExperimentRelService
 * @generated
 */
public class SegmentsExperimentRelServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.segments.service.impl.SegmentsExperimentRelServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static SegmentsExperimentRel addSegmentsExperimentRel(
			long segmentsExperimentId, long segmentsExperienceId,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addSegmentsExperimentRel(
			segmentsExperimentId, segmentsExperienceId, serviceContext);
	}

	public static SegmentsExperimentRel deleteSegmentsExperimentRel(
			long segmentsExperimentRelId)
		throws PortalException {

		return getService().deleteSegmentsExperimentRel(
			segmentsExperimentRelId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static SegmentsExperimentRel getSegmentsExperimentRel(
			long segmentsExperimentId, long segmentsExperienceId)
		throws PortalException {

		return getService().getSegmentsExperimentRel(
			segmentsExperimentId, segmentsExperienceId);
	}

	public static List<SegmentsExperimentRel> getSegmentsExperimentRels(
			long segmentsExperimentId)
		throws PortalException {

		return getService().getSegmentsExperimentRels(segmentsExperimentId);
	}

	public static SegmentsExperimentRel updateSegmentsExperimentRel(
			long segmentsExperimentRelId, double split)
		throws PortalException {

		return getService().updateSegmentsExperimentRel(
			segmentsExperimentRelId, split);
	}

	public static SegmentsExperimentRel updateSegmentsExperimentRel(
			long segmentsExperimentRelId, String name,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().updateSegmentsExperimentRel(
			segmentsExperimentRelId, name, serviceContext);
	}

	public static SegmentsExperimentRelService getService() {
		return _serviceDCLSingleton.getSingleton(
			SegmentsExperimentRelServiceUtil::_getService);
	}

	private static SegmentsExperimentRelService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			SegmentsExperimentRelServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<SegmentsExperimentRelService> serviceReference =
			bundleContext.getServiceReference(
				SegmentsExperimentRelService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<SegmentsExperimentRelService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
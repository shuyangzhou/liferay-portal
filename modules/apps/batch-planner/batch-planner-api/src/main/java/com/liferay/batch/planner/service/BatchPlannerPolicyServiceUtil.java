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

package com.liferay.batch.planner.service;

import com.liferay.batch.planner.model.BatchPlannerPolicy;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for BatchPlannerPolicy. This utility wraps
 * <code>com.liferay.batch.planner.service.impl.BatchPlannerPolicyServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Igor Beslic
 * @see BatchPlannerPolicyService
 * @generated
 */
public class BatchPlannerPolicyServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.batch.planner.service.impl.BatchPlannerPolicyServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static BatchPlannerPolicy addBatchPlannerPolicy(
			long batchPlannerPlanId, String name, String value)
		throws PortalException {

		return getService().addBatchPlannerPolicy(
			batchPlannerPlanId, name, value);
	}

	public static BatchPlannerPolicy deleteBatchPlannerPolicy(
			long batchPlannerPlanId, String name)
		throws PortalException {

		return getService().deleteBatchPlannerPolicy(batchPlannerPlanId, name);
	}

	public static List<BatchPlannerPolicy> getBatchPlannerPolicies(
			long batchPlannerPlanId)
		throws PortalException {

		return getService().getBatchPlannerPolicies(batchPlannerPlanId);
	}

	public static BatchPlannerPolicy getBatchPlannerPolicy(
			long batchPlannerPlanId, String name)
		throws PortalException {

		return getService().getBatchPlannerPolicy(batchPlannerPlanId, name);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static boolean hasBatchPlannerPolicy(
			long batchPlannerPlanId, String name)
		throws PortalException {

		return getService().hasBatchPlannerPolicy(batchPlannerPlanId, name);
	}

	public static BatchPlannerPolicy updateBatchPlannerPolicy(
			long batchPlannerPlanId, String name, String value)
		throws PortalException {

		return getService().updateBatchPlannerPolicy(
			batchPlannerPlanId, name, value);
	}

	public static BatchPlannerPolicyService getService() {
		return _serviceDCLSingleton.getSingleton(
			BatchPlannerPolicyServiceUtil::_getService);
	}

	private static BatchPlannerPolicyService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			BatchPlannerPolicyServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<BatchPlannerPolicyService> serviceReference =
			bundleContext.getServiceReference(BatchPlannerPolicyService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<BatchPlannerPolicyService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
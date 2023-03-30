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

package com.liferay.fragment.service;

import com.liferay.fragment.model.FragmentComposition;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for FragmentComposition. This utility wraps
 * <code>com.liferay.fragment.service.impl.FragmentCompositionServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see FragmentCompositionService
 * @generated
 */
public class FragmentCompositionServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.fragment.service.impl.FragmentCompositionServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static FragmentComposition addFragmentComposition(
			long groupId, long fragmentCollectionId,
			String fragmentCompositionKey, String name, String description,
			String data, long previewFileEntryId, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addFragmentComposition(
			groupId, fragmentCollectionId, fragmentCompositionKey, name,
			description, data, previewFileEntryId, status, serviceContext);
	}

	public static FragmentComposition deleteFragmentComposition(
			long fragmentCompositionId)
		throws PortalException {

		return getService().deleteFragmentComposition(fragmentCompositionId);
	}

	public static FragmentComposition fetchFragmentComposition(
		long fragmentCompositionId) {

		return getService().fetchFragmentComposition(fragmentCompositionId);
	}

	public static FragmentComposition fetchFragmentComposition(
		long groupId, String fragmentCompositionKey) {

		return getService().fetchFragmentComposition(
			groupId, fragmentCompositionKey);
	}

	public static List<FragmentComposition> getFragmentCompositions(
		long fragmentCollectionId) {

		return getService().getFragmentCompositions(fragmentCollectionId);
	}

	public static List<FragmentComposition> getFragmentCompositions(
		long fragmentCollectionId, int start, int end) {

		return getService().getFragmentCompositions(
			fragmentCollectionId, start, end);
	}

	public static List<FragmentComposition> getFragmentCompositions(
		long groupId, long fragmentCollectionId, int status) {

		return getService().getFragmentCompositions(
			groupId, fragmentCollectionId, status);
	}

	public static List<FragmentComposition> getFragmentCompositions(
		long groupId, long fragmentCollectionId, int start, int end,
		OrderByComparator<FragmentComposition> orderByComparator) {

		return getService().getFragmentCompositions(
			groupId, fragmentCollectionId, start, end, orderByComparator);
	}

	public static List<FragmentComposition> getFragmentCompositions(
		long groupId, long fragmentCollectionId, String name, int start,
		int end, OrderByComparator<FragmentComposition> orderByComparator) {

		return getService().getFragmentCompositions(
			groupId, fragmentCollectionId, name, start, end, orderByComparator);
	}

	public static int getFragmentCompositionsCount(long fragmentCollectionId) {
		return getService().getFragmentCompositionsCount(fragmentCollectionId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static FragmentComposition moveFragmentComposition(
			long fragmentCompositionId, long fragmentCollectionId)
		throws PortalException {

		return getService().moveFragmentComposition(
			fragmentCompositionId, fragmentCollectionId);
	}

	public static FragmentComposition updateFragmentComposition(
			long fragmentCompositionId, long previewFileEntryId)
		throws PortalException {

		return getService().updateFragmentComposition(
			fragmentCompositionId, previewFileEntryId);
	}

	public static FragmentComposition updateFragmentComposition(
			long fragmentCompositionId, long fragmentCollectionId, String name,
			String description, String data, long previewFileEntryId,
			int status)
		throws PortalException {

		return getService().updateFragmentComposition(
			fragmentCompositionId, fragmentCollectionId, name, description,
			data, previewFileEntryId, status);
	}

	public static FragmentComposition updateFragmentComposition(
			long fragmentCompositionId, String name)
		throws PortalException {

		return getService().updateFragmentComposition(
			fragmentCompositionId, name);
	}

	public static FragmentComposition updateFragmentComposition(
			long fragmentCompositionId, String name, String description,
			String data, long previewFileEntryId, int status)
		throws PortalException {

		return getService().updateFragmentComposition(
			fragmentCompositionId, name, description, data, previewFileEntryId,
			status);
	}

	public static FragmentCompositionService getService() {
		return _serviceDCLSingleton.getSingleton(
			FragmentCompositionServiceUtil::_getService);
	}

	private static FragmentCompositionService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			FragmentCompositionServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<FragmentCompositionService> serviceReference =
			bundleContext.getServiceReference(FragmentCompositionService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<FragmentCompositionService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
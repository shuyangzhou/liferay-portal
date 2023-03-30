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

package com.liferay.portal.tools.service.builder.test.service;

import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.io.Serializable;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the local service utility for GenericMethodsEntry. This utility wraps
 * <code>com.liferay.portal.tools.service.builder.test.service.impl.GenericMethodsEntryLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see GenericMethodsEntryLocalService
 * @generated
 */
public class GenericMethodsEntryLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.tools.service.builder.test.service.impl.GenericMethodsEntryLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static <E extends Exception> void typeParameterAndBoundMethod(
			java.util.function.BiConsumer<String, E> biConsumer)
		throws E {

		getService().typeParameterAndBoundMethod(biConsumer);
	}

	public static <T> void typeParameterMethod(
			java.util.function.Consumer<T> consumer)
		throws Exception {

		getService().typeParameterMethod(consumer);
	}

	public static <T, E extends Exception> List<T> typeParametersAndBoundMethod(
		java.util.function.BiFunction<Long, T, E> biFunction,
		java.util.function.BiConsumer<Long, E> biConsumer) {

		return getService().typeParametersAndBoundMethod(
			biFunction, biConsumer);
	}

	public static <N extends Number, E extends Exception> List<N>
		typeParametersAndBoundsMethod(
			java.util.function.BiFunction<Long, N, E> biFunction,
			java.util.function.BiConsumer<Long, N> biConsumer) {

		return getService().typeParametersAndBoundsMethod(
			biFunction, biConsumer);
	}

	public static
		<N extends Number & java.util.function.ObjIntConsumer,
		 E extends Exception & Serializable> List<N>
			 typeParametersAndMultipleBoundsMethod(
			 	java.util.function.BiFunction<Long, N, E> biFunction,
			 	java.util.function.BiConsumer<Long, N> biConsumer) {

		return getService().typeParametersAndMultipleBoundsMethod(
			biFunction, biConsumer);
	}

	public static GenericMethodsEntryLocalService getService() {
		return _serviceDCLSingleton.getSingleton(
			GenericMethodsEntryLocalServiceUtil::_getService);
	}

	private static GenericMethodsEntryLocalService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			GenericMethodsEntryLocalServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<GenericMethodsEntryLocalService> serviceReference =
			bundleContext.getServiceReference(
				GenericMethodsEntryLocalService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<GenericMethodsEntryLocalService>
		_serviceDCLSingleton = new DCLSingleton<>();

}
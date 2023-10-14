/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.aop.internal;

import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;

import java.util.Arrays;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.wiring.BundleWiring;

/**
 * @author Shuyang Zhou
 */
public class ServiceReferenceHelper {

	public ServiceReferenceHelper(
		BundleContext bundleContext,
		ServiceReference<AopService> serviceReference) {

		_bundleContext = bundleContext;
		_serviceReference = serviceReference;

		Class<?>[] aopInterfaces = _extract(serviceReference);

		if (aopInterfaces == null) {
			_aopInterfaces = _getAopInterfaces(
				_aopServiceDCLSingleton.getSingleton(
					() -> bundleContext.getService(_serviceReference)));
		}
		else {
			_aopInterfaces = aopInterfaces;
		}
	}

	public void dispose() {
		_aopServiceDCLSingleton.destroy(
			aopService -> _bundleContext.ungetService(_serviceReference));
	}

	public Class<?>[] getAopInterfaces() {
		return _aopInterfaces;
	}

	public AopService getAopService() {
		return _aopServiceDCLSingleton.getSingleton(
			() -> _bundleContext.getService(_serviceReference));
	}

	public ClassLoader getAopServiceClassLoader() {
		Bundle bundle = _serviceReference.getBundle();

		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

		return bundleWiring.getClassLoader();
	}

	private Class<?>[] _extract(ServiceReference<AopService> serviceReference) {
		String componentName = (String)serviceReference.getProperty(
			"component.name");

		if (componentName == null) {
			return null;
		}

		Bundle bundle = serviceReference.getBundle();

		try {
			Class<?> clazz = bundle.loadClass(componentName);

			return _getAopInterfaces((AopService)clazz.newInstance());
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Unable to extract aop interfaces without pulling up " +
						"service for " + serviceReference,
					reflectiveOperationException);
			}

			return null;
		}
	}

	private Class<?>[] _getAopInterfaces(AopService aopService) {
		Class<?>[] aopInterfaces = aopService.getAopInterfaces();

		Class<? extends AopService> aopServiceClass = aopService.getClass();

		if (ArrayUtil.isEmpty(aopInterfaces)) {
			return ArrayUtil.remove(
				aopServiceClass.getInterfaces(), AopService.class);
		}

		for (Class<?> aopInterface : aopInterfaces) {
			if (!aopInterface.isInterface()) {
				throw new IllegalArgumentException(
					StringBundler.concat(
						"Unable to proxy ", aopServiceClass, " because ",
						aopInterface, " is not an interface"));
			}

			if (!aopInterface.isAssignableFrom(aopServiceClass)) {
				throw new IllegalArgumentException(
					StringBundler.concat(
						"Unable to proxy ", aopServiceClass, " because ",
						aopInterface, " is not implemented"));
			}

			if (aopInterface == AopService.class) {
				throw new IllegalArgumentException(
					"Do not include AopService in service interfaces");
			}
		}

		return Arrays.copyOf(aopInterfaces, aopInterfaces.length);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ServiceReferenceHelper.class);

	private final Class<?>[] _aopInterfaces;
	private final DCLSingleton<AopService> _aopServiceDCLSingleton =
		new DCLSingleton<>();
	private final BundleContext _bundleContext;
	private final ServiceReference<AopService> _serviceReference;

}
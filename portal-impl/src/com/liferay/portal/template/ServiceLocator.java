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

package com.liferay.portal.template;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.service.BaseService;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.util.function.Function;

/**
 * @author Brian Wing Shun Chan
 */
public class ServiceLocator {

	public static ServiceLocator getInstance() {
		return _serviceLocator;
	}

	public Object findService(String serviceName) {
		Object bean = null;

		try {
			Registry registry = RegistryUtil.getRegistry();

			bean = registry.callService(serviceName, Function.identity());

			if (!(bean instanceof BaseService) &&
				!(bean instanceof BaseLocalService)) {

				if (_log.isWarnEnabled()) {
					_log.warn(
						StringBundler.concat(
							"Denied access to service \"", serviceName,
							"\" as it is not a service builder generated ",
							"service"));
				}

				bean = null;
			}
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}

		return bean;
	}

	/**
	 * @deprecated As of Cavanaugh (7.4.x), with no direct replacement
	 */
	@Deprecated
	public Object findService(String servletContextName, String serviceName) {
		return findService(serviceName);
	}

	private ServiceLocator() {
	}

	private static final Log _log = LogFactoryUtil.getLog(ServiceLocator.class);

	private static final ServiceLocator _serviceLocator = new ServiceLocator();

}
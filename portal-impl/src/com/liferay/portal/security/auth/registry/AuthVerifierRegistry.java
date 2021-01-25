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

package com.liferay.portal.security.auth.registry;

import com.liferay.portal.kernel.security.auth.verifier.AuthVerifier;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.registry.collections.ServiceTrackerMap;
import com.liferay.registry.collections.ServiceTrackerMapFactory;
import com.liferay.registry.collections.ServiceTrackerMapFactoryUtil;

/**
 * @author Carlos Sierra Andrés
 */
public class AuthVerifierRegistry {

	public static AuthVerifier getAuthVerifier(String simpleClassName) {
		return _serviceTrackerMap.getService(simpleClassName);
	}

	private static final ServiceTrackerMap<String, AuthVerifier>
		_serviceTrackerMap;

	static {
		ServiceTrackerMapFactory serviceTrackerMapFactory =
			ServiceTrackerMapFactoryUtil.getServiceTrackerMapFactory();

		_serviceTrackerMap = serviceTrackerMapFactory.openSingleValueMap(
			AuthVerifier.class, null,
			(serviceReference, emitter) -> {
				String authVerifierClassName = GetterUtil.getString(
					serviceReference.getProperty("auth.verifier.class.name"));

				if (Validator.isNotNull(authVerifierClassName)) {
					emitter.emit(authVerifierClassName);
				}
			});
	}
}
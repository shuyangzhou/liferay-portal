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

package com.liferay.apio.architect.application.internal.filter;

import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.logger.ApioLogger;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.url.ServerURL;
import com.liferay.apio.architect.wiring.osgi.manager.ProviderManager;

import java.io.IOException;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * This filter checks if the mandatory providers for Apio are present. If not,
 * throws a {@link NotFoundException} which will be converted into a {@code 404}
 * response.
 *
 * @author Alejandro Hernández
 */
@Component(
	immediate = true,
	property = "liferay.apio.architect.container.request.filter=true"
)
public class MandatoryProvidersFilter implements ContainerRequestFilter {

	public static final List<String> mandatoryClassNames = Arrays.asList(
		Credentials.class.getName(), ServerURL.class.getName(),
		Pagination.class.getName());

	@Override
	public void filter(ContainerRequestContext containerRequestContext)
		throws IOException {

		List<String> missingMandatoryProviders =
			_providerManager.getMissingProviders(mandatoryClassNames);

		if (!missingMandatoryProviders.isEmpty()) {
			_apioLogger.warning(
				"Missing providers for mandatory classes: " +
					missingMandatoryProviders);

			throw new NotFoundException();
		}
	}

	@Reference
	private ApioLogger _apioLogger;

	@Reference
	private ProviderManager _providerManager;

}
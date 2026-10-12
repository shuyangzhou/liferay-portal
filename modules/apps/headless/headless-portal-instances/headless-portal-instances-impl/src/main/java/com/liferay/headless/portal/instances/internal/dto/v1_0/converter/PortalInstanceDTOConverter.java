/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.portal.instances.internal.dto.v1_0.converter;

import com.liferay.headless.portal.instances.dto.v1_0.PortalInstance;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jorge Díaz
 */
@Component(
	property = "dto.class.name=com.liferay.portal.kernel.model.Company",
	service = DTOConverter.class
)
public class PortalInstanceDTOConverter
	implements DTOConverter<Company, PortalInstance> {

	@Override
	public String getContentType() {
		return PortalInstance.class.getSimpleName();
	}

	@Override
	public PortalInstance toDTO(
		DTOConverterContext dtoConverterContext, Company company) {

		return new PortalInstance() {
			{
				setActive(company::isActive);
				setCompanyId(company::getCompanyId);
				setDomain(company::getMx);
				setMaxUsers(company::getMaxUsers);
				setPortalInstanceId(company::getWebId);
				setVirtualHost(company::getVirtualHostname);
			}
		};
	}

}
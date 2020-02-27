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

package com.liferay.company.generator;

import com.liferay.company.generator.configuration.CompanyGeneratorConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.instances.service.PortalInstancesLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.util.ProxyFactory;

import java.util.Map;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Kyle Miho
 */
@Component(
	configurationPid = "com.liferay.company.generator.configuration.CompanyGeneratorConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true,
	service = {}
)
public class CompanyGenerator {

	@Activate
	protected void activate(Map<String, Object> properties) {
		_companyGeneratorConfiguration = ConfigurableUtil.createConfigurable(
			CompanyGeneratorConfiguration.class, properties);

		try {
			Company company = _companyLocalService.addCompany(
				_companyGeneratorConfiguration.virtualHostName(),
				_companyGeneratorConfiguration.virtualHostName(), "liferay.com",
				false, 0, true);

			_portalInstancesLocalService.initializePortalInstance(
				ProxyFactory.newDummyInstance(ServletContext.class),
				company.getWebId());

			_portalInstancesLocalService.synchronizePortalInstances();

			if (_log.isInfoEnabled()) {
				_log.info(
					_companyGeneratorConfiguration.customActivationMessage());
			}
		}
		catch (PortalException portalException) {
			_log.error(portalException, portalException);
		}
	}

	@Deactivate
	protected void deactivate() {
		Company company = _companyLocalService.fetchCompanyByVirtualHost(
			_companyGeneratorConfiguration.virtualHostName());

		try {
			_companyLocalService.deleteCompany(company);

			_portalInstancesLocalService.synchronizePortalInstances();
		}
		catch (PortalException portalException) {
			_log.error(portalException, portalException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CompanyGenerator.class);

	private CompanyGeneratorConfiguration _companyGeneratorConfiguration;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private PortalInstancesLocalService _portalInstancesLocalService;

}
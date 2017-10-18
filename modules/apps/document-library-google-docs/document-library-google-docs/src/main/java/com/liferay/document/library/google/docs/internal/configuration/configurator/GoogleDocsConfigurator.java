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

package com.liferay.document.library.google.docs.internal.configuration.configurator;

import com.liferay.document.library.google.docs.internal.util.GoogleDocsDLFileEntryTypeHelper;
import com.liferay.document.library.kernel.service.DLFileEntryTypeLocalService;
import com.liferay.dynamic.data.mapping.io.DDMFormXSDDeserializer;
import com.liferay.dynamic.data.mapping.kernel.DDMStructureLinkManager;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureVersionLocalService;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.dynamic.data.mapping.util.DDMStructurePermissionSupport;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Iván Zaera
 */
@Component(immediate = true, service = GoogleDocsConfigurator.class)
public class GoogleDocsConfigurator {

	@Activate
	public void activate() throws PortalException {
		ActionableDynamicQuery actionableDynamicQuery =
			_companyLocalService.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<Company>() {

				@Override
				public void performAction(Company company)
					throws PortalException {

					GoogleDocsDLFileEntryTypeHelper
						googleDocsDLFileEntryTypeHelper =
							new GoogleDocsDLFileEntryTypeHelper(
								company, _classNameLocalService, _ddm,
								_ddmFormXSDDeserializer,
								_ddmStructureLocalService,
								_dlFileEntryTypeLocalService,
								_userLocalService);

					googleDocsDLFileEntryTypeHelper.
						addGoogleDocsDLFileEntryType();
				}

			});

		actionableDynamicQuery.performActions();
	}

	@Reference(unbind = "-")
	protected void setDDMStructureLinkManager(
		DDMStructureLinkManager ddmStructureLinkManager) {
	}

	@Reference(unbind = "-")
	protected void setDDMStructureVersionLocalService(
		DDMStructureVersionLocalService ddmStructureVersionLocalService) {

		_ddmStructureVersionLocalService = ddmStructureVersionLocalService;
	}

	@Reference(
		target = "(model.class.name=com.liferay.document.library.kernel.model.DLFileEntryMetadata)",
		unbind = "-"
	)
	protected void setDLFileEntryMetadataDDMPermissionSupport(
		DDMStructurePermissionSupport ddmStructurePermissionSupport) {
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	@Reference(
		target = "(model.class.name=com.liferay.document.library.kernel.util.RawMetadataProcessor)",
		unbind = "-"
	)
	protected void setRawMetadataProcessorDDMPermissionSupport(
		DDMStructurePermissionSupport ddmStructurePermissionSupport) {
	}

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private DDM _ddm;

	@Reference
	private DDMFormXSDDeserializer _ddmFormXSDDeserializer;

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	private DDMStructureVersionLocalService _ddmStructureVersionLocalService;

	@Reference
	private DLFileEntryTypeLocalService _dlFileEntryTypeLocalService;

	@Reference
	private UserLocalService _userLocalService;

}
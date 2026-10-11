/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.instances.web.internal.portlet.action;

import com.liferay.batch.engine.jaxrs.uri.BatchEngineUriInfo;
import com.liferay.headless.portal.instances.resource.v1_0.PortalInstanceExportResource;
import com.liferay.portal.db.partition.util.DBPartitionUtil;
import com.liferay.portal.instances.constants.PortalInstancesPortletKeys;
import com.liferay.portal.instances.web.internal.util.PortalInstancesResourceContextUtil;
import com.liferay.portal.kernel.exception.NoSuchCompanyException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResourceFactory;

import jakarta.portlet.ActionRequest;
import jakarta.portlet.ActionResponse;

import java.util.Collections;

import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * @author Jorge Avalos
 */
@Component(
	property = {
		"jakarta.portlet.name=" + PortalInstancesPortletKeys.PORTAL_INSTANCES,
		"mvc.command.name=/portal_instances/export_instance"
	},
	service = MVCActionCommand.class
)
public class ExportInstanceMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		hideDefaultSuccessMessage(actionRequest);

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		String portalInstanceId = ParamUtil.getString(
			actionRequest, "portalInstanceId");

		try {
			Company company = _companyLocalService.getCompanyByWebId(
				portalInstanceId);

			if (DBPartitionUtil.existsExportedPartition(
					company.getCompanyId())) {

				jsonObject.put(
					"error",
					_language.format(
						actionRequest.getLocale(),
						"the-exported-schema-x-already-exists",
						DBPartitionUtil.getExportedPartitionName(
							company.getCompanyId())));
			}
			else {
				_exportPortalInstance(actionRequest);
			}
		}
		catch (NoSuchCompanyException noSuchCompanyException) {
			if (_log.isDebugEnabled()) {
				_log.debug(noSuchCompanyException);
			}

			jsonObject.put(
				"error",
				_language.format(
					actionRequest.getLocale(),
					"the-instance-x-is-no-longer-available", portalInstanceId));
		}
		catch (Exception exception) {
			_log.error(exception);

			jsonObject.put(
				"error",
				_language.get(
					actionRequest.getLocale(), "an-unexpected-error-occurred"));
		}

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, jsonObject);
	}

	private void _exportPortalInstance(ActionRequest actionRequest)
		throws Exception {

		PortalInstanceExportResource portalInstanceExportResource =
			_componentServiceObjects.getService();

		try {
			portalInstanceExportResource.setContextAcceptLanguage(
				PortalInstancesResourceContextUtil.getAcceptLanguage(
					_portal.getLocale(actionRequest)));
			portalInstanceExportResource.setContextCompany(
				_portal.getCompany(actionRequest));
			portalInstanceExportResource.setContextHttpServletRequest(
				PortalInstancesResourceContextUtil.getHttpServletRequest(
					_portal.getHttpServletRequest(actionRequest)));
			portalInstanceExportResource.setContextUriInfo(
				new BatchEngineUriInfo.Builder(
				).build());
			portalInstanceExportResource.setContextUser(
				_portal.getUser(actionRequest));
			portalInstanceExportResource.setVulcanBatchEngineImportTaskResource(
				_vulcanBatchEngineImportTaskResourceFactory.create());

			portalInstanceExportResource.postPortalInstanceExportBatch(
				null,
				Collections.singletonList(
					HashMapBuilder.put(
						"portalInstanceId",
						ParamUtil.getString(actionRequest, "portalInstanceId")
					).build()));
		}
		finally {
			_componentServiceObjects.ungetService(portalInstanceExportResource);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ExportInstanceMVCActionCommand.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<PortalInstanceExportResource>
		_componentServiceObjects;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	@Reference
	private VulcanBatchEngineImportTaskResourceFactory
		_vulcanBatchEngineImportTaskResourceFactory;

}
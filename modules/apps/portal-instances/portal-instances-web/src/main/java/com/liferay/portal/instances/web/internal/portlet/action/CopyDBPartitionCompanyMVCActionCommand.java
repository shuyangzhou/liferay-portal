/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.instances.web.internal.portlet.action;

import com.liferay.batch.engine.jaxrs.uri.BatchEngineUriInfo;
import com.liferay.headless.portal.instances.resource.v1_0.PortalInstanceCopyResource;
import com.liferay.portal.instances.constants.PortalInstancesPortletKeys;
import com.liferay.portal.instances.web.internal.util.PortalInstancesResourceContextUtil;
import com.liferay.portal.kernel.exception.CompanyNameException;
import com.liferay.portal.kernel.exception.CompanyVirtualHostException;
import com.liferay.portal.kernel.exception.CompanyWebIdException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.instance.PortalInstancePool;
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
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
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
		"mvc.command.name=/portal_instances/copy_db_partition_company"
	},
	service = MVCActionCommand.class
)
public class CopyDBPartitionCompanyMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		hideDefaultSuccessMessage(actionRequest);

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		try {
			Company company = _companyLocalService.getCompany(
				ParamUtil.getLong(actionRequest, "sourceCompanyId"));

			_validateCompany(actionRequest, company);

			_copyPortalInstance(actionRequest, company);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			jsonObject.put(
				"error",
				_language.get(
					actionRequest.getLocale(), _getErrorMessageKey(exception)));
		}

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, jsonObject);
	}

	private void _copyPortalInstance(
			ActionRequest actionRequest, Company company)
		throws Exception {

		PortalInstanceCopyResource portalInstanceCopyResource =
			_componentServiceObjects.getService();

		try {
			portalInstanceCopyResource.setContextAcceptLanguage(
				PortalInstancesResourceContextUtil.getAcceptLanguage(
					_portal.getLocale(actionRequest)));
			portalInstanceCopyResource.setContextCompany(
				_portal.getCompany(actionRequest));
			portalInstanceCopyResource.setContextHttpServletRequest(
				PortalInstancesResourceContextUtil.getHttpServletRequest(
					_portal.getHttpServletRequest(actionRequest)));
			portalInstanceCopyResource.setContextUriInfo(
				new BatchEngineUriInfo.Builder(
				).build());
			portalInstanceCopyResource.setContextUser(
				_portal.getUser(actionRequest));
			portalInstanceCopyResource.setVulcanBatchEngineImportTaskResource(
				_vulcanBatchEngineImportTaskResourceFactory.create());

			portalInstanceCopyResource.postPortalInstanceCopyBatch(
				null,
				Collections.singletonList(
					HashMapBuilder.<String, Object>put(
						"destinationCompanyId",
						_getDestinationCompanyId(actionRequest)
					).put(
						"name", ParamUtil.getString(actionRequest, "name")
					).put(
						"sourcePortalInstanceId", company.getWebId()
					).put(
						"virtualHost",
						ParamUtil.getString(actionRequest, "virtualHostname")
					).put(
						"webId", ParamUtil.getString(actionRequest, "webId")
					).build()));
		}
		finally {
			_componentServiceObjects.ungetService(portalInstanceCopyResource);
		}
	}

	private Long _getDestinationCompanyId(ActionRequest actionRequest) {
		String destinationCompanyId = ParamUtil.getString(
			actionRequest, "destinationCompanyId");

		if (Validator.isNull(destinationCompanyId)) {
			return null;
		}

		if (!Validator.isNumber(destinationCompanyId)) {
			throw new IllegalArgumentException();
		}

		try {
			return Long.parseLong(destinationCompanyId);
		}
		catch (NumberFormatException numberFormatException) {
			throw new IllegalArgumentException(numberFormatException);
		}
	}

	private String _getErrorMessageKey(Exception exception) {
		if (exception instanceof CompanyNameException) {
			return "please-enter-a-valid-name";
		}

		if (exception instanceof CompanyVirtualHostException) {
			return "please-enter-a-valid-virtual-host";
		}

		if (exception instanceof CompanyWebIdException) {
			return "please-enter-a-valid-web-id";
		}

		if (exception instanceof IllegalArgumentException) {
			String message = GetterUtil.getString(exception.getMessage());

			if (message.endsWith(" is the default company ID")) {
				return "the-default-instance-cannot-be-copied";
			}

			return "please-enter-a-valid-destination-company-id";
		}

		_log.error(exception);

		return "an-unexpected-error-occurred";
	}

	private void _validateCompany(ActionRequest actionRequest, Company company)
		throws PortalException {

		if (company.getCompanyId() ==
				PortalInstancePool.getDefaultCompanyId()) {

			throw new IllegalArgumentException(
				"Company ID " + company.getCompanyId() +
					" is the default company ID");
		}

		if (Validator.isNull(ParamUtil.getString(actionRequest, "name"))) {
			throw new CompanyNameException();
		}

		_companyLocalService.validateCompany(
			ParamUtil.getString(actionRequest, "webId"),
			ParamUtil.getString(actionRequest, "virtualHostname"),
			company.getMx(), 0);

		Long destinationCompanyId = _getDestinationCompanyId(actionRequest);

		if ((destinationCompanyId != null) &&
			((destinationCompanyId == 0) ||
			 ArrayUtil.contains(
				 PortalInstancePool.getCompanyIds(), destinationCompanyId))) {

			throw new IllegalArgumentException(
				"Company ID " + destinationCompanyId + " already exists");
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CopyDBPartitionCompanyMVCActionCommand.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<PortalInstanceCopyResource>
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
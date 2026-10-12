/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.instances.web.internal.portlet.action;

import com.liferay.headless.portal.instances.resource.v1_0.PortalInstanceExportResource;
import com.liferay.portal.db.partition.util.DBPartitionUtil;
import com.liferay.portal.kernel.exception.NoSuchCompanyException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResource;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResourceFactory;

import jakarta.portlet.ActionRequest;
import jakarta.portlet.ActionResponse;
import jakarta.portlet.PortletRequest;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Jorge Avalos
 */
public class ExportInstanceMVCActionCommandTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_dbPartitionUtilMockedStatic = Mockito.mockStatic(
			DBPartitionUtil.class);

		_dbPartitionUtilMockedStatic.when(
			() -> DBPartitionUtil.getExportedPartitionName(_COMPANY_ID)
		).thenCallRealMethod();

		Mockito.when(
			_actionRequest.getParameter("portalInstanceId")
		).thenReturn(
			_PORTAL_INSTANCE_ID
		);

		Mockito.when(
			_componentServiceObjects.getService()
		).thenReturn(
			_portalInstanceExportResource
		);

		ReflectionTestUtil.setFieldValue(
			_exportInstanceMVCActionCommand, "_companyLocalService",
			_companyLocalService);
		ReflectionTestUtil.setFieldValue(
			_exportInstanceMVCActionCommand, "_componentServiceObjects",
			_componentServiceObjects);
		ReflectionTestUtil.setFieldValue(
			_exportInstanceMVCActionCommand, "_jsonFactory", _jsonFactory);
		ReflectionTestUtil.setFieldValue(
			_exportInstanceMVCActionCommand, "_language", _language);
		ReflectionTestUtil.setFieldValue(
			_exportInstanceMVCActionCommand, "_portal", _portal);
		ReflectionTestUtil.setFieldValue(
			_exportInstanceMVCActionCommand,
			"_vulcanBatchEngineImportTaskResourceFactory",
			_vulcanBatchEngineImportTaskResourceFactory);

		Company company = Mockito.mock(Company.class);

		Mockito.when(
			company.getCompanyId()
		).thenReturn(
			_COMPANY_ID
		);

		Mockito.when(
			_companyLocalService.getCompanyByWebId(_PORTAL_INSTANCE_ID)
		).thenReturn(
			company
		);

		Mockito.when(
			_jsonFactory.createJSONObject()
		).thenReturn(
			_jsonObject
		);

		Mockito.when(
			_portal.getCompany(_actionRequest)
		).thenReturn(
			Mockito.mock(Company.class)
		);

		Mockito.when(
			_portal.getHttpServletRequest(_actionRequest)
		).thenReturn(
			_httpServletRequest
		);

		Mockito.when(
			_portal.getLocale(_actionRequest)
		).thenReturn(
			LocaleUtil.US
		);

		Mockito.when(
			_portal.getUser(_actionRequest)
		).thenReturn(
			Mockito.mock(User.class)
		);

		Mockito.when(
			_vulcanBatchEngineImportTaskResourceFactory.create()
		).thenReturn(
			_vulcanBatchEngineImportTaskResource
		);
	}

	@After
	public void tearDown() {
		_dbPartitionUtilMockedStatic.close();
	}

	@Test
	public void testDoProcessAction() throws Exception {
		_processAction();

		Mockito.verify(
			_portalInstanceExportResource
		).postPortalInstanceExportBatch(
			Mockito.isNull(), Mockito.any()
		);

		Mockito.verify(
			_jsonObject, Mockito.never()
		).put(
			Mockito.eq("error"), Mockito.any(Object.class)
		);
	}

	@Test
	public void testDoProcessActionWhenTheExportedSchemaExists()
		throws Exception {

		_dbPartitionUtilMockedStatic.when(
			() -> DBPartitionUtil.existsExportedPartition(_COMPANY_ID)
		).thenReturn(
			true
		);

		String message = RandomTestUtil.randomString();

		Mockito.when(
			_language.format(
				LocaleUtil.US, "the-exported-schema-x-already-exists",
				DBPartitionUtil.getExportedPartitionName(_COMPANY_ID))
		).thenReturn(
			message
		);

		_processAction();

		Mockito.verify(
			_jsonObject
		).put(
			"error", message
		);

		Mockito.verifyNoInteractions(_componentServiceObjects);
	}

	@Test
	public void testDoProcessActionWhenThePortalInstanceIsDeleted()
		throws Exception {

		Mockito.when(
			_companyLocalService.getCompanyByWebId(_PORTAL_INSTANCE_ID)
		).thenThrow(
			new NoSuchCompanyException()
		);

		String message = RandomTestUtil.randomString();

		Mockito.when(
			_language.format(
				LocaleUtil.US, "the-instance-x-is-no-longer-available",
				_PORTAL_INSTANCE_ID)
		).thenReturn(
			message
		);

		_processAction();

		Mockito.verify(
			_jsonObject
		).put(
			"error", message
		);

		Mockito.verifyNoInteractions(_componentServiceObjects);
	}

	@Test
	public void testExportPortalInstanceForcesTheJSONContentType()
		throws Exception {

		Mockito.when(
			_httpServletRequest.getHeader("X-Other")
		).thenReturn(
			"delegated"
		);

		_exportPortalInstance();

		ArgumentCaptor<HttpServletRequest> argumentCaptor =
			ArgumentCaptor.forClass(HttpServletRequest.class);

		Mockito.verify(
			_portalInstanceExportResource
		).setContextHttpServletRequest(
			argumentCaptor.capture()
		);

		HttpServletRequest httpServletRequest = argumentCaptor.getValue();

		Assert.assertEquals(
			ContentTypes.APPLICATION_JSON,
			httpServletRequest.getHeader(HttpHeaders.CONTENT_TYPE));
		Assert.assertEquals(
			"delegated", httpServletRequest.getHeader("X-Other"));
	}

	@Test
	public void testExportPortalInstanceSendsThePortalInstanceId()
		throws Exception {

		_exportPortalInstance();

		ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(
			Object.class);

		Mockito.verify(
			_portalInstanceExportResource
		).postPortalInstanceExportBatch(
			Mockito.isNull(), argumentCaptor.capture()
		);

		List<Map<String, String>> maps =
			(List<Map<String, String>>)argumentCaptor.getValue();

		Assert.assertEquals(maps.toString(), 1, maps.size());

		Map<String, String> map = maps.get(0);

		Assert.assertEquals(_PORTAL_INSTANCE_ID, map.get("portalInstanceId"));
	}

	@Test
	public void testExportPortalInstanceSetsThePreferredLocale()
		throws Exception {

		_exportPortalInstance();

		ArgumentCaptor<AcceptLanguage> argumentCaptor = ArgumentCaptor.forClass(
			AcceptLanguage.class);

		Mockito.verify(
			_portalInstanceExportResource
		).setContextAcceptLanguage(
			argumentCaptor.capture()
		);

		AcceptLanguage acceptLanguage = argumentCaptor.getValue();

		Assert.assertEquals(LocaleUtil.US, acceptLanguage.getPreferredLocale());
	}

	@Test
	public void testExportPortalInstanceSetsTheVulcanBatchEngineResource()
		throws Exception {

		_exportPortalInstance();

		Mockito.verify(
			_portalInstanceExportResource
		).setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource
		);
	}

	@Test
	public void testExportPortalInstanceUngetsTheService() throws Exception {
		_exportPortalInstance();

		Mockito.verify(
			_componentServiceObjects
		).ungetService(
			_portalInstanceExportResource
		);
	}

	@Test
	public void testExportPortalInstanceUngetsTheServiceWhenTheBatchFails()
		throws Exception {

		Mockito.when(
			_portalInstanceExportResource.postPortalInstanceExportBatch(
				Mockito.isNull(), Mockito.any())
		).thenThrow(
			new IllegalStateException()
		);

		try {
			_exportPortalInstance();

			Assert.fail();
		}
		catch (IllegalStateException illegalStateException) {
		}

		Mockito.verify(
			_componentServiceObjects
		).ungetService(
			_portalInstanceExportResource
		);
	}

	private void _exportPortalInstance() throws Exception {
		ReflectionTestUtil.invoke(
			_exportInstanceMVCActionCommand, "_exportPortalInstance",
			new Class<?>[] {ActionRequest.class}, _actionRequest);
	}

	private void _processAction() throws Exception {
		Mockito.when(
			_actionRequest.getLocale()
		).thenReturn(
			LocaleUtil.US
		);

		try (MockedStatic<JSONPortletResponseUtil>
				jsonPortletResponseUtilMockedStatic = Mockito.mockStatic(
					JSONPortletResponseUtil.class)) {

			ReflectionTestUtil.invoke(
				_exportInstanceMVCActionCommand, "doProcessAction",
				new Class<?>[] {ActionRequest.class, ActionResponse.class},
				_actionRequest, Mockito.mock(ActionResponse.class));
		}
	}

	private static final long _COMPANY_ID = RandomTestUtil.randomLong();

	private static final String _PORTAL_INSTANCE_ID =
		RandomTestUtil.randomString();

	private final ActionRequest _actionRequest = Mockito.mock(
		ActionRequest.class);
	private final CompanyLocalService _companyLocalService = Mockito.mock(
		CompanyLocalService.class);
	private final ComponentServiceObjects<PortalInstanceExportResource>
		_componentServiceObjects = Mockito.mock(ComponentServiceObjects.class);
	private MockedStatic<DBPartitionUtil> _dbPartitionUtilMockedStatic;

	private final ExportInstanceMVCActionCommand
		_exportInstanceMVCActionCommand = new ExportInstanceMVCActionCommand() {

			@Override
			protected void hideDefaultSuccessMessage(
				PortletRequest portletRequest) {
			}

		};

	private final HttpServletRequest _httpServletRequest = Mockito.mock(
		HttpServletRequest.class);
	private final JSONFactory _jsonFactory = Mockito.mock(JSONFactory.class);
	private final JSONObject _jsonObject = Mockito.mock(JSONObject.class);
	private final Language _language = Mockito.mock(Language.class);
	private final Portal _portal = Mockito.mock(Portal.class);
	private final PortalInstanceExportResource _portalInstanceExportResource =
		Mockito.mock(PortalInstanceExportResource.class);
	private final VulcanBatchEngineImportTaskResource
		_vulcanBatchEngineImportTaskResource = Mockito.mock(
			VulcanBatchEngineImportTaskResource.class);
	private final VulcanBatchEngineImportTaskResourceFactory
		_vulcanBatchEngineImportTaskResourceFactory = Mockito.mock(
			VulcanBatchEngineImportTaskResourceFactory.class);

}
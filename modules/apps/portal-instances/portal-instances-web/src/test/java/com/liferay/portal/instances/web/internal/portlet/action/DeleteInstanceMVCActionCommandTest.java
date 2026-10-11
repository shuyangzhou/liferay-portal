/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.instances.web.internal.portlet.action;

import com.liferay.headless.portal.instances.resource.v1_0.PortalInstanceResource;
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

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Luis Ortiz
 */
public class DeleteInstanceMVCActionCommandTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		Mockito.when(
			_actionRequest.getParameter("portalInstanceId")
		).thenReturn(
			_PORTAL_INSTANCE_ID
		);

		Mockito.when(
			_componentServiceObjects.getService()
		).thenReturn(
			_portalInstanceResource
		);

		ReflectionTestUtil.setFieldValue(
			_deleteInstanceMVCActionCommand, "_companyLocalService",
			_companyLocalService);
		ReflectionTestUtil.setFieldValue(
			_deleteInstanceMVCActionCommand, "_componentServiceObjects",
			_componentServiceObjects);
		ReflectionTestUtil.setFieldValue(
			_deleteInstanceMVCActionCommand, "_jsonFactory", _jsonFactory);
		ReflectionTestUtil.setFieldValue(
			_deleteInstanceMVCActionCommand, "_language", _language);
		ReflectionTestUtil.setFieldValue(
			_deleteInstanceMVCActionCommand, "_portal", _portal);
		ReflectionTestUtil.setFieldValue(
			_deleteInstanceMVCActionCommand,
			"_vulcanBatchEngineImportTaskResourceFactory",
			_vulcanBatchEngineImportTaskResourceFactory);

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

	@Test
	public void testDeletePortalInstanceForcesTheJSONContentType()
		throws Exception {

		Mockito.when(
			_httpServletRequest.getHeader("X-Other")
		).thenReturn(
			"delegated"
		);

		_processAction();

		ArgumentCaptor<HttpServletRequest> argumentCaptor =
			ArgumentCaptor.forClass(HttpServletRequest.class);

		Mockito.verify(
			_portalInstanceResource
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
	public void testDeletePortalInstanceSendsThePortalInstanceId()
		throws Exception {

		_processAction();

		ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(
			Object.class);

		Mockito.verify(
			_portalInstanceResource
		).deletePortalInstanceBatch(
			Mockito.isNull(), argumentCaptor.capture()
		);

		List<Map<String, String>> maps =
			(List<Map<String, String>>)argumentCaptor.getValue();

		Assert.assertEquals(maps.toString(), 1, maps.size());

		Map<String, String> map = maps.get(0);

		Assert.assertEquals(_PORTAL_INSTANCE_ID, map.get("portalInstanceId"));
	}

	@Test
	public void testDeletePortalInstanceSetsThePreferredLocale()
		throws Exception {

		_processAction();

		ArgumentCaptor<AcceptLanguage> argumentCaptor = ArgumentCaptor.forClass(
			AcceptLanguage.class);

		Mockito.verify(
			_portalInstanceResource
		).setContextAcceptLanguage(
			argumentCaptor.capture()
		);

		AcceptLanguage acceptLanguage = argumentCaptor.getValue();

		Assert.assertEquals(LocaleUtil.US, acceptLanguage.getPreferredLocale());
	}

	@Test
	public void testDeletePortalInstanceSetsTheVulcanBatchEngineResource()
		throws Exception {

		_processAction();

		Mockito.verify(
			_portalInstanceResource
		).setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource
		);
	}

	@Test
	public void testDeletePortalInstanceUngetsTheService() throws Exception {
		_processAction();

		Mockito.verify(
			_componentServiceObjects
		).ungetService(
			_portalInstanceResource
		);
	}

	@Test
	public void testDoProcessAction() throws Exception {
		_processAction();

		Mockito.verify(
			_portalInstanceResource
		).deletePortalInstanceBatch(
			Mockito.isNull(), Mockito.any()
		);

		Mockito.verify(
			_jsonObject, Mockito.never()
		).put(
			Mockito.eq("error"), Mockito.any(Object.class)
		);
	}

	@Test
	public void testDoProcessActionWhenTheBatchFails() throws Exception {
		Mockito.when(
			_portalInstanceResource.deletePortalInstanceBatch(
				Mockito.isNull(), Mockito.any())
		).thenThrow(
			new IllegalStateException()
		);

		String message = RandomTestUtil.randomString();

		Mockito.when(
			_language.get(LocaleUtil.US, "an-unexpected-error-occurred")
		).thenReturn(
			message
		);

		_processAction();

		Mockito.verify(
			_componentServiceObjects
		).ungetService(
			_portalInstanceResource
		);

		Mockito.verify(
			_jsonObject
		).put(
			"error", message
		);
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
				_deleteInstanceMVCActionCommand, "doProcessAction",
				new Class<?>[] {ActionRequest.class, ActionResponse.class},
				_actionRequest, Mockito.mock(ActionResponse.class));
		}
	}

	private static final String _PORTAL_INSTANCE_ID =
		RandomTestUtil.randomString();

	private final ActionRequest _actionRequest = Mockito.mock(
		ActionRequest.class);
	private final CompanyLocalService _companyLocalService = Mockito.mock(
		CompanyLocalService.class);
	private final ComponentServiceObjects<PortalInstanceResource>
		_componentServiceObjects = Mockito.mock(ComponentServiceObjects.class);

	private final DeleteInstanceMVCActionCommand
		_deleteInstanceMVCActionCommand = new DeleteInstanceMVCActionCommand() {

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
	private final PortalInstanceResource _portalInstanceResource = Mockito.mock(
		PortalInstanceResource.class);
	private final VulcanBatchEngineImportTaskResource
		_vulcanBatchEngineImportTaskResource = Mockito.mock(
			VulcanBatchEngineImportTaskResource.class);
	private final VulcanBatchEngineImportTaskResourceFactory
		_vulcanBatchEngineImportTaskResourceFactory = Mockito.mock(
			VulcanBatchEngineImportTaskResourceFactory.class);

}
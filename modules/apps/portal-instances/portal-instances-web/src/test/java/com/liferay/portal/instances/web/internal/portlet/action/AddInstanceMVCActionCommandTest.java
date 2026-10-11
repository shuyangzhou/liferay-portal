/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.instances.web.internal.portlet.action;

import com.liferay.headless.portal.instances.resource.v1_0.PortalInstanceResource;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.CompanyMaxUsersException;
import com.liferay.portal.kernel.exception.CompanyMxException;
import com.liferay.portal.kernel.exception.CompanyVirtualHostException;
import com.liferay.portal.kernel.exception.CompanyWebIdException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.security.auth.EmailAddressValidator;
import com.liferay.portal.kernel.security.auth.ScreenNameValidator;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropsValues;
import com.liferay.portal.security.auth.EmailAddressValidatorFactory;
import com.liferay.portal.security.auth.ScreenNameValidatorFactory;
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
 * @author Luis Ortiz
 */
public class AddInstanceMVCActionCommandTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_defaultAdminPassword = ReflectionTestUtil.getFieldValue(
			PropsValues.class, "DEFAULT_ADMIN_PASSWORD");

		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "DEFAULT_ADMIN_PASSWORD", StringPool.BLANK);

		Mockito.when(
			_componentServiceObjects.getService()
		).thenReturn(
			_portalInstanceResource
		);

		ReflectionTestUtil.setFieldValue(
			_addInstanceMVCActionCommand, "_companyLocalService",
			_companyLocalService);
		ReflectionTestUtil.setFieldValue(
			_addInstanceMVCActionCommand, "_componentServiceObjects",
			_componentServiceObjects);
		ReflectionTestUtil.setFieldValue(
			_addInstanceMVCActionCommand, "_jsonFactory", _jsonFactory);
		ReflectionTestUtil.setFieldValue(
			_addInstanceMVCActionCommand, "_language", _language);
		ReflectionTestUtil.setFieldValue(
			_addInstanceMVCActionCommand, "_portal", _portal);
		ReflectionTestUtil.setFieldValue(
			_addInstanceMVCActionCommand,
			"_vulcanBatchEngineImportTaskResourceFactory",
			_vulcanBatchEngineImportTaskResourceFactory);

		_setParameter("active", "true");
		_setParameter("defaultAdminEmailAddress", _EMAIL_ADDRESS);
		_setParameter("defaultAdminFirstName", _GIVEN_NAME);
		_setParameter("defaultAdminLastName", _FAMILY_NAME);
		_setParameter("defaultAdminMiddleName", _MIDDLE_NAME);
		_setParameter("defaultAdminPassword", _PASSWORD);
		_setParameter("defaultAdminScreenName", _SCREEN_NAME);
		_setParameter("maxUsers", String.valueOf(_MAX_USERS));
		_setParameter("mx", _DOMAIN);
		_setParameter("siteInitializerKey", _SITE_INITIALIZER_KEY);
		_setParameter("virtualHostname", _VIRTUAL_HOST);
		_setParameter("webId", _PORTAL_INSTANCE_ID);

		Mockito.when(
			_actionRequest.getLocale()
		).thenReturn(
			LocaleUtil.US
		);

		Mockito.when(
			_emailAddressValidator.validate(0, _EMAIL_ADDRESS)
		).thenReturn(
			true
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
			_screenNameValidator.validate(0, _SCREEN_NAME)
		).thenReturn(
			true
		);

		Mockito.when(
			_vulcanBatchEngineImportTaskResourceFactory.create()
		).thenReturn(
			_vulcanBatchEngineImportTaskResource
		);
	}

	@After
	public void tearDown() {
		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "DEFAULT_ADMIN_PASSWORD", _defaultAdminPassword);
	}

	@Test
	public void testAddPortalInstanceForcesTheJSONContentType()
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
	public void testAddPortalInstanceSendsTheAdmin() throws Exception {
		_processAction();

		Map<String, Object> portalInstanceMap = _capturePortalInstanceMap();

		Map<String, String> adminMap =
			(Map<String, String>)portalInstanceMap.get("admin");

		Assert.assertEquals(_EMAIL_ADDRESS, adminMap.get("emailAddress"));
		Assert.assertEquals(_FAMILY_NAME, adminMap.get("familyName"));
		Assert.assertEquals(_GIVEN_NAME, adminMap.get("givenName"));
		Assert.assertEquals(_MIDDLE_NAME, adminMap.get("middleName"));
		Assert.assertEquals(_PASSWORD, adminMap.get("password"));
		Assert.assertEquals(_SCREEN_NAME, adminMap.get("screenName"));
	}

	@Test
	public void testAddPortalInstanceSendsThePortalInstance() throws Exception {
		_processAction();

		Map<String, Object> portalInstanceMap = _capturePortalInstanceMap();

		Assert.assertEquals(Boolean.TRUE, portalInstanceMap.get("active"));
		Assert.assertEquals(_DOMAIN, portalInstanceMap.get("domain"));
		Assert.assertEquals(_MAX_USERS, portalInstanceMap.get("maxUsers"));
		Assert.assertEquals(
			_PORTAL_INSTANCE_ID, portalInstanceMap.get("portalInstanceId"));
		Assert.assertEquals(
			_SITE_INITIALIZER_KEY, portalInstanceMap.get("siteInitializerKey"));
		Assert.assertEquals(
			_VIRTUAL_HOST, portalInstanceMap.get("virtualHost"));
	}

	@Test
	public void testAddPortalInstanceSetsThePreferredLocale() throws Exception {
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
	public void testAddPortalInstanceSetsTheVulcanBatchEngineResource()
		throws Exception {

		_processAction();

		Mockito.verify(
			_portalInstanceResource
		).setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource
		);
	}

	@Test
	public void testAddPortalInstanceUngetsTheService() throws Exception {
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
			_companyLocalService
		).validateCompany(
			_PORTAL_INSTANCE_ID, _VIRTUAL_HOST, _DOMAIN, _MAX_USERS
		);

		Mockito.verify(
			_emailAddressValidator
		).validate(
			0, _EMAIL_ADDRESS
		);

		Mockito.verify(
			_jsonObject, Mockito.never()
		).put(
			Mockito.eq("error"), Mockito.any(Object.class)
		);

		Mockito.verify(
			_portalInstanceResource
		).postPortalInstanceBatch(
			Mockito.isNull(), Mockito.any()
		);

		Mockito.verify(
			_screenNameValidator
		).validate(
			0, _SCREEN_NAME
		);
	}

	@Test
	public void testDoProcessActionWhenTheAdminEmailAddressIsInvalid()
		throws Exception {

		Mockito.when(
			_emailAddressValidator.validate(0, _EMAIL_ADDRESS)
		).thenReturn(
			false
		);

		_assertDoProcessActionError("please-enter-a-valid-email-address");
	}

	@Test
	public void testDoProcessActionWhenTheAdminEmailAddressIsNull()
		throws Exception {

		_setParameter("defaultAdminEmailAddress", null);

		_assertDoProcessActionError("please-enter-a-valid-email-address");
	}

	@Test
	public void testDoProcessActionWhenTheAdminPasswordIsNull()
		throws Exception {

		_setParameter("defaultAdminPassword", null);

		_assertDoProcessActionError("please-enter-a-valid-password");
	}

	@Test
	public void testDoProcessActionWhenTheAdminScreenNameIsInvalid()
		throws Exception {

		Mockito.when(
			_screenNameValidator.validate(0, _SCREEN_NAME)
		).thenReturn(
			false
		);

		_assertDoProcessActionError("please-enter-a-valid-screen-name");
	}

	@Test
	public void testDoProcessActionWhenTheAdminScreenNameIsNull()
		throws Exception {

		_setParameter("defaultAdminScreenName", null);

		_assertDoProcessActionError("please-enter-a-valid-screen-name");
	}

	@Test
	public void testDoProcessActionWhenTheBatchFails() throws Exception {
		Mockito.when(
			_portalInstanceResource.postPortalInstanceBatch(
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
	public void testDoProcessActionWhenTheCompanyDomainIsInvalid()
		throws Exception {

		_assertDoProcessActionError(
			new CompanyMxException(), "please-enter-a-valid-mail-domain");
	}

	@Test
	public void testDoProcessActionWhenTheCompanyMaxUsersIsInvalid()
		throws Exception {

		_assertDoProcessActionError(
			new CompanyMaxUsersException(), "please-enter-a-valid-max-users");
	}

	@Test
	public void testDoProcessActionWhenTheCompanyVirtualHostIsInvalid()
		throws Exception {

		_assertDoProcessActionError(
			new CompanyVirtualHostException(),
			"please-enter-a-valid-virtual-host");
	}

	@Test
	public void testDoProcessActionWhenTheCompanyWebIdIsInvalid()
		throws Exception {

		_assertDoProcessActionError(
			new CompanyWebIdException(), "please-enter-a-valid-web-id");
	}

	@Test
	public void testDoProcessActionWhenTheDefaultAdminPasswordIsSet()
		throws Exception {

		_setParameter("defaultAdminEmailAddress", null);
		_setParameter("defaultAdminPassword", null);
		_setParameter("defaultAdminScreenName", null);

		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "DEFAULT_ADMIN_PASSWORD",
			RandomTestUtil.randomString());

		_processAction();

		Mockito.verify(
			_jsonObject, Mockito.never()
		).put(
			Mockito.eq("error"), Mockito.any(Object.class)
		);

		Mockito.verify(
			_portalInstanceResource
		).postPortalInstanceBatch(
			Mockito.isNull(), Mockito.any()
		);

		Mockito.verifyNoInteractions(
			_emailAddressValidator, _screenNameValidator);
	}

	@Test
	public void testGetPortalInstanceMapOmitsABlankSiteInitializerKey()
		throws Exception {

		_setParameter("siteInitializerKey", StringPool.BLANK);

		_processAction();

		Map<String, Object> portalInstanceMap = _capturePortalInstanceMap();

		Assert.assertFalse(
			portalInstanceMap.toString(),
			portalInstanceMap.containsKey("siteInitializerKey"));
	}

	@Test
	public void testGetPortalInstanceMapOmitsTheAdminWithoutAnEmailAddress()
		throws Exception {

		_setParameter("defaultAdminEmailAddress", null);

		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "DEFAULT_ADMIN_PASSWORD",
			RandomTestUtil.randomString());

		_processAction();

		Map<String, Object> portalInstanceMap = _capturePortalInstanceMap();

		Assert.assertFalse(
			portalInstanceMap.toString(),
			portalInstanceMap.containsKey("admin"));
	}

	private void _assertDoProcessActionError(
			PortalException portalException, String key)
		throws Exception {

		Mockito.doThrow(
			portalException
		).when(
			_companyLocalService
		).validateCompany(
			_PORTAL_INSTANCE_ID, _VIRTUAL_HOST, _DOMAIN, _MAX_USERS
		);

		_assertDoProcessActionError(key);
	}

	private void _assertDoProcessActionError(String key) throws Exception {
		String message = RandomTestUtil.randomString();

		Mockito.when(
			_language.get(LocaleUtil.US, key)
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

	private Map<String, Object> _capturePortalInstanceMap() throws Exception {
		ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(
			Object.class);

		Mockito.verify(
			_portalInstanceResource
		).postPortalInstanceBatch(
			Mockito.isNull(), argumentCaptor.capture()
		);

		List<Map<String, Object>> maps =
			(List<Map<String, Object>>)argumentCaptor.getValue();

		Assert.assertEquals(maps.toString(), 1, maps.size());

		return maps.get(0);
	}

	private void _processAction() throws Exception {
		try (MockedStatic<EmailAddressValidatorFactory>
				emailAddressValidatorFactoryMockedStatic = Mockito.mockStatic(
					EmailAddressValidatorFactory.class);
			MockedStatic<JSONPortletResponseUtil>
				jsonPortletResponseUtilMockedStatic = Mockito.mockStatic(
					JSONPortletResponseUtil.class);
			MockedStatic<ScreenNameValidatorFactory>
				screenNameValidatorFactoryMockedStatic = Mockito.mockStatic(
					ScreenNameValidatorFactory.class)) {

			emailAddressValidatorFactoryMockedStatic.when(
				EmailAddressValidatorFactory::getInstance
			).thenReturn(
				_emailAddressValidator
			);

			screenNameValidatorFactoryMockedStatic.when(
				ScreenNameValidatorFactory::getInstance
			).thenReturn(
				_screenNameValidator
			);

			ReflectionTestUtil.invoke(
				_addInstanceMVCActionCommand, "doProcessAction",
				new Class<?>[] {ActionRequest.class, ActionResponse.class},
				_actionRequest, Mockito.mock(ActionResponse.class));
		}
	}

	private void _setParameter(String name, String value) {
		Mockito.when(
			_actionRequest.getParameter(name)
		).thenReturn(
			value
		);
	}

	private static final String _DOMAIN = RandomTestUtil.randomString();

	private static final String _EMAIL_ADDRESS = RandomTestUtil.randomString();

	private static final String _FAMILY_NAME = RandomTestUtil.randomString();

	private static final String _GIVEN_NAME = RandomTestUtil.randomString();

	private static final int _MAX_USERS = RandomTestUtil.randomInt(1, 100);

	private static final String _MIDDLE_NAME = RandomTestUtil.randomString();

	private static final String _PASSWORD = RandomTestUtil.randomString();

	private static final String _PORTAL_INSTANCE_ID =
		RandomTestUtil.randomString();

	private static final String _SCREEN_NAME = RandomTestUtil.randomString();

	private static final String _SITE_INITIALIZER_KEY =
		RandomTestUtil.randomString();

	private static final String _VIRTUAL_HOST = RandomTestUtil.randomString();

	private final ActionRequest _actionRequest = Mockito.mock(
		ActionRequest.class);

	private final AddInstanceMVCActionCommand _addInstanceMVCActionCommand =
		new AddInstanceMVCActionCommand() {

			@Override
			protected void hideDefaultSuccessMessage(
				PortletRequest portletRequest) {
			}

		};

	private final CompanyLocalService _companyLocalService = Mockito.mock(
		CompanyLocalService.class);
	private final ComponentServiceObjects<PortalInstanceResource>
		_componentServiceObjects = Mockito.mock(ComponentServiceObjects.class);
	private String _defaultAdminPassword;
	private final EmailAddressValidator _emailAddressValidator = Mockito.mock(
		EmailAddressValidator.class);
	private final HttpServletRequest _httpServletRequest = Mockito.mock(
		HttpServletRequest.class);
	private final JSONFactory _jsonFactory = Mockito.mock(JSONFactory.class);
	private final JSONObject _jsonObject = Mockito.mock(JSONObject.class);
	private final Language _language = Mockito.mock(Language.class);
	private final Portal _portal = Mockito.mock(Portal.class);
	private final PortalInstanceResource _portalInstanceResource = Mockito.mock(
		PortalInstanceResource.class);
	private final ScreenNameValidator _screenNameValidator = Mockito.mock(
		ScreenNameValidator.class);
	private final VulcanBatchEngineImportTaskResource
		_vulcanBatchEngineImportTaskResource = Mockito.mock(
			VulcanBatchEngineImportTaskResource.class);
	private final VulcanBatchEngineImportTaskResourceFactory
		_vulcanBatchEngineImportTaskResourceFactory = Mockito.mock(
			VulcanBatchEngineImportTaskResourceFactory.class);

}
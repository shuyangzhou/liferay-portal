/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.portal.instances.internal.batch.engine.exception.handler;

import com.liferay.batch.engine.BatchEngineTaskOperation;
import com.liferay.batch.engine.model.BatchEngineImportTask;
import com.liferay.batch.engine.thread.local.BatchEngineThreadLocal;
import com.liferay.headless.portal.instances.dto.v1_0.PortalInstance;
import com.liferay.headless.portal.instances.dto.v1_0.PortalInstanceCopy;
import com.liferay.headless.portal.instances.dto.v1_0.PortalInstanceExport;
import com.liferay.headless.portal.instances.dto.v1_0.PortalInstanceImport;
import com.liferay.petra.lang.SafeCloseable;
import com.liferay.portal.instances.constants.PortalInstancesPortletKeys;
import com.liferay.portal.kernel.exception.CompanyMaxUsersException;
import com.liferay.portal.kernel.exception.CompanyMxException;
import com.liferay.portal.kernel.exception.CompanyNameException;
import com.liferay.portal.kernel.exception.CompanyVirtualHostException;
import com.liferay.portal.kernel.exception.CompanyWebIdException;
import com.liferay.portal.kernel.exception.ContactNameException;
import com.liferay.portal.kernel.exception.NoSuchCompanyException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.RequiredCompanyException;
import com.liferay.portal.kernel.exception.UserEmailAddressException;
import com.liferay.portal.kernel.exception.UserPasswordException;
import com.liferay.portal.kernel.exception.UserScreenNameException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.UserNotificationDeliveryConstants;
import com.liferay.portal.kernel.security.auth.FullNameValidator;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserNotificationEventLocalService;
import com.liferay.portal.kernel.service.UserNotificationEventLocalServiceUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import jakarta.ws.rs.BadRequestException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * @author Luis Ortiz
 */
public class PortalInstanceBatchEngineImportTaskExceptionHandlerTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_safeCloseable =
			BatchEngineThreadLocal.setBatchImportInProcessWithSafeCloseable(
				true);

		ReflectionTestUtil.setFieldValue(
			_portalInstanceBatchEngineImportTaskExceptionHandler,
			"_companyLocalService", _companyLocalService);

		UserNotificationEventLocalServiceUtil.setService(
			_userNotificationEventLocalService);

		Company company = Mockito.mock(Company.class);

		Mockito.when(
			company.getCompanyId()
		).thenReturn(
			_COMPANY_ID
		);

		Mockito.when(
			_companyLocalService.getCompanyByWebId(Mockito.anyString())
		).thenReturn(
			company
		);

		Mockito.when(
			_batchEngineImportTask.getOperation()
		).thenReturn(
			BatchEngineTaskOperation.DELETE.name()
		);

		Mockito.when(
			_batchEngineImportTask.getUserId()
		).thenReturn(
			_USER_ID
		);
	}

	@After
	public void tearDown() {
		_safeCloseable.close();
	}

	@Test
	public void testHandleIgnoresAnotherOperation() {
		Mockito.when(
			_batchEngineImportTask.getOperation()
		).thenReturn(
			BatchEngineTaskOperation.UPDATE.name()
		);

		_handle(new RequiredCompanyException(), RandomTestUtil.randomString());

		Mockito.verifyNoInteractions(_userNotificationEventLocalService);
	}

	@Test
	public void testHandleIgnoresItemsOfAnotherType() {
		_portalInstanceBatchEngineImportTaskExceptionHandler.handle(
			_batchEngineImportTask, null, new RequiredCompanyException(),
			RandomTestUtil.randomString(), RandomTestUtil.randomString());

		Mockito.verifyNoInteractions(_userNotificationEventLocalService);
	}

	@Test
	public void testHandleIgnoresTheCopyItemForTheDeleteOperation() {
		_handleCopy(
			new IllegalArgumentException(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString());

		Mockito.verifyNoInteractions(_userNotificationEventLocalService);
	}

	@Test
	public void testHandleIgnoresTheExportItemForTheDeleteOperation() {
		_handleExport(
			new IllegalArgumentException(), RandomTestUtil.randomString());

		Mockito.verifyNoInteractions(_userNotificationEventLocalService);
	}

	@Test
	public void testHandleIgnoresTheImportItemForTheDeleteOperation() {
		_handleImport(
			new IllegalArgumentException(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString());

		Mockito.verifyNoInteractions(_userNotificationEventLocalService);
	}

	@Test
	public void testHandleMapsCopyExceptions() throws Exception {
		Mockito.when(
			_batchEngineImportTask.getOperation()
		).thenReturn(
			BatchEngineTaskOperation.CREATE.name()
		);

		_assertCopyErrorMessageKey(null, new Exception());
		_assertCopyErrorMessageKey(
			null, Mockito.mock(BadRequestException.class));
		_assertCopyErrorMessageKey(null, new UnsupportedOperationException());
		_assertCopyErrorMessageKey(
			"copying-an-instance-is-already-in-progress",
			new UnsupportedOperationException(
				"Copying an instance is already in progress"));
		_assertCopyErrorMessageKey(
			"database-partitioning-must-be-enabled",
			new UnsupportedOperationException(
				"Database partitioning must be enabled"));
		_assertCopyErrorMessageKey(
			"please-enter-a-valid-destination-company-id",
			new IllegalArgumentException());
		_assertCopyErrorMessageKey(
			"please-enter-a-valid-destination-company-id",
			new IllegalArgumentException(
				"Company ID " + RandomTestUtil.randomLong() +
					" already exists"));
		_assertCopyErrorMessageKey(
			"please-enter-a-valid-name", new CompanyNameException());
		_assertCopyErrorMessageKey(
			"please-enter-a-valid-name",
			new Exception(new CompanyNameException()));
		_assertCopyErrorMessageKey(
			"please-enter-a-valid-virtual-host",
			new CompanyVirtualHostException());
		_assertCopyErrorMessageKey(
			"please-enter-a-valid-virtual-host",
			new Exception(new CompanyVirtualHostException()));
		_assertCopyErrorMessageKey(
			"please-enter-a-valid-web-id", new CompanyWebIdException());
		_assertCopyErrorMessageKey(
			"please-enter-a-valid-web-id",
			new Exception(new CompanyWebIdException()));
		_assertCopyErrorMessageKey(
			"the-default-instance-cannot-be-copied",
			new IllegalArgumentException(
				"Company ID " + RandomTestUtil.randomLong() +
					" is the default company ID"));
	}

	@Test
	public void testHandleMapsExceptions() throws Exception {
		_assertErrorMessageKey(null, new Exception());
		_assertErrorMessageKey(
			null, new IllegalArgumentException(RandomTestUtil.randomString()));
		_assertErrorMessageKey(
			null,
			new NoSuchCompanyException(
				"No Company exists with the key {webId=missing}"));
		_assertErrorMessageKey(null, new PortalException(new Exception()));
		_assertErrorMessageKey(
			"please-enter-a-valid-email-address",
			new UserEmailAddressException.MustNotBeNull());
		_assertErrorMessageKey(
			"please-enter-a-valid-first-middle-and-last-name",
			new ContactNameException.MustHaveValidFullName(
				Mockito.mock(FullNameValidator.class)));
		_assertErrorMessageKey(
			"please-enter-a-valid-first-name",
			new ContactNameException.MustHaveFirstName());
		_assertErrorMessageKey(
			"please-enter-a-valid-first-name",
			new PortalException(new ContactNameException.MustHaveFirstName()));
		_assertErrorMessageKey(
			"please-enter-a-valid-last-name",
			new ContactNameException.MustHaveLastName());
		_assertErrorMessageKey(
			"please-enter-a-valid-mail-domain", new CompanyMxException());
		_assertErrorMessageKey(
			"please-enter-a-valid-max-users", new CompanyMaxUsersException());
		_assertErrorMessageKey(
			"please-enter-a-valid-middle-name",
			new ContactNameException.MustHaveMiddleName());
		_assertErrorMessageKey(
			"please-enter-a-valid-password",
			new PortalException(
				new UserPasswordException.MustHaveMoreNumbers(
					RandomTestUtil.randomInt())));
		_assertErrorMessageKey(
			"please-enter-a-valid-password",
			new UserPasswordException.MustHaveMoreNumbers(
				RandomTestUtil.randomInt()));
		_assertErrorMessageKey(
			"please-enter-a-valid-screen-name",
			new PortalException(new UserScreenNameException.MustNotBeNull()));
		_assertErrorMessageKey(
			"please-enter-a-valid-screen-name",
			new UserScreenNameException.MustNotBeNull());
		_assertErrorMessageKey(
			"please-enter-a-valid-virtual-host",
			new CompanyVirtualHostException());
		_assertErrorMessageKey(
			"please-enter-a-valid-web-id", new CompanyWebIdException());
		_assertErrorMessageKey(
			"please-select-a-valid-virtual-instance-initializer",
			new IllegalArgumentException(
				"Site initializer does-not-exist does not exist"));
		_assertErrorMessageKey(
			"please-select-a-valid-virtual-instance-initializer",
			new IllegalArgumentException(
				"Site initializer inactive is inactive"));
		_assertErrorMessageKey(
			"the-default-instance-cannot-be-deleted",
			new RequiredCompanyException());
	}

	@Test
	public void testHandleMapsExportExceptions() throws Exception {
		Mockito.when(
			_batchEngineImportTask.getOperation()
		).thenReturn(
			BatchEngineTaskOperation.CREATE.name()
		);

		_assertExportErrorMessageKey(null, new Exception());
		_assertExportErrorMessageKey(
			null, Mockito.mock(BadRequestException.class));
		_assertExportErrorMessageKey(
			null,
			new NoSuchCompanyException(
				"No Company exists with the key {webId=missing}"));
		_assertExportErrorMessageKey(
			"the-default-instance-cannot-be-exported",
			new RequiredCompanyException());
		_assertExportErrorMessageKey(
			"the-exported-schema-x-already-exists",
			new IllegalArgumentException());
	}

	@Test
	public void testHandleMapsImportExceptions() throws Exception {
		Mockito.when(
			_batchEngineImportTask.getOperation()
		).thenReturn(
			BatchEngineTaskOperation.CREATE.name()
		);

		_assertImportErrorMessageKey(
			"an-instance-for-this-schema-already-exists",
			new IllegalArgumentException(
				"Database partition " + RandomTestUtil.randomString()));
		_assertImportErrorMessageKey(null, new Exception());
		_assertImportErrorMessageKey(
			null, Mockito.mock(BadRequestException.class));
		_assertImportErrorMessageKey(null, new IllegalArgumentException());
		_assertImportErrorMessageKey(null, new UnsupportedOperationException());
		_assertImportErrorMessageKey(
			"database-partitioning-must-be-enabled",
			new UnsupportedOperationException(
				"Database partitioning must be enabled"));
		_assertImportErrorMessageKey(
			"importing-an-instance-is-already-in-progress",
			new UnsupportedOperationException(
				"Importing an instance is already in progress"));
		_assertImportErrorMessageKey(
			"please-enter-a-valid-name", new CompanyNameException());
		_assertImportErrorMessageKey(
			"please-enter-a-valid-name",
			new Exception(new CompanyNameException()));
		_assertImportErrorMessageKey(
			"please-enter-a-valid-schema-name",
			new IllegalArgumentException(
				"Company ID " + RandomTestUtil.randomLong() +
					" is the default company ID"));
		_assertImportErrorMessageKey(
			"please-enter-a-valid-schema-name",
			new IllegalArgumentException(
				"Invalid schema name " + RandomTestUtil.randomString()));
		_assertImportErrorMessageKey(
			"please-enter-a-valid-virtual-host",
			new CompanyVirtualHostException());
		_assertImportErrorMessageKey(
			"please-enter-a-valid-virtual-host",
			new Exception(new CompanyVirtualHostException()));
		_assertImportErrorMessageKey(
			"please-enter-a-valid-web-id", new CompanyWebIdException());
		_assertImportErrorMessageKey(
			"please-enter-a-valid-web-id",
			new Exception(new CompanyWebIdException()));
		_assertImportErrorMessageKey(
			"the-exported-schema-does-not-exist",
			new IllegalArgumentException(
				"Unable to insert the database partition " +
					RandomTestUtil.randomString()));
	}

	@Test
	public void testHandleSendsUserNotificationEvent() throws Exception {
		String portalInstanceId = RandomTestUtil.randomString();

		_handle(new RequiredCompanyException(), portalInstanceId);

		JSONObject payloadJSONObject = _capturePayloadJSONObject();

		Assert.assertEquals(
			_MESSAGE, payloadJSONObject.getString("errorMessage"));
		Assert.assertEquals(
			"DELETE", payloadJSONObject.getString("operationType"));
		Assert.assertEquals(
			portalInstanceId, payloadJSONObject.getString("portalInstanceId"));
		Assert.assertEquals("FAILED", payloadJSONObject.getString("status"));
	}

	@Test
	public void testHandleSendsUserNotificationEventForTheAddOperation()
		throws Exception {

		Mockito.when(
			_batchEngineImportTask.getOperation()
		).thenReturn(
			BatchEngineTaskOperation.CREATE.name()
		);

		String portalInstanceId = RandomTestUtil.randomString();

		_handle(new CompanyWebIdException(), portalInstanceId);

		JSONObject payloadJSONObject = _capturePayloadJSONObject();

		Assert.assertEquals(
			_MESSAGE, payloadJSONObject.getString("errorMessage"));
		Assert.assertEquals(
			"ADD", payloadJSONObject.getString("operationType"));
		Assert.assertEquals(
			portalInstanceId, payloadJSONObject.getString("portalInstanceId"));
		Assert.assertEquals("FAILED", payloadJSONObject.getString("status"));
	}

	@Test
	public void testHandleSendsUserNotificationEventForTheCopyOperation()
		throws Exception {

		Mockito.when(
			_batchEngineImportTask.getOperation()
		).thenReturn(
			BatchEngineTaskOperation.CREATE.name()
		);

		String sourcePortalInstanceId = RandomTestUtil.randomString();
		String webId = RandomTestUtil.randomString();

		_handleCopy(new CompanyWebIdException(), sourcePortalInstanceId, webId);

		JSONObject payloadJSONObject = _capturePayloadJSONObject();

		Assert.assertEquals(
			_MESSAGE, payloadJSONObject.getString("errorMessage"));
		Assert.assertEquals(
			"please-enter-a-valid-web-id",
			payloadJSONObject.getString("errorMessageKey"));
		Assert.assertEquals(
			"COPY", payloadJSONObject.getString("operationType"));
		Assert.assertEquals(
			webId, payloadJSONObject.getString("portalInstanceId"));
		Assert.assertEquals(
			sourcePortalInstanceId,
			payloadJSONObject.getString("sourcePortalInstanceId"));
		Assert.assertEquals("FAILED", payloadJSONObject.getString("status"));

		Mockito.verifyNoInteractions(_companyLocalService);
	}

	@Test
	public void testHandleSendsUserNotificationEventForTheExportOperation()
		throws Exception {

		Mockito.when(
			_batchEngineImportTask.getOperation()
		).thenReturn(
			BatchEngineTaskOperation.CREATE.name()
		);

		String portalInstanceId = RandomTestUtil.randomString();

		_handleExport(new IllegalArgumentException(), portalInstanceId);

		JSONObject payloadJSONObject = _capturePayloadJSONObject();

		Assert.assertEquals(
			_MESSAGE, payloadJSONObject.getString("errorMessage"));
		Assert.assertEquals(
			"EXPORT", payloadJSONObject.getString("operationType"));
		Assert.assertEquals(
			portalInstanceId, payloadJSONObject.getString("portalInstanceId"));
		Assert.assertEquals(
			"lexported_" + _COMPANY_ID,
			payloadJSONObject.getString("schemaName"));
		Assert.assertEquals("FAILED", payloadJSONObject.getString("status"));
	}

	@Test
	public void testHandleSendsUserNotificationEventForTheImportOperation()
		throws Exception {

		Mockito.when(
			_batchEngineImportTask.getOperation()
		).thenReturn(
			BatchEngineTaskOperation.CREATE.name()
		);

		String schemaName = RandomTestUtil.randomString();
		String webId = RandomTestUtil.randomString();

		_handleImport(new CompanyWebIdException(), schemaName, webId);

		JSONObject payloadJSONObject = _capturePayloadJSONObject();

		Assert.assertEquals(
			_MESSAGE, payloadJSONObject.getString("errorMessage"));
		Assert.assertEquals(
			"please-enter-a-valid-web-id",
			payloadJSONObject.getString("errorMessageKey"));
		Assert.assertEquals(
			"IMPORT", payloadJSONObject.getString("operationType"));
		Assert.assertEquals(
			webId, payloadJSONObject.getString("portalInstanceId"));
		Assert.assertEquals(
			schemaName, payloadJSONObject.getString("schemaName"));
		Assert.assertEquals("FAILED", payloadJSONObject.getString("status"));

		Mockito.verifyNoInteractions(_companyLocalService);
	}

	private void _assertCopyErrorMessageKey(
			String errorMessageKey, Exception exception)
		throws Exception {

		Mockito.clearInvocations(_userNotificationEventLocalService);

		_handleCopy(
			exception, RandomTestUtil.randomString(),
			RandomTestUtil.randomString());

		JSONObject payloadJSONObject = _capturePayloadJSONObject();

		Assert.assertEquals(
			errorMessageKey, payloadJSONObject.get("errorMessageKey"));
	}

	private void _assertErrorMessageKey(
			String errorMessageKey, Exception exception)
		throws Exception {

		Mockito.clearInvocations(_userNotificationEventLocalService);

		_handle(exception, RandomTestUtil.randomString());

		JSONObject payloadJSONObject = _capturePayloadJSONObject();

		Assert.assertEquals(
			errorMessageKey, payloadJSONObject.get("errorMessageKey"));
	}

	private void _assertExportErrorMessageKey(
			String errorMessageKey, Exception exception)
		throws Exception {

		Mockito.clearInvocations(_userNotificationEventLocalService);

		_handleExport(exception, RandomTestUtil.randomString());

		JSONObject payloadJSONObject = _capturePayloadJSONObject();

		Assert.assertEquals(
			errorMessageKey, payloadJSONObject.get("errorMessageKey"));
	}

	private void _assertImportErrorMessageKey(
			String errorMessageKey, Exception exception)
		throws Exception {

		Mockito.clearInvocations(_userNotificationEventLocalService);

		_handleImport(
			exception, RandomTestUtil.randomString(),
			RandomTestUtil.randomString());

		JSONObject payloadJSONObject = _capturePayloadJSONObject();

		Assert.assertEquals(
			errorMessageKey, payloadJSONObject.get("errorMessageKey"));
	}

	private JSONObject _capturePayloadJSONObject() throws Exception {
		ArgumentCaptor<JSONObject> argumentCaptor = ArgumentCaptor.forClass(
			JSONObject.class);

		Mockito.verify(
			_userNotificationEventLocalService
		).sendUserNotificationEvents(
			Mockito.eq(_USER_ID),
			Mockito.eq(PortalInstancesPortletKeys.PORTAL_INSTANCES),
			Mockito.eq(UserNotificationDeliveryConstants.TYPE_WEBSITE),
			argumentCaptor.capture()
		);

		return argumentCaptor.getValue();
	}

	private void _handle(Exception exception, String portalInstanceId) {
		PortalInstance portalInstance = new PortalInstance();

		portalInstance.setPortalInstanceId(() -> portalInstanceId);

		_portalInstanceBatchEngineImportTaskExceptionHandler.handle(
			_batchEngineImportTask, null, exception, portalInstance, _MESSAGE);
	}

	private void _handleCopy(
		Exception exception, String sourcePortalInstanceId, String webId) {

		PortalInstanceCopy portalInstanceCopy = new PortalInstanceCopy();

		portalInstanceCopy.setSourcePortalInstanceId(
			() -> sourcePortalInstanceId);
		portalInstanceCopy.setWebId(() -> webId);

		_portalInstanceBatchEngineImportTaskExceptionHandler.handle(
			_batchEngineImportTask, null, exception, portalInstanceCopy,
			_MESSAGE);
	}

	private void _handleExport(Exception exception, String portalInstanceId) {
		PortalInstanceExport portalInstanceExport = new PortalInstanceExport();

		portalInstanceExport.setPortalInstanceId(() -> portalInstanceId);

		_portalInstanceBatchEngineImportTaskExceptionHandler.handle(
			_batchEngineImportTask, null, exception, portalInstanceExport,
			_MESSAGE);
	}

	private void _handleImport(
		Exception exception, String schemaName, String webId) {

		PortalInstanceImport portalInstanceImport = new PortalInstanceImport();

		portalInstanceImport.setSchemaName(() -> schemaName);
		portalInstanceImport.setWebId(() -> webId);

		_portalInstanceBatchEngineImportTaskExceptionHandler.handle(
			_batchEngineImportTask, null, exception, portalInstanceImport,
			_MESSAGE);
	}

	private static final long _COMPANY_ID = RandomTestUtil.randomLong();

	private static final String _MESSAGE = RandomTestUtil.randomString();

	private static final long _USER_ID = RandomTestUtil.randomLong();

	private final BatchEngineImportTask _batchEngineImportTask = Mockito.mock(
		BatchEngineImportTask.class);
	private final CompanyLocalService _companyLocalService = Mockito.mock(
		CompanyLocalService.class);
	private final PortalInstanceBatchEngineImportTaskExceptionHandler
		_portalInstanceBatchEngineImportTaskExceptionHandler =
			new PortalInstanceBatchEngineImportTaskExceptionHandler();
	private SafeCloseable _safeCloseable;
	private final UserNotificationEventLocalService
		_userNotificationEventLocalService = Mockito.mock(
			UserNotificationEventLocalService.class);

}
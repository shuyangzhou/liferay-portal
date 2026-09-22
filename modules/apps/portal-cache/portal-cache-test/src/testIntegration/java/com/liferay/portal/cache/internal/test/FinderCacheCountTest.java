/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.cache.internal.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.Phone;
import com.liferay.portal.kernel.service.persistence.ContactPersistence;
import com.liferay.portal.kernel.service.persistence.PhonePersistence;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LogEntry;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.List;
import java.util.function.IntSupplier;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Shuyang Zhou
 */
@RunWith(Arquillian.class)
public class FinderCacheCountTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testCountAfterAdd() throws Throwable {
		long classNameId = RandomTestUtil.nextLong();
		long classPK = RandomTestUtil.nextLong();

		_assertQueriedCount(
			0, () -> _contactPersistence.countByC_C(classNameId, classPK));

		Contact contact = _addContact(classNameId, classPK);

		try {
			_assertCachedCount(
				1, () -> _contactPersistence.countByC_C(classNameId, classPK));
		}
		finally {
			_removeContact(contact);
		}
	}

	@Test
	public void testCountAfterAddChangeTrackedModel() throws Throwable {
		long classNameId = RandomTestUtil.nextLong();
		long classPK = RandomTestUtil.nextLong();
		long companyId = TestPropsValues.getCompanyId();

		_assertQueriedCount(
			0,
			() -> _phonePersistence.countByC_C_C(
				companyId, classNameId, classPK));

		Phone phone = TransactionInvokerUtil.invoke(
			_transactionConfig, () -> _createPhone(classNameId, classPK));

		try {
			_assertCachedCount(
				1,
				() -> _phonePersistence.countByC_C_C(
					companyId, classNameId, classPK));
		}
		finally {
			TransactionInvokerUtil.invoke(
				_transactionConfig, () -> _phonePersistence.remove(phone));
		}
	}

	@Test
	public void testCountAfterClearCache() throws Throwable {
		long classNameId = RandomTestUtil.nextLong();
		long classPK = RandomTestUtil.nextLong();

		_assertQueriedCount(
			0, () -> _contactPersistence.countByC_C(classNameId, classPK));

		Contact contact = _addContact(classNameId, classPK);

		try {
			_assertCachedCount(
				1, () -> _contactPersistence.countByC_C(classNameId, classPK));

			_clearCache(contact);

			_assertQueriedCount(
				1, () -> _contactPersistence.countByC_C(classNameId, classPK));
		}
		finally {
			_removeContact(contact);
		}
	}

	@Test
	public void testCountAfterDelete() throws Throwable {
		long classNameId = RandomTestUtil.nextLong();
		long classPK = RandomTestUtil.nextLong();

		_assertQueriedCount(
			0, () -> _contactPersistence.countByC_C(classNameId, classPK));

		Contact contact = _addContact(classNameId, classPK);

		_assertCachedCount(
			1, () -> _contactPersistence.countByC_C(classNameId, classPK));

		_removeContact(contact);

		_assertCachedCount(
			0, () -> _contactPersistence.countByC_C(classNameId, classPK));
	}

	@Test
	public void testCountAfterDeleteChangedColumn() throws Throwable {
		long classNameId = RandomTestUtil.nextLong();
		long classPK = RandomTestUtil.nextLong();

		_assertQueriedCount(
			0, () -> _contactPersistence.countByC_C(classNameId, classPK));

		Contact contact = _addContact(classNameId, classPK);

		_assertCachedCount(
			1, () -> _contactPersistence.countByC_C(classNameId, classPK));

		contact.setClassPK(RandomTestUtil.nextLong());

		_removeContact(contact);

		_assertCachedCount(
			0, () -> _contactPersistence.countByC_C(classNameId, classPK));
	}

	@Test
	public void testCountAfterRollback() throws Throwable {
		long classNameId = RandomTestUtil.nextLong();
		long classPK = RandomTestUtil.nextLong();

		_assertQueriedCount(
			0, () -> _contactPersistence.countByC_C(classNameId, classPK));

		try {
			TransactionInvokerUtil.invoke(
				_transactionConfig,
				() -> {
					_createContact(classNameId, classPK);

					throw new PortalException("Roll back");
				});

			Assert.fail();
		}
		catch (Throwable throwable) {
			Assert.assertEquals("Roll back", throwable.getMessage());
		}

		_assertCachedCount(
			0, () -> _contactPersistence.countByC_C(classNameId, classPK));
	}

	@Test
	public void testCountAfterUpdate() throws Throwable {
		long classNameId = RandomTestUtil.nextLong();
		long classPK = RandomTestUtil.nextLong();
		long newClassPK = RandomTestUtil.nextLong();

		_assertQueriedCount(
			0, () -> _contactPersistence.countByC_C(classNameId, classPK));
		_assertQueriedCount(
			0, () -> _contactPersistence.countByC_C(classNameId, newClassPK));

		Contact contact = _addContact(classNameId, classPK);

		try {
			_assertCachedCount(
				1, () -> _contactPersistence.countByC_C(classNameId, classPK));
			_assertCachedCount(
				0,
				() -> _contactPersistence.countByC_C(classNameId, newClassPK));

			contact = _updateContact(contact, newClassPK);

			_assertCachedCount(
				0, () -> _contactPersistence.countByC_C(classNameId, classPK));
			_assertCachedCount(
				1,
				() -> _contactPersistence.countByC_C(classNameId, newClassPK));
		}
		finally {
			_removeContact(contact);
		}
	}

	@Test
	public void testCountInAddingTransaction() throws Throwable {
		long classNameId = RandomTestUtil.nextLong();
		long classPK = RandomTestUtil.nextLong();

		_assertQueriedCount(
			0, () -> _contactPersistence.countByC_C(classNameId, classPK));

		Contact contact = TransactionInvokerUtil.invoke(
			_transactionConfig,
			() -> {
				Contact addedContact = _createContact(classNameId, classPK);

				_assertCachedCount(
					1,
					() -> _contactPersistence.countByC_C(classNameId, classPK));

				return addedContact;
			});

		try {
			_assertCachedCount(
				1, () -> _contactPersistence.countByC_C(classNameId, classPK));
		}
		finally {
			_removeContact(contact);
		}
	}

	private Contact _addContact(long classNameId, long classPK)
		throws Throwable {

		return TransactionInvokerUtil.invoke(
			_transactionConfig, () -> _createContact(classNameId, classPK));
	}

	private void _assertCachedCount(
		int expectedCount, IntSupplier intSupplier) {

		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				"org.hibernate.SQL", LoggerTestUtil.DEBUG)) {

			Assert.assertEquals(expectedCount, _getCount(intSupplier));

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertTrue(logEntries.toString(), logEntries.isEmpty());
		}
	}

	private void _assertQueriedCount(
		int expectedCount, IntSupplier intSupplier) {

		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				"org.hibernate.SQL", LoggerTestUtil.DEBUG)) {

			Assert.assertEquals(expectedCount, _getCount(intSupplier));

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertFalse(logEntries.toString(), logEntries.isEmpty());
		}
	}

	private void _clearCache(Contact contact) throws Throwable {
		TransactionInvokerUtil.invoke(
			_transactionConfig,
			() -> {
				_contactPersistence.clearCache(contact);

				return null;
			});
	}

	private Contact _createContact(long classNameId, long classPK)
		throws Exception {

		Contact contact = _contactPersistence.create(
			_counterLocalService.increment());

		contact.setCompanyId(TestPropsValues.getCompanyId());
		contact.setUserId(TestPropsValues.getUserId());
		contact.setClassNameId(classNameId);
		contact.setClassPK(classPK);

		return _contactPersistence.update(contact);
	}

	private Phone _createPhone(long classNameId, long classPK)
		throws Exception {

		Phone phone = _phonePersistence.create(
			_counterLocalService.increment());

		phone.setCompanyId(TestPropsValues.getCompanyId());
		phone.setUserId(TestPropsValues.getUserId());
		phone.setClassNameId(classNameId);
		phone.setClassPK(classPK);

		return _phonePersistence.update(phone);
	}

	private int _getCount(IntSupplier intSupplier) {
		try {
			return TransactionInvokerUtil.invoke(
				_supportsTransactionConfig, intSupplier::getAsInt);
		}
		catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

	private void _removeContact(Contact contact) throws Throwable {
		TransactionInvokerUtil.invoke(
			_transactionConfig, () -> _contactPersistence.remove(contact));
	}

	private Contact _updateContact(Contact contact, long classPK)
		throws Throwable {

		long contactId = contact.getContactId();

		return TransactionInvokerUtil.invoke(
			_transactionConfig,
			() -> {
				Contact updatedContact = _contactPersistence.findByPrimaryKey(
					contactId);

				updatedContact.setClassPK(classPK);

				return _contactPersistence.update(updatedContact);
			});
	}

	private static final TransactionConfig _supportsTransactionConfig =
		TransactionConfig.Factory.create(
			Propagation.SUPPORTS, new Class<?>[] {Exception.class});
	private static final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.REQUIRES_NEW, new Class<?>[] {Exception.class});

	@Inject
	private ContactPersistence _contactPersistence;

	@Inject
	private CounterLocalService _counterLocalService;

	@Inject
	private PhonePersistence _phonePersistence;

}
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

package com.liferay.portal.classloader.tracker.internal.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Matthew Tambara
 */
@RunWith(Arquillian.class)
public class FinderCacheTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@After
	public void tearDown() throws PortalException {
		for (User user : _users) {
			_userLocalService.deleteUser(user);
		}

		_companyLocalService.deleteCompany(_company);
	}

	@Test
	public void testFinderCache() throws Exception {
		_company = CompanyTestUtil.addCompany("FinderCache");

		long companyId = _company.getCompanyId();

		System.out.println("##### companyId: " + companyId);

		User user = UserTestUtil.addUser(_company);

		_users.add(user);

		_executeInThread(
			() -> {
				_countAndAddUser(companyId);
			});

		System.out.println(
			"###### Third count " +
				_userLocalService.getCompanyUsersCount(companyId));
	}

	private void _countAndAddUser(long companyId) {
		try {
			TransactionInvokerUtil.invoke(
				_transactionConfig,
				() -> {
					System.out.println(
						"###### First count " +
							_userLocalService.getCompanyUsersCount(companyId));

					_executeInThread(
						() -> {
							try {
								User userNew = UserTestUtil.addUser(_company);

								_users.add(userNew);

								System.out.println(
									"###### Second count " +
										_userLocalService.getCompanyUsersCount(
											companyId));
							}
							catch (Exception e) {
								throw new RuntimeException(e);
							}
						});

					return null;
				});
		}
		catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	private void _executeInThread(Runnable runnable) throws Exception {
		Thread thread = new Thread(runnable);

		thread.setDaemon(true);
		thread.start();

		thread.join();
	}

	private Company _company;

	@Inject
	private CompanyLocalService _companyLocalService;

	private final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.REQUIRED, new Class<?>[] {Exception.class});

	@Inject
	private UserLocalService _userLocalService;

	private final List<User> _users = new ArrayList<>();

}
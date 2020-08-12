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

package com.liferay.portal.dao.orm.hibernate.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.model.ClassName;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.model.impl.ClassNameImpl;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Tina Tian
 */
@RunWith(Arquillian.class)
public class SessionImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testEvict() throws Throwable {
		TransactionInvokerUtil.invoke(
			_transactionConfig,
			() -> {
				Session session = _classNamePersistence.getCurrentSession();

				try {
					ClassName className = _classNamePersistence.create(
						RandomTestUtil.nextLong());

					className.setMvccVersion(RandomTestUtil.nextLong());
					className.setValue(RandomTestUtil.randomString());

					className = _classNamePersistence.update(className);

					Object sessionObject = session.get(
						ClassNameImpl.class, className.getClassNameId());

					Assert.assertSame(className, sessionObject);

					ClassName existingClassName =
						_classNamePersistence.fetchByPrimaryKey(
							className.getClassNameId());

					Assert.assertEquals(sessionObject, existingClassName);
					Assert.assertNotSame(sessionObject, existingClassName);

					session.evict(existingClassName);

					Assert.assertNull(
						session.get(
							ClassNameImpl.class, className.getClassNameId()));
				}
				finally {
					session.clear();
				}

				return null;
			});
	}

	@Inject
	private static ClassNamePersistence _classNamePersistence;

	private static final TransactionConfig _transactionConfig;

	static {
		TransactionConfig.Builder builder = new TransactionConfig.Builder();

		builder.setPropagation(Propagation.REQUIRES_NEW);
		builder.setRollbackForClasses(Exception.class);

		_transactionConfig = builder.build();
	}

}
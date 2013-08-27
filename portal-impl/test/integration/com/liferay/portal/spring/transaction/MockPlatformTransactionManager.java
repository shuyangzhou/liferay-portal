/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.spring.transaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

/**
 * @author László Csontos
 */
public class MockPlatformTransactionManager
	implements PlatformTransactionManager {

	public MockPlatformTransactionManager(
		PlatformTransactionManager platformTransactionManager,
		boolean throwRuntimeException) {

		_platformTransactionManager = platformTransactionManager;

		_throwRuntimeException = throwRuntimeException;
	}

	@Override
	public void commit(TransactionStatus transactionStatus)
		throws TransactionException {

		_platformTransactionManager.rollback(transactionStatus);

		if (_throwRuntimeException) {
			throw new RuntimeException("MockPlatformTransactionManager");
		}
	}

	@Override
	public TransactionStatus getTransaction(
			TransactionDefinition transactionDefinition)
		throws TransactionException {

		return _platformTransactionManager.getTransaction(
			transactionDefinition);
	}

	@Override
	public void rollback(TransactionStatus transactionStatus)
		throws TransactionException {

		_platformTransactionManager.rollback(transactionStatus);
	}

	private PlatformTransactionManager _platformTransactionManager;
	private boolean _throwRuntimeException;

}
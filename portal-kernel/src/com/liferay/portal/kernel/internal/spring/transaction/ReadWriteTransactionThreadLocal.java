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

package com.liferay.portal.kernel.internal.spring.transaction;

import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.portal.kernel.transaction.NewTransactionLifecycleListener;
import com.liferay.portal.kernel.transaction.TransactionAttribute;
import com.liferay.portal.kernel.transaction.TransactionLifecycleListener;
import com.liferay.portal.kernel.transaction.TransactionStatus;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Preston Crary
 */
public class ReadWriteTransactionThreadLocal {

	public static final TransactionLifecycleListener
		TRANSACTION_LIFECYCLE_LISTENER = new NewTransactionLifecycleListener() {

			@Override
			protected void doCommitted(
				TransactionAttribute transactionAttribute,
				TransactionStatus transactionStatus) {

				if (!transactionAttribute.isReadOnly()) {
					Deque<Boolean> deque =
						_readWriteTransactionThreadLocal.get();

					deque.pop();
				}
			}

			@Override
			protected void doCreated(
				TransactionAttribute transactionAttribute,
				TransactionStatus transactionStatus) {

				if (!transactionAttribute.isReadOnly()) {
					Deque<Boolean> deque =
						_readWriteTransactionThreadLocal.get();

					deque.push(Boolean.TRUE);
				}
			}

			@Override
			protected void doRollbacked(
				TransactionAttribute transactionAttribute,
				TransactionStatus transactionStatus, Throwable throwable) {

				if (!transactionAttribute.isReadOnly()) {
					Deque<Boolean> deque =
						_readWriteTransactionThreadLocal.get();

					deque.pop();
				}
			}

		};

	public static boolean isReadWriteTransaction() {
		Deque<Boolean> readWriteTransactionDeque =
			_readWriteTransactionThreadLocal.get();

		Boolean readWrite = readWriteTransactionDeque.peek();

		if (readWrite == null) {
			return false;
		}

		return readWrite;
	}

	private static final ThreadLocal<Deque<Boolean>>
		_readWriteTransactionThreadLocal = new CentralizedThreadLocal<>(
			ReadWriteTransactionThreadLocal.class +
				"._readWriteTransactionThreadLocal",
			ArrayDeque::new, false);

}
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

package com.liferay.portal.kernel.transaction;

import com.liferay.portal.kernel.util.InitialThreadLocal;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Shuyang Zhou
 */
public abstract class CurrentTransactionLifecycleListener
	implements TransactionLifecycleListener {

	@Override
	public void committed(
		TransactionAttribute transactionAttribute,
		TransactionStatus transactionStatus) {

		try {
			doCommitted(transactionAttribute, transactionStatus);
		}
		finally {
			Deque<TransactionInfo> transactionInfoDeque =
				_transactionInfoDequeThreadLocal.get();

			transactionInfoDeque.removeLast();
		}
	}

	@Override
	public void created(
		TransactionAttribute transactionAttribute,
		TransactionStatus transactionStatus) {

		Deque<TransactionInfo> transactionInfoDeque =
			_transactionInfoDequeThreadLocal.get();

		transactionInfoDeque.addLast(
			new TransactionInfo(transactionAttribute, transactionStatus));

		doCreated(transactionAttribute, transactionStatus);
	}

	public TransactionAttribute getCurrentTransactionAttribute() {
		Deque<TransactionInfo> transactionInfoDeque =
			_transactionInfoDequeThreadLocal.get();

		if (transactionInfoDeque.isEmpty()) {
			return null;
		}

		TransactionInfo transactionInfo = transactionInfoDeque.getLast();

		return transactionInfo._transactionAttribute;
	}

	public TransactionStatus getCurrentTransactionStatus() {
		Deque<TransactionInfo> transactionInfoDeque =
			_transactionInfoDequeThreadLocal.get();

		if (transactionInfoDeque.isEmpty()) {
			return null;
		}

		TransactionInfo transactionInfo = transactionInfoDeque.getLast();

		return transactionInfo._transactionStatus;
	}

	@Override
	public void rollbacked(
		TransactionAttribute transactionAttribute,
		TransactionStatus transactionStatus, Throwable throwable) {

		try {
			doRollbacked(transactionAttribute, transactionStatus, throwable);
		}
		finally {
			Deque<TransactionInfo> transactionInfoDeque =
				_transactionInfoDequeThreadLocal.get();

			transactionInfoDeque.removeLast();
		}
	}

	protected abstract void doCommitted(
		TransactionAttribute transactionAttribute,
		TransactionStatus transactionStatus);

	protected abstract void doCreated(
		TransactionAttribute transactionAttribute,
		TransactionStatus transactionStatus);

	protected abstract void doRollbacked(
		TransactionAttribute transactionAttribute,
		TransactionStatus transactionStatus, Throwable throwable);

	private static final ThreadLocal<Deque<TransactionInfo>>
		_transactionInfoDequeThreadLocal =
			new InitialThreadLocal<Deque<TransactionInfo>>(
				CurrentTransactionLifecycleListener.class.getName() +
					"._transactionInfoDequeThreadLocal",
				new LinkedList<TransactionInfo>());

	private static class TransactionInfo {

		private TransactionInfo(
			TransactionAttribute transactionAttribute,
			TransactionStatus transactionStatus) {

			_transactionAttribute = transactionAttribute;
			_transactionStatus = transactionStatus;
		}

		private final TransactionAttribute _transactionAttribute;
		private final TransactionStatus _transactionStatus;

	}

}
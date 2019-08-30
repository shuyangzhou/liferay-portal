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

package com.liferay.batch.engine.internal;

import com.liferay.batch.engine.BatchItemWriter;
import com.liferay.batch.engine.BatchOperation;
import com.liferay.batch.engine.BatchStatus;
import com.liferay.batch.engine.BatchTaskExecutor;
import com.liferay.batch.engine.internal.reader.BatchItemReader;
import com.liferay.batch.engine.internal.reader.BatchItemReaderFactory;
import com.liferay.batch.engine.model.BatchTask;
import com.liferay.batch.engine.service.BatchTaskLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ivica Cardic
 */
public class BatchTaskExecutorImpl<T> implements BatchTaskExecutor {

	public BatchTaskExecutorImpl(
		Class<T> domainClass,
		BatchItemReaderFactoryRegistry batchItemReaderFactoryRegistry,
		BatchItemWriterRegistry batchItemWriterRegistry, int batchSize,
		BatchTaskLocalService batchTaskLocalService) {

		_domainClass = domainClass;
		_batchItemReaderFactoryRegistry = batchItemReaderFactoryRegistry;
		_batchItemWriterRegistry = batchItemWriterRegistry;
		_batchSize = batchSize;
		_batchTaskLocalService = batchTaskLocalService;
	}

	@Override
	public void execute(long batchTaskId) {
		BatchTask batchTask = null;

		try {
			batchTask = _batchTaskLocalService.getBatchTask(batchTaskId);

			batchTask.setStartTime(new Date());
			batchTask.setStatus(BatchStatus.STARTED.toString());

			_batchTaskLocalService.updateBatchTask(batchTask);

			_execute(batchTask);

			batchTask = _batchTaskLocalService.getBatchTask(
				batchTask.getBatchTaskId());

			batchTask.setEndTime(new Date());
			batchTask.setStatus(BatchStatus.COMPLETED.toString());

			_batchTaskLocalService.updateBatchTask(batchTask);
		}
		catch (Throwable t) {
			_log.error("Batch task with id " + batchTaskId + " failed: ", t);

			if (batchTask == null) {
				return;
			}

			batchTask.setErrorMessage(t.getMessage());

			batchTask.setEndTime(new Date());
			batchTask.setStatus(BatchStatus.FAILED.toString());

			_batchTaskLocalService.updateBatchTask(batchTask);
		}
	}

	private Void _commitItems(
			BatchItemWriter<T> batchItemWriter, List<T> items,
			BatchOperation batchOperation)
		throws Exception {

		batchItemWriter.write(items, batchOperation);

		return null;
	}

	private void _execute(BatchTask batchTask) throws Throwable {
		List<T> items = new ArrayList<>();

		T item = null;

		BatchItemWriter<T> batchItemWriter = _batchItemWriterRegistry.get(
			batchTask.getClassName(), batchTask.getVersion());

		BatchOperation batchOperation = BatchOperation.valueOf(
			batchTask.getOperation());

		BatchItemReaderFactory batchItemReaderFactory =
			_batchItemReaderFactoryRegistry.get(batchTask.getContentType());

		try (BatchItemReader<T> batchItemReader = batchItemReaderFactory.create(
				_domainClass, batchTask.getFileEntryId())) {

			while ((item = batchItemReader.read()) != null) {
				if (items.size() < _batchSize) {
					items.add(item);
				}

				if (items.size() == _batchSize) {
					TransactionInvokerUtil.invoke(
						_transactionConfig,
						() -> _commitItems(
							batchItemWriter, items, batchOperation));

					items.clear();
				}
			}
		}

		if (!items.isEmpty()) {
			TransactionInvokerUtil.invoke(
				_transactionConfig,
				() -> _commitItems(batchItemWriter, items, batchOperation));
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BatchTaskExecutorImpl.class);

	private static final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.REQUIRES_NEW, new Class<?>[] {Exception.class});

	private final BatchItemReaderFactoryRegistry
		_batchItemReaderFactoryRegistry;
	private final BatchItemWriterRegistry _batchItemWriterRegistry;
	private final long _batchSize;
	private final BatchTaskLocalService _batchTaskLocalService;
	private final Class<T> _domainClass;

}

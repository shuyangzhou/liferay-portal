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

package com.liferay.batch.engine.internal.unit;

import com.liferay.batch.engine.BatchEngineTaskItemDelegate;
import com.liferay.batch.engine.BatchEngineTaskItemDelegateRegistry;
import com.liferay.petra.string.StringBundler;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matija Petanjek
 */
@Component(service = RetryBatchEngineTaskItemDelegateRegistry.class)
public class RetryBatchEngineTaskItemDelegateRegistry {

	public BatchEngineTaskItemDelegate<?> getBatchEngineTaskItemDelegate(
			String className, String taskItemDelegateName)
		throws Exception {

		int retries = _MAX_RETRIES;

		while (retries-- > 0) {
			BatchEngineTaskItemDelegate<?> batchEngineTaskItemDelegate =
				_batchEngineTaskItemDelegateRegistry.
					getBatchEngineTaskItemDelegate(
						className, taskItemDelegateName);

			if (batchEngineTaskItemDelegate != null) {
				return batchEngineTaskItemDelegate;
			}

			Thread.sleep(_RETRY_INTERVAL_MILLIS);
		}

		throw new IllegalStateException(
			StringBundler.concat(
				"Unable to get batch engine task item delegate for class ",
				className, " and task item delegate ", taskItemDelegateName));
	}

	private static final int _MAX_RETRIES = 10;

	private static final int _RETRY_INTERVAL_MILLIS = 5000;

	@Reference
	private BatchEngineTaskItemDelegateRegistry
		_batchEngineTaskItemDelegateRegistry;

}
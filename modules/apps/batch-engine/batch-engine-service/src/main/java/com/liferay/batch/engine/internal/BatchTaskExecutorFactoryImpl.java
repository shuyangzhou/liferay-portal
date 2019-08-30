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

import com.liferay.batch.engine.BatchTaskExecutor;
import com.liferay.batch.engine.BatchTaskExecutorFactory;
import com.liferay.batch.engine.service.BatchTaskLocalService;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 * @author Ivica Cardic
 */
@Component(
	property = "batch.size=100", service = BatchTaskExecutorFactory.class
)
public class BatchTaskExecutorFactoryImpl implements BatchTaskExecutorFactory {

	@Activate
	public void activate(Map<String, Object> properties) {
		_batchSize = GetterUtil.getInteger(properties.get("batch.size"));

		if (_batchSize <= 0) {
			_batchSize = 1;
		}
	}

	@Override
	public BatchTaskExecutor create(Class<?> domainClass) {
		return new BatchTaskExecutorImpl<>(
			domainClass, _batchItemReaderFactoryRegistry,
			_batchItemWriterRegistry, _batchSize, _batchTaskLocalService);
	}

	@Reference
	private BatchItemReaderFactoryRegistry _batchItemReaderFactoryRegistry;

	@Reference
	private BatchItemWriterRegistry _batchItemWriterRegistry;

	private int _batchSize;

	@Reference
	private BatchTaskLocalService _batchTaskLocalService;

}
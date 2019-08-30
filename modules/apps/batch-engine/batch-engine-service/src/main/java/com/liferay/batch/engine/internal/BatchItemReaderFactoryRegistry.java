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

import com.liferay.batch.engine.internal.reader.BatchItemReaderFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Ivica Cardic
 */
@Component(service = BatchItemReaderFactoryRegistry.class)
public class BatchItemReaderFactoryRegistry {

	@Activate
	public void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, BatchItemReaderFactory.class, "content.type");
	}

	public BatchItemReaderFactory get(String contentType) {
		BatchItemReaderFactory batchItemWriter = _serviceTrackerMap.getService(
			contentType);

		if (batchItemWriter == null) {
			throw new IllegalStateException(
				"Unknown item reader type : " + contentType);
		}

		return batchItemWriter;
	}

	private ServiceTrackerMap<String, BatchItemReaderFactory>
		_serviceTrackerMap;

}
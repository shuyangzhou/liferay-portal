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

package com.liferay.batch.engine.internal.reader;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 * @author Ivica Cardic
 */
@Component(
	immediate = true, property = "content.type=JSON",
	service = BatchItemReaderFactory.class
)
public class JSONBatchItemReaderFactory implements BatchItemReaderFactory {

	@Override
	public <T> BatchItemReader<T> create(Class<T> domainClass, long fileEntryId)
		throws Exception {

		return new JSONBatchItemReader<>(
			domainClass, _fileEntryReader.getInputStream(fileEntryId));
	}

	@Reference
	private FileEntryReader _fileEntryReader;

}
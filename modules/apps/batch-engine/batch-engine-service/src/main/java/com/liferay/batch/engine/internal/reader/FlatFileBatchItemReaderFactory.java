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

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 * @author Ivica Cardic
 */
@Component(
	immediate = true,
	property = {"column.delimiter=,", "content.type=CSV", "lines.to.skip=1"},
	service = BatchItemReaderFactory.class
)
public class FlatFileBatchItemReaderFactory implements BatchItemReaderFactory {

	@Activate
	public void activate(Map<String, Object> properties) {
		_delimiter = (String)properties.get("column.delimiter");

		if (Validator.isNull(_delimiter)) {
			_delimiter = ",";
		}

		_linesToSkip = GetterUtil.getInteger(properties.get("lines.to.skip"));

		if (_linesToSkip <= 0) {
			_linesToSkip = 1;
		}
	}

	@Override
	public <T> BatchItemReader<T> create(Class<T> domainClass, long fileEntryId)
		throws Exception {

		return new FlatFileBatchItemReader<>(
			_delimiter, domainClass,
			_fileEntryReader.getInputStream(fileEntryId), _linesToSkip);
	}

	private String _delimiter;

	@Reference
	private FileEntryReader _fileEntryReader;

	private int _linesToSkip;

}
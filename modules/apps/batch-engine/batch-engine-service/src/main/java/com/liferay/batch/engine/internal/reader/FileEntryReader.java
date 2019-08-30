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

import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.MimeTypesUtil;

import java.io.BufferedInputStream;
import java.io.InputStream;

import java.util.Objects;
import java.util.zip.ZipInputStream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica Cardic
 */
@Component(service = FileEntryReader.class)
public class FileEntryReader {

	public InputStream getInputStream(long fileEntryId) throws Exception {
		FileEntry fileEntry = _dlAppLocalService.getFileEntry(fileEntryId);

		return _checkCompressedFile(
			MimeTypesUtil.getContentType(fileEntry.getFileName()),
			fileEntry.getContentStream());
	}

	private InputStream _checkCompressedFile(
			String contentType, InputStream inputStream)
		throws Exception {

		if (Objects.equals(ContentTypes.APPLICATION_ZIP, contentType)) {
			ZipInputStream zipInputStream = new ZipInputStream(
				new BufferedInputStream(inputStream));

			zipInputStream.getNextEntry();

			inputStream = zipInputStream;
		}

		return inputStream;
	}

	@Reference
	private DLAppLocalService _dlAppLocalService;

}
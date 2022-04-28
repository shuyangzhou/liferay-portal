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

package com.liferay.portal.upload.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upload.FileItem;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadServletRequestConfigurationHelperUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.upload.ServletFileUpload;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadBase;

import org.osgi.service.component.annotations.Component;

/**
 * @author Shuyang Zhou
 */
@Component(service = ServletFileUpload.class)
public class ServletFileUploadImpl implements ServletFileUpload {

	@Override
	public List<FileItem> parseRequest(
		HttpServletRequest httpServletRequest, long sizeMax, long fileSizeMax,
		String location, int fileSizeThreshold) {

		try {
			org.apache.commons.fileupload.servlet.ServletFileUpload
				servletFileUpload =
					new org.apache.commons.fileupload.servlet.ServletFileUpload(
						new LiferayFileItemFactory(
							new File(
								GetterUtil.getString(
									location,
									UploadServletRequestConfigurationHelperUtil.
										getTempDir())),
							fileSizeThreshold,
							httpServletRequest.getCharacterEncoding()));

			servletFileUpload.setSizeMax(sizeMax);

			servletFileUpload.setFileSizeMax(fileSizeMax);

			List<FileItem> fileItemList = new ArrayList<>();

			for (org.apache.commons.fileupload.FileItem fileItem :
					servletFileUpload.parseRequest(httpServletRequest)) {

				if (fileItem.getSize() > LiferayFileItem.THRESHOLD_SIZE) {
					UploadException uploadException = new UploadException(
						StringBundler.concat(
							"The field ", fileItem.getFieldName(),
							" exceeds its maximum permitted size of ",
							LiferayFileItem.THRESHOLD_SIZE, " bytes"));

					uploadException.setExceededLiferayFileItemSizeLimit(true);

					httpServletRequest.setAttribute(
						WebKeys.UPLOAD_EXCEPTION, uploadException);
				}

				fileItemList.add((FileItem)fileItem);
			}

			return fileItemList;
		}
		catch (Exception exception) {
			UploadException uploadException = new UploadException(exception);

			if (exception instanceof
					FileUploadBase.FileSizeLimitExceededException) {

				uploadException.setExceededFileSizeLimit(true);
			}
			else if (exception instanceof
						FileUploadBase.SizeLimitExceededException) {

				uploadException.setExceededUploadRequestSizeLimit(true);
			}

			httpServletRequest.setAttribute(
				WebKeys.UPLOAD_EXCEPTION, uploadException);

			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
			else if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to parse upload request: " +
						exception.getMessage());
			}

			return null;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ServletFileUploadImpl.class);

}
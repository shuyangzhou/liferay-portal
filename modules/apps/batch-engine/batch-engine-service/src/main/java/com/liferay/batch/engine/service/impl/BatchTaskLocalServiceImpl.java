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

package com.liferay.batch.engine.service.impl;

import com.liferay.batch.engine.BatchOperation;
import com.liferay.batch.engine.BatchStatus;
import com.liferay.batch.engine.model.BatchTask;
import com.liferay.batch.engine.service.base.BatchTaskLocalServiceBaseImpl;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 */
@Component(
	property = "model.class.name=com.liferay.batch.engine.model.BatchTask",
	service = AopService.class
)
public class BatchTaskLocalServiceImpl extends BatchTaskLocalServiceBaseImpl {

	@Override
	public BatchTask addBatchTask(
		long fileEntryId, String className, String version, String contentType,
		BatchOperation batchOperation) {

		BatchTask batchTask = batchTaskPersistence.create(
			counterLocalService.increment(BatchTask.class.getName()));

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		batchTask.setCompanyId(serviceContext.getCompanyId());

		batchTask.setFileEntryId(fileEntryId);
		batchTask.setClassName(className);
		batchTask.setVersion(version);
		batchTask.setContentType(contentType);
		batchTask.setOperation(batchOperation.toString());
		batchTask.setStatus(BatchStatus.INITIAL.toString());

		return batchTaskPersistence.update(batchTask);
	}

	@Override
	public BatchTask deleteBatchTask(BatchTask batchTask) {
		try {
			_dlAppLocalService.deleteFileEntry(batchTask.getFileEntryId());
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}

		return super.deleteBatchTask(batchTask);
	}

	@Override
	public BatchTask deleteBatchTask(long batchTaskId) throws PortalException {
		BatchTask batchTask = getBatchTask(batchTaskId);

		_dlAppLocalService.deleteFileEntry(batchTask.getFileEntryId());

		return super.deleteBatchTask(batchTaskId);
	}

	@Reference
	private DLAppLocalService _dlAppLocalService;

}
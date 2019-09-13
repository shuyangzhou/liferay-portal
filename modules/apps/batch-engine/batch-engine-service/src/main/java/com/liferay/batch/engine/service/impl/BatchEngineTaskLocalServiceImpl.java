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

import com.liferay.batch.engine.BatchEngineTaskContentType;
import com.liferay.batch.engine.BatchEngineTaskExecuteStatus;
import com.liferay.batch.engine.BatchEngineTaskOperation;
import com.liferay.batch.engine.internal.BatchEngineTaskResourceRegistry;
import com.liferay.batch.engine.model.BatchEngineTask;
import com.liferay.batch.engine.service.base.BatchEngineTaskLocalServiceBaseImpl;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.jdbc.OutputBlob;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 * @author Ivica Cardic
 */
@Component(
	property = "model.class.name=com.liferay.batch.engine.model.BatchEngineTask",
	service = AopService.class
)
public class BatchEngineTaskLocalServiceImpl
	extends BatchEngineTaskLocalServiceBaseImpl {

	@Override
	public BatchEngineTask addBatchEngineTask(
		long companyId, long userId,
		BatchEngineTaskContentType batchEngineTaskContentType,
		BatchEngineTaskOperation batchEngineTaskOperation, long batchSize,
		String className, byte[] content, String version) {

		if (!_batchEngineTaskResourceRegistry.isResourceMethodRegistered(
				batchEngineTaskOperation, className)) {

			StringBundler sb = new StringBundler(4);

			sb.append(
				"No resource method available for batchEngineTaskOperation ");
			sb.append(batchEngineTaskOperation);
			sb.append(" and className ");
			sb.append(className);

			throw new SystemException(sb.toString());
		}

		BatchEngineTask batchEngineTask = batchEngineTaskPersistence.create(
			counterLocalService.increment(BatchEngineTask.class.getName()));

		batchEngineTask.setCompanyId(companyId);
		batchEngineTask.setUserId(userId);
		batchEngineTask.setBatchSize(batchSize);
		batchEngineTask.setClassName(className);
		batchEngineTask.setContent(
			new OutputBlob(
				new UnsyncByteArrayInputStream(content), content.length));
		batchEngineTask.setContentType(batchEngineTaskContentType.toString());
		batchEngineTask.setExecuteStatus(
			BatchEngineTaskExecuteStatus.INITIAL.toString());
		batchEngineTask.setOperation(batchEngineTaskOperation.toString());
		batchEngineTask.setVersion(version);

		return batchEngineTaskPersistence.update(batchEngineTask);
	}

	@Reference
	private BatchEngineTaskResourceRegistry _batchEngineTaskResourceRegistry;

}
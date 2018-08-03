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

package com.liferay.apio.architect.impl.wiring.osgi.manager.message.json;

import static com.liferay.apio.architect.impl.wiring.osgi.manager.cache.ManagerCache.INSTANCE;

import com.liferay.apio.architect.impl.message.json.BatchResultMessageMapper;
import com.liferay.apio.architect.impl.wiring.osgi.manager.base.MessageMapperBaseManager;

import java.util.Optional;

import javax.ws.rs.core.Request;

import org.osgi.service.component.annotations.Component;

/**
 * @author Alejandro Hernández
 */
@Component
public class BatchResultMessageMapperManagerImpl
	extends MessageMapperBaseManager<BatchResultMessageMapper>
	implements BatchResultMessageMapperManager {

	public BatchResultMessageMapperManagerImpl() {
		super(
			BatchResultMessageMapper.class,
			INSTANCE::putBatchResultMessageMapper);
	}

	@Override
	public <T> Optional<BatchResultMessageMapper<T>>
		getBatchResultMessageMapperOptional(Request request) {

		return INSTANCE.getBatchResultMessageMapperOptional(
			request, this::computeMessageMappers);
	}

}
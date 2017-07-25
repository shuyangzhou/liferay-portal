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

package com.liferay.adaptive.media.internal.processor;

import com.liferay.adaptive.media.internal.constants.AdaptiveMediaDestinationNames;
import com.liferay.adaptive.media.internal.messaging.AdaptiveMediaProcessorCommand;
import com.liferay.adaptive.media.processor.AdaptiveMediaAsyncProcessor;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Sergio González
 */
public final class AdaptiveMediaAsyncProcessorImpl<M, T>
	implements AdaptiveMediaAsyncProcessor<M, T> {

	public AdaptiveMediaAsyncProcessorImpl(
		Class<M> clazz, MessageBus messageBus) {

		_clazz = clazz;
		_messageBus = messageBus;
	}

	@Override
	public void cleanQueue(
		AdaptiveMediaProcessorCommand command, String modelId) {

		List<String> commandModelIds = _modelIds.get(command);

		commandModelIds.remove(modelId);

		if (_log.isInfoEnabled()) {
			_log.info(
				"Cleaned queue for model id " + modelId + " and command " +
					command);
		}
	}

	@Override
	public void triggerCleanUp(M model, String modelId) throws PortalException {
		if (Validator.isNotNull(modelId)) {
			_modelIds.putIfAbsent(
				AdaptiveMediaProcessorCommand.CLEAN_UP,
				new CopyOnWriteArrayList<>());

			List<String> cleanUpModelIds = _modelIds.get(
				AdaptiveMediaProcessorCommand.CLEAN_UP);

			if (cleanUpModelIds.contains(modelId)) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Omitted clean up for model id " + modelId +
							" because it is already in progress");
				}

				return;
			}

			cleanUpModelIds.add(modelId);

			if (_log.isInfoEnabled()) {
				_log.info(
					"Added clean up for model id " + modelId + " to the queue");
			}
		}

		Message message = new Message();

		message.put("className", _clazz.getName());
		message.put("model", model);
		message.put("command", AdaptiveMediaProcessorCommand.CLEAN_UP);

		if (Validator.isNotNull(modelId)) {
			message.put("modelId", modelId);
		}

		TransactionCommitCallbackUtil.registerCallback(
			() -> {
				_messageBus.sendMessage(
					AdaptiveMediaDestinationNames.ADAPTIVE_MEDIA_PROCESSOR,
					message);

				return null;
			});
	}

	@Override
	public void triggerProcess(M model, String modelId) throws PortalException {
		if (Validator.isNotNull(modelId)) {
			_modelIds.putIfAbsent(
				AdaptiveMediaProcessorCommand.PROCESS,
				new CopyOnWriteArrayList<>());

			List<String> processModelIds = _modelIds.get(
				AdaptiveMediaProcessorCommand.PROCESS);

			if (processModelIds.contains(modelId)) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Omitted process for model id " + modelId +
							" because it is already in progress");
				}

				return;
			}

			processModelIds.add(modelId);

			if (_log.isInfoEnabled()) {
				_log.info(
					"Added process for model id " + modelId + " to the queue");
			}
		}

		Message message = new Message();

		message.put("className", _clazz.getName());
		message.put("model", model);
		message.put("command", AdaptiveMediaProcessorCommand.PROCESS);

		if (Validator.isNotNull(modelId)) {
			message.put("modelId", modelId);
		}

		TransactionCommitCallbackUtil.registerCallback(
			() -> {
				_messageBus.sendMessage(
					AdaptiveMediaDestinationNames.ADAPTIVE_MEDIA_PROCESSOR,
					message);

				return null;
			});
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AdaptiveMediaAsyncProcessorImpl.class);

	private static final Map<AdaptiveMediaProcessorCommand, List<String>>
		_modelIds = new ConcurrentHashMap<>();

	private final Class<M> _clazz;
	private final MessageBus _messageBus;

}
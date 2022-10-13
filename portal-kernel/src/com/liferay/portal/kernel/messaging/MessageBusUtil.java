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

package com.liferay.portal.kernel.messaging;

import com.liferay.portal.kernel.messaging.sender.SynchronousMessageSender;
import com.liferay.portal.kernel.util.ServiceProxyFactory;

/**
 * @author Michael C. Han
 * @author Raymond Augé
 */
public class MessageBusUtil {

	public static Message createResponseMessage(Message requestMessage) {
		Message responseMessage = new Message();

		responseMessage.setDestinationName(
			requestMessage.getResponseDestinationName());
		responseMessage.setResponseId(requestMessage.getResponseId());

		return responseMessage;
	}

	public static Message createResponseMessage(
		Message requestMessage, Object payload) {

		Message responseMessage = createResponseMessage(requestMessage);

		responseMessage.setPayload(payload);

		return responseMessage;
	}

	public static Destination getDestination(String destinationName) {
		return _messageBus.getDestination(destinationName);
	}

	public static MessageBus getMessageBus() {
		return _messageBus;
	}

	public static boolean hasMessageListener(String destination) {
		return _messageBus.hasMessageListener(destination);
	}

	public static void registerMessageListener(
		String destinationName, MessageListener messageListener) {

		_messageBus.registerMessageListener(destinationName, messageListener);
	}

	public static void sendMessage(String destinationName, Message message) {
		_messageBus.sendMessage(destinationName, message);
	}

	public static void sendMessage(String destinationName, Object payload) {
		Message message = new Message();

		message.setPayload(payload);

		_messageBus.sendMessage(destinationName, message);
	}

	public static Object sendSynchronousMessage(
			String destinationName, Message message)
		throws MessageBusException {

		return _synchronousMessageSender.send(destinationName, message);
	}

	public static Object sendSynchronousMessage(
			String destinationName, Object payload)
		throws MessageBusException {

		return sendSynchronousMessage(destinationName, payload, null);
	}

	public static Object sendSynchronousMessage(
			String destinationName, Object payload, long timeout)
		throws MessageBusException {

		return sendSynchronousMessage(destinationName, payload, null, timeout);
	}

	public static Object sendSynchronousMessage(
			String destinationName, Object payload,
			String responseDestinationName)
		throws MessageBusException {

		Message message = new Message();

		message.setResponseDestinationName(responseDestinationName);
		message.setPayload(payload);

		return sendSynchronousMessage(destinationName, message);
	}

	public static Object sendSynchronousMessage(
			String destinationName, Object payload,
			String responseDestinationName, long timeout)
		throws MessageBusException {

		Message message = new Message();

		message.setResponseDestinationName(responseDestinationName);
		message.setPayload(payload);

		return sendSynchronousMessage(destinationName, message, timeout);
	}

	public static void shutdown() {
		_messageBus.shutdown();
	}

	public static void shutdown(boolean force) {
		_messageBus.shutdown(force);
	}

	public static boolean unregisterMessageListener(
		String destinationName, MessageListener messageListener) {

		return _messageBus.unregisterMessageListener(
			destinationName, messageListener);
	}

	private static volatile MessageBus _messageBus =
		ServiceProxyFactory.newServiceTrackedInstance(
			MessageBus.class, MessageBusUtil.class, "_messageBus", true);
	private static volatile SynchronousMessageSender _synchronousMessageSender =
		ServiceProxyFactory.newServiceTrackedInstance(
			SynchronousMessageSender.class, MessageBusUtil.class,
			"_synchronousMessageSender", true);

}
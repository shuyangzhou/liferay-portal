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
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael C. Han
 * @author Raymond Augé
 */
public class MessageBusUtil {

	public static void addDestination(Destination destination) {
		_instance._addDestination(destination);
	}

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
		return _instance._getDestination(destinationName);
	}

	public static MessageBus getMessageBus() {
		return _instance._getMessageBus();
	}

	public static SynchronousMessageSender getSynchronousMessageSender(
		SynchronousMessageSender.Mode mode) {

		return _instance._synchronousMessageSenders.get(mode);
	}

	public static boolean hasMessageListener(String destination) {
		return _instance._hasMessageListener(destination);
	}

	public static void registerMessageListener(
		String destinationName, MessageListener messageListener) {

		_instance._registerMessageListener(destinationName, messageListener);
	}

	public static void removeDestination(String destinationName) {
		_instance._removeDestination(destinationName);
	}

	public static void sendMessage(String destinationName, Message message) {
		_instance._sendMessage(destinationName, message);
	}

	public static void sendMessage(String destinationName, Object payload) {
		_instance._sendMessage(destinationName, payload);
	}

	public static Object sendSynchronousMessage(
			String destinationName, Message message)
		throws MessageBusException {

		return _instance._sendSynchronousMessage(destinationName, message);
	}

	public static Object sendSynchronousMessage(
			String destinationName, Message message, long timeout)
		throws MessageBusException {

		return _instance._sendSynchronousMessage(
			destinationName, message, timeout);
	}

	public static Object sendSynchronousMessage(
			String destinationName, Object payload)
		throws MessageBusException {

		return _instance._sendSynchronousMessage(
			destinationName, payload, null);
	}

	public static Object sendSynchronousMessage(
			String destinationName, Object payload, long timeout)
		throws MessageBusException {

		return _instance._sendSynchronousMessage(
			destinationName, payload, null, timeout);
	}

	public static Object sendSynchronousMessage(
			String destinationName, Object payload,
			String responseDestinationName)
		throws MessageBusException {

		return _instance._sendSynchronousMessage(
			destinationName, payload, responseDestinationName);
	}

	public static Object sendSynchronousMessage(
			String destinationName, Object payload,
			String responseDestinationName, long timeout)
		throws MessageBusException {

		return _instance._sendSynchronousMessage(
			destinationName, payload, responseDestinationName, timeout);
	}

	public static void shutdown() {
		_instance._shutdown();
	}

	public static void shutdown(boolean force) {
		_instance._shutdown(force);
	}

	public static boolean unregisterMessageListener(
		String destinationName, MessageListener messageListener) {

		return _instance._unregisterMessageListener(
			destinationName, messageListener);
	}

	public MessageBusUtil() {
		Registry registry = RegistryUtil.getRegistry();

		_messageBusServiceTracker = registry.trackServices(MessageBus.class);

		_messageBusServiceTracker.open();

		_synchronousMessageSenderServiceTracker = registry.trackServices(
			SynchronousMessageSender.class,
			new SynchronousMessageSenderServiceTrackerCustomizer());

		_synchronousMessageSenderServiceTracker.open();
	}

	public void setMode(SynchronousMessageSender.Mode mode) {
		_mode = mode;
	}

	private void _addDestination(Destination destination) {
		_getMessageBus().addDestination(destination);
	}

	private Destination _getDestination(String destinationName) {
		return _getMessageBus().getDestination(destinationName);
	}

	private MessageBus _getMessageBus() {
		return _messageBusServiceTracker.getService();
	}

	private boolean _hasMessageListener(String destinationName) {
		return _getMessageBus().hasMessageListener(destinationName);
	}

	private void _registerMessageListener(
		String destinationName, MessageListener messageListener) {

		_getMessageBus().registerMessageListener(
			destinationName, messageListener);
	}

	private void _removeDestination(String destinationName) {
		_getMessageBus().removeDestination(destinationName);
	}

	private void _sendMessage(String destinationName, Message message) {
		_getMessageBus().sendMessage(destinationName, message);
	}

	private void _sendMessage(String destinationName, Object payload) {
		Message message = new Message();

		message.setPayload(payload);

		_sendMessage(destinationName, message);
	}

	private Object _sendSynchronousMessage(
			String destinationName, Message message)
		throws MessageBusException {

		SynchronousMessageSender synchronousMessageSender =
			_synchronousMessageSenders.get(_mode);

		if (synchronousMessageSender == null) {
			throw new MessageBusException(
				"No SynchronousMessageSender configured for: " + _mode);
		}

		return synchronousMessageSender.send(destinationName, message);
	}

	private Object _sendSynchronousMessage(
			String destinationName, Message message, long timeout)
		throws MessageBusException {

		SynchronousMessageSender synchronousMessageSender =
			_synchronousMessageSenders.get(_mode);

		if (synchronousMessageSender == null) {
			throw new MessageBusException(
				"No SynchronousMessageSender configured for: " + _mode);
		}

		return synchronousMessageSender.send(destinationName, message, timeout);
	}

	private Object _sendSynchronousMessage(
			String destinationName, Object payload,
			String responseDestinationName)
		throws MessageBusException {

		Message message = new Message();

		message.setResponseDestinationName(responseDestinationName);
		message.setPayload(payload);

		return _sendSynchronousMessage(destinationName, message);
	}

	private Object _sendSynchronousMessage(
			String destinationName, Object payload,
			String responseDestinationName, long timeout)
		throws MessageBusException {

		Message message = new Message();

		message.setResponseDestinationName(responseDestinationName);
		message.setPayload(payload);

		return _sendSynchronousMessage(destinationName, message, timeout);
	}

	private void _shutdown() {
		PortalRuntimePermission.checkGetBeanProperty(MessageBusUtil.class);

		_getMessageBus().shutdown();
	}

	private void _shutdown(boolean force) {
		PortalRuntimePermission.checkGetBeanProperty(MessageBusUtil.class);

		_getMessageBus().shutdown(force);
	}

	private boolean _unregisterMessageListener(
		String destinationName, MessageListener messageListener) {

		return _getMessageBus().unregisterMessageListener(
			destinationName, messageListener);
	}

	private static final MessageBusUtil _instance = new MessageBusUtil();

	private final ServiceTracker<MessageBus, MessageBus>
		_messageBusServiceTracker;
	private SynchronousMessageSender.Mode _mode;
	private final Map<SynchronousMessageSender.Mode, SynchronousMessageSender>
		_synchronousMessageSenders = new HashMap<>();
	private final ServiceTracker
		<SynchronousMessageSender, SynchronousMessageSender>
			_synchronousMessageSenderServiceTracker;

	private class SynchronousMessageSenderServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<SynchronousMessageSender, SynchronousMessageSender> {

		@Override
		public SynchronousMessageSender addingService(
			ServiceReference<SynchronousMessageSender> serviceReference) {

			Map<String, Object> properties = serviceReference.getProperties();

			SynchronousMessageSender.Mode mode = getMode(properties);

			Registry registry = RegistryUtil.getRegistry();

			SynchronousMessageSender synchronousMessageSender =
				registry.getService(serviceReference);

			_synchronousMessageSenders.put(mode, synchronousMessageSender);

			return synchronousMessageSender;
		}

		@Override
		public void modifiedService(
			ServiceReference<SynchronousMessageSender> serviceReference,
			SynchronousMessageSender service) {
		}

		@Override
		public void removedService(
			ServiceReference<SynchronousMessageSender> serviceReference,
			SynchronousMessageSender service) {

			Map<String, Object> properties = serviceReference.getProperties();

			SynchronousMessageSender.Mode mode = getMode(properties);

			_synchronousMessageSenders.remove(mode);
		}

		protected SynchronousMessageSender.Mode getMode(
			Map<String, Object> properties) {

			String mode = GetterUtil.getString(properties.get("mode"));

			return SynchronousMessageSender.Mode.valueOf(mode);
		}

	}

}
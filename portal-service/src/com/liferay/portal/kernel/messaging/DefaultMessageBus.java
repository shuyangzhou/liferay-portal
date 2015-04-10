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

import com.liferay.portal.kernel.concurrent.ConcurrentHashSet;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Michael C. Han
 */
public class DefaultMessageBus implements MessageBus {

	@Override
	public void addDestination(Destination destination) {
		Destination previousDestination = _destinations.put(
			destination.getName(), destination);

		if (previousDestination != null) {
			doRemoveDestination(previousDestination);
		}

		doAddDestination(destination);
	}

	@Override
	public boolean addMessageBusEventListener(
		MessageBusEventListener messageBusEventListener) {

		return _messageBusEventListeners.add(messageBusEventListener);
	}

	@Override
	public Destination getDestination(String destinationName) {
		return _destinations.get(destinationName);
	}

	@Override
	public int getDestinationCount() {
		return _destinations.size();
	}

	@Override
	public Collection<Destination> getDestinations() {
		return Collections.unmodifiableCollection(_destinations.values());
	}

	@Override
	public boolean hasDestination(String destinationName) {
		return _destinations.containsKey(destinationName);
	}

	@Override
	public Destination removeDestination(String destinationName) {
		Destination destination = _destinations.remove(destinationName);

		if (destination != null) {
			doRemoveDestination(destination);
		}

		return destination;
	}

	@Override
	public boolean removeMessageBusEventListener(
		MessageBusEventListener messageBusEventListener) {

		return _messageBusEventListeners.remove(messageBusEventListener);
	}

	@Override
	public void replace(Destination destination) {
		while (true) {
			Destination oldDestination = _destinations.get(
				destination.getName());

			if (oldDestination == null) {
				throw new IllegalArgumentException(
					"No exist destination with name " + destination.getName());
			}

			// Back up listeners, in case concurrent replace failed, they are
			// needed to restore destination

			Set<DestinationEventListener> destinationEventListeners =
				new HashSet<>(destination.getDestinationEventListeners());

			Set<MessageListener> messageListeners = new HashSet<>(
				destination.getMessageListeners());

			oldDestination.copyDestinationEventListeners(destination);
			oldDestination.copyMessageListeners(destination);

			if (_destinations.replace(
					destination.getName(), oldDestination, destination)) {

				doRemoveDestination(oldDestination);
				doAddDestination(destination);

				return;
			}

			// Concurrent replace failed, clean and restore destination, then
			// try again.

			destination.removeDestinationEventListeners();
			destination.unregisterMessageListeners();

			for (DestinationEventListener destinationEventListener :
					destinationEventListeners) {

				destination.addDestinationEventListener(
					destinationEventListener);
			}

			for (MessageListener messageListener : messageListeners) {
				destination.register(messageListener);
			}
		}
	}

	@Override
	public void sendMessage(String destinationName, Message message) {
		Destination destination = _destinations.get(destinationName);

		if (destination == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Destination " + destinationName + " is not configured");
			}

			return;
		}

		message.setDestinationName(destinationName);

		destination.send(message);
	}

	@Override
	public void shutdown() {
		shutdown(false);
	}

	@Override
	public void shutdown(boolean force) {
		Collection<Destination> destinations = _destinations.values();

		Iterator<Destination> iterator = destinations.iterator();

		while (iterator.hasNext()) {
			Destination destination = iterator.next();

			destination.close(force);

			iterator.remove();
		}
	}

	protected void doAddDestination(Destination destination) {
		for (MessageBusEventListener messageBusEventListener :
				_messageBusEventListeners) {

			messageBusEventListener.destinationAdded(destination);
		}
	}

	protected void doRemoveDestination(Destination destination) {
		destination.removeDestinationEventListeners();
		destination.unregisterMessageListeners();

		for (MessageBusEventListener messageBusEventListener :
				_messageBusEventListeners) {

			messageBusEventListener.destinationRemoved(destination);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DefaultMessageBus.class);

	private final ConcurrentMap<String, Destination> _destinations =
		new ConcurrentHashMap<>();
	private final Set<MessageBusEventListener> _messageBusEventListeners =
		new ConcurrentHashSet<>();

}
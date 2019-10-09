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

package com.liferay.portal.messaging.internal;

import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.InvokerMessageListener;
import com.liferay.portal.kernel.messaging.MessageBusEventListener;
import com.liferay.portal.kernel.messaging.MessageListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Tina Tian
 */
public class DefaultMessageBusTest {

	@Before
	public void setUp() {
		_defaultMessageBus = new DefaultMessageBus();

		_testMessageBusEventListener = new TestMessageBusEventListener();

		_defaultMessageBus.registerMessageBusEventListener(
			_testMessageBusEventListener);

		_testMessageListener1 = message -> {
		};
		_testMessageListener2 = message -> {
		};
	}

	@Test
	public void testRegisterDestination() {
		_defaultMessageBus.registerMessageListener(
			_TEST_DESTINATION_NAME, _testMessageListener1);
		_defaultMessageBus.registerMessageListener(
			_TEST_DESTINATION_NAME, _testMessageListener2);

		// Test 1

		_testMessageBusEventListener.reset();

		Destination destination1 = _registerDestination(
			_TEST_DESTINATION_NAME, 1);

		Assert.assertSame(
			destination1,
			_defaultMessageBus.getDestination(_TEST_DESTINATION_NAME));

		_assertMessageListener(
			destination1, _testMessageListener1, _testMessageListener2);

		Assert.assertNull(_testMessageBusEventListener.getRemovedDestination());
		Assert.assertSame(
			destination1, _testMessageBusEventListener.getAddedDestination());

		// Test 2

		_testMessageBusEventListener.reset();

		_registerDestination(_TEST_DESTINATION_NAME, -1);

		Assert.assertSame(
			destination1,
			_defaultMessageBus.getDestination(_TEST_DESTINATION_NAME));

		_assertMessageListener(
			destination1, _testMessageListener1, _testMessageListener2);

		Assert.assertNull(_testMessageBusEventListener.getRemovedDestination());
		Assert.assertNull(_testMessageBusEventListener.getAddedDestination());

		// Test 3

		_testMessageBusEventListener.reset();

		Destination destination2 = _registerDestination(
			_TEST_DESTINATION_NAME, 2);

		Assert.assertSame(
			destination2,
			_defaultMessageBus.getDestination(_TEST_DESTINATION_NAME));

		_assertMessageListener(
			destination2, _testMessageListener1, _testMessageListener2);

		MessageListener testmessageListener3 = message -> {
		};

		_defaultMessageBus.registerMessageListener(
			_TEST_DESTINATION_NAME, testmessageListener3);

		_defaultMessageBus.unregisterMessageListener(
			_TEST_DESTINATION_NAME, _testMessageListener2);

		_assertMessageListener(
			destination2, _testMessageListener1, testmessageListener3);

		Assert.assertSame(
			destination1, _testMessageBusEventListener.getRemovedDestination());
		Assert.assertSame(
			destination2, _testMessageBusEventListener.getAddedDestination());

		// Test 4

		_testMessageBusEventListener.reset();

		Destination destination3 = _registerDestination(
			_TEST_DESTINATION_NAME, 3);

		Assert.assertSame(
			destination3,
			_defaultMessageBus.getDestination(_TEST_DESTINATION_NAME));

		_assertMessageListener(
			destination3, _testMessageListener1, testmessageListener3);

		Assert.assertSame(
			destination2, _testMessageBusEventListener.getRemovedDestination());
		Assert.assertSame(
			destination3, _testMessageBusEventListener.getAddedDestination());
	}

	@Test
	public void testUnregisterDestination() {
		Destination destination1 = _registerDestination(
			_TEST_DESTINATION_NAME, 1);

		Destination destination2 = _registerDestination(
			_TEST_DESTINATION_NAME, 2);

		Destination destination3 = _registerDestination(
			_TEST_DESTINATION_NAME, 3);

		_defaultMessageBus.registerMessageListener(
			_TEST_DESTINATION_NAME, _testMessageListener1);
		_defaultMessageBus.registerMessageListener(
			_TEST_DESTINATION_NAME, _testMessageListener2);

		Assert.assertSame(
			destination3,
			_defaultMessageBus.getDestination(_TEST_DESTINATION_NAME));

		_assertMessageListener(
			destination3, _testMessageListener1, _testMessageListener2);

		Assert.assertFalse(destination1.isRegistered());
		Assert.assertFalse(destination2.isRegistered());

		// Test 1

		_testMessageBusEventListener.reset();

		_defaultMessageBus.unregisterDestination(
			destination3, Collections.singletonMap("service.ranking", 3));

		Assert.assertSame(
			destination2,
			_defaultMessageBus.getDestination(_TEST_DESTINATION_NAME));

		_assertMessageListener(
			destination2, _testMessageListener1, _testMessageListener2);

		Assert.assertSame(
			destination3, _testMessageBusEventListener.getRemovedDestination());
		Assert.assertSame(
			destination2, _testMessageBusEventListener.getAddedDestination());

		// Test 2

		_testMessageBusEventListener.reset();

		_defaultMessageBus.unregisterDestination(
			destination1, Collections.singletonMap("service.ranking", 1));

		Assert.assertSame(
			destination2,
			_defaultMessageBus.getDestination(_TEST_DESTINATION_NAME));

		_assertMessageListener(
			destination2, _testMessageListener1, _testMessageListener2);

		Assert.assertNull(_testMessageBusEventListener.getRemovedDestination());
		Assert.assertNull(_testMessageBusEventListener.getAddedDestination());

		// Test 3

		_testMessageBusEventListener.reset();

		_defaultMessageBus.unregisterDestination(
			destination2, Collections.singletonMap("service.ranking", 3));

		Assert.assertSame(
			destination2,
			_defaultMessageBus.getDestination(_TEST_DESTINATION_NAME));

		_assertMessageListener(
			destination2, _testMessageListener1, _testMessageListener2);

		Assert.assertNull(_testMessageBusEventListener.getRemovedDestination());
		Assert.assertNull(_testMessageBusEventListener.getAddedDestination());

		// Test 4

		_testMessageBusEventListener.reset();

		_defaultMessageBus.unregisterDestination(
			destination2, Collections.singletonMap("service.ranking", 2));

		Assert.assertNull(
			_defaultMessageBus.getDestination(_TEST_DESTINATION_NAME));

		Assert.assertSame(
			destination2, _testMessageBusEventListener.getRemovedDestination());
		Assert.assertNull(_testMessageBusEventListener.getAddedDestination());

		// Test 5

		_testMessageBusEventListener.reset();

		_defaultMessageBus.unregisterDestination(
			destination2, Collections.singletonMap("service.ranking", 2));

		Assert.assertNull(
			_defaultMessageBus.getDestination(_TEST_DESTINATION_NAME));

		Assert.assertNull(_testMessageBusEventListener.getRemovedDestination());
		Assert.assertNull(_testMessageBusEventListener.getAddedDestination());
	}

	private void _assertMessageListener(
		Destination destination, MessageListener... messageListeners) {

		List<MessageListener> messageListenerList = Arrays.asList(
			messageListeners);

		Set<MessageListener> messageListenerSet =
			destination.getMessageListeners();

		for (MessageListener messageListener : messageListenerSet) {
			InvokerMessageListener invokerMessageListener =
				(InvokerMessageListener)messageListener;

			messageListenerList.contains(invokerMessageListener);
		}

		Assert.assertEquals(
			messageListenerSet.toString(), messageListeners.length,
			messageListenerSet.size());
	}

	private Destination _registerDestination(
		String destinationName, int serviceRanking) {

		SynchronousDestination synchronousDestination =
			new SynchronousDestination();

		synchronousDestination.setName(_TEST_DESTINATION_NAME);

		Map<String, Object> properties = new HashMap<>();

		properties.put("destination.name", destinationName);
		properties.put("service.ranking", serviceRanking);

		_defaultMessageBus.registerDestination(
			synchronousDestination, properties);

		return synchronousDestination;
	}

	private static final String _TEST_DESTINATION_NAME = "test/destination";

	private DefaultMessageBus _defaultMessageBus;
	private TestMessageBusEventListener _testMessageBusEventListener;
	private MessageListener _testMessageListener1;
	private MessageListener _testMessageListener2;

	private class TestMessageBusEventListener
		implements MessageBusEventListener {

		@Override
		public void destinationAdded(Destination destination) {
			_addedDestination = destination;
		}

		@Override
		public void destinationRemoved(Destination destination) {
			_removedDestination = destination;
		}

		public Destination getAddedDestination() {
			return _addedDestination;
		}

		public Destination getRemovedDestination() {
			return _removedDestination;
		}

		public void reset() {
			_addedDestination = null;
			_removedDestination = null;
		}

		private Destination _addedDestination;
		private Destination _removedDestination;

	}

}
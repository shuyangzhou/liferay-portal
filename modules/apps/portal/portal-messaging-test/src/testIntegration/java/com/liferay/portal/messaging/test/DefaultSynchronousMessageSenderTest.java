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

package com.liferay.portal.messaging.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.DestinationConfiguration;
import com.liferay.portal.kernel.messaging.DestinationFactory;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageBusException;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.sender.SynchronousMessageSender;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Shuyang Zhou
 */
@RunWith(Arquillian.class)
public class DefaultSynchronousMessageSenderTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() {
		_destinations = ReflectionTestUtil.getFieldValue(
			_messageBus, "_destinations");

		Destination synchronousDestination =
			_destinationFactory.createDestination(
				DestinationConfiguration.
					createSynchronousDestinationConfiguration(
						DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE));

		_orignalDestination = _destinations.put(
			synchronousDestination.getName(), synchronousDestination);

		synchronousDestination.open();
	}

	@After
	public void tearDown() {
		Destination synchronousDestination = _destinations.put(
			DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE, _orignalDestination);

		synchronousDestination.destroy();
	}

	@Test
	public void testSendToAsyncDestination() throws MessageBusException {
		Destination serialDestination = _destinationFactory.createDestination(
			DestinationConfiguration.createSerialDestinationConfiguration(
				"testSerialDestination"));

		serialDestination.open();

		doTestSend(serialDestination);
	}

	@Test
	public void testSendToSynchronousDestination() throws MessageBusException {
		Destination synchronousDestination =
			_destinationFactory.createDestination(
				DestinationConfiguration.
					createSynchronousDestinationConfiguration(
						"testSynchronousDestination"));

		synchronousDestination.open();

		doTestSend(synchronousDestination);
	}

	protected void doTestSend(Destination destination)
		throws MessageBusException {

		Object response = new Object();

		destination.register(new ReplayMessageListener(response));

		_destinations.put(destination.getName(), destination);

		try {
			Assert.assertSame(
				response,
				_synchronousMessageSender.send(
					destination.getName(), new Message()));
		}
		finally {
			_destinations.remove(destination.getName());

			destination.destroy();
		}
	}

	@Inject
	private DestinationFactory _destinationFactory;

	private Map<String, Destination> _destinations;

	@Inject
	private MessageBus _messageBus;

	private Destination _orignalDestination;

	@Inject(filter = "mode=DEFAULT", type = SynchronousMessageSender.class)
	private SynchronousMessageSender _synchronousMessageSender;

	private class ReplayMessageListener implements MessageListener {

		public ReplayMessageListener(Object response) {
			_response = response;
		}

		@Override
		public void receive(Message message) {
			Message responseMessage = new Message();

			responseMessage.setDestinationName(
				message.getResponseDestinationName());
			responseMessage.setResponseId(message.getResponseId());

			responseMessage.setPayload(_response);

			_messageBus.sendMessage(
				message.getResponseDestinationName(), responseMessage);
		}

		private final Object _response;

	}

}
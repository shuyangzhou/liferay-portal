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

package com.liferay.change.tracking.internal.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.service.CTCollectionLocalService;
import com.liferay.change.tracking.service.CTMessageLocalService;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.petra.lang.SafeClosable;
import com.liferay.portal.kernel.change.tracking.CTCollectionThreadLocal;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.DestinationConfiguration;
import com.liferay.portal.kernel.messaging.DestinationFactory;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Tina Tian
 */
@RunWith(Arquillian.class)
public class MessageBusCTTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Bundle bundle = FrameworkUtil.getBundle(MessageBusCTTest.class);

		_bundleContext = bundle.getBundleContext();
	}

	@Before
	public void setUp() throws Exception {
		_ctCollection = _ctCollectionLocalService.createCTCollection(
			_counterLocalService.increment());

		_ctCollection.setCompanyId(TestPropsValues.getCompanyId());
		_ctCollection.setUserId(TestPropsValues.getUserId());

		_ctCollection = _ctCollectionLocalService.updateCTCollection(
			_ctCollection);
	}

	@After
	public void tearDown() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	@Test
	public void testPublish() throws PortalException {
		_registerDestination(
			DestinationConfiguration.DESTINATION_TYPE_SYNCHRONOUS);

		Message message = new Message();

		message.setPayload(_TEST_MESSAGE_PAYLOAD);
		message.setDestinationName(_TEST_DESTINATION_NAME);

		_ctMessageLocalService.addCTMessage(
			_ctCollection.getCtCollectionId(), message);

		ServiceReference<?> serviceReference =
			_bundleContext.getServiceReference(
				"com.liferay.change.tracking.internal.background.task." +
					"CTMessagePublisher");

		Assert.assertNull(_testMessageListener.getReceivedMessage());

		ReflectionTestUtil.invoke(
			_bundleContext.getService(serviceReference), "publish",
			new Class<?>[] {long.class}, _ctCollection.getCtCollectionId());

		Message receivedMessage = _testMessageListener.getReceivedMessage();

		Assert.assertEquals(message.getPayload(), receivedMessage.getPayload());

		_bundleContext.ungetService(serviceReference);
	}

	@Test
	public void testSendMessage() {
		_registerDestination(DestinationConfiguration.DESTINATION_TYPE_SERIAL);

		Message message = new Message();

		message.setPayload(_TEST_MESSAGE_PAYLOAD);

		try (SafeClosable safeClosable1 =
				CTCollectionThreadLocal.setCTCollectionId(
					_ctCollection.getCtCollectionId())) {

			_messageBus.sendMessage(_TEST_DESTINATION_NAME, message);
		}

		List<Message> messages = _ctMessageLocalService.getMessages(
			_ctCollection.getCtCollectionId());

		Assert.assertSame(messages.toString(), 1, messages.size());

		Message deSerializedMessage = messages.get(0);

		Assert.assertEquals(
			message.getPayload(), deSerializedMessage.getPayload());
	}

	private void _registerDestination(String destinationType) {
		DestinationConfiguration destinationConfiguration =
			new DestinationConfiguration(
				destinationType, _TEST_DESTINATION_NAME);

		Destination destination = _destinationFactory.createDestination(
			destinationConfiguration);

		_testMessageListener = new TestMessageListener();

		destination.register(_testMessageListener);

		_serviceRegistration = _bundleContext.registerService(
			Destination.class, destination,
			MapUtil.singletonDictionary(
				"destination.name", _TEST_DESTINATION_NAME));
	}

	private static final String _TEST_DESTINATION_NAME =
		"TEST_DESTINATION_NAME";

	private static final String _TEST_MESSAGE_PAYLOAD = "TEST_MESSAGE_PAYLOAD";

	private static BundleContext _bundleContext;

	@Inject
	private static CounterLocalService _counterLocalService;

	@Inject
	private static CTCollectionLocalService _ctCollectionLocalService;

	@Inject
	private static CTMessageLocalService _ctMessageLocalService;

	@Inject
	private static DestinationFactory _destinationFactory;

	@Inject
	private static MessageBus _messageBus;

	@DeleteAfterTestRun
	private CTCollection _ctCollection;

	private ServiceRegistration<Destination> _serviceRegistration;
	private TestMessageListener _testMessageListener;

	private class TestMessageListener implements MessageListener {

		public Message getReceivedMessage() {
			return _message;
		}

		@Override
		public void receive(Message message) {
			_message = message;
		}

		private Message _message;

	}

}
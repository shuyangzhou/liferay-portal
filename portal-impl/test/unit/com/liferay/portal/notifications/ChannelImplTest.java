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

package com.liferay.portal.notifications;

import com.liferay.portal.json.JSONObjectImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.notifications.Channel;
import com.liferay.portal.kernel.notifications.ChannelException;
import com.liferay.portal.kernel.notifications.NotificationEvent;
import com.liferay.portal.kernel.notifications.NotificationEventFactoryUtil;
import com.liferay.portal.kernel.security.SecureRandomUtil;
import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;
import com.liferay.portal.kernel.test.NewEnv;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.UserNotificationEvent;
import com.liferay.portal.model.impl.UserNotificationEventImpl;
import com.liferay.portal.service.UserNotificationEventLocalService;
import com.liferay.portal.service.UserNotificationEventLocalServiceUtil;
import com.liferay.portal.test.AdviseWith;
import com.liferay.portal.test.AspectJNewEnvTestRule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Andrew Betts
 */
public class ChannelImplTest {

	@ClassRule
	@Rule
	public static final AspectJNewEnvTestRule aspectJNewEnvTestRule =
		AspectJNewEnvTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_testChannelListener = new TestChannelListener();
		_channel.registerChannelListener(_testChannelListener);

		_throwJSONException = new AtomicBoolean();
		_throwException = new AtomicBoolean();
		_throwSystemException = new AtomicBoolean();

		_notificationEvents = ReflectionTestUtil.getFieldValue(
			_channel, "_notificationEvents");
		_unconfirmedNotificationEvents = ReflectionTestUtil.getFieldValue(
			_channel, "_unconfirmedNotificationEvents");

		ReflectionTestUtil.setFieldValue(
			NotificationEventFactoryUtil.class, "_notificationEventFactory",
			new NotificationEventFactoryImpl()
		);

		ReflectionTestUtil.setFieldValue(
			JSONFactoryUtil.class, "_jsonFactory",
			ProxyUtil.newProxyInstance(
				JSONFactoryUtil.class.getClassLoader(),
				new Class[]{JSONFactory.class},
				new JSONFactoryProxyInvocationHandler()));

		ReflectionTestUtil.setFieldValue(
			UserNotificationEventLocalServiceUtil.class, "_service",
			ProxyUtil.newProxyInstance(
				UserNotificationEventLocalServiceUtil.class.getClassLoader(),
				new Class[]{UserNotificationEventLocalService.class},
				new UserNotificationEventLocalServiceProxyInvocationHandler()));
	}

	@Test
	public void testCleanUpPropertyDisabled() throws Exception {
		_prepareNotificationEventLists();

		_channel.cleanUp();

		Assert.assertEquals(8, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(8, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			EnableUserNotificationEventConfirmationAdvice.class,
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testCleanUpPropertyEnabled() throws Exception {
		_prepareNotificationEventLists();

		_channel.cleanUp();

		Assert.assertEquals(8, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(8, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testCleanUpPropertyEnabledThrowsException() {
		_prepareNotificationEventLists();

		_throwException.set(true);

		try {
			_channel.cleanUp();

			Assert.fail();
		}
		catch (ChannelException ce) {
			String message = ce.getMessage();

			Assert.assertTrue(message.contains("Unable to clean up channel"));
		}

		Assert.assertEquals(8, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(8, _notificationEvents.size());
	}

	@Test
	public void testClone() {
		Channel channel = _channel.clone(
			CompanyConstants.SYSTEM, _channel.getUserId());

		Assert.assertNotSame(_channel, channel);
	}

	@Test
	public void testConfirmDeliveryPropertyDisabled() throws Exception {
		_prepareNotificationEventLists();

		List<String> uuids = new ArrayList<String>();

		for ( String uuid :_unconfirmedNotificationEvents.keySet()) {
			uuids.add(uuid);
		}

		for (String uuid: uuids) {
			_channel.confirmDelivery(uuid);
		}

		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testConfirmDeliveryPropertyEnabled() throws Exception {
		_prepareNotificationEventLists();

		List<String> uuids = new ArrayList<String>();

		for ( String uuid :_unconfirmedNotificationEvents.keySet()) {
			uuids.add(uuid);
		}

		for (String uuid: uuids) {
			_channel.confirmDelivery(uuid);
		}

		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testConfirmDeliveryPropertyEnabledArchived() throws Exception {
		_prepareNotificationEventLists();

		List<String> uuids = new ArrayList<String>();

		for ( String uuid :_unconfirmedNotificationEvents.keySet()) {
			uuids.add(uuid);
		}

		for (String uuid: uuids) {
			_channel.confirmDelivery(uuid, true);
		}

		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testConfirmDeliveryPropertyEnabledThrowsException()
		throws Exception {

		_prepareNotificationEventLists();

		_throwException.set(true);

		try {
			List<String> uuids = new ArrayList<String>();

			for ( String uuid :_unconfirmedNotificationEvents.keySet()) {
				uuids.add(uuid);
			}

			for (String uuid: uuids) {
				_channel.confirmDelivery(uuid);
			}

			Assert.fail();
		}
		catch (ChannelException e) {
		}

		Assert.assertEquals(12, _unconfirmedNotificationEvents.size());
	}

	@Test
	public void testDeleteUserNotificiationEvent() throws ChannelException {
		_prepareNotificationEventLists();

		List<String> uuids = new ArrayList<String>();

		for ( String uuid :_unconfirmedNotificationEvents.keySet()) {
			uuids.add(uuid);
		}

		for (String uuid: uuids) {
			_channel.deleteUserNotificiationEvent(uuid);
		}

		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
	}

	@Test
	public void testDeleteUserNotificiationEventThrowsException()
		throws ChannelException {

		_prepareNotificationEventLists();

		_throwException.set(true);

		try {
			List<String> uuids = new ArrayList<String>();

			for ( String uuid :_unconfirmedNotificationEvents.keySet()) {
				uuids.add(uuid);
			}

			for (String uuid: uuids) {
				_channel.deleteUserNotificiationEvent(uuid);
			}

			Assert.fail();
		}
		catch (ChannelException e) {
		}

		Assert.assertEquals(12, _unconfirmedNotificationEvents.size());
	}

	@Test
	public void testFlush() throws Exception {
		_prepareNotificationEventLists();

		_channel.flush();

		Assert.assertEquals(0, _notificationEvents.size());
	}

	@Test
	public void testFlushBeforeTimestamp() throws Exception {
		_prepareNotificationEventLists();

		NotificationEvent lastNotification = _notificationEvents.last();

		_channel.flush(lastNotification.getTimestamp());

		Assert.assertEquals(1, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testGetNotificationEvents() throws Exception {
		_prepareNotificationEventLists();

		List<NotificationEvent> notificationEvents =
			_channel.getNotificationEvents(false);

		Assert.assertEquals(14, notificationEvents.size());
		Assert.assertEquals(4, _notificationEvents.size());
		Assert.assertEquals(10, _unconfirmedNotificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testGetNotificationEventsFlush() throws Exception {
		_prepareNotificationEventLists();

		List<NotificationEvent> notificationEvents =
			_channel.getNotificationEvents(true);

		Assert.assertEquals(14, notificationEvents.size());
		Assert.assertEquals(0, _notificationEvents.size());
		Assert.assertEquals(10, _unconfirmedNotificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testGetNotificationEventsThrowsException() throws Exception {
		_prepareNotificationEventLists();

		_throwException.set(true);

		try {
			_channel.getNotificationEvents(false);

			Assert.fail();
		}
		catch (ChannelException e) {
		}

		Assert.assertEquals(4, _notificationEvents.size());
		Assert.assertEquals(10, _unconfirmedNotificationEvents.size());
	}

	@Test
	public void testInitPropertyDisabled() throws Exception {
		_channel.init();

		Assert.assertTrue(_unconfirmedNotificationEvents.isEmpty());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testInitPropertyEnabled() throws Exception {
		_channel.init();

		Assert.assertEquals(8, _unconfirmedNotificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testInitThrowsJSONException() throws Exception {
		_throwJSONException.set(true);

		CaptureHandler captureHandler = JDKLoggerTestUtil.configureJDKLogger(
			ChannelImpl.class.getName(), Level.ALL);

		_channel.init();

		List<LogRecord> list = captureHandler.getLogRecords();

		LogRecord logRecord = list.get(0);

		Assert.assertSame(_jsone, logRecord.getThrown());

		captureHandler.close();
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testInitThrowsSystemException() throws Exception {
		_throwSystemException.set(true);

		try {
			_channel.init();

			Assert.fail();
		}
		catch (ChannelException e) {
			String message = e.getMessage();

			Assert.assertTrue(message.contains("Unable to init channel"));
		}
	}

	@Test
	public void testRemoveTransientNotificationEvents() {
		_prepareNotificationEventLists();

		Set<NotificationEvent> notificationEventSet =
			new HashSet<NotificationEvent>();

		notificationEventSet.add(_notificationEvents.last());

		_channel.removeTransientNotificationEvents(notificationEventSet);

		Assert.assertEquals(11, _notificationEvents.size());
	}

	@Test
	public void testRemoveTransientNotificationEventsByUuid() {
		_prepareNotificationEventLists();

		Set<String> notificationEventUuidSet = new HashSet<String>();

		NotificationEvent notificationEvent = _notificationEvents.last();

		notificationEventUuidSet.add(notificationEvent.getUuid());

		_channel.removeTransientNotificationEventsByUuid(
			notificationEventUuidSet);

		Assert.assertEquals(11, _notificationEvents.size());
	}

	@Test
	public void testRemoveTransientNotificationEventsByUuidEventNotExists() {
		_prepareNotificationEventLists();

		Set<String> notificationEventSet = new HashSet<String>();

		NotificationEvent notificationEvent = _createNotificationEvent(
			0, false, false);

		notificationEventSet.add(notificationEvent.getUuid());

		_channel.removeTransientNotificationEventsByUuid(notificationEventSet);

		Assert.assertEquals(12, _notificationEvents.size());
	}

	@Test
	public void testSendNotificationEventPropertyDisabled() throws Exception {
		List<NotificationEvent> possibleNotificationEvents =
			_createAllPossibleNotifications();

		for (NotificationEvent notificationEvent : possibleNotificationEvents) {
			_channel.sendNotificationEvent(notificationEvent);
		}

		Assert.assertEquals(
			possibleNotificationEvents.size(),
			_testChannelListener.getNofityCount());
		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(8, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testSendNotificationEventPropertyEnabled() throws Exception {
		List<NotificationEvent> possibleNotificationEvents =
			_createAllPossibleNotifications();

		for (NotificationEvent notificationEvent : possibleNotificationEvents) {
			_channel.sendNotificationEvent(notificationEvent);
		}

		Assert.assertEquals(12, _testChannelListener.getNofityCount());
		Assert.assertEquals(4, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(4, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testSendNotificationEventPropertyEnabledThrowsException() {
		List<NotificationEvent> possibleNotificationEvents =
			_createAllPossibleNotifications();

		_throwException.set(true);

		try {
			for (NotificationEvent notificationEvent :
					possibleNotificationEvents) {

				_channel.sendNotificationEvent(notificationEvent);
			}

			Assert.fail();
		}
		catch (ChannelException ce) {
			String message = ce.getMessage();

			Assert.assertTrue(message.contains("Unable to send event"));
		}

		Assert.assertEquals(2, _testChannelListener.getNofityCount());
		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(2, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			MaxNotificationEventsAdvice.class
	})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testStoreNotificationEventMaxNotifications() throws Exception {
		for (NotificationEvent notificationEvent :
				_createAllPossibleNotifications()) {

			_channel.storeNotificationEvent(
				notificationEvent, System.currentTimeMillis());
		}

		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(
			MaxNotificationEventsAdvice.MAX, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testStoreNotificationEventPropertyEnabled() throws Exception {
		for (NotificationEvent notificationEvent :
				_createAllPossibleNotifications()) {

			_channel.storeNotificationEvent(
				notificationEvent, System.currentTimeMillis());
		}

		Assert.assertEquals(4, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(4, _notificationEvents.size());
	}

	@Aspect
	public static class EnableUserNotificationEventConfirmationAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues." +
				"USER_NOTIFICATION_EVENT_CONFIRMATION_ENABLED)")
		public Object enableUserNotificationEventConfirmation(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[] {Boolean.TRUE});
		}

	}

	@Aspect
	public static class MaxNotificationEventsAdvice {

		public static int MAX = 3;

		@Around(
			"set(* com.liferay.portal.util.PropsValues." +
				"NOTIFICATIONS_MAX_EVENTS)")
		public Object disableUserNotificationEventConfirmation(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[]{MAX});
		}

	}

	public class JSONFactoryProxyInvocationHandler
		implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

			if (_throwJSONException.get()) {
				throw _jsone;
			}

			if (!"createJSONObject".equals(method.getName())) {
				return null;
			}

			return new JSONObjectImpl();
		}

	}

	public class MockUserNotificationEventImpl
		extends UserNotificationEventImpl {

		public MockUserNotificationEventImpl(
			NotificationEvent notificationEvent) {

			_notificationEvent = notificationEvent;
		}

		@Override
		public long getDeliverBy() {
			return _notificationEvent.getDeliverBy();
		}

		@Override
		public String getPayload() {
			JSONObject payload = _notificationEvent.getPayload();
			return payload.toString();
		}

		@Override
		public long getTimestamp() {
			return _notificationEvent.getTimestamp();
		}

		@Override
		public String getType() {
			return _notificationEvent.getType();
		}

		public String getUuid() {
			return _notificationEvent.getUuid();
		}

		private final NotificationEvent _notificationEvent;

	}

	public class UserNotificationEventLocalServiceProxyInvocationHandler
		implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

			if (_throwException.get()) {
				throw new Exception();
			}

			if (_throwSystemException.get()) {
				throw new SystemException();
			}

			if (!"getDeliveredUserNotificationEvents".equals(
					method.getName())) {

				return null;
			}

			List<UserNotificationEvent> userNotificationEvents =
				new ArrayList<UserNotificationEvent>();

			for (NotificationEvent notificationEvent :
					_createAllPossibleNotifications()) {

				userNotificationEvents.add(
					new MockUserNotificationEventImpl(notificationEvent));
			}

			return userNotificationEvents;
		}

	}

	protected static List<NotificationEvent> _createAllPossibleNotifications() {
		List<NotificationEvent> notificationEvents =
			new ArrayList<NotificationEvent>();

		long deliverBy = 0;
		notificationEvents.add(_createNotificationEvent(deliverBy,false,false));
		notificationEvents.add(_createNotificationEvent(deliverBy,false,true));
		notificationEvents.add(_createNotificationEvent(deliverBy,true,false));
		notificationEvents.add(_createNotificationEvent(deliverBy,true,true));

		deliverBy = 1;
		notificationEvents.add(_createNotificationEvent(deliverBy,false,false));
		notificationEvents.add(_createNotificationEvent(deliverBy,false,true));
		notificationEvents.add(_createNotificationEvent(deliverBy,true,false));
		notificationEvents.add(_createNotificationEvent(deliverBy,true,true));

		deliverBy = System.currentTimeMillis() + Time.HOUR;
		notificationEvents.add(_createNotificationEvent(deliverBy,false,false));
		notificationEvents.add(_createNotificationEvent(deliverBy,false,true));
		notificationEvents.add(_createNotificationEvent(deliverBy,true,false));
		notificationEvents.add(_createNotificationEvent(deliverBy,true,true));

		return notificationEvents;
	}

	protected static NotificationEvent _createNotificationEvent(
		long deliverBy, boolean deliveryRequired, boolean archived) {

		try {
			Thread.sleep(1);
		}
		catch (InterruptedException ie) {
		}

		NotificationEvent notificationEvent = new NotificationEvent(
			System.currentTimeMillis(), ChannelImplTest.class.getName(),
			new JSONObjectImpl());

		UUID uuid = new UUID(
			SecureRandomUtil.nextLong(), SecureRandomUtil.nextLong());

		notificationEvent.setUuid(uuid.toString());
		notificationEvent.setArchived(archived);

		if (deliveryRequired) {
			notificationEvent.setDeliveryRequired(deliverBy);
		}
		else {
			notificationEvent.setDeliverBy(deliverBy);
		}

		return notificationEvent;
	}

	protected void _prepareNotificationEventLists() {
		_notificationEvents.addAll(_createAllPossibleNotifications());

		for (NotificationEvent notificationEvent :
			_createAllPossibleNotifications()) {

			_unconfirmedNotificationEvents.put(
				notificationEvent.getUuid(), notificationEvent);
		}
	}

	private final Channel _channel = new ChannelImpl();
	private final JSONException _jsone = new JSONException();
	private TreeSet<NotificationEvent> _notificationEvents;
	private TestChannelListener _testChannelListener;
	private AtomicBoolean _throwException;
	private AtomicBoolean _throwJSONException;
	private AtomicBoolean _throwSystemException;
	private Map<String, NotificationEvent> _unconfirmedNotificationEvents;

}
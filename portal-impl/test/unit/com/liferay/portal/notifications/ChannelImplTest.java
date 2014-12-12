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
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.notifications.Channel;
import com.liferay.portal.kernel.notifications.ChannelException;
import com.liferay.portal.kernel.notifications.ChannelListener;
import com.liferay.portal.kernel.notifications.NotificationEvent;
import com.liferay.portal.kernel.security.SecureRandomUtil;
import com.liferay.portal.kernel.test.AggregateTestRule;
import com.liferay.portal.kernel.test.CodeCoverageAssertor;
import com.liferay.portal.kernel.test.NewEnv;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.UserNotificationEvent;
import com.liferay.portal.test.AdviseWith;
import com.liferay.portal.test.AspectJNewEnvTestRule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.management.remote.NotificationResult;

/**
 * Created by liferay on 12/3/2014.
 */
public class ChannelImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			CodeCoverageAssertor.INSTANCE, AspectJNewEnvTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_channel = new ChannelImpl();

		_testChannelListener = new TestChannelListener();
		_channel.registerChannelListener(_testChannelListener);

		_throwUNELSUAdviceRuntimeException = new AtomicBoolean();
		_throwJSONAdviceJSONException = new AtomicBoolean();
		_throwUNELSUAdviceSystemException = new AtomicBoolean();

		_notificationEvents = ReflectionTestUtil.getFieldValue(
			_channel, "_notificationEvents");
		_unconfirmedNotificationEvents = ReflectionTestUtil.getFieldValue(
			_channel, "_unconfirmedNotificationEvents");
	}

	@Test
	public void testClone() {
		Channel channel = _channel.clone(
			CompanyConstants.SYSTEM, _channel.getUserId());

		Assert.assertNotSame(_channel, channel);
	}

	@AdviseWith(
		adviceClasses = {
			EnableUserNotificationEventConfirmationAdvice.class,
			UserNotificationEventLocalServiceUtilAdvice.class
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testConfirmDelivery0() throws Exception {
		Stack<String> uuidStack = new Stack<String>();

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			uuidStack.push(notificationEvent.getUuid());

			_unconfirmedNotificationEvents.put(
				uuidStack.peek(), notificationEvent);
		}

		_channel.confirmDelivery(uuidStack.pop());

		Assert.assertEquals(2, _unconfirmedNotificationEvents.size());

		_channel.confirmDelivery(uuidStack.pop(), true);

		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());

		_throwUNELSUAdviceRuntimeException.set(true);

		try {
			_channel.confirmDelivery(uuidStack.pop());

			Assert.fail();
		}
		catch (ChannelException e) {
		}

		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			DisableUserNotificationEventConfirmationAdvice.class
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testConfirmDelivery1() throws Exception {
		Stack<String> uuidStack = new Stack<String>();

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			uuidStack.push(notificationEvent.getUuid());

			_unconfirmedNotificationEvents.put(
				uuidStack.peek(), notificationEvent);
		}

		_channel.confirmDelivery(uuidStack.pop());

		Assert.assertEquals(2, _unconfirmedNotificationEvents.size());
	}

	@AdviseWith(adviceClasses =
		{UserNotificationEventLocalServiceUtilAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testDeleteUserNotificiationEvent()
		throws ChannelException {
		Stack<String> uuidStack = new Stack<String>();

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			uuidStack.push(notificationEvent.getUuid());

			_unconfirmedNotificationEvents.put(
				uuidStack.peek(), notificationEvent);
		}

		_channel.deleteUserNotificiationEvent(uuidStack.pop());

		Assert.assertEquals(2, _unconfirmedNotificationEvents.size());

		_throwUNELSUAdviceRuntimeException.set(true);

		try {
			_channel.deleteUserNotificiationEvent(uuidStack.pop());

			Assert.fail();
		}
		catch (ChannelException e) {
		}

		Assert.assertEquals(2, _unconfirmedNotificationEvents.size());
	}

	@Test
	public void testFlush() throws Exception {
		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			_notificationEvents.add(notificationEvent);
		}

		_channel.flush();

		Assert.assertEquals(0, _notificationEvents.size());
	}

	@Test
	public void testFlushBeforeTimestamp() throws Exception {
		long timestamp = 0;

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			timestamp = notificationEvent.getTimestamp();

			_notificationEvents.add(notificationEvent);
		}

		_channel.flush(timestamp);

		Assert.assertEquals(1, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			EnableUserNotificationEventConfirmationAdvice.class,
			UserNotificationEventLocalServiceUtilAdvice.class
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testGetNotifications() throws Exception {
		NotificationEvent expiredNotificationEvent =
			_createNotificationEvent();

		expiredNotificationEvent.setDeliverBy(1);

		_notificationEvents.add(expiredNotificationEvent);
		_notificationEvents.add(_createNotificationEvent());

		NotificationEvent expiredUnconfirmedEvent =
			_createNotificationEvent();

		expiredUnconfirmedEvent.setDeliverBy(1);

		_unconfirmedNotificationEvents.put(
			expiredUnconfirmedEvent.getUuid(), expiredUnconfirmedEvent);

		NotificationEvent unconfirmedNotificaitonEvent =
			_createNotificationEvent();

		_unconfirmedNotificationEvents.put(
			unconfirmedNotificaitonEvent.getUuid(),
			unconfirmedNotificaitonEvent);

		List<NotificationEvent> notificationEvents =
			_channel.getNotificationEvents(false);

		Assert.assertEquals(2, notificationEvents.size());
		Assert.assertEquals(1, _notificationEvents.size());
		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());

		expiredNotificationEvent = _createNotificationEvent();
		expiredNotificationEvent.setDeliverBy(1);

		_notificationEvents.add(expiredNotificationEvent);

		expiredUnconfirmedEvent = _createNotificationEvent();
		expiredUnconfirmedEvent.setDeliverBy(1);

		_unconfirmedNotificationEvents.put(
			expiredUnconfirmedEvent.getUuid(), expiredUnconfirmedEvent);

		_throwUNELSUAdviceRuntimeException.set(true);

		try {
			_channel.getNotificationEvents(false);

			Assert.fail();
		}
		catch (ChannelException e) {
		}

		_throwUNELSUAdviceRuntimeException.set(false);

		notificationEvents = _channel.getNotificationEvents(false);

		Assert.assertEquals(2, notificationEvents.size());
		Assert.assertEquals(1, _notificationEvents.size());
		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());

		expiredNotificationEvent = _createNotificationEvent();
		expiredNotificationEvent.setDeliverBy(1);

		_notificationEvents.add(expiredNotificationEvent);

		expiredUnconfirmedEvent = _createNotificationEvent();
		expiredUnconfirmedEvent.setDeliverBy(1);

		_unconfirmedNotificationEvents.put(
			expiredUnconfirmedEvent.getUuid(), expiredUnconfirmedEvent);

		NotificationEvent archivedNotificationEvent =
			_createNotificationEvent();

		archivedNotificationEvent.setDeliverBy(1);
		archivedNotificationEvent.setArchived(true);

		_unconfirmedNotificationEvents.put(
			archivedNotificationEvent.getUuid(),
			archivedNotificationEvent);

		Assert.assertEquals(3, _unconfirmedNotificationEvents.size());

		notificationEvents = _channel.getNotificationEvents(true);

		Assert.assertEquals(3, notificationEvents.size());
		Assert.assertEquals(0, _notificationEvents.size());
		Assert.assertEquals(2, _unconfirmedNotificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			EnableUserNotificationEventConfirmationAdvice.class,
			UserNotificationEventLocalServiceUtilAdvice.class,
			JSONFactoryUtilAdvice.class,
			NotificationEventFactoryUtilAdvice.class
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testInit0() throws Exception {
		_channel.init();

		Assert.assertEquals(3, _unconfirmedNotificationEvents.size());

		_throwUNELSUAdviceSystemException.set(true);

		try {
			_channel.init();

			Assert.fail();
		}
		catch (ChannelException e) {
		}

		_throwUNELSUAdviceSystemException.set(false);

		_throwJSONAdviceJSONException.set(true);

		try{
			_channel.init();

			Assert.fail();
		}
		catch (ChannelException ce) {

		}

	}

	@AdviseWith(
		adviceClasses = {DisableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testInit1() throws Exception {
		_channel.init();

		Assert.assertTrue(_unconfirmedNotificationEvents.isEmpty());
		Assert.assertTrue(_notificationEvents.isEmpty());
	}

	@AdviseWith(
		adviceClasses = {DisableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testInit2() throws Exception {

		Assert.assertTrue(_unconfirmedNotificationEvents.isEmpty());
		Assert.assertTrue(_notificationEvents.isEmpty());
	}

	@AdviseWith(
		adviceClasses = {
			EnableUserNotificationEventConfirmationAdvice.class,
			UserNotificationEventLocalServiceUtilAdvice.class,
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testCleanUp0() throws Exception {
		NotificationEvent expiredNotificationEvent = _createNotificationEvent();

		expiredNotificationEvent.setDeliverBy(1);

		_notificationEvents.add(expiredNotificationEvent);
		_notificationEvents.add(_createNotificationEvent());

		NotificationEvent expiredNotificationEvent1 =
			_createNotificationEvent();

		expiredNotificationEvent1.setDeliverBy(1);

		NotificationEvent unconfirmedNotificaitonEvent =
			_createNotificationEvent();

		_unconfirmedNotificationEvents.put(
			expiredNotificationEvent1.getUuid(), expiredNotificationEvent1);
		_unconfirmedNotificationEvents.put(
			unconfirmedNotificaitonEvent.getUuid(),
			unconfirmedNotificaitonEvent);

		_channel.cleanUp();

		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(1, _notificationEvents.size());

		NotificationEvent expiredNotificationEvent2 =
			_createNotificationEvent();

		expiredNotificationEvent2.setDeliverBy(1);

		_notificationEvents.add(expiredNotificationEvent2);

		NotificationEvent expiredNotificationEvent3 =
			_createNotificationEvent();

		expiredNotificationEvent3.setDeliverBy(1);

		_unconfirmedNotificationEvents.put(
			expiredNotificationEvent3.getUuid(), expiredNotificationEvent3);

		_throwUNELSUAdviceRuntimeException.set(true);

		try {
			_channel.cleanUp();

			Assert.fail();
		}
		catch (ChannelException ce) {
		}

		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(1, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			EnableUserNotificationEventConfirmationAdvice.class,
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testCleanUp2() throws Exception {
		_notificationEvents.add(_createNotificationEvent());

		NotificationEvent unconfirmedNotificaitonEvent =
			_createNotificationEvent();

		_unconfirmedNotificationEvents.put(
			unconfirmedNotificaitonEvent.getUuid(),
			unconfirmedNotificaitonEvent);

		_channel.cleanUp();

		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(1, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			DisableUserNotificationEventConfirmationAdvice.class,
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testCleanUp3() throws Exception {
		NotificationEvent expiredNotificationEvent = _createNotificationEvent();

		expiredNotificationEvent.setDeliverBy(1);

		_notificationEvents.add(expiredNotificationEvent);
		_notificationEvents.add(_createNotificationEvent());

		NotificationEvent expiredNotificationEvent1 =
			_createNotificationEvent();

		expiredNotificationEvent1.setDeliverBy(1);

		NotificationEvent unconfirmedNotificaitonEvent =
			_createNotificationEvent();

		_unconfirmedNotificationEvents.put(
			expiredNotificationEvent1.getUuid(), expiredNotificationEvent1);
		_unconfirmedNotificationEvents.put(
			unconfirmedNotificaitonEvent.getUuid(),
			unconfirmedNotificaitonEvent);

		_channel.cleanUp();

		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(1, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			DisableUserNotificationEventConfirmationAdvice.class,
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testCleanUp1() throws Exception {
		_notificationEvents.add(_createNotificationEvent());

		NotificationEvent unconfirmedNotificaitonEvent =
			_createNotificationEvent();

		_unconfirmedNotificationEvents.put(
			unconfirmedNotificaitonEvent.getUuid(),
			unconfirmedNotificaitonEvent);

		_channel.cleanUp();

		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(1, _notificationEvents.size());
	}

	@Test
	public void testRemoveTransientNotificationEvents() {
		Set<NotificationEvent> notificationEventSet =
			new HashSet<NotificationEvent>(3);

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			_notificationEvents.add(notificationEvent);

			notificationEventSet.add(notificationEvent);
		}

		_channel.removeTransientNotificationEvents(notificationEventSet);

		Assert.assertEquals(0, _notificationEvents.size());

		ReflectionTestUtil.setFieldValue(
			_channel, "_notificationEvents", null);

		_channel.removeTransientNotificationEvents(notificationEventSet);
	}

	@Test
	public void testRemoveTransientNotificationEventsByUuid() {
		Set<String> notificationEventSet = new HashSet<String>(3);

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			_notificationEvents.add(notificationEvent);

			notificationEventSet.add(notificationEvent.getUuid());
		}

		_notificationEvents.add(_createNotificationEvent());

		Assert.assertEquals(4, _notificationEvents.size());

		_channel.removeTransientNotificationEventsByUuid(notificationEventSet);

		Assert.assertEquals(1, _notificationEvents.size());

		notificationEventSet.clear();

		notificationEventSet.add(_createNotificationEvent().getUuid());

		_channel.removeTransientNotificationEventsByUuid(notificationEventSet);

		Assert.assertEquals(1, _notificationEvents.size());

		ReflectionTestUtil.setFieldValue(
			_channel, "_notificationEvents", null);

		_channel.removeTransientNotificationEventsByUuid(notificationEventSet);

		Assert.assertEquals(1, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			EnableUserNotificationEventConfirmationAdvice.class,
			UserNotificationEventLocalServiceUtilAdvice.class
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testSendNotificationEvent0() throws Exception {
		NotificationEvent notificationEvent = _createNotificationEvent();

		notificationEvent.setDeliveryRequired(0);

		_channel.sendNotificationEvent(notificationEvent);

		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(0, _notificationEvents.size());
		Assert.assertEquals(1, _testChannelListener.getNofityListnerCount());

		NotificationEvent notificationEvent1 = _createNotificationEvent();

		_channel.sendNotificationEvent(notificationEvent1);

		Assert.assertEquals(2, _testChannelListener.getNofityListnerCount());
		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(1, _notificationEvents.size());

		_throwUNELSUAdviceRuntimeException.set(true);

		try {
			NotificationEvent notificationEvent2 = _createNotificationEvent();

			notificationEvent2.setDeliveryRequired(0);

			_channel.sendNotificationEvent(notificationEvent2);

			Assert.fail();
		}
		catch (ChannelException ce) {
		}

		Assert.assertEquals(2, _testChannelListener.getNofityListnerCount());
		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(1, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {DisableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testSendNotificationEvent1() throws Exception {
		NotificationEvent notificationEvent = _createNotificationEvent();

		notificationEvent.setDeliveryRequired(0);

		_channel.sendNotificationEvent(notificationEvent);

		Assert.assertEquals(1, _testChannelListener.getNofityListnerCount());
		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(1, _notificationEvents.size());

		NotificationEvent notificationEvent1 = _createNotificationEvent();

		_channel.sendNotificationEvent(notificationEvent1);

		Assert.assertEquals(2, _testChannelListener.getNofityListnerCount());
		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(2, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testStoreNotificationEvent0() throws Exception {
		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			notificationEvent.setDeliveryRequired(0);

			_channel.storeNotificationEvent(
				notificationEvent, System.currentTimeMillis());
		}

		Assert.assertEquals(3, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(0, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			DisableUserNotificationEventConfirmationAdvice.class,
			TenMaxNotificationEventsAdvice.class
	})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testStoreNotificationEvent1() throws Exception {
		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(0, _notificationEvents.size());

		for (int i = 0; i < 10; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			notificationEvent.setDeliveryRequired(0);

			_channel.storeNotificationEvent(
				notificationEvent, System.currentTimeMillis());
		}

		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(10, _notificationEvents.size());

		NotificationEvent notificationEvent = _createNotificationEvent();

		notificationEvent.setDeliveryRequired(0);

		_channel.storeNotificationEvent(
			notificationEvent, System.currentTimeMillis());

		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(10, _notificationEvents.size());
	}

	@Test
	public void testStorNotificationEventExpired() throws ChannelException {
		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			notificationEvent.setDeliverBy(System.currentTimeMillis());

			_channel.storeNotificationEvent(
				notificationEvent, System.currentTimeMillis());
		}

		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(0, _notificationEvents.size());
	}

	@Aspect
	public static class DisableUserNotificationEventConfirmationAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues." +
				"USER_NOTIFICATION_EVENT_CONFIRMATION_ENABLED)")
		public Object disableUserNotificationEventConfirmation(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[] {Boolean.FALSE});
		}

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
	public static class JSONFactoryUtilAdvice {

		@Around(
			"execution(" +
				"public static com.liferay.portal.kernel.json.JSONObject " +
				"com.liferay.portal.kernel.json.JSONFactoryUtil." +
				"createJSONObject(String)) && args(json)"
		)
		public JSONObject createJSONObject(String json) throws JSONException {
			if (_throwJSONAdviceJSONException.get()) {
				throw new JSONException();
			}

			return null;
		}
	}

	@Aspect
	public static class NotificationEventFactoryUtilAdvice {

		@Around(
			"execution(" +
				"public static com.liferay.portal.kernel.notifications.NotificationEvent " +
				"com.liferay.portal.kernel.notifications.NotificationEventFactoryUtil." +
				"createNotificationEvent(long, String, com.liferay.portal.kernel.json.JSONObject)) && " +
			"args(timestamp, type, payloadJSONObject)")
		public NotificationEvent createNotificationEvent(
			long timestamp, String type, JSONObject payloadJSONObject) {

			return new NotificationEvent(timestamp, type, payloadJSONObject);
		}
	}


	@Aspect
	public static class TenMaxNotificationEventsAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues." +
				"NOTIFICATIONS_MAX_EVENTS)")
		public Object disableUserNotificationEventConfirmation(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[] {10});
		}

	}

	@Aspect
	public static class UserNotificationEventLocalServiceUtilAdvice {

		@Around(
			"execution(" +
				"public static com.liferay.portal.model.UserNotificationEvent " +
				"com.liferay.portal.service.UserNotificationEventLocalServiceUtil." +
				"addUserNotificationEvent(long, com.liferay.portal.kernel.notifications.NotificationEvent)) && " +
			"args(userId, notificationEvent)")
		public UserNotificationEvent addUserNotificationEvent(
			long userId, NotificationEvent notificationEvent) {

			if (_throwUNELSUAdviceRuntimeException.get()) {
				throw new RuntimeException();
			}

			return _createUserNotificationEventProxy(notificationEvent);
		}

		@Around(
			"execution(" +
				"public static void " +
				"com.liferay.portal.service.UserNotificationEventLocalServiceUtil." +
				"deleteUserNotificationEvent(String, long)) && " +
			"args(uuid, companyId)")
		public void deleteUserNotificationEvent(String uuid, long companyId) {
			if (_throwUNELSUAdviceRuntimeException.get()) {
				throw new RuntimeException();
			}
		}

		@Around(
			"execution(" +
				"public static void " +
				"com.liferay.portal.service.UserNotificationEventLocalServiceUtil." +
				"deleteUserNotificationEvents(java.util.Collection<String>, long)) && " +
			"args(uuids, companyId)")
		public void deleteUserNotificationEvents(
			Collection<String> uuids, long companyId) {

			if (_throwUNELSUAdviceRuntimeException.get()) {
				throw new RuntimeException();
			}
		}

		@Around(
			"execution(" +
				"public static java.util.List<com.liferay.portal.model.UserNotificationEvent> " +
				"com.liferay.portal.service.UserNotificationEventLocalServiceUtil." +
				"getDeliveredUserNotificationEvents(long, boolean)) && " +
			"args(userId, delivered)")
		public List<UserNotificationEvent> getDeliveredUserNotificationEvents(
			long userId, boolean delivered) {

			if (_throwUNELSUAdviceRuntimeException.get()) {
				throw new RuntimeException();
			}

			if (_throwUNELSUAdviceSystemException.get()) {
				throw new SystemException();
			}

			List<UserNotificationEvent> userNotificationEvents =
				new ArrayList<UserNotificationEvent>();

			for (int i = 0; i < 3; i++) {
				userNotificationEvents.add(_createUserNotificationEventProxy());
			}

			NotificationEvent expiredNotificationEvent =
				_createNotificationEvent();

			expiredNotificationEvent.setDeliverBy(1);

			userNotificationEvents.add(
				_createUserNotificationEventProxy(expiredNotificationEvent));

			return userNotificationEvents;
		}

		@Around(
			"execution(" +
				"public static com.liferay.portal.model.UserNotificationEvent" +
				" com.liferay.portal.service." +
					"UserNotificationEventLocalServiceUtil." +
				"updateUserNotificationEvent(String, long, boolean)) && " +
			"args(uuid, companyId, archive)")
		public UserNotificationEvent updateUserNotificationEvent(
			String uuid, long companyId, boolean archive) {

			if (_throwUNELSUAdviceRuntimeException.get()) {
				throw new RuntimeException();
			}

			return _createUserNotificationEventProxy();
		}

	}

	private static NotificationEvent _createNotificationEvent() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		NotificationEvent notificationEvent = new NotificationEvent(
			System.currentTimeMillis(), ChannelImplTest.class.getName(),
			new JSONObjectImpl());

		UUID uuid = new UUID(
			SecureRandomUtil.nextLong(), SecureRandomUtil.nextLong());

		notificationEvent.setUuid(uuid.toString());
		notificationEvent.setArchived(false);

		return notificationEvent;
	}

	private static UserNotificationEvent _createUserNotificationEventProxy() {
		return (UserNotificationEvent)ProxyUtil.newProxyInstance(
			UserNotificationEvent.class.getClassLoader(),
			new Class<?>[]{UserNotificationEvent.class},
			new TestUserNotificationInvocationHandler(
				_createNotificationEvent()));
	}

	private static UserNotificationEvent _createUserNotificationEventProxy(
		NotificationEvent notificationEvent) {

		return (UserNotificationEvent)ProxyUtil.newProxyInstance(
			UserNotificationEvent.class.getClassLoader(),
			new Class<?>[]{UserNotificationEvent.class},
			new TestUserNotificationInvocationHandler(notificationEvent));
	}

	private static AtomicBoolean _throwUNELSUAdviceRuntimeException;
	private static AtomicBoolean _throwJSONAdviceJSONException;
	private static AtomicBoolean _throwUNELSUAdviceSystemException;

	private Channel _channel;
	private TestChannelListener _testChannelListener;
	private TreeSet<NotificationEvent> _notificationEvents;
	private Map<String, NotificationEvent> _unconfirmedNotificationEvents;

	private static class TestUserNotificationInvocationHandler
		implements InvocationHandler {

		public TestUserNotificationInvocationHandler(
			NotificationEvent notificationEvent) {

			_notificationEvent = notificationEvent;
		}

		@Override
		public Object invoke(
			Object proxy, Method method, Object[] args) throws Throwable {

			String methodName = method.getName();

			if (methodName.equals("getDeliverBy")) {
				return _notificationEvent.getDeliverBy();
			}
			else if (methodName.equals("getPayload")) {
				return _notificationEvent.getPayload().toString();
			}
			else if (methodName.equals("getTimestamp")) {
				return _notificationEvent.getTimestamp();
			}
			else if (methodName.equals("getType")) {
				return _notificationEvent.getType();
			}
			else if (methodName.equals("getUuid")) {
				return _notificationEvent.getUuid();
			}

			throw new UnsupportedOperationException();
		}

		private NotificationEvent _notificationEvent;

	}

	private class TestChannelListener implements ChannelListener {

		public TestChannelListener() {
			notifyListenerCount = 0;
		}

		@Override
		public void channelListenerRemoved(long channelId) {
		}

		public int getNofityListnerCount() {
			return notifyListenerCount;
		}

		@Override
		public void notificationEventsAvailable(long channelId) {
			notifyListenerCount++;
		}

		private int notifyListenerCount;

	}

}
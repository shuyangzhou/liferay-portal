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

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.notifications.BaseChannelImpl;
import com.liferay.portal.kernel.notifications.Channel;
import com.liferay.portal.kernel.notifications.ChannelException;
import com.liferay.portal.kernel.notifications.NotificationEvent;
import com.liferay.portal.kernel.notifications.NotificationEventComparator;
import com.liferay.portal.kernel.notifications.NotificationEventFactoryUtil;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.UserNotificationEvent;
import com.liferay.portal.service.UserNotificationEventLocalServiceUtil;
import com.liferay.portal.util.PropsValues;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Edward Han
 * @author Brian Wing Shun Chan
 * @author Jonathan Lee
 */
public class ChannelImpl extends BaseChannelImpl {

	public ChannelImpl() {
		this(CompanyConstants.SYSTEM, 0);
	}

	public ChannelImpl(long companyId, long usedId) {
		super(companyId, usedId);

		_notificationEvents = new TreeSet<NotificationEvent>(_comparator);
		_unconfirmedNotificationEvents =
			new LinkedHashMap<String, NotificationEvent>();
	}

	@Override
	public Channel clone(long companyId, long userId) {
		return new ChannelImpl(companyId, userId);
	}

	@Override
	public void confirmDelivery(String notificationEventUuid)
		throws ChannelException {

		confirmDelivery(notificationEventUuid, false);
	}

	@Override
	public void confirmDelivery(String notificationEventUuid, boolean archive)
		throws ChannelException {

		_reentrantLock.lock();

		try {
			if (PropsValues.USER_NOTIFICATION_EVENT_CONFIRMATION_ENABLED) {
				if (archive) {
					UserNotificationEventLocalServiceUtil.
						updateUserNotificationEvent(
							notificationEventUuid, getCompanyId(), archive);
				}
				else {
					UserNotificationEventLocalServiceUtil.
						deleteUserNotificationEvent(
							notificationEventUuid, getCompanyId());
				}
			}

			_unconfirmedNotificationEvents.remove(notificationEventUuid);
		}
		catch (Exception e) {
			throw new ChannelException(
				"Unable to confirm delivery for " + notificationEventUuid , e);
		}
		finally {
			_reentrantLock.unlock();
		}
	}

	@Override
	public void deleteUserNotificiationEvent(String notificationEventUuid)
		throws ChannelException {

		_reentrantLock.lock();

		try {
			UserNotificationEventLocalServiceUtil.deleteUserNotificationEvent(
				notificationEventUuid, getCompanyId());

			_unconfirmedNotificationEvents.remove(notificationEventUuid);
		}
		catch (Exception e) {
			throw new ChannelException(
				"Unable to delete event " + notificationEventUuid , e);
		}
		finally {
			_reentrantLock.unlock();
		}
	}

	@Override
	public void flush() {
		_reentrantLock.lock();

		try {
			_notificationEvents.clear();
		}
		finally {
			_reentrantLock.unlock();
		}
	}

	@Override
	public void flush(long timestamp) {
		_reentrantLock.lock();

		try {
			Iterator<NotificationEvent> itr = _notificationEvents.iterator();

			while (itr.hasNext()) {
				NotificationEvent notificationEvent = itr.next();

				if (notificationEvent.getTimestamp() < timestamp) {
					itr.remove();
				}
			}
		}
		finally {
			_reentrantLock.unlock();
		}
	}

	@Override
	public List<NotificationEvent> getNotificationEvents(boolean flush)
		throws ChannelException {

		_reentrantLock.lock();

		try {
			return doGetNotificationEvents(flush);
		}
		catch (Exception e) {
			throw new ChannelException(e);
		}
		finally {
			_reentrantLock.unlock();
		}
	}

	@Override
	public void init() throws ChannelException {
		_reentrantLock.lock();

		try {
			doInit();
		}
		catch (SystemException se) {
			throw new ChannelException(
				"Unable to init channel " + getUserId(), se);
		}
		finally {
			_reentrantLock.unlock();
		}
	}

	@Override
	public void removeTransientNotificationEvents(
		Collection<NotificationEvent> notificationEvents) {

		_reentrantLock.lock();

		try {
			if (_notificationEvents != null) {
				_notificationEvents.removeAll(notificationEvents);
			}
		}
		finally {
			_reentrantLock.unlock();
		}
	}

	@Override
	public void removeTransientNotificationEventsByUuid(
		Collection<String> notificationEventUuids) {

		Set<String> notificationEventUuidsSet = new HashSet<String>(
			notificationEventUuids);

		_reentrantLock.lock();

		try {
			if (_notificationEvents == null) {
				return;
			}

			Iterator<NotificationEvent> itr = _notificationEvents.iterator();

			while (itr.hasNext()) {
				NotificationEvent notificationEvent = itr.next();

				if (notificationEventUuidsSet.contains(
						notificationEvent.getUuid())) {

					itr.remove();
				}
			}
		}
		finally {
			_reentrantLock.unlock();
		}
	}

	@Override
	public void sendNotificationEvent(NotificationEvent notificationEvent)
		throws ChannelException {

		_reentrantLock.lock();

		try {
			long currentTime = System.currentTimeMillis();

			if (PropsValues.USER_NOTIFICATION_EVENT_CONFIRMATION_ENABLED &&
				notificationEvent.isDeliveryRequired()) {

				UserNotificationEventLocalServiceUtil.addUserNotificationEvent(
					getUserId(), notificationEvent);
			}

			storeNotificationEvent(notificationEvent, currentTime);
		}
		catch (Exception e) {
			throw new ChannelException("Unable to send event", e);
		}
		finally {
			_reentrantLock.unlock();
		}

		notifyChannelListeners();
	}

	@Override
	public void storeNotificationEvent(
		NotificationEvent notificationEvent, long currentTime) {

		if (isExpired(notificationEvent, currentTime)) {
			return;
		}

		if (PropsValues.USER_NOTIFICATION_EVENT_CONFIRMATION_ENABLED &&
			notificationEvent.isDeliveryRequired()) {

			_unconfirmedNotificationEvents.put(
				notificationEvent.getUuid(), notificationEvent);
		}
		else {
			_notificationEvents.add(notificationEvent);

			if (_notificationEvents.size() >
					PropsValues.NOTIFICATIONS_MAX_EVENTS) {

				NotificationEvent firstNotificationEvent =
					_notificationEvents.first();

				_notificationEvents.remove(firstNotificationEvent);
			}
		}
	}

	@Override
	protected void doCleanUp() throws Exception {
		_reentrantLock.lock();

		try {
			long currentTime = System.currentTimeMillis();

			Iterator<NotificationEvent> itr1 = _notificationEvents.iterator();

			while (itr1.hasNext()) {
				NotificationEvent notificationEvent = itr1.next();

				if (isExpired(notificationEvent, currentTime)) {
					itr1.remove();
				}
			}

			List<String> invalidNotificationEventUuids = new ArrayList<String>(
				_unconfirmedNotificationEvents.size());

			Set<Map.Entry<String, NotificationEvent>>
				unconfirmedNotificationEventsSet =
					_unconfirmedNotificationEvents.entrySet();

			Iterator<Map.Entry<String, NotificationEvent>> itr2 =
				unconfirmedNotificationEventsSet.iterator();

			while (itr2.hasNext()) {
				Map.Entry<String, NotificationEvent> entry = itr2.next();

				NotificationEvent notificationEvent = entry.getValue();

				if (isExpired(notificationEvent, currentTime)) {
					invalidNotificationEventUuids.add(entry.getKey());

					itr2.remove();
				}
			}

			if (PropsValues.USER_NOTIFICATION_EVENT_CONFIRMATION_ENABLED &&
				!invalidNotificationEventUuids.isEmpty()) {

				UserNotificationEventLocalServiceUtil.
					deleteUserNotificationEvents(
						invalidNotificationEventUuids, getCompanyId());
			}
		}
		catch (Exception e) {
			throw new ChannelException(
				"Unable to clean up channel " + getUserId(), e);
		}
		finally {
			_reentrantLock.unlock();
		}
	}

	protected List<NotificationEvent> doGetNotificationEvents(boolean flush)
		throws Exception {

		long currentTime = System.currentTimeMillis();

		List<NotificationEvent> notificationEvents =
			new ArrayList<NotificationEvent>(
				_notificationEvents.size() +
					_unconfirmedNotificationEvents.size());

		for (NotificationEvent notificationEvent : _notificationEvents) {
			if (isExpired(notificationEvent, currentTime)) {
				break;
			}
			else {
				notificationEvents.add(notificationEvent);
			}
		}

		if (flush) {
			_notificationEvents.clear();
		}
		else if (_notificationEvents.size() != notificationEvents.size()) {
			_notificationEvents.retainAll(notificationEvents);
		}

		List<String> invalidNotificationEventUuids = new ArrayList<String>(
			_unconfirmedNotificationEvents.size());

		Set<Map.Entry<String, NotificationEvent>>
			unconfirmedNotificationEventsSet =
				_unconfirmedNotificationEvents.entrySet();

		Iterator<Map.Entry<String, NotificationEvent>> itr =
			unconfirmedNotificationEventsSet.iterator();

		while (itr.hasNext()) {
			Map.Entry<String, NotificationEvent> entry = itr.next();

			NotificationEvent notificationEvent = entry.getValue();

			if (isExpired(notificationEvent, currentTime) &&
				!notificationEvent.isArchived()) {

				invalidNotificationEventUuids.add(notificationEvent.getUuid());

				itr.remove();
			}
			else {
				notificationEvents.add(entry.getValue());
			}
		}

		if (PropsValues.USER_NOTIFICATION_EVENT_CONFIRMATION_ENABLED &&
			!invalidNotificationEventUuids.isEmpty()) {

			UserNotificationEventLocalServiceUtil.deleteUserNotificationEvents(
				invalidNotificationEventUuids, getCompanyId());
		}

		return notificationEvents;
	}

	protected void doInit() {
		if (!PropsValues.USER_NOTIFICATION_EVENT_CONFIRMATION_ENABLED) {
			return;
		}

		List<UserNotificationEvent> userNotificationEvents =
			UserNotificationEventLocalServiceUtil.
				getDeliveredUserNotificationEvents(getUserId(), false);

		List<String> invalidNotificationEventUuids = new ArrayList<String>(
			_unconfirmedNotificationEvents.size());

		long currentTime = System.currentTimeMillis();

		for (UserNotificationEvent persistedNotificationEvent :
				userNotificationEvents) {

			try {
				JSONObject payloadJSONObject = JSONFactoryUtil.createJSONObject(
					persistedNotificationEvent.getPayload());

				NotificationEvent notificationEvent =
					NotificationEventFactoryUtil.createNotificationEvent(
						persistedNotificationEvent.getTimestamp(),
						persistedNotificationEvent.getType(),
						payloadJSONObject);

				notificationEvent.setDeliveryRequired(
					persistedNotificationEvent.getDeliverBy());

				notificationEvent.setUuid(persistedNotificationEvent.getUuid());

				if (isExpired(notificationEvent, currentTime)) {
					invalidNotificationEventUuids.add(
						notificationEvent.getUuid());
				}
				else {
					_unconfirmedNotificationEvents.put(
						notificationEvent.getUuid(), notificationEvent);
				}
			}
			catch (JSONException jsone) {
				_log.error(jsone, jsone);

				invalidNotificationEventUuids.add(
					persistedNotificationEvent.getUuid());
			}
		}

		if (!invalidNotificationEventUuids.isEmpty()) {
			UserNotificationEventLocalServiceUtil.deleteUserNotificationEvents(
				invalidNotificationEventUuids, getCompanyId());
		}
	}

	protected boolean isExpired(
		NotificationEvent notificationEvent, long currentTime) {

		if ((notificationEvent.getDeliverBy() != 0) &&
			(notificationEvent.getDeliverBy() <= currentTime)) {

			return true;
		}
		else {
			return false;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(ChannelImpl.class);

	private static final Comparator<NotificationEvent> _comparator =
		new NotificationEventComparator();

	private TreeSet<NotificationEvent> _notificationEvents;
	private final ReentrantLock _reentrantLock = new ReentrantLock();
	private final Map<String, NotificationEvent> _unconfirmedNotificationEvents;

}
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

package com.liferay.analytics.java.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jeyvison Nascimento
 * @author Marcellus Tavares
 */
public class AnalyticsEventsMessageBuilderTest {

	@Test
	public void testCreateContext() {
		long expectedInstanceId = randomLong();
		String expectedLanguageId = randomString();
		String expectedURL = randomString();
		long expectedUserId = randomLong();

		AnalyticsEventsMessage.Context actualContext = createContext(
			expectedInstanceId, expectedLanguageId, expectedURL,
			expectedUserId);

		assertContext(
			expectedInstanceId, expectedLanguageId, expectedURL, expectedUserId,
			actualContext);
	}

	@Test
	public void testCreateEvent() {
		String expectedAdditionalInfo = randomString();
		String expectedEvent = randomString();

		Map<String, String> expectedProperties = new HashMap<>();

		expectedProperties.put(randomString(), randomString());
		expectedProperties.put(randomString(), randomString());
		expectedProperties.put(randomString(), randomString());

		List<AnalyticsEventsMessage.Referrer> expectedReferrers =
			new ArrayList<>();

		List<String> expectedReferrerEntityIds = Arrays.asList(
			randomString(), randomString(), randomString());

		String expectedReferrerEntityType = randomString();

		AnalyticsEventsMessage.Referrer expectedReferrer = createReferrer(
			expectedReferrerEntityIds, expectedReferrerEntityType);

		expectedReferrers.add(expectedReferrer);

		Date expectedTimestamp = new Date();

		AnalyticsEventsMessage.Event actualEvent = createEvent(
			expectedAdditionalInfo, expectedEvent, expectedProperties,
			expectedReferrers, expectedTimestamp);

		assertEvent(
			expectedAdditionalInfo, expectedEvent, expectedProperties,
			expectedReferrers, expectedTimestamp, actualEvent);
	}

	@Test
	public void testCreateMessage() {

		// Context

		AnalyticsEventsMessage.Context expectedContext = createContext(
			randomLong(), randomString(), randomString(), randomLong());

		// Events

		List<AnalyticsEventsMessage.Event> expectedEvents = new ArrayList<>();

		String expectedAdditionalInfo = randomString();
		String expectedEventName = randomString();

		Map<String, String> expectedProperties = new HashMap<>();

		expectedProperties.put(randomString(), randomString());

		List<AnalyticsEventsMessage.Referrer> expectedReferrers =
			new ArrayList<>();

		expectedReferrers.add(
			createReferrer(
				Arrays.asList(randomString(), randomString(), randomString()),
				randomString()));

		Date expectedTimestamp = new Date();

		expectedEvents.add(
			createEvent(
				expectedAdditionalInfo, expectedEventName, expectedProperties,
				expectedReferrers, expectedTimestamp));

		// Message

		String expectedAnalyticsKey = randomString();
		long expectedAnonymousUserId = randomLong();
		String expectedApplicationId = randomString();
		String expectedClientIP = randomString();
		String expectedMessageFormat = randomString();
		String expectedProtocolVersion = randomString();
		String expectedUserAgent = randomString();

		AnalyticsEventsMessage actualAnalyticsEventsMessage =
			createAnalyticsEventsMessage(
				expectedAnalyticsKey, expectedAnonymousUserId,
				expectedApplicationId, expectedClientIP, expectedContext,
				expectedEvents, expectedMessageFormat, expectedProtocolVersion,
				expectedUserAgent);

		Assert.assertEquals(
			expectedAnonymousUserId,
			actualAnalyticsEventsMessage.getAnonymousUserId());
		Assert.assertEquals(
			expectedAnalyticsKey,
			actualAnalyticsEventsMessage.getAnalyticsKey());
		Assert.assertEquals(
			expectedApplicationId,
			actualAnalyticsEventsMessage.getApplicationId());
		Assert.assertEquals(
			expectedClientIP, actualAnalyticsEventsMessage.getClientIP());

		assertContext(
			expectedContext.getInstanceId(), expectedContext.getLanguageId(),
			expectedContext.getURL(), expectedContext.getUserId(),
			actualAnalyticsEventsMessage.getContext());

		List<AnalyticsEventsMessage.Event> actualEvents =
			actualAnalyticsEventsMessage.getEvents();

		Assert.assertEquals(
			expectedEvents.toString(), expectedEvents.size(),
			actualEvents.size());

		int i = 0;

		for (AnalyticsEventsMessage.Event expectedEvent : expectedEvents) {
			assertEvent(
				expectedEvent.getAdditionalInfo(), expectedEvent.getEvent(),
				expectedEvent.getProperties(), expectedEvent.getReferrers(),
				expectedEvent.getTimestamp(), actualEvents.get(i++));
		}

		Assert.assertEquals(
			expectedMessageFormat,
			actualAnalyticsEventsMessage.getMessageFormat());
		Assert.assertEquals(
			expectedProtocolVersion,
			actualAnalyticsEventsMessage.getProtocolVersion());
		Assert.assertEquals(
			expectedUserAgent, actualAnalyticsEventsMessage.getUserAgent());
	}

	@Test
	public void testCreateReferrer() {
		List<String> expectedReferrerEntityIds = Arrays.asList(
			randomString(), randomString(), randomString());

		String expectedReferrerEntityType = randomString();

		AnalyticsEventsMessage.Referrer referrer = createReferrer(
			expectedReferrerEntityIds, expectedReferrerEntityType);

		assertReferrer(
			expectedReferrerEntityIds, expectedReferrerEntityType, referrer);
	}

	protected void assertContext(
		long expectedInstanceId, String expectedLanguageId, String expectedURL,
		long expectedUserId, AnalyticsEventsMessage.Context actualContext) {

		Assert.assertEquals(expectedInstanceId, actualContext.getInstanceId());
		Assert.assertEquals(expectedLanguageId, actualContext.getLanguageId());
		Assert.assertEquals(expectedURL, actualContext.getURL());
		Assert.assertEquals(expectedUserId, actualContext.getUserId());
	}

	protected void assertEvent(
		String expectedAdditionalInfo, String expectedEvent,
		Map<String, String> expectedProperties,
		List<AnalyticsEventsMessage.Referrer> expectedReferrers,
		Date expectedTimestamp, AnalyticsEventsMessage.Event actualEvent) {

		Assert.assertEquals(
			expectedAdditionalInfo, actualEvent.getAdditionalInfo());
		Assert.assertEquals(expectedEvent, actualEvent.getEvent());
		Assert.assertEquals(expectedProperties, actualEvent.getProperties());

		List<AnalyticsEventsMessage.Referrer> actualReferrers =
			actualEvent.getReferrers();

		Assert.assertEquals(
			expectedReferrers.toString(), expectedReferrers.size(),
			actualReferrers.size());

		int i = 0;

		for (AnalyticsEventsMessage.Referrer expectedReferrer :
				expectedReferrers) {

			assertReferrer(
				expectedReferrer.getReferrerEntityIds(),
				expectedReferrer.getReferrerEntityType(),
				actualReferrers.get(i++));
		}

		Assert.assertEquals(expectedTimestamp, actualEvent.getTimestamp());
	}

	protected void assertReferrer(
		List<String> expectedReferrerEntityIds,
		String expectedReferrerEntityType,
		AnalyticsEventsMessage.Referrer actualReferrer) {

		Assert.assertEquals(
			expectedReferrerEntityIds, actualReferrer.getReferrerEntityIds());
		Assert.assertEquals(
			expectedReferrerEntityType, actualReferrer.getReferrerEntityType());
	}

	protected AnalyticsEventsMessage createAnalyticsEventsMessage(
		String analyticsKey, long anonymousUserId, String applicationId,
		String clientIP, AnalyticsEventsMessage.Context context,
		List<AnalyticsEventsMessage.Event> events, String messageFormat,
		String protocolVersion, String userAgent) {

		AnalyticsEventsMessage.Builder messageBuilder =
			AnalyticsEventsMessage.builder();

		messageBuilder.analyticsKey(analyticsKey);
		messageBuilder.anonymousUserId(anonymousUserId);
		messageBuilder.applicationId(applicationId);
		messageBuilder.clientIP(clientIP);
		messageBuilder.context(context);

		for (AnalyticsEventsMessage.Event event : events) {
			messageBuilder.event(event);
		}

		messageBuilder.messageFormat(messageFormat);
		messageBuilder.protocolVersion(protocolVersion);
		messageBuilder.userAgent(userAgent);

		return messageBuilder.build();
	}

	protected AnalyticsEventsMessage.Context createContext(
		long instanceId, String languageId, String url, long userId) {

		AnalyticsEventsMessage.Context.Builder contextBuilder =
			AnalyticsEventsMessage.Context.builder();

		contextBuilder.instanceId(instanceId);
		contextBuilder.languageId(languageId);
		contextBuilder.url(url);
		contextBuilder.userId(userId);

		return contextBuilder.build();
	}

	protected AnalyticsEventsMessage.Event createEvent(
		String additionalInfo, String event, Map<String, String> properties,
		List<AnalyticsEventsMessage.Referrer> referrers, Date timestamp) {

		AnalyticsEventsMessage.Event.Builder eventBuilder =
			AnalyticsEventsMessage.Event.builder();

		eventBuilder.additionalInfo(additionalInfo);
		eventBuilder.event(event);

		for (Map.Entry<String, String> entry : properties.entrySet()) {
			eventBuilder.property(entry.getKey(), entry.getValue());
		}

		for (AnalyticsEventsMessage.Referrer referrer : referrers) {
			eventBuilder.referrer(referrer);
		}

		eventBuilder.timestamp(timestamp);

		return eventBuilder.build();
	}

	protected AnalyticsEventsMessage.Referrer createReferrer(
		List<String> referrerEntityIds, String referrerEntityType) {

		AnalyticsEventsMessage.Referrer.Builder referrerBuilder =
			AnalyticsEventsMessage.Referrer.builder();

		referrerBuilder.referrerEntityIds(referrerEntityIds);
		referrerBuilder.referrerEntityType(referrerEntityType);

		return referrerBuilder.build();
	}

	protected long randomLong() {
		return RandomUtils.nextLong();
	}

	protected String randomString() {
		return RandomStringUtils.random(5);
	}

}
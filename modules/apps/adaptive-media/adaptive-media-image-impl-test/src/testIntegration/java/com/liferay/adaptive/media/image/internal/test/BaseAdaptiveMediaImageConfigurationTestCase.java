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

package com.liferay.adaptive.media.image.internal.test;

import com.liferay.adaptive.media.image.configuration.AdaptiveMediaImageConfigurationEntry;
import com.liferay.adaptive.media.image.configuration.AdaptiveMediaImageConfigurationHelper;
import com.liferay.adaptive.media.image.messaging.AdaptiveMediaImageDestinationNames;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.registry.Filter;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceTracker;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author Alejandro Hernández
 */
public abstract class BaseAdaptiveMediaImageConfigurationTestCase {

	@BeforeClass
	public static void setUpClass() {
		Registry registry = RegistryUtil.getRegistry();

		Filter filter = registry.getFilter(
			"(objectClass=" +
				AdaptiveMediaImageConfigurationHelper.class.getName() + ")");

		serviceTracker = registry.trackServices(filter);

		serviceTracker.open();
	}

	@AfterClass
	public static void tearDownClass() {
		serviceTracker.close();
	}

	@Before
	public void setUp() throws Exception {
		deleteAllConfigurationEntries();
	}

	@After
	public void tearDown() throws Exception {
		deleteAllConfigurationEntries();
	}

	protected static void deleteAllConfigurationEntries()
		throws IOException, PortalException {

		AdaptiveMediaImageConfigurationHelper
			adaptiveMediaImageConfigurationHelper = serviceTracker.getService();

		Collection<AdaptiveMediaImageConfigurationEntry> configurationEntries =
			adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					TestPropsValues.getCompanyId(), configurationEntry -> true);

		for (AdaptiveMediaImageConfigurationEntry configurationEntry :
				configurationEntries) {

			adaptiveMediaImageConfigurationHelper.
				forceDeleteAdaptiveMediaImageConfigurationEntry(
					TestPropsValues.getCompanyId(),
					configurationEntry.getUUID());
		}
	}

	protected void assertDisabled(
		Optional<AdaptiveMediaImageConfigurationEntry>
			configurationEntryOptional) {

		Assert.assertTrue(configurationEntryOptional.isPresent());

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			configurationEntryOptional.get();

		Assert.assertFalse(configurationEntry.isEnabled());
	}

	protected void assertEnabled(
		Optional<AdaptiveMediaImageConfigurationEntry>
			configurationEntryOptional) {

		Assert.assertTrue(configurationEntryOptional.isPresent());

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			configurationEntryOptional.get();

		Assert.assertTrue(configurationEntry.isEnabled());
	}

	protected List<Message> collectConfigurationMessages(
			CheckedRunnable runnable)
		throws Exception {

		String destinationName =
			AdaptiveMediaImageDestinationNames.
				ADAPTIVE_MEDIA_IMAGE_CONFIGURATION;

		List<Message> messages = new ArrayList<>();

		MessageListener messageListener = messages::add;

		MessageBusUtil.registerMessageListener(
			destinationName, messageListener);

		try {
			runnable.run();
		}
		finally {
			MessageBusUtil.unregisterMessageListener(
				destinationName, messageListener);
		}

		return messages;
	}

	protected static ServiceTracker
		<AdaptiveMediaImageConfigurationHelper,
			AdaptiveMediaImageConfigurationHelper> serviceTracker;

	@FunctionalInterface
	protected interface CheckedRunnable {

		public void run() throws Exception;

	}

}
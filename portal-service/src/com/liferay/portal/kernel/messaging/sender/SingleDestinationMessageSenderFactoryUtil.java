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

package com.liferay.portal.kernel.messaging.sender;

import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceTracker;

/**
 * @author Michael C. Han
 */
public class SingleDestinationMessageSenderFactoryUtil {

	public static SingleDestinationMessageSender
		createSingleDestinationMessageSender(String destinationName) {

		return _instance.getSingleDestinationMessageSenderFactory().
			createSingleDestinationMessageSender(destinationName);
	}

	public static SingleDestinationSynchronousMessageSender
		createSingleDestinationSynchronousMessageSender(
			String destinationName, SynchronousMessageSender.Mode mode) {

		return _instance.getSingleDestinationMessageSenderFactory().
			createSingleDestinationSynchronousMessageSender(
				destinationName, mode);
	}

	protected SingleDestinationMessageSenderFactory
		getSingleDestinationMessageSenderFactory() {

		return _serviceTracker.getService();
	}

	private SingleDestinationMessageSenderFactoryUtil() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			SingleDestinationMessageSenderFactory.class);

		_serviceTracker.open();
	}

	private static final SingleDestinationMessageSenderFactoryUtil _instance =
		new SingleDestinationMessageSenderFactoryUtil();

	private final ServiceTracker
		<SingleDestinationMessageSenderFactory,
			SingleDestinationMessageSenderFactory> _serviceTracker;

}
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

package com.liferay.portal.kernel.portlet.bridges.mvc;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.registry.Filter;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

/**
 * @author Sergio González
 */
public class MVCResourceCommandCache {

	public static final MVCResourceCommand EMPTY = new MVCResourceCommand() {

		@Override
		public boolean serveResource(
			ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) {

			return false;
		}

	};

	public MVCResourceCommandCache(String packagePrefix, String portletName) {
		if (Validator.isNotNull(packagePrefix) &&
			!packagePrefix.endsWith(StringPool.PERIOD)) {

			packagePrefix = packagePrefix + StringPool.PERIOD;
		}

		_packagePrefix = packagePrefix;

		Registry registry = RegistryUtil.getRegistry();

		Filter filter = registry.getFilter(
			"(&(javax.portlet.name=" + portletName +
				")(mvc.command.name=*)(objectClass=" +
					MVCResourceCommand.class.getName() + "))");

		_serviceTracker = registry.trackServices(
			filter, new MVCResourceCommandServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	public void close() {
		_serviceTracker.close();
	}

	public MVCResourceCommand getMVCResourceCommand(
		String mvcResourceCommandName) {

		String className = null;

		try {
			MVCResourceCommand mvcResourceCommand =
				_mvcResourceCommandCache.get(mvcResourceCommandName);

			if (mvcResourceCommand != null) {
				return mvcResourceCommand;
			}

			if (Validator.isNull(_packagePrefix)) {
				return EMPTY;
			}

			StringBundler sb = new StringBundler(4);

			sb.append(_packagePrefix);
			sb.append(Character.toUpperCase(mvcResourceCommandName.charAt(0)));
			sb.append(mvcResourceCommandName.substring(1));
			sb.append("MVCResourceCommand");

			className = sb.toString();

			mvcResourceCommand =
				(MVCResourceCommand)InstanceFactory.newInstance(className);

			_mvcResourceCommandCache.put(
				mvcResourceCommandName, mvcResourceCommand);

			return mvcResourceCommand;
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to instantiate MVCResourceCommand " + className);
			}

			_mvcResourceCommandCache.put(mvcResourceCommandName, EMPTY);

			return EMPTY;
		}
	}

	public List<MVCResourceCommand> getMVCResourceCommandChain(
		String mvcResourceCommandChain) {

		List<MVCResourceCommand> mvcResourceCommands =
			_mvcResourceCommandChainCache.get(mvcResourceCommandChain);

		if (mvcResourceCommands != null) {
			return mvcResourceCommands;
		}

		mvcResourceCommands = new ArrayList<>();

		String[] mvcResourceCommandNames = StringUtil.split(
			mvcResourceCommandChain);

		for (String mvcResourceCommandName : mvcResourceCommandNames) {
			MVCResourceCommand mvcResourceCommand = getMVCResourceCommand(
				mvcResourceCommandName);

			if (mvcResourceCommand != EMPTY) {
				mvcResourceCommands.add(mvcResourceCommand);
			}
			else {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to find MVCResourceCommand " +
							mvcResourceCommandChain);
				}
			}
		}

		_mvcResourceCommandChainCache.put(
			mvcResourceCommandChain, mvcResourceCommands);

		return mvcResourceCommands;
	}

	public boolean isEmpty() {
		return _mvcResourceCommandCache.isEmpty();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MVCResourceCommandCache.class);

	private final Map<String, MVCResourceCommand> _mvcResourceCommandCache =
		new ConcurrentHashMap<>();
	private final Map<String, List<MVCResourceCommand>>
		_mvcResourceCommandChainCache = new ConcurrentHashMap<>();
	private final String _packagePrefix;
	private final ServiceTracker<MVCResourceCommand, MVCResourceCommand>
		_serviceTracker;

	private class MVCResourceCommandServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<MVCResourceCommand, MVCResourceCommand> {

		@Override
		public MVCResourceCommand addingService(
			ServiceReference<MVCResourceCommand> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			MVCResourceCommand mvcResourceCommand = registry.getService(
				serviceReference);

			String commandName = (String)serviceReference.getProperty(
				"mvc.command.name");

			_mvcResourceCommandCache.put(commandName, mvcResourceCommand);

			return mvcResourceCommand;
		}

		@Override
		public void modifiedService(
			ServiceReference<MVCResourceCommand> serviceReference,
			MVCResourceCommand mvcResourceCommand) {
		}

		@Override
		public void removedService(
			ServiceReference<MVCResourceCommand> serviceReference,
			MVCResourceCommand mvcResourceCommand) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			String commandName = (String)serviceReference.getProperty(
				"mvc.command.name");

			_mvcResourceCommandCache.remove(commandName);

			for (List<MVCResourceCommand> mvcResourceCommands :
					_mvcResourceCommandChainCache.values()) {

				mvcResourceCommands.remove(mvcResourceCommand);
			}
		}

	}

}
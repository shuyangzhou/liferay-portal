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

package com.liferay.portal.osgi.debug.internal.osgi.commands;

import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dependency.manager.DependencyManagerSyncUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.osgi.debug.SystemChecker;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.FutureTask;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Tina Tian
 */
@Component(
	property = {"osgi.command.function=check", "osgi.command.scope=system"},
	service = {}
)
public class SystemCheckOSGiCommands {

	public void check() {
		_check(true);
	}

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_bundleContext = bundleContext;

		if (_log.isInfoEnabled()) {
			_log.info(
				"System check is enabled. You can run a system check with " +
					"the command \"system:check\" in Gogo shell.");
		}

		boolean checkEnabled = GetterUtil.getBoolean(
			_props.get(PropsKeys.INITIAL_SYSTEM_CHECK_ENABLED), true);

		if (checkEnabled) {
			_futureTask = new FutureTask<>(
				() -> {
					DependencyManagerSyncUtil.sync();

					if (_log.isInfoEnabled()) {
						_log.info("Running system check");
					}

					_check(false);
				},
				null);

			Thread systemCheckerThread = new Thread(
				_futureTask,
				SystemCheckOSGiCommands.class.getName() + "-SystemChecker");

			systemCheckerThread.setDaemon(true);

			systemCheckerThread.start();
		}

		Dictionary<String, Object> osgiCommandProperties =
			new HashMapDictionary<>();

		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			String key = entry.getKey();

			if (key.startsWith("osgi.command.")) {
				osgiCommandProperties.put(key, entry.getValue());
			}
		}

		_serviceRegistration = bundleContext.registerService(
			SystemCheckOSGiCommands.class, this, osgiCommandProperties);
	}

	@Deactivate
	protected void deactivate() {
		_serviceRegistration.unregister();

		if (_futureTask != null) {
			_futureTask.cancel(true);
		}

		_serviceTrackerDCLSingleton.destroy(ServiceTracker::close);
	}

	private void _check(boolean useSystemOut) {
		ServiceTracker<SystemChecker, SystemChecker> serviceTracker =
			_serviceTrackerDCLSingleton.getSingleton(
				() -> {
					ServiceTracker<SystemChecker, SystemChecker>
						newServiceTracker = new ServiceTracker<>(
							_bundleContext, SystemChecker.class, null);

					newServiceTracker.open();

					return newServiceTracker;
				});

		Map<ServiceReference<SystemChecker>, SystemChecker> systemCheckerMap =
			serviceTracker.getTracked();

		Collection<SystemChecker> systemCheckers = systemCheckerMap.values();

		if (useSystemOut) {
			System.out.println("Available checkers: " + systemCheckers);
		}
		else if (_log.isInfoEnabled()) {
			_log.info("Available checkers :" + systemCheckers);
		}

		for (SystemChecker systemChecker : systemCheckers) {
			StringBundler sb = new StringBundler(5);

			sb.append("Running \"");
			sb.append(systemChecker.getName());
			sb.append("\". You can run this by itself with command \"");
			sb.append(systemChecker.getOSGiCommand());
			sb.append("\" in gogo shell.");

			if (useSystemOut) {
				System.out.println(sb.toString());
			}
			else if (_log.isInfoEnabled()) {
				_log.info(sb.toString());
			}

			String result = systemChecker.check();

			if (Validator.isNull(result)) {
				if (useSystemOut) {
					System.out.println(
						systemChecker.getName() +
							" check result: No issues were found.");
				}
				else if (_log.isInfoEnabled()) {
					_log.info(
						systemChecker.getName() +
							" check result: No issues were found.");
				}
			}
			else {
				if (useSystemOut) {
					System.out.println(
						systemChecker.getName() + " check result: " + result);
				}
				else if (_log.isWarnEnabled()) {
					_log.warn(
						systemChecker.getName() + " check result: " + result);
				}
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SystemCheckOSGiCommands.class);

	private BundleContext _bundleContext;
	private FutureTask<?> _futureTask;

	@Reference(target = ModuleServiceLifecycle.SYSTEM_CHECK)
	private ModuleServiceLifecycle _moduleServiceLifecycle;

	@Reference
	private Props _props;

	private ServiceRegistration<?> _serviceRegistration;
	private final DCLSingleton<ServiceTracker<SystemChecker, SystemChecker>>
		_serviceTrackerDCLSingleton = new DCLSingleton<>();

}
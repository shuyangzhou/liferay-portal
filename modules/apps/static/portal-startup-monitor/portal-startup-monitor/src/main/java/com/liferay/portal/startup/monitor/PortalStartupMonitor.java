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

package com.liferay.portal.startup.monitor;

import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.ThreadUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Matthew Tambara
 */
@Component(immediate = true)
public class PortalStartupMonitor {

	@Activate
	protected void activate(BundleContext bundleContext) {
		_futureTask = new FutureTask<>(

			new Callable<Void>() {

				@Override
				public Void call() throws InterruptedException {
					StringBundler sb = new StringBundler(5);

					while (true) {
						Thread.sleep(_SLEEP);

						sb.append("Thread dump for portal startup  ");
						sb.append("after waiting ");
						sb.append(_SLEEP);
						sb.append("ms:");
						sb.append(ThreadUtil.threadDump());

						System.out.println(sb.toString());

						sb.setIndex(0);
					}
				}

			});

		_serviceTracker = new ServiceTracker<>(
			bundleContext, ModuleServiceLifecycle.class,
			new ServiceTrackerCustomizer<ModuleServiceLifecycle,
				ModuleServiceLifecycle>() {

				@Override
				public ModuleServiceLifecycle addingService(
					ServiceReference<ModuleServiceLifecycle> serviceReference) {

					String property = String.valueOf(
						serviceReference.getProperty(
							"module.service.lifecycle"));

					if (property.equals("portal.initialized")) {
						deactivate();
					}

					return bundleContext.getService(serviceReference);
				}

				@Override
				public void modifiedService(
					ServiceReference<ModuleServiceLifecycle> serviceReference,
					ModuleServiceLifecycle service) {
				}

				@Override
				public void removedService(
					ServiceReference<ModuleServiceLifecycle> serviceReference,
					ModuleServiceLifecycle service) {
				}

			});

		_serviceTracker.open();

		_thread = new Thread(_futureTask, "Portal Startup Monitoring Thread");

		_thread.start();
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();

		if (!_futureTask.isDone()) {
			try {
				_futureTask.cancel(true);

				_thread.join();
			}
			catch (InterruptedException ie) {
				throw new RuntimeException(ie);
			}
		}
	}

	private static final long _SLEEP = 600000;

	private FutureTask<Void> _futureTask;
	private ServiceTracker<ModuleServiceLifecycle, ModuleServiceLifecycle>
		_serviceTracker;
	private Thread _thread;

}
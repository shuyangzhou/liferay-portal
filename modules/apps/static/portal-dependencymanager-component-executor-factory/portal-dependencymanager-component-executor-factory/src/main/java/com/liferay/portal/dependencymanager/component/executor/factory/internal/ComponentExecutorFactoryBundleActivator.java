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

package com.liferay.portal.dependencymanager.component.executor.factory.internal;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.NamedThreadFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.felix.dm.Component;
import org.apache.felix.dm.ComponentExecutorFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Shuyang Zhou
 */
public class ComponentExecutorFactoryBundleActivator
	implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) {
		Runtime runtime = Runtime.getRuntime();

		int threadPoolSize = GetterUtil.getInteger(
			bundleContext.getProperty("dependencymanager.threadpool.size"),
			runtime.availableProcessors());

		_serviceRegistration = bundleContext.registerService(
			ComponentExecutorFactory.class,
			new ComponentExecutorFactoryImpl(
				new ThreadPoolExecutor(
					0, threadPoolSize, 0, TimeUnit.MILLISECONDS,
					new LinkedBlockingDeque<>(),
					new NamedThreadFactory(
						"Portal Dependencymanager Component Executor-",
						Thread.NORM_PRIORITY,
						ComponentExecutorFactory.class.getClassLoader()))),
			null);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		_serviceRegistration.unregister();
	}

	private ServiceRegistration<ComponentExecutorFactory> _serviceRegistration;

	private static class ComponentExecutorFactoryImpl
		implements ComponentExecutorFactory {

		@Override
		public Executor getExecutorFor(Component component) {
			return _executor;
		}

		private ComponentExecutorFactoryImpl(Executor executor) {
			_executor = executor;
		}

		private final Executor _executor;

	}

}
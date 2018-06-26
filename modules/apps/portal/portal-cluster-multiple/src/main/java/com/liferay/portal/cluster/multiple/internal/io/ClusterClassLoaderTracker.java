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

package com.liferay.portal.cluster.multiple.internal.io;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.util.tracker.BundleTracker;

/**
 * @author Lance Ji
 */
public class ClusterClassLoaderTracker implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		_bundleTracker = new BundleTracker<ClassLoader>(
			bundleContext, Bundle.ACTIVE, null) {

			@Override
			public ClassLoader addingBundle(
				Bundle bundle, BundleEvent bundleEvent) {

				BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

				ClassLoader classLoader = bundleWiring.getClassLoader();

				ClusterClassLoaderPool.register(
					bundle.getSymbolicName(), bundle.getVersion(), classLoader);

				return classLoader;
			}

			@Override
			public void removedBundle(
				Bundle bundle, BundleEvent bundleEvent,
				ClassLoader classLoader) {

				ClusterClassLoaderPool.unregister(classLoader);
			}

		};

		_bundleTracker.open();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		_bundleTracker.close();
	}

	private BundleTracker<ClassLoader> _bundleTracker;

}
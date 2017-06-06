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

package com.liferay.portal.classloader.tracker.internal;

import com.liferay.portal.kernel.util.ClassLoaderPool;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.BundleTracker;

/**
 * @author Shuyang Zhou
 */
@Component(immediate = true, service = ClassLoaderTracker.class)
public class ClassLoaderTracker {

	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleTracker = new BundleTracker<Void>(
			bundleContext, Bundle.ACTIVE, null) {

			@Override
			public Void addingBundle(Bundle bundle, BundleEvent bundleEvent) {
				BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

				ClassLoaderPool.register(
					bundle.getSymbolicName(), bundleWiring.getClassLoader());

				return null;
			}

			@Override
			public void removedBundle(
				Bundle bundle, BundleEvent event, Void object) {

				ClassLoaderPool.unregister(bundle.getSymbolicName());
			}

		};

		_bundleTracker.open();
	}

	@Deactivate
	public void deactivate() {
		_bundleTracker.close();
	}

	private BundleTracker<Void> _bundleTracker;

}
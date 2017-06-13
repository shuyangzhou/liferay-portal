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
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.BundleTracker;

/**
 * @author Tina Tian
 */
@Component(immediate = true)
public class ClassLoaderTracker {

	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleTracker = new BundleTracker<ClassLoader>(
			bundleContext, Bundle.ACTIVE, null) {

			@Override
			public ClassLoader addingBundle(
				Bundle bundle, BundleEvent bundleEvent) {

				BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

				ClassLoader classLoader = bundleWiring.getClassLoader();

				StringBundler sb = new StringBundler(3);

				sb.append(bundle.getSymbolicName());
				sb.append(StringPool.UNDERLINE);
				sb.append(bundle.getVersion());

				ClassLoaderPool.register(sb.toString(), classLoader);

				return classLoader;
			}

			@Override
			public void removedBundle(
				Bundle bundle, BundleEvent bundleEvent,
				ClassLoader classLoader) {

				ClassLoaderPool.unregister(classLoader);
			}

		};

		_bundleTracker.open();
	}

	@Deactivate
	public void deactivate() {
		_bundleTracker.close();
	}

	private BundleTracker<ClassLoader> _bundleTracker;

}
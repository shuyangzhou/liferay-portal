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

package com.liferay.portal.social.activity.extender.internal;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.social.kernel.util.SocialConfigurationUtil;

import java.net.URL;

import java.util.Enumeration;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.util.tracker.BundleTracker;

/**
 * @author Shuyang Zhou
 */
public class SocialActivityExtender implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		_tracker = new BundleTracker<Void>(
			bundleContext, Bundle.STARTING, null) {

			@Override
			public Void addingBundle(Bundle bundle, BundleEvent bundleEvent) {
				try {
					_readSocialActivity(
						bundle, "META-INF/social/liferay-social.xml");
					_readSocialActivity(
						bundle, "META-INF/social/liferay-social-ext.xml");
				}
				catch (Exception e) {
					_log.error(
						"Unable to read social activity for bundle " +
							bundle.getSymbolicName(),
						e);
				}

				return null;
			}

		};

		_tracker.open();
	}

	@Override
	public void stop(BundleContext context) {
		_tracker.close();
	}

	private void _readSocialActivity(Bundle bundle, String resourcePath)
		throws Exception {

		Enumeration<URL> enumeration = bundle.getResources(resourcePath);

		if (enumeration == null) {
			return;
		}

		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

		ClassLoader classLoader = bundleWiring.getClassLoader();

		while (enumeration.hasMoreElements()) {
			SocialConfigurationUtil.read(
				classLoader,
				new String[] {
					StringUtil.read(
						classLoader.getResourceAsStream(resourcePath))
				});
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SocialActivityExtender.class);

	private volatile BundleTracker<Void> _tracker;

}
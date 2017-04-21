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

package com.liferay.portal.osgi.web.servlet.jsp.compiler.internal;

import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.util.PropsValues;

import java.io.File;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

/**
 * @author Matthew Tambara
 */
@Component(immediate = true)
public class JspReloader {

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleTracker = new BundleTracker<>(
			bundleContext, -1,
			new BundleTrackerCustomizer<Bundle>() {

				@Override
				public Bundle addingBundle(
					Bundle bundle, BundleEvent bundleEvent) {

					return bundle;
				}

				@Override
				public void modifiedBundle(
					Bundle bundle, BundleEvent bundleEvent, Bundle oldBundle) {

					int event = bundleEvent.getType();

					if ((event == BundleEvent.UPDATED) ||
						(event == BundleEvent.UNINSTALLED)) {

						File file = new File(
							_WORK_DIR,
							bundle.getSymbolicName() + StringPool.DASH +
								bundle.getVersion());

						if (file.exists()) {
							FileUtil.deltree(file);
						}
					}
				}

				@Override
				public void removedBundle(
					Bundle bundle, BundleEvent bundleEvent, Bundle oldBundle) {
				}

			});

		_bundleTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_bundleTracker.close();
	}

	private static final String _WORK_DIR =
		PropsValues.LIFERAY_HOME + File.separator + "work" + File.separator;

	private BundleTracker<Bundle> _bundleTracker;

}
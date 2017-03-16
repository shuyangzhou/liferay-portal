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

import java.nio.file.Path;
import java.nio.file.Paths;

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
		_bundleTracker = new BundleTracker(
			bundleContext, Bundle.ACTIVE,
			new BundleTrackerCustomizer<Void>() {

				@Override
				public Void addingBundle(Bundle bundle, BundleEvent event) {
					long bundleId = bundle.getBundleId();

					if (bundleId == 0) {
						return null;
					}

					if (bundle.findEntries("/", "*.jsp", true) == null) {
						return null;
					}

					long lastModified = 0;

					for (String fileString : FileUtil.find(
							_BUNDLE_FILE_PATH.concat(String.valueOf(bundleId)),
							"**", null)) {

						File file = new File(fileString);

						lastModified = file.lastModified();
					}

					Path folderPath = Paths.get(
						_WORK_DIR,
						bundle.getSymbolicName() + StringPool.DASH +
							bundle.getVersion());

					File file = new File(folderPath.toString());

					if (file.exists() && (file.lastModified() < lastModified)) {
						FileUtil.deltree(file);
					}

					return null;
				}

				@Override
				public void modifiedBundle(
					Bundle bundle, BundleEvent event, Void object) {

					return;
				}

				@Override
				public void removedBundle(
					Bundle bundle, BundleEvent event, Void object) {

					return;
				}

			});

		_bundleTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_bundleTracker.close();
	}

	private static final String _BUNDLE_FILE_PATH =
		PropsValues.MODULE_FRAMEWORK_STATE_DIR.concat("/org.eclipse.osgi/");

	private static final String _WORK_DIR = PropsValues.LIFERAY_HOME.concat(
		"/work");

	private BundleTracker<Void> _bundleTracker;

}
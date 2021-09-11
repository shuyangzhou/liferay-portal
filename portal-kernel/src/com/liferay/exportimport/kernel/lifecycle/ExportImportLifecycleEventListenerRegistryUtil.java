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

package com.liferay.exportimport.kernel.lifecycle;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.osgi.framework.BundleContext;

/**
 * @author Daniel Kocsis
 */
public class ExportImportLifecycleEventListenerRegistryUtil {

	public static Set<ExportImportLifecycleListener>
		getAsyncExportImportLifecycleListeners() {

		return new HashSet<>(
			_exportImportLifecycleListeners.getService(Boolean.TRUE));
	}

	public static Set<ExportImportLifecycleListener>
		getSyncExportImportLifecycleListeners() {

		return new HashSet<>(
			_exportImportLifecycleListeners.getService(Boolean.FALSE));
	}

	private ExportImportLifecycleEventListenerRegistryUtil() {
	}

	private static final BundleContext _bundleContext =
		SystemBundleUtil.getBundleContext();

	private static final ServiceTrackerMap
		<Boolean, List<ExportImportLifecycleListener>>
			_exportImportLifecycleListeners =
				ServiceTrackerMapFactory.openMultiValueMap(
					_bundleContext, ExportImportLifecycleListener.class, null,
					(serviceReference, emitter) -> {
						ExportImportLifecycleListener
							exportImportLifecycleListener =
								_bundleContext.getService(serviceReference);

						if (exportImportLifecycleListener instanceof
								ProcessAwareExportImportLifecycleListener) {

							exportImportLifecycleListener =
								ExportImportLifecycleListenerFactoryUtil.create(
									(ProcessAwareExportImportLifecycleListener)
										exportImportLifecycleListener);
						}
						else if (exportImportLifecycleListener instanceof
									EventAwareExportImportLifecycleListener) {

							exportImportLifecycleListener =
								ExportImportLifecycleListenerFactoryUtil.create(
									(EventAwareExportImportLifecycleListener)
										exportImportLifecycleListener);
						}

						emitter.emit(
							exportImportLifecycleListener.isParallel());
					});

}
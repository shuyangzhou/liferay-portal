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

package com.liferay.exportimport.kernel.controller;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.registry.util.StringPlus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Kocsis
 */
public class ExportImportControllerRegistryUtil {

	public static ExportController getExportController(String className) {
		ExportImportController exportImportController =
			_exportImportControllers.getService(className);

		if (exportImportController instanceof ExportController) {
			return (ExportController)exportImportController;
		}

		return null;
	}

	public static List<ExportImportController> getExportImportControllers() {
		return new ArrayList<>(_exportImportControllers.values());
	}

	public static ImportController getImportController(String className) {
		ExportImportController exportImportController =
			_exportImportControllers.getService(className);

		if (exportImportController instanceof ImportController) {
			return (ImportController)exportImportController;
		}

		return null;
	}

	private ExportImportControllerRegistryUtil() {
	}

	private static final ServiceTrackerMap<String, ExportImportController>
		_exportImportControllers = ServiceTrackerMapFactory.openSingleValueMap(
			SystemBundleUtil.getBundleContext(), ExportImportController.class,
			null,
			(serviceReference, emitter) -> {
				for (String modelClassName :
						StringPlus.asList(
							serviceReference.getProperty("model.class.name"))) {

					emitter.emit(modelClassName);
				}
			});

}
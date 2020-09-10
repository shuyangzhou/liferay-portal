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

package com.liferay.dispatch.internal.type;

import com.liferay.dispatch.task.DispatchTask;
import com.liferay.dispatch.task.type.DispatchTaskTypeRegistry;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerCustomizerFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerCustomizerFactory.ServiceWrapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Matija Petanjek
 */
@Component(immediate = true, service = DispatchTaskTypeRegistry.class)
public class DispatchTaskTypeRegistryImpl implements DispatchTaskTypeRegistry {

	@Override
	public List<String> getDispatchTaskTypes() {
		return new ArrayList<>(_dispatchTaskServiceTrackerMap.keySet());
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_dispatchTaskServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, DispatchTask.class, "dispatch.task.type",
				ServiceTrackerCustomizerFactory.<DispatchTask>serviceWrapper(
					bundleContext));
	}

	@Deactivate
	protected void deactivate() {
		_dispatchTaskServiceTrackerMap.close();
	}

	private ServiceTrackerMap<String, ServiceWrapper<DispatchTask>>
		_dispatchTaskServiceTrackerMap;

}
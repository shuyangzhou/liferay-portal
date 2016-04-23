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

package com.liferay.portal.cluster.single.activator;

import com.liferay.portal.cluster.single.SingleClusterFilter;
import com.liferay.portal.kernel.util.ReleaseInfo;

import org.apache.felix.dm.Component;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;

import org.osgi.framework.BundleContext;

/**
 * @author Tina Tian
 */
public class SingleClusterActivator extends DependencyActivatorBase {

	@Override
	public void init(
			BundleContext bundleContext, DependencyManager dependencyManager)
		throws Exception {

		String releaseName = ReleaseInfo.getName();

		if (!releaseName.contains("Community")) {
			return;
		}

		Component component = createComponent();

		component.setInterface(SingleClusterFilter.class.getName(), null);
		component.setImplementation(new SingleClusterFilter());

		dependencyManager.add(component);
	}

}
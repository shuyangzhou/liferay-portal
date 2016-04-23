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

package com.liferay.portal.cache.single.internal.bootstrap;

import com.liferay.portal.cache.PortalCacheBootstrapLoader;
import com.liferay.portal.cache.PortalCacheBootstrapLoaderFactory;
import com.liferay.portal.cluster.single.SingleClusterFilter;

import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tina Tian
 */
@Component(immediate = true, service = PortalCacheBootstrapLoaderFactory.class)
public class SinglePortalCacheBootstrapLoaderFactory
	implements PortalCacheBootstrapLoaderFactory {

	@Override
	public PortalCacheBootstrapLoader create(Properties properties) {
		return null;
	}

	@Reference(unbind = "-")
	protected void setSingleClusterFilter(
		SingleClusterFilter singleClusterFilter) {
	}

}
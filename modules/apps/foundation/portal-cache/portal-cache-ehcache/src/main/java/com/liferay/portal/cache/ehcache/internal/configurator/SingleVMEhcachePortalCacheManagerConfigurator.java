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

package com.liferay.portal.cache.ehcache.internal.configurator;

import com.liferay.portal.cache.ehcache.configuration.PortalCacheEhcacheConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Dante Wang
 */
@Component(
	configurationPid = "com.liferay.portal.cache.ehcache.configuration.PortalCacheEhcacheConfiguration",
	immediate = true,
	service = SingleVMEhcachePortalCacheManagerConfigurator.class
)
public class SingleVMEhcachePortalCacheManagerConfigurator
	extends BaseEhcachePortalCacheManagerConfigurator {

	@Activate
	protected void activate(ComponentContext componentContext) {
		PortalCacheEhcacheConfiguration portalCacheEhcacheConfiguration =
			ConfigurableUtil.createConfigurable(
				PortalCacheEhcacheConfiguration.class,
				componentContext.getProperties());

		defaultDebugEnabled =
			portalCacheEhcacheConfiguration.defaultDebugEnabled();

		debugEnabledPortalCacheNames = SetUtil.fromArray(
			StringUtil.split(
				portalCacheEhcacheConfiguration.debugEnabledPortalCacheNames(),
				StringPool.COMMA));
	}

	@Reference(unbind = "-")
	protected void setProps(Props props) {
		this.props = props;
	}

}
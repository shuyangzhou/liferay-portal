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

package com.liferay.portal.cache.multiple.internal;

import com.liferay.portal.kernel.cache.PortalCacheManager;

import java.io.Serializable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Tina Tian
 */
@Component(immediate = true, service = {})
public class PortalCacheManagerUtil {

	public static PortalCacheManager<? extends Serializable, ?>
		getPortalCacheManager(String portalCacheManagerName) {

		return _portalCacheManagers.get(portalCacheManagerName);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void addPortalCacheManager(
		PortalCacheManager<? extends Serializable, ?> portalCacheManager) {

		_portalCacheManagers.put(
			portalCacheManager.getPortalCacheManagerName(), portalCacheManager);
	}

	protected void removePortalCacheManager(
		PortalCacheManager<? extends Serializable, ?> portalCacheManager) {

		_portalCacheManagers.remove(
			portalCacheManager.getPortalCacheManagerName());
	}

	private static final Map
		<String, PortalCacheManager<? extends Serializable, ?>>
			_portalCacheManagers = new ConcurrentHashMap<>();

}
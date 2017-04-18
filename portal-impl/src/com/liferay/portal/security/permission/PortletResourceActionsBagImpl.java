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

package com.liferay.portal.security.permission;

import com.liferay.portal.kernel.security.permission.PortletResourceActionsBag;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author László Csontos
 */
public class PortletResourceActionsBagImpl
	extends ResourceActionsBagImpl implements PortletResourceActionsBag {

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             #PortletResourceActionsBagImpl(String)}
	 */
	@Deprecated
	public PortletResourceActionsBagImpl() {
		_portletName = null;
	}

	public PortletResourceActionsBagImpl(
		PortletResourceActionsBag portletResourceActionsBag) {

		super(portletResourceActionsBag);

		_portletName = portletResourceActionsBag.getPortletName();
		_portletRootModelResource =
			portletResourceActionsBag.getPortletRootModelResource();
		_resourceLayoutManagerActions.addAll(
			portletResourceActionsBag.getResourceLayoutManagerActions());
	}

	public PortletResourceActionsBagImpl(String portletName) {
		_portletName = portletName;
	}

	@Override
	public PortletResourceActionsBag clone() {
		return new PortletResourceActionsBagImpl(this);
	}

	@Override
	public String getPortletName() {
		return _portletName;
	}

	@Override
	public String getPortletRootModelResource() {
		return _portletRootModelResource;
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             #getPortletRootModelResource()}
	 */
	@Deprecated
	@Override
	public Map<String, String> getPortletRootModelResources() {
		return Collections.singletonMap(
			_portletName, _portletRootModelResource);
	}

	@Override
	public Set<String> getResourceLayoutManagerActions() {
		return _resourceLayoutManagerActions;
	}

	@Override
	public void setPortletRootModelResource(String portletRootModelResource) {
		_portletRootModelResource = portletRootModelResource;
	}

	private final String _portletName;
	private String _portletRootModelResource;
	private final Set<String> _resourceLayoutManagerActions = new HashSet<>();

}
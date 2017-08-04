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

package com.liferay.portlet.tck.bridge.setup;

/**
 * @author Vernon Singleton
 */
public class Portlet {

	public Portlet(String portletName, String bundleName) {
		this(portletName, bundleName, true);
	}

	public Portlet(
		String portletName, String bundleName, ArtifactType artifactType) {

		this(portletName, bundleName, true, artifactType);
	}

	public Portlet(
		String portletName, String bundleName, boolean instanceable) {

		this(portletName, bundleName, instanceable, ArtifactType.WAR);
	}

	public Portlet(
		String portletName, String bundleName, boolean instanceable,
		ArtifactType artifactType) {

		_portletName = portletName;
		_bundleName = bundleName;
		_instanceable = instanceable;
		_artifactType = artifactType;
	}

	public ArtifactType getArtifactType() {
		return _artifactType;
	}

	public String getBundleName() {
		return _bundleName;
	}

	public String getInstanceToken() {
		if (_instanceable) {
			return "_INSTANCE_ABCD";
		}
		else {
			return "";
		}
	}

	public String getPortletName() {
		return _portletName;
	}

	public enum ArtifactType {

		WAR, WAB

	}

	private final ArtifactType _artifactType;
	private final String _bundleName;
	private final boolean _instanceable;
	private final String _portletName;

}
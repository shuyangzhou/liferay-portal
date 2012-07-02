/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.servlet.taglib.portletext;

import com.liferay.portal.kernel.util.Mergeable;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Julio Camarero
 */
public class UniqueElementIDs implements Mergeable<UniqueElementIDs> {

	public void addUniqueElemntID(String runtimePortletID) {
		_uniqueElementIDs.add(runtimePortletID);
	}

	public Set<String> getUniqueElementIDs() {
		return _uniqueElementIDs;
	}

	public UniqueElementIDs merge(UniqueElementIDs runtimePortletIDs) {
		if ((runtimePortletIDs != null) && (runtimePortletIDs != this)) {
			_uniqueElementIDs.addAll(runtimePortletIDs._uniqueElementIDs);
		}

		return this;
	}

	private Set<String> _uniqueElementIDs = new HashSet<String>();

}
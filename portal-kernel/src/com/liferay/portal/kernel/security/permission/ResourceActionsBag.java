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

package com.liferay.portal.kernel.security.permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Dante Wang
 */
public class ResourceActionsBag {

	public ResourceActionsBag(
		Set<String> resourceActions, Set<String> groupDefaultActions,
		Set<String> guestDefaultActions, Set<String> guestUnsupportedActions,
		Set<String> layoutManagerActions, Set<String> ownerDefaultActions) {

		_supportsActions = resourceActions;
		_groupDefaultActions = groupDefaultActions;
		_guestDefaultActions = guestDefaultActions;
		_guestUnsupportedActions = guestUnsupportedActions;
		_layoutManagerActions = layoutManagerActions;
		_ownerDefaultActions = ownerDefaultActions;
	}

	public List<String> getGroupDefaultActions() {
		return new ArrayList<>(_groupDefaultActions);
	}

	public List<String> getGuestDefaultActions() {
		return new ArrayList<>(_guestDefaultActions);
	}

	public List<String> getGuestUnsupportedActions() {
		return new ArrayList<>(_guestUnsupportedActions);
	}

	public List<String> getLayoutManagerActions() {
		return new ArrayList<>(_layoutManagerActions);
	}

	public List<String> getOwnerDefaultActions() {
		return new ArrayList<>(_ownerDefaultActions);
	}

	public List<String> getSupportsActions() {
		return new ArrayList<>(_supportsActions);
	}

	private final Set<String> _groupDefaultActions;
	private final Set<String> _guestDefaultActions;
	private final Set<String> _guestUnsupportedActions;
	private final Set<String> _layoutManagerActions;
	private final Set<String> _ownerDefaultActions;
	private final Set<String> _supportsActions;

}
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

package com.liferay.portal.model;

import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Group;

/**
 * @author Andrew Betts
 */
public abstract class LiveGroupIdModelListener<T extends BaseModel<T>>
	extends BaseModelListener<T>  {

	protected void validateGroup(Group group) {
		if (group != null && group.isStagingGroup()) {
			throw new StagingGroupMembershipException(group);
		}
	}

	public static class StagingGroupMembershipException
		extends ModelListenerException {

		public StagingGroupMembershipException(Group group) {
			super(
				"Associations cannot be made to Staging group" +
					group.getGroupId() + ". Use the liveGroup " +
						group.getLiveGroupId());
		}
	}

}

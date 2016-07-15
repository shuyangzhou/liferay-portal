/*
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
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.service.persistence.GroupUtil;

/**
 * @author Andrew Betts
 */
public class StagingUserGroupRoleModelListener
	extends LiveGroupIdModelListener<UserGroupRole> {

	@Override
	public void onBeforeCreate(UserGroupRole userGroupRole)
		throws ModelListenerException {

		validateGroup(GroupUtil.fetchByPrimaryKey(
			userGroupRole.getGroupId()));
	}

}

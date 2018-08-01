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

package com.liferay.portal.social.layout.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutLocalServiceWrapper;

/**
 * @author Preston Crary
 */
public class TestLayoutLocalServiceImpl extends LayoutLocalServiceWrapper {

	public TestLayoutLocalServiceImpl(LayoutLocalService layoutLocalService) {
		super(layoutLocalService);
	}

	@Override
	public Layout getLayout(long plid) throws PortalException {

		System.out.println("Getting layout with plid = " + plid);

		return super.getLayout(plid);
	}

}
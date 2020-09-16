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

package com.liferay.dispatch.web.internal;

import com.liferay.dispatch.executor.ScheduledTaskExecutor;
import com.liferay.portal.kernel.exception.PortalException;

import java.io.IOException;

import org.osgi.service.component.annotations.Component;

/**
 * @author Matija Petanjek
 */
@Component(
	immediate = true, property = "scheduled.task.executor.type=11111",
	service = ScheduledTaskExecutor.class
)
public class Izbrisime implements ScheduledTaskExecutor {

	@Override
	public void execute(long dispatchTriggerId)
		throws IOException, PortalException {

		System.out.println("11111111111111");
	}

	@Override
	public String getName() {
		return "11111";
	}

}
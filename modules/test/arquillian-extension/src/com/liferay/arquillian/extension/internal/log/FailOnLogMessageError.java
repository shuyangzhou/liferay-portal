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

package com.liferay.arquillian.extension.internal.log;

import com.liferay.portal.log.CaptureAppender;

import org.jboss.arquillian.core.spi.EventContext;

/**
 * @author Cristina González
 */
public interface FailOnLogMessageError {

	public void caughtLogMessageError(LogMessageError logMessageError)
		throws Throwable;

	public CaptureAppender start(EventContext<?> eventContext) throws Throwable;

	public void stop(
			EventContext<?> eventContext, CaptureAppender captureAppender)
		throws Throwable;

}
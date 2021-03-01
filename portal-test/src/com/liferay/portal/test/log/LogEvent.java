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

package com.liferay.portal.test.log;

import com.liferay.petra.string.StringBundler;

import org.apache.logging.log4j.message.Message;

/**
 * @author Tina Tian
 */
public class LogEvent {

	public LogEvent(org.apache.logging.log4j.core.LogEvent logEvent) {
		_logEvent = logEvent;
	}

	public String getMessage() {
		Message message = _logEvent.getMessage();

		return message.getFormattedMessage();
	}

	public String getPriority() {
		return String.valueOf(_logEvent.getLevel());
	}

	public Throwable getThrowable() {
		return _logEvent.getThrown();
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(5);

		sb.append("{level=");
		sb.append(getPriority());
		sb.append(", message=");
		sb.append(getMessage());
		sb.append("}");

		return sb.toString();
	}

	private final org.apache.logging.log4j.core.LogEvent _logEvent;

}
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

package com.liferay.portal.test.rule;

import com.liferay.petra.string.StringBundler;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.message.Message;

/**
 * @author William Newbury
 */
public class LogAssertionAppender extends AbstractAppender {

	public static final LogAssertionAppender INSTANCE =
		new LogAssertionAppender();

	public LogAssertionAppender() {
		super("LogAssertionAppender", null, null, true, null);

		start();
	}

	@Override
	public void append(LogEvent logEvent) {
		Level level = logEvent.getLevel();

		if (level.equals(Level.ERROR) || level.equals(Level.FATAL)) {
			StringBundler sb = new StringBundler(6);

			sb.append("{level=");
			sb.append(logEvent.getLevel());
			sb.append(", loggerName=");
			sb.append(logEvent.getLoggerName());
			sb.append(", message=");

			Message objectMessage = logEvent.getMessage();

			sb.append(objectMessage.getFormattedMessage());

			LogAssertionTestRule.caughtFailure(
				new AssertionError(sb.toString(), logEvent.getThrown()));
		}
	}

}
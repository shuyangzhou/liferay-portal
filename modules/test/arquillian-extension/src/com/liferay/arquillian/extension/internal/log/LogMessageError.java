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

import com.liferay.portal.kernel.util.StringBundler;

import java.util.logging.LogRecord;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import org.jboss.arquillian.core.spi.event.Event;

/**
 * @author Cristina González
 */
public class LogMessageError implements Event {

	public LogMessageError(LoggingEvent loggingEvent, Thread logThread) {
		_logThread = logThread;

		StringBundler sb = new StringBundler(6);

		sb.append("{level=");
		sb.append(loggingEvent.getLevel());
		sb.append(", loggerName=");
		sb.append(loggingEvent.getLoggerName());
		sb.append(", message=");
		sb.append(loggingEvent.getMessage());

		ThrowableInformation throwableInformation =
			loggingEvent.getThrowableInformation();

		Throwable throwable = null;

		if (throwableInformation != null) {
			throwable = throwableInformation.getThrowable();
		}

		_assertionError = new AssertionError(sb.toString(), throwable);
	}

	public LogMessageError(LogRecord logRecord, Thread logThread) {
		_logThread = logThread;

		StringBundler sb = new StringBundler(6);

		sb.append("{level=");
		sb.append(logRecord.getLevel());
		sb.append(", loggerName=");
		sb.append(logRecord.getLoggerName());
		sb.append(", message=");
		sb.append(logRecord.getMessage());

		_assertionError = new AssertionError(
			sb.toString(), logRecord.getThrown());
	}

	public AssertionError getAssertionError() {
		return _assertionError;
	}

	public Thread getLogThread() {
		return _logThread;
	}

	private final AssertionError _assertionError;
	private final Thread _logThread;

}
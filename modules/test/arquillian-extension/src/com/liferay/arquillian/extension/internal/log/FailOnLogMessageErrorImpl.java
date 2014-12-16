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

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.log.CaptureAppender;
import com.liferay.portal.log.Log4JLoggerTestUtil;

import java.lang.reflect.Method;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.EventContext;
import org.jboss.arquillian.test.spi.event.suite.TestEvent;

import org.junit.Assert;

/**
 * @author Cristina González
 */
public class FailOnLogMessageErrorImpl implements FailOnLogMessageError {

	@Override
	public void caughtLogMessageError(LogMessageError logMessageError)
		throws Throwable {

		if (_started) {
			if (logMessageError.getLogThread() != _thread) {
				_concurrentFailures.put(
					logMessageError.getLogThread(),
					logMessageError.getAssertionError());

				_thread.interrupt();
			}
			else {
				throw logMessageError.getAssertionError();
			}
		}
	}

	@Override
	public CaptureAppender start(EventContext<?> eventContext)
		throws Throwable {

		ExpectedLogs expectedLogs = getAnnotation(eventContext.getEvent());

		return start(expectedLogs);
	}

	@Override
	public void stop(
			EventContext<?> eventContext, CaptureAppender captureAppender)
		throws Throwable {

		ExpectedLogs expectedLogs = getAnnotation(eventContext.getEvent());

		stop(expectedLogs, captureAppender);
	}

	protected ExpectedLogs getAnnotation(Object event) {
		if (!(event instanceof TestEvent)) {
			return null;
		}

		Method method = ((TestEvent)event).getTestMethod();

		return method.getAnnotation(ExpectedLogs.class);
	}

	protected void installJdk14Handler() {
		Logger logger = Logger.getLogger(StringPool.BLANK);

		FailOnLogMessageErrorHandler failOnLogMessageErrorHandler =
			_failOnErrorHandlerInstance.get();

		logger.removeHandler(failOnLogMessageErrorHandler);

		logger.addHandler(failOnLogMessageErrorHandler);
	}

	protected void installLog4jAppender() {
		org.apache.log4j.Logger logger =
			org.apache.log4j.Logger.getRootLogger();

		FailOnLogMessageErrorAppender failOnErrorAppender =
			_failOnErrorAppenderInstance.get();

		logger.removeAppender(failOnErrorAppender);

		logger.addAppender(failOnErrorAppender);
	}

	protected boolean isExpected(
		ExpectedLogs expectedLogs, String renderedMessage) {

		for (ExpectedLog expectedLog : expectedLogs.expectedLogs()) {
			ExpectedType expectedType = expectedLog.expectedType();

			if (expectedType == ExpectedType.EXACT) {
				if (renderedMessage.equals(expectedLog.expectedLog())) {
					return true;
				}
			}
			else if (expectedType == ExpectedType.POSTFIX) {
				if (renderedMessage.endsWith(expectedLog.expectedLog())) {
					return true;
				}
			}
			else if (expectedType == ExpectedType.PREFIX) {
				if (renderedMessage.startsWith(expectedLog.expectedLog())) {
					return true;
				}
			}
		}

		return false;
	}

	protected CaptureAppender start(ExpectedLogs expectedLogs) {
		_thread = Thread.currentThread();

		CaptureAppender captureAppender = null;

		if (expectedLogs != null) {
			Class<?> clazz = expectedLogs.loggerClass();

			captureAppender = Log4JLoggerTestUtil.configureLog4JLogger(
				clazz.getName(), Level.toLevel(expectedLogs.level()));
		}

		installJdk14Handler();
		installLog4jAppender();

		_started = true;

		return captureAppender;
	}

	protected void stop(
		ExpectedLogs expectedLogs, CaptureAppender captureAppender) {

		_started = false;

		if (expectedLogs != null) {
			try {
				for (LoggingEvent loggingEvent :
						captureAppender.getLoggingEvents()) {

					String renderedMessage = loggingEvent.getRenderedMessage();

					if (!isExpected(expectedLogs, renderedMessage)) {
						Assert.fail(renderedMessage);
					}
				}
			}
			finally {
				captureAppender.close();
			}
		}

		_thread = null;

		try {
			for (Map.Entry<Thread, Error> entry :
					_concurrentFailures.entrySet()) {

				Thread thread = entry.getKey();
				Error error = entry.getValue();

				Assert.fail(
					"Thread " + thread + " caught concurrent failure: " +
						error);

				throw error;
			}
		}
		finally {
			_concurrentFailures.clear();
		}
	}

	private static final Map<Thread, Error> _concurrentFailures =
		new ConcurrentHashMap<Thread, Error>();
	private static volatile boolean _started = false;
	private static volatile Thread _thread;

	@Inject
	private Instance<FailOnLogMessageErrorAppender>
		_failOnErrorAppenderInstance;

	@Inject
	private Instance<FailOnLogMessageErrorHandler> _failOnErrorHandlerInstance;

}
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

package com.liferay.portal.kernel.log;

/**
 * @author Brian Wing Shun Chan
 */
public class LogWrapper implements Log {

	public LogWrapper(Log log) {
		_log = log;
	}

	public void clearCache() {
		_debugEnabled = null;
		_errorEnabled = null;
		_fatalEnabled = null;
		_infoEnabled = null;
		_traceEnabled = null;
		_warnEnabled = null;
	}

	@Override
	public void debug(Object msg) {
		try {
			_log.debug(msg);
		}
		catch (Exception e) {
			printMsg(msg);
		}
	}

	@Override
	public void debug(Object msg, Throwable t) {
		try {
			_log.debug(msg, t);
		}
		catch (Exception e) {
			printMsg(msg);
		}
	}

	@Override
	public void debug(Throwable t) {
		try {
			_log.debug(t);
		}
		catch (Exception e) {
			printMsg(t.getMessage());
		}
	}

	@Override
	public void error(Object msg) {
		try {
			_log.error(msg);
		}
		catch (Exception e) {
			printMsg(msg);
		}
	}

	@Override
	public void error(Object msg, Throwable t) {
		try {
			_log.error(msg, t);
		}
		catch (Exception e) {
			printMsg(msg);
		}
	}

	@Override
	public void error(Throwable t) {
		try {
			_log.error(t);
		}
		catch (Exception e) {
			printMsg(t.getMessage());
		}
	}

	@Override
	public void fatal(Object msg) {
		try {
			_log.fatal(msg);
		}
		catch (Exception e) {
			printMsg(msg);
		}
	}

	@Override
	public void fatal(Object msg, Throwable t) {
		try {
			_log.fatal(msg, t);
		}
		catch (Exception e) {
			printMsg(msg);
		}
	}

	@Override
	public void fatal(Throwable t) {
		try {
			_log.fatal(t);
		}
		catch (Exception e) {
			printMsg(t.getMessage());
		}
	}

	public Log getWrappedLog() {
		return _log;
	}

	@Override
	public void info(Object msg) {
		try {
			_log.info(msg);
		}
		catch (Exception e) {
			printMsg(msg);
		}
	}

	@Override
	public void info(Object msg, Throwable t) {
		try {
			_log.info(msg, t);
		}
		catch (Exception e) {
			printMsg(msg);
		}
	}

	@Override
	public void info(Throwable t) {
		try {
			_log.info(t);
		}
		catch (Exception e) {
			printMsg(t.getMessage());
		}
	}

	@Override
	public boolean isDebugEnabled() {
		Boolean debugEnabled = _debugEnabled;

		if (debugEnabled == null) {
			debugEnabled = _log.isDebugEnabled();

			_debugEnabled = debugEnabled;
		}

		return debugEnabled;
	}

	@Override
	public boolean isErrorEnabled() {
		Boolean errorEnabled = _errorEnabled;

		if (errorEnabled == null) {
			errorEnabled = _log.isErrorEnabled();

			_errorEnabled = errorEnabled;
		}

		return errorEnabled;
	}

	@Override
	public boolean isFatalEnabled() {
		Boolean fatalEnabled = _fatalEnabled;

		if (fatalEnabled == null) {
			fatalEnabled = _log.isFatalEnabled();

			_fatalEnabled = fatalEnabled;
		}

		return fatalEnabled;
	}

	@Override
	public boolean isInfoEnabled() {
		Boolean infoEnabled = _infoEnabled;

		if (infoEnabled == null) {
			infoEnabled = _log.isInfoEnabled();

			_infoEnabled = infoEnabled;
		}

		return infoEnabled;
	}

	@Override
	public boolean isTraceEnabled() {
		Boolean traceEnabled = _traceEnabled;

		if (traceEnabled == null) {
			traceEnabled = _log.isTraceEnabled();

			_traceEnabled = traceEnabled;
		}

		return traceEnabled;
	}

	@Override
	public boolean isWarnEnabled() {
		Boolean warnEnabled = _warnEnabled;

		if (warnEnabled == null) {
			warnEnabled = _log.isWarnEnabled();

			_warnEnabled = warnEnabled;
		}

		return warnEnabled;
	}

	public void setLog(Log log) {
		_log = log;

		clearCache();
	}

	@Override
	public void setLogWrapperClassName(String className) {
		_log.setLogWrapperClassName(className);
	}

	@Override
	public void trace(Object msg) {
		try {
			_log.trace(msg);
		}
		catch (Exception e) {
			printMsg(msg);
		}
	}

	@Override
	public void trace(Object msg, Throwable t) {
		try {
			_log.trace(msg, t);
		}
		catch (Exception e) {
			printMsg(msg);
		}
	}

	@Override
	public void trace(Throwable t) {
		try {
			_log.trace(t);
		}
		catch (Exception e) {
			printMsg(t.getMessage());
		}
	}

	@Override
	public void warn(Object msg) {
		try {
			_log.warn(msg);
		}
		catch (Exception e) {
			printMsg(msg);
		}
	}

	@Override
	public void warn(Object msg, Throwable t) {
		try {
			_log.warn(msg, t);
		}
		catch (Exception e) {
			printMsg(msg);
		}
	}

	@Override
	public void warn(Throwable t) {
		try {
			_log.warn(t);
		}
		catch (Exception e) {
			printMsg(t.getMessage());
		}
	}

	protected void printMsg(Object msg) {
		System.err.println(msg);
	}

	private volatile Boolean _debugEnabled;
	private volatile Boolean _errorEnabled;
	private volatile Boolean _fatalEnabled;
	private volatile Boolean _infoEnabled;
	private Log _log;
	private volatile Boolean _traceEnabled;
	private volatile Boolean _warnEnabled;

}
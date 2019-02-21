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

package com.liferay.arquillian.extension.junit.bridge.result;

import java.io.Serializable;

import org.jboss.arquillian.test.spi.ExceptionProxy;

/**
 * @author Matthew Tambara
 */
public final class TestResult implements Serializable {

	public static TestResult failed(Throwable cause) {
		return new TestResult(Status.FAILED, cause);
	}

	public static TestResult passed() {
		return new TestResult(Status.PASSED);
	}

	public static TestResult skipped(Throwable cause) {
		return new TestResult(Status.SKIPPED, cause);
	}

	@Deprecated
	public TestResult() {
		this(null);
	}

	@Deprecated
	public TestResult(Status status) {
		this(status, null);
	}

	@Deprecated
	public TestResult(Status status, Throwable throwable) {
		_status = status;

		setThrowable(throwable);

		_start = System.currentTimeMillis();
	}

	public long getEnd() {
		return _end;
	}

	public ExceptionProxy getExceptionProxy() {
		return _exceptionProxy;
	}

	public long getStart() {
		return _start;
	}

	public Status getStatus() {
		return _status;
	}

	public Throwable getThrowable() {
		if ((_throwable == null) && (_exceptionProxy != null)) {
			_throwable = _exceptionProxy.createException();
		}

		return _throwable;
	}

	public TestResult setEnd(long end) {
		_end = end;

		return this;
	}

	public TestResult setStart(long start) {
		_start = start;

		return this;
	}

	@Deprecated
	public TestResult setStatus(Status status) {
		_status = status;

		return this;
	}

	public TestResult setThrowable(Throwable throwable) {
		_throwable = throwable;

		_exceptionProxy = ExceptionProxy.createForException(throwable);

		return this;
	}

	public enum Status {

		FAILED, PASSED, SKIPPED

	}

	private static final long serialVersionUID = 1L;

	private long _end;
	private ExceptionProxy _exceptionProxy;
	private long _start;
	private Status _status;
	private transient Throwable _throwable;

}
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

/**
 * @author Matthew Tambara
 */
public final class TestResult implements Serializable {

	public static TestResult failed(Throwable cause) {
		return new TestResult(Status.FAILED, cause);
	}

	public static TestResult passed() {
		return new TestResult(Status.PASSED, null);
	}

	public static TestResult skipped(Throwable cause) {
		return new TestResult(Status.SKIPPED, cause);
	}

	public TestResult(Status status, Throwable throwable) {
		_status = status;

		setThrowable(throwable);
	}

	public Status getStatus() {
		return _status;
	}

	public Throwable getThrowable() {
		return _throwable;
	}

	public TestResult setThrowable(Throwable throwable) {
		_throwable = throwable;

		return this;
	}

	public enum Status {

		FAILED, PASSED, SKIPPED

	}

	private static final long serialVersionUID = 1L;

	private final Status _status;
	private Throwable _throwable;

}
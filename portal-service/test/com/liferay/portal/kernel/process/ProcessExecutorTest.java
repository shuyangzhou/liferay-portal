/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.process;

import com.liferay.portal.kernel.test.TestCase;

import java.io.ByteArrayOutputStream;

/**
 * @author Alexander Chow
 */
public class ProcessExecutorTest extends TestCase {

	public void testOut() throws Exception {
		ProcessCallable<String> processCallable =
			new TestOutProcessCallable(_TEST_STRING);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

		ProcessExecutor.execute(
			processCallable, _classPath, outputStream, errorStream, true);

		assertEquals(_TEST_STRING, outputStream.toString());
	}

	public void testReturn() throws Exception {
		ProcessCallable<String> processCallable =
			new TestReturnProcessCallable(_TEST_STRING);

		String retStr = ProcessExecutor.execute(
			processCallable, _classPath, false);

		assertEquals(_TEST_STRING, retStr);
	}

	public void testThrow() throws Exception {
		ProcessCallable<String> processCallable =
			new TestThrowProcessCallable(_TEST_STRING);

		try {
			ProcessExecutor.execute(processCallable, _classPath, false);
		}
		catch (ProcessException pe) {
			String message = pe.getMessage();

			assertTrue(message.contains(_TEST_STRING));

			return;
		}

		fail("Did not throw ProcessException");
	}

	private static final String _TEST_STRING = "Hello World";

	private static String _classPath = System.getProperty("java.class.path");

	private static class TestOutProcessCallable
		implements ProcessCallable<String> {

		public TestOutProcessCallable(String str) {
			_str = str;
		}

		public String call() throws ProcessException {
			System.out.println(_str);

			return null;
		}

		private String _str;

	}

	private static class TestReturnProcessCallable
		implements ProcessCallable<String> {

		public TestReturnProcessCallable(String str) {
			_str = str;
		}

		public String call() throws ProcessException {
			return _str;
		}

		private String _str;

	}

	private static class TestThrowProcessCallable
		implements ProcessCallable<String> {

		public TestThrowProcessCallable(String str) {
			_str = str;
		}

		public String call() throws ProcessException {
			throw new ProcessException(_str);
		}

		private String _str;

	}

}
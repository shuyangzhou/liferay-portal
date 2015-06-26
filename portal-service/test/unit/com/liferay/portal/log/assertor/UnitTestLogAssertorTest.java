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

package com.liferay.portal.log.assertor;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

/**
 * @author William Newbury
 */
public class UnitTestLogAssertorTest {

	@Test
	public void testScanXmlLog() throws IOException {
		String jenkinsHome = System.getenv("JENKINS_HOME");

		if (jenkinsHome == null) {
			return;
		}

		Path log4jLogsPath = Paths.get(
			System.getProperty("user.dir") + "/unit-test-log4j-logs.xml");

		if (Files.exists(log4jLogsPath)) {
			LogAssertorUtil.scanLog4jXmlLogFile(log4jLogsPath);
		}

		Path jdkLogsPath = Paths.get(
			System.getProperty("user.dir") + "/unit-test-jdk-logs.xml");

		if (Files.exists(jdkLogsPath)) {
			LogAssertorUtil.scanJdkXMLLogFile(jdkLogsPath);
		}
	}

}
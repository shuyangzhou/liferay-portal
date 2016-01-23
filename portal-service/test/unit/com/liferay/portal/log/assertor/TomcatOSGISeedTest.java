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

import java.io.File;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author William Newbury
 */
public class TomcatOSGISeedTest {

	@Test
	public void testOSGISeedLog() throws IOException {
		String testBuildTimestamp = null;
		String logOutputPath = null;

		testBuildTimestamp = System.getProperty("osgi.shuffle.timestamp");
		logOutputPath = System.getProperty("osgi.shuffle.output.log.path");

		if ((testBuildTimestamp == null) || (logOutputPath == null)) {
			Assert.fail("No osgi shuffling log configuration supplied");
		}

		File osgiSeedingLog = new File(logOutputPath);

		List<String> lines = Files.readAllLines(
			osgiSeedingLog.toPath(), StandardCharsets.UTF_8);

		int count = 0;

		try {
			for (String line : lines) {
				if (line.contains(testBuildTimestamp)) {
					count++;
				}
			}

			Assert.assertEquals(4, count);
		}
		finally {
			osgiSeedingLog.delete();
		}
	}

}
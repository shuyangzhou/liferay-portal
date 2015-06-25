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

import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class PortalLogAssertorTest {

	@Test
	public void testScanXmlLog() throws IOException {
		String jenkinsHome = System.getenv("JENKINS_HOME");

		if (jenkinsHome == null) {
			return;
		}

		Files.walkFileTree(
			Paths.get(System.getProperty("liferay.log.dir")),
			new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(
						Path path, BasicFileAttributes basicFileAttributes)
					throws IOException {

					String pathString = StringUtil.toLowerCase(path.toString());

					if (pathString.endsWith(".xml")) {
						LogAssertorUtil.scanLog4jXmlLogFile(path);
					}

					return FileVisitResult.CONTINUE;
				}

			});
	}

}
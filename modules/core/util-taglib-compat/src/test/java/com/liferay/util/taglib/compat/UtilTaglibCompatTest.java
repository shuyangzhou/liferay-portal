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

package com.liferay.util.taglib.compat;

import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;

import java.io.File;
import java.io.FileReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class UtilTaglibCompatTest {

	@Test
	public void testNoConflictingImportedClasses() throws Exception {
		File importedClassesPropertiesFile = new File(
			"imported-classes.properties");

		Assert.assertTrue(importedClassesPropertiesFile.exists());

		try (FileReader fileReader = new FileReader(
				importedClassesPropertiesFile);
			UnsyncBufferedReader unsyncBufferedReader =
				new UnsyncBufferedReader(fileReader)) {

			String line = null;

			while ((line = unsyncBufferedReader.readLine()) != null) {
				if (line.indexOf('=') >= 0) {
					Matcher matcher = _dependencyPattern.matcher(line);

					Assert.assertTrue(matcher.matches());
				}
				else {
					line = line.trim();

					if (line.endsWith(",\\")) {
						line = line.substring(0, line.length() - 2);
					}

					File file = new File(
						"../../../util-taglib/src/" + line.replace('.', '/') +
							".java");

					Assert.assertFalse(file.getPath(), file.exists());
				}
			}
		}
	}

	private static final Pattern _dependencyPattern = Pattern.compile(
		"com.liferay.portal\\\\:com.liferay.util.taglib\\\\:" +
			"\\d+\\.\\d+\\.\\d+=\\\\");

}
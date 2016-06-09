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

package com.liferay.portal.lpkg;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.util.PropsValues;

import java.io.BufferedReader;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/**
 * @author Matthew Tambara
 */
public class LPKGUpgradeTest {

	@Test
	public void testUpgradeLPKG() throws IOException {
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(
				Paths.get(PropsValues.LIFERAY_HOME, "/osgi/marketplace"))) {

			for (Path lpkgPath : directoryStream) {
				try (FileSystem fileSystem = FileSystems.newFileSystem(
						lpkgPath, null)) {

					Path path = fileSystem.getPath(
						"liferay-marketplace.properties");

					try (BufferedReader bufferedReader =
							Files.newBufferedReader(
								path, StandardCharsets.UTF_8)) {

						String line = null;

						StringBundler sb = new StringBundler();

						while ((line = bufferedReader.readLine()) != null) {
							if (!line.contains("version=")) {
								sb.append(line);
								sb.append(StringPool.NEW_LINE);

								continue;
							}

							int index = line.lastIndexOf(StringPool.PERIOD);

							int minorVersion = Integer.parseInt(
								line.substring(index + 1));

							String majorVersion = line.substring(0, index + 1);

							line = majorVersion.concat(
								String.valueOf(minorVersion + 1));

							sb = sb.append(line);
							sb = sb.append(StringPool.NEW_LINE);
						}

						String result = sb.toString();

						Files.write(
							path, result.getBytes(), StandardOpenOption.CREATE,
							StandardOpenOption.TRUNCATE_EXISTING,
							StandardOpenOption.WRITE);
					}
				}
			}
		}
	}

}
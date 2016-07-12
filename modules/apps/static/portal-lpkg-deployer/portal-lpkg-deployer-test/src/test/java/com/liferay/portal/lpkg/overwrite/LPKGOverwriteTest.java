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

package com.liferay.portal.lpkg.overwrite;

import com.liferay.portal.kernel.io.unsync.UnsyncStringReader;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.File;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.Assert;
import org.junit.Test;

import org.osgi.framework.Version;

/**
 * @author Matthew Tambara
 */
public class LPKGOverwriteTest {

	@Test
	public void testOverwriteLPKG() throws IOException {
		String liferayHome = System.getProperty("liferay.home");

		Assert.assertNotNull(
			"Missing system property \"liferay.home\"", liferayHome);

		File file = new File(liferayHome, "/osgi/marketplace/overwritten");

		if (file.exists()) {
			String[] files = file.list();

			for (String childPath : files) {
				File childFile = new File(file.getPath(), childPath);

				childFile.delete();
			}
		}
		else {
			file.mkdir();
		}

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(
				Paths.get(liferayHome, "/osgi/marketplace"))) {

			for (Path lpkgPath : directoryStream) {
				String lpkgPathString = lpkgPath.toString();

				if (lpkgPathString.endsWith("overwritten") ||
					lpkgPathString.contains("Static")) {

					continue;
				}

				try (ZipFile zipFile = new ZipFile(lpkgPath.toFile())) {
					Enumeration<? extends ZipEntry> zipEntries =
						zipFile.entries();

					while (zipEntries.hasMoreElements()) {
						ZipEntry zipEntry = zipEntries.nextElement();

						String name = zipEntry.getName();

						if ((name.startsWith("com.liferay") &&
							 name.endsWith(".jar")) ||
							name.endsWith(".war")) {

							Matcher matcher = _pattern.matcher(name);

							String overriddenName = matcher.replaceAll("$2");

							Files.copy(
								zipFile.getInputStream(zipEntry),
								Paths.get(file.toString(), overriddenName),
								StandardCopyOption.REPLACE_EXISTING);
						}
					}
				}
			}
		}

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(
				Paths.get(file.toURI()))) {

			for (Path overwritePath : directoryStream) {
				String overwriteString = overwritePath.toString();

				if (!overwriteString.endsWith(".jar")) {
					continue;
				}

				try (FileSystem fileSystem = FileSystems.newFileSystem(
						overwritePath, null)) {

					Path path = fileSystem.getPath("META-INF/MANIFEST.MF");

					String propertiesString = new String(
						Files.readAllBytes(path), StandardCharsets.UTF_8);

					Properties properties = new Properties();

					properties.load(new UnsyncStringReader(propertiesString));

					String versionString = properties.getProperty(
						"Bundle-Version");

					Version version = new Version(versionString);

					version = new Version(
						version.getMajor(), version.getMinor(),
						version.getMicro() + 1);

					propertiesString = StringUtil.replace(
						propertiesString,
						"Bundle-Version: ".concat(versionString),
						"Bundle-Version: ".concat(version.toString()));

					Files.write(
						path, Arrays.asList(propertiesString),
						StandardCharsets.UTF_8,
						StandardOpenOption.TRUNCATE_EXISTING,
						StandardOpenOption.WRITE);

					String symbolicName = properties.getProperty(
						"Bundle-SymbolicName");

					int index = propertiesString.indexOf(
						StringPool.NEW_LINE,
						propertiesString.indexOf(
							"Bundle-SymbolicName: ".concat(symbolicName)));

					if (CharPool.SPACE == propertiesString.charAt(index + 1)) {
						symbolicName = symbolicName.concat(
							propertiesString.substring(
								index + 2,
								propertiesString.indexOf(
									StringPool.NEW_LINE, index + 1) - 1));
					}

					_overwrites.put(symbolicName, version.toString());
				}
			}

			StringBundler sb = new StringBundler(_overwrites.size() * 4);

			for (Entry<String, String> entry : _overwrites.entrySet()) {
				sb.append(entry.getKey());
				sb.append(StringPool.COLON);
				sb.append(entry.getValue());
				sb.append(StringPool.NEW_LINE);
			}

			sb.setIndex(sb.index() - 1);

			Files.write(
				Paths.get(liferayHome, "/overwrites"),
				Arrays.asList(sb.toString()), StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.WRITE);
		}
	}

	private static final Pattern _pattern = Pattern.compile(
		"(-\\d.+)(\\.([jw]ar))");

	private final Map<String, String> _overwrites = new HashMap<>();

}
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

package com.liferay.portal.lpkg.overwrite.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.Version;

/**
 * @author Matthew Tambara
 */
@RunWith(Arquillian.class)
public class LPKGOverwriteVerifyTest {

	@Test
	public void testOverwrittenLPKGJars() throws Exception {
		Bundle testBundle = FrameworkUtil.getBundle(
			LPKGOverwriteVerifyTest.class);

		BundleContext bundleContext = testBundle.getBundleContext();

		Properties properties = new Properties();

		Path path = Paths.get(PropsValues.LIFERAY_HOME, "/overwrites");

		Assert.assertTrue(Files.exists(path));

		properties.load(Files.newBufferedReader(path));

		for (Bundle bundle : bundleContext.getBundles()) {
			String symbolicName = bundle.getSymbolicName();

			String version = (String)properties.remove(symbolicName);

			if (version != null) {
				Assert.assertEquals(
					"Bundle not sucessfully overwritten: " + symbolicName,
					new Version(version), bundle.getVersion());
			}
		}

		List<Entry> leftoverEntries = new ArrayList<>();

		leftoverEntries.addAll(properties.entrySet());

		Collections.sort(
			leftoverEntries,
			new Comparator<Entry>() {

				@Override
				public int compare(Entry entry1, Entry entry2) {
					String entrySymbolicname = (String)entry1.getKey();

					return entrySymbolicname.compareTo((String)entry2.getKey());
				}

			});

		Assert.assertTrue(
			"Bundle not overwritten: " + properties.entrySet(),
			properties.isEmpty());
	}

	@Test
	public void testOverwrittenLPKGWars() throws Exception {
		Bundle testBundle = FrameworkUtil.getBundle(
			LPKGOverwriteVerifyTest.class);

		BundleContext bundleContext = testBundle.getBundleContext();

		final List<String> wars = new ArrayList<>();

		Files.walkFileTree(
			Paths.get(
				PropsValues.LIFERAY_HOME, "/osgi/marketplace/overwritten"),
			new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(
						Path filePath, BasicFileAttributes basicFileAttributes)
					throws IOException {

					Path fileNamePath = filePath.getFileName();

					String fileName = fileNamePath.toString();

					if (fileName.endsWith(".war")) {
						wars.add(fileName.substring(0, fileName.length() - 4));
					}

					return FileVisitResult.CONTINUE;
				}

			});

		for (Bundle bundle : bundleContext.getBundles()) {
			String symbolicName = bundle.getSymbolicName();

			if (wars.remove(symbolicName)) {
				String location = bundle.getLocation();

				Assert.assertTrue(
					symbolicName + " was not overwritten sucessfully",
					!location.startsWith("lpkg://"));
			}
		}

		Assert.assertTrue("Wars not overwritten: " + wars, wars.isEmpty());
	}

}
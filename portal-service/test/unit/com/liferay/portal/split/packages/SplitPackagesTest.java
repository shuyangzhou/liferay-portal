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

package com.liferay.portal.split.packages;

import java.io.IOException;

import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Tom Wang
 */
public class SplitPackagesTest {

	@Test
	public void testSplitPackage() throws IOException {
		List<String> portalImplList = _getPackageNameList(
			Paths.get("portal-impl/src"));

		List<String> portalServiceList = _getPackageNameList(
			Paths.get("portal-service/src"));

		portalImplList.retainAll(portalServiceList);

		System.out.println(
			"The following packages are present in both sides: ");

		for (String duplicated : portalImplList) {
			System.out.println(duplicated);
		}

		Assert.assertTrue(portalImplList.isEmpty());
	}

	private List<String> _getPackageNameList(final Path path) throws IOException {
		final List<String> packageNameList = new ArrayList<>();

		Files.walkFileTree(
			path,
			new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(
					Path dirPath, BasicFileAttributes attrs)
					throws IOException {

					try (DirectoryStream directoryStream =
						Files.newDirectoryStream(dirPath, "*.java")) {

						Iterator iterator = directoryStream.iterator();

						if (iterator.hasNext()) {
							Path relativePath = path.relativize(dirPath);

							String relativePathString = relativePath.toString();

							String packageName = relativePathString.replace(
								"/", ".");

							packageNameList.add(packageName);
						}
					}

					return FileVisitResult.CONTINUE;
				}
			});

		return packageNameList;
	}

}

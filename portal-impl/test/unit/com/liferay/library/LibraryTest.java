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

package com.liferay.library;

import com.liferay.portal.kernel.util.StringBundler;

import java.io.File;

import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Matthew Tambara
 */
public class LibraryTest {

	@BeforeClass
	public static void setUpClass() {
		_getLibJars();

		DocumentBuilderFactory documentBuilderFactory =
			DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder documentBuilder =
				documentBuilderFactory.newDocumentBuilder();

			_getClasspathJars(documentBuilder);

			_getNBProjectJars(documentBuilder);

			_getVersionsJars(documentBuilder);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testClasspathJarsInLib() {
		_doSearch(_classpathJars, _libJars, _CLASSPATH_PATH);
	}

	@Test
	public void testLibJarsInClasspath() {
		_doSearch(_libJars, _classpathJars, _CLASSPATH_PATH);
	}

	@Test
	public void testLibJarsInNBProject() {
		_doSearch(_libJars, _nbProjectJars, _NBPROJECT_PATH);
	}

	@Test
	public void testLibJarsInVersions() {
		_doSearch(_libJars, _versionsJars, _VERSIONS_PATH);
	}

	@Test
	public void testNBProjectJarsInLib() {
		_doSearch(_nbProjectJars, _libJars, _NBPROJECT_PATH);
	}

	@Test
	public void testVersionsJarsInLib() {
		_doSearch(_versionsJars, _libJars, _VERSIONS_PATH);
	}

	private static void _getClasspathJars(DocumentBuilder documentBuilder) {
		try {
			Document document = documentBuilder.parse(
				new File(_CLASSPATH_PATH));

			NodeList nodelist = document.getElementsByTagName("classpathentry");

			_classpathJars = new HashSet<>();

			for (int i = 0; i<nodelist.getLength(); i++) {
				Node node = nodelist.item(i);

				NamedNodeMap namedNodeMap = node.getAttributes();

				Node pathNode = namedNodeMap.getNamedItem("path");

				String path = pathNode.getNodeValue();

				if (path.startsWith(_LIB)) {
					_classpathJars.add(path);
				}
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void _getLibJars() {
		File file = new File(_LIB);

		_libJars = new HashSet<>();

		for (File jar : FileUtils.listFiles(file, new String[] {"jar"}, true)) {
			_libJars.add(jar.getPath());
		}
	}

	private static void _getNBProjectJars(DocumentBuilder documentBuilder) {
		try {
			Document document = documentBuilder.parse(
				new File(_NBPROJECT_PATH));

			NodeList nodelist = document.getElementsByTagName("classpath");

			_nbProjectJars = new HashSet<>();

			for (int i = 0; i<nodelist.getLength(); i++) {
				Node node = nodelist.item(i);

				String path = node.getTextContent();

				if (path.startsWith(_LIB)) {
					_nbProjectJars.add(path);
				}
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void _getVersionsJars(DocumentBuilder documentBuilder) {
		try {
			Document document = documentBuilder.parse(new File(_VERSIONS_PATH));

			NodeList nodelist = document.getElementsByTagName("file-name");

			_versionsJars = new HashSet<>();

			for (int i = 0; i<nodelist.getLength(); i++) {
				Node node = nodelist.item(i);

				String path = node.getTextContent();

				if (path.startsWith(_LIB)) {
					_versionsJars.add(path);
				}
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void _doSearch(
		Set<String> searchFor, Set<String> searchIn, String path) {

		for (String jar : searchFor) {
			if (searchIn.contains(jar)) {
				continue;
			}

			if (searchFor.equals(_libJars)) {
				Assert.fail(
					_generateMissingReferenceMessage(jar, searchIn, path));
			}

			else {
				Assert.fail(
					_generateExtraReferenceMessage(jar, searchFor, path));
			}
		}
	}

	private String _generateExtraReferenceMessage(
		String jar, Set<String> jarSet, String path) {

		StringBundler sb = new StringBundler(3);

		sb.append(path);
		sb.append(" references non-existent jar: ");
		sb.append(jar);

		return sb.toString();
	}

	private String _generateMissingReferenceMessage(
		String jar, Set<String> jarSet, String path) {

		StringBundler sb = new StringBundler(3);

		sb.append(jar);
		sb.append(" is not in ");
		sb.append(path);

		return sb.toString();
	}

	private static final String _CLASSPATH_PATH = ".classpath";

	private static final String _LIB = "lib";

	private static final String _NBPROJECT_PATH = "nbproject/project.xml";

	private static final String _VERSIONS_PATH = _LIB + "/versions.xml";

	private static Set<String> _classpathJars;
	private static Set<String> _libJars;
	private static Set<String> _nbProjectJars;
	private static Set<String> _versionsJars;

}
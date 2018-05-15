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

package com.liferay.poshi.runner.elements;

import com.liferay.poshi.runner.util.Dom4JUtil;
import com.liferay.poshi.runner.util.FileUtil;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import org.dom4j.util.NodeComparator;

import org.junit.Test;

/**
 * @author Kenji Heigel
 */
public class PoshiElementFactoryTest {

	@Test
	public void testPoshiMacroToReadable() throws Exception {
		String expected = FileUtil.read(_BASE_DIR + "ReadableSyntax.macro");

		PoshiElement poshiElement = _getPoshiElement("PoshiSyntax.macro");

		String actual = poshiElement.toReadableSyntax();

		_assertEqualStrings(
			actual, expected,
			"Poshi syntax does not translate to readable syntax");
	}

	@Test
	public void testPoshiTestToReadable() throws Exception {
		String expected = FileUtil.read(_BASE_DIR + "ReadableSyntax.testcase");

		PoshiElement poshiElement = _getPoshiElement("PoshiSyntax.testcase");

		String actual = poshiElement.toReadableSyntax();

		_assertEqualStrings(
			actual, expected,
			"Poshi syntax does not translate to readable syntax");
	}

	@Test
	public void testPoshiTestToReadableToXML() throws Exception {
		PoshiElement poshiElement = _getPoshiElement("PoshiSyntax.testcase");

		String readableSyntax = poshiElement.toReadableSyntax();

		PoshiElement actualElement =
			(PoshiElement)PoshiNodeFactory.newPoshiNode(
				readableSyntax, "testcase");

		Element expectedElement = _getDom4JElement("PoshiSyntax.testcase");

		_assertEqualElements(
			actualElement, expectedElement,
			"Poshi syntax is not preserved in full translation.");
	}

	@Test
	public void testPoshiTestToXML() throws Exception {
		PoshiElement actualElement = _getPoshiElement("PoshiSyntax.testcase");
		Element expectedElement = _getDom4JElement("PoshiSyntax.testcase");

		_assertEqualElements(
			actualElement, expectedElement,
			"Poshi syntax does not translate to XML.");
	}

	@Test
	public void testReadableMacroToXML() throws Exception {
		PoshiElement actualElement = _getPoshiElement("ReadableSyntax.macro");
		Element expectedElement = _getDom4JElement("PoshiSyntax.macro");

		_assertEqualElements(
			actualElement, expectedElement,
			"Readable syntax does not translate to XML.");
	}

	@Test
	public void testReadableTestToXML() throws Exception {
		PoshiElement actualElement = _getPoshiElement(
			"ReadableSyntax.testcase");
		Element expectedElement = _getDom4JElement("PoshiSyntax.testcase");

		_assertEqualElements(
			actualElement, expectedElement,
			"Readable syntax does not translate to XML.");
	}

	private static void _assertEqualElements(
			Element actualElement, Element expectedElement, String errorMessage)
		throws Exception {

		NodeComparator nodeComparator = new NodeComparator();

		int compare = nodeComparator.compare(actualElement, expectedElement);

		if (compare != 0) {
			String actual = Dom4JUtil.format(actualElement);
			String expected = Dom4JUtil.format(expectedElement);

			_getErrorMessage(actual, expected, errorMessage);

			throw new Exception(errorMessage);
		}
	}

	private static void _assertEqualStrings(
			String actual, String expected, String errorMessage)
		throws Exception {

		if (!actual.equals(expected)) {
			errorMessage = _getErrorMessage(actual, expected, errorMessage);

			throw new Exception(errorMessage);
		}
	}

	private static Element _getDom4JElement(String fileName) throws Exception {
		String fileContent = FileUtil.read(_BASE_DIR + fileName);

		Document document = Dom4JUtil.parse(fileContent);

		Element rootElement = document.getRootElement();

		_removeWhiteSpaceTextNodes(rootElement);

		return rootElement;
	}

	private static String _getErrorMessage(
			String actual, String expected, String errorMessage)
		throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append(errorMessage);
		sb.append("\n\nExpected:\n");
		sb.append(expected);
		sb.append("\n\nActual:\n");
		sb.append(actual);

		return sb.toString();
	}

	private static PoshiElement _getPoshiElement(String fileName) {
		return (PoshiElement)PoshiNodeFactory.newPoshiNodeFromFile(
			_BASE_DIR + fileName);
	}

	private static void _removeWhiteSpaceTextNodes(Element element) {
		for (Node node : Dom4JUtil.toNodeList(element.content())) {
			if (node instanceof Text) {
				String nodeText = node.getText();

				nodeText = nodeText.trim();

				if (nodeText.length() == 0) {
					node.detach();
				}
			}
		}

		for (Element childElement :
				Dom4JUtil.toElementList(element.elements())) {

			_removeWhiteSpaceTextNodes(childElement);
		}
	}

	private static final String _BASE_DIR =
		"src/test/resources/com/liferay/poshi/runner/dependencies/elements/";

}
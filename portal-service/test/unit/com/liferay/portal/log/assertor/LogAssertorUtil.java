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

import com.liferay.portal.kernel.io.unsync.UnsyncStringReader;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Assert;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

/**
 * @author William Newbury
 */
public class LogAssertorUtil {

	protected static void scanJdkXMLLogFile(Path path) throws IOException {
		String content = StringUtil.replace(
			new String(Files.readAllBytes(path), StringPool.UTF8),
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"" +
				"?>\n<!DOCTYPE log SYSTEM \"logger.dtd\">\n<log>\n", "");

		content = StringUtil.replace(content, "</log>\n", "");

		content = "<jdk>" + content + "</jdk>";

		try {
			Document document = _documentBuilder.parse(
				new InputSource(new UnsyncStringReader(content)));

			NodeList nodeList = document.getElementsByTagName("record");

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);

				NodeList childNodelist = node.getChildNodes();

				String level = StringPool.BLANK;
				String message = StringPool.BLANK;

				for (int j = 0; j < childNodelist.getLength(); j++) {
					Node childNode = childNodelist.item(j);

					String childNodeName = childNode.getNodeName();

					if (childNodeName.equals("level")) {
						level = childNode.getTextContent();
					}
					else if (childNodeName.equals("message")) {
						message = childNode.getTextContent();
					}
				}

				if (level.equals("SEVERE") || level.equals("WARNING")) {
					StringBundler sb = new StringBundler(4);

					sb.append(
						"\nPortal log assert failure, see above log for " +
							"more information: \n");
					sb.append(level);
					sb.append(" - ");
					sb.append(message);

					System.out.println(
						"Detected error, for more details refer to file: " +
							StringUtil.replace(
								path.toString(), ".xml", ".log"));

					Assert.fail(sb.toString());
				}
			}
		}
		catch (Exception e) {
			throw new IOException(e);
		}
	}

	protected static void scanLog4jXmlLogFile(Path path) throws IOException {
		String content = StringUtil.replace(
			new String(Files.readAllBytes(path), StringPool.UTF8), "log4j:",
			"");

		content = "<log4j>" + content + "</log4j>";

		try {
			Document document = _documentBuilder.parse(
				new InputSource(new UnsyncStringReader(content)));

			NodeList nodelist = document.getElementsByTagName("event");

			for (int i = 0; i < nodelist.getLength(); i++) {
				Node node = nodelist.item(i);

				NamedNodeMap namedNodeMap = node.getAttributes();

				Node levelNode = namedNodeMap.getNamedItem("level");

				String levelString = levelNode.getNodeValue();

				if (levelString.equals("ERROR") ||
					levelString.equals("FATAL") || levelString.equals("WARN")) {

					NodeList childNodelist = node.getChildNodes();

					String message =
						"\nPortal log assert failure, see above log for more " +
							"information: \n";

					for (int j = 0; j < childNodelist.getLength(); j++) {
						Node childNode = childNodelist.item(j);

						String nodeName = childNode.getNodeName();

						if (nodeName.equals("message")) {
							message += childNode.getTextContent();
						}
						else if (nodeName.equals("throwable")) {
							message += "\n" + childNode.getTextContent();
						}
					}

					System.out.println(
						"Detected error, for more details refer to file: " +
							StringUtil.replace(
								path.toString(), ".xml", ".log"));

					Assert.fail(message);
				}
			}
		}
		catch (Exception e) {
			throw new IOException(e);
		}
	}

	private static final DocumentBuilder _documentBuilder;

	static {
		try {
			DocumentBuilderFactory documentBuilderFactory =
				DocumentBuilderFactory.newInstance();

			_documentBuilder = documentBuilderFactory.newDocumentBuilder();
		}
		catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

}
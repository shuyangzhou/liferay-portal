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

package com.liferay.portal.jsp.engine.internal.delegate;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.descriptor.TaglibDescriptor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tomcat.util.descriptor.web.JspConfigDescriptorImpl;
import org.apache.tomcat.util.descriptor.web.TaglibDescriptorImpl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Shuyang Zhou
 */
public class NamespacedServletContextDelegate {

	public NamespacedServletContextDelegate(
		ServletContext servletContext, ClassLoader classLoader) {

		_servletContext = servletContext;
		_classLoader = classLoader;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ServletContext)) {
			return false;
		}

		ServletContext servletContext = (ServletContext)object;

		return servletContext.equals(_servletContext);
	}

	public Object getAttribute(String name) {
		return _servletContext.getAttribute(_encodeName(name));
	}

	public Enumeration<String> getAttributeNames() {
		List<String> names = new ArrayList<>();

		Enumeration<String> enumeration = _servletContext.getAttributeNames();

		while (enumeration.hasMoreElements()) {
			names.add(_decodeName(enumeration.nextElement()));
		}

		return Collections.enumeration(names);
	}

	public ClassLoader getClassLoader() {
		return _classLoader;
	}

	public String getInitParameter(String name) {
		return _servletContext.getInitParameter(_encodeName(name));
	}

	public Enumeration<String> getInitParameterNames() {
		List<String> names = new ArrayList<>();

		Enumeration<String> enumeration =
			_servletContext.getInitParameterNames();

		while (enumeration.hasMoreElements()) {
			names.add(_decodeName(enumeration.nextElement()));
		}

		return Collections.enumeration(names);
	}

	public JspConfigDescriptor getJspConfigDescriptor() {
		List<TaglibDescriptor> taglibDescriptors = new ArrayList<>();

		DocumentBuilderFactory documentBuilderFactory =
			DocumentBuilderFactory.newInstance();

		try (InputStream inputStream = _servletContext.getResourceAsStream(
				"/WEB-INF/shielded-container-web.xml")) {

			DocumentBuilder documentBuilder =
				documentBuilderFactory.newDocumentBuilder();

			Document document = documentBuilder.parse(inputStream);

			NodeList taglibNodeList = document.getElementsByTagName("taglib");

			for (int i = 0; i < taglibNodeList.getLength(); i++) {
				Element taglibElement = (Element)taglibNodeList.item(i);

				NodeList taglibLocationNodeList =
					taglibElement.getElementsByTagName("taglib-location");

				Node taglibLocationNode = taglibLocationNodeList.item(0);

				NodeList taglibURINodeList = taglibElement.getElementsByTagName(
					"taglib-uri");

				Node taglibURINode = taglibURINodeList.item(0);

				taglibDescriptors.add(
					new TaglibDescriptorImpl(
						taglibLocationNode.getTextContent(),
						taglibURINode.getTextContent()));
			}
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}

		return new JspConfigDescriptorImpl(
			Collections.emptySet(), taglibDescriptors);
	}

	@Override
	public int hashCode() {
		return _servletContext.hashCode();
	}

	public void removeAttribute(String name) {
		_servletContext.removeAttribute(_encodeName(name));
	}

	public void setAttribute(String name, Object object) {
		_servletContext.setAttribute(_encodeName(name), object);
	}

	public boolean setInitParameter(String name, String value) {
		return _servletContext.setInitParameter(_encodeName(name), value);
	}

	private String _decodeName(String name) {
		if (name.startsWith(_LIFERAY_NAMESPACE)) {
			return name.substring(_LIFERAY_NAMESPACE.length());
		}

		return name;
	}

	private String _encodeName(String name) {
		if (name.startsWith(_APACHE_NAMESPACE)) {
			return _LIFERAY_NAMESPACE.concat(name);
		}

		return name;
	}

	private static final String _APACHE_NAMESPACE = "org.apache.";

	private static final String _LIFERAY_NAMESPACE = "com.liferay.";

	private final ClassLoader _classLoader;
	private final ServletContext _servletContext;

}
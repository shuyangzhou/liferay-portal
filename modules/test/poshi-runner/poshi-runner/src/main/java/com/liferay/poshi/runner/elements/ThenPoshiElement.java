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

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * @author Kenji Heigel
 */
public class ThenPoshiElement extends PoshiElement {

	@Override
	public PoshiElement clone(Element element) {
		if (isElementType(_ELEMENT_NAME, element)) {
			return new ThenPoshiElement(element);
		}

		return null;
	}

	@Override
	public PoshiElement clone(
		PoshiElement parentPoshiElement, String poshiScript) {

		if (_isElementType(parentPoshiElement, poshiScript)) {
			return new ThenPoshiElement(parentPoshiElement, poshiScript);
		}

		return null;
	}

	@Override
	public void parsePoshiScript(String poshiScript) {
		for (String poshiScriptSnippet : getPoshiScriptSnippets(poshiScript)) {
			if (isPoshiScriptComment(poshiScriptSnippet)) {
				add(PoshiNodeFactory.newPoshiNode(this, poshiScriptSnippet));

				continue;
			}

			add(PoshiNodeFactory.newPoshiNode(this, poshiScriptSnippet));
		}
	}

	protected ThenPoshiElement() {
	}

	protected ThenPoshiElement(Element element) {
		super(_ELEMENT_NAME, element);
	}

	protected ThenPoshiElement(List<Attribute> attributes, List<Node> nodes) {
		this(_ELEMENT_NAME, attributes, nodes);
	}

	protected ThenPoshiElement(
		PoshiElement parentPoshiElement, String poshiScript) {

		super(_ELEMENT_NAME, parentPoshiElement, poshiScript);
	}

	protected ThenPoshiElement(String name, Element element) {
		super(name, element);
	}

	protected ThenPoshiElement(
		String elementName, List<Attribute> attributes, List<Node> nodes) {

		super(elementName, attributes, nodes);
	}

	protected ThenPoshiElement(
		String name, PoshiElement parentPoshiElement, String poshiScript) {

		super(name, parentPoshiElement, poshiScript);
	}

	@Override
	protected String getBlockName() {
		return "then";
	}

	protected List<String> getPoshiScriptSnippets(String poshiScript) {
		StringBuilder sb = new StringBuilder();

		List<String> poshiScriptSnippets = new ArrayList<>();

		for (String line : poshiScript.split("\n")) {
			String trimmedLine = line.trim();

			String poshiScriptSnippet = sb.toString();

			if (trimmedLine.endsWith("{") && poshiScriptSnippets.isEmpty()) {
				continue;
			}

			if (!trimmedLine.startsWith("else {")) {
				poshiScriptSnippet = poshiScriptSnippet.trim();

				if (isValidPoshiScriptSnippet(poshiScriptSnippet)) {
					poshiScriptSnippets.add(poshiScriptSnippet);

					sb.setLength(0);
				}
			}

			sb.append(line);
			sb.append("\n");
		}

		return poshiScriptSnippets;
	}

	private boolean _isElementType(
		PoshiElement parentPoshiElement, String poshiScript) {

		if ((parentPoshiElement instanceof IfPoshiElement) &&
			poshiScript.startsWith("{")) {

			return true;
		}

		return false;
	}

	private static final String _ELEMENT_NAME = "then";

}
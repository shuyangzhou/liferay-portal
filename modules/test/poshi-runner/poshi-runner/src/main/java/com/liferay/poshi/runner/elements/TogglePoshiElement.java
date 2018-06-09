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
public class TogglePoshiElement extends PoshiElement {

	@Override
	public PoshiElement clone(Element element) {
		if (isElementType(_ELEMENT_NAME, element)) {
			return new TogglePoshiElement(element);
		}

		return null;
	}

	@Override
	public PoshiElement clone(
		PoshiElement parentPoshiElement, String poshiScript) {

		if (_isElementType(poshiScript)) {
			return new TogglePoshiElement(parentPoshiElement, poshiScript);
		}

		return null;
	}

	@Override
	public void parsePoshiScript(String poshiScript) {
		for (String poshiScriptSnippet : getPoshiScriptSnippets(poshiScript)) {
			if (poshiScriptSnippet.startsWith("toggle (")) {
				String parentheticalContent = getParentheticalContent(
					poshiScriptSnippet);

				String summary = getQuotedContent(parentheticalContent);

				addAttribute("name", summary);

				continue;
			}

			add(PoshiNodeFactory.newPoshiNode(this, poshiScriptSnippet));
		}
	}

	@Override
	public String toPoshiScript() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n");

		StringBuilder content = new StringBuilder();

		for (PoshiElement poshiElement : toPoshiElements(elements())) {
			content.append(poshiElement.toPoshiScript());
		}

		String poshiScriptSnippet = createPoshiScriptSnippet(
			content.toString());

		sb.append(poshiScriptSnippet);

		return sb.toString();
	}

	protected TogglePoshiElement() {
	}

	protected TogglePoshiElement(Element element) {
		super(_ELEMENT_NAME, element);
	}

	protected TogglePoshiElement(List<Attribute> attributes, List<Node> nodes) {
		super(_ELEMENT_NAME, attributes, nodes);
	}

	protected TogglePoshiElement(
		PoshiElement parentPoshiElement, String poshiScript) {

		super(_ELEMENT_NAME, parentPoshiElement, poshiScript);
	}

	@Override
	protected String getBlockName() {
		StringBuilder sb = new StringBuilder();

		sb.append("toggle (\"");
		sb.append(attributeValue("name"));
		sb.append("\")");

		return sb.toString();
	}

	protected String getPoshiScriptKeyword() {
		return getName();
	}

	protected List<String> getPoshiScriptSnippets(String poshiScript) {
		StringBuilder sb = new StringBuilder();

		List<String> poshiScriptSnippets = new ArrayList<>();

		for (String line : poshiScript.split("\n")) {
			String poshiScriptSnippet = sb.toString();

			poshiScriptSnippet = poshiScriptSnippet.trim();

			if (line.startsWith(getPoshiScriptKeyword() + " (") &&
				line.endsWith("{") && (poshiScriptSnippet.length() == 0)) {

				poshiScriptSnippets.add(line);

				continue;
			}

			if (line.endsWith("{") && poshiScriptSnippets.isEmpty()) {
				continue;
			}

			if (isValidPoshiScriptSnippet(poshiScriptSnippet)) {
				poshiScriptSnippets.add(poshiScriptSnippet);

				sb.setLength(0);
			}

			sb.append(line);
			sb.append("\n");
		}

		return poshiScriptSnippets;
	}

	private boolean _isElementType(String poshiScript) {
		poshiScript = poshiScript.trim();

		if (!isBalancedPoshiScript(poshiScript)) {
			return false;
		}

		if (!poshiScript.startsWith("toggle (")) {
			return false;
		}

		if (!poshiScript.endsWith("}")) {
			return false;
		}

		return true;
	}

	private static final String _ELEMENT_NAME = "toggle";

}
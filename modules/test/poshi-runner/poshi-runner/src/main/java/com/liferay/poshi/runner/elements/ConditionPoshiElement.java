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

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * @author Kenji Heigel
 */
public class ConditionPoshiElement extends ExecutePoshiElement {

	@Override
	public PoshiElement clone(Element element) {
		if (isElementType(_ELEMENT_NAME, element)) {
			return new ConditionPoshiElement(element);
		}

		return null;
	}

	@Override
	public PoshiElement clone(
		PoshiElement parentPoshiElement, String poshiScript) {

		if (_isElementType(parentPoshiElement, poshiScript)) {
			return new ConditionPoshiElement(parentPoshiElement, poshiScript);
		}

		return null;
	}

	protected ConditionPoshiElement() {
	}

	protected ConditionPoshiElement(Element element) {
		super(_ELEMENT_NAME, element);
	}

	protected ConditionPoshiElement(
		List<Attribute> attributes, List<Node> nodes) {

		super(_ELEMENT_NAME, attributes, nodes);
	}

	protected ConditionPoshiElement(
		PoshiElement parentPoshiElement, String poshiScript) {

		super(_ELEMENT_NAME, parentPoshiElement, poshiScript);
	}

	@Override
	protected String createPoshiScriptSnippet(List<String> assignments) {
		String poshiScriptSnippet = super.createPoshiScriptSnippet(assignments);

		poshiScriptSnippet = poshiScriptSnippet.trim();

		if (poshiScriptSnippet.endsWith(";")) {
			poshiScriptSnippet = poshiScriptSnippet.substring(
				0, poshiScriptSnippet.length() - 1);
		}

		return poshiScriptSnippet;
	}

	private boolean _isElementType(
		PoshiElement parentPoshiElement, String poshiScript) {

		if (!isConditionValidInParent(parentPoshiElement)) {
			return false;
		}

		if (poshiScript.contains(" && ") || poshiScript.contains(" || ") ||
			poshiScript.startsWith("!") || poshiScript.startsWith("contains") ||
			poshiScript.startsWith("else if (")) {

			return false;
		}

		if (poshiScript.endsWith(")") && !poshiScript.startsWith("isSet(")) {
			return true;
		}

		return false;
	}

	private static final String _ELEMENT_NAME = "condition";

}
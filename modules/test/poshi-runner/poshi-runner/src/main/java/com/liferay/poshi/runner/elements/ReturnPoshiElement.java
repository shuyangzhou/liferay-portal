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

import com.liferay.poshi.runner.util.RegexUtil;
import com.liferay.poshi.runner.util.StringUtil;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * @author Kenji Heigel
 */
public class ReturnPoshiElement extends PoshiElement {

	@Override
	public PoshiElement clone(Element element) {
		if (isElementType(_ELEMENT_NAME, element)) {
			return new ReturnPoshiElement(element);
		}

		return null;
	}

	@Override
	public PoshiElement clone(
		PoshiElement parentPoshiElement, String readableSyntax) {

		if (_isElementType(parentPoshiElement, readableSyntax)) {
			return new ReturnPoshiElement(parentPoshiElement, readableSyntax);
		}

		return null;
	}

	@Override
	public void parseReadableSyntax(String readableSyntax) {
		if (getParent() instanceof ExecutePoshiElement) {
			String returnName = RegexUtil.getGroup(
				readableSyntax, "var\\s*(.+?)\\s*=", 1);

			addAttribute("name", returnName);

			return;
		}

		addAttribute("value", getQuotedContent(readableSyntax));
	}

	@Override
	public String toReadableSyntax() {
		if (getParent() instanceof ExecutePoshiElement) {
			return "";
		}

		return StringUtil.combine(
			"\n\n", getPad(), "return \"", attributeValue("value"), "\";");
	}

	protected ReturnPoshiElement() {
	}

	protected ReturnPoshiElement(Element element) {
		super(_ELEMENT_NAME, element);
	}

	protected ReturnPoshiElement(List<Attribute> attributes, List<Node> nodes) {
		super(_ELEMENT_NAME, attributes, nodes);
	}

	protected ReturnPoshiElement(
		PoshiElement parentPoshiElement, String readableSyntax) {

		super(_ELEMENT_NAME, parentPoshiElement, readableSyntax);
	}

	@Override
	protected String createReadableBlock(String content) {
		StringBuilder sb = new StringBuilder();

		String blockName = getBlockName();
		String pad = getPad();

		sb.append("\n\n");
		sb.append(pad);
		sb.append(blockName);
		sb.append(content.trim());

		return sb.toString();
	}

	@Override
	protected String getBlockName() {
		StringBuilder sb = new StringBuilder();

		sb.append("var ");
		sb.append(attributeValue("name"));
		sb.append(" = ");

		return sb.toString();
	}

	private boolean _isElementType(
		PoshiElement parentPoshiElement, String readableSyntax) {

		readableSyntax = readableSyntax.trim();

		if (parentPoshiElement instanceof ExecutePoshiElement) {
			if (!readableSyntax.startsWith("var")) {
				return false;
			}

			if (isMacroReturnVar(readableSyntax)) {
				return true;
			}

			return false;
		}

		if (readableSyntax.startsWith("return ") &&
			isBalancedReadableSyntax(readableSyntax)) {

			return true;
		}

		return false;
	}

	private static final String _ELEMENT_NAME = "return";

}
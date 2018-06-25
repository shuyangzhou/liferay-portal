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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * @author Kenji Heigel
 */
public class CommandPoshiElement extends PoshiElement {

	@Override
	public PoshiElement clone(Element element) {
		if (isElementType(_ELEMENT_NAME, element)) {
			return new CommandPoshiElement(element);
		}

		return null;
	}

	@Override
	public PoshiElement clone(
		PoshiElement parentPoshiElement, String poshiScript) {

		if (_isElementType(parentPoshiElement, poshiScript)) {
			return new CommandPoshiElement(parentPoshiElement, poshiScript);
		}

		return null;
	}

	@Override
	public void parsePoshiScript(String poshiScript) {
		String blockName = getBlockName(poshiScript);

		Matcher poshiScriptAnnotationMatcher =
			poshiScriptAnnotationPattern.matcher(blockName);

		while (poshiScriptAnnotationMatcher.find()) {
			String annotation = poshiScriptAnnotationMatcher.group();

			if (annotation.startsWith("@description")) {
				add(PoshiNodeFactory.newPoshiNode(this, annotation));

				continue;
			}

			String name = getNameFromAssignment(annotation);
			String value = getQuotedContent(annotation);

			addAttribute(name, value);
		}

		Matcher blockNameMatcher = _blockNamePattern.matcher(blockName);

		if (blockNameMatcher.find()) {
			addAttribute("name", blockNameMatcher.group(3));
		}

		String blockContent = getBlockContent(poshiScript);

		for (String poshiScriptSnippet : getPoshiScriptSnippets(blockContent)) {
			add(PoshiNodeFactory.newPoshiNode(this, poshiScriptSnippet.trim()));
		}
	}

	@Override
	public String toPoshiScript() {
		StringBuilder sb = new StringBuilder();

		for (PoshiElement poshiElement :
				toPoshiElements(elements("description"))) {

			sb.append("\n\t@description = \"");
			sb.append(poshiElement.attributeValue("message"));
			sb.append("\"");
		}

		for (PoshiElementAttribute poshiElementAttribute :
				toPoshiElementAttributes(attributeList())) {

			String name = poshiElementAttribute.getName();

			if (name.equals("name")) {
				continue;
			}

			sb.append("\n\t@");
			sb.append(poshiElementAttribute.toPoshiScript());
		}

		List<String> poshiScriptSnippets = new ArrayList<>();

		for (Node node : Dom4JUtil.toNodeList(content())) {
			if (node instanceof PoshiComment) {
				PoshiComment poshiComment = (PoshiComment)node;

				poshiScriptSnippets.add(poshiComment.toPoshiScript());
			}
			else if (node instanceof PoshiElement) {
				PoshiElement poshiElement = (PoshiElement)node;

				poshiScriptSnippets.add(poshiElement.toPoshiScript());
			}
		}

		sb.append(createPoshiScriptSnippet(poshiScriptSnippets));

		return sb.toString();
	}

	protected CommandPoshiElement() {
	}

	protected CommandPoshiElement(Element element) {
		this(_ELEMENT_NAME, element);
	}

	protected CommandPoshiElement(
		List<Attribute> attributes, List<Node> nodes) {

		this(_ELEMENT_NAME, attributes, nodes);
	}

	protected CommandPoshiElement(
		PoshiElement parentPoshiElement, String poshiScript) {

		this(_ELEMENT_NAME, parentPoshiElement, poshiScript);
	}

	protected CommandPoshiElement(String name, Element element) {
		super(name, element);
	}

	protected CommandPoshiElement(
		String elementName, List<Attribute> attributes, List<Node> nodes) {

		super(elementName, attributes, nodes);
	}

	protected CommandPoshiElement(
		String name, PoshiElement parentPoshiElement, String poshiScript) {

		super(name, parentPoshiElement, poshiScript);
	}

	protected String createPoshiScriptSnippet(List<String> items) {
		StringBuilder sb = new StringBuilder();

		sb.append("\n");

		String pad = getPad();

		sb.append(pad);

		sb.append(getBlockName());
		sb.append(" {");

		for (int i = 0; i < items.size(); i++) {
			String item = items.get(i);

			if (i == 0) {
				if (item.startsWith("\n\n")) {
					item = item.replaceFirst("\n\n", "\n");
				}
			}

			if (isCDATAVar(item)) {
				item = item.replaceFirst("\t", pad + "\t");

				String trimmedItem = item.trim();

				if (!trimmedItem.startsWith("var")) {
					Matcher matcher = nestedVarAssignmentPattern.matcher(item);

					item = matcher.replaceAll("\t$1$2");

					if (item.endsWith(");")) {
						item = item.substring(0, item.length() - 2);

						item = item + "\t);";
					}
				}

				sb.append(item);

				continue;
			}

			if (isMultilinePoshiScriptComment(item)) {
				item = item.replaceFirst("\t", pad + "\t");

				sb.append(item);

				continue;
			}

			item = item.replaceAll("\n", "\n" + pad);

			sb.append(item.replaceAll("\n\t\n", "\n\n"));
		}

		sb.append("\n");
		sb.append(pad);
		sb.append("}");

		return sb.toString();
	}

	@Override
	protected String getBlockName() {
		return getPoshiScriptKeyword() + " " + attributeValue("name");
	}

	protected boolean isCDATAVar(String poshiScript) {
		if (poshiScript.contains("\'\'\'")) {
			return true;
		}

		return false;
	}

	private boolean _isElementType(
		PoshiElement parentPoshiElement, String poshiScript) {

		if (!(parentPoshiElement instanceof DefinitionPoshiElement)) {
			return false;
		}

		return isValidPoshiScriptBlock(_blockNamePattern, poshiScript);
	}

	private static final String _ELEMENT_NAME = "command";

	private static final String _POSHI_SCRIPT_KEYWORD_REGEX = "(macro|test)";

	private static final Pattern _blockNamePattern = Pattern.compile(
		"^" + BLOCK_NAME_ANNOTATION_REGEX + _POSHI_SCRIPT_KEYWORD_REGEX +
			"[\\s]*([\\w]*)",
		Pattern.DOTALL);

}
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

import com.liferay.poshi.runner.util.StringUtil;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Node;

/**
* @author Michael Hashimoto
 */
public interface PoshiNode<A extends Node, B extends PoshiNode<A, B>>
	extends Node {

	public B clone(A node);

	public B clone(String poshiScript);

	public String getPoshiScript();

	public default int getPoshiScriptLineNumber() {
		PoshiElement parentPoshiElement = (PoshiElement)getParent();

		if (parentPoshiElement == null) {
			return 1;
		}

		int line = 0;

		List<PoshiNode> poshiNodes = parentPoshiElement.getPoshiNodes();

		PoshiNode previousPoshiNode = null;

		for (Iterator<PoshiNode> iterator = poshiNodes.iterator();
				iterator.hasNext();) {

			PoshiNode poshiNode = iterator.next();

			if (poshiNode instanceof DescriptionPoshiElement) {
				continue;
			}

			if (poshiNode.equals(this)) {
				if (previousPoshiNode == null) {
					break;
				}

				String previousPoshiScript = previousPoshiNode.getPoshiScript();

				String poshiScript = poshiNode.getPoshiScript();

				return line + previousPoshiNode.getPoshiScriptLineNumber() -
					StringUtil.countStartingNewLines(previousPoshiScript) +
						StringUtil.countStartingNewLines(poshiScript) +
							StringUtil.count(previousPoshiScript, "\n");
			}

			previousPoshiNode = poshiNode;
		}

		line = parentPoshiElement.getPoshiScriptLineNumber();

		String parentPoshiScript = parentPoshiElement.getPoshiScript();

		if (parentPoshiElement.isValidPoshiScriptBlock(
				PoshiElement.poshiScriptBlockNamePattern, parentPoshiScript)) {

			String blockName = parentPoshiElement.getBlockName(
				parentPoshiScript);

			line = line + StringUtil.count(blockName, "\n");
		}

		return line + 1;
	}

	public void parsePoshiScript(String poshiScript);

	public void setPoshiScript(String poshiScript);

	public String toPoshiScript();

}
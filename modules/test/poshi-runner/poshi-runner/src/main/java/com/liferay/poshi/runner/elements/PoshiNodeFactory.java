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

import com.google.common.reflect.ClassPath;

import com.liferay.poshi.runner.util.Dom4JUtil;
import com.liferay.poshi.runner.util.FileUtil;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * @author Kenji Heigel
 */
public abstract class PoshiNodeFactory {

	public static PoshiNode<?, ?> newPoshiNode(Node node) {
		PoshiNode<?, ?> newPoshiNode = null;

		if (node instanceof Comment) {
			newPoshiNode = _newPoshiComment((Comment)node);
		}

		if (node instanceof Element) {
			newPoshiNode = _newPoshiElement((Element)node);
		}

		if (newPoshiNode != null) {
			return newPoshiNode;
		}

		String nodeContent;

		try {
			nodeContent = Dom4JUtil.format(node);
		}
		catch (IOException ioe) {
			nodeContent = node.toString();
		}

		throw new RuntimeException("Unknown node\n" + nodeContent);
	}

	public static PoshiNode<?, ?> newPoshiNode(
		PoshiNode<?, ?> parentPoshiNode, String readableSyntax) {

		PoshiNode<?, ?> newPoshiNode = null;

		newPoshiNode = _newPoshiComment(readableSyntax);

		if (newPoshiNode != null) {
			return newPoshiNode;
		}

		newPoshiNode = _newPoshiElement(
			(PoshiElement)parentPoshiNode, readableSyntax);

		if (newPoshiNode != null) {
			return newPoshiNode;
		}

		throw new RuntimeException(
			"Unknown readable syntax\n" + readableSyntax);
	}

	public static PoshiNode<?, ?> newPoshiNode(
		String content, String fileType) {

		try {
			DefinitionPoshiElement definitionPoshiElement = null;

			for (PoshiElement poshiElement : _poshiElements) {
				if (poshiElement instanceof DefinitionPoshiElement &&
					fileType.equals(poshiElement.getFileType())) {

					definitionPoshiElement =
						(DefinitionPoshiElement)poshiElement;
				}
			}

			if (content.contains("<definition")) {
				Document document = Dom4JUtil.parse(content);

				Element rootElement = document.getRootElement();

				return definitionPoshiElement.clone(rootElement);
			}

			return definitionPoshiElement.clone(content);
		}
		catch (Exception e) {
			System.out.println("Unable to generate the Poshi element");

			e.printStackTrace();
		}

		return null;
	}

	public static PoshiNode<?, ?> newPoshiNodeFromFile(String filePath) {
		try {
			File file = new File(filePath);

			String content = FileUtil.read(file);

			int index = filePath.lastIndexOf(".");

			String fileType = filePath.substring(index + 1);

			return newPoshiNode(content, fileType);
		}
		catch (Exception e) {
			System.out.println("Unable to generate the Poshi element");

			e.printStackTrace();
		}

		return null;
	}

	private static PoshiComment _newPoshiComment(Comment comment) {
		for (PoshiComment poshiComment : _poshiComments) {
			PoshiComment newPoshiComment = poshiComment.clone(comment);

			if (newPoshiComment != null) {
				return newPoshiComment;
			}
		}

		return null;
	}

	private static PoshiComment _newPoshiComment(String readableSyntax) {
		for (PoshiComment poshiComment : _poshiComments) {
			PoshiComment newPoshiComment = poshiComment.clone(readableSyntax);

			if (newPoshiComment != null) {
				return newPoshiComment;
			}
		}

		return null;
	}

	private static PoshiElement _newPoshiElement(Element element) {
		for (PoshiElement poshiElement : _poshiElements) {
			PoshiElement newPoshiElement = poshiElement.clone(element);

			if (newPoshiElement != null) {
				return newPoshiElement;
			}
		}

		return null;
	}

	private static PoshiElement _newPoshiElement(
		PoshiElement parentPoshiElement, String readableSyntax) {

		for (PoshiElement poshiElement : _poshiElements) {
			PoshiElement newPoshiElement = poshiElement.clone(
				parentPoshiElement, readableSyntax);

			if (newPoshiElement != null) {
				return newPoshiElement;
			}
		}

		return null;
	}

	private static final List<PoshiComment> _poshiComments = new ArrayList<>();
	private static final List<PoshiElement> _poshiElements = new ArrayList<>();

	static {
		try {
			ClassPath classPath = ClassPath.from(
				PoshiNode.class.getClassLoader());

			List<Class<?>> poshiElementClasses = new ArrayList<>();

			for (ClassPath.ClassInfo classInfo :
					classPath.getTopLevelClasses(
						"com.liferay.poshi.runner.elements")) {

				poshiElementClasses.add(classInfo.load());
			}

			Collections.sort(
				poshiElementClasses,
				new Comparator<Class<?>>() {

					@Override
					public int compare(Class<?> class1, Class<?> class2) {
						String className1 = class1.getName();
						String className2 = class2.getName();

						return className1.compareTo(className2);
					}

				});

			for (Class<?> clazz : poshiElementClasses) {
				if (Modifier.isAbstract(clazz.getModifiers()) ||
					!(PoshiComment.class.isAssignableFrom(clazz) ||
					PoshiElement.class.isAssignableFrom(clazz))) {

					continue;
				}

				PoshiNode<?, ?> poshiNode =
					(PoshiNode<?, ?>)clazz.newInstance();

				if (poshiNode instanceof PoshiComment) {
					_poshiComments.add((PoshiComment)poshiNode);

					continue;
				}

				if (poshiNode instanceof PoshiElement) {
					_poshiElements.add((PoshiElement)poshiNode);
				}
			}
		}
		catch (IllegalAccessException | InstantiationException |
			   IOException e) {

			throw new RuntimeException(e);
		}
	}

}
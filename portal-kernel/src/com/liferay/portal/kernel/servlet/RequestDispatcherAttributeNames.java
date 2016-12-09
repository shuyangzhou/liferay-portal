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

package com.liferay.portal.kernel.servlet;

import com.liferay.portal.kernel.util.JavaConstants;

import java.util.HashSet;
import java.util.Set;

import javax.portlet.MimeResponse;

/**
 * @author Shuyang Zhou
 */
public class RequestDispatcherAttributeNames {

	public static boolean contains(String name) {
		if ((name == null) || (name.length() < _attributeNamesLowerBound) ||
			(name.length() > _attributeNamesUpperBound) ||
			!_attributeNamesHash[name.length() - _attributeNamesLowerBound]) {

			return false;
		}

		return _attributeNames.contains(name);
	}

	private static final Set<String> _attributeNames = new HashSet<>();
	private static final boolean[] _attributeNamesHash;
	private static final int _attributeNamesLowerBound;
	private static final int _attributeNamesUpperBound;

	static {
		_attributeNames.add(JavaConstants.JAVAX_SERVLET_FORWARD_CONTEXT_PATH);
		_attributeNames.add(JavaConstants.JAVAX_SERVLET_FORWARD_PATH_INFO);
		_attributeNames.add(JavaConstants.JAVAX_SERVLET_FORWARD_QUERY_STRING);
		_attributeNames.add(JavaConstants.JAVAX_SERVLET_FORWARD_REQUEST_URI);
		_attributeNames.add(JavaConstants.JAVAX_SERVLET_FORWARD_SERVLET_PATH);
		_attributeNames.add(JavaConstants.JAVAX_SERVLET_INCLUDE_CONTEXT_PATH);
		_attributeNames.add(JavaConstants.JAVAX_SERVLET_INCLUDE_PATH_INFO);
		_attributeNames.add(JavaConstants.JAVAX_SERVLET_INCLUDE_QUERY_STRING);
		_attributeNames.add(JavaConstants.JAVAX_SERVLET_INCLUDE_REQUEST_URI);
		_attributeNames.add(JavaConstants.JAVAX_SERVLET_INCLUDE_SERVLET_PATH);
		_attributeNames.add(MimeResponse.MARKUP_HEAD_ELEMENT);

		int lowerBound = Integer.MAX_VALUE;
		int upperBound = 0;

		for (String name : _attributeNames) {
			if (name.length() < lowerBound) {
				lowerBound = name.length();
			}

			if (name.length() > upperBound) {
				upperBound = name.length();
			}
		}

		_attributeNamesLowerBound = lowerBound;
		_attributeNamesUpperBound = upperBound;

		_attributeNamesHash = new boolean[upperBound - lowerBound + 1];

		for (String name : _attributeNames) {
			_attributeNamesHash[name.length() - _attributeNamesLowerBound] =
				true;
		}
	}

}
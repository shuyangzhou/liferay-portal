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

package org.eclipse.osgi.internal.serviceregistry;

import org.osgi.framework.Filter;

/**
 * @author Preston Crary
 */
public class ObjectClassUtil {

	public static String getRequiredObjectClass(Filter filter) {
		if (filter == null) {
			return null;
		}

		String filterString = filter.toString();

		int objectClassIndex = filterString.indexOf("(objectClass=");

		while (objectClassIndex >= 0) {
			int depth = 1;

			for (int i = objectClassIndex - 1; i >= 0; i--) {
				char c = filterString.charAt(i);

				if (c == '(') {
					if (depth == 1) {
						c = filterString.charAt(i + 1);

						if (c != '&') {
							depth++;

							break;
						}
					}
					else {
						depth--;
					}
				}
				else if (c == ')') {
					depth++;
				}
			}

			int valueEndIndex = filterString.indexOf(
				')', objectClassIndex + 14);

			if (depth == 1) {
				String objectClass = filterString.substring(
					objectClassIndex + 13, valueEndIndex);

				if (objectClass.indexOf('*') < 0) {
					return objectClass;
				}
			}

			objectClassIndex = filterString.indexOf(
				"(objectClass=", valueEndIndex + 1);
		}

		return null;
	}

	private ObjectClassUtil() {
	}

}
/* @generated */
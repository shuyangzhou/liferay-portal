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

package com.liferay.portal.kernel.template;

import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import java.util.Map;

/**
 * @author Preston Crary
 */
public class SimpleTemplate {

	public SimpleTemplate(Class<?> clazz, String templateName) {
		_tmplContent = StringUtil.read(clazz, templateName);
	}

	public SimpleTemplate(InputStream is) throws IOException {
		_tmplContent = StringUtil.read(is);
	}

	public void processTemplate(Writer writer, Map<String, String> context)
		throws IOException {

		int pos = 0;

		while (true) {
			int x = _tmplContent.indexOf("${", pos);

			int y = _tmplContent.indexOf("}", x + 2);

			if ((x == -1) || (y == -1)) {
				writer.write(_tmplContent, pos, _tmplContent.length() - pos);

				break;
			}
			else {
				writer.write(_tmplContent, pos, x);

				String oldValue = _tmplContent.substring(x + 2, y);

				String newValue = context.get(oldValue);

				if (newValue == null) {
					newValue = oldValue;
				}

				writer.write(newValue);

				pos = y + 1;
			}
		}
	}

	private final String _tmplContent;

}
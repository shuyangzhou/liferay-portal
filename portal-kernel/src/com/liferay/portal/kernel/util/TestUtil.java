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

package com.liferay.portal.kernel.util;

import com.liferay.petra.string.StringBundler;
import java.util.function.Supplier;

/**
 * @author Shuyang Zhou
 */
public class TestUtil {

	public static synchronized void setId(long id) {
		_id = id;
	}

	public static synchronized void appendMessage(long currentId, Supplier<String> messageSupplier) {
		if (_id != null && _id == currentId) {
			_sb.append("\n");
			_sb.append(messageSupplier.get());
		}
	}

	public static synchronized void appendMessage(String message) {
		_sb.append("\n");
		_sb.append(message);
	}

	public static synchronized String captureMessage() {
		_id = null;

		_sb.append("\nTHE END\n");

		String message =  _sb.toString();

		_sb.setIndex(0);

		return message;
	}

	private static Long _id;

	private static final StringBundler _sb = new StringBundler();

}
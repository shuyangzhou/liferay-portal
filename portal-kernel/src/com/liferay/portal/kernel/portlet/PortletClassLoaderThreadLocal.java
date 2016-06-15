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

package com.liferay.portal.kernel.portlet;

import com.liferay.portal.kernel.util.AutoResetThreadLocal;

/**
 * @author Shuyang Zhou
 */
public class PortletClassLoaderThreadLocal {

	public static void setClassLoader(ClassLoader classLoader) {
		_classLoader.set(classLoader);
	}

	public static ClassLoader getClassLoader() {
		return _classLoader.get();
	}

	public static void removeClassLoader() {
		_classLoader.remove();
	}

	private static final ThreadLocal<ClassLoader> _classLoader =
		new AutoResetThreadLocal<>(
			PortletClassLoaderThreadLocal.class + "._classLoader");

}
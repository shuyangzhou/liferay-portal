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

package com.liferay.portal.osgi.web.portlet.container.upload.servlet.request.test;

import com.liferay.portal.kernel.test.util.RandomTestUtil;

/**
 * @author Manuel de la Peña
 */
public class UploadServletRequestTestHelper {

	public static final byte[] BYTES =
		"Enterprise. Open Source. For Life.".getBytes();

	public static String fileNameParameter;

	public static void setUp() throws Exception {
		fileNameParameter = RandomTestUtil.randomString();
	}

}
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.cluster;

import org.junit.Test;

/**
 * @author Tina Tian
 */
public class TestBean {

	public static String TIMESTAMP;

	@Test
	public static String testMethod1(String timeStamp) {
		if (timeStamp.length() == 0) {
			return null;
		}

		TIMESTAMP = timeStamp;

		return timeStamp;
	}

	@Test
	public static Object testMethod2() {
		return new Object();
	}

	@Test
	public static Object testMethod3(String timeStamp) throws Exception {
		throw new Exception(timeStamp);
	}

	public void setKey(String key) {
		_key = key;
	}

	@Test
	public String testMethod4() {
		return _key;
	}

	private String _key;

}
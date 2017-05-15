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

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class StringParserTest {

	@Test
	public void testBuild() {
		StringParser stringParser = StringParser.create(
			"/{nodeId:\\d+}/{title:[^/]+}/");

		Map<String, String> params = new HashMap<>();

		params.put("nodeId", "123");
		params.put("title", "abc");

		Assert.assertEquals("/123/abc/", stringParser.build(params));

		params = new HashMap<>();

		params.put("nodeId", "1a3");
		params.put("title", "abc");

		Assert.assertNull(stringParser.build(params));

		params = new HashMap<>();

		params.put("nodeId", "123");
		params.put("title", "ab/c");

		Assert.assertNull(stringParser.build(params));

		stringParser = StringParser.create("{mvcPathName}");

		params = new HashMap<>();

		params.put("mvcPathName", "test-path");

		Assert.assertEquals("test-path", stringParser.build(params));

		stringParser = StringParser.create("/maximized");

		params = new HashMap<>();

		params.put("nodeId", "123");
		params.put("title", "abc");

		Assert.assertEquals("/maximized", stringParser.build(params));

		stringParser = StringParser.create(
			"/{userIdAndInstanceId}/{type}/{urlTitle:(?!id/)[^/]+}");

		params = new HashMap<>();

		params.put("type", "abc");
		params.put("urlTitle", "xyz");
		params.put("userIdAndInstanceId", "123");

		Assert.assertEquals("/123/abc/xyz", stringParser.build(params));

		params = new HashMap<>();

		params.put("type", "abc");
		params.put("urlTitle", "id/xyz");
		params.put("userIdAndInstanceId", "123");

		Assert.assertNull(stringParser.build(params));

		params = new HashMap<>();

		params.put("type", "abc");
		params.put("urlTitle", "xy/z");
		params.put("userIdAndInstanceId", "123");

		Assert.assertNull(stringParser.build(params));

		stringParser = StringParser.create("/{test}");

		params = new HashMap<>();

		params.put("test", "a.");

		Assert.assertNull(stringParser.build(params));

		stringParser = StringParser.create("/{test:\\d+}");

		params = new HashMap<>();

		params.put("test", "1a");

		Assert.assertNull(stringParser.build(params));
	}

	@Test
	public void testParse() {
		StringParser stringParser = StringParser.create(
			"/{nodeId:\\d+}/{title:[^/]+}/");

		Map<String, String> params = new HashMap<>();

		stringParser.parse("/123/abc/", params);

		Assert.assertEquals(params.toString(), 2, params.size());
		Assert.assertEquals("123", params.get("nodeId"));
		Assert.assertEquals("abc", params.get("title"));

		stringParser = StringParser.create("{mvcPathName}");

		params = new HashMap<>();

		stringParser.parse("test-path", params);

		Assert.assertEquals(params.toString(), 1, params.size());
		Assert.assertEquals("test-path", params.get("mvcPathName"));

		stringParser = StringParser.create("/maximized");

		params = new HashMap<>();

		stringParser.parse("/maximized", params);

		Assert.assertTrue(params.toString(), params.isEmpty());

		stringParser = StringParser.create(
			"/{userIdAndInstanceId}/{type}/{urlTitle:(?!id/)[^/]+}");

		params = new HashMap<>();

		stringParser.parse("/123/abc/xyz", params);

		Assert.assertEquals(params.toString(), 3, params.size());
		Assert.assertEquals("123", params.get("userIdAndInstanceId"));
		Assert.assertEquals("abc", params.get("type"));
		Assert.assertEquals("xyz", params.get("urlTitle"));

		stringParser = StringParser.create(
			"/{userIdAndInstanceId}/{type}/{urlTitle:(?!id/)[^/]+}");

		params = new HashMap<>();

		stringParser.parse("/123/abc/id/", params);

		Assert.assertTrue(params.toString(), params.isEmpty());

		stringParser = StringParser.create("/{test}");

		params = new HashMap<>();

		stringParser.parse("/a.", params);

		Assert.assertTrue(params.toString(), params.isEmpty());

		stringParser = StringParser.create("/{test:\\d+}");

		params = new HashMap<>();

		stringParser.parse("/1a", params);

		Assert.assertTrue(params.toString(), params.isEmpty());
	}

}
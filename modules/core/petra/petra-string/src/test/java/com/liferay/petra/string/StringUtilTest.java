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

package com.liferay.petra.string;

import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Alexander Chow
 * @author Shuyang Zhou
 * @author Hugo Huijser
 */
public class StringUtilTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		new CodeCoverageAssertor();

	@Test
	public void testMerge() {
		Assert.assertEquals(
			"1,2,3", StringUtil.merge(new String[] {"1", " 2 ", "3"}));
		Assert.assertEquals("1", StringUtil.merge(new String[] {"1"}));
		Assert.assertEquals("", StringUtil.merge(new String[0]));
		Assert.assertEquals(
			"true,false,true",
			StringUtil.merge(new boolean[] {true, false, true}));
		Assert.assertEquals("true", StringUtil.merge(new boolean[] {true}));
		Assert.assertEquals(
			"1.1,2.2,3.3", StringUtil.merge(new double[] {1.1, 2.2, 3.3}));
		Assert.assertEquals("1.1", StringUtil.merge(new double[] {1.1}));
		Assert.assertEquals("1,2,3", StringUtil.merge(new int[] {1, 2, 3}));
		Assert.assertEquals("1", StringUtil.merge(new int[] {1}));
		Assert.assertEquals("1,2,3", StringUtil.merge(new long[] {1, 2, 3}));
		Assert.assertEquals("1", StringUtil.merge(new long[] {1}));
	}

	@Test
	public void testReplaceCharStringArrays() {
		Assert.assertEquals(
			"Hello World,HELLO WORLD,Hello World",
			StringUtil.replace(
				"Hello World/HI WORLD/Hello World",
				new char[] {CharPool.SLASH, CharPool.UPPER_CASE_I},
				new String[] {StringPool.COMMA, "ELLO"}));
		Assert.assertEquals(
			"Hello World,HELLO WORLD,Hello World",
			StringUtil.replace(
				"Hello World/HI WORLD/Hello World",
				new char[] {
					CharPool.SLASH, CharPool.SLASH, CharPool.UPPER_CASE_I,
					CharPool.UPPER_CASE_I
				},
				new String[] {
					StringPool.COMMA, StringPool.COMMA, "ELLO", "ELLO"
				}));
	}

	@Test
	public void testReplaceEmptyString() {
		Assert.assertEquals(
			"Hello World HELLO WORLD Hello World",
			StringUtil.replace(
				"Hello World HELLO WORLD Hello World", "", "Aloha"));
	}

	@Test
	public void testReplaceMap() {
		Map<String, String> map = new HashMap<>();

		map.put("Hallo", "Hello");
		map.put("Wirld", "World");

		Assert.assertEquals(
			"Hello World",
			StringUtil.replace("AB Hallo CD AB Wirld CD", "AB ", " CD", map));
		Assert.assertEquals(
			"Hello World",
			StringUtil.replace(
				"Hello World", StringPool.BLANK, StringPool.BLANK, map));
	}

	@Test
	public void testReplaceSpaceString() {
		Assert.assertEquals(
			"HelloWorldHELLOWORLDHelloWorld",
			StringUtil.replace(
				"Hello World HELLO WORLD Hello World", " ", StringPool.BLANK));
	}

	@Test
	public void testReplaceString() {
		Assert.assertEquals(
			"Aloha World HELLO WORLD Aloha World",
			StringUtil.replace(
				"Hello World HELLO WORLD Hello World", "Hello", "Aloha"));
	}

	@Test
	public void testReplaceStringArray() {
		Assert.assertEquals(
			"Aloha World ALOHA WORLD Aloha World",
			StringUtil.replace(
				"Hello World HELLO WORLD Hello World",
				new String[] {"Hello", "HELLO"},
				new String[] {"Aloha", "ALOHA"}));
	}

	@Test
	public void testSplit() {
		Assert.assertArrayEquals(
			new String[] {"Alice", "Bob", "Charlie"},
			StringUtil.split("Alice,Bob,Charlie"));
		Assert.assertArrayEquals(
			new String[] {"First", "Second", "Third"},
			StringUtil.split("First;Second;Third", ';'));
		Assert.assertArrayEquals(
			new String[] {"One", "Two", "Three"},
			StringUtil.split("OnexTwoxThree", 'x'));
	}

}
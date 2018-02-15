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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Alexander Chow
 * @author Shuyang Zhou
 * @author Hugo Huijser
 * @author Preston Crary
 */
public class StringUtilTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		new CodeCoverageAssertor() {

			@Override
			public void appendAssertClasses(List<Class<?>> assertClasses) {
				assertClasses.add(CharPool.class);
				assertClasses.add(StringPool.class);
			}

		};

	@Test
	public void testConstructors() {
		new CharPool();
		new StringPool();
		new StringUtil();
	}

	@Test
	public void testExtractDigits() {
		Assert.assertEquals(StringPool.BLANK, StringUtil.extractDigits(null));
		Assert.assertEquals(
			StringPool.BLANK, StringUtil.extractDigits(StringPool.BLANK));
		Assert.assertEquals(StringPool.BLANK, StringUtil.extractDigits("abc"));
		Assert.assertEquals("123", StringUtil.extractDigits("a1b2c3"));
		Assert.assertEquals(
			"1234567890", StringUtil.extractDigits("1234567890"));
		Assert.assertEquals(
			"1234567890", StringUtil.extractDigits("123-456-7890"));
		Assert.assertEquals(
			"1234567890", StringUtil.extractDigits("(123) 456-7890"));
	}

	@Test
	public void testIsDigit() {
		char[] digitChars = {
			CharPool.NUMBER_0, CharPool.NUMBER_1, CharPool.NUMBER_2,
			CharPool.NUMBER_3, CharPool.NUMBER_4, CharPool.NUMBER_5,
			CharPool.NUMBER_6, CharPool.NUMBER_7, CharPool.NUMBER_8,
			CharPool.NUMBER_9
		};

		for (char c : digitChars) {
			Assert.assertTrue(StringUtil.isDigit(c));
		}

		char[] nonDigitChars = {
			CharPool.SPACE, CharPool.COMMA, CharPool.LOWER_CASE_A,
			CharPool.UPPER_CASE_A, CharPool.LOWER_CASE_B, CharPool.UPPER_CASE_B,
			CharPool.LOWER_CASE_C, CharPool.UPPER_CASE_C, CharPool.EXCLAMATION,
			CharPool.AT
		};

		for (char c : nonDigitChars) {
			Assert.assertFalse(StringUtil.isDigit(c));
		}
	}

	@Test
	public void testMerge() {
		Assert.assertEquals(null, StringUtil.merge(null, StringPool.BLANK));

		Object[] emptyArray = new String[0];

		Assert.assertEquals(
			StringPool.BLANK, StringUtil.merge(emptyArray, StringPool.COMMA));

		Object[] unaryArray = {"alphabet"};

		Assert.assertEquals(
			"alphabet", StringUtil.merge(unaryArray, StringPool.COMMA));

		Object[] abcArray = {"abc", "Abc", "ABC"};

		Assert.assertEquals(
			"abc,Abc,ABC", StringUtil.merge(abcArray, StringPool.COMMA));
	}

	@Test
	public void testRemoveChars() {
		Assert.assertEquals(null, StringUtil.removeChars(null, null));
		Assert.assertEquals(
			null, StringUtil.removeChars(null, CharPool.LOWER_CASE_A));
		Assert.assertEquals("abc", StringUtil.removeChars("abc", null));
		Assert.assertEquals(
			StringPool.BLANK,
			StringUtil.removeChars("a", CharPool.LOWER_CASE_A));

		char[] abcArray = {
			CharPool.LOWER_CASE_A, CharPool.LOWER_CASE_B, CharPool.LOWER_CASE_C
		};

		Assert.assertEquals(
			StringPool.BLANK, StringUtil.removeChars("a", abcArray));
		Assert.assertEquals(
			StringPool.BLANK, StringUtil.removeChars("abc", abcArray));
		Assert.assertEquals("d", StringUtil.removeChars("abcd", abcArray));
		Assert.assertEquals(
			"deed", StringUtil.removeChars("abcdeedcba", abcArray));

		char[] upperAbcArray = {
			CharPool.UPPER_CASE_A, CharPool.UPPER_CASE_B, CharPool.UPPER_CASE_C
		};

		Assert.assertEquals(
			"alphabet", StringUtil.removeChars("alphabet", upperAbcArray));
	}

	@Test
	public void testSplit() {
		Assert.assertSame(Collections.emptyList(), StringUtil.split(null));
		Assert.assertSame(
			Collections.emptyList(), StringUtil.split(StringPool.BLANK));
		Assert.assertSame(
			Collections.emptyList(), StringUtil.split(StringPool.SPACE));

		Assert.assertEquals(
			Collections.<String>emptyList(),
			StringUtil.split(StringPool.COMMA));
		Assert.assertEquals(
			Collections.<String>emptyList(), StringUtil.split(",,,"));

		Assert.assertEquals(
			Collections.singletonList("test"), StringUtil.split("test"));
		Assert.assertEquals(
			Collections.singletonList("test"), StringUtil.split("test,"));
		Assert.assertEquals(
			Collections.singletonList("test"), StringUtil.split(",test"));

		Assert.assertEquals(
			Arrays.asList("test1", "test2"),
			StringUtil.split("test1-test2", CharPool.DASH));
	}

}
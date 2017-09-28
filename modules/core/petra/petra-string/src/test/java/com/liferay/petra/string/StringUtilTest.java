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
import java.util.Collection;
import java.util.Collections;

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
		new CodeCoverageAssertor();

	@Test
	public void testConstructor() throws Exception {
		new StringUtil();
	}

	@Test
	public void testMergeEmpty() {
		Assert.assertSame(StringPool.BLANK, StringUtil.merge(new boolean[0]));
		Assert.assertSame(StringPool.BLANK, StringUtil.merge(new char[0]));
		Assert.assertSame(
			StringPool.BLANK, StringUtil.merge(Collections.emptyList()));
		Assert.assertSame(StringPool.BLANK, StringUtil.merge(new double[0]));
		Assert.assertSame(StringPool.BLANK, StringUtil.merge(new float[0]));
		Assert.assertSame(StringPool.BLANK, StringUtil.merge(new int[0]));
		Assert.assertSame(StringPool.BLANK, StringUtil.merge(new long[0]));
		Assert.assertSame(StringPool.BLANK, StringUtil.merge(new short[0]));
		Assert.assertSame(StringPool.BLANK, StringUtil.merge(new String[0]));
	}

	@Test
	public void testMergeNull() {
		Assert.assertNull(StringUtil.merge((boolean[])null));
		Assert.assertNull(StringUtil.merge((char[])null));
		Assert.assertNull(StringUtil.merge((Collection<?>)null));
		Assert.assertNull(StringUtil.merge((double[])null));
		Assert.assertNull(StringUtil.merge((float[])null));
		Assert.assertNull(StringUtil.merge((int[])null));
		Assert.assertNull(StringUtil.merge((long[])null));
		Assert.assertNull(StringUtil.merge((short[])null));
		Assert.assertNull(StringUtil.merge((String[])null));
	}

	@Test
	public void testMergeOne() {
		Assert.assertEquals("true", StringUtil.merge(new boolean[] {true}));
		Assert.assertEquals(
			"A", StringUtil.merge(new char[] {CharPool.UPPER_CASE_A}));
		Assert.assertEquals(
			"test", StringUtil.merge(Collections.singletonList("test")));
		Assert.assertEquals("3.14", StringUtil.merge(new double[] {3.14}));
		Assert.assertEquals("3.14", StringUtil.merge(new float[] {3.14F}));
		Assert.assertEquals("10", StringUtil.merge(new int[] {10}));
		Assert.assertEquals("10", StringUtil.merge(new long[] {10L}));
		Assert.assertEquals("10", StringUtil.merge(new short[] {10}));
		Assert.assertEquals("test", StringUtil.merge(new String[] {"test"}));
	}

	@Test
	public void testMergeTwo() {
		Assert.assertEquals(
			"true,false", StringUtil.merge(new boolean[] {true, false}));
		Assert.assertEquals(
			"A,B",
			StringUtil.merge(
				new char[] {CharPool.UPPER_CASE_A, CharPool.UPPER_CASE_B}));
		Assert.assertEquals(
			"test,test2", StringUtil.merge(Arrays.asList("test", "test2")));
		Assert.assertEquals(
			"3.14,2.72", StringUtil.merge(new double[] {3.14, 2.72}));
		Assert.assertEquals(
			"3.14,2.72", StringUtil.merge(new float[] {3.14F, 2.72F}));
		Assert.assertEquals("10,-10", StringUtil.merge(new int[] {10, -10}));
		Assert.assertEquals("10,-10", StringUtil.merge(new long[] {10L, -10L}));
		Assert.assertEquals("10,-10", StringUtil.merge(new short[] {10, -10}));
		Assert.assertEquals(
			"test,test2", StringUtil.merge(new String[] {"test", "test2"}));
	}

	@Test
	public void testReplace() {
		Assert.assertEquals(
			"b.b",
			StringUtil.replace(
				"a.a", CharPool.LOWER_CASE_A, CharPool.LOWER_CASE_B));
		Assert.assertEquals(
			".b.b.",
			StringUtil.replace(
				".a.a.", CharPool.LOWER_CASE_A, CharPool.LOWER_CASE_B));

		Assert.assertEquals(
			"beta.beta",
			StringUtil.replace("a.a", CharPool.LOWER_CASE_A, "beta"));
		Assert.assertEquals(
			".beta.beta.",
			StringUtil.replace(".a.a.", CharPool.LOWER_CASE_A, "beta"));

		Assert.assertEquals(
			"b.b",
			StringUtil.replace(
				"a.a", new char[] {CharPool.LOWER_CASE_A},
				new char[] {CharPool.LOWER_CASE_B}));
		Assert.assertEquals(
			".b.b.",
			StringUtil.replace(
				".a.a.", new char[] {CharPool.LOWER_CASE_A},
				new char[] {CharPool.LOWER_CASE_B}));

		Assert.assertEquals(
			"beta.beta",
			StringUtil.replace(
				"a.a", new char[] {CharPool.LOWER_CASE_A},
				new String[] {"beta"}));
		Assert.assertEquals(
			".beta.beta.",
			StringUtil.replace(
				".a.a.", new char[] {CharPool.LOWER_CASE_A},
				new String[] {"beta"}));

		Assert.assertEquals(
			"beta.beta",
			StringUtil.replace(
				"alpha.alpha", new String[] {"alpha"}, new String[] {"beta"}));
		Assert.assertEquals(
			".beta.beta.",
			StringUtil.replace(
				".alpha.alpha.", new String[] {"alpha"},
				new String[] {"beta"}));

		Assert.assertEquals(
			"beta.beta", StringUtil.replace("alpha.alpha", "alpha", "beta"));
		Assert.assertEquals(
			".beta.beta.",
			StringUtil.replace(".alpha.alpha.", "alpha", "beta"));
	}

	@Test
	public void testReplaceEmpty() {
		Assert.assertSame(
			StringPool.BLANK,
			StringUtil.replace(
				StringPool.BLANK, CharPool.UPPER_CASE_A,
				CharPool.UPPER_CASE_B));
		Assert.assertSame(
			StringPool.BLANK,
			StringUtil.replace(
				StringPool.BLANK, CharPool.UPPER_CASE_A, "beta"));
		Assert.assertSame(
			StringPool.BLANK,
			StringUtil.replace(
				StringPool.BLANK, new char[] {CharPool.UPPER_CASE_A},
				new char[] {CharPool.UPPER_CASE_B}));
		Assert.assertSame(
			StringPool.BLANK,
			StringUtil.replace(
				StringPool.BLANK, new char[] {CharPool.UPPER_CASE_A},
				new String[] {"beta"}));
		Assert.assertSame(
			StringPool.BLANK,
			StringUtil.replace(
				StringPool.BLANK, new String[] {"alpha"},
				new String[] {"beta"}));
		Assert.assertSame(
			StringPool.BLANK,
			StringUtil.replace(StringPool.BLANK, "alpha", "beta"));

		String s = "A";

		Assert.assertSame(s, StringUtil.replace(s, StringPool.BLANK, "beta"));
	}

	@Test
	public void testReplaceMap() {
		Assert.assertEquals(
			"beta",
			StringUtil.replace(
				"${alpha}", "${", "}",
				Collections.singletonMap("alpha", "beta")));
		Assert.assertEquals(
			"alpha}",
			StringUtil.replace(
				"alpha}", "${", "}",
				Collections.singletonMap("alpha", "beta")));
		Assert.assertEquals(
			"${alpha",
			StringUtil.replace(
				"${alpha", "${", "}",
				Collections.singletonMap("alpha", "beta")));
	}

	@Test
	public void testReplaceMapEmpty() {
		Assert.assertEquals(
			StringPool.BLANK,
			StringUtil.replace(
				StringPool.BLANK, "${", "}", Collections.emptyMap()));
		Assert.assertEquals(
			"test",
			StringUtil.replace(
				"test", StringPool.BLANK, "}", Collections.emptyMap()));
		Assert.assertEquals(
			"test",
			StringUtil.replace(
				"test", "${", StringPool.BLANK, Collections.emptyMap()));
		Assert.assertEquals(
			"test",
			StringUtil.replace("test", "${", "}", Collections.emptyMap()));
	}

	@Test
	public void testReplaceMapNull() {
		Assert.assertNull(
			StringUtil.replace(null, "${", "}", Collections.emptyMap()));
		Assert.assertEquals(
			"${test}",
			StringUtil.replace("${test}", null, "}", Collections.emptyMap()));
		Assert.assertEquals(
			"${test}",
			StringUtil.replace("${test}", "${", null, Collections.emptyMap()));
		Assert.assertEquals(
			"${test}", StringUtil.replace("${test}", "${", "}", null));
		Assert.assertEquals(
			"test",
			StringUtil.replace(
				"${test}", "${", "}", Collections.singletonMap("test", null)));
	}

	@Test
	public void testReplaceNull() {
		String s = "alpha";

		Assert.assertNull(
			StringUtil.replace(
				null, CharPool.UPPER_CASE_A, CharPool.UPPER_CASE_B));

		Assert.assertNull(
			StringUtil.replace(null, CharPool.UPPER_CASE_A, "beta"));
		Assert.assertNull(StringUtil.replace(s, CharPool.UPPER_CASE_A, null));

		Assert.assertNull(
			StringUtil.replace(
				null, new char[] {CharPool.UPPER_CASE_A},
				new char[] {CharPool.UPPER_CASE_B}));
		Assert.assertNull(
			StringUtil.replace(s, null, new char[] {CharPool.UPPER_CASE_B}));
		Assert.assertNull(
			StringUtil.replace(
				s, new char[] {CharPool.UPPER_CASE_A}, (char[])null));

		Assert.assertNull(
			StringUtil.replace(null, new char[] {'A'}, new String[] {"beta"}));
		Assert.assertNull(
			StringUtil.replace(s, (char[])null, new String[] {"beta"}));
		Assert.assertNull(
			StringUtil.replace(s, new char[] {'A'}, (String[])null));

		Assert.assertNull(
			StringUtil.replace(
				null, new String[] {"alpha"}, new String[] {"beta"}));
		Assert.assertNull(
			StringUtil.replace(s, (String[])null, new String[] {"beta"}));
		Assert.assertNull(StringUtil.replace(s, new String[] {"alpha"}, null));

		Assert.assertNull(StringUtil.replace(null, "alpha", "beta"));
		Assert.assertSame(s, StringUtil.replace(s, null, "beta"));
		Assert.assertSame(
			StringPool.BLANK, StringUtil.replace("alpha", "alpha", null));
	}

	@Test
	public void testReplaceOneChange() {
		Assert.assertEquals(
			"B",
			StringUtil.replace(
				"A", CharPool.UPPER_CASE_A, CharPool.UPPER_CASE_B));
		Assert.assertEquals(
			"beta", StringUtil.replace("A", CharPool.UPPER_CASE_A, "beta"));
		Assert.assertEquals(
			"B",
			StringUtil.replace(
				"A", new char[] {CharPool.UPPER_CASE_A},
				new char[] {CharPool.UPPER_CASE_B}));
		Assert.assertEquals(
			"beta",
			StringUtil.replace(
				"A", new char[] {CharPool.UPPER_CASE_A},
				new String[] {"beta"}));
		Assert.assertEquals(
			"beta",
			StringUtil.replace(
				"alpha", new String[] {"alpha"}, new String[] {"beta"}));
		Assert.assertEquals(
			"beta", StringUtil.replace("alpha", "alpha", "beta"));
	}

	@Test
	public void testReplaceOneNoChange() {
		String s = "beta";

		Assert.assertSame(
			s,
			StringUtil.replace(
				s, CharPool.UPPER_CASE_A, CharPool.UPPER_CASE_B));
		Assert.assertSame(
			s, StringUtil.replace(s, CharPool.UPPER_CASE_A, "beta"));
		Assert.assertSame(
			s,
			StringUtil.replace(
				s, new char[] {CharPool.UPPER_CASE_A},
				new char[] {CharPool.UPPER_CASE_B}));
		Assert.assertSame(
			s,
			StringUtil.replace(
				s, new char[] {CharPool.UPPER_CASE_A}, new String[] {"beta"}));
		Assert.assertSame(
			s,
			StringUtil.replace(
				s, new String[] {"alpha"}, new String[] {"beta"}));
		Assert.assertSame(s, StringUtil.replace(s, "alpha", "beta"));
	}

	@Test
	public void testReplaceWrongNumberOfReplacements() {
		String s = "A";

		Assert.assertSame(
			s,
			StringUtil.replace(
				s, new char[] {CharPool.UPPER_CASE_A, CharPool.UPPER_CASE_A},
				new char[] {CharPool.UPPER_CASE_B}));
		Assert.assertSame(
			s,
			StringUtil.replace(
				s, new char[] {CharPool.UPPER_CASE_A, CharPool.UPPER_CASE_A},
				new String[] {"B"}));
		Assert.assertSame(
			s,
			StringUtil.replace(s, new String[] {"A", "A"}, new String[] {"B"}));
	}

	@Test
	public void testSplitEmpty() {
		Assert.assertArrayEquals(
			new String[0], StringUtil.split(StringPool.BLANK));
		Assert.assertArrayEquals(
			new String[0], StringUtil.split(StringPool.THREE_SPACES));

		Assert.assertArrayEquals(
			new String[0],
			StringUtil.split(StringPool.BLANK, StringPool.BLANK));
		Assert.assertArrayEquals(
			new String[0],
			StringUtil.split(StringPool.THREE_SPACES, StringPool.BLANK));
		Assert.assertArrayEquals(
			new String[0],
			StringUtil.split(
				StringPool.TRIPLE_PERIOD, StringPool.TRIPLE_PERIOD));
	}

	@Test
	public void testSplitNull() {
		Assert.assertArrayEquals(new String[0], StringUtil.split(null));

		Assert.assertArrayEquals(new String[0], StringUtil.split(null, null));
		Assert.assertArrayEquals(new String[0], StringUtil.split("test", null));
	}

	@Test
	public void testSplitOne() {
		Assert.assertArrayEquals(
			new String[] {"test"}, StringUtil.split("test"));
		Assert.assertArrayEquals(
			new String[] {"test"}, StringUtil.split("test,"));

		Assert.assertArrayEquals(
			new String[] {"test...test2"},
			StringUtil.split("test...test2", StringPool.COMMA));
		Assert.assertArrayEquals(
			new String[] {"test...test2"},
			StringUtil.split("test...test2", StringPool.COMMA_AND_SPACE));
		Assert.assertArrayEquals(
			new String[] {"test"},
			StringUtil.split("test", StringPool.TRIPLE_PERIOD));
		Assert.assertArrayEquals(
			new String[] {"test"},
			StringUtil.split("test...", StringPool.TRIPLE_PERIOD));
	}

	@Test
	public void testSplitTwo() {
		Assert.assertArrayEquals(
			new String[] {"test", "test2"}, StringUtil.split("test,test2"));

		Assert.assertArrayEquals(
			new String[] {"test", "test2"},
			StringUtil.split("test...test2", StringPool.TRIPLE_PERIOD));
	}

	@Test
	public void testToHexString() {
		Assert.assertEquals("a", StringUtil.toHexString(Integer.valueOf(10)));
		Assert.assertEquals("40", StringUtil.toHexString(Integer.valueOf(64)));
		Assert.assertEquals(
			"7fffffff", StringUtil.toHexString(Integer.MAX_VALUE));
		Assert.assertEquals(
			"80000000", StringUtil.toHexString(Integer.MIN_VALUE));

		Assert.assertEquals("b", StringUtil.toHexString(Long.valueOf(11)));
		Assert.assertEquals("41", StringUtil.toHexString(Long.valueOf(65)));
		Assert.assertEquals(
			"7fffffffffffffff", StringUtil.toHexString(Long.MAX_VALUE));
		Assert.assertEquals(
			"8000000000000000", StringUtil.toHexString(Long.MIN_VALUE));

		Assert.assertSame("test", StringUtil.toHexString("test"));
	}

}
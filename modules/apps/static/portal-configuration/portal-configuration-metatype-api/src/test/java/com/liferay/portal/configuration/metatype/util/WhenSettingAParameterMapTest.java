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

package com.liferay.portal.configuration.metatype.util;

import com.liferay.portal.kernel.module.configuration.ConfigurationException;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jorge Ferrer
 */
public class WhenSettingAParameterMapTest {

	@Before
	public void setUp() throws ConfigurationException {
		ParameterMapUtilTestHelper.TestBean testBean =
			ParameterMapUtilTestHelper.getTestBean();

		Map<String, String[]> parameterMap = new HashMap<>();

		parameterMap.put("testBoolean1", new String[] {"false"});
		parameterMap.put(
			"testString1",
			new String[] {ParameterMapUtilTestHelper.PARAMETER_MAP_STRING});
		parameterMap.put(
			"testStringArray1",
			ParameterMapUtilTestHelper.PARAMETER_MAP_STRING_ARRAY);

		_testBean = ParameterMapUtil.setParameterMap(
			ParameterMapUtilTestHelper.TestBean.class, testBean, parameterMap);
	}

	@Test
	public void testValuesInTheParameterMapAreReadFirst() throws Exception {
		Assert.assertEquals(false, _testBean.testBoolean1());
		Assert.assertEquals(
			ParameterMapUtilTestHelper.PARAMETER_MAP_STRING,
			_testBean.testString1());
		Assert.assertArrayEquals(
			ParameterMapUtilTestHelper.PARAMETER_MAP_STRING_ARRAY,
			_testBean.testStringArray1());
	}

	@Test
	public void testValuesNotInTheParameterMapAreReadFromBean()
		throws Exception {

		Assert.assertEquals(true, _testBean.testBoolean2());
		Assert.assertEquals(
			ParameterMapUtilTestHelper.TEST_BEAN_STRING,
			_testBean.testString2());
		Assert.assertArrayEquals(
			ParameterMapUtilTestHelper.TEST_BEAN_STRING_ARRAY,
			_testBean.testStringArray2());
	}

	private ParameterMapUtilTestHelper.TestBean _testBean;

}
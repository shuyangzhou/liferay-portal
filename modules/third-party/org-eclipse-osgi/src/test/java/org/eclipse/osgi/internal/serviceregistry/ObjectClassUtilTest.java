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

package org.eclipse.osgi.internal.serviceregistry;

import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.lang.reflect.Constructor;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;

/**
 * @author Preston Crary
 */
public class ObjectClassUtilTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		new CodeCoverageAssertor();

	@Test
	public void testConstructor() throws Exception {
		Constructor<ObjectClassUtil> constructor =
			ObjectClassUtil.class.getDeclaredConstructor();

		constructor.setAccessible(true);

		constructor.newInstance();
	}

	@Test
	public void testObjectName() throws InvalidSyntaxException {
		Assert.assertNull(ObjectClassUtil.getRequiredObjectClass(null));

		Assert.assertEquals(
			"A",
			ObjectClassUtil.getRequiredObjectClass(
				FrameworkUtil.createFilter("(objectClass=A)")));
		Assert.assertEquals(
			" A ",
			ObjectClassUtil.getRequiredObjectClass(
				FrameworkUtil.createFilter("( objectClass = A )")));
		Assert.assertEquals(
			ObjectClassUtil.class.getName(),
			ObjectClassUtil.getRequiredObjectClass(
				FrameworkUtil.createFilter(
					"(objectClass=" + ObjectClassUtil.class.getName() + ")")));

		Assert.assertEquals(
			"A",
			ObjectClassUtil.getRequiredObjectClass(
				FrameworkUtil.createFilter(
					"(&(property=value)(objectClass=A))")));
		Assert.assertEquals(
			"A",
			ObjectClassUtil.getRequiredObjectClass(
				FrameworkUtil.createFilter(
					"(&(property1=value1)(&(property2=value2)" +
						"(objectClass=A)))")));
		Assert.assertEquals(
			"A",
			ObjectClassUtil.getRequiredObjectClass(
				FrameworkUtil.createFilter(
					"(&(objectClass=A)(objectClass=B))")));

		Assert.assertNull(
			ObjectClassUtil.getRequiredObjectClass(
				FrameworkUtil.createFilter("(property=value)")));
		Assert.assertNull(
			ObjectClassUtil.getRequiredObjectClass(
				FrameworkUtil.createFilter("(objectClass=*)")));
		Assert.assertNull(
			ObjectClassUtil.getRequiredObjectClass(
				FrameworkUtil.createFilter(
					"(|(objectClass=A)(objectClass=B))")));

		Assert.assertEquals(
			"B",
			ObjectClassUtil.getRequiredObjectClass(
				FrameworkUtil.createFilter(
					"(&(objectClass=A*)(objectClass=B))")));
		Assert.assertEquals(
			"B",
			ObjectClassUtil.getRequiredObjectClass(
				FrameworkUtil.createFilter(
					"(&(property1=value1)(|(property2=value2)(objectClass=A))" +
						"(objectClass=B))")));
	}

}
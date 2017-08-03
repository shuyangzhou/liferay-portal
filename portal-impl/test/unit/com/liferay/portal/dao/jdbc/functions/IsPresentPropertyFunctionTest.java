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

package com.liferay.portal.dao.jdbc.functions;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class IsPresentPropertyFunctionTest {

	@Test
	public void testIsNotPresent() {
		IsPresentPropertyFunction function = new IsPresentPropertyFunction(
			"key");

		String[] keys = {
			"acquireIncrement", "acquireRetryAttempts", "acquireRetryDelay",
			"connectionCustomizerClassName", "idleConnectionTestPeriod",
			"initialPoolSize", "maxIdleTime", "maxPoolSize", "minPoolSize",
			"numHelperThreads", "preferredTestQuery"
		};

		Assert.assertFalse(function.apply(keys));
	}

	@Test
	public void testIsPresent() {
		IsPresentPropertyFunction function = new IsPresentPropertyFunction(
			"maxPoolSize");

		String[] keys = {
			"acquireIncrement", "acquireRetryAttempts", "acquireRetryDelay",
			"connectionCustomizerClassName", "idleConnectionTestPeriod",
			"initialPoolSize", "maxIdleTime", "maxPoolSize", "minPoolSize",
			"numHelperThreads", "preferredTestQuery"
		};

		Assert.assertTrue(function.apply(keys));
	}

	@Test
	public void testIsPresentUpperCase() {
		IsPresentPropertyFunction function = new IsPresentPropertyFunction(
			"MAXPOOLSIZE");

		String[] keys = {
			"acquireIncrement", "acquireRetryAttempts", "acquireRetryDelay",
			"connectionCustomizerClassName", "idleConnectionTestPeriod",
			"initialPoolSize", "maxIdleTime", "maxPoolSize", "minPoolSize",
			"numHelperThreads", "preferredTestQuery"
		};

		Assert.assertTrue(function.apply(keys));
	}

}
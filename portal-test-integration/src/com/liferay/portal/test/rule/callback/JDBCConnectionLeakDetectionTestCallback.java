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

package com.liferay.portal.test.rule.callback;

import com.liferay.portal.connection.LiferayDataSourceProxy;
import com.liferay.portal.kernel.test.rule.callback.BaseTestCallback;

import org.junit.Assert;
import org.junit.runner.Description;

/**
 * @author Tom Wang
 */
public class JDBCConnectionLeakDetectionTestCallback
	extends BaseTestCallback<Void, Void> {

	public static final JDBCConnectionLeakDetectionTestCallback INSTANCE =
		new JDBCConnectionLeakDetectionTestCallback();

	@Override
	public void afterClass(Description description, Void v) {
		int count = LiferayDataSourceProxy.getAndResetOpenConnectionCount();

		Assert.assertEquals("Leaked connection count: " + count, 0, count);
	}

	@Override
	public Void beforeClass(Description description) {
		LiferayDataSourceProxy.setTestThreadThreadLocal(true);

		LiferayDataSourceProxy.getAndResetOpenConnectionCount();

		return null;
	}

}
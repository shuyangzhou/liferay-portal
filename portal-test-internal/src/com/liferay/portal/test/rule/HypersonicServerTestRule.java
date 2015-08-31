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

package com.liferay.portal.test.rule;

import com.liferay.portal.kernel.test.rule.BaseTestRule;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.test.rule.callback.HypersonicServerTestCallback;
import com.liferay.portal.util.PropsImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author William Newbury
 */
public class HypersonicServerTestRule extends BaseTestRule<Object, Object> {

	public HypersonicServerTestRule(String databaseName) {
		super(new HypersonicServerTestCallback(databaseName, _HYPERSONIC));

		_databaseURL = "jdbc:hsqldb:hsql://localhost/" + databaseName;
	}

	public List<String> getJdbcProperties() {
		List<String> jdbcProps = new ArrayList<>();

		if (_HYPERSONIC) {
			jdbcProps.add("portal:jdbc.default.url=" + _databaseURL);
			jdbcProps.add("portal:jdbc.default.username=sa");
			jdbcProps.add("portal:jdbc.default.password=");
		}

		return jdbcProps;
	}

	private static final boolean _HYPERSONIC;

	static {
		Props props = new PropsImpl();

		String jdbcDriver = props.get("jdbc.default.driverClassName");

		_HYPERSONIC = jdbcDriver.contains("hsqldb");
	}

	private final String _databaseURL;

}
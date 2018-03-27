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

import com.liferay.petra.log4j.Log4JUtil;
import com.liferay.portal.kernel.process.ClassPathUtil;
import com.liferay.portal.kernel.test.rule.BaseTestRule;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.spring.hibernate.DialectDetector;
import com.liferay.portal.test.rule.callback.LogAssertionTestCallback;
import com.liferay.portal.util.InitUtil;
import com.liferay.portal.util.PortalClassPathUtil;
import com.liferay.portal.util.PropsUtil;
import org.apache.log4j.Level;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.mock.web.MockServletContext;

import java.util.Collections;
import java.util.List;

/**
 * @author Tom Wang
 */
public class SpringInitializationTestRule implements TestRule {

	public SpringInitializationTestRule(String springConfiguration) {
		_springConfiguration = springConfiguration;
	}

	@Override
	public Statement apply(
		Statement statement, Description description) {

		return new BaseTestRule.StatementWrapper(statement) {

			@Override
			public void evaluate() throws Throwable {
				if (!InitUtil.isInitialized()) {
					List<String> configLocations = ListUtil.fromArray(
						PropsUtil.getArray(_springConfiguration));

					boolean configureLog4j = false;

					if (GetterUtil.getBoolean(
						SystemProperties.get(
							"log4j.configure.on.startup"),
						true)) {

						SystemProperties.set(
							"log4j.configure.on.startup", "false");

						configureLog4j = true;
					}

					Log4JUtil.setLevel(
						DialectDetector.class.getName(),
						Level.INFO.toString(), false);

					ClassPathUtil.initializeClassPaths(
						new MockServletContext());
					PortalClassPathUtil.initializeClassPaths(
						new MockServletContext());

					InitUtil.initWithSpring(
						configLocations, true, true);

					if (configureLog4j) {
						Log4JUtil.configureLog4J(
							InitUtil.class.getClassLoader());

						LogAssertionTestCallback.startAssert(
							Collections.<ExpectedLogs>emptyList());
					}

					if (System.getProperty("external-properties") ==
						null) {

						System.setProperty(
							"external-properties",
							"portal-test.properties");
					}
				}

				statement.evaluate();
			}

		};
	}

	private String _springConfiguration;

}
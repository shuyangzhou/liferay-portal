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

package com.liferay.portal.kernel.test;

import com.liferay.portal.kernel.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Shuyang Zhou
 */
public class AggregateTestRule implements TestRule {

	public AggregateTestRule(boolean sort, TestRule... testRules) {
		if (testRules == null) {
			throw new NullPointerException("Test rules is null");
		}

		if (testRules.length < 2) {
			throw new IllegalArgumentException(
				"Rule number " + testRules.length + " is less than 2");
		}

		if (_databaseCleanupTestRule != null) {
			testRules = ArrayUtil.append(testRules, _databaseCleanupTestRule);
		}

		_testRules = Arrays.asList(testRules);

		for (TestRule testRule : _testRules) {
			if (testRule instanceof AggregateTestRule) {
				((AggregateTestRule)testRule)._setParentAggregateTestRule(this);
			}
		}

		if (sort) {
			Collections.sort(_testRules, _testRuleComparator);
		}
	}

	public AggregateTestRule(TestRule... testRules) {
		this(true, testRules);
	}

	@Override
	public Statement apply(Statement statement, Description description) {
		for (TestRule testRule: _testRules) {
			statement = testRule.apply(statement, description);
		}

		return statement;
	}

	private void _setParentAggregateTestRule(
		AggregateTestRule parentAggregateTestRule) {

		_parentAggregateTestRule = parentAggregateTestRule;

		if (_databaseCleanupTestRule != null) {
			_testRules = new ArrayList<TestRule>(_testRules);
			_testRules.remove(_databaseCleanupTestRule);
		}
	}

	private static final String[] _ORDERED_RULE_CLASS_NAMES = new String[] {
		HeapDumpTestRule.class.getName(), CodeCoverageAssertor.class.getName(),
		NewEnvTestRule.class.getName(),
		"com.liferay.portal.test.LiferayIntegrationTestRule",
		"com.liferay.portal.test.MainServletTestRule",
		"com.liferay.portal.test.PersistenceTestRule",
		"com.liferay.portal.test.TransactionalTestRule",
		"com.liferay.portal.test.SynchronousDestinationTestRule",
		"com.liferay.portal.test.jdbc.DatabaseCleanupTestRule"
	};

	private static final TestRule _databaseCleanupTestRule;

	private static final Comparator<TestRule> _testRuleComparator =
		new Comparator<TestRule>() {

			@Override
			public int compare(TestRule testRule1, TestRule testRule2) {
				return getIndex(testRule2.getClass()) -
					getIndex(testRule1.getClass());
			}

			private int getIndex(Class<?> testRuleClass) {
				Set<String> testRuleClassNames = new HashSet<>();

				while (TestRule.class.isAssignableFrom(testRuleClass)) {
					testRuleClassNames.add(testRuleClass.getName());

					testRuleClass = testRuleClass.getSuperclass();
				}

				for (int i = 0; i < _ORDERED_RULE_CLASS_NAMES.length; i++) {
					if (testRuleClassNames.contains(
							_ORDERED_RULE_CLASS_NAMES[i])) {

						return i;
					}
				}

				throw new IllegalArgumentException(
					"Unknown test rule class : " + testRuleClass);
			}

		};

	static {
		if (Boolean.getBoolean("database.cleanup")) {
			try {
				Class clazz = Class.forName(
					"com.liferay.portal.test.jdbc.DatabaseCleanupTestRule");

				_databaseCleanupTestRule = ReflectionTestUtil.getFieldValue(
					clazz, "INSTANCE");
			}
			catch (Exception e) {
				throw new ExceptionInInitializerError(e);
			}
		}
		else {
			_databaseCleanupTestRule = null;
		}
	}

	private AggregateTestRule _parentAggregateTestRule = null;
	private List<TestRule> _testRules;

}
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.increment;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author László Csontos
 */
public class ComparableIncrementTest {

	@Test
	public void testDecrease() {
		doTest(false, false);
		doTest(false, true);
	}

	@Test
	public void testIncrease() {
		doTest(true, false);
		doTest(true, true);
	}

	protected void doTest(boolean testIncrease, boolean withIncreasing) {
		ComparableIncrement<Integer>
			comparableIncrement1 = new IntegerIncrement(withIncreasing);

		ComparableIncrement<Integer> comparableIncrement2 = null;

		int actual = 0;
		int expected = 0;

		if (testIncrease) {
			actual = _START + 1;
			expected = _START + 1;

			if (!withIncreasing) {
				expected = _START;
			}

			comparableIncrement2 = comparableIncrement1.increaseForNew(actual);
		}
		else {
			actual = _START - 1;
			expected = _START - 1;

			if (withIncreasing) {
				expected = _START;
			}

			comparableIncrement2 = comparableIncrement1.decreaseForNew(actual);
		}

		Assert.assertNotSame(comparableIncrement1, comparableIncrement2);
		Assert.assertEquals(_START, comparableIncrement1.getValue());
		Assert.assertEquals(expected, comparableIncrement2.getValue());

		if (testIncrease) {
			actual = _START + 1;
			expected = _START + 1;

			if (!withIncreasing) {
				expected = _START;
			}

			comparableIncrement1.increase(actual);
		}
		else {
			actual = _START - 1;
			expected = _START - 1;

			if (withIncreasing) {
				expected = _START;
			}

			comparableIncrement1.decrease(actual);
		}

		Assert.assertEquals(expected, comparableIncrement1.getValue());

		comparableIncrement1 = new IntegerIncrement(withIncreasing);

		if (testIncrease) {
			actual = _START - 1;
			expected = _START;

			comparableIncrement1.increase(actual);
		}
		else {
			actual = _START + 1;
			expected = _START;

			comparableIncrement1.decrease(actual);
		}

		Assert.assertEquals(expected, comparableIncrement1.getValue());
	}

	private static final int _START = 1;

	private static class IntegerIncrement extends ComparableIncrement<Integer> {

		public IntegerIncrement(boolean increasing) {
			super(_START, increasing);
		}

		@Override
		protected ComparableIncrement<Integer> createComparableIncrement() {
			return new IntegerIncrement(increasing);
		}
	}

}
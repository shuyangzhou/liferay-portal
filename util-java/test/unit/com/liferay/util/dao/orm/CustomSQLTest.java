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

package com.liferay.util.dao.orm;

import com.liferay.portal.kernel.util.OrderByComparator;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Laszlo Csontos
 */
public class CustomSQLTest {

	@BeforeClass
	public static void setUp() throws Exception {
		_customSQL = new CustomSQL() {
			public void reloadCustomSQL() {
			}
		};
	}

	@Test
	public void testReplaceOrderByWithoutTableName() {
		String expected = _SQL1 + " ORDER BY " + _ORDER_BY_PREDICATE;

		doTest(new String[] {expected, expected}, null);
	}

	@Test
	public void testReplaceOrderByWithTableName() {
		String expected = _SQL1 + " ORDER BY ot." + _ORDER_BY_PREDICATE;

		doTest(new String[] {expected, expected}, "ot");
	}

	protected void doTest(String[] expecteds, String tableName) {
		String[] actuals = new String[] {
			_customSQL.replaceOrderBy(_SQL1, _ORDER_BY_COMPARATOR, tableName),
			_customSQL.replaceOrderBy(_SQL2, _ORDER_BY_COMPARATOR, tableName)
		};

		Assert.assertArrayEquals(expecteds, actuals);
	}

	private static final OrderByComparator _ORDER_BY_COMPARATOR =
		new OrderByComparator() {

		@Override
		public int compare(Object obj1, Object obj2) {
			return 0;
		}

		@Override
		public String getOrderBy() {
			return _ORDER_BY_PREDICATE;
		}

	};

	private static final String _ORDER_BY_PREDICATE =
		"createDate DESC, st.modifiedDate ASC";

	private static final String _SQL1 =
		"SELECT * FROM someTable st, otherTable ot WHERE st.id = ot.id";

	private static final String _SQL2 = _SQL1 + " ORDER BY st.name";

	private static CustomSQL _customSQL;

}
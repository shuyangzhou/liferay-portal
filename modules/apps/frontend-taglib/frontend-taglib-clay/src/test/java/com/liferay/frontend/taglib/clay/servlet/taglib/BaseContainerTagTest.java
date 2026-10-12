/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.taglib.clay.servlet.taglib;

import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.LinkedHashSet;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class BaseContainerTagTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testProcessCssClasses() {
		ColTag colTag = new ColTag();

		String[][] colCssClassesArray = {
			{null, "col"}, {"\t", "col "}, {" a  b ", "col a  b"},
			{"a col a", "col a col a"},
			{
				"col-lg-4 col-sm-12 col-12 col-md-4 d-flex flex-column ",
				"col col-lg-4 col-sm-12 col-12 col-md-4 d-flex flex-column"
			}
		};

		for (String[] colCssClasses : colCssClassesArray) {
			colTag.setCssClass(colCssClasses[0]);

			Assert.assertEquals(
				colCssClasses[0], colCssClasses[1],
				colTag.processCssClasses(new LinkedHashSet<>()));
		}

		LinkTag linkTag = new LinkTag();

		linkTag.setCssClass("a b ");

		Assert.assertEquals(
			"a b", linkTag.processCssClasses(new LinkedHashSet<>()));
	}

}
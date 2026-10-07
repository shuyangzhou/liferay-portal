/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template.freemarker.internal;

import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class FreeMarkerBundleClassloaderTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testFindClass() {
		List<String> names = new ArrayList<>();

		FreeMarkerBundleClassloader freeMarkerBundleClassloader =
			new FreeMarkerBundleClassloader(
				Collections.singleton(
					new ClassLoader() {

						@Override
						public Class<?> loadClass(String name)
							throws ClassNotFoundException {

							names.add(name);

							throw new ClassNotFoundException(name);
						}

					}));

		for (String name :
				Arrays.asList("Foo", "FooBeanInfo", "FooCustomizer")) {

			try {
				freeMarkerBundleClassloader.loadClass(name);

				Assert.fail(name);
			}
			catch (ClassNotFoundException classNotFoundException) {
				Assert.assertEquals(name, classNotFoundException.getMessage());
			}
		}

		Assert.assertEquals(Arrays.asList("Foo"), names);
	}

}
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

package com.liferay.portal.search.elasticsearch;

import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.util.Collection;
import java.util.Iterator;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Cristina González
 */
public class ElasticsearchRegistrationTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testGetSearchEngineService() throws Exception {
		Registry registry = RegistryUtil.getRegistry();

		Collection<SearchEngine> searchEngines = registry.getServices(
			SearchEngine.class, null);

		Assert.assertEquals(1, searchEngines.size());

		Iterator<SearchEngine> iterator = searchEngines.iterator();

		SearchEngine searchEngine = iterator.next();

		Class<? extends SearchEngine> searchEngineClass =
			searchEngine.getClass();

		String searchEngineClassName = searchEngineClass.getName();

		Assert.assertTrue(
			"The registered search engine is " + searchEngineClassName,
			searchEngineClassName.endsWith("ElasticsearchSearchEngine"));
	}

}
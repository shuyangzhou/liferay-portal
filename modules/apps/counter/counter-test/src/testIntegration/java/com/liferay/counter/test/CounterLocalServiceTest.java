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

package com.liferay.counter.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.PropsUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Shuyang Zhou
 */
@RunWith(Arquillian.class)
public class CounterLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testConcurrentIncrement() throws Exception {
		try {
			PropsUtil.set(
				PropsKeys.COUNTER_INCREMENT_PREFIX + _COUNTER_NAME, "1");

			_counterLocalService.reset(_COUNTER_NAME, 0);

			List<Future<List<Long>>> futuresList = new ArrayList<>();

			for (int i = 0; i < _THREAD_COUNT; i++) {
				FutureTask<List<Long>> futureTask = new FutureTask<>(
					() -> {
						List<Long> ids = new ArrayList<>();

						for (int j = 0; j < _INCREMENT_COUNT; j++) {
							ids.add(
								_counterLocalService.increment(_COUNTER_NAME));
						}

						return ids;
					});

				Thread thread = new Thread(futureTask, "Increment Thread-" + i);

				thread.start();

				futuresList.add(futureTask);
			}

			int total = _THREAD_COUNT * _INCREMENT_COUNT;

			List<Long> allIds = new ArrayList<>(total);

			for (Future<List<Long>> future : futuresList) {
				allIds.addAll(future.get());
			}

			Assert.assertEquals(allIds.toString(), total, allIds.size());

			Collections.sort(allIds);

			for (int i = 0; i < total; i++) {
				Long id = allIds.get(i);

				Assert.assertEquals(i + 1, id.intValue());
			}
		}
		finally {
			_counterLocalService.reset(_COUNTER_NAME);

			PropsUtil.set(
				PropsKeys.COUNTER_INCREMENT_PREFIX + _COUNTER_NAME, null);
		}
	}

	private static final String _COUNTER_NAME =
		CounterLocalServiceTest.class.getName();

	private static final int _INCREMENT_COUNT = 10000;

	private static final int _THREAD_COUNT = 4;

	@Inject
	private CounterLocalService _counterLocalService;

}
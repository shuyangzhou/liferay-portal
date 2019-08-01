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
import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.counter.kernel.service.persistence.CounterFinder;
import com.liferay.counter.model.CounterRegister;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.PropsValues;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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

	@Before
	public void setUp() {
		CounterLocalServiceUtil.reset(_COUNTER_NAME);

		CounterLocalServiceUtil.reset(_COUNTER_NAME, 0);
	}

	@After
	public void tearDown() {
		CounterLocalServiceUtil.reset(_COUNTER_NAME);
	}

	@Test
	public void testConcurrentIncrement() throws Exception {
		Field field = ReflectionUtil.getDeclaredField(
			_counterFinder.getClass(), "_counterRegisterMap");

		Map<String, CounterRegister> counterRegisterMap =
			(Map<String, CounterRegister>)field.get(_counterFinder);

		counterRegisterMap.remove(_COUNTER_NAME);

		field = ReflectionUtil.getDeclaredField(
			_counterFinder.getClass(), "_rangeSizeMap");

		Map<String, Integer> rangeSizeMap = (Map<String, Integer>)field.get(
			_counterFinder);

		rangeSizeMap.put(_COUNTER_NAME, 1);

		List<Future<Long[]>> futuresList = new ArrayList<>();

		for (int i = 0; i < _THREAD_COUNT; i++) {
			FutureTask<Long[]> futureTask = new FutureTask<>(
				() -> {
					List<Long> ids = new ArrayList<>();

					for (int j = 0; j < _INCREMENT_COUNT; j++) {
						ids.add(
							CounterLocalServiceUtil.increment(_COUNTER_NAME));
					}

					return ids.toArray(new Long[0]);
				});

			_startThread(futureTask, "Increment Thread-" + i);

			futuresList.add(futureTask);
		}

		int total = _THREAD_COUNT * _INCREMENT_COUNT;

		List<Long> ids = new ArrayList<>(total);

		for (Future<Long[]> futures : futuresList) {
			Collections.addAll(ids, futures.get());
		}

		Assert.assertEquals(ids.toString(), total, ids.size());

		Collections.sort(ids);

		for (int i = 0; i < total; i++) {
			Long id = ids.get(i);

			Assert.assertEquals(
				i + 1 + PropsValues.COUNTER_INCREMENT, id.intValue());
		}
	}

	private void _startThread(FutureTask<Long[]> futureTask, String name) {
		Thread thread = new Thread(futureTask, name);

		thread.start();
	}

	private static final String _COUNTER_NAME = StringUtil.randomString();

	private static final int _INCREMENT_COUNT = 10000;

	private static final int _THREAD_COUNT = 4;

	@Inject
	private CounterFinder _counterFinder;

}
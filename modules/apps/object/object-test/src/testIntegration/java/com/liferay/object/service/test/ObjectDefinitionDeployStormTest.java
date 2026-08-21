/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.field.util.ObjectFieldUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.test.util.ObjectDefinitionTestUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LogEntry;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Shuyang Zhou
 */
@RunWith(Arquillian.class)
public class ObjectDefinitionDeployStormTest {

	@ClassRule
	@Rule
	public static AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testConcurrentPublishAndDeleteOnRecycledClassNames()
		throws Exception {

		// Recycling a class name is safe on its own and concurrency is safe on
		// its own. This drives both at once: a small pool of class names is
		// published and deleted repeatedly by several threads, so the same
		// portlet ID is claimed and released over and over with overlapping
		// deploys and undeploys.

		List<Throwable> throwables = Collections.synchronizedList(
			new ArrayList<>());

		CountDownLatch startCountDownLatch = new CountDownLatch(1);

		List<Thread> threads = new ArrayList<>();

		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				_CLASS_NAME_PORTLET_TRACKER, LoggerTestUtil.ERROR)) {

			for (int i = 0; i < _THREAD_COUNT; i++) {
				int threadIndex = i;

				Thread thread = new Thread(
					() -> {
						String className =
							ObjectDefinitionConstants.
								CLASS_NAME_PREFIX_CUSTOM_OBJECT_DEFINITION +
									_CLASS_NAME_TOKENS[
										threadIndex % _CLASS_NAME_TOKENS.length];

						try {
							startCountDownLatch.await();

							for (int j = 0; j < _ITERATION_COUNT; j++) {
								ObjectDefinition objectDefinition = _publish(
									className);

								_objectDefinitionLocalService.
									deleteObjectDefinition(
										objectDefinition.
											getObjectDefinitionId());
							}
						}
						catch (Throwable throwable) {
							throwables.add(throwable);
						}
					},
					"ObjectDefinitionDeployStorm-" + i);

				thread.setDaemon(true);

				threads.add(thread);

				thread.start();
			}

			startCountDownLatch.countDown();

			for (Thread thread : threads) {
				thread.join();
			}

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertTrue(
				"Portlet tracker logged " + logEntries, logEntries.isEmpty());
		}

		// Two threads racing one class name legitimately see it taken, which
		// the service reports as a duplicate. Anything else is a defect.

		List<Throwable> unexpectedThrowables = new ArrayList<>();

		for (Throwable throwable : throwables) {
			String message = String.valueOf(throwable.getMessage());

			if (!message.contains("Duplicate") &&
				!message.contains("already exists") &&
				!message.contains("No ObjectDefinition exists")) {

				unexpectedThrowables.add(throwable);
			}
		}

		Assert.assertTrue(
			unexpectedThrowables.toString(), unexpectedThrowables.isEmpty());
	}

	private ObjectDefinition _publish(String className) throws Exception {
		String name = ObjectDefinitionTestUtil.getRandomName();

		List<ObjectField> objectFields = Arrays.asList(
			ObjectFieldUtil.createObjectField(
				ObjectFieldConstants.BUSINESS_TYPE_TEXT,
				ObjectFieldConstants.DB_TYPE_STRING,
				RandomTestUtil.randomString(), StringUtil.randomId()));

		ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.addCustomObjectDefinition(
				null, TestPropsValues.getUserId(), 0, className, true, false,
				false, true, false, false, false, false, false, null,
				LocalizedMapUtil.getLocalizedMap(name), name, null, null,
				LocalizedMapUtil.getLocalizedMap(name), true,
				ObjectDefinitionConstants.SCOPE_COMPANY,
				ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT,
				Collections.emptyList(), objectFields, Collections.emptyList(),
				new ServiceContext());

		return _objectDefinitionLocalService.publishCustomObjectDefinition(
			TestPropsValues.getUserId(),
			objectDefinition.getObjectDefinitionId());
	}

	private static final String _CLASS_NAME_PORTLET_TRACKER =
		"com.liferay.portal.osgi.web.portlet.tracker.internal.osgi.util." +
			"tracker.PortletTracker";

	private static final String[] _CLASS_NAME_TOKENS =
		{"Z9Z9", "Y8Y8", "X7X7"};

	private static final int _ITERATION_COUNT = 8;

	private static final int _THREAD_COUNT = 6;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

}
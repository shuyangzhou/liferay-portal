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

package com.liferay.portal.background.task.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.background.task.model.BackgroundTask;
import com.liferay.portal.background.task.service.BackgroundTaskLocalServiceUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutorRegistryUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskLockHelperUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManagerUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskStatus;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskStatusMessageTranslator;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.io.Serializable;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

/**
 * @author Daniel Kocsis
 */
@RunWith(Arquillian.class)
public class BackgroundTaskCancellationTest {

	@ClassRule
	@Rule
	public static final TestRule testRule = new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		BackgroundTaskExecutorRegistryUtil.registerBackgroundTaskExecutor(
			"MockBackgroundTaskExecutor", new MockBackgroundTaskExecutor());
	}

	@After
	public void tearDown() throws Exception {
		BackgroundTaskExecutorRegistryUtil.unregisterBackgroundTaskExecutor(
			"MockBackgroundTaskExecutor");
	}

	@Test
	public void testQueuedTasksCancellation1() throws Exception {
		_interruptBackgroundTaskSignal = new CountDownLatch(1);
		_startBackgroundTaskSignal = new CountDownLatch(1);

		BackgroundTask backgroundTask1 =
			BackgroundTaskLocalServiceUtil.addBackgroundTask(
				TestPropsValues.getUserId(), _group.getGroupId(),
				RandomTestUtil.randomString(), "MockBackgroundTaskExecutor",
				new HashMap<String, Serializable>(),
				ServiceContextTestUtil.getServiceContext());

		_startBackgroundTaskSignal.await(10, TimeUnit.SECONDS);

		BackgroundTask backgroundTask2 =
			BackgroundTaskLocalServiceUtil.addBackgroundTask(
				TestPropsValues.getUserId(), _group.getGroupId(),
				RandomTestUtil.randomString(), "MockBackgroundTaskExecutor",
				new HashMap<String, Serializable>(),
				ServiceContextTestUtil.getServiceContext());

		backgroundTask1 =
			BackgroundTaskLocalServiceUtil.fetchBackgroundTaskWithoutCaching(
				backgroundTask1.getBackgroundTaskId());

		Assert.assertEquals(
			BackgroundTaskConstants.STATUS_IN_PROGRESS,
			backgroundTask1.getStatus());

		// to make sure the 2nd task has time to be queued properly

		waitForStatusChange(
			backgroundTask2.getBackgroundTaskId(),
			BackgroundTaskConstants.STATUS_QUEUED);

		backgroundTask2 =
			BackgroundTaskLocalServiceUtil.fetchBackgroundTaskWithoutCaching(
				backgroundTask2.getBackgroundTaskId());

		Assert.assertEquals(
			BackgroundTaskConstants.STATUS_QUEUED, backgroundTask2.getStatus());

		BackgroundTaskManagerUtil.interruptBackgroundTask(
			backgroundTask1.getBackgroundTaskId());

		_interruptBackgroundTaskSignal.countDown();

		// wait for locking related overhead

		waitForStatusChange(
			backgroundTask2.getBackgroundTaskId(),
			BackgroundTaskConstants.STATUS_SUCCESSFUL);

		backgroundTask1 =
			BackgroundTaskLocalServiceUtil.fetchBackgroundTaskWithoutCaching(
				backgroundTask1.getBackgroundTaskId());

		Assert.assertEquals(
			BackgroundTaskConstants.STATUS_CANCELLED,
			backgroundTask1.getStatus());

		backgroundTask2 =
			BackgroundTaskLocalServiceUtil.fetchBackgroundTaskWithoutCaching(
				backgroundTask2.getBackgroundTaskId());

		Assert.assertEquals(
			BackgroundTaskConstants.STATUS_SUCCESSFUL,
			backgroundTask2.getStatus());
	}

	@Test
	public void testQueuedTasksCancellation2() throws Exception {
		_interruptBackgroundTaskSignal = new CountDownLatch(1);
		_startBackgroundTaskSignal = new CountDownLatch(1);

		BackgroundTask backgroundTask1 =
			BackgroundTaskLocalServiceUtil.addBackgroundTask(
				TestPropsValues.getUserId(), _group.getGroupId(),
				RandomTestUtil.randomString(), "MockBackgroundTaskExecutor",
				new HashMap<String, Serializable>(),
				ServiceContextTestUtil.getServiceContext());

		_startBackgroundTaskSignal.await(10, TimeUnit.SECONDS);

		BackgroundTask backgroundTask2 =
			BackgroundTaskLocalServiceUtil.addBackgroundTask(
				TestPropsValues.getUserId(), _group.getGroupId(),
				RandomTestUtil.randomString(), "MockBackgroundTaskExecutor",
				new HashMap<String, Serializable>(),
				ServiceContextTestUtil.getServiceContext());

		backgroundTask1 =
			BackgroundTaskLocalServiceUtil.fetchBackgroundTaskWithoutCaching(
				backgroundTask1.getBackgroundTaskId());

		Assert.assertEquals(
			BackgroundTaskConstants.STATUS_IN_PROGRESS,
			backgroundTask1.getStatus());

		// to make sure the 2nd task has time to be queued properly

		waitForStatusChange(
			backgroundTask2.getBackgroundTaskId(),
			BackgroundTaskConstants.STATUS_QUEUED);

		backgroundTask2 =
			BackgroundTaskLocalServiceUtil.fetchBackgroundTaskWithoutCaching(
				backgroundTask2.getBackgroundTaskId());

		Assert.assertEquals(
			BackgroundTaskConstants.STATUS_QUEUED, backgroundTask2.getStatus());

		BackgroundTaskManagerUtil.interruptBackgroundTask(
			backgroundTask2.getBackgroundTaskId());

		_interruptBackgroundTaskSignal.countDown();

		// wait for locking related overhead

		waitForStatusChange(
			backgroundTask2.getBackgroundTaskId(),
			BackgroundTaskConstants.STATUS_INTERRUPTED);

		backgroundTask2 =
			BackgroundTaskLocalServiceUtil.fetchBackgroundTaskWithoutCaching(
				backgroundTask2.getBackgroundTaskId());

		Assert.assertEquals(
			BackgroundTaskConstants.STATUS_INTERRUPTED,
			backgroundTask2.getStatus());

		waitForStatusChange(
			backgroundTask1.getBackgroundTaskId(),
			BackgroundTaskConstants.STATUS_SUCCESSFUL);

		backgroundTask1 =
			BackgroundTaskLocalServiceUtil.fetchBackgroundTaskWithoutCaching(
				backgroundTask1.getBackgroundTaskId());

		Assert.assertEquals(
			BackgroundTaskConstants.STATUS_SUCCESSFUL,
			backgroundTask1.getStatus());

		// Locking

		com.liferay.portal.kernel.backgroundtask.BackgroundTask
			backgroundTask11 = BackgroundTaskManagerUtil.fetchBackgroundTask(
				backgroundTask1.getBackgroundTaskId());

		Assert.assertFalse(
			BackgroundTaskLockHelperUtil.isLockedBackgroundTask(
				backgroundTask11));

		com.liferay.portal.kernel.backgroundtask.BackgroundTask
			backgroundTask21 = BackgroundTaskManagerUtil.fetchBackgroundTask(
				backgroundTask2.getBackgroundTaskId());

		Assert.assertFalse(
			BackgroundTaskLockHelperUtil.isLockedBackgroundTask(
				backgroundTask21));
	}

	@Test
	public void testTaskCancellation() throws Exception {
		_interruptBackgroundTaskSignal = new CountDownLatch(1);
		_startBackgroundTaskSignal = new CountDownLatch(1);

		BackgroundTask backgroundTask =
			BackgroundTaskLocalServiceUtil.addBackgroundTask(
				TestPropsValues.getUserId(), _group.getGroupId(),
				RandomTestUtil.randomString(), "MockBackgroundTaskExecutor",
				new HashMap<String, Serializable>(),
				ServiceContextTestUtil.getServiceContext());

		_startBackgroundTaskSignal.await(10, TimeUnit.SECONDS);

		BackgroundTaskManagerUtil.interruptBackgroundTask(
			backgroundTask.getBackgroundTaskId());

		_interruptBackgroundTaskSignal.countDown();

		// wait for locking related overhead

		waitForStatusChange(
			backgroundTask.getBackgroundTaskId(),
			BackgroundTaskConstants.STATUS_CANCELLED);

		backgroundTask =
			BackgroundTaskLocalServiceUtil.fetchBackgroundTaskWithoutCaching(
				backgroundTask.getBackgroundTaskId());

		Assert.assertEquals(
			BackgroundTaskConstants.STATUS_CANCELLED,
			backgroundTask.getStatus());

		Assert.assertFalse(backgroundTask.isCompleted());

		// Locking

		com.liferay.portal.kernel.backgroundtask.BackgroundTask
			backgroundTask1 = BackgroundTaskManagerUtil.fetchBackgroundTask(
				backgroundTask.getBackgroundTaskId());

		Assert.assertFalse(
			BackgroundTaskLockHelperUtil.isLockedBackgroundTask(
				backgroundTask1));
	}

	protected void waitForStatusChange(long backgroundTaskId, int status)
		throws Exception {

		waitForStatusChange(backgroundTaskId, status, 10 * Time.SECOND);
	}

	protected void waitForStatusChange(
			long backgroundTaskId, int status, long timeout)
		throws Exception {

		long startTime = System.currentTimeMillis();

		BackgroundTask backgroundTask =
			BackgroundTaskLocalServiceUtil.fetchBackgroundTaskWithoutCaching(
				backgroundTaskId);

		while ((System.currentTimeMillis() - startTime) <= timeout) {
			if (backgroundTask.getStatus() == status) {
				break;
			}

			Thread.sleep(1000);

			backgroundTask =
				BackgroundTaskLocalServiceUtil.
					fetchBackgroundTaskWithoutCaching(backgroundTaskId);
		}
	}

	@DeleteAfterTestRun
	private Group _group;

	private CountDownLatch _interruptBackgroundTaskSignal;
	private CountDownLatch _startBackgroundTaskSignal;

	private class MockBackgroundTaskExecutor implements BackgroundTaskExecutor {

		@Override
		public BackgroundTaskExecutor clone() {
			return new MockBackgroundTaskExecutor();
		}

		@Override
		public BackgroundTaskResult execute(
				com.liferay.portal.kernel.backgroundtask.BackgroundTask
					backgroundTask)
			throws Exception {

			_startBackgroundTaskSignal.countDown();

			_interruptBackgroundTaskSignal.await(10, TimeUnit.SECONDS);

			BackgroundTaskManagerUtil.interruptCurrentBackgroundTask();

			return BackgroundTaskResult.SUCCESS;
		}

		@Override
		public String generateLockKey(
			com.liferay.portal.kernel.backgroundtask.BackgroundTask
				backgroundTask) {

			return "MockBackgroundTaskExecutor";
		}

		@Override
		public BackgroundTaskDisplay getBackgroundTaskDisplay(
			com.liferay.portal.kernel.backgroundtask.BackgroundTask
				backgroundTask) {

			return null;
		}

		@Override
		public BackgroundTaskStatusMessageTranslator
			getBackgroundTaskStatusMessageTranslator() {

			return new BackgroundTaskStatusMessageTranslator() {

				@Override
				public void translate(
					BackgroundTaskStatus backgroundTaskStatus,
					Message message) {
				}

			};
		}

		@Override
		public int getIsolationLevel() {
			return BackgroundTaskConstants.ISOLATION_LEVEL_CLASS;
		}

		@Override
		public String handleException(
			com.liferay.portal.kernel.backgroundtask.BackgroundTask
				backgroundTask, Exception e) {

			return StringPool.BLANK;
		}

		@Override
		public boolean isSerial() {
			return Boolean.TRUE;
		}

	}

}
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

package com.liferay.portal.cluster.internal;

import com.liferay.portal.cluster.ClusterChannel;
import com.liferay.portal.cluster.ClusterReceiver;
import com.liferay.portal.cluster.configuration.ClusterExecutorConfiguration;
import com.liferay.portal.kernel.cluster.Address;
import com.liferay.portal.kernel.cluster.ClusterEvent;
import com.liferay.portal.kernel.cluster.ClusterEventListener;
import com.liferay.portal.kernel.cluster.ClusterEventType;
import com.liferay.portal.kernel.cluster.ClusterMasterTokenTransitionListener;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.cluster.FutureClusterResponses;
import com.liferay.portal.kernel.concurrent.NoticeableFuture;
import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.Props;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Matthew Tambara
 */
public class ClusterMasterExecutorImplTest extends BaseClusterTestCase {

	@Before
	@Override
	public void setUp() {
		super.setUp();
	}

	@Test
	public void testClusterMasterTokenClusterEventListener() {

		// Test 1, test with JOIN event

		ClusterMasterExecutorImpl clusterMasterExecutorImpl =
			new ClusterMasterExecutorImpl();

		MockClusterExecutor mockClusterExecutor = new MockClusterExecutor(true);

		clusterMasterExecutorImpl.setClusterExecutorImpl(mockClusterExecutor);

		clusterMasterExecutorImpl.activate();

		List<ClusterEventListener> clusterEventListeners =
			mockClusterExecutor.getClusterEventListeners();

		ClusterEventListener clusterEventListener = clusterEventListeners.get(
			0);

		Assert.assertTrue(clusterMasterExecutorImpl.isMaster());

		clusterEventListener.processClusterEvent(
			new ClusterEvent(ClusterEventType.JOIN));

		Assert.assertTrue(clusterMasterExecutorImpl.isMaster());

		// Test 2, test with DEPART event

		clusterEventListener.processClusterEvent(
			new ClusterEvent(ClusterEventType.DEPART));

		Assert.assertTrue(clusterMasterExecutorImpl.isMaster());

		// Test 3, test with COORDINATOR_UPDATE event when coordiator is not
		// changed

		clusterEventListener.processClusterEvent(
			new ClusterEvent(ClusterEventType.COORDINATOR_UPDATE));

		Assert.assertTrue(clusterMasterExecutorImpl.isMaster());

		// Test 4, test with COORDINATOR_UPDATE when coordiator is changed

		mockClusterExecutor.setCoordintor(new TestAddress("test.address"));

		clusterEventListener.processClusterEvent(
			new ClusterEvent(ClusterEventType.COORDINATOR_UPDATE));

		Assert.assertFalse(clusterMasterExecutorImpl.isMaster());
	}

	@Test
	public void testClusterMasterTokenTransitionListeners() {

		// Test 1, register cluster master token transition listener

		ClusterMasterExecutorImpl clusterMasterExecutorImpl =
			new ClusterMasterExecutorImpl();

		Set<ClusterMasterTokenTransitionListener>
			clusterMasterTokenTransitionListeners =
				ReflectionTestUtil.getFieldValue(
					clusterMasterExecutorImpl,
					"_clusterMasterTokenTransitionListeners");

		Assert.assertTrue(clusterMasterTokenTransitionListeners.isEmpty());

		ClusterMasterTokenTransitionListener
			mockClusterMasterTokenTransitionListener =
				new MockClusterMasterTokenTransitionListener();

		clusterMasterExecutorImpl.addClusterMasterTokenTransitionListener(
			mockClusterMasterTokenTransitionListener);

		Assert.assertEquals(1, clusterMasterTokenTransitionListeners.size());

		// Test 2, unregister cluster master token transition listener

		clusterMasterExecutorImpl.removeClusterMasterTokenTransitionListener(
			mockClusterMasterTokenTransitionListener);

		Assert.assertTrue(clusterMasterTokenTransitionListeners.isEmpty());

		// Test 3, set cluster master token transition listeners

		clusterMasterExecutorImpl.setClusterMasterTokenTransitionListeners(
			Collections.singleton(mockClusterMasterTokenTransitionListener));

		Assert.assertEquals(1, clusterMasterTokenTransitionListeners.size());
	}

	@Test
	public void testDeactivate() {

		// Test 1, destroy when cluster link is enabled

		ClusterMasterExecutorImpl clusterMasterExecutorImpl =
			new ClusterMasterExecutorImpl();

		MockClusterExecutor mockClusterExecutor = new MockClusterExecutor(true);

		clusterMasterExecutorImpl.setClusterExecutorImpl(mockClusterExecutor);

		clusterMasterExecutorImpl.activate();

		List<ClusterEventListener> clusterEventListeners =
			mockClusterExecutor.getClusterEventListeners();

		Assert.assertEquals(1, clusterEventListeners.size());

		clusterMasterExecutorImpl.deactivate();

		Assert.assertTrue(clusterEventListeners.isEmpty());

		// Test 2, destory when cluster link is disabled

		clusterMasterExecutorImpl.setClusterExecutorImpl(
			new MockClusterExecutor(false));

		clusterMasterExecutorImpl.activate();

		clusterMasterExecutorImpl.deactivate();
	}

	@Test
	public void testExecuteOnMasterDisabled() throws Exception {

		// Test 1, execute without exception when log is eanbled

		ClusterMasterExecutorImpl clusterMasterExecutorImpl =
			new ClusterMasterExecutorImpl();

		clusterMasterExecutorImpl.setClusterExecutorImpl(
			new MockClusterExecutor(false));

		clusterMasterExecutorImpl.activate();

		Assert.assertFalse(clusterMasterExecutorImpl.isEnabled());

		String timeString = String.valueOf(System.currentTimeMillis());

		MethodHandler methodHandler = new MethodHandler(
			_TEST_METHOD, timeString);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					ClusterMasterExecutorImpl.class.getName(), Level.WARNING)) {

			NoticeableFuture<String> noticeableFuture =
				clusterMasterExecutorImpl.executeOnMaster(methodHandler);

			Assert.assertSame(timeString, noticeableFuture.get());

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Executing on the local node because the cluster master " +
					"executor is disabled",
				logRecord.getMessage());
		}

		// Test 2, execute without exception when log is disabled

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					ClusterMasterExecutorImpl.class.getName(), Level.OFF)) {

			NoticeableFuture<String> noticeableFuture =
				clusterMasterExecutorImpl.executeOnMaster(methodHandler);

			Assert.assertSame(timeString, noticeableFuture.get());

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertTrue(logRecords.isEmpty());
		}

		// Test 3, execute with exception

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					ClusterMasterExecutorImpl.class.getName(), Level.WARNING)) {

			try {
				clusterMasterExecutorImpl.executeOnMaster(null);

				Assert.fail();
			}
			catch (SystemException se) {
				Throwable throwable = se.getCause();

				Assert.assertSame(
					NullPointerException.class, throwable.getClass());

				List<LogRecord> logRecords = captureHandler.getLogRecords();

				Assert.assertEquals(1, logRecords.size());

				LogRecord logRecord = logRecords.get(0);

				Assert.assertEquals(
					"Executing on the local node because the cluster master " +
						"executor is disabled",
					logRecord.getMessage());
			}
		}
	}

	@Test
	public void testExecuteOnMasterEnabled() throws Exception {

		// Test 1, execute without exception

		ClusterMasterExecutorImpl clusterMasterExecutorImpl =
			new ClusterMasterExecutorImpl();

		MockClusterExecutor mockClusterExecutor = new MockClusterExecutor(true);

		clusterMasterExecutorImpl.setClusterExecutorImpl(mockClusterExecutor);

		clusterMasterExecutorImpl.activate();

		Assert.assertTrue(clusterMasterExecutorImpl.isEnabled());

		String timeString = String.valueOf(System.currentTimeMillis());

		NoticeableFuture<String> noticeableFuture =
			clusterMasterExecutorImpl.executeOnMaster(
				new MethodHandler(_TEST_METHOD, timeString));

		Assert.assertSame(timeString, noticeableFuture.get());

		// Test 2, execute with exception

		try {
			clusterMasterExecutorImpl.executeOnMaster(_BAD_METHOD_HANDLER);

			Assert.fail();
		}
		catch (SystemException se) {
			Assert.assertEquals(
				"Unable to execute on master " +
					mockClusterExecutor.getLocalClusterNodeId(),
				se.getMessage());
		}
	}

	@Test
	public void testGetMasterClusterNodeId() {

		// Test 1, current node is master

		ClusterMasterExecutorImpl clusterMasterExecutorImpl =
			new ClusterMasterExecutorImpl();

		MockClusterExecutor mockClusterExecutor = new MockClusterExecutor(true);

		clusterMasterExecutorImpl.setClusterExecutorImpl(mockClusterExecutor);

		clusterMasterExecutorImpl.activate();

		Assert.assertTrue(clusterMasterExecutorImpl.isMaster());
		Assert.assertEquals(
			mockClusterExecutor.getLocalClusterNodeId(),
			clusterMasterExecutorImpl.getMasterClusterNodeId(null));

		// Test 2, current node is not master

		ClusterMasterExecutorImpl anotherClusterMasterExecutorImpl =
			new ClusterMasterExecutorImpl();

		MockClusterExecutor anotherMockClusterExecutor =
			new MockClusterExecutor(true);

		anotherClusterMasterExecutorImpl.setClusterExecutorImpl(
			anotherMockClusterExecutor);

		anotherClusterMasterExecutorImpl.activate();

		Assert.assertFalse(anotherClusterMasterExecutorImpl.isMaster());

		ClusterChannel clusterChannel = mockClusterExecutor.getClusterChannel();

		anotherMockClusterExecutor.addClusterNode(
			clusterChannel.getLocalAddress(),
			mockClusterExecutor.getLocalClusterNode());

		Assert.assertEquals(
			mockClusterExecutor.getLocalClusterNodeId(),
			anotherClusterMasterExecutorImpl.getMasterClusterNodeId(
				clusterChannel.getLocalAddress()));
	}

	@Test
	public void testInitialize() {

		// Test 1, initialize when cluster link is disabled

		ClusterMasterExecutorImpl clusterMasterExecutorImpl =
			new ClusterMasterExecutorImpl();

		clusterMasterExecutorImpl.setClusterExecutorImpl(
			new MockClusterExecutor(false));

		clusterMasterExecutorImpl.activate();

		Assert.assertFalse(clusterMasterExecutorImpl.isEnabled());
		Assert.assertTrue(clusterMasterExecutorImpl.isMaster());

		// Test 2, initialize when cluster link is enabled and local node is
		// master

		clusterMasterExecutorImpl.setClusterExecutorImpl(
			new MockClusterExecutor(true));

		clusterMasterExecutorImpl.activate();

		Assert.assertTrue(clusterMasterExecutorImpl.isEnabled());
		Assert.assertTrue(clusterMasterExecutorImpl.isMaster());

		// Test 3, initialize when cluster link is enabled and local node is
		// not master

		clusterMasterExecutorImpl.setClusterExecutorImpl(
			new MockClusterExecutor(true));

		clusterMasterExecutorImpl.activate();

		Assert.assertTrue(clusterMasterExecutorImpl.isEnabled());
		Assert.assertFalse(clusterMasterExecutorImpl.isMaster());
	}

	@Test
	public void testNotifyMasterTokenTransitionListeners() {

		// Test 1, notify when master is required

		ClusterMasterExecutorImpl clusterMasterExecutorImpl =
			new ClusterMasterExecutorImpl();

		MockClusterMasterTokenTransitionListener
			mockClusterMasterTokenTransitionListener =
				new MockClusterMasterTokenTransitionListener();

		clusterMasterExecutorImpl.addClusterMasterTokenTransitionListener(
			mockClusterMasterTokenTransitionListener);

		clusterMasterExecutorImpl.notifyMasterTokenTransitionListeners(true);

		Assert.assertTrue(
			mockClusterMasterTokenTransitionListener.
				isMasterTokenAcquiredNotified());
		Assert.assertFalse(
			mockClusterMasterTokenTransitionListener.
				isMasterTokenReleasedNotified());

		// Test 2, notify when master is released

		mockClusterMasterTokenTransitionListener =
			new MockClusterMasterTokenTransitionListener();

		clusterMasterExecutorImpl.addClusterMasterTokenTransitionListener(
			mockClusterMasterTokenTransitionListener);

		clusterMasterExecutorImpl.notifyMasterTokenTransitionListeners(false);

		Assert.assertFalse(
			mockClusterMasterTokenTransitionListener.
				isMasterTokenAcquiredNotified());
		Assert.assertTrue(
			mockClusterMasterTokenTransitionListener.
				isMasterTokenReleasedNotified());
	}

	@Test
	public void testUpdateCoordinator() {

		// Test 1, master to slave

		ClusterMasterExecutorImpl clusterMasterExecutorImpl =
			new ClusterMasterExecutorImpl();

		MockClusterExecutor mockClusterExecutor = new MockClusterExecutor(true);

		clusterMasterExecutorImpl.setClusterExecutorImpl(mockClusterExecutor);

		clusterMasterExecutorImpl.activate();

		Assert.assertTrue(clusterMasterExecutorImpl.isMaster());

		MockClusterMasterTokenTransitionListener
			mockClusterMasterTokenTransitionListener =
				new MockClusterMasterTokenTransitionListener();

		clusterMasterExecutorImpl.addClusterMasterTokenTransitionListener(
			mockClusterMasterTokenTransitionListener);

		mockClusterExecutor.setCoordintor(new TestAddress("test.address"));

		clusterMasterExecutorImpl.updateMasterAddress(true);

		Assert.assertFalse(clusterMasterExecutorImpl.isMaster());
		Assert.assertTrue(
			mockClusterMasterTokenTransitionListener.
				isMasterTokenReleasedNotified());

		// Test 2, slave to master

		ClusterChannel clusterChannel = mockClusterExecutor.getClusterChannel();

		mockClusterExecutor.setCoordintor(clusterChannel.getLocalAddress());

		clusterMasterExecutorImpl.updateMasterAddress(true);

		Assert.assertTrue(clusterMasterExecutorImpl.isMaster());
		Assert.assertTrue(
			mockClusterMasterTokenTransitionListener.
				isMasterTokenAcquiredNotified());
	}

	private static final MethodHandler _BAD_METHOD_HANDLER = new MethodHandler(
		new MethodKey());

	private static final MethodKey _TEST_METHOD = new MethodKey(
		TestBean.class, "testMethod1", String.class);

	private static class MockClusterExecutor extends ClusterExecutorImpl {

		public void addClusterNode(Address address, ClusterNode clusterNode) {
			if (!_enabled) {
				return;
			}

			_clusterNodes.put(address, clusterNode);
		}

		@Override
		public FutureClusterResponses execute(ClusterRequest clusterRequest) {
			if (clusterRequest.getPayload() == _BAD_METHOD_HANDLER) {
				throw new RuntimeException();
			}

			return super.execute(clusterRequest);
		}

		public String getLocalClusterNodeId() {
			ClusterNode clusterNode = getLocalClusterNode();

			return clusterNode.getClusterNodeId();
		}

		@Override
		public boolean isEnabled() {
			return _enabled;
		}

		public void setCoordintor(Address address) {
			ClusterChannel clusterChannel = getClusterChannel();

			ClusterReceiver clusterReceiver =
				clusterChannel.getClusterReceiver();

			clusterReceiver.coordinatorUpdated(address);
		}

		@Override
		protected ClusterNode getClusterNode(Address address) {
			return _clusterNodes.get(address);
		}

		private MockClusterExecutor(boolean enabled) {
			_enabled = enabled;

			setClusterChannelFactory(new TestClusterChannelFactory());

			clusterExecutorConfiguration = new ClusterExecutorConfiguration() {

				@Override
				public boolean debugEnabled() {
					return false;
				}

			};

			setPortalExecutorManager(new MockPortalExecutorManager());

			setProps(
				new Props() {

					@Override
					public boolean contains(String key) {
						return false;
					}

					@Override
					public String get(String key) {
						return null;
					}

					@Override
					public String get(String key, Filter filter) {
						return null;
					}

					@Override
					public String[] getArray(String key) {
						return null;
					}

					@Override
					public String[] getArray(String key, Filter filter) {
						return null;
					}

					@Override
					public Properties getProperties() {
						return null;
					}

					@Override
					public Properties getProperties(
						String prefix, boolean removePrefix) {

						return null;
					}

				});

			initialize(
				"test-channel-properties-mock", "test-channel-name-mock");
		}

		private final Map<Address, ClusterNode> _clusterNodes = new HashMap<>();
		private final boolean _enabled;

	}

	private static class MockClusterMasterTokenTransitionListener
		implements ClusterMasterTokenTransitionListener {

		public boolean isMasterTokenAcquiredNotified() {
			return _masterTokenAcquiredNotified;
		}

		public boolean isMasterTokenReleasedNotified() {
			return _masterTokenReleasedNotified;
		}

		@Override
		public void masterTokenAcquired() {
			_masterTokenAcquiredNotified = true;
		}

		@Override
		public void masterTokenReleased() {
			_masterTokenReleasedNotified = true;
		}

		private boolean _masterTokenAcquiredNotified;
		private boolean _masterTokenReleasedNotified;

	}

}
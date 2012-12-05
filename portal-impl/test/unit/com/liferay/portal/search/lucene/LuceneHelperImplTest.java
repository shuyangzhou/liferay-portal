/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.search.lucene;

import com.liferay.portal.cluster.AddressImpl;
import com.liferay.portal.kernel.cluster.Address;
import com.liferay.portal.kernel.cluster.ClusterEvent;
import com.liferay.portal.kernel.cluster.ClusterEventListener;
import com.liferay.portal.kernel.cluster.ClusterExecutor;
import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.cluster.ClusterMessageType;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.cluster.ClusterNodeResponse;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.cluster.ClusterResponseCallback;
import com.liferay.portal.kernel.cluster.FutureClusterResponses;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedInputStream;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.security.auth.TransientTokenUtil;
import com.liferay.portal.test.AdviseWith;
import com.liferay.portal.test.AspectJMockingNewClassLoaderJUnitTestRunner;
import com.liferay.portal.util.PortalImpl;
import com.liferay.portal.util.PortalInstances;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.uuid.PortalUUIDImpl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import junit.framework.Assert;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Tina Tian
 */
@RunWith(AspectJMockingNewClassLoaderJUnitTestRunner.class)
public class LuceneHelperImplTest {

	@Before
	public void setUp() throws Exception {
		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(new PortalImpl());

		PortalUUIDUtil portalUUIDUtil = new PortalUUIDUtil();

		portalUUIDUtil.setPortalUUID(new PortalUUIDImpl());

		_port = _getAvailablePort();
		_localhost = InetAddress.getLocalHost();
	}

	@After
	public void tearDown() throws Exception {
		ClusterExecutorUtil.destroy();
	}

	@AdviseWith(
		adviceClasses = {
			DisableIndexOnStartUpAdvice.class, EnableClusterLinkAdvice.class,
			EnableLuceneReplicateWriteAdvice.class
		}
	)
	@Test
	public void testLoadIndexClusterEventListener1() throws Exception {
		JDKLoggerTestUtil.configureJDKLogger(
			LuceneHelperImpl.class.getName(), Level.OFF);

		_setupClusterExecutor(2, _port);

		long currentTime = System.currentTimeMillis();

		MockIndexAccessor mockIndexAccessor = new MockIndexAccessor(
			currentTime, currentTime);

		_setupLucenHelperImpl(mockIndexAccessor);

		MockServer mockServer = new MockServer(_port);

		mockServer.start();

		new Thread() {

			public void run() {
				try {
					ClusterNode clusterNode = _generateClusterNode(_port);

					ClusterEvent clusterEvent = ClusterEvent.join(clusterNode);

					_fireClusterEventListeners(clusterEvent);
				}
				catch (Exception ex) {
				}
			}

		}.start();

		String responseMessage = mockIndexAccessor.waitforResponseMessage();

		mockServer.close();

		Assert.assertEquals(_RESPONSE_MESSAGE, responseMessage);
	}

	@AdviseWith(
		adviceClasses = {
			DisableIndexOnStartUpAdvice.class, EnableClusterLinkAdvice.class,
			EnableLuceneReplicateWriteAdvice.class
		}
	)
	@Test
	public void testLoadIndexClusterEventListener2() throws Exception {
		List<LogRecord> logRecords = JDKLoggerTestUtil.configureJDKLogger(
			LuceneHelperImpl.class.getName(), Level.SEVERE);

		MockClusterExecutor mockClusterExecutor = _setupClusterExecutor(2, 0);

		mockClusterExecutor.setThrowException(true);

		long currentTime = System.currentTimeMillis();

		MockIndexAccessor mockIndexAccessor = new MockIndexAccessor(
			currentTime, currentTime);

		_setupLucenHelperImpl(mockIndexAccessor);

		ClusterNode clusterNode = _generateClusterNode(0);

		ClusterEvent clusterEvent = ClusterEvent.join(clusterNode);

		_fireClusterEventListeners(clusterEvent);

		Assert.assertEquals(1, logRecords.size());

		_assertLogger(
			logRecords.get(0),
			"Unable to load indexes for company " + currentTime,
			SystemException.class);
	}

	@AdviseWith(
		adviceClasses = {
			DisableIndexOnStartUpAdvice.class, EnableClusterLinkAdvice.class,
			EnableLuceneReplicateWriteAdvice.class
		}
	)
	@Test
	public void testLoadIndexClusterEventListener3() throws Exception {
		List<LogRecord> logRecords = JDKLoggerTestUtil.configureJDKLogger(
			LuceneHelperImpl.class.getName(), Level.FINE);

		_setupClusterExecutor(3, 0);

		_setupLucenHelperImpl(null);

		ClusterNode clusterNode = _generateClusterNode(0);

		ClusterEvent clusterEvent = ClusterEvent.join(clusterNode);

		_fireClusterEventListeners(clusterEvent);

		Assert.assertEquals(1, logRecords.size());

		_assertLogger(
			logRecords.get(0),
			"Number of original cluster members is greater than one", null);

		logRecords.clear();
	}

	@AdviseWith(
		adviceClasses = {
			DisableIndexOnStartUpAdvice.class, EnableClusterLinkAdvice.class,
			EnableLuceneReplicateWriteAdvice.class
		}
	)
	@Test
	public void testLoadIndexClusterEventListener4() throws Exception {
		List<LogRecord> logRecords = JDKLoggerTestUtil.configureJDKLogger(
			LuceneHelperImpl.class.getName(), Level.INFO);

		_setupClusterExecutor(3, 0);

		_setupLucenHelperImpl(null);

		ClusterNode clusterNode = _generateClusterNode(0);

		ClusterEvent clusterEvent = ClusterEvent.join(clusterNode);

		_fireClusterEventListeners(clusterEvent);

		Assert.assertEquals(0, logRecords.size());
	}

	@AdviseWith(
		adviceClasses = {
			DisableIndexOnStartUpAdvice.class, EnableClusterLinkAdvice.class,
			EnableLuceneReplicateWriteAdvice.class
		}
	)
	@Test
	public void testLoadIndexFromCluster1() throws Exception {
		JDKLoggerTestUtil.configureJDKLogger(
			LuceneHelperImpl.class.getName(), Level.INFO);

		_setupClusterExecutor(2, _port);

		final long currentTime = System.currentTimeMillis();

		MockIndexAccessor mockIndexAccessor = new MockIndexAccessor(
			currentTime, currentTime);

		final LuceneHelperImpl luceneHelperImpl = _setupLucenHelperImpl(
			mockIndexAccessor);

		MockServer mockServer = new MockServer(_port);

		mockServer.start();

		new Thread() {

			public void run() {
				try {
					luceneHelperImpl.loadIndexesFromCluster(currentTime);
				}
				catch (Exception ex) {
				}
			}

		}.start();

		String responseMessage = mockIndexAccessor.waitforResponseMessage();

		mockServer.close();

		Assert.assertEquals(_RESPONSE_MESSAGE, responseMessage);
	}

	@AdviseWith(
		adviceClasses = {
			DisableIndexOnStartUpAdvice.class, EnableClusterLinkAdvice.class,
			EnableLuceneReplicateWriteAdvice.class
		}
	)
	@Test
	public void testLoadIndexFromCluster2() throws Exception {
		List<LogRecord> logRecords = JDKLoggerTestUtil.configureJDKLogger(
			LuceneHelperImpl.class.getName(), Level.FINE);

		MockClusterExecutor mockClusterExecutor = _setupClusterExecutor(3, 0);

		mockClusterExecutor.setAutoResponse(false);

		long currentTime = System.currentTimeMillis();

		MockIndexAccessor mockIndexAccessor = new MockIndexAccessor(
			currentTime, currentTime);

		LuceneHelperImpl luceneHelperImpl = _setupLucenHelperImpl(
			mockIndexAccessor);

		luceneHelperImpl.loadIndexesFromCluster(currentTime);

		Assert.assertEquals(2, logRecords.size());

		_assertLogger(
			logRecords.get(0), "Unable to get cluster node response in 10000" +
				TimeUnit.MILLISECONDS,
			null);

		_assertLogger(
			logRecords.get(1), "Unable to get cluster node response in 10000" +
				TimeUnit.MILLISECONDS,
			null);

		logRecords.clear();
	}

	@AdviseWith(
		adviceClasses = {
			DisableIndexOnStartUpAdvice.class, EnableClusterLinkAdvice.class,
			EnableLuceneReplicateWriteAdvice.class
		}
	)
	@Test
	public void testLoadIndexFromCluster3() throws Exception {
		List<LogRecord> logRecords = JDKLoggerTestUtil.configureJDKLogger(
			LuceneHelperImpl.class.getName(), Level.FINE);

		_setupClusterExecutor(2, 0);

		long currentTime = System.currentTimeMillis();

		MockIndexAccessor mockIndexAccessor = new MockIndexAccessor(
			currentTime, currentTime);

		LuceneHelperImpl luceneHelperImpl = _setupLucenHelperImpl(
			mockIndexAccessor);

		luceneHelperImpl.loadIndexesFromCluster(currentTime);

		Assert.assertEquals(1, logRecords.size());

		_assertLogger(logRecords.get(0), "invalid port", null);

		logRecords.clear();
	}

	@AdviseWith(
		adviceClasses = {
			DisableIndexOnStartUpAdvice.class, EnableClusterLinkAdvice.class,
			EnableLuceneReplicateWriteAdvice.class
		}
	)
	@Test
	public void testLoadIndexFromCluster4() throws Exception {
		List<LogRecord> logRecords = JDKLoggerTestUtil.configureJDKLogger(
			LuceneHelperImpl.class.getName(), Level.FINE);

		_setupClusterExecutor(2, _port);

		long currentTime = System.currentTimeMillis();

		MockIndexAccessor mockIndexAccessor = new MockIndexAccessor(
			currentTime, currentTime);

		LuceneHelper luceneHelperImpl = _setupLucenHelperImpl(
			mockIndexAccessor);

		luceneHelperImpl.loadIndexesFromCluster(currentTime);

		Assert.assertEquals(2, logRecords.size());

		_assertLogger(
			logRecords.get(0),
			"Start loading lucene index files from cluster node", null);

		_assertLogger(
			logRecords.get(1),
			"Unable to load index for company " + currentTime,
			SystemException.class);

		logRecords.clear();
	}

	@AdviseWith(
		adviceClasses = {
			DisableIndexOnStartUpAdvice.class, EnableClusterLinkAdvice.class,
			EnableLuceneReplicateWriteAdvice.class
		}
	)
	@Test
	public void testLoadIndexFromCluster5() throws Exception {
		List<LogRecord> logRecords = JDKLoggerTestUtil.configureJDKLogger(
			LuceneHelperImpl.class.getName(), Level.FINE);

		MockClusterExecutor mockClusterExecutor = _setupClusterExecutor(
			2, _port);

		mockClusterExecutor.setInvokeMethodThrowException(true);

		long currentTime = System.currentTimeMillis();

		MockIndexAccessor mockIndexAccessor = new MockIndexAccessor(
			currentTime, currentTime);

		LuceneHelper luceneHelperImpl = _setupLucenHelperImpl(
			mockIndexAccessor);

		luceneHelperImpl.loadIndexesFromCluster(currentTime);

		Assert.assertEquals(1, logRecords.size());

		_assertLogger(
			logRecords.get(0),
			"Suppress exception caused by remote method invocation",
			Exception.class);

		logRecords.clear();
	}

	@AdviseWith(
		adviceClasses = {
			DisableIndexOnStartUpAdvice.class, EnableClusterLinkAdvice.class,
			EnableLuceneReplicateWriteAdvice.class
		}
	)
	@Test
	public void testLoadIndexFromCluster6() throws Exception {
		List<LogRecord> logRecords = JDKLoggerTestUtil.configureJDKLogger(
			LuceneHelperImpl.class.getName(), Level.FINE);

		_setupClusterExecutor(2, _port);

		long currentTime = System.currentTimeMillis() + 1000 * 100;

		MockIndexAccessor mockIndexAccessor = new MockIndexAccessor(
			currentTime, currentTime);

		LuceneHelper luceneHelperImpl = _setupLucenHelperImpl(
			mockIndexAccessor);

		luceneHelperImpl.loadIndexesFromCluster(currentTime);

		Assert.assertEquals(0, logRecords.size());
	}

	@AdviseWith(
		adviceClasses = {
			DisableIndexOnStartUpAdvice.class, EnableClusterLinkAdvice.class,
			EnableLuceneReplicateWriteAdvice.class
		}
	)
	@Test
	public void testLoadIndexFromCluster7() throws Exception {
		List<LogRecord> logRecords = JDKLoggerTestUtil.configureJDKLogger(
			LuceneHelperImpl.class.getName(), Level.FINE);

		_setupClusterExecutor(1, 0);

		long currentTime = System.currentTimeMillis();

		MockIndexAccessor mockIndexAccessor = new MockIndexAccessor(
			currentTime, currentTime);

		LuceneHelper luceneHelperImpl = _setupLucenHelperImpl(
			mockIndexAccessor);

		luceneHelperImpl.loadIndexesFromCluster(currentTime);

		Assert.assertEquals(1, logRecords.size());

		_assertLogger(
			logRecords.get(0),
			"Do not load indexes because there is either one portal instance " +
				"or no portal instances in the cluster",
			null);
	}

	@AdviseWith(
		adviceClasses = {
			DisableIndexOnStartUpAdvice.class, DisableClusterLinkAdvice.class,
			EnableLuceneReplicateWriteAdvice.class
		}
	)
	@Test
	public void testLoadIndexFromCluster8() throws Exception {
		List<LogRecord> logRecords = JDKLoggerTestUtil.configureJDKLogger(
			LuceneHelperImpl.class.getName(), Level.FINE);

		LuceneHelper luceneHelperImpl = _setupLucenHelperImpl(null);

		luceneHelperImpl.loadIndexesFromCluster(0);

		Assert.assertEquals(2, logRecords.size());

		_assertLogger(
			logRecords.get(0), "Load index from cluster is not enabled", null);

		_assertLogger(
			logRecords.get(0), "Load index from cluster is not enabled", null);
	}

	@Aspect
	public static class DisableClusterLinkAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues.CLUSTER_LINK_ENABLED)")
		public Object disableClusterLink(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[]{Boolean.FALSE});
		}

	}

	@Aspect
	public static class DisableIndexOnStartUpAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues.INDEX_ON_STARTUP)")
		public Object disableIndexOnStartUp(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[]{Boolean.FALSE});
		}

	}

	@Aspect
	public static class EnableClusterLinkAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues.CLUSTER_LINK_ENABLED)")
		public Object enableClusterLink(ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[]{Boolean.TRUE});
		}

	}

	@Aspect
	public static class EnableLuceneReplicateWriteAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues.LUCENE_REPLICATE_WRITE)")
		public Object enableLuceneReplicateWrite(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[]{Boolean.TRUE});
		}

	}

	private void _assertLogger(
		LogRecord logRecord, String message, Class<?> exceptionClass) {

		org.junit.Assert.assertTrue(logRecord.getMessage().contains(message));

		if (exceptionClass == null) {
			org.junit.Assert.assertNull(logRecord.getThrown());
		}
		else {
			Throwable throwable = logRecord.getThrown();

			org.junit.Assert.assertEquals(exceptionClass, throwable.getClass());
		}
	}

	private void _fireClusterEventListeners(ClusterEvent clusterEvent) {
		ClusterExecutor clusterExecutor =
			ClusterExecutorUtil.getClusterExecutor();

		List<ClusterEventListener> clusterEventListeners =
			clusterExecutor.getClusterEventListeners();

		for (
			ClusterEventListener clusterEventListener : clusterEventListeners) {

			clusterEventListener.processClusterEvent(clusterEvent);
		}
	}

	private ClusterNode _generateClusterNode(int port) {
		ClusterNode clusterNode = new ClusterNode(
			String.valueOf(System.currentTimeMillis()), _localhost);

		clusterNode.setPort(port);

		return clusterNode;
	}

	private int _getAvailablePort() {
		int port = 1024;

		ServerSocket serverSocket = null;

		while (true) {
			try {
				serverSocket = new ServerSocket(port);

				return port;
			}
			catch (IOException ex) {
				port++;

				continue;
			}
			finally {
				if (serverSocket != null) {
					try {
						serverSocket.close();
					}
					catch (IOException ex) {
					}
				}
			}
		}
	}

	private MockClusterExecutor _setupClusterExecutor(
		int clusterNodeNumber, int port) {

		MockClusterExecutor mockClusterExecutor = new MockClusterExecutor(
			clusterNodeNumber, port);

		ClusterExecutorUtil clusterExecutorUtil = new ClusterExecutorUtil();

		clusterExecutorUtil.setClusterExecutor(mockClusterExecutor);

		return mockClusterExecutor;
	}

	private LuceneHelperImpl _setupLucenHelperImpl(IndexAccessor indexAccessor)
		throws Exception {

		Class<LuceneHelperImpl> clazz = LuceneHelperImpl.class;

		Constructor<LuceneHelperImpl> luceneHelperImplConstructor =
			clazz.getDeclaredConstructor();

		luceneHelperImplConstructor.setAccessible(true);

		LuceneHelperImpl luceneHelperImpl =
			luceneHelperImplConstructor.newInstance();

		if (indexAccessor != null) {
			Field indexAccessorsField =clazz.getDeclaredField(
				"_indexAccessors");

			indexAccessorsField.setAccessible(true);

			Map<Long, IndexAccessor> indexAccessorMap =
				(Map<Long, IndexAccessor>)indexAccessorsField.get(
					luceneHelperImpl);

			indexAccessorMap.put(indexAccessor.getCompanyId(), indexAccessor);

			PortalInstances.addCompanyId(indexAccessor.getCompanyId());
		}

		LuceneHelperUtil luceneHelperUtil = new LuceneHelperUtil();

		luceneHelperUtil.setLuceneHelper(luceneHelperImpl);

		return luceneHelperImpl;
	}

	private static final String _RESPONSE_MESSAGE = "response message";

	private InetAddress _localhost;
	private int _port;

	private class MockAddress implements org.jgroups.Address {

		public int compareTo(org.jgroups.Address jGroupsAddress) {
			return 0;
		}

		public void readExternal(ObjectInput objectInput) {
		}

		public void readFrom(DataInput dataInput) throws Exception {
		}

		public int size() {
			return 0;
		}

		public void writeExternal(ObjectOutput objectOutput) {
		}

		public void writeTo(DataOutput dataOutput) throws Exception {
		}

	}

	private class MockClusterExecutor implements ClusterExecutor {

		public MockClusterExecutor(int nodeNumber, int port) {
			for (int i = 0; i < nodeNumber; i++) {
				_addresses.add(new AddressImpl(new MockAddress()));
			}

			_port = port;
		}

		public void addClusterEventListener(
			ClusterEventListener clusterEventListener) {

			_clusterEventListeners.add(clusterEventListener);
		}

		public void destroy() {
			_addresses.clear();
			_clusterEventListeners.clear();
		}

		public FutureClusterResponses execute(ClusterRequest clusterRequest)
			throws SystemException {

			if (_throwException) {
				throw new SystemException();
			}

			if (!_autoResponse) {
				return new FutureClusterResponses(Collections.EMPTY_LIST);
			}

			FutureClusterResponses futureClusterResponses =
				new FutureClusterResponses(_addresses);

			for (Address address : _addresses) {
				ClusterNodeResponse clusterNodeResponse =
					new ClusterNodeResponse();

				clusterNodeResponse.setAddress(address);
				clusterNodeResponse.setClusterMessageType(
					ClusterMessageType.EXECUTE);
				clusterNodeResponse.setMulticast(clusterRequest.isMulticast());
				clusterNodeResponse.setUuid(clusterRequest.getUuid());

				try {
					ClusterNode clusterNode = new ClusterNode(
						String.valueOf(System.currentTimeMillis()),
						InetAddress.getLocalHost());

					clusterNode.setPort(_port);

					clusterNodeResponse.setClusterNode(clusterNode);
				}
				catch (Exception ex) {
				}

				try {
					clusterNodeResponse.setResult(
						_invoke(clusterRequest.getMethodHandler()));
				}
				catch (Exception e) {
					clusterNodeResponse.setException(e);
				}

				futureClusterResponses.addClusterNodeResponse(
					clusterNodeResponse);
			}

			return futureClusterResponses;
		}

		public void execute(
				ClusterRequest clusterRequest,
				ClusterResponseCallback clusterResponseCallback)
			throws SystemException {

			FutureClusterResponses futureClusterResponses = execute(
				clusterRequest);

			try {
				clusterResponseCallback.callback(
					futureClusterResponses.get().getClusterResponses());
			}
			catch (InterruptedException ie) {
			}
		}

		public void execute(
				ClusterRequest clusterRequest,
				ClusterResponseCallback clusterResponseCallback, long timeout,
				TimeUnit timeUnit)
			throws SystemException {

			FutureClusterResponses futureClusterResponses = execute(
				clusterRequest);

			try {
				clusterResponseCallback.callback(
					futureClusterResponses.get(
						timeout, timeUnit).getClusterResponses());
			}
			catch (Exception e) {
			}
		}

		public List<ClusterEventListener> getClusterEventListeners() {
			return Collections.unmodifiableList(_clusterEventListeners);
		}

		public List<Address> getClusterNodeAddresses() {
			return Collections.unmodifiableList(_addresses);
		}

		public List<ClusterNode> getClusterNodes() {
			return Collections.EMPTY_LIST;
		}

		public ClusterNode getLocalClusterNode() {
			return null;
		}

		public Address getLocalClusterNodeAddress() {
			return _addresses.get(0);
		}

		public void initialize() {
		}

		public boolean isClusterNodeAlive(Address address) {
			return _addresses.contains(address);
		}

		public boolean isClusterNodeAlive(String clusterNodeId) {
			return false;
		}

		public boolean isEnabled() {
			return PropsValues.CLUSTER_LINK_ENABLED;
		}

		public void removeClusterEventListener(
			ClusterEventListener clusterEventListener) {

			_clusterEventListeners.remove(clusterEventListener);
		}

		public void setAutoResponse(boolean autoResponse) {
			_autoResponse = autoResponse;
		}

		public void setInvokeMethodThrowException(
			boolean invokeMethodThrowException) {

			_invokeMethodThrowException = invokeMethodThrowException;
		}

		public void setPort(int port) {
			_port = port;
		}

		public void setThrowException(boolean throwException) {
			_throwException = throwException;
		}

		private Object _invoke(MethodHandler methodHandler) throws Exception {
			if (_invokeMethodThrowException) {
				throw new Exception();
			}

			MethodKey methodKey = methodHandler.getMethodKey();

			if (methodKey.equals(_createTokenMethodKey)) {
				long timeToLive = (Long)methodHandler.getArguments()[0];

				return TransientTokenUtil.createToken(timeToLive);
			}
			else if (methodKey.equals(_getLastGenerationMethodKey)) {
				return System.currentTimeMillis();
			}

			return null;
		}

		private List<Address> _addresses = new ArrayList<Address>();
		private boolean _autoResponse = true;
		private List<ClusterEventListener> _clusterEventListeners =
			new ArrayList<ClusterEventListener>();
		private MethodKey _createTokenMethodKey = new MethodKey(
			TransientTokenUtil.class, "createToken", long.class);
		private MethodKey _getLastGenerationMethodKey = new MethodKey(
			LuceneHelperUtil.class, "getLastGeneration", long.class);
		private int _port;
		private boolean _throwException = false;
		private boolean _invokeMethodThrowException = false;

	}

	private class MockIndexAccessor implements IndexAccessor {

		public MockIndexAccessor(long companyId, long lastGeneration) {
			_companyId = companyId;
			_lastGeneration = lastGeneration;
		}

		public void addDocument(Document document) throws IOException {
		}

		public void close() {
		}

		public void delete() {
		}

		public void deleteDocuments(Term term) throws IOException {
		}

		public void dumpIndex(OutputStream outputStream) throws IOException {
		}

		public long getCompanyId() {
			return _companyId;
		}

		public long getLastGeneration() {
			return _lastGeneration;
		}

		public Directory getLuceneDir() {
			return null;
		}

		public void loadIndex(InputStream inputStream) throws IOException {
			inputStream = new UnsyncBufferedInputStream(inputStream);

			StringBuilder sb = new StringBuilder();

			while (true) {
				int c = inputStream.read();

				if (c == -1) {
					break;
				}

				sb.append((char)c);
			}

			try {
				_exchanger.exchange(sb.toString());
			}
			catch (InterruptedException ex) {
			}
		}

		public void updateDocument(Term term, Document document)
			throws IOException {
		}

		public String waitforResponseMessage() {
			try {
				return _exchanger.exchange(null, 15000, TimeUnit.MILLISECONDS);
			}
			catch (Exception te) {
				return null;
			}
		}

		private long _companyId;
		private long _lastGeneration;
		private Exchanger<String> _exchanger = new Exchanger<String>();

	}

	private class MockServer extends Thread {

		public MockServer(int port) {
			_port = port;
		}

		public boolean receiveRequest() {
			boolean receiveRequest = false;

			try {
				receiveRequest = _countDownLatch.await(
					15000, TimeUnit.MILLISECONDS);
			}
			catch (Exception e) {
			}

			return receiveRequest;
		}

		public void close() {
			if (_serverSocket != null) {
				try {
					_serverSocket.close();
				}
				catch (IOException ex) {
				}
			}
		}

		@Override
		public void run() {
			Socket connection = null;

			try {
				_serverSocket = new ServerSocket(_port, 1, _localhost);

				connection = _serverSocket.accept();

				_countDownLatch.countDown();

				InputStream inputStream =
					new UnsyncBufferedInputStream(connection.getInputStream());

				StringBuilder sb = new StringBuilder(80);

				while (true) {
					int c = inputStream.read();

					if ((c == -1) || (c == CharPool.RETURN) ||
						(c == CharPool.NEW_LINE)) {

						break;
					}

					sb.append((char)c);
				}

				String request = sb.toString();

				if (!request.contains("/lucene/dump")) {
					return;
				}

				StringBundler stringBundler = new StringBundler(4);

				stringBundler.append(
					"HTTP/1.0 200 OK\r\nServer: \r\nContent-length: ");
				stringBundler.append(_RESPONSE_MESSAGE.length());
				stringBundler.append("\r\nContent-type: text/plain\r\n\r\n");
				stringBundler.append(_RESPONSE_MESSAGE);

				OutputStream outputStream = connection.getOutputStream();

				outputStream.write(stringBundler.toString().getBytes());

				outputStream.close();
			}
			catch (Exception e) {
			}
			finally {
				if (connection != null) {
					try {
						connection.close();
					}
					catch (IOException ex) {
					}
				}
			}
		}

		private CountDownLatch _countDownLatch = new CountDownLatch(1);
		private int _port;
		private ServerSocket _serverSocket;

	}

}
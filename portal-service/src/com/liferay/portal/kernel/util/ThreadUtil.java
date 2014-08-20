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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.cluster.Address;
import com.liferay.portal.kernel.cluster.BaseClusterResponseCallback;
import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.cluster.ClusterNodeResponse;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.cluster.ClusterResponseCallback;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.kernel.zip.ZipWriterFactoryUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import java.text.Format;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Tina Tian
 * @author Shuyang Zhou
 * @author László Csontos
 */
public class ThreadUtil {

	public static Thread[] getThreads() {
		Thread currentThread = Thread.currentThread();

		ThreadGroup threadGroup = currentThread.getThreadGroup();

		while (threadGroup.getParent() != null) {
			threadGroup = threadGroup.getParent();
		}

		int threadCountGuess = threadGroup.activeCount();

		Thread[] threads = new Thread[threadCountGuess];

		int threadCountActual = threadGroup.enumerate(threads);

		while (threadCountActual == threadCountGuess) {
			threadCountGuess *= 2;

			threads = new Thread[threadCountGuess];

			threadCountActual = threadGroup.enumerate(threads);
		}

		return threads;
	}

	public static ThreadDumpResult takeThreadDump() {
		String threadDump = _getThreadDumpFromJstack();

		if (Validator.isNull(threadDump)) {
			threadDump = _getThreadDumpFromStackTrace();
		}

		return new ThreadDumpResult(threadDump);
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	public static String threadDump() {
		ThreadDumpResult threadDumpResult = takeThreadDump();

		return "\n\n".concat(threadDumpResult.getThreadDump());
	}

	public static void writeThreadDump(ThreadDumpType threadDumpType) {
		if (threadDumpType == null) {
			throw new IllegalArgumentException("threadDumpType cannot be null");
		}

		if (ThreadDumpType.CLUSTER_WIDE.equals(threadDumpType)) {
			ClusterRequest clusterRequest =
				ClusterRequest.createMulticastRequest(
					new MethodHandler(_TAKE_THREAD_DUMP_METHOD_KEY), false);

			List<Address> clusterNodeAddresses =
				ClusterExecutorUtil.getClusterNodeAddresses();

			ClusterResponseCallback threadDumpClusterResponseCallback =
				new ThreadDumpClusterResponseCallback(clusterNodeAddresses);

			ClusterExecutorUtil.execute(
				clusterRequest, threadDumpClusterResponseCallback);

			if (_log.isInfoEnabled()) {
				_log.info(
					"Cluster wide thread dump request has been submitted.");
			}

			return;
		}

		ThreadDumpResult threadDumpResult = takeThreadDump();

		File threadDumpFile = _getThreadDumpFile(
			threadDumpType, threadDumpResult.getTakenAt(),
			threadDumpResult.getTargetHost());

		try {
			FileUtil.write(threadDumpFile, threadDumpResult.getThreadDump());

			if (_log.isInfoEnabled()) {
				_log.info("Thread dump has been written to " + threadDumpFile);
			}
		}
		catch (IOException ioe) {
			_log.error(ioe);
		}
	}

	private static String _getThreadDumpDestDir() {
		String destDir = PropsUtil.get(PropsKeys.THREAD_DUMP_DEST_DIR);

		if (Validator.isBlank(destDir)) {
			destDir = SystemProperties.get(SystemProperties.TMP_DIR);
		}

		if (!FileUtil.exists(destDir)) {
			FileUtil.mkdirs(destDir);
		}

		return destDir;
	}

	private static File _getThreadDumpFile(
		ThreadDumpType threadDumpType, Date takenAt, String targetHost) {

		int size = 6;

		if (Validator.isNotNull(targetHost)) {
			size = 8;
		}

		StringBundler sb = new StringBundler(size);

		sb.append(threadDumpType.getDescription());
		sb.append("ThreadDump");

		if (takenAt == null) {
			takenAt = new Date();
		}

		sb.append(StringPool.DASH);
		sb.append(_ISO_DATE_FORMAT.format(takenAt));

		if (Validator.isNotNull(targetHost)) {
			sb.append(StringPool.DASH);
			sb.append(targetHost);
		}

		sb.append(StringPool.PERIOD);

		String extension = "txt";

		if (ThreadDumpType.CLUSTER_WIDE.equals(threadDumpType)) {
			extension = "zip";
		}

		sb.append(extension);

		File threadDumpFile = new File(_getThreadDumpDestDir(), sb.toString());

		return threadDumpFile;
	}

	private static String _getThreadDumpFromJstack() {
		UnsyncByteArrayOutputStream outputStream =
			new UnsyncByteArrayOutputStream();

		try {
			String vendorURL = System.getProperty("java.vendor.url");

			if (!vendorURL.equals("http://java.oracle.com/") &&
				!vendorURL.equals("http://java.sun.com/")) {

				return StringPool.BLANK;
			}

			RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

			String name = runtimeMXBean.getName();

			if (Validator.isNull(name)) {
				return StringPool.BLANK;
			}

			int pos = name.indexOf(CharPool.AT);

			if (pos == -1) {
				return StringPool.BLANK;
			}

			String pidString = name.substring(0, pos);

			if (!Validator.isNumber(pidString)) {
				return StringPool.BLANK;
			}

			Runtime runtime = Runtime.getRuntime();

			int pid = GetterUtil.getInteger(pidString);

			String[] cmd = new String[] {"jstack", String.valueOf(pid)};

			Process process = runtime.exec(cmd);

			InputStream inputStream = process.getInputStream();

			StreamUtil.transfer(inputStream, outputStream);
		}
		catch (Exception e) {
		}

		return outputStream.toString();
	}

	private static String _getThreadDumpFromStackTrace() {
		String jvm =
			System.getProperty("java.vm.name") + " " +
				System.getProperty("java.vm.version");

		StringBundler sb = new StringBundler(
			"Full thread dump of " + jvm + " on " + String.valueOf(new Date()) +
				"\n\n");

		Map<Thread, StackTraceElement[]> stackTraces =
			Thread.getAllStackTraces();

		for (Map.Entry<Thread, StackTraceElement[]> entry :
				stackTraces.entrySet()) {

			Thread thread = entry.getKey();
			StackTraceElement[] elements = entry.getValue();

			sb.append(StringPool.QUOTE);
			sb.append(thread.getName());
			sb.append(StringPool.QUOTE);

			if (thread.getThreadGroup() != null) {
				sb.append(StringPool.SPACE);
				sb.append(StringPool.OPEN_PARENTHESIS);
				sb.append(thread.getThreadGroup().getName());
				sb.append(StringPool.CLOSE_PARENTHESIS);
			}

			sb.append(", priority=");
			sb.append(thread.getPriority());
			sb.append(", id=");
			sb.append(thread.getId());
			sb.append(", state=");
			sb.append(thread.getState());
			sb.append("\n");

			for (int i = 0; i < elements.length; i++) {
				sb.append("\t");
				sb.append(elements[i]);
				sb.append("\n");
			}

			sb.append("\n");
		}

		return sb.toString();
	}

	private static final Format _ISO_DATE_FORMAT =
		FastDateFormatFactoryUtil.getSimpleDateFormat("yyyyMMdd'T'HHmmssz");

	private static final MethodKey _TAKE_THREAD_DUMP_METHOD_KEY = new MethodKey(
		ThreadUtil.class, "takeThreadDump");

	private static final int _THREAD_DUMP_CLUSTER_WIDE_TIMEOUT =
		GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.THREAD_DUMP_CLUSTER_WIDE_TIMEOUT));

	private static Log _log = LogFactoryUtil.getLog(ThreadUtil.class);

	private static class ThreadDumpClusterResponseCallback
		extends BaseClusterResponseCallback {

		@Override
		public void callback(BlockingQueue<ClusterNodeResponse> blockingQueue) {
			_beginDumpBundle();

			do {
				_clusterNodeAddressCount--;

				ClusterNodeResponse clusterNodeResponse = null;

				try {
					clusterNodeResponse = blockingQueue.poll(
						_THREAD_DUMP_CLUSTER_WIDE_TIMEOUT, TimeUnit.SECONDS);
				}
				catch (InterruptedException ie) {
					_log.error(
						"Unable to get cluster node response in " +
							_THREAD_DUMP_CLUSTER_WIDE_TIMEOUT +
							TimeUnit.SECONDS);
				}

				if (clusterNodeResponse == null) {
					continue;
				}

				Address clusterNodeAddress = clusterNodeResponse.getAddress();

				if (_log.isDebugEnabled()) {
					_log.debug(
						"Processing response of node " + clusterNodeAddress);
				}

				ThreadDumpResult threadDumpResult = null;

				boolean addDumpSuccess = false;

				try {
					threadDumpResult =
						(ThreadDumpResult)clusterNodeResponse.getResult();

					_silentClusterNodeAddresses.remove(clusterNodeAddress);
				}
				catch (Exception e) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							"Exception occured on node " +
								clusterNodeAddress, e);
					}

					addDumpSuccess = _addDump(clusterNodeAddress, e);
				}

				if (threadDumpResult == null) {
					continue;
				}

				addDumpSuccess = _addDump(threadDumpResult);

				if (!addDumpSuccess) {
					_log.error(
						"Writing thread dump bundle has failed; aborting.");

					return;
				}
			}
			while (_clusterNodeAddressCount > 0);

			// Silent nodes

			if (_log.isWarnEnabled() &&
				!_silentClusterNodeAddresses.isEmpty()) {

				_log.warn(
					"The following nodes gave no response: " +
						StringUtil.merge(_silentClusterNodeAddresses));
			}

			_endDumpBundle();
		}

		@Override
		public void processInterruptedException(
			InterruptedException interruptedException) {

			if (_log.isWarnEnabled()) {
				_log.warn(
					"Cluster wide thread dump generation has been " +
						"interrupted; sealing bundle.");
			}

			_endDumpBundle();
		}

		@Override
		public void processTimeoutException(TimeoutException timeoutException) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Waiting for cluster nodes has timed out; sealing bundle.");
			}

			_endDumpBundle();
		}

		private ThreadDumpClusterResponseCallback(
			List<Address> clusterNodeAddresses) {

			_clusterNodeAddressCount = clusterNodeAddresses.size();

			_silentClusterNodeAddresses = SetUtil.fromList(
				clusterNodeAddresses);
		}

		private boolean _addDump(Address clusterNodeAddress, Exception e) {
			File threadDumpFile = _getThreadDumpFile(
				ThreadDumpType.LOCAL, new Date(),
				clusterNodeAddress.getDescription());

			String stackTrace = StackTraceUtil.getStackTrace(e);

			return _addZipEntry(threadDumpFile, stackTrace);
		}

		private boolean _addDump(ThreadDumpResult threadDumpResult) {
			File threadDumpFile = _getThreadDumpFile(
				ThreadDumpType.LOCAL, threadDumpResult.getTakenAt(),
				threadDumpResult.getTargetHost());

			return _addZipEntry(
				threadDumpFile, threadDumpResult.getThreadDump());
		}

		private boolean _addZipEntry(File file, String content) {
			try {
				_zipWriter.addEntry(StringPool.SLASH + file.getName(), content);
			}
			catch (IOException ioe) {
				_log.error(ioe);

				return false;
			}

			return true;
		}

		private void _beginDumpBundle() {
			_zipWriter = ZipWriterFactoryUtil.getZipWriter();
		}

		private void _endDumpBundle() {
			boolean success = false;

			try {
				File threadDumpsFile = _getThreadDumpFile(
					ThreadDumpType.CLUSTER_WIDE, null, null);

				success = FileUtil.move(_zipWriter.getFile(), threadDumpsFile);

				if (_log.isInfoEnabled() && success) {
					_log.info(
						"Cluster wide thread dump has been written to " +
							threadDumpsFile);
				}
			}
			catch (Exception e) {
				success = false;

				if (_log.isDebugEnabled()) {
					_log.debug(e);
				}
			}

			if (!success) {
				_log.error("Cluster wide thread dump generation has failed.");
			}
		}

		private int _clusterNodeAddressCount;
		private Set<Address> _silentClusterNodeAddresses;
		private ZipWriter _zipWriter;

	}

}
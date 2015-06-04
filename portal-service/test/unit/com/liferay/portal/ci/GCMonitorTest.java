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

package com.liferay.portal.ci;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.process.ProcessUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HeapUtil;

import java.io.BufferedReader;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class GCMonitorTest {

	@Test
	public void testGCLog() throws Exception {
		if (!_shouldRun()) {
			return;
		}

		Path gcLogPath = Paths.get(
			GetterUtil.get(
				System.getProperty("gc.log.path"), "/tmp/ant-gc.log"));

		Assert.assertTrue(
			"GC log file " + gcLogPath + " does not exist",
			Files.exists(gcLogPath));

		int continuousFullGCCountHeapDumpThreshold = GetterUtil.getInteger(
			System.getProperty("continuous.full.gc.count.heap.dump.threshold"),
			10);

		String fullGCKey = GetterUtil.getString(
			System.getProperty("full.gc.key"), "[Full GC (");

		int continuousFullGCCount = 0;

		try (BufferedReader bufferedReader = Files.newBufferedReader(
			gcLogPath, Charset.defaultCharset())) {

			String line = bufferedReader.readLine();

			if (line.contains(fullGCKey)) {
				if (++continuousFullGCCount >
						continuousFullGCCountHeapDumpThreshold) {

					_log.error(
						"Detected continuous full gc count passed threshold " +
							continuousFullGCCountHeapDumpThreshold +
								", generating heap dump.");

					String heapDumpPath = GetterUtil.getString(
						System.getProperty("heap.dump.path"),
						System.getProperty("java.io.tmpdir") + "/ant-process-" +
							HeapUtil.getProcessId() + "-heap-dump.bin");

					Future<?> future = HeapUtil.heapDump(
						true, true, heapDumpPath,
						ProcessUtil.ECHO_OUTPUT_PROCESSOR);

					future.get();

					_log.error("Generated heap dump at " + heapDumpPath);
				}
			}
			else {
				continuousFullGCCount = 0;
			}
		}

		if (_log.isInfoEnabled()) {
			_log.info(gcLogPath + " looks healthy");
		}
	}

	private boolean _shouldRun() {
//		String hostname = System.getenv("HOSTNAME");
//
//		if (hostname == null) {
//			if (_log.isWarnEnabled()) {
//				_log.warn(
//					"Unable to detect HOSTNAME env variable, skip running");
//			}
//
//			return false;
//		}
//
//		if (!hostname.endsWith(".lax.liferay.com")) {
//			if (_log.isWarnEnabled()) {
//				_log.warn(
//					"Unknow hostname " + hostname +
//						", not on CI? Skip running");
//			}
//
//			return false;
//		}
//
//		return true;

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(GCMonitorTest.class);

}
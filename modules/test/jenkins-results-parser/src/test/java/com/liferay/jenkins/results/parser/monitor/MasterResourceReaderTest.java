/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.jenkins.results.parser.monitor;

import com.liferay.jenkins.results.parser.RandomTestUtil;

import java.io.IOException;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Brittney Nguyen
 */
public class MasterResourceReaderTest
	extends com.liferay.jenkins.results.parser.Test {

	@Test
	public void testClearInstances() {
		String masterName = MonitorTestUtil.newJenkinsMasterName();

		MasterResourceReader masterResourceReader =
			MasterResourceReader.getInstance(masterName);

		MasterResourceReader.clearInstances();

		Assert.assertNotSame(
			masterResourceReader, MasterResourceReader.getInstance(masterName));
	}

	@Test
	public void testGetInstance() {
		String masterName = MonitorTestUtil.newJenkinsMasterName();

		testSame(
			MasterResourceReader.getInstance(masterName),
			MasterResourceReader.getInstance(masterName));

		Assert.assertNotSame(
			MasterResourceReader.getInstance(masterName),
			MasterResourceReader.getInstance(
				MonitorTestUtil.newJenkinsMasterName()));
	}

	@Test
	public void testGetJobJSONObjects() throws Exception {
		String jobName = RandomTestUtil.randomString();

		mockURLReaders();

		setURLReaderOutput(_newJobsContent(jobName), "/api/json?tree=jobs");

		MasterResourceReader masterResourceReader =
			MasterResourceReader.getInstance(
				MonitorTestUtil.newJenkinsMasterName());

		Map<String, JSONObject> jobJSONObjects =
			masterResourceReader.getJobJSONObjects(_MILLIS_TIMEOUT);

		JSONObject jobJSONObject = jobJSONObjects.get(jobName);

		testEquals(jobName, jobJSONObject.optString("name"));

		testEquals(null, jobJSONObjects.get(RandomTestUtil.randomString()));

		testSame(
			jobJSONObjects,
			masterResourceReader.getJobJSONObjects(_MILLIS_TIMEOUT));
	}

	@Test
	public void testGetJobJSONObjectsIsUnmodifiable() throws Exception {
		mockURLReaders();

		setURLReaderOutput(
			_newJobsContent(RandomTestUtil.randomString()),
			"/api/json?tree=jobs");

		MasterResourceReader masterResourceReader =
			MasterResourceReader.getInstance(
				MonitorTestUtil.newJenkinsMasterName());

		Map<String, JSONObject> jobJSONObjects =
			masterResourceReader.getJobJSONObjects(_MILLIS_TIMEOUT);

		try {
			jobJSONObjects.put(RandomTestUtil.randomString(), new JSONObject());

			Assert.fail();
		}
		catch (UnsupportedOperationException unsupportedOperationException) {
		}
	}

	@Test
	public void testGetJobJSONObjectsWithReadFailure() throws Exception {
		mockURLReaders();

		setURLReaderException(
			new IOException(RandomTestUtil.randomString()),
			"/api/json?tree=jobs");

		MasterResourceReader masterResourceReader =
			MasterResourceReader.getInstance(
				MonitorTestUtil.newJenkinsMasterName());

		try {
			masterResourceReader.getJobJSONObjects(_MILLIS_TIMEOUT);

			Assert.fail();
		}
		catch (Exception exception) {
		}

		String jobName = RandomTestUtil.randomString();

		setURLReaderOutput(_newJobsContent(jobName), "/api/json?tree=jobs");

		Map<String, JSONObject> jobJSONObjects =
			masterResourceReader.getJobJSONObjects(_MILLIS_TIMEOUT);

		Assert.assertNotNull(jobJSONObjects.get(jobName));
	}

	@Test
	public void testGetMemoryInfo() throws Exception {
		setShellCommandOutput(
			"cat /proc/meminfo", mockShell(),
			MonitorTestUtil.newMemoryInfo(23791372L, 32249488L));

		String masterName = MonitorTestUtil.newJenkinsMasterName();

		MasterResourceReader masterResourceReader =
			MasterResourceReader.getInstance(masterName);

		testSame(
			masterResourceReader.getMemoryInfo(_MILLIS_TIMEOUT),
			masterResourceReader.getMemoryInfo(_MILLIS_TIMEOUT));
	}

	@Test
	public void testGetMemoryInfoWithoutPrometheusScrape() throws Exception {
		mockURLReaders();

		String memoryInfo = MonitorTestUtil.newMemoryInfo(23791372L, 32249488L);

		setShellCommandOutput("cat /proc/meminfo", mockShell(), memoryInfo);

		String masterName = MonitorTestUtil.newJenkinsMasterName();

		MasterResourceReader masterResourceReader =
			MasterResourceReader.getInstance(masterName);

		testEquals(
			memoryInfo, masterResourceReader.getMemoryInfo(_MILLIS_TIMEOUT));
	}

	@Test
	public void testGetMemoryInfoWithoutTimeout() throws Exception {
		String memoryInfo = MonitorTestUtil.newMemoryInfo(23791372L, 32249488L);

		setShellCommandOutput("cat /proc/meminfo", mockShell(), memoryInfo);

		String masterName = MonitorTestUtil.newJenkinsMasterName();

		MasterResourceReader masterResourceReader =
			MasterResourceReader.getInstance(masterName);

		testEquals(memoryInfo, masterResourceReader.getMemoryInfo());
	}

	@Test
	public void testGetPrometheusScrape() throws Exception {
		mockURLReaders();

		setURLReaderOutput(
			MonitorTestUtil.newSample(
				"label", RandomTestUtil.randomString(),
				MonitorTestUtil.newMetricName(), "1.0"),
			"/prometheus");

		String masterName = MonitorTestUtil.newJenkinsMasterName();

		MasterResourceReader masterResourceReader =
			MasterResourceReader.getInstance(masterName);

		testSame(
			masterResourceReader.getPrometheusScrape(_MILLIS_TIMEOUT),
			masterResourceReader.getPrometheusScrape(_MILLIS_TIMEOUT));
	}

	@Test
	public void testGetPrometheusScrapeWithEmptyContent() throws Exception {
		mockURLReaders();

		setURLReaderOutput("", "/prometheus");

		String masterName = MonitorTestUtil.newJenkinsMasterName();

		MasterResourceReader masterResourceReader =
			MasterResourceReader.getInstance(masterName);

		PrometheusScrape prometheusScrape =
			masterResourceReader.getPrometheusScrape(_MILLIS_TIMEOUT);

		String labelValue = RandomTestUtil.randomString();
		String name = MonitorTestUtil.newMetricName();

		setURLReaderOutput(
			MonitorTestUtil.newSample("label", labelValue, name, "1.0"),
			"/prometheus");

		testSame(
			prometheusScrape,
			masterResourceReader.getPrometheusScrape(_MILLIS_TIMEOUT));
		testEquals(null, prometheusScrape.getValue("label", labelValue, name));

		MasterResourceReader.clearInstances();

		masterResourceReader = MasterResourceReader.getInstance(masterName);

		prometheusScrape = masterResourceReader.getPrometheusScrape(
			_MILLIS_TIMEOUT);

		testEquals(1.0D, prometheusScrape.getValue("label", labelValue, name));
	}

	@Test
	public void testGetPrometheusScrapeWithReadFailure() throws Exception {
		mockURLReaders();

		setURLReaderException(
			new IOException(RandomTestUtil.randomString()), "/prometheus");

		String masterName = MonitorTestUtil.newJenkinsMasterName();

		MasterResourceReader masterResourceReader =
			MasterResourceReader.getInstance(masterName);

		try {
			masterResourceReader.getPrometheusScrape(_MILLIS_TIMEOUT);

			Assert.fail();
		}
		catch (IOException ioException) {
		}

		String labelValue = RandomTestUtil.randomString();
		String name = MonitorTestUtil.newMetricName();

		setURLReaderOutput(
			MonitorTestUtil.newSample("label", labelValue, name, "1.0"),
			"/prometheus");

		PrometheusScrape prometheusScrape =
			masterResourceReader.getPrometheusScrape(_MILLIS_TIMEOUT);

		testEquals(1.0D, prometheusScrape.getValue("label", labelValue, name));
	}

	@Test
	public void testGetPrometheusScrapeWithoutMemoryInfo() throws Exception {
		mockShell();
		mockURLReaders();

		String labelValue = RandomTestUtil.randomString();
		String name = MonitorTestUtil.newMetricName();

		setURLReaderOutput(
			MonitorTestUtil.newSample("label", labelValue, name, "1.0"),
			"/prometheus");

		String masterName = MonitorTestUtil.newJenkinsMasterName();

		MasterResourceReader masterResourceReader =
			MasterResourceReader.getInstance(masterName);

		PrometheusScrape prometheusScrape =
			masterResourceReader.getPrometheusScrape(_MILLIS_TIMEOUT);

		testEquals(1.0D, prometheusScrape.getValue("label", labelValue, name));
	}

	private String _newJobsContent(String jobName) {
		JSONObject jobsJSONObject = new JSONObject(
		).put(
			"jobs",
			new JSONArray(
			).put(
				new JSONObject(
				).put(
					"buildable", true
				).put(
					"disabled", false
				).put(
					"name", jobName
				)
			)
		);

		return jobsJSONObject.toString();
	}

	private static final int _MILLIS_TIMEOUT = 1000;

}
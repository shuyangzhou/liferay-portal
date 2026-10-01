/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.solr8.internal.logging;

import com.liferay.portal.kernel.search.MatchAllQuery;
import com.liferay.portal.search.solr8.internal.SolrIndexSearcher;
import com.liferay.portal.search.solr8.internal.SolrUnitTestRequirements;
import com.liferay.portal.search.solr8.internal.indexing.SolrIndexingFixture;
import com.liferay.portal.search.solr8.internal.search.engine.adapter.search.CountSearchRequestExecutor;
import com.liferay.portal.search.solr8.internal.search.engine.adapter.search.SearchSearchRequestExecutor;
import com.liferay.portal.search.test.util.indexing.BaseIndexingTestCase;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LogEntry;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Bryan Engler
 */
public class SolrIndexSearcherLoggingTest extends BaseIndexingTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		Assume.assumeTrue(
			SolrUnitTestRequirements.isSolrExternallyStartedByDeveloper());
	}

	@Test
	public void testCountSearchRequestExecutorLogsViaIndexer() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				CountSearchRequestExecutor.class.getName(),
				LoggerTestUtil.DEBUG)) {

			searchCount(createSearchContext(), new MatchAllQuery());

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 2, logEntries.size());

			LogEntry logEntry = logEntries.get(0);

			_assertLogEntry("Search query", logEntry, LoggerTestUtil.DEBUG);

			String message = logEntry.getMessage();

			Assert.assertTrue(
				message + " does not contain rows=0",
				message.contains("rows=0"));

			_assertLogEntry(
				"The search engine processed", logEntries.get(1),
				LoggerTestUtil.DEBUG);
		}
	}

	@Test
	public void testIndexerSearchCountLogs() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexSearcher.class.getName(), LoggerTestUtil.INFO)) {

			searchCount(createSearchContext(), new MatchAllQuery());

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 2, logEntries.size());

			_assertLogEntry(
				"The search engine processed", logEntries.get(0),
				LoggerTestUtil.INFO);
			_assertLogEntry(
				"Searching took", logEntries.get(1), LoggerTestUtil.INFO);
		}
	}

	@Test
	public void testIndexerSearchLogs() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexSearcher.class.getName(), LoggerTestUtil.INFO)) {

			search(createSearchContext());

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 2, logEntries.size());

			_assertLogEntry(
				"The search engine processed", logEntries.get(0),
				LoggerTestUtil.INFO);
			_assertLogEntry(
				"Searching took", logEntries.get(1), LoggerTestUtil.INFO);
		}
	}

	@Test
	public void testSearchSearchRequestExecutorLogsViaIndexer() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SearchSearchRequestExecutor.class.getName(),
				LoggerTestUtil.DEBUG)) {

			search(createSearchContext());

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 2, logEntries.size());

			LogEntry logEntry = logEntries.get(0);

			_assertLogEntry("Search query", logEntry, LoggerTestUtil.DEBUG);

			String message = logEntry.getMessage();

			Assert.assertTrue(
				message + " does not contain rows=20",
				message.contains("rows=20"));

			_assertLogEntry(
				"The search engine processed", logEntries.get(1),
				LoggerTestUtil.DEBUG);
		}
	}

	@Override
	protected IndexingFixture createIndexingFixture() throws Exception {
		return new SolrIndexingFixture();
	}

	private void _assertLogEntry(
		String expectedMessage, LogEntry logEntry, String logLevel) {

		Assert.assertEquals(logLevel, logEntry.getPriority());

		String message = logEntry.getMessage();

		Assert.assertTrue(
			message + " does not start with " + expectedMessage,
			message.startsWith(expectedMessage));
	}

}
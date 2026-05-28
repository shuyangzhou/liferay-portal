/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.logging;

import com.liferay.portal.kernel.search.MatchAllQuery;
import com.liferay.portal.search.opensearch2.internal.OpenSearchIndexSearcher;
import com.liferay.portal.search.opensearch2.internal.OpenSearchTestRule;
import com.liferay.portal.search.opensearch2.internal.indexing.LiferayOpenSearchIndexingFixtureFactory;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.search.CountSearchRequestExecutor;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.search.SearchSearchRequestExecutor;
import com.liferay.portal.search.test.util.indexing.BaseIndexingTestCase;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LogEntry;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Bryan Engler
 * @author André de Oliveira
 */
public class OpenSearchIndexSearcherLoggingTest extends BaseIndexingTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@ClassRule
	public static OpenSearchTestRule openSearchTestRule =
		OpenSearchTestRule.INSTANCE;

	@Test
	public void testCountSearchRequestExecutorLogsViaIndexer() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				CountSearchRequestExecutor.class.getName(),
				LoggerTestUtil.DEBUG)) {

			searchCount(createSearchContext(), new MatchAllQuery());

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 1, logEntries.size());

			_assertLogEntry(
				logEntries.get(0), "The search engine processed",
				LoggerTestUtil.DEBUG);
		}
	}

	@Test
	public void testIndexerSearchCountLogs() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				OpenSearchIndexSearcher.class.getName(), LoggerTestUtil.INFO)) {

			searchCount(createSearchContext(), new MatchAllQuery());

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 2, logEntries.size());

			_assertLogEntry(
				logEntries.get(0), "The search engine processed",
				LoggerTestUtil.INFO);
			_assertLogEntry(
				logEntries.get(1), "Searching took", LoggerTestUtil.INFO);
		}
	}

	@Test
	public void testIndexerSearchLogs() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				OpenSearchIndexSearcher.class.getName(), LoggerTestUtil.INFO)) {

			search(createSearchContext());

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 2, logEntries.size());

			_assertLogEntry(
				logEntries.get(0), "The search engine processed",
				LoggerTestUtil.INFO);
			_assertLogEntry(
				logEntries.get(1), "Searching took", LoggerTestUtil.INFO);
		}
	}

	@Test
	public void testSearchSearchRequestExecutorLogs() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SearchSearchRequestExecutor.class.getName(),
				LoggerTestUtil.DEBUG)) {

			search(createSearchContext());

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 1, logEntries.size());

			_assertLogEntry(
				logEntries.get(0), "The search engine processed",
				LoggerTestUtil.DEBUG);
		}
	}

	@Test
	public void testSearchSearchRequestExecutorLogsPrettyPrintedString() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SearchSearchRequestExecutor.class.getName(),
				LoggerTestUtil.DEBUG)) {

			search(createSearchContext());

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 1, logEntries.size());

			_assertLogEntry(
				logEntries.get(0), "The search engine processed",
				LoggerTestUtil.DEBUG);
		}
	}

	@Test
	public void testSearchSearchRequestExecutorLogsViaIndexer() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				OpenSearchIndexSearcher.class.getName(), LoggerTestUtil.INFO)) {

			search(createSearchContext());

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 2, logEntries.size());

			_assertLogEntry(
				logEntries.get(0), "The search engine processed",
				LoggerTestUtil.INFO);
			_assertLogEntry(
				logEntries.get(1), "Searching took", LoggerTestUtil.INFO);
		}
	}

	@Override
	protected IndexingFixture createIndexingFixture() {
		return LiferayOpenSearchIndexingFixtureFactory.getInstance();
	}

	private void _assertLogEntry(
		LogEntry logEntry, String expectedMessage, String logLevel) {

		Assert.assertEquals(logLevel, logEntry.getPriority());

		String message = logEntry.getMessage();

		Assert.assertTrue(
			message + " does not start with " + expectedMessage,
			message.startsWith(expectedMessage));
	}

}
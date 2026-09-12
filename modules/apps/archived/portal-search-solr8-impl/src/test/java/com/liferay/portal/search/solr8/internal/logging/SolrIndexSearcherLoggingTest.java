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
import java.util.function.Consumer;

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

			_assertLogEntry(
				message -> {
					Assert.assertTrue(
						message + " does not start with Search query",
						message.startsWith("Search query"));
					Assert.assertTrue(
						message + " does not contain rows=0",
						message.contains("rows=0"));
				},
				logEntries.get(0), LoggerTestUtil.DEBUG);

			_assertLogEntry(
				message -> Assert.assertTrue(
					message + " does not start with " +
						_SEARCH_ENGINE_PROCESSED,
					message.startsWith(_SEARCH_ENGINE_PROCESSED)),
				logEntries.get(1), LoggerTestUtil.DEBUG);
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
				message -> Assert.assertTrue(
					message + " does not start with " +
						_SEARCH_ENGINE_PROCESSED,
					message.startsWith(_SEARCH_ENGINE_PROCESSED)),
				logEntries.get(0), LoggerTestUtil.INFO);

			_assertLogEntry(
				message -> Assert.assertTrue(
					message + " does not start with Searching took",
					message.startsWith("Searching took")),
				logEntries.get(1), LoggerTestUtil.INFO);
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
				message -> Assert.assertTrue(
					message + " does not start with " +
						_SEARCH_ENGINE_PROCESSED,
					message.startsWith(_SEARCH_ENGINE_PROCESSED)),
				logEntries.get(0), LoggerTestUtil.INFO);

			_assertLogEntry(
				message -> Assert.assertTrue(
					message + " does not start with Searching took",
					message.startsWith("Searching took")),
				logEntries.get(1), LoggerTestUtil.INFO);
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

			_assertLogEntry(
				message -> {
					Assert.assertTrue(
						message + " does not start with Search query",
						message.startsWith("Search query"));
					Assert.assertTrue(
						message + " does not contain rows=20",
						message.contains("rows=20"));
				},
				logEntries.get(0), LoggerTestUtil.DEBUG);

			_assertLogEntry(
				message -> Assert.assertTrue(
					message + " does not start with " +
						_SEARCH_ENGINE_PROCESSED,
					message.startsWith(_SEARCH_ENGINE_PROCESSED)),
				logEntries.get(1), LoggerTestUtil.DEBUG);
		}
	}

	@Override
	protected IndexingFixture createIndexingFixture() throws Exception {
		return new SolrIndexingFixture();
	}

	private void _assertLogEntry(
		Consumer<String> consumer, LogEntry logEntry, String logLevel) {

		Assert.assertEquals(logLevel, logEntry.getPriority());
		consumer.accept(logEntry.getMessage());
	}

	private static final String _SEARCH_ENGINE_PROCESSED =
		"The search engine processed";

}
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.solr8.internal.logging;

import com.liferay.portal.kernel.search.MatchAllQuery;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.CountSearchRequest;
import com.liferay.portal.search.engine.adapter.search.MultisearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.query.QueriesUtil;
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
public class SolrSearchEngineAdapterLoggingTest extends BaseIndexingTestCase {

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
	public void testCountSearchRequestExecutorLogs() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				CountSearchRequestExecutor.class.getName(),
				LoggerTestUtil.DEBUG)) {

			SearchEngineAdapter searchEngineAdapter = getSearchEngineAdapter();

			searchEngineAdapter.execute(
				new CountSearchRequest() {
					{
						setIndexNames(_INDEX_NAME);
						setQuery(new MatchAllQuery());
					}
				});

			_assertLogCapture("rows=0", logCapture);
		}
	}

	@Test
	public void testMultisearchSearchRequestExecutorLogs() {
		SearchEngineAdapter searchEngineAdapter = getSearchEngineAdapter();

		try {
			searchEngineAdapter.execute(
				new MultisearchSearchRequest() {
					{
						addSearchSearchRequest(
							new SearchSearchRequest() {
								{
									setIndexNames(_INDEX_NAME);
									setQuery(new MatchAllQuery());
								}
							});
					}
				});

			Assert.fail();
		}
		catch (UnsupportedOperationException unsupportedOperationException) {
		}
	}

	@Test
	public void testSearchSearchRequestExecutorLogs() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SearchSearchRequestExecutor.class.getName(),
				LoggerTestUtil.DEBUG)) {

			SearchEngineAdapter searchEngineAdapter = getSearchEngineAdapter();

			searchEngineAdapter.execute(
				new SearchSearchRequest() {
					{
						setIndexNames(_INDEX_NAME);
						setQuery(new MatchAllQuery());
					}
				});

			_assertLogCapture("fl=uid", logCapture);
		}
	}

	@Test
	public void testSearchSearchRequestWithPortalSearchQuery() {
		SearchEngineAdapter searchEngineAdapter = getSearchEngineAdapter();

		searchEngineAdapter.execute(
			new SearchSearchRequest() {
				{
					setIndexNames(_INDEX_NAME);
					setQuery(QueriesUtil.matchAll());
				}
			});
	}

	@Override
	protected IndexingFixture createIndexingFixture() throws Exception {
		return new SolrIndexingFixture();
	}

	private void _assertLogCapture(String expectedLog, LogCapture logCapture) {
		List<LogEntry> logEntries = logCapture.getLogEntries();

		Assert.assertEquals(logEntries.toString(), 2, logEntries.size());

		LogEntry firstLogEntry = logEntries.get(0);

		Assert.assertEquals(LoggerTestUtil.DEBUG, firstLogEntry.getPriority());

		_assertMessage(expectedLog, "Search query", firstLogEntry.getMessage());

		LogEntry secondLogEntry = logEntries.get(1);

		Assert.assertEquals(LoggerTestUtil.DEBUG, secondLogEntry.getPriority());

		_assertMessage(
			expectedLog, "The search engine processed",
			secondLogEntry.getMessage());
	}

	private void _assertMessage(
		String expectedLog, String expectedPrefix, String message) {

		Assert.assertTrue(
			message + " does not start with " + expectedPrefix,
			message.startsWith(expectedPrefix));
		Assert.assertTrue(
			message + " does not contain " + expectedLog,
			message.contains(expectedLog));
	}

	private static final String _INDEX_NAME = "liferay";

}
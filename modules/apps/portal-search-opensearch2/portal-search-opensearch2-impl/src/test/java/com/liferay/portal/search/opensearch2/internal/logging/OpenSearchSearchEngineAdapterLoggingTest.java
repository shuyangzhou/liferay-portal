/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.logging;

import com.liferay.portal.kernel.search.MatchAllQuery;
import com.liferay.portal.search.engine.adapter.search.CountSearchRequest;
import com.liferay.portal.search.engine.adapter.search.MultisearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.opensearch2.internal.BaseOpenSearchTestCase;
import com.liferay.portal.search.opensearch2.internal.OpenSearchTestRule;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.search.CountSearchRequestExecutor;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.search.MultisearchSearchRequestExecutor;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.search.SearchSearchRequestExecutor;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LogEntry;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Bryan Engler
 * @author André de Oliveira
 */
public class OpenSearchSearchEngineAdapterLoggingTest
	extends BaseOpenSearchTestCase {

	@ClassRule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@ClassRule
	public static OpenSearchTestRule openSearchTestRule =
		OpenSearchTestRule.INSTANCE;

	@Test
	public void testCountSearchRequestExecutorLogs() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				CountSearchRequestExecutor.class.getName(),
				LoggerTestUtil.DEBUG)) {

			searchEngineAdapter.execute(
				new CountSearchRequest() {
					{
						setIndexNames(_INDEX_NAME);
						setQuery(new MatchAllQuery());
					}
				});

			_assertLogCapture(logCapture);
		}
	}

	@Test
	public void testMultisearchSearchRequestExecutorLogs() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				MultisearchSearchRequestExecutor.class.getName(),
				LoggerTestUtil.DEBUG)) {

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

			_assertLogCapture(logCapture);
		}
	}

	@Test
	public void testSearchSearchRequestExecutorLogs() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SearchSearchRequestExecutor.class.getName(),
				LoggerTestUtil.DEBUG)) {

			searchEngineAdapter.execute(
				new SearchSearchRequest() {
					{
						setIndexNames(_INDEX_NAME);
						setQuery(new MatchAllQuery());
					}
				});

			_assertLogCapture(logCapture);
		}
	}

	private void _assertLogCapture(LogCapture logCapture) {
		List<LogEntry> logEntries = logCapture.getLogEntries();

		Assert.assertEquals(logEntries.toString(), 1, logEntries.size());

		LogEntry logEntry = logEntries.get(0);

		Assert.assertEquals(LoggerTestUtil.DEBUG, logEntry.getPriority());

		String message = logEntry.getMessage();

		String expectedMessage = "The search engine processed";

		Assert.assertTrue(
			message + " does not start with " + expectedMessage,
			message.startsWith(expectedMessage));
	}

	private static final String _INDEX_NAME = "_all";

}
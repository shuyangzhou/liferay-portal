/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.solr8.internal.logging;

import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.search.solr8.internal.SolrIndexSearcher;
import com.liferay.portal.search.solr8.internal.SolrUnitTestRequirements;
import com.liferay.portal.search.solr8.internal.indexing.SolrIndexingFixture;
import com.liferay.portal.search.test.util.indexing.BaseIndexingTestCase;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LogEntry;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.search.SyntaxError;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Bryan Engler
 */
public class SolrIndexSearcherLogExceptionsOnlyTest
	extends BaseIndexingTestCase {

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
	public void testExceptionOnlyLoggedWhenQueryMalformedSearch() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexSearcher.class.getName(), LoggerTestUtil.ERROR)) {

			search(createSearchContext(), getMalformedQuery());

			_assertLogCapture(logCapture);
		}
	}

	@Test
	public void testExceptionOnlyLoggedWhenQueryMalformedSearchCount() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexSearcher.class.getName(), LoggerTestUtil.ERROR)) {

			searchCount(createSearchContext(), getMalformedQuery());

			_assertLogCapture(logCapture);
		}
	}

	@Override
	protected IndexingFixture createIndexingFixture() throws Exception {
		return new SolrIndexingFixture(
			HashMapBuilder.<String, Object>put(
				"logExceptionsOnly", true
			).build());
	}

	protected Query getMalformedQuery() {
		BooleanQuery booleanQuery = new BooleanQuery();

		booleanQuery.add(
			new TermQuery("f^eld", "text"), BooleanClauseOccur.MUST);

		return booleanQuery;
	}

	private void _assertLogCapture(LogCapture logCapture) {
		List<LogEntry> logEntries = logCapture.getLogEntries();

		Assert.assertEquals(logEntries.toString(), 1, logEntries.size());

		LogEntry logEntry = logEntries.get(0);

		Assert.assertEquals(LoggerTestUtil.ERROR, logEntry.getPriority());

		Assert.assertSame(
			HttpSolrClient.RemoteSolrException.class,
			logEntry.getThrowable(
			).getClass());

		String message = logEntry.getMessage();

		String expectedPrefix = "Error from server at";

		Assert.assertTrue(
			message + " does not start with " + expectedPrefix,
			message.startsWith(expectedPrefix));

		String expectedLog =
			SyntaxError.class.getName() + ": Cannot parse '+(+f^eld:text)";

		Assert.assertTrue(
			message + " does not contain " + expectedLog,
			message.contains(expectedLog));
	}

}
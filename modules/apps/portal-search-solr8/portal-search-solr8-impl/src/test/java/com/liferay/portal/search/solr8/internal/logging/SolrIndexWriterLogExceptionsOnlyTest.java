/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.solr8.internal.logging;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexWriter;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.search.solr8.internal.SolrIndexWriter;
import com.liferay.portal.search.solr8.internal.SolrUnitTestRequirements;
import com.liferay.portal.search.solr8.internal.indexing.SolrIndexingFixture;
import com.liferay.portal.search.solr8.internal.search.engine.adapter.document.BulkDocumentRequestExecutor;
import com.liferay.portal.search.test.util.indexing.BaseIndexingTestCase;
import com.liferay.portal.search.test.util.indexing.DocumentCreationHelpers;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LogEntry;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.apache.solr.client.solrj.impl.HttpSolrClient;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Bryan Engler
 */
public class SolrIndexWriterLogExceptionsOnlyTest extends BaseIndexingTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		Assume.assumeTrue(
			SolrUnitTestRequirements.isSolrExternallyStartedByDeveloper());
	}

	@After
	@Override
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddDocument() throws Exception {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexWriter.class.getName(), LoggerTestUtil.ERROR)) {

			addDocument(
				DocumentCreationHelpers.singleKeyword(
					Field.EXPIRATION_DATE, "text"));

			_assertLogCapture(
				this::_assertHttpSolrClientErrorMessage, logCapture,
				LoggerTestUtil.ERROR);
		}
	}

	@Test
	public void testAddDocuments() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexWriter.class.getName(), LoggerTestUtil.ERROR)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.addDocuments(
					createSearchContext(),
					Collections.singletonList(getTestDocument()));
			}
			catch (SearchException searchException) {
			}

			_assertLogCapture(
				message -> Assert.assertEquals("Bulk add failed", message),
				logCapture, LoggerTestUtil.ERROR);
		}
	}

	@Test
	public void testAddDocumentsBulkExecutor() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				BulkDocumentRequestExecutor.class.getName(),
				LoggerTestUtil.WARN)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.addDocuments(
					createSearchContext(),
					Collections.singletonList(getTestDocument()));
			}
			catch (SearchException searchException) {
			}

			_assertLogCapture(
				message -> {
					Assert.assertTrue(
						message + " does not contain " + _EXPECTED_MIME_TYPE,
						message.contains(_EXPECTED_MIME_TYPE));
					Assert.assertTrue(
						message + " does not start with " +
							_EXPECTED_BULK_PREFIX,
						message.startsWith(_EXPECTED_BULK_PREFIX));
					Assert.assertTrue(
						message + " does not contain " + _EXPECTED_STATUS,
						message.contains(_EXPECTED_STATUS));
				},
				logCapture, LoggerTestUtil.WARN);
		}
	}

	@Test
	public void testCommit() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexWriter.class.getName(), LoggerTestUtil.ERROR)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.commit(createSearchContext());
			}
			catch (SearchException searchException) {
			}

			_assertLogCapture(
				this::_assertHttpSolrClientErrorMessage, logCapture,
				LoggerTestUtil.ERROR);
		}
	}

	@Test
	public void testDeleteDocument() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexWriter.class.getName(), LoggerTestUtil.ERROR)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.deleteDocument(createSearchContext(), null);
			}
			catch (SearchException searchException) {
			}

			_assertLogCapture(
				this::_assertHttpSolrClientErrorMessage, logCapture,
				LoggerTestUtil.ERROR);
		}
	}

	@Test
	public void testDeleteDocuments() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexWriter.class.getName(), LoggerTestUtil.ERROR)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.deleteDocuments(
					createSearchContext(), Collections.singletonList(null));
			}
			catch (SearchException searchException) {
			}

			_assertLogCapture(
				message -> Assert.assertEquals("Bulk delete failed", message),
				logCapture, LoggerTestUtil.ERROR);
		}
	}

	@Test
	public void testDeleteDocumentsBulkExecutor() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				BulkDocumentRequestExecutor.class.getName(),
				LoggerTestUtil.WARN)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.deleteDocuments(
					createSearchContext(), Collections.singletonList(null));
			}
			catch (SearchException searchException) {
			}

			_assertLogCapture(
				message -> {
					Assert.assertTrue(
						message + " does not contain " + _EXPECTED_MIME_TYPE,
						message.contains(_EXPECTED_MIME_TYPE));
					Assert.assertTrue(
						message + " does not start with " +
							_EXPECTED_BULK_PREFIX,
						message.startsWith(_EXPECTED_BULK_PREFIX));
					Assert.assertTrue(
						message + " does not contain " + _EXPECTED_STATUS,
						message.contains(_EXPECTED_STATUS));
				},
				logCapture, LoggerTestUtil.WARN);
		}
	}

	@Test
	public void testDeleteEntityDocuments() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexWriter.class.getName(), LoggerTestUtil.ERROR)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.deleteEntityDocuments(createSearchContext(), null);
			}
			catch (SearchException searchException) {
			}

			String expectedMessage =
				"Cannot invoke \"String.isEmpty()\" because \"value\" is null";

			_assertLogCapture(
				message -> Assert.assertEquals(expectedMessage, message),
				logCapture, LoggerTestUtil.ERROR);
		}
	}

	@Test
	public void testPartiallyUpdateDocument() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexWriter.class.getName(), LoggerTestUtil.ERROR)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.partiallyUpdateDocument(
					createSearchContext(), getTestDocument());
			}
			catch (SearchException searchException) {
			}

			_assertLogCapture(
				this::_assertHttpSolrClientErrorMessage, logCapture,
				LoggerTestUtil.ERROR);
		}
	}

	@Test
	public void testPartiallyUpdateDocuments() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexWriter.class.getName(), LoggerTestUtil.ERROR)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.partiallyUpdateDocuments(
					createSearchContext(),
					Collections.singletonList(getTestDocument()));
			}
			catch (SearchException searchException) {
			}

			_assertLogCapture(
				message -> Assert.assertEquals(
					"Bulk partial update failed", message),
				logCapture, LoggerTestUtil.ERROR);
		}
	}

	@Test
	public void testPartiallyUpdateDocumentsBulkExecutor() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				BulkDocumentRequestExecutor.class.getName(),
				LoggerTestUtil.WARN)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.partiallyUpdateDocuments(
					createSearchContext(),
					Collections.singletonList(getTestDocument()));
			}
			catch (SearchException searchException) {
			}

			_assertLogCapture(
				message -> {
					Assert.assertTrue(
						message + " does not contain " + _EXPECTED_MIME_TYPE,
						message.contains(_EXPECTED_MIME_TYPE));
					Assert.assertTrue(
						message + " does not start with " +
							_EXPECTED_BULK_PREFIX,
						message.startsWith(_EXPECTED_BULK_PREFIX));
					Assert.assertTrue(
						message + " does not contain " + _EXPECTED_STATUS,
						message.contains(_EXPECTED_STATUS));
				},
				logCapture, LoggerTestUtil.WARN);
		}
	}

	@Test
	public void testUpdateDocument() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexWriter.class.getName(), LoggerTestUtil.ERROR)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.updateDocument(
					createSearchContext(), getTestDocument());
			}
			catch (SearchException searchException) {
			}

			String expectedMessagePrefix = "Update failed: " + _EXPECTED_PREFIX;

			_assertLogCapture(
				message -> {
					Assert.assertTrue(
						message + " does not contain " + _EXPECTED_MIME_TYPE,
						message.contains(_EXPECTED_MIME_TYPE));
					Assert.assertTrue(
						message + " does not start with " +
							expectedMessagePrefix,
						message.startsWith(expectedMessagePrefix));
					Assert.assertTrue(
						message + " does not contain " + _EXPECTED_STATUS,
						message.contains(_EXPECTED_STATUS));
				},
				logCapture, LoggerTestUtil.ERROR);
		}
	}

	@Test
	public void testUpdateDocumentBulkExecutor() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				BulkDocumentRequestExecutor.class.getName(),
				LoggerTestUtil.WARN)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.updateDocument(
					createSearchContext(), getTestDocument());
			}
			catch (SearchException searchException) {
			}

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 2, logEntries.size());

			for (LogEntry logEntry : logEntries) {
				_assertLogEntry(
					message -> {
						Assert.assertTrue(
							message + " does not contain " +
								_EXPECTED_MIME_TYPE,
							message.contains(_EXPECTED_MIME_TYPE));
						Assert.assertTrue(
							message + " does not start with " +
								_EXPECTED_BULK_PREFIX,
							message.startsWith(_EXPECTED_BULK_PREFIX));
						Assert.assertTrue(
							message + " does not contain " + _EXPECTED_STATUS,
							message.contains(_EXPECTED_STATUS));
					},
					logEntry, LoggerTestUtil.WARN);
			}
		}
	}

	@Test
	public void testUpdateDocuments() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				SolrIndexWriter.class.getName(), LoggerTestUtil.ERROR)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.updateDocuments(
					createSearchContext(),
					Collections.singletonList(getTestDocument()));
			}
			catch (SearchException searchException) {
			}

			String expectedMessagePrefix = "Update failed: " + _EXPECTED_PREFIX;

			_assertLogCapture(
				message -> {
					Assert.assertTrue(
						message + " does not contain " + _EXPECTED_MIME_TYPE,
						message.contains(_EXPECTED_MIME_TYPE));
					Assert.assertTrue(
						message + " does not start with " +
							expectedMessagePrefix,
						message.startsWith(expectedMessagePrefix));
					Assert.assertTrue(
						message + " does not contain " + _EXPECTED_STATUS,
						message.contains(_EXPECTED_STATUS));
				},
				logCapture, LoggerTestUtil.ERROR);
		}
	}

	@Test
	public void testUpdateDocumentsBulkExecutor() {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				BulkDocumentRequestExecutor.class.getName(),
				LoggerTestUtil.WARN)) {

			IndexWriter indexWriter = getIndexWriter();

			try {
				indexWriter.updateDocuments(
					createSearchContext(),
					Collections.singletonList(getTestDocument()));
			}
			catch (SearchException searchException) {
			}

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 2, logEntries.size());

			for (LogEntry logEntry : logEntries) {
				_assertLogEntry(
					message -> {
						Assert.assertTrue(
							message + " does not contain " +
								_EXPECTED_MIME_TYPE,
							message.contains(_EXPECTED_MIME_TYPE));
						Assert.assertTrue(
							message + " does not start with " +
								_EXPECTED_BULK_PREFIX,
							message.startsWith(_EXPECTED_BULK_PREFIX));
						Assert.assertTrue(
							message + " does not contain " + _EXPECTED_STATUS,
							message.contains(_EXPECTED_STATUS));
					},
					logEntry, LoggerTestUtil.WARN);
			}
		}
	}

	@Override
	protected IndexingFixture createIndexingFixture() throws Exception {
		return new SolrIndexingFixture(
			HashMapBuilder.<String, Object>put(
				"defaultCollection", _COLLECTION_NAME
			).put(
				"logExceptionsOnly", true
			).build());
	}

	protected Document getTestDocument() {
		Document document = new DocumentImpl();

		document.addUID(
			RandomTestUtil.randomString(), RandomTestUtil.randomLong());

		return document;
	}

	private void _assertHttpSolrClientErrorMessage(String message) {
		Assert.assertTrue(
			message + " does not contain " + _EXPECTED_MIME_TYPE,
			message.contains(_EXPECTED_MIME_TYPE));
		Assert.assertTrue(
			message + " does not start with " + _EXPECTED_PREFIX,
			message.startsWith(_EXPECTED_PREFIX));
		Assert.assertTrue(
			message + " does not contain " + _EXPECTED_STATUS,
			message.contains(_EXPECTED_STATUS));
	}

	private void _assertLogCapture(
		Consumer<String> consumer, LogCapture logCapture, String logLevel) {

		List<LogEntry> logEntries = logCapture.getLogEntries();

		Assert.assertEquals(logEntries.toString(), 1, logEntries.size());

		_assertLogEntry(consumer, logEntries.get(0), logLevel);
	}

	private void _assertLogEntry(
		Consumer<String> consumer, LogEntry logEntry, String logLevel) {

		Assert.assertEquals(logLevel, logEntry.getPriority());
		consumer.accept(logEntry.getMessage());
	}

	private static final String _COLLECTION_NAME = "alpha";

	private static final String _EXPECTED_BULK_PREFIX =
		"{class=class " + HttpSolrClient.RemoteSolrException.class.getName() +
			", message=Error from server at";

	private static final String _EXPECTED_MIME_TYPE =
		"Expected mime type application/octet-stream but got text";

	private static final String _EXPECTED_PREFIX = "Error from server at";

	private static final String _EXPECTED_STATUS = "Error 404 Not Found";

}
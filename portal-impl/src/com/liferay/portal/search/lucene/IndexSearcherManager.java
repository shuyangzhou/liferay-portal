/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.search.lucene;

import com.liferay.portal.kernel.executor.PortalExecutorManagerUtil;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.AlreadyClosedException;

/**
 * @author Tina Tian
 * @author Shuyang Zhou
 */
public class IndexSearcherManager {

	public IndexSearcherManager(IndexWriter writer) throws IOException {
		_indexSearcher = _createIndexSearcher(IndexReader.open(writer, true));
	}

	public IndexSearcher aquire() throws IOException {
		IndexSearcher indexSearcher = _indexSearcher;

		if (_invalid) {
			synchronized (this) {
				if (_invalid) {
					indexSearcher = _indexSearcher;

					if (indexSearcher == null) {
						throw new AlreadyClosedException(
							"IndexSearcherManager is closed");
					}

					IndexReader newIndexReader = IndexReader.openIfChanged(
						indexSearcher.getIndexReader());

					if (newIndexReader != null) {
						release(indexSearcher);

						indexSearcher = _createIndexSearcher(newIndexReader);

						_indexSearcher = indexSearcher;
					}

					_invalid = false;
				}
			}
		}

		while (indexSearcher != null) {
			IndexReader indexReader = indexSearcher.getIndexReader();

			if (indexReader.tryIncRef()) {
				return indexSearcher;
			}

			indexSearcher = _indexSearcher;
		}

		throw new AlreadyClosedException("IndexSearcherManager is closed");
	}

	public synchronized void close() throws IOException {
		release(_indexSearcher);

		_indexSearcher = null;

		PortalExecutorManagerUtil.shutdown(
			_LUCENE_SEARCHER_MANAGER_THREAD_POOL);
	}

	public synchronized void invalid() {
		_invalid = true;
	}

	public void release(IndexSearcher indexSearcher) throws IOException {
		if (indexSearcher == null) {
			return;
		}

		IndexReader indexReader = indexSearcher.getIndexReader();

		indexReader.decRef();
	}

	private IndexSearcher _createIndexSearcher(IndexReader indexReader) {
		IndexSearcher indexSearcher = new IndexSearcher(
			indexReader,
			PortalExecutorManagerUtil.getPortalExecutor(
				_LUCENE_SEARCHER_MANAGER_THREAD_POOL));

		indexSearcher.setDefaultFieldSortScoring(true, false);
		indexSearcher.setSimilarity(new FieldWeightSimilarity());

		return indexSearcher;
	}

	private static final String _LUCENE_SEARCHER_MANAGER_THREAD_POOL =
		"LUCENE_SEARCHER_MANAGER_THREAD_POOL";

	private volatile IndexSearcher _indexSearcher;
	private volatile boolean _invalid;

}
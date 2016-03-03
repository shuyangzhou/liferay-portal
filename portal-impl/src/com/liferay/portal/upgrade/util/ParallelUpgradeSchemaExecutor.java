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

package com.liferay.portal.upgrade.util;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.util.LoggingTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Miguel Pastor
 */
public class ParallelUpgradeSchemaExecutor {

	public ParallelUpgradeSchemaExecutor(String... sqlFileNames) {
		_sqlFileNames = sqlFileNames;
	}

	public void execute() throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(3);

		List<Future<Void>> futures = new ArrayList<>();

		try {
			for (String _sqlFileName : _sqlFileNames) {
				futures.add(
					executorService.submit(
						new CallableSQLExecutor(_sqlFileName)));
			}

			for (Future<Void> future : futures) {
				future.get();
			}
		}
		finally {
			executorService.shutdown();
		}
	}

	private final String[] _sqlFileNames;

	private static class CallableSQLExecutor implements Callable<Void> {

		public CallableSQLExecutor(String sqlFile) {
			_sqlFileName = sqlFile;
		}

		@Override
		public Void call() throws Exception {
			DB db = DBManagerUtil.getDB();

			try (LoggingTimer loggingTimer = new LoggingTimer(_sqlFileName)) {
				db.runSQLTemplate(_sqlFileName, false);
			}

			return null;
		}

		private final String _sqlFileName;

	}

}
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

package com.liferay.arquillian.extension.junit.bridge.server;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.runners.model.Statement;

/**
 * @author Matthew Tambara
 */
public class TestProfilingStatement extends Statement {

	public TestProfilingStatement(Statement statement, String className) {
		_statement = statement;
		_className = className;

		String fileLocation = System.getProperty(_SYSTEM_KEY);

		if (fileLocation == null) {
			try {
				Path path = Files.createTempFile(null, null);

				_profileFile = path.toFile();

				Runtime runtime = Runtime.getRuntime();

				runtime.addShutdownHook(
					new Thread(new ShutdownHookRunnable(_profileFile)));

				System.setProperty(_SYSTEM_KEY, _profileFile.toString());
			}
			catch (IOException ioe) {
				_logger.log(
					Level.WARNING, "Unable to make profiling file", ioe);
			}
		}
		else {
			_profileFile = new File(fileLocation);
		}
	}

	@Override
	public void evaluate() throws Throwable {
		long startTime = System.currentTimeMillis();

		try {
			_statement.evaluate();
		}
		finally {
			long endTime = System.currentTimeMillis();

			StringBundler sb = new StringBundler(4);

			sb.append(_className);
			sb.append(StringPool.EQUAL);
			sb.append(endTime - startTime);
			sb.append(StringPool.NEW_LINE);

			String line = sb.toString();

			Files.write(
				_profileFile.toPath(), line.getBytes(),
				StandardOpenOption.APPEND);
		}
	}

	private static final String _SYSTEM_KEY = "arquillian.profiling.times.file";

	private static final int _TESTS_TO_DISPLAY = 10;

	private static final Logger _logger = Logger.getLogger(
		TestProfilingStatement.class.getName());

	private final String _className;
	private File _profileFile;
	private final Statement _statement;

	private class ShutdownHookRunnable implements Runnable {

		public ShutdownHookRunnable(File file) {
			_profileFile = file;
		}

		@Override
		public void run() {
			Properties properties = new Properties();

			try (InputStream inputStream = new FileInputStream(_profileFile)) {
				properties.load(inputStream);
			}
			catch (IOException ioe) {
				_logger.log(Level.SEVERE, "Unable to read profile", ioe);

				return;
			}

			Map<String, Long> timeMap = new HashMap<>();

			for (String stringPropertyName : properties.stringPropertyNames()) {
				timeMap.put(
					stringPropertyName,
					Long.valueOf(properties.getProperty(stringPropertyName)));
			}

			List<Map.Entry<String, Long>> times = new ArrayList<>(
				timeMap.entrySet());

			Collections.sort(times, Map.Entry.comparingByValue());
			Collections.reverse(times);

			int limit = _TESTS_TO_DISPLAY;

			int size = times.size();

			if (size < limit) {
				limit = size;
			}

			StringBundler sb = new StringBundler();

			sb.append("========= Slowest tests =========\n");

			for (int i = 0; i < limit; i++) {
				Map.Entry<String, Long> entry = times.get(i);

				sb.append(entry.getKey());

				sb.append(" took ");
				sb.append(entry.getValue());
				sb.append("ms");
				sb.append(StringPool.NEW_LINE);
			}

			System.out.println(sb.toString());

			try {
				Files.delete(_profileFile.toPath());
			}
			catch (IOException ioe) {
				_logger.log(Level.SEVERE, "Unable to delete profile", ioe);
			}
		}

		private final File _profileFile;

	}

}
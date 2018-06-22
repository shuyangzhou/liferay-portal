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

package com.liferay.poshi.runner.var.type;

import com.liferay.poshi.runner.PoshiRunner;
import com.liferay.poshi.runner.PoshiRunnerContext;
import com.liferay.poshi.runner.selenium.SeleniumUtil;
import com.liferay.poshi.runner.util.TableUtil;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.lang3.ArrayUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Yi-Chen Tsai
 */
public class TableTest extends TestCase {

	public TableTest() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		_rawDataList = TableUtil.getRawDataListFromString(_RAW_DATA_STRING);

		_testBaseDir = new File(_TEST_BASE_DIR_NAME);

		if (!_testBaseDir.exists()) {
			throw new RuntimeException(
				"Test directory does not exist: " + _TEST_BASE_DIR_NAME);
		}

		String[] poshiFileNames = ArrayUtils.addAll(
			PoshiRunnerContext.POSHI_SUPPORT_FILE_INCLUDES,
			PoshiRunnerContext.POSHI_TEST_FILE_INCLUDES);

		PoshiRunnerContext.readFiles(poshiFileNames, _TEST_BASE_DIR_NAME);
	}

	@Test
	public void testEchoTablesPoshiScript() throws Exception {
		PoshiRunner pr = new PoshiRunner(
			"LocalFile.TableTest#echoTablePoshiScript");

		pr.setUp();

		pr.test();

		SeleniumUtil.stopSelenium();
	}

	@Test
	public void testEchoTablesXML() throws Exception {
		PoshiRunner pr = new PoshiRunner("LocalFile.TableTest#echoTableXML");

		pr.setUp();

		pr.test();

		SeleniumUtil.stopSelenium();
	}

	@Test
	public void testHashesTable() throws Exception {
		HashesTable hashesTable = (HashesTable)TableFactory.newTable(
			_rawDataList, "HashesTable");

		List<Map<String, String>> actual = hashesTable.getTable();

		List<Map<String, String>> expected = new ArrayList<>();

		if (_rawDataList.size() < 2) {
			return;
		}

		List<String> rowKeys = _rawDataList.get(0);

		for (int i = 1; i < _rawDataList.size(); i++) {
			List<String> rowEntries = _rawDataList.get(i);

			LinkedHashMap<String, String> hashesRow = new LinkedHashMap<>();

			for (int j = 0; j < rowEntries.size(); j++) {
				hashesRow.put(rowKeys.get(j), rowEntries.get(j));
			}

			expected.add(hashesRow);
		}

		assertEquals(actual, expected);
	}

	@Test
	public void testRawTable() throws Exception {
		RawTable rawTable = (RawTable)TableFactory.newTable(
			_rawDataList, "RawTable");

		Iterable<List<String>> actual = rawTable.getTable();

		List<List<String>> expected = _rawDataList;

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRowsHashTable() throws Exception {
		RowsHashTable rowsHashTable = (RowsHashTable)TableFactory.newTable(
			_rawDataList, "RowsHashTable");

		List<Map<String, String>> actual = rowsHashTable.getTable();

		List<Map<String, String>> expected = new ArrayList<>();

		Map<String, String> row = new LinkedHashMap<>();

		for (List<String> rawDataRow : _rawDataList) {
			row.put(rawDataRow.get(0), rawDataRow.get(1));
		}

		expected.add(row);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testTransposedTable() throws Exception {
		List<List<String>> transposedRawDataList =
			TableUtil.getTransposedRawDataList(_rawDataList);

		RawTable transposedTable = (RawTable)TableFactory.newTable(
			transposedRawDataList, "RawTable");

		Iterable<List<String>> actual = transposedTable.getTable();

		List<List<String>> expected = new ArrayList<>();

		for (int i = 0; i < TableUtil.getRawDataListWidth(_rawDataList); i++) {
			List<String> column = new ArrayList<>();

			for (List<String> row : _rawDataList) {
				column.add(row.get(i));
			}

			expected.add(column);
		}

		Assert.assertEquals(expected, actual);
	}

	protected String read(File file) throws IOException {
		return new String(Files.readAllBytes(Paths.get(file.toURI())));
	}

	protected String read(File dir, String fileName) throws IOException {
		return read(new File(dir, fileName));
	}

	private static final String _RAW_DATA_STRING =
		"| 1 | 2 |\n| 3 | 4 |\n| 5 | 6 |\n| 7 | 8 |";

	private static final String _TEST_BASE_DIR_NAME =
		"src/test/resources/com/liferay/poshi/runner/dependencies/var/type/";

	private List<List<String>> _rawDataList;
	private File _testBaseDir;

}
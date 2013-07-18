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

package com.liferay.portal.tools.samplesqlbuilder;

import com.liferay.portal.dao.db.MySQLDB;
import com.liferay.portal.freemarker.FreeMarkerUtil;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.io.CharPipe;
import com.liferay.portal.kernel.io.OutputStreamWriter;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedWriter;
import com.liferay.portal.kernel.io.unsync.UnsyncTeeWriter;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.InitUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import java.nio.channels.FileChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public class SampleSQLBuilder {

	public static void main(String[] args) {
		Properties properties = new Properties();
		Reader reader = null;

		try {
			reader = new FileReader(args[0]);

			properties.load(reader);

			new SampleSQLBuilder(properties);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

	public SampleSQLBuilder(Properties properties) throws Exception {
		List<String> extraConfigLocations = new ArrayList<String>();

		extraConfigLocations.add("META-INF/portlet-container-spring.xml");

		InitUtil.initWithSpring(false, extraConfigLocations);

		_dataFactory = new DataFactory(properties);
		_dbType = properties.getProperty("sample.sql.db.type");
		_optimizeBufferSize = GetterUtil.getInteger(
			properties.getProperty("sample.sql.optimize.buffer.size"));
		_outputCSVFiles = StringUtil.split(
			getPropertyWithDefault(
				properties, "sample.sql.output.csv.files", _DEFAULT_CSV_FILES));
		_outputDir = properties.getProperty("sample.sql.output.dir");
		boolean outputMerge = GetterUtil.getBoolean(
			properties.getProperty("sample.sql.output.merge"));
		_script = getPropertyWithDefault(
			properties, "sample.sql.script.file", _DEFAULT_SCRIPT);

		CharPipe charPipe = generateSQL();

		String endSQLFileName = "others.sql";
		File tempDir = new File(_outputDir, "temp");

		tempDir.mkdirs();

		try {
			compressSQL(charPipe.getReader(), tempDir, endSQLFileName);

			if (outputMerge) {
				File mergedSQLFile = new File(
					_outputDir, "sample-" + _dbType + ".sql");

				FileUtil.delete(mergedSQLFile);

				mergeSQL(mergedSQLFile, tempDir, endSQLFileName);
			}
			else {
				File dividedSQLDir = new File(_outputDir, _dbType);

				// Clean up previous output

				FileUtil.deltree(dividedSQLDir);

				if (!tempDir.renameTo(dividedSQLDir)) {

					// This will only happen when temp and output folders are on
					// different file systems

					FileUtil.copyDirectory(tempDir, dividedSQLDir);
				}
			}
		}
		finally {
			FileUtil.deltree(tempDir);
		}

		writeOutProperties(
			properties, new File(_outputDir, "benchmarks-actual.properties"));
	}

	protected void compressInsertSQL(
			DB db, String insertSQL, File dir,
			Map<String, StringBundler> insertSQLs,
			Map<String, Writer> insertSQLWriters, int optimizeBufferSize)
		throws IOException {

		String fileName =
			insertSQL.substring(0, insertSQL.indexOf(CharPool.SPACE)) + ".sql";

		int pos = insertSQL.indexOf(" values ") + 8;

		String values = insertSQL.substring(pos, insertSQL.length() - 1);

		StringBundler sb = insertSQLs.get(fileName);

		if ((sb == null) || (sb.index() == 0)) {
			sb = new StringBundler();

			insertSQLs.put(fileName, sb);

			sb.append("insert into ");
			sb.append(insertSQL.substring(0, pos));
			sb.append("\n");
		}
		else {
			sb.append(",\n");
		}

		sb.append(values);

		if (sb.index() >= optimizeBufferSize) {
			sb.append(";\n");

			String sql = db.buildSQL(sb.toString());

			sb.setIndex(0);

			Writer insertSQLWriter = insertSQLWriters.get(fileName);

			if (insertSQLWriter == null) {
				insertSQLWriter = createFileWriter(new File(dir, fileName));

				insertSQLWriters.put(fileName, insertSQLWriter);
			}

			insertSQLWriter.write(sql);
		}
	}

	protected void compressSQL(Reader reader, File dir, String endSQLFileName)
		throws IOException {

		DB db = DBFactoryUtil.getDB(_dbType);

		if (db instanceof MySQLDB) {
			db = new SampleMySQLDB();
		}

		Map<String, StringBundler> insertSQLs =
			new HashMap<String, StringBundler>();
		Map<String, Writer> insertSQLWriters = new HashMap<String, Writer>();
		List<String> otherSQLs = new ArrayList<String>();

		UnsyncBufferedReader unsyncBufferedReader = new UnsyncBufferedReader(
			reader);

		String line = null;

		while ((line = unsyncBufferedReader.readLine()) != null) {
			line = line.trim();

			if (line.length() == 0) {
				continue;
			}

			if (line.startsWith("insert into ")) {
				compressInsertSQL(
					db, line.substring(12), dir, insertSQLs, insertSQLWriters,
					_optimizeBufferSize);
			}
			else {
				otherSQLs.add(line);
			}
		}

		unsyncBufferedReader.close();

		for (Map.Entry<String, StringBundler> entry : insertSQLs.entrySet()) {
			String fileName = entry.getKey();

			String sql = db.buildSQL(entry.getValue().toString());

			Writer insertSQLWriter = insertSQLWriters.remove(fileName);

			if (insertSQLWriter == null) {
				insertSQLWriter = createFileWriter(new File(dir, fileName));
			}

			insertSQLWriter.write(sql);
			insertSQLWriter.write(";\n");

			insertSQLWriter.close();
		}

		Writer endSQLFileWriter = new FileWriter(new File(dir, endSQLFileName));

		for (String sql : otherSQLs) {
			endSQLFileWriter.write(db.buildSQL(sql));
			endSQLFileWriter.write(StringPool.NEW_LINE);
		}

		endSQLFileWriter.close();
	}

	protected Writer createFileWriter(File file) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(file);

		Writer writer = new OutputStreamWriter(fileOutputStream);

		return createUnsyncBufferedWriter(writer);
	}

	protected Writer createUnsyncBufferedWriter(Writer writer) {
		return new UnsyncBufferedWriter(writer, _WRITER_BUFFER_SIZE) {

			@Override
			public void flush() {

				// Disable FreeMarker from flushing

			}

		};
	}

	protected void doMergeSQL(File SQLFile, FileChannel outputFileChannel)
		throws IOException {

		FileInputStream fileInputStream = new FileInputStream(SQLFile);

		FileChannel inputFileChannel = fileInputStream.getChannel();

		inputFileChannel.transferTo(
			0, inputFileChannel.size(), outputFileChannel);

		inputFileChannel.close();
	}

	protected CharPipe generateSQL() {
		final CharPipe charPipe = new CharPipe(_PIPE_BUFFER_SIZE);

		new Thread() {

			@Override
			public void run() {
				try {
					Map<String, Object> context = getContext();

					Writer writerSampleSQL = new UnsyncTeeWriter(
						charPipe.getWriter(),
						createFileWriter(new File(_outputDir, "sample.sql")));

					FreeMarkerUtil.process(_script, context, writerSampleSQL);

					for (Object value : context.values()) {
						if (value instanceof Writer) {
							Writer writer = (Writer)value;

							writer.close();
						}
					}

					writerSampleSQL.close();

					charPipe.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

		}.start();

		return charPipe;
	}

	protected Map<String, Object> getContext() throws Exception {
		Map<String, Object> context = new HashMap<String, Object>();

		context.put("dataFactory", _dataFactory);

		for (String fileName : _outputCSVFiles) {
			Writer writer = createFileWriter(
				new File(_outputDir, fileName + ".csv"));

			context.put(fileName + "CSVWriter", writer);
		}

		return context;
	}

	protected String getPropertyWithDefault(
		Properties properties, String key, String defaultValue) {

		String value = properties.getProperty(key);

		if ((value == null) || value.equals(StringPool.BLANK)) {
			return defaultValue;
		}

		return value;
	}

	protected void mergeSQL(
			File mergedSQLFile, File tempDir, String endSQLFileName)
		throws IOException {

		FileOutputStream fileOutputStream = new FileOutputStream(mergedSQLFile);
		FileChannel fileChannel = fileOutputStream.getChannel();

		File lastSQLFile = null;

		for (File tableFile : tempDir.listFiles()) {
			if (tableFile.getName().equals(endSQLFileName)) {
				lastSQLFile = tableFile;

				continue;
			}

			doMergeSQL(tableFile, fileChannel);

			tableFile.delete();
		}

		if (lastSQLFile != null) {
			doMergeSQL(lastSQLFile, fileChannel);

			lastSQLFile.delete();
		}

		fileChannel.close();
	}

	protected void writeOutProperties(Properties properties, File outputFile)
		throws Exception {

		StringBundler sb = new StringBundler();

		Set<String> propertyNames = properties.stringPropertyNames();

		List<String> keys = new ArrayList<String>(propertyNames);

		Collections.sort(keys);

		for (String key : keys) {
			if (!key.startsWith("sample.sql")) {
				continue;
			}

			String value = properties.getProperty(key);

			sb.append(key);
			sb.append(StringPool.EQUAL);
			sb.append(value);
			sb.append(StringPool.NEW_LINE);
		}

		FileUtil.write(outputFile, sb.toString());
	}

	private static final String _DEFAULT_CSV_FILES =
		"assetPublisher,blog,company,documentLibrary,dynamicDataList,layout," +
		"messageBoard,repository,wiki";

	private static final String _DEFAULT_SCRIPT =
		"com/liferay/portal/tools/samplesqlbuilder/dependencies/sample.ftl";

	private static final int _PIPE_BUFFER_SIZE = 16 * 1024 * 1024;

	private static final int _WRITER_BUFFER_SIZE = 16 * 1024;

	private DataFactory _dataFactory;
	private String _dbType;
	private int _optimizeBufferSize;
	private String[] _outputCSVFiles;
	private String _outputDir;
	private String _script;

}
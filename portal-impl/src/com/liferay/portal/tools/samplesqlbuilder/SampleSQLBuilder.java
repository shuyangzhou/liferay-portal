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

		String dbType = properties.getProperty("sample.sql.db.type");
		int optimizeBufferSize = GetterUtil.getInteger(
			properties.getProperty("sample.sql.optimize.buffer.size"));
		String outputDir = properties.getProperty("sample.sql.output.dir");
		boolean outputMerge = GetterUtil.getBoolean(
			properties.getProperty("sample.sql.output.merge"));

		// Clean up previous output

		File rawSQLFile = new File(outputDir, "sample.sql");
		File mergedSQLFile = new File(outputDir, "sample-" + dbType + ".sql");
		File dividedSQLFolder = new File(outputDir, "output");

		FileUtil.delete(rawSQLFile);
		FileUtil.delete(mergedSQLFile);
		FileUtil.deltree(dividedSQLFolder);

		// Generic

		final CharPipe charPipe = new CharPipe(_PIPE_BUFFER_SIZE);

		Map<String, Object> context = initContext(properties);

		generateSQL(
			_TPL_ROOT + "sample.ftl", context, charPipe, rawSQLFile, outputDir,
			new String[] {
				"assetPublisher", "blog", "company", "documentLibrary",
				"dynamicDataList", "layout", "messageBoard", "repository",
				"wiki"});

		File tempDir = new File(outputDir, "temp");

		tempDir.mkdirs();

		String lastSQLFileName = "others.sql";

		try {

			// Specific

			compressSQL(
				dbType, charPipe.getReader(), optimizeBufferSize, tempDir,
				lastSQLFileName);

			// Merge

			if (outputMerge) {
				mergeSQL(mergedSQLFile, tempDir, lastSQLFileName);
			}
			else if (!tempDir.renameTo(dividedSQLFolder)) {

				// This will only happen when temp and output folders are on
				// different file systems

				FileUtil.copyDirectory(tempDir, dividedSQLFolder);
			}
		}
		finally {
			FileUtil.deltree(tempDir);
		}

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

		FileUtil.write(
			new File(outputDir, "benchmarks-actual.properties"), sb.toString());
	}

	protected void compressInsertSQL(
			DB db, String insertSQL, int optimizeBufferSize, File tempDir,
			Map<String, StringBundler> insertSQLs,
			Map<String, Writer> insertSQLWriters)
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
				insertSQLWriter = createFileWriter(new File(tempDir, fileName));

				insertSQLWriters.put(fileName, insertSQLWriter);
			}

			insertSQLWriter.write(sql);
		}
	}

	protected void compressSQL(
			String dbType, Reader reader, int optimizeBufferSize, File tempDir,
			String lastSQLFileName)
		throws IOException {

		DB db = DBFactoryUtil.getDB(dbType);

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
					db, line.substring(12), optimizeBufferSize, tempDir,
					insertSQLs, insertSQLWriters);
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
				insertSQLWriter = createFileWriter(new File(tempDir, fileName));
			}

			insertSQLWriter.write(sql);
			insertSQLWriter.write(";\n");

			insertSQLWriter.close();
		}

		Writer lastSQLFileWriter = new FileWriter(
			new File(tempDir, lastSQLFileName));

		for (String sql : otherSQLs) {
			lastSQLFileWriter.write(db.buildSQL(sql));
			lastSQLFileWriter.write(StringPool.NEW_LINE);
		}

		lastSQLFileWriter.close();
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

	protected void generateSQL(
		final String script, final Map<String, Object> context,
		final CharPipe charPipe, final File outputFile, final String outputDir,
		final String[] csvFileNames) {

		final Writer charPipeWriter = createUnsyncBufferedWriter(
			charPipe.getWriter());

		Thread thread = new Thread() {

			@Override
			public void run() {
				try {
					List<Writer> csvWriters = initCSVWriters(
						context, outputDir, csvFileNames);

					Writer writerSampleSQL = new UnsyncTeeWriter(
						charPipeWriter, createFileWriter(outputFile));

					FreeMarkerUtil.process(script, context, writerSampleSQL);

					for (Writer csvWriter : csvWriters) {
						csvWriter.close();
					}

					writerSampleSQL.close();

					charPipe.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

		};

		thread.start();
	}

	protected Map<String, Object> initContext(Properties properties)
		throws Exception {

		String baseDir = System.getProperty("sample.sql.base.dir");

		int maxAssetCategoryCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.asset.category.count"));
		int maxAssetEntryToAssetCategoryCount = GetterUtil.getInteger(
			properties.getProperty(
				"sample.sql.max.asset.entry.to.asset.category.count"));
		int maxAssetEntryToAssetTagCount = GetterUtil.getInteger(
			properties.getProperty(
				"sample.sql.max.asset.entry.to.asset.tag.count"));
		int maxAssetPublisherFilterRuleCount = GetterUtil.getInteger(
			properties.getProperty(
				"sample.sql.max.asset.publisher.filter.rule.count"));
		int maxAssetPublisherPageCount = GetterUtil.getInteger(
			properties.getProperty(
				"sample.sql.max.asset.publisher.page.count"));
		int maxAssetTagCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.asset.tag.count"));
		int maxAssetVocabularyCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.asset.vocabulary.count"));
		int maxBlogsEntryCommentCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.blogs.entry.comment.count"));
		int maxBlogsEntryCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.blogs.entry.count"));
		int maxDDLCustomFieldCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.ddl.custom.field.count"));
		int maxDDLRecordCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.ddl.record.count"));
		int maxDDLRecordSetCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.ddl.record.set.count"));
		int maxDLFileEntryCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.dl.file.entry.count"));
		int maxDLFileEntrySize = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.dl.file.entry.size"));
		int maxDLFolderCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.dl.folder.count"));
		int maxDLFolderDepth = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.dl.folder.depth"));
		int maxGroupCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.group.count"));
		int maxJournalArticleCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.journal.article.count"));
		int maxJournalArticlePageCount = GetterUtil.getInteger(
			properties.getProperty(
				"sample.sql.max.journal.article.page.count"));
		int maxJournalArticleSize = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.journal.article.size"));
		int maxJournalArticleVersionCount = GetterUtil.getInteger(
			properties.getProperty(
				"sample.sql.max.journal.article.version.count"));
		int maxMBCategoryCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.mb.category.count"));
		int maxMBMessageCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.mb.message.count"));
		int maxMBThreadCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.mb.thread.count"));
		int maxUserCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.user.count"));
		int maxUserToGroupCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.user.to.group.count"));
		int maxWikiNodeCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.wiki.node.count"));
		int maxWikiPageCommentCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.wiki.page.comment.count"));
		int maxWikiPageCount = GetterUtil.getInteger(
			properties.getProperty("sample.sql.max.wiki.page.count"));

		DataFactory dataFactory = new DataFactory(
			baseDir, maxAssetCategoryCount, maxAssetEntryToAssetCategoryCount,
			maxAssetEntryToAssetTagCount, maxAssetPublisherFilterRuleCount,
			maxAssetPublisherPageCount, maxAssetTagCount,
			maxAssetVocabularyCount, maxBlogsEntryCount, maxDDLCustomFieldCount,
			maxGroupCount, maxJournalArticleCount, maxJournalArticleSize,
			maxMBCategoryCount, maxMBThreadCount, maxMBMessageCount,
			maxUserToGroupCount);

		Map<String, Object> context = new HashMap<String, Object>();

		context.put("counter", dataFactory.getCounter());
		context.put("dataFactory", dataFactory);
		context.put("maxAssetPublisherPageCount", maxAssetPublisherPageCount);
		context.put("maxDLFileEntrySize", maxDLFileEntrySize);
		context.put("maxBlogsEntryCommentCount", maxBlogsEntryCommentCount);
		context.put("maxBlogsEntryCount", maxBlogsEntryCount);
		context.put("maxDDLRecordCount", maxDDLRecordCount);
		context.put("maxDDLRecordSetCount", maxDDLRecordSetCount);
		context.put("maxDLFileEntryCount", maxDLFileEntryCount);
		context.put("maxDLFolderCount", maxDLFolderCount);
		context.put("maxDLFolderDepth", maxDLFolderDepth);
		context.put("maxGroupCount", maxGroupCount);
		context.put("maxJournalArticleCount", maxJournalArticleCount);
		context.put("maxJournalArticlePageCount", maxJournalArticlePageCount);
		context.put(
			"maxJournalArticleVersionCount", maxJournalArticleVersionCount);
		context.put("maxMBCategoryCount", maxMBCategoryCount);
		context.put("maxMBMessageCount", maxMBMessageCount);
		context.put("maxMBThreadCount", maxMBThreadCount);
		context.put("maxUserCount", maxUserCount);
		context.put("maxUserToGroupCount", maxUserToGroupCount);
		context.put("maxWikiNodeCount", maxWikiNodeCount);
		context.put("maxWikiPageCommentCount", maxWikiPageCommentCount);
		context.put("maxWikiPageCount", maxWikiPageCount);

		return context;
	}

	protected List<Writer> initCSVWriters(
			Map<String, Object> context, String outputDir, String[] fileNames)
		throws Exception {

		List<Writer> writers = new ArrayList<Writer>();

		for (String fileName : fileNames) {
			Writer writer = createFileWriter(
				new File(outputDir, fileName + ".csv"));

			context.put(fileName + "CSVWriter", writer);

			writers.add(writer);
		}

		return writers;
	}

	protected void mergeSQL(
			File mergedSQLFile, File tempDir, String lastSQLFileName)
		throws IOException {

		FileOutputStream fileOutputStream = new FileOutputStream(mergedSQLFile);
		FileChannel fileChannel = fileOutputStream.getChannel();

		File lastSQLFile = null;

		for (File tableFile : tempDir.listFiles()) {
			if (tableFile.getName().equals(lastSQLFileName)) {
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

	private static final int _PIPE_BUFFER_SIZE = 16 * 1024 * 1024;

	private static final String _TPL_ROOT =
		"com/liferay/portal/tools/samplesqlbuilder/dependencies/";

	private static final int _WRITER_BUFFER_SIZE = 16 * 1024;

}
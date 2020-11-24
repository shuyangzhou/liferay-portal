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

package com.liferay.dispatch.talend.web.internal.executor;

import com.liferay.dispatch.executor.BaseDispatchTaskExecutor;
import com.liferay.dispatch.executor.DispatchTaskExecutor;
import com.liferay.dispatch.executor.DispatchTaskExecutorOutput;
import com.liferay.dispatch.model.DispatchTrigger;
import com.liferay.dispatch.repository.DispatchFileRepository;
import com.liferay.dispatch.repository.exception.DispatchRepositoryException;
import com.liferay.dispatch.service.DispatchTriggerLocalService;
import com.liferay.dispatch.talend.web.internal.archive.TalendArchive;
import com.liferay.dispatch.talend.web.internal.archive.TalendArchiveParser;
import com.liferay.dispatch.talend.web.internal.process.TalendProcess;
import com.liferay.dispatch.talend.web.internal.process.TalendProcessCallable;
import com.liferay.petra.concurrent.NoticeableFuture;
import com.liferay.petra.io.StreamUtil;
import com.liferay.petra.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.petra.process.CollectorOutputProcessor;
import com.liferay.petra.process.ProcessChannel;
import com.liferay.petra.process.ProcessConfig;
import com.liferay.petra.process.ProcessException;
import com.liferay.petra.process.ProcessLog;
import com.liferay.petra.process.local.LocalProcessExecutor;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Date;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Igor Beslic
 */
@Component(
	immediate = true,
	property = "dispatch.task.executor.type=" + TalendDispatchTaskExecutor.DISPATCH_TASK_EXECUTOR_TYPE_TALEND,
	service = DispatchTaskExecutor.class
)
public class TalendDispatchTaskExecutor extends BaseDispatchTaskExecutor {

	public static final String DISPATCH_TASK_EXECUTOR_TYPE_TALEND = "talend";

	@Override
	public void doExecute(
			DispatchTrigger dispatchTrigger,
			DispatchTaskExecutorOutput dispatchTaskExecutorOutput)
		throws PortalException {

		TalendArchive talendArchive = fetchTalendArchive(
			dispatchTrigger.getDispatchTriggerId());

		if (talendArchive == null) {
			throw new PortalException("Unable to fetch talend archive");
		}

		TalendProcess talendProcess = _getTalendProcess(
			dispatchTrigger, talendArchive);

		ProcessConfig talendProcessConfig = talendProcess.getProcessConfig();

		ProcessConfig.Builder processConfigBuilder = new ProcessConfig.Builder(
			talendProcessConfig);

		processConfigBuilder.setBootstrapClassPath(_getBootstrapClassPath());
		processConfigBuilder.setProcessLogConsumer(this::consumeProcessLog);

		try {
			ProcessChannel<Serializable> processChannel =
				_localProcessExecutor.execute(
					processConfigBuilder.build(),
					new TalendProcessCallable(
						ArrayUtil.toStringArray(
							talendProcess.getJobArguments()),
						talendArchive.getJobMainClassFQN()));

			NoticeableFuture<Serializable> future =
				processChannel.getProcessNoticeableFuture();

			while (!future.isDone()) {
				Thread.yield();
			}

			if (_log.isInfoEnabled()) {
				_log.info(
					"Completed job for dispatch trigger ID " +
						dispatchTrigger.getDispatchTriggerId());
			}
		}
		catch (Exception exception) {
			throw new PortalException(exception);
		}
		finally {
			FileUtil.deltree(new File(talendArchive.getJobDirectory()));
		}
	}

	@Override
	public String getName() {
		return DISPATCH_TASK_EXECUTOR_TYPE_TALEND;
	}

	protected void consumeProcessLog(ProcessLog processLog) {
		if (ProcessLog.Level.DEBUG == processLog.getLevel()) {
			if (_log.isDebugEnabled()) {
				_log.debug(processLog.getMessage(), processLog.getThrowable());
			}
		}
		else if (ProcessLog.Level.INFO == processLog.getLevel()) {
			if (_log.isInfoEnabled()) {
				_log.info(processLog.getMessage(), processLog.getThrowable());
			}
		}
		else if (ProcessLog.Level.WARN == processLog.getLevel()) {
			if (_log.isWarnEnabled()) {
				_log.warn(processLog.getMessage(), processLog.getThrowable());
			}
		}
		else {
			_log.error(processLog.getMessage(), processLog.getThrowable());
		}
	}

	protected TalendArchive fetchTalendArchive(long dispatchTriggerId)
		throws PortalException {

		FileEntry fileEntry = _dispatchFileRepository.fetchFileEntry(
			dispatchTriggerId);

		if (fileEntry == null) {
			throw new DispatchRepositoryException(
				"Unable to get file entry for dispatch trigger ID " +
					dispatchTriggerId);
		}

		return _talendArchiveParser.parse(fileEntry.getContentStream());
	}

	private String _createClasspath(
		Path dirPath, DirectoryStream.Filter<Path> filter) {

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(
				dirPath, filter)) {

			StringBundler sb = new StringBundler();

			directoryStream.forEach(
				path -> {
					sb.append(path);
					sb.append(File.pathSeparator);
				});

			if (sb.index() > 0) {
				sb.setIndex(sb.index() - 1);
			}

			return sb.toString();
		}
		catch (IOException ioException) {
			throw new RuntimeException(
				"Unable to iterate " + dirPath, ioException);
		}
	}

	private String _getBootstrapClassPath() {
		return _createClasspath(
			Paths.get(_props.get(PropsKeys.LIFERAY_LIB_GLOBAL_DIR)),
			path -> {
				String fileName = String.valueOf(path.getFileName());

				if (fileName.startsWith("com.liferay.petra")) {
					return true;
				}

				return false;
			});
	}

	private TalendProcess _getTalendProcess(
		DispatchTrigger dispatchTrigger, TalendArchive talendArchive) {

		TalendProcess.Builder talendProcessBuilder =
			new TalendProcess.Builder();

		talendProcessBuilder.companyId(dispatchTrigger.getCompanyId());

		Date lastRunStateDate =
			_dispatchTriggerLocalService.fetchPreviousFireDate(
				dispatchTrigger.getDispatchTriggerId());

		talendProcessBuilder.lastRunStartDate(lastRunStateDate);

		talendProcessBuilder.talendArchive(talendArchive);

		UnicodeProperties taskSettingsUnicodeProperties =
			dispatchTrigger.getTaskSettingsUnicodeProperties();

		if (taskSettingsUnicodeProperties != null) {
			for (Map.Entry<String, String> propEntry :
					taskSettingsUnicodeProperties.entrySet()) {

				talendProcessBuilder.contextParam(
					propEntry.getKey(), propEntry.getValue());
			}
		}

		return talendProcessBuilder.build();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		TalendDispatchTaskExecutor.class);

	@Reference
	private DispatchFileRepository _dispatchFileRepository;

	@Reference
	private DispatchTriggerLocalService _dispatchTriggerLocalService;

	private final LocalProcessExecutor _localProcessExecutor =
		new LocalProcessExecutor();

	@Reference
	private Props _props;

	private final TalendArchiveParser _talendArchiveParser =
		new TalendArchiveParser();

	private class DispatchTalendCollectorOutputProcessor
		extends CollectorOutputProcessor {

		@Override
		public byte[] processStdErr(InputStream stdErrInputStream)
			throws ProcessException {

			UnsyncByteArrayOutputStream unsyncByteArrayOutputStream =
				new UnsyncByteArrayOutputStream();

			try {
				StreamUtil.transfer(
					stdErrInputStream, unsyncByteArrayOutputStream, false);
			}
			catch (IOException ioException) {
				throw new ProcessException(ioException);
			}

			_stdErrByteArray = unsyncByteArrayOutputStream.toByteArray();

			return _stdErrByteArray;
		}

		private byte[] _stdErrByteArray;

	}

}
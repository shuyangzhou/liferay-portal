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

package com.liferay.simple.socks.proxy.manager;

import com.liferay.petra.concurrent.NoticeableFuture;
import com.liferay.petra.process.ProcessChannel;
import com.liferay.petra.process.ProcessConfig;
import com.liferay.petra.process.ProcessException;
import com.liferay.petra.process.local.LocalProcessExecutor;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.simple.socks.proxy.manager.process.SocksProxyServerCloseProcessCallable;
import com.liferay.simple.socks.proxy.manager.process.SocksProxyServerProcessCallable;

import java.io.Serializable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Tom Wang
 */
public class SocksProxyServerManager {

	public SocksProxyServerManager(
		LocalProcessExecutor localProcessExecutor,
		List<String> allowedIPAddress, long shutdownWaitTime,
		int serverSocketPort) {

		_localProcessExecutor = localProcessExecutor;
		_allowedIPAddress = allowedIPAddress;
		_shutdownWaitTime = shutdownWaitTime;
		_serverSocketPort = serverSocketPort;
	}

	public void start() throws ProcessException {
		_processChannel = _localProcessExecutor.execute(
			_processConfig,
			new SocksProxyServerProcessCallable(
				_allowedIPAddress, _shutdownWaitTime, _serverSocketPort));
	}

	public void stop() {
		try {
			NoticeableFuture<Serializable> future = _processChannel.write(
				new SocksProxyServerCloseProcessCallable());

			future.get(_shutdownWaitTime, TimeUnit.MILLISECONDS);
		}
		catch (Exception e) {
			if (_log.isInfoEnabled()) {
				_log.info("Server terminated", e);
			}
		}
		finally {
			NoticeableFuture<Serializable> future =
				_processChannel.getProcessNoticeableFuture();

			try {
				future.get(_shutdownWaitTime, TimeUnit.MILLISECONDS);
			}
			catch (Exception e) {
				if (_log.isInfoEnabled()) {
					_log.info("ProcessChannel terminated", e);
				}
			}
			finally {
				future.cancel(true);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SocksProxyServerManager.class);

	private static final ProcessConfig _processConfig;

	static {
		ProcessConfig.Builder builder = new ProcessConfig.Builder();

		_processConfig = builder.build();
	}

	private final List<String> _allowedIPAddress;
	private final LocalProcessExecutor _localProcessExecutor;
	private ProcessChannel<Serializable> _processChannel;
	private final int _serverSocketPort;
	private final long _shutdownWaitTime;

}
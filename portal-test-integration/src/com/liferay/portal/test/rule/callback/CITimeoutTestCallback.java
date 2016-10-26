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

package com.liferay.portal.test.rule.callback;

import com.liferay.ibm.icu.impl.Assert;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.test.rule.callback.BaseTestCallback;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.Time;

import org.junit.runner.Description;

/**
 * @author Shuyang Zhou
 */
public class CITimeoutTestCallback extends BaseTestCallback<Thread, Object> {

	public static final CITimeoutTestCallback INSTANCE =
		new CITimeoutTestCallback();

	@Override
	public void afterClass(
		Description description, Thread ciTimeoutMonitorThread) {

		ciTimeoutMonitorThread.interrupt();
	}

	@Override
	public Thread beforeClass(Description description) {
		Thread ciTimeoutMonitorThread = new Thread(
			"CI timeout monitor thread for " + description.getClassName()) {

			@Override
			public void run() {
				try {
					Thread.sleep(TestPropsValues.CI_TEST_TIMEOUT_TIME);

					Thread killerThread = new Thread(
						"CI timeout killer thread for " +
							description.getClassName()) {

						@Override
						public void run() {
							try {
								Thread.sleep(Time.MINUTE);

								System.exit(140);
							}
							catch (InterruptedException ie) {
								_log.error(getName() + " got cancelled");
							}
						}

					};

					killerThread.start();

					Assert.fail(
						"Scheduled to kill the current CI jvm in 1 minute, " +
							"because of " + description.getClassName() +
							" timeout");
				}
				catch (InterruptedException ie) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							description.getClassName() +
								" completed in time, cancelled timeout " +
									"monitoring.");
					}
				}
			}

		};

		ciTimeoutMonitorThread.start();

		return ciTimeoutMonitorThread;
	}

	private CITimeoutTestCallback() {
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CITimeoutTestCallback.class);

}
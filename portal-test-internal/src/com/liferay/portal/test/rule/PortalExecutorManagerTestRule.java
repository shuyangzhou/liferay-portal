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

package com.liferay.portal.test.rule;

import com.liferay.portal.kernel.concurrent.ThreadPoolExecutor;
import com.liferay.portal.kernel.executor.PortalExecutorManager;
import com.liferay.portal.kernel.test.rule.BaseTestRule;
import com.liferay.portal.test.rule.callback.PersistenceTestCallback;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

/**
 * @author Michael C. Han
 */
public class PortalExecutorManagerTestRule
	extends BaseTestRule<Object, Object> {

	public static final PortalExecutorManagerTestRule INSTANCE =
		new PortalExecutorManagerTestRule();

	protected class MockPortalExecutorManager implements PortalExecutorManager {

		@Override
		public ThreadPoolExecutor getPortalExecutor(String name) {
			return _threadPoolExecutor;
		}

		@Override
		public ThreadPoolExecutor getPortalExecutor(
			String name, boolean createIfAbsent) {

			return _threadPoolExecutor;
		}

		@Override
		public ThreadPoolExecutor registerPortalExecutor(
			String name, ThreadPoolExecutor threadPoolExecutor) {

			return _threadPoolExecutor;
		}

		@Override
		public void shutdown() {
			shutdown(false);
		}

		@Override
		public void shutdown(boolean interrupt) {
			if (interrupt) {
				_threadPoolExecutor.shutdownNow();
			}
			else {
				_threadPoolExecutor.shutdown();
			}
		}

		private final ThreadPoolExecutor _threadPoolExecutor =
			new ThreadPoolExecutor(0, 1);

	}

	private PortalExecutorManagerTestRule() {
		super(PersistenceTestCallback.INSTANCE);

		RegistryUtil.setRegistry(new BasicRegistryImpl());

		Registry registry = RegistryUtil.getRegistry();

		PortalExecutorManager portalExecutorManager =
			new MockPortalExecutorManager();

		registry.registerService(
			PortalExecutorManager.class, portalExecutorManager);
	}

}
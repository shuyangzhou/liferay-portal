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

package com.liferay.portal.events;

import com.liferay.portal.kernel.concurrent.ThreadPoolExecutor;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.executor.PortalExecutorManagerUtil;
import com.liferay.portal.kernel.interval.IntervalActionProcessor;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.model.impl.GroupModelImpl;
import com.liferay.portal.model.impl.OrganizationModelImpl;
import com.liferay.portal.model.impl.RoleModelImpl;
import com.liferay.portal.model.impl.UserGroupModelImpl;
import com.liferay.portal.model.impl.UserModelImpl;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserGroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * @author Preston Crary
 */
@SuppressWarnings("unused")
public class CacheStartupAction extends SimpleAction {

	@Override
	public void run(String[] ids) {
		try {
			initializeGroupCache();
			initializeOrganizationCache();
			initializeRoleCache();
			initializeUserCache();
			initializeUserGroupCache();
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}
	}

	protected void initializeGroupCache() throws PortalException {
		if (!GroupModelImpl.ENTITY_CACHE_ENABLED) {
			return;
		}

		int count = GroupLocalServiceUtil.getGroupsCount();

		final IntervalActionProcessor<Void> intervalActionProcessor =
			new IntervalActionProcessor<>(count, _INTERVAL);

		intervalActionProcessor.setPerformIntervalActionMethod(
			new IntervalActionProcessor.PerformIntervalActionMethod<Void>() {

			@Override
			public Void performIntervalAction(final int start, final int end) {
				_threadPoolExecutor.submit(new Runnable() {

					@Override
					public void run() {
						GroupLocalServiceUtil.getGroups(start, end);
					}

				});

				intervalActionProcessor.incrementStart(_INTERVAL);

				return null;
			}

		});

		intervalActionProcessor.performIntervalActions();
	}

	protected void initializeOrganizationCache() throws PortalException {
		if (!OrganizationModelImpl.ENTITY_CACHE_ENABLED) {
			return;
		}

		int count = OrganizationLocalServiceUtil.getOrganizationsCount();

		final IntervalActionProcessor<Void> intervalActionProcessor =
			new IntervalActionProcessor<>(count, _INTERVAL);

		intervalActionProcessor.setPerformIntervalActionMethod(
			new IntervalActionProcessor.PerformIntervalActionMethod<Void>() {

			@Override
			public Void performIntervalAction(final int start, final int end) {
				_threadPoolExecutor.submit(new Runnable() {

					@Override
					public void run() {
						OrganizationLocalServiceUtil.getOrganizations(
							start, end);
					}

				});

				intervalActionProcessor.incrementStart(_INTERVAL);

				return null;
			}

		});

		intervalActionProcessor.performIntervalActions();
	}

	protected void initializeRoleCache() throws PortalException {
		if (!RoleModelImpl.ENTITY_CACHE_ENABLED) {
			return;
		}

		int count = RoleLocalServiceUtil.getRolesCount();

		final IntervalActionProcessor<Void> intervalActionProcessor =
			new IntervalActionProcessor<>(count, _INTERVAL);

		intervalActionProcessor.setPerformIntervalActionMethod(
			new IntervalActionProcessor.PerformIntervalActionMethod<Void>() {

			@Override
			public Void performIntervalAction(final int start, final int end) {
				_threadPoolExecutor.submit(new Runnable() {

					@Override
					public void run() {
						RoleLocalServiceUtil.getRoles(start, end);
					}

				});

				intervalActionProcessor.incrementStart(_INTERVAL);

				return null;
			}

		});

		intervalActionProcessor.performIntervalActions();
	}

	protected void initializeUserCache() throws PortalException {
		if (!UserModelImpl.ENTITY_CACHE_ENABLED) {
			return;
		}

		int count = UserLocalServiceUtil.getUsersCount();

		final IntervalActionProcessor<Void> intervalActionProcessor =
			new IntervalActionProcessor<>(count, _INTERVAL);

		intervalActionProcessor.setPerformIntervalActionMethod(
			new IntervalActionProcessor.PerformIntervalActionMethod<Void>() {

			@Override
			public Void performIntervalAction(final int start, final int end) {
				_threadPoolExecutor.submit(new Runnable() {

					@Override
					public void run() {
						UserLocalServiceUtil.getUsers(start, end);
					}

				});

				intervalActionProcessor.incrementStart(_INTERVAL);

				return null;
			}

		});

		intervalActionProcessor.performIntervalActions();
	}

	protected void initializeUserGroupCache() throws PortalException {
		if (!UserGroupModelImpl.ENTITY_CACHE_ENABLED) {
			return;
		}

		int count = UserGroupLocalServiceUtil.getUserGroupsCount();

		final IntervalActionProcessor<Void> intervalActionProcessor =
			new IntervalActionProcessor<>(count, _INTERVAL);

		intervalActionProcessor.setPerformIntervalActionMethod(
			new IntervalActionProcessor.PerformIntervalActionMethod<Void>() {

			@Override
			public Void performIntervalAction(final int start, final int end) {
				_threadPoolExecutor.submit(new Runnable() {

					@Override
					public void run() {
						UserGroupLocalServiceUtil.getUserGroups(start, end);
					}

				});

				intervalActionProcessor.incrementStart(_INTERVAL);

				return null;
			}

		});

		intervalActionProcessor.performIntervalActions();
	}

	private static final int _INTERVAL = Indexer.DEFAULT_INTERVAL;

	private static final Log _log = LogFactoryUtil.getLog(
		CacheStartupAction.class);

	private static final ThreadPoolExecutor _threadPoolExecutor =
		PortalExecutorManagerUtil.getPortalExecutor(
			CacheStartupAction.class.getName());

}
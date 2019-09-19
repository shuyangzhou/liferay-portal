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

package com.liferay.batch.engine.internal.messaging;

import com.liferay.batch.engine.BatchEngineTaskExecuteStatus;
import com.liferay.batch.engine.BatchEngineTaskExecutor;
import com.liferay.batch.engine.model.BatchEngineTask;
import com.liferay.batch.engine.service.BatchEngineTaskLocalService;
import com.liferay.petra.executor.PortalExecutorManager;
import com.liferay.portal.kernel.cluster.ClusterMasterExecutor;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.SchedulerEntry;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.TimeUnit;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica Cardic
 */
@Component(
	immediate = true, property = {"interval=15", "parallel.executions=4"},
	service = MessageListener.class
)
public class BatchEngineTaskListener extends BaseMessageListener {

	@Activate
	protected void activate(Map<String, Object> properties) {
		if (_clusterMasterExecutor.isEnabled() &&
			!_clusterMasterExecutor.isMaster()) {

			return;
		}

		_parallelExecutions = GetterUtil.getInteger(
			properties.get("parallel.executions"));

		_checkUnfinishedBatchEngineTasks();

		_batchEngineTaskExecutor.enable();

		_activateSchedulerEngine(
			GetterUtil.getInteger(properties.get("interval")));
	}

	@Deactivate
	protected void deactivate() {
		_schedulerEngineHelper.unregister(this);

		_batchEngineTaskExecutor.disable();

		try {
			_latch.awaitZero();
		}
		catch (InterruptedException ie) {
			_log.error(ie.getMessage(), ie);
		}
	}

	@Override
	protected void doReceive(Message message) throws Exception {
		if (_batchEngineTaskExecutor.isDisabled()) {
			return;
		}

		_startBatchEngineTasks();
	}

	private void _activateSchedulerEngine(int interval) {
		Class<?> clazz = getClass();

		String className = clazz.getName();

		Trigger trigger = _triggerFactory.createTrigger(
			className, className, null, null, interval, TimeUnit.SECOND);

		SchedulerEntry schedulerEntry = new SchedulerEntryImpl(
			className, trigger);

		_schedulerEngineHelper.register(
			this, schedulerEntry, DestinationNames.SCHEDULER_DISPATCH);
	}

	private void _checkUnfinishedBatchEngineTasks() {
		List<BatchEngineTask> batchEngineTasks =
			_batchEngineTaskLocalService.getBatchEngineTasks(
				BatchEngineTaskExecuteStatus.STARTED);

		for (BatchEngineTask batchEngineTask : batchEngineTasks) {
			batchEngineTask.setExecuteStatus(
				BatchEngineTaskExecuteStatus.INITIAL.toString());

			_batchEngineTaskLocalService.updateBatchEngineTask(batchEngineTask);
		}
	}

	private void _startBatchEngineTasks() {
		int startedBatchEngineTaskCount =
			_batchEngineTaskLocalService.countBatchEngineTasks(
				BatchEngineTaskExecuteStatus.STARTED);

		int availableBatchEngineTaskCount =
			_parallelExecutions - startedBatchEngineTaskCount;

		if (availableBatchEngineTaskCount == 0) {
			return;
		}

		List<BatchEngineTask> batchEngineTasks =
			_batchEngineTaskLocalService.getFirstBatchEngineTasks(
				BatchEngineTaskExecuteStatus.INITIAL,
				availableBatchEngineTaskCount);

		ExecutorService executorService =
			_portalExecutorManager.getPortalExecutor(
				BatchEngineTaskListener.class.getName());

		for (BatchEngineTask batchEngineTask : batchEngineTasks) {
			executorService.submit(
				() -> {
					_latch.increment();

					try {
						_batchEngineTaskExecutor.execute(batchEngineTask);
					}
					finally {
						_latch.decrement();
					}
				});
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BatchEngineTaskListener.class);

	@Reference
	private BatchEngineTaskExecutor _batchEngineTaskExecutor;

	@Reference
	private BatchEngineTaskLocalService _batchEngineTaskLocalService;

	@Reference
	private ClusterMasterExecutor _clusterMasterExecutor;

	private final Latch _latch = new Latch();

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED)
	private ModuleServiceLifecycle _moduleServiceLifecycle;

	private int _parallelExecutions;

	@Reference
	private PortalExecutorManager _portalExecutorManager;

	@Reference
	private SchedulerEngineHelper _schedulerEngineHelper;

	@Reference
	private TriggerFactory _triggerFactory;

	private static class Latch {

		public void awaitZero() throws InterruptedException {
			synchronized (_monitor) {
				while (_count > 0) {
					_monitor.wait();
				}
			}
		}

		public void decrement() {
			synchronized (_monitor) {
				if (--_count <= 0) {
					_monitor.notifyAll();
				}
			}
		}

		public void increment() {
			synchronized (_monitor) {
				_count++;
			}
		}

		private int _count;
		private final Object _monitor = new Object();

	}

}
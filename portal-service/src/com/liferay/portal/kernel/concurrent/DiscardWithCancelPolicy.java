/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.concurrent;

import java.util.concurrent.Future;

/**
 * Handles rejected tasks by canceling them immediately.
 *
 * <p>
 * Use this policy for efficiently discarding rejected tasks. Unlike {@link
 * DiscardWithCancelPolicy}, this policy maintains the order of tasks in the
 * task queue and the application server's rendering of portlets is not
 * disrupted. Unlike {@link DiscardOldestPolicy} and {@link DiscardPolicy}, the
 * meaningless blocking wait until timeout is bypassed, since {@link
 * Future#get()} is not called for the rejected task.
 * </p>
 *
 * @author Shuyang Zhou
 */
public class DiscardWithCancelPolicy implements RejectedExecutionHandler {

	/**
	 * Rejects execution of the {@link Runnable} task by canceling it
	 * immediately.
	 *
	 * @param runnable the task
	 * @param threadPoolExecutor the executor
	 */
	public void rejectedExecution(
		Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {

		if (runnable instanceof Future<?>) {
			Future<?> future = (Future<?>)runnable;

			// There is no point to try and interrupt the runner thread since
			// being rejected means it is not yet running

			future.cancel(false);
		}
	}

}
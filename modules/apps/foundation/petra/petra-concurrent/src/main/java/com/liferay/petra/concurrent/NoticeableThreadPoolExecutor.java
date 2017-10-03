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

package com.liferay.petra.concurrent;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author Shuyang Zhou
 */
public class NoticeableThreadPoolExecutor
	extends AbstractNoticeableExecutorService {

	public NoticeableThreadPoolExecutor(
		int corePoolSize, int maximumPoolSize, long keepAliveTime,
		TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue,
		ThreadFactory threadFactory,
		RejectedExecutionHandler rejectedExecutionHandler) {

		AtomicInteger terminationCounter = new AtomicInteger(2);

		_workerThreadPoolExecutor = new java.util.concurrent.ThreadPoolExecutor(
			corePoolSize, maximumPoolSize, keepAliveTime, timeUnit,
			new SynchronousQueue<>(), threadFactory,
			(runnable, threadPoolExecutor) -> {
				if (threadPoolExecutor.isShutdown()) {
					rejectedExecutionHandler.rejectedExecution(
						runnable, threadPoolExecutor);

					return;
				}

				WorkerRunnable workerRunnable = (WorkerRunnable)runnable;

				if (!workerRunnable._tryToRunAsWaitingTask()) {

					// Unable to lock down any worker or dispatcher pool has
					// been shutdown with interruption, re-execute.

					threadPoolExecutor.execute(runnable);
				}
			}) {

			@Override
			protected void terminated() {
				if (terminationCounter.decrementAndGet() == 0) {
					terminationDefaultNoticeableFuture.run();
				}
			}

		};

		_dispatcherThreadPoolExecutor =
			new java.util.concurrent.ThreadPoolExecutor(
				0, 1, keepAliveTime, timeUnit, blockingQueue,
				runnable -> {
					Thread thread = threadFactory.newThread(runnable);

					thread.setName(thread.getName() + "-dispatcher");

					return thread;
				},
				rejectedExecutionHandler) {

				@Override
				protected void terminated() {
					if (terminationCounter.decrementAndGet() == 0) {
						terminationDefaultNoticeableFuture.run();
					}
				}

			};
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit timeUnit)
		throws InterruptedException {

		long startTime = System.currentTimeMillis();

		if (!_dispatcherThreadPoolExecutor.awaitTermination(
				timeout, timeUnit)) {

			return false;
		}

		timeout -= timeUnit.convert(
			System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);

		return _workerThreadPoolExecutor.awaitTermination(timeout, timeUnit);
	}

	@Override
	public void execute(Runnable runnable) {
		if (runnable == null) {
			throw new NullPointerException("Runnable is null");
		}

		_dispatcherThreadPoolExecutor.execute(
			() -> {
				_workerThreadPoolExecutor.execute(new WorkerRunnable(runnable));
			});
	}

	public int getActiveCount() {
		return _workerThreadPoolExecutor.getActiveCount();
	}

	public long getCompletedTaskCount() {
		return _workerThreadPoolExecutor.getCompletedTaskCount() +
			_completedWaitingTasks.sum();
	}

	public int getCorePoolSize() {
		return _workerThreadPoolExecutor.getCorePoolSize();
	}

	public int getLargestPoolSize() {
		return _workerThreadPoolExecutor.getLargestPoolSize();
	}

	public int getMaximumPoolSize() {
		return _workerThreadPoolExecutor.getMaximumPoolSize();
	}

	public int getPendingTaskCount() {
		BlockingQueue<Runnable> dispatcherBlockingQueue =
			_dispatcherThreadPoolExecutor.getQueue();

		return dispatcherBlockingQueue.size();
	}

	public int getPoolSize() {
		return _dispatcherThreadPoolExecutor.getPoolSize();
	}

	@Override
	public boolean isShutdown() {
		return _dispatcherThreadPoolExecutor.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		if (_dispatcherThreadPoolExecutor.isTerminated() ||
			_workerThreadPoolExecutor.isTerminated()) {

			return true;
		}

		return false;
	}

	public void setCorePoolSize(int corePoolSize) {
		_workerThreadPoolExecutor.setCorePoolSize(corePoolSize);
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		_workerThreadPoolExecutor.setMaximumPoolSize(maximumPoolSize);
	}

	@Override
	public void shutdown() {
		_dispatcherThreadPoolExecutor.shutdown();

		_workerThreadPoolExecutor.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		List<Runnable> runnables = _dispatcherThreadPoolExecutor.shutdownNow();

		_workerThreadPoolExecutor.shutdownNow();

		return runnables;
	}

	private final LongAdder _completedWaitingTasks = new LongAdder();
	private final java.util.concurrent.ThreadPoolExecutor
		_dispatcherThreadPoolExecutor;
	private final Semaphore _semaphore = new Semaphore(0);
	private final AtomicReference<WorkerRunnable> _waitingRunnableReference =
		new AtomicReference<>();
	private final java.util.concurrent.ThreadPoolExecutor
		_workerThreadPoolExecutor;

	private class WorkerRunnable implements Runnable {

		@Override
		public void run() {
			Runnable currentRunnable = _runnable;

			Throwable throwable = null;

			while (currentRunnable != null) {
				_semaphore.release();

				try {
					currentRunnable.run();
				}
				catch (Throwable t) {
					if (throwable == null) {
						throwable = t;
					}
					else {
						throwable.addSuppressed(t);
					}
				}
				finally {
					_semaphore.acquireUninterruptibly();

					if (currentRunnable != _runnable) {
						_completedWaitingTasks.increment();
					}

					WorkerRunnable workerRunnable =
						_waitingRunnableReference.getAndSet(null);

					if (workerRunnable == null) {
						currentRunnable = null;
					}
					else {
						currentRunnable = workerRunnable._runnable;

						Semaphore waitingSemaphore =
							workerRunnable._waitingSemaphore;

						waitingSemaphore.release();
					}
				}
			}

			if (throwable != null) {
				throw new RuntimeException(throwable);
			}
		}

		private WorkerRunnable(Runnable runnable) {
			_runnable = runnable;
		}

		private boolean _tryToRunAsWaitingTask() {
			if (!_semaphore.tryAcquire()) {
				return false;
			}

			// Locked down one ongoing worker, blocking on waiting queue
			// until the job is taken by that worker.

			_waitingRunnableReference.set(this);

			_semaphore.release();

			try {
				_waitingSemaphore.acquire();
			}
			catch (InterruptedException ie) {

				// Dispatcher pool has been shutdown with interruption,
				// re-execute to check on shutdown.

			}

			return true;
		}

		private final Runnable _runnable;
		private final Semaphore _waitingSemaphore = new Semaphore(0);

	}

}
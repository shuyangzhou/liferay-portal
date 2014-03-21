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

package com.liferay.portal.kernel.concurrent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 * @author Manuel de la Peña
 */
public class TaskQueueTest {

	@Test
	public void testConstructor() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>(10);

		Assert.assertEquals(10, taskQueue.remainingCapacity());
		Assert.assertEquals(0, taskQueue.size());
	}

	@Test
	public void testConstructorDefault() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>();

		Assert.assertEquals(Integer.MAX_VALUE, taskQueue.remainingCapacity());
		Assert.assertEquals(0, taskQueue.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithZeroCapacity() {
		new TaskQueue<Object>(0);

		Assert.fail();
	}

	@Test
	public void testDrainToListMoreThanTwoElements() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>();

		Object object1 = new Object();
		Object object2 = new Object();
		Object object3 = new Object();
		Object object4 = new Object();

		Assert.assertTrue(taskQueue.offer(object1, new boolean[1]));
		Assert.assertTrue(taskQueue.offer(object2, new boolean[1]));
		Assert.assertTrue(taskQueue.offer(object3, new boolean[1]));
		Assert.assertTrue(taskQueue.offer(object4, new boolean[1]));

		List<Object> list = new ArrayList<Object>() {

			@Override
			public boolean add(Object e) {
				if (size() >= 2) {
					throw new IllegalStateException();
				}

				return super.add(e);
			}

		};

		try {
			taskQueue.drainTo(list);

			Assert.fail();
		}
		catch (IllegalStateException ise) {
		}

		Assert.assertEquals(2, list.size());
		Assert.assertSame(object1, list.get(0));
		Assert.assertSame(object2, list.get(1));
		Assert.assertEquals(2, taskQueue.size());
		Assert.assertSame(object3, taskQueue.poll());
		Assert.assertSame(object4, taskQueue.poll());
	}

	@Test(expected = NullPointerException.class)
	public void testDrainToNull() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>();

		taskQueue.drainTo(null);

		Assert.fail();
	}

	@Test
	public void testDrainToSet() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>();

		Object object1 = new Object();
		Object object2 = new Object();
		Object object3 = new Object();
		Object object4 = new Object();

		Assert.assertTrue(taskQueue.offer(object1, new boolean[1]));
		Assert.assertTrue(taskQueue.offer(object2, new boolean[1]));
		Assert.assertTrue(taskQueue.offer(object3, new boolean[1]));
		Assert.assertTrue(taskQueue.offer(object4, new boolean[1]));

		Set<Object> set = new HashSet<Object>();

		taskQueue.drainTo(set);

		Assert.assertEquals(4, set.size());
		Assert.assertTrue(set.contains(object1));
		Assert.assertTrue(set.contains(object2));
		Assert.assertTrue(set.contains(object3));
		Assert.assertTrue(set.contains(object4));
	}

	@Test
	public void testIsEmpty() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>();

		Assert.assertTrue(taskQueue.isEmpty());
	}

	@Test
	public void testIsEmptyAfterOffer() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>();

		Assert.assertTrue(taskQueue.offer(new Object(), new boolean[1]));

		Assert.assertFalse(taskQueue.isEmpty());
	}

	@Test
	public void testIsEmptyAfterOfferAndPoll() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>();

		taskQueue.offer(new Object(), new boolean[1]);

		Assert.assertNotNull(taskQueue.poll());
		Assert.assertTrue(taskQueue.isEmpty());
	}

	@Test(expected = NullPointerException.class)
	public void testOfferNull() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>(10);

		taskQueue.offer(null, new boolean[1]);

		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOfferWaiterMakerEmpty() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>(10);

			taskQueue.offer(new Object(), new boolean[0]);

			Assert.fail();
	}

	@Test
	public void testOfferWaiterMakerFalse() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>(10);

		boolean[] hasWaiterMarker = new boolean[1];

		boolean result = taskQueue.offer(new Object(), hasWaiterMarker);

		Assert.assertTrue(result);
		Assert.assertFalse(hasWaiterMarker[0]);
	}

	@Test
	public void testOfferWaiterMakerTrue() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>(10);

		boolean[] hasWaiterMarker = new boolean[1];
		hasWaiterMarker[0] = true;

		boolean result = taskQueue.offer(new Object(), hasWaiterMarker);

		Assert.assertTrue(result);
		Assert.assertTrue(hasWaiterMarker[0]);
	}

	@Test
	public void testPoll() throws InterruptedException {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>();

		Assert.assertNull(taskQueue.poll());
	}

	@Test
	public void testPollAfterOffer() throws InterruptedException {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>();

		Object object1 = new Object();

		Assert.assertTrue(taskQueue.offer(object1, new boolean[1]));

		Assert.assertSame(object1, taskQueue.poll());
	}

	@Test
	public void testPollAfterOffers() throws InterruptedException {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>();

		Object[] objects = new Object[1000];

		for (int i = 0; i < objects.length; i++) {
			objects[i] = new Object();
		}

		for (Object object : objects) {
			Assert.assertTrue(taskQueue.offer(object, new boolean[1]));

			Assert.assertSame(object, taskQueue.poll());
		}
	}

	@Test
	public void testPollWithTimeout() throws InterruptedException {
		TaskQueue<Object>taskQueue = new TaskQueue<Object>();

		Assert.assertNull(taskQueue.poll(0, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testPollWithTimeoutAfterOffer() throws InterruptedException {
		TaskQueue<Object>taskQueue = new TaskQueue<Object>();

		Object object1 = new Object();

		Assert.assertTrue(taskQueue.offer(object1, new boolean[1]));

		Assert.assertSame(object1, taskQueue.poll(100, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testPollWithTimeoutAfterOffers() throws InterruptedException {
		TaskQueue<Object>taskQueue = new TaskQueue<Object>();

		Object[] objects = new Object[1000];

		for (int i = 0; i < objects.length; i++) {
			objects[i] = new Object();
		}

		for (Object object : objects) {
			Assert.assertTrue(taskQueue.offer(object, new boolean[1]));

			Assert.assertSame(
				object, taskQueue.poll(100, TimeUnit.MILLISECONDS));
		}
	}

	@Test
	public void testPollWithTimeoutNegativeMilliseconds()
		throws InterruptedException {

		TaskQueue<Object>taskQueue = new TaskQueue<Object>();

		Assert.assertNull(taskQueue.poll(-1, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testPollWithTimeoutPositiveMilliseconds()
		throws InterruptedException {

		TaskQueue<Object>taskQueue = new TaskQueue<Object>();

		Assert.assertNull(taskQueue.poll(100, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testRemainingCapacity() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>(10);

		Assert.assertEquals(10, taskQueue.remainingCapacity());
	}

	@Test
	public void testRemainingCapacityAfterOffer() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>(10);

		for (int i = 1; i <= 10; i++) {
			Assert.assertTrue(taskQueue.offer(new Object(), new boolean[1]));

			Assert.assertEquals(10 - i, taskQueue.remainingCapacity());
		}
	}

	@Test
	public void testRemove() {
		int max = 1000;

		TaskQueue<Object> taskQueue = new TaskQueue<Object>(max);

		Object[] objects = new Object[max];

		for (int i = 0; i < objects.length; i++) {
			objects[i] = new Object();

			taskQueue.offer(objects[i], new boolean[1]);
		}

		for (int i = 0; i < objects.length; i++) {
			Object object = objects[i];

			int currentCount = objects.length - i;

			Assert.assertEquals(currentCount, taskQueue.size());
			Assert.assertTrue(taskQueue.remove(object));
			Assert.assertEquals(currentCount - 1, taskQueue.size());
		}
	}

	@Test
	public void testRemoveElementFromEmptyQueue() {
		int max = 1000;

		TaskQueue<Object> taskQueue = new TaskQueue<Object>(max);

		Object[] objects = new Object[max];

		for (int i = 0; i < objects.length; i++) {
			objects[i] = new Object();

			taskQueue.offer(objects[i], new boolean[1]);
		}

		for (Object object : objects) {
			taskQueue.remove(object);
		}

		Assert.assertFalse(taskQueue.remove(objects[1]));
	}

	@Test
	public void testRemoveElementTwice() {
		int max = 1000;

		TaskQueue<Object> taskQueue = new TaskQueue<Object>(max);

		Object[] objects = new Object[max];

		for (int i = 0; i < objects.length; i++) {
			objects[i] = new Object();

			taskQueue.offer(objects[i], new boolean[1]);
		}

		for (int i = 0; i < objects.length; i++) {
			Object object = objects[i];

			int currentCount = objects.length - i;

			taskQueue.remove(object);

			Assert.assertEquals(currentCount - 1, taskQueue.size());

			Assert.assertFalse(taskQueue.remove(object));

			Assert.assertEquals(currentCount - 1, taskQueue.size());
		}

		Assert.assertEquals(0, taskQueue.size());
	}

	@Test
	public void testRemoveFromEmptyQueue() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>(10);

		Assert.assertFalse(taskQueue.remove(new Object()));
	}

	@Test
	public void testRemoveNullFromEmptyQueue() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>(10);

		Assert.assertFalse(taskQueue.remove(null));
	}

	@Test
	public void testSize() {
		int maxCapacity = 1000;

		TaskQueue<Object> taskQueue = new TaskQueue<Object>(maxCapacity);

		Assert.assertEquals(0, taskQueue.size());

		for (int i = 1; i <= maxCapacity; i++) {
			Assert.assertTrue(taskQueue.offer(new Object(), new boolean[1]));

			Assert.assertEquals(i, taskQueue.size());
		}
	}

	@Test
	public void testSizeFromFullQueue() {
		int maxCapacity = 1000;

		TaskQueue<Object> taskQueue = new TaskQueue<Object>(maxCapacity);

		Assert.assertEquals(0, taskQueue.size());

		for (int i = 1; i <= maxCapacity; i++) {
			taskQueue.offer(new Object(), new boolean[1]);

			Assert.assertEquals(i, taskQueue.size());
		}

		taskQueue.offer(new Object(), new boolean[1]);

		Assert.assertEquals(maxCapacity, taskQueue.size());
		Assert.assertEquals(0, taskQueue.remainingCapacity());
	}

	@Test
	public void testSizeOne() {
		TaskQueue<Object> taskQueue = new TaskQueue<Object>(1);

		Assert.assertEquals(0, taskQueue.size());

		Assert.assertTrue(taskQueue.offer(new Object(), new boolean[1]));

		Assert.assertEquals(1, taskQueue.size());
	}

	@Test
	public void testTake10() throws InterruptedException {
		testTake(10, TestUtil.SHORT_WAIT);
	}

	@Test
	public void testTake10KeepAlive() throws InterruptedException {
		testTake(10, TestUtil.KEEPALIVE_TIME);
	}

	@Test
	public void testTake100() throws InterruptedException {
		testTake(100, TestUtil.SHORT_WAIT);
	}

	@Test
	public void testTake100KeepAlive() throws InterruptedException {
		testTake(100, TestUtil.KEEPALIVE_TIME);
	}

	@Test
	public void testTake1000() throws InterruptedException {
		testTake(1000, TestUtil.SHORT_WAIT);
	}

	@Test
	public void testTake1000KeepAlive() throws InterruptedException {
		testTake(1000, TestUtil.KEEPALIVE_TIME);
	}

	protected void testTake(final int capacity, long sleep)
		throws InterruptedException {

		final TaskQueue<Object> taskQueue = new TaskQueue<Object>();
		final Object object = new Object();

		Assert.assertTrue(taskQueue.offer(object, new boolean[1]));

		Assert.assertSame(object, taskQueue.take());

		Thread thread = new Thread() {

			@Override
			public void run() {
				try {
					for (int i = 0; i < capacity; i++) {
						Assert.assertEquals(i, taskQueue.take());
					}
				}
				catch (InterruptedException ie) {
					Assert.fail();
				}

				try {
					taskQueue.take();
					Assert.fail();
				}
				catch (InterruptedException ie) {
				}
			}

		};

		thread.start();

		for (int i = 0; i < capacity; i++) {
			Assert.assertTrue(taskQueue.offer(i, new boolean[1]));
		}

		Thread.sleep(sleep);

		thread.interrupt();
		thread.join();
	}

}
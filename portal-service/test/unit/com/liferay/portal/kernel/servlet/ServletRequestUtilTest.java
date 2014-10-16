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

package com.liferay.portal.kernel.servlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author László Csontos
 */
public class ServletRequestUtilTest {

	@Before
	public void setUp() throws Exception {
		_executorService = Executors.newFixedThreadPool(_NUM_THREADS);

		_request = new MockHttpServletRequest();
	}

	@After
	public void tearDown() throws Exception {
		_executorService.shutdownNow();
	}

	@Test
	public void testGetAndSetAttribute() throws Exception {
		MockRunnable mockRunnable = new GetAndSetAttribute();

		AtomicInteger mockAttribute = doTest(mockRunnable);

		// The original approach should fail in multi-threaded context

		Assert.assertTrue(mockAttribute.get() < _NUM_REQUESTS);
	}

	@Test
	public void testSetAttributeIfAbsentWithLock() throws Exception {
		MockRunnable mockRunnable = new SetAttributeIfAbsent();

		_request.setAttribute(
			WebKeys.PARALLEL_RENDERING_ATTRIBUTE_LOCK,
			new ReentrantReadWriteLock());

		AtomicInteger mockAttribute = doTest(mockRunnable);

		// With the global lock this should succeed

		Assert.assertEquals(_NUM_REQUESTS, mockAttribute.get());
	}

	@Test
	public void testSetAttributeIfAbsentWithoutLock() throws Exception {
		MockRunnable mockRunnable = new SetAttributeIfAbsent();

		AtomicInteger mockAttribute = doTest(mockRunnable);

		// If the global lock isn't there, this should also fail

		Assert.assertTrue(mockAttribute.get() < _NUM_REQUESTS);
	}

	@SuppressWarnings("unchecked")
	protected <T> T doTest(MockRunnable mockRunnable)
		throws InterruptedException {

		for (int i = 0; i < _NUM_REQUESTS; i++) {
			_executorService.submit(mockRunnable);
		}

		Assert.assertTrue(
			"Test has timed out",
			mockRunnable._latch.await(10, TimeUnit.SECONDS));

		T mockAttribute = (T)_request.getAttribute("MOCK_ATTRIBUTE");

		Assert.assertNotNull(mockAttribute);

		return mockAttribute;
	}

	private static final int _NUM_REQUESTS = 100;

	private static final int _NUM_THREADS =
		Runtime.getRuntime().availableProcessors();

	private static Log _log = LogFactoryUtil.getLog(
		ServletRequestUtilTest.class);

	private ExecutorService _executorService;
	private HttpServletRequest _request;

	private static abstract class MockRunnable implements Runnable {

		@Override
		public final void run() {
			try {
				AtomicInteger mockAttribute = getMockAttribute();

				mockAttribute.incrementAndGet();
			}
			catch (Exception e) {
				_log.error(e);
			}
			finally {
				_latch.countDown();
			}
		}

		protected abstract AtomicInteger getMockAttribute() throws Exception;

		private CountDownLatch _latch = new CountDownLatch(_NUM_REQUESTS);

	}

	private class GetAndSetAttribute extends MockRunnable {

		@Override
		public AtomicInteger getMockAttribute() throws InterruptedException {
			AtomicInteger mockAttribute = (AtomicInteger)_request.getAttribute(
				"MOCK_ATTRIBUTE");

			if (mockAttribute == null) {
				mockAttribute = new AtomicInteger();

				// Increase the likelihood of failure

				Thread.sleep(100);

				_request.setAttribute("MOCK_ATTRIBUTE", mockAttribute);
			}

			return mockAttribute;
		}

	}

	private class SetAttributeIfAbsent extends MockRunnable {

		@Override
		public AtomicInteger getMockAttribute() throws InterruptedException {
			AtomicInteger mockAttribute = (AtomicInteger)_request.getAttribute(
				"MOCK_ATTRIBUTE");

			if (mockAttribute == null) {
				mockAttribute = new AtomicInteger();

				// Increase the likelihood of failure

				Thread.sleep(100);

				mockAttribute =
					(AtomicInteger)ServletRequestUtil.setAttributeIfAbsent(
						_request, "MOCK_ATTRIBUTE", mockAttribute);
			}

			return mockAttribute;
		}

	}

}
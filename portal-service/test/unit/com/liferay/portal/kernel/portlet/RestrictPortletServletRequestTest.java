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

package com.liferay.portal.kernel.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.util.PropsImpl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author László Csontos
 */
public class RestrictPortletServletRequestTest {

	@BeforeClass
	public static void setUpClass() {
		PropsUtil.setProps(new PropsImpl());
	}

	@Before
	public void setUp() throws Exception {
		_executorService = Executors.newFixedThreadPool(_NUM_THREADS);

		_request = new RestrictPortletServletRequest(
			new MockHttpServletRequest());
	}

	@After
	public void tearDown() throws Exception {
		_executorService.shutdownNow();
	}

	@Test
	public void testGetAndSetAttribute() throws Exception {
		MockRunnable mockRunnable = new MockRunnable() {

			@Override
			public AtomicInteger getMockAttribute()
				throws InterruptedException {

				AtomicInteger mockAttribute =
					(AtomicInteger)_request.getAttribute("MOCK_ATTRIBUTE");

				if (mockAttribute == null) {
					mockAttribute = new AtomicInteger();

					// Increase the likelihood of failure

					Thread.sleep(100);

					_request.setAttribute("MOCK_ATTRIBUTE", mockAttribute);
				}

				return mockAttribute;
			}

		};

		AtomicInteger mockObject = doTest(mockRunnable);

		// The original approach should fail in multi-threaded context

		Assert.assertTrue(mockObject.get() < _NUM_REQUESTS);
	}

	@Test
	public void testSetAttributeIfAbsent() throws Exception {
		MockRunnable mockRunnable = new MockRunnable() {

			@Override
			public AtomicInteger getMockAttribute()
				throws InterruptedException {

				AtomicInteger mockAttribute =
					(AtomicInteger)_request.getAttribute("MOCK_ATTRIBUTE");

				if (mockAttribute == null) {
					mockAttribute = new AtomicInteger();

					// Increase the likelihood of failure

					Thread.sleep(100);

					mockAttribute =
						(AtomicInteger)_request.setAttributeIfAbsent(
							"MOCK_ATTRIBUTE", mockAttribute);
				}

				return mockAttribute;
			}

		};

		AtomicInteger mockObject = doTest(mockRunnable);

		Assert.assertEquals(_NUM_REQUESTS, mockObject.get());
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

		T mockObject = (T)_request.getAttribute("MOCK_ATTRIBUTE");

		Assert.assertNotNull(mockObject);

		return mockObject;
	}

	private static final int _NUM_REQUESTS = 100;

	private static final int _NUM_THREADS =
		Runtime.getRuntime().availableProcessors();

	private static Log _log = LogFactoryUtil.getLog(
		RestrictPortletServletRequestTest.class);

	private ExecutorService _executorService;
	private RestrictPortletServletRequest _request;

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

}
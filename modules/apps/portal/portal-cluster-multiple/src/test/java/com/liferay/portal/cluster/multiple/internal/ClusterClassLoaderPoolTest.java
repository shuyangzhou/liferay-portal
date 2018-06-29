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

package com.liferay.portal.cluster.multiple.internal;

import com.liferay.portal.cluster.multiple.internal.io.ClusterClassLoaderPool;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.osgi.framework.Version;

/**
 * @author Lance Ji
 */
public class ClusterClassLoaderPoolTest {

	@Before
	public void setUp() {
		Thread currentThread = Thread.currentThread();

		ClassLoader currentClassLoader = currentThread.getContextClassLoader();

		PortalClassLoaderUtil.setClassLoader(currentClassLoader);

		_classLoaders = ReflectionTestUtil.getFieldValue(
			ClusterClassLoaderPool.class, "_classLoaders");

		_classLoaders.clear();

		_contextNames = ReflectionTestUtil.getFieldValue(
			ClusterClassLoaderPool.class, "_contextNames");

		_contextNames.clear();

		_fallbackClassLoaders = ReflectionTestUtil.getFieldValue(
			ClusterClassLoaderPool.class, "_fallbackClassLoaders");

		_fallbackClassLoaders.clear();
	}

	@Test
	public void testGetFallbackClassLoader() {
		ClassLoader oldClassLoader = new URLClassLoader(new URL[0]);
		ClassLoader newClassLoader = new URLClassLoader(new URL[0]);

		ClusterClassLoaderPool.register(
			_CONTEXT_NAME, new Version("1.0.0"), oldClassLoader);
		ClusterClassLoaderPool.register(
			_CONTEXT_NAME, new Version("2.0.0"), newClassLoader);

		Assert.assertSame(
			newClassLoader,
			ClusterClassLoaderPool.getClassLoader(
				_CONTEXT_NAME.concat("_3.0.0")));
	}

	@Test
	public void testRegisterFallbackClassLoader() {
		ClassLoader oldClassLoader = new URLClassLoader(new URL[0]);
		ClassLoader newClassLoader = new URLClassLoader(new URL[0]);

		ClusterClassLoaderPool.register(
			_CONTEXT_NAME, new Version("1.0.0"), oldClassLoader);
		ClusterClassLoaderPool.register(
			_CONTEXT_NAME, new Version("2.0.0"), newClassLoader);

		Assert.assertEquals(
			_fallbackClassLoaders.toString(), 1, _fallbackClassLoaders.size());
	}

	@Test
	public void testUnregisterFallbackClassLoader() {
		ClassLoader classLoader1 = new URLClassLoader(new URL[0]);
		ClassLoader classLoader2 = new URLClassLoader(new URL[0]);

		ClusterClassLoaderPool.register(
			_CONTEXT_NAME, new Version("1.0.0"), classLoader1);

		ClusterClassLoaderPool.unregister(classLoader1);
		ClusterClassLoaderPool.unregister(classLoader2);

		_assertEmptyMaps();
	}

	private void _assertEmptyMaps() {
		Assert.assertTrue(_contextNames.toString(), _contextNames.isEmpty());
		Assert.assertTrue(_classLoaders.toString(), _classLoaders.isEmpty());
		Assert.assertTrue(
			_fallbackClassLoaders.toString(), _fallbackClassLoaders.isEmpty());
	}

	private static final String _CONTEXT_NAME = "contextName";

	private static Map<String, ClassLoader> _classLoaders;
	private static Map<ClassLoader, String> _contextNames;
	private static Map<String, List> _fallbackClassLoaders;

}
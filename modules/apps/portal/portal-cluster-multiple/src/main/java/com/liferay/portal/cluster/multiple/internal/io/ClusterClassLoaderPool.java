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

package com.liferay.portal.cluster.multiple.internal.io;

import com.liferay.petra.lang.ClassLoaderPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ClassLoaderUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.framework.Version;

/**
 * @author Lance Ji
 */
public class ClusterClassLoaderPool {

	public static void register(
		String symbolicName, Version version, ClassLoader classLoader) {

		String contextName = symbolicName;

		if (version != null) {
			contextName = StringBundler.concat(
				contextName, StringPool.UNDERLINE, version.toString());
		}

		_classLoaders.put(contextName, classLoader);
		_contextNames.put(classLoader, contextName);

		_registerFallback(symbolicName, version, classLoader);
	}

	public static void unregister(ClassLoader classLoader) {
		String contextName = _contextNames.remove(classLoader);

		if (contextName != null) {
			_classLoaders.remove(contextName);
		}
	}

	private static void _registerFallback(
		String symbolicName, Version version, ClassLoader classLoader) {

		if (version == null) {
			return;
		}

		List<VersionedClassLoader> versionedClassLoaders =
			_fallbackClassLoaders.get(symbolicName);

		if (versionedClassLoaders == null) {
			versionedClassLoaders = new CopyOnWriteArrayList<>();
		}

		versionedClassLoaders.add(
			new VersionedClassLoader(classLoader, version));

		if (versionedClassLoaders.size() > 1) {
			Collections.sort(versionedClassLoaders);
		}

		_fallbackClassLoaders.put(symbolicName, versionedClassLoaders);
	}

	private static final String _PORTAL_SERVLETCONTEXTNAME = StringPool.BLANK;

	private static final Map<String, ClassLoader> _classLoaders =
		new ConcurrentHashMap<>();
	private static final Map<ClassLoader, String> _contextNames =
		new ConcurrentHashMap<>();
	private static final Map<String, List<VersionedClassLoader>>
		_fallbackClassLoaders = new ConcurrentHashMap<>();

	static {
		register(
			"GlobalClassLoader", null, ClassLoaderPool.class.getClassLoader());
		register(
			_PORTAL_SERVLETCONTEXTNAME, null,
			ClassLoaderUtil.getPortalClassLoader());
	}

	private static class VersionedClassLoader
		implements Comparable<VersionedClassLoader> {

		@Override
		public int compareTo(VersionedClassLoader versionedClassLoader) {
			Version comparedVersion = versionedClassLoader.getVersion();

			return comparedVersion.compareTo(getVersion());
		}

		public ClassLoader getClassLoader() {
			return _classLoader;
		}

		public Version getVersion() {
			return _version;
		}

		private VersionedClassLoader(ClassLoader classLoader, Version version) {
			_classLoader = classLoader;
			_version = version;
		}

		private final ClassLoader _classLoader;
		private final Version _version;

	}

}
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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
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

	public static ClassLoader getClassLoader(String contextName) {
		ClassLoader classLoader = null;

		if ((contextName != null) && !contextName.equals("null")) {
			ClassLoader contextClassLoader =
				ClassLoaderUtil.getContextClassLoader();

			try {
				ClassLoaderUtil.setContextClassLoader(null);

				classLoader = ClassLoaderPool.getClassLoader(contextName);
			}
			finally {
				ClassLoaderUtil.setContextClassLoader(contextClassLoader);
			}

			if (classLoader == null) {
				String[] bundleInfo = _parseContextName(contextName);

				List<VersionedClassLoader> classLoadersInOrder =
					_fallbackClassLoaders.get(bundleInfo[0]);

				if (classLoadersInOrder != null) {
					VersionedClassLoader latestVersionClassLoader =
						classLoadersInOrder.get(0);

					classLoader = latestVersionClassLoader.getClassLoader();

					if (_log.isWarnEnabled()) {
						_log.warn(
							StringBundler.concat(
								"Unable to find ClassLoader for ", contextName,
								", ClassLoader ", bundleInfo[0],
								StringPool.UNDERLINE,
								latestVersionClassLoader.getVersion(),
								" is provided instead"));
					}
				}
			}
		}

		if (classLoader == null) {
			Thread currentThread = Thread.currentThread();

			classLoader = currentThread.getContextClassLoader();
		}

		return classLoader;
	}

	public static String getContextName(ClassLoader classLoader) {
		String contextName = ClassLoaderPool.getContextName(classLoader);

		if (contextName.equals("null")) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					StringBundler.concat(
						"Unable to find contextName for ", classLoader,
						" , send 'null' as contextName instead"));
			}
		}

		return contextName;
	}

	public static void register(
		String symbolicName, Version version, ClassLoader classLoader) {

		_registerFallback(symbolicName, version, classLoader);
	}

	public static void unregister(String symbolicName, Version version) {
		_unregisterFallback(symbolicName, version);
	}

	private static String[] _parseContextName(String contextName) {
		String[] bundleInfo = new String[2];

		int pos = contextName.indexOf(StringPool.UNDERLINE);

		if (pos < 0) {
			bundleInfo[0] = contextName;
			bundleInfo[1] = null;
		}
		else {
			bundleInfo[0] = contextName.substring(0, pos);
			bundleInfo[1] = contextName.substring(pos + 1);
		}

		return bundleInfo;
	}

	private static void _registerFallback(
		String symbolicName, Version version, ClassLoader classLoader) {

		List<VersionedClassLoader> versionedClassLoaders =
			_fallbackClassLoaders.get(symbolicName);

		if (versionedClassLoaders == null) {
			versionedClassLoaders = new CopyOnWriteArrayList<>();

			_fallbackClassLoaders.put(symbolicName, versionedClassLoaders);
		}

		versionedClassLoaders.add(
			new VersionedClassLoader(classLoader, version));

		if (versionedClassLoaders.size() > 1) {
			Collections.sort(versionedClassLoaders);
		}
	}

	private static void _unregisterFallback(
		String symbolicName, Version version) {

		List<VersionedClassLoader> classLoadersInOrder =
			_fallbackClassLoaders.get(symbolicName);

		if (classLoadersInOrder == null) {
			return;
		}

		for (VersionedClassLoader versionedClassLoader : classLoadersInOrder) {
			Version targetVersion = versionedClassLoader.getVersion();

			if (version.equals(targetVersion)) {
				classLoadersInOrder.remove(versionedClassLoader);

				if (classLoadersInOrder.isEmpty()) {
					_fallbackClassLoaders.remove(symbolicName);
				}

				break;
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ClusterClassLoaderPool.class);

	private static final Map<String, List<VersionedClassLoader>>
		_fallbackClassLoaders = new ConcurrentHashMap<>();

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
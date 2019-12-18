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

package com.liferay.portal.search.elasticsearch7.internal.sidecar;

import com.liferay.petra.process.ProcessCallable;
import com.liferay.petra.process.ProcessException;
import com.liferay.petra.process.local.LocalProcessLauncher;
import com.liferay.petra.reflect.ReflectionUtil;

import java.io.Serializable;

import java.lang.reflect.Method;

/**
 * @author Tina Tian
 */
public class SidecarProcessCallable implements ProcessCallable<Serializable> {

	public SidecarProcessCallable(String[] args) {
		_args = args;
	}

	@Override
	public Serializable call() throws ProcessException {
		LocalProcessLauncher.ProcessContext.attach(
			"Sidecar-SidecarProcessCallable", 10000,
			(shutdownCode, shutdownThrowable) -> {
				System.exit(shutdownCode);

				return true;
			});

		Thread currentThread = Thread.currentThread();

		ClassLoader classLoader = currentThread.getContextClassLoader();

		try {
			Class<?> elasticsearchClass = classLoader.loadClass(
				"org.elasticsearch.bootstrap.Elasticsearch");

			Method mainMethod = ReflectionUtil.getDeclaredMethod(
				elasticsearchClass, "main", String[].class);

			mainMethod.invoke(null, new Object[] {_args});
		}
		catch (Exception e) {
			throw new ProcessException(
				"Unable to start Elasticsearch server", e);
		}

		return null;
	}

	private static final long serialVersionUID = 1L;

	private final String[] _args;

}
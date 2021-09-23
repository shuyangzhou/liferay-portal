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

package com.liferay.portal.web.internal.session.replication;

import com.liferay.petra.io.Deserializer;
import com.liferay.petra.io.Serializer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.HttpSessionWrapper;
import com.liferay.portal.kernel.util.TransientValue;

import java.io.Serializable;

import java.nio.ByteBuffer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

/**
 * @author Dante Wang
 */
public class SessionReplicationHttpSessionWrapper extends HttpSessionWrapper {

	public SessionReplicationHttpSessionWrapper(HttpSession httpSession) {
		super(httpSession);
	}

	@Override
	public Object getAttribute(String name) {
		Object value = super.getAttribute(name);

		Set<String> scrubbedNames = _getScrubbedNames();

		if (!scrubbedNames.contains(name)) {
			return value;
		}

		Deserializer deserializer = new Deserializer(
			ByteBuffer.wrap((byte[])value));

		try {
			return deserializer.readObject();
		}
		catch (Exception exception) {
			_log.error("Unable to deserialize object", exception);

			return null;
		}
	}

	@Override
	public void removeAttribute(String name) {
		super.removeAttribute(name);

		Set<String> scrubbedNames = _getScrubbedNames();

		scrubbedNames.remove(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		if (value instanceof Serializable) {
			Class<?> clazz = value.getClass();

			if (!(value instanceof TransientValue) &&
				!_safeClassLoaders.contains(clazz.getClassLoader())) {

				Set<String> scrubbedNames = _getScrubbedNames();

				scrubbedNames.add(name);

				Serializer serializer = new Serializer();

				serializer.writeObject((Serializable)value);

				ByteBuffer byteBuffer = serializer.toByteBuffer();

				value = byteBuffer.array();
			}
		}

		super.setAttribute(name, value);
	}

	private Set<String> _getScrubbedNames() {
		Set<String> scrubbedNames = (Set<String>)super.getAttribute(
			_SCRUBBED_NAMES_NAME);

		if (scrubbedNames == null) {
			scrubbedNames = Collections.newSetFromMap(
				new ConcurrentHashMap<>());

			super.setAttribute(_SCRUBBED_NAMES_NAME, scrubbedNames);
		}

		return scrubbedNames;
	}

	private static final String _SCRUBBED_NAMES_NAME =
		SessionReplicationHttpSessionWrapper.class.getName() +
			"._SCRUBBED_NAMES_NAME";

	private static final Log _log = LogFactoryUtil.getLog(
		SessionReplicationHttpSessionWrapper.class);

	private static final Set<ClassLoader> _safeClassLoaders =
		new HashSet<ClassLoader>() {
			{
				add(String.class.getClassLoader());
				add(HttpSession.class.getClassLoader());
				add(Logger.class.getClassLoader());
			}
		};

}
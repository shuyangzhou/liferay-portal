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

import java.io.Serializable;

import java.nio.ByteBuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

/**
 * @author Dante Wang
 */
public class SessionReplicationHttpSessionWrapper extends HttpSessionWrapper {

	public SessionReplicationHttpSessionWrapper(HttpSession session) {
		super(session);
	}

	@Override
	public Object getAttribute(String name) {
		Object value = super.getAttribute(
			_SERIALIZED_ATTRIBUTE_PREFIX.concat(name));

		if (value == null) {
			return super.getAttribute(name);
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
	public Enumeration<String> getAttributeNames() {
		Enumeration<String> attributeNameEnumeration =
			super.getAttributeNames();

		List<String> attributeNames = new ArrayList<>();

		while (attributeNameEnumeration.hasMoreElements()) {
			String attributeName = attributeNameEnumeration.nextElement();

			if (attributeName.startsWith(_SERIALIZED_ATTRIBUTE_PREFIX)) {
				attributeName = attributeName.substring(
					_SERIALIZED_ATTRIBUTE_PREFIX.length());
			}

			attributeNames.add(attributeName);
		}

		return Collections.enumeration(attributeNames);
	}

	@Override
	public void removeAttribute(String name) {
		super.removeAttribute(name);

		super.removeAttribute(_SERIALIZED_ATTRIBUTE_PREFIX.concat(name));
	}

	@Override
	public void setAttribute(String name, Object value) {
		if (value instanceof Serializable) {
			Class<?> clazz = value.getClass();

			if (!_safeClassLoaders.contains(clazz.getClassLoader())) {
				Serializer serializer = new Serializer();

				serializer.writeObject((Serializable)value);

				ByteBuffer byteBuffer = serializer.toByteBuffer();

				super.setAttribute(
					_SERIALIZED_ATTRIBUTE_PREFIX.concat(name),
					byteBuffer.array());

				return;
			}
		}

		super.setAttribute(name, value);
	}

	private static final String _SERIALIZED_ATTRIBUTE_PREFIX =
		"SERIALIZED_ATTRIBUTE_PREFIX_";

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
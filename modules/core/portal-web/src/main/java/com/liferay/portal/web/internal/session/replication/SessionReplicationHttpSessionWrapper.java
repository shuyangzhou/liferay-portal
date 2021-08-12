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
		catch (ClassNotFoundException classNotFoundException) {
			_log.error("Unable to deserialize object", classNotFoundException);

			return null;
		}
	}

	@Override
	public void setAttribute(String name, Object value) {
		if (!(value instanceof Serializable) ||
			_isSafeClass(value.getClass())) {

			super.setAttribute(name, value);

			return;
		}

		Serializer serializer = new Serializer();

		serializer.writeObject((Serializable)value);

		ByteBuffer byteBuffer = serializer.toByteBuffer();

		super.setAttribute(
			_SERIALIZED_ATTRIBUTE_PREFIX.concat(name), byteBuffer.array());
	}

	private boolean _isSafeClass(Class<?> clazz) {
		ClassLoader classLoader = clazz.getClassLoader();

		if ((classLoader == String.class.getClassLoader()) ||
			(classLoader == HttpSession.class.getClassLoader())) {

			return true;
		}

		return false;
	}

	private static final String _SERIALIZED_ATTRIBUTE_PREFIX =
		"SERIALIZED_ATTRIBUTE_PREFIX_";

	private static final Log _log = LogFactoryUtil.getLog(
		SessionReplicationHttpSessionWrapper.class);

}
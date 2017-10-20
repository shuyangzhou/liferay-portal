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

package com.liferay.portal.util;

import com.liferay.portal.kernel.io.Deserializer;
import com.liferay.portal.kernel.io.Serializer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import java.nio.ByteBuffer;

/**
 * @author Tina Tian
 */
public class LazySerializableObjectWrapper implements Externalizable {

	public static <T> T unwrap(Object object) {
		if (!(object instanceof LazySerializableObjectWrapper)) {
			return (T)object;
		}

		LazySerializableObjectWrapper lazySerializableObjectWrapper =
			(LazySerializableObjectWrapper)object;

		if (lazySerializableObjectWrapper._serializable instanceof
				LazySerializable) {

			LazySerializable lazySerializable =
				(LazySerializable)lazySerializableObjectWrapper._serializable;

			Serializable serializable = lazySerializable.getSerializable();

			if (serializable == null) {
				return null;
			}

			lazySerializableObjectWrapper._serializable = serializable;
		}

		return (T)lazySerializableObjectWrapper._serializable;
	}

	/**
	 * The empty constructor is required by {@link Externalizable}. Do not use
	 * this for any other purpose.
	 */
	public LazySerializableObjectWrapper() {
	}

	public LazySerializableObjectWrapper(Serializable serializable) {
		_serializable = serializable;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		byte[] data = new byte[objectInput.readInt()];

		objectInput.readFully(data);

		_serializable = new LazySerializable(data);
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		byte[] data = _getData();

		objectOutput.writeInt(data.length);

		objectOutput.write(data, 0, data.length);
	}

	private byte[] _getData() {
		if (_serializable instanceof LazySerializable) {
			LazySerializable lazySerializable = (LazySerializable)_serializable;

			return lazySerializable.getData();
		}

		Serializer serializer = new Serializer();

		serializer.writeObject(_serializable);

		ByteBuffer byteBuffer = serializer.toByteBuffer();

		return byteBuffer.array();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LazySerializableObjectWrapper.class);

	private volatile Serializable _serializable;

	private static class LazySerializable implements Serializable {

		public byte[] getData() {
			return _data;
		}

		public Serializable getSerializable() {
			Deserializer deserializer = new Deserializer(
				ByteBuffer.wrap(_data));

			try {
				return deserializer.readObject();
			}
			catch (ClassNotFoundException cnfe) {
				_log.error("Unable to deserialize object", cnfe);

				return null;
			}
		}

		private LazySerializable(byte[] data) {
			_data = data;
		}

		private final byte[] _data;

	}

}
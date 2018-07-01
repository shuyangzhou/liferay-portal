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

import com.liferay.portal.kernel.io.SerializationConstants;
import com.liferay.portal.kernel.io.Serializer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * @author Lance Ji
 */
public class ClusterSerializer extends Serializer {

	@Override
	public void writeObject(Serializable serializable) {
		if ((serializable == null) || (serializable instanceof Long) ||
			(serializable instanceof String) ||
			(serializable instanceof Integer) ||
			(serializable instanceof Boolean) ||
			(serializable instanceof Short) ||
			(serializable instanceof Character) ||
			(serializable instanceof Byte) ||
			(serializable instanceof Double) ||
			(serializable instanceof Float)) {

			super.writeObject(serializable);

			return;
		}

		if (serializable instanceof Class) {
			Class<?> clazz = (Class<?>)serializable;

			writeByte(SerializationConstants.TC_CLASS);
			writeString(_getContextName(clazz));
			writeString(clazz.getName());

			return;
		}

		writeByte(SerializationConstants.TC_OBJECT);

		try {
			ObjectOutputStream objectOutputStream =
				new CLusterAnnotatedObjectOutputStream(
					new BufferOutputStream());

			objectOutputStream.writeObject(serializable);

			objectOutputStream.flush();
		}
		catch (IOException ioe) {
			throw new RuntimeException(
				"Unable to write ordinary serializable object " + serializable,
				ioe);
		}
	}

	private String _getContextName(Class<?> clazz) {
		ClassLoader classLoader = clazz.getClassLoader();

		return ClusterClassLoaderPool.getContextName(classLoader);
	}

	private class BufferOutputStream extends OutputStream {

		@Override
		public void write(byte[] bytes) {
			write(bytes, 0, bytes.length);
		}

		@Override
		public void write(byte[] bytes, int offset, int length) {
			byte[] buffer = getBuffer(length);

			System.arraycopy(bytes, offset, buffer, index, length);

			index += length;
		}

		@Override
		public void write(int b) {
			getBuffer(1)[index++] = (byte)b;
		}

	}

	private class CLusterAnnotatedObjectOutputStream
		extends ObjectOutputStream {

		public CLusterAnnotatedObjectOutputStream(OutputStream outputStream)
			throws IOException {

			super(outputStream);
		}

		@Override
		protected void annotateClass(Class<?> clazz) throws IOException {
			writeUTF(_getContextName(clazz));
		}

	}

}
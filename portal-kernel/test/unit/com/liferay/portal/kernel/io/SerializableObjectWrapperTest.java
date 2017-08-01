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

package com.liferay.portal.kernel.io;

import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.util.ClassLoaderPool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Tina Tian
 */
public class SerializableObjectWrapperTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testEquals() {
		Assert.assertFalse(
			_testSerializableObjectWrapper.equals(_TEST_SERIALIZABLE));

		Assert.assertTrue(
			_testSerializableObjectWrapper.equals(
				_testSerializableObjectWrapper));
		Assert.assertTrue(
			_testSerializableObjectWrapper.equals(
				new SerializableObjectWrapper(_TEST_SERIALIZABLE)));
		Assert.assertTrue(
			_testSerializableObjectWrapper.equals(
				_getDeserializedObject(_testSerializableObjectWrapper)));
		Assert.assertTrue(
			_getDeserializedObject(_testSerializableObjectWrapper).equals(
				_testSerializableObjectWrapper));
		Assert.assertTrue(
			_getDeserializedObject(_testSerializableObjectWrapper).equals(
				_getDeserializedObject(_testSerializableObjectWrapper)));
	}

	@Test
	public void testHashCode() {
		Assert.assertEquals(
			_testSerializableObjectWrapper.hashCode(),
			new SerializableObjectWrapper(_TEST_SERIALIZABLE).hashCode());
		Assert.assertEquals(
			_testSerializableObjectWrapper.hashCode(),
			_getDeserializedObject(_testSerializableObjectWrapper).hashCode());
	}

	@Test
	public void testUnwrap() {
		Assert.assertEquals(
			_TEST_SERIALIZABLE,
			SerializableObjectWrapper.unwrap(_testSerializableObjectWrapper));

		Assert.assertEquals(
			_TEST_SERIALIZABLE,
			SerializableObjectWrapper.unwrap(
				_getDeserializedObject(_testSerializableObjectWrapper)));

		Assert.assertEquals(
			_TEST_SERIALIZABLE,
			SerializableObjectWrapper.unwrap(_TEST_SERIALIZABLE));

		ClassLoaderPool.unregister(ClassLoaderPool.class.getClassLoader());

		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		currentThread.setContextClassLoader(
			new ClassLoader() {

				@Override
				public Class<?> loadClass(String name)
					throws ClassNotFoundException {

					if (name.equals(TestSerializable.class.getName())) {
						throw new ClassNotFoundException();
					}

					return super.loadClass(name);
				}

			});

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SerializableObjectWrapper.class.getName(), Level.ALL)) {

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertNull(
				SerializableObjectWrapper.unwrap(
					_getDeserializedObject(_testSerializableObjectWrapper)));

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Unable to deserialize object", logRecord.getMessage());
		}
		finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}
	}

	@Test
	public void testWriteExternal() {
		SerializableObjectWrapper deserializedObject = _getDeserializedObject(
			_testSerializableObjectWrapper);

		Assert.assertEquals(
			deserializedObject, _getDeserializedObject(deserializedObject));
	}

	private SerializableObjectWrapper _getDeserializedObject(
		SerializableObjectWrapper serializableObjectWrapper) {

		try {
			ByteArrayOutputStream byteArrayOutputStream =
				new ByteArrayOutputStream();

			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);

			objectOutputStream.writeObject(serializableObjectWrapper);

			objectOutputStream.flush();

			byte[] serializedData = byteArrayOutputStream.toByteArray();

			ByteArrayInputStream byteArrayInputStream =
				new ByteArrayInputStream(serializedData);

			ObjectInputStream objectInputStream = new ObjectInputStream(
				byteArrayInputStream);

			return (SerializableObjectWrapper)objectInputStream.readObject();
		}
		catch (Exception e) {
			Assert.fail(
				"Unable to deserialize object " + serializableObjectWrapper);
		}

		return null;
	}

	private static final TestSerializable _TEST_SERIALIZABLE =
		new TestSerializable("_TEST_SERIALIZABLE");

	private final SerializableObjectWrapper _testSerializableObjectWrapper =
		new SerializableObjectWrapper(_TEST_SERIALIZABLE);

	private static class TestSerializable implements Serializable {

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}

			if (!(object instanceof TestSerializable)) {
				return false;
			}

			TestSerializable testSerializable = (TestSerializable)object;

			return Objects.equals(_name, testSerializable._name);
		}

		@Override
		public int hashCode() {
			return _name.hashCode();
		}

		private TestSerializable(String name) {
			_name = name;
		}

		private final String _name;

	}

}
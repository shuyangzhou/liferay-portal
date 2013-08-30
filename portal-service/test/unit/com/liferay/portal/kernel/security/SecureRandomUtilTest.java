/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.security;

import com.liferay.portal.kernel.io.BigEndianCodec;
import com.liferay.portal.kernel.test.CodeCoverageAssertor;
import com.liferay.portal.kernel.test.NewClassLoaderJUnitTestRunner;
import com.liferay.portal.kernel.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Shuyang Zhou
 */
@RunWith(NewClassLoaderJUnitTestRunner.class)
public class SecureRandomUtilTest {

	@ClassRule
	public static CodeCoverageAssertor codeCoverageAssertor =
		new CodeCoverageAssertor();

	@Before
	public void setUp() {
		System.setProperty(_KEY_BUFFER_SIZE, "2048");
	}

	@After
	public void tearDown() {
		System.clearProperty(_KEY_BUFFER_SIZE);
	}

	@Test
	public void testConcurrentReload() throws Exception {
		SecureRandom secureRandom = installPredictableRandom();

		FutureTask<Long> futureTask = new FutureTask<Long>(
			new Callable<Long>() {

				@Override
				public Long call() throws Exception {
					return reload();
				}

			});

		Thread reloadThread = new Thread(futureTask);

		long gapValue = -1;
		long secretState = getSecretState();

		synchronized (secureRandom) {
			reloadThread.start();

			while (reloadThread.getState() != Thread.State.BLOCKED);

			gapValue = reload();

			Assert.assertEquals(
				getGapValueFromSecretSeed(secretState), gapValue);
		}

		reloadThread.join();

		secretState = getNextSecretState(secretState + 1);

		Assert.assertEquals(
			(Long)(getGapValueFromSecretSeed(secretState)), futureTask.get());
	}

	@Test
	public void testConcurrentReloadWeaknesses() throws Exception {
		SecureRandom secureRandom = installPredictableRandom();

		FutureTask<Long> futureTask = new FutureTask<Long>(
			new Callable<Long>() {

				@Override
				public Long call() throws Exception {
					return reload();
				}

			});

		Thread reloadThread = new Thread(futureTask);

		Set<Long> even = new HashSet<Long>(100);
		Set<Long> odd = new HashSet<Long>(100);

		synchronized (secureRandom) {
			reloadThread.start();

			while (reloadThread.getState() != Thread.State.BLOCKED);

			for (int i = 0; i < 50; i++) {
				odd.add(reload());
				even.add(reload());
			}

			Assert.assertEquals(odd.size(), 50);
			Assert.assertEquals(even.size(), 50);
		}

		reloadThread.join();

		for (int i = 0; i < 50; i++) {
			odd.add(reload());
			even.add(reload());
		}

		Assert.assertEquals(odd.size(), 100);
		Assert.assertEquals(even.size(), 100);
	}

	@Test
	public void testConcurrentReloadWithoutSHA() throws Exception {
		Security.insertProviderAt(new ExceptionThrowingProvider(), 1);

		try {
			SecureRandom secureRandom = installPredictableRandom();

			FutureTask<Long> futureTask = new FutureTask<Long>(
				new Callable<Long>() {

					@Override
					public Long call() throws Exception {
						return reload();
					}

				});

			Thread reloadThread = new Thread(futureTask);

			synchronized (secureRandom) {
				reloadThread.start();

				while (reloadThread.getState() != Thread.State.BLOCKED);

				try {
					reload();
					Assert.fail();
				}
				catch (InvocationTargetException e) {
					Assert.assertEquals(
						"SHA1 is not available!", e.getCause().getMessage());
				}
			}

			reloadThread.join();

			try {
				reload();
				Assert.fail();
			}
			catch (InvocationTargetException e) {
				Assert.assertEquals(
					"SHA1 is not available!", e.getCause().getMessage());
			}
		}
		finally {
			Security.removeProvider(ExceptionThrowingProvider._NAME);
		}
	}

	@Test
	public void testInitialization() throws Exception {
		System.setProperty(_KEY_BUFFER_SIZE, "10");

		Field bufferSizeField = ReflectionUtil.getDeclaredField(
			SecureRandomUtil.class, "_BUFFER_SIZE");

		Assert.assertEquals(1024, bufferSizeField.get(null));

		Field bytesField = ReflectionUtil.getDeclaredField(
			SecureRandomUtil.class, "_bytes");

		byte[] bytes = (byte[])bytesField.get(null);

		Assert.assertEquals(1024, bytes.length);
	}

	@Test
	public void testNextBoolean() throws Exception {

		// First load

		installPredictableRandom();

		for (int i = 0; i < 2048; i++) {
			byte b = (byte)i;

			if (b < 0) {
				Assert.assertFalse(SecureRandomUtil.nextBoolean());
			}
			else {
				Assert.assertTrue(SecureRandomUtil.nextBoolean());
			}
		}

		// Gap number

		long secretState = getSecretState();

		boolean nextBoolean = SecureRandomUtil.nextBoolean();

		secretState = getNextSecretState(secretState);

		long result = getGapValueFromSecretSeed(secretState);

		if ((byte)result < 0) {
			Assert.assertFalse(nextBoolean);
		}
		else {
			Assert.assertTrue(nextBoolean);
		}

		// Second load

		for (int i = 0; i < 2048; i++) {
			byte b = (byte)i;

			if (b < 0) {
				Assert.assertFalse(SecureRandomUtil.nextBoolean());
			}
			else {
				Assert.assertTrue(SecureRandomUtil.nextBoolean());
			}
		}
	}

	@Test
	public void testNextByte() throws Exception {

		// First load

		installPredictableRandom();

		for (int i = 0; i < 2048; i++) {
			Assert.assertEquals((byte)i, SecureRandomUtil.nextByte());
		}

		// Gap number

		long secretState = getSecretState();

		byte nextByte = SecureRandomUtil.nextByte();

		secretState = getNextSecretState(secretState);

		long result = getGapValueFromSecretSeed(secretState);

		Assert.assertEquals((byte)result, nextByte);

		// Second load

		for (int i = 0; i < 2048; i++) {
			Assert.assertEquals((byte)i, SecureRandomUtil.nextByte());
		}
	}

	@Test
	public void testNextDouble() throws Exception {

		// First load

		installPredictableRandom();

		for (int i = 0; i < 256; i++) {
			byte b = (byte)(i * 8);

			byte[] bytes = new byte[8];

			for (int j = 0; j < 8; j++) {
				bytes[j] = (byte)(b + j);
			}

			Assert.assertEquals(
				BigEndianCodec.getDouble(bytes, 0),
				SecureRandomUtil.nextDouble(), 0);
		}

		// Gap number

		long secretState = getSecretState();

		double nextDouble = SecureRandomUtil.nextDouble();

		secretState = getNextSecretState(secretState);

		long result = getGapValueFromSecretSeed(secretState);

		Assert.assertEquals(Double.longBitsToDouble(result), nextDouble, 0);

		// Second load

		for (int i = 0; i < 256; i++) {
			byte b = (byte)(i * 8);

			byte[] bytes = new byte[8];

			for (int j = 0; j < 8; j++) {
				bytes[j] = (byte)(b + j);
			}

			Assert.assertEquals(
				BigEndianCodec.getDouble(bytes, 0),
				SecureRandomUtil.nextDouble(), 0);
		}
	}

	@Test
	public void testNextFloat() throws Exception {

		// First load

		installPredictableRandom();

		for (int i = 0; i < 512; i++) {
			byte b = (byte)(i * 4);

			byte[] bytes = new byte[4];

			for (int j = 0; j < 4; j++) {
				bytes[j] = (byte)(b + j);
			}

			Assert.assertEquals(
				BigEndianCodec.getFloat(bytes, 0), SecureRandomUtil.nextFloat(),
				0);
		}

		// Gap number

		long secretState = getSecretState();

		float nextFloat = SecureRandomUtil.nextFloat();

		secretState = getNextSecretState(secretState);

		long result = getGapValueFromSecretSeed(secretState);

		Assert.assertEquals(Float.intBitsToFloat((int)result), nextFloat, 0);

		// Second load

		for (int i = 0; i < 512; i++) {
			byte b = (byte)(i * 4);

			byte[] bytes = new byte[4];

			for (int j = 0; j < 4; j++) {
				bytes[j] = (byte)(b + j);
			}

			Assert.assertEquals(
				BigEndianCodec.getFloat(bytes, 0), SecureRandomUtil.nextFloat(),
				0);
		}
	}

	@Test
	public void testNextInt() throws Exception {

		// First load

		installPredictableRandom();

		for (int i = 0; i < 512; i++) {
			byte b = (byte)(i * 4);

			byte[] bytes = new byte[4];

			for (int j = 0; j < 4; j++) {
				bytes[j] = (byte)(b + j);
			}

			Assert.assertEquals(
				BigEndianCodec.getInt(bytes, 0), SecureRandomUtil.nextInt(), 0);
		}

		// Gap number

		long secretState = getSecretState();

		int nextInt = SecureRandomUtil.nextInt();

		secretState = getNextSecretState(secretState);

		long result = getGapValueFromSecretSeed(secretState);

		Assert.assertEquals((int)result, nextInt, 0);

		// Second load

		for (int i = 0; i < 512; i++) {
			byte b = (byte)(i * 4);

			byte[] bytes = new byte[4];

			for (int j = 0; j < 4; j++) {
				bytes[j] = (byte)(b + j);
			}

			Assert.assertEquals(
				BigEndianCodec.getInt(bytes, 0), SecureRandomUtil.nextInt(), 0);
		}
	}

	@Test
	public void testNextLong() throws Exception {

		// First load

		installPredictableRandom();

		for (int i = 0; i < 256; i++) {
			byte b = (byte)(i * 8);

			byte[] bytes = new byte[8];

			for (int j = 0; j < 8; j++) {
				bytes[j] = (byte)(b + j);
			}

			Assert.assertEquals(
				BigEndianCodec.getLong(bytes, 0), SecureRandomUtil.nextLong(),
				0);
		}

		// Gap number

		long secretState = getSecretState();

		long nextLong = SecureRandomUtil.nextLong();

		secretState = getNextSecretState(secretState);

		long result = getGapValueFromSecretSeed(secretState);

		Assert.assertEquals(result, nextLong, 0);

		// Second load

		for (int i = 0; i < 256; i++) {
			byte b = (byte)(i * 8);

			byte[] bytes = new byte[8];

			for (int j = 0; j < 8; j++) {
				bytes[j] = (byte)(b + j);
			}

			Assert.assertEquals(
				BigEndianCodec.getLong(bytes, 0), SecureRandomUtil.nextLong(),
				0);
		}
	}

	protected long getGapValueFromSecretSeed(long secretState)
		throws Exception {

		try {
			byte[] secretStateBytes = new byte[8];
			BigEndianCodec.putLong(secretStateBytes, 0, secretState);

			MessageDigest mDigest = MessageDigest.getInstance("SHA1");
			byte[] result = mDigest.digest(secretStateBytes);

			int pos = ((int)secretState & 0xF) % 11;
			return BigEndianCodec.getLong(result, pos & 0xF);
		}
		catch (NoSuchAlgorithmException e) {
			return new Random(secretState).nextLong();
		}
	}

	protected int getIntAtPos(int pos) throws Exception {
		Field bytesField = ReflectionUtil.getDeclaredField(
			SecureRandomUtil.class, "_bytes");

		byte[] bytes = (byte[])bytesField.get(null);

		return BigEndianCodec.getInt(bytes, pos);
	}

	protected long getLongAtPos(int pos) throws Exception {
		Field bytesField = ReflectionUtil.getDeclaredField(
			SecureRandomUtil.class, "_bytes");

		byte[] bytes = (byte[])bytesField.get(null);

		return BigEndianCodec.getLong(bytes, pos);
	}

	protected long getNextSecretState(long secretState) throws Exception {
		int pos = getIntAtPos(2048 - 4) & 0x7FFFFFFF;
		pos = pos % (2048 - 12);

		long l = getLongAtPos(pos);

		return secretState ^ l;
	}

	protected long getSecretState() throws Exception {
		Field secretStateField = ReflectionUtil.getDeclaredField(
			SecureRandomUtil.class, "_secretState");

		AtomicLong secretState = (AtomicLong)secretStateField.get(null);
		return secretState.get();
	}

	protected SecureRandom installPredictableRandom() throws Exception {
		Field secureRandomField = ReflectionUtil.getDeclaredField(
			SecureRandomUtil.class, "_random");

		SecureRandom predictableRandom = new PredictableRandom();

		secureRandomField.set(null, predictableRandom);

		Field bytesField = ReflectionUtil.getDeclaredField(
			SecureRandomUtil.class, "_bytes");

		byte[] bytes = (byte[])bytesField.get(null);

		predictableRandom.nextBytes(bytes);

		return predictableRandom;
	}

	protected long reload() throws Exception {
		Method reloadMethod = ReflectionUtil.getDeclaredMethod(
			SecureRandomUtil.class, "_reload");

		return (Long)reloadMethod.invoke(null);
	}

	private static final String _KEY_BUFFER_SIZE =
		SecureRandomUtil.class.getName() + ".buffer.size";

	private static class ExceptionThrowingProvider extends Provider {

		public ExceptionThrowingProvider() {
			super(_NAME, 1.0, "Provider for SHA1");
		}

		public synchronized Service getService(String type, String algorithm) {
			if (algorithm.equals("SHA1") && type.equals("MessageDigest")) {
				throw new RuntimeException();
			}

			return null;
		}

		private static final String _NAME = "Liferay";

	}

	private static class PredictableRandom extends SecureRandom {

		@Override
		public synchronized void nextBytes(byte[] bytes) {
			for (int i = 0; i < bytes.length; i++) {
				bytes[i] = (byte)_counter.getAndIncrement();
			}
		}

		private AtomicInteger _counter = new AtomicInteger();

	}

}
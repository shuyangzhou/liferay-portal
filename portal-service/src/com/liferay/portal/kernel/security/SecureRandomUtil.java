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
import com.liferay.portal.kernel.util.GetterUtil;

import java.security.MessageDigest;
import java.security.SecureRandom;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Shuyang Zhou
 * @author Tomas Polesovsky
 */
public class SecureRandomUtil {

	public static boolean nextBoolean() {
		byte b = nextByte();

		if (b < 0) {
			return false;
		}
		else {
			return true;
		}
	}

	public static byte nextByte() {
		int index = _index.getAndIncrement();

		if (index < _BUFFER_SIZE) {
			return _bytes[index];
		}

		return (byte)_reload();
	}

	public static double nextDouble() {
		int index = _index.getAndAdd(8);

		if ((index + 7) < _BUFFER_SIZE) {
			return BigEndianCodec.getDouble(_bytes, index);
		}

		return Double.longBitsToDouble(_reload());
	}

	public static float nextFloat() {
		int index = _index.getAndAdd(4);

		if ((index + 3) < _BUFFER_SIZE) {
			return BigEndianCodec.getFloat(_bytes, index);
		}

		return Float.intBitsToFloat((int)_reload());
	}

	public static int nextInt() {
		int index = _index.getAndAdd(4);

		if ((index + 3) < _BUFFER_SIZE) {
			return BigEndianCodec.getInt(_bytes, index);
		}

		return (int)_reload();
	}

	public static long nextLong() {
		int index = _index.getAndAdd(8);

		if ((index + 7) < _BUFFER_SIZE) {
			return BigEndianCodec.getLong(_bytes, index);
		}

		return _reload();
	}

	private static long _reload() {
		if (_reloadingFlag.compareAndSet(false, true)) {
			_random.nextBytes(_bytes);

			_index.set(0);

			int pos = BigEndianCodec.getInt(
				_bytes, _BUFFER_SIZE - 4) & 0x7FFFFFFF;
			pos = pos % (_BUFFER_SIZE - 12);

			long l = BigEndianCodec.getLong(_bytes, pos);

			_secretState.set(_secretState.get() ^ l);

			_reloadingFlag.set(false);
		}

		long secretState = _secretState.getAndIncrement();
		try {
			byte[] secretStateBytes = new byte[8];
			BigEndianCodec.putLong(secretStateBytes, 0, secretState);

			MessageDigest mDigest = MessageDigest.getInstance("SHA1");
			byte[] result = mDigest.digest(secretStateBytes);

			int pos = ((int)secretState & 0xF) % (_SHA1_SIZE - 9);
			return BigEndianCodec.getLong(result, pos);
		}
		catch (Exception e) {
			throw new RuntimeException("SHA1 is not available!", e);
		}
	}

	private static final int _BUFFER_SIZE;

	private static final int _MIN_BUFFER_SIZE = 1024;

	private static final int _SHA1_SIZE = 20;

	private static byte[] _bytes;
	private static AtomicInteger _index = new AtomicInteger();
	private static Random _random = new SecureRandom();
	private static AtomicBoolean _reloadingFlag = new AtomicBoolean();
	private static AtomicLong _secretState = new AtomicLong();

	static {
		int bufferSize = GetterUtil.getInteger(
			System.getProperty(
				SecureRandomUtil.class.getName() + ".buffer.size"));

		if (bufferSize < _MIN_BUFFER_SIZE) {
			bufferSize = _MIN_BUFFER_SIZE;
		}

		_BUFFER_SIZE = bufferSize;

		_bytes = new byte[_BUFFER_SIZE];

		_random.nextBytes(_bytes);

		_secretState.set(_random.nextLong());
	}

}
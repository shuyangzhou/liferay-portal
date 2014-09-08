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

package com.liferay.portal.kernel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

import java.nio.channels.FileChannel;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class StreamUtilTest {

	@Test
	public void testTransferFileChannel() throws Exception {
		File fromFile = new File("from-file");

		fromFile.deleteOnExit();

		byte[] fromBytes;

		try (FileOutputStream fromFileOutputStream = new FileOutputStream(
				fromFile)) {

			Random random = new Random();

			fromBytes = new byte[1024 * 1024];

			random.nextBytes(fromBytes);

			fromFileOutputStream.write(fromBytes);
		}

		File toFile = new File("to-file");

		toFile.deleteOnExit();

		FileInputStream fromFileInputStream = new FileInputStream(fromFile);

		byte[] buffer = new byte[fromBytes.length / 2];

		int length = 0;

		while (
			(length += fromFileInputStream.read(
				buffer, length, buffer.length - length)) < buffer.length);

		try (FileOutputStream toFileOutputStream = new FileOutputStream(
				toFile)) {

			toFileOutputStream.write(buffer);

			try (FileChannel fromFileChannel = fromFileInputStream.getChannel())
					{

				StreamUtil.transferFileChannel(
					fromFileChannel, toFileOutputStream.getChannel(),
					fromBytes.length - buffer.length);
			}
		}

		byte[] toBytes;

		try (RandomAccessFile toRandomAccessFile = new RandomAccessFile(
				toFile, "r")) {

			Assert.assertEquals(fromBytes.length, toRandomAccessFile.length());

			toBytes = new byte[fromBytes.length];

			toRandomAccessFile.readFully(toBytes);
		}

		Assert.assertArrayEquals(fromBytes, toBytes);
	}

}
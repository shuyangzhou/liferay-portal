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

package com.liferay.petra.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class CharsetDecoderUtilTest {

	@Test
	public void testCharsetDecoderUtilConstructor() {
		new CharsetDecoderUtil();
	}

	@Test
	public void testCharsetEncoderUtilConstructor() {
		new CharsetEncoderUtil();
	}

	@Test
	public void testEncodeDecode() {
		String s = "test";

		ByteBuffer byteBuffer = CharsetEncoderUtil.encode(
			"UTF-8", CharBuffer.wrap(s));

		CharBuffer charBuffer = CharsetDecoderUtil.decode("UTF-8", byteBuffer);

		Assert.assertEquals(s, charBuffer.toString());
	}

}
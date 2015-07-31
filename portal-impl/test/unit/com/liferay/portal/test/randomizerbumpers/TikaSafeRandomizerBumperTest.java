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

package com.liferay.portal.test.randomizerbumpers;

import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ContentTypes;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Matthew Tambara
 */
public class TikaSafeRandomizerBumperTest {

	@Test
	public void testAccept() {
		TikaSafeRandomizerBumper tikaSafeRandomizerBumper =
			new TikaSafeRandomizerBumper(ContentTypes.TEXT);

		String randomString = RandomTestUtil.randomString();

		Assert.assertTrue(
			tikaSafeRandomizerBumper.accept(randomString.getBytes()));

		Assert.assertFalse(tikaSafeRandomizerBumper.accept(EXE_BYTE_ARRAY));
	}

	private static final byte[] EXE_BYTE_ARRAY =
		new byte[] {77, 90, -112, 0, 3, 0, 0, 0};

}
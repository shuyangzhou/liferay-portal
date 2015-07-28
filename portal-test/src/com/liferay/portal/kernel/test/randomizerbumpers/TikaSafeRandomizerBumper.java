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

package com.liferay.portal.kernel.test.randomizerbumpers;

import com.liferay.portal.kernel.metadata.RawMetadataProcessorUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.io.File;

/**
 * @author Matthew Tambara
 */
public class TikaSafeRandomizerBumper implements RandomizerBumper<String> {

	public static final TikaSafeRandomizerBumper INSTANCE =
		new TikaSafeRandomizerBumper();

	@Override
	public boolean accept(String randomValue) {
		return accept(randomValue, "txt");
	}

	public boolean accept(String randomValue, String extension) {
		File file = null;

		try {
			file = FileUtil.createTempFile(randomValue.getBytes());

			RawMetadataProcessorUtil.getRawMetadataMap(
				extension, StringPool.BLANK, file);

			return true;
		}
		catch (Exception e) {
			return false;
		}
		finally {
			FileUtil.delete(file);
		}
	}

}
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

package com.liferay.portal.cache.key;

import com.liferay.portal.kernel.cache.key.CacheKeyGenerator;
import com.liferay.portal.kernel.util.Digester;

import java.security.NoSuchAlgorithmException;

/**
 * @author Vilmos Papp
 */
public class JavaSHA1CacheKeyGenerator
	extends BaseMessageDigestCacheKeyGenerator {

	public JavaSHA1CacheKeyGenerator() throws NoSuchAlgorithmException {
		this(-1);
	}

	public JavaSHA1CacheKeyGenerator(int maxLength)
		throws NoSuchAlgorithmException {

		super(Digester.SHA_1, maxLength);
	}

	@Override
	public CacheKeyGenerator clone() {
		try {
			return new JavaSHA1CacheKeyGenerator(getMaxLength());
		}
		catch (NoSuchAlgorithmException nsae) {
			throw new IllegalStateException(nsae.getMessage(), nsae);
		}
	}

}
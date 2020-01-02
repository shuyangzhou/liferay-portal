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

package com.liferay.portal.security.crypto.generator.spi.hashing;

import com.liferay.portal.security.crypto.generator.hashing.HashGenerator;

/**
 * @author Arthur Chan
 */
public abstract class AbstractHashGenerator implements HashGenerator {

	@Override
	public HashGenerator withPepper(byte[] pepper) {
		if (pepper == null) {
			throw new IllegalArgumentException("pepper can not be null");
		}

		this.pepper = pepper;

		return this;
	}

	@Override
	public HashGenerator withSalt(byte[] salt) {
		if (salt == null) {
			throw new IllegalArgumentException("salt can not be null");
		}

		this.salt = salt;

		return this;
	}

	protected byte[] pepper = new byte[0];
	protected byte[] salt = new byte[0];

}
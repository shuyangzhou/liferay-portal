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

package com.liferay.portal.security.crypto.hashing.algorithm.bcrypt;

import com.liferay.portal.security.crypto.generator.spi.hashing.AbstractHashGenerator;
import com.liferay.portal.security.crypto.hashing.algorithm.bcrypt.salt.BCryptSaltGenerator;
import com.liferay.portal.security.crypto.generator.hashing.HashGenerator;
import com.liferay.portal.security.crypto.generator.hashing.salt.SaltGenerator;

import jodd.util.BCrypt;

import org.osgi.service.component.annotations.Component;

/**
 * @author Arthur Chan
 */
@Component(
	immediate = true, property = "crypto.hashing.algorithm=BCrypt",
	service = HashGenerator.class
)
public class BCryptHashGenerator extends AbstractHashGenerator {

	public BCryptHashGenerator() {
		_saltGenerator = new BCryptSaltGenerator();
	}

	public BCryptHashGenerator(int rounds) {
		_saltGenerator = new BCryptSaltGenerator(rounds);
	}

	private SaltGenerator _saltGenerator;

	@Override
	public SaltGenerator getSaltGenerator() {
		return _saltGenerator;
	}

	@Override
	public byte[] hash(byte[] toBeHashed) throws Exception {
		byte[] combined = new byte[toBeHashed.length + pepper.length];

		for (int i = 0; i < toBeHashed.length; ++i) {
			combined[i] = toBeHashed[i];
		}

		if (pepper.length > 0) {
			for (int i = toBeHashed.length; i < combined.length; ++i) {
				combined[i] = pepper[i];
			}
		}

		String hash = BCrypt.hashpw(new String(combined), new String(salt));

		return hash.getBytes();
	}

	@Override
	public String[] getGeneratorNames() {
		return new String[] {"BCrypt"};
	}

}
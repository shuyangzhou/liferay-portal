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

package com.liferay.portal.security.crypto.hashing.algorithm.message.digest;

import com.liferay.portal.security.crypto.generator.hashing.HashGenerator;
import com.liferay.portal.security.crypto.generator.hashing.salt.SaltGenerator;
import com.liferay.portal.security.crypto.generator.hashing.salt.VariableSizeSaltGenerator;
import com.liferay.portal.security.crypto.generator.spi.hashing.AbstractHashGenerator;

import java.security.MessageDigest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Arthur Chan
 */
@Component(
	immediate = true,
	property = {
		"crypto.hashing.algorithm=MD2", "crypto.hashing.algorithm=MD5",
		"crypto.hashing.algorithm=SHA-1", "crypto.hashing.algorithm=SHA-224",
		"crypto.hashing.algorithm=SHA-256", "crypto.hashing.algorithm=SHA-384",
		"crypto.hashing.algorithm=SHA-512"
	},
	service = HashGenerator.class
)
public class MessageDigestHashGenerator extends AbstractHashGenerator {

	public MessageDigestHashGenerator(String generatorName) {
		_chosenGenerator = generatorName;
	}

	@Override
	public String[] getGeneratorNames() {
		return new String[] {
			"MD2", "MD5", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512"
		};
	}

	@Override
	public SaltGenerator getSaltGenerator() {
		return VariableSizeSaltGenerator.DEFAULT;
	}

	@Override
	public byte[] hash(byte[] toBeHashed) throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance(
			_chosenGenerator);

		byte[] combined =
			new byte[toBeHashed.length + pepper.length + salt.length];

		System.arraycopy(toBeHashed, 0, combined, 0, toBeHashed.length);
		System.arraycopy(pepper, 0, combined, toBeHashed.length, pepper.length);
		System.arraycopy(
			salt, 0, combined, toBeHashed.length + pepper.length, salt.length);

		return messageDigest.digest(combined);
	}

	private final String _chosenGenerator;

}
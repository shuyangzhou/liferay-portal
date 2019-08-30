package com.liferay.portal.security.crypto.hashing.algorithm.bcrypt.salt;

import com.liferay.portal.security.crypto.generator.hashing.salt.FixedSizeSaltGenerator;

import jodd.util.BCrypt;

public class BCryptSaltGenerator implements FixedSizeSaltGenerator {

	public BCryptSaltGenerator() {
	}

	public BCryptSaltGenerator(int rounds) {
		_rounds = rounds;
	}

	@Override
	public byte[] generateSalt() {
		String salt = BCrypt.gensalt(_rounds);

		return salt.getBytes();
	}

	// The log value of log2(iterations), not salt size
	// e.g. rounds = 12 meaning:
	// There will be 2^12 = 4096 number of iterations to be able to generate the salt for BCrypt 
	private int _rounds = 12;
}

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

package com.liferay.portal.kernel.security.pwd;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ServiceProxyFactory;

/**
 * @author arthurchan35
 */
public class PasswordHelperUtil {

	/**
	 * Hash the password and store the hash for user
	 *
	 * @param userId, the userId
	 * @param password, the given password to be hashed and stored
	 */
	public static void addPassword(long userId, String password)
		throws PortalException {

		_passwordHelper.addPassword(userId, password);
	}

	/**
	 * Compare if this given password is the same as user's by using a timing-attack-proof comparison algorithm
	 * See http://bryanavery.co.uk/cryptography-net-avoiding-timing-attack/
	 *
	 * @param userId, the userId
	 * @param password, the given password to be compared with
	 * @param encrypted, indicating if the given password is encrypted or plain text
	 * @return a boolean value indicating equal or not.
	 */
	public static boolean compare(
		long userId, String password, boolean encrypted) {

		return _passwordHelper.compare(userId, password, encrypted);
	}

	/**
	 * Compare if this given password is the same as one of user's previous passwords by using a timing-attack-proof comparison algorithm
	 * See http://bryanavery.co.uk/cryptography-net-avoiding-timing-attack/
	 *
	 * @param userId, the userId
	 * @param password, the given password to be compared with
	 * @param encrypted, indicating if the given password is encrypted or plain text
	 * @return a boolean value indicating a match or not
	 */
	public static boolean compareHistory(
		long userId, String password, boolean encrypted) {

		return _passwordHelper.compareHistory(userId, password, encrypted);
	}

	/**
	 * delete all password information for user
	 *
	 * @param userId, the userId
	 */
	public static void deletePassword(long userId) throws PortalException {
		_passwordHelper.deletePassword(userId);
	}

	/**
	 * Get hash from user of UserId, do not use the result to compare, use {@link #compare(long, String, boolean)}
	 *
	 * @param userId, the userId
	 * @return a String of hash
	 */
	public static String getPassword(long userId) throws PortalException {
		return _passwordHelper.getPassword(userId);
	}

	/**
	 * Hash the given password using algorithms of User of given userId. Used primarily for JAAS, do not use the result to compare, use {@link #compare(long, String, boolean)}.
	 *
	 * @param userId, the userId
	 * @param password, the given password to be hashed
	 * @return a String of hash of the given password
	 */
	public static String hash(long userId, String password)
		throws PortalException {

		return _passwordHelper.hash(userId, password);
	}

	/**
	 * Update the user password
	 *
	 * @param userId, the userId
	 * @param password, the new password to be hashed and stored
	 * @param validate, if the password requires password validation
	 */
	public static void updatePassword(
			long userId, String password, boolean validate)
		throws PortalException {

		_passwordHelper.updatePassword(userId, password, validate);
	}

	private static volatile PasswordHelper _passwordHelper =
		ServiceProxyFactory.newServiceTrackedInstance(
			PasswordHelper.class, PasswordHelperUtil.class, "_passwordHelper",
			false);

}
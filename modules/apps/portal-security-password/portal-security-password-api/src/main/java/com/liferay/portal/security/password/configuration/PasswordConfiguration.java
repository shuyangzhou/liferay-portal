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

package com.liferay.portal.security.password.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author arthurchan35
 */
@ExtendedObjectClassDefinition(category = "password")
@Meta.OCD(
	id = "com.liferay.portal.security.password.configuration.PasswordConfiguration",
	localization = "content/Language", name = "password-configuration-name"
)
public interface PasswordConfiguration {

	/**
	 * Name of hash algorithm that will be used to hash passwords
	 */
	@Meta.AD(deflt = "PBKDF2", name = "hash-algorithm-name")
	public String hashAlgorithmName();

	@Meta.AD(
		deflt = "prfName:HmacSHA1|keySize:160|rounds:128000",
		name = "hash-algorithm-meta"
	)
	public String[] hashAlgorithmMeta();

}
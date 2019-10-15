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

package com.liferay.portal.security.password.internal.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PasswordPolicy;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.pwd.PasswordHelper;
import com.liferay.portal.kernel.service.PasswordPolicyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.crypto.generator.hashing.HashGenerator;
import com.liferay.portal.security.crypto.generator.registry.HashGeneratorFactoryRegistry;
import com.liferay.portal.security.password.model.HashAlgorithmEntry;
import com.liferay.portal.security.password.model.PasswordEntry;
import com.liferay.portal.security.password.model.PasswordMeta;
import com.liferay.portal.security.password.service.HashAlgorithmEntryLocalService;
import com.liferay.portal.security.password.service.PasswordEntryLocalService;
import com.liferay.portal.security.password.service.PasswordMetaLocalService;
import com.liferay.portal.security.pwd.PwdToolkitUtil;
import com.liferay.portal.util.PropsValues;

import java.util.List;

import org.json.JSONObject;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Arthur Chan
 */
@Component(immediate = true, service = PasswordHelper.class)
public class PasswordHelperImpl implements PasswordHelper {

	@Override
	public void addPassword(long userId, String newPassword)
		throws PortalException {

		_passwordEntryLocalService.addEntry(userId, newPassword);
	}

	@Override
	public boolean compare(long userId, String password, boolean encrypted) {
		PasswordEntry passwordEntry = null;

		try {
			passwordEntry = _passwordEntryLocalService.getEntryByUserId(userId);
		}
		catch (Exception e) {
			try {
				User user = _userLocalService.getUser(userId);

				String screenName = user.getScreenName();

				// If cannot find an entry and the user is default admin user,
				// we need to apply this workaround before user service moves
				// out to module.

				if (screenName.equals(PropsValues.DEFAULT_ADMIN_SCREEN_NAME) ||
					user.isDefaultUser()) {

					passwordEntry = _passwordEntryLocalService.addEntry(
						userId, password);
				}
				else {
					return false;
				}
			}
			catch (Exception e2) {
				return false;
			}
		}

		try {
			if (!encrypted) {
				password = _hash(passwordEntry, password);
			}

			return _timingAttackProofCompare(passwordEntry.getHash(), password);
		}
		catch (Exception e) {
			return false;
		}
	}

	public boolean compareHistory(
		long userId, String password, boolean encrypted) {

		List<PasswordEntry> passwordEntries = null;

		int historyCount = 0;

		try {
			PasswordPolicy passwordPolicy =
				_passwordPolicyLocalService.getPasswordPolicyByUserId(userId);

			if ((passwordPolicy == null) || !passwordPolicy.isHistory()) {
				return false;
			}

			passwordEntries =
				_passwordEntryLocalService.getHistoryEntriesByUserId(userId);

			historyCount = passwordPolicy.getHistoryCount();
		}
		catch (Exception e) {
			return false;
		}

		String hash = password;

		for (int i = passwordEntries.size() - 1; i > -1; --i) {
			if (historyCount-- < 1) {
				break;
			}

			PasswordEntry passwordEntry = passwordEntries.get(i);

			if (!encrypted) {
				try {
					hash = _hash(passwordEntry, password);
				}
				catch (Exception e) {
					continue;
				}
			}

			if (_timingAttackProofCompare(passwordEntry.getHash(), hash)) {
				return true;
			}
		}

		return false;
	}

	public void deletePassword(long userId) throws PortalException {
		_passwordEntryLocalService.deleteEntriesByUserId(userId);
	}

	public String getPassword(long userId) throws PortalException {
		PasswordEntry entry = _passwordEntryLocalService.getEntryByUserId(
			userId);

		return entry.getHash();
	}

	public String hash(long userId, String password) throws PortalException {
		PasswordEntry passwordEntry =
			_passwordEntryLocalService.getEntryByUserId(userId);

		return _hash(passwordEntry, password);
	}

	public void updatePassword(long userId, String password, boolean validate)
		throws PortalException {

		if (validate) {
			PasswordPolicy passwordPolicy =
				_passwordPolicyLocalService.getPasswordPolicyByUserId(userId);

			User user = _userLocalService.getUser(userId);

			PwdToolkitUtil.validate(
				user.getCompanyId(), userId, password, password,
				passwordPolicy);
		}

		_passwordEntryLocalService.updateEntriesByUserId(userId, password);
	}

	private String _hash(PasswordEntry passwordEntry, String password)
		throws PortalException {

		try {
			List<PasswordMeta> passwordMetas =
				_passwordMetaLocalService.getMetasByEntry(passwordEntry);

			for (PasswordMeta passwordMeta : passwordMetas) {
				HashAlgorithmEntry algorithm =
					_hashAlgorithmEntryLocalService.getHashAlgorithmEntry(
						passwordMeta.getHashAlgorithmEntryId());

				JSONObject generatorMeta = new JSONObject(algorithm.getMeta());

				HashGenerator hashGenerator =
					_hashGeneratorFactoryRegistry.createHashGenerator(
						algorithm.getName(), generatorMeta);

				if (Validator.isNotNull(passwordMeta.getSalt())) {
					String salt = passwordMeta.getSalt();

					hashGenerator.withSalt(salt.getBytes());
				}

				password = Base64.encode(
					hashGenerator.hash(password.getBytes()));
			}

			return password;
		}
		catch (Exception e) {
			throw new PortalException(e.getMessage());
		}
	}

	private boolean _timingAttackProofCompare(String string1, String string2) {
		byte[] bytes1 = string1.getBytes();

		byte[] bytes2 = string2.getBytes();

		int diff = bytes1.length ^ bytes2.length;

		for (int i = 0; (i < bytes1.length) && (i < bytes2.length); ++i) {
			diff |= bytes1[i] ^ bytes2[i];
		}

		if (diff == 0) {
			return true;
		}

		return false;
	}

	@Reference
	private HashAlgorithmEntryLocalService _hashAlgorithmEntryLocalService;

	@Reference
	private HashGeneratorFactoryRegistry _hashGeneratorFactoryRegistry;

	@Reference
	private PasswordEntryLocalService _passwordEntryLocalService;

	@Reference
	private PasswordMetaLocalService _passwordMetaLocalService;

	@Reference
	private PasswordPolicyLocalService _passwordPolicyLocalService;

	@Reference
	private UserLocalService _userLocalService;

}
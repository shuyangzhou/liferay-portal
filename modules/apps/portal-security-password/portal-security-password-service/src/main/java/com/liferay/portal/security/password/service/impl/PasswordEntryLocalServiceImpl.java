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

package com.liferay.portal.security.password.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PasswordPolicy;
import com.liferay.portal.kernel.service.PasswordPolicyLocalService;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.crypto.generator.hashing.HashGenerator;
import com.liferay.portal.security.crypto.generator.registry.HashGeneratorFactoryRegistry;
import com.liferay.portal.security.password.exception.NoSuchEntryException;
import com.liferay.portal.security.password.model.HashAlgorithmEntry;
import com.liferay.portal.security.password.model.PasswordEntry;
import com.liferay.portal.security.password.model.PasswordMeta;
import com.liferay.portal.security.password.service.HashAlgorithmEntryLocalService;
import com.liferay.portal.security.password.service.PasswordMetaLocalService;
import com.liferay.portal.security.password.service.base.PasswordEntryLocalServiceBaseImpl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the password entry local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.portal.security.password.service.PasswordEntryLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS passwords because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Arthur Chan
 * @see PasswordEntryLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.portal.security.password.model.PasswordEntry",
	service = AopService.class
)
public class PasswordEntryLocalServiceImpl
	extends PasswordEntryLocalServiceBaseImpl {

	/**
	 * Add a passwordEntry for user of userId
	 *
	 * @param  UserId, ID of user
	 * @param  Password, plain text password
	 * @return Generated PassowrdEntry
	 * @throws PortalException
	 */
	@Override
	public PasswordEntry addEntry(long userId, String password)
		throws PortalException {

		long entryId = counterLocalService.increment();

		PasswordEntry entry = passwordEntryPersistence.create(entryId);

		PasswordMeta meta = _passwordMetaLocalService.addMetaByEntry(entry);

		entry.setUserId(userId);

		return _updateEntry(entry, meta, password);
	}

	/**
	 * Remove all password entries for given user, including current password and history passwords.
	 *
	 * @param  UserId, ID of user
	 * @throws PortalException
	 */
	@Override
	public void deleteEntriesByUserId(long userId) throws PortalException {
		List<PasswordEntry> passwordEntries = getEntriesByUserId(userId);

		for (PasswordEntry passwordEntry : passwordEntries) {
			deletePasswordEntry(passwordEntry);
		}
	}

	/**
	 * Remove all history password entries for given user.
	 *
	 * @param  UserId, ID of user
	 * @throws PortalException
	 */
	@Override
	public void deleteHistoryEntriesByUserId(long userId)
		throws PortalException {

		List<PasswordEntry> passwordEntries = getHistoryEntriesByUserId(userId);

		for (PasswordEntry passwordEntry : passwordEntries) {
			deletePasswordEntry(passwordEntry);
		}
	}

	@Override
	public PasswordEntry deletePasswordEntry(long entryId)
		throws PortalException {

		_passwordMetaLocalService.deleteMetasByEntryId(entryId);

		return passwordEntryPersistence.remove(entryId);
	}

	@Override
	public PasswordEntry deletePasswordEntry(PasswordEntry entry)
		throws PortalException {

		return deletePasswordEntry(entry.getEntryId());
	}

	/**
	 * Return all password entries for given user, including current password and history passwords. Ordered by modified date.
	 *
	 * @param  UserId, ID of user
	 * @return A list of password entries
	 */
	@Override
	public List<PasswordEntry> getEntriesByUserId(long userId) {
		return passwordEntryPersistence.findByUserId(userId);
	}

	/**
	 * Return current password for given user.
	 *
	 * @param  UserId, ID of user
	 * @return PasswordEntry
	 * @throws PortalException
	 */
	@Override
	public PasswordEntry getEntryByUserId(long userId) throws PortalException {
		List<PasswordEntry> entries = getEntriesByUserId(userId);

		if (entries.isEmpty()) {
			throw new NoSuchEntryException("for UserId: " + userId);
		}

		return entries.get(entries.size() - 1);
	}

	/**
	 * Return history passwords for given user. Ordered by modified date.
	 *
	 * @param  UserId, ID of user
	 * @return A list of Password entries the user used in the past.
	 */
	@Override
	public List<PasswordEntry> getHistoryEntriesByUserId(long userId) {
		int historyCount = passwordEntryPersistence.countByUserId(userId) - 1;

		if (historyCount < 1) {
			return new ArrayList<>();
		}

		return passwordEntryPersistence.findByUserId(userId, 0, historyCount);
	}

	/**
	 * When updating passwordEntries for a user, two things can happen:
	 * 1. when passwordPolicy is set to have password history,
	 *    remove extra history passwords(oldest) that are over history count limit,
	 *    and return a newly added password entry for the user.
	 * 2. otherwise,
	 *    remove all history passwords for the user,
	 *    and return an updated passwordEntry.
	 *
	 * @param  UserId, ID of user
	 * @param  Password, plain text password
	 * @return Updated PassowrdEntry
	 * @throws PortalException
	 */
	@Override
	public PasswordEntry updateEntriesByUserId(long userId, String password)
		throws PortalException {

		PasswordPolicy passwordPolicy =
			_passwordPolicyLocalService.getPasswordPolicyByUserId(userId);

		if ((passwordPolicy == null) || !passwordPolicy.isHistory()) {
			List<PasswordEntry> historyPasswords = getHistoryEntriesByUserId(
				userId);

			for (PasswordEntry historyPassword : historyPasswords) {
				deletePasswordEntry(historyPassword);
			}

			PasswordEntry current = getEntryByUserId(userId);

			PasswordMeta meta = _passwordMetaLocalService.updateMetasByEntry(
				current);

			return _updateEntry(current, meta, password);
		}

		PasswordEntry newEntry = addEntry(userId, password);

		int historyCount = passwordPolicy.getHistoryCount();

		List<PasswordEntry> historyPasswords = getHistoryEntriesByUserId(
			userId);

		int toBeRemoved = historyPasswords.size() - historyCount;

		if (toBeRemoved < 0) {
			toBeRemoved = 0;
		}

		for (int i = 0; i < toBeRemoved; ++i) {
			deletePasswordEntry(historyPasswords.get(i));
		}

		return newEntry;
	}

	private PasswordEntry _updateEntry(
			PasswordEntry entry, PasswordMeta meta, String password)
		throws PortalException {

		HashAlgorithmEntry currentAlgorithm =
			_hashAlgorithmEntryLocalService.getCurrentEntry();

		try {
			JSONObject generatorMeta = new JSONObject(
				currentAlgorithm.getMeta());

			HashGenerator hashGenerator =
				_hashGeneratorFactoryRegistry.createHashGenerator(
					currentAlgorithm.getName(), generatorMeta);

			if (Validator.isNotNull(meta.getSalt())) {
				String salt = meta.getSalt();

				hashGenerator.withSalt(salt.getBytes());
			}

			byte[] hashByte = hashGenerator.hash(password.getBytes());

			entry.setHash(Base64.encode(hashByte));

			return passwordEntryPersistence.update(entry);
		}
		catch (Exception e) {
			throw new PortalException(e.getMessage());
		}
	}

	@Reference
	private HashAlgorithmEntryLocalService _hashAlgorithmEntryLocalService;

	@Reference
	private HashGeneratorFactoryRegistry _hashGeneratorFactoryRegistry;

	@Reference
	private PasswordMetaLocalService _passwordMetaLocalService;

	@Reference
	private PasswordPolicyLocalService _passwordPolicyLocalService;

}
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
import com.liferay.portal.security.crypto.generator.hashing.HashGenerator;
import com.liferay.portal.security.crypto.generator.hashing.salt.SaltGenerator;
import com.liferay.portal.security.crypto.generator.registry.HashGeneratorFactoryRegistry;
import com.liferay.portal.security.password.model.HashAlgorithmEntry;
import com.liferay.portal.security.password.model.PasswordEntry;
import com.liferay.portal.security.password.model.PasswordMeta;
import com.liferay.portal.security.password.service.HashAlgorithmEntryLocalService;
import com.liferay.portal.security.password.service.base.PasswordMetaLocalServiceBaseImpl;

import java.util.List;

import org.json.JSONObject;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the password meta local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.portal.security.password.service.PasswordMetaLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Arthur Chan
 * @see PasswordMetaLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.portal.security.password.model.PasswordMeta",
	service = AopService.class
)
public class PasswordMetaLocalServiceImpl
	extends PasswordMetaLocalServiceBaseImpl {

	/**
	 * Add a passwordMeta for the given passwordEntry during passwordEntry generation.
	 *
	 * @param  PasswordEntry, the passwordEntry
	 * @return Generated meta for passwordEntry
	 * @throws PortalException
	 */
	@Override
	public PasswordMeta addMetaByEntry(PasswordEntry passwordEntry)
		throws PortalException {

		try {
			HashAlgorithmEntry currentAlgorithm =
				_hashAlgorithmEntryLocalService.getCurrentEntry();

			JSONObject generatorMeta = new JSONObject(
				currentAlgorithm.getMeta());

			HashGenerator hashGenerator =
				_hashGeneratorFactoryRegistry.createHashGenerator(
					currentAlgorithm.getName(), generatorMeta);

			SaltGenerator saltGenerator = hashGenerator.getSaltGenerator();

			long metaId = counterLocalService.increment();

			PasswordMeta meta = passwordMetaPersistence.create(metaId);

			meta.setPasswordEntryId(passwordEntry.getEntryId());
			meta.setHashAlgorithmEntryId(currentAlgorithm.getEntryId());
			meta.setSalt(new String(saltGenerator.generateSalt()));

			return passwordMetaPersistence.update(meta);
		}
		catch (Exception e) {
			throw new PortalException(e.getMessage());
		}
	}

	@Override
	public void deleteMetasByEntry(PasswordEntry passwordEntry)
		throws PortalException {

		deleteMetasByEntryId(passwordEntry.getEntryId());
	}

	@Override
	public void deleteMetasByEntryId(long passwordEntryId)
		throws PortalException {

		List<PasswordMeta> metas =
			passwordMetaPersistence.findByPasswordEntryId(passwordEntryId);

		for (PasswordMeta meta : metas) {
			deletePasswordMeta(meta);
		}
	}

	@Override
	public PasswordMeta deletePasswordMeta(long metaId) throws PortalException {
		return deletePasswordMeta(getPasswordMeta(metaId));
	}

	@Override
	public PasswordMeta deletePasswordMeta(PasswordMeta passwordMeta)
		throws PortalException {

		long algorithmId = passwordMeta.getHashAlgorithmEntryId();

		int countOfSameAlgorithm =
			passwordMetaPersistence.countByHashAlgorithmEntryId(algorithmId);

		if (countOfSameAlgorithm == 1) {
			_hashAlgorithmEntryLocalService.deleteHashAlgorithmEntry(
				algorithmId);
		}

		return passwordMetaPersistence.remove(passwordMeta);
	}

	/**
	 * Return a list of metas of PasswordEntry, ordered by modified date
	 *
	 * @param  PasswordEntry, the passwordEntry
	 * @return a list of metas of PasswordEntry
	 * @throws PortalException
	 */
	@Override
	public List<PasswordMeta> getMetasByEntry(PasswordEntry passwordEntry)
		throws PortalException {

		return getMetasByEntryId(passwordEntry.getEntryId());
	}

	/**
	 * Return a list of metas of PasswordEntry, ordered by modified date
	 *
	 * @param  PasswordEntryId, the ID of passwordEntry
	 * @return a list of metas of PasswordEntry
	 * @throws PortalException
	 */
	@Override
	public List<PasswordMeta> getMetasByEntryId(long passwordEntryId)
		throws PortalException {

		return passwordMetaPersistence.findByPasswordEntryId(passwordEntryId);
	}

	@Override
	public int getMetasCountByEntry(PasswordEntry passwordEntry)
		throws PortalException {

		return getMetasCountByEntryId(passwordEntry.getEntryId());
	}

	@Override
	public int getMetasCountByEntryId(long passwordEntryId)
		throws PortalException {

		return passwordMetaPersistence.countByPasswordEntryId(passwordEntryId);
	}

	/**
	 * When a password is being updated, it's metas will be reset to one that uses current Hash algorithm.
	 *
	 * @param  PasswordEntry,the passwordEntry
	 * @return Newly generated single meta for passwordEntry
	 * @throws PortalException
	 */
	@Override
	public PasswordMeta updateMetasByEntry(PasswordEntry passwordEntry)
		throws PortalException {

		return updateMetasByEntryId(passwordEntry.getEntryId());
	}

	/**
	 * When a password is being updated, it's metas will be reset to one that uses current Hash algorithm.
	 *
	 * @param  PasswordEntryId, ID of passwordEntry
	 * @return Newly generated single meta for passwordEntry
	 * @throws PortalException
	 */
	@Override
	public PasswordMeta updateMetasByEntryId(long passwordEntryId)
		throws PortalException {

		HashAlgorithmEntry currentAlgorithm =
			_hashAlgorithmEntryLocalService.getCurrentEntry();

		List<PasswordMeta> metas = getMetasByEntryId(passwordEntryId);

		for (int i = 0; i < (metas.size() - 1); ++i) {
			deletePasswordMeta(metas.get(i));
		}

		PasswordMeta meta = metas.get(metas.size() - 1);

		meta.setHashAlgorithmEntryId(currentAlgorithm.getEntryId());

		try {
			JSONObject generatorMeta = new JSONObject(
				currentAlgorithm.getMeta());

			HashGenerator hashGenerator =
				_hashGeneratorFactoryRegistry.createHashGenerator(
					currentAlgorithm.getName(), generatorMeta);

			SaltGenerator saltGenerator = hashGenerator.getSaltGenerator();

			meta.setSalt(new String(saltGenerator.generateSalt()));
		}
		catch (Exception e) {
			throw new PortalException(e.getMessage());
		}

		return passwordMetaPersistence.update(meta);
	}

	@Reference
	private HashAlgorithmEntryLocalService _hashAlgorithmEntryLocalService;

	@Reference
	private HashGeneratorFactoryRegistry _hashGeneratorFactoryRegistry;

}
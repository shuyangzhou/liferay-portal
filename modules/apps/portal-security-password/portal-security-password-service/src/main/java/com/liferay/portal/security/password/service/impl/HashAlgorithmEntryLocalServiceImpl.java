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

import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.PortalPreferencesLocalService;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.password.configuration.PasswordConfiguration;
import com.liferay.portal.security.password.model.HashAlgorithmEntry;
import com.liferay.portal.security.password.service.base.HashAlgorithmEntryLocalServiceBaseImpl;

import java.util.Map;

import javax.portlet.PortletPreferences;

import org.json.JSONObject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the hash algorithm entry local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.portal.security.password.service.HashAlgorithmEntryLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Arthur Chan
 * @see HashAlgorithmEntryLocalServiceBaseImpl
 */
@Component(
	configurationPid = "com.liferay.portal.security.password.configuration.PasswordConfiguration",
	property = "model.class.name=com.liferay.portal.security.password.model.HashAlgorithmEntry",
	service = AopService.class
)
public class HashAlgorithmEntryLocalServiceImpl
	extends HashAlgorithmEntryLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>com.liferay.portal.security.password.service.HashAlgorithmEntryLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.portal.security.password.service.HashAlgorithmEntryLocalServiceUtil</code>.
	 */
	@Override
	public HashAlgorithmEntry addEntry(String name, JSONObject meta)
		throws PortalException {

		long entryId = counterLocalService.increment();

		HashAlgorithmEntry entry = hashAlgorithmEntryPersistence.create(
			entryId);

		entry.setName(name);
		entry.setMeta(meta.toString());

		entry = hashAlgorithmEntryPersistence.update(entry);

		PortletPreferences portletPreferences =
			_portalPreferencesLocalService.getPreferences(0, 1);

		try {
			portletPreferences.setValue(
				"currentHashAlgorithmId", String.valueOf(entry.getEntryId()));
			portletPreferences.store();
		}
		catch (Exception e) {
			throw new PortalException(e);
		}

		return entry;
	}

	@Override
	public HashAlgorithmEntry deleteEntry(HashAlgorithmEntry entry)
		throws PortalException {

		return hashAlgorithmEntryPersistence.remove(entry);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public HashAlgorithmEntry getCurrentEntry() throws PortalException {
		PortletPreferences portletPreferences =
			_portalPreferencesLocalService.getPreferences(0, 1);

		String currentHashAlgorithmId = portletPreferences.getValue(
			"currentHashAlgorithmId", null);

		if (Validator.isNull(currentHashAlgorithmId)) {
			String name = _passwordConfiguration.hashAlgorithmName();
			String[] attributes = _passwordConfiguration.hashAlgorithmMeta();

			JSONObject metas = new JSONObject();

			for (String attribute : attributes) {
				String[] keyValue = attribute.split(StringPool.COLON);

				metas.put(keyValue[0], keyValue[1]);
			}

			return addEntry(name, metas);
		}

		try {
			long entryId = Long.parseLong(currentHashAlgorithmId);

			return getHashAlgorithmEntry(entryId);
		}
		catch (NumberFormatException nfe) {
			throw new PortalException(nfe);
		}
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_passwordConfiguration = ConfigurableUtil.createConfigurable(
			PasswordConfiguration.class, properties);
	}

	private volatile PasswordConfiguration _passwordConfiguration;

	@Reference
	private PortalPreferencesLocalService _portalPreferencesLocalService;

}
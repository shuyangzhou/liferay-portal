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

package com.liferay.portal.configuration.persistence.internal.upgrade;

import com.liferay.portal.configuration.persistence.upgrade.ConfigurationUpgradeStepFactory;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.upgrade.UpgradeStep;

import org.apache.felix.cm.PersistenceManager;

import java.io.IOException;
import java.util.Dictionary;

/**
 * @author Preston Crary
 */
public class ConfigurationUpgradeStepFactoryImpl
	implements ConfigurationUpgradeStepFactory {

	public ConfigurationUpgradeStepFactoryImpl(
		PersistenceManager persistenceManager) {

		_persistenceManager = persistenceManager;
	}

	@Override
	public UpgradeStep createUpgradeStep(String oldPid, String newPid) {
		return dbProcessContext -> {
			try {
				if (_persistenceManager.exists(oldPid)) {
					Dictionary<?, ?> dictionary = _persistenceManager.load(
						oldPid);

					_persistenceManager.store(newPid, dictionary);

					_persistenceManager.delete(oldPid);
				}
			}
			catch (IOException ioe) {
				throw new UpgradeException(ioe);
			}
		};
	}

	private final PersistenceManager _persistenceManager;

}
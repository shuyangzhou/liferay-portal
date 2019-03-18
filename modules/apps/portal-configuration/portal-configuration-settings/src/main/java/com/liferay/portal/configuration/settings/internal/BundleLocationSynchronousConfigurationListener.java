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

package com.liferay.portal.configuration.settings.internal;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.IOException;

import java.util.Objects;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.SynchronousConfigurationListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Cristina Rodríguez Yrezábal
 * @author Mariano Álvaro Sáiz
 */
@Component(immediate = true, service = SynchronousConfigurationListener.class)
public class BundleLocationSynchronousConfigurationListener
	implements SynchronousConfigurationListener {

	@Override
	public void configurationEvent(ConfigurationEvent event) {
		if (event.getType() != ConfigurationEvent.CM_UPDATED) {
			return;
		}

		String pid = GetterUtil.getString(
			event.getPid(), event.getFactoryPid());

		try {
			Configuration configuration = _configurationAdmin.getConfiguration(
				pid, StringPool.QUESTION);

			String oldLocation = configuration.getBundleLocation();

			if (!Objects.equals(oldLocation, StringPool.QUESTION)) {
				configuration.setBundleLocation(StringPool.QUESTION);
			}
		}
		catch (IOException ioe) {
			_log.error("Unable to update bundle location ", ioe);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BundleLocationSynchronousConfigurationListener.class);

	@Reference
	private ConfigurationAdmin _configurationAdmin;

}
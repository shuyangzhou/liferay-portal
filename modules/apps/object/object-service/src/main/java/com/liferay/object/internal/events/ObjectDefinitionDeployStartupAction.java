/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.events;

import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsValues;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luis Ortiz
 */
@Component(
	property = "key=application.startup.events", service = LifecycleAction.class
)
public class ObjectDefinitionDeployStartupAction extends SimpleAction {

	@Override
	public void run(String[] ids) {
		if (!PropsValues.DATABASE_PARTITION_ENABLED) {
			return;
		}

		long companyId = GetterUtil.getLong(ids[0]);

		try {
			_objectDefinitionLocalService.deployObjectDefinitions(companyId);
		}
		catch (Exception exception) {
			_log.error(
				"Unable to deploy object definitions for company " + companyId,
				exception);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ObjectDefinitionDeployStartupAction.class);

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

}
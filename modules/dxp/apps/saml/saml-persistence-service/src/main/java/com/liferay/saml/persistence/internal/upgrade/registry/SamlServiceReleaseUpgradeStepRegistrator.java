/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.saml.persistence.internal.upgrade.registry;

import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.portal.upgrade.release.BaseReleaseUpgradeStepRegistrator;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 */
@Component(service = UpgradeStepRegistrator.class)
public class SamlServiceReleaseUpgradeStepRegistrator
	extends BaseReleaseUpgradeStepRegistrator {

	@Override
	protected String getNewServletContextName() {
		return "com.liferay.saml.persistence.service";
	}

	@Override
	protected String getOldServletContextName() {
		return "saml-portlet";
	}

	@Override
	protected UpgradeStepRegistrator getUpgradeStepRegistrator() {
		return new SamlServiceUpgradeStepRegistrator(_configurationAdmin);
	}

	@Reference
	private ConfigurationAdmin _configurationAdmin;

}
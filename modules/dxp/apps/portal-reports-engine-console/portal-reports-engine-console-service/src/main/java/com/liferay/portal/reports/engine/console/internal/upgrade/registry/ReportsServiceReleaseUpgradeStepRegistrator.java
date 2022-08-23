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

package com.liferay.portal.reports.engine.console.internal.upgrade.registry;

import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.portal.upgrade.release.BaseReleaseUpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Shuyang Zhou
 */
@Component(service = UpgradeStepRegistrator.class)
public class ReportsServiceReleaseUpgradeStepRegistrator
	extends BaseReleaseUpgradeStepRegistrator {

	@Override
	protected String getNewServletContextName() {
		return "com.liferay.portal.reports.engine.console.service";
	}

	@Override
	protected String getOldServletContextName() {
		return "reports-portlet";
	}

	@Override
	protected UpgradeStepRegistrator getUpgradeStepRegistrator() {
		return new ReportsServiceUpgradeStepRegistrator();
	}

}
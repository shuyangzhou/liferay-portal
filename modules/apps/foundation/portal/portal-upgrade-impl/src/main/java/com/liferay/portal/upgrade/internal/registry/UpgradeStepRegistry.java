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

package com.liferay.portal.upgrade.internal.registry;

import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.upgrade.internal.executor.UpgradeExecutor;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Preston Crary
 */
public class UpgradeStepRegistry implements Registry {

	public UpgradeStepRegistry(
		String bundleSymbolicName, int buildNumber,
		UpgradeExecutor upgradeExecutor) {

		_bundleSymbolicName = bundleSymbolicName;
		_buildNumber = buildNumber;
		_upgradeExecutor = upgradeExecutor;
	}

	@Override
	public void register(
		String bundleSymbolicName, String fromSchemaVersionString,
		String toSchemaVersionString, UpgradeStep... upgradeSteps) {

		register(fromSchemaVersionString, toSchemaVersionString, upgradeSteps);
	}

	@Override
	public void register(
		String fromSchemaVersionString, String toSchemaVersionString,
		UpgradeStep... upgradeSteps) {

		List<UpgradeInfo> upgradeInfos = createUpgradeInfos(
			fromSchemaVersionString, toSchemaVersionString, _buildNumber,
			upgradeSteps);

		_upgradeExecutor.execute(_bundleSymbolicName, upgradeInfos);
	}

	protected static List<UpgradeInfo> createUpgradeInfos(
		String fromSchemaVersionString, String toSchemaVersionString,
		int buildNumber, UpgradeStep... upgradeSteps) {

		if (ArrayUtil.isEmpty(upgradeSteps)) {
			return Collections.emptyList();
		}

		List<UpgradeInfo> upgradeInfos = new ArrayList<>();

		String upgradeInfoFromSchemaVersionString = fromSchemaVersionString;

		for (int i = 0; i < upgradeSteps.length - 1; i++) {
			UpgradeStep upgradeStep = upgradeSteps[i];

			String upgradeInfoToSchemaVersionString =
				toSchemaVersionString + "-step" + (i - upgradeSteps.length + 1);

			UpgradeInfo upgradeInfo = new UpgradeInfo(
				upgradeInfoFromSchemaVersionString,
				upgradeInfoToSchemaVersionString, buildNumber, upgradeStep);

			upgradeInfos.add(upgradeInfo);

			upgradeInfoFromSchemaVersionString =
				upgradeInfoToSchemaVersionString;
		}

		UpgradeInfo upgradeInfo = new UpgradeInfo(
			upgradeInfoFromSchemaVersionString, toSchemaVersionString,
			buildNumber, upgradeSteps[upgradeSteps.length - 1]);

		upgradeInfos.add(upgradeInfo);

		return upgradeInfos;
	}

	private final int _buildNumber;
	private final String _bundleSymbolicName;
	private final UpgradeExecutor _upgradeExecutor;

}
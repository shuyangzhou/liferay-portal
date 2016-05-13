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

package com.liferay.split.packages.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.module.framework.ModuleFrameworkUtilAdapter;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.io.IOException;

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * @author Tom Wang
 */
@RunWith(Arquillian.class)
public class SplitPackagesTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testSplitPackage() throws IOException {
		Bundle frameworkBundle =
			(Bundle)ModuleFrameworkUtilAdapter.getFramework();

		BundleContext frameworkBundleContext =
			frameworkBundle.getBundleContext();

		Bundle[] frameworkBundles = frameworkBundleContext.getBundles();

		Map<Bundle, Map<String, Packages>> bundlesMap = new HashMap<>();

		Map<String, Packages> suitableImportsMap = _createSuitableImportsMap();

		for (Bundle bundle : frameworkBundles) {
			Dictionary<String, String> headers = bundle.getHeaders();

			String exportPackages = headers.get("Export-Package");

			if (exportPackages != null) {
				exportPackages = exportPackages.replaceAll(
					";uses:=\"[^\"]*\"", "");

				String bundleName = headers.get("Bundle-Name");

				Map<String, Packages> currentBundlePackages =
					_createCurrentBundlePackagesMap(bundleName, exportPackages);

				for (Map.Entry<Bundle, Map<String, Packages>> entry :
						bundlesMap.entrySet()) {

					Map<String, Packages> mapBundlePackages = new HashMap<>(
						entry.getValue());

					Set<String> keySet = mapBundlePackages.keySet();

					keySet.retainAll(currentBundlePackages.keySet());

					if (!mapBundlePackages.isEmpty()) {
						_processDuplicatedPackages(
							entry.getKey(), mapBundlePackages.values(),
							currentBundlePackages, suitableImportsMap,
							bundleName);
					}
				}

				bundlesMap.put(bundle, currentBundlePackages);
			}
		}
	}

	private boolean _checkSuitableImportContains(
		Packages suitableImportPackage, Packages duplicatedPackage) {

		String declaredVersion = suitableImportPackage.getVersion();
		String declaredBundles = suitableImportPackage.getBundles();

		if (declaredVersion.equals(duplicatedPackage.getVersion()) &&
			declaredBundles.contains(duplicatedPackage.getBundles())) {

			return true;
		}

		return false;
	}

	private Map<String, Packages> _createCurrentBundlePackagesMap(
		String bundleName, String exportPackages) {

		Map<String, Packages> currentBundlePackages = new HashMap<>();

		for (String exportPackage : StringUtil.split(exportPackages, ',')) {
			String[] exportPackageSplit = StringUtil.split(exportPackage, ';');

			if (exportPackageSplit.length > 1) {
				String[] version = StringUtil.split(exportPackageSplit[1], '=');

				currentBundlePackages.put(
					exportPackageSplit[0],
					new Packages(
						exportPackageSplit[0], StringUtil.unquote(version[1]),
						bundleName));
			}
			else {
				currentBundlePackages.put(
					exportPackageSplit[0],
					new Packages(exportPackageSplit[0], "0.0", bundleName));
			}
		}

		return currentBundlePackages;
	}

	private Map<String, Packages> _createSuitableImportsMap()
		throws IOException {

		Class clazz = this.getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		Map<String, Packages> suitableImports = new HashMap<>();

		for (String packages :
				StringUtil.split(
					IOUtils.toString(
						classLoader.getResourceAsStream("SuitableImports"),
						StringPool.UTF8),'\n')) {

			String[] packageSplit = StringUtil.split(packages, ';');

			suitableImports.put(
				packageSplit[0],
				new Packages(
					packageSplit[0], packageSplit[1], packageSplit[2]));
		}

		return suitableImports;
	}

	private void _processDuplicatedPackages(
		Bundle mapBundle, Collection<Packages> duplicatedPackages,
		Map<String, Packages> currentBundlePackages,
		Map<String, Packages> suitableImportsMap, String bundleName) {

		Dictionary<String, String> mapBundleHeaders = mapBundle.getHeaders();

		String mapBundleName = mapBundleHeaders.get("Bundle-Name");

		for (Packages duplicatedPackage : duplicatedPackages) {
			String duplicatedPackageName = duplicatedPackage.getName();

			Packages currentBundlePackage = currentBundlePackages.get(
				duplicatedPackageName);

			String currentBundlePackageVersion =
				currentBundlePackage.getVersion();

			if (currentBundlePackageVersion.equals(
					duplicatedPackage.getVersion())) {

				Assert.assertTrue(
					"Detected split packages in " + bundleName + " and " +
						mapBundleName + ": " + duplicatedPackageName,
					suitableImportsMap.containsKey(duplicatedPackageName));

				Assert.assertTrue(
					"Detected split packages in " + bundleName + " and " +
						mapBundleName + ": " + duplicatedPackages,
					_checkSuitableImportContains(
						suitableImportsMap.get(duplicatedPackageName),
						currentBundlePackage));
			}
		}
	}

	private class Packages {

		public Packages(String name, String version, String bundles) {
			_name = name;
			_version = version;
			_bundles = bundles;
		}

		public String getBundles() {
			return _bundles;
		}

		public String getName() {
			return _name;
		}

		public String getVersion() {
			return _version;
		}

		private final String _bundles;
		private final String _name;
		private final String _version;

	}

}
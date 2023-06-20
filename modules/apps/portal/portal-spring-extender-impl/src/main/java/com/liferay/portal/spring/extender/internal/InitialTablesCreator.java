/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.extender.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.db.DBResourceUtil;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.configuration.ConfigurationFactoryUtil;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Release;
import com.liferay.portal.kernel.model.ReleaseConstants;
import com.liferay.portal.kernel.service.ReleaseLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.spring.hibernate.DialectDetector;
import com.liferay.portal.upgrade.release.ReleasePublisher;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Dictionary;

import javax.sql.DataSource;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 */
@Component(service = InitialTablesCreator.class)
public class InitialTablesCreator {

	public void create(Bundle bundle, DataSource dataSource)
		throws UpgradeException {

		Release release = _releaseLocalService.fetchRelease(
			bundle.getSymbolicName());

		if (release != null) {
			return;
		}

		DB db = DBManagerUtil.getDB(
			DialectDetector.getDialect(dataSource), dataSource);

		try {
			db.process(
				companyId -> {
					if (_log.isInfoEnabled() &&
						Validator.isNotNull(companyId)) {

						_log.info(
							StringBundler.concat(
								toString(), StringPool.SPACE,
								bundle.getSymbolicName(), "#", companyId));
					}

					_upgrade(bundle, dataSource, db);
				});

			Dictionary<String, String> headers = bundle.getHeaders(
				StringPool.BLANK);

			release = _releaseLocalService.addRelease(
				bundle.getSymbolicName(),
				GetterUtil.getString(
					headers.get("Liferay-Require-SchemaVersion"),
					headers.get("Bundle-Version")));

			BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

			Configuration configuration =
				ConfigurationFactoryUtil.getConfiguration(
					bundleWiring.getClassLoader(), "service");

			if (configuration != null) {
				String buildNumber = configuration.get("build.number");

				if (buildNumber != null) {
					release.setBuildNumber(GetterUtil.getInteger(buildNumber));
				}
			}

			release.setVerified(true);
			release.setState(ReleaseConstants.STATE_GOOD);

			_releasePublisher.publish(
				_releaseLocalService.updateRelease(release), true);
		}
		catch (Exception exception) {
			throw new UpgradeException(exception);
		}
	}

	private void _upgrade(Bundle bundle, DataSource dataSource, DB db)
		throws UpgradeException {

		String indexesSQL = DBResourceUtil.getModuleIndexesSQL(bundle);
		String sequencesSQL = DBResourceUtil.getModuleSequencesSQL(bundle);
		String tablesSQL = DBResourceUtil.getModuleTablesSQL(bundle);

		try (Connection connection = dataSource.getConnection()) {
			if (tablesSQL != null) {
				try {
					db.runSQLTemplateString(connection, tablesSQL, true);
				}
				catch (Exception exception) {
					throw new UpgradeException(
						StringBundler.concat(
							"Bundle ", bundle,
							" has invalid content in tables.sql:\n", tablesSQL),
						exception);
				}
			}

			if (sequencesSQL != null) {
				try {
					db.runSQLTemplateString(connection, sequencesSQL, true);
				}
				catch (Exception exception) {
					throw new UpgradeException(
						StringBundler.concat(
							"Bundle ", bundle,
							" has invalid content in sequences.sql:\n",
							sequencesSQL),
						exception);
				}
			}

			if (indexesSQL != null) {
				try {
					db.runSQLTemplateString(connection, indexesSQL, true);
				}
				catch (Exception exception) {
					throw new UpgradeException(
						StringBundler.concat(
							"Bundle ", bundle,
							" has invalid content in indexes.sql:\n",
							indexesSQL),
						exception);
				}
			}
		}
		catch (SQLException sqlException) {
			throw new UpgradeException(sqlException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		InitialTablesCreator.class);

	@Reference
	private ReleaseLocalService _releaseLocalService;

	@Reference
	private ReleasePublisher _releasePublisher;

}
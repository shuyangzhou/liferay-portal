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

package com.liferay.view.count.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.view.count.service.persistence.ViewCountEntryFinder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Component;

/**
 * @author Preston Crary
 */
@Component(service = ViewCountEntryFinder.class)
public class ViewCountEntryFinderImpl
	extends ViewCountEntryFinderBaseImpl implements ViewCountEntryFinder {

	@Override
	public void incrementViewCount(
		long companyId, long classNameId, long classPK, int increment) {

		DataSource dataSource = getDataSource();

		try (Connection connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement(
				StringBundler.concat(
					"update ViewCountEntry set viewCount = viewCount + ",
					increment, " where companyId = ", companyId,
					" and classNameId = ", classNameId, " and classPK = ",
					classPK))) {

			int rowCount = ps.executeUpdate();

			if ((rowCount != 1) && _log.isWarnEnabled()) {
				_log.warn(
					StringBundler.concat(
						"Unexpected row count ", rowCount,
						" for ViewCountEntry increment {companyId=", companyId,
						", classNameId=", classNameId, ", classPK=", classPK,
						"}"));
			}
		}
		catch (SQLException sqle) {
			throw new SystemException(sqle);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ViewCountEntryFinderImpl.class);

}
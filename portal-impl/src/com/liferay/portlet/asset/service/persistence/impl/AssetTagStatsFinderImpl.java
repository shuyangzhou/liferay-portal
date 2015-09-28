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

package com.liferay.portlet.asset.service.persistence.impl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.WildcardMode;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.asset.model.impl.AssetTagImpl;
import com.liferay.portlet.asset.model.impl.AssetTagStatsImpl;
import com.liferay.portlet.asset.service.persistence.AssetTagStatsFinder;
import com.liferay.portlet.documentlibrary.service.persistence.impl.DLFileEntryFinderImpl;
import com.liferay.portlet.documentlibrary.service.persistence.impl.DLFolderFinderImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

/**
 * @author Preston Crary
 */
public class AssetTagStatsFinderImpl
	extends AssetTagStatsFinderBaseImpl implements AssetTagStatsFinder {

	@Override
	public int updateAssetCountByDLFileEntryC_T_V(
		long classNameId, String treePath, boolean visible) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(
				AssetTagFinderImpl.UPDATE_ASSET_COUNT);

			sql = StringUtil.replace(
				sql, StringPool.SPACE + AssetTagImpl.TABLE_NAME,
				StringPool.SPACE + AssetTagStatsImpl.TABLE_NAME);
			sql = StringUtil.replace(
				sql, AssetTagImpl.TABLE_NAME + StringPool.PERIOD,
				AssetTagStatsImpl.TABLE_NAME + StringPool.PERIOD);

			sql = StringUtil.replace(
				sql, "[$JOIN$]", CustomSQLUtil.get(
					DLFileEntryFinderImpl.JOIN_AE_BY_DL_FILE_ENTRY));

			sql = StringUtil.replace(
				sql, "[$WHERE$]", "DLFileEntry.treePath LIKE ? AND");

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(
				CustomSQLUtil.keywords(treePath, WildcardMode.TRAILING)[0]);
			qPos.add(classNameId);
			qPos.add(visible);

			return q.executeUpdate();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	public int updateAssetCountByDLFolderC_T_V(
		long classNameId, String treePath, boolean visible) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(
				AssetTagFinderImpl.UPDATE_ASSET_COUNT);

			sql = StringUtil.replace(
				sql, StringPool.SPACE + AssetTagImpl.TABLE_NAME,
				StringPool.SPACE + AssetTagStatsImpl.TABLE_NAME);
			sql = StringUtil.replace(
				sql, AssetTagImpl.TABLE_NAME + StringPool.PERIOD,
				AssetTagStatsImpl.TABLE_NAME + StringPool.PERIOD);

			sql = StringUtil.replace(
				sql, "[$JOIN$]", CustomSQLUtil.get(
					DLFolderFinderImpl.JOIN_AE_BY_DL_FOLDER));

			sql = StringUtil.replace(
				sql, "[$WHERE$]", "DLFolder.treePath LIKE ? AND");

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(
				CustomSQLUtil.keywords(treePath, WildcardMode.TRAILING)[0]);
			qPos.add(classNameId);
			qPos.add(visible);

			return q.executeUpdate();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

}
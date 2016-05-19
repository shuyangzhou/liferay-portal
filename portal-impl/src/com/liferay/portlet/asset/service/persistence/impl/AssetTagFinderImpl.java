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

import static test.generated.Tables.ASSETENTRIES_ASSETTAGS;
import static test.generated.Tables.ASSETENTRY;
import static test.generated.Tables.ASSETTAG;
import static test.generated.Tables.SOCIALACTIVITYCOUNTER;

import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.asset.kernel.service.persistence.AssetTagFinder;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.asset.model.impl.AssetTagImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

/**
 * @author Brian Wing Shun Chan
 * @author Bruno Farache
 */
public class AssetTagFinderImpl
	extends AssetTagFinderBaseImpl implements AssetTagFinder {

	public static final String COUNT_BY_G_N =
		AssetTagFinder.class.getName() + ".countByG_N";

	public static final String COUNT_BY_G_C_N =
		AssetTagFinder.class.getName() + ".countByG_C_N";

	public static final String FIND_BY_G_C_N =
		AssetTagFinder.class.getName() + ".findByG_C_N";

	public static final String FIND_BY_G_N_S_E =
		AssetTagFinder.class.getName() + ".findByG_N_S_E";

	@Override
	public int countByG_N(long groupId, String name) {
		Session session = null;

		try {
			session = openSession();

			DSLContext context = DSL.using(SQLDialect.DEFAULT, _settings);

			String sql = context.select(
				ASSETENTRIES_ASSETTAGS.ENTRYID
					.countDistinct()
					.as(COUNT_COLUMN_NAME))
				.from(ASSETTAG)
				.innerJoin(ASSETENTRIES_ASSETTAGS)
				.on(ASSETENTRIES_ASSETTAGS.TAGID.eq(ASSETTAG.TAGID))
				.where(ASSETENTRIES_ASSETTAGS.ENTRYID.in(
					context.select(ASSETENTRIES_ASSETTAGS.ENTRYID)
						.from(ASSETENTRY)
						.where(ASSETENTRY.GROUPID.eq(groupId)
							.and(ASSETENTRY.VISIBLE.isTrue())))
					.and(ASSETTAG.NAME.like(StringUtil.toLowerCase(name))))
				.getSQL();

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			Iterator<Long> itr = q.iterate();

			if (itr.hasNext()) {
				Long count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	public List<AssetTag> findByG_N_S_E(
		long groupId, String name, int startPeriod, int endPeriod,
		int periodLength) {

		Session session = null;

		try {
			session = openSession();

			DSLContext context = DSL.using(SQLDialect.DEFAULT, _settings);

			String sql = context.select(
				ASSETTAG.TAGID, ASSETTAG.NAME,
				DSL.sum(SOCIALACTIVITYCOUNTER.CURRENTVALUE))
				.from(ASSETTAG)
				.innerJoin(ASSETENTRIES_ASSETTAGS)
				.on(ASSETENTRIES_ASSETTAGS.TAGID.eq(ASSETTAG.TAGID))
				.innerJoin(ASSETENTRY)
				.on(ASSETENTRY.ENTRYID.eq(ASSETENTRIES_ASSETTAGS.ENTRYID))
				.innerJoin(SOCIALACTIVITYCOUNTER)
				.on(SOCIALACTIVITYCOUNTER.CLASSNAMEID.eq(ASSETENTRY.CLASSNAMEID)
					.and(SOCIALACTIVITYCOUNTER.CLASSPK.eq(ASSETENTRY.CLASSPK)))
				.where(SOCIALACTIVITYCOUNTER.GROUPID.eq(groupId)
					.and(SOCIALACTIVITYCOUNTER.NAME.eq(name))
					.and(SOCIALACTIVITYCOUNTER.STARTPERIOD.ge(startPeriod))
					.and(SOCIALACTIVITYCOUNTER.STARTPERIOD.le(endPeriod))
					.and(SOCIALACTIVITYCOUNTER.STARTPERIOD.plus(periodLength)
						.le(endPeriod)))
				.groupBy(ASSETTAG.TAGID, ASSETTAG.NAME)
				.getSQL();

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			List<AssetTag> assetTags = new ArrayList<>();

			Iterator<Object[]> itr = q.iterate();

			while (itr.hasNext()) {
				Object[] array = itr.next();

				AssetTag assetTag = new AssetTagImpl();

				assetTag.setTagId(GetterUtil.getLong(array[0]));
				assetTag.setName(GetterUtil.getString(array[1]));
				assetTag.setAssetCount(GetterUtil.getInteger(array[2]));

				assetTags.add(assetTag);
			}

			return assetTags;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	private static final Settings _settings = new Settings();

	@Override
	public int countByG_C_N(long groupId, long classNameId, String name) {
		Session session = null;

		try {
			session = openSession();

			DSLContext context = DSL.using(SQLDialect.DEFAULT, _settings);

			String sql = context.select(
				ASSETENTRIES_ASSETTAGS.ENTRYID
				.countDistinct()
					.as(COUNT_COLUMN_NAME))
				.from(ASSETTAG)
				.innerJoin(ASSETENTRIES_ASSETTAGS)
				.on(ASSETENTRIES_ASSETTAGS.TAGID.eq(ASSETTAG.TAGID))
				.where(ASSETENTRIES_ASSETTAGS.ENTRYID.in(
					context.select(ASSETENTRIES_ASSETTAGS.ENTRYID)
						.from(ASSETENTRY)
						.where(ASSETENTRY.GROUPID.eq(groupId)
							.and(ASSETENTRY.CLASSNAMEID.eq(classNameId))
							.and(ASSETENTRY.VISIBLE.isTrue())))
					.and(ASSETTAG.NAME.like(StringUtil.toLowerCase(name))
						.or(Validator.isNull(name))))
					.getSQL();

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			Iterator<Long> itr = q.iterate();

			if (itr.hasNext()) {
				Long count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	public List<AssetTag> findByG_C_N(
		long groupId, long classNameId, String name, int start, int end,
		OrderByComparator<AssetTag> obc) {

		Session session = null;

		try {
			session = openSession();

			DSLContext context = DSL.using(SQLDialect.DEFAULT, _settings);

			String sql = context.select()
				.distinctOn(ASSETTAG.TAGID)
				.from(ASSETTAG)
				.innerJoin(ASSETENTRIES_ASSETTAGS)
				.on(ASSETENTRIES_ASSETTAGS.TAGID.eq(ASSETTAG.TAGID))
				.where(ASSETENTRIES_ASSETTAGS.ENTRYID.in(
					context.select(ASSETENTRY.ENTRYID)
						.from(ASSETENTRY)
						.where(ASSETENTRY.GROUPID.eq(groupId))
						.and(ASSETENTRY.CLASSNAMEID.eq(classNameId))
						.and(ASSETENTRY.VISIBLE.isTrue()))
					.and(ASSETTAG.NAME.like(StringUtil.lowerCase(name))
						.or(Validator.isNull(name))))
				.orderBy(ASSETTAG.NAME.asc())
				.getSQL();

			sql = CustomSQLUtil.replaceOrderBy(sql, obc);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addEntity("AssetTag", AssetTagImpl.class);

			return (List<AssetTag>)QueryUtil.list(q, getDialect(), start, end);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	static {
		_settings.setParamType(ParamType.INLINED);
		_settings.setRenderNameStyle(RenderNameStyle.AS_IS);
		_settings.setRenderSchema(Boolean.FALSE);
	}

}
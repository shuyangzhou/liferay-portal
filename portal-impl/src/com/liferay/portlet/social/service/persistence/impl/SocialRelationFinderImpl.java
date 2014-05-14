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

package com.liferay.portlet.social.service.persistence.impl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.persistence.UserUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portlet.social.model.SocialRelation;
import com.liferay.portlet.social.model.SocialRelationConstants;
import com.liferay.portlet.social.service.persistence.SocialRelationFinder;
import com.liferay.util.dao.orm.CustomSQLUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
public class SocialRelationFinderImpl
	extends BasePersistenceImpl<SocialRelation>
	implements SocialRelationFinder {

	public static final String COUNT_SOCIAL_USERS =
		SocialRelationFinder.class.getName() + ".countSocialUsers";

	public static final String COUNT_SOCIAL_USERS_BY_TYPE =
		SocialRelationFinder.class.getName() + ".countSocialUsersByType";

	public static final String FIND_SOCIAL_USERS =
		SocialRelationFinder.class.getName() + ".findSocialUsers";

	public static final String FIND_SOCIAL_USERS_BY_TYPE =
		SocialRelationFinder.class.getName() + ".findSocialUsersByType";

	public int countSocialUsers(long companyId, long userId, int status)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(COUNT_SOCIAL_USERS);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(SocialRelationConstants.TYPE_UNI_ENEMY);
			qPos.add(userId);
			qPos.add(companyId);
			qPos.add(Boolean.FALSE);
			qPos.add(status);

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

	/**
	 * Returns the number of users with a social relation of the type with the
	 * user.
	 *
	 * @param  userId the primary key of the user
	 * @param  type the type of social relation. The possible types can be found
	 *         in {@link
	 *         com.liferay.portlet.social.model.SocialRelationConstants}.
	 * @return the number of users with a social relation of the type with the
	 *         user
	 * @throws PortalException if a user with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public int countSocialUsersByType(
			long companyId, long userId, int type, int status)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(COUNT_SOCIAL_USERS_BY_TYPE);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(type);
			qPos.add(userId);
			qPos.add(companyId);
			qPos.add(Boolean.FALSE);
			qPos.add(status);

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

	public List<User> findSocialUsers(
			long companyId, long userId, int status, int start, int end,
			OrderByComparator obc)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_SOCIAL_USERS);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addScalar("userId", Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(SocialRelationConstants.TYPE_UNI_ENEMY);
			qPos.add(userId);
			qPos.add(companyId);
			qPos.add(Boolean.FALSE);
			qPos.add(status);

			Set<Long> userIds = new LinkedHashSet<Long>(
				(List<Long>)QueryUtil.list(q, getDialect(), start, end));

			List<User> users = new ArrayList<User>(userIds.size());

			for (Long userId1 : userIds) {
				User user = UserUtil.findByPrimaryKey(userId1);

				users.add(user);
			}

			return users;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List<User> findSocialUsersByType(
			long companyId, long userId, int type, int status, int start,
			int end, OrderByComparator obc)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_SOCIAL_USERS_BY_TYPE);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addScalar("userId", Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(type);
			qPos.add(userId);
			qPos.add(companyId);
			qPos.add(Boolean.FALSE);
			qPos.add(status);

			Set<Long> userIds = new LinkedHashSet<Long>(
				(List<Long>)QueryUtil.list(q, getDialect(), start, end));

			List<User> users = new ArrayList<User>(userIds.size());

			for (Long userId1 : userIds) {
				User user = UserUtil.findByPrimaryKey(userId1.longValue());

				users.add(user);
			}

			return users;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

}
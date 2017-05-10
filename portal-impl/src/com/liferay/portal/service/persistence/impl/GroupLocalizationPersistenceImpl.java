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

package com.liferay.portal.service.persistence.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.NoSuchGroupLocalizationException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.GroupLocalization;
import com.liferay.portal.kernel.service.persistence.CompanyProvider;
import com.liferay.portal.kernel.service.persistence.CompanyProviderWrapper;
import com.liferay.portal.kernel.service.persistence.GroupLocalizationPersistence;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.impl.GroupLocalizationImpl;
import com.liferay.portal.model.impl.GroupLocalizationModelImpl;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The persistence implementation for the group localization service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see GroupLocalizationPersistence
 * @see com.liferay.portal.kernel.service.persistence.GroupLocalizationUtil
 * @generated
 */
@ProviderType
public class GroupLocalizationPersistenceImpl extends BasePersistenceImpl<GroupLocalization>
	implements GroupLocalizationPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link GroupLocalizationUtil} to access the group localization persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = GroupLocalizationImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			GroupLocalizationModelImpl.FINDER_CACHE_ENABLED,
			GroupLocalizationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			GroupLocalizationModelImpl.FINDER_CACHE_ENABLED,
			GroupLocalizationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			GroupLocalizationModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPID = new FinderPath(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			GroupLocalizationModelImpl.FINDER_CACHE_ENABLED,
			GroupLocalizationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroupId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID =
		new FinderPath(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			GroupLocalizationModelImpl.FINDER_CACHE_ENABLED,
			GroupLocalizationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupId",
			new String[] { Long.class.getName() },
			GroupLocalizationModelImpl.GROUPID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_GROUPID = new FinderPath(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			GroupLocalizationModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the group localizations where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching group localizations
	 */
	@Override
	public List<GroupLocalization> findByGroupId(long groupId) {
		return findByGroupId(groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the group localizations where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link GroupLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of group localizations
	 * @param end the upper bound of the range of group localizations (not inclusive)
	 * @return the range of matching group localizations
	 */
	@Override
	public List<GroupLocalization> findByGroupId(long groupId, int start,
		int end) {
		return findByGroupId(groupId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the group localizations where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link GroupLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of group localizations
	 * @param end the upper bound of the range of group localizations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching group localizations
	 */
	@Override
	public List<GroupLocalization> findByGroupId(long groupId, int start,
		int end, OrderByComparator<GroupLocalization> orderByComparator) {
		return findByGroupId(groupId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the group localizations where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link GroupLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of group localizations
	 * @param end the upper bound of the range of group localizations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching group localizations
	 */
	@Override
	public List<GroupLocalization> findByGroupId(long groupId, int start,
		int end, OrderByComparator<GroupLocalization> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID;
			finderArgs = new Object[] { groupId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPID;
			finderArgs = new Object[] { groupId, start, end, orderByComparator };
		}

		List<GroupLocalization> list = null;

		if (retrieveFromCache) {
			list = (List<GroupLocalization>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (GroupLocalization groupLocalization : list) {
					if ((groupId != groupLocalization.getGroupId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_GROUPLOCALIZATION_WHERE);

			query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(GroupLocalizationModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (!pagination) {
					list = (List<GroupLocalization>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<GroupLocalization>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first group localization in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching group localization
	 * @throws NoSuchGroupLocalizationException if a matching group localization could not be found
	 */
	@Override
	public GroupLocalization findByGroupId_First(long groupId,
		OrderByComparator<GroupLocalization> orderByComparator)
		throws NoSuchGroupLocalizationException {
		GroupLocalization groupLocalization = fetchByGroupId_First(groupId,
				orderByComparator);

		if (groupLocalization != null) {
			return groupLocalization;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchGroupLocalizationException(msg.toString());
	}

	/**
	 * Returns the first group localization in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching group localization, or <code>null</code> if a matching group localization could not be found
	 */
	@Override
	public GroupLocalization fetchByGroupId_First(long groupId,
		OrderByComparator<GroupLocalization> orderByComparator) {
		List<GroupLocalization> list = findByGroupId(groupId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last group localization in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching group localization
	 * @throws NoSuchGroupLocalizationException if a matching group localization could not be found
	 */
	@Override
	public GroupLocalization findByGroupId_Last(long groupId,
		OrderByComparator<GroupLocalization> orderByComparator)
		throws NoSuchGroupLocalizationException {
		GroupLocalization groupLocalization = fetchByGroupId_Last(groupId,
				orderByComparator);

		if (groupLocalization != null) {
			return groupLocalization;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchGroupLocalizationException(msg.toString());
	}

	/**
	 * Returns the last group localization in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching group localization, or <code>null</code> if a matching group localization could not be found
	 */
	@Override
	public GroupLocalization fetchByGroupId_Last(long groupId,
		OrderByComparator<GroupLocalization> orderByComparator) {
		int count = countByGroupId(groupId);

		if (count == 0) {
			return null;
		}

		List<GroupLocalization> list = findByGroupId(groupId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the group localizations before and after the current group localization in the ordered set where groupId = &#63;.
	 *
	 * @param groupLocalizationId the primary key of the current group localization
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next group localization
	 * @throws NoSuchGroupLocalizationException if a group localization with the primary key could not be found
	 */
	@Override
	public GroupLocalization[] findByGroupId_PrevAndNext(
		long groupLocalizationId, long groupId,
		OrderByComparator<GroupLocalization> orderByComparator)
		throws NoSuchGroupLocalizationException {
		GroupLocalization groupLocalization = findByPrimaryKey(groupLocalizationId);

		Session session = null;

		try {
			session = openSession();

			GroupLocalization[] array = new GroupLocalizationImpl[3];

			array[0] = getByGroupId_PrevAndNext(session, groupLocalization,
					groupId, orderByComparator, true);

			array[1] = groupLocalization;

			array[2] = getByGroupId_PrevAndNext(session, groupLocalization,
					groupId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected GroupLocalization getByGroupId_PrevAndNext(Session session,
		GroupLocalization groupLocalization, long groupId,
		OrderByComparator<GroupLocalization> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(4 +
					(orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_GROUPLOCALIZATION_WHERE);

		query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(GroupLocalizationModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(groupLocalization);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<GroupLocalization> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the group localizations where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 */
	@Override
	public void removeByGroupId(long groupId) {
		for (GroupLocalization groupLocalization : findByGroupId(groupId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(groupLocalization);
		}
	}

	/**
	 * Returns the number of group localizations where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching group localizations
	 */
	@Override
	public int countByGroupId(long groupId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_GROUPID;

		Object[] finderArgs = new Object[] { groupId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_GROUPLOCALIZATION_WHERE);

			query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_GROUPID_GROUPID_2 = "groupLocalization.groupId = ?";
	public static final FinderPath FINDER_PATH_FETCH_BY_GROUPID_LANGUAGEID = new FinderPath(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			GroupLocalizationModelImpl.FINDER_CACHE_ENABLED,
			GroupLocalizationImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByGroupId_LanguageId",
			new String[] { Long.class.getName(), String.class.getName() },
			GroupLocalizationModelImpl.GROUPID_COLUMN_BITMASK |
			GroupLocalizationModelImpl.LANGUAGEID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_GROUPID_LANGUAGEID = new FinderPath(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			GroupLocalizationModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByGroupId_LanguageId",
			new String[] { Long.class.getName(), String.class.getName() });

	/**
	 * Returns the group localization where groupId = &#63; and languageId = &#63; or throws a {@link NoSuchGroupLocalizationException} if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param languageId the language ID
	 * @return the matching group localization
	 * @throws NoSuchGroupLocalizationException if a matching group localization could not be found
	 */
	@Override
	public GroupLocalization findByGroupId_LanguageId(long groupId,
		String languageId) throws NoSuchGroupLocalizationException {
		GroupLocalization groupLocalization = fetchByGroupId_LanguageId(groupId,
				languageId);

		if (groupLocalization == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("groupId=");
			msg.append(groupId);

			msg.append(", languageId=");
			msg.append(languageId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isDebugEnabled()) {
				_log.debug(msg.toString());
			}

			throw new NoSuchGroupLocalizationException(msg.toString());
		}

		return groupLocalization;
	}

	/**
	 * Returns the group localization where groupId = &#63; and languageId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param languageId the language ID
	 * @return the matching group localization, or <code>null</code> if a matching group localization could not be found
	 */
	@Override
	public GroupLocalization fetchByGroupId_LanguageId(long groupId,
		String languageId) {
		return fetchByGroupId_LanguageId(groupId, languageId, true);
	}

	/**
	 * Returns the group localization where groupId = &#63; and languageId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param languageId the language ID
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the matching group localization, or <code>null</code> if a matching group localization could not be found
	 */
	@Override
	public GroupLocalization fetchByGroupId_LanguageId(long groupId,
		String languageId, boolean retrieveFromCache) {
		Object[] finderArgs = new Object[] { groupId, languageId };

		Object result = null;

		if (retrieveFromCache) {
			result = finderCache.getResult(FINDER_PATH_FETCH_BY_GROUPID_LANGUAGEID,
					finderArgs, this);
		}

		if (result instanceof GroupLocalization) {
			GroupLocalization groupLocalization = (GroupLocalization)result;

			if ((groupId != groupLocalization.getGroupId()) ||
					!Objects.equals(languageId,
						groupLocalization.getLanguageId())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_GROUPLOCALIZATION_WHERE);

			query.append(_FINDER_COLUMN_GROUPID_LANGUAGEID_GROUPID_2);

			boolean bindLanguageId = false;

			if (languageId == null) {
				query.append(_FINDER_COLUMN_GROUPID_LANGUAGEID_LANGUAGEID_1);
			}
			else if (languageId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GROUPID_LANGUAGEID_LANGUAGEID_3);
			}
			else {
				bindLanguageId = true;

				query.append(_FINDER_COLUMN_GROUPID_LANGUAGEID_LANGUAGEID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindLanguageId) {
					qPos.add(languageId);
				}

				List<GroupLocalization> list = q.list();

				if (list.isEmpty()) {
					finderCache.putResult(FINDER_PATH_FETCH_BY_GROUPID_LANGUAGEID,
						finderArgs, list);
				}
				else {
					GroupLocalization groupLocalization = list.get(0);

					result = groupLocalization;

					cacheResult(groupLocalization);

					if ((groupLocalization.getGroupId() != groupId) ||
							(groupLocalization.getLanguageId() == null) ||
							!groupLocalization.getLanguageId().equals(languageId)) {
						finderCache.putResult(FINDER_PATH_FETCH_BY_GROUPID_LANGUAGEID,
							finderArgs, groupLocalization);
					}
				}
			}
			catch (Exception e) {
				finderCache.removeResult(FINDER_PATH_FETCH_BY_GROUPID_LANGUAGEID,
					finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (GroupLocalization)result;
		}
	}

	/**
	 * Removes the group localization where groupId = &#63; and languageId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param languageId the language ID
	 * @return the group localization that was removed
	 */
	@Override
	public GroupLocalization removeByGroupId_LanguageId(long groupId,
		String languageId) throws NoSuchGroupLocalizationException {
		GroupLocalization groupLocalization = findByGroupId_LanguageId(groupId,
				languageId);

		return remove(groupLocalization);
	}

	/**
	 * Returns the number of group localizations where groupId = &#63; and languageId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param languageId the language ID
	 * @return the number of matching group localizations
	 */
	@Override
	public int countByGroupId_LanguageId(long groupId, String languageId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_GROUPID_LANGUAGEID;

		Object[] finderArgs = new Object[] { groupId, languageId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_GROUPLOCALIZATION_WHERE);

			query.append(_FINDER_COLUMN_GROUPID_LANGUAGEID_GROUPID_2);

			boolean bindLanguageId = false;

			if (languageId == null) {
				query.append(_FINDER_COLUMN_GROUPID_LANGUAGEID_LANGUAGEID_1);
			}
			else if (languageId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GROUPID_LANGUAGEID_LANGUAGEID_3);
			}
			else {
				bindLanguageId = true;

				query.append(_FINDER_COLUMN_GROUPID_LANGUAGEID_LANGUAGEID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindLanguageId) {
					qPos.add(languageId);
				}

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_GROUPID_LANGUAGEID_GROUPID_2 = "groupLocalization.groupId = ? AND ";
	private static final String _FINDER_COLUMN_GROUPID_LANGUAGEID_LANGUAGEID_1 = "groupLocalization.languageId IS NULL";
	private static final String _FINDER_COLUMN_GROUPID_LANGUAGEID_LANGUAGEID_2 = "groupLocalization.languageId = ?";
	private static final String _FINDER_COLUMN_GROUPID_LANGUAGEID_LANGUAGEID_3 = "(groupLocalization.languageId IS NULL OR groupLocalization.languageId = '')";

	public GroupLocalizationPersistenceImpl() {
		setModelClass(GroupLocalization.class);
	}

	/**
	 * Caches the group localization in the entity cache if it is enabled.
	 *
	 * @param groupLocalization the group localization
	 */
	@Override
	public void cacheResult(GroupLocalization groupLocalization) {
		entityCache.putResult(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			GroupLocalizationImpl.class, groupLocalization.getPrimaryKey(),
			groupLocalization);

		finderCache.putResult(FINDER_PATH_FETCH_BY_GROUPID_LANGUAGEID,
			new Object[] {
				groupLocalization.getGroupId(),
				groupLocalization.getLanguageId()
			}, groupLocalization);

		groupLocalization.resetOriginalValues();
	}

	/**
	 * Caches the group localizations in the entity cache if it is enabled.
	 *
	 * @param groupLocalizations the group localizations
	 */
	@Override
	public void cacheResult(List<GroupLocalization> groupLocalizations) {
		for (GroupLocalization groupLocalization : groupLocalizations) {
			if (entityCache.getResult(
						GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
						GroupLocalizationImpl.class,
						groupLocalization.getPrimaryKey()) == null) {
				cacheResult(groupLocalization);
			}
			else {
				groupLocalization.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all group localizations.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(GroupLocalizationImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the group localization.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(GroupLocalization groupLocalization) {
		entityCache.removeResult(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			GroupLocalizationImpl.class, groupLocalization.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache((GroupLocalizationModelImpl)groupLocalization,
			true);
	}

	@Override
	public void clearCache(List<GroupLocalization> groupLocalizations) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (GroupLocalization groupLocalization : groupLocalizations) {
			entityCache.removeResult(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
				GroupLocalizationImpl.class, groupLocalization.getPrimaryKey());

			clearUniqueFindersCache((GroupLocalizationModelImpl)groupLocalization,
				true);
		}
	}

	protected void cacheUniqueFindersCache(
		GroupLocalizationModelImpl groupLocalizationModelImpl) {
		Object[] args = new Object[] {
				groupLocalizationModelImpl.getGroupId(),
				groupLocalizationModelImpl.getLanguageId()
			};

		finderCache.putResult(FINDER_PATH_COUNT_BY_GROUPID_LANGUAGEID, args,
			Long.valueOf(1), false);
		finderCache.putResult(FINDER_PATH_FETCH_BY_GROUPID_LANGUAGEID, args,
			groupLocalizationModelImpl, false);
	}

	protected void clearUniqueFindersCache(
		GroupLocalizationModelImpl groupLocalizationModelImpl,
		boolean clearCurrent) {
		if (clearCurrent) {
			Object[] args = new Object[] {
					groupLocalizationModelImpl.getGroupId(),
					groupLocalizationModelImpl.getLanguageId()
				};

			finderCache.removeResult(FINDER_PATH_COUNT_BY_GROUPID_LANGUAGEID,
				args);
			finderCache.removeResult(FINDER_PATH_FETCH_BY_GROUPID_LANGUAGEID,
				args);
		}

		if ((groupLocalizationModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_GROUPID_LANGUAGEID.getColumnBitmask()) != 0) {
			Object[] args = new Object[] {
					groupLocalizationModelImpl.getOriginalGroupId(),
					groupLocalizationModelImpl.getOriginalLanguageId()
				};

			finderCache.removeResult(FINDER_PATH_COUNT_BY_GROUPID_LANGUAGEID,
				args);
			finderCache.removeResult(FINDER_PATH_FETCH_BY_GROUPID_LANGUAGEID,
				args);
		}
	}

	/**
	 * Creates a new group localization with the primary key. Does not add the group localization to the database.
	 *
	 * @param groupLocalizationId the primary key for the new group localization
	 * @return the new group localization
	 */
	@Override
	public GroupLocalization create(long groupLocalizationId) {
		GroupLocalization groupLocalization = new GroupLocalizationImpl();

		groupLocalization.setNew(true);
		groupLocalization.setPrimaryKey(groupLocalizationId);

		groupLocalization.setCompanyId(companyProvider.getCompanyId());

		return groupLocalization;
	}

	/**
	 * Removes the group localization with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param groupLocalizationId the primary key of the group localization
	 * @return the group localization that was removed
	 * @throws NoSuchGroupLocalizationException if a group localization with the primary key could not be found
	 */
	@Override
	public GroupLocalization remove(long groupLocalizationId)
		throws NoSuchGroupLocalizationException {
		return remove((Serializable)groupLocalizationId);
	}

	/**
	 * Removes the group localization with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the group localization
	 * @return the group localization that was removed
	 * @throws NoSuchGroupLocalizationException if a group localization with the primary key could not be found
	 */
	@Override
	public GroupLocalization remove(Serializable primaryKey)
		throws NoSuchGroupLocalizationException {
		Session session = null;

		try {
			session = openSession();

			GroupLocalization groupLocalization = (GroupLocalization)session.get(GroupLocalizationImpl.class,
					primaryKey);

			if (groupLocalization == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchGroupLocalizationException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(groupLocalization);
		}
		catch (NoSuchGroupLocalizationException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected GroupLocalization removeImpl(GroupLocalization groupLocalization) {
		groupLocalization = toUnwrappedModel(groupLocalization);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(groupLocalization)) {
				groupLocalization = (GroupLocalization)session.get(GroupLocalizationImpl.class,
						groupLocalization.getPrimaryKeyObj());
			}

			if (groupLocalization != null) {
				session.delete(groupLocalization);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (groupLocalization != null) {
			clearCache(groupLocalization);
		}

		return groupLocalization;
	}

	@Override
	public GroupLocalization updateImpl(GroupLocalization groupLocalization) {
		groupLocalization = toUnwrappedModel(groupLocalization);

		boolean isNew = groupLocalization.isNew();

		GroupLocalizationModelImpl groupLocalizationModelImpl = (GroupLocalizationModelImpl)groupLocalization;

		Session session = null;

		try {
			session = openSession();

			if (groupLocalization.isNew()) {
				session.save(groupLocalization);

				groupLocalization.setNew(false);
			}
			else {
				groupLocalization = (GroupLocalization)session.merge(groupLocalization);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!GroupLocalizationModelImpl.COLUMN_BITMASK_ENABLED) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else
		 if (isNew) {
			Object[] args = new Object[] { groupLocalizationModelImpl.getGroupId() };

			finderCache.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
			finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
				args);

			finderCache.removeResult(FINDER_PATH_COUNT_ALL, FINDER_ARGS_EMPTY);
			finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL,
				FINDER_ARGS_EMPTY);
		}

		else {
			if ((groupLocalizationModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						groupLocalizationModelImpl.getOriginalGroupId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
					args);

				args = new Object[] { groupLocalizationModelImpl.getGroupId() };

				finderCache.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
					args);
			}
		}

		entityCache.putResult(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			GroupLocalizationImpl.class, groupLocalization.getPrimaryKey(),
			groupLocalization, false);

		clearUniqueFindersCache(groupLocalizationModelImpl, false);
		cacheUniqueFindersCache(groupLocalizationModelImpl);

		groupLocalization.resetOriginalValues();

		return groupLocalization;
	}

	protected GroupLocalization toUnwrappedModel(
		GroupLocalization groupLocalization) {
		if (groupLocalization instanceof GroupLocalizationImpl) {
			return groupLocalization;
		}

		GroupLocalizationImpl groupLocalizationImpl = new GroupLocalizationImpl();

		groupLocalizationImpl.setNew(groupLocalization.isNew());
		groupLocalizationImpl.setPrimaryKey(groupLocalization.getPrimaryKey());

		groupLocalizationImpl.setMvccVersion(groupLocalization.getMvccVersion());
		groupLocalizationImpl.setGroupLocalizationId(groupLocalization.getGroupLocalizationId());
		groupLocalizationImpl.setCompanyId(groupLocalization.getCompanyId());
		groupLocalizationImpl.setGroupId(groupLocalization.getGroupId());
		groupLocalizationImpl.setLanguageId(groupLocalization.getLanguageId());
		groupLocalizationImpl.setName(groupLocalization.getName());
		groupLocalizationImpl.setDescription(groupLocalization.getDescription());

		return groupLocalizationImpl;
	}

	/**
	 * Returns the group localization with the primary key or throws a {@link com.liferay.portal.kernel.exception.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the group localization
	 * @return the group localization
	 * @throws NoSuchGroupLocalizationException if a group localization with the primary key could not be found
	 */
	@Override
	public GroupLocalization findByPrimaryKey(Serializable primaryKey)
		throws NoSuchGroupLocalizationException {
		GroupLocalization groupLocalization = fetchByPrimaryKey(primaryKey);

		if (groupLocalization == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchGroupLocalizationException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return groupLocalization;
	}

	/**
	 * Returns the group localization with the primary key or throws a {@link NoSuchGroupLocalizationException} if it could not be found.
	 *
	 * @param groupLocalizationId the primary key of the group localization
	 * @return the group localization
	 * @throws NoSuchGroupLocalizationException if a group localization with the primary key could not be found
	 */
	@Override
	public GroupLocalization findByPrimaryKey(long groupLocalizationId)
		throws NoSuchGroupLocalizationException {
		return findByPrimaryKey((Serializable)groupLocalizationId);
	}

	/**
	 * Returns the group localization with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the group localization
	 * @return the group localization, or <code>null</code> if a group localization with the primary key could not be found
	 */
	@Override
	public GroupLocalization fetchByPrimaryKey(Serializable primaryKey) {
		Serializable serializable = entityCache.getResult(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
				GroupLocalizationImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		GroupLocalization groupLocalization = (GroupLocalization)serializable;

		if (groupLocalization == null) {
			Session session = null;

			try {
				session = openSession();

				groupLocalization = (GroupLocalization)session.get(GroupLocalizationImpl.class,
						primaryKey);

				if (groupLocalization != null) {
					cacheResult(groupLocalization);
				}
				else {
					entityCache.putResult(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
						GroupLocalizationImpl.class, primaryKey, nullModel);
				}
			}
			catch (Exception e) {
				entityCache.removeResult(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
					GroupLocalizationImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return groupLocalization;
	}

	/**
	 * Returns the group localization with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param groupLocalizationId the primary key of the group localization
	 * @return the group localization, or <code>null</code> if a group localization with the primary key could not be found
	 */
	@Override
	public GroupLocalization fetchByPrimaryKey(long groupLocalizationId) {
		return fetchByPrimaryKey((Serializable)groupLocalizationId);
	}

	@Override
	public Map<Serializable, GroupLocalization> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, GroupLocalization> map = new HashMap<Serializable, GroupLocalization>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			GroupLocalization groupLocalization = fetchByPrimaryKey(primaryKey);

			if (groupLocalization != null) {
				map.put(primaryKey, groupLocalization);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = entityCache.getResult(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
					GroupLocalizationImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(primaryKey, (GroupLocalization)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_GROUPLOCALIZATION_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			query.append((long)primaryKey);

			query.append(StringPool.COMMA);
		}

		query.setIndex(query.index() - 1);

		query.append(StringPool.CLOSE_PARENTHESIS);

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			for (GroupLocalization groupLocalization : (List<GroupLocalization>)q.list()) {
				map.put(groupLocalization.getPrimaryKeyObj(), groupLocalization);

				cacheResult(groupLocalization);

				uncachedPrimaryKeys.remove(groupLocalization.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(GroupLocalizationModelImpl.ENTITY_CACHE_ENABLED,
					GroupLocalizationImpl.class, primaryKey, nullModel);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the group localizations.
	 *
	 * @return the group localizations
	 */
	@Override
	public List<GroupLocalization> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the group localizations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link GroupLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of group localizations
	 * @param end the upper bound of the range of group localizations (not inclusive)
	 * @return the range of group localizations
	 */
	@Override
	public List<GroupLocalization> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the group localizations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link GroupLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of group localizations
	 * @param end the upper bound of the range of group localizations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of group localizations
	 */
	@Override
	public List<GroupLocalization> findAll(int start, int end,
		OrderByComparator<GroupLocalization> orderByComparator) {
		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the group localizations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link GroupLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of group localizations
	 * @param end the upper bound of the range of group localizations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of group localizations
	 */
	@Override
	public List<GroupLocalization> findAll(int start, int end,
		OrderByComparator<GroupLocalization> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<GroupLocalization> list = null;

		if (retrieveFromCache) {
			list = (List<GroupLocalization>)finderCache.getResult(finderPath,
					finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_GROUPLOCALIZATION);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_GROUPLOCALIZATION;

				if (pagination) {
					sql = sql.concat(GroupLocalizationModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<GroupLocalization>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<GroupLocalization>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the group localizations from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (GroupLocalization groupLocalization : findAll()) {
			remove(groupLocalization);
		}
	}

	/**
	 * Returns the number of group localizations.
	 *
	 * @return the number of group localizations
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_GROUPLOCALIZATION);

				count = (Long)q.uniqueResult();

				finderCache.putResult(FINDER_PATH_COUNT_ALL, FINDER_ARGS_EMPTY,
					count);
			}
			catch (Exception e) {
				finderCache.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return GroupLocalizationModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the group localization persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		entityCache.removeCache(GroupLocalizationImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@BeanReference(type = CompanyProviderWrapper.class)
	protected CompanyProvider companyProvider;
	protected EntityCache entityCache = EntityCacheUtil.getEntityCache();
	protected FinderCache finderCache = FinderCacheUtil.getFinderCache();
	private static final String _SQL_SELECT_GROUPLOCALIZATION = "SELECT groupLocalization FROM GroupLocalization groupLocalization";
	private static final String _SQL_SELECT_GROUPLOCALIZATION_WHERE_PKS_IN = "SELECT groupLocalization FROM GroupLocalization groupLocalization WHERE groupLocalizationId IN (";
	private static final String _SQL_SELECT_GROUPLOCALIZATION_WHERE = "SELECT groupLocalization FROM GroupLocalization groupLocalization WHERE ";
	private static final String _SQL_COUNT_GROUPLOCALIZATION = "SELECT COUNT(groupLocalization) FROM GroupLocalization groupLocalization";
	private static final String _SQL_COUNT_GROUPLOCALIZATION_WHERE = "SELECT COUNT(groupLocalization) FROM GroupLocalization groupLocalization WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "groupLocalization.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No GroupLocalization exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No GroupLocalization exists with the key {";
	private static final Log _log = LogFactoryUtil.getLog(GroupLocalizationPersistenceImpl.class);
}
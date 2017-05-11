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
import com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ABCTestEntityLocalization;
import com.liferay.portal.kernel.service.persistence.ABCTestEntityLocalizationPersistence;
import com.liferay.portal.kernel.service.persistence.CompanyProvider;
import com.liferay.portal.kernel.service.persistence.CompanyProviderWrapper;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.impl.ABCTestEntityLocalizationImpl;
import com.liferay.portal.model.impl.ABCTestEntityLocalizationModelImpl;

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
 * The persistence implementation for the abc test entity localization service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ABCTestEntityLocalizationPersistence
 * @see com.liferay.portal.kernel.service.persistence.ABCTestEntityLocalizationUtil
 * @generated
 */
@ProviderType
public class ABCTestEntityLocalizationPersistenceImpl
	extends BasePersistenceImpl<ABCTestEntityLocalization>
	implements ABCTestEntityLocalizationPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link ABCTestEntityLocalizationUtil} to access the abc test entity localization persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = ABCTestEntityLocalizationImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationModelImpl.FINDER_CACHE_ENABLED,
			ABCTestEntityLocalizationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationModelImpl.FINDER_CACHE_ENABLED,
			ABCTestEntityLocalizationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationModelImpl.FINDER_CACHE_ENABLED,
			Long.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ABCTESTENTITYID =
		new FinderPath(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationModelImpl.FINDER_CACHE_ENABLED,
			ABCTestEntityLocalizationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByAbcTestEntityId",
			new String[] {
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ABCTESTENTITYID =
		new FinderPath(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationModelImpl.FINDER_CACHE_ENABLED,
			ABCTestEntityLocalizationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByAbcTestEntityId",
			new String[] { String.class.getName() },
			ABCTestEntityLocalizationModelImpl.ABCTESTENTITYID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_ABCTESTENTITYID = new FinderPath(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationModelImpl.FINDER_CACHE_ENABLED,
			Long.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByAbcTestEntityId", new String[] { String.class.getName() });

	/**
	 * Returns all the abc test entity localizations where abcTestEntityId = &#63;.
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @return the matching abc test entity localizations
	 */
	@Override
	public List<ABCTestEntityLocalization> findByAbcTestEntityId(
		String abcTestEntityId) {
		return findByAbcTestEntityId(abcTestEntityId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the abc test entity localizations where abcTestEntityId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @param start the lower bound of the range of abc test entity localizations
	 * @param end the upper bound of the range of abc test entity localizations (not inclusive)
	 * @return the range of matching abc test entity localizations
	 */
	@Override
	public List<ABCTestEntityLocalization> findByAbcTestEntityId(
		String abcTestEntityId, int start, int end) {
		return findByAbcTestEntityId(abcTestEntityId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the abc test entity localizations where abcTestEntityId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @param start the lower bound of the range of abc test entity localizations
	 * @param end the upper bound of the range of abc test entity localizations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching abc test entity localizations
	 */
	@Override
	public List<ABCTestEntityLocalization> findByAbcTestEntityId(
		String abcTestEntityId, int start, int end,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		return findByAbcTestEntityId(abcTestEntityId, start, end,
			orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the abc test entity localizations where abcTestEntityId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @param start the lower bound of the range of abc test entity localizations
	 * @param end the upper bound of the range of abc test entity localizations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching abc test entity localizations
	 */
	@Override
	public List<ABCTestEntityLocalization> findByAbcTestEntityId(
		String abcTestEntityId, int start, int end,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ABCTESTENTITYID;
			finderArgs = new Object[] { abcTestEntityId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ABCTESTENTITYID;
			finderArgs = new Object[] {
					abcTestEntityId,
					
					start, end, orderByComparator
				};
		}

		List<ABCTestEntityLocalization> list = null;

		if (retrieveFromCache) {
			list = (List<ABCTestEntityLocalization>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (ABCTestEntityLocalization abcTestEntityLocalization : list) {
					if (!Objects.equals(abcTestEntityId,
								abcTestEntityLocalization.getAbcTestEntityId())) {
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

			query.append(_SQL_SELECT_ABCTESTENTITYLOCALIZATION_WHERE);

			boolean bindAbcTestEntityId = false;

			if (abcTestEntityId == null) {
				query.append(_FINDER_COLUMN_ABCTESTENTITYID_ABCTESTENTITYID_1);
			}
			else if (abcTestEntityId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_ABCTESTENTITYID_ABCTESTENTITYID_3);
			}
			else {
				bindAbcTestEntityId = true;

				query.append(_FINDER_COLUMN_ABCTESTENTITYID_ABCTESTENTITYID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(ABCTestEntityLocalizationModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindAbcTestEntityId) {
					qPos.add(abcTestEntityId);
				}

				if (!pagination) {
					list = (List<ABCTestEntityLocalization>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<ABCTestEntityLocalization>)QueryUtil.list(q,
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
	 * Returns the first abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching abc test entity localization
	 * @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization findByAbcTestEntityId_First(
		String abcTestEntityId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws NoSuchABCTestEntityLocalizationException {
		ABCTestEntityLocalization abcTestEntityLocalization = fetchByAbcTestEntityId_First(abcTestEntityId,
				orderByComparator);

		if (abcTestEntityLocalization != null) {
			return abcTestEntityLocalization;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("abcTestEntityId=");
		msg.append(abcTestEntityId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchABCTestEntityLocalizationException(msg.toString());
	}

	/**
	 * Returns the first abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization fetchByAbcTestEntityId_First(
		String abcTestEntityId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		List<ABCTestEntityLocalization> list = findByAbcTestEntityId(abcTestEntityId,
				0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching abc test entity localization
	 * @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization findByAbcTestEntityId_Last(
		String abcTestEntityId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws NoSuchABCTestEntityLocalizationException {
		ABCTestEntityLocalization abcTestEntityLocalization = fetchByAbcTestEntityId_Last(abcTestEntityId,
				orderByComparator);

		if (abcTestEntityLocalization != null) {
			return abcTestEntityLocalization;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("abcTestEntityId=");
		msg.append(abcTestEntityId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchABCTestEntityLocalizationException(msg.toString());
	}

	/**
	 * Returns the last abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization fetchByAbcTestEntityId_Last(
		String abcTestEntityId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		int count = countByAbcTestEntityId(abcTestEntityId);

		if (count == 0) {
			return null;
		}

		List<ABCTestEntityLocalization> list = findByAbcTestEntityId(abcTestEntityId,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the abc test entity localizations before and after the current abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	 *
	 * @param abcTestEntityLocalizationId the primary key of the current abc test entity localization
	 * @param abcTestEntityId the abc test entity ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next abc test entity localization
	 * @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	 */
	@Override
	public ABCTestEntityLocalization[] findByAbcTestEntityId_PrevAndNext(
		long abcTestEntityLocalizationId, String abcTestEntityId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws NoSuchABCTestEntityLocalizationException {
		ABCTestEntityLocalization abcTestEntityLocalization = findByPrimaryKey(abcTestEntityLocalizationId);

		Session session = null;

		try {
			session = openSession();

			ABCTestEntityLocalization[] array = new ABCTestEntityLocalizationImpl[3];

			array[0] = getByAbcTestEntityId_PrevAndNext(session,
					abcTestEntityLocalization, abcTestEntityId,
					orderByComparator, true);

			array[1] = abcTestEntityLocalization;

			array[2] = getByAbcTestEntityId_PrevAndNext(session,
					abcTestEntityLocalization, abcTestEntityId,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected ABCTestEntityLocalization getByAbcTestEntityId_PrevAndNext(
		Session session, ABCTestEntityLocalization abcTestEntityLocalization,
		String abcTestEntityId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(4 +
					(orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ABCTESTENTITYLOCALIZATION_WHERE);

		boolean bindAbcTestEntityId = false;

		if (abcTestEntityId == null) {
			query.append(_FINDER_COLUMN_ABCTESTENTITYID_ABCTESTENTITYID_1);
		}
		else if (abcTestEntityId.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_ABCTESTENTITYID_ABCTESTENTITYID_3);
		}
		else {
			bindAbcTestEntityId = true;

			query.append(_FINDER_COLUMN_ABCTESTENTITYID_ABCTESTENTITYID_2);
		}

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
			query.append(ABCTestEntityLocalizationModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindAbcTestEntityId) {
			qPos.add(abcTestEntityId);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(abcTestEntityLocalization);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<ABCTestEntityLocalization> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the abc test entity localizations where abcTestEntityId = &#63; from the database.
	 *
	 * @param abcTestEntityId the abc test entity ID
	 */
	@Override
	public void removeByAbcTestEntityId(String abcTestEntityId) {
		for (ABCTestEntityLocalization abcTestEntityLocalization : findByAbcTestEntityId(
				abcTestEntityId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(abcTestEntityLocalization);
		}
	}

	/**
	 * Returns the number of abc test entity localizations where abcTestEntityId = &#63;.
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @return the number of matching abc test entity localizations
	 */
	@Override
	public int countByAbcTestEntityId(String abcTestEntityId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_ABCTESTENTITYID;

		Object[] finderArgs = new Object[] { abcTestEntityId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_ABCTESTENTITYLOCALIZATION_WHERE);

			boolean bindAbcTestEntityId = false;

			if (abcTestEntityId == null) {
				query.append(_FINDER_COLUMN_ABCTESTENTITYID_ABCTESTENTITYID_1);
			}
			else if (abcTestEntityId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_ABCTESTENTITYID_ABCTESTENTITYID_3);
			}
			else {
				bindAbcTestEntityId = true;

				query.append(_FINDER_COLUMN_ABCTESTENTITYID_ABCTESTENTITYID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindAbcTestEntityId) {
					qPos.add(abcTestEntityId);
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

	private static final String _FINDER_COLUMN_ABCTESTENTITYID_ABCTESTENTITYID_1 =
		"abcTestEntityLocalization.abcTestEntityId IS NULL";
	private static final String _FINDER_COLUMN_ABCTESTENTITYID_ABCTESTENTITYID_2 =
		"abcTestEntityLocalization.abcTestEntityId = ?";
	private static final String _FINDER_COLUMN_ABCTESTENTITYID_ABCTESTENTITYID_3 =
		"(abcTestEntityLocalization.abcTestEntityId IS NULL OR abcTestEntityLocalization.abcTestEntityId = '')";
	public static final FinderPath FINDER_PATH_FETCH_BY_ABCTESTENTITYID_LANGUAGEID =
		new FinderPath(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationModelImpl.FINDER_CACHE_ENABLED,
			ABCTestEntityLocalizationImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByAbcTestEntityId_LanguageId",
			new String[] { String.class.getName(), String.class.getName() },
			ABCTestEntityLocalizationModelImpl.ABCTESTENTITYID_COLUMN_BITMASK |
			ABCTestEntityLocalizationModelImpl.LANGUAGEID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_ABCTESTENTITYID_LANGUAGEID =
		new FinderPath(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationModelImpl.FINDER_CACHE_ENABLED,
			Long.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByAbcTestEntityId_LanguageId",
			new String[] { String.class.getName(), String.class.getName() });

	/**
	 * Returns the abc test entity localization where abcTestEntityId = &#63; and languageId = &#63; or throws a {@link NoSuchABCTestEntityLocalizationException} if it could not be found.
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @param languageId the language ID
	 * @return the matching abc test entity localization
	 * @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization findByAbcTestEntityId_LanguageId(
		String abcTestEntityId, String languageId)
		throws NoSuchABCTestEntityLocalizationException {
		ABCTestEntityLocalization abcTestEntityLocalization = fetchByAbcTestEntityId_LanguageId(abcTestEntityId,
				languageId);

		if (abcTestEntityLocalization == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("abcTestEntityId=");
			msg.append(abcTestEntityId);

			msg.append(", languageId=");
			msg.append(languageId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isDebugEnabled()) {
				_log.debug(msg.toString());
			}

			throw new NoSuchABCTestEntityLocalizationException(msg.toString());
		}

		return abcTestEntityLocalization;
	}

	/**
	 * Returns the abc test entity localization where abcTestEntityId = &#63; and languageId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @param languageId the language ID
	 * @return the matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization fetchByAbcTestEntityId_LanguageId(
		String abcTestEntityId, String languageId) {
		return fetchByAbcTestEntityId_LanguageId(abcTestEntityId, languageId,
			true);
	}

	/**
	 * Returns the abc test entity localization where abcTestEntityId = &#63; and languageId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @param languageId the language ID
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization fetchByAbcTestEntityId_LanguageId(
		String abcTestEntityId, String languageId, boolean retrieveFromCache) {
		Object[] finderArgs = new Object[] { abcTestEntityId, languageId };

		Object result = null;

		if (retrieveFromCache) {
			result = finderCache.getResult(FINDER_PATH_FETCH_BY_ABCTESTENTITYID_LANGUAGEID,
					finderArgs, this);
		}

		if (result instanceof ABCTestEntityLocalization) {
			ABCTestEntityLocalization abcTestEntityLocalization = (ABCTestEntityLocalization)result;

			if (!Objects.equals(abcTestEntityId,
						abcTestEntityLocalization.getAbcTestEntityId()) ||
					!Objects.equals(languageId,
						abcTestEntityLocalization.getLanguageId())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_ABCTESTENTITYLOCALIZATION_WHERE);

			boolean bindAbcTestEntityId = false;

			if (abcTestEntityId == null) {
				query.append(_FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_ABCTESTENTITYID_1);
			}
			else if (abcTestEntityId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_ABCTESTENTITYID_3);
			}
			else {
				bindAbcTestEntityId = true;

				query.append(_FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_ABCTESTENTITYID_2);
			}

			boolean bindLanguageId = false;

			if (languageId == null) {
				query.append(_FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_LANGUAGEID_1);
			}
			else if (languageId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_LANGUAGEID_3);
			}
			else {
				bindLanguageId = true;

				query.append(_FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_LANGUAGEID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindAbcTestEntityId) {
					qPos.add(abcTestEntityId);
				}

				if (bindLanguageId) {
					qPos.add(languageId);
				}

				List<ABCTestEntityLocalization> list = q.list();

				if (list.isEmpty()) {
					finderCache.putResult(FINDER_PATH_FETCH_BY_ABCTESTENTITYID_LANGUAGEID,
						finderArgs, list);
				}
				else {
					ABCTestEntityLocalization abcTestEntityLocalization = list.get(0);

					result = abcTestEntityLocalization;

					cacheResult(abcTestEntityLocalization);

					if ((abcTestEntityLocalization.getAbcTestEntityId() == null) ||
							!abcTestEntityLocalization.getAbcTestEntityId()
														  .equals(abcTestEntityId) ||
							(abcTestEntityLocalization.getLanguageId() == null) ||
							!abcTestEntityLocalization.getLanguageId()
														  .equals(languageId)) {
						finderCache.putResult(FINDER_PATH_FETCH_BY_ABCTESTENTITYID_LANGUAGEID,
							finderArgs, abcTestEntityLocalization);
					}
				}
			}
			catch (Exception e) {
				finderCache.removeResult(FINDER_PATH_FETCH_BY_ABCTESTENTITYID_LANGUAGEID,
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
			return (ABCTestEntityLocalization)result;
		}
	}

	/**
	 * Removes the abc test entity localization where abcTestEntityId = &#63; and languageId = &#63; from the database.
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @param languageId the language ID
	 * @return the abc test entity localization that was removed
	 */
	@Override
	public ABCTestEntityLocalization removeByAbcTestEntityId_LanguageId(
		String abcTestEntityId, String languageId)
		throws NoSuchABCTestEntityLocalizationException {
		ABCTestEntityLocalization abcTestEntityLocalization = findByAbcTestEntityId_LanguageId(abcTestEntityId,
				languageId);

		return remove(abcTestEntityLocalization);
	}

	/**
	 * Returns the number of abc test entity localizations where abcTestEntityId = &#63; and languageId = &#63;.
	 *
	 * @param abcTestEntityId the abc test entity ID
	 * @param languageId the language ID
	 * @return the number of matching abc test entity localizations
	 */
	@Override
	public int countByAbcTestEntityId_LanguageId(String abcTestEntityId,
		String languageId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_ABCTESTENTITYID_LANGUAGEID;

		Object[] finderArgs = new Object[] { abcTestEntityId, languageId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_ABCTESTENTITYLOCALIZATION_WHERE);

			boolean bindAbcTestEntityId = false;

			if (abcTestEntityId == null) {
				query.append(_FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_ABCTESTENTITYID_1);
			}
			else if (abcTestEntityId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_ABCTESTENTITYID_3);
			}
			else {
				bindAbcTestEntityId = true;

				query.append(_FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_ABCTESTENTITYID_2);
			}

			boolean bindLanguageId = false;

			if (languageId == null) {
				query.append(_FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_LANGUAGEID_1);
			}
			else if (languageId.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_LANGUAGEID_3);
			}
			else {
				bindLanguageId = true;

				query.append(_FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_LANGUAGEID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindAbcTestEntityId) {
					qPos.add(abcTestEntityId);
				}

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

	private static final String _FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_ABCTESTENTITYID_1 =
		"abcTestEntityLocalization.abcTestEntityId IS NULL AND ";
	private static final String _FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_ABCTESTENTITYID_2 =
		"abcTestEntityLocalization.abcTestEntityId = ? AND ";
	private static final String _FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_ABCTESTENTITYID_3 =
		"(abcTestEntityLocalization.abcTestEntityId IS NULL OR abcTestEntityLocalization.abcTestEntityId = '') AND ";
	private static final String _FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_LANGUAGEID_1 =
		"abcTestEntityLocalization.languageId IS NULL";
	private static final String _FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_LANGUAGEID_2 =
		"abcTestEntityLocalization.languageId = ?";
	private static final String _FINDER_COLUMN_ABCTESTENTITYID_LANGUAGEID_LANGUAGEID_3 =
		"(abcTestEntityLocalization.languageId IS NULL OR abcTestEntityLocalization.languageId = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPID = new FinderPath(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationModelImpl.FINDER_CACHE_ENABLED,
			ABCTestEntityLocalizationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroupId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID =
		new FinderPath(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationModelImpl.FINDER_CACHE_ENABLED,
			ABCTestEntityLocalizationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupId",
			new String[] { Long.class.getName() },
			ABCTestEntityLocalizationModelImpl.GROUPID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_GROUPID = new FinderPath(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationModelImpl.FINDER_CACHE_ENABLED,
			Long.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByGroupId", new String[] { Long.class.getName() });

	/**
	 * Returns all the abc test entity localizations where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching abc test entity localizations
	 */
	@Override
	public List<ABCTestEntityLocalization> findByGroupId(long groupId) {
		return findByGroupId(groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the abc test entity localizations where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of abc test entity localizations
	 * @param end the upper bound of the range of abc test entity localizations (not inclusive)
	 * @return the range of matching abc test entity localizations
	 */
	@Override
	public List<ABCTestEntityLocalization> findByGroupId(long groupId,
		int start, int end) {
		return findByGroupId(groupId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the abc test entity localizations where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of abc test entity localizations
	 * @param end the upper bound of the range of abc test entity localizations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching abc test entity localizations
	 */
	@Override
	public List<ABCTestEntityLocalization> findByGroupId(long groupId,
		int start, int end,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		return findByGroupId(groupId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the abc test entity localizations where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of abc test entity localizations
	 * @param end the upper bound of the range of abc test entity localizations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching abc test entity localizations
	 */
	@Override
	public List<ABCTestEntityLocalization> findByGroupId(long groupId,
		int start, int end,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator,
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

		List<ABCTestEntityLocalization> list = null;

		if (retrieveFromCache) {
			list = (List<ABCTestEntityLocalization>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (ABCTestEntityLocalization abcTestEntityLocalization : list) {
					if ((groupId != abcTestEntityLocalization.getGroupId())) {
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

			query.append(_SQL_SELECT_ABCTESTENTITYLOCALIZATION_WHERE);

			query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(ABCTestEntityLocalizationModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (!pagination) {
					list = (List<ABCTestEntityLocalization>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<ABCTestEntityLocalization>)QueryUtil.list(q,
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
	 * Returns the first abc test entity localization in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching abc test entity localization
	 * @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization findByGroupId_First(long groupId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws NoSuchABCTestEntityLocalizationException {
		ABCTestEntityLocalization abcTestEntityLocalization = fetchByGroupId_First(groupId,
				orderByComparator);

		if (abcTestEntityLocalization != null) {
			return abcTestEntityLocalization;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchABCTestEntityLocalizationException(msg.toString());
	}

	/**
	 * Returns the first abc test entity localization in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization fetchByGroupId_First(long groupId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		List<ABCTestEntityLocalization> list = findByGroupId(groupId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last abc test entity localization in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching abc test entity localization
	 * @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization findByGroupId_Last(long groupId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws NoSuchABCTestEntityLocalizationException {
		ABCTestEntityLocalization abcTestEntityLocalization = fetchByGroupId_Last(groupId,
				orderByComparator);

		if (abcTestEntityLocalization != null) {
			return abcTestEntityLocalization;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchABCTestEntityLocalizationException(msg.toString());
	}

	/**
	 * Returns the last abc test entity localization in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization fetchByGroupId_Last(long groupId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		int count = countByGroupId(groupId);

		if (count == 0) {
			return null;
		}

		List<ABCTestEntityLocalization> list = findByGroupId(groupId,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the abc test entity localizations before and after the current abc test entity localization in the ordered set where groupId = &#63;.
	 *
	 * @param abcTestEntityLocalizationId the primary key of the current abc test entity localization
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next abc test entity localization
	 * @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	 */
	@Override
	public ABCTestEntityLocalization[] findByGroupId_PrevAndNext(
		long abcTestEntityLocalizationId, long groupId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws NoSuchABCTestEntityLocalizationException {
		ABCTestEntityLocalization abcTestEntityLocalization = findByPrimaryKey(abcTestEntityLocalizationId);

		Session session = null;

		try {
			session = openSession();

			ABCTestEntityLocalization[] array = new ABCTestEntityLocalizationImpl[3];

			array[0] = getByGroupId_PrevAndNext(session,
					abcTestEntityLocalization, groupId, orderByComparator, true);

			array[1] = abcTestEntityLocalization;

			array[2] = getByGroupId_PrevAndNext(session,
					abcTestEntityLocalization, groupId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected ABCTestEntityLocalization getByGroupId_PrevAndNext(
		Session session, ABCTestEntityLocalization abcTestEntityLocalization,
		long groupId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(4 +
					(orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ABCTESTENTITYLOCALIZATION_WHERE);

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
			query.append(ABCTestEntityLocalizationModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(abcTestEntityLocalization);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<ABCTestEntityLocalization> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the abc test entity localizations where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 */
	@Override
	public void removeByGroupId(long groupId) {
		for (ABCTestEntityLocalization abcTestEntityLocalization : findByGroupId(
				groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(abcTestEntityLocalization);
		}
	}

	/**
	 * Returns the number of abc test entity localizations where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching abc test entity localizations
	 */
	@Override
	public int countByGroupId(long groupId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_GROUPID;

		Object[] finderArgs = new Object[] { groupId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_ABCTESTENTITYLOCALIZATION_WHERE);

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

	private static final String _FINDER_COLUMN_GROUPID_GROUPID_2 = "abcTestEntityLocalization.groupId = ?";
	public static final FinderPath FINDER_PATH_FETCH_BY_G_N = new FinderPath(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationModelImpl.FINDER_CACHE_ENABLED,
			ABCTestEntityLocalizationImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByG_N",
			new String[] { Long.class.getName(), String.class.getName() },
			ABCTestEntityLocalizationModelImpl.GROUPID_COLUMN_BITMASK |
			ABCTestEntityLocalizationModelImpl.NAME_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_N = new FinderPath(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationModelImpl.FINDER_CACHE_ENABLED,
			Long.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByG_N",
			new String[] { Long.class.getName(), String.class.getName() });

	/**
	 * Returns the abc test entity localization where groupId = &#63; and name = &#63; or throws a {@link NoSuchABCTestEntityLocalizationException} if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param name the name
	 * @return the matching abc test entity localization
	 * @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization findByG_N(long groupId, String name)
		throws NoSuchABCTestEntityLocalizationException {
		ABCTestEntityLocalization abcTestEntityLocalization = fetchByG_N(groupId,
				name);

		if (abcTestEntityLocalization == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("groupId=");
			msg.append(groupId);

			msg.append(", name=");
			msg.append(name);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isDebugEnabled()) {
				_log.debug(msg.toString());
			}

			throw new NoSuchABCTestEntityLocalizationException(msg.toString());
		}

		return abcTestEntityLocalization;
	}

	/**
	 * Returns the abc test entity localization where groupId = &#63; and name = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param name the name
	 * @return the matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization fetchByG_N(long groupId, String name) {
		return fetchByG_N(groupId, name, true);
	}

	/**
	 * Returns the abc test entity localization where groupId = &#63; and name = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param name the name
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	 */
	@Override
	public ABCTestEntityLocalization fetchByG_N(long groupId, String name,
		boolean retrieveFromCache) {
		Object[] finderArgs = new Object[] { groupId, name };

		Object result = null;

		if (retrieveFromCache) {
			result = finderCache.getResult(FINDER_PATH_FETCH_BY_G_N,
					finderArgs, this);
		}

		if (result instanceof ABCTestEntityLocalization) {
			ABCTestEntityLocalization abcTestEntityLocalization = (ABCTestEntityLocalization)result;

			if ((groupId != abcTestEntityLocalization.getGroupId()) ||
					!Objects.equals(name, abcTestEntityLocalization.getName())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_ABCTESTENTITYLOCALIZATION_WHERE);

			query.append(_FINDER_COLUMN_G_N_GROUPID_2);

			boolean bindName = false;

			if (name == null) {
				query.append(_FINDER_COLUMN_G_N_NAME_1);
			}
			else if (name.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_N_NAME_3);
			}
			else {
				bindName = true;

				query.append(_FINDER_COLUMN_G_N_NAME_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindName) {
					qPos.add(name);
				}

				List<ABCTestEntityLocalization> list = q.list();

				if (list.isEmpty()) {
					finderCache.putResult(FINDER_PATH_FETCH_BY_G_N, finderArgs,
						list);
				}
				else {
					ABCTestEntityLocalization abcTestEntityLocalization = list.get(0);

					result = abcTestEntityLocalization;

					cacheResult(abcTestEntityLocalization);

					if ((abcTestEntityLocalization.getGroupId() != groupId) ||
							(abcTestEntityLocalization.getName() == null) ||
							!abcTestEntityLocalization.getName().equals(name)) {
						finderCache.putResult(FINDER_PATH_FETCH_BY_G_N,
							finderArgs, abcTestEntityLocalization);
					}
				}
			}
			catch (Exception e) {
				finderCache.removeResult(FINDER_PATH_FETCH_BY_G_N, finderArgs);

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
			return (ABCTestEntityLocalization)result;
		}
	}

	/**
	 * Removes the abc test entity localization where groupId = &#63; and name = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param name the name
	 * @return the abc test entity localization that was removed
	 */
	@Override
	public ABCTestEntityLocalization removeByG_N(long groupId, String name)
		throws NoSuchABCTestEntityLocalizationException {
		ABCTestEntityLocalization abcTestEntityLocalization = findByG_N(groupId,
				name);

		return remove(abcTestEntityLocalization);
	}

	/**
	 * Returns the number of abc test entity localizations where groupId = &#63; and name = &#63;.
	 *
	 * @param groupId the group ID
	 * @param name the name
	 * @return the number of matching abc test entity localizations
	 */
	@Override
	public int countByG_N(long groupId, String name) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_N;

		Object[] finderArgs = new Object[] { groupId, name };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_ABCTESTENTITYLOCALIZATION_WHERE);

			query.append(_FINDER_COLUMN_G_N_GROUPID_2);

			boolean bindName = false;

			if (name == null) {
				query.append(_FINDER_COLUMN_G_N_NAME_1);
			}
			else if (name.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_N_NAME_3);
			}
			else {
				bindName = true;

				query.append(_FINDER_COLUMN_G_N_NAME_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindName) {
					qPos.add(name);
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

	private static final String _FINDER_COLUMN_G_N_GROUPID_2 = "abcTestEntityLocalization.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_N_NAME_1 = "abcTestEntityLocalization.name IS NULL";
	private static final String _FINDER_COLUMN_G_N_NAME_2 = "abcTestEntityLocalization.name = ?";
	private static final String _FINDER_COLUMN_G_N_NAME_3 = "(abcTestEntityLocalization.name IS NULL OR abcTestEntityLocalization.name = '')";

	public ABCTestEntityLocalizationPersistenceImpl() {
		setModelClass(ABCTestEntityLocalization.class);
	}

	/**
	 * Caches the abc test entity localization in the entity cache if it is enabled.
	 *
	 * @param abcTestEntityLocalization the abc test entity localization
	 */
	@Override
	public void cacheResult(ABCTestEntityLocalization abcTestEntityLocalization) {
		entityCache.putResult(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationImpl.class,
			abcTestEntityLocalization.getPrimaryKey(), abcTestEntityLocalization);

		finderCache.putResult(FINDER_PATH_FETCH_BY_ABCTESTENTITYID_LANGUAGEID,
			new Object[] {
				abcTestEntityLocalization.getAbcTestEntityId(),
				abcTestEntityLocalization.getLanguageId()
			}, abcTestEntityLocalization);

		finderCache.putResult(FINDER_PATH_FETCH_BY_G_N,
			new Object[] {
				abcTestEntityLocalization.getGroupId(),
				abcTestEntityLocalization.getName()
			}, abcTestEntityLocalization);

		abcTestEntityLocalization.resetOriginalValues();
	}

	/**
	 * Caches the abc test entity localizations in the entity cache if it is enabled.
	 *
	 * @param abcTestEntityLocalizations the abc test entity localizations
	 */
	@Override
	public void cacheResult(
		List<ABCTestEntityLocalization> abcTestEntityLocalizations) {
		for (ABCTestEntityLocalization abcTestEntityLocalization : abcTestEntityLocalizations) {
			if (entityCache.getResult(
						ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
						ABCTestEntityLocalizationImpl.class,
						abcTestEntityLocalization.getPrimaryKey()) == null) {
				cacheResult(abcTestEntityLocalization);
			}
			else {
				abcTestEntityLocalization.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all abc test entity localizations.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(ABCTestEntityLocalizationImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the abc test entity localization.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(ABCTestEntityLocalization abcTestEntityLocalization) {
		entityCache.removeResult(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationImpl.class,
			abcTestEntityLocalization.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache((ABCTestEntityLocalizationModelImpl)abcTestEntityLocalization,
			true);
	}

	@Override
	public void clearCache(
		List<ABCTestEntityLocalization> abcTestEntityLocalizations) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (ABCTestEntityLocalization abcTestEntityLocalization : abcTestEntityLocalizations) {
			entityCache.removeResult(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
				ABCTestEntityLocalizationImpl.class,
				abcTestEntityLocalization.getPrimaryKey());

			clearUniqueFindersCache((ABCTestEntityLocalizationModelImpl)abcTestEntityLocalization,
				true);
		}
	}

	protected void cacheUniqueFindersCache(
		ABCTestEntityLocalizationModelImpl abcTestEntityLocalizationModelImpl) {
		Object[] args = new Object[] {
				abcTestEntityLocalizationModelImpl.getAbcTestEntityId(),
				abcTestEntityLocalizationModelImpl.getLanguageId()
			};

		finderCache.putResult(FINDER_PATH_COUNT_BY_ABCTESTENTITYID_LANGUAGEID,
			args, Long.valueOf(1), false);
		finderCache.putResult(FINDER_PATH_FETCH_BY_ABCTESTENTITYID_LANGUAGEID,
			args, abcTestEntityLocalizationModelImpl, false);

		args = new Object[] {
				abcTestEntityLocalizationModelImpl.getGroupId(),
				abcTestEntityLocalizationModelImpl.getName()
			};

		finderCache.putResult(FINDER_PATH_COUNT_BY_G_N, args, Long.valueOf(1),
			false);
		finderCache.putResult(FINDER_PATH_FETCH_BY_G_N, args,
			abcTestEntityLocalizationModelImpl, false);
	}

	protected void clearUniqueFindersCache(
		ABCTestEntityLocalizationModelImpl abcTestEntityLocalizationModelImpl,
		boolean clearCurrent) {
		if (clearCurrent) {
			Object[] args = new Object[] {
					abcTestEntityLocalizationModelImpl.getAbcTestEntityId(),
					abcTestEntityLocalizationModelImpl.getLanguageId()
				};

			finderCache.removeResult(FINDER_PATH_COUNT_BY_ABCTESTENTITYID_LANGUAGEID,
				args);
			finderCache.removeResult(FINDER_PATH_FETCH_BY_ABCTESTENTITYID_LANGUAGEID,
				args);
		}

		if ((abcTestEntityLocalizationModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_ABCTESTENTITYID_LANGUAGEID.getColumnBitmask()) != 0) {
			Object[] args = new Object[] {
					abcTestEntityLocalizationModelImpl.getOriginalAbcTestEntityId(),
					abcTestEntityLocalizationModelImpl.getOriginalLanguageId()
				};

			finderCache.removeResult(FINDER_PATH_COUNT_BY_ABCTESTENTITYID_LANGUAGEID,
				args);
			finderCache.removeResult(FINDER_PATH_FETCH_BY_ABCTESTENTITYID_LANGUAGEID,
				args);
		}

		if (clearCurrent) {
			Object[] args = new Object[] {
					abcTestEntityLocalizationModelImpl.getGroupId(),
					abcTestEntityLocalizationModelImpl.getName()
				};

			finderCache.removeResult(FINDER_PATH_COUNT_BY_G_N, args);
			finderCache.removeResult(FINDER_PATH_FETCH_BY_G_N, args);
		}

		if ((abcTestEntityLocalizationModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_G_N.getColumnBitmask()) != 0) {
			Object[] args = new Object[] {
					abcTestEntityLocalizationModelImpl.getOriginalGroupId(),
					abcTestEntityLocalizationModelImpl.getOriginalName()
				};

			finderCache.removeResult(FINDER_PATH_COUNT_BY_G_N, args);
			finderCache.removeResult(FINDER_PATH_FETCH_BY_G_N, args);
		}
	}

	/**
	 * Creates a new abc test entity localization with the primary key. Does not add the abc test entity localization to the database.
	 *
	 * @param abcTestEntityLocalizationId the primary key for the new abc test entity localization
	 * @return the new abc test entity localization
	 */
	@Override
	public ABCTestEntityLocalization create(long abcTestEntityLocalizationId) {
		ABCTestEntityLocalization abcTestEntityLocalization = new ABCTestEntityLocalizationImpl();

		abcTestEntityLocalization.setNew(true);
		abcTestEntityLocalization.setPrimaryKey(abcTestEntityLocalizationId);

		abcTestEntityLocalization.setCompanyId(companyProvider.getCompanyId());

		return abcTestEntityLocalization;
	}

	/**
	 * Removes the abc test entity localization with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param abcTestEntityLocalizationId the primary key of the abc test entity localization
	 * @return the abc test entity localization that was removed
	 * @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	 */
	@Override
	public ABCTestEntityLocalization remove(long abcTestEntityLocalizationId)
		throws NoSuchABCTestEntityLocalizationException {
		return remove((Serializable)abcTestEntityLocalizationId);
	}

	/**
	 * Removes the abc test entity localization with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the abc test entity localization
	 * @return the abc test entity localization that was removed
	 * @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	 */
	@Override
	public ABCTestEntityLocalization remove(Serializable primaryKey)
		throws NoSuchABCTestEntityLocalizationException {
		Session session = null;

		try {
			session = openSession();

			ABCTestEntityLocalization abcTestEntityLocalization = (ABCTestEntityLocalization)session.get(ABCTestEntityLocalizationImpl.class,
					primaryKey);

			if (abcTestEntityLocalization == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchABCTestEntityLocalizationException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(abcTestEntityLocalization);
		}
		catch (NoSuchABCTestEntityLocalizationException nsee) {
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
	protected ABCTestEntityLocalization removeImpl(
		ABCTestEntityLocalization abcTestEntityLocalization) {
		abcTestEntityLocalization = toUnwrappedModel(abcTestEntityLocalization);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(abcTestEntityLocalization)) {
				abcTestEntityLocalization = (ABCTestEntityLocalization)session.get(ABCTestEntityLocalizationImpl.class,
						abcTestEntityLocalization.getPrimaryKeyObj());
			}

			if (abcTestEntityLocalization != null) {
				session.delete(abcTestEntityLocalization);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (abcTestEntityLocalization != null) {
			clearCache(abcTestEntityLocalization);
		}

		return abcTestEntityLocalization;
	}

	@Override
	public ABCTestEntityLocalization updateImpl(
		ABCTestEntityLocalization abcTestEntityLocalization) {
		abcTestEntityLocalization = toUnwrappedModel(abcTestEntityLocalization);

		boolean isNew = abcTestEntityLocalization.isNew();

		ABCTestEntityLocalizationModelImpl abcTestEntityLocalizationModelImpl = (ABCTestEntityLocalizationModelImpl)abcTestEntityLocalization;

		Session session = null;

		try {
			session = openSession();

			if (abcTestEntityLocalization.isNew()) {
				session.save(abcTestEntityLocalization);

				abcTestEntityLocalization.setNew(false);
			}
			else {
				abcTestEntityLocalization = (ABCTestEntityLocalization)session.merge(abcTestEntityLocalization);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!ABCTestEntityLocalizationModelImpl.COLUMN_BITMASK_ENABLED) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else
		 if (isNew) {
			Object[] args = new Object[] {
					abcTestEntityLocalizationModelImpl.getAbcTestEntityId()
				};

			finderCache.removeResult(FINDER_PATH_COUNT_BY_ABCTESTENTITYID, args);
			finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ABCTESTENTITYID,
				args);

			args = new Object[] { abcTestEntityLocalizationModelImpl.getGroupId() };

			finderCache.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
			finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
				args);

			finderCache.removeResult(FINDER_PATH_COUNT_ALL, FINDER_ARGS_EMPTY);
			finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL,
				FINDER_ARGS_EMPTY);
		}

		else {
			if ((abcTestEntityLocalizationModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ABCTESTENTITYID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						abcTestEntityLocalizationModelImpl.getOriginalAbcTestEntityId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_ABCTESTENTITYID,
					args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ABCTESTENTITYID,
					args);

				args = new Object[] {
						abcTestEntityLocalizationModelImpl.getAbcTestEntityId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_ABCTESTENTITYID,
					args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ABCTESTENTITYID,
					args);
			}

			if ((abcTestEntityLocalizationModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						abcTestEntityLocalizationModelImpl.getOriginalGroupId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
					args);

				args = new Object[] {
						abcTestEntityLocalizationModelImpl.getGroupId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
					args);
			}
		}

		entityCache.putResult(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityLocalizationImpl.class,
			abcTestEntityLocalization.getPrimaryKey(),
			abcTestEntityLocalization, false);

		clearUniqueFindersCache(abcTestEntityLocalizationModelImpl, false);
		cacheUniqueFindersCache(abcTestEntityLocalizationModelImpl);

		abcTestEntityLocalization.resetOriginalValues();

		return abcTestEntityLocalization;
	}

	protected ABCTestEntityLocalization toUnwrappedModel(
		ABCTestEntityLocalization abcTestEntityLocalization) {
		if (abcTestEntityLocalization instanceof ABCTestEntityLocalizationImpl) {
			return abcTestEntityLocalization;
		}

		ABCTestEntityLocalizationImpl abcTestEntityLocalizationImpl = new ABCTestEntityLocalizationImpl();

		abcTestEntityLocalizationImpl.setNew(abcTestEntityLocalization.isNew());
		abcTestEntityLocalizationImpl.setPrimaryKey(abcTestEntityLocalization.getPrimaryKey());

		abcTestEntityLocalizationImpl.setMvccVersion(abcTestEntityLocalization.getMvccVersion());
		abcTestEntityLocalizationImpl.setAbcTestEntityLocalizationId(abcTestEntityLocalization.getAbcTestEntityLocalizationId());
		abcTestEntityLocalizationImpl.setCompanyId(abcTestEntityLocalization.getCompanyId());
		abcTestEntityLocalizationImpl.setAbcTestEntityId(abcTestEntityLocalization.getAbcTestEntityId());
		abcTestEntityLocalizationImpl.setLanguageId(abcTestEntityLocalization.getLanguageId());
		abcTestEntityLocalizationImpl.setName(abcTestEntityLocalization.getName());
		abcTestEntityLocalizationImpl.setDescription(abcTestEntityLocalization.getDescription());
		abcTestEntityLocalizationImpl.setGroupId(abcTestEntityLocalization.getGroupId());

		return abcTestEntityLocalizationImpl;
	}

	/**
	 * Returns the abc test entity localization with the primary key or throws a {@link com.liferay.portal.kernel.exception.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the abc test entity localization
	 * @return the abc test entity localization
	 * @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	 */
	@Override
	public ABCTestEntityLocalization findByPrimaryKey(Serializable primaryKey)
		throws NoSuchABCTestEntityLocalizationException {
		ABCTestEntityLocalization abcTestEntityLocalization = fetchByPrimaryKey(primaryKey);

		if (abcTestEntityLocalization == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchABCTestEntityLocalizationException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return abcTestEntityLocalization;
	}

	/**
	 * Returns the abc test entity localization with the primary key or throws a {@link NoSuchABCTestEntityLocalizationException} if it could not be found.
	 *
	 * @param abcTestEntityLocalizationId the primary key of the abc test entity localization
	 * @return the abc test entity localization
	 * @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	 */
	@Override
	public ABCTestEntityLocalization findByPrimaryKey(
		long abcTestEntityLocalizationId)
		throws NoSuchABCTestEntityLocalizationException {
		return findByPrimaryKey((Serializable)abcTestEntityLocalizationId);
	}

	/**
	 * Returns the abc test entity localization with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the abc test entity localization
	 * @return the abc test entity localization, or <code>null</code> if a abc test entity localization with the primary key could not be found
	 */
	@Override
	public ABCTestEntityLocalization fetchByPrimaryKey(Serializable primaryKey) {
		Serializable serializable = entityCache.getResult(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
				ABCTestEntityLocalizationImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		ABCTestEntityLocalization abcTestEntityLocalization = (ABCTestEntityLocalization)serializable;

		if (abcTestEntityLocalization == null) {
			Session session = null;

			try {
				session = openSession();

				abcTestEntityLocalization = (ABCTestEntityLocalization)session.get(ABCTestEntityLocalizationImpl.class,
						primaryKey);

				if (abcTestEntityLocalization != null) {
					cacheResult(abcTestEntityLocalization);
				}
				else {
					entityCache.putResult(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
						ABCTestEntityLocalizationImpl.class, primaryKey,
						nullModel);
				}
			}
			catch (Exception e) {
				entityCache.removeResult(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
					ABCTestEntityLocalizationImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return abcTestEntityLocalization;
	}

	/**
	 * Returns the abc test entity localization with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param abcTestEntityLocalizationId the primary key of the abc test entity localization
	 * @return the abc test entity localization, or <code>null</code> if a abc test entity localization with the primary key could not be found
	 */
	@Override
	public ABCTestEntityLocalization fetchByPrimaryKey(
		long abcTestEntityLocalizationId) {
		return fetchByPrimaryKey((Serializable)abcTestEntityLocalizationId);
	}

	@Override
	public Map<Serializable, ABCTestEntityLocalization> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, ABCTestEntityLocalization> map = new HashMap<Serializable, ABCTestEntityLocalization>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			ABCTestEntityLocalization abcTestEntityLocalization = fetchByPrimaryKey(primaryKey);

			if (abcTestEntityLocalization != null) {
				map.put(primaryKey, abcTestEntityLocalization);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = entityCache.getResult(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
					ABCTestEntityLocalizationImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(primaryKey, (ABCTestEntityLocalization)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_ABCTESTENTITYLOCALIZATION_WHERE_PKS_IN);

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

			for (ABCTestEntityLocalization abcTestEntityLocalization : (List<ABCTestEntityLocalization>)q.list()) {
				map.put(abcTestEntityLocalization.getPrimaryKeyObj(),
					abcTestEntityLocalization);

				cacheResult(abcTestEntityLocalization);

				uncachedPrimaryKeys.remove(abcTestEntityLocalization.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(ABCTestEntityLocalizationModelImpl.ENTITY_CACHE_ENABLED,
					ABCTestEntityLocalizationImpl.class, primaryKey, nullModel);
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
	 * Returns all the abc test entity localizations.
	 *
	 * @return the abc test entity localizations
	 */
	@Override
	public List<ABCTestEntityLocalization> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the abc test entity localizations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of abc test entity localizations
	 * @param end the upper bound of the range of abc test entity localizations (not inclusive)
	 * @return the range of abc test entity localizations
	 */
	@Override
	public List<ABCTestEntityLocalization> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the abc test entity localizations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of abc test entity localizations
	 * @param end the upper bound of the range of abc test entity localizations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of abc test entity localizations
	 */
	@Override
	public List<ABCTestEntityLocalization> findAll(int start, int end,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the abc test entity localizations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of abc test entity localizations
	 * @param end the upper bound of the range of abc test entity localizations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of abc test entity localizations
	 */
	@Override
	public List<ABCTestEntityLocalization> findAll(int start, int end,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator,
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

		List<ABCTestEntityLocalization> list = null;

		if (retrieveFromCache) {
			list = (List<ABCTestEntityLocalization>)finderCache.getResult(finderPath,
					finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_ABCTESTENTITYLOCALIZATION);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_ABCTESTENTITYLOCALIZATION;

				if (pagination) {
					sql = sql.concat(ABCTestEntityLocalizationModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<ABCTestEntityLocalization>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<ABCTestEntityLocalization>)QueryUtil.list(q,
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
	 * Removes all the abc test entity localizations from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (ABCTestEntityLocalization abcTestEntityLocalization : findAll()) {
			remove(abcTestEntityLocalization);
		}
	}

	/**
	 * Returns the number of abc test entity localizations.
	 *
	 * @return the number of abc test entity localizations
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_ABCTESTENTITYLOCALIZATION);

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
		return ABCTestEntityLocalizationModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the abc test entity localization persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		entityCache.removeCache(ABCTestEntityLocalizationImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@BeanReference(type = CompanyProviderWrapper.class)
	protected CompanyProvider companyProvider;
	protected EntityCache entityCache = EntityCacheUtil.getEntityCache();
	protected FinderCache finderCache = FinderCacheUtil.getFinderCache();
	private static final String _SQL_SELECT_ABCTESTENTITYLOCALIZATION = "SELECT abcTestEntityLocalization FROM ABCTestEntityLocalization abcTestEntityLocalization";
	private static final String _SQL_SELECT_ABCTESTENTITYLOCALIZATION_WHERE_PKS_IN =
		"SELECT abcTestEntityLocalization FROM ABCTestEntityLocalization abcTestEntityLocalization WHERE abcTestEntityLocalizationId IN (";
	private static final String _SQL_SELECT_ABCTESTENTITYLOCALIZATION_WHERE = "SELECT abcTestEntityLocalization FROM ABCTestEntityLocalization abcTestEntityLocalization WHERE ";
	private static final String _SQL_COUNT_ABCTESTENTITYLOCALIZATION = "SELECT COUNT(abcTestEntityLocalization) FROM ABCTestEntityLocalization abcTestEntityLocalization";
	private static final String _SQL_COUNT_ABCTESTENTITYLOCALIZATION_WHERE = "SELECT COUNT(abcTestEntityLocalization) FROM ABCTestEntityLocalization abcTestEntityLocalization WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "abcTestEntityLocalization.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No ABCTestEntityLocalization exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No ABCTestEntityLocalization exists with the key {";
	private static final Log _log = LogFactoryUtil.getLog(ABCTestEntityLocalizationPersistenceImpl.class);
}
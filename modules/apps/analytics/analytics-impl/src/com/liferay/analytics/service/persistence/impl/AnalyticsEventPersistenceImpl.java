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

package com.liferay.analytics.service.persistence.impl;

import com.liferay.analytics.NoSuchEventException;
import com.liferay.analytics.model.AnalyticsEvent;
import com.liferay.analytics.model.impl.AnalyticsEventImpl;
import com.liferay.analytics.model.impl.AnalyticsEventModelImpl;
import com.liferay.analytics.service.persistence.AnalyticsEventPersistence;

import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.io.Serializable;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence implementation for the analytics event service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AnalyticsEventPersistence
 * @see AnalyticsEventUtil
 * @generated
 */
public class AnalyticsEventPersistenceImpl extends BasePersistenceImpl<AnalyticsEvent>
	implements AnalyticsEventPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link AnalyticsEventUtil} to access the analytics event persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = AnalyticsEventImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED,
			AnalyticsEventImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED,
			AnalyticsEventImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_COMPANYID =
		new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED,
			AnalyticsEventImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByCompanyId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID =
		new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED,
			AnalyticsEventImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCompanyId",
			new String[] { Long.class.getName() },
			AnalyticsEventModelImpl.COMPANYID_COLUMN_BITMASK |
			AnalyticsEventModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_COMPANYID = new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCompanyId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the analytics events where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByCompanyId(long companyId) {
		return findByCompanyId(companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the analytics events where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @return the range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByCompanyId(long companyId, int start,
		int end) {
		return findByCompanyId(companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analytics events where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByCompanyId(long companyId, int start,
		int end, OrderByComparator<AnalyticsEvent> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID;
			finderArgs = new Object[] { companyId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_COMPANYID;
			finderArgs = new Object[] { companyId, start, end, orderByComparator };
		}

		List<AnalyticsEvent> list = (List<AnalyticsEvent>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (AnalyticsEvent analyticsEvent : list) {
				if ((companyId != analyticsEvent.getCompanyId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

			query.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				if (!pagination) {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analytics event in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByCompanyId_First(long companyId,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByCompanyId_First(companyId,
				orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the first analytics event in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByCompanyId_First(long companyId,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		List<AnalyticsEvent> list = findByCompanyId(companyId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analytics event in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByCompanyId_Last(long companyId,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByCompanyId_Last(companyId,
				orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the last analytics event in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByCompanyId_Last(long companyId,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		int count = countByCompanyId(companyId);

		if (count == 0) {
			return null;
		}

		List<AnalyticsEvent> list = findByCompanyId(companyId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the analytics events before and after the current analytics event in the ordered set where companyId = &#63;.
	 *
	 * @param analyticsEventId the primary key of the current analytics event
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	 */
	@Override
	public AnalyticsEvent[] findByCompanyId_PrevAndNext(long analyticsEventId,
		long companyId, OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = findByPrimaryKey(analyticsEventId);

		Session session = null;

		try {
			session = openSession();

			AnalyticsEvent[] array = new AnalyticsEventImpl[3];

			array[0] = getByCompanyId_PrevAndNext(session, analyticsEvent,
					companyId, orderByComparator, true);

			array[1] = analyticsEvent;

			array[2] = getByCompanyId_PrevAndNext(session, analyticsEvent,
					companyId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AnalyticsEvent getByCompanyId_PrevAndNext(Session session,
		AnalyticsEvent analyticsEvent, long companyId,
		OrderByComparator<AnalyticsEvent> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

		query.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

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
			query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(companyId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(analyticsEvent);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AnalyticsEvent> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the analytics events where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	@Override
	public void removeByCompanyId(long companyId) {
		for (AnalyticsEvent analyticsEvent : findByCompanyId(companyId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analyticsEvent);
		}
	}

	/**
	 * Returns the number of analytics events where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching analytics events
	 */
	@Override
	public int countByCompanyId(long companyId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_COMPANYID;

		Object[] finderArgs = new Object[] { companyId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_ANALYTICSEVENT_WHERE);

			query.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_COMPANYID_COMPANYID_2 = "analyticsEvent.companyId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_C_GTCD = new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED,
			AnalyticsEventImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByC_GtCD",
			new String[] {
				Long.class.getName(), Date.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_C_GTCD = new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByC_GtCD",
			new String[] { Long.class.getName(), Date.class.getName() });

	/**
	 * Returns all the analytics events where companyId = &#63; and createDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @return the matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByC_GtCD(long companyId, Date createDate) {
		return findByC_GtCD(companyId, createDate, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analytics events where companyId = &#63; and createDate &gt; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @return the range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByC_GtCD(long companyId, Date createDate,
		int start, int end) {
		return findByC_GtCD(companyId, createDate, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analytics events where companyId = &#63; and createDate &gt; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByC_GtCD(long companyId, Date createDate,
		int start, int end, OrderByComparator<AnalyticsEvent> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_C_GTCD;
		finderArgs = new Object[] {
				companyId, createDate,
				
				start, end, orderByComparator
			};

		List<AnalyticsEvent> list = (List<AnalyticsEvent>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (AnalyticsEvent analyticsEvent : list) {
				if ((companyId != analyticsEvent.getCompanyId()) ||
						(createDate.getTime() >= analyticsEvent.getCreateDate()
																   .getTime())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

			query.append(_FINDER_COLUMN_C_GTCD_COMPANYID_2);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_C_GTCD_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_C_GTCD_CREATEDATE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				if (!pagination) {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analytics event in the ordered set where companyId = &#63; and createDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByC_GtCD_First(long companyId, Date createDate,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByC_GtCD_First(companyId,
				createDate, orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", createDate=");
		msg.append(createDate);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the first analytics event in the ordered set where companyId = &#63; and createDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByC_GtCD_First(long companyId, Date createDate,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		List<AnalyticsEvent> list = findByC_GtCD(companyId, createDate, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analytics event in the ordered set where companyId = &#63; and createDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByC_GtCD_Last(long companyId, Date createDate,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByC_GtCD_Last(companyId,
				createDate, orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", createDate=");
		msg.append(createDate);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the last analytics event in the ordered set where companyId = &#63; and createDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByC_GtCD_Last(long companyId, Date createDate,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		int count = countByC_GtCD(companyId, createDate);

		if (count == 0) {
			return null;
		}

		List<AnalyticsEvent> list = findByC_GtCD(companyId, createDate,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the analytics events before and after the current analytics event in the ordered set where companyId = &#63; and createDate &gt; &#63;.
	 *
	 * @param analyticsEventId the primary key of the current analytics event
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	 */
	@Override
	public AnalyticsEvent[] findByC_GtCD_PrevAndNext(long analyticsEventId,
		long companyId, Date createDate,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = findByPrimaryKey(analyticsEventId);

		Session session = null;

		try {
			session = openSession();

			AnalyticsEvent[] array = new AnalyticsEventImpl[3];

			array[0] = getByC_GtCD_PrevAndNext(session, analyticsEvent,
					companyId, createDate, orderByComparator, true);

			array[1] = analyticsEvent;

			array[2] = getByC_GtCD_PrevAndNext(session, analyticsEvent,
					companyId, createDate, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AnalyticsEvent getByC_GtCD_PrevAndNext(Session session,
		AnalyticsEvent analyticsEvent, long companyId, Date createDate,
		OrderByComparator<AnalyticsEvent> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

		query.append(_FINDER_COLUMN_C_GTCD_COMPANYID_2);

		boolean bindCreateDate = false;

		if (createDate == null) {
			query.append(_FINDER_COLUMN_C_GTCD_CREATEDATE_1);
		}
		else {
			bindCreateDate = true;

			query.append(_FINDER_COLUMN_C_GTCD_CREATEDATE_2);
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
			query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(companyId);

		if (bindCreateDate) {
			qPos.add(new Timestamp(createDate.getTime()));
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(analyticsEvent);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AnalyticsEvent> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the analytics events where companyId = &#63; and createDate &gt; &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 */
	@Override
	public void removeByC_GtCD(long companyId, Date createDate) {
		for (AnalyticsEvent analyticsEvent : findByC_GtCD(companyId,
				createDate, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analyticsEvent);
		}
	}

	/**
	 * Returns the number of analytics events where companyId = &#63; and createDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @return the number of matching analytics events
	 */
	@Override
	public int countByC_GtCD(long companyId, Date createDate) {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_COUNT_BY_C_GTCD;

		Object[] finderArgs = new Object[] { companyId, createDate };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_ANALYTICSEVENT_WHERE);

			query.append(_FINDER_COLUMN_C_GTCD_COMPANYID_2);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_C_GTCD_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_C_GTCD_CREATEDATE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_C_GTCD_COMPANYID_2 = "analyticsEvent.companyId = ? AND ";
	private static final String _FINDER_COLUMN_C_GTCD_CREATEDATE_1 = "analyticsEvent.createDate > NULL";
	private static final String _FINDER_COLUMN_C_GTCD_CREATEDATE_2 = "analyticsEvent.createDate > ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_C_LTCD = new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED,
			AnalyticsEventImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByC_LtCD",
			new String[] {
				Long.class.getName(), Date.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_C_LTCD = new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByC_LtCD",
			new String[] { Long.class.getName(), Date.class.getName() });

	/**
	 * Returns all the analytics events where companyId = &#63; and createDate &lt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @return the matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByC_LtCD(long companyId, Date createDate) {
		return findByC_LtCD(companyId, createDate, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analytics events where companyId = &#63; and createDate &lt; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @return the range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByC_LtCD(long companyId, Date createDate,
		int start, int end) {
		return findByC_LtCD(companyId, createDate, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analytics events where companyId = &#63; and createDate &lt; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByC_LtCD(long companyId, Date createDate,
		int start, int end, OrderByComparator<AnalyticsEvent> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_C_LTCD;
		finderArgs = new Object[] {
				companyId, createDate,
				
				start, end, orderByComparator
			};

		List<AnalyticsEvent> list = (List<AnalyticsEvent>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (AnalyticsEvent analyticsEvent : list) {
				if ((companyId != analyticsEvent.getCompanyId()) ||
						(createDate.getTime() <= analyticsEvent.getCreateDate()
																   .getTime())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

			query.append(_FINDER_COLUMN_C_LTCD_COMPANYID_2);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_C_LTCD_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_C_LTCD_CREATEDATE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				if (!pagination) {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analytics event in the ordered set where companyId = &#63; and createDate &lt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByC_LtCD_First(long companyId, Date createDate,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByC_LtCD_First(companyId,
				createDate, orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", createDate=");
		msg.append(createDate);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the first analytics event in the ordered set where companyId = &#63; and createDate &lt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByC_LtCD_First(long companyId, Date createDate,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		List<AnalyticsEvent> list = findByC_LtCD(companyId, createDate, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analytics event in the ordered set where companyId = &#63; and createDate &lt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByC_LtCD_Last(long companyId, Date createDate,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByC_LtCD_Last(companyId,
				createDate, orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", createDate=");
		msg.append(createDate);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the last analytics event in the ordered set where companyId = &#63; and createDate &lt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByC_LtCD_Last(long companyId, Date createDate,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		int count = countByC_LtCD(companyId, createDate);

		if (count == 0) {
			return null;
		}

		List<AnalyticsEvent> list = findByC_LtCD(companyId, createDate,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the analytics events before and after the current analytics event in the ordered set where companyId = &#63; and createDate &lt; &#63;.
	 *
	 * @param analyticsEventId the primary key of the current analytics event
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	 */
	@Override
	public AnalyticsEvent[] findByC_LtCD_PrevAndNext(long analyticsEventId,
		long companyId, Date createDate,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = findByPrimaryKey(analyticsEventId);

		Session session = null;

		try {
			session = openSession();

			AnalyticsEvent[] array = new AnalyticsEventImpl[3];

			array[0] = getByC_LtCD_PrevAndNext(session, analyticsEvent,
					companyId, createDate, orderByComparator, true);

			array[1] = analyticsEvent;

			array[2] = getByC_LtCD_PrevAndNext(session, analyticsEvent,
					companyId, createDate, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AnalyticsEvent getByC_LtCD_PrevAndNext(Session session,
		AnalyticsEvent analyticsEvent, long companyId, Date createDate,
		OrderByComparator<AnalyticsEvent> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

		query.append(_FINDER_COLUMN_C_LTCD_COMPANYID_2);

		boolean bindCreateDate = false;

		if (createDate == null) {
			query.append(_FINDER_COLUMN_C_LTCD_CREATEDATE_1);
		}
		else {
			bindCreateDate = true;

			query.append(_FINDER_COLUMN_C_LTCD_CREATEDATE_2);
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
			query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(companyId);

		if (bindCreateDate) {
			qPos.add(new Timestamp(createDate.getTime()));
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(analyticsEvent);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AnalyticsEvent> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the analytics events where companyId = &#63; and createDate &lt; &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 */
	@Override
	public void removeByC_LtCD(long companyId, Date createDate) {
		for (AnalyticsEvent analyticsEvent : findByC_LtCD(companyId,
				createDate, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analyticsEvent);
		}
	}

	/**
	 * Returns the number of analytics events where companyId = &#63; and createDate &lt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param createDate the create date
	 * @return the number of matching analytics events
	 */
	@Override
	public int countByC_LtCD(long companyId, Date createDate) {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_COUNT_BY_C_LTCD;

		Object[] finderArgs = new Object[] { companyId, createDate };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_ANALYTICSEVENT_WHERE);

			query.append(_FINDER_COLUMN_C_LTCD_COMPANYID_2);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_C_LTCD_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_C_LTCD_CREATEDATE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_C_LTCD_COMPANYID_2 = "analyticsEvent.companyId = ? AND ";
	private static final String _FINDER_COLUMN_C_LTCD_CREATEDATE_1 = "analyticsEvent.createDate < NULL";
	private static final String _FINDER_COLUMN_C_LTCD_CREATEDATE_2 = "analyticsEvent.createDate < ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_GTCD_E_E = new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED,
			AnalyticsEventImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByGtCD_E_E",
			new String[] {
				Date.class.getName(), String.class.getName(),
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_GTCD_E_E =
		new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByGtCD_E_E",
			new String[] {
				Date.class.getName(), String.class.getName(),
				String.class.getName()
			});

	/**
	 * Returns all the analytics events where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param elementKey the element key
	 * @param type the type
	 * @return the matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByGtCD_E_E(Date createDate,
		String elementKey, String type) {
		return findByGtCD_E_E(createDate, elementKey, type, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analytics events where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param createDate the create date
	 * @param elementKey the element key
	 * @param type the type
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @return the range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByGtCD_E_E(Date createDate,
		String elementKey, String type, int start, int end) {
		return findByGtCD_E_E(createDate, elementKey, type, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analytics events where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param createDate the create date
	 * @param elementKey the element key
	 * @param type the type
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByGtCD_E_E(Date createDate,
		String elementKey, String type, int start, int end,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_GTCD_E_E;
		finderArgs = new Object[] {
				createDate, elementKey, type,
				
				start, end, orderByComparator
			};

		List<AnalyticsEvent> list = (List<AnalyticsEvent>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (AnalyticsEvent analyticsEvent : list) {
				if ((createDate.getTime() >= analyticsEvent.getCreateDate()
															   .getTime()) ||
						!Validator.equals(elementKey,
							analyticsEvent.getElementKey()) ||
						!Validator.equals(type, analyticsEvent.getType())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(5 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_GTCD_E_E_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_GTCD_E_E_CREATEDATE_2);
			}

			boolean bindElementKey = false;

			if (elementKey == null) {
				query.append(_FINDER_COLUMN_GTCD_E_E_ELEMENTKEY_1);
			}
			else if (elementKey.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_E_E_ELEMENTKEY_3);
			}
			else {
				bindElementKey = true;

				query.append(_FINDER_COLUMN_GTCD_E_E_ELEMENTKEY_2);
			}

			boolean bindType = false;

			if (type == null) {
				query.append(_FINDER_COLUMN_GTCD_E_E_TYPE_1);
			}
			else if (type.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_E_E_TYPE_3);
			}
			else {
				bindType = true;

				query.append(_FINDER_COLUMN_GTCD_E_E_TYPE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				if (bindElementKey) {
					qPos.add(elementKey);
				}

				if (bindType) {
					qPos.add(type);
				}

				if (!pagination) {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analytics event in the ordered set where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param elementKey the element key
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByGtCD_E_E_First(Date createDate,
		String elementKey, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByGtCD_E_E_First(createDate,
				elementKey, type, orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("createDate=");
		msg.append(createDate);

		msg.append(", elementKey=");
		msg.append(elementKey);

		msg.append(", type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the first analytics event in the ordered set where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param elementKey the element key
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByGtCD_E_E_First(Date createDate,
		String elementKey, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		List<AnalyticsEvent> list = findByGtCD_E_E(createDate, elementKey,
				type, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analytics event in the ordered set where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param elementKey the element key
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByGtCD_E_E_Last(Date createDate,
		String elementKey, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByGtCD_E_E_Last(createDate,
				elementKey, type, orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("createDate=");
		msg.append(createDate);

		msg.append(", elementKey=");
		msg.append(elementKey);

		msg.append(", type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the last analytics event in the ordered set where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param elementKey the element key
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByGtCD_E_E_Last(Date createDate,
		String elementKey, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		int count = countByGtCD_E_E(createDate, elementKey, type);

		if (count == 0) {
			return null;
		}

		List<AnalyticsEvent> list = findByGtCD_E_E(createDate, elementKey,
				type, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the analytics events before and after the current analytics event in the ordered set where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param analyticsEventId the primary key of the current analytics event
	 * @param createDate the create date
	 * @param elementKey the element key
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	 */
	@Override
	public AnalyticsEvent[] findByGtCD_E_E_PrevAndNext(long analyticsEventId,
		Date createDate, String elementKey, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = findByPrimaryKey(analyticsEventId);

		Session session = null;

		try {
			session = openSession();

			AnalyticsEvent[] array = new AnalyticsEventImpl[3];

			array[0] = getByGtCD_E_E_PrevAndNext(session, analyticsEvent,
					createDate, elementKey, type, orderByComparator, true);

			array[1] = analyticsEvent;

			array[2] = getByGtCD_E_E_PrevAndNext(session, analyticsEvent,
					createDate, elementKey, type, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AnalyticsEvent getByGtCD_E_E_PrevAndNext(Session session,
		AnalyticsEvent analyticsEvent, Date createDate, String elementKey,
		String type, OrderByComparator<AnalyticsEvent> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

		boolean bindCreateDate = false;

		if (createDate == null) {
			query.append(_FINDER_COLUMN_GTCD_E_E_CREATEDATE_1);
		}
		else {
			bindCreateDate = true;

			query.append(_FINDER_COLUMN_GTCD_E_E_CREATEDATE_2);
		}

		boolean bindElementKey = false;

		if (elementKey == null) {
			query.append(_FINDER_COLUMN_GTCD_E_E_ELEMENTKEY_1);
		}
		else if (elementKey.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_GTCD_E_E_ELEMENTKEY_3);
		}
		else {
			bindElementKey = true;

			query.append(_FINDER_COLUMN_GTCD_E_E_ELEMENTKEY_2);
		}

		boolean bindType = false;

		if (type == null) {
			query.append(_FINDER_COLUMN_GTCD_E_E_TYPE_1);
		}
		else if (type.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_GTCD_E_E_TYPE_3);
		}
		else {
			bindType = true;

			query.append(_FINDER_COLUMN_GTCD_E_E_TYPE_2);
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
			query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindCreateDate) {
			qPos.add(new Timestamp(createDate.getTime()));
		}

		if (bindElementKey) {
			qPos.add(elementKey);
		}

		if (bindType) {
			qPos.add(type);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(analyticsEvent);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AnalyticsEvent> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the analytics events where createDate &gt; &#63; and elementKey = &#63; and type = &#63; from the database.
	 *
	 * @param createDate the create date
	 * @param elementKey the element key
	 * @param type the type
	 */
	@Override
	public void removeByGtCD_E_E(Date createDate, String elementKey, String type) {
		for (AnalyticsEvent analyticsEvent : findByGtCD_E_E(createDate,
				elementKey, type, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analyticsEvent);
		}
	}

	/**
	 * Returns the number of analytics events where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param elementKey the element key
	 * @param type the type
	 * @return the number of matching analytics events
	 */
	@Override
	public int countByGtCD_E_E(Date createDate, String elementKey, String type) {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_COUNT_BY_GTCD_E_E;

		Object[] finderArgs = new Object[] { createDate, elementKey, type };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_ANALYTICSEVENT_WHERE);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_GTCD_E_E_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_GTCD_E_E_CREATEDATE_2);
			}

			boolean bindElementKey = false;

			if (elementKey == null) {
				query.append(_FINDER_COLUMN_GTCD_E_E_ELEMENTKEY_1);
			}
			else if (elementKey.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_E_E_ELEMENTKEY_3);
			}
			else {
				bindElementKey = true;

				query.append(_FINDER_COLUMN_GTCD_E_E_ELEMENTKEY_2);
			}

			boolean bindType = false;

			if (type == null) {
				query.append(_FINDER_COLUMN_GTCD_E_E_TYPE_1);
			}
			else if (type.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_E_E_TYPE_3);
			}
			else {
				bindType = true;

				query.append(_FINDER_COLUMN_GTCD_E_E_TYPE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				if (bindElementKey) {
					qPos.add(elementKey);
				}

				if (bindType) {
					qPos.add(type);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_GTCD_E_E_CREATEDATE_1 = "analyticsEvent.createDate > NULL AND ";
	private static final String _FINDER_COLUMN_GTCD_E_E_CREATEDATE_2 = "analyticsEvent.createDate > ? AND ";
	private static final String _FINDER_COLUMN_GTCD_E_E_ELEMENTKEY_1 = "analyticsEvent.elementKey IS NULL AND ";
	private static final String _FINDER_COLUMN_GTCD_E_E_ELEMENTKEY_2 = "analyticsEvent.elementKey = ? AND ";
	private static final String _FINDER_COLUMN_GTCD_E_E_ELEMENTKEY_3 = "(analyticsEvent.elementKey IS NULL OR analyticsEvent.elementKey = '') AND ";
	private static final String _FINDER_COLUMN_GTCD_E_E_TYPE_1 = "analyticsEvent.type IS NULL";
	private static final String _FINDER_COLUMN_GTCD_E_E_TYPE_2 = "analyticsEvent.type = ?";
	private static final String _FINDER_COLUMN_GTCD_E_E_TYPE_3 = "(analyticsEvent.type IS NULL OR analyticsEvent.type = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_GTCD_C_C_E =
		new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED,
			AnalyticsEventImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByGtCD_C_C_E",
			new String[] {
				Date.class.getName(), String.class.getName(),
				Long.class.getName(), String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_GTCD_C_C_E =
		new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByGtCD_C_C_E",
			new String[] {
				Date.class.getName(), String.class.getName(),
				Long.class.getName(), String.class.getName()
			});

	/**
	 * Returns all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param type the type
	 * @return the matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByGtCD_C_C_E(Date createDate,
		String className, long classPK, String type) {
		return findByGtCD_C_C_E(createDate, className, classPK, type,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param type the type
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @return the range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByGtCD_C_C_E(Date createDate,
		String className, long classPK, String type, int start, int end) {
		return findByGtCD_C_C_E(createDate, className, classPK, type, start,
			end, null);
	}

	/**
	 * Returns an ordered range of all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param type the type
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByGtCD_C_C_E(Date createDate,
		String className, long classPK, String type, int start, int end,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_GTCD_C_C_E;
		finderArgs = new Object[] {
				createDate, className, classPK, type,
				
				start, end, orderByComparator
			};

		List<AnalyticsEvent> list = (List<AnalyticsEvent>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (AnalyticsEvent analyticsEvent : list) {
				if ((createDate.getTime() >= analyticsEvent.getCreateDate()
															   .getTime()) ||
						!Validator.equals(className,
							analyticsEvent.getClassName()) ||
						(classPK != analyticsEvent.getClassPK()) ||
						!Validator.equals(type, analyticsEvent.getType())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(6 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(6);
			}

			query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_E_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_E_CREATEDATE_2);
			}

			boolean bindClassName = false;

			if (className == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_E_CLASSNAME_1);
			}
			else if (className.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_C_C_E_CLASSNAME_3);
			}
			else {
				bindClassName = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_E_CLASSNAME_2);
			}

			query.append(_FINDER_COLUMN_GTCD_C_C_E_CLASSPK_2);

			boolean bindType = false;

			if (type == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_E_TYPE_1);
			}
			else if (type.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_C_C_E_TYPE_3);
			}
			else {
				bindType = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_E_TYPE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				if (bindClassName) {
					qPos.add(className);
				}

				qPos.add(classPK);

				if (bindType) {
					qPos.add(type);
				}

				if (!pagination) {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByGtCD_C_C_E_First(Date createDate,
		String className, long classPK, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByGtCD_C_C_E_First(createDate,
				className, classPK, type, orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(10);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("createDate=");
		msg.append(createDate);

		msg.append(", className=");
		msg.append(className);

		msg.append(", classPK=");
		msg.append(classPK);

		msg.append(", type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the first analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByGtCD_C_C_E_First(Date createDate,
		String className, long classPK, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		List<AnalyticsEvent> list = findByGtCD_C_C_E(createDate, className,
				classPK, type, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByGtCD_C_C_E_Last(Date createDate,
		String className, long classPK, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByGtCD_C_C_E_Last(createDate,
				className, classPK, type, orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(10);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("createDate=");
		msg.append(createDate);

		msg.append(", className=");
		msg.append(className);

		msg.append(", classPK=");
		msg.append(classPK);

		msg.append(", type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the last analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByGtCD_C_C_E_Last(Date createDate,
		String className, long classPK, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		int count = countByGtCD_C_C_E(createDate, className, classPK, type);

		if (count == 0) {
			return null;
		}

		List<AnalyticsEvent> list = findByGtCD_C_C_E(createDate, className,
				classPK, type, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the analytics events before and after the current analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	 *
	 * @param analyticsEventId the primary key of the current analytics event
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	 */
	@Override
	public AnalyticsEvent[] findByGtCD_C_C_E_PrevAndNext(
		long analyticsEventId, Date createDate, String className, long classPK,
		String type, OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = findByPrimaryKey(analyticsEventId);

		Session session = null;

		try {
			session = openSession();

			AnalyticsEvent[] array = new AnalyticsEventImpl[3];

			array[0] = getByGtCD_C_C_E_PrevAndNext(session, analyticsEvent,
					createDate, className, classPK, type, orderByComparator,
					true);

			array[1] = analyticsEvent;

			array[2] = getByGtCD_C_C_E_PrevAndNext(session, analyticsEvent,
					createDate, className, classPK, type, orderByComparator,
					false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AnalyticsEvent getByGtCD_C_C_E_PrevAndNext(Session session,
		AnalyticsEvent analyticsEvent, Date createDate, String className,
		long classPK, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

		boolean bindCreateDate = false;

		if (createDate == null) {
			query.append(_FINDER_COLUMN_GTCD_C_C_E_CREATEDATE_1);
		}
		else {
			bindCreateDate = true;

			query.append(_FINDER_COLUMN_GTCD_C_C_E_CREATEDATE_2);
		}

		boolean bindClassName = false;

		if (className == null) {
			query.append(_FINDER_COLUMN_GTCD_C_C_E_CLASSNAME_1);
		}
		else if (className.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_GTCD_C_C_E_CLASSNAME_3);
		}
		else {
			bindClassName = true;

			query.append(_FINDER_COLUMN_GTCD_C_C_E_CLASSNAME_2);
		}

		query.append(_FINDER_COLUMN_GTCD_C_C_E_CLASSPK_2);

		boolean bindType = false;

		if (type == null) {
			query.append(_FINDER_COLUMN_GTCD_C_C_E_TYPE_1);
		}
		else if (type.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_GTCD_C_C_E_TYPE_3);
		}
		else {
			bindType = true;

			query.append(_FINDER_COLUMN_GTCD_C_C_E_TYPE_2);
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
			query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindCreateDate) {
			qPos.add(new Timestamp(createDate.getTime()));
		}

		if (bindClassName) {
			qPos.add(className);
		}

		qPos.add(classPK);

		if (bindType) {
			qPos.add(type);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(analyticsEvent);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AnalyticsEvent> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63; from the database.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param type the type
	 */
	@Override
	public void removeByGtCD_C_C_E(Date createDate, String className,
		long classPK, String type) {
		for (AnalyticsEvent analyticsEvent : findByGtCD_C_C_E(createDate,
				className, classPK, type, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
				null)) {
			remove(analyticsEvent);
		}
	}

	/**
	 * Returns the number of analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param type the type
	 * @return the number of matching analytics events
	 */
	@Override
	public int countByGtCD_C_C_E(Date createDate, String className,
		long classPK, String type) {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_COUNT_BY_GTCD_C_C_E;

		Object[] finderArgs = new Object[] { createDate, className, classPK, type };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(5);

			query.append(_SQL_COUNT_ANALYTICSEVENT_WHERE);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_E_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_E_CREATEDATE_2);
			}

			boolean bindClassName = false;

			if (className == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_E_CLASSNAME_1);
			}
			else if (className.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_C_C_E_CLASSNAME_3);
			}
			else {
				bindClassName = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_E_CLASSNAME_2);
			}

			query.append(_FINDER_COLUMN_GTCD_C_C_E_CLASSPK_2);

			boolean bindType = false;

			if (type == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_E_TYPE_1);
			}
			else if (type.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_C_C_E_TYPE_3);
			}
			else {
				bindType = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_E_TYPE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				if (bindClassName) {
					qPos.add(className);
				}

				qPos.add(classPK);

				if (bindType) {
					qPos.add(type);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_GTCD_C_C_E_CREATEDATE_1 = "analyticsEvent.createDate > NULL AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_E_CREATEDATE_2 = "analyticsEvent.createDate > ? AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_E_CLASSNAME_1 = "analyticsEvent.className IS NULL AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_E_CLASSNAME_2 = "analyticsEvent.className = ? AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_E_CLASSNAME_3 = "(analyticsEvent.className IS NULL OR analyticsEvent.className = '') AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_E_CLASSPK_2 = "analyticsEvent.classPK = ? AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_E_TYPE_1 = "analyticsEvent.type IS NULL";
	private static final String _FINDER_COLUMN_GTCD_C_C_E_TYPE_2 = "analyticsEvent.type = ?";
	private static final String _FINDER_COLUMN_GTCD_C_C_E_TYPE_3 = "(analyticsEvent.type IS NULL OR analyticsEvent.type = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_GTCD_C_C_R_R_E =
		new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED,
			AnalyticsEventImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByGtCD_C_C_R_R_E",
			new String[] {
				Date.class.getName(), String.class.getName(),
				Long.class.getName(), String.class.getName(),
				Long.class.getName(), String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_GTCD_C_C_R_R_E =
		new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByGtCD_C_C_R_R_E",
			new String[] {
				Date.class.getName(), String.class.getName(),
				Long.class.getName(), String.class.getName(),
				Long.class.getName(), String.class.getName()
			});

	/**
	 * Returns all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param type the type
	 * @return the matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByGtCD_C_C_R_R_E(Date createDate,
		String className, long classPK, String referrerClassName,
		long referrerClassPK, String type) {
		return findByGtCD_C_C_R_R_E(createDate, className, classPK,
			referrerClassName, referrerClassPK, type, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param type the type
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @return the range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByGtCD_C_C_R_R_E(Date createDate,
		String className, long classPK, String referrerClassName,
		long referrerClassPK, String type, int start, int end) {
		return findByGtCD_C_C_R_R_E(createDate, className, classPK,
			referrerClassName, referrerClassPK, type, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param type the type
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByGtCD_C_C_R_R_E(Date createDate,
		String className, long classPK, String referrerClassName,
		long referrerClassPK, String type, int start, int end,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_GTCD_C_C_R_R_E;
		finderArgs = new Object[] {
				createDate, className, classPK, referrerClassName,
				referrerClassPK, type,
				
				start, end, orderByComparator
			};

		List<AnalyticsEvent> list = (List<AnalyticsEvent>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (AnalyticsEvent analyticsEvent : list) {
				if ((createDate.getTime() >= analyticsEvent.getCreateDate()
															   .getTime()) ||
						!Validator.equals(className,
							analyticsEvent.getClassName()) ||
						(classPK != analyticsEvent.getClassPK()) ||
						!Validator.equals(referrerClassName,
							analyticsEvent.getReferrerClassName()) ||
						(referrerClassPK != analyticsEvent.getReferrerClassPK()) ||
						!Validator.equals(type, analyticsEvent.getType())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(8 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(8);
			}

			query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CREATEDATE_2);
			}

			boolean bindClassName = false;

			if (className == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSNAME_1);
			}
			else if (className.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSNAME_3);
			}
			else {
				bindClassName = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSNAME_2);
			}

			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSPK_2);

			boolean bindReferrerClassName = false;

			if (referrerClassName == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSNAME_1);
			}
			else if (referrerClassName.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSNAME_3);
			}
			else {
				bindReferrerClassName = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSNAME_2);
			}

			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSPK_2);

			boolean bindType = false;

			if (type == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_TYPE_1);
			}
			else if (type.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_TYPE_3);
			}
			else {
				bindType = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_TYPE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				if (bindClassName) {
					qPos.add(className);
				}

				qPos.add(classPK);

				if (bindReferrerClassName) {
					qPos.add(referrerClassName);
				}

				qPos.add(referrerClassPK);

				if (bindType) {
					qPos.add(type);
				}

				if (!pagination) {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByGtCD_C_C_R_R_E_First(Date createDate,
		String className, long classPK, String referrerClassName,
		long referrerClassPK, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByGtCD_C_C_R_R_E_First(createDate,
				className, classPK, referrerClassName, referrerClassPK, type,
				orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(14);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("createDate=");
		msg.append(createDate);

		msg.append(", className=");
		msg.append(className);

		msg.append(", classPK=");
		msg.append(classPK);

		msg.append(", referrerClassName=");
		msg.append(referrerClassName);

		msg.append(", referrerClassPK=");
		msg.append(referrerClassPK);

		msg.append(", type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the first analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByGtCD_C_C_R_R_E_First(Date createDate,
		String className, long classPK, String referrerClassName,
		long referrerClassPK, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		List<AnalyticsEvent> list = findByGtCD_C_C_R_R_E(createDate, className,
				classPK, referrerClassName, referrerClassPK, type, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByGtCD_C_C_R_R_E_Last(Date createDate,
		String className, long classPK, String referrerClassName,
		long referrerClassPK, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByGtCD_C_C_R_R_E_Last(createDate,
				className, classPK, referrerClassName, referrerClassPK, type,
				orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(14);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("createDate=");
		msg.append(createDate);

		msg.append(", className=");
		msg.append(className);

		msg.append(", classPK=");
		msg.append(classPK);

		msg.append(", referrerClassName=");
		msg.append(referrerClassName);

		msg.append(", referrerClassPK=");
		msg.append(referrerClassPK);

		msg.append(", type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the last analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByGtCD_C_C_R_R_E_Last(Date createDate,
		String className, long classPK, String referrerClassName,
		long referrerClassPK, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		int count = countByGtCD_C_C_R_R_E(createDate, className, classPK,
				referrerClassName, referrerClassPK, type);

		if (count == 0) {
			return null;
		}

		List<AnalyticsEvent> list = findByGtCD_C_C_R_R_E(createDate, className,
				classPK, referrerClassName, referrerClassPK, type, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the analytics events before and after the current analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	 *
	 * @param analyticsEventId the primary key of the current analytics event
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	 */
	@Override
	public AnalyticsEvent[] findByGtCD_C_C_R_R_E_PrevAndNext(
		long analyticsEventId, Date createDate, String className, long classPK,
		String referrerClassName, long referrerClassPK, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = findByPrimaryKey(analyticsEventId);

		Session session = null;

		try {
			session = openSession();

			AnalyticsEvent[] array = new AnalyticsEventImpl[3];

			array[0] = getByGtCD_C_C_R_R_E_PrevAndNext(session, analyticsEvent,
					createDate, className, classPK, referrerClassName,
					referrerClassPK, type, orderByComparator, true);

			array[1] = analyticsEvent;

			array[2] = getByGtCD_C_C_R_R_E_PrevAndNext(session, analyticsEvent,
					createDate, className, classPK, referrerClassName,
					referrerClassPK, type, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AnalyticsEvent getByGtCD_C_C_R_R_E_PrevAndNext(Session session,
		AnalyticsEvent analyticsEvent, Date createDate, String className,
		long classPK, String referrerClassName, long referrerClassPK,
		String type, OrderByComparator<AnalyticsEvent> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

		boolean bindCreateDate = false;

		if (createDate == null) {
			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CREATEDATE_1);
		}
		else {
			bindCreateDate = true;

			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CREATEDATE_2);
		}

		boolean bindClassName = false;

		if (className == null) {
			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSNAME_1);
		}
		else if (className.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSNAME_3);
		}
		else {
			bindClassName = true;

			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSNAME_2);
		}

		query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSPK_2);

		boolean bindReferrerClassName = false;

		if (referrerClassName == null) {
			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSNAME_1);
		}
		else if (referrerClassName.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSNAME_3);
		}
		else {
			bindReferrerClassName = true;

			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSNAME_2);
		}

		query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSPK_2);

		boolean bindType = false;

		if (type == null) {
			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_TYPE_1);
		}
		else if (type.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_TYPE_3);
		}
		else {
			bindType = true;

			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_TYPE_2);
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
			query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindCreateDate) {
			qPos.add(new Timestamp(createDate.getTime()));
		}

		if (bindClassName) {
			qPos.add(className);
		}

		qPos.add(classPK);

		if (bindReferrerClassName) {
			qPos.add(referrerClassName);
		}

		qPos.add(referrerClassPK);

		if (bindType) {
			qPos.add(type);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(analyticsEvent);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AnalyticsEvent> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63; from the database.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param type the type
	 */
	@Override
	public void removeByGtCD_C_C_R_R_E(Date createDate, String className,
		long classPK, String referrerClassName, long referrerClassPK,
		String type) {
		for (AnalyticsEvent analyticsEvent : findByGtCD_C_C_R_R_E(createDate,
				className, classPK, referrerClassName, referrerClassPK, type,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analyticsEvent);
		}
	}

	/**
	 * Returns the number of analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param className the class name
	 * @param classPK the class p k
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param type the type
	 * @return the number of matching analytics events
	 */
	@Override
	public int countByGtCD_C_C_R_R_E(Date createDate, String className,
		long classPK, String referrerClassName, long referrerClassPK,
		String type) {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_COUNT_BY_GTCD_C_C_R_R_E;

		Object[] finderArgs = new Object[] {
				createDate, className, classPK, referrerClassName,
				referrerClassPK, type
			};

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(7);

			query.append(_SQL_COUNT_ANALYTICSEVENT_WHERE);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CREATEDATE_2);
			}

			boolean bindClassName = false;

			if (className == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSNAME_1);
			}
			else if (className.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSNAME_3);
			}
			else {
				bindClassName = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSNAME_2);
			}

			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSPK_2);

			boolean bindReferrerClassName = false;

			if (referrerClassName == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSNAME_1);
			}
			else if (referrerClassName.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSNAME_3);
			}
			else {
				bindReferrerClassName = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSNAME_2);
			}

			query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSPK_2);

			boolean bindType = false;

			if (type == null) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_TYPE_1);
			}
			else if (type.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_TYPE_3);
			}
			else {
				bindType = true;

				query.append(_FINDER_COLUMN_GTCD_C_C_R_R_E_TYPE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				if (bindClassName) {
					qPos.add(className);
				}

				qPos.add(classPK);

				if (bindReferrerClassName) {
					qPos.add(referrerClassName);
				}

				qPos.add(referrerClassPK);

				if (bindType) {
					qPos.add(type);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_GTCD_C_C_R_R_E_CREATEDATE_1 = "analyticsEvent.createDate > NULL AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_R_R_E_CREATEDATE_2 = "analyticsEvent.createDate > ? AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSNAME_1 = "analyticsEvent.className IS NULL AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSNAME_2 = "analyticsEvent.className = ? AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSNAME_3 = "(analyticsEvent.className IS NULL OR analyticsEvent.className = '') AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_R_R_E_CLASSPK_2 = "analyticsEvent.classPK = ? AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSNAME_1 =
		"analyticsEvent.referrerClassName IS NULL AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSNAME_2 =
		"analyticsEvent.referrerClassName = ? AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSNAME_3 =
		"(analyticsEvent.referrerClassName IS NULL OR analyticsEvent.referrerClassName = '') AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_R_R_E_REFERRERCLASSPK_2 = "analyticsEvent.referrerClassPK = ? AND ";
	private static final String _FINDER_COLUMN_GTCD_C_C_R_R_E_TYPE_1 = "analyticsEvent.type IS NULL";
	private static final String _FINDER_COLUMN_GTCD_C_C_R_R_E_TYPE_2 = "analyticsEvent.type = ?";
	private static final String _FINDER_COLUMN_GTCD_C_C_R_R_E_TYPE_3 = "(analyticsEvent.type IS NULL OR analyticsEvent.type = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_GTCD_R_R_E_E =
		new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED,
			AnalyticsEventImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByGtCD_R_R_E_E",
			new String[] {
				Date.class.getName(), String.class.getName(),
				Long.class.getName(), String.class.getName(),
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_GTCD_R_R_E_E =
		new FinderPath(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByGtCD_R_R_E_E",
			new String[] {
				Date.class.getName(), String.class.getName(),
				Long.class.getName(), String.class.getName(),
				String.class.getName()
			});

	/**
	 * Returns all the analytics events where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param elementKey the element key
	 * @param type the type
	 * @return the matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByGtCD_R_R_E_E(Date createDate,
		String referrerClassName, long referrerClassPK, String elementKey,
		String type) {
		return findByGtCD_R_R_E_E(createDate, referrerClassName,
			referrerClassPK, elementKey, type, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analytics events where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param createDate the create date
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param elementKey the element key
	 * @param type the type
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @return the range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByGtCD_R_R_E_E(Date createDate,
		String referrerClassName, long referrerClassPK, String elementKey,
		String type, int start, int end) {
		return findByGtCD_R_R_E_E(createDate, referrerClassName,
			referrerClassPK, elementKey, type, start, end, null);
	}

	/**
	 * Returns an ordered range of all the analytics events where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param createDate the create date
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param elementKey the element key
	 * @param type the type
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analytics events
	 */
	@Override
	public List<AnalyticsEvent> findByGtCD_R_R_E_E(Date createDate,
		String referrerClassName, long referrerClassPK, String elementKey,
		String type, int start, int end,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_GTCD_R_R_E_E;
		finderArgs = new Object[] {
				createDate, referrerClassName, referrerClassPK, elementKey, type,
				
				start, end, orderByComparator
			};

		List<AnalyticsEvent> list = (List<AnalyticsEvent>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (AnalyticsEvent analyticsEvent : list) {
				if ((createDate.getTime() >= analyticsEvent.getCreateDate()
															   .getTime()) ||
						!Validator.equals(referrerClassName,
							analyticsEvent.getReferrerClassName()) ||
						(referrerClassPK != analyticsEvent.getReferrerClassPK()) ||
						!Validator.equals(elementKey,
							analyticsEvent.getElementKey()) ||
						!Validator.equals(type, analyticsEvent.getType())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(7 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(7);
			}

			query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_CREATEDATE_2);
			}

			boolean bindReferrerClassName = false;

			if (referrerClassName == null) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSNAME_1);
			}
			else if (referrerClassName.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSNAME_3);
			}
			else {
				bindReferrerClassName = true;

				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSNAME_2);
			}

			query.append(_FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSPK_2);

			boolean bindElementKey = false;

			if (elementKey == null) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_ELEMENTKEY_1);
			}
			else if (elementKey.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_ELEMENTKEY_3);
			}
			else {
				bindElementKey = true;

				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_ELEMENTKEY_2);
			}

			boolean bindType = false;

			if (type == null) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_TYPE_1);
			}
			else if (type.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_TYPE_3);
			}
			else {
				bindType = true;

				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_TYPE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				if (bindReferrerClassName) {
					qPos.add(referrerClassName);
				}

				qPos.add(referrerClassPK);

				if (bindElementKey) {
					qPos.add(elementKey);
				}

				if (bindType) {
					qPos.add(type);
				}

				if (!pagination) {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first analytics event in the ordered set where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param elementKey the element key
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByGtCD_R_R_E_E_First(Date createDate,
		String referrerClassName, long referrerClassPK, String elementKey,
		String type, OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByGtCD_R_R_E_E_First(createDate,
				referrerClassName, referrerClassPK, elementKey, type,
				orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(12);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("createDate=");
		msg.append(createDate);

		msg.append(", referrerClassName=");
		msg.append(referrerClassName);

		msg.append(", referrerClassPK=");
		msg.append(referrerClassPK);

		msg.append(", elementKey=");
		msg.append(elementKey);

		msg.append(", type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the first analytics event in the ordered set where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param elementKey the element key
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByGtCD_R_R_E_E_First(Date createDate,
		String referrerClassName, long referrerClassPK, String elementKey,
		String type, OrderByComparator<AnalyticsEvent> orderByComparator) {
		List<AnalyticsEvent> list = findByGtCD_R_R_E_E(createDate,
				referrerClassName, referrerClassPK, elementKey, type, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last analytics event in the ordered set where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param elementKey the element key
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent findByGtCD_R_R_E_E_Last(Date createDate,
		String referrerClassName, long referrerClassPK, String elementKey,
		String type, OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByGtCD_R_R_E_E_Last(createDate,
				referrerClassName, referrerClassPK, elementKey, type,
				orderByComparator);

		if (analyticsEvent != null) {
			return analyticsEvent;
		}

		StringBundler msg = new StringBundler(12);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("createDate=");
		msg.append(createDate);

		msg.append(", referrerClassName=");
		msg.append(referrerClassName);

		msg.append(", referrerClassPK=");
		msg.append(referrerClassPK);

		msg.append(", elementKey=");
		msg.append(elementKey);

		msg.append(", type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEventException(msg.toString());
	}

	/**
	 * Returns the last analytics event in the ordered set where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param elementKey the element key
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	 */
	@Override
	public AnalyticsEvent fetchByGtCD_R_R_E_E_Last(Date createDate,
		String referrerClassName, long referrerClassPK, String elementKey,
		String type, OrderByComparator<AnalyticsEvent> orderByComparator) {
		int count = countByGtCD_R_R_E_E(createDate, referrerClassName,
				referrerClassPK, elementKey, type);

		if (count == 0) {
			return null;
		}

		List<AnalyticsEvent> list = findByGtCD_R_R_E_E(createDate,
				referrerClassName, referrerClassPK, elementKey, type,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the analytics events before and after the current analytics event in the ordered set where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param analyticsEventId the primary key of the current analytics event
	 * @param createDate the create date
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param elementKey the element key
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	 */
	@Override
	public AnalyticsEvent[] findByGtCD_R_R_E_E_PrevAndNext(
		long analyticsEventId, Date createDate, String referrerClassName,
		long referrerClassPK, String elementKey, String type,
		OrderByComparator<AnalyticsEvent> orderByComparator)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = findByPrimaryKey(analyticsEventId);

		Session session = null;

		try {
			session = openSession();

			AnalyticsEvent[] array = new AnalyticsEventImpl[3];

			array[0] = getByGtCD_R_R_E_E_PrevAndNext(session, analyticsEvent,
					createDate, referrerClassName, referrerClassPK, elementKey,
					type, orderByComparator, true);

			array[1] = analyticsEvent;

			array[2] = getByGtCD_R_R_E_E_PrevAndNext(session, analyticsEvent,
					createDate, referrerClassName, referrerClassPK, elementKey,
					type, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AnalyticsEvent getByGtCD_R_R_E_E_PrevAndNext(Session session,
		AnalyticsEvent analyticsEvent, Date createDate,
		String referrerClassName, long referrerClassPK, String elementKey,
		String type, OrderByComparator<AnalyticsEvent> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE);

		boolean bindCreateDate = false;

		if (createDate == null) {
			query.append(_FINDER_COLUMN_GTCD_R_R_E_E_CREATEDATE_1);
		}
		else {
			bindCreateDate = true;

			query.append(_FINDER_COLUMN_GTCD_R_R_E_E_CREATEDATE_2);
		}

		boolean bindReferrerClassName = false;

		if (referrerClassName == null) {
			query.append(_FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSNAME_1);
		}
		else if (referrerClassName.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSNAME_3);
		}
		else {
			bindReferrerClassName = true;

			query.append(_FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSNAME_2);
		}

		query.append(_FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSPK_2);

		boolean bindElementKey = false;

		if (elementKey == null) {
			query.append(_FINDER_COLUMN_GTCD_R_R_E_E_ELEMENTKEY_1);
		}
		else if (elementKey.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_GTCD_R_R_E_E_ELEMENTKEY_3);
		}
		else {
			bindElementKey = true;

			query.append(_FINDER_COLUMN_GTCD_R_R_E_E_ELEMENTKEY_2);
		}

		boolean bindType = false;

		if (type == null) {
			query.append(_FINDER_COLUMN_GTCD_R_R_E_E_TYPE_1);
		}
		else if (type.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_GTCD_R_R_E_E_TYPE_3);
		}
		else {
			bindType = true;

			query.append(_FINDER_COLUMN_GTCD_R_R_E_E_TYPE_2);
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
			query.append(AnalyticsEventModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindCreateDate) {
			qPos.add(new Timestamp(createDate.getTime()));
		}

		if (bindReferrerClassName) {
			qPos.add(referrerClassName);
		}

		qPos.add(referrerClassPK);

		if (bindElementKey) {
			qPos.add(elementKey);
		}

		if (bindType) {
			qPos.add(type);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(analyticsEvent);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AnalyticsEvent> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the analytics events where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63; from the database.
	 *
	 * @param createDate the create date
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param elementKey the element key
	 * @param type the type
	 */
	@Override
	public void removeByGtCD_R_R_E_E(Date createDate, String referrerClassName,
		long referrerClassPK, String elementKey, String type) {
		for (AnalyticsEvent analyticsEvent : findByGtCD_R_R_E_E(createDate,
				referrerClassName, referrerClassPK, elementKey, type,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(analyticsEvent);
		}
	}

	/**
	 * Returns the number of analytics events where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	 *
	 * @param createDate the create date
	 * @param referrerClassName the referrer class name
	 * @param referrerClassPK the referrer class p k
	 * @param elementKey the element key
	 * @param type the type
	 * @return the number of matching analytics events
	 */
	@Override
	public int countByGtCD_R_R_E_E(Date createDate, String referrerClassName,
		long referrerClassPK, String elementKey, String type) {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_COUNT_BY_GTCD_R_R_E_E;

		Object[] finderArgs = new Object[] {
				createDate, referrerClassName, referrerClassPK, elementKey, type
			};

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(6);

			query.append(_SQL_COUNT_ANALYTICSEVENT_WHERE);

			boolean bindCreateDate = false;

			if (createDate == null) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_CREATEDATE_1);
			}
			else {
				bindCreateDate = true;

				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_CREATEDATE_2);
			}

			boolean bindReferrerClassName = false;

			if (referrerClassName == null) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSNAME_1);
			}
			else if (referrerClassName.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSNAME_3);
			}
			else {
				bindReferrerClassName = true;

				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSNAME_2);
			}

			query.append(_FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSPK_2);

			boolean bindElementKey = false;

			if (elementKey == null) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_ELEMENTKEY_1);
			}
			else if (elementKey.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_ELEMENTKEY_3);
			}
			else {
				bindElementKey = true;

				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_ELEMENTKEY_2);
			}

			boolean bindType = false;

			if (type == null) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_TYPE_1);
			}
			else if (type.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_TYPE_3);
			}
			else {
				bindType = true;

				query.append(_FINDER_COLUMN_GTCD_R_R_E_E_TYPE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindCreateDate) {
					qPos.add(new Timestamp(createDate.getTime()));
				}

				if (bindReferrerClassName) {
					qPos.add(referrerClassName);
				}

				qPos.add(referrerClassPK);

				if (bindElementKey) {
					qPos.add(elementKey);
				}

				if (bindType) {
					qPos.add(type);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_GTCD_R_R_E_E_CREATEDATE_1 = "analyticsEvent.createDate > NULL AND ";
	private static final String _FINDER_COLUMN_GTCD_R_R_E_E_CREATEDATE_2 = "analyticsEvent.createDate > ? AND ";
	private static final String _FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSNAME_1 = "analyticsEvent.referrerClassName IS NULL AND ";
	private static final String _FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSNAME_2 = "analyticsEvent.referrerClassName = ? AND ";
	private static final String _FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSNAME_3 = "(analyticsEvent.referrerClassName IS NULL OR analyticsEvent.referrerClassName = '') AND ";
	private static final String _FINDER_COLUMN_GTCD_R_R_E_E_REFERRERCLASSPK_2 = "analyticsEvent.referrerClassPK = ? AND ";
	private static final String _FINDER_COLUMN_GTCD_R_R_E_E_ELEMENTKEY_1 = "analyticsEvent.elementKey IS NULL AND ";
	private static final String _FINDER_COLUMN_GTCD_R_R_E_E_ELEMENTKEY_2 = "analyticsEvent.elementKey = ? AND ";
	private static final String _FINDER_COLUMN_GTCD_R_R_E_E_ELEMENTKEY_3 = "(analyticsEvent.elementKey IS NULL OR analyticsEvent.elementKey = '') AND ";
	private static final String _FINDER_COLUMN_GTCD_R_R_E_E_TYPE_1 = "analyticsEvent.type IS NULL";
	private static final String _FINDER_COLUMN_GTCD_R_R_E_E_TYPE_2 = "analyticsEvent.type = ?";
	private static final String _FINDER_COLUMN_GTCD_R_R_E_E_TYPE_3 = "(analyticsEvent.type IS NULL OR analyticsEvent.type = '')";

	public AnalyticsEventPersistenceImpl() {
		setModelClass(AnalyticsEvent.class);
	}

	/**
	 * Caches the analytics event in the entity cache if it is enabled.
	 *
	 * @param analyticsEvent the analytics event
	 */
	@Override
	public void cacheResult(AnalyticsEvent analyticsEvent) {
		EntityCacheUtil.putResult(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventImpl.class, analyticsEvent.getPrimaryKey(),
			analyticsEvent);

		analyticsEvent.resetOriginalValues();
	}

	/**
	 * Caches the analytics events in the entity cache if it is enabled.
	 *
	 * @param analyticsEvents the analytics events
	 */
	@Override
	public void cacheResult(List<AnalyticsEvent> analyticsEvents) {
		for (AnalyticsEvent analyticsEvent : analyticsEvents) {
			if (EntityCacheUtil.getResult(
						AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
						AnalyticsEventImpl.class, analyticsEvent.getPrimaryKey()) == null) {
				cacheResult(analyticsEvent);
			}
			else {
				analyticsEvent.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all analytics events.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(AnalyticsEventImpl.class.getName());
		}

		EntityCacheUtil.clearCache(AnalyticsEventImpl.class);

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the analytics event.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(AnalyticsEvent analyticsEvent) {
		EntityCacheUtil.removeResult(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventImpl.class, analyticsEvent.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<AnalyticsEvent> analyticsEvents) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (AnalyticsEvent analyticsEvent : analyticsEvents) {
			EntityCacheUtil.removeResult(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
				AnalyticsEventImpl.class, analyticsEvent.getPrimaryKey());
		}
	}

	/**
	 * Creates a new analytics event with the primary key. Does not add the analytics event to the database.
	 *
	 * @param analyticsEventId the primary key for the new analytics event
	 * @return the new analytics event
	 */
	@Override
	public AnalyticsEvent create(long analyticsEventId) {
		AnalyticsEvent analyticsEvent = new AnalyticsEventImpl();

		analyticsEvent.setNew(true);
		analyticsEvent.setPrimaryKey(analyticsEventId);

		return analyticsEvent;
	}

	/**
	 * Removes the analytics event with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param analyticsEventId the primary key of the analytics event
	 * @return the analytics event that was removed
	 * @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	 */
	@Override
	public AnalyticsEvent remove(long analyticsEventId)
		throws NoSuchEventException {
		return remove((Serializable)analyticsEventId);
	}

	/**
	 * Removes the analytics event with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the analytics event
	 * @return the analytics event that was removed
	 * @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	 */
	@Override
	public AnalyticsEvent remove(Serializable primaryKey)
		throws NoSuchEventException {
		Session session = null;

		try {
			session = openSession();

			AnalyticsEvent analyticsEvent = (AnalyticsEvent)session.get(AnalyticsEventImpl.class,
					primaryKey);

			if (analyticsEvent == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchEventException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(analyticsEvent);
		}
		catch (NoSuchEventException nsee) {
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
	protected AnalyticsEvent removeImpl(AnalyticsEvent analyticsEvent) {
		analyticsEvent = toUnwrappedModel(analyticsEvent);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(analyticsEvent)) {
				analyticsEvent = (AnalyticsEvent)session.get(AnalyticsEventImpl.class,
						analyticsEvent.getPrimaryKeyObj());
			}

			if (analyticsEvent != null) {
				session.delete(analyticsEvent);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (analyticsEvent != null) {
			clearCache(analyticsEvent);
		}

		return analyticsEvent;
	}

	@Override
	public AnalyticsEvent updateImpl(
		com.liferay.analytics.model.AnalyticsEvent analyticsEvent) {
		analyticsEvent = toUnwrappedModel(analyticsEvent);

		boolean isNew = analyticsEvent.isNew();

		AnalyticsEventModelImpl analyticsEventModelImpl = (AnalyticsEventModelImpl)analyticsEvent;

		Session session = null;

		try {
			session = openSession();

			if (analyticsEvent.isNew()) {
				session.save(analyticsEvent);

				analyticsEvent.setNew(false);
			}
			else {
				session.merge(analyticsEvent);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !AnalyticsEventModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((analyticsEventModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						analyticsEventModelImpl.getOriginalCompanyId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_COMPANYID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID,
					args);

				args = new Object[] { analyticsEventModelImpl.getCompanyId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_COMPANYID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID,
					args);
			}
		}

		EntityCacheUtil.putResult(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
			AnalyticsEventImpl.class, analyticsEvent.getPrimaryKey(),
			analyticsEvent, false);

		analyticsEvent.resetOriginalValues();

		return analyticsEvent;
	}

	protected AnalyticsEvent toUnwrappedModel(AnalyticsEvent analyticsEvent) {
		if (analyticsEvent instanceof AnalyticsEventImpl) {
			return analyticsEvent;
		}

		AnalyticsEventImpl analyticsEventImpl = new AnalyticsEventImpl();

		analyticsEventImpl.setNew(analyticsEvent.isNew());
		analyticsEventImpl.setPrimaryKey(analyticsEvent.getPrimaryKey());

		analyticsEventImpl.setAnalyticsEventId(analyticsEvent.getAnalyticsEventId());
		analyticsEventImpl.setCompanyId(analyticsEvent.getCompanyId());
		analyticsEventImpl.setUserId(analyticsEvent.getUserId());
		analyticsEventImpl.setCreateDate(analyticsEvent.getCreateDate());
		analyticsEventImpl.setAnonymousUserId(analyticsEvent.getAnonymousUserId());
		analyticsEventImpl.setClassName(analyticsEvent.getClassName());
		analyticsEventImpl.setClassPK(analyticsEvent.getClassPK());
		analyticsEventImpl.setReferrerClassName(analyticsEvent.getReferrerClassName());
		analyticsEventImpl.setReferrerClassPK(analyticsEvent.getReferrerClassPK());
		analyticsEventImpl.setElementKey(analyticsEvent.getElementKey());
		analyticsEventImpl.setType(analyticsEvent.getType());
		analyticsEventImpl.setClientIP(analyticsEvent.getClientIP());
		analyticsEventImpl.setUserAgent(analyticsEvent.getUserAgent());
		analyticsEventImpl.setLanguageId(analyticsEvent.getLanguageId());
		analyticsEventImpl.setUrl(analyticsEvent.getUrl());
		analyticsEventImpl.setAdditionalInfo(analyticsEvent.getAdditionalInfo());

		return analyticsEventImpl;
	}

	/**
	 * Returns the analytics event with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the analytics event
	 * @return the analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	 */
	@Override
	public AnalyticsEvent findByPrimaryKey(Serializable primaryKey)
		throws NoSuchEventException {
		AnalyticsEvent analyticsEvent = fetchByPrimaryKey(primaryKey);

		if (analyticsEvent == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchEventException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return analyticsEvent;
	}

	/**
	 * Returns the analytics event with the primary key or throws a {@link com.liferay.analytics.NoSuchEventException} if it could not be found.
	 *
	 * @param analyticsEventId the primary key of the analytics event
	 * @return the analytics event
	 * @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	 */
	@Override
	public AnalyticsEvent findByPrimaryKey(long analyticsEventId)
		throws NoSuchEventException {
		return findByPrimaryKey((Serializable)analyticsEventId);
	}

	/**
	 * Returns the analytics event with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the analytics event
	 * @return the analytics event, or <code>null</code> if a analytics event with the primary key could not be found
	 */
	@Override
	public AnalyticsEvent fetchByPrimaryKey(Serializable primaryKey) {
		AnalyticsEvent analyticsEvent = (AnalyticsEvent)EntityCacheUtil.getResult(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
				AnalyticsEventImpl.class, primaryKey);

		if (analyticsEvent == _nullAnalyticsEvent) {
			return null;
		}

		if (analyticsEvent == null) {
			Session session = null;

			try {
				session = openSession();

				analyticsEvent = (AnalyticsEvent)session.get(AnalyticsEventImpl.class,
						primaryKey);

				if (analyticsEvent != null) {
					cacheResult(analyticsEvent);
				}
				else {
					EntityCacheUtil.putResult(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
						AnalyticsEventImpl.class, primaryKey,
						_nullAnalyticsEvent);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
					AnalyticsEventImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return analyticsEvent;
	}

	/**
	 * Returns the analytics event with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param analyticsEventId the primary key of the analytics event
	 * @return the analytics event, or <code>null</code> if a analytics event with the primary key could not be found
	 */
	@Override
	public AnalyticsEvent fetchByPrimaryKey(long analyticsEventId) {
		return fetchByPrimaryKey((Serializable)analyticsEventId);
	}

	@Override
	public Map<Serializable, AnalyticsEvent> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, AnalyticsEvent> map = new HashMap<Serializable, AnalyticsEvent>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			AnalyticsEvent analyticsEvent = fetchByPrimaryKey(primaryKey);

			if (analyticsEvent != null) {
				map.put(primaryKey, analyticsEvent);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			AnalyticsEvent analyticsEvent = (AnalyticsEvent)EntityCacheUtil.getResult(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
					AnalyticsEventImpl.class, primaryKey);

			if (analyticsEvent == null) {
				if (uncachedPrimaryKeys == null) {
					uncachedPrimaryKeys = new HashSet<Serializable>();
				}

				uncachedPrimaryKeys.add(primaryKey);
			}
			else {
				map.put(primaryKey, analyticsEvent);
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_ANALYTICSEVENT_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			query.append(String.valueOf(primaryKey));

			query.append(StringPool.COMMA);
		}

		query.setIndex(query.index() - 1);

		query.append(StringPool.CLOSE_PARENTHESIS);

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			for (AnalyticsEvent analyticsEvent : (List<AnalyticsEvent>)q.list()) {
				map.put(analyticsEvent.getPrimaryKeyObj(), analyticsEvent);

				cacheResult(analyticsEvent);

				uncachedPrimaryKeys.remove(analyticsEvent.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				EntityCacheUtil.putResult(AnalyticsEventModelImpl.ENTITY_CACHE_ENABLED,
					AnalyticsEventImpl.class, primaryKey, _nullAnalyticsEvent);
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
	 * Returns all the analytics events.
	 *
	 * @return the analytics events
	 */
	@Override
	public List<AnalyticsEvent> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the analytics events.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @return the range of analytics events
	 */
	@Override
	public List<AnalyticsEvent> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the analytics events.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of analytics events
	 * @param end the upper bound of the range of analytics events (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of analytics events
	 */
	@Override
	public List<AnalyticsEvent> findAll(int start, int end,
		OrderByComparator<AnalyticsEvent> orderByComparator) {
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

		List<AnalyticsEvent> list = (List<AnalyticsEvent>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_ANALYTICSEVENT);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_ANALYTICSEVENT;

				if (pagination) {
					sql = sql.concat(AnalyticsEventModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AnalyticsEvent>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the analytics events from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (AnalyticsEvent analyticsEvent : findAll()) {
			remove(analyticsEvent);
		}
	}

	/**
	 * Returns the number of analytics events.
	 *
	 * @return the number of analytics events
	 */
	@Override
	public int countAll() {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_ANALYTICSEVENT);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_ALL,
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
	protected Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	/**
	 * Initializes the analytics event persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.liferay.analytics.model.AnalyticsEvent")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<AnalyticsEvent>> listenersList = new ArrayList<ModelListener<AnalyticsEvent>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<AnalyticsEvent>)InstanceFactory.newInstance(
							getClassLoader(), listenerClassName));
				}

				listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}
	}

	public void destroy() {
		EntityCacheUtil.removeCache(AnalyticsEventImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_ANALYTICSEVENT = "SELECT analyticsEvent FROM AnalyticsEvent analyticsEvent";
	private static final String _SQL_SELECT_ANALYTICSEVENT_WHERE_PKS_IN = "SELECT analyticsEvent FROM AnalyticsEvent analyticsEvent WHERE analyticsEventId IN (";
	private static final String _SQL_SELECT_ANALYTICSEVENT_WHERE = "SELECT analyticsEvent FROM AnalyticsEvent analyticsEvent WHERE ";
	private static final String _SQL_COUNT_ANALYTICSEVENT = "SELECT COUNT(analyticsEvent) FROM AnalyticsEvent analyticsEvent";
	private static final String _SQL_COUNT_ANALYTICSEVENT_WHERE = "SELECT COUNT(analyticsEvent) FROM AnalyticsEvent analyticsEvent WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "analyticsEvent.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No AnalyticsEvent exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No AnalyticsEvent exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = com.liferay.portal.util.PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE;
	private static Log _log = LogFactoryUtil.getLog(AnalyticsEventPersistenceImpl.class);
	private static Set<String> _badColumnNames = SetUtil.fromArray(new String[] {
				"type"
			});
	private static AnalyticsEvent _nullAnalyticsEvent = new AnalyticsEventImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<AnalyticsEvent> toCacheModel() {
				return _nullAnalyticsEventCacheModel;
			}
		};

	private static CacheModel<AnalyticsEvent> _nullAnalyticsEventCacheModel = new CacheModel<AnalyticsEvent>() {
			@Override
			public AnalyticsEvent toEntityModel() {
				return _nullAnalyticsEvent;
			}
		};
}
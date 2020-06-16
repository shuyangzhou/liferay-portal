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

package com.liferay.portal.workflow.kaleo.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.workflow.kaleo.exception.NoSuchTimerInstanceTokenException;
import com.liferay.portal.workflow.kaleo.model.KaleoTimerInstanceToken;
import com.liferay.portal.workflow.kaleo.model.KaleoTimerInstanceTokenTable;
import com.liferay.portal.workflow.kaleo.model.impl.KaleoTimerInstanceTokenImpl;
import com.liferay.portal.workflow.kaleo.model.impl.KaleoTimerInstanceTokenModelImpl;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTimerInstanceTokenPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.impl.constants.KaleoPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import javax.sql.DataSource;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the kaleo timer instance token service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = KaleoTimerInstanceTokenPersistence.class)
public class KaleoTimerInstanceTokenPersistenceImpl
	extends BasePersistenceImpl<KaleoTimerInstanceToken>
	implements KaleoTimerInstanceTokenPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>KaleoTimerInstanceTokenUtil</code> to access the kaleo timer instance token persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		KaleoTimerInstanceTokenImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	/**
	 * Returns all the kaleo timer instance tokens where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @return the matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKaleoInstanceId(
		long kaleoInstanceId) {

		return findByKaleoInstanceId(
			kaleoInstanceId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the kaleo timer instance tokens where kaleoInstanceId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @return the range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKaleoInstanceId(
		long kaleoInstanceId, int start, int end) {

		return findByKaleoInstanceId(kaleoInstanceId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens where kaleoInstanceId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKaleoInstanceId(
		long kaleoInstanceId, int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		return findByKaleoInstanceId(
			kaleoInstanceId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens where kaleoInstanceId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKaleoInstanceId(
		long kaleoInstanceId, int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
					"findByKaleoInstanceId");
				finderArgs = new Object[] {kaleoInstanceId};
			}
		}
		else if (useFinderCache) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"findByKaleoInstanceId");
			finderArgs = new Object[] {
				kaleoInstanceId, start, end, orderByComparator
			};
		}

		List<KaleoTimerInstanceToken> list = null;

		if (useFinderCache) {
			list = (List<KaleoTimerInstanceToken>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (KaleoTimerInstanceToken kaleoTimerInstanceToken : list) {
					if (kaleoInstanceId !=
							kaleoTimerInstanceToken.getKaleoInstanceId()) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KALEOINSTANCEID_KALEOINSTANCEID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceId);

				list = (List<KaleoTimerInstanceToken>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first kaleo timer instance token in the ordered set where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKaleoInstanceId_First(
			long kaleoInstanceId,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken =
			fetchByKaleoInstanceId_First(kaleoInstanceId, orderByComparator);

		if (kaleoTimerInstanceToken != null) {
			return kaleoTimerInstanceToken;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoInstanceId=");
		sb.append(kaleoInstanceId);

		sb.append("}");

		throw new NoSuchTimerInstanceTokenException(sb.toString());
	}

	/**
	 * Returns the first kaleo timer instance token in the ordered set where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKaleoInstanceId_First(
		long kaleoInstanceId,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		List<KaleoTimerInstanceToken> list = findByKaleoInstanceId(
			kaleoInstanceId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last kaleo timer instance token in the ordered set where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKaleoInstanceId_Last(
			long kaleoInstanceId,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken =
			fetchByKaleoInstanceId_Last(kaleoInstanceId, orderByComparator);

		if (kaleoTimerInstanceToken != null) {
			return kaleoTimerInstanceToken;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoInstanceId=");
		sb.append(kaleoInstanceId);

		sb.append("}");

		throw new NoSuchTimerInstanceTokenException(sb.toString());
	}

	/**
	 * Returns the last kaleo timer instance token in the ordered set where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKaleoInstanceId_Last(
		long kaleoInstanceId,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		int count = countByKaleoInstanceId(kaleoInstanceId);

		if (count == 0) {
			return null;
		}

		List<KaleoTimerInstanceToken> list = findByKaleoInstanceId(
			kaleoInstanceId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the kaleo timer instance tokens before and after the current kaleo timer instance token in the ordered set where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the current kaleo timer instance token
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken[] findByKaleoInstanceId_PrevAndNext(
			long kaleoTimerInstanceTokenId, long kaleoInstanceId,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = findByPrimaryKey(
			kaleoTimerInstanceTokenId);

		Session session = null;

		try {
			session = openSession();

			KaleoTimerInstanceToken[] array =
				new KaleoTimerInstanceTokenImpl[3];

			array[0] = getByKaleoInstanceId_PrevAndNext(
				session, kaleoTimerInstanceToken, kaleoInstanceId,
				orderByComparator, true);

			array[1] = kaleoTimerInstanceToken;

			array[2] = getByKaleoInstanceId_PrevAndNext(
				session, kaleoTimerInstanceToken, kaleoInstanceId,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected KaleoTimerInstanceToken getByKaleoInstanceId_PrevAndNext(
		Session session, KaleoTimerInstanceToken kaleoTimerInstanceToken,
		long kaleoInstanceId,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

		sb.append(_FINDER_COLUMN_KALEOINSTANCEID_KALEOINSTANCEID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(kaleoInstanceId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						kaleoTimerInstanceToken)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<KaleoTimerInstanceToken> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the kaleo timer instance tokens where kaleoInstanceId = &#63; from the database.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 */
	@Override
	public void removeByKaleoInstanceId(long kaleoInstanceId) {
		for (KaleoTimerInstanceToken kaleoTimerInstanceToken :
				findByKaleoInstanceId(
					kaleoInstanceId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(kaleoTimerInstanceToken);
		}
	}

	/**
	 * Returns the number of kaleo timer instance tokens where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @return the number of matching kaleo timer instance tokens
	 */
	@Override
	public int countByKaleoInstanceId(long kaleoInstanceId) {
		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByKaleoInstanceId");

		Object[] finderArgs = new Object[] {kaleoInstanceId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KALEOINSTANCEID_KALEOINSTANCEID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_KALEOINSTANCEID_KALEOINSTANCEID_2 =
			"kaleoTimerInstanceToken.kaleoInstanceId = ?";

	/**
	 * Returns the kaleo timer instance token where kaleoInstanceTokenId = &#63; and kaleoTimerId = &#63; or throws a <code>NoSuchTimerInstanceTokenException</code> if it could not be found.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param kaleoTimerId the kaleo timer ID
	 * @return the matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKITI_KTI(
			long kaleoInstanceTokenId, long kaleoTimerId)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = fetchByKITI_KTI(
			kaleoInstanceTokenId, kaleoTimerId);

		if (kaleoTimerInstanceToken == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("kaleoInstanceTokenId=");
			sb.append(kaleoInstanceTokenId);

			sb.append(", kaleoTimerId=");
			sb.append(kaleoTimerId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchTimerInstanceTokenException(sb.toString());
		}

		return kaleoTimerInstanceToken;
	}

	/**
	 * Returns the kaleo timer instance token where kaleoInstanceTokenId = &#63; and kaleoTimerId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param kaleoTimerId the kaleo timer ID
	 * @return the matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKITI_KTI(
		long kaleoInstanceTokenId, long kaleoTimerId) {

		return fetchByKITI_KTI(kaleoInstanceTokenId, kaleoTimerId, true);
	}

	/**
	 * Returns the kaleo timer instance token where kaleoInstanceTokenId = &#63; and kaleoTimerId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param kaleoTimerId the kaleo timer ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKITI_KTI(
		long kaleoInstanceTokenId, long kaleoTimerId, boolean useFinderCache) {

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {kaleoInstanceTokenId, kaleoTimerId};
		}

		Object result = null;

		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByKITI_KTI");

		if (useFinderCache) {
			result = finderCache.getResult(finderPath, finderArgs, this);
		}

		if (result instanceof KaleoTimerInstanceToken) {
			KaleoTimerInstanceToken kaleoTimerInstanceToken =
				(KaleoTimerInstanceToken)result;

			if ((kaleoInstanceTokenId !=
					kaleoTimerInstanceToken.getKaleoInstanceTokenId()) ||
				(kaleoTimerId != kaleoTimerInstanceToken.getKaleoTimerId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KITI_KTI_KALEOINSTANCETOKENID_2);

			sb.append(_FINDER_COLUMN_KITI_KTI_KALEOTIMERID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceTokenId);

				queryPos.add(kaleoTimerId);

				List<KaleoTimerInstanceToken> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(finderPath, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {
									kaleoInstanceTokenId, kaleoTimerId
								};
							}

							_log.warn(
								"KaleoTimerInstanceTokenPersistenceImpl.fetchByKITI_KTI(long, long, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					KaleoTimerInstanceToken kaleoTimerInstanceToken = list.get(
						0);

					result = kaleoTimerInstanceToken;

					cacheResult(kaleoTimerInstanceToken);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (KaleoTimerInstanceToken)result;
		}
	}

	/**
	 * Removes the kaleo timer instance token where kaleoInstanceTokenId = &#63; and kaleoTimerId = &#63; from the database.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param kaleoTimerId the kaleo timer ID
	 * @return the kaleo timer instance token that was removed
	 */
	@Override
	public KaleoTimerInstanceToken removeByKITI_KTI(
			long kaleoInstanceTokenId, long kaleoTimerId)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = findByKITI_KTI(
			kaleoInstanceTokenId, kaleoTimerId);

		return remove(kaleoTimerInstanceToken);
	}

	/**
	 * Returns the number of kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and kaleoTimerId = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param kaleoTimerId the kaleo timer ID
	 * @return the number of matching kaleo timer instance tokens
	 */
	@Override
	public int countByKITI_KTI(long kaleoInstanceTokenId, long kaleoTimerId) {
		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByKITI_KTI");

		Object[] finderArgs = new Object[] {kaleoInstanceTokenId, kaleoTimerId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KITI_KTI_KALEOINSTANCETOKENID_2);

			sb.append(_FINDER_COLUMN_KITI_KTI_KALEOTIMERID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceTokenId);

				queryPos.add(kaleoTimerId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_KITI_KTI_KALEOINSTANCETOKENID_2 =
		"kaleoTimerInstanceToken.kaleoInstanceTokenId = ? AND ";

	private static final String _FINDER_COLUMN_KITI_KTI_KALEOTIMERID_2 =
		"kaleoTimerInstanceToken.kaleoTimerId = ?";

	/**
	 * Returns all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @return the matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_C(
		long kaleoInstanceTokenId, boolean completed) {

		return findByKITI_C(
			kaleoInstanceTokenId, completed, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @return the range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_C(
		long kaleoInstanceTokenId, boolean completed, int start, int end) {

		return findByKITI_C(kaleoInstanceTokenId, completed, start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_C(
		long kaleoInstanceTokenId, boolean completed, int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		return findByKITI_C(
			kaleoInstanceTokenId, completed, start, end, orderByComparator,
			true);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_C(
		long kaleoInstanceTokenId, boolean completed, int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByKITI_C");
				finderArgs = new Object[] {kaleoInstanceTokenId, completed};
			}
		}
		else if (useFinderCache) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByKITI_C");
			finderArgs = new Object[] {
				kaleoInstanceTokenId, completed, start, end, orderByComparator
			};
		}

		List<KaleoTimerInstanceToken> list = null;

		if (useFinderCache) {
			list = (List<KaleoTimerInstanceToken>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (KaleoTimerInstanceToken kaleoTimerInstanceToken : list) {
					if ((kaleoInstanceTokenId !=
							kaleoTimerInstanceToken.
								getKaleoInstanceTokenId()) ||
						(completed != kaleoTimerInstanceToken.isCompleted())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KITI_C_KALEOINSTANCETOKENID_2);

			sb.append(_FINDER_COLUMN_KITI_C_COMPLETED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceTokenId);

				queryPos.add(completed);

				list = (List<KaleoTimerInstanceToken>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKITI_C_First(
			long kaleoInstanceTokenId, boolean completed,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = fetchByKITI_C_First(
			kaleoInstanceTokenId, completed, orderByComparator);

		if (kaleoTimerInstanceToken != null) {
			return kaleoTimerInstanceToken;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoInstanceTokenId=");
		sb.append(kaleoInstanceTokenId);

		sb.append(", completed=");
		sb.append(completed);

		sb.append("}");

		throw new NoSuchTimerInstanceTokenException(sb.toString());
	}

	/**
	 * Returns the first kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKITI_C_First(
		long kaleoInstanceTokenId, boolean completed,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		List<KaleoTimerInstanceToken> list = findByKITI_C(
			kaleoInstanceTokenId, completed, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKITI_C_Last(
			long kaleoInstanceTokenId, boolean completed,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = fetchByKITI_C_Last(
			kaleoInstanceTokenId, completed, orderByComparator);

		if (kaleoTimerInstanceToken != null) {
			return kaleoTimerInstanceToken;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoInstanceTokenId=");
		sb.append(kaleoInstanceTokenId);

		sb.append(", completed=");
		sb.append(completed);

		sb.append("}");

		throw new NoSuchTimerInstanceTokenException(sb.toString());
	}

	/**
	 * Returns the last kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKITI_C_Last(
		long kaleoInstanceTokenId, boolean completed,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		int count = countByKITI_C(kaleoInstanceTokenId, completed);

		if (count == 0) {
			return null;
		}

		List<KaleoTimerInstanceToken> list = findByKITI_C(
			kaleoInstanceTokenId, completed, count - 1, count,
			orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the kaleo timer instance tokens before and after the current kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the current kaleo timer instance token
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken[] findByKITI_C_PrevAndNext(
			long kaleoTimerInstanceTokenId, long kaleoInstanceTokenId,
			boolean completed,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = findByPrimaryKey(
			kaleoTimerInstanceTokenId);

		Session session = null;

		try {
			session = openSession();

			KaleoTimerInstanceToken[] array =
				new KaleoTimerInstanceTokenImpl[3];

			array[0] = getByKITI_C_PrevAndNext(
				session, kaleoTimerInstanceToken, kaleoInstanceTokenId,
				completed, orderByComparator, true);

			array[1] = kaleoTimerInstanceToken;

			array[2] = getByKITI_C_PrevAndNext(
				session, kaleoTimerInstanceToken, kaleoInstanceTokenId,
				completed, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected KaleoTimerInstanceToken getByKITI_C_PrevAndNext(
		Session session, KaleoTimerInstanceToken kaleoTimerInstanceToken,
		long kaleoInstanceTokenId, boolean completed,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

		sb.append(_FINDER_COLUMN_KITI_C_KALEOINSTANCETOKENID_2);

		sb.append(_FINDER_COLUMN_KITI_C_COMPLETED_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(kaleoInstanceTokenId);

		queryPos.add(completed);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						kaleoTimerInstanceToken)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<KaleoTimerInstanceToken> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and completed = &#63; from the database.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 */
	@Override
	public void removeByKITI_C(long kaleoInstanceTokenId, boolean completed) {
		for (KaleoTimerInstanceToken kaleoTimerInstanceToken :
				findByKITI_C(
					kaleoInstanceTokenId, completed, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(kaleoTimerInstanceToken);
		}
	}

	/**
	 * Returns the number of kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @return the number of matching kaleo timer instance tokens
	 */
	@Override
	public int countByKITI_C(long kaleoInstanceTokenId, boolean completed) {
		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByKITI_C");

		Object[] finderArgs = new Object[] {kaleoInstanceTokenId, completed};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KITI_C_KALEOINSTANCETOKENID_2);

			sb.append(_FINDER_COLUMN_KITI_C_COMPLETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceTokenId);

				queryPos.add(completed);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_KITI_C_KALEOINSTANCETOKENID_2 =
		"kaleoTimerInstanceToken.kaleoInstanceTokenId = ? AND ";

	private static final String _FINDER_COLUMN_KITI_C_COMPLETED_2 =
		"kaleoTimerInstanceToken.completed = ?";

	/**
	 * Returns all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @return the matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_B_C(
		long kaleoInstanceTokenId, boolean blocking, boolean completed) {

		return findByKITI_B_C(
			kaleoInstanceTokenId, blocking, completed, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @return the range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_B_C(
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		int start, int end) {

		return findByKITI_B_C(
			kaleoInstanceTokenId, blocking, completed, start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_B_C(
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		return findByKITI_B_C(
			kaleoInstanceTokenId, blocking, completed, start, end,
			orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_B_C(
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
					"findByKITI_B_C");
				finderArgs = new Object[] {
					kaleoInstanceTokenId, blocking, completed
				};
			}
		}
		else if (useFinderCache) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByKITI_B_C");
			finderArgs = new Object[] {
				kaleoInstanceTokenId, blocking, completed, start, end,
				orderByComparator
			};
		}

		List<KaleoTimerInstanceToken> list = null;

		if (useFinderCache) {
			list = (List<KaleoTimerInstanceToken>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (KaleoTimerInstanceToken kaleoTimerInstanceToken : list) {
					if ((kaleoInstanceTokenId !=
							kaleoTimerInstanceToken.
								getKaleoInstanceTokenId()) ||
						(blocking != kaleoTimerInstanceToken.isBlocking()) ||
						(completed != kaleoTimerInstanceToken.isCompleted())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					5 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(5);
			}

			sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KITI_B_C_KALEOINSTANCETOKENID_2);

			sb.append(_FINDER_COLUMN_KITI_B_C_BLOCKING_2);

			sb.append(_FINDER_COLUMN_KITI_B_C_COMPLETED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceTokenId);

				queryPos.add(blocking);

				queryPos.add(completed);

				list = (List<KaleoTimerInstanceToken>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKITI_B_C_First(
			long kaleoInstanceTokenId, boolean blocking, boolean completed,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = fetchByKITI_B_C_First(
			kaleoInstanceTokenId, blocking, completed, orderByComparator);

		if (kaleoTimerInstanceToken != null) {
			return kaleoTimerInstanceToken;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoInstanceTokenId=");
		sb.append(kaleoInstanceTokenId);

		sb.append(", blocking=");
		sb.append(blocking);

		sb.append(", completed=");
		sb.append(completed);

		sb.append("}");

		throw new NoSuchTimerInstanceTokenException(sb.toString());
	}

	/**
	 * Returns the first kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKITI_B_C_First(
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		List<KaleoTimerInstanceToken> list = findByKITI_B_C(
			kaleoInstanceTokenId, blocking, completed, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKITI_B_C_Last(
			long kaleoInstanceTokenId, boolean blocking, boolean completed,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = fetchByKITI_B_C_Last(
			kaleoInstanceTokenId, blocking, completed, orderByComparator);

		if (kaleoTimerInstanceToken != null) {
			return kaleoTimerInstanceToken;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoInstanceTokenId=");
		sb.append(kaleoInstanceTokenId);

		sb.append(", blocking=");
		sb.append(blocking);

		sb.append(", completed=");
		sb.append(completed);

		sb.append("}");

		throw new NoSuchTimerInstanceTokenException(sb.toString());
	}

	/**
	 * Returns the last kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKITI_B_C_Last(
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		int count = countByKITI_B_C(kaleoInstanceTokenId, blocking, completed);

		if (count == 0) {
			return null;
		}

		List<KaleoTimerInstanceToken> list = findByKITI_B_C(
			kaleoInstanceTokenId, blocking, completed, count - 1, count,
			orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the kaleo timer instance tokens before and after the current kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the current kaleo timer instance token
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken[] findByKITI_B_C_PrevAndNext(
			long kaleoTimerInstanceTokenId, long kaleoInstanceTokenId,
			boolean blocking, boolean completed,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = findByPrimaryKey(
			kaleoTimerInstanceTokenId);

		Session session = null;

		try {
			session = openSession();

			KaleoTimerInstanceToken[] array =
				new KaleoTimerInstanceTokenImpl[3];

			array[0] = getByKITI_B_C_PrevAndNext(
				session, kaleoTimerInstanceToken, kaleoInstanceTokenId,
				blocking, completed, orderByComparator, true);

			array[1] = kaleoTimerInstanceToken;

			array[2] = getByKITI_B_C_PrevAndNext(
				session, kaleoTimerInstanceToken, kaleoInstanceTokenId,
				blocking, completed, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected KaleoTimerInstanceToken getByKITI_B_C_PrevAndNext(
		Session session, KaleoTimerInstanceToken kaleoTimerInstanceToken,
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

		sb.append(_FINDER_COLUMN_KITI_B_C_KALEOINSTANCETOKENID_2);

		sb.append(_FINDER_COLUMN_KITI_B_C_BLOCKING_2);

		sb.append(_FINDER_COLUMN_KITI_B_C_COMPLETED_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(kaleoInstanceTokenId);

		queryPos.add(blocking);

		queryPos.add(completed);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						kaleoTimerInstanceToken)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<KaleoTimerInstanceToken> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63; from the database.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 */
	@Override
	public void removeByKITI_B_C(
		long kaleoInstanceTokenId, boolean blocking, boolean completed) {

		for (KaleoTimerInstanceToken kaleoTimerInstanceToken :
				findByKITI_B_C(
					kaleoInstanceTokenId, blocking, completed,
					QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(kaleoTimerInstanceToken);
		}
	}

	/**
	 * Returns the number of kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @return the number of matching kaleo timer instance tokens
	 */
	@Override
	public int countByKITI_B_C(
		long kaleoInstanceTokenId, boolean blocking, boolean completed) {

		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByKITI_B_C");

		Object[] finderArgs = new Object[] {
			kaleoInstanceTokenId, blocking, completed
		};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KITI_B_C_KALEOINSTANCETOKENID_2);

			sb.append(_FINDER_COLUMN_KITI_B_C_BLOCKING_2);

			sb.append(_FINDER_COLUMN_KITI_B_C_COMPLETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceTokenId);

				queryPos.add(blocking);

				queryPos.add(completed);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_KITI_B_C_KALEOINSTANCETOKENID_2 =
		"kaleoTimerInstanceToken.kaleoInstanceTokenId = ? AND ";

	private static final String _FINDER_COLUMN_KITI_B_C_BLOCKING_2 =
		"kaleoTimerInstanceToken.blocking = ? AND ";

	private static final String _FINDER_COLUMN_KITI_B_C_COMPLETED_2 =
		"kaleoTimerInstanceToken.completed = ?";

	public KaleoTimerInstanceTokenPersistenceImpl() {
		setModelClass(KaleoTimerInstanceToken.class);

		setModelImplClass(KaleoTimerInstanceTokenImpl.class);
		setModelPKClass(long.class);

		setTable(KaleoTimerInstanceTokenTable.INSTANCE);
	}

	/**
	 * Caches the kaleo timer instance token in the entity cache if it is enabled.
	 *
	 * @param kaleoTimerInstanceToken the kaleo timer instance token
	 */
	@Override
	public void cacheResult(KaleoTimerInstanceToken kaleoTimerInstanceToken) {
		entityCache.putResult(
			entityCacheEnabled, KaleoTimerInstanceTokenImpl.class,
			kaleoTimerInstanceToken.getPrimaryKey(), kaleoTimerInstanceToken,
			new Object[] {
				_columnBitmaskEnabled,
				((KaleoTimerInstanceTokenModelImpl)kaleoTimerInstanceToken).
					getColumnBitmask()
			});

		finderCache.putResult(
			_getFinderPath(FINDER_CLASS_NAME_ENTITY, "fetchByKITI_KTI"),
			new Object[] {
				kaleoTimerInstanceToken.getKaleoInstanceTokenId(),
				kaleoTimerInstanceToken.getKaleoTimerId()
			},
			kaleoTimerInstanceToken);

		kaleoTimerInstanceToken.resetOriginalValues();
	}

	/**
	 * Caches the kaleo timer instance tokens in the entity cache if it is enabled.
	 *
	 * @param kaleoTimerInstanceTokens the kaleo timer instance tokens
	 */
	@Override
	public void cacheResult(
		List<KaleoTimerInstanceToken> kaleoTimerInstanceTokens) {

		for (KaleoTimerInstanceToken kaleoTimerInstanceToken :
				kaleoTimerInstanceTokens) {

			if (entityCache.getResult(
					entityCacheEnabled, KaleoTimerInstanceTokenImpl.class,
					kaleoTimerInstanceToken.getPrimaryKey()) == null) {

				cacheResult(kaleoTimerInstanceToken);
			}
			else {
				kaleoTimerInstanceToken.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all kaleo timer instance tokens.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(KaleoTimerInstanceTokenImpl.class);
	}

	/**
	 * Clears the cache for the kaleo timer instance token.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(KaleoTimerInstanceToken kaleoTimerInstanceToken) {
		entityCache.removeResult(
			entityCacheEnabled, KaleoTimerInstanceTokenImpl.class,
			kaleoTimerInstanceToken.getPrimaryKey(), kaleoTimerInstanceToken,
			new Object[] {
				_columnBitmaskEnabled,
				((KaleoTimerInstanceTokenModelImpl)kaleoTimerInstanceToken).
					getColumnBitmask()
			});
	}

	@Override
	public void clearCache(
		List<KaleoTimerInstanceToken> kaleoTimerInstanceTokens) {

		for (KaleoTimerInstanceToken kaleoTimerInstanceToken :
				kaleoTimerInstanceTokens) {

			entityCache.removeResult(
				entityCacheEnabled, KaleoTimerInstanceTokenImpl.class,
				kaleoTimerInstanceToken.getPrimaryKey(),
				kaleoTimerInstanceToken,
				new Object[] {
					_columnBitmaskEnabled,
					((KaleoTimerInstanceTokenModelImpl)kaleoTimerInstanceToken).
						getColumnBitmask()
				});
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				entityCacheEnabled, KaleoTimerInstanceTokenImpl.class,
				primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		KaleoTimerInstanceTokenModelImpl kaleoTimerInstanceTokenModelImpl) {

		Object[] args = new Object[] {
			kaleoTimerInstanceTokenModelImpl.getKaleoInstanceTokenId(),
			kaleoTimerInstanceTokenModelImpl.getKaleoTimerId()
		};

		finderCache.putResult(
			_getFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByKITI_KTI"),
			args, Long.valueOf(1), false);
		finderCache.putResult(
			_getFinderPath(FINDER_CLASS_NAME_ENTITY, "fetchByKITI_KTI"), args,
			kaleoTimerInstanceTokenModelImpl, false);
	}

	/**
	 * Creates a new kaleo timer instance token with the primary key. Does not add the kaleo timer instance token to the database.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key for the new kaleo timer instance token
	 * @return the new kaleo timer instance token
	 */
	@Override
	public KaleoTimerInstanceToken create(long kaleoTimerInstanceTokenId) {
		KaleoTimerInstanceToken kaleoTimerInstanceToken =
			new KaleoTimerInstanceTokenImpl();

		kaleoTimerInstanceToken.setNew(true);
		kaleoTimerInstanceToken.setPrimaryKey(kaleoTimerInstanceTokenId);

		kaleoTimerInstanceToken.setCompanyId(CompanyThreadLocal.getCompanyId());

		return kaleoTimerInstanceToken;
	}

	/**
	 * Removes the kaleo timer instance token with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the kaleo timer instance token
	 * @return the kaleo timer instance token that was removed
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken remove(long kaleoTimerInstanceTokenId)
		throws NoSuchTimerInstanceTokenException {

		return remove((Serializable)kaleoTimerInstanceTokenId);
	}

	/**
	 * Removes the kaleo timer instance token with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the kaleo timer instance token
	 * @return the kaleo timer instance token that was removed
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken remove(Serializable primaryKey)
		throws NoSuchTimerInstanceTokenException {

		Session session = null;

		try {
			session = openSession();

			KaleoTimerInstanceToken kaleoTimerInstanceToken =
				(KaleoTimerInstanceToken)session.get(
					KaleoTimerInstanceTokenImpl.class, primaryKey);

			if (kaleoTimerInstanceToken == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchTimerInstanceTokenException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(kaleoTimerInstanceToken);
		}
		catch (NoSuchTimerInstanceTokenException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected KaleoTimerInstanceToken removeImpl(
		KaleoTimerInstanceToken kaleoTimerInstanceToken) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(kaleoTimerInstanceToken)) {
				kaleoTimerInstanceToken = (KaleoTimerInstanceToken)session.get(
					KaleoTimerInstanceTokenImpl.class,
					kaleoTimerInstanceToken.getPrimaryKeyObj());
			}

			if (kaleoTimerInstanceToken != null) {
				session.delete(kaleoTimerInstanceToken);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (kaleoTimerInstanceToken != null) {
			clearCache(kaleoTimerInstanceToken);
		}

		return kaleoTimerInstanceToken;
	}

	@Override
	public KaleoTimerInstanceToken updateImpl(
		KaleoTimerInstanceToken kaleoTimerInstanceToken) {

		boolean isNew = kaleoTimerInstanceToken.isNew();

		if (!(kaleoTimerInstanceToken instanceof
				KaleoTimerInstanceTokenModelImpl)) {

			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(kaleoTimerInstanceToken.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					kaleoTimerInstanceToken);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in kaleoTimerInstanceToken proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom KaleoTimerInstanceToken implementation " +
					kaleoTimerInstanceToken.getClass());
		}

		KaleoTimerInstanceTokenModelImpl kaleoTimerInstanceTokenModelImpl =
			(KaleoTimerInstanceTokenModelImpl)kaleoTimerInstanceToken;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (kaleoTimerInstanceToken.getCreateDate() == null)) {
			if (serviceContext == null) {
				kaleoTimerInstanceToken.setCreateDate(now);
			}
			else {
				kaleoTimerInstanceToken.setCreateDate(
					serviceContext.getCreateDate(now));
			}
		}

		if (!kaleoTimerInstanceTokenModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				kaleoTimerInstanceToken.setModifiedDate(now);
			}
			else {
				kaleoTimerInstanceToken.setModifiedDate(
					serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(kaleoTimerInstanceToken);
			}
			else {
				kaleoTimerInstanceToken =
					(KaleoTimerInstanceToken)session.merge(
						kaleoTimerInstanceToken);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			entityCacheEnabled, KaleoTimerInstanceTokenImpl.class,
			kaleoTimerInstanceTokenModelImpl.getPrimaryKey(),
			kaleoTimerInstanceTokenModelImpl, false,
			new Object[] {
				_columnBitmaskEnabled,
				kaleoTimerInstanceTokenModelImpl.getColumnBitmask()
			});

		cacheUniqueFindersCache(kaleoTimerInstanceTokenModelImpl);

		kaleoTimerInstanceToken.resetOriginalValues();

		if (isNew) {
			kaleoTimerInstanceToken.setNew(false);
		}

		return kaleoTimerInstanceToken;
	}

	/**
	 * Returns the kaleo timer instance token with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the kaleo timer instance token
	 * @return the kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByPrimaryKey(Serializable primaryKey)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = fetchByPrimaryKey(
			primaryKey);

		if (kaleoTimerInstanceToken == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchTimerInstanceTokenException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return kaleoTimerInstanceToken;
	}

	/**
	 * Returns the kaleo timer instance token with the primary key or throws a <code>NoSuchTimerInstanceTokenException</code> if it could not be found.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the kaleo timer instance token
	 * @return the kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByPrimaryKey(
			long kaleoTimerInstanceTokenId)
		throws NoSuchTimerInstanceTokenException {

		return findByPrimaryKey((Serializable)kaleoTimerInstanceTokenId);
	}

	/**
	 * Returns the kaleo timer instance token with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the kaleo timer instance token
	 * @return the kaleo timer instance token, or <code>null</code> if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByPrimaryKey(
		long kaleoTimerInstanceTokenId) {

		return fetchByPrimaryKey((Serializable)kaleoTimerInstanceTokenId);
	}

	/**
	 * Returns all the kaleo timer instance tokens.
	 *
	 * @return the kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the kaleo timer instance tokens.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @return the range of kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findAll(
		int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findAll(
		int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll");
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll");
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<KaleoTimerInstanceToken> list = null;

		if (useFinderCache) {
			list = (List<KaleoTimerInstanceToken>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_KALEOTIMERINSTANCETOKEN;

				sql = sql.concat(
					KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<KaleoTimerInstanceToken>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the kaleo timer instance tokens from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (KaleoTimerInstanceToken kaleoTimerInstanceToken : findAll()) {
			remove(kaleoTimerInstanceToken);
		}
	}

	/**
	 * Returns the number of kaleo timer instance tokens.
	 *
	 * @return the number of kaleo timer instance tokens
	 */
	@Override
	public int countAll() {
		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll");

		Long count = (Long)finderCache.getResult(
			finderPath, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(
					_SQL_COUNT_KALEOTIMERINSTANCETOKEN);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, FINDER_ARGS_EMPTY);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "kaleoTimerInstanceTokenId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_KALEOTIMERINSTANCETOKEN;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return KaleoTimerInstanceTokenModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the kaleo timer instance token persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		KaleoTimerInstanceTokenModelImpl.setEntityCacheEnabled(
			entityCacheEnabled);
		KaleoTimerInstanceTokenModelImpl.setFinderCacheEnabled(
			finderCacheEnabled);

		_bundleContext = bundleContext;
		Bundle bundle = FrameworkUtil.getBundle(
			KaleoTimerInstanceTokenPersistenceImpl.class);

		_bundleContext = bundle.getBundleContext();
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(KaleoTimerInstanceTokenImpl.class.getName());

		for (ServiceRegistration<FinderPath> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	@Override
	@Reference(
		target = KaleoPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.workflow.kaleo.model.KaleoTimerInstanceToken"),
			true);
	}

	@Override
	@Reference(
		target = KaleoPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = KaleoPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private boolean _columnBitmaskEnabled;
	private BundleContext _bundleContext;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_KALEOTIMERINSTANCETOKEN =
		"SELECT kaleoTimerInstanceToken FROM KaleoTimerInstanceToken kaleoTimerInstanceToken";

	private static final String _SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE =
		"SELECT kaleoTimerInstanceToken FROM KaleoTimerInstanceToken kaleoTimerInstanceToken WHERE ";

	private static final String _SQL_COUNT_KALEOTIMERINSTANCETOKEN =
		"SELECT COUNT(kaleoTimerInstanceToken) FROM KaleoTimerInstanceToken kaleoTimerInstanceToken";

	private static final String _SQL_COUNT_KALEOTIMERINSTANCETOKEN_WHERE =
		"SELECT COUNT(kaleoTimerInstanceToken) FROM KaleoTimerInstanceToken kaleoTimerInstanceToken WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS =
		"kaleoTimerInstanceToken.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No KaleoTimerInstanceToken exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No KaleoTimerInstanceToken exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		KaleoTimerInstanceTokenPersistenceImpl.class);

	static {
		try {
			Class.forName(KaleoPersistenceConstants.class.getName());
		}
		catch (ClassNotFoundException classNotFoundException) {
			throw new ExceptionInInitializerError(classNotFoundException);
		}
	}

	private FinderPath _getFinderPath(String cacheName, String methodName) {
		if (!finderCacheEnabled) {
			return null;
		}

		return _finderPathMap.computeIfAbsent(
			StringBundler.concat(cacheName, "_", methodName),
			key -> {
				Class<?> returnClass = KaleoTimerInstanceTokenImpl.class;

				Object[] bitMaskArray = _COLUMN_BITMASK_ARRAY_MAP.get(
					methodName);

				if (methodName.startsWith("count")) {
					returnClass = Long.class;

					String methodNamePostfix = methodName.substring(5);

					bitMaskArray = _COLUMN_BITMASK_ARRAY_MAP.get(
						"find" + methodNamePostfix);

					if (bitMaskArray == null) {
						bitMaskArray = _COLUMN_BITMASK_ARRAY_MAP.get(
							"fetch" + methodNamePostfix);
					}
				}

				FinderPath finderPath = null;

				if ((bitMaskArray == null) || (bitMaskArray.length != 3)) {
					finderPath = new FinderPath(
						entityCacheEnabled, true, returnClass, cacheName,
						methodName, new String[0]);
				}
				else {
					finderPath = new FinderPath(
						entityCacheEnabled, true, returnClass, cacheName,
						methodName, new String[0], (long)bitMaskArray[0],
						(Function<BaseModel<?>, Object[]>)bitMaskArray[1],
						(Function<BaseModel<?>, Object[]>)bitMaskArray[2]);
				}

				if (!cacheName.equals(FINDER_CLASS_NAME_LIST_WITH_PAGINATION)) {
					_serviceRegistrations.add(
						_bundleContext.registerService(
							FinderPath.class, finderPath,
							MapUtil.singletonDictionary(
								"cache.name", cacheName)));
				}

				return finderPath;
			});
	}

	private Map<String, FinderPath> _finderPathMap = new ConcurrentHashMap<>();
	private Set<ServiceRegistration<FinderPath>> _serviceRegistrations =
		Collections.newSetFromMap(new ConcurrentHashMap<>());

	private static final Map<String, Object[]> _COLUMN_BITMASK_ARRAY_MAP =
		new HashMap<>();

	static {
		_COLUMN_BITMASK_ARRAY_MAP.put(
			"findByKaleoInstanceId",
			new Object[] {
				KaleoTimerInstanceTokenModelImpl.KALEOINSTANCEID_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					KaleoTimerInstanceTokenModelImpl
						kaleoTimerInstanceTokenModelImpl =
							(KaleoTimerInstanceTokenModelImpl)baseModel;

					return new Object[] {
						kaleoTimerInstanceTokenModelImpl.getKaleoInstanceId()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					KaleoTimerInstanceTokenModelImpl
						kaleoTimerInstanceTokenModelImpl =
							(KaleoTimerInstanceTokenModelImpl)baseModel;

					return new Object[] {
						kaleoTimerInstanceTokenModelImpl.
							getOriginalKaleoInstanceId()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"fetchByKITI_KTI",
			new Object[] {
				KaleoTimerInstanceTokenModelImpl.
					KALEOINSTANCETOKENID_COLUMN_BITMASK |
				KaleoTimerInstanceTokenModelImpl.KALEOTIMERID_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					KaleoTimerInstanceTokenModelImpl
						kaleoTimerInstanceTokenModelImpl =
							(KaleoTimerInstanceTokenModelImpl)baseModel;

					return new Object[] {
						kaleoTimerInstanceTokenModelImpl.
							getKaleoInstanceTokenId(),
						kaleoTimerInstanceTokenModelImpl.getKaleoTimerId()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					KaleoTimerInstanceTokenModelImpl
						kaleoTimerInstanceTokenModelImpl =
							(KaleoTimerInstanceTokenModelImpl)baseModel;

					return new Object[] {
						kaleoTimerInstanceTokenModelImpl.
							getOriginalKaleoInstanceTokenId(),
						kaleoTimerInstanceTokenModelImpl.
							getOriginalKaleoTimerId()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"findByKITI_C",
			new Object[] {
				KaleoTimerInstanceTokenModelImpl.
					KALEOINSTANCETOKENID_COLUMN_BITMASK |
				KaleoTimerInstanceTokenModelImpl.COMPLETED_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					KaleoTimerInstanceTokenModelImpl
						kaleoTimerInstanceTokenModelImpl =
							(KaleoTimerInstanceTokenModelImpl)baseModel;

					return new Object[] {
						kaleoTimerInstanceTokenModelImpl.
							getKaleoInstanceTokenId(),
						kaleoTimerInstanceTokenModelImpl.isCompleted()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					KaleoTimerInstanceTokenModelImpl
						kaleoTimerInstanceTokenModelImpl =
							(KaleoTimerInstanceTokenModelImpl)baseModel;

					return new Object[] {
						kaleoTimerInstanceTokenModelImpl.
							getOriginalKaleoInstanceTokenId(),
						kaleoTimerInstanceTokenModelImpl.getOriginalCompleted()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"findByKITI_B_C",
			new Object[] {
				KaleoTimerInstanceTokenModelImpl.
					KALEOINSTANCETOKENID_COLUMN_BITMASK |
				KaleoTimerInstanceTokenModelImpl.BLOCKING_COLUMN_BITMASK |
				KaleoTimerInstanceTokenModelImpl.COMPLETED_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					KaleoTimerInstanceTokenModelImpl
						kaleoTimerInstanceTokenModelImpl =
							(KaleoTimerInstanceTokenModelImpl)baseModel;

					return new Object[] {
						kaleoTimerInstanceTokenModelImpl.
							getKaleoInstanceTokenId(),
						kaleoTimerInstanceTokenModelImpl.isBlocking(),
						kaleoTimerInstanceTokenModelImpl.isCompleted()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					KaleoTimerInstanceTokenModelImpl
						kaleoTimerInstanceTokenModelImpl =
							(KaleoTimerInstanceTokenModelImpl)baseModel;

					return new Object[] {
						kaleoTimerInstanceTokenModelImpl.
							getOriginalKaleoInstanceTokenId(),
						kaleoTimerInstanceTokenModelImpl.getOriginalBlocking(),
						kaleoTimerInstanceTokenModelImpl.getOriginalCompleted()
					};
				}
			});
	}

}
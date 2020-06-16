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

package com.liferay.dynamic.data.lists.service.persistence.impl;

import com.liferay.dynamic.data.lists.exception.NoSuchRecordVersionException;
import com.liferay.dynamic.data.lists.model.DDLRecordVersion;
import com.liferay.dynamic.data.lists.model.DDLRecordVersionTable;
import com.liferay.dynamic.data.lists.model.impl.DDLRecordVersionImpl;
import com.liferay.dynamic.data.lists.model.impl.DDLRecordVersionModelImpl;
import com.liferay.dynamic.data.lists.service.persistence.DDLRecordVersionPersistence;
import com.liferay.dynamic.data.lists.service.persistence.impl.constants.DDLPersistenceConstants;
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
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
 * The persistence implementation for the ddl record version service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = DDLRecordVersionPersistence.class)
public class DDLRecordVersionPersistenceImpl
	extends BasePersistenceImpl<DDLRecordVersion>
	implements DDLRecordVersionPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>DDLRecordVersionUtil</code> to access the ddl record version persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		DDLRecordVersionImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	/**
	 * Returns all the ddl record versions where recordId = &#63;.
	 *
	 * @param recordId the record ID
	 * @return the matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByRecordId(long recordId) {
		return findByRecordId(
			recordId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the ddl record versions where recordId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param recordId the record ID
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @return the range of matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByRecordId(
		long recordId, int start, int end) {

		return findByRecordId(recordId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the ddl record versions where recordId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param recordId the record ID
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByRecordId(
		long recordId, int start, int end,
		OrderByComparator<DDLRecordVersion> orderByComparator) {

		return findByRecordId(recordId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the ddl record versions where recordId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param recordId the record ID
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByRecordId(
		long recordId, int start, int end,
		OrderByComparator<DDLRecordVersion> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
					"findByRecordId");
				finderArgs = new Object[] {recordId};
			}
		}
		else if (useFinderCache) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByRecordId");
			finderArgs = new Object[] {recordId, start, end, orderByComparator};
		}

		List<DDLRecordVersion> list = null;

		if (useFinderCache) {
			list = (List<DDLRecordVersion>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (DDLRecordVersion ddlRecordVersion : list) {
					if (recordId != ddlRecordVersion.getRecordId()) {
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

			sb.append(_SQL_SELECT_DDLRECORDVERSION_WHERE);

			sb.append(_FINDER_COLUMN_RECORDID_RECORDID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(DDLRecordVersionModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(recordId);

				list = (List<DDLRecordVersion>)QueryUtil.list(
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
	 * Returns the first ddl record version in the ordered set where recordId = &#63;.
	 *
	 * @param recordId the record ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ddl record version
	 * @throws NoSuchRecordVersionException if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion findByRecordId_First(
			long recordId,
			OrderByComparator<DDLRecordVersion> orderByComparator)
		throws NoSuchRecordVersionException {

		DDLRecordVersion ddlRecordVersion = fetchByRecordId_First(
			recordId, orderByComparator);

		if (ddlRecordVersion != null) {
			return ddlRecordVersion;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("recordId=");
		sb.append(recordId);

		sb.append("}");

		throw new NoSuchRecordVersionException(sb.toString());
	}

	/**
	 * Returns the first ddl record version in the ordered set where recordId = &#63;.
	 *
	 * @param recordId the record ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ddl record version, or <code>null</code> if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion fetchByRecordId_First(
		long recordId, OrderByComparator<DDLRecordVersion> orderByComparator) {

		List<DDLRecordVersion> list = findByRecordId(
			recordId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last ddl record version in the ordered set where recordId = &#63;.
	 *
	 * @param recordId the record ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ddl record version
	 * @throws NoSuchRecordVersionException if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion findByRecordId_Last(
			long recordId,
			OrderByComparator<DDLRecordVersion> orderByComparator)
		throws NoSuchRecordVersionException {

		DDLRecordVersion ddlRecordVersion = fetchByRecordId_Last(
			recordId, orderByComparator);

		if (ddlRecordVersion != null) {
			return ddlRecordVersion;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("recordId=");
		sb.append(recordId);

		sb.append("}");

		throw new NoSuchRecordVersionException(sb.toString());
	}

	/**
	 * Returns the last ddl record version in the ordered set where recordId = &#63;.
	 *
	 * @param recordId the record ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ddl record version, or <code>null</code> if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion fetchByRecordId_Last(
		long recordId, OrderByComparator<DDLRecordVersion> orderByComparator) {

		int count = countByRecordId(recordId);

		if (count == 0) {
			return null;
		}

		List<DDLRecordVersion> list = findByRecordId(
			recordId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the ddl record versions before and after the current ddl record version in the ordered set where recordId = &#63;.
	 *
	 * @param recordVersionId the primary key of the current ddl record version
	 * @param recordId the record ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ddl record version
	 * @throws NoSuchRecordVersionException if a ddl record version with the primary key could not be found
	 */
	@Override
	public DDLRecordVersion[] findByRecordId_PrevAndNext(
			long recordVersionId, long recordId,
			OrderByComparator<DDLRecordVersion> orderByComparator)
		throws NoSuchRecordVersionException {

		DDLRecordVersion ddlRecordVersion = findByPrimaryKey(recordVersionId);

		Session session = null;

		try {
			session = openSession();

			DDLRecordVersion[] array = new DDLRecordVersionImpl[3];

			array[0] = getByRecordId_PrevAndNext(
				session, ddlRecordVersion, recordId, orderByComparator, true);

			array[1] = ddlRecordVersion;

			array[2] = getByRecordId_PrevAndNext(
				session, ddlRecordVersion, recordId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected DDLRecordVersion getByRecordId_PrevAndNext(
		Session session, DDLRecordVersion ddlRecordVersion, long recordId,
		OrderByComparator<DDLRecordVersion> orderByComparator,
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

		sb.append(_SQL_SELECT_DDLRECORDVERSION_WHERE);

		sb.append(_FINDER_COLUMN_RECORDID_RECORDID_2);

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
			sb.append(DDLRecordVersionModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(recordId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						ddlRecordVersion)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<DDLRecordVersion> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the ddl record versions where recordId = &#63; from the database.
	 *
	 * @param recordId the record ID
	 */
	@Override
	public void removeByRecordId(long recordId) {
		for (DDLRecordVersion ddlRecordVersion :
				findByRecordId(
					recordId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(ddlRecordVersion);
		}
	}

	/**
	 * Returns the number of ddl record versions where recordId = &#63;.
	 *
	 * @param recordId the record ID
	 * @return the number of matching ddl record versions
	 */
	@Override
	public int countByRecordId(long recordId) {
		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByRecordId");

		Object[] finderArgs = new Object[] {recordId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_DDLRECORDVERSION_WHERE);

			sb.append(_FINDER_COLUMN_RECORDID_RECORDID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(recordId);

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

	private static final String _FINDER_COLUMN_RECORDID_RECORDID_2 =
		"ddlRecordVersion.recordId = ?";

	/**
	 * Returns all the ddl record versions where recordSetId = &#63; and recordSetVersion = &#63;.
	 *
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @return the matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByR_R(
		long recordSetId, String recordSetVersion) {

		return findByR_R(
			recordSetId, recordSetVersion, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the ddl record versions where recordSetId = &#63; and recordSetVersion = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @return the range of matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByR_R(
		long recordSetId, String recordSetVersion, int start, int end) {

		return findByR_R(recordSetId, recordSetVersion, start, end, null);
	}

	/**
	 * Returns an ordered range of all the ddl record versions where recordSetId = &#63; and recordSetVersion = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByR_R(
		long recordSetId, String recordSetVersion, int start, int end,
		OrderByComparator<DDLRecordVersion> orderByComparator) {

		return findByR_R(
			recordSetId, recordSetVersion, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the ddl record versions where recordSetId = &#63; and recordSetVersion = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByR_R(
		long recordSetId, String recordSetVersion, int start, int end,
		OrderByComparator<DDLRecordVersion> orderByComparator,
		boolean useFinderCache) {

		recordSetVersion = Objects.toString(recordSetVersion, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByR_R");
				finderArgs = new Object[] {recordSetId, recordSetVersion};
			}
		}
		else if (useFinderCache) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByR_R");
			finderArgs = new Object[] {
				recordSetId, recordSetVersion, start, end, orderByComparator
			};
		}

		List<DDLRecordVersion> list = null;

		if (useFinderCache) {
			list = (List<DDLRecordVersion>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (DDLRecordVersion ddlRecordVersion : list) {
					if ((recordSetId != ddlRecordVersion.getRecordSetId()) ||
						!recordSetVersion.equals(
							ddlRecordVersion.getRecordSetVersion())) {

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

			sb.append(_SQL_SELECT_DDLRECORDVERSION_WHERE);

			sb.append(_FINDER_COLUMN_R_R_RECORDSETID_2);

			boolean bindRecordSetVersion = false;

			if (recordSetVersion.isEmpty()) {
				sb.append(_FINDER_COLUMN_R_R_RECORDSETVERSION_3);
			}
			else {
				bindRecordSetVersion = true;

				sb.append(_FINDER_COLUMN_R_R_RECORDSETVERSION_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(DDLRecordVersionModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(recordSetId);

				if (bindRecordSetVersion) {
					queryPos.add(recordSetVersion);
				}

				list = (List<DDLRecordVersion>)QueryUtil.list(
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
	 * Returns the first ddl record version in the ordered set where recordSetId = &#63; and recordSetVersion = &#63;.
	 *
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ddl record version
	 * @throws NoSuchRecordVersionException if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion findByR_R_First(
			long recordSetId, String recordSetVersion,
			OrderByComparator<DDLRecordVersion> orderByComparator)
		throws NoSuchRecordVersionException {

		DDLRecordVersion ddlRecordVersion = fetchByR_R_First(
			recordSetId, recordSetVersion, orderByComparator);

		if (ddlRecordVersion != null) {
			return ddlRecordVersion;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("recordSetId=");
		sb.append(recordSetId);

		sb.append(", recordSetVersion=");
		sb.append(recordSetVersion);

		sb.append("}");

		throw new NoSuchRecordVersionException(sb.toString());
	}

	/**
	 * Returns the first ddl record version in the ordered set where recordSetId = &#63; and recordSetVersion = &#63;.
	 *
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ddl record version, or <code>null</code> if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion fetchByR_R_First(
		long recordSetId, String recordSetVersion,
		OrderByComparator<DDLRecordVersion> orderByComparator) {

		List<DDLRecordVersion> list = findByR_R(
			recordSetId, recordSetVersion, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last ddl record version in the ordered set where recordSetId = &#63; and recordSetVersion = &#63;.
	 *
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ddl record version
	 * @throws NoSuchRecordVersionException if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion findByR_R_Last(
			long recordSetId, String recordSetVersion,
			OrderByComparator<DDLRecordVersion> orderByComparator)
		throws NoSuchRecordVersionException {

		DDLRecordVersion ddlRecordVersion = fetchByR_R_Last(
			recordSetId, recordSetVersion, orderByComparator);

		if (ddlRecordVersion != null) {
			return ddlRecordVersion;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("recordSetId=");
		sb.append(recordSetId);

		sb.append(", recordSetVersion=");
		sb.append(recordSetVersion);

		sb.append("}");

		throw new NoSuchRecordVersionException(sb.toString());
	}

	/**
	 * Returns the last ddl record version in the ordered set where recordSetId = &#63; and recordSetVersion = &#63;.
	 *
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ddl record version, or <code>null</code> if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion fetchByR_R_Last(
		long recordSetId, String recordSetVersion,
		OrderByComparator<DDLRecordVersion> orderByComparator) {

		int count = countByR_R(recordSetId, recordSetVersion);

		if (count == 0) {
			return null;
		}

		List<DDLRecordVersion> list = findByR_R(
			recordSetId, recordSetVersion, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the ddl record versions before and after the current ddl record version in the ordered set where recordSetId = &#63; and recordSetVersion = &#63;.
	 *
	 * @param recordVersionId the primary key of the current ddl record version
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ddl record version
	 * @throws NoSuchRecordVersionException if a ddl record version with the primary key could not be found
	 */
	@Override
	public DDLRecordVersion[] findByR_R_PrevAndNext(
			long recordVersionId, long recordSetId, String recordSetVersion,
			OrderByComparator<DDLRecordVersion> orderByComparator)
		throws NoSuchRecordVersionException {

		recordSetVersion = Objects.toString(recordSetVersion, "");

		DDLRecordVersion ddlRecordVersion = findByPrimaryKey(recordVersionId);

		Session session = null;

		try {
			session = openSession();

			DDLRecordVersion[] array = new DDLRecordVersionImpl[3];

			array[0] = getByR_R_PrevAndNext(
				session, ddlRecordVersion, recordSetId, recordSetVersion,
				orderByComparator, true);

			array[1] = ddlRecordVersion;

			array[2] = getByR_R_PrevAndNext(
				session, ddlRecordVersion, recordSetId, recordSetVersion,
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

	protected DDLRecordVersion getByR_R_PrevAndNext(
		Session session, DDLRecordVersion ddlRecordVersion, long recordSetId,
		String recordSetVersion,
		OrderByComparator<DDLRecordVersion> orderByComparator,
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

		sb.append(_SQL_SELECT_DDLRECORDVERSION_WHERE);

		sb.append(_FINDER_COLUMN_R_R_RECORDSETID_2);

		boolean bindRecordSetVersion = false;

		if (recordSetVersion.isEmpty()) {
			sb.append(_FINDER_COLUMN_R_R_RECORDSETVERSION_3);
		}
		else {
			bindRecordSetVersion = true;

			sb.append(_FINDER_COLUMN_R_R_RECORDSETVERSION_2);
		}

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
			sb.append(DDLRecordVersionModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(recordSetId);

		if (bindRecordSetVersion) {
			queryPos.add(recordSetVersion);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						ddlRecordVersion)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<DDLRecordVersion> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the ddl record versions where recordSetId = &#63; and recordSetVersion = &#63; from the database.
	 *
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 */
	@Override
	public void removeByR_R(long recordSetId, String recordSetVersion) {
		for (DDLRecordVersion ddlRecordVersion :
				findByR_R(
					recordSetId, recordSetVersion, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(ddlRecordVersion);
		}
	}

	/**
	 * Returns the number of ddl record versions where recordSetId = &#63; and recordSetVersion = &#63;.
	 *
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @return the number of matching ddl record versions
	 */
	@Override
	public int countByR_R(long recordSetId, String recordSetVersion) {
		recordSetVersion = Objects.toString(recordSetVersion, "");

		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByR_R");

		Object[] finderArgs = new Object[] {recordSetId, recordSetVersion};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_DDLRECORDVERSION_WHERE);

			sb.append(_FINDER_COLUMN_R_R_RECORDSETID_2);

			boolean bindRecordSetVersion = false;

			if (recordSetVersion.isEmpty()) {
				sb.append(_FINDER_COLUMN_R_R_RECORDSETVERSION_3);
			}
			else {
				bindRecordSetVersion = true;

				sb.append(_FINDER_COLUMN_R_R_RECORDSETVERSION_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(recordSetId);

				if (bindRecordSetVersion) {
					queryPos.add(recordSetVersion);
				}

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

	private static final String _FINDER_COLUMN_R_R_RECORDSETID_2 =
		"ddlRecordVersion.recordSetId = ? AND ";

	private static final String _FINDER_COLUMN_R_R_RECORDSETVERSION_2 =
		"ddlRecordVersion.recordSetVersion = ?";

	private static final String _FINDER_COLUMN_R_R_RECORDSETVERSION_3 =
		"(ddlRecordVersion.recordSetVersion IS NULL OR ddlRecordVersion.recordSetVersion = '')";

	/**
	 * Returns the ddl record version where recordId = &#63; and version = &#63; or throws a <code>NoSuchRecordVersionException</code> if it could not be found.
	 *
	 * @param recordId the record ID
	 * @param version the version
	 * @return the matching ddl record version
	 * @throws NoSuchRecordVersionException if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion findByR_V(long recordId, String version)
		throws NoSuchRecordVersionException {

		DDLRecordVersion ddlRecordVersion = fetchByR_V(recordId, version);

		if (ddlRecordVersion == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("recordId=");
			sb.append(recordId);

			sb.append(", version=");
			sb.append(version);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchRecordVersionException(sb.toString());
		}

		return ddlRecordVersion;
	}

	/**
	 * Returns the ddl record version where recordId = &#63; and version = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param recordId the record ID
	 * @param version the version
	 * @return the matching ddl record version, or <code>null</code> if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion fetchByR_V(long recordId, String version) {
		return fetchByR_V(recordId, version, true);
	}

	/**
	 * Returns the ddl record version where recordId = &#63; and version = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param recordId the record ID
	 * @param version the version
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching ddl record version, or <code>null</code> if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion fetchByR_V(
		long recordId, String version, boolean useFinderCache) {

		version = Objects.toString(version, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {recordId, version};
		}

		Object result = null;

		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByR_V");

		if (useFinderCache) {
			result = finderCache.getResult(finderPath, finderArgs, this);
		}

		if (result instanceof DDLRecordVersion) {
			DDLRecordVersion ddlRecordVersion = (DDLRecordVersion)result;

			if ((recordId != ddlRecordVersion.getRecordId()) ||
				!Objects.equals(version, ddlRecordVersion.getVersion())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_DDLRECORDVERSION_WHERE);

			sb.append(_FINDER_COLUMN_R_V_RECORDID_2);

			boolean bindVersion = false;

			if (version.isEmpty()) {
				sb.append(_FINDER_COLUMN_R_V_VERSION_3);
			}
			else {
				bindVersion = true;

				sb.append(_FINDER_COLUMN_R_V_VERSION_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(recordId);

				if (bindVersion) {
					queryPos.add(version);
				}

				List<DDLRecordVersion> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(finderPath, finderArgs, list);
					}
				}
				else {
					DDLRecordVersion ddlRecordVersion = list.get(0);

					result = ddlRecordVersion;

					cacheResult(ddlRecordVersion);
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
			return (DDLRecordVersion)result;
		}
	}

	/**
	 * Removes the ddl record version where recordId = &#63; and version = &#63; from the database.
	 *
	 * @param recordId the record ID
	 * @param version the version
	 * @return the ddl record version that was removed
	 */
	@Override
	public DDLRecordVersion removeByR_V(long recordId, String version)
		throws NoSuchRecordVersionException {

		DDLRecordVersion ddlRecordVersion = findByR_V(recordId, version);

		return remove(ddlRecordVersion);
	}

	/**
	 * Returns the number of ddl record versions where recordId = &#63; and version = &#63;.
	 *
	 * @param recordId the record ID
	 * @param version the version
	 * @return the number of matching ddl record versions
	 */
	@Override
	public int countByR_V(long recordId, String version) {
		version = Objects.toString(version, "");

		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByR_V");

		Object[] finderArgs = new Object[] {recordId, version};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_DDLRECORDVERSION_WHERE);

			sb.append(_FINDER_COLUMN_R_V_RECORDID_2);

			boolean bindVersion = false;

			if (version.isEmpty()) {
				sb.append(_FINDER_COLUMN_R_V_VERSION_3);
			}
			else {
				bindVersion = true;

				sb.append(_FINDER_COLUMN_R_V_VERSION_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(recordId);

				if (bindVersion) {
					queryPos.add(version);
				}

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

	private static final String _FINDER_COLUMN_R_V_RECORDID_2 =
		"ddlRecordVersion.recordId = ? AND ";

	private static final String _FINDER_COLUMN_R_V_VERSION_2 =
		"ddlRecordVersion.version = ?";

	private static final String _FINDER_COLUMN_R_V_VERSION_3 =
		"(ddlRecordVersion.version IS NULL OR ddlRecordVersion.version = '')";

	/**
	 * Returns all the ddl record versions where recordId = &#63; and status = &#63;.
	 *
	 * @param recordId the record ID
	 * @param status the status
	 * @return the matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByR_S(long recordId, int status) {
		return findByR_S(
			recordId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the ddl record versions where recordId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param recordId the record ID
	 * @param status the status
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @return the range of matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByR_S(
		long recordId, int status, int start, int end) {

		return findByR_S(recordId, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the ddl record versions where recordId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param recordId the record ID
	 * @param status the status
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByR_S(
		long recordId, int status, int start, int end,
		OrderByComparator<DDLRecordVersion> orderByComparator) {

		return findByR_S(recordId, status, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the ddl record versions where recordId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param recordId the record ID
	 * @param status the status
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByR_S(
		long recordId, int status, int start, int end,
		OrderByComparator<DDLRecordVersion> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByR_S");
				finderArgs = new Object[] {recordId, status};
			}
		}
		else if (useFinderCache) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByR_S");
			finderArgs = new Object[] {
				recordId, status, start, end, orderByComparator
			};
		}

		List<DDLRecordVersion> list = null;

		if (useFinderCache) {
			list = (List<DDLRecordVersion>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (DDLRecordVersion ddlRecordVersion : list) {
					if ((recordId != ddlRecordVersion.getRecordId()) ||
						(status != ddlRecordVersion.getStatus())) {

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

			sb.append(_SQL_SELECT_DDLRECORDVERSION_WHERE);

			sb.append(_FINDER_COLUMN_R_S_RECORDID_2);

			sb.append(_FINDER_COLUMN_R_S_STATUS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(DDLRecordVersionModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(recordId);

				queryPos.add(status);

				list = (List<DDLRecordVersion>)QueryUtil.list(
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
	 * Returns the first ddl record version in the ordered set where recordId = &#63; and status = &#63;.
	 *
	 * @param recordId the record ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ddl record version
	 * @throws NoSuchRecordVersionException if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion findByR_S_First(
			long recordId, int status,
			OrderByComparator<DDLRecordVersion> orderByComparator)
		throws NoSuchRecordVersionException {

		DDLRecordVersion ddlRecordVersion = fetchByR_S_First(
			recordId, status, orderByComparator);

		if (ddlRecordVersion != null) {
			return ddlRecordVersion;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("recordId=");
		sb.append(recordId);

		sb.append(", status=");
		sb.append(status);

		sb.append("}");

		throw new NoSuchRecordVersionException(sb.toString());
	}

	/**
	 * Returns the first ddl record version in the ordered set where recordId = &#63; and status = &#63;.
	 *
	 * @param recordId the record ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ddl record version, or <code>null</code> if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion fetchByR_S_First(
		long recordId, int status,
		OrderByComparator<DDLRecordVersion> orderByComparator) {

		List<DDLRecordVersion> list = findByR_S(
			recordId, status, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last ddl record version in the ordered set where recordId = &#63; and status = &#63;.
	 *
	 * @param recordId the record ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ddl record version
	 * @throws NoSuchRecordVersionException if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion findByR_S_Last(
			long recordId, int status,
			OrderByComparator<DDLRecordVersion> orderByComparator)
		throws NoSuchRecordVersionException {

		DDLRecordVersion ddlRecordVersion = fetchByR_S_Last(
			recordId, status, orderByComparator);

		if (ddlRecordVersion != null) {
			return ddlRecordVersion;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("recordId=");
		sb.append(recordId);

		sb.append(", status=");
		sb.append(status);

		sb.append("}");

		throw new NoSuchRecordVersionException(sb.toString());
	}

	/**
	 * Returns the last ddl record version in the ordered set where recordId = &#63; and status = &#63;.
	 *
	 * @param recordId the record ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ddl record version, or <code>null</code> if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion fetchByR_S_Last(
		long recordId, int status,
		OrderByComparator<DDLRecordVersion> orderByComparator) {

		int count = countByR_S(recordId, status);

		if (count == 0) {
			return null;
		}

		List<DDLRecordVersion> list = findByR_S(
			recordId, status, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the ddl record versions before and after the current ddl record version in the ordered set where recordId = &#63; and status = &#63;.
	 *
	 * @param recordVersionId the primary key of the current ddl record version
	 * @param recordId the record ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ddl record version
	 * @throws NoSuchRecordVersionException if a ddl record version with the primary key could not be found
	 */
	@Override
	public DDLRecordVersion[] findByR_S_PrevAndNext(
			long recordVersionId, long recordId, int status,
			OrderByComparator<DDLRecordVersion> orderByComparator)
		throws NoSuchRecordVersionException {

		DDLRecordVersion ddlRecordVersion = findByPrimaryKey(recordVersionId);

		Session session = null;

		try {
			session = openSession();

			DDLRecordVersion[] array = new DDLRecordVersionImpl[3];

			array[0] = getByR_S_PrevAndNext(
				session, ddlRecordVersion, recordId, status, orderByComparator,
				true);

			array[1] = ddlRecordVersion;

			array[2] = getByR_S_PrevAndNext(
				session, ddlRecordVersion, recordId, status, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected DDLRecordVersion getByR_S_PrevAndNext(
		Session session, DDLRecordVersion ddlRecordVersion, long recordId,
		int status, OrderByComparator<DDLRecordVersion> orderByComparator,
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

		sb.append(_SQL_SELECT_DDLRECORDVERSION_WHERE);

		sb.append(_FINDER_COLUMN_R_S_RECORDID_2);

		sb.append(_FINDER_COLUMN_R_S_STATUS_2);

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
			sb.append(DDLRecordVersionModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(recordId);

		queryPos.add(status);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						ddlRecordVersion)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<DDLRecordVersion> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the ddl record versions where recordId = &#63; and status = &#63; from the database.
	 *
	 * @param recordId the record ID
	 * @param status the status
	 */
	@Override
	public void removeByR_S(long recordId, int status) {
		for (DDLRecordVersion ddlRecordVersion :
				findByR_S(
					recordId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(ddlRecordVersion);
		}
	}

	/**
	 * Returns the number of ddl record versions where recordId = &#63; and status = &#63;.
	 *
	 * @param recordId the record ID
	 * @param status the status
	 * @return the number of matching ddl record versions
	 */
	@Override
	public int countByR_S(long recordId, int status) {
		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByR_S");

		Object[] finderArgs = new Object[] {recordId, status};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_DDLRECORDVERSION_WHERE);

			sb.append(_FINDER_COLUMN_R_S_RECORDID_2);

			sb.append(_FINDER_COLUMN_R_S_STATUS_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(recordId);

				queryPos.add(status);

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

	private static final String _FINDER_COLUMN_R_S_RECORDID_2 =
		"ddlRecordVersion.recordId = ? AND ";

	private static final String _FINDER_COLUMN_R_S_STATUS_2 =
		"ddlRecordVersion.status = ?";

	/**
	 * Returns all the ddl record versions where userId = &#63; and recordSetId = &#63; and recordSetVersion = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param status the status
	 * @return the matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByU_R_R_S(
		long userId, long recordSetId, String recordSetVersion, int status) {

		return findByU_R_R_S(
			userId, recordSetId, recordSetVersion, status, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the ddl record versions where userId = &#63; and recordSetId = &#63; and recordSetVersion = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param status the status
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @return the range of matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByU_R_R_S(
		long userId, long recordSetId, String recordSetVersion, int status,
		int start, int end) {

		return findByU_R_R_S(
			userId, recordSetId, recordSetVersion, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the ddl record versions where userId = &#63; and recordSetId = &#63; and recordSetVersion = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param status the status
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByU_R_R_S(
		long userId, long recordSetId, String recordSetVersion, int status,
		int start, int end,
		OrderByComparator<DDLRecordVersion> orderByComparator) {

		return findByU_R_R_S(
			userId, recordSetId, recordSetVersion, status, start, end,
			orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the ddl record versions where userId = &#63; and recordSetId = &#63; and recordSetVersion = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param status the status
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findByU_R_R_S(
		long userId, long recordSetId, String recordSetVersion, int status,
		int start, int end,
		OrderByComparator<DDLRecordVersion> orderByComparator,
		boolean useFinderCache) {

		recordSetVersion = Objects.toString(recordSetVersion, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByU_R_R_S");
				finderArgs = new Object[] {
					userId, recordSetId, recordSetVersion, status
				};
			}
		}
		else if (useFinderCache) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByU_R_R_S");
			finderArgs = new Object[] {
				userId, recordSetId, recordSetVersion, status, start, end,
				orderByComparator
			};
		}

		List<DDLRecordVersion> list = null;

		if (useFinderCache) {
			list = (List<DDLRecordVersion>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (DDLRecordVersion ddlRecordVersion : list) {
					if ((userId != ddlRecordVersion.getUserId()) ||
						(recordSetId != ddlRecordVersion.getRecordSetId()) ||
						!recordSetVersion.equals(
							ddlRecordVersion.getRecordSetVersion()) ||
						(status != ddlRecordVersion.getStatus())) {

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
					6 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(6);
			}

			sb.append(_SQL_SELECT_DDLRECORDVERSION_WHERE);

			sb.append(_FINDER_COLUMN_U_R_R_S_USERID_2);

			sb.append(_FINDER_COLUMN_U_R_R_S_RECORDSETID_2);

			boolean bindRecordSetVersion = false;

			if (recordSetVersion.isEmpty()) {
				sb.append(_FINDER_COLUMN_U_R_R_S_RECORDSETVERSION_3);
			}
			else {
				bindRecordSetVersion = true;

				sb.append(_FINDER_COLUMN_U_R_R_S_RECORDSETVERSION_2);
			}

			sb.append(_FINDER_COLUMN_U_R_R_S_STATUS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(DDLRecordVersionModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(userId);

				queryPos.add(recordSetId);

				if (bindRecordSetVersion) {
					queryPos.add(recordSetVersion);
				}

				queryPos.add(status);

				list = (List<DDLRecordVersion>)QueryUtil.list(
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
	 * Returns the first ddl record version in the ordered set where userId = &#63; and recordSetId = &#63; and recordSetVersion = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ddl record version
	 * @throws NoSuchRecordVersionException if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion findByU_R_R_S_First(
			long userId, long recordSetId, String recordSetVersion, int status,
			OrderByComparator<DDLRecordVersion> orderByComparator)
		throws NoSuchRecordVersionException {

		DDLRecordVersion ddlRecordVersion = fetchByU_R_R_S_First(
			userId, recordSetId, recordSetVersion, status, orderByComparator);

		if (ddlRecordVersion != null) {
			return ddlRecordVersion;
		}

		StringBundler sb = new StringBundler(10);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("userId=");
		sb.append(userId);

		sb.append(", recordSetId=");
		sb.append(recordSetId);

		sb.append(", recordSetVersion=");
		sb.append(recordSetVersion);

		sb.append(", status=");
		sb.append(status);

		sb.append("}");

		throw new NoSuchRecordVersionException(sb.toString());
	}

	/**
	 * Returns the first ddl record version in the ordered set where userId = &#63; and recordSetId = &#63; and recordSetVersion = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ddl record version, or <code>null</code> if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion fetchByU_R_R_S_First(
		long userId, long recordSetId, String recordSetVersion, int status,
		OrderByComparator<DDLRecordVersion> orderByComparator) {

		List<DDLRecordVersion> list = findByU_R_R_S(
			userId, recordSetId, recordSetVersion, status, 0, 1,
			orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last ddl record version in the ordered set where userId = &#63; and recordSetId = &#63; and recordSetVersion = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ddl record version
	 * @throws NoSuchRecordVersionException if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion findByU_R_R_S_Last(
			long userId, long recordSetId, String recordSetVersion, int status,
			OrderByComparator<DDLRecordVersion> orderByComparator)
		throws NoSuchRecordVersionException {

		DDLRecordVersion ddlRecordVersion = fetchByU_R_R_S_Last(
			userId, recordSetId, recordSetVersion, status, orderByComparator);

		if (ddlRecordVersion != null) {
			return ddlRecordVersion;
		}

		StringBundler sb = new StringBundler(10);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("userId=");
		sb.append(userId);

		sb.append(", recordSetId=");
		sb.append(recordSetId);

		sb.append(", recordSetVersion=");
		sb.append(recordSetVersion);

		sb.append(", status=");
		sb.append(status);

		sb.append("}");

		throw new NoSuchRecordVersionException(sb.toString());
	}

	/**
	 * Returns the last ddl record version in the ordered set where userId = &#63; and recordSetId = &#63; and recordSetVersion = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ddl record version, or <code>null</code> if a matching ddl record version could not be found
	 */
	@Override
	public DDLRecordVersion fetchByU_R_R_S_Last(
		long userId, long recordSetId, String recordSetVersion, int status,
		OrderByComparator<DDLRecordVersion> orderByComparator) {

		int count = countByU_R_R_S(
			userId, recordSetId, recordSetVersion, status);

		if (count == 0) {
			return null;
		}

		List<DDLRecordVersion> list = findByU_R_R_S(
			userId, recordSetId, recordSetVersion, status, count - 1, count,
			orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the ddl record versions before and after the current ddl record version in the ordered set where userId = &#63; and recordSetId = &#63; and recordSetVersion = &#63; and status = &#63;.
	 *
	 * @param recordVersionId the primary key of the current ddl record version
	 * @param userId the user ID
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ddl record version
	 * @throws NoSuchRecordVersionException if a ddl record version with the primary key could not be found
	 */
	@Override
	public DDLRecordVersion[] findByU_R_R_S_PrevAndNext(
			long recordVersionId, long userId, long recordSetId,
			String recordSetVersion, int status,
			OrderByComparator<DDLRecordVersion> orderByComparator)
		throws NoSuchRecordVersionException {

		recordSetVersion = Objects.toString(recordSetVersion, "");

		DDLRecordVersion ddlRecordVersion = findByPrimaryKey(recordVersionId);

		Session session = null;

		try {
			session = openSession();

			DDLRecordVersion[] array = new DDLRecordVersionImpl[3];

			array[0] = getByU_R_R_S_PrevAndNext(
				session, ddlRecordVersion, userId, recordSetId,
				recordSetVersion, status, orderByComparator, true);

			array[1] = ddlRecordVersion;

			array[2] = getByU_R_R_S_PrevAndNext(
				session, ddlRecordVersion, userId, recordSetId,
				recordSetVersion, status, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected DDLRecordVersion getByU_R_R_S_PrevAndNext(
		Session session, DDLRecordVersion ddlRecordVersion, long userId,
		long recordSetId, String recordSetVersion, int status,
		OrderByComparator<DDLRecordVersion> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				7 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(6);
		}

		sb.append(_SQL_SELECT_DDLRECORDVERSION_WHERE);

		sb.append(_FINDER_COLUMN_U_R_R_S_USERID_2);

		sb.append(_FINDER_COLUMN_U_R_R_S_RECORDSETID_2);

		boolean bindRecordSetVersion = false;

		if (recordSetVersion.isEmpty()) {
			sb.append(_FINDER_COLUMN_U_R_R_S_RECORDSETVERSION_3);
		}
		else {
			bindRecordSetVersion = true;

			sb.append(_FINDER_COLUMN_U_R_R_S_RECORDSETVERSION_2);
		}

		sb.append(_FINDER_COLUMN_U_R_R_S_STATUS_2);

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
			sb.append(DDLRecordVersionModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(userId);

		queryPos.add(recordSetId);

		if (bindRecordSetVersion) {
			queryPos.add(recordSetVersion);
		}

		queryPos.add(status);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						ddlRecordVersion)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<DDLRecordVersion> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the ddl record versions where userId = &#63; and recordSetId = &#63; and recordSetVersion = &#63; and status = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param status the status
	 */
	@Override
	public void removeByU_R_R_S(
		long userId, long recordSetId, String recordSetVersion, int status) {

		for (DDLRecordVersion ddlRecordVersion :
				findByU_R_R_S(
					userId, recordSetId, recordSetVersion, status,
					QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(ddlRecordVersion);
		}
	}

	/**
	 * Returns the number of ddl record versions where userId = &#63; and recordSetId = &#63; and recordSetVersion = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param recordSetId the record set ID
	 * @param recordSetVersion the record set version
	 * @param status the status
	 * @return the number of matching ddl record versions
	 */
	@Override
	public int countByU_R_R_S(
		long userId, long recordSetId, String recordSetVersion, int status) {

		recordSetVersion = Objects.toString(recordSetVersion, "");

		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByU_R_R_S");

		Object[] finderArgs = new Object[] {
			userId, recordSetId, recordSetVersion, status
		};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(5);

			sb.append(_SQL_COUNT_DDLRECORDVERSION_WHERE);

			sb.append(_FINDER_COLUMN_U_R_R_S_USERID_2);

			sb.append(_FINDER_COLUMN_U_R_R_S_RECORDSETID_2);

			boolean bindRecordSetVersion = false;

			if (recordSetVersion.isEmpty()) {
				sb.append(_FINDER_COLUMN_U_R_R_S_RECORDSETVERSION_3);
			}
			else {
				bindRecordSetVersion = true;

				sb.append(_FINDER_COLUMN_U_R_R_S_RECORDSETVERSION_2);
			}

			sb.append(_FINDER_COLUMN_U_R_R_S_STATUS_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(userId);

				queryPos.add(recordSetId);

				if (bindRecordSetVersion) {
					queryPos.add(recordSetVersion);
				}

				queryPos.add(status);

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

	private static final String _FINDER_COLUMN_U_R_R_S_USERID_2 =
		"ddlRecordVersion.userId = ? AND ";

	private static final String _FINDER_COLUMN_U_R_R_S_RECORDSETID_2 =
		"ddlRecordVersion.recordSetId = ? AND ";

	private static final String _FINDER_COLUMN_U_R_R_S_RECORDSETVERSION_2 =
		"ddlRecordVersion.recordSetVersion = ? AND ";

	private static final String _FINDER_COLUMN_U_R_R_S_RECORDSETVERSION_3 =
		"(ddlRecordVersion.recordSetVersion IS NULL OR ddlRecordVersion.recordSetVersion = '') AND ";

	private static final String _FINDER_COLUMN_U_R_R_S_STATUS_2 =
		"ddlRecordVersion.status = ?";

	public DDLRecordVersionPersistenceImpl() {
		setModelClass(DDLRecordVersion.class);

		setModelImplClass(DDLRecordVersionImpl.class);
		setModelPKClass(long.class);

		setTable(DDLRecordVersionTable.INSTANCE);
	}

	/**
	 * Caches the ddl record version in the entity cache if it is enabled.
	 *
	 * @param ddlRecordVersion the ddl record version
	 */
	@Override
	public void cacheResult(DDLRecordVersion ddlRecordVersion) {
		entityCache.putResult(
			entityCacheEnabled, DDLRecordVersionImpl.class,
			ddlRecordVersion.getPrimaryKey(), ddlRecordVersion,
			new Object[] {
				_columnBitmaskEnabled,
				((DDLRecordVersionModelImpl)ddlRecordVersion).getColumnBitmask()
			});

		finderCache.putResult(
			_getFinderPath(FINDER_CLASS_NAME_ENTITY, "fetchByR_V"),
			new Object[] {
				ddlRecordVersion.getRecordId(), ddlRecordVersion.getVersion()
			},
			ddlRecordVersion);

		ddlRecordVersion.resetOriginalValues();
	}

	/**
	 * Caches the ddl record versions in the entity cache if it is enabled.
	 *
	 * @param ddlRecordVersions the ddl record versions
	 */
	@Override
	public void cacheResult(List<DDLRecordVersion> ddlRecordVersions) {
		for (DDLRecordVersion ddlRecordVersion : ddlRecordVersions) {
			if (entityCache.getResult(
					entityCacheEnabled, DDLRecordVersionImpl.class,
					ddlRecordVersion.getPrimaryKey()) == null) {

				cacheResult(ddlRecordVersion);
			}
			else {
				ddlRecordVersion.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all ddl record versions.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(DDLRecordVersionImpl.class);
	}

	/**
	 * Clears the cache for the ddl record version.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(DDLRecordVersion ddlRecordVersion) {
		entityCache.removeResult(
			entityCacheEnabled, DDLRecordVersionImpl.class,
			ddlRecordVersion.getPrimaryKey(), ddlRecordVersion,
			new Object[] {
				_columnBitmaskEnabled,
				((DDLRecordVersionModelImpl)ddlRecordVersion).getColumnBitmask()
			});
	}

	@Override
	public void clearCache(List<DDLRecordVersion> ddlRecordVersions) {
		for (DDLRecordVersion ddlRecordVersion : ddlRecordVersions) {
			entityCache.removeResult(
				entityCacheEnabled, DDLRecordVersionImpl.class,
				ddlRecordVersion.getPrimaryKey(), ddlRecordVersion,
				new Object[] {
					_columnBitmaskEnabled,
					((DDLRecordVersionModelImpl)ddlRecordVersion).
						getColumnBitmask()
				});
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				entityCacheEnabled, DDLRecordVersionImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		DDLRecordVersionModelImpl ddlRecordVersionModelImpl) {

		Object[] args = new Object[] {
			ddlRecordVersionModelImpl.getRecordId(),
			ddlRecordVersionModelImpl.getVersion()
		};

		finderCache.putResult(
			_getFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByR_V"),
			args, Long.valueOf(1), false);
		finderCache.putResult(
			_getFinderPath(FINDER_CLASS_NAME_ENTITY, "fetchByR_V"), args,
			ddlRecordVersionModelImpl, false);
	}

	/**
	 * Creates a new ddl record version with the primary key. Does not add the ddl record version to the database.
	 *
	 * @param recordVersionId the primary key for the new ddl record version
	 * @return the new ddl record version
	 */
	@Override
	public DDLRecordVersion create(long recordVersionId) {
		DDLRecordVersion ddlRecordVersion = new DDLRecordVersionImpl();

		ddlRecordVersion.setNew(true);
		ddlRecordVersion.setPrimaryKey(recordVersionId);

		ddlRecordVersion.setCompanyId(CompanyThreadLocal.getCompanyId());

		return ddlRecordVersion;
	}

	/**
	 * Removes the ddl record version with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param recordVersionId the primary key of the ddl record version
	 * @return the ddl record version that was removed
	 * @throws NoSuchRecordVersionException if a ddl record version with the primary key could not be found
	 */
	@Override
	public DDLRecordVersion remove(long recordVersionId)
		throws NoSuchRecordVersionException {

		return remove((Serializable)recordVersionId);
	}

	/**
	 * Removes the ddl record version with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the ddl record version
	 * @return the ddl record version that was removed
	 * @throws NoSuchRecordVersionException if a ddl record version with the primary key could not be found
	 */
	@Override
	public DDLRecordVersion remove(Serializable primaryKey)
		throws NoSuchRecordVersionException {

		Session session = null;

		try {
			session = openSession();

			DDLRecordVersion ddlRecordVersion = (DDLRecordVersion)session.get(
				DDLRecordVersionImpl.class, primaryKey);

			if (ddlRecordVersion == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchRecordVersionException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(ddlRecordVersion);
		}
		catch (NoSuchRecordVersionException noSuchEntityException) {
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
	protected DDLRecordVersion removeImpl(DDLRecordVersion ddlRecordVersion) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(ddlRecordVersion)) {
				ddlRecordVersion = (DDLRecordVersion)session.get(
					DDLRecordVersionImpl.class,
					ddlRecordVersion.getPrimaryKeyObj());
			}

			if (ddlRecordVersion != null) {
				session.delete(ddlRecordVersion);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (ddlRecordVersion != null) {
			clearCache(ddlRecordVersion);
		}

		return ddlRecordVersion;
	}

	@Override
	public DDLRecordVersion updateImpl(DDLRecordVersion ddlRecordVersion) {
		boolean isNew = ddlRecordVersion.isNew();

		if (!(ddlRecordVersion instanceof DDLRecordVersionModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(ddlRecordVersion.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					ddlRecordVersion);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in ddlRecordVersion proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom DDLRecordVersion implementation " +
					ddlRecordVersion.getClass());
		}

		DDLRecordVersionModelImpl ddlRecordVersionModelImpl =
			(DDLRecordVersionModelImpl)ddlRecordVersion;

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(ddlRecordVersion);
			}
			else {
				ddlRecordVersion = (DDLRecordVersion)session.merge(
					ddlRecordVersion);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			entityCacheEnabled, DDLRecordVersionImpl.class,
			ddlRecordVersionModelImpl.getPrimaryKey(),
			ddlRecordVersionModelImpl, false,
			new Object[] {
				_columnBitmaskEnabled,
				ddlRecordVersionModelImpl.getColumnBitmask()
			});

		cacheUniqueFindersCache(ddlRecordVersionModelImpl);

		ddlRecordVersion.resetOriginalValues();

		if (isNew) {
			ddlRecordVersion.setNew(false);
		}

		return ddlRecordVersion;
	}

	/**
	 * Returns the ddl record version with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the ddl record version
	 * @return the ddl record version
	 * @throws NoSuchRecordVersionException if a ddl record version with the primary key could not be found
	 */
	@Override
	public DDLRecordVersion findByPrimaryKey(Serializable primaryKey)
		throws NoSuchRecordVersionException {

		DDLRecordVersion ddlRecordVersion = fetchByPrimaryKey(primaryKey);

		if (ddlRecordVersion == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchRecordVersionException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return ddlRecordVersion;
	}

	/**
	 * Returns the ddl record version with the primary key or throws a <code>NoSuchRecordVersionException</code> if it could not be found.
	 *
	 * @param recordVersionId the primary key of the ddl record version
	 * @return the ddl record version
	 * @throws NoSuchRecordVersionException if a ddl record version with the primary key could not be found
	 */
	@Override
	public DDLRecordVersion findByPrimaryKey(long recordVersionId)
		throws NoSuchRecordVersionException {

		return findByPrimaryKey((Serializable)recordVersionId);
	}

	/**
	 * Returns the ddl record version with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param recordVersionId the primary key of the ddl record version
	 * @return the ddl record version, or <code>null</code> if a ddl record version with the primary key could not be found
	 */
	@Override
	public DDLRecordVersion fetchByPrimaryKey(long recordVersionId) {
		return fetchByPrimaryKey((Serializable)recordVersionId);
	}

	/**
	 * Returns all the ddl record versions.
	 *
	 * @return the ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the ddl record versions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @return the range of ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the ddl record versions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findAll(
		int start, int end,
		OrderByComparator<DDLRecordVersion> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the ddl record versions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DDLRecordVersionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ddl record versions
	 * @param end the upper bound of the range of ddl record versions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of ddl record versions
	 */
	@Override
	public List<DDLRecordVersion> findAll(
		int start, int end,
		OrderByComparator<DDLRecordVersion> orderByComparator,
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

		List<DDLRecordVersion> list = null;

		if (useFinderCache) {
			list = (List<DDLRecordVersion>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_DDLRECORDVERSION);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_DDLRECORDVERSION;

				sql = sql.concat(DDLRecordVersionModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<DDLRecordVersion>)QueryUtil.list(
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
	 * Removes all the ddl record versions from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (DDLRecordVersion ddlRecordVersion : findAll()) {
			remove(ddlRecordVersion);
		}
	}

	/**
	 * Returns the number of ddl record versions.
	 *
	 * @return the number of ddl record versions
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

				Query query = session.createQuery(_SQL_COUNT_DDLRECORDVERSION);

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
		return "recordVersionId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_DDLRECORDVERSION;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return DDLRecordVersionModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the ddl record version persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		DDLRecordVersionModelImpl.setEntityCacheEnabled(entityCacheEnabled);
		DDLRecordVersionModelImpl.setFinderCacheEnabled(finderCacheEnabled);

		_bundleContext = bundleContext;
		Bundle bundle = FrameworkUtil.getBundle(
			DDLRecordVersionPersistenceImpl.class);

		_bundleContext = bundle.getBundleContext();
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(DDLRecordVersionImpl.class.getName());

		for (ServiceRegistration<FinderPath> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	@Override
	@Reference(
		target = DDLPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.dynamic.data.lists.model.DDLRecordVersion"),
			true);
	}

	@Override
	@Reference(
		target = DDLPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = DDLPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
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

	private static final String _SQL_SELECT_DDLRECORDVERSION =
		"SELECT ddlRecordVersion FROM DDLRecordVersion ddlRecordVersion";

	private static final String _SQL_SELECT_DDLRECORDVERSION_WHERE =
		"SELECT ddlRecordVersion FROM DDLRecordVersion ddlRecordVersion WHERE ";

	private static final String _SQL_COUNT_DDLRECORDVERSION =
		"SELECT COUNT(ddlRecordVersion) FROM DDLRecordVersion ddlRecordVersion";

	private static final String _SQL_COUNT_DDLRECORDVERSION_WHERE =
		"SELECT COUNT(ddlRecordVersion) FROM DDLRecordVersion ddlRecordVersion WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "ddlRecordVersion.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No DDLRecordVersion exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No DDLRecordVersion exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		DDLRecordVersionPersistenceImpl.class);

	static {
		try {
			Class.forName(DDLPersistenceConstants.class.getName());
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
				Class<?> returnClass = DDLRecordVersionImpl.class;

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
			"findByRecordId",
			new Object[] {
				DDLRecordVersionModelImpl.RECORDID_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DDLRecordVersionModelImpl ddlRecordVersionModelImpl =
						(DDLRecordVersionModelImpl)baseModel;

					return new Object[] {
						ddlRecordVersionModelImpl.getRecordId()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DDLRecordVersionModelImpl ddlRecordVersionModelImpl =
						(DDLRecordVersionModelImpl)baseModel;

					return new Object[] {
						ddlRecordVersionModelImpl.getOriginalRecordId()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"findByR_R",
			new Object[] {
				DDLRecordVersionModelImpl.RECORDSETID_COLUMN_BITMASK |
				DDLRecordVersionModelImpl.RECORDSETVERSION_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DDLRecordVersionModelImpl ddlRecordVersionModelImpl =
						(DDLRecordVersionModelImpl)baseModel;

					return new Object[] {
						ddlRecordVersionModelImpl.getRecordSetId(),
						ddlRecordVersionModelImpl.getRecordSetVersion()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DDLRecordVersionModelImpl ddlRecordVersionModelImpl =
						(DDLRecordVersionModelImpl)baseModel;

					return new Object[] {
						ddlRecordVersionModelImpl.getOriginalRecordSetId(),
						ddlRecordVersionModelImpl.getOriginalRecordSetVersion()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"fetchByR_V",
			new Object[] {
				DDLRecordVersionModelImpl.RECORDID_COLUMN_BITMASK |
				DDLRecordVersionModelImpl.VERSION_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DDLRecordVersionModelImpl ddlRecordVersionModelImpl =
						(DDLRecordVersionModelImpl)baseModel;

					return new Object[] {
						ddlRecordVersionModelImpl.getRecordId(),
						ddlRecordVersionModelImpl.getVersion()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DDLRecordVersionModelImpl ddlRecordVersionModelImpl =
						(DDLRecordVersionModelImpl)baseModel;

					return new Object[] {
						ddlRecordVersionModelImpl.getOriginalRecordId(),
						ddlRecordVersionModelImpl.getOriginalVersion()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"findByR_S",
			new Object[] {
				DDLRecordVersionModelImpl.RECORDID_COLUMN_BITMASK |
				DDLRecordVersionModelImpl.STATUS_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DDLRecordVersionModelImpl ddlRecordVersionModelImpl =
						(DDLRecordVersionModelImpl)baseModel;

					return new Object[] {
						ddlRecordVersionModelImpl.getRecordId(),
						ddlRecordVersionModelImpl.getStatus()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DDLRecordVersionModelImpl ddlRecordVersionModelImpl =
						(DDLRecordVersionModelImpl)baseModel;

					return new Object[] {
						ddlRecordVersionModelImpl.getOriginalRecordId(),
						ddlRecordVersionModelImpl.getOriginalStatus()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"findByU_R_R_S",
			new Object[] {
				DDLRecordVersionModelImpl.USERID_COLUMN_BITMASK |
				DDLRecordVersionModelImpl.RECORDSETID_COLUMN_BITMASK |
				DDLRecordVersionModelImpl.RECORDSETVERSION_COLUMN_BITMASK |
				DDLRecordVersionModelImpl.STATUS_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DDLRecordVersionModelImpl ddlRecordVersionModelImpl =
						(DDLRecordVersionModelImpl)baseModel;

					return new Object[] {
						ddlRecordVersionModelImpl.getUserId(),
						ddlRecordVersionModelImpl.getRecordSetId(),
						ddlRecordVersionModelImpl.getRecordSetVersion(),
						ddlRecordVersionModelImpl.getStatus()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DDLRecordVersionModelImpl ddlRecordVersionModelImpl =
						(DDLRecordVersionModelImpl)baseModel;

					return new Object[] {
						ddlRecordVersionModelImpl.getOriginalUserId(),
						ddlRecordVersionModelImpl.getOriginalRecordSetId(),
						ddlRecordVersionModelImpl.getOriginalRecordSetVersion(),
						ddlRecordVersionModelImpl.getOriginalStatus()
					};
				}
			});
	}

}
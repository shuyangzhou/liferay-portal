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

package com.liferay.depot.service.persistence.impl;

import com.liferay.depot.exception.NoSuchEntryGroupRelException;
import com.liferay.depot.model.DepotEntryGroupRel;
import com.liferay.depot.model.DepotEntryGroupRelTable;
import com.liferay.depot.model.impl.DepotEntryGroupRelImpl;
import com.liferay.depot.model.impl.DepotEntryGroupRelModelImpl;
import com.liferay.depot.service.persistence.DepotEntryGroupRelPersistence;
import com.liferay.depot.service.persistence.impl.constants.DepotPersistenceConstants;
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
 * The persistence implementation for the depot entry group rel service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = DepotEntryGroupRelPersistence.class)
public class DepotEntryGroupRelPersistenceImpl
	extends BasePersistenceImpl<DepotEntryGroupRel>
	implements DepotEntryGroupRelPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>DepotEntryGroupRelUtil</code> to access the depot entry group rel persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		DepotEntryGroupRelImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	/**
	 * Returns all the depot entry group rels where depotEntryId = &#63;.
	 *
	 * @param depotEntryId the depot entry ID
	 * @return the matching depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findByDepotEntryId(long depotEntryId) {
		return findByDepotEntryId(
			depotEntryId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the depot entry group rels where depotEntryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DepotEntryGroupRelModelImpl</code>.
	 * </p>
	 *
	 * @param depotEntryId the depot entry ID
	 * @param start the lower bound of the range of depot entry group rels
	 * @param end the upper bound of the range of depot entry group rels (not inclusive)
	 * @return the range of matching depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findByDepotEntryId(
		long depotEntryId, int start, int end) {

		return findByDepotEntryId(depotEntryId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the depot entry group rels where depotEntryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DepotEntryGroupRelModelImpl</code>.
	 * </p>
	 *
	 * @param depotEntryId the depot entry ID
	 * @param start the lower bound of the range of depot entry group rels
	 * @param end the upper bound of the range of depot entry group rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findByDepotEntryId(
		long depotEntryId, int start, int end,
		OrderByComparator<DepotEntryGroupRel> orderByComparator) {

		return findByDepotEntryId(
			depotEntryId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the depot entry group rels where depotEntryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DepotEntryGroupRelModelImpl</code>.
	 * </p>
	 *
	 * @param depotEntryId the depot entry ID
	 * @param start the lower bound of the range of depot entry group rels
	 * @param end the upper bound of the range of depot entry group rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findByDepotEntryId(
		long depotEntryId, int start, int end,
		OrderByComparator<DepotEntryGroupRel> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
					"findByDepotEntryId");
				finderArgs = new Object[] {depotEntryId};
			}
		}
		else if (useFinderCache) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByDepotEntryId");
			finderArgs = new Object[] {
				depotEntryId, start, end, orderByComparator
			};
		}

		List<DepotEntryGroupRel> list = null;

		if (useFinderCache) {
			list = (List<DepotEntryGroupRel>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (DepotEntryGroupRel depotEntryGroupRel : list) {
					if (depotEntryId != depotEntryGroupRel.getDepotEntryId()) {
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

			sb.append(_SQL_SELECT_DEPOTENTRYGROUPREL_WHERE);

			sb.append(_FINDER_COLUMN_DEPOTENTRYID_DEPOTENTRYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(DepotEntryGroupRelModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(depotEntryId);

				list = (List<DepotEntryGroupRel>)QueryUtil.list(
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
	 * Returns the first depot entry group rel in the ordered set where depotEntryId = &#63;.
	 *
	 * @param depotEntryId the depot entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching depot entry group rel
	 * @throws NoSuchEntryGroupRelException if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel findByDepotEntryId_First(
			long depotEntryId,
			OrderByComparator<DepotEntryGroupRel> orderByComparator)
		throws NoSuchEntryGroupRelException {

		DepotEntryGroupRel depotEntryGroupRel = fetchByDepotEntryId_First(
			depotEntryId, orderByComparator);

		if (depotEntryGroupRel != null) {
			return depotEntryGroupRel;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("depotEntryId=");
		sb.append(depotEntryId);

		sb.append("}");

		throw new NoSuchEntryGroupRelException(sb.toString());
	}

	/**
	 * Returns the first depot entry group rel in the ordered set where depotEntryId = &#63;.
	 *
	 * @param depotEntryId the depot entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching depot entry group rel, or <code>null</code> if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel fetchByDepotEntryId_First(
		long depotEntryId,
		OrderByComparator<DepotEntryGroupRel> orderByComparator) {

		List<DepotEntryGroupRel> list = findByDepotEntryId(
			depotEntryId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last depot entry group rel in the ordered set where depotEntryId = &#63;.
	 *
	 * @param depotEntryId the depot entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching depot entry group rel
	 * @throws NoSuchEntryGroupRelException if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel findByDepotEntryId_Last(
			long depotEntryId,
			OrderByComparator<DepotEntryGroupRel> orderByComparator)
		throws NoSuchEntryGroupRelException {

		DepotEntryGroupRel depotEntryGroupRel = fetchByDepotEntryId_Last(
			depotEntryId, orderByComparator);

		if (depotEntryGroupRel != null) {
			return depotEntryGroupRel;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("depotEntryId=");
		sb.append(depotEntryId);

		sb.append("}");

		throw new NoSuchEntryGroupRelException(sb.toString());
	}

	/**
	 * Returns the last depot entry group rel in the ordered set where depotEntryId = &#63;.
	 *
	 * @param depotEntryId the depot entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching depot entry group rel, or <code>null</code> if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel fetchByDepotEntryId_Last(
		long depotEntryId,
		OrderByComparator<DepotEntryGroupRel> orderByComparator) {

		int count = countByDepotEntryId(depotEntryId);

		if (count == 0) {
			return null;
		}

		List<DepotEntryGroupRel> list = findByDepotEntryId(
			depotEntryId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the depot entry group rels before and after the current depot entry group rel in the ordered set where depotEntryId = &#63;.
	 *
	 * @param depotEntryGroupRelId the primary key of the current depot entry group rel
	 * @param depotEntryId the depot entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next depot entry group rel
	 * @throws NoSuchEntryGroupRelException if a depot entry group rel with the primary key could not be found
	 */
	@Override
	public DepotEntryGroupRel[] findByDepotEntryId_PrevAndNext(
			long depotEntryGroupRelId, long depotEntryId,
			OrderByComparator<DepotEntryGroupRel> orderByComparator)
		throws NoSuchEntryGroupRelException {

		DepotEntryGroupRel depotEntryGroupRel = findByPrimaryKey(
			depotEntryGroupRelId);

		Session session = null;

		try {
			session = openSession();

			DepotEntryGroupRel[] array = new DepotEntryGroupRelImpl[3];

			array[0] = getByDepotEntryId_PrevAndNext(
				session, depotEntryGroupRel, depotEntryId, orderByComparator,
				true);

			array[1] = depotEntryGroupRel;

			array[2] = getByDepotEntryId_PrevAndNext(
				session, depotEntryGroupRel, depotEntryId, orderByComparator,
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

	protected DepotEntryGroupRel getByDepotEntryId_PrevAndNext(
		Session session, DepotEntryGroupRel depotEntryGroupRel,
		long depotEntryId,
		OrderByComparator<DepotEntryGroupRel> orderByComparator,
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

		sb.append(_SQL_SELECT_DEPOTENTRYGROUPREL_WHERE);

		sb.append(_FINDER_COLUMN_DEPOTENTRYID_DEPOTENTRYID_2);

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
			sb.append(DepotEntryGroupRelModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(depotEntryId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						depotEntryGroupRel)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<DepotEntryGroupRel> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the depot entry group rels where depotEntryId = &#63; from the database.
	 *
	 * @param depotEntryId the depot entry ID
	 */
	@Override
	public void removeByDepotEntryId(long depotEntryId) {
		for (DepotEntryGroupRel depotEntryGroupRel :
				findByDepotEntryId(
					depotEntryId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(depotEntryGroupRel);
		}
	}

	/**
	 * Returns the number of depot entry group rels where depotEntryId = &#63;.
	 *
	 * @param depotEntryId the depot entry ID
	 * @return the number of matching depot entry group rels
	 */
	@Override
	public int countByDepotEntryId(long depotEntryId) {
		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByDepotEntryId");

		Object[] finderArgs = new Object[] {depotEntryId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_DEPOTENTRYGROUPREL_WHERE);

			sb.append(_FINDER_COLUMN_DEPOTENTRYID_DEPOTENTRYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(depotEntryId);

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

	private static final String _FINDER_COLUMN_DEPOTENTRYID_DEPOTENTRYID_2 =
		"depotEntryGroupRel.depotEntryId = ?";

	/**
	 * Returns all the depot entry group rels where toGroupId = &#63;.
	 *
	 * @param toGroupId the to group ID
	 * @return the matching depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findByToGroupId(long toGroupId) {
		return findByToGroupId(
			toGroupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the depot entry group rels where toGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DepotEntryGroupRelModelImpl</code>.
	 * </p>
	 *
	 * @param toGroupId the to group ID
	 * @param start the lower bound of the range of depot entry group rels
	 * @param end the upper bound of the range of depot entry group rels (not inclusive)
	 * @return the range of matching depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findByToGroupId(
		long toGroupId, int start, int end) {

		return findByToGroupId(toGroupId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the depot entry group rels where toGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DepotEntryGroupRelModelImpl</code>.
	 * </p>
	 *
	 * @param toGroupId the to group ID
	 * @param start the lower bound of the range of depot entry group rels
	 * @param end the upper bound of the range of depot entry group rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findByToGroupId(
		long toGroupId, int start, int end,
		OrderByComparator<DepotEntryGroupRel> orderByComparator) {

		return findByToGroupId(toGroupId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the depot entry group rels where toGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DepotEntryGroupRelModelImpl</code>.
	 * </p>
	 *
	 * @param toGroupId the to group ID
	 * @param start the lower bound of the range of depot entry group rels
	 * @param end the upper bound of the range of depot entry group rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findByToGroupId(
		long toGroupId, int start, int end,
		OrderByComparator<DepotEntryGroupRel> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
					"findByToGroupId");
				finderArgs = new Object[] {toGroupId};
			}
		}
		else if (useFinderCache) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByToGroupId");
			finderArgs = new Object[] {
				toGroupId, start, end, orderByComparator
			};
		}

		List<DepotEntryGroupRel> list = null;

		if (useFinderCache) {
			list = (List<DepotEntryGroupRel>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (DepotEntryGroupRel depotEntryGroupRel : list) {
					if (toGroupId != depotEntryGroupRel.getToGroupId()) {
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

			sb.append(_SQL_SELECT_DEPOTENTRYGROUPREL_WHERE);

			sb.append(_FINDER_COLUMN_TOGROUPID_TOGROUPID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(DepotEntryGroupRelModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(toGroupId);

				list = (List<DepotEntryGroupRel>)QueryUtil.list(
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
	 * Returns the first depot entry group rel in the ordered set where toGroupId = &#63;.
	 *
	 * @param toGroupId the to group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching depot entry group rel
	 * @throws NoSuchEntryGroupRelException if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel findByToGroupId_First(
			long toGroupId,
			OrderByComparator<DepotEntryGroupRel> orderByComparator)
		throws NoSuchEntryGroupRelException {

		DepotEntryGroupRel depotEntryGroupRel = fetchByToGroupId_First(
			toGroupId, orderByComparator);

		if (depotEntryGroupRel != null) {
			return depotEntryGroupRel;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("toGroupId=");
		sb.append(toGroupId);

		sb.append("}");

		throw new NoSuchEntryGroupRelException(sb.toString());
	}

	/**
	 * Returns the first depot entry group rel in the ordered set where toGroupId = &#63;.
	 *
	 * @param toGroupId the to group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching depot entry group rel, or <code>null</code> if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel fetchByToGroupId_First(
		long toGroupId,
		OrderByComparator<DepotEntryGroupRel> orderByComparator) {

		List<DepotEntryGroupRel> list = findByToGroupId(
			toGroupId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last depot entry group rel in the ordered set where toGroupId = &#63;.
	 *
	 * @param toGroupId the to group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching depot entry group rel
	 * @throws NoSuchEntryGroupRelException if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel findByToGroupId_Last(
			long toGroupId,
			OrderByComparator<DepotEntryGroupRel> orderByComparator)
		throws NoSuchEntryGroupRelException {

		DepotEntryGroupRel depotEntryGroupRel = fetchByToGroupId_Last(
			toGroupId, orderByComparator);

		if (depotEntryGroupRel != null) {
			return depotEntryGroupRel;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("toGroupId=");
		sb.append(toGroupId);

		sb.append("}");

		throw new NoSuchEntryGroupRelException(sb.toString());
	}

	/**
	 * Returns the last depot entry group rel in the ordered set where toGroupId = &#63;.
	 *
	 * @param toGroupId the to group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching depot entry group rel, or <code>null</code> if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel fetchByToGroupId_Last(
		long toGroupId,
		OrderByComparator<DepotEntryGroupRel> orderByComparator) {

		int count = countByToGroupId(toGroupId);

		if (count == 0) {
			return null;
		}

		List<DepotEntryGroupRel> list = findByToGroupId(
			toGroupId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the depot entry group rels before and after the current depot entry group rel in the ordered set where toGroupId = &#63;.
	 *
	 * @param depotEntryGroupRelId the primary key of the current depot entry group rel
	 * @param toGroupId the to group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next depot entry group rel
	 * @throws NoSuchEntryGroupRelException if a depot entry group rel with the primary key could not be found
	 */
	@Override
	public DepotEntryGroupRel[] findByToGroupId_PrevAndNext(
			long depotEntryGroupRelId, long toGroupId,
			OrderByComparator<DepotEntryGroupRel> orderByComparator)
		throws NoSuchEntryGroupRelException {

		DepotEntryGroupRel depotEntryGroupRel = findByPrimaryKey(
			depotEntryGroupRelId);

		Session session = null;

		try {
			session = openSession();

			DepotEntryGroupRel[] array = new DepotEntryGroupRelImpl[3];

			array[0] = getByToGroupId_PrevAndNext(
				session, depotEntryGroupRel, toGroupId, orderByComparator,
				true);

			array[1] = depotEntryGroupRel;

			array[2] = getByToGroupId_PrevAndNext(
				session, depotEntryGroupRel, toGroupId, orderByComparator,
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

	protected DepotEntryGroupRel getByToGroupId_PrevAndNext(
		Session session, DepotEntryGroupRel depotEntryGroupRel, long toGroupId,
		OrderByComparator<DepotEntryGroupRel> orderByComparator,
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

		sb.append(_SQL_SELECT_DEPOTENTRYGROUPREL_WHERE);

		sb.append(_FINDER_COLUMN_TOGROUPID_TOGROUPID_2);

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
			sb.append(DepotEntryGroupRelModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(toGroupId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						depotEntryGroupRel)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<DepotEntryGroupRel> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the depot entry group rels where toGroupId = &#63; from the database.
	 *
	 * @param toGroupId the to group ID
	 */
	@Override
	public void removeByToGroupId(long toGroupId) {
		for (DepotEntryGroupRel depotEntryGroupRel :
				findByToGroupId(
					toGroupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(depotEntryGroupRel);
		}
	}

	/**
	 * Returns the number of depot entry group rels where toGroupId = &#63;.
	 *
	 * @param toGroupId the to group ID
	 * @return the number of matching depot entry group rels
	 */
	@Override
	public int countByToGroupId(long toGroupId) {
		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByToGroupId");

		Object[] finderArgs = new Object[] {toGroupId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_DEPOTENTRYGROUPREL_WHERE);

			sb.append(_FINDER_COLUMN_TOGROUPID_TOGROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(toGroupId);

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

	private static final String _FINDER_COLUMN_TOGROUPID_TOGROUPID_2 =
		"depotEntryGroupRel.toGroupId = ?";

	/**
	 * Returns the depot entry group rel where depotEntryId = &#63; and toGroupId = &#63; or throws a <code>NoSuchEntryGroupRelException</code> if it could not be found.
	 *
	 * @param depotEntryId the depot entry ID
	 * @param toGroupId the to group ID
	 * @return the matching depot entry group rel
	 * @throws NoSuchEntryGroupRelException if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel findByD_TGI(long depotEntryId, long toGroupId)
		throws NoSuchEntryGroupRelException {

		DepotEntryGroupRel depotEntryGroupRel = fetchByD_TGI(
			depotEntryId, toGroupId);

		if (depotEntryGroupRel == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("depotEntryId=");
			sb.append(depotEntryId);

			sb.append(", toGroupId=");
			sb.append(toGroupId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchEntryGroupRelException(sb.toString());
		}

		return depotEntryGroupRel;
	}

	/**
	 * Returns the depot entry group rel where depotEntryId = &#63; and toGroupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param depotEntryId the depot entry ID
	 * @param toGroupId the to group ID
	 * @return the matching depot entry group rel, or <code>null</code> if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel fetchByD_TGI(long depotEntryId, long toGroupId) {
		return fetchByD_TGI(depotEntryId, toGroupId, true);
	}

	/**
	 * Returns the depot entry group rel where depotEntryId = &#63; and toGroupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param depotEntryId the depot entry ID
	 * @param toGroupId the to group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching depot entry group rel, or <code>null</code> if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel fetchByD_TGI(
		long depotEntryId, long toGroupId, boolean useFinderCache) {

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {depotEntryId, toGroupId};
		}

		Object result = null;

		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByD_TGI");

		if (useFinderCache) {
			result = finderCache.getResult(finderPath, finderArgs, this);
		}

		if (result instanceof DepotEntryGroupRel) {
			DepotEntryGroupRel depotEntryGroupRel = (DepotEntryGroupRel)result;

			if ((depotEntryId != depotEntryGroupRel.getDepotEntryId()) ||
				(toGroupId != depotEntryGroupRel.getToGroupId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_DEPOTENTRYGROUPREL_WHERE);

			sb.append(_FINDER_COLUMN_D_TGI_DEPOTENTRYID_2);

			sb.append(_FINDER_COLUMN_D_TGI_TOGROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(depotEntryId);

				queryPos.add(toGroupId);

				List<DepotEntryGroupRel> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(finderPath, finderArgs, list);
					}
				}
				else {
					DepotEntryGroupRel depotEntryGroupRel = list.get(0);

					result = depotEntryGroupRel;

					cacheResult(depotEntryGroupRel);
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
			return (DepotEntryGroupRel)result;
		}
	}

	/**
	 * Removes the depot entry group rel where depotEntryId = &#63; and toGroupId = &#63; from the database.
	 *
	 * @param depotEntryId the depot entry ID
	 * @param toGroupId the to group ID
	 * @return the depot entry group rel that was removed
	 */
	@Override
	public DepotEntryGroupRel removeByD_TGI(long depotEntryId, long toGroupId)
		throws NoSuchEntryGroupRelException {

		DepotEntryGroupRel depotEntryGroupRel = findByD_TGI(
			depotEntryId, toGroupId);

		return remove(depotEntryGroupRel);
	}

	/**
	 * Returns the number of depot entry group rels where depotEntryId = &#63; and toGroupId = &#63;.
	 *
	 * @param depotEntryId the depot entry ID
	 * @param toGroupId the to group ID
	 * @return the number of matching depot entry group rels
	 */
	@Override
	public int countByD_TGI(long depotEntryId, long toGroupId) {
		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByD_TGI");

		Object[] finderArgs = new Object[] {depotEntryId, toGroupId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_DEPOTENTRYGROUPREL_WHERE);

			sb.append(_FINDER_COLUMN_D_TGI_DEPOTENTRYID_2);

			sb.append(_FINDER_COLUMN_D_TGI_TOGROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(depotEntryId);

				queryPos.add(toGroupId);

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

	private static final String _FINDER_COLUMN_D_TGI_DEPOTENTRYID_2 =
		"depotEntryGroupRel.depotEntryId = ? AND ";

	private static final String _FINDER_COLUMN_D_TGI_TOGROUPID_2 =
		"depotEntryGroupRel.toGroupId = ?";

	/**
	 * Returns all the depot entry group rels where searchable = &#63; and toGroupId = &#63;.
	 *
	 * @param searchable the searchable
	 * @param toGroupId the to group ID
	 * @return the matching depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findByS_TGI(
		boolean searchable, long toGroupId) {

		return findByS_TGI(
			searchable, toGroupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the depot entry group rels where searchable = &#63; and toGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DepotEntryGroupRelModelImpl</code>.
	 * </p>
	 *
	 * @param searchable the searchable
	 * @param toGroupId the to group ID
	 * @param start the lower bound of the range of depot entry group rels
	 * @param end the upper bound of the range of depot entry group rels (not inclusive)
	 * @return the range of matching depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findByS_TGI(
		boolean searchable, long toGroupId, int start, int end) {

		return findByS_TGI(searchable, toGroupId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the depot entry group rels where searchable = &#63; and toGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DepotEntryGroupRelModelImpl</code>.
	 * </p>
	 *
	 * @param searchable the searchable
	 * @param toGroupId the to group ID
	 * @param start the lower bound of the range of depot entry group rels
	 * @param end the upper bound of the range of depot entry group rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findByS_TGI(
		boolean searchable, long toGroupId, int start, int end,
		OrderByComparator<DepotEntryGroupRel> orderByComparator) {

		return findByS_TGI(
			searchable, toGroupId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the depot entry group rels where searchable = &#63; and toGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DepotEntryGroupRelModelImpl</code>.
	 * </p>
	 *
	 * @param searchable the searchable
	 * @param toGroupId the to group ID
	 * @param start the lower bound of the range of depot entry group rels
	 * @param end the upper bound of the range of depot entry group rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findByS_TGI(
		boolean searchable, long toGroupId, int start, int end,
		OrderByComparator<DepotEntryGroupRel> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByS_TGI");
				finderArgs = new Object[] {searchable, toGroupId};
			}
		}
		else if (useFinderCache) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByS_TGI");
			finderArgs = new Object[] {
				searchable, toGroupId, start, end, orderByComparator
			};
		}

		List<DepotEntryGroupRel> list = null;

		if (useFinderCache) {
			list = (List<DepotEntryGroupRel>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (DepotEntryGroupRel depotEntryGroupRel : list) {
					if ((searchable != depotEntryGroupRel.isSearchable()) ||
						(toGroupId != depotEntryGroupRel.getToGroupId())) {

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

			sb.append(_SQL_SELECT_DEPOTENTRYGROUPREL_WHERE);

			sb.append(_FINDER_COLUMN_S_TGI_SEARCHABLE_2);

			sb.append(_FINDER_COLUMN_S_TGI_TOGROUPID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(DepotEntryGroupRelModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(searchable);

				queryPos.add(toGroupId);

				list = (List<DepotEntryGroupRel>)QueryUtil.list(
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
	 * Returns the first depot entry group rel in the ordered set where searchable = &#63; and toGroupId = &#63;.
	 *
	 * @param searchable the searchable
	 * @param toGroupId the to group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching depot entry group rel
	 * @throws NoSuchEntryGroupRelException if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel findByS_TGI_First(
			boolean searchable, long toGroupId,
			OrderByComparator<DepotEntryGroupRel> orderByComparator)
		throws NoSuchEntryGroupRelException {

		DepotEntryGroupRel depotEntryGroupRel = fetchByS_TGI_First(
			searchable, toGroupId, orderByComparator);

		if (depotEntryGroupRel != null) {
			return depotEntryGroupRel;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("searchable=");
		sb.append(searchable);

		sb.append(", toGroupId=");
		sb.append(toGroupId);

		sb.append("}");

		throw new NoSuchEntryGroupRelException(sb.toString());
	}

	/**
	 * Returns the first depot entry group rel in the ordered set where searchable = &#63; and toGroupId = &#63;.
	 *
	 * @param searchable the searchable
	 * @param toGroupId the to group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching depot entry group rel, or <code>null</code> if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel fetchByS_TGI_First(
		boolean searchable, long toGroupId,
		OrderByComparator<DepotEntryGroupRel> orderByComparator) {

		List<DepotEntryGroupRel> list = findByS_TGI(
			searchable, toGroupId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last depot entry group rel in the ordered set where searchable = &#63; and toGroupId = &#63;.
	 *
	 * @param searchable the searchable
	 * @param toGroupId the to group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching depot entry group rel
	 * @throws NoSuchEntryGroupRelException if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel findByS_TGI_Last(
			boolean searchable, long toGroupId,
			OrderByComparator<DepotEntryGroupRel> orderByComparator)
		throws NoSuchEntryGroupRelException {

		DepotEntryGroupRel depotEntryGroupRel = fetchByS_TGI_Last(
			searchable, toGroupId, orderByComparator);

		if (depotEntryGroupRel != null) {
			return depotEntryGroupRel;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("searchable=");
		sb.append(searchable);

		sb.append(", toGroupId=");
		sb.append(toGroupId);

		sb.append("}");

		throw new NoSuchEntryGroupRelException(sb.toString());
	}

	/**
	 * Returns the last depot entry group rel in the ordered set where searchable = &#63; and toGroupId = &#63;.
	 *
	 * @param searchable the searchable
	 * @param toGroupId the to group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching depot entry group rel, or <code>null</code> if a matching depot entry group rel could not be found
	 */
	@Override
	public DepotEntryGroupRel fetchByS_TGI_Last(
		boolean searchable, long toGroupId,
		OrderByComparator<DepotEntryGroupRel> orderByComparator) {

		int count = countByS_TGI(searchable, toGroupId);

		if (count == 0) {
			return null;
		}

		List<DepotEntryGroupRel> list = findByS_TGI(
			searchable, toGroupId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the depot entry group rels before and after the current depot entry group rel in the ordered set where searchable = &#63; and toGroupId = &#63;.
	 *
	 * @param depotEntryGroupRelId the primary key of the current depot entry group rel
	 * @param searchable the searchable
	 * @param toGroupId the to group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next depot entry group rel
	 * @throws NoSuchEntryGroupRelException if a depot entry group rel with the primary key could not be found
	 */
	@Override
	public DepotEntryGroupRel[] findByS_TGI_PrevAndNext(
			long depotEntryGroupRelId, boolean searchable, long toGroupId,
			OrderByComparator<DepotEntryGroupRel> orderByComparator)
		throws NoSuchEntryGroupRelException {

		DepotEntryGroupRel depotEntryGroupRel = findByPrimaryKey(
			depotEntryGroupRelId);

		Session session = null;

		try {
			session = openSession();

			DepotEntryGroupRel[] array = new DepotEntryGroupRelImpl[3];

			array[0] = getByS_TGI_PrevAndNext(
				session, depotEntryGroupRel, searchable, toGroupId,
				orderByComparator, true);

			array[1] = depotEntryGroupRel;

			array[2] = getByS_TGI_PrevAndNext(
				session, depotEntryGroupRel, searchable, toGroupId,
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

	protected DepotEntryGroupRel getByS_TGI_PrevAndNext(
		Session session, DepotEntryGroupRel depotEntryGroupRel,
		boolean searchable, long toGroupId,
		OrderByComparator<DepotEntryGroupRel> orderByComparator,
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

		sb.append(_SQL_SELECT_DEPOTENTRYGROUPREL_WHERE);

		sb.append(_FINDER_COLUMN_S_TGI_SEARCHABLE_2);

		sb.append(_FINDER_COLUMN_S_TGI_TOGROUPID_2);

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
			sb.append(DepotEntryGroupRelModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(searchable);

		queryPos.add(toGroupId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						depotEntryGroupRel)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<DepotEntryGroupRel> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the depot entry group rels where searchable = &#63; and toGroupId = &#63; from the database.
	 *
	 * @param searchable the searchable
	 * @param toGroupId the to group ID
	 */
	@Override
	public void removeByS_TGI(boolean searchable, long toGroupId) {
		for (DepotEntryGroupRel depotEntryGroupRel :
				findByS_TGI(
					searchable, toGroupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(depotEntryGroupRel);
		}
	}

	/**
	 * Returns the number of depot entry group rels where searchable = &#63; and toGroupId = &#63;.
	 *
	 * @param searchable the searchable
	 * @param toGroupId the to group ID
	 * @return the number of matching depot entry group rels
	 */
	@Override
	public int countByS_TGI(boolean searchable, long toGroupId) {
		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByS_TGI");

		Object[] finderArgs = new Object[] {searchable, toGroupId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_DEPOTENTRYGROUPREL_WHERE);

			sb.append(_FINDER_COLUMN_S_TGI_SEARCHABLE_2);

			sb.append(_FINDER_COLUMN_S_TGI_TOGROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(searchable);

				queryPos.add(toGroupId);

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

	private static final String _FINDER_COLUMN_S_TGI_SEARCHABLE_2 =
		"depotEntryGroupRel.searchable = ? AND ";

	private static final String _FINDER_COLUMN_S_TGI_TOGROUPID_2 =
		"depotEntryGroupRel.toGroupId = ?";

	public DepotEntryGroupRelPersistenceImpl() {
		setModelClass(DepotEntryGroupRel.class);

		setModelImplClass(DepotEntryGroupRelImpl.class);
		setModelPKClass(long.class);

		setTable(DepotEntryGroupRelTable.INSTANCE);
	}

	/**
	 * Caches the depot entry group rel in the entity cache if it is enabled.
	 *
	 * @param depotEntryGroupRel the depot entry group rel
	 */
	@Override
	public void cacheResult(DepotEntryGroupRel depotEntryGroupRel) {
		entityCache.putResult(
			entityCacheEnabled, DepotEntryGroupRelImpl.class,
			depotEntryGroupRel.getPrimaryKey(), depotEntryGroupRel,
			new Object[] {
				_columnBitmaskEnabled,
				((DepotEntryGroupRelModelImpl)depotEntryGroupRel).
					getColumnBitmask()
			});

		finderCache.putResult(
			_getFinderPath(FINDER_CLASS_NAME_ENTITY, "fetchByD_TGI"),
			new Object[] {
				depotEntryGroupRel.getDepotEntryId(),
				depotEntryGroupRel.getToGroupId()
			},
			depotEntryGroupRel);

		depotEntryGroupRel.resetOriginalValues();
	}

	/**
	 * Caches the depot entry group rels in the entity cache if it is enabled.
	 *
	 * @param depotEntryGroupRels the depot entry group rels
	 */
	@Override
	public void cacheResult(List<DepotEntryGroupRel> depotEntryGroupRels) {
		for (DepotEntryGroupRel depotEntryGroupRel : depotEntryGroupRels) {
			if (entityCache.getResult(
					entityCacheEnabled, DepotEntryGroupRelImpl.class,
					depotEntryGroupRel.getPrimaryKey()) == null) {

				cacheResult(depotEntryGroupRel);
			}
			else {
				depotEntryGroupRel.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all depot entry group rels.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(DepotEntryGroupRelImpl.class);
	}

	/**
	 * Clears the cache for the depot entry group rel.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(DepotEntryGroupRel depotEntryGroupRel) {
		entityCache.removeResult(
			entityCacheEnabled, DepotEntryGroupRelImpl.class,
			depotEntryGroupRel.getPrimaryKey(), depotEntryGroupRel,
			new Object[] {
				_columnBitmaskEnabled,
				((DepotEntryGroupRelModelImpl)depotEntryGroupRel).
					getColumnBitmask()
			});
	}

	@Override
	public void clearCache(List<DepotEntryGroupRel> depotEntryGroupRels) {
		for (DepotEntryGroupRel depotEntryGroupRel : depotEntryGroupRels) {
			entityCache.removeResult(
				entityCacheEnabled, DepotEntryGroupRelImpl.class,
				depotEntryGroupRel.getPrimaryKey(), depotEntryGroupRel,
				new Object[] {
					_columnBitmaskEnabled,
					((DepotEntryGroupRelModelImpl)depotEntryGroupRel).
						getColumnBitmask()
				});
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				entityCacheEnabled, DepotEntryGroupRelImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		DepotEntryGroupRelModelImpl depotEntryGroupRelModelImpl) {

		Object[] args = new Object[] {
			depotEntryGroupRelModelImpl.getDepotEntryId(),
			depotEntryGroupRelModelImpl.getToGroupId()
		};

		finderCache.putResult(
			_getFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByD_TGI"),
			args, Long.valueOf(1), false);
		finderCache.putResult(
			_getFinderPath(FINDER_CLASS_NAME_ENTITY, "fetchByD_TGI"), args,
			depotEntryGroupRelModelImpl, false);
	}

	/**
	 * Creates a new depot entry group rel with the primary key. Does not add the depot entry group rel to the database.
	 *
	 * @param depotEntryGroupRelId the primary key for the new depot entry group rel
	 * @return the new depot entry group rel
	 */
	@Override
	public DepotEntryGroupRel create(long depotEntryGroupRelId) {
		DepotEntryGroupRel depotEntryGroupRel = new DepotEntryGroupRelImpl();

		depotEntryGroupRel.setNew(true);
		depotEntryGroupRel.setPrimaryKey(depotEntryGroupRelId);

		depotEntryGroupRel.setCompanyId(CompanyThreadLocal.getCompanyId());

		return depotEntryGroupRel;
	}

	/**
	 * Removes the depot entry group rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param depotEntryGroupRelId the primary key of the depot entry group rel
	 * @return the depot entry group rel that was removed
	 * @throws NoSuchEntryGroupRelException if a depot entry group rel with the primary key could not be found
	 */
	@Override
	public DepotEntryGroupRel remove(long depotEntryGroupRelId)
		throws NoSuchEntryGroupRelException {

		return remove((Serializable)depotEntryGroupRelId);
	}

	/**
	 * Removes the depot entry group rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the depot entry group rel
	 * @return the depot entry group rel that was removed
	 * @throws NoSuchEntryGroupRelException if a depot entry group rel with the primary key could not be found
	 */
	@Override
	public DepotEntryGroupRel remove(Serializable primaryKey)
		throws NoSuchEntryGroupRelException {

		Session session = null;

		try {
			session = openSession();

			DepotEntryGroupRel depotEntryGroupRel =
				(DepotEntryGroupRel)session.get(
					DepotEntryGroupRelImpl.class, primaryKey);

			if (depotEntryGroupRel == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchEntryGroupRelException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(depotEntryGroupRel);
		}
		catch (NoSuchEntryGroupRelException noSuchEntityException) {
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
	protected DepotEntryGroupRel removeImpl(
		DepotEntryGroupRel depotEntryGroupRel) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(depotEntryGroupRel)) {
				depotEntryGroupRel = (DepotEntryGroupRel)session.get(
					DepotEntryGroupRelImpl.class,
					depotEntryGroupRel.getPrimaryKeyObj());
			}

			if (depotEntryGroupRel != null) {
				session.delete(depotEntryGroupRel);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (depotEntryGroupRel != null) {
			clearCache(depotEntryGroupRel);
		}

		return depotEntryGroupRel;
	}

	@Override
	public DepotEntryGroupRel updateImpl(
		DepotEntryGroupRel depotEntryGroupRel) {

		boolean isNew = depotEntryGroupRel.isNew();

		if (!(depotEntryGroupRel instanceof DepotEntryGroupRelModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(depotEntryGroupRel.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					depotEntryGroupRel);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in depotEntryGroupRel proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom DepotEntryGroupRel implementation " +
					depotEntryGroupRel.getClass());
		}

		DepotEntryGroupRelModelImpl depotEntryGroupRelModelImpl =
			(DepotEntryGroupRelModelImpl)depotEntryGroupRel;

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(depotEntryGroupRel);
			}
			else {
				depotEntryGroupRel = (DepotEntryGroupRel)session.merge(
					depotEntryGroupRel);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			entityCacheEnabled, DepotEntryGroupRelImpl.class,
			depotEntryGroupRelModelImpl.getPrimaryKey(),
			depotEntryGroupRelModelImpl, false,
			new Object[] {
				_columnBitmaskEnabled,
				depotEntryGroupRelModelImpl.getColumnBitmask()
			});

		cacheUniqueFindersCache(depotEntryGroupRelModelImpl);

		depotEntryGroupRel.resetOriginalValues();

		if (isNew) {
			depotEntryGroupRel.setNew(false);
		}

		return depotEntryGroupRel;
	}

	/**
	 * Returns the depot entry group rel with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the depot entry group rel
	 * @return the depot entry group rel
	 * @throws NoSuchEntryGroupRelException if a depot entry group rel with the primary key could not be found
	 */
	@Override
	public DepotEntryGroupRel findByPrimaryKey(Serializable primaryKey)
		throws NoSuchEntryGroupRelException {

		DepotEntryGroupRel depotEntryGroupRel = fetchByPrimaryKey(primaryKey);

		if (depotEntryGroupRel == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchEntryGroupRelException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return depotEntryGroupRel;
	}

	/**
	 * Returns the depot entry group rel with the primary key or throws a <code>NoSuchEntryGroupRelException</code> if it could not be found.
	 *
	 * @param depotEntryGroupRelId the primary key of the depot entry group rel
	 * @return the depot entry group rel
	 * @throws NoSuchEntryGroupRelException if a depot entry group rel with the primary key could not be found
	 */
	@Override
	public DepotEntryGroupRel findByPrimaryKey(long depotEntryGroupRelId)
		throws NoSuchEntryGroupRelException {

		return findByPrimaryKey((Serializable)depotEntryGroupRelId);
	}

	/**
	 * Returns the depot entry group rel with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param depotEntryGroupRelId the primary key of the depot entry group rel
	 * @return the depot entry group rel, or <code>null</code> if a depot entry group rel with the primary key could not be found
	 */
	@Override
	public DepotEntryGroupRel fetchByPrimaryKey(long depotEntryGroupRelId) {
		return fetchByPrimaryKey((Serializable)depotEntryGroupRelId);
	}

	/**
	 * Returns all the depot entry group rels.
	 *
	 * @return the depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the depot entry group rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DepotEntryGroupRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of depot entry group rels
	 * @param end the upper bound of the range of depot entry group rels (not inclusive)
	 * @return the range of depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the depot entry group rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DepotEntryGroupRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of depot entry group rels
	 * @param end the upper bound of the range of depot entry group rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findAll(
		int start, int end,
		OrderByComparator<DepotEntryGroupRel> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the depot entry group rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DepotEntryGroupRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of depot entry group rels
	 * @param end the upper bound of the range of depot entry group rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of depot entry group rels
	 */
	@Override
	public List<DepotEntryGroupRel> findAll(
		int start, int end,
		OrderByComparator<DepotEntryGroupRel> orderByComparator,
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

		List<DepotEntryGroupRel> list = null;

		if (useFinderCache) {
			list = (List<DepotEntryGroupRel>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_DEPOTENTRYGROUPREL);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_DEPOTENTRYGROUPREL;

				sql = sql.concat(DepotEntryGroupRelModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<DepotEntryGroupRel>)QueryUtil.list(
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
	 * Removes all the depot entry group rels from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (DepotEntryGroupRel depotEntryGroupRel : findAll()) {
			remove(depotEntryGroupRel);
		}
	}

	/**
	 * Returns the number of depot entry group rels.
	 *
	 * @return the number of depot entry group rels
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
					_SQL_COUNT_DEPOTENTRYGROUPREL);

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
		return "depotEntryGroupRelId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_DEPOTENTRYGROUPREL;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return DepotEntryGroupRelModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the depot entry group rel persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		DepotEntryGroupRelModelImpl.setEntityCacheEnabled(entityCacheEnabled);
		DepotEntryGroupRelModelImpl.setFinderCacheEnabled(finderCacheEnabled);

		_bundleContext = bundleContext;
		Bundle bundle = FrameworkUtil.getBundle(
			DepotEntryGroupRelPersistenceImpl.class);

		_bundleContext = bundle.getBundleContext();
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(DepotEntryGroupRelImpl.class.getName());

		for (ServiceRegistration<FinderPath> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	@Override
	@Reference(
		target = DepotPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.depot.model.DepotEntryGroupRel"),
			true);
	}

	@Override
	@Reference(
		target = DepotPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = DepotPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
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

	private static final String _SQL_SELECT_DEPOTENTRYGROUPREL =
		"SELECT depotEntryGroupRel FROM DepotEntryGroupRel depotEntryGroupRel";

	private static final String _SQL_SELECT_DEPOTENTRYGROUPREL_WHERE =
		"SELECT depotEntryGroupRel FROM DepotEntryGroupRel depotEntryGroupRel WHERE ";

	private static final String _SQL_COUNT_DEPOTENTRYGROUPREL =
		"SELECT COUNT(depotEntryGroupRel) FROM DepotEntryGroupRel depotEntryGroupRel";

	private static final String _SQL_COUNT_DEPOTENTRYGROUPREL_WHERE =
		"SELECT COUNT(depotEntryGroupRel) FROM DepotEntryGroupRel depotEntryGroupRel WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "depotEntryGroupRel.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No DepotEntryGroupRel exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No DepotEntryGroupRel exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		DepotEntryGroupRelPersistenceImpl.class);

	static {
		try {
			Class.forName(DepotPersistenceConstants.class.getName());
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
				Class<?> returnClass = DepotEntryGroupRelImpl.class;

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
			"findByDepotEntryId",
			new Object[] {
				DepotEntryGroupRelModelImpl.DEPOTENTRYID_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DepotEntryGroupRelModelImpl depotEntryGroupRelModelImpl =
						(DepotEntryGroupRelModelImpl)baseModel;

					return new Object[] {
						depotEntryGroupRelModelImpl.getDepotEntryId()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DepotEntryGroupRelModelImpl depotEntryGroupRelModelImpl =
						(DepotEntryGroupRelModelImpl)baseModel;

					return new Object[] {
						depotEntryGroupRelModelImpl.getOriginalDepotEntryId()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"findByToGroupId",
			new Object[] {
				DepotEntryGroupRelModelImpl.TOGROUPID_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DepotEntryGroupRelModelImpl depotEntryGroupRelModelImpl =
						(DepotEntryGroupRelModelImpl)baseModel;

					return new Object[] {
						depotEntryGroupRelModelImpl.getToGroupId()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DepotEntryGroupRelModelImpl depotEntryGroupRelModelImpl =
						(DepotEntryGroupRelModelImpl)baseModel;

					return new Object[] {
						depotEntryGroupRelModelImpl.getOriginalToGroupId()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"fetchByD_TGI",
			new Object[] {
				DepotEntryGroupRelModelImpl.DEPOTENTRYID_COLUMN_BITMASK |
				DepotEntryGroupRelModelImpl.TOGROUPID_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DepotEntryGroupRelModelImpl depotEntryGroupRelModelImpl =
						(DepotEntryGroupRelModelImpl)baseModel;

					return new Object[] {
						depotEntryGroupRelModelImpl.getDepotEntryId(),
						depotEntryGroupRelModelImpl.getToGroupId()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DepotEntryGroupRelModelImpl depotEntryGroupRelModelImpl =
						(DepotEntryGroupRelModelImpl)baseModel;

					return new Object[] {
						depotEntryGroupRelModelImpl.getOriginalDepotEntryId(),
						depotEntryGroupRelModelImpl.getOriginalToGroupId()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"findByS_TGI",
			new Object[] {
				DepotEntryGroupRelModelImpl.SEARCHABLE_COLUMN_BITMASK |
				DepotEntryGroupRelModelImpl.TOGROUPID_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DepotEntryGroupRelModelImpl depotEntryGroupRelModelImpl =
						(DepotEntryGroupRelModelImpl)baseModel;

					return new Object[] {
						depotEntryGroupRelModelImpl.isSearchable(),
						depotEntryGroupRelModelImpl.getToGroupId()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					DepotEntryGroupRelModelImpl depotEntryGroupRelModelImpl =
						(DepotEntryGroupRelModelImpl)baseModel;

					return new Object[] {
						depotEntryGroupRelModelImpl.getOriginalSearchable(),
						depotEntryGroupRelModelImpl.getOriginalToGroupId()
					};
				}
			});
	}

}
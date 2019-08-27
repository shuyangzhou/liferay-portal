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
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.view.count.exception.NoSuchEntryException;
import com.liferay.view.count.model.ViewCountEntry;
import com.liferay.view.count.model.impl.ViewCountEntryImpl;
import com.liferay.view.count.model.impl.ViewCountEntryModelImpl;
import com.liferay.view.count.service.persistence.ViewCountEntryPersistence;
import com.liferay.view.count.service.persistence.impl.constants.ViewCountPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.osgi.annotation.versioning.ProviderType;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the view count entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Preston Crary
 * @generated
 */
@Component(service = ViewCountEntryPersistence.class)
@ProviderType
public class ViewCountEntryPersistenceImpl
	extends BasePersistenceImpl<ViewCountEntry>
	implements ViewCountEntryPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>ViewCountEntryUtil</code> to access the view count entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		ViewCountEntryImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByC_C;
	private FinderPath _finderPathWithoutPaginationFindByC_C;
	private FinderPath _finderPathCountByC_C;
	private FinderPath _finderPathWithPaginationCountByC_C;

	/**
	 * Returns all the view count entries where companyId = &#63; and classNameId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @return the matching view count entries
	 */
	@Override
	public List<ViewCountEntry> findByC_C(long companyId, long classNameId) {
		return findByC_C(
			companyId, classNameId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the view count entries where companyId = &#63; and classNameId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ViewCountEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param start the lower bound of the range of view count entries
	 * @param end the upper bound of the range of view count entries (not inclusive)
	 * @return the range of matching view count entries
	 */
	@Override
	public List<ViewCountEntry> findByC_C(
		long companyId, long classNameId, int start, int end) {

		return findByC_C(companyId, classNameId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the view count entries where companyId = &#63; and classNameId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ViewCountEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param start the lower bound of the range of view count entries
	 * @param end the upper bound of the range of view count entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching view count entries
	 */
	@Override
	public List<ViewCountEntry> findByC_C(
		long companyId, long classNameId, int start, int end,
		OrderByComparator<ViewCountEntry> orderByComparator) {

		return findByC_C(
			companyId, classNameId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the view count entries where companyId = &#63; and classNameId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ViewCountEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param start the lower bound of the range of view count entries
	 * @param end the upper bound of the range of view count entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching view count entries
	 */
	@Override
	public List<ViewCountEntry> findByC_C(
		long companyId, long classNameId, int start, int end,
		OrderByComparator<ViewCountEntry> orderByComparator,
		boolean useFinderCache) {

		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			pagination = false;

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByC_C;
				finderArgs = new Object[] {companyId, classNameId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByC_C;
			finderArgs = new Object[] {
				companyId, classNameId, start, end, orderByComparator
			};
		}

		List<ViewCountEntry> list = null;

		if (useFinderCache) {
			list = (List<ViewCountEntry>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (ViewCountEntry viewCountEntry : list) {
					if ((companyId != viewCountEntry.getCompanyId()) ||
						(classNameId != viewCountEntry.getClassNameId())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_VIEWCOUNTENTRY_WHERE);

			query.append(_FINDER_COLUMN_C_C_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_C_CLASSNAMEID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else if (pagination) {
				query.append(ViewCountEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(classNameId);

				if (!pagination) {
					list = (List<ViewCountEntry>)QueryUtil.list(
						q, getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<ViewCountEntry>)QueryUtil.list(
						q, getDialect(), start, end);
				}

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception e) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first view count entry in the ordered set where companyId = &#63; and classNameId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching view count entry
	 * @throws NoSuchEntryException if a matching view count entry could not be found
	 */
	@Override
	public ViewCountEntry findByC_C_First(
			long companyId, long classNameId,
			OrderByComparator<ViewCountEntry> orderByComparator)
		throws NoSuchEntryException {

		ViewCountEntry viewCountEntry = fetchByC_C_First(
			companyId, classNameId, orderByComparator);

		if (viewCountEntry != null) {
			return viewCountEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", classNameId=");
		msg.append(classNameId);

		msg.append("}");

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first view count entry in the ordered set where companyId = &#63; and classNameId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching view count entry, or <code>null</code> if a matching view count entry could not be found
	 */
	@Override
	public ViewCountEntry fetchByC_C_First(
		long companyId, long classNameId,
		OrderByComparator<ViewCountEntry> orderByComparator) {

		List<ViewCountEntry> list = findByC_C(
			companyId, classNameId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last view count entry in the ordered set where companyId = &#63; and classNameId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching view count entry
	 * @throws NoSuchEntryException if a matching view count entry could not be found
	 */
	@Override
	public ViewCountEntry findByC_C_Last(
			long companyId, long classNameId,
			OrderByComparator<ViewCountEntry> orderByComparator)
		throws NoSuchEntryException {

		ViewCountEntry viewCountEntry = fetchByC_C_Last(
			companyId, classNameId, orderByComparator);

		if (viewCountEntry != null) {
			return viewCountEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", classNameId=");
		msg.append(classNameId);

		msg.append("}");

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last view count entry in the ordered set where companyId = &#63; and classNameId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching view count entry, or <code>null</code> if a matching view count entry could not be found
	 */
	@Override
	public ViewCountEntry fetchByC_C_Last(
		long companyId, long classNameId,
		OrderByComparator<ViewCountEntry> orderByComparator) {

		int count = countByC_C(companyId, classNameId);

		if (count == 0) {
			return null;
		}

		List<ViewCountEntry> list = findByC_C(
			companyId, classNameId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the view count entries before and after the current view count entry in the ordered set where companyId = &#63; and classNameId = &#63;.
	 *
	 * @param viewCountEntryId the primary key of the current view count entry
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next view count entry
	 * @throws NoSuchEntryException if a view count entry with the primary key could not be found
	 */
	@Override
	public ViewCountEntry[] findByC_C_PrevAndNext(
			long viewCountEntryId, long companyId, long classNameId,
			OrderByComparator<ViewCountEntry> orderByComparator)
		throws NoSuchEntryException {

		ViewCountEntry viewCountEntry = findByPrimaryKey(viewCountEntryId);

		Session session = null;

		try {
			session = openSession();

			ViewCountEntry[] array = new ViewCountEntryImpl[3];

			array[0] = getByC_C_PrevAndNext(
				session, viewCountEntry, companyId, classNameId,
				orderByComparator, true);

			array[1] = viewCountEntry;

			array[2] = getByC_C_PrevAndNext(
				session, viewCountEntry, companyId, classNameId,
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

	protected ViewCountEntry getByC_C_PrevAndNext(
		Session session, ViewCountEntry viewCountEntry, long companyId,
		long classNameId, OrderByComparator<ViewCountEntry> orderByComparator,
		boolean previous) {

		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(4);
		}

		query.append(_SQL_SELECT_VIEWCOUNTENTRY_WHERE);

		query.append(_FINDER_COLUMN_C_C_COMPANYID_2);

		query.append(_FINDER_COLUMN_C_C_CLASSNAMEID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

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
			query.append(ViewCountEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(companyId);

		qPos.add(classNameId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						viewCountEntry)) {

				qPos.add(orderByConditionValue);
			}
		}

		List<ViewCountEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the view count entries where companyId = &#63; and classNameId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ViewCountEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param classNameIds the class name IDs
	 * @return the matching view count entries
	 */
	@Override
	public List<ViewCountEntry> findByC_C(long companyId, long[] classNameIds) {
		return findByC_C(
			companyId, classNameIds, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the view count entries where companyId = &#63; and classNameId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ViewCountEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param classNameIds the class name IDs
	 * @param start the lower bound of the range of view count entries
	 * @param end the upper bound of the range of view count entries (not inclusive)
	 * @return the range of matching view count entries
	 */
	@Override
	public List<ViewCountEntry> findByC_C(
		long companyId, long[] classNameIds, int start, int end) {

		return findByC_C(companyId, classNameIds, start, end, null);
	}

	/**
	 * Returns an ordered range of all the view count entries where companyId = &#63; and classNameId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ViewCountEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param classNameIds the class name IDs
	 * @param start the lower bound of the range of view count entries
	 * @param end the upper bound of the range of view count entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching view count entries
	 */
	@Override
	public List<ViewCountEntry> findByC_C(
		long companyId, long[] classNameIds, int start, int end,
		OrderByComparator<ViewCountEntry> orderByComparator) {

		return findByC_C(
			companyId, classNameIds, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the view count entries where companyId = &#63; and classNameId = &#63;, optionally using the finder cache.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ViewCountEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param start the lower bound of the range of view count entries
	 * @param end the upper bound of the range of view count entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching view count entries
	 */
	@Override
	public List<ViewCountEntry> findByC_C(
		long companyId, long[] classNameIds, int start, int end,
		OrderByComparator<ViewCountEntry> orderByComparator,
		boolean useFinderCache) {

		if (classNameIds == null) {
			classNameIds = new long[0];
		}
		else if (classNameIds.length > 1) {
			classNameIds = ArrayUtil.sortedUnique(classNameIds);
		}

		if (classNameIds.length == 1) {
			return findByC_C(
				companyId, classNameIds[0], start, end, orderByComparator);
		}

		boolean pagination = true;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			pagination = false;

			if (useFinderCache) {
				finderArgs = new Object[] {
					companyId, StringUtil.merge(classNameIds)
				};
			}
		}
		else if (useFinderCache) {
			finderArgs = new Object[] {
				companyId, StringUtil.merge(classNameIds), start, end,
				orderByComparator
			};
		}

		List<ViewCountEntry> list = null;

		if (useFinderCache) {
			list = (List<ViewCountEntry>)finderCache.getResult(
				_finderPathWithPaginationFindByC_C, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (ViewCountEntry viewCountEntry : list) {
					if ((companyId != viewCountEntry.getCompanyId()) ||
						!ArrayUtil.contains(
							classNameIds, viewCountEntry.getClassNameId())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_SELECT_VIEWCOUNTENTRY_WHERE);

			query.append(_FINDER_COLUMN_C_C_COMPANYID_2);

			if (classNameIds.length > 0) {
				query.append("(");

				query.append(_FINDER_COLUMN_C_C_CLASSNAMEID_7);

				query.append(StringUtil.merge(classNameIds));

				query.append(")");

				query.append(")");
			}

			query.setStringAt(
				removeConjunction(query.stringAt(query.index() - 1)),
				query.index() - 1);

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else if (pagination) {
				query.append(ViewCountEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				if (!pagination) {
					list = (List<ViewCountEntry>)QueryUtil.list(
						q, getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<ViewCountEntry>)QueryUtil.list(
						q, getDialect(), start, end);
				}

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(
						_finderPathWithPaginationFindByC_C, finderArgs, list);
				}
			}
			catch (Exception e) {
				if (useFinderCache) {
					finderCache.removeResult(
						_finderPathWithPaginationFindByC_C, finderArgs);
				}

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the view count entries where companyId = &#63; and classNameId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 */
	@Override
	public void removeByC_C(long companyId, long classNameId) {
		for (ViewCountEntry viewCountEntry :
				findByC_C(
					companyId, classNameId, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(viewCountEntry);
		}
	}

	/**
	 * Returns the number of view count entries where companyId = &#63; and classNameId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @return the number of matching view count entries
	 */
	@Override
	public int countByC_C(long companyId, long classNameId) {
		FinderPath finderPath = _finderPathCountByC_C;

		Object[] finderArgs = new Object[] {companyId, classNameId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_VIEWCOUNTENTRY_WHERE);

			query.append(_FINDER_COLUMN_C_C_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_C_CLASSNAMEID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(classNameId);

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

	/**
	 * Returns the number of view count entries where companyId = &#63; and classNameId = any &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameIds the class name IDs
	 * @return the number of matching view count entries
	 */
	@Override
	public int countByC_C(long companyId, long[] classNameIds) {
		if (classNameIds == null) {
			classNameIds = new long[0];
		}
		else if (classNameIds.length > 1) {
			classNameIds = ArrayUtil.sortedUnique(classNameIds);
		}

		Object[] finderArgs = new Object[] {
			companyId, StringUtil.merge(classNameIds)
		};

		Long count = (Long)finderCache.getResult(
			_finderPathWithPaginationCountByC_C, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_COUNT_VIEWCOUNTENTRY_WHERE);

			query.append(_FINDER_COLUMN_C_C_COMPANYID_2);

			if (classNameIds.length > 0) {
				query.append("(");

				query.append(_FINDER_COLUMN_C_C_CLASSNAMEID_7);

				query.append(StringUtil.merge(classNameIds));

				query.append(")");

				query.append(")");
			}

			query.setStringAt(
				removeConjunction(query.stringAt(query.index() - 1)),
				query.index() - 1);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(
					_finderPathWithPaginationCountByC_C, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(
					_finderPathWithPaginationCountByC_C, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_C_C_COMPANYID_2 =
		"viewCountEntry.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_C_CLASSNAMEID_2 =
		"viewCountEntry.classNameId = ?";

	private static final String _FINDER_COLUMN_C_C_CLASSNAMEID_7 =
		"viewCountEntry.classNameId IN (";

	private FinderPath _finderPathFetchByC_C_C;
	private FinderPath _finderPathCountByC_C_C;

	/**
	 * Returns the view count entry where companyId = &#63; and classNameId = &#63; and classPK = &#63; or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the matching view count entry
	 * @throws NoSuchEntryException if a matching view count entry could not be found
	 */
	@Override
	public ViewCountEntry findByC_C_C(
			long companyId, long classNameId, long classPK)
		throws NoSuchEntryException {

		ViewCountEntry viewCountEntry = fetchByC_C_C(
			companyId, classNameId, classPK);

		if (viewCountEntry == null) {
			StringBundler msg = new StringBundler(8);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("companyId=");
			msg.append(companyId);

			msg.append(", classNameId=");
			msg.append(classNameId);

			msg.append(", classPK=");
			msg.append(classPK);

			msg.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(msg.toString());
			}

			throw new NoSuchEntryException(msg.toString());
		}

		return viewCountEntry;
	}

	/**
	 * Returns the view count entry where companyId = &#63; and classNameId = &#63; and classPK = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the matching view count entry, or <code>null</code> if a matching view count entry could not be found
	 */
	@Override
	public ViewCountEntry fetchByC_C_C(
		long companyId, long classNameId, long classPK) {

		return fetchByC_C_C(companyId, classNameId, classPK, true);
	}

	/**
	 * Returns the view count entry where companyId = &#63; and classNameId = &#63; and classPK = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching view count entry, or <code>null</code> if a matching view count entry could not be found
	 */
	@Override
	public ViewCountEntry fetchByC_C_C(
		long companyId, long classNameId, long classPK,
		boolean useFinderCache) {

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {companyId, classNameId, classPK};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByC_C_C, finderArgs, this);
		}

		if (result instanceof ViewCountEntry) {
			ViewCountEntry viewCountEntry = (ViewCountEntry)result;

			if ((companyId != viewCountEntry.getCompanyId()) ||
				(classNameId != viewCountEntry.getClassNameId()) ||
				(classPK != viewCountEntry.getClassPK())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(5);

			query.append(_SQL_SELECT_VIEWCOUNTENTRY_WHERE);

			query.append(_FINDER_COLUMN_C_C_C_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_C_C_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_C_C_C_CLASSPK_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(classNameId);

				qPos.add(classPK);

				List<ViewCountEntry> list = q.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByC_C_C, finderArgs, list);
					}
				}
				else {
					ViewCountEntry viewCountEntry = list.get(0);

					result = viewCountEntry;

					cacheResult(viewCountEntry);
				}
			}
			catch (Exception e) {
				if (useFinderCache) {
					finderCache.removeResult(
						_finderPathFetchByC_C_C, finderArgs);
				}

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
			return (ViewCountEntry)result;
		}
	}

	/**
	 * Removes the view count entry where companyId = &#63; and classNameId = &#63; and classPK = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the view count entry that was removed
	 */
	@Override
	public ViewCountEntry removeByC_C_C(
			long companyId, long classNameId, long classPK)
		throws NoSuchEntryException {

		ViewCountEntry viewCountEntry = findByC_C_C(
			companyId, classNameId, classPK);

		return remove(viewCountEntry);
	}

	/**
	 * Returns the number of view count entries where companyId = &#63; and classNameId = &#63; and classPK = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the number of matching view count entries
	 */
	@Override
	public int countByC_C_C(long companyId, long classNameId, long classPK) {
		FinderPath finderPath = _finderPathCountByC_C_C;

		Object[] finderArgs = new Object[] {companyId, classNameId, classPK};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_VIEWCOUNTENTRY_WHERE);

			query.append(_FINDER_COLUMN_C_C_C_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_C_C_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_C_C_C_CLASSPK_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(classNameId);

				qPos.add(classPK);

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

	private static final String _FINDER_COLUMN_C_C_C_COMPANYID_2 =
		"viewCountEntry.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_C_C_CLASSNAMEID_2 =
		"viewCountEntry.classNameId = ? AND ";

	private static final String _FINDER_COLUMN_C_C_C_CLASSPK_2 =
		"viewCountEntry.classPK = ?";

	public ViewCountEntryPersistenceImpl() {
		setModelClass(ViewCountEntry.class);

		setModelImplClass(ViewCountEntryImpl.class);
		setModelPKClass(long.class);
	}

	/**
	 * Caches the view count entry in the entity cache if it is enabled.
	 *
	 * @param viewCountEntry the view count entry
	 */
	@Override
	public void cacheResult(ViewCountEntry viewCountEntry) {
		entityCache.putResult(
			entityCacheEnabled, ViewCountEntryImpl.class,
			viewCountEntry.getPrimaryKey(), viewCountEntry);

		finderCache.putResult(
			_finderPathFetchByC_C_C,
			new Object[] {
				viewCountEntry.getCompanyId(), viewCountEntry.getClassNameId(),
				viewCountEntry.getClassPK()
			},
			viewCountEntry);

		viewCountEntry.resetOriginalValues();
	}

	/**
	 * Caches the view count entries in the entity cache if it is enabled.
	 *
	 * @param viewCountEntries the view count entries
	 */
	@Override
	public void cacheResult(List<ViewCountEntry> viewCountEntries) {
		for (ViewCountEntry viewCountEntry : viewCountEntries) {
			if (entityCache.getResult(
					entityCacheEnabled, ViewCountEntryImpl.class,
					viewCountEntry.getPrimaryKey()) == null) {

				cacheResult(viewCountEntry);
			}
			else {
				viewCountEntry.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all view count entries.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(ViewCountEntryImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the view count entry.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(ViewCountEntry viewCountEntry) {
		entityCache.removeResult(
			entityCacheEnabled, ViewCountEntryImpl.class,
			viewCountEntry.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache((ViewCountEntryModelImpl)viewCountEntry, true);
	}

	@Override
	public void clearCache(List<ViewCountEntry> viewCountEntries) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (ViewCountEntry viewCountEntry : viewCountEntries) {
			entityCache.removeResult(
				entityCacheEnabled, ViewCountEntryImpl.class,
				viewCountEntry.getPrimaryKey());

			clearUniqueFindersCache(
				(ViewCountEntryModelImpl)viewCountEntry, true);
		}
	}

	protected void cacheUniqueFindersCache(
		ViewCountEntryModelImpl viewCountEntryModelImpl) {

		Object[] args = new Object[] {
			viewCountEntryModelImpl.getCompanyId(),
			viewCountEntryModelImpl.getClassNameId(),
			viewCountEntryModelImpl.getClassPK()
		};

		finderCache.putResult(
			_finderPathCountByC_C_C, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByC_C_C, args, viewCountEntryModelImpl, false);
	}

	protected void clearUniqueFindersCache(
		ViewCountEntryModelImpl viewCountEntryModelImpl, boolean clearCurrent) {

		if (clearCurrent) {
			Object[] args = new Object[] {
				viewCountEntryModelImpl.getCompanyId(),
				viewCountEntryModelImpl.getClassNameId(),
				viewCountEntryModelImpl.getClassPK()
			};

			finderCache.removeResult(_finderPathCountByC_C_C, args);
			finderCache.removeResult(_finderPathFetchByC_C_C, args);
		}

		if ((viewCountEntryModelImpl.getColumnBitmask() &
			 _finderPathFetchByC_C_C.getColumnBitmask()) != 0) {

			Object[] args = new Object[] {
				viewCountEntryModelImpl.getOriginalCompanyId(),
				viewCountEntryModelImpl.getOriginalClassNameId(),
				viewCountEntryModelImpl.getOriginalClassPK()
			};

			finderCache.removeResult(_finderPathCountByC_C_C, args);
			finderCache.removeResult(_finderPathFetchByC_C_C, args);
		}
	}

	/**
	 * Creates a new view count entry with the primary key. Does not add the view count entry to the database.
	 *
	 * @param viewCountEntryId the primary key for the new view count entry
	 * @return the new view count entry
	 */
	@Override
	public ViewCountEntry create(long viewCountEntryId) {
		ViewCountEntry viewCountEntry = new ViewCountEntryImpl();

		viewCountEntry.setNew(true);
		viewCountEntry.setPrimaryKey(viewCountEntryId);

		viewCountEntry.setCompanyId(CompanyThreadLocal.getCompanyId());

		return viewCountEntry;
	}

	/**
	 * Removes the view count entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param viewCountEntryId the primary key of the view count entry
	 * @return the view count entry that was removed
	 * @throws NoSuchEntryException if a view count entry with the primary key could not be found
	 */
	@Override
	public ViewCountEntry remove(long viewCountEntryId)
		throws NoSuchEntryException {

		return remove((Serializable)viewCountEntryId);
	}

	/**
	 * Removes the view count entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the view count entry
	 * @return the view count entry that was removed
	 * @throws NoSuchEntryException if a view count entry with the primary key could not be found
	 */
	@Override
	public ViewCountEntry remove(Serializable primaryKey)
		throws NoSuchEntryException {

		Session session = null;

		try {
			session = openSession();

			ViewCountEntry viewCountEntry = (ViewCountEntry)session.get(
				ViewCountEntryImpl.class, primaryKey);

			if (viewCountEntry == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchEntryException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(viewCountEntry);
		}
		catch (NoSuchEntryException nsee) {
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
	protected ViewCountEntry removeImpl(ViewCountEntry viewCountEntry) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(viewCountEntry)) {
				viewCountEntry = (ViewCountEntry)session.get(
					ViewCountEntryImpl.class,
					viewCountEntry.getPrimaryKeyObj());
			}

			if (viewCountEntry != null) {
				session.delete(viewCountEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (viewCountEntry != null) {
			clearCache(viewCountEntry);
		}

		return viewCountEntry;
	}

	@Override
	public ViewCountEntry updateImpl(ViewCountEntry viewCountEntry) {
		boolean isNew = viewCountEntry.isNew();

		if (!(viewCountEntry instanceof ViewCountEntryModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(viewCountEntry.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					viewCountEntry);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in viewCountEntry proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom ViewCountEntry implementation " +
					viewCountEntry.getClass());
		}

		ViewCountEntryModelImpl viewCountEntryModelImpl =
			(ViewCountEntryModelImpl)viewCountEntry;

		Session session = null;

		try {
			session = openSession();

			if (viewCountEntry.isNew()) {
				session.save(viewCountEntry);

				viewCountEntry.setNew(false);
			}
			else {
				viewCountEntry = (ViewCountEntry)session.merge(viewCountEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!_columnBitmaskEnabled) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else if (isNew) {
			Object[] args = new Object[] {
				viewCountEntryModelImpl.getCompanyId(),
				viewCountEntryModelImpl.getClassNameId()
			};

			finderCache.removeResult(_finderPathCountByC_C, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByC_C, args);

			finderCache.removeResult(_finderPathCountAll, FINDER_ARGS_EMPTY);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindAll, FINDER_ARGS_EMPTY);
		}
		else {
			if ((viewCountEntryModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByC_C.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					viewCountEntryModelImpl.getOriginalCompanyId(),
					viewCountEntryModelImpl.getOriginalClassNameId()
				};

				finderCache.removeResult(_finderPathCountByC_C, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByC_C, args);

				args = new Object[] {
					viewCountEntryModelImpl.getCompanyId(),
					viewCountEntryModelImpl.getClassNameId()
				};

				finderCache.removeResult(_finderPathCountByC_C, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByC_C, args);
			}
		}

		entityCache.putResult(
			entityCacheEnabled, ViewCountEntryImpl.class,
			viewCountEntry.getPrimaryKey(), viewCountEntry, false);

		clearUniqueFindersCache(viewCountEntryModelImpl, false);
		cacheUniqueFindersCache(viewCountEntryModelImpl);

		viewCountEntry.resetOriginalValues();

		return viewCountEntry;
	}

	/**
	 * Returns the view count entry with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the view count entry
	 * @return the view count entry
	 * @throws NoSuchEntryException if a view count entry with the primary key could not be found
	 */
	@Override
	public ViewCountEntry findByPrimaryKey(Serializable primaryKey)
		throws NoSuchEntryException {

		ViewCountEntry viewCountEntry = fetchByPrimaryKey(primaryKey);

		if (viewCountEntry == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchEntryException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return viewCountEntry;
	}

	/**
	 * Returns the view count entry with the primary key or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param viewCountEntryId the primary key of the view count entry
	 * @return the view count entry
	 * @throws NoSuchEntryException if a view count entry with the primary key could not be found
	 */
	@Override
	public ViewCountEntry findByPrimaryKey(long viewCountEntryId)
		throws NoSuchEntryException {

		return findByPrimaryKey((Serializable)viewCountEntryId);
	}

	/**
	 * Returns the view count entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param viewCountEntryId the primary key of the view count entry
	 * @return the view count entry, or <code>null</code> if a view count entry with the primary key could not be found
	 */
	@Override
	public ViewCountEntry fetchByPrimaryKey(long viewCountEntryId) {
		return fetchByPrimaryKey((Serializable)viewCountEntryId);
	}

	/**
	 * Returns all the view count entries.
	 *
	 * @return the view count entries
	 */
	@Override
	public List<ViewCountEntry> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the view count entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ViewCountEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of view count entries
	 * @param end the upper bound of the range of view count entries (not inclusive)
	 * @return the range of view count entries
	 */
	@Override
	public List<ViewCountEntry> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the view count entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ViewCountEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of view count entries
	 * @param end the upper bound of the range of view count entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of view count entries
	 */
	@Override
	public List<ViewCountEntry> findAll(
		int start, int end,
		OrderByComparator<ViewCountEntry> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the view count entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ViewCountEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of view count entries
	 * @param end the upper bound of the range of view count entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of view count entries
	 */
	@Override
	public List<ViewCountEntry> findAll(
		int start, int end, OrderByComparator<ViewCountEntry> orderByComparator,
		boolean useFinderCache) {

		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			pagination = false;

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<ViewCountEntry> list = null;

		if (useFinderCache) {
			list = (List<ViewCountEntry>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_VIEWCOUNTENTRY);

				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_VIEWCOUNTENTRY;

				if (pagination) {
					sql = sql.concat(ViewCountEntryModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<ViewCountEntry>)QueryUtil.list(
						q, getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<ViewCountEntry>)QueryUtil.list(
						q, getDialect(), start, end);
				}

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception e) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the view count entries from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (ViewCountEntry viewCountEntry : findAll()) {
			remove(viewCountEntry);
		}
	}

	/**
	 * Returns the number of view count entries.
	 *
	 * @return the number of view count entries
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_VIEWCOUNTENTRY);

				count = (Long)q.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				finderCache.removeResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY);

				throw processException(e);
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
		return "viewCountEntryId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_VIEWCOUNTENTRY;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return ViewCountEntryModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the view count entry persistence.
	 */
	@Activate
	public void activate() {
		ViewCountEntryModelImpl.setEntityCacheEnabled(entityCacheEnabled);
		ViewCountEntryModelImpl.setFinderCacheEnabled(finderCacheEnabled);

		_finderPathWithPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, ViewCountEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, ViewCountEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll",
			new String[0]);

		_finderPathCountAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);

		_finderPathWithPaginationFindByC_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, ViewCountEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByC_C",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByC_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, ViewCountEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByC_C",
			new String[] {Long.class.getName(), Long.class.getName()},
			ViewCountEntryModelImpl.COMPANYID_COLUMN_BITMASK |
			ViewCountEntryModelImpl.CLASSNAMEID_COLUMN_BITMASK |
			ViewCountEntryModelImpl.VIEWCOUNT_COLUMN_BITMASK);

		_finderPathCountByC_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_C",
			new String[] {Long.class.getName(), Long.class.getName()});

		_finderPathWithPaginationCountByC_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByC_C",
			new String[] {Long.class.getName(), Long.class.getName()});

		_finderPathFetchByC_C_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, ViewCountEntryImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByC_C_C",
			new String[] {
				Long.class.getName(), Long.class.getName(), Long.class.getName()
			},
			ViewCountEntryModelImpl.COMPANYID_COLUMN_BITMASK |
			ViewCountEntryModelImpl.CLASSNAMEID_COLUMN_BITMASK |
			ViewCountEntryModelImpl.CLASSPK_COLUMN_BITMASK);

		_finderPathCountByC_C_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_C_C",
			new String[] {
				Long.class.getName(), Long.class.getName(), Long.class.getName()
			});
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(ViewCountEntryImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	@Reference(
		target = ViewCountPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.view.count.model.ViewCountEntry"),
			true);
	}

	@Override
	@Reference(
		target = ViewCountPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = ViewCountPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private boolean _columnBitmaskEnabled;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_VIEWCOUNTENTRY =
		"SELECT viewCountEntry FROM ViewCountEntry viewCountEntry";

	private static final String _SQL_SELECT_VIEWCOUNTENTRY_WHERE =
		"SELECT viewCountEntry FROM ViewCountEntry viewCountEntry WHERE ";

	private static final String _SQL_COUNT_VIEWCOUNTENTRY =
		"SELECT COUNT(viewCountEntry) FROM ViewCountEntry viewCountEntry";

	private static final String _SQL_COUNT_VIEWCOUNTENTRY_WHERE =
		"SELECT COUNT(viewCountEntry) FROM ViewCountEntry viewCountEntry WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "viewCountEntry.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No ViewCountEntry exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No ViewCountEntry exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		ViewCountEntryPersistenceImpl.class);

	static {
		try {
			Class.forName(ViewCountPersistenceConstants.class.getName());
		}
		catch (ClassNotFoundException cnfe) {
			throw new ExceptionInInitializerError(cnfe);
		}
	}

}
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

package com.liferay.journal.service.persistence.impl;

import com.liferay.journal.exception.NoSuchArticleResourceException;
import com.liferay.journal.model.JournalArticleResource;
import com.liferay.journal.model.JournalArticleResourceTable;
import com.liferay.journal.model.impl.JournalArticleResourceImpl;
import com.liferay.journal.model.impl.JournalArticleResourceModelImpl;
import com.liferay.journal.service.persistence.JournalArticleResourcePersistence;
import com.liferay.journal.service.persistence.impl.constants.JournalPersistenceConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.change.tracking.CTColumnResolutionType;
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
import com.liferay.portal.kernel.service.persistence.change.tracking.helper.CTPersistenceHelper;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
 * The persistence implementation for the journal article resource service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = JournalArticleResourcePersistence.class)
public class JournalArticleResourcePersistenceImpl
	extends BasePersistenceImpl<JournalArticleResource>
	implements JournalArticleResourcePersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>JournalArticleResourceUtil</code> to access the journal article resource persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		JournalArticleResourceImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	/**
	 * Returns all the journal article resources where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching journal article resources
	 */
	@Override
	public List<JournalArticleResource> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the journal article resources where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>JournalArticleResourceModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of journal article resources
	 * @param end the upper bound of the range of journal article resources (not inclusive)
	 * @return the range of matching journal article resources
	 */
	@Override
	public List<JournalArticleResource> findByUuid(
		String uuid, int start, int end) {

		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the journal article resources where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>JournalArticleResourceModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of journal article resources
	 * @param end the upper bound of the range of journal article resources (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching journal article resources
	 */
	@Override
	public List<JournalArticleResource> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<JournalArticleResource> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the journal article resources where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>JournalArticleResourceModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of journal article resources
	 * @param end the upper bound of the range of journal article resources (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching journal article resources
	 */
	@Override
	public List<JournalArticleResource> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<JournalArticleResource> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			JournalArticleResource.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid");
				finderArgs = new Object[] {uuid};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid");
			finderArgs = new Object[] {uuid, start, end, orderByComparator};
		}

		List<JournalArticleResource> list = null;

		if (useFinderCache && productionMode) {
			list = (List<JournalArticleResource>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (JournalArticleResource journalArticleResource : list) {
					if (!uuid.equals(journalArticleResource.getUuid())) {
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

			sb.append(_SQL_SELECT_JOURNALARTICLERESOURCE_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(JournalArticleResourceModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				list = (List<JournalArticleResource>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache && productionMode) {
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
	 * Returns the first journal article resource in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal article resource
	 * @throws NoSuchArticleResourceException if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource findByUuid_First(
			String uuid,
			OrderByComparator<JournalArticleResource> orderByComparator)
		throws NoSuchArticleResourceException {

		JournalArticleResource journalArticleResource = fetchByUuid_First(
			uuid, orderByComparator);

		if (journalArticleResource != null) {
			return journalArticleResource;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchArticleResourceException(sb.toString());
	}

	/**
	 * Returns the first journal article resource in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal article resource, or <code>null</code> if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource fetchByUuid_First(
		String uuid,
		OrderByComparator<JournalArticleResource> orderByComparator) {

		List<JournalArticleResource> list = findByUuid(
			uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last journal article resource in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal article resource
	 * @throws NoSuchArticleResourceException if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource findByUuid_Last(
			String uuid,
			OrderByComparator<JournalArticleResource> orderByComparator)
		throws NoSuchArticleResourceException {

		JournalArticleResource journalArticleResource = fetchByUuid_Last(
			uuid, orderByComparator);

		if (journalArticleResource != null) {
			return journalArticleResource;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchArticleResourceException(sb.toString());
	}

	/**
	 * Returns the last journal article resource in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal article resource, or <code>null</code> if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource fetchByUuid_Last(
		String uuid,
		OrderByComparator<JournalArticleResource> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<JournalArticleResource> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the journal article resources before and after the current journal article resource in the ordered set where uuid = &#63;.
	 *
	 * @param resourcePrimKey the primary key of the current journal article resource
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next journal article resource
	 * @throws NoSuchArticleResourceException if a journal article resource with the primary key could not be found
	 */
	@Override
	public JournalArticleResource[] findByUuid_PrevAndNext(
			long resourcePrimKey, String uuid,
			OrderByComparator<JournalArticleResource> orderByComparator)
		throws NoSuchArticleResourceException {

		uuid = Objects.toString(uuid, "");

		JournalArticleResource journalArticleResource = findByPrimaryKey(
			resourcePrimKey);

		Session session = null;

		try {
			session = openSession();

			JournalArticleResource[] array = new JournalArticleResourceImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, journalArticleResource, uuid, orderByComparator, true);

			array[1] = journalArticleResource;

			array[2] = getByUuid_PrevAndNext(
				session, journalArticleResource, uuid, orderByComparator,
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

	protected JournalArticleResource getByUuid_PrevAndNext(
		Session session, JournalArticleResource journalArticleResource,
		String uuid,
		OrderByComparator<JournalArticleResource> orderByComparator,
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

		sb.append(_SQL_SELECT_JOURNALARTICLERESOURCE_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2);
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
			sb.append(JournalArticleResourceModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						journalArticleResource)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<JournalArticleResource> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the journal article resources where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (JournalArticleResource journalArticleResource :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(journalArticleResource);
		}
	}

	/**
	 * Returns the number of journal article resources where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching journal article resources
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			JournalArticleResource.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid");

			finderArgs = new Object[] {uuid};

			count = (Long)finderCache.getResult(finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_JOURNALARTICLERESOURCE_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				if (productionMode) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_UUID_2 =
		"journalArticleResource.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(journalArticleResource.uuid IS NULL OR journalArticleResource.uuid = '')";

	/**
	 * Returns the journal article resource where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchArticleResourceException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching journal article resource
	 * @throws NoSuchArticleResourceException if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource findByUUID_G(String uuid, long groupId)
		throws NoSuchArticleResourceException {

		JournalArticleResource journalArticleResource = fetchByUUID_G(
			uuid, groupId);

		if (journalArticleResource == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("uuid=");
			sb.append(uuid);

			sb.append(", groupId=");
			sb.append(groupId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchArticleResourceException(sb.toString());
		}

		return journalArticleResource;
	}

	/**
	 * Returns the journal article resource where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching journal article resource, or <code>null</code> if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource fetchByUUID_G(String uuid, long groupId) {
		return fetchByUUID_G(uuid, groupId, true);
	}

	/**
	 * Returns the journal article resource where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching journal article resource, or <code>null</code> if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			JournalArticleResource.class);

		Object[] finderArgs = null;

		if (useFinderCache && productionMode) {
			finderArgs = new Object[] {uuid, groupId};
		}

		Object result = null;

		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByUUID_G");

		if (useFinderCache && productionMode) {
			result = finderCache.getResult(finderPath, finderArgs, this);
		}

		if (result instanceof JournalArticleResource) {
			JournalArticleResource journalArticleResource =
				(JournalArticleResource)result;

			if (!Objects.equals(uuid, journalArticleResource.getUuid()) ||
				(groupId != journalArticleResource.getGroupId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_JOURNALARTICLERESOURCE_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_G_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_G_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(groupId);

				List<JournalArticleResource> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache && productionMode) {
						finderCache.putResult(finderPath, finderArgs, list);
					}
				}
				else {
					JournalArticleResource journalArticleResource = list.get(0);

					result = journalArticleResource;

					cacheResult(journalArticleResource);
				}
			}
			catch (Exception exception) {
				if (useFinderCache && productionMode) {
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
			return (JournalArticleResource)result;
		}
	}

	/**
	 * Removes the journal article resource where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the journal article resource that was removed
	 */
	@Override
	public JournalArticleResource removeByUUID_G(String uuid, long groupId)
		throws NoSuchArticleResourceException {

		JournalArticleResource journalArticleResource = findByUUID_G(
			uuid, groupId);

		return remove(journalArticleResource);
	}

	/**
	 * Returns the number of journal article resources where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching journal article resources
	 */
	@Override
	public int countByUUID_G(String uuid, long groupId) {
		uuid = Objects.toString(uuid, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			JournalArticleResource.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUUID_G");

			finderArgs = new Object[] {uuid, groupId};

			count = (Long)finderCache.getResult(finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_JOURNALARTICLERESOURCE_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_G_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_G_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(groupId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				if (productionMode) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_G_UUID_2 =
		"journalArticleResource.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_G_UUID_3 =
		"(journalArticleResource.uuid IS NULL OR journalArticleResource.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_G_GROUPID_2 =
		"journalArticleResource.groupId = ?";

	/**
	 * Returns all the journal article resources where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching journal article resources
	 */
	@Override
	public List<JournalArticleResource> findByUuid_C(
		String uuid, long companyId) {

		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the journal article resources where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>JournalArticleResourceModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of journal article resources
	 * @param end the upper bound of the range of journal article resources (not inclusive)
	 * @return the range of matching journal article resources
	 */
	@Override
	public List<JournalArticleResource> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the journal article resources where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>JournalArticleResourceModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of journal article resources
	 * @param end the upper bound of the range of journal article resources (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching journal article resources
	 */
	@Override
	public List<JournalArticleResource> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<JournalArticleResource> orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the journal article resources where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>JournalArticleResourceModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of journal article resources
	 * @param end the upper bound of the range of journal article resources (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching journal article resources
	 */
	@Override
	public List<JournalArticleResource> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<JournalArticleResource> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			JournalArticleResource.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid_C");
				finderArgs = new Object[] {uuid, companyId};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid_C");
			finderArgs = new Object[] {
				uuid, companyId, start, end, orderByComparator
			};
		}

		List<JournalArticleResource> list = null;

		if (useFinderCache && productionMode) {
			list = (List<JournalArticleResource>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (JournalArticleResource journalArticleResource : list) {
					if (!uuid.equals(journalArticleResource.getUuid()) ||
						(companyId != journalArticleResource.getCompanyId())) {

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

			sb.append(_SQL_SELECT_JOURNALARTICLERESOURCE_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(JournalArticleResourceModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				list = (List<JournalArticleResource>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache && productionMode) {
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
	 * Returns the first journal article resource in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal article resource
	 * @throws NoSuchArticleResourceException if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<JournalArticleResource> orderByComparator)
		throws NoSuchArticleResourceException {

		JournalArticleResource journalArticleResource = fetchByUuid_C_First(
			uuid, companyId, orderByComparator);

		if (journalArticleResource != null) {
			return journalArticleResource;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchArticleResourceException(sb.toString());
	}

	/**
	 * Returns the first journal article resource in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal article resource, or <code>null</code> if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<JournalArticleResource> orderByComparator) {

		List<JournalArticleResource> list = findByUuid_C(
			uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last journal article resource in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal article resource
	 * @throws NoSuchArticleResourceException if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<JournalArticleResource> orderByComparator)
		throws NoSuchArticleResourceException {

		JournalArticleResource journalArticleResource = fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);

		if (journalArticleResource != null) {
			return journalArticleResource;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchArticleResourceException(sb.toString());
	}

	/**
	 * Returns the last journal article resource in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal article resource, or <code>null</code> if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<JournalArticleResource> orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<JournalArticleResource> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the journal article resources before and after the current journal article resource in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param resourcePrimKey the primary key of the current journal article resource
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next journal article resource
	 * @throws NoSuchArticleResourceException if a journal article resource with the primary key could not be found
	 */
	@Override
	public JournalArticleResource[] findByUuid_C_PrevAndNext(
			long resourcePrimKey, String uuid, long companyId,
			OrderByComparator<JournalArticleResource> orderByComparator)
		throws NoSuchArticleResourceException {

		uuid = Objects.toString(uuid, "");

		JournalArticleResource journalArticleResource = findByPrimaryKey(
			resourcePrimKey);

		Session session = null;

		try {
			session = openSession();

			JournalArticleResource[] array = new JournalArticleResourceImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, journalArticleResource, uuid, companyId,
				orderByComparator, true);

			array[1] = journalArticleResource;

			array[2] = getByUuid_C_PrevAndNext(
				session, journalArticleResource, uuid, companyId,
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

	protected JournalArticleResource getByUuid_C_PrevAndNext(
		Session session, JournalArticleResource journalArticleResource,
		String uuid, long companyId,
		OrderByComparator<JournalArticleResource> orderByComparator,
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

		sb.append(_SQL_SELECT_JOURNALARTICLERESOURCE_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

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
			sb.append(JournalArticleResourceModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						journalArticleResource)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<JournalArticleResource> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the journal article resources where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (JournalArticleResource journalArticleResource :
				findByUuid_C(
					uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(journalArticleResource);
		}
	}

	/**
	 * Returns the number of journal article resources where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching journal article resources
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			JournalArticleResource.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid_C");

			finderArgs = new Object[] {uuid, companyId};

			count = (Long)finderCache.getResult(finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_JOURNALARTICLERESOURCE_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				if (productionMode) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_C_UUID_2 =
		"journalArticleResource.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(journalArticleResource.uuid IS NULL OR journalArticleResource.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"journalArticleResource.companyId = ?";

	/**
	 * Returns all the journal article resources where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching journal article resources
	 */
	@Override
	public List<JournalArticleResource> findByGroupId(long groupId) {
		return findByGroupId(
			groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the journal article resources where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>JournalArticleResourceModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of journal article resources
	 * @param end the upper bound of the range of journal article resources (not inclusive)
	 * @return the range of matching journal article resources
	 */
	@Override
	public List<JournalArticleResource> findByGroupId(
		long groupId, int start, int end) {

		return findByGroupId(groupId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the journal article resources where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>JournalArticleResourceModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of journal article resources
	 * @param end the upper bound of the range of journal article resources (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching journal article resources
	 */
	@Override
	public List<JournalArticleResource> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<JournalArticleResource> orderByComparator) {

		return findByGroupId(groupId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the journal article resources where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>JournalArticleResourceModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of journal article resources
	 * @param end the upper bound of the range of journal article resources (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching journal article resources
	 */
	@Override
	public List<JournalArticleResource> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<JournalArticleResource> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			JournalArticleResource.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupId");
				finderArgs = new Object[] {groupId};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroupId");
			finderArgs = new Object[] {groupId, start, end, orderByComparator};
		}

		List<JournalArticleResource> list = null;

		if (useFinderCache && productionMode) {
			list = (List<JournalArticleResource>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (JournalArticleResource journalArticleResource : list) {
					if (groupId != journalArticleResource.getGroupId()) {
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

			sb.append(_SQL_SELECT_JOURNALARTICLERESOURCE_WHERE);

			sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(JournalArticleResourceModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				list = (List<JournalArticleResource>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache && productionMode) {
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
	 * Returns the first journal article resource in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal article resource
	 * @throws NoSuchArticleResourceException if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource findByGroupId_First(
			long groupId,
			OrderByComparator<JournalArticleResource> orderByComparator)
		throws NoSuchArticleResourceException {

		JournalArticleResource journalArticleResource = fetchByGroupId_First(
			groupId, orderByComparator);

		if (journalArticleResource != null) {
			return journalArticleResource;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append("}");

		throw new NoSuchArticleResourceException(sb.toString());
	}

	/**
	 * Returns the first journal article resource in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching journal article resource, or <code>null</code> if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource fetchByGroupId_First(
		long groupId,
		OrderByComparator<JournalArticleResource> orderByComparator) {

		List<JournalArticleResource> list = findByGroupId(
			groupId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last journal article resource in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal article resource
	 * @throws NoSuchArticleResourceException if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource findByGroupId_Last(
			long groupId,
			OrderByComparator<JournalArticleResource> orderByComparator)
		throws NoSuchArticleResourceException {

		JournalArticleResource journalArticleResource = fetchByGroupId_Last(
			groupId, orderByComparator);

		if (journalArticleResource != null) {
			return journalArticleResource;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append("}");

		throw new NoSuchArticleResourceException(sb.toString());
	}

	/**
	 * Returns the last journal article resource in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching journal article resource, or <code>null</code> if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource fetchByGroupId_Last(
		long groupId,
		OrderByComparator<JournalArticleResource> orderByComparator) {

		int count = countByGroupId(groupId);

		if (count == 0) {
			return null;
		}

		List<JournalArticleResource> list = findByGroupId(
			groupId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the journal article resources before and after the current journal article resource in the ordered set where groupId = &#63;.
	 *
	 * @param resourcePrimKey the primary key of the current journal article resource
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next journal article resource
	 * @throws NoSuchArticleResourceException if a journal article resource with the primary key could not be found
	 */
	@Override
	public JournalArticleResource[] findByGroupId_PrevAndNext(
			long resourcePrimKey, long groupId,
			OrderByComparator<JournalArticleResource> orderByComparator)
		throws NoSuchArticleResourceException {

		JournalArticleResource journalArticleResource = findByPrimaryKey(
			resourcePrimKey);

		Session session = null;

		try {
			session = openSession();

			JournalArticleResource[] array = new JournalArticleResourceImpl[3];

			array[0] = getByGroupId_PrevAndNext(
				session, journalArticleResource, groupId, orderByComparator,
				true);

			array[1] = journalArticleResource;

			array[2] = getByGroupId_PrevAndNext(
				session, journalArticleResource, groupId, orderByComparator,
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

	protected JournalArticleResource getByGroupId_PrevAndNext(
		Session session, JournalArticleResource journalArticleResource,
		long groupId,
		OrderByComparator<JournalArticleResource> orderByComparator,
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

		sb.append(_SQL_SELECT_JOURNALARTICLERESOURCE_WHERE);

		sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

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
			sb.append(JournalArticleResourceModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						journalArticleResource)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<JournalArticleResource> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the journal article resources where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 */
	@Override
	public void removeByGroupId(long groupId) {
		for (JournalArticleResource journalArticleResource :
				findByGroupId(
					groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(journalArticleResource);
		}
	}

	/**
	 * Returns the number of journal article resources where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching journal article resources
	 */
	@Override
	public int countByGroupId(long groupId) {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			JournalArticleResource.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupId");

			finderArgs = new Object[] {groupId};

			count = (Long)finderCache.getResult(finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_JOURNALARTICLERESOURCE_WHERE);

			sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				if (productionMode) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_GROUPID_GROUPID_2 =
		"journalArticleResource.groupId = ?";

	/**
	 * Returns the journal article resource where groupId = &#63; and articleId = &#63; or throws a <code>NoSuchArticleResourceException</code> if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @return the matching journal article resource
	 * @throws NoSuchArticleResourceException if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource findByG_A(long groupId, String articleId)
		throws NoSuchArticleResourceException {

		JournalArticleResource journalArticleResource = fetchByG_A(
			groupId, articleId);

		if (journalArticleResource == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("groupId=");
			sb.append(groupId);

			sb.append(", articleId=");
			sb.append(articleId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchArticleResourceException(sb.toString());
		}

		return journalArticleResource;
	}

	/**
	 * Returns the journal article resource where groupId = &#63; and articleId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @return the matching journal article resource, or <code>null</code> if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource fetchByG_A(long groupId, String articleId) {
		return fetchByG_A(groupId, articleId, true);
	}

	/**
	 * Returns the journal article resource where groupId = &#63; and articleId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching journal article resource, or <code>null</code> if a matching journal article resource could not be found
	 */
	@Override
	public JournalArticleResource fetchByG_A(
		long groupId, String articleId, boolean useFinderCache) {

		articleId = Objects.toString(articleId, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			JournalArticleResource.class);

		Object[] finderArgs = null;

		if (useFinderCache && productionMode) {
			finderArgs = new Object[] {groupId, articleId};
		}

		Object result = null;

		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByG_A");

		if (useFinderCache && productionMode) {
			result = finderCache.getResult(finderPath, finderArgs, this);
		}

		if (result instanceof JournalArticleResource) {
			JournalArticleResource journalArticleResource =
				(JournalArticleResource)result;

			if ((groupId != journalArticleResource.getGroupId()) ||
				!Objects.equals(
					articleId, journalArticleResource.getArticleId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_JOURNALARTICLERESOURCE_WHERE);

			sb.append(_FINDER_COLUMN_G_A_GROUPID_2);

			boolean bindArticleId = false;

			if (articleId.isEmpty()) {
				sb.append(_FINDER_COLUMN_G_A_ARTICLEID_3);
			}
			else {
				bindArticleId = true;

				sb.append(_FINDER_COLUMN_G_A_ARTICLEID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindArticleId) {
					queryPos.add(articleId);
				}

				List<JournalArticleResource> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache && productionMode) {
						finderCache.putResult(finderPath, finderArgs, list);
					}
				}
				else {
					JournalArticleResource journalArticleResource = list.get(0);

					result = journalArticleResource;

					cacheResult(journalArticleResource);
				}
			}
			catch (Exception exception) {
				if (useFinderCache && productionMode) {
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
			return (JournalArticleResource)result;
		}
	}

	/**
	 * Removes the journal article resource where groupId = &#63; and articleId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @return the journal article resource that was removed
	 */
	@Override
	public JournalArticleResource removeByG_A(long groupId, String articleId)
		throws NoSuchArticleResourceException {

		JournalArticleResource journalArticleResource = findByG_A(
			groupId, articleId);

		return remove(journalArticleResource);
	}

	/**
	 * Returns the number of journal article resources where groupId = &#63; and articleId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param articleId the article ID
	 * @return the number of matching journal article resources
	 */
	@Override
	public int countByG_A(long groupId, String articleId) {
		articleId = Objects.toString(articleId, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			JournalArticleResource.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_A");

			finderArgs = new Object[] {groupId, articleId};

			count = (Long)finderCache.getResult(finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_JOURNALARTICLERESOURCE_WHERE);

			sb.append(_FINDER_COLUMN_G_A_GROUPID_2);

			boolean bindArticleId = false;

			if (articleId.isEmpty()) {
				sb.append(_FINDER_COLUMN_G_A_ARTICLEID_3);
			}
			else {
				bindArticleId = true;

				sb.append(_FINDER_COLUMN_G_A_ARTICLEID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindArticleId) {
					queryPos.add(articleId);
				}

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				if (productionMode) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_A_GROUPID_2 =
		"journalArticleResource.groupId = ? AND ";

	private static final String _FINDER_COLUMN_G_A_ARTICLEID_2 =
		"journalArticleResource.articleId = ?";

	private static final String _FINDER_COLUMN_G_A_ARTICLEID_3 =
		"(journalArticleResource.articleId IS NULL OR journalArticleResource.articleId = '')";

	public JournalArticleResourcePersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");

		setDBColumnNames(dbColumnNames);

		setModelClass(JournalArticleResource.class);

		setModelImplClass(JournalArticleResourceImpl.class);
		setModelPKClass(long.class);

		setTable(JournalArticleResourceTable.INSTANCE);
	}

	/**
	 * Caches the journal article resource in the entity cache if it is enabled.
	 *
	 * @param journalArticleResource the journal article resource
	 */
	@Override
	public void cacheResult(JournalArticleResource journalArticleResource) {
		if (journalArticleResource.getCtCollectionId() != 0) {
			journalArticleResource.resetOriginalValues();

			return;
		}

		entityCache.putResult(
			entityCacheEnabled, JournalArticleResourceImpl.class,
			journalArticleResource.getPrimaryKey(), journalArticleResource,
			new Object[] {
				_columnBitmaskEnabled,
				((JournalArticleResourceModelImpl)journalArticleResource).
					getColumnBitmask()
			});

		finderCache.putResult(
			_getFinderPath(FINDER_CLASS_NAME_ENTITY, "fetchByUUID_G"),
			new Object[] {
				journalArticleResource.getUuid(),
				journalArticleResource.getGroupId()
			},
			journalArticleResource);

		finderCache.putResult(
			_getFinderPath(FINDER_CLASS_NAME_ENTITY, "fetchByG_A"),
			new Object[] {
				journalArticleResource.getGroupId(),
				journalArticleResource.getArticleId()
			},
			journalArticleResource);

		journalArticleResource.resetOriginalValues();
	}

	/**
	 * Caches the journal article resources in the entity cache if it is enabled.
	 *
	 * @param journalArticleResources the journal article resources
	 */
	@Override
	public void cacheResult(
		List<JournalArticleResource> journalArticleResources) {

		for (JournalArticleResource journalArticleResource :
				journalArticleResources) {

			if (journalArticleResource.getCtCollectionId() != 0) {
				journalArticleResource.resetOriginalValues();

				continue;
			}

			if (entityCache.getResult(
					entityCacheEnabled, JournalArticleResourceImpl.class,
					journalArticleResource.getPrimaryKey()) == null) {

				cacheResult(journalArticleResource);
			}
			else {
				journalArticleResource.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all journal article resources.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(JournalArticleResourceImpl.class);
	}

	/**
	 * Clears the cache for the journal article resource.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(JournalArticleResource journalArticleResource) {
		entityCache.removeResult(
			entityCacheEnabled, JournalArticleResourceImpl.class,
			journalArticleResource.getPrimaryKey(), journalArticleResource,
			new Object[] {
				_columnBitmaskEnabled,
				((JournalArticleResourceModelImpl)journalArticleResource).
					getColumnBitmask()
			});
	}

	@Override
	public void clearCache(
		List<JournalArticleResource> journalArticleResources) {

		for (JournalArticleResource journalArticleResource :
				journalArticleResources) {

			entityCache.removeResult(
				entityCacheEnabled, JournalArticleResourceImpl.class,
				journalArticleResource.getPrimaryKey(), journalArticleResource,
				new Object[] {
					_columnBitmaskEnabled,
					((JournalArticleResourceModelImpl)journalArticleResource).
						getColumnBitmask()
				});
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				entityCacheEnabled, JournalArticleResourceImpl.class,
				primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		JournalArticleResourceModelImpl journalArticleResourceModelImpl) {

		Object[] args = new Object[] {
			journalArticleResourceModelImpl.getUuid(),
			journalArticleResourceModelImpl.getGroupId()
		};

		finderCache.putResult(
			_getFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUUID_G"),
			args, Long.valueOf(1), false);
		finderCache.putResult(
			_getFinderPath(FINDER_CLASS_NAME_ENTITY, "fetchByUUID_G"), args,
			journalArticleResourceModelImpl, false);

		args = new Object[] {
			journalArticleResourceModelImpl.getGroupId(),
			journalArticleResourceModelImpl.getArticleId()
		};

		finderCache.putResult(
			_getFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_A"),
			args, Long.valueOf(1), false);
		finderCache.putResult(
			_getFinderPath(FINDER_CLASS_NAME_ENTITY, "fetchByG_A"), args,
			journalArticleResourceModelImpl, false);
	}

	/**
	 * Creates a new journal article resource with the primary key. Does not add the journal article resource to the database.
	 *
	 * @param resourcePrimKey the primary key for the new journal article resource
	 * @return the new journal article resource
	 */
	@Override
	public JournalArticleResource create(long resourcePrimKey) {
		JournalArticleResource journalArticleResource =
			new JournalArticleResourceImpl();

		journalArticleResource.setNew(true);
		journalArticleResource.setPrimaryKey(resourcePrimKey);

		String uuid = PortalUUIDUtil.generate();

		journalArticleResource.setUuid(uuid);

		journalArticleResource.setCompanyId(CompanyThreadLocal.getCompanyId());

		return journalArticleResource;
	}

	/**
	 * Removes the journal article resource with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param resourcePrimKey the primary key of the journal article resource
	 * @return the journal article resource that was removed
	 * @throws NoSuchArticleResourceException if a journal article resource with the primary key could not be found
	 */
	@Override
	public JournalArticleResource remove(long resourcePrimKey)
		throws NoSuchArticleResourceException {

		return remove((Serializable)resourcePrimKey);
	}

	/**
	 * Removes the journal article resource with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the journal article resource
	 * @return the journal article resource that was removed
	 * @throws NoSuchArticleResourceException if a journal article resource with the primary key could not be found
	 */
	@Override
	public JournalArticleResource remove(Serializable primaryKey)
		throws NoSuchArticleResourceException {

		Session session = null;

		try {
			session = openSession();

			JournalArticleResource journalArticleResource =
				(JournalArticleResource)session.get(
					JournalArticleResourceImpl.class, primaryKey);

			if (journalArticleResource == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchArticleResourceException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(journalArticleResource);
		}
		catch (NoSuchArticleResourceException noSuchEntityException) {
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
	protected JournalArticleResource removeImpl(
		JournalArticleResource journalArticleResource) {

		if (!ctPersistenceHelper.isRemove(journalArticleResource)) {
			return journalArticleResource;
		}

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(journalArticleResource)) {
				journalArticleResource = (JournalArticleResource)session.get(
					JournalArticleResourceImpl.class,
					journalArticleResource.getPrimaryKeyObj());
			}

			if (journalArticleResource != null) {
				session.delete(journalArticleResource);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (journalArticleResource != null) {
			clearCache(journalArticleResource);
		}

		return journalArticleResource;
	}

	@Override
	public JournalArticleResource updateImpl(
		JournalArticleResource journalArticleResource) {

		boolean isNew = journalArticleResource.isNew();

		if (!(journalArticleResource instanceof
				JournalArticleResourceModelImpl)) {

			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(journalArticleResource.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					journalArticleResource);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in journalArticleResource proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom JournalArticleResource implementation " +
					journalArticleResource.getClass());
		}

		JournalArticleResourceModelImpl journalArticleResourceModelImpl =
			(JournalArticleResourceModelImpl)journalArticleResource;

		if (Validator.isNull(journalArticleResource.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			journalArticleResource.setUuid(uuid);
		}

		Session session = null;

		try {
			session = openSession();

			if (ctPersistenceHelper.isInsert(journalArticleResource)) {
				if (!isNew) {
					JournalArticleResource oldJournalArticleResource =
						(JournalArticleResource)session.get(
							JournalArticleResourceImpl.class,
							journalArticleResource.getPrimaryKeyObj());

					if (oldJournalArticleResource != null) {
						session.evict(oldJournalArticleResource);
					}
				}

				session.save(journalArticleResource);
			}
			else {
				journalArticleResource = (JournalArticleResource)session.merge(
					journalArticleResource);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (journalArticleResource.getCtCollectionId() != 0) {
			journalArticleResource.resetOriginalValues();

			if (isNew) {
				journalArticleResource.setNew(false);
			}

			return journalArticleResource;
		}

		entityCache.putResult(
			entityCacheEnabled, JournalArticleResourceImpl.class,
			journalArticleResourceModelImpl.getPrimaryKey(),
			journalArticleResourceModelImpl, false,
			new Object[] {
				_columnBitmaskEnabled,
				journalArticleResourceModelImpl.getColumnBitmask()
			});

		cacheUniqueFindersCache(journalArticleResourceModelImpl);

		journalArticleResource.resetOriginalValues();

		if (isNew) {
			journalArticleResource.setNew(false);
		}

		return journalArticleResource;
	}

	/**
	 * Returns the journal article resource with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the journal article resource
	 * @return the journal article resource
	 * @throws NoSuchArticleResourceException if a journal article resource with the primary key could not be found
	 */
	@Override
	public JournalArticleResource findByPrimaryKey(Serializable primaryKey)
		throws NoSuchArticleResourceException {

		JournalArticleResource journalArticleResource = fetchByPrimaryKey(
			primaryKey);

		if (journalArticleResource == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchArticleResourceException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return journalArticleResource;
	}

	/**
	 * Returns the journal article resource with the primary key or throws a <code>NoSuchArticleResourceException</code> if it could not be found.
	 *
	 * @param resourcePrimKey the primary key of the journal article resource
	 * @return the journal article resource
	 * @throws NoSuchArticleResourceException if a journal article resource with the primary key could not be found
	 */
	@Override
	public JournalArticleResource findByPrimaryKey(long resourcePrimKey)
		throws NoSuchArticleResourceException {

		return findByPrimaryKey((Serializable)resourcePrimKey);
	}

	/**
	 * Returns the journal article resource with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the journal article resource
	 * @return the journal article resource, or <code>null</code> if a journal article resource with the primary key could not be found
	 */
	@Override
	public JournalArticleResource fetchByPrimaryKey(Serializable primaryKey) {
		if (ctPersistenceHelper.isProductionMode(
				JournalArticleResource.class)) {

			return super.fetchByPrimaryKey(primaryKey);
		}

		JournalArticleResource journalArticleResource = null;

		Session session = null;

		try {
			session = openSession();

			journalArticleResource = (JournalArticleResource)session.get(
				JournalArticleResourceImpl.class, primaryKey);

			if (journalArticleResource != null) {
				cacheResult(journalArticleResource);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return journalArticleResource;
	}

	/**
	 * Returns the journal article resource with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param resourcePrimKey the primary key of the journal article resource
	 * @return the journal article resource, or <code>null</code> if a journal article resource with the primary key could not be found
	 */
	@Override
	public JournalArticleResource fetchByPrimaryKey(long resourcePrimKey) {
		return fetchByPrimaryKey((Serializable)resourcePrimKey);
	}

	@Override
	public Map<Serializable, JournalArticleResource> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		if (ctPersistenceHelper.isProductionMode(
				JournalArticleResource.class)) {

			return super.fetchByPrimaryKeys(primaryKeys);
		}

		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, JournalArticleResource> map =
			new HashMap<Serializable, JournalArticleResource>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			JournalArticleResource journalArticleResource = fetchByPrimaryKey(
				primaryKey);

			if (journalArticleResource != null) {
				map.put(primaryKey, journalArticleResource);
			}

			return map;
		}

		StringBundler sb = new StringBundler(primaryKeys.size() * 2 + 1);

		sb.append(getSelectSQL());
		sb.append(" WHERE ");
		sb.append(getPKDBName());
		sb.append(" IN (");

		for (Serializable primaryKey : primaryKeys) {
			sb.append((long)primaryKey);

			sb.append(",");
		}

		sb.setIndex(sb.index() - 1);

		sb.append(")");

		String sql = sb.toString();

		Session session = null;

		try {
			session = openSession();

			Query query = session.createQuery(sql);

			for (JournalArticleResource journalArticleResource :
					(List<JournalArticleResource>)query.list()) {

				map.put(
					journalArticleResource.getPrimaryKeyObj(),
					journalArticleResource);

				cacheResult(journalArticleResource);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the journal article resources.
	 *
	 * @return the journal article resources
	 */
	@Override
	public List<JournalArticleResource> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the journal article resources.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>JournalArticleResourceModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of journal article resources
	 * @param end the upper bound of the range of journal article resources (not inclusive)
	 * @return the range of journal article resources
	 */
	@Override
	public List<JournalArticleResource> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the journal article resources.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>JournalArticleResourceModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of journal article resources
	 * @param end the upper bound of the range of journal article resources (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of journal article resources
	 */
	@Override
	public List<JournalArticleResource> findAll(
		int start, int end,
		OrderByComparator<JournalArticleResource> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the journal article resources.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>JournalArticleResourceModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of journal article resources
	 * @param end the upper bound of the range of journal article resources (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of journal article resources
	 */
	@Override
	public List<JournalArticleResource> findAll(
		int start, int end,
		OrderByComparator<JournalArticleResource> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			JournalArticleResource.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll");
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll");
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<JournalArticleResource> list = null;

		if (useFinderCache && productionMode) {
			list = (List<JournalArticleResource>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_JOURNALARTICLERESOURCE);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_JOURNALARTICLERESOURCE;

				sql = sql.concat(JournalArticleResourceModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<JournalArticleResource>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache && productionMode) {
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
	 * Removes all the journal article resources from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (JournalArticleResource journalArticleResource : findAll()) {
			remove(journalArticleResource);
		}
	}

	/**
	 * Returns the number of journal article resources.
	 *
	 * @return the number of journal article resources
	 */
	@Override
	public int countAll() {
		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			JournalArticleResource.class);

		Long count = null;

		if (productionMode) {
			count = (Long)finderCache.getResult(
				finderPath, FINDER_ARGS_EMPTY, this);
		}

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(
					_SQL_COUNT_JOURNALARTICLERESOURCE);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, FINDER_ARGS_EMPTY, count);
				}
			}
			catch (Exception exception) {
				if (productionMode) {
					finderCache.removeResult(finderPath, FINDER_ARGS_EMPTY);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "resourcePrimKey";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_JOURNALARTICLERESOURCE;
	}

	@Override
	public Set<String> getCTColumnNames(
		CTColumnResolutionType ctColumnResolutionType) {

		return _ctColumnNamesMap.get(ctColumnResolutionType);
	}

	@Override
	public List<String> getMappingTableNames() {
		return _mappingTableNames;
	}

	@Override
	public Map<String, Integer> getTableColumnsMap() {
		return JournalArticleResourceModelImpl.TABLE_COLUMNS_MAP;
	}

	@Override
	public String getTableName() {
		return "JournalArticleResource";
	}

	@Override
	public List<String[]> getUniqueIndexColumnNames() {
		return _uniqueIndexColumnNames;
	}

	private static final Map<CTColumnResolutionType, Set<String>>
		_ctColumnNamesMap = new EnumMap<CTColumnResolutionType, Set<String>>(
			CTColumnResolutionType.class);
	private static final List<String> _mappingTableNames =
		new ArrayList<String>();
	private static final List<String[]> _uniqueIndexColumnNames =
		new ArrayList<String[]>();

	static {
		Set<String> ctControlColumnNames = new HashSet<String>();
		Set<String> ctIgnoreColumnNames = new HashSet<String>();
		Set<String> ctMergeColumnNames = new HashSet<String>();
		Set<String> ctStrictColumnNames = new HashSet<String>();

		ctControlColumnNames.add("mvccVersion");
		ctControlColumnNames.add("ctCollectionId");
		ctStrictColumnNames.add("uuid_");
		ctStrictColumnNames.add("groupId");
		ctStrictColumnNames.add("companyId");
		ctStrictColumnNames.add("articleId");

		_ctColumnNamesMap.put(
			CTColumnResolutionType.CONTROL, ctControlColumnNames);
		_ctColumnNamesMap.put(
			CTColumnResolutionType.IGNORE, ctIgnoreColumnNames);
		_ctColumnNamesMap.put(CTColumnResolutionType.MERGE, ctMergeColumnNames);
		_ctColumnNamesMap.put(
			CTColumnResolutionType.PK,
			Collections.singleton("resourcePrimKey"));
		_ctColumnNamesMap.put(
			CTColumnResolutionType.STRICT, ctStrictColumnNames);

		_uniqueIndexColumnNames.add(new String[] {"uuid_", "groupId"});

		_uniqueIndexColumnNames.add(new String[] {"groupId", "articleId"});
	}

	/**
	 * Initializes the journal article resource persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		JournalArticleResourceModelImpl.setEntityCacheEnabled(
			entityCacheEnabled);
		JournalArticleResourceModelImpl.setFinderCacheEnabled(
			finderCacheEnabled);

		_bundleContext = bundleContext;
		Bundle bundle = FrameworkUtil.getBundle(
			JournalArticleResourcePersistenceImpl.class);

		_bundleContext = bundle.getBundleContext();
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(JournalArticleResourceImpl.class.getName());

		for (ServiceRegistration<FinderPath> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	@Override
	@Reference(
		target = JournalPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.journal.model.JournalArticleResource"),
			true);
	}

	@Override
	@Reference(
		target = JournalPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = JournalPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private boolean _columnBitmaskEnabled;
	private BundleContext _bundleContext;

	@Reference
	protected CTPersistenceHelper ctPersistenceHelper;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_JOURNALARTICLERESOURCE =
		"SELECT journalArticleResource FROM JournalArticleResource journalArticleResource";

	private static final String _SQL_SELECT_JOURNALARTICLERESOURCE_WHERE =
		"SELECT journalArticleResource FROM JournalArticleResource journalArticleResource WHERE ";

	private static final String _SQL_COUNT_JOURNALARTICLERESOURCE =
		"SELECT COUNT(journalArticleResource) FROM JournalArticleResource journalArticleResource";

	private static final String _SQL_COUNT_JOURNALARTICLERESOURCE_WHERE =
		"SELECT COUNT(journalArticleResource) FROM JournalArticleResource journalArticleResource WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS =
		"journalArticleResource.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No JournalArticleResource exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No JournalArticleResource exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		JournalArticleResourcePersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid"});

	static {
		try {
			Class.forName(JournalPersistenceConstants.class.getName());
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
				Class<?> returnClass = JournalArticleResourceImpl.class;

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
			"findByUuid",
			new Object[] {
				JournalArticleResourceModelImpl.UUID_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					JournalArticleResourceModelImpl
						journalArticleResourceModelImpl =
							(JournalArticleResourceModelImpl)baseModel;

					return new Object[] {
						journalArticleResourceModelImpl.getUuid()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					JournalArticleResourceModelImpl
						journalArticleResourceModelImpl =
							(JournalArticleResourceModelImpl)baseModel;

					return new Object[] {
						journalArticleResourceModelImpl.getOriginalUuid()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"fetchByUUID_G",
			new Object[] {
				JournalArticleResourceModelImpl.UUID_COLUMN_BITMASK |
				JournalArticleResourceModelImpl.GROUPID_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					JournalArticleResourceModelImpl
						journalArticleResourceModelImpl =
							(JournalArticleResourceModelImpl)baseModel;

					return new Object[] {
						journalArticleResourceModelImpl.getUuid(),
						journalArticleResourceModelImpl.getGroupId()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					JournalArticleResourceModelImpl
						journalArticleResourceModelImpl =
							(JournalArticleResourceModelImpl)baseModel;

					return new Object[] {
						journalArticleResourceModelImpl.getOriginalUuid(),
						journalArticleResourceModelImpl.getOriginalGroupId()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"findByUuid_C",
			new Object[] {
				JournalArticleResourceModelImpl.UUID_COLUMN_BITMASK |
				JournalArticleResourceModelImpl.COMPANYID_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					JournalArticleResourceModelImpl
						journalArticleResourceModelImpl =
							(JournalArticleResourceModelImpl)baseModel;

					return new Object[] {
						journalArticleResourceModelImpl.getUuid(),
						journalArticleResourceModelImpl.getCompanyId()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					JournalArticleResourceModelImpl
						journalArticleResourceModelImpl =
							(JournalArticleResourceModelImpl)baseModel;

					return new Object[] {
						journalArticleResourceModelImpl.getOriginalUuid(),
						journalArticleResourceModelImpl.getOriginalCompanyId()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"findByGroupId",
			new Object[] {
				JournalArticleResourceModelImpl.GROUPID_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					JournalArticleResourceModelImpl
						journalArticleResourceModelImpl =
							(JournalArticleResourceModelImpl)baseModel;

					return new Object[] {
						journalArticleResourceModelImpl.getGroupId()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					JournalArticleResourceModelImpl
						journalArticleResourceModelImpl =
							(JournalArticleResourceModelImpl)baseModel;

					return new Object[] {
						journalArticleResourceModelImpl.getOriginalGroupId()
					};
				}
			});

		_COLUMN_BITMASK_ARRAY_MAP.put(
			"fetchByG_A",
			new Object[] {
				JournalArticleResourceModelImpl.GROUPID_COLUMN_BITMASK |
				JournalArticleResourceModelImpl.ARTICLEID_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					JournalArticleResourceModelImpl
						journalArticleResourceModelImpl =
							(JournalArticleResourceModelImpl)baseModel;

					return new Object[] {
						journalArticleResourceModelImpl.getGroupId(),
						journalArticleResourceModelImpl.getArticleId()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					JournalArticleResourceModelImpl
						journalArticleResourceModelImpl =
							(JournalArticleResourceModelImpl)baseModel;

					return new Object[] {
						journalArticleResourceModelImpl.getOriginalGroupId(),
						journalArticleResourceModelImpl.getOriginalArticleId()
					};
				}
			});
	}

}
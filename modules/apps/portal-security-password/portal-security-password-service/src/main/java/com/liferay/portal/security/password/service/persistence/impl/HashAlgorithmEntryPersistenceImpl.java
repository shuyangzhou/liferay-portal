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

package com.liferay.portal.security.password.service.persistence.impl;

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
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.security.password.exception.NoSuchHashAlgorithmEntryException;
import com.liferay.portal.security.password.model.HashAlgorithmEntry;
import com.liferay.portal.security.password.model.impl.HashAlgorithmEntryImpl;
import com.liferay.portal.security.password.model.impl.HashAlgorithmEntryModelImpl;
import com.liferay.portal.security.password.service.persistence.HashAlgorithmEntryPersistence;
import com.liferay.portal.security.password.service.persistence.impl.constants.PasswordPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the hash algorithm entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author arthurchan35
 * @generated
 */
@Component(service = HashAlgorithmEntryPersistence.class)
public class HashAlgorithmEntryPersistenceImpl
	extends BasePersistenceImpl<HashAlgorithmEntry>
	implements HashAlgorithmEntryPersistence {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>HashAlgorithmEntryUtil</code> to access the hash algorithm entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		HashAlgorithmEntryImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByUuid;
	private FinderPath _finderPathWithoutPaginationFindByUuid;
	private FinderPath _finderPathCountByUuid;

	/**
	 * Returns all the hash algorithm entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching hash algorithm entries
	 */
	@Override
	public List<HashAlgorithmEntry> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the hash algorithm entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HashAlgorithmEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of hash algorithm entries
	 * @param end the upper bound of the range of hash algorithm entries (not inclusive)
	 * @return the range of matching hash algorithm entries
	 */
	@Override
	public List<HashAlgorithmEntry> findByUuid(
		String uuid, int start, int end) {

		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the hash algorithm entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HashAlgorithmEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of hash algorithm entries
	 * @param end the upper bound of the range of hash algorithm entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching hash algorithm entries
	 */
	@Override
	public List<HashAlgorithmEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<HashAlgorithmEntry> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the hash algorithm entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HashAlgorithmEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of hash algorithm entries
	 * @param end the upper bound of the range of hash algorithm entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching hash algorithm entries
	 */
	@Override
	public List<HashAlgorithmEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<HashAlgorithmEntry> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid;
				finderArgs = new Object[] {uuid};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid;
			finderArgs = new Object[] {uuid, start, end, orderByComparator};
		}

		List<HashAlgorithmEntry> list = null;

		if (useFinderCache) {
			list = (List<HashAlgorithmEntry>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (HashAlgorithmEntry hashAlgorithmEntry : list) {
					if (!uuid.equals(hashAlgorithmEntry.getUuid())) {
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
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_HASHALGORITHMENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				query.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				query.append(HashAlgorithmEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				list = (List<HashAlgorithmEntry>)QueryUtil.list(
					q, getDialect(), start, end);

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
	 * Returns the first hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching hash algorithm entry
	 * @throws NoSuchHashAlgorithmEntryException if a matching hash algorithm entry could not be found
	 */
	@Override
	public HashAlgorithmEntry findByUuid_First(
			String uuid,
			OrderByComparator<HashAlgorithmEntry> orderByComparator)
		throws NoSuchHashAlgorithmEntryException {

		HashAlgorithmEntry hashAlgorithmEntry = fetchByUuid_First(
			uuid, orderByComparator);

		if (hashAlgorithmEntry != null) {
			return hashAlgorithmEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append("}");

		throw new NoSuchHashAlgorithmEntryException(msg.toString());
	}

	/**
	 * Returns the first hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching hash algorithm entry, or <code>null</code> if a matching hash algorithm entry could not be found
	 */
	@Override
	public HashAlgorithmEntry fetchByUuid_First(
		String uuid, OrderByComparator<HashAlgorithmEntry> orderByComparator) {

		List<HashAlgorithmEntry> list = findByUuid(
			uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching hash algorithm entry
	 * @throws NoSuchHashAlgorithmEntryException if a matching hash algorithm entry could not be found
	 */
	@Override
	public HashAlgorithmEntry findByUuid_Last(
			String uuid,
			OrderByComparator<HashAlgorithmEntry> orderByComparator)
		throws NoSuchHashAlgorithmEntryException {

		HashAlgorithmEntry hashAlgorithmEntry = fetchByUuid_Last(
			uuid, orderByComparator);

		if (hashAlgorithmEntry != null) {
			return hashAlgorithmEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append("}");

		throw new NoSuchHashAlgorithmEntryException(msg.toString());
	}

	/**
	 * Returns the last hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching hash algorithm entry, or <code>null</code> if a matching hash algorithm entry could not be found
	 */
	@Override
	public HashAlgorithmEntry fetchByUuid_Last(
		String uuid, OrderByComparator<HashAlgorithmEntry> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<HashAlgorithmEntry> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the hash algorithm entries before and after the current hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param entryId the primary key of the current hash algorithm entry
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next hash algorithm entry
	 * @throws NoSuchHashAlgorithmEntryException if a hash algorithm entry with the primary key could not be found
	 */
	@Override
	public HashAlgorithmEntry[] findByUuid_PrevAndNext(
			long entryId, String uuid,
			OrderByComparator<HashAlgorithmEntry> orderByComparator)
		throws NoSuchHashAlgorithmEntryException {

		uuid = Objects.toString(uuid, "");

		HashAlgorithmEntry hashAlgorithmEntry = findByPrimaryKey(entryId);

		Session session = null;

		try {
			session = openSession();

			HashAlgorithmEntry[] array = new HashAlgorithmEntryImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, hashAlgorithmEntry, uuid, orderByComparator, true);

			array[1] = hashAlgorithmEntry;

			array[2] = getByUuid_PrevAndNext(
				session, hashAlgorithmEntry, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected HashAlgorithmEntry getByUuid_PrevAndNext(
		Session session, HashAlgorithmEntry hashAlgorithmEntry, String uuid,
		OrderByComparator<HashAlgorithmEntry> orderByComparator,
		boolean previous) {

		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_HASHALGORITHMENTRY_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			query.append(_FINDER_COLUMN_UUID_UUID_3);
		}
		else {
			bindUuid = true;

			query.append(_FINDER_COLUMN_UUID_UUID_2);
		}

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
			query.append(HashAlgorithmEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindUuid) {
			qPos.add(uuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						hashAlgorithmEntry)) {

				qPos.add(orderByConditionValue);
			}
		}

		List<HashAlgorithmEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the hash algorithm entries where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (HashAlgorithmEntry hashAlgorithmEntry :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(hashAlgorithmEntry);
		}
	}

	/**
	 * Returns the number of hash algorithm entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching hash algorithm entries
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid;

		Object[] finderArgs = new Object[] {uuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_HASHALGORITHMENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				query.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
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

	private static final String _FINDER_COLUMN_UUID_UUID_2 =
		"hashAlgorithmEntry.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(hashAlgorithmEntry.uuid IS NULL OR hashAlgorithmEntry.uuid = '')";

	public HashAlgorithmEntryPersistenceImpl() {
		setModelClass(HashAlgorithmEntry.class);

		setModelImplClass(HashAlgorithmEntryImpl.class);
		setModelPKClass(long.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");

		setDBColumnNames(dbColumnNames);
	}

	/**
	 * Caches the hash algorithm entry in the entity cache if it is enabled.
	 *
	 * @param hashAlgorithmEntry the hash algorithm entry
	 */
	@Override
	public void cacheResult(HashAlgorithmEntry hashAlgorithmEntry) {
		entityCache.putResult(
			entityCacheEnabled, HashAlgorithmEntryImpl.class,
			hashAlgorithmEntry.getPrimaryKey(), hashAlgorithmEntry);

		hashAlgorithmEntry.resetOriginalValues();
	}

	/**
	 * Caches the hash algorithm entries in the entity cache if it is enabled.
	 *
	 * @param hashAlgorithmEntries the hash algorithm entries
	 */
	@Override
	public void cacheResult(List<HashAlgorithmEntry> hashAlgorithmEntries) {
		for (HashAlgorithmEntry hashAlgorithmEntry : hashAlgorithmEntries) {
			if (entityCache.getResult(
					entityCacheEnabled, HashAlgorithmEntryImpl.class,
					hashAlgorithmEntry.getPrimaryKey()) == null) {

				cacheResult(hashAlgorithmEntry);
			}
			else {
				hashAlgorithmEntry.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all hash algorithm entries.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(HashAlgorithmEntryImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the hash algorithm entry.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(HashAlgorithmEntry hashAlgorithmEntry) {
		entityCache.removeResult(
			entityCacheEnabled, HashAlgorithmEntryImpl.class,
			hashAlgorithmEntry.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<HashAlgorithmEntry> hashAlgorithmEntries) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (HashAlgorithmEntry hashAlgorithmEntry : hashAlgorithmEntries) {
			entityCache.removeResult(
				entityCacheEnabled, HashAlgorithmEntryImpl.class,
				hashAlgorithmEntry.getPrimaryKey());
		}
	}

	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				entityCacheEnabled, HashAlgorithmEntryImpl.class, primaryKey);
		}
	}

	/**
	 * Creates a new hash algorithm entry with the primary key. Does not add the hash algorithm entry to the database.
	 *
	 * @param entryId the primary key for the new hash algorithm entry
	 * @return the new hash algorithm entry
	 */
	@Override
	public HashAlgorithmEntry create(long entryId) {
		HashAlgorithmEntry hashAlgorithmEntry = new HashAlgorithmEntryImpl();

		hashAlgorithmEntry.setNew(true);
		hashAlgorithmEntry.setPrimaryKey(entryId);

		String uuid = PortalUUIDUtil.generate();

		hashAlgorithmEntry.setUuid(uuid);

		return hashAlgorithmEntry;
	}

	/**
	 * Removes the hash algorithm entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the hash algorithm entry
	 * @return the hash algorithm entry that was removed
	 * @throws NoSuchHashAlgorithmEntryException if a hash algorithm entry with the primary key could not be found
	 */
	@Override
	public HashAlgorithmEntry remove(long entryId)
		throws NoSuchHashAlgorithmEntryException {

		return remove((Serializable)entryId);
	}

	/**
	 * Removes the hash algorithm entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the hash algorithm entry
	 * @return the hash algorithm entry that was removed
	 * @throws NoSuchHashAlgorithmEntryException if a hash algorithm entry with the primary key could not be found
	 */
	@Override
	public HashAlgorithmEntry remove(Serializable primaryKey)
		throws NoSuchHashAlgorithmEntryException {

		Session session = null;

		try {
			session = openSession();

			HashAlgorithmEntry hashAlgorithmEntry =
				(HashAlgorithmEntry)session.get(
					HashAlgorithmEntryImpl.class, primaryKey);

			if (hashAlgorithmEntry == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchHashAlgorithmEntryException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(hashAlgorithmEntry);
		}
		catch (NoSuchHashAlgorithmEntryException nsee) {
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
	protected HashAlgorithmEntry removeImpl(
		HashAlgorithmEntry hashAlgorithmEntry) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(hashAlgorithmEntry)) {
				hashAlgorithmEntry = (HashAlgorithmEntry)session.get(
					HashAlgorithmEntryImpl.class,
					hashAlgorithmEntry.getPrimaryKeyObj());
			}

			if (hashAlgorithmEntry != null) {
				session.delete(hashAlgorithmEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (hashAlgorithmEntry != null) {
			clearCache(hashAlgorithmEntry);
		}

		return hashAlgorithmEntry;
	}

	@Override
	public HashAlgorithmEntry updateImpl(
		HashAlgorithmEntry hashAlgorithmEntry) {

		boolean isNew = hashAlgorithmEntry.isNew();

		if (!(hashAlgorithmEntry instanceof HashAlgorithmEntryModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(hashAlgorithmEntry.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					hashAlgorithmEntry);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in hashAlgorithmEntry proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom HashAlgorithmEntry implementation " +
					hashAlgorithmEntry.getClass());
		}

		HashAlgorithmEntryModelImpl hashAlgorithmEntryModelImpl =
			(HashAlgorithmEntryModelImpl)hashAlgorithmEntry;

		if (Validator.isNull(hashAlgorithmEntry.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			hashAlgorithmEntry.setUuid(uuid);
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (hashAlgorithmEntry.getCreateDate() == null)) {
			if (serviceContext == null) {
				hashAlgorithmEntry.setCreateDate(now);
			}
			else {
				hashAlgorithmEntry.setCreateDate(
					serviceContext.getCreateDate(now));
			}
		}

		if (!hashAlgorithmEntryModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				hashAlgorithmEntry.setModifiedDate(now);
			}
			else {
				hashAlgorithmEntry.setModifiedDate(
					serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (hashAlgorithmEntry.isNew()) {
				session.save(hashAlgorithmEntry);

				hashAlgorithmEntry.setNew(false);
			}
			else {
				hashAlgorithmEntry = (HashAlgorithmEntry)session.merge(
					hashAlgorithmEntry);
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
				hashAlgorithmEntryModelImpl.getUuid()
			};

			finderCache.removeResult(_finderPathCountByUuid, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByUuid, args);

			finderCache.removeResult(_finderPathCountAll, FINDER_ARGS_EMPTY);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindAll, FINDER_ARGS_EMPTY);
		}
		else {
			if ((hashAlgorithmEntryModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByUuid.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					hashAlgorithmEntryModelImpl.getOriginalUuid()
				};

				finderCache.removeResult(_finderPathCountByUuid, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid, args);

				args = new Object[] {hashAlgorithmEntryModelImpl.getUuid()};

				finderCache.removeResult(_finderPathCountByUuid, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid, args);
			}
		}

		entityCache.putResult(
			entityCacheEnabled, HashAlgorithmEntryImpl.class,
			hashAlgorithmEntry.getPrimaryKey(), hashAlgorithmEntry, false);

		hashAlgorithmEntry.resetOriginalValues();

		return hashAlgorithmEntry;
	}

	/**
	 * Returns the hash algorithm entry with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the hash algorithm entry
	 * @return the hash algorithm entry
	 * @throws NoSuchHashAlgorithmEntryException if a hash algorithm entry with the primary key could not be found
	 */
	@Override
	public HashAlgorithmEntry findByPrimaryKey(Serializable primaryKey)
		throws NoSuchHashAlgorithmEntryException {

		HashAlgorithmEntry hashAlgorithmEntry = fetchByPrimaryKey(primaryKey);

		if (hashAlgorithmEntry == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchHashAlgorithmEntryException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return hashAlgorithmEntry;
	}

	/**
	 * Returns the hash algorithm entry with the primary key or throws a <code>NoSuchHashAlgorithmEntryException</code> if it could not be found.
	 *
	 * @param entryId the primary key of the hash algorithm entry
	 * @return the hash algorithm entry
	 * @throws NoSuchHashAlgorithmEntryException if a hash algorithm entry with the primary key could not be found
	 */
	@Override
	public HashAlgorithmEntry findByPrimaryKey(long entryId)
		throws NoSuchHashAlgorithmEntryException {

		return findByPrimaryKey((Serializable)entryId);
	}

	/**
	 * Returns the hash algorithm entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param entryId the primary key of the hash algorithm entry
	 * @return the hash algorithm entry, or <code>null</code> if a hash algorithm entry with the primary key could not be found
	 */
	@Override
	public HashAlgorithmEntry fetchByPrimaryKey(long entryId) {
		return fetchByPrimaryKey((Serializable)entryId);
	}

	/**
	 * Returns all the hash algorithm entries.
	 *
	 * @return the hash algorithm entries
	 */
	@Override
	public List<HashAlgorithmEntry> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the hash algorithm entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HashAlgorithmEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of hash algorithm entries
	 * @param end the upper bound of the range of hash algorithm entries (not inclusive)
	 * @return the range of hash algorithm entries
	 */
	@Override
	public List<HashAlgorithmEntry> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the hash algorithm entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HashAlgorithmEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of hash algorithm entries
	 * @param end the upper bound of the range of hash algorithm entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of hash algorithm entries
	 */
	@Override
	public List<HashAlgorithmEntry> findAll(
		int start, int end,
		OrderByComparator<HashAlgorithmEntry> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the hash algorithm entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HashAlgorithmEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of hash algorithm entries
	 * @param end the upper bound of the range of hash algorithm entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of hash algorithm entries
	 */
	@Override
	public List<HashAlgorithmEntry> findAll(
		int start, int end,
		OrderByComparator<HashAlgorithmEntry> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<HashAlgorithmEntry> list = null;

		if (useFinderCache) {
			list = (List<HashAlgorithmEntry>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_HASHALGORITHMENTRY);

				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_HASHALGORITHMENTRY;

				sql = sql.concat(HashAlgorithmEntryModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				list = (List<HashAlgorithmEntry>)QueryUtil.list(
					q, getDialect(), start, end);

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
	 * Removes all the hash algorithm entries from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (HashAlgorithmEntry hashAlgorithmEntry : findAll()) {
			remove(hashAlgorithmEntry);
		}
	}

	/**
	 * Returns the number of hash algorithm entries.
	 *
	 * @return the number of hash algorithm entries
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_HASHALGORITHMENTRY);

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
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "entryId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_HASHALGORITHMENTRY;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return HashAlgorithmEntryModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the hash algorithm entry persistence.
	 */
	@Activate
	public void activate() {
		HashAlgorithmEntryModelImpl.setEntityCacheEnabled(entityCacheEnabled);
		HashAlgorithmEntryModelImpl.setFinderCacheEnabled(finderCacheEnabled);

		_finderPathWithPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			HashAlgorithmEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			HashAlgorithmEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll",
			new String[0]);

		_finderPathCountAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);

		_finderPathWithPaginationFindByUuid = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			HashAlgorithmEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByUuid = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			HashAlgorithmEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] {String.class.getName()},
			HashAlgorithmEntryModelImpl.UUID_COLUMN_BITMASK);

		_finderPathCountByUuid = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] {String.class.getName()});
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(HashAlgorithmEntryImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	@Reference(
		target = PasswordPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.security.password.model.HashAlgorithmEntry"),
			true);
	}

	@Override
	@Reference(
		target = PasswordPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = PasswordPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
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

	private static final String _SQL_SELECT_HASHALGORITHMENTRY =
		"SELECT hashAlgorithmEntry FROM HashAlgorithmEntry hashAlgorithmEntry";

	private static final String _SQL_SELECT_HASHALGORITHMENTRY_WHERE =
		"SELECT hashAlgorithmEntry FROM HashAlgorithmEntry hashAlgorithmEntry WHERE ";

	private static final String _SQL_COUNT_HASHALGORITHMENTRY =
		"SELECT COUNT(hashAlgorithmEntry) FROM HashAlgorithmEntry hashAlgorithmEntry";

	private static final String _SQL_COUNT_HASHALGORITHMENTRY_WHERE =
		"SELECT COUNT(hashAlgorithmEntry) FROM HashAlgorithmEntry hashAlgorithmEntry WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "hashAlgorithmEntry.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No HashAlgorithmEntry exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No HashAlgorithmEntry exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		HashAlgorithmEntryPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid"});

	static {
		try {
			Class.forName(PasswordPersistenceConstants.class.getName());
		}
		catch (ClassNotFoundException cnfe) {
			throw new ExceptionInInitializerError(cnfe);
		}
	}

}
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

package com.liferay.portal.security.password.service.persistence;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.security.password.model.HashAlgorithmEntry;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the hash algorithm entry service. This utility wraps <code>com.liferay.portal.security.password.service.persistence.impl.HashAlgorithmEntryPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author arthurchan35
 * @see HashAlgorithmEntryPersistence
 * @generated
 */
public class HashAlgorithmEntryUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(HashAlgorithmEntry hashAlgorithmEntry) {
		getPersistence().clearCache(hashAlgorithmEntry);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, HashAlgorithmEntry> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<HashAlgorithmEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<HashAlgorithmEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<HashAlgorithmEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<HashAlgorithmEntry> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static HashAlgorithmEntry update(
		HashAlgorithmEntry hashAlgorithmEntry) {

		return getPersistence().update(hashAlgorithmEntry);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static HashAlgorithmEntry update(
		HashAlgorithmEntry hashAlgorithmEntry, ServiceContext serviceContext) {

		return getPersistence().update(hashAlgorithmEntry, serviceContext);
	}

	/**
	 * Returns all the hash algorithm entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching hash algorithm entries
	 */
	public static List<HashAlgorithmEntry> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
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
	public static List<HashAlgorithmEntry> findByUuid(
		String uuid, int start, int end) {

		return getPersistence().findByUuid(uuid, start, end);
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
	public static List<HashAlgorithmEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<HashAlgorithmEntry> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
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
	public static List<HashAlgorithmEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<HashAlgorithmEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching hash algorithm entry
	 * @throws NoSuchHashAlgorithmEntryException if a matching hash algorithm entry could not be found
	 */
	public static HashAlgorithmEntry findByUuid_First(
			String uuid,
			OrderByComparator<HashAlgorithmEntry> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashAlgorithmEntryException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching hash algorithm entry, or <code>null</code> if a matching hash algorithm entry could not be found
	 */
	public static HashAlgorithmEntry fetchByUuid_First(
		String uuid, OrderByComparator<HashAlgorithmEntry> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching hash algorithm entry
	 * @throws NoSuchHashAlgorithmEntryException if a matching hash algorithm entry could not be found
	 */
	public static HashAlgorithmEntry findByUuid_Last(
			String uuid,
			OrderByComparator<HashAlgorithmEntry> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashAlgorithmEntryException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching hash algorithm entry, or <code>null</code> if a matching hash algorithm entry could not be found
	 */
	public static HashAlgorithmEntry fetchByUuid_Last(
		String uuid, OrderByComparator<HashAlgorithmEntry> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
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
	public static HashAlgorithmEntry[] findByUuid_PrevAndNext(
			long entryId, String uuid,
			OrderByComparator<HashAlgorithmEntry> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashAlgorithmEntryException {

		return getPersistence().findByUuid_PrevAndNext(
			entryId, uuid, orderByComparator);
	}

	/**
	 * Removes all the hash algorithm entries where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of hash algorithm entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching hash algorithm entries
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Caches the hash algorithm entry in the entity cache if it is enabled.
	 *
	 * @param hashAlgorithmEntry the hash algorithm entry
	 */
	public static void cacheResult(HashAlgorithmEntry hashAlgorithmEntry) {
		getPersistence().cacheResult(hashAlgorithmEntry);
	}

	/**
	 * Caches the hash algorithm entries in the entity cache if it is enabled.
	 *
	 * @param hashAlgorithmEntries the hash algorithm entries
	 */
	public static void cacheResult(
		List<HashAlgorithmEntry> hashAlgorithmEntries) {

		getPersistence().cacheResult(hashAlgorithmEntries);
	}

	/**
	 * Creates a new hash algorithm entry with the primary key. Does not add the hash algorithm entry to the database.
	 *
	 * @param entryId the primary key for the new hash algorithm entry
	 * @return the new hash algorithm entry
	 */
	public static HashAlgorithmEntry create(long entryId) {
		return getPersistence().create(entryId);
	}

	/**
	 * Removes the hash algorithm entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the hash algorithm entry
	 * @return the hash algorithm entry that was removed
	 * @throws NoSuchHashAlgorithmEntryException if a hash algorithm entry with the primary key could not be found
	 */
	public static HashAlgorithmEntry remove(long entryId)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashAlgorithmEntryException {

		return getPersistence().remove(entryId);
	}

	public static HashAlgorithmEntry updateImpl(
		HashAlgorithmEntry hashAlgorithmEntry) {

		return getPersistence().updateImpl(hashAlgorithmEntry);
	}

	/**
	 * Returns the hash algorithm entry with the primary key or throws a <code>NoSuchHashAlgorithmEntryException</code> if it could not be found.
	 *
	 * @param entryId the primary key of the hash algorithm entry
	 * @return the hash algorithm entry
	 * @throws NoSuchHashAlgorithmEntryException if a hash algorithm entry with the primary key could not be found
	 */
	public static HashAlgorithmEntry findByPrimaryKey(long entryId)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashAlgorithmEntryException {

		return getPersistence().findByPrimaryKey(entryId);
	}

	/**
	 * Returns the hash algorithm entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param entryId the primary key of the hash algorithm entry
	 * @return the hash algorithm entry, or <code>null</code> if a hash algorithm entry with the primary key could not be found
	 */
	public static HashAlgorithmEntry fetchByPrimaryKey(long entryId) {
		return getPersistence().fetchByPrimaryKey(entryId);
	}

	/**
	 * Returns all the hash algorithm entries.
	 *
	 * @return the hash algorithm entries
	 */
	public static List<HashAlgorithmEntry> findAll() {
		return getPersistence().findAll();
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
	public static List<HashAlgorithmEntry> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
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
	public static List<HashAlgorithmEntry> findAll(
		int start, int end,
		OrderByComparator<HashAlgorithmEntry> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
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
	public static List<HashAlgorithmEntry> findAll(
		int start, int end,
		OrderByComparator<HashAlgorithmEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the hash algorithm entries from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of hash algorithm entries.
	 *
	 * @return the number of hash algorithm entries
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static HashAlgorithmEntryPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<HashAlgorithmEntryPersistence, HashAlgorithmEntryPersistence>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			HashAlgorithmEntryPersistence.class);

		ServiceTracker
			<HashAlgorithmEntryPersistence, HashAlgorithmEntryPersistence>
				serviceTracker =
					new ServiceTracker
						<HashAlgorithmEntryPersistence,
						 HashAlgorithmEntryPersistence>(
							 bundle.getBundleContext(),
							 HashAlgorithmEntryPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}
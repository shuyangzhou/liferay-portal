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
import com.liferay.portal.security.password.model.PasswordEntry;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the password entry service. This utility wraps <code>com.liferay.portal.security.password.service.persistence.impl.PasswordEntryPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author arthurchan35
 * @see PasswordEntryPersistence
 * @generated
 */
public class PasswordEntryUtil {

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
	public static void clearCache(PasswordEntry passwordEntry) {
		getPersistence().clearCache(passwordEntry);
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
	public static Map<Serializable, PasswordEntry> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<PasswordEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<PasswordEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<PasswordEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<PasswordEntry> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static PasswordEntry update(PasswordEntry passwordEntry) {
		return getPersistence().update(passwordEntry);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static PasswordEntry update(
		PasswordEntry passwordEntry, ServiceContext serviceContext) {

		return getPersistence().update(passwordEntry, serviceContext);
	}

	/**
	 * Returns all the password entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching password entries
	 */
	public static List<PasswordEntry> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
	}

	/**
	 * Returns a range of all the password entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of password entries
	 * @param end the upper bound of the range of password entries (not inclusive)
	 * @return the range of matching password entries
	 */
	public static List<PasswordEntry> findByUuid(
		String uuid, int start, int end) {

		return getPersistence().findByUuid(uuid, start, end);
	}

	/**
	 * Returns an ordered range of all the password entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of password entries
	 * @param end the upper bound of the range of password entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching password entries
	 */
	public static List<PasswordEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<PasswordEntry> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the password entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of password entries
	 * @param end the upper bound of the range of password entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching password entries
	 */
	public static List<PasswordEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<PasswordEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first password entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password entry
	 * @throws NoSuchEntryException if a matching password entry could not be found
	 */
	public static PasswordEntry findByUuid_First(
			String uuid, OrderByComparator<PasswordEntry> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchEntryException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first password entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password entry, or <code>null</code> if a matching password entry could not be found
	 */
	public static PasswordEntry fetchByUuid_First(
		String uuid, OrderByComparator<PasswordEntry> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last password entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password entry
	 * @throws NoSuchEntryException if a matching password entry could not be found
	 */
	public static PasswordEntry findByUuid_Last(
			String uuid, OrderByComparator<PasswordEntry> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchEntryException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last password entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password entry, or <code>null</code> if a matching password entry could not be found
	 */
	public static PasswordEntry fetchByUuid_Last(
		String uuid, OrderByComparator<PasswordEntry> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the password entries before and after the current password entry in the ordered set where uuid = &#63;.
	 *
	 * @param entryId the primary key of the current password entry
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password entry
	 * @throws NoSuchEntryException if a password entry with the primary key could not be found
	 */
	public static PasswordEntry[] findByUuid_PrevAndNext(
			long entryId, String uuid,
			OrderByComparator<PasswordEntry> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchEntryException {

		return getPersistence().findByUuid_PrevAndNext(
			entryId, uuid, orderByComparator);
	}

	/**
	 * Removes all the password entries where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of password entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching password entries
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns all the password entries where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the matching password entries
	 */
	public static List<PasswordEntry> findByUserId(long userId) {
		return getPersistence().findByUserId(userId);
	}

	/**
	 * Returns a range of all the password entries where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordEntryModelImpl</code>.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of password entries
	 * @param end the upper bound of the range of password entries (not inclusive)
	 * @return the range of matching password entries
	 */
	public static List<PasswordEntry> findByUserId(
		long userId, int start, int end) {

		return getPersistence().findByUserId(userId, start, end);
	}

	/**
	 * Returns an ordered range of all the password entries where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordEntryModelImpl</code>.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of password entries
	 * @param end the upper bound of the range of password entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching password entries
	 */
	public static List<PasswordEntry> findByUserId(
		long userId, int start, int end,
		OrderByComparator<PasswordEntry> orderByComparator) {

		return getPersistence().findByUserId(
			userId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the password entries where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordEntryModelImpl</code>.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of password entries
	 * @param end the upper bound of the range of password entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching password entries
	 */
	public static List<PasswordEntry> findByUserId(
		long userId, int start, int end,
		OrderByComparator<PasswordEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUserId(
			userId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first password entry in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password entry
	 * @throws NoSuchEntryException if a matching password entry could not be found
	 */
	public static PasswordEntry findByUserId_First(
			long userId, OrderByComparator<PasswordEntry> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchEntryException {

		return getPersistence().findByUserId_First(userId, orderByComparator);
	}

	/**
	 * Returns the first password entry in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password entry, or <code>null</code> if a matching password entry could not be found
	 */
	public static PasswordEntry fetchByUserId_First(
		long userId, OrderByComparator<PasswordEntry> orderByComparator) {

		return getPersistence().fetchByUserId_First(userId, orderByComparator);
	}

	/**
	 * Returns the last password entry in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password entry
	 * @throws NoSuchEntryException if a matching password entry could not be found
	 */
	public static PasswordEntry findByUserId_Last(
			long userId, OrderByComparator<PasswordEntry> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchEntryException {

		return getPersistence().findByUserId_Last(userId, orderByComparator);
	}

	/**
	 * Returns the last password entry in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password entry, or <code>null</code> if a matching password entry could not be found
	 */
	public static PasswordEntry fetchByUserId_Last(
		long userId, OrderByComparator<PasswordEntry> orderByComparator) {

		return getPersistence().fetchByUserId_Last(userId, orderByComparator);
	}

	/**
	 * Returns the password entries before and after the current password entry in the ordered set where userId = &#63;.
	 *
	 * @param entryId the primary key of the current password entry
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password entry
	 * @throws NoSuchEntryException if a password entry with the primary key could not be found
	 */
	public static PasswordEntry[] findByUserId_PrevAndNext(
			long entryId, long userId,
			OrderByComparator<PasswordEntry> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchEntryException {

		return getPersistence().findByUserId_PrevAndNext(
			entryId, userId, orderByComparator);
	}

	/**
	 * Removes all the password entries where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 */
	public static void removeByUserId(long userId) {
		getPersistence().removeByUserId(userId);
	}

	/**
	 * Returns the number of password entries where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching password entries
	 */
	public static int countByUserId(long userId) {
		return getPersistence().countByUserId(userId);
	}

	/**
	 * Caches the password entry in the entity cache if it is enabled.
	 *
	 * @param passwordEntry the password entry
	 */
	public static void cacheResult(PasswordEntry passwordEntry) {
		getPersistence().cacheResult(passwordEntry);
	}

	/**
	 * Caches the password entries in the entity cache if it is enabled.
	 *
	 * @param passwordEntries the password entries
	 */
	public static void cacheResult(List<PasswordEntry> passwordEntries) {
		getPersistence().cacheResult(passwordEntries);
	}

	/**
	 * Creates a new password entry with the primary key. Does not add the password entry to the database.
	 *
	 * @param entryId the primary key for the new password entry
	 * @return the new password entry
	 */
	public static PasswordEntry create(long entryId) {
		return getPersistence().create(entryId);
	}

	/**
	 * Removes the password entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the password entry
	 * @return the password entry that was removed
	 * @throws NoSuchEntryException if a password entry with the primary key could not be found
	 */
	public static PasswordEntry remove(long entryId)
		throws com.liferay.portal.security.password.exception.
			NoSuchEntryException {

		return getPersistence().remove(entryId);
	}

	public static PasswordEntry updateImpl(PasswordEntry passwordEntry) {
		return getPersistence().updateImpl(passwordEntry);
	}

	/**
	 * Returns the password entry with the primary key or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param entryId the primary key of the password entry
	 * @return the password entry
	 * @throws NoSuchEntryException if a password entry with the primary key could not be found
	 */
	public static PasswordEntry findByPrimaryKey(long entryId)
		throws com.liferay.portal.security.password.exception.
			NoSuchEntryException {

		return getPersistence().findByPrimaryKey(entryId);
	}

	/**
	 * Returns the password entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param entryId the primary key of the password entry
	 * @return the password entry, or <code>null</code> if a password entry with the primary key could not be found
	 */
	public static PasswordEntry fetchByPrimaryKey(long entryId) {
		return getPersistence().fetchByPrimaryKey(entryId);
	}

	/**
	 * Returns all the password entries.
	 *
	 * @return the password entries
	 */
	public static List<PasswordEntry> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the password entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password entries
	 * @param end the upper bound of the range of password entries (not inclusive)
	 * @return the range of password entries
	 */
	public static List<PasswordEntry> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the password entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password entries
	 * @param end the upper bound of the range of password entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of password entries
	 */
	public static List<PasswordEntry> findAll(
		int start, int end,
		OrderByComparator<PasswordEntry> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the password entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password entries
	 * @param end the upper bound of the range of password entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of password entries
	 */
	public static List<PasswordEntry> findAll(
		int start, int end, OrderByComparator<PasswordEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the password entries from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of password entries.
	 *
	 * @return the number of password entries
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static PasswordEntryPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<PasswordEntryPersistence, PasswordEntryPersistence> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(PasswordEntryPersistence.class);

		ServiceTracker<PasswordEntryPersistence, PasswordEntryPersistence>
			serviceTracker =
				new ServiceTracker
					<PasswordEntryPersistence, PasswordEntryPersistence>(
						bundle.getBundleContext(),
						PasswordEntryPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}
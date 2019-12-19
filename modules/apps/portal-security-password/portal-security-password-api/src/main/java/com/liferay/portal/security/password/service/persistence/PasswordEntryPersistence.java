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

import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.security.password.exception.NoSuchEntryException;
import com.liferay.portal.security.password.model.PasswordEntry;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the password entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author arthurchan35
 * @see PasswordEntryUtil
 * @generated
 */
@ProviderType
public interface PasswordEntryPersistence
	extends BasePersistence<PasswordEntry> {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link PasswordEntryUtil} to access the password entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the password entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching password entries
	 */
	public java.util.List<PasswordEntry> findByUuid(String uuid);

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
	public java.util.List<PasswordEntry> findByUuid(
		String uuid, int start, int end);

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
	public java.util.List<PasswordEntry> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
			orderByComparator);

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
	public java.util.List<PasswordEntry> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first password entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password entry
	 * @throws NoSuchEntryException if a matching password entry could not be found
	 */
	public PasswordEntry findByUuid_First(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
				orderByComparator)
		throws NoSuchEntryException;

	/**
	 * Returns the first password entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password entry, or <code>null</code> if a matching password entry could not be found
	 */
	public PasswordEntry fetchByUuid_First(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
			orderByComparator);

	/**
	 * Returns the last password entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password entry
	 * @throws NoSuchEntryException if a matching password entry could not be found
	 */
	public PasswordEntry findByUuid_Last(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
				orderByComparator)
		throws NoSuchEntryException;

	/**
	 * Returns the last password entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password entry, or <code>null</code> if a matching password entry could not be found
	 */
	public PasswordEntry fetchByUuid_Last(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
			orderByComparator);

	/**
	 * Returns the password entries before and after the current password entry in the ordered set where uuid = &#63;.
	 *
	 * @param entryId the primary key of the current password entry
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password entry
	 * @throws NoSuchEntryException if a password entry with the primary key could not be found
	 */
	public PasswordEntry[] findByUuid_PrevAndNext(
			long entryId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
				orderByComparator)
		throws NoSuchEntryException;

	/**
	 * Removes all the password entries where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public void removeByUuid(String uuid);

	/**
	 * Returns the number of password entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching password entries
	 */
	public int countByUuid(String uuid);

	/**
	 * Returns all the password entries where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the matching password entries
	 */
	public java.util.List<PasswordEntry> findByUserId(long userId);

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
	public java.util.List<PasswordEntry> findByUserId(
		long userId, int start, int end);

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
	public java.util.List<PasswordEntry> findByUserId(
		long userId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
			orderByComparator);

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
	public java.util.List<PasswordEntry> findByUserId(
		long userId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first password entry in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password entry
	 * @throws NoSuchEntryException if a matching password entry could not be found
	 */
	public PasswordEntry findByUserId_First(
			long userId,
			com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
				orderByComparator)
		throws NoSuchEntryException;

	/**
	 * Returns the first password entry in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password entry, or <code>null</code> if a matching password entry could not be found
	 */
	public PasswordEntry fetchByUserId_First(
		long userId,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
			orderByComparator);

	/**
	 * Returns the last password entry in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password entry
	 * @throws NoSuchEntryException if a matching password entry could not be found
	 */
	public PasswordEntry findByUserId_Last(
			long userId,
			com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
				orderByComparator)
		throws NoSuchEntryException;

	/**
	 * Returns the last password entry in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password entry, or <code>null</code> if a matching password entry could not be found
	 */
	public PasswordEntry fetchByUserId_Last(
		long userId,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
			orderByComparator);

	/**
	 * Returns the password entries before and after the current password entry in the ordered set where userId = &#63;.
	 *
	 * @param entryId the primary key of the current password entry
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password entry
	 * @throws NoSuchEntryException if a password entry with the primary key could not be found
	 */
	public PasswordEntry[] findByUserId_PrevAndNext(
			long entryId, long userId,
			com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
				orderByComparator)
		throws NoSuchEntryException;

	/**
	 * Removes all the password entries where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 */
	public void removeByUserId(long userId);

	/**
	 * Returns the number of password entries where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching password entries
	 */
	public int countByUserId(long userId);

	/**
	 * Caches the password entry in the entity cache if it is enabled.
	 *
	 * @param passwordEntry the password entry
	 */
	public void cacheResult(PasswordEntry passwordEntry);

	/**
	 * Caches the password entries in the entity cache if it is enabled.
	 *
	 * @param passwordEntries the password entries
	 */
	public void cacheResult(java.util.List<PasswordEntry> passwordEntries);

	/**
	 * Creates a new password entry with the primary key. Does not add the password entry to the database.
	 *
	 * @param entryId the primary key for the new password entry
	 * @return the new password entry
	 */
	public PasswordEntry create(long entryId);

	/**
	 * Removes the password entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the password entry
	 * @return the password entry that was removed
	 * @throws NoSuchEntryException if a password entry with the primary key could not be found
	 */
	public PasswordEntry remove(long entryId) throws NoSuchEntryException;

	public PasswordEntry updateImpl(PasswordEntry passwordEntry);

	/**
	 * Returns the password entry with the primary key or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param entryId the primary key of the password entry
	 * @return the password entry
	 * @throws NoSuchEntryException if a password entry with the primary key could not be found
	 */
	public PasswordEntry findByPrimaryKey(long entryId)
		throws NoSuchEntryException;

	/**
	 * Returns the password entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param entryId the primary key of the password entry
	 * @return the password entry, or <code>null</code> if a password entry with the primary key could not be found
	 */
	public PasswordEntry fetchByPrimaryKey(long entryId);

	/**
	 * Returns all the password entries.
	 *
	 * @return the password entries
	 */
	public java.util.List<PasswordEntry> findAll();

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
	public java.util.List<PasswordEntry> findAll(int start, int end);

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
	public java.util.List<PasswordEntry> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
			orderByComparator);

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
	public java.util.List<PasswordEntry> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordEntry>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the password entries from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of password entries.
	 *
	 * @return the number of password entries
	 */
	public int countAll();

}
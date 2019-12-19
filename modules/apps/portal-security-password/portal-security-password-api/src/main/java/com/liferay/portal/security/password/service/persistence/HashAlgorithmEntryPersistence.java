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
import com.liferay.portal.security.password.exception.NoSuchHashAlgorithmEntryException;
import com.liferay.portal.security.password.model.HashAlgorithmEntry;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the hash algorithm entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author arthurchan35
 * @see HashAlgorithmEntryUtil
 * @generated
 */
@ProviderType
public interface HashAlgorithmEntryPersistence
	extends BasePersistence<HashAlgorithmEntry> {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link HashAlgorithmEntryUtil} to access the hash algorithm entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the hash algorithm entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching hash algorithm entries
	 */
	public java.util.List<HashAlgorithmEntry> findByUuid(String uuid);

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
	public java.util.List<HashAlgorithmEntry> findByUuid(
		String uuid, int start, int end);

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
	public java.util.List<HashAlgorithmEntry> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<HashAlgorithmEntry>
			orderByComparator);

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
	public java.util.List<HashAlgorithmEntry> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<HashAlgorithmEntry>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching hash algorithm entry
	 * @throws NoSuchHashAlgorithmEntryException if a matching hash algorithm entry could not be found
	 */
	public HashAlgorithmEntry findByUuid_First(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<HashAlgorithmEntry>
				orderByComparator)
		throws NoSuchHashAlgorithmEntryException;

	/**
	 * Returns the first hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching hash algorithm entry, or <code>null</code> if a matching hash algorithm entry could not be found
	 */
	public HashAlgorithmEntry fetchByUuid_First(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<HashAlgorithmEntry>
			orderByComparator);

	/**
	 * Returns the last hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching hash algorithm entry
	 * @throws NoSuchHashAlgorithmEntryException if a matching hash algorithm entry could not be found
	 */
	public HashAlgorithmEntry findByUuid_Last(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<HashAlgorithmEntry>
				orderByComparator)
		throws NoSuchHashAlgorithmEntryException;

	/**
	 * Returns the last hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching hash algorithm entry, or <code>null</code> if a matching hash algorithm entry could not be found
	 */
	public HashAlgorithmEntry fetchByUuid_Last(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<HashAlgorithmEntry>
			orderByComparator);

	/**
	 * Returns the hash algorithm entries before and after the current hash algorithm entry in the ordered set where uuid = &#63;.
	 *
	 * @param entryId the primary key of the current hash algorithm entry
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next hash algorithm entry
	 * @throws NoSuchHashAlgorithmEntryException if a hash algorithm entry with the primary key could not be found
	 */
	public HashAlgorithmEntry[] findByUuid_PrevAndNext(
			long entryId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<HashAlgorithmEntry>
				orderByComparator)
		throws NoSuchHashAlgorithmEntryException;

	/**
	 * Removes all the hash algorithm entries where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public void removeByUuid(String uuid);

	/**
	 * Returns the number of hash algorithm entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching hash algorithm entries
	 */
	public int countByUuid(String uuid);

	/**
	 * Caches the hash algorithm entry in the entity cache if it is enabled.
	 *
	 * @param hashAlgorithmEntry the hash algorithm entry
	 */
	public void cacheResult(HashAlgorithmEntry hashAlgorithmEntry);

	/**
	 * Caches the hash algorithm entries in the entity cache if it is enabled.
	 *
	 * @param hashAlgorithmEntries the hash algorithm entries
	 */
	public void cacheResult(
		java.util.List<HashAlgorithmEntry> hashAlgorithmEntries);

	/**
	 * Creates a new hash algorithm entry with the primary key. Does not add the hash algorithm entry to the database.
	 *
	 * @param entryId the primary key for the new hash algorithm entry
	 * @return the new hash algorithm entry
	 */
	public HashAlgorithmEntry create(long entryId);

	/**
	 * Removes the hash algorithm entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the hash algorithm entry
	 * @return the hash algorithm entry that was removed
	 * @throws NoSuchHashAlgorithmEntryException if a hash algorithm entry with the primary key could not be found
	 */
	public HashAlgorithmEntry remove(long entryId)
		throws NoSuchHashAlgorithmEntryException;

	public HashAlgorithmEntry updateImpl(HashAlgorithmEntry hashAlgorithmEntry);

	/**
	 * Returns the hash algorithm entry with the primary key or throws a <code>NoSuchHashAlgorithmEntryException</code> if it could not be found.
	 *
	 * @param entryId the primary key of the hash algorithm entry
	 * @return the hash algorithm entry
	 * @throws NoSuchHashAlgorithmEntryException if a hash algorithm entry with the primary key could not be found
	 */
	public HashAlgorithmEntry findByPrimaryKey(long entryId)
		throws NoSuchHashAlgorithmEntryException;

	/**
	 * Returns the hash algorithm entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param entryId the primary key of the hash algorithm entry
	 * @return the hash algorithm entry, or <code>null</code> if a hash algorithm entry with the primary key could not be found
	 */
	public HashAlgorithmEntry fetchByPrimaryKey(long entryId);

	/**
	 * Returns all the hash algorithm entries.
	 *
	 * @return the hash algorithm entries
	 */
	public java.util.List<HashAlgorithmEntry> findAll();

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
	public java.util.List<HashAlgorithmEntry> findAll(int start, int end);

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
	public java.util.List<HashAlgorithmEntry> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<HashAlgorithmEntry>
			orderByComparator);

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
	public java.util.List<HashAlgorithmEntry> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<HashAlgorithmEntry>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the hash algorithm entries from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of hash algorithm entries.
	 *
	 * @return the number of hash algorithm entries
	 */
	public int countAll();

}
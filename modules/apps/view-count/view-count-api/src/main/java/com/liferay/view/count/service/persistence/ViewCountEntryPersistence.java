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

package com.liferay.view.count.service.persistence;

import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.view.count.exception.NoSuchEntryException;
import com.liferay.view.count.model.ViewCountEntry;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the view count entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Preston Crary
 * @see ViewCountEntryUtil
 * @generated
 */
@ProviderType
public interface ViewCountEntryPersistence
	extends BasePersistence<ViewCountEntry> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link ViewCountEntryUtil} to access the view count entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the view count entries where companyId = &#63; and classNameId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @return the matching view count entries
	 */
	public java.util.List<ViewCountEntry> findByC_C(
		long companyId, long classNameId);

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
	public java.util.List<ViewCountEntry> findByC_C(
		long companyId, long classNameId, int start, int end);

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
	public java.util.List<ViewCountEntry> findByC_C(
		long companyId, long classNameId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ViewCountEntry>
			orderByComparator);

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
	public java.util.List<ViewCountEntry> findByC_C(
		long companyId, long classNameId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ViewCountEntry>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first view count entry in the ordered set where companyId = &#63; and classNameId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching view count entry
	 * @throws NoSuchEntryException if a matching view count entry could not be found
	 */
	public ViewCountEntry findByC_C_First(
			long companyId, long classNameId,
			com.liferay.portal.kernel.util.OrderByComparator<ViewCountEntry>
				orderByComparator)
		throws NoSuchEntryException;

	/**
	 * Returns the first view count entry in the ordered set where companyId = &#63; and classNameId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching view count entry, or <code>null</code> if a matching view count entry could not be found
	 */
	public ViewCountEntry fetchByC_C_First(
		long companyId, long classNameId,
		com.liferay.portal.kernel.util.OrderByComparator<ViewCountEntry>
			orderByComparator);

	/**
	 * Returns the last view count entry in the ordered set where companyId = &#63; and classNameId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching view count entry
	 * @throws NoSuchEntryException if a matching view count entry could not be found
	 */
	public ViewCountEntry findByC_C_Last(
			long companyId, long classNameId,
			com.liferay.portal.kernel.util.OrderByComparator<ViewCountEntry>
				orderByComparator)
		throws NoSuchEntryException;

	/**
	 * Returns the last view count entry in the ordered set where companyId = &#63; and classNameId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching view count entry, or <code>null</code> if a matching view count entry could not be found
	 */
	public ViewCountEntry fetchByC_C_Last(
		long companyId, long classNameId,
		com.liferay.portal.kernel.util.OrderByComparator<ViewCountEntry>
			orderByComparator);

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
	public ViewCountEntry[] findByC_C_PrevAndNext(
			long viewCountEntryId, long companyId, long classNameId,
			com.liferay.portal.kernel.util.OrderByComparator<ViewCountEntry>
				orderByComparator)
		throws NoSuchEntryException;

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
	public java.util.List<ViewCountEntry> findByC_C(
		long companyId, long[] classNameIds);

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
	public java.util.List<ViewCountEntry> findByC_C(
		long companyId, long[] classNameIds, int start, int end);

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
	public java.util.List<ViewCountEntry> findByC_C(
		long companyId, long[] classNameIds, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ViewCountEntry>
			orderByComparator);

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
	public java.util.List<ViewCountEntry> findByC_C(
		long companyId, long[] classNameIds, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ViewCountEntry>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the view count entries where companyId = &#63; and classNameId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 */
	public void removeByC_C(long companyId, long classNameId);

	/**
	 * Returns the number of view count entries where companyId = &#63; and classNameId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @return the number of matching view count entries
	 */
	public int countByC_C(long companyId, long classNameId);

	/**
	 * Returns the number of view count entries where companyId = &#63; and classNameId = any &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameIds the class name IDs
	 * @return the number of matching view count entries
	 */
	public int countByC_C(long companyId, long[] classNameIds);

	/**
	 * Returns the view count entry where companyId = &#63; and classNameId = &#63; and classPK = &#63; or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the matching view count entry
	 * @throws NoSuchEntryException if a matching view count entry could not be found
	 */
	public ViewCountEntry findByC_C_C(
			long companyId, long classNameId, long classPK)
		throws NoSuchEntryException;

	/**
	 * Returns the view count entry where companyId = &#63; and classNameId = &#63; and classPK = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the matching view count entry, or <code>null</code> if a matching view count entry could not be found
	 */
	public ViewCountEntry fetchByC_C_C(
		long companyId, long classNameId, long classPK);

	/**
	 * Returns the view count entry where companyId = &#63; and classNameId = &#63; and classPK = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching view count entry, or <code>null</code> if a matching view count entry could not be found
	 */
	public ViewCountEntry fetchByC_C_C(
		long companyId, long classNameId, long classPK, boolean useFinderCache);

	/**
	 * Removes the view count entry where companyId = &#63; and classNameId = &#63; and classPK = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the view count entry that was removed
	 */
	public ViewCountEntry removeByC_C_C(
			long companyId, long classNameId, long classPK)
		throws NoSuchEntryException;

	/**
	 * Returns the number of view count entries where companyId = &#63; and classNameId = &#63; and classPK = &#63;.
	 *
	 * @param companyId the company ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the number of matching view count entries
	 */
	public int countByC_C_C(long companyId, long classNameId, long classPK);

	/**
	 * Caches the view count entry in the entity cache if it is enabled.
	 *
	 * @param viewCountEntry the view count entry
	 */
	public void cacheResult(ViewCountEntry viewCountEntry);

	/**
	 * Caches the view count entries in the entity cache if it is enabled.
	 *
	 * @param viewCountEntries the view count entries
	 */
	public void cacheResult(java.util.List<ViewCountEntry> viewCountEntries);

	/**
	 * Creates a new view count entry with the primary key. Does not add the view count entry to the database.
	 *
	 * @param viewCountEntryId the primary key for the new view count entry
	 * @return the new view count entry
	 */
	public ViewCountEntry create(long viewCountEntryId);

	/**
	 * Removes the view count entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param viewCountEntryId the primary key of the view count entry
	 * @return the view count entry that was removed
	 * @throws NoSuchEntryException if a view count entry with the primary key could not be found
	 */
	public ViewCountEntry remove(long viewCountEntryId)
		throws NoSuchEntryException;

	public ViewCountEntry updateImpl(ViewCountEntry viewCountEntry);

	/**
	 * Returns the view count entry with the primary key or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param viewCountEntryId the primary key of the view count entry
	 * @return the view count entry
	 * @throws NoSuchEntryException if a view count entry with the primary key could not be found
	 */
	public ViewCountEntry findByPrimaryKey(long viewCountEntryId)
		throws NoSuchEntryException;

	/**
	 * Returns the view count entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param viewCountEntryId the primary key of the view count entry
	 * @return the view count entry, or <code>null</code> if a view count entry with the primary key could not be found
	 */
	public ViewCountEntry fetchByPrimaryKey(long viewCountEntryId);

	/**
	 * Returns all the view count entries.
	 *
	 * @return the view count entries
	 */
	public java.util.List<ViewCountEntry> findAll();

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
	public java.util.List<ViewCountEntry> findAll(int start, int end);

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
	public java.util.List<ViewCountEntry> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ViewCountEntry>
			orderByComparator);

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
	public java.util.List<ViewCountEntry> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ViewCountEntry>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the view count entries from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of view count entries.
	 *
	 * @return the number of view count entries
	 */
	public int countAll();

}
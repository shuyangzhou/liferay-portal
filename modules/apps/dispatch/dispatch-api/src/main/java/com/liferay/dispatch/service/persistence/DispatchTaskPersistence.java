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

package com.liferay.dispatch.service.persistence;

import com.liferay.dispatch.exception.NoSuchTaskException;
import com.liferay.dispatch.model.DispatchTask;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the dispatch task service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Matija Petanjek
 * @see DispatchTaskUtil
 * @generated
 */
@ProviderType
public interface DispatchTaskPersistence extends BasePersistence<DispatchTask> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link DispatchTaskUtil} to access the dispatch task persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the dispatch tasks where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching dispatch tasks
	 */
	public java.util.List<DispatchTask> findByCompanyId(long companyId);

	/**
	 * Returns a range of all the dispatch tasks where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @return the range of matching dispatch tasks
	 */
	public java.util.List<DispatchTask> findByCompanyId(
		long companyId, int start, int end);

	/**
	 * Returns an ordered range of all the dispatch tasks where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching dispatch tasks
	 */
	public java.util.List<DispatchTask> findByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
			orderByComparator);

	/**
	 * Returns an ordered range of all the dispatch tasks where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching dispatch tasks
	 */
	public java.util.List<DispatchTask> findByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first dispatch task in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dispatch task
	 * @throws NoSuchTaskException if a matching dispatch task could not be found
	 */
	public DispatchTask findByCompanyId_First(
			long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
				orderByComparator)
		throws NoSuchTaskException;

	/**
	 * Returns the first dispatch task in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dispatch task, or <code>null</code> if a matching dispatch task could not be found
	 */
	public DispatchTask fetchByCompanyId_First(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
			orderByComparator);

	/**
	 * Returns the last dispatch task in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dispatch task
	 * @throws NoSuchTaskException if a matching dispatch task could not be found
	 */
	public DispatchTask findByCompanyId_Last(
			long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
				orderByComparator)
		throws NoSuchTaskException;

	/**
	 * Returns the last dispatch task in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dispatch task, or <code>null</code> if a matching dispatch task could not be found
	 */
	public DispatchTask fetchByCompanyId_Last(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
			orderByComparator);

	/**
	 * Returns the dispatch tasks before and after the current dispatch task in the ordered set where companyId = &#63;.
	 *
	 * @param dispatchTaskId the primary key of the current dispatch task
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next dispatch task
	 * @throws NoSuchTaskException if a dispatch task with the primary key could not be found
	 */
	public DispatchTask[] findByCompanyId_PrevAndNext(
			long dispatchTaskId, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
				orderByComparator)
		throws NoSuchTaskException;

	/**
	 * Returns all the dispatch tasks that the user has permission to view where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching dispatch tasks that the user has permission to view
	 */
	public java.util.List<DispatchTask> filterFindByCompanyId(long companyId);

	/**
	 * Returns a range of all the dispatch tasks that the user has permission to view where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @return the range of matching dispatch tasks that the user has permission to view
	 */
	public java.util.List<DispatchTask> filterFindByCompanyId(
		long companyId, int start, int end);

	/**
	 * Returns an ordered range of all the dispatch tasks that the user has permissions to view where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching dispatch tasks that the user has permission to view
	 */
	public java.util.List<DispatchTask> filterFindByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
			orderByComparator);

	/**
	 * Returns the dispatch tasks before and after the current dispatch task in the ordered set of dispatch tasks that the user has permission to view where companyId = &#63;.
	 *
	 * @param dispatchTaskId the primary key of the current dispatch task
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next dispatch task
	 * @throws NoSuchTaskException if a dispatch task with the primary key could not be found
	 */
	public DispatchTask[] filterFindByCompanyId_PrevAndNext(
			long dispatchTaskId, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
				orderByComparator)
		throws NoSuchTaskException;

	/**
	 * Removes all the dispatch tasks where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	public void removeByCompanyId(long companyId);

	/**
	 * Returns the number of dispatch tasks where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching dispatch tasks
	 */
	public int countByCompanyId(long companyId);

	/**
	 * Returns the number of dispatch tasks that the user has permission to view where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching dispatch tasks that the user has permission to view
	 */
	public int filterCountByCompanyId(long companyId);

	/**
	 * Returns the dispatch task where companyId = &#63; and name = &#63; or throws a <code>NoSuchTaskException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @return the matching dispatch task
	 * @throws NoSuchTaskException if a matching dispatch task could not be found
	 */
	public DispatchTask findByC_N(long companyId, String name)
		throws NoSuchTaskException;

	/**
	 * Returns the dispatch task where companyId = &#63; and name = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @return the matching dispatch task, or <code>null</code> if a matching dispatch task could not be found
	 */
	public DispatchTask fetchByC_N(long companyId, String name);

	/**
	 * Returns the dispatch task where companyId = &#63; and name = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching dispatch task, or <code>null</code> if a matching dispatch task could not be found
	 */
	public DispatchTask fetchByC_N(
		long companyId, String name, boolean useFinderCache);

	/**
	 * Removes the dispatch task where companyId = &#63; and name = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @return the dispatch task that was removed
	 */
	public DispatchTask removeByC_N(long companyId, String name)
		throws NoSuchTaskException;

	/**
	 * Returns the number of dispatch tasks where companyId = &#63; and name = &#63;.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @return the number of matching dispatch tasks
	 */
	public int countByC_N(long companyId, String name);

	/**
	 * Returns all the dispatch tasks where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @return the matching dispatch tasks
	 */
	public java.util.List<DispatchTask> findByC_T(long companyId, String type);

	/**
	 * Returns a range of all the dispatch tasks where companyId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @return the range of matching dispatch tasks
	 */
	public java.util.List<DispatchTask> findByC_T(
		long companyId, String type, int start, int end);

	/**
	 * Returns an ordered range of all the dispatch tasks where companyId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching dispatch tasks
	 */
	public java.util.List<DispatchTask> findByC_T(
		long companyId, String type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
			orderByComparator);

	/**
	 * Returns an ordered range of all the dispatch tasks where companyId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching dispatch tasks
	 */
	public java.util.List<DispatchTask> findByC_T(
		long companyId, String type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first dispatch task in the ordered set where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dispatch task
	 * @throws NoSuchTaskException if a matching dispatch task could not be found
	 */
	public DispatchTask findByC_T_First(
			long companyId, String type,
			com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
				orderByComparator)
		throws NoSuchTaskException;

	/**
	 * Returns the first dispatch task in the ordered set where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dispatch task, or <code>null</code> if a matching dispatch task could not be found
	 */
	public DispatchTask fetchByC_T_First(
		long companyId, String type,
		com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
			orderByComparator);

	/**
	 * Returns the last dispatch task in the ordered set where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dispatch task
	 * @throws NoSuchTaskException if a matching dispatch task could not be found
	 */
	public DispatchTask findByC_T_Last(
			long companyId, String type,
			com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
				orderByComparator)
		throws NoSuchTaskException;

	/**
	 * Returns the last dispatch task in the ordered set where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dispatch task, or <code>null</code> if a matching dispatch task could not be found
	 */
	public DispatchTask fetchByC_T_Last(
		long companyId, String type,
		com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
			orderByComparator);

	/**
	 * Returns the dispatch tasks before and after the current dispatch task in the ordered set where companyId = &#63; and type = &#63;.
	 *
	 * @param dispatchTaskId the primary key of the current dispatch task
	 * @param companyId the company ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next dispatch task
	 * @throws NoSuchTaskException if a dispatch task with the primary key could not be found
	 */
	public DispatchTask[] findByC_T_PrevAndNext(
			long dispatchTaskId, long companyId, String type,
			com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
				orderByComparator)
		throws NoSuchTaskException;

	/**
	 * Returns all the dispatch tasks that the user has permission to view where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @return the matching dispatch tasks that the user has permission to view
	 */
	public java.util.List<DispatchTask> filterFindByC_T(
		long companyId, String type);

	/**
	 * Returns a range of all the dispatch tasks that the user has permission to view where companyId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @return the range of matching dispatch tasks that the user has permission to view
	 */
	public java.util.List<DispatchTask> filterFindByC_T(
		long companyId, String type, int start, int end);

	/**
	 * Returns an ordered range of all the dispatch tasks that the user has permissions to view where companyId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching dispatch tasks that the user has permission to view
	 */
	public java.util.List<DispatchTask> filterFindByC_T(
		long companyId, String type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
			orderByComparator);

	/**
	 * Returns the dispatch tasks before and after the current dispatch task in the ordered set of dispatch tasks that the user has permission to view where companyId = &#63; and type = &#63;.
	 *
	 * @param dispatchTaskId the primary key of the current dispatch task
	 * @param companyId the company ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next dispatch task
	 * @throws NoSuchTaskException if a dispatch task with the primary key could not be found
	 */
	public DispatchTask[] filterFindByC_T_PrevAndNext(
			long dispatchTaskId, long companyId, String type,
			com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
				orderByComparator)
		throws NoSuchTaskException;

	/**
	 * Removes all the dispatch tasks where companyId = &#63; and type = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 */
	public void removeByC_T(long companyId, String type);

	/**
	 * Returns the number of dispatch tasks where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @return the number of matching dispatch tasks
	 */
	public int countByC_T(long companyId, String type);

	/**
	 * Returns the number of dispatch tasks that the user has permission to view where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @return the number of matching dispatch tasks that the user has permission to view
	 */
	public int filterCountByC_T(long companyId, String type);

	/**
	 * Caches the dispatch task in the entity cache if it is enabled.
	 *
	 * @param dispatchTask the dispatch task
	 */
	public void cacheResult(DispatchTask dispatchTask);

	/**
	 * Caches the dispatch tasks in the entity cache if it is enabled.
	 *
	 * @param dispatchTasks the dispatch tasks
	 */
	public void cacheResult(java.util.List<DispatchTask> dispatchTasks);

	/**
	 * Creates a new dispatch task with the primary key. Does not add the dispatch task to the database.
	 *
	 * @param dispatchTaskId the primary key for the new dispatch task
	 * @return the new dispatch task
	 */
	public DispatchTask create(long dispatchTaskId);

	/**
	 * Removes the dispatch task with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param dispatchTaskId the primary key of the dispatch task
	 * @return the dispatch task that was removed
	 * @throws NoSuchTaskException if a dispatch task with the primary key could not be found
	 */
	public DispatchTask remove(long dispatchTaskId) throws NoSuchTaskException;

	public DispatchTask updateImpl(DispatchTask dispatchTask);

	/**
	 * Returns the dispatch task with the primary key or throws a <code>NoSuchTaskException</code> if it could not be found.
	 *
	 * @param dispatchTaskId the primary key of the dispatch task
	 * @return the dispatch task
	 * @throws NoSuchTaskException if a dispatch task with the primary key could not be found
	 */
	public DispatchTask findByPrimaryKey(long dispatchTaskId)
		throws NoSuchTaskException;

	/**
	 * Returns the dispatch task with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param dispatchTaskId the primary key of the dispatch task
	 * @return the dispatch task, or <code>null</code> if a dispatch task with the primary key could not be found
	 */
	public DispatchTask fetchByPrimaryKey(long dispatchTaskId);

	/**
	 * Returns all the dispatch tasks.
	 *
	 * @return the dispatch tasks
	 */
	public java.util.List<DispatchTask> findAll();

	/**
	 * Returns a range of all the dispatch tasks.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @return the range of dispatch tasks
	 */
	public java.util.List<DispatchTask> findAll(int start, int end);

	/**
	 * Returns an ordered range of all the dispatch tasks.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of dispatch tasks
	 */
	public java.util.List<DispatchTask> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
			orderByComparator);

	/**
	 * Returns an ordered range of all the dispatch tasks.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of dispatch tasks
	 */
	public java.util.List<DispatchTask> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DispatchTask>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the dispatch tasks from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of dispatch tasks.
	 *
	 * @return the number of dispatch tasks
	 */
	public int countAll();

}
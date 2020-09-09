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

import com.liferay.dispatch.model.DispatchTask;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the dispatch task service. This utility wraps <code>com.liferay.dispatch.service.persistence.impl.DispatchTaskPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Matija Petanjek
 * @see DispatchTaskPersistence
 * @generated
 */
public class DispatchTaskUtil {

	/*
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
	public static void clearCache(DispatchTask dispatchTask) {
		getPersistence().clearCache(dispatchTask);
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
	public static Map<Serializable, DispatchTask> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<DispatchTask> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<DispatchTask> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<DispatchTask> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<DispatchTask> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static DispatchTask update(DispatchTask dispatchTask) {
		return getPersistence().update(dispatchTask);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static DispatchTask update(
		DispatchTask dispatchTask, ServiceContext serviceContext) {

		return getPersistence().update(dispatchTask, serviceContext);
	}

	/**
	 * Returns all the dispatch tasks where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching dispatch tasks
	 */
	public static List<DispatchTask> findByCompanyId(long companyId) {
		return getPersistence().findByCompanyId(companyId);
	}

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
	public static List<DispatchTask> findByCompanyId(
		long companyId, int start, int end) {

		return getPersistence().findByCompanyId(companyId, start, end);
	}

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
	public static List<DispatchTask> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<DispatchTask> orderByComparator) {

		return getPersistence().findByCompanyId(
			companyId, start, end, orderByComparator);
	}

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
	public static List<DispatchTask> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<DispatchTask> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByCompanyId(
			companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first dispatch task in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dispatch task
	 * @throws NoSuchTaskException if a matching dispatch task could not be found
	 */
	public static DispatchTask findByCompanyId_First(
			long companyId, OrderByComparator<DispatchTask> orderByComparator)
		throws com.liferay.dispatch.exception.NoSuchTaskException {

		return getPersistence().findByCompanyId_First(
			companyId, orderByComparator);
	}

	/**
	 * Returns the first dispatch task in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dispatch task, or <code>null</code> if a matching dispatch task could not be found
	 */
	public static DispatchTask fetchByCompanyId_First(
		long companyId, OrderByComparator<DispatchTask> orderByComparator) {

		return getPersistence().fetchByCompanyId_First(
			companyId, orderByComparator);
	}

	/**
	 * Returns the last dispatch task in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dispatch task
	 * @throws NoSuchTaskException if a matching dispatch task could not be found
	 */
	public static DispatchTask findByCompanyId_Last(
			long companyId, OrderByComparator<DispatchTask> orderByComparator)
		throws com.liferay.dispatch.exception.NoSuchTaskException {

		return getPersistence().findByCompanyId_Last(
			companyId, orderByComparator);
	}

	/**
	 * Returns the last dispatch task in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dispatch task, or <code>null</code> if a matching dispatch task could not be found
	 */
	public static DispatchTask fetchByCompanyId_Last(
		long companyId, OrderByComparator<DispatchTask> orderByComparator) {

		return getPersistence().fetchByCompanyId_Last(
			companyId, orderByComparator);
	}

	/**
	 * Returns the dispatch tasks before and after the current dispatch task in the ordered set where companyId = &#63;.
	 *
	 * @param dispatchTaskId the primary key of the current dispatch task
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next dispatch task
	 * @throws NoSuchTaskException if a dispatch task with the primary key could not be found
	 */
	public static DispatchTask[] findByCompanyId_PrevAndNext(
			long dispatchTaskId, long companyId,
			OrderByComparator<DispatchTask> orderByComparator)
		throws com.liferay.dispatch.exception.NoSuchTaskException {

		return getPersistence().findByCompanyId_PrevAndNext(
			dispatchTaskId, companyId, orderByComparator);
	}

	/**
	 * Removes all the dispatch tasks where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	public static void removeByCompanyId(long companyId) {
		getPersistence().removeByCompanyId(companyId);
	}

	/**
	 * Returns the number of dispatch tasks where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching dispatch tasks
	 */
	public static int countByCompanyId(long companyId) {
		return getPersistence().countByCompanyId(companyId);
	}

	/**
	 * Returns the dispatch task where companyId = &#63; and name = &#63; or throws a <code>NoSuchTaskException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @return the matching dispatch task
	 * @throws NoSuchTaskException if a matching dispatch task could not be found
	 */
	public static DispatchTask findByC_N(long companyId, String name)
		throws com.liferay.dispatch.exception.NoSuchTaskException {

		return getPersistence().findByC_N(companyId, name);
	}

	/**
	 * Returns the dispatch task where companyId = &#63; and name = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @return the matching dispatch task, or <code>null</code> if a matching dispatch task could not be found
	 */
	public static DispatchTask fetchByC_N(long companyId, String name) {
		return getPersistence().fetchByC_N(companyId, name);
	}

	/**
	 * Returns the dispatch task where companyId = &#63; and name = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching dispatch task, or <code>null</code> if a matching dispatch task could not be found
	 */
	public static DispatchTask fetchByC_N(
		long companyId, String name, boolean useFinderCache) {

		return getPersistence().fetchByC_N(companyId, name, useFinderCache);
	}

	/**
	 * Removes the dispatch task where companyId = &#63; and name = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @return the dispatch task that was removed
	 */
	public static DispatchTask removeByC_N(long companyId, String name)
		throws com.liferay.dispatch.exception.NoSuchTaskException {

		return getPersistence().removeByC_N(companyId, name);
	}

	/**
	 * Returns the number of dispatch tasks where companyId = &#63; and name = &#63;.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @return the number of matching dispatch tasks
	 */
	public static int countByC_N(long companyId, String name) {
		return getPersistence().countByC_N(companyId, name);
	}

	/**
	 * Returns all the dispatch tasks where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @return the matching dispatch tasks
	 */
	public static List<DispatchTask> findByC_T(long companyId, String type) {
		return getPersistence().findByC_T(companyId, type);
	}

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
	public static List<DispatchTask> findByC_T(
		long companyId, String type, int start, int end) {

		return getPersistence().findByC_T(companyId, type, start, end);
	}

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
	public static List<DispatchTask> findByC_T(
		long companyId, String type, int start, int end,
		OrderByComparator<DispatchTask> orderByComparator) {

		return getPersistence().findByC_T(
			companyId, type, start, end, orderByComparator);
	}

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
	public static List<DispatchTask> findByC_T(
		long companyId, String type, int start, int end,
		OrderByComparator<DispatchTask> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByC_T(
			companyId, type, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first dispatch task in the ordered set where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dispatch task
	 * @throws NoSuchTaskException if a matching dispatch task could not be found
	 */
	public static DispatchTask findByC_T_First(
			long companyId, String type,
			OrderByComparator<DispatchTask> orderByComparator)
		throws com.liferay.dispatch.exception.NoSuchTaskException {

		return getPersistence().findByC_T_First(
			companyId, type, orderByComparator);
	}

	/**
	 * Returns the first dispatch task in the ordered set where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dispatch task, or <code>null</code> if a matching dispatch task could not be found
	 */
	public static DispatchTask fetchByC_T_First(
		long companyId, String type,
		OrderByComparator<DispatchTask> orderByComparator) {

		return getPersistence().fetchByC_T_First(
			companyId, type, orderByComparator);
	}

	/**
	 * Returns the last dispatch task in the ordered set where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dispatch task
	 * @throws NoSuchTaskException if a matching dispatch task could not be found
	 */
	public static DispatchTask findByC_T_Last(
			long companyId, String type,
			OrderByComparator<DispatchTask> orderByComparator)
		throws com.liferay.dispatch.exception.NoSuchTaskException {

		return getPersistence().findByC_T_Last(
			companyId, type, orderByComparator);
	}

	/**
	 * Returns the last dispatch task in the ordered set where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dispatch task, or <code>null</code> if a matching dispatch task could not be found
	 */
	public static DispatchTask fetchByC_T_Last(
		long companyId, String type,
		OrderByComparator<DispatchTask> orderByComparator) {

		return getPersistence().fetchByC_T_Last(
			companyId, type, orderByComparator);
	}

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
	public static DispatchTask[] findByC_T_PrevAndNext(
			long dispatchTaskId, long companyId, String type,
			OrderByComparator<DispatchTask> orderByComparator)
		throws com.liferay.dispatch.exception.NoSuchTaskException {

		return getPersistence().findByC_T_PrevAndNext(
			dispatchTaskId, companyId, type, orderByComparator);
	}

	/**
	 * Removes all the dispatch tasks where companyId = &#63; and type = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 */
	public static void removeByC_T(long companyId, String type) {
		getPersistence().removeByC_T(companyId, type);
	}

	/**
	 * Returns the number of dispatch tasks where companyId = &#63; and type = &#63;.
	 *
	 * @param companyId the company ID
	 * @param type the type
	 * @return the number of matching dispatch tasks
	 */
	public static int countByC_T(long companyId, String type) {
		return getPersistence().countByC_T(companyId, type);
	}

	/**
	 * Caches the dispatch task in the entity cache if it is enabled.
	 *
	 * @param dispatchTask the dispatch task
	 */
	public static void cacheResult(DispatchTask dispatchTask) {
		getPersistence().cacheResult(dispatchTask);
	}

	/**
	 * Caches the dispatch tasks in the entity cache if it is enabled.
	 *
	 * @param dispatchTasks the dispatch tasks
	 */
	public static void cacheResult(List<DispatchTask> dispatchTasks) {
		getPersistence().cacheResult(dispatchTasks);
	}

	/**
	 * Creates a new dispatch task with the primary key. Does not add the dispatch task to the database.
	 *
	 * @param dispatchTaskId the primary key for the new dispatch task
	 * @return the new dispatch task
	 */
	public static DispatchTask create(long dispatchTaskId) {
		return getPersistence().create(dispatchTaskId);
	}

	/**
	 * Removes the dispatch task with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param dispatchTaskId the primary key of the dispatch task
	 * @return the dispatch task that was removed
	 * @throws NoSuchTaskException if a dispatch task with the primary key could not be found
	 */
	public static DispatchTask remove(long dispatchTaskId)
		throws com.liferay.dispatch.exception.NoSuchTaskException {

		return getPersistence().remove(dispatchTaskId);
	}

	public static DispatchTask updateImpl(DispatchTask dispatchTask) {
		return getPersistence().updateImpl(dispatchTask);
	}

	/**
	 * Returns the dispatch task with the primary key or throws a <code>NoSuchTaskException</code> if it could not be found.
	 *
	 * @param dispatchTaskId the primary key of the dispatch task
	 * @return the dispatch task
	 * @throws NoSuchTaskException if a dispatch task with the primary key could not be found
	 */
	public static DispatchTask findByPrimaryKey(long dispatchTaskId)
		throws com.liferay.dispatch.exception.NoSuchTaskException {

		return getPersistence().findByPrimaryKey(dispatchTaskId);
	}

	/**
	 * Returns the dispatch task with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param dispatchTaskId the primary key of the dispatch task
	 * @return the dispatch task, or <code>null</code> if a dispatch task with the primary key could not be found
	 */
	public static DispatchTask fetchByPrimaryKey(long dispatchTaskId) {
		return getPersistence().fetchByPrimaryKey(dispatchTaskId);
	}

	/**
	 * Returns all the dispatch tasks.
	 *
	 * @return the dispatch tasks
	 */
	public static List<DispatchTask> findAll() {
		return getPersistence().findAll();
	}

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
	public static List<DispatchTask> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

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
	public static List<DispatchTask> findAll(
		int start, int end, OrderByComparator<DispatchTask> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

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
	public static List<DispatchTask> findAll(
		int start, int end, OrderByComparator<DispatchTask> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the dispatch tasks from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of dispatch tasks.
	 *
	 * @return the number of dispatch tasks
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static DispatchTaskPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<DispatchTaskPersistence, DispatchTaskPersistence> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(DispatchTaskPersistence.class);

		ServiceTracker<DispatchTaskPersistence, DispatchTaskPersistence>
			serviceTracker =
				new ServiceTracker
					<DispatchTaskPersistence, DispatchTaskPersistence>(
						bundle.getBundleContext(),
						DispatchTaskPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}
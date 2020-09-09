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

package com.liferay.dispatch.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for DispatchTask. This utility wraps
 * <code>com.liferay.dispatch.service.impl.DispatchTaskLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Matija Petanjek
 * @see DispatchTaskLocalService
 * @generated
 */
public class DispatchTaskLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.dispatch.service.impl.DispatchTaskLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the dispatch task to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect DispatchTaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param dispatchTask the dispatch task
	 * @return the dispatch task that was added
	 */
	public static com.liferay.dispatch.model.DispatchTask addDispatchTask(
		com.liferay.dispatch.model.DispatchTask dispatchTask) {

		return getService().addDispatchTask(dispatchTask);
	}

	public static com.liferay.dispatch.model.DispatchTask addDispatchTask(
			long userId, String name, boolean system, String type,
			com.liferay.portal.kernel.util.UnicodeProperties
				typeSettingsUnicodeProperties)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addDispatchTask(
			userId, name, system, type, typeSettingsUnicodeProperties);
	}

	/**
	 * Creates a new dispatch task with the primary key. Does not add the dispatch task to the database.
	 *
	 * @param dispatchTaskId the primary key for the new dispatch task
	 * @return the new dispatch task
	 */
	public static com.liferay.dispatch.model.DispatchTask createDispatchTask(
		long dispatchTaskId) {

		return getService().createDispatchTask(dispatchTaskId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			createPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	/**
	 * Deletes the dispatch task from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect DispatchTaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param dispatchTask the dispatch task
	 * @return the dispatch task that was removed
	 * @throws PortalException
	 */
	public static com.liferay.dispatch.model.DispatchTask deleteDispatchTask(
			com.liferay.dispatch.model.DispatchTask dispatchTask)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteDispatchTask(dispatchTask);
	}

	/**
	 * Deletes the dispatch task with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect DispatchTaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param dispatchTaskId the primary key of the dispatch task
	 * @return the dispatch task that was removed
	 * @throws PortalException if a dispatch task with the primary key could not be found
	 */
	public static com.liferay.dispatch.model.DispatchTask deleteDispatchTask(
			long dispatchTaskId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteDispatchTask(dispatchTaskId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			deletePersistedModel(
				com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static <T> T dslQuery(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return getService().dslQuery(dslQuery);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery
		dynamicQuery() {

		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.dispatch.model.impl.DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.dispatch.model.impl.DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static com.liferay.dispatch.model.DispatchTask fetchDispatchTask(
		long dispatchTaskId) {

		return getService().fetchDispatchTask(dispatchTaskId);
	}

	public static com.liferay.dispatch.model.DispatchTask fetchDispatchTask(
		long companyId, String name) {

		return getService().fetchDispatchTask(companyId, name);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	/**
	 * Returns the dispatch task with the primary key.
	 *
	 * @param dispatchTaskId the primary key of the dispatch task
	 * @return the dispatch task
	 * @throws PortalException if a dispatch task with the primary key could not be found
	 */
	public static com.liferay.dispatch.model.DispatchTask getDispatchTask(
			long dispatchTaskId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getDispatchTask(dispatchTaskId);
	}

	/**
	 * Returns a range of all the dispatch tasks.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.dispatch.model.impl.DispatchTaskModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of dispatch tasks
	 * @param end the upper bound of the range of dispatch tasks (not inclusive)
	 * @return the range of dispatch tasks
	 */
	public static java.util.List<com.liferay.dispatch.model.DispatchTask>
		getDispatchTasks(int start, int end) {

		return getService().getDispatchTasks(start, end);
	}

	public static java.util.List<com.liferay.dispatch.model.DispatchTask>
		getDispatchTasks(long companyId, int start, int end) {

		return getService().getDispatchTasks(companyId, start, end);
	}

	/**
	 * Returns the number of dispatch tasks.
	 *
	 * @return the number of dispatch tasks
	 */
	public static int getDispatchTasksCount() {
		return getService().getDispatchTasksCount();
	}

	public static int getDispatchTasksCount(long companyId) {
		return getService().getDispatchTasksCount(companyId);
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	public static java.util.Date getNextFireDate(long dispatchTaskId) {
		return getService().getNextFireDate(dispatchTaskId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	public static java.util.Date getPreviousFireDate(long dispatchTaskId) {
		return getService().getPreviousFireDate(dispatchTaskId);
	}

	/**
	 * Updates the dispatch task in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect DispatchTaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param dispatchTask the dispatch task
	 * @return the dispatch task that was updated
	 */
	public static com.liferay.dispatch.model.DispatchTask updateDispatchTask(
		com.liferay.dispatch.model.DispatchTask dispatchTask) {

		return getService().updateDispatchTask(dispatchTask);
	}

	public static com.liferay.dispatch.model.DispatchTask updateDispatchTask(
			long dispatchTaskId, String name,
			com.liferay.portal.kernel.util.UnicodeProperties
				typeSettingsUnicodeProperties)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateDispatchTask(
			dispatchTaskId, name, typeSettingsUnicodeProperties);
	}

	public static com.liferay.dispatch.model.DispatchTask
			updateDispatchTaskTrigger(
				long dispatchTaskId, boolean active, String cronExpression,
				int endDateMonth, int endDateDay, int endDateYear,
				int endDateHour, int endDateMinute, boolean neverEnd,
				int startDateMonth, int startDateDay, int startDateYear,
				int startDateHour, int startDateMinute)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateDispatchTaskTrigger(
			dispatchTaskId, active, cronExpression, endDateMonth, endDateDay,
			endDateYear, endDateHour, endDateMinute, neverEnd, startDateMonth,
			startDateDay, startDateYear, startDateHour, startDateMinute);
	}

	public static DispatchTaskLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<DispatchTaskLocalService, DispatchTaskLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(DispatchTaskLocalService.class);

		ServiceTracker<DispatchTaskLocalService, DispatchTaskLocalService>
			serviceTracker =
				new ServiceTracker
					<DispatchTaskLocalService, DispatchTaskLocalService>(
						bundle.getBundleContext(),
						DispatchTaskLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}
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

package com.liferay.analytics.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link AnalyticsEventLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see AnalyticsEventLocalService
 * @generated
 */
@ProviderType
public class AnalyticsEventLocalServiceWrapper
	implements AnalyticsEventLocalService,
		ServiceWrapper<AnalyticsEventLocalService> {
	public AnalyticsEventLocalServiceWrapper(
		AnalyticsEventLocalService analyticsEventLocalService) {
		_analyticsEventLocalService = analyticsEventLocalService;
	}

	/**
	* Adds the analytics event to the database. Also notifies the appropriate model listeners.
	*
	* @param analyticsEvent the analytics event
	* @return the analytics event that was added
	*/
	@Override
	public com.liferay.analytics.model.AnalyticsEvent addAnalyticsEvent(
		com.liferay.analytics.model.AnalyticsEvent analyticsEvent) {
		return _analyticsEventLocalService.addAnalyticsEvent(analyticsEvent);
	}

	/**
	* Creates a new analytics event with the primary key. Does not add the analytics event to the database.
	*
	* @param analyticsEventId the primary key for the new analytics event
	* @return the new analytics event
	*/
	@Override
	public com.liferay.analytics.model.AnalyticsEvent createAnalyticsEvent(
		long analyticsEventId) {
		return _analyticsEventLocalService.createAnalyticsEvent(analyticsEventId);
	}

	/**
	* Deletes the analytics event from the database. Also notifies the appropriate model listeners.
	*
	* @param analyticsEvent the analytics event
	* @return the analytics event that was removed
	*/
	@Override
	public com.liferay.analytics.model.AnalyticsEvent deleteAnalyticsEvent(
		com.liferay.analytics.model.AnalyticsEvent analyticsEvent) {
		return _analyticsEventLocalService.deleteAnalyticsEvent(analyticsEvent);
	}

	/**
	* Deletes the analytics event with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param analyticsEventId the primary key of the analytics event
	* @return the analytics event that was removed
	* @throws PortalException if a analytics event with the primary key could not be found
	*/
	@Override
	public com.liferay.analytics.model.AnalyticsEvent deleteAnalyticsEvent(
		long analyticsEventId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _analyticsEventLocalService.deleteAnalyticsEvent(analyticsEventId);
	}

	/**
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.model.PersistedModel deletePersistedModel(
		com.liferay.portal.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _analyticsEventLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _analyticsEventLocalService.dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _analyticsEventLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {
		return _analyticsEventLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {
		return _analyticsEventLocalService.dynamicQuery(dynamicQuery, start,
			end, orderByComparator);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows that match the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _analyticsEventLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows that match the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {
		return _analyticsEventLocalService.dynamicQueryCount(dynamicQuery,
			projection);
	}

	@Override
	public com.liferay.analytics.model.AnalyticsEvent fetchAnalyticsEvent(
		long analyticsEventId) {
		return _analyticsEventLocalService.fetchAnalyticsEvent(analyticsEventId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return _analyticsEventLocalService.getActionableDynamicQuery();
	}

	/**
	* Returns the analytics event with the primary key.
	*
	* @param analyticsEventId the primary key of the analytics event
	* @return the analytics event
	* @throws PortalException if a analytics event with the primary key could not be found
	*/
	@Override
	public com.liferay.analytics.model.AnalyticsEvent getAnalyticsEvent(
		long analyticsEventId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _analyticsEventLocalService.getAnalyticsEvent(analyticsEventId);
	}

	/**
	* Returns a range of all the analytics events.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @return the range of analytics events
	*/
	@Override
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> getAnalyticsEvents(
		int start, int end) {
		return _analyticsEventLocalService.getAnalyticsEvents(start, end);
	}

	/**
	* Returns the number of analytics events.
	*
	* @return the number of analytics events
	*/
	@Override
	public int getAnalyticsEventsCount() {
		return _analyticsEventLocalService.getAnalyticsEventsCount();
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _analyticsEventLocalService.getBeanIdentifier();
	}

	@Override
	public com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _analyticsEventLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_analyticsEventLocalService.setBeanIdentifier(beanIdentifier);
	}

	/**
	* Updates the analytics event in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param analyticsEvent the analytics event
	* @return the analytics event that was updated
	*/
	@Override
	public com.liferay.analytics.model.AnalyticsEvent updateAnalyticsEvent(
		com.liferay.analytics.model.AnalyticsEvent analyticsEvent) {
		return _analyticsEventLocalService.updateAnalyticsEvent(analyticsEvent);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	@Deprecated
	public AnalyticsEventLocalService getWrappedAnalyticsEventLocalService() {
		return _analyticsEventLocalService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	@Deprecated
	public void setWrappedAnalyticsEventLocalService(
		AnalyticsEventLocalService analyticsEventLocalService) {
		_analyticsEventLocalService = analyticsEventLocalService;
	}

	@Override
	public AnalyticsEventLocalService getWrappedService() {
		return _analyticsEventLocalService;
	}

	@Override
	public void setWrappedService(
		AnalyticsEventLocalService analyticsEventLocalService) {
		_analyticsEventLocalService = analyticsEventLocalService;
	}

	private AnalyticsEventLocalService _analyticsEventLocalService;
}
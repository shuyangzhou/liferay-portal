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

package com.liferay.analytics.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.analytics.model.AnalyticsEvent;

import com.liferay.portal.service.persistence.BasePersistence;

/**
 * The persistence interface for the analytics event service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AnalyticsEventPersistenceImpl
 * @see AnalyticsEventUtil
 * @generated
 */
@ProviderType
public interface AnalyticsEventPersistence extends BasePersistence<AnalyticsEvent> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link AnalyticsEventUtil} to access the analytics event persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns all the analytics events where companyId = &#63;.
	*
	* @param companyId the company ID
	* @return the matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByCompanyId(
		long companyId);

	/**
	* Returns a range of all the analytics events where companyId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @return the range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByCompanyId(
		long companyId, int start, int end);

	/**
	* Returns an ordered range of all the analytics events where companyId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the first analytics event in the ordered set where companyId = &#63;.
	*
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByCompanyId_First(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the first analytics event in the ordered set where companyId = &#63;.
	*
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByCompanyId_First(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the last analytics event in the ordered set where companyId = &#63;.
	*
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByCompanyId_Last(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the last analytics event in the ordered set where companyId = &#63;.
	*
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByCompanyId_Last(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the analytics events before and after the current analytics event in the ordered set where companyId = &#63;.
	*
	* @param analyticsEventId the primary key of the current analytics event
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent[] findByCompanyId_PrevAndNext(
		long analyticsEventId, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Removes all the analytics events where companyId = &#63; from the database.
	*
	* @param companyId the company ID
	*/
	public void removeByCompanyId(long companyId);

	/**
	* Returns the number of analytics events where companyId = &#63;.
	*
	* @param companyId the company ID
	* @return the number of matching analytics events
	*/
	public int countByCompanyId(long companyId);

	/**
	* Returns all the analytics events where companyId = &#63; and createDate &gt; &#63;.
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @return the matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByC_GtCD(
		long companyId, java.util.Date createDate);

	/**
	* Returns a range of all the analytics events where companyId = &#63; and createDate &gt; &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @return the range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByC_GtCD(
		long companyId, java.util.Date createDate, int start, int end);

	/**
	* Returns an ordered range of all the analytics events where companyId = &#63; and createDate &gt; &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByC_GtCD(
		long companyId, java.util.Date createDate, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the first analytics event in the ordered set where companyId = &#63; and createDate &gt; &#63;.
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByC_GtCD_First(
		long companyId, java.util.Date createDate,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the first analytics event in the ordered set where companyId = &#63; and createDate &gt; &#63;.
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByC_GtCD_First(
		long companyId, java.util.Date createDate,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the last analytics event in the ordered set where companyId = &#63; and createDate &gt; &#63;.
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByC_GtCD_Last(
		long companyId, java.util.Date createDate,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the last analytics event in the ordered set where companyId = &#63; and createDate &gt; &#63;.
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByC_GtCD_Last(
		long companyId, java.util.Date createDate,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the analytics events before and after the current analytics event in the ordered set where companyId = &#63; and createDate &gt; &#63;.
	*
	* @param analyticsEventId the primary key of the current analytics event
	* @param companyId the company ID
	* @param createDate the create date
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent[] findByC_GtCD_PrevAndNext(
		long analyticsEventId, long companyId, java.util.Date createDate,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Removes all the analytics events where companyId = &#63; and createDate &gt; &#63; from the database.
	*
	* @param companyId the company ID
	* @param createDate the create date
	*/
	public void removeByC_GtCD(long companyId, java.util.Date createDate);

	/**
	* Returns the number of analytics events where companyId = &#63; and createDate &gt; &#63;.
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @return the number of matching analytics events
	*/
	public int countByC_GtCD(long companyId, java.util.Date createDate);

	/**
	* Returns all the analytics events where companyId = &#63; and createDate &lt; &#63;.
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @return the matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByC_LtCD(
		long companyId, java.util.Date createDate);

	/**
	* Returns a range of all the analytics events where companyId = &#63; and createDate &lt; &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @return the range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByC_LtCD(
		long companyId, java.util.Date createDate, int start, int end);

	/**
	* Returns an ordered range of all the analytics events where companyId = &#63; and createDate &lt; &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByC_LtCD(
		long companyId, java.util.Date createDate, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the first analytics event in the ordered set where companyId = &#63; and createDate &lt; &#63;.
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByC_LtCD_First(
		long companyId, java.util.Date createDate,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the first analytics event in the ordered set where companyId = &#63; and createDate &lt; &#63;.
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByC_LtCD_First(
		long companyId, java.util.Date createDate,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the last analytics event in the ordered set where companyId = &#63; and createDate &lt; &#63;.
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByC_LtCD_Last(
		long companyId, java.util.Date createDate,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the last analytics event in the ordered set where companyId = &#63; and createDate &lt; &#63;.
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByC_LtCD_Last(
		long companyId, java.util.Date createDate,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the analytics events before and after the current analytics event in the ordered set where companyId = &#63; and createDate &lt; &#63;.
	*
	* @param analyticsEventId the primary key of the current analytics event
	* @param companyId the company ID
	* @param createDate the create date
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent[] findByC_LtCD_PrevAndNext(
		long analyticsEventId, long companyId, java.util.Date createDate,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Removes all the analytics events where companyId = &#63; and createDate &lt; &#63; from the database.
	*
	* @param companyId the company ID
	* @param createDate the create date
	*/
	public void removeByC_LtCD(long companyId, java.util.Date createDate);

	/**
	* Returns the number of analytics events where companyId = &#63; and createDate &lt; &#63;.
	*
	* @param companyId the company ID
	* @param createDate the create date
	* @return the number of matching analytics events
	*/
	public int countByC_LtCD(long companyId, java.util.Date createDate);

	/**
	* Returns all the analytics events where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param elementKey the element key
	* @param type the type
	* @return the matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByGtCD_E_E(
		java.util.Date createDate, java.lang.String elementKey,
		java.lang.String type);

	/**
	* Returns a range of all the analytics events where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param createDate the create date
	* @param elementKey the element key
	* @param type the type
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @return the range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByGtCD_E_E(
		java.util.Date createDate, java.lang.String elementKey,
		java.lang.String type, int start, int end);

	/**
	* Returns an ordered range of all the analytics events where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param createDate the create date
	* @param elementKey the element key
	* @param type the type
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByGtCD_E_E(
		java.util.Date createDate, java.lang.String elementKey,
		java.lang.String type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the first analytics event in the ordered set where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param elementKey the element key
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByGtCD_E_E_First(
		java.util.Date createDate, java.lang.String elementKey,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the first analytics event in the ordered set where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param elementKey the element key
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByGtCD_E_E_First(
		java.util.Date createDate, java.lang.String elementKey,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the last analytics event in the ordered set where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param elementKey the element key
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByGtCD_E_E_Last(
		java.util.Date createDate, java.lang.String elementKey,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the last analytics event in the ordered set where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param elementKey the element key
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByGtCD_E_E_Last(
		java.util.Date createDate, java.lang.String elementKey,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the analytics events before and after the current analytics event in the ordered set where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param analyticsEventId the primary key of the current analytics event
	* @param createDate the create date
	* @param elementKey the element key
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent[] findByGtCD_E_E_PrevAndNext(
		long analyticsEventId, java.util.Date createDate,
		java.lang.String elementKey, java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Removes all the analytics events where createDate &gt; &#63; and elementKey = &#63; and type = &#63; from the database.
	*
	* @param createDate the create date
	* @param elementKey the element key
	* @param type the type
	*/
	public void removeByGtCD_E_E(java.util.Date createDate,
		java.lang.String elementKey, java.lang.String type);

	/**
	* Returns the number of analytics events where createDate &gt; &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param elementKey the element key
	* @param type the type
	* @return the number of matching analytics events
	*/
	public int countByGtCD_E_E(java.util.Date createDate,
		java.lang.String elementKey, java.lang.String type);

	/**
	* Returns all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param type the type
	* @return the matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByGtCD_C_C_E(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String type);

	/**
	* Returns a range of all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param type the type
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @return the range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByGtCD_C_C_E(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String type, int start, int end);

	/**
	* Returns an ordered range of all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param type the type
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByGtCD_C_C_E(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the first analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByGtCD_C_C_E_First(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the first analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByGtCD_C_C_E_First(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the last analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByGtCD_C_C_E_Last(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the last analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByGtCD_C_C_E_Last(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the analytics events before and after the current analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	*
	* @param analyticsEventId the primary key of the current analytics event
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent[] findByGtCD_C_C_E_PrevAndNext(
		long analyticsEventId, java.util.Date createDate,
		java.lang.String className, long classPK, java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Removes all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63; from the database.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param type the type
	*/
	public void removeByGtCD_C_C_E(java.util.Date createDate,
		java.lang.String className, long classPK, java.lang.String type);

	/**
	* Returns the number of analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param type the type
	* @return the number of matching analytics events
	*/
	public int countByGtCD_C_C_E(java.util.Date createDate,
		java.lang.String className, long classPK, java.lang.String type);

	/**
	* Returns all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param type the type
	* @return the matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByGtCD_C_C_R_R_E(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String referrerClassName, long referrerClassPK,
		java.lang.String type);

	/**
	* Returns a range of all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param type the type
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @return the range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByGtCD_C_C_R_R_E(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String referrerClassName, long referrerClassPK,
		java.lang.String type, int start, int end);

	/**
	* Returns an ordered range of all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param type the type
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByGtCD_C_C_R_R_E(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String referrerClassName, long referrerClassPK,
		java.lang.String type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the first analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByGtCD_C_C_R_R_E_First(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String referrerClassName, long referrerClassPK,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the first analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByGtCD_C_C_R_R_E_First(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String referrerClassName, long referrerClassPK,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the last analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByGtCD_C_C_R_R_E_Last(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String referrerClassName, long referrerClassPK,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the last analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByGtCD_C_C_R_R_E_Last(
		java.util.Date createDate, java.lang.String className, long classPK,
		java.lang.String referrerClassName, long referrerClassPK,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the analytics events before and after the current analytics event in the ordered set where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	*
	* @param analyticsEventId the primary key of the current analytics event
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent[] findByGtCD_C_C_R_R_E_PrevAndNext(
		long analyticsEventId, java.util.Date createDate,
		java.lang.String className, long classPK,
		java.lang.String referrerClassName, long referrerClassPK,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Removes all the analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63; from the database.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param type the type
	*/
	public void removeByGtCD_C_C_R_R_E(java.util.Date createDate,
		java.lang.String className, long classPK,
		java.lang.String referrerClassName, long referrerClassPK,
		java.lang.String type);

	/**
	* Returns the number of analytics events where createDate &gt; &#63; and className = &#63; and classPK = &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param className the class name
	* @param classPK the class p k
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param type the type
	* @return the number of matching analytics events
	*/
	public int countByGtCD_C_C_R_R_E(java.util.Date createDate,
		java.lang.String className, long classPK,
		java.lang.String referrerClassName, long referrerClassPK,
		java.lang.String type);

	/**
	* Returns all the analytics events where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param elementKey the element key
	* @param type the type
	* @return the matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByGtCD_R_R_E_E(
		java.util.Date createDate, java.lang.String referrerClassName,
		long referrerClassPK, java.lang.String elementKey, java.lang.String type);

	/**
	* Returns a range of all the analytics events where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param createDate the create date
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param elementKey the element key
	* @param type the type
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @return the range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByGtCD_R_R_E_E(
		java.util.Date createDate, java.lang.String referrerClassName,
		long referrerClassPK, java.lang.String elementKey,
		java.lang.String type, int start, int end);

	/**
	* Returns an ordered range of all the analytics events where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param createDate the create date
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param elementKey the element key
	* @param type the type
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findByGtCD_R_R_E_E(
		java.util.Date createDate, java.lang.String referrerClassName,
		long referrerClassPK, java.lang.String elementKey,
		java.lang.String type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the first analytics event in the ordered set where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param elementKey the element key
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByGtCD_R_R_E_E_First(
		java.util.Date createDate, java.lang.String referrerClassName,
		long referrerClassPK, java.lang.String elementKey,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the first analytics event in the ordered set where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param elementKey the element key
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByGtCD_R_R_E_E_First(
		java.util.Date createDate, java.lang.String referrerClassName,
		long referrerClassPK, java.lang.String elementKey,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the last analytics event in the ordered set where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param elementKey the element key
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByGtCD_R_R_E_E_Last(
		java.util.Date createDate, java.lang.String referrerClassName,
		long referrerClassPK, java.lang.String elementKey,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the last analytics event in the ordered set where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param elementKey the element key
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching analytics event, or <code>null</code> if a matching analytics event could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByGtCD_R_R_E_E_Last(
		java.util.Date createDate, java.lang.String referrerClassName,
		long referrerClassPK, java.lang.String elementKey,
		java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Returns the analytics events before and after the current analytics event in the ordered set where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param analyticsEventId the primary key of the current analytics event
	* @param createDate the create date
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param elementKey the element key
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent[] findByGtCD_R_R_E_E_PrevAndNext(
		long analyticsEventId, java.util.Date createDate,
		java.lang.String referrerClassName, long referrerClassPK,
		java.lang.String elementKey, java.lang.String type,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Removes all the analytics events where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63; from the database.
	*
	* @param createDate the create date
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param elementKey the element key
	* @param type the type
	*/
	public void removeByGtCD_R_R_E_E(java.util.Date createDate,
		java.lang.String referrerClassName, long referrerClassPK,
		java.lang.String elementKey, java.lang.String type);

	/**
	* Returns the number of analytics events where createDate &gt; &#63; and referrerClassName = &#63; and referrerClassPK = &#63; and elementKey = &#63; and type = &#63;.
	*
	* @param createDate the create date
	* @param referrerClassName the referrer class name
	* @param referrerClassPK the referrer class p k
	* @param elementKey the element key
	* @param type the type
	* @return the number of matching analytics events
	*/
	public int countByGtCD_R_R_E_E(java.util.Date createDate,
		java.lang.String referrerClassName, long referrerClassPK,
		java.lang.String elementKey, java.lang.String type);

	/**
	* Caches the analytics event in the entity cache if it is enabled.
	*
	* @param analyticsEvent the analytics event
	*/
	public void cacheResult(
		com.liferay.analytics.model.AnalyticsEvent analyticsEvent);

	/**
	* Caches the analytics events in the entity cache if it is enabled.
	*
	* @param analyticsEvents the analytics events
	*/
	public void cacheResult(
		java.util.List<com.liferay.analytics.model.AnalyticsEvent> analyticsEvents);

	/**
	* Creates a new analytics event with the primary key. Does not add the analytics event to the database.
	*
	* @param analyticsEventId the primary key for the new analytics event
	* @return the new analytics event
	*/
	public com.liferay.analytics.model.AnalyticsEvent create(
		long analyticsEventId);

	/**
	* Removes the analytics event with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param analyticsEventId the primary key of the analytics event
	* @return the analytics event that was removed
	* @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent remove(
		long analyticsEventId)
		throws com.liferay.analytics.NoSuchEventException;

	public com.liferay.analytics.model.AnalyticsEvent updateImpl(
		com.liferay.analytics.model.AnalyticsEvent analyticsEvent);

	/**
	* Returns the analytics event with the primary key or throws a {@link com.liferay.analytics.NoSuchEventException} if it could not be found.
	*
	* @param analyticsEventId the primary key of the analytics event
	* @return the analytics event
	* @throws com.liferay.analytics.NoSuchEventException if a analytics event with the primary key could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent findByPrimaryKey(
		long analyticsEventId)
		throws com.liferay.analytics.NoSuchEventException;

	/**
	* Returns the analytics event with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param analyticsEventId the primary key of the analytics event
	* @return the analytics event, or <code>null</code> if a analytics event with the primary key could not be found
	*/
	public com.liferay.analytics.model.AnalyticsEvent fetchByPrimaryKey(
		long analyticsEventId);

	@Override
	public java.util.Map<java.io.Serializable, com.liferay.analytics.model.AnalyticsEvent> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the analytics events.
	*
	* @return the analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findAll();

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
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findAll(
		int start, int end);

	/**
	* Returns an ordered range of all the analytics events.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.analytics.model.impl.AnalyticsEventModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of analytics events
	* @param end the upper bound of the range of analytics events (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of analytics events
	*/
	public java.util.List<com.liferay.analytics.model.AnalyticsEvent> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.analytics.model.AnalyticsEvent> orderByComparator);

	/**
	* Removes all the analytics events from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of analytics events.
	*
	* @return the number of analytics events
	*/
	public int countAll();
}
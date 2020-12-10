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

package com.liferay.portal.security.audit.storage.service;

/**
 * Provides the remote service utility for AuditEvent. This utility wraps
 * <code>com.liferay.portal.security.audit.storage.service.impl.AuditEventServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see AuditEventService
 * @generated
 */
public class AuditEventServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.security.audit.storage.service.impl.AuditEventServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static java.util.List
		<com.liferay.portal.security.audit.storage.model.AuditEvent>
				getAuditEvents(long companyId, int start, int end)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAuditEvents(companyId, start, end);
	}

	public static java.util.List
		<com.liferay.portal.security.audit.storage.model.AuditEvent>
				getAuditEvents(
					long companyId, int start, int end,
					com.liferay.portal.kernel.util.OrderByComparator
						<com.liferay.portal.security.audit.storage.model.
							AuditEvent> orderByComparator)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAuditEvents(
			companyId, start, end, orderByComparator);
	}

	public static java.util.List
		<com.liferay.portal.security.audit.storage.model.AuditEvent>
				getAuditEvents(
					long companyId, long userId, java.lang.String userName,
					java.util.Date createDateGT, java.util.Date createDateLT,
					java.lang.String eventType, java.lang.String className,
					java.lang.String classPK, java.lang.String clientHost,
					java.lang.String clientIP, java.lang.String serverName,
					int serverPort, java.lang.String sessionID,
					boolean andSearch, int start, int end)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAuditEvents(
			companyId, userId, userName, createDateGT, createDateLT, eventType,
			className, classPK, clientHost, clientIP, serverName, serverPort,
			sessionID, andSearch, start, end);
	}

	public static java.util.List
		<com.liferay.portal.security.audit.storage.model.AuditEvent>
				getAuditEvents(
					long companyId, long userId, java.lang.String userName,
					java.util.Date createDateGT, java.util.Date createDateLT,
					java.lang.String eventType, java.lang.String className,
					java.lang.String classPK, java.lang.String clientHost,
					java.lang.String clientIP, java.lang.String serverName,
					int serverPort, java.lang.String sessionID,
					boolean andSearch, int start, int end,
					com.liferay.portal.kernel.util.OrderByComparator
						<com.liferay.portal.security.audit.storage.model.
							AuditEvent> orderByComparator)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAuditEvents(
			companyId, userId, userName, createDateGT, createDateLT, eventType,
			className, classPK, clientHost, clientIP, serverName, serverPort,
			sessionID, andSearch, start, end, orderByComparator);
	}

	public static int getAuditEventsCount(long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAuditEventsCount(companyId);
	}

	public static int getAuditEventsCount(
			long companyId, long userId, java.lang.String userName,
			java.util.Date createDateGT, java.util.Date createDateLT,
			java.lang.String eventType, java.lang.String className,
			java.lang.String classPK, java.lang.String clientHost,
			java.lang.String clientIP, java.lang.String serverName,
			int serverPort, java.lang.String sessionID, boolean andSearch)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAuditEventsCount(
			companyId, userId, userName, createDateGT, createDateLT, eventType,
			className, classPK, clientHost, clientIP, serverName, serverPort,
			sessionID, andSearch);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static java.lang.String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static AuditEventService getService() {
		return _auditEventService;
	}

	private static volatile AuditEventService _auditEventService;

}
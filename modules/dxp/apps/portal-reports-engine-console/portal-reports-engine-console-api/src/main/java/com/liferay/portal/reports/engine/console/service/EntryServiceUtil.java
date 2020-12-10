/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.portal.reports.engine.console.service;

/**
 * Provides the remote service utility for Entry. This utility wraps
 * <code>com.liferay.portal.reports.engine.console.service.impl.EntryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see EntryService
 * @generated
 */
public class EntryServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.reports.engine.console.service.impl.EntryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portal.reports.engine.console.model.Entry
			addEntry(
				long groupId, long definitionId, java.lang.String format,
				boolean schedulerRequest, java.util.Date startDate,
				java.util.Date endDate, boolean repeating,
				java.lang.String recurrence,
				java.lang.String emailNotifications,
				java.lang.String emailDelivery, java.lang.String portletId,
				java.lang.String pageURL, java.lang.String reportName,
				java.lang.String reportParameters,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addEntry(
			groupId, definitionId, format, schedulerRequest, startDate, endDate,
			repeating, recurrence, emailNotifications, emailDelivery, portletId,
			pageURL, reportName, reportParameters, serviceContext);
	}

	public static void deleteAttachment(
			long companyId, long entryId, java.lang.String fileName)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteAttachment(companyId, entryId, fileName);
	}

	public static com.liferay.portal.reports.engine.console.model.Entry
			deleteEntry(long entryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteEntry(entryId);
	}

	public static java.util.List
		<com.liferay.portal.reports.engine.console.model.Entry> getEntries(
				long groupId, java.lang.String definitionName,
				java.lang.String userName, java.util.Date createDateGT,
				java.util.Date createDateLT, boolean andSearch, int start,
				int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.portal.reports.engine.console.model.Entry>
						orderByComparator)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getEntries(
			groupId, definitionName, userName, createDateGT, createDateLT,
			andSearch, start, end, orderByComparator);
	}

	public static int getEntriesCount(
			long groupId, java.lang.String definitionName,
			java.lang.String userName, java.util.Date createDateGT,
			java.util.Date createDateLT, boolean andSearch)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getEntriesCount(
			groupId, definitionName, userName, createDateGT, createDateLT,
			andSearch);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static java.lang.String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static void sendEmails(
			long entryId, java.lang.String fileName,
			java.lang.String[] emailAddresses, boolean notification)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().sendEmails(
			entryId, fileName, emailAddresses, notification);
	}

	public static void unscheduleEntry(long entryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().unscheduleEntry(entryId);
	}

	public static EntryService getService() {
		return _entryService;
	}

	private static volatile EntryService _entryService;

}
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

package com.liferay.commerce.data.integration.service;

/**
 * Provides the remote service utility for CommerceDataIntegrationProcessLog. This utility wraps
 * <code>com.liferay.commerce.data.integration.service.impl.CommerceDataIntegrationProcessLogServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Alessio Antonio Rendina
 * @see CommerceDataIntegrationProcessLogService
 * @generated
 */
public class CommerceDataIntegrationProcessLogServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.data.integration.service.impl.CommerceDataIntegrationProcessLogServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.commerce.data.integration.model.
		CommerceDataIntegrationProcessLog addCommerceDataIntegrationProcessLog(
				long userId, long commerceDataIntegrationProcessId,
				java.lang.String error, java.lang.String output, int status,
				java.util.Date startDate, java.util.Date endDate)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addCommerceDataIntegrationProcessLog(
			userId, commerceDataIntegrationProcessId, error, output, status,
			startDate, endDate);
	}

	public static void deleteCommerceDataIntegrationProcessLog(
			long cDataIntegrationProcessLogId)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteCommerceDataIntegrationProcessLog(
			cDataIntegrationProcessLogId);
	}

	public static com.liferay.commerce.data.integration.model.
		CommerceDataIntegrationProcessLog getCommerceDataIntegrationProcessLog(
				long cDataIntegrationProcessLogId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCommerceDataIntegrationProcessLog(
			cDataIntegrationProcessLogId);
	}

	public static java.util.List
		<com.liferay.commerce.data.integration.model.
			CommerceDataIntegrationProcessLog>
					getCommerceDataIntegrationProcessLogs(
						long commerceDataIntegrationProcessId, int start,
						int end)
				throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCommerceDataIntegrationProcessLogs(
			commerceDataIntegrationProcessId, start, end);
	}

	public static int getCommerceDataIntegrationProcessLogsCount(
			long commerceDataIntegrationProcessId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCommerceDataIntegrationProcessLogsCount(
			commerceDataIntegrationProcessId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static java.lang.String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.commerce.data.integration.model.
		CommerceDataIntegrationProcessLog
				updateCommerceDataIntegrationProcessLog(
					long cDataIntegrationProcessLogId, java.lang.String error,
					java.lang.String output, int status, java.util.Date endDate)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateCommerceDataIntegrationProcessLog(
			cDataIntegrationProcessLogId, error, output, status, endDate);
	}

	public static CommerceDataIntegrationProcessLogService getService() {
		return _commerceDataIntegrationProcessLogService;
	}

	private static volatile CommerceDataIntegrationProcessLogService
		_commerceDataIntegrationProcessLogService;

}
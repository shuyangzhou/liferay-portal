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
 * Provides the remote service utility for Definition. This utility wraps
 * <code>com.liferay.portal.reports.engine.console.service.impl.DefinitionServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see DefinitionService
 * @generated
 */
public class DefinitionServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.reports.engine.console.service.impl.DefinitionServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portal.reports.engine.console.model.Definition
			addDefinition(
				long groupId,
				java.util.Map<java.util.Locale, java.lang.String> nameMap,
				java.util.Map<java.util.Locale, java.lang.String>
					descriptionMap,
				long sourceId, java.lang.String reportParameters,
				java.lang.String fileName, java.io.InputStream inputStream,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addDefinition(
			groupId, nameMap, descriptionMap, sourceId, reportParameters,
			fileName, inputStream, serviceContext);
	}

	public static com.liferay.portal.reports.engine.console.model.Definition
			deleteDefinition(long definitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteDefinition(definitionId);
	}

	public static com.liferay.portal.reports.engine.console.model.Definition
			getDefinition(long definitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getDefinition(definitionId);
	}

	public static java.util.List
		<com.liferay.portal.reports.engine.console.model.Definition>
				getDefinitions(
					long groupId, java.lang.String definitionName,
					java.lang.String description, java.lang.String sourceId,
					java.lang.String reportName, boolean andSearch, int start,
					int end,
					com.liferay.portal.kernel.util.OrderByComparator
						<com.liferay.portal.reports.engine.console.model.
							Definition> orderByComparator)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getDefinitions(
			groupId, definitionName, description, sourceId, reportName,
			andSearch, start, end, orderByComparator);
	}

	public static int getDefinitionsCount(
		long groupId, java.lang.String definitionName,
		java.lang.String description, java.lang.String sourceId,
		java.lang.String reportName, boolean andSearch) {

		return getService().getDefinitionsCount(
			groupId, definitionName, description, sourceId, reportName,
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

	public static com.liferay.portal.reports.engine.console.model.Definition
			updateDefinition(
				long definitionId,
				java.util.Map<java.util.Locale, java.lang.String> nameMap,
				java.util.Map<java.util.Locale, java.lang.String>
					descriptionMap,
				long sourceId, java.lang.String reportParameters,
				java.lang.String fileName, java.io.InputStream inputStream,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateDefinition(
			definitionId, nameMap, descriptionMap, sourceId, reportParameters,
			fileName, inputStream, serviceContext);
	}

	public static DefinitionService getService() {
		return _definitionService;
	}

	private static volatile DefinitionService _definitionService;

}
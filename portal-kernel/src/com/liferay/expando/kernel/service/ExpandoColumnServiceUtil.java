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

package com.liferay.expando.kernel.service;

/**
 * Provides the remote service utility for ExpandoColumn. This utility wraps
 * <code>com.liferay.portlet.expando.service.impl.ExpandoColumnServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see ExpandoColumnService
 * @generated
 */
public class ExpandoColumnServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portlet.expando.service.impl.ExpandoColumnServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.expando.kernel.model.ExpandoColumn addColumn(
			long tableId, java.lang.String name, int type)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addColumn(tableId, name, type);
	}

	public static com.liferay.expando.kernel.model.ExpandoColumn addColumn(
			long tableId, java.lang.String name, int type,
			java.lang.Object defaultData)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addColumn(tableId, name, type, defaultData);
	}

	public static void deleteColumn(long columnId)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteColumn(columnId);
	}

	public static com.liferay.expando.kernel.model.ExpandoColumn
			fetchExpandoColumn(long columnId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().fetchExpandoColumn(columnId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static java.lang.String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.expando.kernel.model.ExpandoColumn updateColumn(
			long columnId, java.lang.String name, int type)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateColumn(columnId, name, type);
	}

	public static com.liferay.expando.kernel.model.ExpandoColumn updateColumn(
			long columnId, java.lang.String name, int type,
			java.lang.Object defaultData)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateColumn(columnId, name, type, defaultData);
	}

	public static com.liferay.expando.kernel.model.ExpandoColumn
			updateTypeSettings(long columnId, java.lang.String typeSettings)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateTypeSettings(columnId, typeSettings);
	}

	public static ExpandoColumnService getService() {
		return _expandoColumnService;
	}

	private static volatile ExpandoColumnService _expandoColumnService;

}
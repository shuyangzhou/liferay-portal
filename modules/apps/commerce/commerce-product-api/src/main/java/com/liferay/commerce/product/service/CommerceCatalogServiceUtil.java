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

package com.liferay.commerce.product.service;

import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;

/**
 * Provides the remote service utility for CommerceCatalog. This utility wraps
 * <code>com.liferay.commerce.product.service.impl.CommerceCatalogServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Marco Leo
 * @see CommerceCatalogService
 * @generated
 */
public class CommerceCatalogServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.product.service.impl.CommerceCatalogServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static CommerceCatalog addCommerceCatalog(
			String name, String commerceCurrencyCode,
			String catalogDefaultLanguageId, String externalReferenceCode,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addCommerceCatalog(
			name, commerceCurrencyCode, catalogDefaultLanguageId,
			externalReferenceCode, serviceContext);
	}

	public static CommerceCatalog deleteCommerceCatalog(long commerceCatalogId)
		throws PortalException {

		return getService().deleteCommerceCatalog(commerceCatalogId);
	}

	public static CommerceCatalog fetchByExternalReferenceCode(
			long companyId, String externalReferenceCode)
		throws PortalException {

		return getService().fetchByExternalReferenceCode(
			companyId, externalReferenceCode);
	}

	public static CommerceCatalog fetchCommerceCatalog(long commerceCatalogId)
		throws PortalException {

		return getService().fetchCommerceCatalog(commerceCatalogId);
	}

	public static CommerceCatalog fetchCommerceCatalogByGroupId(long groupId)
		throws PortalException {

		return getService().fetchCommerceCatalogByGroupId(groupId);
	}

	public static CommerceCatalog getCommerceCatalog(long commerceCatalogId)
		throws PortalException {

		return getService().getCommerceCatalog(commerceCatalogId);
	}

	public static List<CommerceCatalog> getCommerceCatalogs(
		long companyId, int start, int end) {

		return getService().getCommerceCatalogs(companyId, start, end);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static List<CommerceCatalog> searchCommerceCatalogs(
			long companyId, String keywords, int start, int end,
			com.liferay.portal.kernel.search.Sort sort)
		throws PortalException {

		return getService().searchCommerceCatalogs(
			companyId, keywords, start, end, sort);
	}

	public static int searchCommerceCatalogsCount(
			long companyId, String keywords)
		throws PortalException {

		return getService().searchCommerceCatalogsCount(companyId, keywords);
	}

	public static CommerceCatalog updateCommerceCatalog(
			long commerceCatalogId, String name, String commerceCurrencyCode,
			String catalogDefaultLanguageId)
		throws PortalException {

		return getService().updateCommerceCatalog(
			commerceCatalogId, name, commerceCurrencyCode,
			catalogDefaultLanguageId);
	}

	public static CommerceCatalog updateCommerceCatalogExternalReferenceCode(
			long commerceCatalogId, String externalReferenceCode)
		throws PortalException {

		return getService().updateCommerceCatalogExternalReferenceCode(
			commerceCatalogId, externalReferenceCode);
	}

	public static CommerceCatalogService getService() {
		return _service;
	}

	private static volatile CommerceCatalogService _service;

}
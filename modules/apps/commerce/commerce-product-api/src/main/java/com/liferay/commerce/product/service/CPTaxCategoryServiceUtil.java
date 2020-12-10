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

/**
 * Provides the remote service utility for CPTaxCategory. This utility wraps
 * <code>com.liferay.commerce.product.service.impl.CPTaxCategoryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Marco Leo
 * @see CPTaxCategoryService
 * @generated
 */
public class CPTaxCategoryServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.product.service.impl.CPTaxCategoryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 #addCPTaxCategory(String, Map, Map, ServiceContext)}
	 */
	@Deprecated
	public static com.liferay.commerce.product.model.CPTaxCategory
			addCPTaxCategory(
				java.util.Map<java.util.Locale, java.lang.String> nameMap,
				java.util.Map<java.util.Locale, java.lang.String>
					descriptionMap,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addCPTaxCategory(
			nameMap, descriptionMap, serviceContext);
	}

	public static com.liferay.commerce.product.model.CPTaxCategory
			addCPTaxCategory(
				java.lang.String externalReferenceCode,
				java.util.Map<java.util.Locale, java.lang.String> nameMap,
				java.util.Map<java.util.Locale, java.lang.String>
					descriptionMap,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addCPTaxCategory(
			externalReferenceCode, nameMap, descriptionMap, serviceContext);
	}

	public static int countCPTaxCategoriesByCompanyId(
			long companyId, java.lang.String keyword)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().countCPTaxCategoriesByCompanyId(companyId, keyword);
	}

	public static void deleteCPTaxCategory(long cpTaxCategoryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteCPTaxCategory(cpTaxCategoryId);
	}

	public static java.util.List
		<com.liferay.commerce.product.model.CPTaxCategory>
				findCPTaxCategoriesByCompanyId(
					long companyId, java.lang.String keyword, int start,
					int end)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().findCPTaxCategoriesByCompanyId(
			companyId, keyword, start, end);
	}

	public static java.util.List
		<com.liferay.commerce.product.model.CPTaxCategory> getCPTaxCategories(
				long companyId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCPTaxCategories(companyId);
	}

	public static java.util.List
		<com.liferay.commerce.product.model.CPTaxCategory> getCPTaxCategories(
				long companyId, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.commerce.product.model.CPTaxCategory>
						orderByComparator)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCPTaxCategories(
			companyId, start, end, orderByComparator);
	}

	public static int getCPTaxCategoriesCount(long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCPTaxCategoriesCount(companyId);
	}

	public static com.liferay.commerce.product.model.CPTaxCategory
			getCPTaxCategory(long cpTaxCategoryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCPTaxCategory(cpTaxCategoryId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static java.lang.String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 #updateCPTaxCategory(String, long, Map, Map)}
	 */
	@Deprecated
	public static com.liferay.commerce.product.model.CPTaxCategory
			updateCPTaxCategory(
				long cpTaxCategoryId,
				java.util.Map<java.util.Locale, java.lang.String> nameMap,
				java.util.Map<java.util.Locale, java.lang.String>
					descriptionMap)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateCPTaxCategory(
			cpTaxCategoryId, nameMap, descriptionMap);
	}

	public static com.liferay.commerce.product.model.CPTaxCategory
			updateCPTaxCategory(
				java.lang.String externalReferenceCode, long cpTaxCategoryId,
				java.util.Map<java.util.Locale, java.lang.String> nameMap,
				java.util.Map<java.util.Locale, java.lang.String>
					descriptionMap)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateCPTaxCategory(
			externalReferenceCode, cpTaxCategoryId, nameMap, descriptionMap);
	}

	public static CPTaxCategoryService getService() {
		return _cpTaxCategoryService;
	}

	private static volatile CPTaxCategoryService _cpTaxCategoryService;

}
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

package com.liferay.commerce.service;

/**
 * Provides the remote service utility for CPDefinitionInventory. This utility wraps
 * <code>com.liferay.commerce.service.impl.CPDefinitionInventoryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Alessio Antonio Rendina
 * @see CPDefinitionInventoryService
 * @generated
 */
public class CPDefinitionInventoryServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.service.impl.CPDefinitionInventoryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.commerce.model.CPDefinitionInventory
			addCPDefinitionInventory(
				long userId, long cpDefinitionId,
				java.lang.String cpDefinitionInventoryEngine,
				java.lang.String lowStockActivity, boolean displayAvailability,
				boolean displayStockQuantity, int minStockQuantity,
				boolean backOrders, int minOrderQuantity, int maxOrderQuantity,
				java.lang.String allowedOrderQuantities,
				int multipleOrderQuantity)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addCPDefinitionInventory(
			userId, cpDefinitionId, cpDefinitionInventoryEngine,
			lowStockActivity, displayAvailability, displayStockQuantity,
			minStockQuantity, backOrders, minOrderQuantity, maxOrderQuantity,
			allowedOrderQuantities, multipleOrderQuantity);
	}

	public static void deleteCPDefinitionInventory(long cpDefinitionInventoryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteCPDefinitionInventory(cpDefinitionInventoryId);
	}

	public static com.liferay.commerce.model.CPDefinitionInventory
			fetchCPDefinitionInventoryByCPDefinitionId(long cpDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().fetchCPDefinitionInventoryByCPDefinitionId(
			cpDefinitionId);
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
	 * @deprecated As of Athanasius (7.3.x)
	 */
	@Deprecated
	public static com.liferay.commerce.model.CPDefinitionInventory
			updateCPDefinitionInventory(
				long groupId, long cpDefinitionInventoryId,
				java.lang.String cpDefinitionInventoryEngine,
				java.lang.String lowStockActivity, boolean displayAvailability,
				boolean displayStockQuantity, int minStockQuantity,
				boolean backOrders, int minOrderQuantity, int maxOrderQuantity,
				java.lang.String allowedOrderQuantities,
				int multipleOrderQuantity)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateCPDefinitionInventory(
			groupId, cpDefinitionInventoryId, cpDefinitionInventoryEngine,
			lowStockActivity, displayAvailability, displayStockQuantity,
			minStockQuantity, backOrders, minOrderQuantity, maxOrderQuantity,
			allowedOrderQuantities, multipleOrderQuantity);
	}

	public static com.liferay.commerce.model.CPDefinitionInventory
			updateCPDefinitionInventory(
				long cpDefinitionInventoryId,
				java.lang.String cpDefinitionInventoryEngine,
				java.lang.String lowStockActivity, boolean displayAvailability,
				boolean displayStockQuantity, int minStockQuantity,
				boolean backOrders, int minOrderQuantity, int maxOrderQuantity,
				java.lang.String allowedOrderQuantities,
				int multipleOrderQuantity)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateCPDefinitionInventory(
			cpDefinitionInventoryId, cpDefinitionInventoryEngine,
			lowStockActivity, displayAvailability, displayStockQuantity,
			minStockQuantity, backOrders, minOrderQuantity, maxOrderQuantity,
			allowedOrderQuantities, multipleOrderQuantity);
	}

	public static CPDefinitionInventoryService getService() {
		return _cpDefinitionInventoryService;
	}

	private static volatile CPDefinitionInventoryService
		_cpDefinitionInventoryService;

}
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

package com.liferay.portal.security.password.service.persistence;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.security.password.model.PasswordMeta;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the password meta service. This utility wraps <code>com.liferay.portal.security.password.service.persistence.impl.PasswordMetaPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author arthurchan35
 * @see PasswordMetaPersistence
 * @generated
 */
public class PasswordMetaUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(PasswordMeta passwordMeta) {
		getPersistence().clearCache(passwordMeta);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, PasswordMeta> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<PasswordMeta> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<PasswordMeta> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<PasswordMeta> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<PasswordMeta> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static PasswordMeta update(PasswordMeta passwordMeta) {
		return getPersistence().update(passwordMeta);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static PasswordMeta update(
		PasswordMeta passwordMeta, ServiceContext serviceContext) {

		return getPersistence().update(passwordMeta, serviceContext);
	}

	/**
	 * Returns all the password metas where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching password metas
	 */
	public static List<PasswordMeta> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
	}

	/**
	 * Returns a range of all the password metas where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @return the range of matching password metas
	 */
	public static List<PasswordMeta> findByUuid(
		String uuid, int start, int end) {

		return getPersistence().findByUuid(uuid, start, end);
	}

	/**
	 * Returns an ordered range of all the password metas where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching password metas
	 */
	public static List<PasswordMeta> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<PasswordMeta> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the password metas where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching password metas
	 */
	public static List<PasswordMeta> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<PasswordMeta> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first password meta in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password meta
	 * @throws NoSuchMetaException if a matching password meta could not be found
	 */
	public static PasswordMeta findByUuid_First(
			String uuid, OrderByComparator<PasswordMeta> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchMetaException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first password meta in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password meta, or <code>null</code> if a matching password meta could not be found
	 */
	public static PasswordMeta fetchByUuid_First(
		String uuid, OrderByComparator<PasswordMeta> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last password meta in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password meta
	 * @throws NoSuchMetaException if a matching password meta could not be found
	 */
	public static PasswordMeta findByUuid_Last(
			String uuid, OrderByComparator<PasswordMeta> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchMetaException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last password meta in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password meta, or <code>null</code> if a matching password meta could not be found
	 */
	public static PasswordMeta fetchByUuid_Last(
		String uuid, OrderByComparator<PasswordMeta> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the password metas before and after the current password meta in the ordered set where uuid = &#63;.
	 *
	 * @param metaId the primary key of the current password meta
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password meta
	 * @throws NoSuchMetaException if a password meta with the primary key could not be found
	 */
	public static PasswordMeta[] findByUuid_PrevAndNext(
			long metaId, String uuid,
			OrderByComparator<PasswordMeta> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchMetaException {

		return getPersistence().findByUuid_PrevAndNext(
			metaId, uuid, orderByComparator);
	}

	/**
	 * Removes all the password metas where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of password metas where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching password metas
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns all the password metas where passwordEntryId = &#63;.
	 *
	 * @param passwordEntryId the password entry ID
	 * @return the matching password metas
	 */
	public static List<PasswordMeta> findByPasswordEntryId(
		long passwordEntryId) {

		return getPersistence().findByPasswordEntryId(passwordEntryId);
	}

	/**
	 * Returns a range of all the password metas where passwordEntryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param passwordEntryId the password entry ID
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @return the range of matching password metas
	 */
	public static List<PasswordMeta> findByPasswordEntryId(
		long passwordEntryId, int start, int end) {

		return getPersistence().findByPasswordEntryId(
			passwordEntryId, start, end);
	}

	/**
	 * Returns an ordered range of all the password metas where passwordEntryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param passwordEntryId the password entry ID
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching password metas
	 */
	public static List<PasswordMeta> findByPasswordEntryId(
		long passwordEntryId, int start, int end,
		OrderByComparator<PasswordMeta> orderByComparator) {

		return getPersistence().findByPasswordEntryId(
			passwordEntryId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the password metas where passwordEntryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param passwordEntryId the password entry ID
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching password metas
	 */
	public static List<PasswordMeta> findByPasswordEntryId(
		long passwordEntryId, int start, int end,
		OrderByComparator<PasswordMeta> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByPasswordEntryId(
			passwordEntryId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first password meta in the ordered set where passwordEntryId = &#63;.
	 *
	 * @param passwordEntryId the password entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password meta
	 * @throws NoSuchMetaException if a matching password meta could not be found
	 */
	public static PasswordMeta findByPasswordEntryId_First(
			long passwordEntryId,
			OrderByComparator<PasswordMeta> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchMetaException {

		return getPersistence().findByPasswordEntryId_First(
			passwordEntryId, orderByComparator);
	}

	/**
	 * Returns the first password meta in the ordered set where passwordEntryId = &#63;.
	 *
	 * @param passwordEntryId the password entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password meta, or <code>null</code> if a matching password meta could not be found
	 */
	public static PasswordMeta fetchByPasswordEntryId_First(
		long passwordEntryId,
		OrderByComparator<PasswordMeta> orderByComparator) {

		return getPersistence().fetchByPasswordEntryId_First(
			passwordEntryId, orderByComparator);
	}

	/**
	 * Returns the last password meta in the ordered set where passwordEntryId = &#63;.
	 *
	 * @param passwordEntryId the password entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password meta
	 * @throws NoSuchMetaException if a matching password meta could not be found
	 */
	public static PasswordMeta findByPasswordEntryId_Last(
			long passwordEntryId,
			OrderByComparator<PasswordMeta> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchMetaException {

		return getPersistence().findByPasswordEntryId_Last(
			passwordEntryId, orderByComparator);
	}

	/**
	 * Returns the last password meta in the ordered set where passwordEntryId = &#63;.
	 *
	 * @param passwordEntryId the password entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password meta, or <code>null</code> if a matching password meta could not be found
	 */
	public static PasswordMeta fetchByPasswordEntryId_Last(
		long passwordEntryId,
		OrderByComparator<PasswordMeta> orderByComparator) {

		return getPersistence().fetchByPasswordEntryId_Last(
			passwordEntryId, orderByComparator);
	}

	/**
	 * Returns the password metas before and after the current password meta in the ordered set where passwordEntryId = &#63;.
	 *
	 * @param metaId the primary key of the current password meta
	 * @param passwordEntryId the password entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password meta
	 * @throws NoSuchMetaException if a password meta with the primary key could not be found
	 */
	public static PasswordMeta[] findByPasswordEntryId_PrevAndNext(
			long metaId, long passwordEntryId,
			OrderByComparator<PasswordMeta> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchMetaException {

		return getPersistence().findByPasswordEntryId_PrevAndNext(
			metaId, passwordEntryId, orderByComparator);
	}

	/**
	 * Removes all the password metas where passwordEntryId = &#63; from the database.
	 *
	 * @param passwordEntryId the password entry ID
	 */
	public static void removeByPasswordEntryId(long passwordEntryId) {
		getPersistence().removeByPasswordEntryId(passwordEntryId);
	}

	/**
	 * Returns the number of password metas where passwordEntryId = &#63;.
	 *
	 * @param passwordEntryId the password entry ID
	 * @return the number of matching password metas
	 */
	public static int countByPasswordEntryId(long passwordEntryId) {
		return getPersistence().countByPasswordEntryId(passwordEntryId);
	}

	/**
	 * Returns all the password metas where hashAlgorithmEntryId = &#63;.
	 *
	 * @param hashAlgorithmEntryId the hash algorithm entry ID
	 * @return the matching password metas
	 */
	public static List<PasswordMeta> findByHashAlgorithmEntryId(
		long hashAlgorithmEntryId) {

		return getPersistence().findByHashAlgorithmEntryId(
			hashAlgorithmEntryId);
	}

	/**
	 * Returns a range of all the password metas where hashAlgorithmEntryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param hashAlgorithmEntryId the hash algorithm entry ID
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @return the range of matching password metas
	 */
	public static List<PasswordMeta> findByHashAlgorithmEntryId(
		long hashAlgorithmEntryId, int start, int end) {

		return getPersistence().findByHashAlgorithmEntryId(
			hashAlgorithmEntryId, start, end);
	}

	/**
	 * Returns an ordered range of all the password metas where hashAlgorithmEntryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param hashAlgorithmEntryId the hash algorithm entry ID
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching password metas
	 */
	public static List<PasswordMeta> findByHashAlgorithmEntryId(
		long hashAlgorithmEntryId, int start, int end,
		OrderByComparator<PasswordMeta> orderByComparator) {

		return getPersistence().findByHashAlgorithmEntryId(
			hashAlgorithmEntryId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the password metas where hashAlgorithmEntryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param hashAlgorithmEntryId the hash algorithm entry ID
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching password metas
	 */
	public static List<PasswordMeta> findByHashAlgorithmEntryId(
		long hashAlgorithmEntryId, int start, int end,
		OrderByComparator<PasswordMeta> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByHashAlgorithmEntryId(
			hashAlgorithmEntryId, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first password meta in the ordered set where hashAlgorithmEntryId = &#63;.
	 *
	 * @param hashAlgorithmEntryId the hash algorithm entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password meta
	 * @throws NoSuchMetaException if a matching password meta could not be found
	 */
	public static PasswordMeta findByHashAlgorithmEntryId_First(
			long hashAlgorithmEntryId,
			OrderByComparator<PasswordMeta> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchMetaException {

		return getPersistence().findByHashAlgorithmEntryId_First(
			hashAlgorithmEntryId, orderByComparator);
	}

	/**
	 * Returns the first password meta in the ordered set where hashAlgorithmEntryId = &#63;.
	 *
	 * @param hashAlgorithmEntryId the hash algorithm entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password meta, or <code>null</code> if a matching password meta could not be found
	 */
	public static PasswordMeta fetchByHashAlgorithmEntryId_First(
		long hashAlgorithmEntryId,
		OrderByComparator<PasswordMeta> orderByComparator) {

		return getPersistence().fetchByHashAlgorithmEntryId_First(
			hashAlgorithmEntryId, orderByComparator);
	}

	/**
	 * Returns the last password meta in the ordered set where hashAlgorithmEntryId = &#63;.
	 *
	 * @param hashAlgorithmEntryId the hash algorithm entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password meta
	 * @throws NoSuchMetaException if a matching password meta could not be found
	 */
	public static PasswordMeta findByHashAlgorithmEntryId_Last(
			long hashAlgorithmEntryId,
			OrderByComparator<PasswordMeta> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchMetaException {

		return getPersistence().findByHashAlgorithmEntryId_Last(
			hashAlgorithmEntryId, orderByComparator);
	}

	/**
	 * Returns the last password meta in the ordered set where hashAlgorithmEntryId = &#63;.
	 *
	 * @param hashAlgorithmEntryId the hash algorithm entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password meta, or <code>null</code> if a matching password meta could not be found
	 */
	public static PasswordMeta fetchByHashAlgorithmEntryId_Last(
		long hashAlgorithmEntryId,
		OrderByComparator<PasswordMeta> orderByComparator) {

		return getPersistence().fetchByHashAlgorithmEntryId_Last(
			hashAlgorithmEntryId, orderByComparator);
	}

	/**
	 * Returns the password metas before and after the current password meta in the ordered set where hashAlgorithmEntryId = &#63;.
	 *
	 * @param metaId the primary key of the current password meta
	 * @param hashAlgorithmEntryId the hash algorithm entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password meta
	 * @throws NoSuchMetaException if a password meta with the primary key could not be found
	 */
	public static PasswordMeta[] findByHashAlgorithmEntryId_PrevAndNext(
			long metaId, long hashAlgorithmEntryId,
			OrderByComparator<PasswordMeta> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchMetaException {

		return getPersistence().findByHashAlgorithmEntryId_PrevAndNext(
			metaId, hashAlgorithmEntryId, orderByComparator);
	}

	/**
	 * Removes all the password metas where hashAlgorithmEntryId = &#63; from the database.
	 *
	 * @param hashAlgorithmEntryId the hash algorithm entry ID
	 */
	public static void removeByHashAlgorithmEntryId(long hashAlgorithmEntryId) {
		getPersistence().removeByHashAlgorithmEntryId(hashAlgorithmEntryId);
	}

	/**
	 * Returns the number of password metas where hashAlgorithmEntryId = &#63;.
	 *
	 * @param hashAlgorithmEntryId the hash algorithm entry ID
	 * @return the number of matching password metas
	 */
	public static int countByHashAlgorithmEntryId(long hashAlgorithmEntryId) {
		return getPersistence().countByHashAlgorithmEntryId(
			hashAlgorithmEntryId);
	}

	/**
	 * Caches the password meta in the entity cache if it is enabled.
	 *
	 * @param passwordMeta the password meta
	 */
	public static void cacheResult(PasswordMeta passwordMeta) {
		getPersistence().cacheResult(passwordMeta);
	}

	/**
	 * Caches the password metas in the entity cache if it is enabled.
	 *
	 * @param passwordMetas the password metas
	 */
	public static void cacheResult(List<PasswordMeta> passwordMetas) {
		getPersistence().cacheResult(passwordMetas);
	}

	/**
	 * Creates a new password meta with the primary key. Does not add the password meta to the database.
	 *
	 * @param metaId the primary key for the new password meta
	 * @return the new password meta
	 */
	public static PasswordMeta create(long metaId) {
		return getPersistence().create(metaId);
	}

	/**
	 * Removes the password meta with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param metaId the primary key of the password meta
	 * @return the password meta that was removed
	 * @throws NoSuchMetaException if a password meta with the primary key could not be found
	 */
	public static PasswordMeta remove(long metaId)
		throws com.liferay.portal.security.password.exception.
			NoSuchMetaException {

		return getPersistence().remove(metaId);
	}

	public static PasswordMeta updateImpl(PasswordMeta passwordMeta) {
		return getPersistence().updateImpl(passwordMeta);
	}

	/**
	 * Returns the password meta with the primary key or throws a <code>NoSuchMetaException</code> if it could not be found.
	 *
	 * @param metaId the primary key of the password meta
	 * @return the password meta
	 * @throws NoSuchMetaException if a password meta with the primary key could not be found
	 */
	public static PasswordMeta findByPrimaryKey(long metaId)
		throws com.liferay.portal.security.password.exception.
			NoSuchMetaException {

		return getPersistence().findByPrimaryKey(metaId);
	}

	/**
	 * Returns the password meta with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param metaId the primary key of the password meta
	 * @return the password meta, or <code>null</code> if a password meta with the primary key could not be found
	 */
	public static PasswordMeta fetchByPrimaryKey(long metaId) {
		return getPersistence().fetchByPrimaryKey(metaId);
	}

	/**
	 * Returns all the password metas.
	 *
	 * @return the password metas
	 */
	public static List<PasswordMeta> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the password metas.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @return the range of password metas
	 */
	public static List<PasswordMeta> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the password metas.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of password metas
	 */
	public static List<PasswordMeta> findAll(
		int start, int end, OrderByComparator<PasswordMeta> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the password metas.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of password metas
	 */
	public static List<PasswordMeta> findAll(
		int start, int end, OrderByComparator<PasswordMeta> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the password metas from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of password metas.
	 *
	 * @return the number of password metas
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static PasswordMetaPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<PasswordMetaPersistence, PasswordMetaPersistence> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(PasswordMetaPersistence.class);

		ServiceTracker<PasswordMetaPersistence, PasswordMetaPersistence>
			serviceTracker =
				new ServiceTracker
					<PasswordMetaPersistence, PasswordMetaPersistence>(
						bundle.getBundleContext(),
						PasswordMetaPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}
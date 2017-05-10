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

package com.liferay.portal.kernel.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.model.GroupLocalization;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ReferenceRegistry;

import java.util.List;

/**
 * The persistence utility for the group localization service. This utility wraps {@link com.liferay.portal.service.persistence.impl.GroupLocalizationPersistenceImpl} and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see GroupLocalizationPersistence
 * @see com.liferay.portal.service.persistence.impl.GroupLocalizationPersistenceImpl
 * @generated
 */
@ProviderType
public class GroupLocalizationUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(GroupLocalization groupLocalization) {
		getPersistence().clearCache(groupLocalization);
	}

	/**
	 * @see BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<GroupLocalization> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<GroupLocalization> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {
		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<GroupLocalization> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<GroupLocalization> orderByComparator) {
		return getPersistence()
				   .findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * @see BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static GroupLocalization update(GroupLocalization groupLocalization) {
		return getPersistence().update(groupLocalization);
	}

	/**
	 * @see BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static GroupLocalization update(
		GroupLocalization groupLocalization, ServiceContext serviceContext) {
		return getPersistence().update(groupLocalization, serviceContext);
	}

	/**
	* Returns all the group localizations where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the matching group localizations
	*/
	public static List<GroupLocalization> findByGroupId(long groupId) {
		return getPersistence().findByGroupId(groupId);
	}

	/**
	* Returns a range of all the group localizations where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link GroupLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of group localizations
	* @param end the upper bound of the range of group localizations (not inclusive)
	* @return the range of matching group localizations
	*/
	public static List<GroupLocalization> findByGroupId(long groupId,
		int start, int end) {
		return getPersistence().findByGroupId(groupId, start, end);
	}

	/**
	* Returns an ordered range of all the group localizations where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link GroupLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of group localizations
	* @param end the upper bound of the range of group localizations (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching group localizations
	*/
	public static List<GroupLocalization> findByGroupId(long groupId,
		int start, int end,
		OrderByComparator<GroupLocalization> orderByComparator) {
		return getPersistence()
				   .findByGroupId(groupId, start, end, orderByComparator);
	}

	/**
	* Returns an ordered range of all the group localizations where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link GroupLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of group localizations
	* @param end the upper bound of the range of group localizations (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of matching group localizations
	*/
	public static List<GroupLocalization> findByGroupId(long groupId,
		int start, int end,
		OrderByComparator<GroupLocalization> orderByComparator,
		boolean retrieveFromCache) {
		return getPersistence()
				   .findByGroupId(groupId, start, end, orderByComparator,
			retrieveFromCache);
	}

	/**
	* Returns the first group localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching group localization
	* @throws NoSuchGroupLocalizationException if a matching group localization could not be found
	*/
	public static GroupLocalization findByGroupId_First(long groupId,
		OrderByComparator<GroupLocalization> orderByComparator)
		throws com.liferay.portal.kernel.exception.NoSuchGroupLocalizationException {
		return getPersistence().findByGroupId_First(groupId, orderByComparator);
	}

	/**
	* Returns the first group localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching group localization, or <code>null</code> if a matching group localization could not be found
	*/
	public static GroupLocalization fetchByGroupId_First(long groupId,
		OrderByComparator<GroupLocalization> orderByComparator) {
		return getPersistence().fetchByGroupId_First(groupId, orderByComparator);
	}

	/**
	* Returns the last group localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching group localization
	* @throws NoSuchGroupLocalizationException if a matching group localization could not be found
	*/
	public static GroupLocalization findByGroupId_Last(long groupId,
		OrderByComparator<GroupLocalization> orderByComparator)
		throws com.liferay.portal.kernel.exception.NoSuchGroupLocalizationException {
		return getPersistence().findByGroupId_Last(groupId, orderByComparator);
	}

	/**
	* Returns the last group localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching group localization, or <code>null</code> if a matching group localization could not be found
	*/
	public static GroupLocalization fetchByGroupId_Last(long groupId,
		OrderByComparator<GroupLocalization> orderByComparator) {
		return getPersistence().fetchByGroupId_Last(groupId, orderByComparator);
	}

	/**
	* Returns the group localizations before and after the current group localization in the ordered set where groupId = &#63;.
	*
	* @param groupLocalizationId the primary key of the current group localization
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next group localization
	* @throws NoSuchGroupLocalizationException if a group localization with the primary key could not be found
	*/
	public static GroupLocalization[] findByGroupId_PrevAndNext(
		long groupLocalizationId, long groupId,
		OrderByComparator<GroupLocalization> orderByComparator)
		throws com.liferay.portal.kernel.exception.NoSuchGroupLocalizationException {
		return getPersistence()
				   .findByGroupId_PrevAndNext(groupLocalizationId, groupId,
			orderByComparator);
	}

	/**
	* Removes all the group localizations where groupId = &#63; from the database.
	*
	* @param groupId the group ID
	*/
	public static void removeByGroupId(long groupId) {
		getPersistence().removeByGroupId(groupId);
	}

	/**
	* Returns the number of group localizations where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the number of matching group localizations
	*/
	public static int countByGroupId(long groupId) {
		return getPersistence().countByGroupId(groupId);
	}

	/**
	* Returns the group localization where groupId = &#63; and languageId = &#63; or throws a {@link NoSuchGroupLocalizationException} if it could not be found.
	*
	* @param groupId the group ID
	* @param languageId the language ID
	* @return the matching group localization
	* @throws NoSuchGroupLocalizationException if a matching group localization could not be found
	*/
	public static GroupLocalization findByGroupId_LanguageId(long groupId,
		java.lang.String languageId)
		throws com.liferay.portal.kernel.exception.NoSuchGroupLocalizationException {
		return getPersistence().findByGroupId_LanguageId(groupId, languageId);
	}

	/**
	* Returns the group localization where groupId = &#63; and languageId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param groupId the group ID
	* @param languageId the language ID
	* @return the matching group localization, or <code>null</code> if a matching group localization could not be found
	*/
	public static GroupLocalization fetchByGroupId_LanguageId(long groupId,
		java.lang.String languageId) {
		return getPersistence().fetchByGroupId_LanguageId(groupId, languageId);
	}

	/**
	* Returns the group localization where groupId = &#63; and languageId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param groupId the group ID
	* @param languageId the language ID
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the matching group localization, or <code>null</code> if a matching group localization could not be found
	*/
	public static GroupLocalization fetchByGroupId_LanguageId(long groupId,
		java.lang.String languageId, boolean retrieveFromCache) {
		return getPersistence()
				   .fetchByGroupId_LanguageId(groupId, languageId,
			retrieveFromCache);
	}

	/**
	* Removes the group localization where groupId = &#63; and languageId = &#63; from the database.
	*
	* @param groupId the group ID
	* @param languageId the language ID
	* @return the group localization that was removed
	*/
	public static GroupLocalization removeByGroupId_LanguageId(long groupId,
		java.lang.String languageId)
		throws com.liferay.portal.kernel.exception.NoSuchGroupLocalizationException {
		return getPersistence().removeByGroupId_LanguageId(groupId, languageId);
	}

	/**
	* Returns the number of group localizations where groupId = &#63; and languageId = &#63;.
	*
	* @param groupId the group ID
	* @param languageId the language ID
	* @return the number of matching group localizations
	*/
	public static int countByGroupId_LanguageId(long groupId,
		java.lang.String languageId) {
		return getPersistence().countByGroupId_LanguageId(groupId, languageId);
	}

	/**
	* Caches the group localization in the entity cache if it is enabled.
	*
	* @param groupLocalization the group localization
	*/
	public static void cacheResult(GroupLocalization groupLocalization) {
		getPersistence().cacheResult(groupLocalization);
	}

	/**
	* Caches the group localizations in the entity cache if it is enabled.
	*
	* @param groupLocalizations the group localizations
	*/
	public static void cacheResult(List<GroupLocalization> groupLocalizations) {
		getPersistence().cacheResult(groupLocalizations);
	}

	/**
	* Creates a new group localization with the primary key. Does not add the group localization to the database.
	*
	* @param groupLocalizationId the primary key for the new group localization
	* @return the new group localization
	*/
	public static GroupLocalization create(long groupLocalizationId) {
		return getPersistence().create(groupLocalizationId);
	}

	/**
	* Removes the group localization with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param groupLocalizationId the primary key of the group localization
	* @return the group localization that was removed
	* @throws NoSuchGroupLocalizationException if a group localization with the primary key could not be found
	*/
	public static GroupLocalization remove(long groupLocalizationId)
		throws com.liferay.portal.kernel.exception.NoSuchGroupLocalizationException {
		return getPersistence().remove(groupLocalizationId);
	}

	public static GroupLocalization updateImpl(
		GroupLocalization groupLocalization) {
		return getPersistence().updateImpl(groupLocalization);
	}

	/**
	* Returns the group localization with the primary key or throws a {@link NoSuchGroupLocalizationException} if it could not be found.
	*
	* @param groupLocalizationId the primary key of the group localization
	* @return the group localization
	* @throws NoSuchGroupLocalizationException if a group localization with the primary key could not be found
	*/
	public static GroupLocalization findByPrimaryKey(long groupLocalizationId)
		throws com.liferay.portal.kernel.exception.NoSuchGroupLocalizationException {
		return getPersistence().findByPrimaryKey(groupLocalizationId);
	}

	/**
	* Returns the group localization with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param groupLocalizationId the primary key of the group localization
	* @return the group localization, or <code>null</code> if a group localization with the primary key could not be found
	*/
	public static GroupLocalization fetchByPrimaryKey(long groupLocalizationId) {
		return getPersistence().fetchByPrimaryKey(groupLocalizationId);
	}

	public static java.util.Map<java.io.Serializable, GroupLocalization> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys) {
		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	* Returns all the group localizations.
	*
	* @return the group localizations
	*/
	public static List<GroupLocalization> findAll() {
		return getPersistence().findAll();
	}

	/**
	* Returns a range of all the group localizations.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link GroupLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of group localizations
	* @param end the upper bound of the range of group localizations (not inclusive)
	* @return the range of group localizations
	*/
	public static List<GroupLocalization> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	* Returns an ordered range of all the group localizations.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link GroupLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of group localizations
	* @param end the upper bound of the range of group localizations (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of group localizations
	*/
	public static List<GroupLocalization> findAll(int start, int end,
		OrderByComparator<GroupLocalization> orderByComparator) {
		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	* Returns an ordered range of all the group localizations.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link GroupLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of group localizations
	* @param end the upper bound of the range of group localizations (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of group localizations
	*/
	public static List<GroupLocalization> findAll(int start, int end,
		OrderByComparator<GroupLocalization> orderByComparator,
		boolean retrieveFromCache) {
		return getPersistence()
				   .findAll(start, end, orderByComparator, retrieveFromCache);
	}

	/**
	* Removes all the group localizations from the database.
	*/
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	* Returns the number of group localizations.
	*
	* @return the number of group localizations
	*/
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static GroupLocalizationPersistence getPersistence() {
		if (_persistence == null) {
			_persistence = (GroupLocalizationPersistence)PortalBeanLocatorUtil.locate(GroupLocalizationPersistence.class.getName());

			ReferenceRegistry.registerReference(GroupLocalizationUtil.class,
				"_persistence");
		}

		return _persistence;
	}

	private static GroupLocalizationPersistence _persistence;
}
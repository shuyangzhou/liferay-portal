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
import com.liferay.portal.kernel.model.ABCTestEntityLocalization;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ReferenceRegistry;

import java.util.List;

/**
 * The persistence utility for the abc test entity localization service. This utility wraps {@link com.liferay.portal.service.persistence.impl.ABCTestEntityLocalizationPersistenceImpl} and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ABCTestEntityLocalizationPersistence
 * @see com.liferay.portal.service.persistence.impl.ABCTestEntityLocalizationPersistenceImpl
 * @generated
 */
@ProviderType
public class ABCTestEntityLocalizationUtil {
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
	public static void clearCache(
		ABCTestEntityLocalization abcTestEntityLocalization) {
		getPersistence().clearCache(abcTestEntityLocalization);
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
	public static List<ABCTestEntityLocalization> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<ABCTestEntityLocalization> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {
		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<ABCTestEntityLocalization> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		return getPersistence()
				   .findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * @see BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static ABCTestEntityLocalization update(
		ABCTestEntityLocalization abcTestEntityLocalization) {
		return getPersistence().update(abcTestEntityLocalization);
	}

	/**
	 * @see BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static ABCTestEntityLocalization update(
		ABCTestEntityLocalization abcTestEntityLocalization,
		ServiceContext serviceContext) {
		return getPersistence().update(abcTestEntityLocalization, serviceContext);
	}

	/**
	* Returns all the abc test entity localizations where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @return the matching abc test entity localizations
	*/
	public static List<ABCTestEntityLocalization> findByAbcTestEntityId(
		java.lang.String abcTestEntityId) {
		return getPersistence().findByAbcTestEntityId(abcTestEntityId);
	}

	/**
	* Returns a range of all the abc test entity localizations where abcTestEntityId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param abcTestEntityId the abc test entity ID
	* @param start the lower bound of the range of abc test entity localizations
	* @param end the upper bound of the range of abc test entity localizations (not inclusive)
	* @return the range of matching abc test entity localizations
	*/
	public static List<ABCTestEntityLocalization> findByAbcTestEntityId(
		java.lang.String abcTestEntityId, int start, int end) {
		return getPersistence()
				   .findByAbcTestEntityId(abcTestEntityId, start, end);
	}

	/**
	* Returns an ordered range of all the abc test entity localizations where abcTestEntityId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param abcTestEntityId the abc test entity ID
	* @param start the lower bound of the range of abc test entity localizations
	* @param end the upper bound of the range of abc test entity localizations (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching abc test entity localizations
	*/
	public static List<ABCTestEntityLocalization> findByAbcTestEntityId(
		java.lang.String abcTestEntityId, int start, int end,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		return getPersistence()
				   .findByAbcTestEntityId(abcTestEntityId, start, end,
			orderByComparator);
	}

	/**
	* Returns an ordered range of all the abc test entity localizations where abcTestEntityId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param abcTestEntityId the abc test entity ID
	* @param start the lower bound of the range of abc test entity localizations
	* @param end the upper bound of the range of abc test entity localizations (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of matching abc test entity localizations
	*/
	public static List<ABCTestEntityLocalization> findByAbcTestEntityId(
		java.lang.String abcTestEntityId, int start, int end,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator,
		boolean retrieveFromCache) {
		return getPersistence()
				   .findByAbcTestEntityId(abcTestEntityId, start, end,
			orderByComparator, retrieveFromCache);
	}

	/**
	* Returns the first abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization findByAbcTestEntityId_First(
		java.lang.String abcTestEntityId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException {
		return getPersistence()
				   .findByAbcTestEntityId_First(abcTestEntityId,
			orderByComparator);
	}

	/**
	* Returns the first abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization fetchByAbcTestEntityId_First(
		java.lang.String abcTestEntityId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		return getPersistence()
				   .fetchByAbcTestEntityId_First(abcTestEntityId,
			orderByComparator);
	}

	/**
	* Returns the last abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization findByAbcTestEntityId_Last(
		java.lang.String abcTestEntityId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException {
		return getPersistence()
				   .findByAbcTestEntityId_Last(abcTestEntityId,
			orderByComparator);
	}

	/**
	* Returns the last abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization fetchByAbcTestEntityId_Last(
		java.lang.String abcTestEntityId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		return getPersistence()
				   .fetchByAbcTestEntityId_Last(abcTestEntityId,
			orderByComparator);
	}

	/**
	* Returns the abc test entity localizations before and after the current abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityLocalizationId the primary key of the current abc test entity localization
	* @param abcTestEntityId the abc test entity ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	*/
	public static ABCTestEntityLocalization[] findByAbcTestEntityId_PrevAndNext(
		long abcTestEntityLocalizationId, java.lang.String abcTestEntityId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException {
		return getPersistence()
				   .findByAbcTestEntityId_PrevAndNext(abcTestEntityLocalizationId,
			abcTestEntityId, orderByComparator);
	}

	/**
	* Removes all the abc test entity localizations where abcTestEntityId = &#63; from the database.
	*
	* @param abcTestEntityId the abc test entity ID
	*/
	public static void removeByAbcTestEntityId(java.lang.String abcTestEntityId) {
		getPersistence().removeByAbcTestEntityId(abcTestEntityId);
	}

	/**
	* Returns the number of abc test entity localizations where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @return the number of matching abc test entity localizations
	*/
	public static int countByAbcTestEntityId(java.lang.String abcTestEntityId) {
		return getPersistence().countByAbcTestEntityId(abcTestEntityId);
	}

	/**
	* Returns the abc test entity localization where abcTestEntityId = &#63; and languageId = &#63; or throws a {@link NoSuchABCTestEntityLocalizationException} if it could not be found.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param languageId the language ID
	* @return the matching abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization findByAbcTestEntityId_LanguageId(
		java.lang.String abcTestEntityId, java.lang.String languageId)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException {
		return getPersistence()
				   .findByAbcTestEntityId_LanguageId(abcTestEntityId, languageId);
	}

	/**
	* Returns the abc test entity localization where abcTestEntityId = &#63; and languageId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param languageId the language ID
	* @return the matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization fetchByAbcTestEntityId_LanguageId(
		java.lang.String abcTestEntityId, java.lang.String languageId) {
		return getPersistence()
				   .fetchByAbcTestEntityId_LanguageId(abcTestEntityId,
			languageId);
	}

	/**
	* Returns the abc test entity localization where abcTestEntityId = &#63; and languageId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param languageId the language ID
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization fetchByAbcTestEntityId_LanguageId(
		java.lang.String abcTestEntityId, java.lang.String languageId,
		boolean retrieveFromCache) {
		return getPersistence()
				   .fetchByAbcTestEntityId_LanguageId(abcTestEntityId,
			languageId, retrieveFromCache);
	}

	/**
	* Removes the abc test entity localization where abcTestEntityId = &#63; and languageId = &#63; from the database.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param languageId the language ID
	* @return the abc test entity localization that was removed
	*/
	public static ABCTestEntityLocalization removeByAbcTestEntityId_LanguageId(
		java.lang.String abcTestEntityId, java.lang.String languageId)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException {
		return getPersistence()
				   .removeByAbcTestEntityId_LanguageId(abcTestEntityId,
			languageId);
	}

	/**
	* Returns the number of abc test entity localizations where abcTestEntityId = &#63; and languageId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param languageId the language ID
	* @return the number of matching abc test entity localizations
	*/
	public static int countByAbcTestEntityId_LanguageId(
		java.lang.String abcTestEntityId, java.lang.String languageId) {
		return getPersistence()
				   .countByAbcTestEntityId_LanguageId(abcTestEntityId,
			languageId);
	}

	/**
	* Returns all the abc test entity localizations where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the matching abc test entity localizations
	*/
	public static List<ABCTestEntityLocalization> findByGroupId(long groupId) {
		return getPersistence().findByGroupId(groupId);
	}

	/**
	* Returns a range of all the abc test entity localizations where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of abc test entity localizations
	* @param end the upper bound of the range of abc test entity localizations (not inclusive)
	* @return the range of matching abc test entity localizations
	*/
	public static List<ABCTestEntityLocalization> findByGroupId(long groupId,
		int start, int end) {
		return getPersistence().findByGroupId(groupId, start, end);
	}

	/**
	* Returns an ordered range of all the abc test entity localizations where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of abc test entity localizations
	* @param end the upper bound of the range of abc test entity localizations (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching abc test entity localizations
	*/
	public static List<ABCTestEntityLocalization> findByGroupId(long groupId,
		int start, int end,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		return getPersistence()
				   .findByGroupId(groupId, start, end, orderByComparator);
	}

	/**
	* Returns an ordered range of all the abc test entity localizations where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of abc test entity localizations
	* @param end the upper bound of the range of abc test entity localizations (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of matching abc test entity localizations
	*/
	public static List<ABCTestEntityLocalization> findByGroupId(long groupId,
		int start, int end,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator,
		boolean retrieveFromCache) {
		return getPersistence()
				   .findByGroupId(groupId, start, end, orderByComparator,
			retrieveFromCache);
	}

	/**
	* Returns the first abc test entity localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization findByGroupId_First(long groupId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException {
		return getPersistence().findByGroupId_First(groupId, orderByComparator);
	}

	/**
	* Returns the first abc test entity localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization fetchByGroupId_First(long groupId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		return getPersistence().fetchByGroupId_First(groupId, orderByComparator);
	}

	/**
	* Returns the last abc test entity localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization findByGroupId_Last(long groupId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException {
		return getPersistence().findByGroupId_Last(groupId, orderByComparator);
	}

	/**
	* Returns the last abc test entity localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization fetchByGroupId_Last(long groupId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		return getPersistence().fetchByGroupId_Last(groupId, orderByComparator);
	}

	/**
	* Returns the abc test entity localizations before and after the current abc test entity localization in the ordered set where groupId = &#63;.
	*
	* @param abcTestEntityLocalizationId the primary key of the current abc test entity localization
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	*/
	public static ABCTestEntityLocalization[] findByGroupId_PrevAndNext(
		long abcTestEntityLocalizationId, long groupId,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException {
		return getPersistence()
				   .findByGroupId_PrevAndNext(abcTestEntityLocalizationId,
			groupId, orderByComparator);
	}

	/**
	* Removes all the abc test entity localizations where groupId = &#63; from the database.
	*
	* @param groupId the group ID
	*/
	public static void removeByGroupId(long groupId) {
		getPersistence().removeByGroupId(groupId);
	}

	/**
	* Returns the number of abc test entity localizations where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the number of matching abc test entity localizations
	*/
	public static int countByGroupId(long groupId) {
		return getPersistence().countByGroupId(groupId);
	}

	/**
	* Returns the abc test entity localization where groupId = &#63; and name = &#63; or throws a {@link NoSuchABCTestEntityLocalizationException} if it could not be found.
	*
	* @param groupId the group ID
	* @param name the name
	* @return the matching abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization findByG_N(long groupId,
		java.lang.String name)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException {
		return getPersistence().findByG_N(groupId, name);
	}

	/**
	* Returns the abc test entity localization where groupId = &#63; and name = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param groupId the group ID
	* @param name the name
	* @return the matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization fetchByG_N(long groupId,
		java.lang.String name) {
		return getPersistence().fetchByG_N(groupId, name);
	}

	/**
	* Returns the abc test entity localization where groupId = &#63; and name = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param groupId the group ID
	* @param name the name
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public static ABCTestEntityLocalization fetchByG_N(long groupId,
		java.lang.String name, boolean retrieveFromCache) {
		return getPersistence().fetchByG_N(groupId, name, retrieveFromCache);
	}

	/**
	* Removes the abc test entity localization where groupId = &#63; and name = &#63; from the database.
	*
	* @param groupId the group ID
	* @param name the name
	* @return the abc test entity localization that was removed
	*/
	public static ABCTestEntityLocalization removeByG_N(long groupId,
		java.lang.String name)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException {
		return getPersistence().removeByG_N(groupId, name);
	}

	/**
	* Returns the number of abc test entity localizations where groupId = &#63; and name = &#63;.
	*
	* @param groupId the group ID
	* @param name the name
	* @return the number of matching abc test entity localizations
	*/
	public static int countByG_N(long groupId, java.lang.String name) {
		return getPersistence().countByG_N(groupId, name);
	}

	/**
	* Caches the abc test entity localization in the entity cache if it is enabled.
	*
	* @param abcTestEntityLocalization the abc test entity localization
	*/
	public static void cacheResult(
		ABCTestEntityLocalization abcTestEntityLocalization) {
		getPersistence().cacheResult(abcTestEntityLocalization);
	}

	/**
	* Caches the abc test entity localizations in the entity cache if it is enabled.
	*
	* @param abcTestEntityLocalizations the abc test entity localizations
	*/
	public static void cacheResult(
		List<ABCTestEntityLocalization> abcTestEntityLocalizations) {
		getPersistence().cacheResult(abcTestEntityLocalizations);
	}

	/**
	* Creates a new abc test entity localization with the primary key. Does not add the abc test entity localization to the database.
	*
	* @param abcTestEntityLocalizationId the primary key for the new abc test entity localization
	* @return the new abc test entity localization
	*/
	public static ABCTestEntityLocalization create(
		long abcTestEntityLocalizationId) {
		return getPersistence().create(abcTestEntityLocalizationId);
	}

	/**
	* Removes the abc test entity localization with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param abcTestEntityLocalizationId the primary key of the abc test entity localization
	* @return the abc test entity localization that was removed
	* @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	*/
	public static ABCTestEntityLocalization remove(
		long abcTestEntityLocalizationId)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException {
		return getPersistence().remove(abcTestEntityLocalizationId);
	}

	public static ABCTestEntityLocalization updateImpl(
		ABCTestEntityLocalization abcTestEntityLocalization) {
		return getPersistence().updateImpl(abcTestEntityLocalization);
	}

	/**
	* Returns the abc test entity localization with the primary key or throws a {@link NoSuchABCTestEntityLocalizationException} if it could not be found.
	*
	* @param abcTestEntityLocalizationId the primary key of the abc test entity localization
	* @return the abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	*/
	public static ABCTestEntityLocalization findByPrimaryKey(
		long abcTestEntityLocalizationId)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException {
		return getPersistence().findByPrimaryKey(abcTestEntityLocalizationId);
	}

	/**
	* Returns the abc test entity localization with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param abcTestEntityLocalizationId the primary key of the abc test entity localization
	* @return the abc test entity localization, or <code>null</code> if a abc test entity localization with the primary key could not be found
	*/
	public static ABCTestEntityLocalization fetchByPrimaryKey(
		long abcTestEntityLocalizationId) {
		return getPersistence().fetchByPrimaryKey(abcTestEntityLocalizationId);
	}

	public static java.util.Map<java.io.Serializable, ABCTestEntityLocalization> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys) {
		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	* Returns all the abc test entity localizations.
	*
	* @return the abc test entity localizations
	*/
	public static List<ABCTestEntityLocalization> findAll() {
		return getPersistence().findAll();
	}

	/**
	* Returns a range of all the abc test entity localizations.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of abc test entity localizations
	* @param end the upper bound of the range of abc test entity localizations (not inclusive)
	* @return the range of abc test entity localizations
	*/
	public static List<ABCTestEntityLocalization> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	* Returns an ordered range of all the abc test entity localizations.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of abc test entity localizations
	* @param end the upper bound of the range of abc test entity localizations (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of abc test entity localizations
	*/
	public static List<ABCTestEntityLocalization> findAll(int start, int end,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator) {
		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	* Returns an ordered range of all the abc test entity localizations.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityLocalizationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of abc test entity localizations
	* @param end the upper bound of the range of abc test entity localizations (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of abc test entity localizations
	*/
	public static List<ABCTestEntityLocalization> findAll(int start, int end,
		OrderByComparator<ABCTestEntityLocalization> orderByComparator,
		boolean retrieveFromCache) {
		return getPersistence()
				   .findAll(start, end, orderByComparator, retrieveFromCache);
	}

	/**
	* Removes all the abc test entity localizations from the database.
	*/
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	* Returns the number of abc test entity localizations.
	*
	* @return the number of abc test entity localizations
	*/
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static ABCTestEntityLocalizationPersistence getPersistence() {
		if (_persistence == null) {
			_persistence = (ABCTestEntityLocalizationPersistence)PortalBeanLocatorUtil.locate(ABCTestEntityLocalizationPersistence.class.getName());

			ReferenceRegistry.registerReference(ABCTestEntityLocalizationUtil.class,
				"_persistence");
		}

		return _persistence;
	}

	private static ABCTestEntityLocalizationPersistence _persistence;
}
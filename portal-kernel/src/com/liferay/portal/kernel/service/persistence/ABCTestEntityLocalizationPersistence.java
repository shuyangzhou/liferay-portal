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

import com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException;
import com.liferay.portal.kernel.model.ABCTestEntityLocalization;

/**
 * The persistence interface for the abc test entity localization service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portal.service.persistence.impl.ABCTestEntityLocalizationPersistenceImpl
 * @see ABCTestEntityLocalizationUtil
 * @generated
 */
@ProviderType
public interface ABCTestEntityLocalizationPersistence extends BasePersistence<ABCTestEntityLocalization> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link ABCTestEntityLocalizationUtil} to access the abc test entity localization persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns all the abc test entity localizations where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @return the matching abc test entity localizations
	*/
	public java.util.List<ABCTestEntityLocalization> findByAbcTestEntityId(
		java.lang.String abcTestEntityId);

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
	public java.util.List<ABCTestEntityLocalization> findByAbcTestEntityId(
		java.lang.String abcTestEntityId, int start, int end);

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
	public java.util.List<ABCTestEntityLocalization> findByAbcTestEntityId(
		java.lang.String abcTestEntityId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator);

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
	public java.util.List<ABCTestEntityLocalization> findByAbcTestEntityId(
		java.lang.String abcTestEntityId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Returns the first abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization findByAbcTestEntityId_First(
		java.lang.String abcTestEntityId,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws NoSuchABCTestEntityLocalizationException;

	/**
	* Returns the first abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization fetchByAbcTestEntityId_First(
		java.lang.String abcTestEntityId,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator);

	/**
	* Returns the last abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization findByAbcTestEntityId_Last(
		java.lang.String abcTestEntityId,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws NoSuchABCTestEntityLocalizationException;

	/**
	* Returns the last abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization fetchByAbcTestEntityId_Last(
		java.lang.String abcTestEntityId,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator);

	/**
	* Returns the abc test entity localizations before and after the current abc test entity localization in the ordered set where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityLocalizationId the primary key of the current abc test entity localization
	* @param abcTestEntityId the abc test entity ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	*/
	public ABCTestEntityLocalization[] findByAbcTestEntityId_PrevAndNext(
		long abcTestEntityLocalizationId, java.lang.String abcTestEntityId,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws NoSuchABCTestEntityLocalizationException;

	/**
	* Removes all the abc test entity localizations where abcTestEntityId = &#63; from the database.
	*
	* @param abcTestEntityId the abc test entity ID
	*/
	public void removeByAbcTestEntityId(java.lang.String abcTestEntityId);

	/**
	* Returns the number of abc test entity localizations where abcTestEntityId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @return the number of matching abc test entity localizations
	*/
	public int countByAbcTestEntityId(java.lang.String abcTestEntityId);

	/**
	* Returns the abc test entity localization where abcTestEntityId = &#63; and languageId = &#63; or throws a {@link NoSuchABCTestEntityLocalizationException} if it could not be found.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param languageId the language ID
	* @return the matching abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization findByAbcTestEntityId_LanguageId(
		java.lang.String abcTestEntityId, java.lang.String languageId)
		throws NoSuchABCTestEntityLocalizationException;

	/**
	* Returns the abc test entity localization where abcTestEntityId = &#63; and languageId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param languageId the language ID
	* @return the matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization fetchByAbcTestEntityId_LanguageId(
		java.lang.String abcTestEntityId, java.lang.String languageId);

	/**
	* Returns the abc test entity localization where abcTestEntityId = &#63; and languageId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param languageId the language ID
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization fetchByAbcTestEntityId_LanguageId(
		java.lang.String abcTestEntityId, java.lang.String languageId,
		boolean retrieveFromCache);

	/**
	* Removes the abc test entity localization where abcTestEntityId = &#63; and languageId = &#63; from the database.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param languageId the language ID
	* @return the abc test entity localization that was removed
	*/
	public ABCTestEntityLocalization removeByAbcTestEntityId_LanguageId(
		java.lang.String abcTestEntityId, java.lang.String languageId)
		throws NoSuchABCTestEntityLocalizationException;

	/**
	* Returns the number of abc test entity localizations where abcTestEntityId = &#63; and languageId = &#63;.
	*
	* @param abcTestEntityId the abc test entity ID
	* @param languageId the language ID
	* @return the number of matching abc test entity localizations
	*/
	public int countByAbcTestEntityId_LanguageId(
		java.lang.String abcTestEntityId, java.lang.String languageId);

	/**
	* Returns all the abc test entity localizations where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the matching abc test entity localizations
	*/
	public java.util.List<ABCTestEntityLocalization> findByGroupId(long groupId);

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
	public java.util.List<ABCTestEntityLocalization> findByGroupId(
		long groupId, int start, int end);

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
	public java.util.List<ABCTestEntityLocalization> findByGroupId(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator);

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
	public java.util.List<ABCTestEntityLocalization> findByGroupId(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Returns the first abc test entity localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization findByGroupId_First(long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws NoSuchABCTestEntityLocalizationException;

	/**
	* Returns the first abc test entity localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization fetchByGroupId_First(long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator);

	/**
	* Returns the last abc test entity localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization findByGroupId_Last(long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws NoSuchABCTestEntityLocalizationException;

	/**
	* Returns the last abc test entity localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization fetchByGroupId_Last(long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator);

	/**
	* Returns the abc test entity localizations before and after the current abc test entity localization in the ordered set where groupId = &#63;.
	*
	* @param abcTestEntityLocalizationId the primary key of the current abc test entity localization
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	*/
	public ABCTestEntityLocalization[] findByGroupId_PrevAndNext(
		long abcTestEntityLocalizationId, long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator)
		throws NoSuchABCTestEntityLocalizationException;

	/**
	* Removes all the abc test entity localizations where groupId = &#63; from the database.
	*
	* @param groupId the group ID
	*/
	public void removeByGroupId(long groupId);

	/**
	* Returns the number of abc test entity localizations where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the number of matching abc test entity localizations
	*/
	public int countByGroupId(long groupId);

	/**
	* Returns the abc test entity localization where groupId = &#63; and name = &#63; or throws a {@link NoSuchABCTestEntityLocalizationException} if it could not be found.
	*
	* @param groupId the group ID
	* @param name the name
	* @return the matching abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization findByG_N(long groupId,
		java.lang.String name) throws NoSuchABCTestEntityLocalizationException;

	/**
	* Returns the abc test entity localization where groupId = &#63; and name = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param groupId the group ID
	* @param name the name
	* @return the matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization fetchByG_N(long groupId,
		java.lang.String name);

	/**
	* Returns the abc test entity localization where groupId = &#63; and name = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param groupId the group ID
	* @param name the name
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the matching abc test entity localization, or <code>null</code> if a matching abc test entity localization could not be found
	*/
	public ABCTestEntityLocalization fetchByG_N(long groupId,
		java.lang.String name, boolean retrieveFromCache);

	/**
	* Removes the abc test entity localization where groupId = &#63; and name = &#63; from the database.
	*
	* @param groupId the group ID
	* @param name the name
	* @return the abc test entity localization that was removed
	*/
	public ABCTestEntityLocalization removeByG_N(long groupId,
		java.lang.String name) throws NoSuchABCTestEntityLocalizationException;

	/**
	* Returns the number of abc test entity localizations where groupId = &#63; and name = &#63;.
	*
	* @param groupId the group ID
	* @param name the name
	* @return the number of matching abc test entity localizations
	*/
	public int countByG_N(long groupId, java.lang.String name);

	/**
	* Caches the abc test entity localization in the entity cache if it is enabled.
	*
	* @param abcTestEntityLocalization the abc test entity localization
	*/
	public void cacheResult(ABCTestEntityLocalization abcTestEntityLocalization);

	/**
	* Caches the abc test entity localizations in the entity cache if it is enabled.
	*
	* @param abcTestEntityLocalizations the abc test entity localizations
	*/
	public void cacheResult(
		java.util.List<ABCTestEntityLocalization> abcTestEntityLocalizations);

	/**
	* Creates a new abc test entity localization with the primary key. Does not add the abc test entity localization to the database.
	*
	* @param abcTestEntityLocalizationId the primary key for the new abc test entity localization
	* @return the new abc test entity localization
	*/
	public ABCTestEntityLocalization create(long abcTestEntityLocalizationId);

	/**
	* Removes the abc test entity localization with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param abcTestEntityLocalizationId the primary key of the abc test entity localization
	* @return the abc test entity localization that was removed
	* @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	*/
	public ABCTestEntityLocalization remove(long abcTestEntityLocalizationId)
		throws NoSuchABCTestEntityLocalizationException;

	public ABCTestEntityLocalization updateImpl(
		ABCTestEntityLocalization abcTestEntityLocalization);

	/**
	* Returns the abc test entity localization with the primary key or throws a {@link NoSuchABCTestEntityLocalizationException} if it could not be found.
	*
	* @param abcTestEntityLocalizationId the primary key of the abc test entity localization
	* @return the abc test entity localization
	* @throws NoSuchABCTestEntityLocalizationException if a abc test entity localization with the primary key could not be found
	*/
	public ABCTestEntityLocalization findByPrimaryKey(
		long abcTestEntityLocalizationId)
		throws NoSuchABCTestEntityLocalizationException;

	/**
	* Returns the abc test entity localization with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param abcTestEntityLocalizationId the primary key of the abc test entity localization
	* @return the abc test entity localization, or <code>null</code> if a abc test entity localization with the primary key could not be found
	*/
	public ABCTestEntityLocalization fetchByPrimaryKey(
		long abcTestEntityLocalizationId);

	@Override
	public java.util.Map<java.io.Serializable, ABCTestEntityLocalization> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the abc test entity localizations.
	*
	* @return the abc test entity localizations
	*/
	public java.util.List<ABCTestEntityLocalization> findAll();

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
	public java.util.List<ABCTestEntityLocalization> findAll(int start, int end);

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
	public java.util.List<ABCTestEntityLocalization> findAll(int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator);

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
	public java.util.List<ABCTestEntityLocalization> findAll(int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntityLocalization> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Removes all the abc test entity localizations from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of abc test entity localizations.
	*
	* @return the number of abc test entity localizations
	*/
	public int countAll();
}
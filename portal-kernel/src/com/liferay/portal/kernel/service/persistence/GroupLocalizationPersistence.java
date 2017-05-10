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

import com.liferay.portal.kernel.exception.NoSuchGroupLocalizationException;
import com.liferay.portal.kernel.model.GroupLocalization;

/**
 * The persistence interface for the group localization service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portal.service.persistence.impl.GroupLocalizationPersistenceImpl
 * @see GroupLocalizationUtil
 * @generated
 */
@ProviderType
public interface GroupLocalizationPersistence extends BasePersistence<GroupLocalization> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link GroupLocalizationUtil} to access the group localization persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns all the group localizations where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the matching group localizations
	*/
	public java.util.List<GroupLocalization> findByGroupId(long groupId);

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
	public java.util.List<GroupLocalization> findByGroupId(long groupId,
		int start, int end);

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
	public java.util.List<GroupLocalization> findByGroupId(long groupId,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<GroupLocalization> orderByComparator);

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
	public java.util.List<GroupLocalization> findByGroupId(long groupId,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<GroupLocalization> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Returns the first group localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching group localization
	* @throws NoSuchGroupLocalizationException if a matching group localization could not be found
	*/
	public GroupLocalization findByGroupId_First(long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<GroupLocalization> orderByComparator)
		throws NoSuchGroupLocalizationException;

	/**
	* Returns the first group localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching group localization, or <code>null</code> if a matching group localization could not be found
	*/
	public GroupLocalization fetchByGroupId_First(long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<GroupLocalization> orderByComparator);

	/**
	* Returns the last group localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching group localization
	* @throws NoSuchGroupLocalizationException if a matching group localization could not be found
	*/
	public GroupLocalization findByGroupId_Last(long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<GroupLocalization> orderByComparator)
		throws NoSuchGroupLocalizationException;

	/**
	* Returns the last group localization in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching group localization, or <code>null</code> if a matching group localization could not be found
	*/
	public GroupLocalization fetchByGroupId_Last(long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<GroupLocalization> orderByComparator);

	/**
	* Returns the group localizations before and after the current group localization in the ordered set where groupId = &#63;.
	*
	* @param groupLocalizationId the primary key of the current group localization
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next group localization
	* @throws NoSuchGroupLocalizationException if a group localization with the primary key could not be found
	*/
	public GroupLocalization[] findByGroupId_PrevAndNext(
		long groupLocalizationId, long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<GroupLocalization> orderByComparator)
		throws NoSuchGroupLocalizationException;

	/**
	* Removes all the group localizations where groupId = &#63; from the database.
	*
	* @param groupId the group ID
	*/
	public void removeByGroupId(long groupId);

	/**
	* Returns the number of group localizations where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the number of matching group localizations
	*/
	public int countByGroupId(long groupId);

	/**
	* Returns the group localization where groupId = &#63; and languageId = &#63; or throws a {@link NoSuchGroupLocalizationException} if it could not be found.
	*
	* @param groupId the group ID
	* @param languageId the language ID
	* @return the matching group localization
	* @throws NoSuchGroupLocalizationException if a matching group localization could not be found
	*/
	public GroupLocalization findByGroupId_LanguageId(long groupId,
		java.lang.String languageId) throws NoSuchGroupLocalizationException;

	/**
	* Returns the group localization where groupId = &#63; and languageId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param groupId the group ID
	* @param languageId the language ID
	* @return the matching group localization, or <code>null</code> if a matching group localization could not be found
	*/
	public GroupLocalization fetchByGroupId_LanguageId(long groupId,
		java.lang.String languageId);

	/**
	* Returns the group localization where groupId = &#63; and languageId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param groupId the group ID
	* @param languageId the language ID
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the matching group localization, or <code>null</code> if a matching group localization could not be found
	*/
	public GroupLocalization fetchByGroupId_LanguageId(long groupId,
		java.lang.String languageId, boolean retrieveFromCache);

	/**
	* Removes the group localization where groupId = &#63; and languageId = &#63; from the database.
	*
	* @param groupId the group ID
	* @param languageId the language ID
	* @return the group localization that was removed
	*/
	public GroupLocalization removeByGroupId_LanguageId(long groupId,
		java.lang.String languageId) throws NoSuchGroupLocalizationException;

	/**
	* Returns the number of group localizations where groupId = &#63; and languageId = &#63;.
	*
	* @param groupId the group ID
	* @param languageId the language ID
	* @return the number of matching group localizations
	*/
	public int countByGroupId_LanguageId(long groupId,
		java.lang.String languageId);

	/**
	* Caches the group localization in the entity cache if it is enabled.
	*
	* @param groupLocalization the group localization
	*/
	public void cacheResult(GroupLocalization groupLocalization);

	/**
	* Caches the group localizations in the entity cache if it is enabled.
	*
	* @param groupLocalizations the group localizations
	*/
	public void cacheResult(
		java.util.List<GroupLocalization> groupLocalizations);

	/**
	* Creates a new group localization with the primary key. Does not add the group localization to the database.
	*
	* @param groupLocalizationId the primary key for the new group localization
	* @return the new group localization
	*/
	public GroupLocalization create(long groupLocalizationId);

	/**
	* Removes the group localization with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param groupLocalizationId the primary key of the group localization
	* @return the group localization that was removed
	* @throws NoSuchGroupLocalizationException if a group localization with the primary key could not be found
	*/
	public GroupLocalization remove(long groupLocalizationId)
		throws NoSuchGroupLocalizationException;

	public GroupLocalization updateImpl(GroupLocalization groupLocalization);

	/**
	* Returns the group localization with the primary key or throws a {@link NoSuchGroupLocalizationException} if it could not be found.
	*
	* @param groupLocalizationId the primary key of the group localization
	* @return the group localization
	* @throws NoSuchGroupLocalizationException if a group localization with the primary key could not be found
	*/
	public GroupLocalization findByPrimaryKey(long groupLocalizationId)
		throws NoSuchGroupLocalizationException;

	/**
	* Returns the group localization with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param groupLocalizationId the primary key of the group localization
	* @return the group localization, or <code>null</code> if a group localization with the primary key could not be found
	*/
	public GroupLocalization fetchByPrimaryKey(long groupLocalizationId);

	@Override
	public java.util.Map<java.io.Serializable, GroupLocalization> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the group localizations.
	*
	* @return the group localizations
	*/
	public java.util.List<GroupLocalization> findAll();

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
	public java.util.List<GroupLocalization> findAll(int start, int end);

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
	public java.util.List<GroupLocalization> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<GroupLocalization> orderByComparator);

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
	public java.util.List<GroupLocalization> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<GroupLocalization> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Removes all the group localizations from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of group localizations.
	*
	* @return the number of group localizations
	*/
	public int countAll();
}
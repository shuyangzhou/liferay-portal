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

package com.liferay.portal.security.password.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link HashAlgorithmEntryLocalService}.
 *
 * @author arthurchan35
 * @see HashAlgorithmEntryLocalService
 * @generated
 */
public class HashAlgorithmEntryLocalServiceWrapper
	implements HashAlgorithmEntryLocalService,
			   ServiceWrapper<HashAlgorithmEntryLocalService> {

	public HashAlgorithmEntryLocalServiceWrapper(
		HashAlgorithmEntryLocalService hashAlgorithmEntryLocalService) {

		_hashAlgorithmEntryLocalService = hashAlgorithmEntryLocalService;
	}

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>HashAlgorithmEntryLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>HashAlgorithmEntryLocalServiceUtil</code>.
	 */
	@Override
	public com.liferay.portal.security.password.model.HashAlgorithmEntry
			addEntry(String name, org.json.JSONObject meta)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _hashAlgorithmEntryLocalService.addEntry(name, meta);
	}

	/**
	 * Adds the hash algorithm entry to the database. Also notifies the appropriate model listeners.
	 *
	 * @param hashAlgorithmEntry the hash algorithm entry
	 * @return the hash algorithm entry that was added
	 */
	@Override
	public com.liferay.portal.security.password.model.HashAlgorithmEntry
		addHashAlgorithmEntry(
			com.liferay.portal.security.password.model.HashAlgorithmEntry
				hashAlgorithmEntry) {

		return _hashAlgorithmEntryLocalService.addHashAlgorithmEntry(
			hashAlgorithmEntry);
	}

	/**
	 * Creates a new hash algorithm entry with the primary key. Does not add the hash algorithm entry to the database.
	 *
	 * @param entryId the primary key for the new hash algorithm entry
	 * @return the new hash algorithm entry
	 */
	@Override
	public com.liferay.portal.security.password.model.HashAlgorithmEntry
		createHashAlgorithmEntry(long entryId) {

		return _hashAlgorithmEntryLocalService.createHashAlgorithmEntry(
			entryId);
	}

	@Override
	public com.liferay.portal.security.password.model.HashAlgorithmEntry
			deleteEntry(
				com.liferay.portal.security.password.model.HashAlgorithmEntry
					entry)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _hashAlgorithmEntryLocalService.deleteEntry(entry);
	}

	/**
	 * Deletes the hash algorithm entry from the database. Also notifies the appropriate model listeners.
	 *
	 * @param hashAlgorithmEntry the hash algorithm entry
	 * @return the hash algorithm entry that was removed
	 */
	@Override
	public com.liferay.portal.security.password.model.HashAlgorithmEntry
		deleteHashAlgorithmEntry(
			com.liferay.portal.security.password.model.HashAlgorithmEntry
				hashAlgorithmEntry) {

		return _hashAlgorithmEntryLocalService.deleteHashAlgorithmEntry(
			hashAlgorithmEntry);
	}

	/**
	 * Deletes the hash algorithm entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the hash algorithm entry
	 * @return the hash algorithm entry that was removed
	 * @throws PortalException if a hash algorithm entry with the primary key could not be found
	 */
	@Override
	public com.liferay.portal.security.password.model.HashAlgorithmEntry
			deleteHashAlgorithmEntry(long entryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _hashAlgorithmEntryLocalService.deleteHashAlgorithmEntry(
			entryId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _hashAlgorithmEntryLocalService.deletePersistedModel(
			persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _hashAlgorithmEntryLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _hashAlgorithmEntryLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.HashAlgorithmEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _hashAlgorithmEntryLocalService.dynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.HashAlgorithmEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _hashAlgorithmEntryLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _hashAlgorithmEntryLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _hashAlgorithmEntryLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.portal.security.password.model.HashAlgorithmEntry
		fetchHashAlgorithmEntry(long entryId) {

		return _hashAlgorithmEntryLocalService.fetchHashAlgorithmEntry(entryId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _hashAlgorithmEntryLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.security.password.model.HashAlgorithmEntry
			getCurrentEntry()
		throws com.liferay.portal.kernel.exception.PortalException {

		return _hashAlgorithmEntryLocalService.getCurrentEntry();
	}

	/**
	 * Returns a range of all the hash algorithm entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.HashAlgorithmEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of hash algorithm entries
	 * @param end the upper bound of the range of hash algorithm entries (not inclusive)
	 * @return the range of hash algorithm entries
	 */
	@Override
	public java.util.List
		<com.liferay.portal.security.password.model.HashAlgorithmEntry>
			getHashAlgorithmEntries(int start, int end) {

		return _hashAlgorithmEntryLocalService.getHashAlgorithmEntries(
			start, end);
	}

	/**
	 * Returns the number of hash algorithm entries.
	 *
	 * @return the number of hash algorithm entries
	 */
	@Override
	public int getHashAlgorithmEntriesCount() {
		return _hashAlgorithmEntryLocalService.getHashAlgorithmEntriesCount();
	}

	/**
	 * Returns the hash algorithm entry with the primary key.
	 *
	 * @param entryId the primary key of the hash algorithm entry
	 * @return the hash algorithm entry
	 * @throws PortalException if a hash algorithm entry with the primary key could not be found
	 */
	@Override
	public com.liferay.portal.security.password.model.HashAlgorithmEntry
			getHashAlgorithmEntry(long entryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _hashAlgorithmEntryLocalService.getHashAlgorithmEntry(entryId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _hashAlgorithmEntryLocalService.
			getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _hashAlgorithmEntryLocalService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _hashAlgorithmEntryLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Updates the hash algorithm entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param hashAlgorithmEntry the hash algorithm entry
	 * @return the hash algorithm entry that was updated
	 */
	@Override
	public com.liferay.portal.security.password.model.HashAlgorithmEntry
		updateHashAlgorithmEntry(
			com.liferay.portal.security.password.model.HashAlgorithmEntry
				hashAlgorithmEntry) {

		return _hashAlgorithmEntryLocalService.updateHashAlgorithmEntry(
			hashAlgorithmEntry);
	}

	@Override
	public HashAlgorithmEntryLocalService getWrappedService() {
		return _hashAlgorithmEntryLocalService;
	}

	@Override
	public void setWrappedService(
		HashAlgorithmEntryLocalService hashAlgorithmEntryLocalService) {

		_hashAlgorithmEntryLocalService = hashAlgorithmEntryLocalService;
	}

	private HashAlgorithmEntryLocalService _hashAlgorithmEntryLocalService;

}
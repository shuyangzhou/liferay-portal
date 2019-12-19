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
 * Provides a wrapper for {@link PasswordEntryLocalService}.
 *
 * @author arthurchan35
 * @see PasswordEntryLocalService
 * @generated
 */
public class PasswordEntryLocalServiceWrapper
	implements PasswordEntryLocalService,
			   ServiceWrapper<PasswordEntryLocalService> {

	public PasswordEntryLocalServiceWrapper(
		PasswordEntryLocalService passwordEntryLocalService) {

		_passwordEntryLocalService = passwordEntryLocalService;
	}

	/**
	 * Add a passwordEntry for user of userId
	 *
	 * @param UserId, ID of user
	 * @param Password, plain text password
	 * @return Generated PassowrdEntry
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordEntry addEntry(
			long userId, String password)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordEntryLocalService.addEntry(userId, password);
	}

	/**
	 * Adds the password entry to the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordEntry the password entry
	 * @return the password entry that was added
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordEntry
		addPasswordEntry(
			com.liferay.portal.security.password.model.PasswordEntry
				passwordEntry) {

		return _passwordEntryLocalService.addPasswordEntry(passwordEntry);
	}

	/**
	 * Creates a new password entry with the primary key. Does not add the password entry to the database.
	 *
	 * @param entryId the primary key for the new password entry
	 * @return the new password entry
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordEntry
		createPasswordEntry(long entryId) {

		return _passwordEntryLocalService.createPasswordEntry(entryId);
	}

	/**
	 * Remove all password entries for given user, including current password and history passwords.
	 *
	 * @param UserId, ID of user
	 * @throws PortalException
	 */
	@Override
	public void deleteEntriesByUserId(long userId)
		throws com.liferay.portal.kernel.exception.PortalException {

		_passwordEntryLocalService.deleteEntriesByUserId(userId);
	}

	/**
	 * Remove all history password entries for given user.
	 *
	 * @param UserId, ID of user
	 * @throws PortalException
	 */
	@Override
	public void deleteHistoryEntriesByUserId(long userId)
		throws com.liferay.portal.kernel.exception.PortalException {

		_passwordEntryLocalService.deleteHistoryEntriesByUserId(userId);
	}

	/**
	 * Deletes the password entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the password entry
	 * @return the password entry that was removed
	 * @throws PortalException if a password entry with the primary key could not be found
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordEntry
			deletePasswordEntry(long entryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordEntryLocalService.deletePasswordEntry(entryId);
	}

	/**
	 * Deletes the password entry from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordEntry the password entry
	 * @return the password entry that was removed
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordEntry
			deletePasswordEntry(
				com.liferay.portal.security.password.model.PasswordEntry
					passwordEntry)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordEntryLocalService.deletePasswordEntry(passwordEntry);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordEntryLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _passwordEntryLocalService.dynamicQuery();
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

		return _passwordEntryLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordEntryModelImpl</code>.
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

		return _passwordEntryLocalService.dynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordEntryModelImpl</code>.
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

		return _passwordEntryLocalService.dynamicQuery(
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

		return _passwordEntryLocalService.dynamicQueryCount(dynamicQuery);
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

		return _passwordEntryLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.portal.security.password.model.PasswordEntry
		fetchPasswordEntry(long entryId) {

		return _passwordEntryLocalService.fetchPasswordEntry(entryId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _passwordEntryLocalService.getActionableDynamicQuery();
	}

	/**
	 * Return all password entries for given user, including current password and history passwords. Ordered by modified date.
	 *
	 * @param UserId, ID of user
	 * @return A list of password entries
	 */
	@Override
	public java.util.List
		<com.liferay.portal.security.password.model.PasswordEntry>
			getEntriesByUserId(long userId) {

		return _passwordEntryLocalService.getEntriesByUserId(userId);
	}

	/**
	 * Return current password for given user.
	 *
	 * @param UserId, ID of user
	 * @return PasswordEntry
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordEntry
			getEntryByUserId(long userId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordEntryLocalService.getEntryByUserId(userId);
	}

	/**
	 * Return history passwords for given user. Ordered by modified date.
	 *
	 * @param UserId, ID of user
	 * @return A list of Password entries the user used in the past.
	 */
	@Override
	public java.util.List
		<com.liferay.portal.security.password.model.PasswordEntry>
			getHistoryEntriesByUserId(long userId) {

		return _passwordEntryLocalService.getHistoryEntriesByUserId(userId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _passwordEntryLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _passwordEntryLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * Returns a range of all the password entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password entries
	 * @param end the upper bound of the range of password entries (not inclusive)
	 * @return the range of password entries
	 */
	@Override
	public java.util.List
		<com.liferay.portal.security.password.model.PasswordEntry>
			getPasswordEntries(int start, int end) {

		return _passwordEntryLocalService.getPasswordEntries(start, end);
	}

	/**
	 * Returns the number of password entries.
	 *
	 * @return the number of password entries
	 */
	@Override
	public int getPasswordEntriesCount() {
		return _passwordEntryLocalService.getPasswordEntriesCount();
	}

	/**
	 * Returns the password entry with the primary key.
	 *
	 * @param entryId the primary key of the password entry
	 * @return the password entry
	 * @throws PortalException if a password entry with the primary key could not be found
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordEntry
			getPasswordEntry(long entryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordEntryLocalService.getPasswordEntry(entryId);
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordEntryLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * When updating passwordEntries for a user, two things can happen:
	 * 1. when passwordPolicy is set to have password history,
	 *    remove extra history passwords(oldest) that are over history count limit,
	 *    and return a newly added password entry for the user.
	 * 2. otherwise,
	 *    remove all history passwords for the user,
	 *    and return an updated passwordEntry.
	 *
	 * @param UserId, ID of user
	 * @param Password, plain text password
	 * @return Updated PassowrdEntry
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordEntry
			updateEntriesByUserId(long userId, String password)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordEntryLocalService.updateEntriesByUserId(
			userId, password);
	}

	/**
	 * Updates the password entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param passwordEntry the password entry
	 * @return the password entry that was updated
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordEntry
		updatePasswordEntry(
			com.liferay.portal.security.password.model.PasswordEntry
				passwordEntry) {

		return _passwordEntryLocalService.updatePasswordEntry(passwordEntry);
	}

	@Override
	public PasswordEntryLocalService getWrappedService() {
		return _passwordEntryLocalService;
	}

	@Override
	public void setWrappedService(
		PasswordEntryLocalService passwordEntryLocalService) {

		_passwordEntryLocalService = passwordEntryLocalService;
	}

	private PasswordEntryLocalService _passwordEntryLocalService;

}
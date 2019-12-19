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
 * Provides a wrapper for {@link PasswordMetaLocalService}.
 *
 * @author arthurchan35
 * @see PasswordMetaLocalService
 * @generated
 */
public class PasswordMetaLocalServiceWrapper
	implements PasswordMetaLocalService,
			   ServiceWrapper<PasswordMetaLocalService> {

	public PasswordMetaLocalServiceWrapper(
		PasswordMetaLocalService passwordMetaLocalService) {

		_passwordMetaLocalService = passwordMetaLocalService;
	}

	/**
	 * Add a passwordMeta for the given passwordEntry during passwordEntry generation.
	 *
	 * @param PasswordEntry, the passwordEntry
	 * @return Generated meta for passwordEntry
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordMeta
			addMetaByEntry(
				com.liferay.portal.security.password.model.PasswordEntry
					passwordEntry)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordMetaLocalService.addMetaByEntry(passwordEntry);
	}

	/**
	 * Adds the password meta to the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordMeta the password meta
	 * @return the password meta that was added
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordMeta
		addPasswordMeta(
			com.liferay.portal.security.password.model.PasswordMeta
				passwordMeta) {

		return _passwordMetaLocalService.addPasswordMeta(passwordMeta);
	}

	/**
	 * Creates a new password meta with the primary key. Does not add the password meta to the database.
	 *
	 * @param metaId the primary key for the new password meta
	 * @return the new password meta
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordMeta
		createPasswordMeta(long metaId) {

		return _passwordMetaLocalService.createPasswordMeta(metaId);
	}

	@Override
	public void deleteMetasByEntry(
			com.liferay.portal.security.password.model.PasswordEntry
				passwordEntry)
		throws com.liferay.portal.kernel.exception.PortalException {

		_passwordMetaLocalService.deleteMetasByEntry(passwordEntry);
	}

	@Override
	public void deleteMetasByEntryId(long passwordEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		_passwordMetaLocalService.deleteMetasByEntryId(passwordEntryId);
	}

	/**
	 * Deletes the password meta with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param metaId the primary key of the password meta
	 * @return the password meta that was removed
	 * @throws PortalException if a password meta with the primary key could not be found
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordMeta
			deletePasswordMeta(long metaId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordMetaLocalService.deletePasswordMeta(metaId);
	}

	/**
	 * Deletes the password meta from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordMeta the password meta
	 * @return the password meta that was removed
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordMeta
			deletePasswordMeta(
				com.liferay.portal.security.password.model.PasswordMeta
					passwordMeta)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordMetaLocalService.deletePasswordMeta(passwordMeta);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordMetaLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _passwordMetaLocalService.dynamicQuery();
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

		return _passwordMetaLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordMetaModelImpl</code>.
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

		return _passwordMetaLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordMetaModelImpl</code>.
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

		return _passwordMetaLocalService.dynamicQuery(
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

		return _passwordMetaLocalService.dynamicQueryCount(dynamicQuery);
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

		return _passwordMetaLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.portal.security.password.model.PasswordMeta
		fetchPasswordMeta(long metaId) {

		return _passwordMetaLocalService.fetchPasswordMeta(metaId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _passwordMetaLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _passwordMetaLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Return a list of metas of PasswordEntry, ordered by modified date
	 *
	 * @param PasswordEntry, the passwordEntry
	 * @return a list of metas of PasswordEntry
	 * @throws PortalException
	 */
	@Override
	public java.util.List
		<com.liferay.portal.security.password.model.PasswordMeta>
				getMetasByEntry(
					com.liferay.portal.security.password.model.PasswordEntry
						passwordEntry)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordMetaLocalService.getMetasByEntry(passwordEntry);
	}

	/**
	 * Return a list of metas of PasswordEntry, ordered by modified date
	 *
	 * @param PasswordEntryId, the ID of passwordEntry
	 * @return a list of metas of PasswordEntry
	 * @throws PortalException
	 */
	@Override
	public java.util.List
		<com.liferay.portal.security.password.model.PasswordMeta>
				getMetasByEntryId(long passwordEntryId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordMetaLocalService.getMetasByEntryId(passwordEntryId);
	}

	@Override
	public int getMetasCountByEntry(
			com.liferay.portal.security.password.model.PasswordEntry
				passwordEntry)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordMetaLocalService.getMetasCountByEntry(passwordEntry);
	}

	@Override
	public int getMetasCountByEntryId(long passwordEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordMetaLocalService.getMetasCountByEntryId(
			passwordEntryId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _passwordMetaLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * Returns the password meta with the primary key.
	 *
	 * @param metaId the primary key of the password meta
	 * @return the password meta
	 * @throws PortalException if a password meta with the primary key could not be found
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordMeta
			getPasswordMeta(long metaId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordMetaLocalService.getPasswordMeta(metaId);
	}

	/**
	 * Returns a range of all the password metas.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @return the range of password metas
	 */
	@Override
	public java.util.List
		<com.liferay.portal.security.password.model.PasswordMeta>
			getPasswordMetas(int start, int end) {

		return _passwordMetaLocalService.getPasswordMetas(start, end);
	}

	/**
	 * Returns the number of password metas.
	 *
	 * @return the number of password metas
	 */
	@Override
	public int getPasswordMetasCount() {
		return _passwordMetaLocalService.getPasswordMetasCount();
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordMetaLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * When a password is being updated, it's metas will be reset to one that uses current Hash algorithm.
	 *
	 * @param PasswordEntry,the passwordEntry
	 * @return Newly generated single meta for passwordEntry
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordMeta
			updateMetasByEntry(
				com.liferay.portal.security.password.model.PasswordEntry
					passwordEntry)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordMetaLocalService.updateMetasByEntry(passwordEntry);
	}

	/**
	 * When a password is being updated, it's metas will be reset to one that uses current Hash algorithm.
	 *
	 * @param PasswordEntryId, ID of passwordEntry
	 * @return Newly generated single meta for passwordEntry
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordMeta
			updateMetasByEntryId(long passwordEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _passwordMetaLocalService.updateMetasByEntryId(passwordEntryId);
	}

	/**
	 * Updates the password meta in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param passwordMeta the password meta
	 * @return the password meta that was updated
	 */
	@Override
	public com.liferay.portal.security.password.model.PasswordMeta
		updatePasswordMeta(
			com.liferay.portal.security.password.model.PasswordMeta
				passwordMeta) {

		return _passwordMetaLocalService.updatePasswordMeta(passwordMeta);
	}

	@Override
	public PasswordMetaLocalService getWrappedService() {
		return _passwordMetaLocalService;
	}

	@Override
	public void setWrappedService(
		PasswordMetaLocalService passwordMetaLocalService) {

		_passwordMetaLocalService = passwordMetaLocalService;
	}

	private PasswordMetaLocalService _passwordMetaLocalService;

}
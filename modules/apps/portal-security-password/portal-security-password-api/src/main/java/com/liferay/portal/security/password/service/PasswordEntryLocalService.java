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

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.security.password.model.PasswordEntry;

import java.io.Serializable;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the local service interface for PasswordEntry. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author arthurchan35
 * @see PasswordEntryLocalServiceUtil
 * @generated
 */
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface PasswordEntryLocalService
	extends BaseLocalService, PersistedModelLocalService {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link PasswordEntryLocalServiceUtil} to access the password entry local service. Add custom service methods to <code>com.liferay.portal.security.password.service.impl.PasswordEntryLocalServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */

	/**
	 * Add a passwordEntry for user of userId
	 *
	 * @param UserId, ID of user
	 * @param Password, plain text password
	 * @return Generated PassowrdEntry
	 * @throws PortalException
	 */
	public PasswordEntry addEntry(long userId, String password)
		throws PortalException;

	/**
	 * Adds the password entry to the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordEntry the password entry
	 * @return the password entry that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	public PasswordEntry addPasswordEntry(PasswordEntry passwordEntry);

	/**
	 * Creates a new password entry with the primary key. Does not add the password entry to the database.
	 *
	 * @param entryId the primary key for the new password entry
	 * @return the new password entry
	 */
	@Transactional(enabled = false)
	public PasswordEntry createPasswordEntry(long entryId);

	/**
	 * Remove all password entries for given user, including current password and history passwords.
	 *
	 * @param UserId, ID of user
	 * @throws PortalException
	 */
	public void deleteEntriesByUserId(long userId) throws PortalException;

	/**
	 * Remove all history password entries for given user.
	 *
	 * @param UserId, ID of user
	 * @throws PortalException
	 */
	public void deleteHistoryEntriesByUserId(long userId)
		throws PortalException;

	/**
	 * Deletes the password entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the password entry
	 * @return the password entry that was removed
	 * @throws PortalException if a password entry with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	public PasswordEntry deletePasswordEntry(long entryId)
		throws PortalException;

	/**
	 * Deletes the password entry from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordEntry the password entry
	 * @return the password entry that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	public PasswordEntry deletePasswordEntry(PasswordEntry passwordEntry)
		throws PortalException;

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public DynamicQuery dynamicQuery();

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery);

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end);

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator);

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long dynamicQueryCount(DynamicQuery dynamicQuery);

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PasswordEntry fetchPasswordEntry(long entryId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ActionableDynamicQuery getActionableDynamicQuery();

	/**
	 * Return all password entries for given user, including current password and history passwords. Ordered by modified date.
	 *
	 * @param UserId, ID of user
	 * @return A list of password entries
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<PasswordEntry> getEntriesByUserId(long userId);

	/**
	 * Return current password for given user.
	 *
	 * @param UserId, ID of user
	 * @return PasswordEntry
	 * @throws PortalException
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PasswordEntry getEntryByUserId(long userId) throws PortalException;

	/**
	 * Return history passwords for given user. Ordered by modified date.
	 *
	 * @param UserId, ID of user
	 * @return A list of Password entries the user used in the past.
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<PasswordEntry> getHistoryEntriesByUserId(long userId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery();

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<PasswordEntry> getPasswordEntries(int start, int end);

	/**
	 * Returns the number of password entries.
	 *
	 * @return the number of password entries
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getPasswordEntriesCount();

	/**
	 * Returns the password entry with the primary key.
	 *
	 * @param entryId the primary key of the password entry
	 * @return the password entry
	 * @throws PortalException if a password entry with the primary key could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PasswordEntry getPasswordEntry(long entryId) throws PortalException;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

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
	public PasswordEntry updateEntriesByUserId(long userId, String password)
		throws PortalException;

	/**
	 * Updates the password entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param passwordEntry the password entry
	 * @return the password entry that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	public PasswordEntry updatePasswordEntry(PasswordEntry passwordEntry);

}
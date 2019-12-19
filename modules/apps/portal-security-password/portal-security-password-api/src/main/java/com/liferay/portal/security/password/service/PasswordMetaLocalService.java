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
import com.liferay.portal.security.password.model.PasswordMeta;

import java.io.Serializable;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the local service interface for PasswordMeta. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author arthurchan35
 * @see PasswordMetaLocalServiceUtil
 * @generated
 */
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface PasswordMetaLocalService
	extends BaseLocalService, PersistedModelLocalService {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link PasswordMetaLocalServiceUtil} to access the password meta local service. Add custom service methods to <code>com.liferay.portal.security.password.service.impl.PasswordMetaLocalServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */

	/**
	 * Add a passwordMeta for the given passwordEntry during passwordEntry generation.
	 *
	 * @param PasswordEntry, the passwordEntry
	 * @return Generated meta for passwordEntry
	 * @throws PortalException
	 */
	public PasswordMeta addMetaByEntry(PasswordEntry passwordEntry)
		throws PortalException;

	/**
	 * Adds the password meta to the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordMeta the password meta
	 * @return the password meta that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	public PasswordMeta addPasswordMeta(PasswordMeta passwordMeta);

	/**
	 * Creates a new password meta with the primary key. Does not add the password meta to the database.
	 *
	 * @param metaId the primary key for the new password meta
	 * @return the new password meta
	 */
	@Transactional(enabled = false)
	public PasswordMeta createPasswordMeta(long metaId);

	public void deleteMetasByEntry(PasswordEntry passwordEntry)
		throws PortalException;

	public void deleteMetasByEntryId(long passwordEntryId)
		throws PortalException;

	/**
	 * Deletes the password meta with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param metaId the primary key of the password meta
	 * @return the password meta that was removed
	 * @throws PortalException if a password meta with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	public PasswordMeta deletePasswordMeta(long metaId) throws PortalException;

	/**
	 * Deletes the password meta from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordMeta the password meta
	 * @return the password meta that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	public PasswordMeta deletePasswordMeta(PasswordMeta passwordMeta)
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordMetaModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordMetaModelImpl</code>.
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
	public PasswordMeta fetchPasswordMeta(long metaId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ActionableDynamicQuery getActionableDynamicQuery();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery();

	/**
	 * Return a list of metas of PasswordEntry, ordered by modified date
	 *
	 * @param PasswordEntry, the passwordEntry
	 * @return a list of metas of PasswordEntry
	 * @throws PortalException
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<PasswordMeta> getMetasByEntry(PasswordEntry passwordEntry)
		throws PortalException;

	/**
	 * Return a list of metas of PasswordEntry, ordered by modified date
	 *
	 * @param PasswordEntryId, the ID of passwordEntry
	 * @return a list of metas of PasswordEntry
	 * @throws PortalException
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<PasswordMeta> getMetasByEntryId(long passwordEntryId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getMetasCountByEntry(PasswordEntry passwordEntry)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getMetasCountByEntryId(long passwordEntryId)
		throws PortalException;

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

	/**
	 * Returns the password meta with the primary key.
	 *
	 * @param metaId the primary key of the password meta
	 * @return the password meta
	 * @throws PortalException if a password meta with the primary key could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PasswordMeta getPasswordMeta(long metaId) throws PortalException;

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<PasswordMeta> getPasswordMetas(int start, int end);

	/**
	 * Returns the number of password metas.
	 *
	 * @return the number of password metas
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getPasswordMetasCount();

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

	/**
	 * When a password is being updated, it's metas will be reset to one that uses current Hash algorithm.
	 *
	 * @param PasswordEntry,the passwordEntry
	 * @return Newly generated single meta for passwordEntry
	 * @throws PortalException
	 */
	public PasswordMeta updateMetasByEntry(PasswordEntry passwordEntry)
		throws PortalException;

	/**
	 * When a password is being updated, it's metas will be reset to one that uses current Hash algorithm.
	 *
	 * @param PasswordEntryId, ID of passwordEntry
	 * @return Newly generated single meta for passwordEntry
	 * @throws PortalException
	 */
	public PasswordMeta updateMetasByEntryId(long passwordEntryId)
		throws PortalException;

	/**
	 * Updates the password meta in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param passwordMeta the password meta
	 * @return the password meta that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	public PasswordMeta updatePasswordMeta(PasswordMeta passwordMeta);

}
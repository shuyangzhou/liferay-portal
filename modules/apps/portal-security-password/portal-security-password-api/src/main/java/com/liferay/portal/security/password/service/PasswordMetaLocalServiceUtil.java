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

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for PasswordMeta. This utility wraps
 * <code>com.liferay.portal.security.password.service.impl.PasswordMetaLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author arthurchan35
 * @see PasswordMetaLocalService
 * @generated
 */
public class PasswordMetaLocalServiceUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.security.password.service.impl.PasswordMetaLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Add a passwordMeta for the given passwordEntry during passwordEntry generation.
	 *
	 * @param PasswordEntry, the passwordEntry
	 * @return Generated meta for passwordEntry
	 * @throws PortalException
	 */
	public static com.liferay.portal.security.password.model.PasswordMeta
			addMetaByEntry(
				com.liferay.portal.security.password.model.PasswordEntry
					passwordEntry)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addMetaByEntry(passwordEntry);
	}

	/**
	 * Adds the password meta to the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordMeta the password meta
	 * @return the password meta that was added
	 */
	public static com.liferay.portal.security.password.model.PasswordMeta
		addPasswordMeta(
			com.liferay.portal.security.password.model.PasswordMeta
				passwordMeta) {

		return getService().addPasswordMeta(passwordMeta);
	}

	/**
	 * Creates a new password meta with the primary key. Does not add the password meta to the database.
	 *
	 * @param metaId the primary key for the new password meta
	 * @return the new password meta
	 */
	public static com.liferay.portal.security.password.model.PasswordMeta
		createPasswordMeta(long metaId) {

		return getService().createPasswordMeta(metaId);
	}

	public static void deleteMetasByEntry(
			com.liferay.portal.security.password.model.PasswordEntry
				passwordEntry)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteMetasByEntry(passwordEntry);
	}

	public static void deleteMetasByEntryId(long passwordEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteMetasByEntryId(passwordEntryId);
	}

	/**
	 * Deletes the password meta with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param metaId the primary key of the password meta
	 * @return the password meta that was removed
	 * @throws PortalException if a password meta with the primary key could not be found
	 */
	public static com.liferay.portal.security.password.model.PasswordMeta
			deletePasswordMeta(long metaId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePasswordMeta(metaId);
	}

	/**
	 * Deletes the password meta from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordMeta the password meta
	 * @return the password meta that was removed
	 * @throws PortalException
	 */
	public static com.liferay.portal.security.password.model.PasswordMeta
			deletePasswordMeta(
				com.liferay.portal.security.password.model.PasswordMeta
					passwordMeta)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePasswordMeta(passwordMeta);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			deletePersistedModel(
				com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery
		dynamicQuery() {

		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQuery(dynamicQuery);
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
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
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
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static com.liferay.portal.security.password.model.PasswordMeta
		fetchPasswordMeta(long metaId) {

		return getService().fetchPasswordMeta(metaId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Return a list of metas of PasswordEntry, ordered by modified date
	 *
	 * @param PasswordEntry, the passwordEntry
	 * @return a list of metas of PasswordEntry
	 * @throws PortalException
	 */
	public static java.util.List
		<com.liferay.portal.security.password.model.PasswordMeta>
				getMetasByEntry(
					com.liferay.portal.security.password.model.PasswordEntry
						passwordEntry)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getMetasByEntry(passwordEntry);
	}

	/**
	 * Return a list of metas of PasswordEntry, ordered by modified date
	 *
	 * @param PasswordEntryId, the ID of passwordEntry
	 * @return a list of metas of PasswordEntry
	 * @throws PortalException
	 */
	public static java.util.List
		<com.liferay.portal.security.password.model.PasswordMeta>
				getMetasByEntryId(long passwordEntryId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getMetasByEntryId(passwordEntryId);
	}

	public static int getMetasCountByEntry(
			com.liferay.portal.security.password.model.PasswordEntry
				passwordEntry)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getMetasCountByEntry(passwordEntry);
	}

	public static int getMetasCountByEntryId(long passwordEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getMetasCountByEntryId(passwordEntryId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * Returns the password meta with the primary key.
	 *
	 * @param metaId the primary key of the password meta
	 * @return the password meta
	 * @throws PortalException if a password meta with the primary key could not be found
	 */
	public static com.liferay.portal.security.password.model.PasswordMeta
			getPasswordMeta(long metaId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPasswordMeta(metaId);
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
	public static java.util.List
		<com.liferay.portal.security.password.model.PasswordMeta>
			getPasswordMetas(int start, int end) {

		return getService().getPasswordMetas(start, end);
	}

	/**
	 * Returns the number of password metas.
	 *
	 * @return the number of password metas
	 */
	public static int getPasswordMetasCount() {
		return getService().getPasswordMetasCount();
	}

	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * When a password is being updated, it's metas will be reset to one that uses current Hash algorithm.
	 *
	 * @param PasswordEntry,the passwordEntry
	 * @return Newly generated single meta for passwordEntry
	 * @throws PortalException
	 */
	public static com.liferay.portal.security.password.model.PasswordMeta
			updateMetasByEntry(
				com.liferay.portal.security.password.model.PasswordEntry
					passwordEntry)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateMetasByEntry(passwordEntry);
	}

	/**
	 * When a password is being updated, it's metas will be reset to one that uses current Hash algorithm.
	 *
	 * @param PasswordEntryId, ID of passwordEntry
	 * @return Newly generated single meta for passwordEntry
	 * @throws PortalException
	 */
	public static com.liferay.portal.security.password.model.PasswordMeta
			updateMetasByEntryId(long passwordEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateMetasByEntryId(passwordEntryId);
	}

	/**
	 * Updates the password meta in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param passwordMeta the password meta
	 * @return the password meta that was updated
	 */
	public static com.liferay.portal.security.password.model.PasswordMeta
		updatePasswordMeta(
			com.liferay.portal.security.password.model.PasswordMeta
				passwordMeta) {

		return getService().updatePasswordMeta(passwordMeta);
	}

	public static PasswordMetaLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<PasswordMetaLocalService, PasswordMetaLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(PasswordMetaLocalService.class);

		ServiceTracker<PasswordMetaLocalService, PasswordMetaLocalService>
			serviceTracker =
				new ServiceTracker
					<PasswordMetaLocalService, PasswordMetaLocalService>(
						bundle.getBundleContext(),
						PasswordMetaLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}
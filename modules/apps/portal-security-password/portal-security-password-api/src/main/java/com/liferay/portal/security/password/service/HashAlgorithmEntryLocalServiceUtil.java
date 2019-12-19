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
 * Provides the local service utility for HashAlgorithmEntry. This utility wraps
 * <code>com.liferay.portal.security.password.service.impl.HashAlgorithmEntryLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author arthurchan35
 * @see HashAlgorithmEntryLocalService
 * @generated
 */
public class HashAlgorithmEntryLocalServiceUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.security.password.service.impl.HashAlgorithmEntryLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>HashAlgorithmEntryLocalService</code> via injection or a <code>ServiceTracker</code> or use <code>HashAlgorithmEntryLocalServiceUtil</code>.
	 */
	public static com.liferay.portal.security.password.model.HashAlgorithmEntry
			addEntry(String name, org.json.JSONObject meta)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addEntry(name, meta);
	}

	/**
	 * Adds the hash algorithm entry to the database. Also notifies the appropriate model listeners.
	 *
	 * @param hashAlgorithmEntry the hash algorithm entry
	 * @return the hash algorithm entry that was added
	 */
	public static com.liferay.portal.security.password.model.HashAlgorithmEntry
		addHashAlgorithmEntry(
			com.liferay.portal.security.password.model.HashAlgorithmEntry
				hashAlgorithmEntry) {

		return getService().addHashAlgorithmEntry(hashAlgorithmEntry);
	}

	/**
	 * Creates a new hash algorithm entry with the primary key. Does not add the hash algorithm entry to the database.
	 *
	 * @param entryId the primary key for the new hash algorithm entry
	 * @return the new hash algorithm entry
	 */
	public static com.liferay.portal.security.password.model.HashAlgorithmEntry
		createHashAlgorithmEntry(long entryId) {

		return getService().createHashAlgorithmEntry(entryId);
	}

	public static com.liferay.portal.security.password.model.HashAlgorithmEntry
			deleteEntry(
				com.liferay.portal.security.password.model.HashAlgorithmEntry
					entry)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteEntry(entry);
	}

	/**
	 * Deletes the hash algorithm entry from the database. Also notifies the appropriate model listeners.
	 *
	 * @param hashAlgorithmEntry the hash algorithm entry
	 * @return the hash algorithm entry that was removed
	 */
	public static com.liferay.portal.security.password.model.HashAlgorithmEntry
		deleteHashAlgorithmEntry(
			com.liferay.portal.security.password.model.HashAlgorithmEntry
				hashAlgorithmEntry) {

		return getService().deleteHashAlgorithmEntry(hashAlgorithmEntry);
	}

	/**
	 * Deletes the hash algorithm entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the hash algorithm entry
	 * @return the hash algorithm entry that was removed
	 * @throws PortalException if a hash algorithm entry with the primary key could not be found
	 */
	public static com.liferay.portal.security.password.model.HashAlgorithmEntry
			deleteHashAlgorithmEntry(long entryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteHashAlgorithmEntry(entryId);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.HashAlgorithmEntryModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.HashAlgorithmEntryModelImpl</code>.
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

	public static com.liferay.portal.security.password.model.HashAlgorithmEntry
		fetchHashAlgorithmEntry(long entryId) {

		return getService().fetchHashAlgorithmEntry(entryId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static com.liferay.portal.security.password.model.HashAlgorithmEntry
			getCurrentEntry()
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCurrentEntry();
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
	public static java.util.List
		<com.liferay.portal.security.password.model.HashAlgorithmEntry>
			getHashAlgorithmEntries(int start, int end) {

		return getService().getHashAlgorithmEntries(start, end);
	}

	/**
	 * Returns the number of hash algorithm entries.
	 *
	 * @return the number of hash algorithm entries
	 */
	public static int getHashAlgorithmEntriesCount() {
		return getService().getHashAlgorithmEntriesCount();
	}

	/**
	 * Returns the hash algorithm entry with the primary key.
	 *
	 * @param entryId the primary key of the hash algorithm entry
	 * @return the hash algorithm entry
	 * @throws PortalException if a hash algorithm entry with the primary key could not be found
	 */
	public static com.liferay.portal.security.password.model.HashAlgorithmEntry
			getHashAlgorithmEntry(long entryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getHashAlgorithmEntry(entryId);
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Updates the hash algorithm entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param hashAlgorithmEntry the hash algorithm entry
	 * @return the hash algorithm entry that was updated
	 */
	public static com.liferay.portal.security.password.model.HashAlgorithmEntry
		updateHashAlgorithmEntry(
			com.liferay.portal.security.password.model.HashAlgorithmEntry
				hashAlgorithmEntry) {

		return getService().updateHashAlgorithmEntry(hashAlgorithmEntry);
	}

	public static HashAlgorithmEntryLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<HashAlgorithmEntryLocalService, HashAlgorithmEntryLocalService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			HashAlgorithmEntryLocalService.class);

		ServiceTracker
			<HashAlgorithmEntryLocalService, HashAlgorithmEntryLocalService>
				serviceTracker =
					new ServiceTracker
						<HashAlgorithmEntryLocalService,
						 HashAlgorithmEntryLocalService>(
							 bundle.getBundleContext(),
							 HashAlgorithmEntryLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}
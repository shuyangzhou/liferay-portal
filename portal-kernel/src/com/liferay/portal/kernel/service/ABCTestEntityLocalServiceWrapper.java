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

package com.liferay.portal.kernel.service;

import aQute.bnd.annotation.ProviderType;

/**
 * Provides a wrapper for {@link ABCTestEntityLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see ABCTestEntityLocalService
 * @generated
 */
@ProviderType
public class ABCTestEntityLocalServiceWrapper
	implements ABCTestEntityLocalService,
		ServiceWrapper<ABCTestEntityLocalService> {
	public ABCTestEntityLocalServiceWrapper(
		ABCTestEntityLocalService abcTestEntityLocalService) {
		_abcTestEntityLocalService = abcTestEntityLocalService;
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _abcTestEntityLocalService.dynamicQuery();
	}

	/**
	* Adds the abc test entity to the database. Also notifies the appropriate model listeners.
	*
	* @param abcTestEntity the abc test entity
	* @return the abc test entity that was added
	*/
	@Override
	public com.liferay.portal.kernel.model.ABCTestEntity addABCTestEntity(
		com.liferay.portal.kernel.model.ABCTestEntity abcTestEntity) {
		return _abcTestEntityLocalService.addABCTestEntity(abcTestEntity);
	}

	/**
	* Creates a new abc test entity with the primary key. Does not add the abc test entity to the database.
	*
	* @param abcTestEntityId the primary key for the new abc test entity
	* @return the new abc test entity
	*/
	@Override
	public com.liferay.portal.kernel.model.ABCTestEntity createABCTestEntity(
		java.lang.String abcTestEntityId) {
		return _abcTestEntityLocalService.createABCTestEntity(abcTestEntityId);
	}

	/**
	* Deletes the abc test entity from the database. Also notifies the appropriate model listeners.
	*
	* @param abcTestEntity the abc test entity
	* @return the abc test entity that was removed
	*/
	@Override
	public com.liferay.portal.kernel.model.ABCTestEntity deleteABCTestEntity(
		com.liferay.portal.kernel.model.ABCTestEntity abcTestEntity) {
		return _abcTestEntityLocalService.deleteABCTestEntity(abcTestEntity);
	}

	/**
	* Deletes the abc test entity with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param abcTestEntityId the primary key of the abc test entity
	* @return the abc test entity that was removed
	* @throws PortalException if a abc test entity with the primary key could not be found
	*/
	@Override
	public com.liferay.portal.kernel.model.ABCTestEntity deleteABCTestEntity(
		java.lang.String abcTestEntityId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _abcTestEntityLocalService.deleteABCTestEntity(abcTestEntityId);
	}

	@Override
	public com.liferay.portal.kernel.model.ABCTestEntity fetchABCTestEntity(
		java.lang.String abcTestEntityId) {
		return _abcTestEntityLocalService.fetchABCTestEntity(abcTestEntityId);
	}

	/**
	* Returns the abc test entity with the primary key.
	*
	* @param abcTestEntityId the primary key of the abc test entity
	* @return the abc test entity
	* @throws PortalException if a abc test entity with the primary key could not be found
	*/
	@Override
	public com.liferay.portal.kernel.model.ABCTestEntity getABCTestEntity(
		java.lang.String abcTestEntityId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _abcTestEntityLocalService.getABCTestEntity(abcTestEntityId);
	}

	/**
	* Updates the abc test entity in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param abcTestEntity the abc test entity
	* @return the abc test entity that was updated
	*/
	@Override
	public com.liferay.portal.kernel.model.ABCTestEntity updateABCTestEntity(
		com.liferay.portal.kernel.model.ABCTestEntity abcTestEntity) {
		return _abcTestEntityLocalService.updateABCTestEntity(abcTestEntity);
	}

	@Override
	public com.liferay.portal.kernel.model.ABCTestEntityLocalization fetchABCTestEntityLocalization(
		java.lang.String abcTestEntityId, java.lang.String languageId) {
		return _abcTestEntityLocalService.fetchABCTestEntityLocalization(abcTestEntityId,
			languageId);
	}

	@Override
	public com.liferay.portal.kernel.model.ABCTestEntityLocalization getABCTestEntityLocalization(
		java.lang.String abcTestEntityId, java.lang.String languageId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _abcTestEntityLocalService.getABCTestEntityLocalization(abcTestEntityId,
			languageId);
	}

	/**
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
		com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _abcTestEntityLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _abcTestEntityLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	* Returns the number of abc test entities.
	*
	* @return the number of abc test entities
	*/
	@Override
	public int getABCTestEntitiesCount() {
		return _abcTestEntityLocalService.getABCTestEntitiesCount();
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public java.lang.String getOSGiServiceIdentifier() {
		return _abcTestEntityLocalService.getOSGiServiceIdentifier();
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
		return _abcTestEntityLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.ABCTestEntityModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return _abcTestEntityLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.ABCTestEntityModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return _abcTestEntityLocalService.dynamicQuery(dynamicQuery, start,
			end, orderByComparator);
	}

	/**
	* Returns a range of all the abc test entities.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.ABCTestEntityModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of abc test entities
	* @param end the upper bound of the range of abc test entities (not inclusive)
	* @return the range of abc test entities
	*/
	@Override
	public java.util.List<com.liferay.portal.kernel.model.ABCTestEntity> getABCTestEntities(
		int start, int end) {
		return _abcTestEntityLocalService.getABCTestEntities(start, end);
	}

	@Override
	public java.util.List<com.liferay.portal.kernel.model.ABCTestEntityLocalization> getABCTestEntityLocalizations(
		java.lang.String abcTestEntityId) {
		return _abcTestEntityLocalService.getABCTestEntityLocalizations(abcTestEntityId);
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
		return _abcTestEntityLocalService.dynamicQueryCount(dynamicQuery);
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
		return _abcTestEntityLocalService.dynamicQueryCount(dynamicQuery,
			projection);
	}

	@Override
	public ABCTestEntityLocalService getWrappedService() {
		return _abcTestEntityLocalService;
	}

	@Override
	public void setWrappedService(
		ABCTestEntityLocalService abcTestEntityLocalService) {
		_abcTestEntityLocalService = abcTestEntityLocalService;
	}

	private ABCTestEntityLocalService _abcTestEntityLocalService;
}
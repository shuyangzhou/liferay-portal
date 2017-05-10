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

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.model.ABCTestEntity;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ReferenceRegistry;

import java.util.List;

/**
 * The persistence utility for the abc test entity service. This utility wraps {@link com.liferay.portal.service.persistence.impl.ABCTestEntityPersistenceImpl} and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ABCTestEntityPersistence
 * @see com.liferay.portal.service.persistence.impl.ABCTestEntityPersistenceImpl
 * @generated
 */
@ProviderType
public class ABCTestEntityUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(ABCTestEntity abcTestEntity) {
		getPersistence().clearCache(abcTestEntity);
	}

	/**
	 * @see BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<ABCTestEntity> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<ABCTestEntity> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {
		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<ABCTestEntity> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<ABCTestEntity> orderByComparator) {
		return getPersistence()
				   .findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * @see BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static ABCTestEntity update(ABCTestEntity abcTestEntity) {
		return getPersistence().update(abcTestEntity);
	}

	/**
	 * @see BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static ABCTestEntity update(ABCTestEntity abcTestEntity,
		ServiceContext serviceContext) {
		return getPersistence().update(abcTestEntity, serviceContext);
	}

	/**
	* Caches the abc test entity in the entity cache if it is enabled.
	*
	* @param abcTestEntity the abc test entity
	*/
	public static void cacheResult(ABCTestEntity abcTestEntity) {
		getPersistence().cacheResult(abcTestEntity);
	}

	/**
	* Caches the abc test entities in the entity cache if it is enabled.
	*
	* @param abcTestEntities the abc test entities
	*/
	public static void cacheResult(List<ABCTestEntity> abcTestEntities) {
		getPersistence().cacheResult(abcTestEntities);
	}

	/**
	* Creates a new abc test entity with the primary key. Does not add the abc test entity to the database.
	*
	* @param abcTestEntityId the primary key for the new abc test entity
	* @return the new abc test entity
	*/
	public static ABCTestEntity create(java.lang.String abcTestEntityId) {
		return getPersistence().create(abcTestEntityId);
	}

	/**
	* Removes the abc test entity with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param abcTestEntityId the primary key of the abc test entity
	* @return the abc test entity that was removed
	* @throws NoSuchABCTestEntityException if a abc test entity with the primary key could not be found
	*/
	public static ABCTestEntity remove(java.lang.String abcTestEntityId)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityException {
		return getPersistence().remove(abcTestEntityId);
	}

	public static ABCTestEntity updateImpl(ABCTestEntity abcTestEntity) {
		return getPersistence().updateImpl(abcTestEntity);
	}

	/**
	* Returns the abc test entity with the primary key or throws a {@link NoSuchABCTestEntityException} if it could not be found.
	*
	* @param abcTestEntityId the primary key of the abc test entity
	* @return the abc test entity
	* @throws NoSuchABCTestEntityException if a abc test entity with the primary key could not be found
	*/
	public static ABCTestEntity findByPrimaryKey(
		java.lang.String abcTestEntityId)
		throws com.liferay.portal.kernel.exception.NoSuchABCTestEntityException {
		return getPersistence().findByPrimaryKey(abcTestEntityId);
	}

	/**
	* Returns the abc test entity with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param abcTestEntityId the primary key of the abc test entity
	* @return the abc test entity, or <code>null</code> if a abc test entity with the primary key could not be found
	*/
	public static ABCTestEntity fetchByPrimaryKey(
		java.lang.String abcTestEntityId) {
		return getPersistence().fetchByPrimaryKey(abcTestEntityId);
	}

	public static java.util.Map<java.io.Serializable, ABCTestEntity> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys) {
		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	* Returns all the abc test entities.
	*
	* @return the abc test entities
	*/
	public static List<ABCTestEntity> findAll() {
		return getPersistence().findAll();
	}

	/**
	* Returns a range of all the abc test entities.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of abc test entities
	* @param end the upper bound of the range of abc test entities (not inclusive)
	* @return the range of abc test entities
	*/
	public static List<ABCTestEntity> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	* Returns an ordered range of all the abc test entities.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of abc test entities
	* @param end the upper bound of the range of abc test entities (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of abc test entities
	*/
	public static List<ABCTestEntity> findAll(int start, int end,
		OrderByComparator<ABCTestEntity> orderByComparator) {
		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	* Returns an ordered range of all the abc test entities.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ABCTestEntityModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of abc test entities
	* @param end the upper bound of the range of abc test entities (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of abc test entities
	*/
	public static List<ABCTestEntity> findAll(int start, int end,
		OrderByComparator<ABCTestEntity> orderByComparator,
		boolean retrieveFromCache) {
		return getPersistence()
				   .findAll(start, end, orderByComparator, retrieveFromCache);
	}

	/**
	* Removes all the abc test entities from the database.
	*/
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	* Returns the number of abc test entities.
	*
	* @return the number of abc test entities
	*/
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static ABCTestEntityPersistence getPersistence() {
		if (_persistence == null) {
			_persistence = (ABCTestEntityPersistence)PortalBeanLocatorUtil.locate(ABCTestEntityPersistence.class.getName());

			ReferenceRegistry.registerReference(ABCTestEntityUtil.class,
				"_persistence");
		}

		return _persistence;
	}

	private static ABCTestEntityPersistence _persistence;
}
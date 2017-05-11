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

import com.liferay.portal.kernel.exception.NoSuchABCTestEntityException;
import com.liferay.portal.kernel.model.ABCTestEntity;

/**
 * The persistence interface for the abc test entity service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portal.service.persistence.impl.ABCTestEntityPersistenceImpl
 * @see ABCTestEntityUtil
 * @generated
 */
@ProviderType
public interface ABCTestEntityPersistence extends BasePersistence<ABCTestEntity> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link ABCTestEntityUtil} to access the abc test entity persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Caches the abc test entity in the entity cache if it is enabled.
	*
	* @param abcTestEntity the abc test entity
	*/
	public void cacheResult(ABCTestEntity abcTestEntity);

	/**
	* Caches the abc test entities in the entity cache if it is enabled.
	*
	* @param abcTestEntities the abc test entities
	*/
	public void cacheResult(java.util.List<ABCTestEntity> abcTestEntities);

	/**
	* Creates a new abc test entity with the primary key. Does not add the abc test entity to the database.
	*
	* @param abcTestEntityId the primary key for the new abc test entity
	* @return the new abc test entity
	*/
	public ABCTestEntity create(java.lang.String abcTestEntityId);

	/**
	* Removes the abc test entity with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param abcTestEntityId the primary key of the abc test entity
	* @return the abc test entity that was removed
	* @throws NoSuchABCTestEntityException if a abc test entity with the primary key could not be found
	*/
	public ABCTestEntity remove(java.lang.String abcTestEntityId)
		throws NoSuchABCTestEntityException;

	public ABCTestEntity updateImpl(ABCTestEntity abcTestEntity);

	/**
	* Returns the abc test entity with the primary key or throws a {@link NoSuchABCTestEntityException} if it could not be found.
	*
	* @param abcTestEntityId the primary key of the abc test entity
	* @return the abc test entity
	* @throws NoSuchABCTestEntityException if a abc test entity with the primary key could not be found
	*/
	public ABCTestEntity findByPrimaryKey(java.lang.String abcTestEntityId)
		throws NoSuchABCTestEntityException;

	/**
	* Returns the abc test entity with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param abcTestEntityId the primary key of the abc test entity
	* @return the abc test entity, or <code>null</code> if a abc test entity with the primary key could not be found
	*/
	public ABCTestEntity fetchByPrimaryKey(java.lang.String abcTestEntityId);

	@Override
	public java.util.Map<java.io.Serializable, ABCTestEntity> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the abc test entities.
	*
	* @return the abc test entities
	*/
	public java.util.List<ABCTestEntity> findAll();

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
	public java.util.List<ABCTestEntity> findAll(int start, int end);

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
	public java.util.List<ABCTestEntity> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntity> orderByComparator);

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
	public java.util.List<ABCTestEntity> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ABCTestEntity> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Removes all the abc test entities from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of abc test entities.
	*
	* @return the number of abc test entities
	*/
	public int countAll();
}
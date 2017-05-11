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

package com.liferay.portal.service.persistence.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.NoSuchABCTestEntityException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ABCTestEntity;
import com.liferay.portal.kernel.service.persistence.ABCTestEntityPersistence;
import com.liferay.portal.kernel.service.persistence.CompanyProvider;
import com.liferay.portal.kernel.service.persistence.CompanyProviderWrapper;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.impl.ABCTestEntityImpl;
import com.liferay.portal.model.impl.ABCTestEntityModelImpl;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence implementation for the abc test entity service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ABCTestEntityPersistence
 * @see com.liferay.portal.kernel.service.persistence.ABCTestEntityUtil
 * @generated
 */
@ProviderType
public class ABCTestEntityPersistenceImpl extends BasePersistenceImpl<ABCTestEntity>
	implements ABCTestEntityPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link ABCTestEntityUtil} to access the abc test entity persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = ABCTestEntityImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(ABCTestEntityModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityModelImpl.FINDER_CACHE_ENABLED,
			ABCTestEntityImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(ABCTestEntityModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityModelImpl.FINDER_CACHE_ENABLED,
			ABCTestEntityImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(ABCTestEntityModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	public ABCTestEntityPersistenceImpl() {
		setModelClass(ABCTestEntity.class);
	}

	/**
	 * Caches the abc test entity in the entity cache if it is enabled.
	 *
	 * @param abcTestEntity the abc test entity
	 */
	@Override
	public void cacheResult(ABCTestEntity abcTestEntity) {
		entityCache.putResult(ABCTestEntityModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityImpl.class, abcTestEntity.getPrimaryKey(),
			abcTestEntity);

		abcTestEntity.resetOriginalValues();
	}

	/**
	 * Caches the abc test entities in the entity cache if it is enabled.
	 *
	 * @param abcTestEntities the abc test entities
	 */
	@Override
	public void cacheResult(List<ABCTestEntity> abcTestEntities) {
		for (ABCTestEntity abcTestEntity : abcTestEntities) {
			if (entityCache.getResult(
						ABCTestEntityModelImpl.ENTITY_CACHE_ENABLED,
						ABCTestEntityImpl.class, abcTestEntity.getPrimaryKey()) == null) {
				cacheResult(abcTestEntity);
			}
			else {
				abcTestEntity.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all abc test entities.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(ABCTestEntityImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the abc test entity.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(ABCTestEntity abcTestEntity) {
		entityCache.removeResult(ABCTestEntityModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityImpl.class, abcTestEntity.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<ABCTestEntity> abcTestEntities) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (ABCTestEntity abcTestEntity : abcTestEntities) {
			entityCache.removeResult(ABCTestEntityModelImpl.ENTITY_CACHE_ENABLED,
				ABCTestEntityImpl.class, abcTestEntity.getPrimaryKey());
		}
	}

	/**
	 * Creates a new abc test entity with the primary key. Does not add the abc test entity to the database.
	 *
	 * @param abcTestEntityId the primary key for the new abc test entity
	 * @return the new abc test entity
	 */
	@Override
	public ABCTestEntity create(String abcTestEntityId) {
		ABCTestEntity abcTestEntity = new ABCTestEntityImpl();

		abcTestEntity.setNew(true);
		abcTestEntity.setPrimaryKey(abcTestEntityId);

		abcTestEntity.setCompanyId(companyProvider.getCompanyId());

		return abcTestEntity;
	}

	/**
	 * Removes the abc test entity with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param abcTestEntityId the primary key of the abc test entity
	 * @return the abc test entity that was removed
	 * @throws NoSuchABCTestEntityException if a abc test entity with the primary key could not be found
	 */
	@Override
	public ABCTestEntity remove(String abcTestEntityId)
		throws NoSuchABCTestEntityException {
		return remove((Serializable)abcTestEntityId);
	}

	/**
	 * Removes the abc test entity with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the abc test entity
	 * @return the abc test entity that was removed
	 * @throws NoSuchABCTestEntityException if a abc test entity with the primary key could not be found
	 */
	@Override
	public ABCTestEntity remove(Serializable primaryKey)
		throws NoSuchABCTestEntityException {
		Session session = null;

		try {
			session = openSession();

			ABCTestEntity abcTestEntity = (ABCTestEntity)session.get(ABCTestEntityImpl.class,
					primaryKey);

			if (abcTestEntity == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchABCTestEntityException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(abcTestEntity);
		}
		catch (NoSuchABCTestEntityException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected ABCTestEntity removeImpl(ABCTestEntity abcTestEntity) {
		abcTestEntity = toUnwrappedModel(abcTestEntity);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(abcTestEntity)) {
				abcTestEntity = (ABCTestEntity)session.get(ABCTestEntityImpl.class,
						abcTestEntity.getPrimaryKeyObj());
			}

			if (abcTestEntity != null) {
				session.delete(abcTestEntity);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (abcTestEntity != null) {
			clearCache(abcTestEntity);
		}

		return abcTestEntity;
	}

	@Override
	public ABCTestEntity updateImpl(ABCTestEntity abcTestEntity) {
		abcTestEntity = toUnwrappedModel(abcTestEntity);

		boolean isNew = abcTestEntity.isNew();

		Session session = null;

		try {
			session = openSession();

			if (abcTestEntity.isNew()) {
				session.save(abcTestEntity);

				abcTestEntity.setNew(false);
			}
			else {
				abcTestEntity = (ABCTestEntity)session.merge(abcTestEntity);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew) {
			finderCache.removeResult(FINDER_PATH_COUNT_ALL, FINDER_ARGS_EMPTY);
			finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL,
				FINDER_ARGS_EMPTY);
		}

		entityCache.putResult(ABCTestEntityModelImpl.ENTITY_CACHE_ENABLED,
			ABCTestEntityImpl.class, abcTestEntity.getPrimaryKey(),
			abcTestEntity, false);

		abcTestEntity.resetOriginalValues();

		return abcTestEntity;
	}

	protected ABCTestEntity toUnwrappedModel(ABCTestEntity abcTestEntity) {
		if (abcTestEntity instanceof ABCTestEntityImpl) {
			return abcTestEntity;
		}

		ABCTestEntityImpl abcTestEntityImpl = new ABCTestEntityImpl();

		abcTestEntityImpl.setNew(abcTestEntity.isNew());
		abcTestEntityImpl.setPrimaryKey(abcTestEntity.getPrimaryKey());

		abcTestEntityImpl.setMvccVersion(abcTestEntity.getMvccVersion());
		abcTestEntityImpl.setAbcTestEntityId(abcTestEntity.getAbcTestEntityId());
		abcTestEntityImpl.setCompanyId(abcTestEntity.getCompanyId());
		abcTestEntityImpl.setGroupId(abcTestEntity.getGroupId());
		abcTestEntityImpl.setDefaultLanguageId(abcTestEntity.getDefaultLanguageId());

		return abcTestEntityImpl;
	}

	/**
	 * Returns the abc test entity with the primary key or throws a {@link com.liferay.portal.kernel.exception.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the abc test entity
	 * @return the abc test entity
	 * @throws NoSuchABCTestEntityException if a abc test entity with the primary key could not be found
	 */
	@Override
	public ABCTestEntity findByPrimaryKey(Serializable primaryKey)
		throws NoSuchABCTestEntityException {
		ABCTestEntity abcTestEntity = fetchByPrimaryKey(primaryKey);

		if (abcTestEntity == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchABCTestEntityException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return abcTestEntity;
	}

	/**
	 * Returns the abc test entity with the primary key or throws a {@link NoSuchABCTestEntityException} if it could not be found.
	 *
	 * @param abcTestEntityId the primary key of the abc test entity
	 * @return the abc test entity
	 * @throws NoSuchABCTestEntityException if a abc test entity with the primary key could not be found
	 */
	@Override
	public ABCTestEntity findByPrimaryKey(String abcTestEntityId)
		throws NoSuchABCTestEntityException {
		return findByPrimaryKey((Serializable)abcTestEntityId);
	}

	/**
	 * Returns the abc test entity with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the abc test entity
	 * @return the abc test entity, or <code>null</code> if a abc test entity with the primary key could not be found
	 */
	@Override
	public ABCTestEntity fetchByPrimaryKey(Serializable primaryKey) {
		Serializable serializable = entityCache.getResult(ABCTestEntityModelImpl.ENTITY_CACHE_ENABLED,
				ABCTestEntityImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		ABCTestEntity abcTestEntity = (ABCTestEntity)serializable;

		if (abcTestEntity == null) {
			Session session = null;

			try {
				session = openSession();

				abcTestEntity = (ABCTestEntity)session.get(ABCTestEntityImpl.class,
						primaryKey);

				if (abcTestEntity != null) {
					cacheResult(abcTestEntity);
				}
				else {
					entityCache.putResult(ABCTestEntityModelImpl.ENTITY_CACHE_ENABLED,
						ABCTestEntityImpl.class, primaryKey, nullModel);
				}
			}
			catch (Exception e) {
				entityCache.removeResult(ABCTestEntityModelImpl.ENTITY_CACHE_ENABLED,
					ABCTestEntityImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return abcTestEntity;
	}

	/**
	 * Returns the abc test entity with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param abcTestEntityId the primary key of the abc test entity
	 * @return the abc test entity, or <code>null</code> if a abc test entity with the primary key could not be found
	 */
	@Override
	public ABCTestEntity fetchByPrimaryKey(String abcTestEntityId) {
		return fetchByPrimaryKey((Serializable)abcTestEntityId);
	}

	@Override
	public Map<Serializable, ABCTestEntity> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, ABCTestEntity> map = new HashMap<Serializable, ABCTestEntity>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			ABCTestEntity abcTestEntity = fetchByPrimaryKey(primaryKey);

			if (abcTestEntity != null) {
				map.put(primaryKey, abcTestEntity);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = entityCache.getResult(ABCTestEntityModelImpl.ENTITY_CACHE_ENABLED,
					ABCTestEntityImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(primaryKey, (ABCTestEntity)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_ABCTESTENTITY_WHERE_PKS_IN);

		for (int i = 0; i < uncachedPrimaryKeys.size(); i++) {
			query.append(StringPool.QUESTION);

			query.append(StringPool.COMMA);
		}

		query.setIndex(query.index() - 1);

		query.append(StringPool.CLOSE_PARENTHESIS);

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			QueryPos qPos = QueryPos.getInstance(q);

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				qPos.add((String)primaryKey);
			}

			for (ABCTestEntity abcTestEntity : (List<ABCTestEntity>)q.list()) {
				map.put(abcTestEntity.getPrimaryKeyObj(), abcTestEntity);

				cacheResult(abcTestEntity);

				uncachedPrimaryKeys.remove(abcTestEntity.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(ABCTestEntityModelImpl.ENTITY_CACHE_ENABLED,
					ABCTestEntityImpl.class, primaryKey, nullModel);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the abc test entities.
	 *
	 * @return the abc test entities
	 */
	@Override
	public List<ABCTestEntity> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<ABCTestEntity> findAll(int start, int end) {
		return findAll(start, end, null);
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
	@Override
	public List<ABCTestEntity> findAll(int start, int end,
		OrderByComparator<ABCTestEntity> orderByComparator) {
		return findAll(start, end, orderByComparator, true);
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
	@Override
	public List<ABCTestEntity> findAll(int start, int end,
		OrderByComparator<ABCTestEntity> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<ABCTestEntity> list = null;

		if (retrieveFromCache) {
			list = (List<ABCTestEntity>)finderCache.getResult(finderPath,
					finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_ABCTESTENTITY);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_ABCTESTENTITY;

				if (pagination) {
					sql = sql.concat(ABCTestEntityModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<ABCTestEntity>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<ABCTestEntity>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the abc test entities from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (ABCTestEntity abcTestEntity : findAll()) {
			remove(abcTestEntity);
		}
	}

	/**
	 * Returns the number of abc test entities.
	 *
	 * @return the number of abc test entities
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_ABCTESTENTITY);

				count = (Long)q.uniqueResult();

				finderCache.putResult(FINDER_PATH_COUNT_ALL, FINDER_ARGS_EMPTY,
					count);
			}
			catch (Exception e) {
				finderCache.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return ABCTestEntityModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the abc test entity persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		entityCache.removeCache(ABCTestEntityImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@BeanReference(type = CompanyProviderWrapper.class)
	protected CompanyProvider companyProvider;
	protected EntityCache entityCache = EntityCacheUtil.getEntityCache();
	protected FinderCache finderCache = FinderCacheUtil.getFinderCache();
	private static final String _SQL_SELECT_ABCTESTENTITY = "SELECT abcTestEntity FROM ABCTestEntity abcTestEntity";
	private static final String _SQL_SELECT_ABCTESTENTITY_WHERE_PKS_IN = "SELECT abcTestEntity FROM ABCTestEntity abcTestEntity WHERE abcTestEntityId IN (";
	private static final String _SQL_COUNT_ABCTESTENTITY = "SELECT COUNT(abcTestEntity) FROM ABCTestEntity abcTestEntity";
	private static final String _ORDER_BY_ENTITY_ALIAS = "abcTestEntity.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No ABCTestEntity exists with the primary key ";
	private static final Log _log = LogFactoryUtil.getLog(ABCTestEntityPersistenceImpl.class);
}
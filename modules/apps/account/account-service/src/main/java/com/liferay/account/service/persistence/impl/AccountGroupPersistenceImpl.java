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

package com.liferay.account.service.persistence.impl;

import com.liferay.account.exception.NoSuchGroupException;
import com.liferay.account.model.AccountGroup;
import com.liferay.account.model.AccountGroupTable;
import com.liferay.account.model.impl.AccountGroupImpl;
import com.liferay.account.model.impl.AccountGroupModelImpl;
import com.liferay.account.service.persistence.AccountGroupPersistence;
import com.liferay.account.service.persistence.impl.constants.AccountPersistenceConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import javax.sql.DataSource;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the account group service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = AccountGroupPersistence.class)
public class AccountGroupPersistenceImpl
	extends BasePersistenceImpl<AccountGroup>
	implements AccountGroupPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>AccountGroupUtil</code> to access the account group persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		AccountGroupImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	/**
	 * Returns the account group where companyId = &#63; and externalReferenceCode = &#63; or throws a <code>NoSuchGroupException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching account group
	 * @throws NoSuchGroupException if a matching account group could not be found
	 */
	@Override
	public AccountGroup findByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchGroupException {

		AccountGroup accountGroup = fetchByC_ERC(
			companyId, externalReferenceCode);

		if (accountGroup == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("companyId=");
			sb.append(companyId);

			sb.append(", externalReferenceCode=");
			sb.append(externalReferenceCode);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchGroupException(sb.toString());
		}

		return accountGroup;
	}

	/**
	 * Returns the account group where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching account group, or <code>null</code> if a matching account group could not be found
	 */
	@Override
	public AccountGroup fetchByC_ERC(
		long companyId, String externalReferenceCode) {

		return fetchByC_ERC(companyId, externalReferenceCode, true);
	}

	/**
	 * Returns the account group where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching account group, or <code>null</code> if a matching account group could not be found
	 */
	@Override
	public AccountGroup fetchByC_ERC(
		long companyId, String externalReferenceCode, boolean useFinderCache) {

		externalReferenceCode = Objects.toString(externalReferenceCode, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {companyId, externalReferenceCode};
		}

		Object result = null;

		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByC_ERC");

		if (useFinderCache) {
			result = finderCache.getResult(finderPath, finderArgs, this);
		}

		if (result instanceof AccountGroup) {
			AccountGroup accountGroup = (AccountGroup)result;

			if ((companyId != accountGroup.getCompanyId()) ||
				!Objects.equals(
					externalReferenceCode,
					accountGroup.getExternalReferenceCode())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_ACCOUNTGROUP_WHERE);

			sb.append(_FINDER_COLUMN_C_ERC_COMPANYID_2);

			boolean bindExternalReferenceCode = false;

			if (externalReferenceCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_3);
			}
			else {
				bindExternalReferenceCode = true;

				sb.append(_FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindExternalReferenceCode) {
					queryPos.add(externalReferenceCode);
				}

				List<AccountGroup> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(finderPath, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {
									companyId, externalReferenceCode
								};
							}

							_log.warn(
								"AccountGroupPersistenceImpl.fetchByC_ERC(long, String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					AccountGroup accountGroup = list.get(0);

					result = accountGroup;

					cacheResult(accountGroup);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (AccountGroup)result;
		}
	}

	/**
	 * Removes the account group where companyId = &#63; and externalReferenceCode = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the account group that was removed
	 */
	@Override
	public AccountGroup removeByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchGroupException {

		AccountGroup accountGroup = findByC_ERC(
			companyId, externalReferenceCode);

		return remove(accountGroup);
	}

	/**
	 * Returns the number of account groups where companyId = &#63; and externalReferenceCode = &#63;.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the number of matching account groups
	 */
	@Override
	public int countByC_ERC(long companyId, String externalReferenceCode) {
		externalReferenceCode = Objects.toString(externalReferenceCode, "");

		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_ERC");

		Object[] finderArgs = new Object[] {companyId, externalReferenceCode};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_ACCOUNTGROUP_WHERE);

			sb.append(_FINDER_COLUMN_C_ERC_COMPANYID_2);

			boolean bindExternalReferenceCode = false;

			if (externalReferenceCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_3);
			}
			else {
				bindExternalReferenceCode = true;

				sb.append(_FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindExternalReferenceCode) {
					queryPos.add(externalReferenceCode);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_C_ERC_COMPANYID_2 =
		"accountGroup.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_2 =
		"accountGroup.externalReferenceCode = ?";

	private static final String _FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_3 =
		"(accountGroup.externalReferenceCode IS NULL OR accountGroup.externalReferenceCode = '')";

	public AccountGroupPersistenceImpl() {
		setModelClass(AccountGroup.class);

		setModelImplClass(AccountGroupImpl.class);
		setModelPKClass(long.class);

		setTable(AccountGroupTable.INSTANCE);
	}

	/**
	 * Caches the account group in the entity cache if it is enabled.
	 *
	 * @param accountGroup the account group
	 */
	@Override
	public void cacheResult(AccountGroup accountGroup) {
		entityCache.putResult(
			entityCacheEnabled, AccountGroupImpl.class,
			accountGroup.getPrimaryKey(), accountGroup,
			new Object[] {
				_columnBitmaskEnabled,
				((AccountGroupModelImpl)accountGroup).getColumnBitmask()
			});

		finderCache.putResult(
			_getFinderPath(FINDER_CLASS_NAME_ENTITY, "fetchByC_ERC"),
			new Object[] {
				accountGroup.getCompanyId(),
				accountGroup.getExternalReferenceCode()
			},
			accountGroup);

		accountGroup.resetOriginalValues();
	}

	/**
	 * Caches the account groups in the entity cache if it is enabled.
	 *
	 * @param accountGroups the account groups
	 */
	@Override
	public void cacheResult(List<AccountGroup> accountGroups) {
		for (AccountGroup accountGroup : accountGroups) {
			if (entityCache.getResult(
					entityCacheEnabled, AccountGroupImpl.class,
					accountGroup.getPrimaryKey()) == null) {

				cacheResult(accountGroup);
			}
			else {
				accountGroup.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all account groups.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(AccountGroupImpl.class);
	}

	/**
	 * Clears the cache for the account group.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(AccountGroup accountGroup) {
		entityCache.removeResult(
			entityCacheEnabled, AccountGroupImpl.class,
			accountGroup.getPrimaryKey(), accountGroup,
			new Object[] {
				_columnBitmaskEnabled,
				((AccountGroupModelImpl)accountGroup).getColumnBitmask()
			});
	}

	@Override
	public void clearCache(List<AccountGroup> accountGroups) {
		for (AccountGroup accountGroup : accountGroups) {
			entityCache.removeResult(
				entityCacheEnabled, AccountGroupImpl.class,
				accountGroup.getPrimaryKey(), accountGroup,
				new Object[] {
					_columnBitmaskEnabled,
					((AccountGroupModelImpl)accountGroup).getColumnBitmask()
				});
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				entityCacheEnabled, AccountGroupImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		AccountGroupModelImpl accountGroupModelImpl) {

		Object[] args = new Object[] {
			accountGroupModelImpl.getCompanyId(),
			accountGroupModelImpl.getExternalReferenceCode()
		};

		finderCache.putResult(
			_getFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_ERC"),
			args, Long.valueOf(1), false);
		finderCache.putResult(
			_getFinderPath(FINDER_CLASS_NAME_ENTITY, "fetchByC_ERC"), args,
			accountGroupModelImpl, false);
	}

	/**
	 * Creates a new account group with the primary key. Does not add the account group to the database.
	 *
	 * @param accountGroupId the primary key for the new account group
	 * @return the new account group
	 */
	@Override
	public AccountGroup create(long accountGroupId) {
		AccountGroup accountGroup = new AccountGroupImpl();

		accountGroup.setNew(true);
		accountGroup.setPrimaryKey(accountGroupId);

		accountGroup.setCompanyId(CompanyThreadLocal.getCompanyId());

		return accountGroup;
	}

	/**
	 * Removes the account group with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param accountGroupId the primary key of the account group
	 * @return the account group that was removed
	 * @throws NoSuchGroupException if a account group with the primary key could not be found
	 */
	@Override
	public AccountGroup remove(long accountGroupId)
		throws NoSuchGroupException {

		return remove((Serializable)accountGroupId);
	}

	/**
	 * Removes the account group with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the account group
	 * @return the account group that was removed
	 * @throws NoSuchGroupException if a account group with the primary key could not be found
	 */
	@Override
	public AccountGroup remove(Serializable primaryKey)
		throws NoSuchGroupException {

		Session session = null;

		try {
			session = openSession();

			AccountGroup accountGroup = (AccountGroup)session.get(
				AccountGroupImpl.class, primaryKey);

			if (accountGroup == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchGroupException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(accountGroup);
		}
		catch (NoSuchGroupException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected AccountGroup removeImpl(AccountGroup accountGroup) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(accountGroup)) {
				accountGroup = (AccountGroup)session.get(
					AccountGroupImpl.class, accountGroup.getPrimaryKeyObj());
			}

			if (accountGroup != null) {
				session.delete(accountGroup);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (accountGroup != null) {
			clearCache(accountGroup);
		}

		return accountGroup;
	}

	@Override
	public AccountGroup updateImpl(AccountGroup accountGroup) {
		boolean isNew = accountGroup.isNew();

		if (!(accountGroup instanceof AccountGroupModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(accountGroup.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					accountGroup);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in accountGroup proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom AccountGroup implementation " +
					accountGroup.getClass());
		}

		AccountGroupModelImpl accountGroupModelImpl =
			(AccountGroupModelImpl)accountGroup;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (accountGroup.getCreateDate() == null)) {
			if (serviceContext == null) {
				accountGroup.setCreateDate(now);
			}
			else {
				accountGroup.setCreateDate(serviceContext.getCreateDate(now));
			}
		}

		if (!accountGroupModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				accountGroup.setModifiedDate(now);
			}
			else {
				accountGroup.setModifiedDate(
					serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(accountGroup);
			}
			else {
				accountGroup = (AccountGroup)session.merge(accountGroup);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			entityCacheEnabled, AccountGroupImpl.class,
			accountGroupModelImpl.getPrimaryKey(), accountGroupModelImpl, false,
			new Object[] {
				_columnBitmaskEnabled, accountGroupModelImpl.getColumnBitmask()
			});

		cacheUniqueFindersCache(accountGroupModelImpl);

		accountGroup.resetOriginalValues();

		if (isNew) {
			accountGroup.setNew(false);
		}

		return accountGroup;
	}

	/**
	 * Returns the account group with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the account group
	 * @return the account group
	 * @throws NoSuchGroupException if a account group with the primary key could not be found
	 */
	@Override
	public AccountGroup findByPrimaryKey(Serializable primaryKey)
		throws NoSuchGroupException {

		AccountGroup accountGroup = fetchByPrimaryKey(primaryKey);

		if (accountGroup == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchGroupException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return accountGroup;
	}

	/**
	 * Returns the account group with the primary key or throws a <code>NoSuchGroupException</code> if it could not be found.
	 *
	 * @param accountGroupId the primary key of the account group
	 * @return the account group
	 * @throws NoSuchGroupException if a account group with the primary key could not be found
	 */
	@Override
	public AccountGroup findByPrimaryKey(long accountGroupId)
		throws NoSuchGroupException {

		return findByPrimaryKey((Serializable)accountGroupId);
	}

	/**
	 * Returns the account group with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param accountGroupId the primary key of the account group
	 * @return the account group, or <code>null</code> if a account group with the primary key could not be found
	 */
	@Override
	public AccountGroup fetchByPrimaryKey(long accountGroupId) {
		return fetchByPrimaryKey((Serializable)accountGroupId);
	}

	/**
	 * Returns all the account groups.
	 *
	 * @return the account groups
	 */
	@Override
	public List<AccountGroup> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the account groups.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AccountGroupModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of account groups
	 * @param end the upper bound of the range of account groups (not inclusive)
	 * @return the range of account groups
	 */
	@Override
	public List<AccountGroup> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the account groups.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AccountGroupModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of account groups
	 * @param end the upper bound of the range of account groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of account groups
	 */
	@Override
	public List<AccountGroup> findAll(
		int start, int end, OrderByComparator<AccountGroup> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the account groups.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AccountGroupModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of account groups
	 * @param end the upper bound of the range of account groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of account groups
	 */
	@Override
	public List<AccountGroup> findAll(
		int start, int end, OrderByComparator<AccountGroup> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _getFinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll");
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _getFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll");
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<AccountGroup> list = null;

		if (useFinderCache) {
			list = (List<AccountGroup>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_ACCOUNTGROUP);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_ACCOUNTGROUP;

				sql = sql.concat(AccountGroupModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<AccountGroup>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the account groups from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (AccountGroup accountGroup : findAll()) {
			remove(accountGroup);
		}
	}

	/**
	 * Returns the number of account groups.
	 *
	 * @return the number of account groups
	 */
	@Override
	public int countAll() {
		FinderPath finderPath = _getFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll");

		Long count = (Long)finderCache.getResult(
			finderPath, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_ACCOUNTGROUP);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, FINDER_ARGS_EMPTY);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "accountGroupId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_ACCOUNTGROUP;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return AccountGroupModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the account group persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		AccountGroupModelImpl.setEntityCacheEnabled(entityCacheEnabled);
		AccountGroupModelImpl.setFinderCacheEnabled(finderCacheEnabled);

		_bundleContext = bundleContext;
		Bundle bundle = FrameworkUtil.getBundle(
			AccountGroupPersistenceImpl.class);

		_bundleContext = bundle.getBundleContext();
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(AccountGroupImpl.class.getName());

		for (ServiceRegistration<FinderPath> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	@Override
	@Reference(
		target = AccountPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.account.model.AccountGroup"),
			true);
	}

	@Override
	@Reference(
		target = AccountPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = AccountPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private boolean _columnBitmaskEnabled;
	private BundleContext _bundleContext;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_ACCOUNTGROUP =
		"SELECT accountGroup FROM AccountGroup accountGroup";

	private static final String _SQL_SELECT_ACCOUNTGROUP_WHERE =
		"SELECT accountGroup FROM AccountGroup accountGroup WHERE ";

	private static final String _SQL_COUNT_ACCOUNTGROUP =
		"SELECT COUNT(accountGroup) FROM AccountGroup accountGroup";

	private static final String _SQL_COUNT_ACCOUNTGROUP_WHERE =
		"SELECT COUNT(accountGroup) FROM AccountGroup accountGroup WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "accountGroup.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No AccountGroup exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No AccountGroup exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		AccountGroupPersistenceImpl.class);

	static {
		try {
			Class.forName(AccountPersistenceConstants.class.getName());
		}
		catch (ClassNotFoundException classNotFoundException) {
			throw new ExceptionInInitializerError(classNotFoundException);
		}
	}

	private FinderPath _getFinderPath(String cacheName, String methodName) {
		if (!finderCacheEnabled) {
			return null;
		}

		return _finderPathMap.computeIfAbsent(
			StringBundler.concat(cacheName, "_", methodName),
			key -> {
				Class<?> returnClass = AccountGroupImpl.class;

				Object[] bitMaskArray = _COLUMN_BITMASK_ARRAY_MAP.get(
					methodName);

				if (methodName.startsWith("count")) {
					returnClass = Long.class;

					String methodNamePostfix = methodName.substring(5);

					bitMaskArray = _COLUMN_BITMASK_ARRAY_MAP.get(
						"find" + methodNamePostfix);

					if (bitMaskArray == null) {
						bitMaskArray = _COLUMN_BITMASK_ARRAY_MAP.get(
							"fetch" + methodNamePostfix);
					}
				}

				FinderPath finderPath = null;

				if ((bitMaskArray == null) || (bitMaskArray.length != 3)) {
					finderPath = new FinderPath(
						entityCacheEnabled, true, returnClass, cacheName,
						methodName, new String[0]);
				}
				else {
					finderPath = new FinderPath(
						entityCacheEnabled, true, returnClass, cacheName,
						methodName, new String[0], (long)bitMaskArray[0],
						(Function<BaseModel<?>, Object[]>)bitMaskArray[1],
						(Function<BaseModel<?>, Object[]>)bitMaskArray[2]);
				}

				if (!cacheName.equals(FINDER_CLASS_NAME_LIST_WITH_PAGINATION)) {
					_serviceRegistrations.add(
						_bundleContext.registerService(
							FinderPath.class, finderPath,
							MapUtil.singletonDictionary(
								"cache.name", cacheName)));
				}

				return finderPath;
			});
	}

	private Map<String, FinderPath> _finderPathMap = new ConcurrentHashMap<>();
	private Set<ServiceRegistration<FinderPath>> _serviceRegistrations =
		Collections.newSetFromMap(new ConcurrentHashMap<>());

	private static final Map<String, Object[]> _COLUMN_BITMASK_ARRAY_MAP =
		new HashMap<>();

	static {
		_COLUMN_BITMASK_ARRAY_MAP.put(
			"fetchByC_ERC",
			new Object[] {
				AccountGroupModelImpl.COMPANYID_COLUMN_BITMASK |
				AccountGroupModelImpl.EXTERNALREFERENCECODE_COLUMN_BITMASK,
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					AccountGroupModelImpl accountGroupModelImpl =
						(AccountGroupModelImpl)baseModel;

					return new Object[] {
						accountGroupModelImpl.getCompanyId(),
						accountGroupModelImpl.getExternalReferenceCode()
					};
				},
				(Function<BaseModel<?>, Object[]>)baseModel -> {
					AccountGroupModelImpl accountGroupModelImpl =
						(AccountGroupModelImpl)baseModel;

					return new Object[] {
						accountGroupModelImpl.getOriginalCompanyId(),
						accountGroupModelImpl.getOriginalExternalReferenceCode()
					};
				}
			});
	}

}
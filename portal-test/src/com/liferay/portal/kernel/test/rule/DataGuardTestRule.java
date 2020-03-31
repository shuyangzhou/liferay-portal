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

package com.liferay.portal.kernel.test.rule;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.bean.ClassLoaderBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistryUtil;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;

import java.io.Closeable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.runner.Description;

/**
 * @author Shuyang Zhou
 */
public class DataGuardTestRule
	extends AbstractTestRule<Map<String, List<BaseModel<?>>>, Void> {

	public static final DataGuardTestRule INSTANCE = new DataGuardTestRule();

	@Override
	protected void afterClass(
		Description description,
		Map<String, List<BaseModel<?>>> previousDataMap) {

		if (previousDataMap == null) {
			return;
		}

		Map<String, List<BaseModel<?>>> dataMap = _captureDataMap();

		StringBundler sb = new StringBundler();

		for (Map.Entry<String, List<BaseModel<?>>> entry : dataMap.entrySet()) {
			String className = entry.getKey();

			List<BaseModel<?>> currentBaseModels = entry.getValue();

			List<BaseModel<?>> previsoutBaseModels = previousDataMap.remove(
				className);

			List<BaseModel<?>> leftoverBaseModels = new ArrayList<>(
				currentBaseModels);

			if (previsoutBaseModels != null) {
				leftoverBaseModels.removeAll(previsoutBaseModels);

				List<BaseModel<?>> overDeletedBaseModels = new ArrayList<>(
					previsoutBaseModels);

				overDeletedBaseModels.removeAll(currentBaseModels);

				if (!overDeletedBaseModels.isEmpty()) {
					sb.append(description.getClassName());
					sb.append(" caused overdeleted data for class :");
					sb.append(className);
					sb.append(" with data : [\n");

					for (BaseModel<?> baseModel : overDeletedBaseModels) {
						sb.append(StringPool.TAB);
						sb.append(baseModel);
						sb.append(",\n");
					}

					sb.setStringAt("\n]\n", sb.index() - 1);
				}
			}

			if (!leftoverBaseModels.isEmpty()) {
				sb.append(description.getClassName());
				sb.append(" caused leftover data for class :");
				sb.append(className);
				sb.append(" with data : [\n");

				for (BaseModel<?> baseModel : leftoverBaseModels) {
					sb.append(StringPool.TAB);
					sb.append(baseModel);
					sb.append(",\n");
				}

				sb.setStringAt("\n]\n", sb.index() - 1);
			}
		}

		Assert.assertTrue(sb.toString(), sb.index() == 0);
	}

	@Override
	protected void afterMethod(Description description, Void m, Object target) {
	}

	@Override
	protected Map<String, List<BaseModel<?>>> beforeClass(
		Description description) {

		DataGuard dataGuard = description.getAnnotation(DataGuard.class);

		if (dataGuard == null) {
			return null;
		}

		return _captureDataMap();
	}

	@Override
	protected Void beforeMethod(Description description, Object target) {
		return null;
	}

	private static Map<String, List<BaseModel<?>>> _captureDataMap() {
		Map<String, List<BaseModel<?>>> dataMap = new HashMap<>();

		for (Map.Entry<String, PersistedModelLocalService> entry :
				_getPersistedModelLocalServices()) {

			PersistedModelLocalService persistedModelLocalService =
				entry.getValue();

			BasePersistence<?> basePersistence = _getBasePersistence(
				persistedModelLocalService);

			if (basePersistence == null) {
				continue;
			}

			Class<?> clazz = basePersistence.getClass();

			Registry registry = RegistryUtil.getRegistry();

			try (Closeable closeable = _installTransactionExecutor(
					registry.getSymbolicName(clazz.getClassLoader()))) {

				TransactionInvokerUtil.invoke(
					_transactionConfig,
					() -> {
						List<BaseModel<?>> baseModels =
							ReflectionTestUtil.invoke(
								basePersistence, "findAll", new Class<?>[0]);

						if (!baseModels.isEmpty()) {
							dataMap.put(entry.getKey(), baseModels);
						}

						return null;
					});
			}
			catch (Throwable t) {
				return ReflectionUtil.throwException(t);
			}
		}

		return dataMap;
	}

	private static BasePersistence<?> _getBasePersistence(
		PersistedModelLocalService persistedModelLocalService) {

		while (true) {
			if (ProxyUtil.isProxyClass(persistedModelLocalService.getClass())) {
				InvocationHandler invocationHandler =
					ProxyUtil.getInvocationHandler(persistedModelLocalService);

				Class<?> clazz = invocationHandler.getClass();

				String className = clazz.getName();

				if (className.equals(
						"com.liferay.portal.spring.aop.AopInvocationHandler")) {

					persistedModelLocalService =
						ReflectionTestUtil.getFieldValue(
							invocationHandler, "_target");

					continue;
				}
				else if (invocationHandler instanceof ClassLoaderBeanHandler) {
					ClassLoaderBeanHandler classLoaderBeanHandler =
						(ClassLoaderBeanHandler)invocationHandler;

					persistedModelLocalService =
						(PersistedModelLocalService)
							classLoaderBeanHandler.getBean();

					continue;
				}
			}

			if (persistedModelLocalService instanceof ServiceWrapper) {
				ServiceWrapper<?> serviceWrapper =
					(ServiceWrapper<?>)persistedModelLocalService;

				Class<?> clazz = serviceWrapper.getClass();

				String simpleName = clazz.getSimpleName();

				if (simpleName.startsWith("Modular")) {
					return null;
				}

				persistedModelLocalService =
					(PersistedModelLocalService)
						serviceWrapper.getWrappedService();

				continue;
			}

			break;
		}

		Class<?> clazz = persistedModelLocalService.getClass();

		Deprecated deprecated = clazz.getAnnotation(Deprecated.class);

		if (deprecated != null) {
			return null;
		}

		return persistedModelLocalService.getBasePersistence();
	}

	private static Set<Map.Entry<String, PersistedModelLocalService>>
		_getPersistedModelLocalServices() {

		Map<String, PersistedModelLocalService> persistedModelLocalServices =
			new HashMap<>(
				ReflectionTestUtil.getFieldValue(
					PersistedModelLocalServiceRegistryUtil.
						getPersistedModelLocalServiceRegistry(),
					"_persistedModelLocalServices"));

		Set<String> classNames = persistedModelLocalServices.keySet();

		classNames.removeAll(_blackListClassNames);

		return persistedModelLocalServices.entrySet();
	}

	private static Closeable _installTransactionExecutor(
			String originBundleSymbolicName)
		throws Exception {

		if (originBundleSymbolicName == null) {
			return () -> {
			};
		}

		ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();

		Class<?> clazz = classLoader.loadClass(
			"com.liferay.portal.spring.transaction." +
				"TransactionExecutorThreadLocal");

		Field field = clazz.getDeclaredField("_transactionExecutorThreadLocal");

		field.setAccessible(true);

		Registry registry = RegistryUtil.getRegistry();

		ServiceReference<?>[] serviceReferences =
			registry.getAllServiceReferences(
				"com.liferay.portal.spring.transaction.TransactionExecutor",
				"(origin.bundle.symbolic.name=" + originBundleSymbolicName +
					")");

		if (serviceReferences == null) {
			return () -> {
			};
		}

		Assert.assertEquals(
			StringBundler.concat(
				"Expected 1 TransactionExecutor for ", originBundleSymbolicName,
				", actually have ", Arrays.toString(serviceReferences)),
			1, serviceReferences.length);

		ServiceReference<?> serviceReference = serviceReferences[0];

		Object portletTransactionExecutor = registry.getService(
			serviceReference);

		ThreadLocal<Deque<Object>> transactionExecutorsThreadLocal =
			(ThreadLocal<Deque<Object>>)field.get(null);

		Deque<Object> transactionExecutors =
			transactionExecutorsThreadLocal.get();

		if (portletTransactionExecutor == transactionExecutors.peek()) {
			return () -> {
			};
		}

		transactionExecutors.push(portletTransactionExecutor);

		return () -> {
			transactionExecutors.pop();

			registry.ungetService(serviceReference);
		};
	}

	private DataGuardTestRule() {
	}

	private static final List<String> _blackListClassNames = Arrays.asList(
		"com.liferay.document.library.sync.model.DLSyncEvent",
		"com.liferay.counter.kernel.model.Counter",
		"com.liferay.portal.kernel.model.ClassName",
		"com.liferay.portal.kernel.model.SystemEvent",
		"com.liferay.portal.security.audit.storage.model.AuditEvent",
		"com.liferay.sync.model.SyncDLObject");
	private static final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.SUPPORTS,
			new Class<?>[] {PortalException.class, SystemException.class});

}
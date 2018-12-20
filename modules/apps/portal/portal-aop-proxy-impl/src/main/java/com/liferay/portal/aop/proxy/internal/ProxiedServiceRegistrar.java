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

package com.liferay.portal.aop.proxy.internal;

import com.liferay.osgi.service.tracker.collections.ServiceTrackerMapBuilder;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapListener;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.proxy.ProxiedService;
import com.liferay.portal.aop.proxy.ProxiedServiceConfiguration;
import com.liferay.portal.kernel.monitoring.ServiceMonitoringControl;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.spring.aop.AopCacheManager;
import com.liferay.portal.spring.aop.AopInvocationHandler;
import com.liferay.portal.spring.aop.ChainableMethodAdvice;
import com.liferay.portal.spring.transaction.TransactionExecutor;

import java.lang.reflect.Field;

import java.util.ArrayDeque;
import java.util.Dictionary;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Preston Crary
 */
@Component(immediate = true, service = {})
public class ProxiedServiceRegistrar {

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		ServiceTrackerMapBuilder.Selector
			<TransactionExecutor, TransactionExecutor>
				selector = ServiceTrackerMapBuilder.SelectorFactory.newSelector(
					bundleContext, TransactionExecutor.class,
					"(service.bundleid=*)");

		ServiceTrackerMapBuilder.Mapper
			<Long, TransactionExecutor, TransactionExecutor, ?>
				mapper = selector.map(
					(serviceReference, emitter) -> emitter.emit(
						(Long)serviceReference.getProperty(
							"service.bundleid")));

		ServiceTrackerMapBuilder.Collector
			<Long, ?, TransactionExecutor, TransactionExecutor>
				collector = mapper.collectSingleValue();

		collector = collector.newCollector(
			new TransactionExecutorServiceTrackerMapListener());

		_serviceTrackerMap = collector.build(true);

		_serviceTracker = new ServiceTracker<>(
			bundleContext, ProxiedService.class,
			new ProxiedServiceServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();

		_serviceTrackerMap.close();
	}

	private BundleContext _bundleContext;

	@Reference(target = "(original.bean=true)")
	private TransactionExecutor _portalTransactionExecutor;

	private final Map<Long, List<ProxiedServiceDependency>>
		_proxiedServiceDependencies = new ConcurrentHashMap<>();

	@Reference
	private ServiceMonitoringControl _serviceMonitoringControl;

	private ServiceTracker<ProxiedService, ProxiedServiceDependency>
		_serviceTracker;
	private ServiceTrackerMap<Long, TransactionExecutor> _serviceTrackerMap;

	private class ProxiedServiceDependency {

		public void close() {
			if (_aopInvocationHandler != null) {
				AopCacheManager.destroy(_aopInvocationHandler);

				_aopInvocationHandler = null;
			}

			if (_serviceRegistration != null) {
				_serviceRegistration.unregister();

				_serviceRegistration = null;
			}
		}

		public void open() {
			Bundle bundle = _serviceReference.getBundle();

			Dictionary<String, String> headers = bundle.getHeaders(
				StringPool.BLANK);

			if (headers.get("Liferay-Service") == null) {
				_registerService(_portalTransactionExecutor);
			}
			else {
				List<ProxiedServiceDependency> proxiedServiceDependencies =
					_proxiedServiceDependencies.computeIfAbsent(
						bundle.getBundleId(),
						bundleId -> new CopyOnWriteArrayList<>());

				proxiedServiceDependencies.add(this);

				TransactionExecutor transactionExecutor =
					_serviceTrackerMap.getService(bundle.getBundleId());

				if (transactionExecutor != null) {
					_registerService(transactionExecutor);
				}
			}
		}

		private ProxiedServiceDependency(
			ServiceReference<ProxiedService> serviceReference,
			ProxiedService proxiedService, String[] serviceNames,
			Dictionary<String, Object> properties, Field field) {

			_serviceReference = serviceReference;
			_proxiedService = proxiedService;
			_serviceNames = serviceNames;
			_properties = properties;
			_field = field;
		}

		private synchronized void _registerService(
			TransactionExecutor transactionExecutor) {

			if (_serviceRegistration != null) {
				return;
			}

			Bundle bundle = _serviceReference.getBundle();

			BundleContext bundleContext = bundle.getBundleContext();

			ChainableMethodAdvice[] chainableMethodAdvices =
				AopCacheManager.createChainableMethodAdvices(
					transactionExecutor, _serviceMonitoringControl);

			_aopInvocationHandler = AopCacheManager.create(
				_proxiedService, chainableMethodAdvices);

			Class<? extends ProxiedService> serviceClass =
				_proxiedService.getClass();

			Object proxy = ProxyUtil.newProxyInstance(
				serviceClass.getClassLoader(),
				ReflectionUtil.getInterfaces(_proxiedService),
				_aopInvocationHandler);

			if (_field != null) {
				try {
					_field.set(_proxiedService, proxy);
				}
				catch (ReflectiveOperationException roe) {
					AopCacheManager.destroy(_aopInvocationHandler);

					_aopInvocationHandler = null;

					ReflectionUtil.throwException(roe);
				}
			}

			_serviceRegistration = bundleContext.registerService(
				_serviceNames, proxy, _properties);
		}

		private AopInvocationHandler _aopInvocationHandler;
		private final Field _field;
		private final Dictionary<String, Object> _properties;
		private final ProxiedService _proxiedService;
		private final String[] _serviceNames;
		private final ServiceReference<ProxiedService> _serviceReference;
		private ServiceRegistration<?> _serviceRegistration;

	}

	private class ProxiedServiceServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<ProxiedService, ProxiedServiceDependency> {

		@Override
		public ProxiedServiceDependency addingService(
			ServiceReference<ProxiedService> serviceReference) {

			ProxiedService proxiedService = _bundleContext.getService(
				serviceReference);

			Class<? extends ProxiedService> serviceClass =
				proxiedService.getClass();

			Dictionary<String, Object> properties = new HashMapDictionary<>();

			Field field = null;

			for (String key : serviceReference.getPropertyKeys()) {
				if (ProxiedService.PROXY_REFERENCE_FIELD.equals(key)) {
					String proxyReference =
						(String)serviceReference.getProperty(
							ProxiedService.PROXY_REFERENCE_FIELD);

					try {
						field = serviceClass.getDeclaredField(proxyReference);

						field.setAccessible(true);
					}
					catch (ReflectiveOperationException roe) {
						ReflectionUtil.throwException(roe);
					}
				}
				else {
					properties.put(key, serviceReference.getProperty(key));
				}
			}

			ProxiedServiceDependency proxiedServiceDependency =
				new ProxiedServiceDependency(
					serviceReference, proxiedService,
					_getServiceNames(serviceClass), properties, field);

			proxiedServiceDependency.open();

			return proxiedServiceDependency;
		}

		@Override
		public void modifiedService(
			ServiceReference<ProxiedService> serviceReference,
			ProxiedServiceDependency proxiedServiceDependency) {
		}

		@Override
		public void removedService(
			ServiceReference<ProxiedService> serviceReference,
			ProxiedServiceDependency proxiedServiceDependency) {

			Bundle bundle = serviceReference.getBundle();

			if (bundle != null) {
				List<ProxiedServiceDependency> proxiedServiceDependencies =
					_proxiedServiceDependencies.get(bundle.getBundleId());

				if (proxiedServiceDependencies != null) {
					proxiedServiceDependencies.remove(proxiedServiceDependency);

					if (proxiedServiceDependencies.isEmpty()) {
						_proxiedServiceDependencies.remove(
							bundle.getBundleId(), proxiedServiceDependencies);
					}
				}
			}

			proxiedServiceDependency.close();

			_bundleContext.ungetService(serviceReference);
		}

		private String[] _getServiceNames(
			Class<? extends ProxiedService> serviceClass) {

			ProxiedServiceConfiguration proxiedServiceConfiguration =
				serviceClass.getAnnotation(ProxiedServiceConfiguration.class);

			if (proxiedServiceConfiguration != null) {
				Class<?>[] interfaces = proxiedServiceConfiguration.service();

				if (interfaces.length > 0) {
					String[] serviceNames = new String[interfaces.length];

					int i = 0;

					for (Class<?> interfaceClass : interfaces) {
						if (!interfaceClass.isInterface()) {
							throw new IllegalArgumentException(
								StringBundler.concat(
									"Cannot proxy ", serviceClass, " because ",
									interfaceClass, " is not an interface"));
						}

						serviceNames[i++] = interfaceClass.getName();
					}

					return serviceNames;
				}
			}

			Set<Class<?>> interfaces = new LinkedHashSet<>();

			Queue<Class<?>> queue = new ArrayDeque<>();

			queue.add(serviceClass);

			while (!queue.isEmpty()) {
				Class<?> clazz = queue.remove();

				for (Class<?> interfaceClass : clazz.getInterfaces()) {
					interfaces.add(interfaceClass);

					queue.add(interfaceClass);
				}

				clazz = clazz.getSuperclass();

				if (clazz != null) {
					if (clazz.isInterface()) {
						interfaces.add(clazz);
					}

					queue.add(clazz);
				}
			}

			interfaces.remove(ProxiedService.class);

			String[] serviceNames = new String[interfaces.size()];

			int i = 0;

			for (Class<?> interfaceClass : interfaces) {
				serviceNames[i++] = interfaceClass.getName();
			}

			return serviceNames;
		}

	}

	private class TransactionExecutorServiceTrackerMapListener
		implements ServiceTrackerMapListener
			<Long, TransactionExecutor, TransactionExecutor> {

		@Override
		public void keyEmitted(
			ServiceTrackerMap<Long, TransactionExecutor> serviceTrackerMap,
			Long bundleId, TransactionExecutor newTransactionExecutor,
			TransactionExecutor transactionExecutor) {

			_updateTransactionExecutorDependencies(
				bundleId, transactionExecutor);
		}

		@Override
		public void keyRemoved(
			ServiceTrackerMap<Long, TransactionExecutor> serviceTrackerMap,
			Long bundleId, TransactionExecutor oldTransactionExecutor,
			TransactionExecutor transactionExecutor) {

			_updateTransactionExecutorDependencies(
				bundleId, transactionExecutor);
		}

		private void _updateTransactionExecutorDependencies(
			Long bundleId, TransactionExecutor transactionExecutor) {

			List<ProxiedServiceDependency> proxiedServiceDependencies =
				_proxiedServiceDependencies.get(bundleId);

			if (proxiedServiceDependencies == null) {
				return;
			}

			for (ProxiedServiceDependency proxiedServiceDependency :
					proxiedServiceDependencies) {

				proxiedServiceDependency.close();

				if (transactionExecutor != null) {
					proxiedServiceDependency._registerService(
						transactionExecutor);
				}
			}
		}

	}

}
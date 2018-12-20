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

package com.liferay.portal.aop.internal;

import com.liferay.osgi.service.tracker.collections.ServiceTrackerMapBuilder;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapListener;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.monitoring.ServiceMonitoringControl;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.spring.aop.AopCacheManager;
import com.liferay.portal.spring.aop.AopInvocationHandler;
import com.liferay.portal.spring.transaction.TransactionExecutor;

import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class AopServiceRegistrar {

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
			bundleContext, AopService.class,
			new AopServiceServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();

		_serviceTrackerMap.close();
	}

	private final Map<Long, AopServiceDependency[]> _aopServiceDependencies =
		new ConcurrentHashMap<>();
	private BundleContext _bundleContext;

	@Reference(target = "(original.bean=true)")
	private TransactionExecutor _portalTransactionExecutor;

	@Reference
	private ServiceMonitoringControl _serviceMonitoringControl;

	private ServiceTracker<AopService, AopServiceDependency> _serviceTracker;
	private ServiceTrackerMap<Long, TransactionExecutor> _serviceTrackerMap;

	private class AopServiceDependency {

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
				_registerAopServiceProxy(_portalTransactionExecutor);
			}
			else {
				_aopServiceDependencies.compute(
					bundle.getBundleId(),
					(bundleId, aopServiceDependencies) -> {
						if (aopServiceDependencies == null) {
							return new AopServiceDependency[] {this};
						}

						return ArrayUtil.append(aopServiceDependencies, this);
					});

				TransactionExecutor transactionExecutor =
					_serviceTrackerMap.getService(bundle.getBundleId());

				if (transactionExecutor != null) {
					synchronized (this) {
						_registerAopServiceProxy(transactionExecutor);
					}
				}
			}
		}

		public synchronized void update(
			TransactionExecutor transactionExecutor) {

			close();

			if (transactionExecutor != null) {
				_registerAopServiceProxy(transactionExecutor);
			}
		}

		private AopServiceDependency(
			ServiceReference<AopService> serviceReference,
			AopService aopService, Class<?>[] aopServiceInterfaces,
			String[] aopServiceNames, Dictionary<String, Object> properties) {

			_serviceReference = serviceReference;
			_aopService = aopService;
			_aopServiceInterfaces = aopServiceInterfaces;
			_aopServiceNames = aopServiceNames;
			_properties = properties;
		}

		private void _registerAopServiceProxy(
			TransactionExecutor transactionExecutor) {

			if (_serviceRegistration != null) {
				return;
			}

			_aopInvocationHandler = AopCacheManager.create(
				_aopService,
				AopCacheManager.createChainableMethodAdvices(
					transactionExecutor, _serviceMonitoringControl));

			Class<? extends AopService> aopServiceClass =
				_aopService.getClass();

			Object aopProxy = ProxyUtil.newProxyInstance(
				aopServiceClass.getClassLoader(), _aopServiceInterfaces,
				_aopInvocationHandler);

			_aopService.setAopProxy(aopProxy);

			Bundle bundle = _serviceReference.getBundle();

			BundleContext bundleContext = bundle.getBundleContext();

			_serviceRegistration = bundleContext.registerService(
				_aopServiceNames, aopProxy, _properties);
		}

		private AopInvocationHandler _aopInvocationHandler;
		private final AopService _aopService;
		private final Class<?>[] _aopServiceInterfaces;
		private final String[] _aopServiceNames;
		private final Dictionary<String, Object> _properties;
		private final ServiceReference<AopService> _serviceReference;
		private ServiceRegistration<?> _serviceRegistration;

	}

	private class AopServiceServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<AopService, AopServiceDependency> {

		@Override
		public AopServiceDependency addingService(
			ServiceReference<AopService> serviceReference) {

			AopService aopService = _bundleContext.getService(serviceReference);

			Class<?>[] aopInterfaces = _getAopInterfaces(aopService);

			if (aopInterfaces.length == 0) {
				throw new IllegalArgumentException(
					StringBundler.concat(
						"Cannot register ", aopService.getClass(),
						" without a service interface"));
			}

			String[] aopServiceNames = new String[aopInterfaces.length];

			for (int i = 0; i < aopInterfaces.length; i++) {
				aopServiceNames[i] = aopInterfaces[i].getName();
			}

			Dictionary<String, Object> properties = new HashMapDictionary<>();

			for (String key : serviceReference.getPropertyKeys()) {
				properties.put(key, serviceReference.getProperty(key));
			}

			AopServiceDependency aopServiceDependency =
				new AopServiceDependency(
					serviceReference, aopService, aopInterfaces,
					aopServiceNames, properties);

			aopServiceDependency.open();

			return aopServiceDependency;
		}

		@Override
		public void modifiedService(
			ServiceReference<AopService> serviceReference,
			AopServiceDependency aopServiceDependency) {
		}

		@Override
		public void removedService(
			ServiceReference<AopService> serviceReference,
			AopServiceDependency aopServiceDependency) {

			Bundle bundle = serviceReference.getBundle();

			if (bundle != null) {
				_aopServiceDependencies.compute(
					bundle.getBundleId(),
					(bundleId, aopServiceDependencies) -> {
						if (aopServiceDependencies == null) {
							return null;
						}

						aopServiceDependencies = ArrayUtil.remove(
							aopServiceDependencies, aopServiceDependency);

						if (aopServiceDependencies.length == 0) {
							return null;
						}

						return aopServiceDependencies;
					});
			}

			aopServiceDependency.close();

			_bundleContext.ungetService(serviceReference);
		}

		private Class<?>[] _getAopInterfaces(AopService aopService) {
			Class<?>[] aopInterfaces = aopService.getAopInterfaces();

			Class<? extends AopService> aopServiceClass = aopService.getClass();

			if (ArrayUtil.isEmpty(aopInterfaces)) {
				return ArrayUtil.remove(
					aopServiceClass.getInterfaces(), AopService.class);
			}

			for (Class<?> aopInterface : aopInterfaces) {
				if (!aopInterface.isInterface()) {
					throw new IllegalArgumentException(
						StringBundler.concat(
							"Cannot proxy ", aopServiceClass, " because ",
							aopInterface, " is not an interface"));
				}

				if (!aopInterface.isAssignableFrom(aopServiceClass)) {
					throw new IllegalArgumentException(
						StringBundler.concat(
							"Cannot proxy ", aopServiceClass, " because ",
							aopInterface, " is not implemented"));
				}

				if (aopInterface == AopService.class) {
					throw new IllegalArgumentException(
						"Do not include AopService in service interfaces");
				}
			}

			return aopInterfaces;
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

			AopServiceDependency[] aopServiceDependencies =
				_aopServiceDependencies.get(bundleId);

			if (aopServiceDependencies == null) {
				return;
			}

			for (AopServiceDependency aopServiceDependency :
					aopServiceDependencies) {

				aopServiceDependency.update(transactionExecutor);
			}
		}

	}

}
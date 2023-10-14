/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.aop.internal;

import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.module.util.BundleUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.spring.aop.AopCacheManager;
import com.liferay.portal.spring.aop.AopInvocationHandler;
import com.liferay.portal.spring.transaction.TransactionExecutor;

import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.PrototypeServiceFactory;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentConstants;

/**
 * @author Preston Crary
 */
public class AopServiceRegistrar {

	public AopServiceRegistrar(
		ServiceReference<AopService> serviceReference,
		ServiceReferenceHelper serviceReferenceHelper,
		Class<?>[] aopServiceInterfaces) {

		_serviceReference = serviceReference;
		_serviceReferenceHelper = serviceReferenceHelper;
		_aopServiceInterfaces = aopServiceInterfaces;

		if (BundleUtil.isLiferayServiceBundle(serviceReference.getBundle()) &&
			GetterUtil.getBoolean(
				serviceReference.getProperty("liferay.service"), true)) {

			_liferayService = true;
		}
		else {
			_liferayService = false;
		}
	}

	public boolean isLiferayService() {
		return _liferayService;
	}

	public void register(TransactionExecutor transactionExecutor) {
		if (_serviceRegistration != null) {
			return;
		}

		Bundle bundle = _serviceReference.getBundle();

		BundleContext bundleContext = bundle.getBundleContext();

		String[] aopServiceNames = new String[_aopServiceInterfaces.length];

		for (int i = 0; i < _aopServiceInterfaces.length; i++) {
			aopServiceNames[i] = _aopServiceInterfaces[i].getName();
		}

		_serviceRegistration = bundleContext.registerService(
			aopServiceNames, _getService(bundleContext, transactionExecutor),
			_getProperties(_serviceReference));
	}

	public void unregister() {
		if (_serviceRegistration != null) {
			if (_aopInvocationHandler != null) {
				AopCacheManager.destroy(_aopInvocationHandler);

				_aopInvocationHandler = null;
			}

			_serviceRegistration.unregister();

			_serviceRegistration = null;

			_serviceReferenceHelper.dispose();
		}
	}

	public void updateProperties() {
		if (_serviceRegistration != null) {
			_serviceRegistration.setProperties(
				_getProperties(_serviceReference));
		}
	}

	private Dictionary<String, Object> _getProperties(
		ServiceReference<AopService> serviceReference) {

		Dictionary<String, Object> properties = null;

		for (String key : serviceReference.getPropertyKeys()) {
			if (_frameworkKeys.contains(key)) {
				continue;
			}

			if (properties == null) {
				properties = new HashMapDictionary<>();
			}

			properties.put(key, serviceReference.getProperty(key));
		}

		return properties;
	}

	private Object _getService(
		BundleContext bundleContext, TransactionExecutor transactionExecutor) {

		Object serviceScope = _serviceReference.getProperty(
			Constants.SERVICE_SCOPE);

		if (Constants.SCOPE_PROTOTYPE.equals(serviceScope)) {
			return new AopServicePrototypeServiceFactory(
				bundleContext.getServiceObjects(_serviceReference),
				transactionExecutor);
		}

		return new SingletonServiceFactory(transactionExecutor);
	}

	private static final Set<String> _frameworkKeys = new HashSet<>(
		Arrays.asList(
			ComponentConstants.COMPONENT_ID, ComponentConstants.COMPONENT_NAME,
			Constants.OBJECTCLASS, Constants.SERVICE_BUNDLEID,
			Constants.SERVICE_ID, Constants.SERVICE_SCOPE));

	private AopInvocationHandler _aopInvocationHandler;
	private final Class<?>[] _aopServiceInterfaces;
	private final boolean _liferayService;
	private final ServiceReference<AopService> _serviceReference;
	private final ServiceReferenceHelper _serviceReferenceHelper;
	private ServiceRegistration<?> _serviceRegistration;

	private class AopServicePrototypeServiceFactory
		implements PrototypeServiceFactory<Object> {

		@Override
		public Object getService(
			Bundle bundle, ServiceRegistration<Object> serviceRegistration) {

			AopService aopService = _serviceObjects.getService();

			Class<?>[] aopInterfaces = aopService.getAopInterfaces();

			Class<? extends AopService> aopServiceClass = aopService.getClass();

			if (ArrayUtil.isEmpty(aopInterfaces)) {
				aopInterfaces = ArrayUtil.remove(
					aopServiceClass.getInterfaces(), AopService.class);
			}

			if (!Arrays.equals(_aopServiceInterfaces, aopInterfaces)) {
				throw new IllegalArgumentException(
					StringBundler.concat(
						"Prototype AopService ", aopService,
						" must have immutable AOP interfaces, expected ",
						Arrays.toString(_aopServiceInterfaces), " but was ",
						Arrays.toString(aopInterfaces)));
			}

			AopInvocationHandler aopInvocationHandler = AopCacheManager.create(
				() -> aopService, _transactionExecutor);

			_aopServices.put(aopInvocationHandler, aopService);

			Object aopProxy = ProxyUtil.newProxyInstance(
				aopServiceClass.getClassLoader(), _aopServiceInterfaces,
				aopInvocationHandler);

			aopService.setAopProxy(aopProxy);

			return aopProxy;
		}

		@Override
		public void ungetService(
			Bundle bundle, ServiceRegistration<Object> serviceRegistration,
			Object aopProxy) {

			AopInvocationHandler aopInvocationHandler =
				ProxyUtil.fetchInvocationHandler(
					aopProxy, AopInvocationHandler.class);

			AopCacheManager.destroy(aopInvocationHandler);

			_serviceObjects.ungetService(
				_aopServices.remove(aopInvocationHandler));
		}

		private AopServicePrototypeServiceFactory(
			ServiceObjects<AopService> serviceObjects,
			TransactionExecutor transactionExecutor) {

			_serviceObjects = serviceObjects;
			_transactionExecutor = transactionExecutor;
		}

		private final Map<AopInvocationHandler, AopService> _aopServices =
			new ConcurrentHashMap<>();
		private final ServiceObjects<AopService> _serviceObjects;
		private final TransactionExecutor _transactionExecutor;

	}

	private class SetAopProxyInvocationHandler extends AopInvocationHandler {

		@Override
		public Object getTarget() {
			return _aopInvocationHandler.getTarget();
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

			_proxyDCLSingleton.getSingleton(() -> _setAopProxy(proxy));

			return _aopInvocationHandler.invoke(proxy, method, args);
		}

		@Override
		public void setTarget(Object target) {
			_aopInvocationHandler.setTarget(target);
		}

		private SetAopProxyInvocationHandler(
			Supplier<AopService> aopServiceSupplier,
			AopInvocationHandler aopInvocationHandler) {

			super(null, null, null);

			_aopServiceSupplier = aopServiceSupplier;
			_aopInvocationHandler = aopInvocationHandler;
		}

		private Object _setAopProxy(Object proxy) {
			ProxyUtil.setInvocationHandler(proxy, _aopInvocationHandler);

			AopService aopService = _aopServiceSupplier.get();

			aopService.setAopProxy(proxy);

			return proxy;
		}

		private final AopInvocationHandler _aopInvocationHandler;
		private final Supplier<AopService> _aopServiceSupplier;
		private final DCLSingleton<Object> _proxyDCLSingleton =
			new DCLSingleton<>();

	}

	private class SingletonServiceFactory implements ServiceFactory<Object> {

		@Override
		public Object getService(
			Bundle bundle, ServiceRegistration<Object> serviceRegistration) {

			return _serviceDCLSingleton.getSingleton(this::_createServiceProxy);
		}

		@Override
		public void ungetService(
			Bundle bundle, ServiceRegistration<Object> serviceRegistration,
			Object service) {
		}

		private SingletonServiceFactory(
			TransactionExecutor transactionExecutor) {

			_transactionExecutor = transactionExecutor;
		}

		private Object _createServiceProxy() {
			_aopInvocationHandler = AopCacheManager.create(
				_serviceReferenceHelper::getAopService, _transactionExecutor);

			return ProxyUtil.newProxyInstance(
				_serviceReferenceHelper.getAopServiceClassLoader(),
				_aopServiceInterfaces,
				new SetAopProxyInvocationHandler(
					_serviceReferenceHelper::getAopService,
					_aopInvocationHandler));
		}

		private final DCLSingleton<Object> _serviceDCLSingleton =
			new DCLSingleton<>();
		private final TransactionExecutor _transactionExecutor;

	}

}
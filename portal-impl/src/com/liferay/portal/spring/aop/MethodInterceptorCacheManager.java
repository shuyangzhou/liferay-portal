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

package com.liferay.portal.spring.aop;

import aQute.bnd.annotation.ProviderType;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.cache.thread.local.ThreadLocalCacheMethodInterceptorFactory;
import com.liferay.portal.dao.jdbc.aop.DynamicDataSourceMethodInterceptorFactory;
import com.liferay.portal.increment.BufferedIncrementMethodInterceptorFactory;
import com.liferay.portal.internal.cluster.ClusterableMethodInterceptorFactory;
import com.liferay.portal.internal.cluster.SPIClusterableMethodInterceptorFactory;
import com.liferay.portal.internal.spring.aop.MethodInterceptorCacheImpl;
import com.liferay.portal.kernel.dao.jdbc.aop.DynamicDataSourceTargetSource;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.resiliency.spi.SPIUtil;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.messaging.async.AsyncMethodInterceptorFactory;
import com.liferay.portal.resiliency.service.PortalResiliencyMethodInterceptorFactory;
import com.liferay.portal.search.IndexableMethodInterceptorFactory;
import com.liferay.portal.security.access.control.AccessControlMethodInterceptorFactory;
import com.liferay.portal.service.ServiceContextMethodInterceptorFactory;
import com.liferay.portal.spring.transaction.TransactionMethodInterceptorFactory;
import com.liferay.portal.systemevent.SystemEventMethodInterceptorFactory;
import com.liferay.portal.util.PropsValues;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Preston Crary
 */
@ProviderType
public class MethodInterceptorCacheManager {

	public static MethodInterceptorCache create(
		MethodInterceptorFactoryHelper methodInterceptorFactoryHelper) {

		MethodInterceptorCacheImpl methodInterceptorCacheImpl =
			new MethodInterceptorCacheImpl();

		synchronized (MethodInterceptorCacheManager.class) {
			_methodInterceptorCacheInitializer.initialize(
				methodInterceptorCacheImpl, methodInterceptorFactoryHelper);

			_methodInterceptorCaches.put(
				methodInterceptorCacheImpl, methodInterceptorFactoryHelper);
		}

		return methodInterceptorCacheImpl;
	}

	public static void destroy(MethodInterceptorCache methodInterceptorCache) {
		synchronized (MethodInterceptorCacheManager.class) {
			_methodInterceptorCaches.remove(methodInterceptorCache);
		}

		methodInterceptorCache.clear();
	}

	private static void _updateMethodInterceptorCaches() {
		for (Map.Entry<MethodInterceptorCache, MethodInterceptorFactoryHelper>
				entry : _methodInterceptorCaches.entrySet()) {

			MethodInterceptorCacheImpl methodInterceptorCacheImpl =
				(MethodInterceptorCacheImpl)entry.getKey();
			MethodInterceptorFactoryHelper methodInterceptorFactoryHelper =
				entry.getValue();

			_methodInterceptorCacheInitializer.initialize(
				methodInterceptorCacheImpl, methodInterceptorFactoryHelper);

			methodInterceptorCacheImpl.clear();
		}
	}

	private MethodInterceptorCacheManager() {
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MethodInterceptorCacheManager.class);

	private static MethodInterceptorCacheInitializer
		_methodInterceptorCacheInitializer;
	private static final Map
		<MethodInterceptorCache, MethodInterceptorFactoryHelper>
			_methodInterceptorCaches = new ConcurrentHashMap<>();

	private static class MethodInterceptorCacheInitializer {

		public void initialize(
			MethodInterceptorCacheImpl methodInterceptorCacheImpl,
			MethodInterceptorFactoryHelper methodInterceptorFactoryHelper) {

			MethodInterceptor[] classMethodInterceptors =
				new MethodInterceptor[_classMethodInterceptorCount];
			MethodInterceptor[] fullMethodInterceptors =
				new MethodInterceptor[_methodInterceptorFactories.length];

			int classMethodInterceptorCount = 0;

			for (int i = 0; i < _methodInterceptorFactories.length; i++) {
				MethodInterceptorFactory methodInterceptorFactory =
					_methodInterceptorFactories[i];

				MethodInterceptor methodInterceptor =
					methodInterceptorFactory.create(
						methodInterceptorFactoryHelper);

				fullMethodInterceptors[i] = methodInterceptor;

				if (_classMethodInterceptorIndexes[i]) {
					classMethodInterceptors[classMethodInterceptorCount++] =
						methodInterceptor;
				}

				_initializeMethodInterceptor(
					methodInterceptor, methodInterceptorCacheImpl,
					methodInterceptorFactory.getAnnotationClass());
			}

			methodInterceptorCacheImpl.setMethodInterceptorsAndAnnotationTypes(
				classMethodInterceptors, fullMethodInterceptors,
				_annotationClasses);
		}

		private MethodInterceptorCacheInitializer(
			MethodInterceptorFactory[] methodInterceptorFactories) {

			_methodInterceptorFactories = methodInterceptorFactories;

			_classMethodInterceptorIndexes = new boolean[
				_methodInterceptorFactories.length];

			int classMethodInterceptorCount = 0;

			iterate:
			for (int i = 0; i < _methodInterceptorFactories.length; i++) {
				MethodInterceptorFactory methodInterceptorFactory =
					_methodInterceptorFactories[i];

				Class<? extends Annotation> annotationType =
					methodInterceptorFactory.getAnnotationClass();

				if (annotationType == null) {
					_classMethodInterceptorIndexes[i] = true;

					classMethodInterceptorCount++;
				}
				else {
					_annotationClasses.add(annotationType);

					Target target = annotationType.getAnnotation(Target.class);

					if (target != null) {
						for (ElementType elementType : target.value()) {
							if (elementType == ElementType.TYPE) {
								_classMethodInterceptorIndexes[i] = true;

								classMethodInterceptorCount++;

								continue iterate;
							}
						}
					}
				}
			}

			_classMethodInterceptorCount = classMethodInterceptorCount;
		}

		private void _initializeMethodInterceptor(
			MethodInterceptor methodInterceptor,
			MethodInterceptorCache methodInterceptorCache,
			Class<? extends Annotation> annotationClass) {

			if (!(methodInterceptor instanceof BaseMethodInterceptor)) {
				return;
			}

			BaseMethodInterceptor baseMethodInterceptor =
				(BaseMethodInterceptor)methodInterceptor;

			baseMethodInterceptor.setMethodInterceptorCache(
				methodInterceptorCache);

			if (baseMethodInterceptor instanceof
					AnnotatedMethodInterceptor<?>) {

				AnnotatedMethodInterceptor<? extends Annotation>
					annotatedMethodInterceptor =
						(AnnotatedMethodInterceptor<? extends Annotation>)
							baseMethodInterceptor;

				annotatedMethodInterceptor.setAnnotationClass(annotationClass);
			}
		}

		private final Set<Class<? extends Annotation>> _annotationClasses =
			new HashSet<>();
		private final int _classMethodInterceptorCount;
		private final boolean[] _classMethodInterceptorIndexes;
		private final MethodInterceptorFactory[] _methodInterceptorFactories;

	}

	private static class
		MethodInterceptorFactoryProviderServiceTrackerCustomizer
			implements ServiceTrackerCustomizer
				<MethodInterceptorFactoryProvider, MethodInterceptorFactory> {

		@Override
		public MethodInterceptorFactory addingService(
			ServiceReference<MethodInterceptorFactoryProvider>
				serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			MethodInterceptorFactoryProvider methodInterceptorFactoryProvider =
				registry.getService(serviceReference);

			MethodInterceptorFactory methodInterceptorFactory =
				methodInterceptorFactoryProvider.getMethodInterceptorFactory();

			Class<? extends MethodInterceptorFactory> parentClass =
				methodInterceptorFactoryProvider.getParentClass();

			synchronized (MethodInterceptorCacheManager.class) {
				MethodInterceptorFactory[] methodInterceptorFactories =
					_methodInterceptorCacheInitializer.
						_methodInterceptorFactories;

				int index = 0;

				for (; index < methodInterceptorFactories.length; index++) {
					if (parentClass ==
							methodInterceptorFactories[index].getClass()) {

						break;
					}
				}

				if (index == methodInterceptorFactories.length) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							StringBundler.concat(
								"Parent ", parentClass, " not found for ",
								methodInterceptorFactoryProvider.getClass()));
					}
				}
				else {
					index++;
				}

				int newLength = methodInterceptorFactories.length + 1;

				MethodInterceptorFactory[] newMethodInterceptorFactories =
					new MethodInterceptorFactory[newLength];

				System.arraycopy(
					methodInterceptorFactories, 0,
					newMethodInterceptorFactories, 0, index);

				newMethodInterceptorFactories[index] = methodInterceptorFactory;

				if (index < methodInterceptorFactories.length) {
					System.arraycopy(
						methodInterceptorFactories, index,
						newMethodInterceptorFactories, index + 1,
						methodInterceptorFactories.length - index);
				}

				_methodInterceptorCacheInitializer =
					new MethodInterceptorCacheInitializer(
						newMethodInterceptorFactories);

				_updateMethodInterceptorCaches();
			}

			return methodInterceptorFactory;
		}

		@Override
		public void modifiedService(
			ServiceReference<MethodInterceptorFactoryProvider> serviceReference,
			MethodInterceptorFactory methodInterceptorFactory) {
		}

		@Override
		public void removedService(
			ServiceReference<MethodInterceptorFactoryProvider> serviceReference,
			MethodInterceptorFactory methodInterceptorFactory) {

			Registry registry = RegistryUtil.getRegistry();

			synchronized (MethodInterceptorCacheManager.class) {
				MethodInterceptorFactory[] methodInterceptorFactories =
					_methodInterceptorCacheInitializer.
						_methodInterceptorFactories;

				int index = 1;

				while (true) {
					if (methodInterceptorFactories[index] ==
							methodInterceptorFactory) {

						break;
					}

					index++;
				}

				int newLength = methodInterceptorFactories.length - 1;

				MethodInterceptorFactory[] newMethodInterceptorFactories =
					new MethodInterceptorFactory[newLength];

				System.arraycopy(
					methodInterceptorFactories, 0,
					newMethodInterceptorFactories, 0, index);

				if (index < newLength) {
					System.arraycopy(
						methodInterceptorFactories, index + 1,
						newMethodInterceptorFactories, index,
						newLength - index);
				}

				_methodInterceptorCacheInitializer =
					new MethodInterceptorCacheInitializer(
						newMethodInterceptorFactories);

				_updateMethodInterceptorCaches();
			}

			registry.ungetService(serviceReference);
		}

	}

	static {
		List<MethodInterceptorFactory> methodInterceptorFactories =
			new ArrayList<>(12);

		methodInterceptorFactories.add(new SkipMethodInterceptorFactory());

		if (SPIUtil.isSPI()) {
			methodInterceptorFactories.add(
				new SPIClusterableMethodInterceptorFactory());
		}
		else if (PropsValues.CLUSTER_LINK_ENABLED) {
			methodInterceptorFactories.add(
				new ClusterableMethodInterceptorFactory());
		}

		methodInterceptorFactories.add(
			new AccessControlMethodInterceptorFactory());

		methodInterceptorFactories.add(
			new PortalResiliencyMethodInterceptorFactory());

		methodInterceptorFactories.add(new AsyncMethodInterceptorFactory());

		methodInterceptorFactories.add(
			new ThreadLocalCacheMethodInterceptorFactory());

		methodInterceptorFactories.add(
			new BufferedIncrementMethodInterceptorFactory());

		methodInterceptorFactories.add(new IndexableMethodInterceptorFactory());

		methodInterceptorFactories.add(
			new SystemEventMethodInterceptorFactory());

		methodInterceptorFactories.add(
			new ServiceContextMethodInterceptorFactory());

		methodInterceptorFactories.add(new RetryMethodInterceptorFactory());

		DynamicDataSourceTargetSource dynamicDataSourceTargetSource =
			InfrastructureUtil.getDynamicDataSourceTargetSource();

		if (dynamicDataSourceTargetSource == null) {
			methodInterceptorFactories.add(
				new TransactionMethodInterceptorFactory());
		}
		else {
			methodInterceptorFactories.add(
				new DynamicDataSourceMethodInterceptorFactory());
		}

		_methodInterceptorCacheInitializer =
			new MethodInterceptorCacheInitializer(
				methodInterceptorFactories.toArray(
					new MethodInterceptorFactory[
						methodInterceptorFactories.size()]));

		Registry registry = RegistryUtil.getRegistry();

		ServiceTracker<?, ?> serviceTracker = registry.trackServices(
			MethodInterceptorFactoryProvider.class,
			new MethodInterceptorFactoryProviderServiceTrackerCustomizer());

		serviceTracker.open();
	}

}
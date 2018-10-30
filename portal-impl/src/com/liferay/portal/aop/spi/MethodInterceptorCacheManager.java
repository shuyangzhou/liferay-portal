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

package com.liferay.portal.aop.spi;

import aQute.bnd.annotation.ProviderType;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.aop.AnnotatedMethodInterceptor;
import com.liferay.portal.aop.BaseMethodInterceptor;
import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.cache.MethodInterceptorCache;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.internal.aop.cache.MethodInterceptorCacheImpl;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.util.PropsUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;

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
		MethodInterceptorContext methodInterceptorContext) {

		MethodInterceptorCacheImpl methodInterceptorCacheImpl =
			new MethodInterceptorCacheImpl();

		synchronized (MethodInterceptorCacheManager.class) {
			_methodInterceptorCacheInitializer.initialize(
				methodInterceptorCacheImpl, methodInterceptorContext);

			_methodInterceptorCaches.put(
				methodInterceptorCacheImpl, methodInterceptorContext);
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
		for (Map.Entry<MethodInterceptorCache, MethodInterceptorContext> entry :
				_methodInterceptorCaches.entrySet()) {

			MethodInterceptorCacheImpl methodInterceptorCacheImpl =
				(MethodInterceptorCacheImpl)entry.getKey();
			MethodInterceptorContext methodInterceptorContext =
				entry.getValue();

			_methodInterceptorCacheInitializer.initialize(
				methodInterceptorCacheImpl, methodInterceptorContext);

			methodInterceptorCacheImpl.clear();
		}
	}

	private MethodInterceptorCacheManager() {
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MethodInterceptorCacheManager.class);

	private static MethodInterceptorCacheInitializer
		_methodInterceptorCacheInitializer;
	private static final Map<MethodInterceptorCache, MethodInterceptorContext>
		_methodInterceptorCaches = new ConcurrentHashMap<>();

	private static class MethodInterceptorCacheInitializer {

		public void initialize(
			MethodInterceptorCacheImpl methodInterceptorCacheImpl,
			MethodInterceptorContext methodInterceptorContext) {

			MethodInterceptor[] classMethodInterceptors =
				new MethodInterceptor[_classMethodInterceptorCount];
			MethodInterceptor[] fullMethodInterceptors =
				new MethodInterceptor[_methodInterceptorFactories.length];

			int classMethodInterceptorCount = 0;

			for (int i = 0; i < _methodInterceptorFactories.length; i++) {
				MethodInterceptorFactory methodInterceptorFactory =
					_methodInterceptorFactories[i];

				MethodInterceptor methodInterceptor =
					methodInterceptorFactory.create(methodInterceptorContext);

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

	private static class MethodInterceptorFactoryServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<MethodInterceptorFactory, MethodInterceptorFactory> {

		@Override
		public MethodInterceptorFactory addingService(
			ServiceReference<MethodInterceptorFactory> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			MethodInterceptorFactory methodInterceptorFactory =
				registry.getService(serviceReference);

			if (!methodInterceptorFactory.isEnabled()) {
				registry.ungetService(serviceReference);

				return null;
			}

			Class<? extends MethodInterceptorFactory> parentClass =
				methodInterceptorFactory.getParentClass();

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
								methodInterceptorFactory.getClass()));
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
			ServiceReference<MethodInterceptorFactory> serviceReference,
			MethodInterceptorFactory methodInterceptorFactory) {
		}

		@Override
		public void removedService(
			ServiceReference<MethodInterceptorFactory> serviceReference,
			MethodInterceptorFactory methodInterceptorFactory) {

			if (methodInterceptorFactory == null) {
				return;
			}

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
		String[] portalDefaultMethodInterceptorFactories = PropsUtil.getArray(
			PropsKeys.PORTAL_DEFAULT_METHOD_INTERCEPTOR_FACTORIES);

		List<MethodInterceptorFactory> methodInterceptorFactories =
			new ArrayList<>(portalDefaultMethodInterceptorFactories.length);

		ClassLoader classLoader =
			MethodInterceptorCacheManager.class.getClassLoader();

		try {
			for (String portalDefaultMethodInterceptorFactory :
					portalDefaultMethodInterceptorFactories) {

				Class<?> clazz = classLoader.loadClass(
					portalDefaultMethodInterceptorFactory);

				Constructor constructor = clazz.getConstructor();

				MethodInterceptorFactory methodInterceptorFactory =
					(MethodInterceptorFactory)constructor.newInstance();

				if (methodInterceptorFactory.isEnabled()) {
					methodInterceptorFactories.add(methodInterceptorFactory);
				}
			}
		}
		catch (ReflectiveOperationException roe) {
			throw new ExceptionInInitializerError(roe);
		}

		_methodInterceptorCacheInitializer =
			new MethodInterceptorCacheInitializer(
				methodInterceptorFactories.toArray(
					new MethodInterceptorFactory[
						methodInterceptorFactories.size()]));

		Registry registry = RegistryUtil.getRegistry();

		ServiceTracker<?, ?> serviceTracker = registry.trackServices(
			MethodInterceptorFactory.class,
			new MethodInterceptorFactoryServiceTrackerCustomizer());

		serviceTracker.open();
	}

}
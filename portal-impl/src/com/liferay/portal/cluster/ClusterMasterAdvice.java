/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.cluster;

import com.liferay.portal.kernel.cluster.ClusterMaster;
import com.liferay.portal.kernel.cluster.ClusterMasterExecutorUtil;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.MethodKeyFactory;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Michael C. Han
 */
public class ClusterMasterAdvice
	extends AnnotationChainableMethodAdvice<ClusterMaster> {

	@Override
	public Object before(final MethodInvocation methodInvocation)
		throws Throwable {

		ClusterMaster clusterMaster = findAnnotation(methodInvocation);

		if ((clusterMaster == _nullClusterMaster) ||
			!ClusterMasterExecutorUtil.isMaster()) {

			return null;
		}

		Class<? extends MethodKeyFactory> methodKeyFactoryClass =
			clusterMaster.methodKeyFactory();

		MethodKey methodKey = createMethodKey(methodKeyFactoryClass);

		return ClusterMasterExecutorUtil.executeOnMaster(
			methodKey, methodInvocation.getArguments());
	}

	@Override
	public ClusterMaster getNullAnnotation() {
		return _nullClusterMaster;
	}

	protected MethodKey createMethodKey(
		Class<? extends MethodKeyFactory> methodKeyFactoryClass) {

		if (methodKeyFactoryClass == null) {
			throw new IllegalStateException("methodKeyFactory cannot be null");
		}

		try {
			Constructor<? extends MethodKeyFactory> constructor =
				methodKeyFactoryClass.getDeclaredConstructor();

			if (!constructor.isAccessible()) {
				constructor.setAccessible(true);
			}

			MethodKeyFactory methodKeyFactory = constructor.newInstance();

			return methodKeyFactory.getMethodKey();
		}
		catch (Exception e) {
			throw new IllegalStateException("Unable to obtain method key", e);
		}
	}

	private static ClusterMaster _nullClusterMaster = new ClusterMaster() {

		@Override
		public Class<? extends MethodKeyFactory> methodKeyFactory() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return ClusterMaster.class;
		}

	};

}
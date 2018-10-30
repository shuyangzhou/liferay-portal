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

package com.liferay.portal.internal.cluster;

import com.liferay.portal.aop.AnnotatedMethodInterceptor;
import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.aop.skip.SkipMethodInterceptorFactory;
import com.liferay.portal.kernel.cluster.ClusterInvokeThreadLocal;
import com.liferay.portal.kernel.cluster.ClusterMasterExecutorUtil;
import com.liferay.portal.kernel.cluster.Clusterable;
import com.liferay.portal.kernel.cluster.ClusterableInvokerUtil;
import com.liferay.portal.kernel.nio.intraband.rpc.IntrabandRPCUtil;
import com.liferay.portal.kernel.resiliency.spi.SPI;
import com.liferay.portal.kernel.resiliency.spi.SPIUtil;
import com.liferay.portal.util.PropsValues;

import java.io.Serializable;

import java.lang.reflect.Method;

import java.util.concurrent.Future;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 * @author Preston Crary
 */
public class ClusterableMethodInterceptorFactory
	implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorContext methodInterceptorContext) {

		if (SPIUtil.isSPI()) {
			return new SPIClusterableMethodInterceptor();
		}

		return new ClusterableMethodInterceptor();
	}

	@Override
	public Class<Clusterable> getAnnotationClass() {
		return Clusterable.class;
	}

	@Override
	public Class<? extends MethodInterceptorFactory> getParentClass() {
		return SkipMethodInterceptorFactory.class;
	}

	@Override
	public boolean isEnabled() {
		if (SPIUtil.isSPI()) {
			return true;
		}

		if (PropsValues.CLUSTER_LINK_ENABLED) {
			return true;
		}

		return false;
	}

	public class ClusterableMethodInterceptor
		extends AnnotatedMethodInterceptor<Clusterable> {

		@Override
		protected void afterReturning(
				MethodInvocation methodInvocation, Object result)
			throws Throwable {

			if (!ClusterInvokeThreadLocal.isEnabled()) {
				return;
			}

			Clusterable clusterable = findAnnotation(methodInvocation);

			if (clusterable == null) {
				return;
			}

			ClusterableInvokerUtil.invokeOnCluster(
				clusterable.acceptor(), methodInvocation.getThis(),
				methodInvocation.getMethod(), methodInvocation.getArguments());
		}

		@Override
		protected Object before(MethodInvocation methodInvocation)
			throws Throwable {

			if (!ClusterInvokeThreadLocal.isEnabled()) {
				return null;
			}

			Clusterable clusterable = findAnnotation(methodInvocation);

			if (clusterable == null) {
				return null;
			}

			if (!clusterable.onMaster()) {
				return null;
			}

			Object result = null;

			if (ClusterMasterExecutorUtil.isMaster()) {
				result = methodInvocation.proceed();
			}
			else {
				result = ClusterableInvokerUtil.invokeOnMaster(
					clusterable.acceptor(), methodInvocation.getThis(),
					methodInvocation.getMethod(),
					methodInvocation.getArguments());
			}

			Method method = methodInvocation.getMethod();

			Class<?> returnType = method.getReturnType();

			if (returnType == void.class) {
				result = nullResult;
			}

			return result;
		}

	}

	public class SPIClusterableMethodInterceptor
		extends AnnotatedMethodInterceptor<Clusterable> {

		@Override
		protected void afterReturning(
				MethodInvocation methodInvocation, Object result)
			throws Throwable {

			Clusterable clusterable = findAnnotation(methodInvocation);

			if (clusterable == null) {
				return;
			}

			SPI spi = SPIUtil.getSPI();

			IntrabandRPCUtil.execute(
				spi.getRegistrationReference(),
				new MethodHandlerProcessCallable<>(
					ClusterableInvokerUtil.createMethodHandler(
						clusterable.acceptor(), methodInvocation.getThis(),
						methodInvocation.getMethod(),
						methodInvocation.getArguments())));
		}

		@Override
		protected Object before(MethodInvocation methodInvocation)
			throws Throwable {

			Clusterable clusterable = findAnnotation(methodInvocation);

			if (clusterable == null) {
				return null;
			}

			if (!clusterable.onMaster()) {
				return null;
			}

			SPI spi = SPIUtil.getSPI();

			Future<Serializable> futureResult = IntrabandRPCUtil.execute(
				spi.getRegistrationReference(),
				new MethodHandlerProcessCallable<>(
					ClusterableInvokerUtil.createMethodHandler(
						clusterable.acceptor(), methodInvocation.getThis(),
						methodInvocation.getMethod(),
						methodInvocation.getArguments())));

			Object result = futureResult.get();

			Method method = methodInvocation.getMethod();

			Class<?> returnType = method.getReturnType();

			if (returnType == void.class) {
				result = nullResult;
			}

			return result;
		}

	}

}
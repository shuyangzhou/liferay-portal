/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.cluster.Clusterable;
import com.liferay.portal.kernel.cluster.ClusterableListener;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.spring.aop.Swallowable;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.pacl.PACLClassLoaderUtil;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;

import java.io.Serializable;

import java.lang.annotation.Annotation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 */
public class ClusterableAdvice
	extends AnnotationChainableMethodAdvice<Clusterable> {

	public static Object invoke(
			MethodHandler methodHandler, String servletContextName,
			String beanIdentifier, Map<String, Serializable> context)
		throws Exception {

		if ((context == null) || context.isEmpty()) {
			throw new Exception(
				"Unable to invoke method " + methodHandler.toString());
		}

		String invokeClassName = (String)context.remove(_invokeClassName);

		if (_skipClusteringInvoke(invokeClassName, context)) {
			return null;
		}

		if (servletContextName == null) {
			if (Validator.isNull(beanIdentifier)) {
				return methodHandler.invoke(true);
			}
			else {
				Object bean = PortalBeanLocatorUtil.locate(beanIdentifier);

				return methodHandler.invoke(bean);
			}
		}

		ClassLoader contextClassLoader =
			PACLClassLoaderUtil.getContextClassLoader();

		try {
			ClassLoader classLoader =
				(ClassLoader)PortletBeanLocatorUtil.locate(
					servletContextName, "portletClassLoader");

			PACLClassLoaderUtil.setContextClassLoader(classLoader);

			if (Validator.isNull(beanIdentifier)) {
				return methodHandler.invoke(true);
			}
			else {
				Object bean = PortletBeanLocatorUtil.locate(
					servletContextName, beanIdentifier);

				return methodHandler.invoke(bean);
			}
		}
		finally {
			PACLClassLoaderUtil.setContextClassLoader(contextClassLoader);
		}
	}

	public static void registerClusterableListener(
		String invokeClassName, ClusterableListener clusterableListener) {

		_clusterableAdviceListenerMap.put(invokeClassName, clusterableListener);
	}

	public static void unregisterClusterableListener(String invokeClassName) {
		_clusterableAdviceListenerMap.remove(invokeClassName);
	}

	@Override
	public void afterReturning(MethodInvocation methodInvocation, Object result)
		throws Throwable {

		if (!ClusterInvokeThreadLocal.isEnabled()) {
			return;
		}

		Clusterable clusterable = findAnnotation(methodInvocation);

		if (clusterable == _nullClusterable) {
			return;
		}

		MethodHandler methodHandler = generateMethodHandler(
			clusterable.invokeClassName(), methodInvocation);

		if (methodHandler == null) {
			return;
		}

		ClusterRequest clusterRequest = ClusterRequest.createMulticastRequest(
			methodHandler, true);

		ClusterExecutorUtil.execute(clusterRequest);
	}

	@Override
	public boolean afterThrowing(
			MethodInvocation methodInvocation, Throwable throwable)
		throws Throwable {

		if (!(throwable instanceof Swallowable)) {
			return true;
		}

		Swallowable swallowable = (Swallowable)throwable;

		if (swallowable.isSwallowable()) {
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public Clusterable getNullAnnotation() {
		return _nullClusterable;
	}

	public void setServletContextName(String servletContextName) {
		_servletContextName = servletContextName;
	}

	protected MethodHandler generateMethodHandler(
		String invokeClassName, MethodInvocation methodInvocation) {

		Object thisObject = methodInvocation.getThis();

		if (!(thisObject instanceof IdentifiableBean)) {
			_log.error(
				"Not clustering calls for " + thisObject.getClass().getName() +
					" because it does not implement " +
						IdentifiableBean.class.getName());

			return null;
		}

		MethodHandler methodHandler = new MethodHandler(
			methodInvocation.getMethod(), methodInvocation.getArguments());

		IdentifiableBean identifiableBean = (IdentifiableBean)thisObject;

		String beanIdentifier = identifiableBean.getBeanIdentifier();

		Map<String, Serializable> context =
			ClusterableContextThreadLocal.getThreadLocalContext();

		context.put(_invokeClassName, invokeClassName);

		return new MethodHandler(
			_invokeMethodKey, methodHandler, beanIdentifier,
			_servletContextName, context);
	}

	private static boolean _skipClusteringInvoke(
			String invokeClassName, Map<String, Serializable> context)
		throws Exception {

		if (Validator.isNull(invokeClassName)) {
			return false;
		}

		ClusterableListener clusterableAdviceListener =
			_clusterableAdviceListenerMap.get(invokeClassName);

		if (clusterableAdviceListener == null) {
			return false;
		}

		return clusterableAdviceListener.skipClusteringInvoke(context);
	}

	private static final String _invokeClassName = "listenerClassName";

	private static Log _log = LogFactoryUtil.getLog(ClusterableAdvice.class);

	private static MethodKey _invokeMethodKey = new MethodKey(
		ClusterableAdvice.class, "invoke", MethodHandler.class, String.class,
		String.class, Map.class);
	private static Clusterable _nullClusterable =
		new Clusterable() {

			public Class<? extends Annotation> annotationType() {
				return Clusterable.class;
			}

			public String invokeClassName() {
				return null;
			}

		};

	private static Map<String, ClusterableListener>
		_clusterableAdviceListenerMap =
			new ConcurrentHashMap<String, ClusterableListener>();

	private String _servletContextName;

}
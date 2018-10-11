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

package com.liferay.portal.internal.spring.aop;

import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.spring.aop.AdvisedSupportAdapter;
import com.liferay.portal.spring.aop.BeanMatcher;
import com.liferay.portal.spring.aop.ServiceBeanMatcher;

import org.aopalliance.intercept.MethodInterceptor;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.util.ClassUtils;

/**
 * @author Preston Crary
 */
public class CounterServiceBeanAutoProxyCreator
	extends AbstractAdvisorAutoProxyCreator {

	public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
		_methodInterceptor = methodInterceptor;
	}

	@Override
	protected void customizeProxyFactory(ProxyFactory proxyFactory) {
		proxyFactory.setAopProxyFactory(
			advisedSupport -> {
				AdvisedSupportAdapter advisedSupportAdapter =
					new AdvisedSupportAdapter(advisedSupport);

				Class<?>[] proxiedInterfaces =
					advisedSupportAdapter.getProxiedInterfaces();

				return new AopProxy() {

					@Override
					public Object getProxy() {
						return getProxy(ClassUtils.getDefaultClassLoader());
					}

					@Override
					public Object getProxy(ClassLoader classLoader) {
						return ProxyUtil.newProxyInstance(
							classLoader, proxiedInterfaces,
							new CounterMethodInterceptorProxyImpl(
								advisedSupportAdapter.getTarget(),
								proxiedInterfaces, _methodInterceptor));
					}

				};
			});
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Object[] getAdvicesAndAdvisorsForBean(
		Class beanClass, String beanName, TargetSource targetSource) {

		Object[] advices = DO_NOT_PROXY;

		if (_beanMatcher.match(beanClass, beanName)) {
			advices = super.getAdvicesAndAdvisorsForBean(
				beanClass, beanName, targetSource);

			if (advices == DO_NOT_PROXY) {
				advices = PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
			}
		}

		return advices;
	}

	private static final BeanMatcher _beanMatcher = new ServiceBeanMatcher(
		true);

	private MethodInterceptor _methodInterceptor;

}
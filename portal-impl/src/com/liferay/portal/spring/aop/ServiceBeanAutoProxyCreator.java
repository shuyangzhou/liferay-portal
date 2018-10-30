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

import com.liferay.portal.aop.cache.MethodInterceptorCache;
import com.liferay.portal.aop.spi.MethodInterceptorCacheManager;
import com.liferay.portal.internal.aop.MethodInterceptorProxyImpl;
import com.liferay.portal.kernel.spring.aop.AopProxyFactory;
import com.liferay.portal.kernel.util.ProxyUtil;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.ClassUtils;

/**
 * @author Shuyang Zhou
 */
public class ServiceBeanAutoProxyCreator
	extends AbstractAdvisorAutoProxyCreator {

	public void afterPropertiesSet() {

		// Backwards compatibility

		if (_beanMatcher == null) {
			_beanMatcher = new ServiceBeanMatcher();
		}

		BeanFactory beanFactory = getBeanFactory();

		_methodInterceptorCache = MethodInterceptorCacheManager.create(
			beanFactory::getBean);
	}

	public void destroy() {
		MethodInterceptorCacheManager.destroy(_methodInterceptorCache);
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
		_aopProxyFactory = aopProxyFactory;
	}

	public void setBeanMatcher(BeanMatcher beanMatcher) {
		_beanMatcher = beanMatcher;
	}

	@Override
	protected void customizeProxyFactory(ProxyFactory proxyFactory) {
		proxyFactory.setAopProxyFactory(
			advisedSupport -> {
				AdvisedSupportAdapter advisedSupportAdapter =
					new AdvisedSupportAdapter(advisedSupport);

				if (_aopProxyFactory == null) {
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
								new MethodInterceptorProxyImpl(
									advisedSupportAdapter.getTarget(),
									proxiedInterfaces,
									_methodInterceptorCache));
						}

					};
				}

				return new AopProxyAdapter(
					_aopProxyFactory.getAopProxy(advisedSupportAdapter));
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

	private AopProxyFactory _aopProxyFactory;
	private BeanMatcher _beanMatcher;
	private MethodInterceptorCache _methodInterceptorCache;

}
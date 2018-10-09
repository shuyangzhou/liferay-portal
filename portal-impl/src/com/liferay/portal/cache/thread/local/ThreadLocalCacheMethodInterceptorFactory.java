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

package com.liferay.portal.cache.thread.local;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.cache.thread.local.ThreadLocalCachable;
import com.liferay.portal.kernel.cache.thread.local.ThreadLocalCache;
import com.liferay.portal.kernel.cache.thread.local.ThreadLocalCacheManager;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.spring.aop.AnnotatedMethodInterceptor;
import com.liferay.portal.spring.aop.MethodInterceptorFactory;
import com.liferay.portal.spring.aop.MethodInterceptorFactoryHelper;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 * @author Brian Wing Shun Chan
 * @author Preston Crary
 */
public class ThreadLocalCacheMethodInterceptorFactory
	implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorFactoryHelper methodInterceptorFactoryHelper) {

		return new ThreadLocalCacheMethodInterceptor();
	}

	@Override
	public Class<ThreadLocalCachable> getAnnotationClass() {
		return ThreadLocalCachable.class;
	}

	private static class ThreadLocalCacheMethodInterceptor
		extends AnnotatedMethodInterceptor<ThreadLocalCachable> {

		@Override
		public Object invoke(MethodInvocation methodInvocation)
			throws Throwable {

			ThreadLocalCachable threadLocalCachable = findAnnotation(
				methodInvocation);

			if (threadLocalCachable == null) {
				return methodInvocation.proceed();
			}

			ThreadLocalCache<Object> threadLocalCache =
				ThreadLocalCacheManager.getThreadLocalCache(
					threadLocalCachable.scope(), methodInvocation.getMethod());

			String cacheKey = _getCacheKey(methodInvocation.getArguments());

			Object value = threadLocalCache.get(cacheKey);

			if (value != null) {
				if (value == nullResult) {
					return null;
				}

				return value;
			}

			Object result = methodInvocation.proceed();

			if (result == null) {
				threadLocalCache.put(cacheKey, nullResult);
			}
			else {
				threadLocalCache.put(cacheKey, result);
			}

			return result;
		}

		private String _getCacheKey(Object[] arguments) {
			if (arguments.length == 1) {
				return StringUtil.toHexString(arguments[0]);
			}

			StringBundler sb = new StringBundler(arguments.length * 2 - 1);

			for (int i = 0; i < arguments.length; i++) {
				sb.append(StringUtil.toHexString(arguments[i]));

				if ((i + 1) < arguments.length) {
					sb.append(StringPool.POUND);
				}
			}

			return sb.toString();
		}

	}

}
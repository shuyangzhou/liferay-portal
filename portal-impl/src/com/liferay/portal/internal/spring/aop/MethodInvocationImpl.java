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

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.spring.aop.AdvisedSupport;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.spring.aop.AdvisedSupportUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Objects;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 * @author Preston Crary
 */
public class MethodInvocationImpl implements MethodInvocation, Serializable {

	public MethodInvocationImpl(
		Object target, Method method, Object[] arguments) {

		_target = target;
		_method = method;
		_arguments = arguments;

		_method.setAccessible(true);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof MethodInvocation)) {
			return false;
		}

		MethodInvocation methodInvocation = (MethodInvocation)object;

		if (Objects.equals(_method, methodInvocation.getMethod())) {
			return true;
		}

		return false;
	}

	@Override
	public Object[] getArguments() {
		return _arguments;
	}

	@Override
	public Method getMethod() {
		return _method;
	}

	@Override
	public Method getStaticPart() {
		return _method;
	}

	@Override
	public Object getThis() {
		return _target;
	}

	@Override
	public int hashCode() {
		if (_hashCode == 0) {
			_hashCode = _method.hashCode();
		}

		return _hashCode;
	}

	public void mark() {
		_markIndex = _index;
	}

	@Override
	public Object proceed() throws Throwable {
		if (_index < _methodInterceptors.length) {
			return _methodInterceptors[_index++].invoke(this);
		}

		if ((_method.getDeclaringClass() == Object.class) &&
			Objects.equals("equals", _method.getName())) {

			Object argument = _arguments[0];

			if (argument == null) {
				return false;
			}

			if (ProxyUtil.isProxyClass(argument.getClass())) {
				AdvisedSupport advisedSupport =
					AdvisedSupportUtil.getAdvisedSupport(argument);

				if (advisedSupport != null) {
					argument = advisedSupport.getTarget();
				}
			}

			return _target.equals(argument);
		}

		try {
			return _method.invoke(_target, _arguments);
		}
		catch (InvocationTargetException ite) {
			throw ite.getTargetException();
		}
	}

	public void reset() {
		_index = _markIndex;
	}

	public void setMethodInterceptors(MethodInterceptor[] methodInterceptors) {
		_methodInterceptors = methodInterceptors;
	}

	@Override
	public String toString() {
		Class<?>[] parameterTypes = _method.getParameterTypes();

		StringBundler sb = new StringBundler(parameterTypes.length * 2 + 6);

		Class<?> declaringClass = _method.getDeclaringClass();

		sb.append(declaringClass.getName());

		sb.append(StringPool.PERIOD);
		sb.append(_method.getName());
		sb.append(StringPool.OPEN_PARENTHESIS);

		for (Class<?> parameterType : parameterTypes) {
			sb.append(parameterType.getName());

			sb.append(StringPool.COMMA);
		}

		if (parameterTypes.length > 0) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append(StringPool.CLOSE_PARENTHESIS);

		sb.append(StringPool.AT);

		Class<?> targetClass = _target.getClass();

		sb.append(targetClass.getName());

		return sb.toString();
	}

	private final Object[] _arguments;
	private int _hashCode;
	private int _index;
	private int _markIndex;
	private final Method _method;
	private MethodInterceptor[] _methodInterceptors;
	private final Object _target;

}
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

package com.liferay.portal.kernel.messaging.proxy;

import com.liferay.petra.reflect.AnnotationLocator;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.MethodKey;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Micha Kiener
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public class ProxyRequest implements Externalizable {

	/**
	 * The empty constructor is required by {@link Externalizable}. Do not use
	 * this for any other purpose.
	 */
	public ProxyRequest() {
	}

	public ProxyRequest(Method method, Object[] arguments) throws Exception {
		_method = method;
		_arguments = arguments;

		Boolean synchronous = _synchronousMap.get(method);

		if (synchronous == null) {
			synchronous = false;

			MessagingProxy messagingProxy = AnnotationLocator.locate(
				method, method.getDeclaringClass(), MessagingProxy.class);

			if (messagingProxy != null) {
				ProxyMode proxyMode = messagingProxy.mode();

				if (proxyMode.equals(ProxyMode.SYNC)) {
					synchronous = true;
				}
			}

			_synchronousMap.put(method, synchronous);
		}

		_synchronous = synchronous;
	}

	public Object execute(Object object) throws Exception {
		try {
			return _method.invoke(object, _arguments);
		}
		catch (InvocationTargetException invocationTargetException) {
			Throwable throwable = invocationTargetException.getCause();

			if (throwable instanceof Exception) {
				throw (Exception)throwable;
			}

			throw new Exception(throwable);
		}
	}

	public Object[] getArguments() {
		return _arguments;
	}

	public boolean hasReturnValue() {
		if (_method.getReturnType() == Void.TYPE) {
			return false;
		}

		return true;
	}

	public boolean isSynchronous() {
		return _synchronous;
	}

	@Override
	public void readExternal(ObjectInput objectInput)
		throws ClassNotFoundException, IOException {

		_arguments = (Object[])objectInput.readObject();

		MethodKey methodKey = (MethodKey)objectInput.readObject();

		try {
			_method = methodKey.getMethod();
		}
		catch (NoSuchMethodException noSuchMethodException) {
			throw new IOException(noSuchMethodException);
		}

		_synchronous = objectInput.readBoolean();
	}

	@Override
	public String toString() {
		return StringBundler.concat(
			"{arguments=", Arrays.toString(_arguments), ", method=", _method,
			", synchronous", _synchronous, "}");
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeObject(_arguments);
		objectOutput.writeObject(new MethodKey(_method));
		objectOutput.writeBoolean(_synchronous);
	}

	private static final Map<Method, Boolean> _synchronousMap =
		new ConcurrentHashMap<>();

	private Object[] _arguments;
	private Method _method;
	private boolean _synchronous;

}
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

package com.liferay.bean.portlet.extension;

import aQute.bnd.annotation.ProviderType;

import java.lang.reflect.Method;

import javax.portlet.PortletMode;

import javax.xml.namespace.QName;

/**
 * @author Neil Griffin
 */
@ProviderType
public class BeanMethodWrapper implements BeanMethod {

	public BeanMethodWrapper(BeanMethod beanMethod) {
		_beanMethod = beanMethod;
	}

	@Override
	public int compareTo(BeanMethod beanMethod) {
		return _beanMethod.compareTo(beanMethod);
	}

	@Override
	public String getActionName() {
		return _beanMethod.getActionName();
	}

	@Override
	public ManagedBean<?> getManagedBean() {
		return _beanMethod.getManagedBean();
	}

	@Override
	public Method getMethod() {
		return _beanMethod.getMethod();
	}

	@Override
	public MethodType getMethodType() {
		return _beanMethod.getMethodType();
	}

	@Override
	public int getOrdinal() {
		return _beanMethod.getOrdinal();
	}

	@Override
	public PortletMode getPortletMode() {
		return _beanMethod.getPortletMode();
	}

	@Override
	public String getResourceID() {
		return _beanMethod.getResourceID();
	}

	public BeanMethod getWrapped() {
		return _beanMethod;
	}

	@Override
	public Object invoke(Object... args) throws ReflectiveOperationException {
		return _beanMethod.invoke(args);
	}

	@Override
	public boolean isEventProcessor(QName qName) {
		return _beanMethod.isEventProcessor(qName);
	}

	private final BeanMethod _beanMethod;

}
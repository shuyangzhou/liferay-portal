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
public interface BeanMethod extends Comparable<BeanMethod> {

	@Override
	public int compareTo(BeanMethod beanMethod);

	public String getActionName();

	public ManagedBean<?> getManagedBean();

	public Method getMethod();

	public MethodType getMethodType();

	public int getOrdinal();

	public PortletMode getPortletMode();

	public String getResourceID();

	public Object invoke(Object... args) throws ReflectiveOperationException;

	public boolean isEventProcessor(QName qName);

}
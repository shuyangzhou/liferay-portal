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

import com.liferay.petra.lang.HashUtil;
import com.liferay.portal.kernel.util.Validator;

import java.lang.reflect.Method;

import java.util.Objects;

import javax.portlet.PortletMode;
import javax.portlet.ProcessAction;
import javax.portlet.RenderMode;
import javax.portlet.annotations.ActionMethod;
import javax.portlet.annotations.EventMethod;
import javax.portlet.annotations.HeaderMethod;
import javax.portlet.annotations.PortletQName;
import javax.portlet.annotations.RenderMethod;
import javax.portlet.annotations.ServeResourceMethod;

import javax.xml.namespace.QName;

/**
 * @author Neil Griffin
 */
@ProviderType
public class BaseBeanMethod implements BeanMethod {

	public BaseBeanMethod(
		BeanManager beanManager, ManagedBean<?> managedBean, Method method,
		MethodType methodType) {

		_beanManager = beanManager;
		_managedBean = managedBean;
		_method = method;
		_methodType = methodType;

		_ordinal = methodType.getOrdinal(method);
	}

	@Override
	public int compareTo(BeanMethod beanMethod) {
		return Integer.compare(_ordinal, beanMethod.getOrdinal());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BaseBeanMethod)) {
			return false;
		}

		BaseBeanMethod beanMethod = (BaseBeanMethod)obj;

		if ((_ordinal == beanMethod._ordinal) &&
			Objects.equals(_method, beanMethod.getMethod()) &&
			(_methodType == beanMethod.getMethodType())) {

			return true;
		}

		return false;
	}

	@Override
	public String getActionName() {
		ActionMethod actionMethod = _method.getAnnotation(ActionMethod.class);

		if (actionMethod != null) {
			String actionName = actionMethod.actionName();

			if (Validator.isNotNull(actionName)) {
				return actionName;
			}
		}

		ProcessAction processAction = _method.getAnnotation(
			ProcessAction.class);

		if (processAction == null) {
			return null;
		}

		return processAction.name();
	}

	@Override
	public ManagedBean<?> getManagedBean() {
		return _managedBean;
	}

	@Override
	public Method getMethod() {
		return _method;
	}

	@Override
	public MethodType getMethodType() {
		return _methodType;
	}

	@Override
	public int getOrdinal() {
		return _ordinal;
	}

	@Override
	public PortletMode getPortletMode() {
		HeaderMethod headerMethod = _method.getAnnotation(HeaderMethod.class);

		if (headerMethod != null) {
			String portletMode = headerMethod.portletMode();

			if (Validator.isNull(portletMode)) {
				return null;
			}

			return new PortletMode(portletMode);
		}

		RenderMethod renderMethod = _method.getAnnotation(RenderMethod.class);

		if (renderMethod != null) {
			String portletMode = renderMethod.portletMode();

			if (Validator.isNull(portletMode)) {
				return null;
			}

			return new PortletMode(portletMode);
		}

		RenderMode renderMode = _method.getAnnotation(RenderMode.class);

		if (renderMode == null) {
			return null;
		}

		String name = renderMode.name();

		if (Validator.isNull(name)) {
			return null;
		}

		return new PortletMode(name);
	}

	@Override
	public String getResourceID() {
		ServeResourceMethod serveResourceMethod = _method.getAnnotation(
			ServeResourceMethod.class);

		if (serveResourceMethod != null) {
			String resourceID = serveResourceMethod.resourceID();

			if (Validator.isNotNull(resourceID)) {
				return resourceID;
			}
		}

		return null;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, _method);

		hashCode = HashUtil.hash(hashCode, _ordinal);

		return HashUtil.hash(hashCode, _methodType);
	}

	@Override
	public Object invoke(Object... args) throws ReflectiveOperationException {
		Object beanInstance = _beanManager.getBeanInstance(_managedBean);

		return _method.invoke(beanInstance, args);
	}

	@Override
	public boolean isEventProcessor(QName qName) {
		EventMethod eventMethod = _method.getAnnotation(EventMethod.class);

		if (eventMethod == null) {
			return false;
		}

		PortletQName[] portletQNames = eventMethod.processingEvents();

		for (PortletQName portletQName : portletQNames) {
			String localPart = portletQName.localPart();

			if (localPart.equals(qName.getLocalPart())) {
				String namespaceURI = portletQName.namespaceURI();

				if (namespaceURI.equals(qName.getNamespaceURI())) {
					return true;
				}
			}
		}

		return false;
	}

	private final BeanManager _beanManager;
	private final ManagedBean<?> _managedBean;
	private final Method _method;
	private final MethodType _methodType;
	private final int _ordinal;

}
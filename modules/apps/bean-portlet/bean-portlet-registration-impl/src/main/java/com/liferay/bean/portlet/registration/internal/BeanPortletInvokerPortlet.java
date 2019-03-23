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

package com.liferay.bean.portlet.registration.internal;

import com.liferay.bean.portlet.extension.BeanMethod;
import com.liferay.bean.portlet.extension.BeanPortletMethodInvoker;
import com.liferay.bean.portlet.extension.MethodType;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventPortlet;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.HeaderPortlet;
import javax.portlet.HeaderRequest;
import javax.portlet.HeaderResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceServingPortlet;

/**
 * @author Neil Griffin
 */
public class BeanPortletInvokerPortlet
	implements EventPortlet, HeaderPortlet, Portlet, ResourceServingPortlet {

	public BeanPortletInvokerPortlet(
		Map<MethodType, List<BeanMethod>> beanMethods,
		BeanPortletMethodInvoker beanPortletMethodInvoker) {

		_beanMethods = beanMethods;
		_beanPortletMethodInvoker = beanPortletMethodInvoker;
	}

	@Override
	public void destroy() {
		try {
			_invokeBeanMethods(_beanMethods.get(MethodType.DESTROY));
		}
		catch (PortletException pe) {
			_log.error(pe, pe);
		}
	}

	@Override
	public void init(PortletConfig portletConfig) throws PortletException {
		_invokeBeanMethods(_beanMethods.get(MethodType.INIT), portletConfig);

		_portletConfig = portletConfig;
	}

	@Override
	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

		_invokeMethodWithActiveScopes(
			actionRequest, actionResponse, _beanMethods.get(MethodType.ACTION));
	}

	@Override
	public void processEvent(
			EventRequest eventRequest, EventResponse eventResponse)
		throws IOException, PortletException {

		List<BeanMethod> beanMethods = _beanMethods.get(MethodType.EVENT);

		if ((beanMethods == null) || beanMethods.isEmpty()) {
			return;
		}

		Event event = eventRequest.getEvent();

		List<BeanMethod> eventMethods = new ArrayList<>();

		for (BeanMethod beanMethod : beanMethods) {
			if (beanMethod.isEventProcessor(event.getQName())) {
				eventMethods.add(beanMethod);
			}
		}

		_invokeMethodWithActiveScopes(
			eventRequest, eventResponse, eventMethods);
	}

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		_invokeMethodWithActiveScopes(
			renderRequest, renderResponse, _beanMethods.get(MethodType.RENDER));
	}

	@Override
	public void renderHeaders(
			HeaderRequest headerRequest, HeaderResponse headerResponse)
		throws IOException, PortletException {

		_invokeMethodWithActiveScopes(
			headerRequest, headerResponse, _beanMethods.get(MethodType.HEADER));
	}

	@Override
	public void serveResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException, PortletException {

		_invokeMethodWithActiveScopes(
			resourceRequest, resourceResponse,
			_beanMethods.get(MethodType.SERVE_RESOURCE));
	}

	private void _invokeBeanMethods(
			List<BeanMethod> beanMethods, Object... args)
		throws PortletException {

		if ((beanMethods == null) || beanMethods.isEmpty()) {
			return;
		}

		for (BeanMethod beanMethod : beanMethods) {
			try {
				beanMethod.invoke(args);
			}
			catch (InvocationTargetException ite) {
				Throwable cause = ite.getCause();

				if (cause instanceof PortletException) {
					throw (PortletException)cause;
				}

				throw new PortletException(cause);
			}
			catch (Exception e) {
				throw new PortletException(e);
			}
		}
	}

	private void _invokeMethodWithActiveScopes(
			PortletRequest portletRequest, PortletResponse portletResponse,
			List<BeanMethod> beanMethods)
		throws PortletException {

		if ((beanMethods == null) || beanMethods.isEmpty()) {
			return;
		}

		_beanPortletMethodInvoker.invokeWithActiveScopes(
			portletRequest, portletResponse, _portletConfig, beanMethods);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BeanPortletInvokerPortlet.class);

	private final Map<MethodType, List<BeanMethod>> _beanMethods;
	private final BeanPortletMethodInvoker _beanPortletMethodInvoker;
	private PortletConfig _portletConfig;

}
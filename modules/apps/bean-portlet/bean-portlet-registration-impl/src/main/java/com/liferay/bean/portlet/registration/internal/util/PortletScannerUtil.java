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

package com.liferay.bean.portlet.registration.internal.util;

import com.liferay.bean.portlet.extension.BaseBeanMethod;
import com.liferay.bean.portlet.extension.BeanManager;
import com.liferay.bean.portlet.extension.BeanMethod;
import com.liferay.bean.portlet.extension.ManagedBean;
import com.liferay.bean.portlet.extension.MethodType;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.lang.reflect.Method;

import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventPortlet;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.HeaderPortlet;
import javax.portlet.HeaderRequest;
import javax.portlet.HeaderResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceServingPortlet;
import javax.portlet.annotations.ActionMethod;
import javax.portlet.annotations.DestroyMethod;
import javax.portlet.annotations.EventMethod;
import javax.portlet.annotations.HeaderMethod;
import javax.portlet.annotations.InitMethod;
import javax.portlet.annotations.RenderMethod;
import javax.portlet.annotations.ServeResourceMethod;

/**
 * @author Neil Griffin
 */
public class PortletScannerUtil {

	public static void scanNonannotatedBeanMethods(
		BeanManager beanManager, Class<?> beanPortletClass,
		Set<BeanMethod> beanMethods) {

		ManagedBean<?> managedBean = beanManager.resolveBean(beanPortletClass);

		if (Portlet.class.isAssignableFrom(beanPortletClass)) {
			try {
				Method processActionMethod = beanPortletClass.getMethod(
					"processAction", ActionRequest.class, ActionResponse.class);

				if (!processActionMethod.isAnnotationPresent(
						ActionMethod.class)) {

					beanMethods.add(
						new BaseBeanMethod(
							beanManager, managedBean, processActionMethod,
							MethodType.ACTION));
				}

				Method destroyMethod = beanPortletClass.getMethod("destroy");

				if (!destroyMethod.isAnnotationPresent(DestroyMethod.class)) {
					beanMethods.add(
						new BaseBeanMethod(
							beanManager, managedBean, destroyMethod,
							MethodType.DESTROY));
				}

				Method initMethod = beanPortletClass.getMethod(
					"init", PortletConfig.class);

				if (!initMethod.isAnnotationPresent(InitMethod.class)) {
					beanMethods.add(
						new BaseBeanMethod(
							beanManager, managedBean, initMethod,
							MethodType.INIT));
				}

				Method renderMethod = beanPortletClass.getMethod(
					"render", RenderRequest.class, RenderResponse.class);

				if (!renderMethod.isAnnotationPresent(RenderMethod.class)) {
					beanMethods.add(
						new BaseBeanMethod(
							beanManager, managedBean, renderMethod,
							MethodType.RENDER));
				}
			}
			catch (NoSuchMethodException nsme) {
				_log.error(nsme, nsme);
			}
		}

		if (EventPortlet.class.isAssignableFrom(beanPortletClass)) {
			try {
				Method eventMethod = beanPortletClass.getMethod(
					"processEvent", EventRequest.class, EventResponse.class);

				if (!eventMethod.isAnnotationPresent(EventMethod.class)) {
					beanMethods.add(
						new BaseBeanMethod(
							beanManager, managedBean, eventMethod,
							MethodType.EVENT));
				}
			}
			catch (NoSuchMethodException nsme) {
				_log.error(nsme, nsme);
			}
		}

		if (HeaderPortlet.class.isAssignableFrom(beanPortletClass)) {
			try {
				Method renderHeadersMethod = beanPortletClass.getMethod(
					"renderHeaders", HeaderRequest.class, HeaderResponse.class);

				if (!renderHeadersMethod.isAnnotationPresent(
						HeaderMethod.class)) {

					beanMethods.add(
						new BaseBeanMethod(
							beanManager, managedBean, renderHeadersMethod,
							MethodType.HEADER));
				}
			}
			catch (NoSuchMethodException nsme) {
				_log.error(nsme, nsme);
			}
		}

		if (ResourceServingPortlet.class.isAssignableFrom(beanPortletClass)) {
			try {
				Method serveResourceMethod = beanPortletClass.getMethod(
					"serveResource", ResourceRequest.class,
					ResourceResponse.class);

				if (!serveResourceMethod.isAnnotationPresent(
						ServeResourceMethod.class)) {

					beanMethods.add(
						new BaseBeanMethod(
							beanManager, managedBean, serveResourceMethod,
							MethodType.SERVE_RESOURCE));
				}
			}
			catch (NoSuchMethodException nsme) {
				_log.error(nsme, nsme);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortletScannerUtil.class);

}
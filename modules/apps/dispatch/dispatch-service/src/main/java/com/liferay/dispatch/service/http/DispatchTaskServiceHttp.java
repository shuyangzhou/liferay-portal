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

package com.liferay.dispatch.service.http;

import com.liferay.dispatch.service.DispatchTaskServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.http.TunnelUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

/**
 * Provides the HTTP utility for the
 * <code>DispatchTaskServiceUtil</code> service
 * utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it requires an additional
 * <code>HttpPrincipal</code> parameter.
 *
 * <p>
 * The benefits of using the HTTP utility is that it is fast and allows for
 * tunneling without the cost of serializing to text. The drawback is that it
 * only works with Java.
 * </p>
 *
 * <p>
 * Set the property <b>tunnel.servlet.hosts.allowed</b> in portal.properties to
 * configure security.
 * </p>
 *
 * <p>
 * The HTTP utility is only generated for remote services.
 * </p>
 *
 * @author Matija Petanjek
 * @see DispatchTaskServiceSoap
 * @generated
 */
public class DispatchTaskServiceHttp {

	public static com.liferay.dispatch.model.DispatchTask addDispatchTask(
			HttpPrincipal httpPrincipal, long userId, String name, String type,
			com.liferay.portal.kernel.util.UnicodeProperties
				typeSettingsUnicodeProperties)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				DispatchTaskServiceUtil.class, "addDispatchTask",
				_addDispatchTaskParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, userId, name, type, typeSettingsUnicodeProperties);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.dispatch.model.DispatchTask)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static void deleteDispatchTask(
			HttpPrincipal httpPrincipal, long dispatchTaskId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				DispatchTaskServiceUtil.class, "deleteDispatchTask",
				_deleteDispatchTaskParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, dispatchTaskId);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.dispatch.model.DispatchTask updateDispatchTask(
			HttpPrincipal httpPrincipal, long dispatchTaskId, String name,
			com.liferay.portal.kernel.util.UnicodeProperties
				typeSettingsUnicodeProperties)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				DispatchTaskServiceUtil.class, "updateDispatchTask",
				_updateDispatchTaskParameterTypes2);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, dispatchTaskId, name, typeSettingsUnicodeProperties);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.dispatch.model.DispatchTask)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.dispatch.model.DispatchTask
			updateDispatchTaskTrigger(
				HttpPrincipal httpPrincipal, long dispatchTaskId,
				boolean active, String cronExpression, int endDateMonth,
				int endDateDay, int endDateYear, int endDateHour,
				int endDateMinute, boolean neverEnd, int startDateMonth,
				int startDateDay, int startDateYear, int startDateHour,
				int startDateMinute)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				DispatchTaskServiceUtil.class, "updateDispatchTaskTrigger",
				_updateDispatchTaskTriggerParameterTypes3);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, dispatchTaskId, active, cronExpression, endDateMonth,
				endDateDay, endDateYear, endDateHour, endDateMinute, neverEnd,
				startDateMonth, startDateDay, startDateYear, startDateHour,
				startDateMinute);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.dispatch.model.DispatchTask)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		DispatchTaskServiceHttp.class);

	private static final Class<?>[] _addDispatchTaskParameterTypes0 =
		new Class[] {
			long.class, String.class, String.class,
			com.liferay.portal.kernel.util.UnicodeProperties.class
		};
	private static final Class<?>[] _deleteDispatchTaskParameterTypes1 =
		new Class[] {long.class};
	private static final Class<?>[] _updateDispatchTaskParameterTypes2 =
		new Class[] {
			long.class, String.class,
			com.liferay.portal.kernel.util.UnicodeProperties.class
		};
	private static final Class<?>[] _updateDispatchTaskTriggerParameterTypes3 =
		new Class[] {
			long.class, boolean.class, String.class, int.class, int.class,
			int.class, int.class, int.class, boolean.class, int.class,
			int.class, int.class, int.class, int.class
		};

}
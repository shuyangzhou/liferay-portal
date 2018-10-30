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

package com.liferay.portal.security.access.control;

import com.liferay.portal.aop.AnnotatedMethodInterceptor;
import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.internal.cluster.ClusterableMethodInterceptorFactory;
import com.liferay.portal.kernel.security.access.control.AccessControlUtil;
import com.liferay.portal.kernel.security.access.control.AccessControlled;
import com.liferay.portal.kernel.security.auth.AccessControlContext;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Tomas Polesovsky
 * @author Igor Spasic
 * @author Michael C. Han
 * @author Raymond Augé
 * @author Shuyang Zhou
 * @author Preston Crary
 */
public class AccessControlMethodInterceptorFactory
	implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorContext methodInterceptorContext) {

		return new AccessControlMethodInterceptor();
	}

	@Override
	public Class<AccessControlled> getAnnotationClass() {
		return AccessControlled.class;
	}

	@Override
	public Class<? extends MethodInterceptorFactory> getParentClass() {
		return ClusterableMethodInterceptorFactory.class;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	private class AccessControlMethodInterceptor
		extends AnnotatedMethodInterceptor<AccessControlled> {

		@Override
		protected Object before(MethodInvocation methodInvocation)
			throws Throwable {

			incrementServiceDepth();

			AccessControlled accessControlled = findAnnotation(
				methodInvocation);

			if (accessControlled == null) {
				return null;
			}

			_accessControlAdvisor.accept(methodInvocation, accessControlled);

			return null;
		}

		protected void decrementServiceDepth() {
			AccessControlContext accessControlContext =
				AccessControlUtil.getAccessControlContext();

			if (accessControlContext == null) {
				return;
			}

			Map<String, Object> settings = accessControlContext.getSettings();

			Integer serviceDepth = (Integer)settings.get(
				AccessControlContext.Settings.SERVICE_DEPTH.toString());

			if (serviceDepth == null) {
				return;
			}

			serviceDepth--;

			settings.put(
				AccessControlContext.Settings.SERVICE_DEPTH.toString(),
				serviceDepth);
		}

		@Override
		protected void duringFinally(MethodInvocation methodInvocation) {
			decrementServiceDepth();
		}

		protected void incrementServiceDepth() {
			AccessControlContext accessControlContext =
				AccessControlUtil.getAccessControlContext();

			if (accessControlContext == null) {
				return;
			}

			Map<String, Object> settings = accessControlContext.getSettings();

			Integer serviceDepth = (Integer)settings.get(
				AccessControlContext.Settings.SERVICE_DEPTH.toString());

			if (serviceDepth == null) {
				serviceDepth = Integer.valueOf(1);
			}
			else {
				serviceDepth++;
			}

			settings.put(
				AccessControlContext.Settings.SERVICE_DEPTH.toString(),
				serviceDepth);
		}

		private final AccessControlAdvisor _accessControlAdvisor =
			new AccessControlAdvisorImpl();

	}

}
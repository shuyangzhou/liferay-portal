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

package com.liferay.portal.workflow;

import com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalServiceUtil;
import com.liferay.portal.kernel.workflow.RequiredWorkflowDefinitionException;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Brian Wing Shun Chan
 */
public class WorkflowLinkAdvice implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Method method = methodInvocation.getMethod();

		String methodName = method.getName();

		Object[] arguments = methodInvocation.getArguments();

		if (methodName.equals(_UPDATE_ACTIVE)) {
			long companyId = (Long)arguments[0];
			String name = (String)arguments[2];
			int version = (Integer)arguments[3];
			boolean active = (Boolean)arguments[4];

			if (!active) {
				int workflowDefinitionLinksCount =
					WorkflowDefinitionLinkLocalServiceUtil.
						getWorkflowDefinitionLinksCount(
							companyId, name, version);

				if (workflowDefinitionLinksCount >= 1) {
					throw new RequiredWorkflowDefinitionException();
				}
			}
		}

		return methodInvocation.proceed();
	}

	private static final String _UPDATE_ACTIVE = "updateActive";

}
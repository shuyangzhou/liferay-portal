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

package com.liferay.portal.service.impl;

import com.liferay.exportimport.kernel.staging.MergeLayoutPrototypesThreadLocal;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.model.impl.VirtualLayout;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupLocalServiceUtil;
import com.liferay.portal.kernel.util.AutoResetThreadLocal;
import com.liferay.portal.kernel.workflow.WorkflowThreadLocal;
import com.liferay.portal.util.PropsValues;
import com.liferay.sites.kernel.util.SitesUtil;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.core.annotation.Order;

/**
 * @author Raymond Augé
 * @author Jorge Ferrer
 */
@Order(2)
public class LayoutLocalServiceVirtualLayoutsAdvice
	implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		if (MergeLayoutPrototypesThreadLocal.isInProgress()) {
			return methodInvocation.proceed();
		}

		Method method = methodInvocation.getMethod();

		String methodName = method.getName();
		Class<?>[] parameterTypes = method.getParameterTypes();

		Object[] arguments = methodInvocation.getArguments();

		if (methodName.equals("getLayouts") &&
			(Arrays.equals(parameterTypes, _TYPES_L_B_L) ||
			 Arrays.equals(parameterTypes, _TYPES_L_B_L_B_I_I))) {

			long groupId = (Long)arguments[0];

			Group group = GroupLocalServiceUtil.getGroup(groupId);

			boolean privateLayout = (Boolean)arguments[1];
			long parentLayoutId = (Long)arguments[2];

			LayoutSet layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(
				groupId, privateLayout);

			if (!_mergeLayouts(group, layoutSet, arguments)) {
				return methodInvocation.proceed();
			}

			List<Layout> layouts = (List<Layout>)methodInvocation.proceed();

			return _injectVirtualLayouts(
				group, layoutSet, layouts, parentLayoutId);
		}

		return methodInvocation.proceed();
	}

	protected List<Layout> addChildUserGroupLayouts(
			Group group, List<Layout> layouts)
		throws Exception {

		List<Layout> childLayouts = new ArrayList<>(layouts.size());

		for (Layout layout : layouts) {
			Group layoutGroup = layout.getGroup();

			if (layoutGroup.isUserGroup()) {
				childLayouts.add(new VirtualLayout(layout, group));
			}
			else {
				childLayouts.add(layout);
			}
		}

		return childLayouts;
	}

	protected List<Layout> addUserGroupLayouts(
			Group group, LayoutSet layoutSet, List<Layout> layouts,
			long parentLayoutId)
		throws Exception {

		layouts = new ArrayList<>(layouts);

		List<UserGroup> userUserGroups =
			UserGroupLocalServiceUtil.getUserUserGroups(group.getClassPK());

		for (UserGroup userGroup : userUserGroups) {
			Group userGroupGroup = userGroup.getGroup();

			List<Layout> userGroupLayouts = LayoutLocalServiceUtil.getLayouts(
				userGroupGroup.getGroupId(), layoutSet.isPrivateLayout(),
				parentLayoutId);

			for (Layout userGroupLayout : userGroupLayouts) {
				layouts.add(new VirtualLayout(userGroupLayout, group));
			}
		}

		return layouts;
	}

	private List<Layout> _injectVirtualLayouts(
			Group group, LayoutSet layoutSet, List<Layout> layouts,
			long parentLayoutId)
		throws Exception {

		if (PropsValues.USER_GROUPS_COPY_LAYOUTS_TO_USER_PERSONAL_SITE) {
			return layouts;
		}

		if (group.isUser()) {
			_virtualLayoutTargetGroupId.set(group.getGroupId());

			if (parentLayoutId == LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {
				return addUserGroupLayouts(
					group, layoutSet, layouts, parentLayoutId);
			}

			return addChildUserGroupLayouts(group, layouts);
		}

		if (group.isUserGroup() &&
			(parentLayoutId != LayoutConstants.DEFAULT_PARENT_LAYOUT_ID)) {

			long targetGroupId = _virtualLayoutTargetGroupId.get();

			if (targetGroupId != GroupConstants.DEFAULT_LIVE_GROUP_ID) {
				Group targetGroup = GroupLocalServiceUtil.getGroup(
					targetGroupId);

				return addChildUserGroupLayouts(targetGroup, layouts);
			}
		}

		return layouts;
	}

	private boolean _mergeLayouts(
		Group group, LayoutSet layoutSet, Object... arguments) {

		if (MergeLayoutPrototypesThreadLocal.isMergeComplete(
				"getLayouts", arguments) &&
			(!group.isUser() ||
			 PropsValues.USER_GROUPS_COPY_LAYOUTS_TO_USER_PERSONAL_SITE)) {

			return false;
		}

		boolean workflowEnabled = WorkflowThreadLocal.isEnabled();

		try {
			if (SitesUtil.isLayoutSetMergeable(group, layoutSet)) {
				WorkflowThreadLocal.setEnabled(false);

				SitesUtil.mergeLayoutSetPrototypeLayouts(group, layoutSet);
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to merge layouts for site template", e);
			}
		}
		finally {
			MergeLayoutPrototypesThreadLocal.setMergeComplete(
				"getLayouts", arguments);
			WorkflowThreadLocal.setEnabled(workflowEnabled);
		}

		return true;
	}

	private static final Class<?>[] _TYPES_L_B_L = {
		Long.TYPE, Boolean.TYPE, Long.TYPE
	};

	private static final Class<?>[] _TYPES_L_B_L_B_I_I = {
		Long.TYPE, Boolean.TYPE, Long.TYPE, Boolean.TYPE, Integer.TYPE,
		Integer.TYPE
	};

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutLocalServiceVirtualLayoutsAdvice.class);

	private static final ThreadLocal<Long> _virtualLayoutTargetGroupId =
		new AutoResetThreadLocal<Long>(
			LayoutLocalServiceVirtualLayoutsAdvice.class +
				"._virtualLayoutTargetGroupId",
			GroupConstants.DEFAULT_LIVE_GROUP_ID);

}
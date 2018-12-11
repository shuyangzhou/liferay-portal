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

package com.liferay.blogs.portlet.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.portal.kernel.exception.NoSuchLayoutException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.VirtualLayout;
import com.liferay.portal.kernel.portlet.BasePortletLayoutFinder;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.portlet.PortletLayoutFinder;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.servlet.PortletServlet;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.test.LayoutTestUtil;

import com.liferay.portlet.brides.mvc.FindMVCActionCommand;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.portlet.MockActionRequest;

/**
 * @author Julio Camarero
 * @author Laszlo Csontos
 * @author Eduardo Garcia
 */
@RunWith(Arquillian.class)
public class PortletLayoutFinderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_portletIds = new String[] {
			PortletProviderUtil.getPortletId(
				BlogsEntry.class.getName(), PortletProvider.Action.MANAGE),
			PortletProviderUtil.getPortletId(
				BlogsEntry.class.getName(), PortletProvider.Action.VIEW)
		};

		_portletLayoutFinder = new BasePortletLayoutFinder() {

			@Override
			protected String[] getPortletIds() {
				return _portletIds;
			}

		};

		User user = TestPropsValues.getUser();

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		_originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		PermissionThreadLocal.setPermissionChecker(permissionChecker);
	}

	@After
	public void tearDown() {
		PermissionThreadLocal.setPermissionChecker(_originalPermissionChecker);
	}

	@Test
	public void testGetPlidAndPortletIdViewInContext() throws Exception {
		addLayouts(true, false);

		PortletLayoutFinder.Result result = _portletLayoutFinder.find(
			getThemeDisplay(), _blogsEntryGroupId);

		Assert.assertEquals(_blogLayout.getPlid(), result.getPlid());

		String portletId = PortletProviderUtil.getPortletId(
			BlogsEntry.class.getName(), PortletProvider.Action.VIEW);

		Assert.assertEquals(portletId, result.getPortletId());
	}

	@Test(expected = NoSuchLayoutException.class)
	public void testGetPlidAndPortletIdWhenPortletDoesNotExist()
		throws Exception {

		addLayouts(false, false);

		_portletLayoutFinder.find(getThemeDisplay(), _blogsEntryGroupId);
	}

	@Test
	public void testSetTargetGroupWithDifferentGroup() throws Exception {
		addLayouts(true, true);

		ThemeDisplay themeDisplay = getThemeDisplay();

		ActionRequest actionRequest = getActionRequest(themeDisplay);

		ReflectionTestUtil.invoke(
			FindMVCActionCommand.class, "_setTargetLayout",
			new Class<?>[] {
				ThemeDisplay.class, ActionRequest.class, long.class, long.class
			},
			themeDisplay, actionRequest, _blogsEntryGroupId,
			_blogLayout.getPlid());

		Layout layout = (Layout)actionRequest.getAttribute(WebKeys.LAYOUT);

		Assert.assertTrue(layout instanceof VirtualLayout);
		Assert.assertNotEquals(_group.getGroupId(), layout.getGroupId());
	}

	@Test
	public void testSetTargetGroupWithSameGroup() throws Exception {
		addLayouts(true, false);

		ThemeDisplay themeDisplay = getThemeDisplay();

		ActionRequest actionRequest = getActionRequest(themeDisplay);

		ReflectionTestUtil.invoke(
			FindMVCActionCommand.class, "_setTargetLayout",
			new Class<?>[] {
				ThemeDisplay.class, ActionRequest.class, long.class, long.class
			},
			actionRequest, _blogsEntryGroupId, _blogLayout.getPlid());

		HttpServletRequest request = PortalUtil.getHttpServletRequest(
			actionRequest);

		Layout layout = (Layout)request.getAttribute(WebKeys.LAYOUT);

		Assert.assertNull(layout);
	}

	protected void addLayouts(
			boolean portletExists, boolean blogEntryWithDifferentGroup)
		throws Exception {

		_group = GroupTestUtil.addGroup();

		_blogLayout = LayoutTestUtil.addLayout(_group);
		_assetLayout = LayoutTestUtil.addLayout(_group);

		if (portletExists) {
			String portletId = PortletProviderUtil.getPortletId(
				BlogsEntry.class.getName(), PortletProvider.Action.VIEW);

			LayoutTestUtil.addPortletToLayout(_blogLayout, portletId);
		}

		Map<String, String[]> preferenceMap = new HashMap<>();

		preferenceMap.put("assetLinkBehavior", new String[] {"viewInPortlet"});

		_testPortletId = PortletIdCodec.encode(
			"com_liferay_hello_world_web_portlet_HelloWorldPortlet");

		LayoutTestUtil.addPortletToLayout(
			TestPropsValues.getUserId(), _assetLayout, _testPortletId,
			"column-1", preferenceMap);

		Group group = _group;

		if (blogEntryWithDifferentGroup) {
			group = GroupTestUtil.addGroup();
		}

		_blogsEntryGroupId = group.getGroupId();
	}

	protected ActionRequest getActionRequest(ThemeDisplay themeDisplay)
		throws Exception {

		HttpServletRequest request = new MockHttpServletRequest();

		request.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);

		ActionRequest actionRequest = new MockActionRequest();

		actionRequest.setAttribute(
			PortletServlet.PORTLET_SERVLET_REQUEST, request);

		return actionRequest;
	}

	protected ThemeDisplay getThemeDisplay() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setScopeGroupId(_group.getGroupId());

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(TestPropsValues.getUser());

		themeDisplay.setPermissionChecker(permissionChecker);

		themeDisplay.setPlid(_assetLayout.getPlid());

		return themeDisplay;
	}

	private static String[] _portletIds;

	private Layout _assetLayout;
	private Layout _blogLayout;
	private long _blogsEntryGroupId;

	@DeleteAfterTestRun
	private Group _group;

	private PermissionChecker _originalPermissionChecker;
	private PortletLayoutFinder _portletLayoutFinder;
	private String _testPortletId;

}
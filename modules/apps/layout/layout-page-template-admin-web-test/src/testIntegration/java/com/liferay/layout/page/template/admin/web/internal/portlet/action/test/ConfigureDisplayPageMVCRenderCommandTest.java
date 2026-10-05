/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.page.template.admin.web.internal.portlet.action.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.depot.constants.DepotConstants;
import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.service.DepotEntryLocalService;
import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.test.util.DisplayPageTemplateTestUtil;
import com.liferay.layout.test.util.ContentLayoutTestUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.PortletQName;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.constants.MVCRenderConstants;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.test.TestInfo;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletRenderRequest;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletRenderResponse;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.FeatureFlagTestUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.FeatureFlag;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Javier Moral
 */
@FeatureFlag("LPD-57283")
@RunWith(Arquillian.class)
public class ConfigureDisplayPageMVCRenderCommandTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_company = _companyLocalService.getCompany(_group.getCompanyId());

		_controlPanelLayout = _layoutLocalService.getLayout(
			_portal.getControlPanelPlid(_company.getCompanyId()));

		FeatureFlagTestUtil.invokeFeatureFlagListeners(
			_company.getCompanyId(), true, "LPD-57283");

		_depotGroup = _addDesignLibraryGroup();
	}

	@Test
	@TestInfo("LPD-107952")
	public void testRender() throws Exception {
		_testRenderRedirectsToPageConfiguration();
		_testRenderWithUnknownExternalReferenceCode();
		_testRenderWithoutExternalReferenceCode();
	}

	private Group _addDesignLibraryGroup() throws Exception {
		DepotEntry depotEntry = _depotEntryLocalService.addDepotEntry(
			Collections.singletonMap(
				LocaleUtil.getDefault(), RandomTestUtil.randomString()),
			null, DepotConstants.TYPE_DESIGN_LIBRARY,
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		return _groupLocalService.getGroup(depotEntry.getGroupId());
	}

	private String _getDecodedParameter(String name, String url) {
		return HttpComponentsUtil.decodeURL(
			HttpComponentsUtil.getParameter(url, name, false));
	}

	private String _getRedirect(
		MockLiferayPortletRenderResponse mockLiferayPortletRenderResponse) {

		MockHttpServletResponse mockHttpServletResponse =
			(MockHttpServletResponse)
				mockLiferayPortletRenderResponse.getHttpServletResponse();

		return mockHttpServletResponse.getRedirectedUrl();
	}

	private String _render(
			String externalReferenceCode,
			MockLiferayPortletRenderResponse mockLiferayPortletRenderResponse,
			String redirect)
		throws Exception {

		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			new MockLiferayPortletRenderRequest();

		ThemeDisplay themeDisplay = ContentLayoutTestUtil.getThemeDisplay(
			_company, _group, _controlPanelLayout);

		themeDisplay.setScopeGroupId(_depotGroup.getGroupId());

		mockLiferayPortletRenderRequest.setAttribute(
			WebKeys.THEME_DISPLAY, themeDisplay);

		mockLiferayPortletRenderRequest.setParameter(
			"displayPageTemplateExternalReferenceCode", externalReferenceCode);
		mockLiferayPortletRenderRequest.setParameter("redirect", redirect);

		return _mvcRenderCommand.render(
			mockLiferayPortletRenderRequest, mockLiferayPortletRenderResponse);
	}

	private void _testRenderRedirectsToPageConfiguration() throws Exception {
		String backURL = "/" + RandomTestUtil.randomString();
		LayoutPageTemplateEntry layoutPageTemplateEntry =
			DisplayPageTemplateTestUtil.addDisplayPageTemplate(
				_depotGroup.getGroupId());
		MockLiferayPortletRenderResponse mockLiferayPortletRenderResponse =
			new MockLiferayPortletRenderResponse();

		Assert.assertEquals(
			MVCRenderConstants.MVC_PATH_VALUE_SKIP_DISPATCH,
			_render(
				layoutPageTemplateEntry.getExternalReferenceCode(),
				mockLiferayPortletRenderResponse, backURL));

		String redirect = _getRedirect(mockLiferayPortletRenderResponse);

		Assert.assertEquals(
			LayoutAdminPortletKeys.GROUP_PAGES,
			_getDecodedParameter("p_p_id", redirect));
		Assert.assertTrue(
			redirect, redirect.contains(_depotGroup.getFriendlyURL()));

		String namespace = _portal.getPortletNamespace(
			LayoutAdminPortletKeys.GROUP_PAGES);

		Assert.assertEquals(
			backURL, _getDecodedParameter(namespace + "backURL", redirect));
		Assert.assertEquals(
			String.valueOf(_depotGroup.getGroupId()),
			_getDecodedParameter(namespace + "groupId", redirect));
		Assert.assertEquals(
			"/layout_admin/edit_layout",
			_getDecodedParameter(namespace + "mvcRenderCommandName", redirect));
		Assert.assertEquals(
			backURL, _getDecodedParameter(namespace + "redirect", redirect));

		Assert.assertEquals(
			String.valueOf(layoutPageTemplateEntry.getPlid()),
			_getDecodedParameter(
				PortletQName.PUBLIC_RENDER_PARAMETER_NAMESPACE + "selPlid",
				redirect));
	}

	private void _testRenderWithUnknownExternalReferenceCode()
		throws Exception {

		MockLiferayPortletRenderResponse mockLiferayPortletRenderResponse =
			new MockLiferayPortletRenderResponse();

		Assert.assertEquals(
			"/view.jsp",
			_render(
				RandomTestUtil.randomString(), mockLiferayPortletRenderResponse,
				""));
		Assert.assertNull(_getRedirect(mockLiferayPortletRenderResponse));
	}

	private void _testRenderWithoutExternalReferenceCode() throws Exception {
		MockLiferayPortletRenderResponse mockLiferayPortletRenderResponse =
			new MockLiferayPortletRenderResponse();

		Assert.assertEquals(
			"/view.jsp", _render("", mockLiferayPortletRenderResponse, ""));
		Assert.assertNull(_getRedirect(mockLiferayPortletRenderResponse));
	}

	private Company _company;

	@Inject
	private CompanyLocalService _companyLocalService;

	private Layout _controlPanelLayout;

	@Inject
	private DepotEntryLocalService _depotEntryLocalService;

	@DeleteAfterTestRun
	private Group _depotGroup;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private GroupLocalService _groupLocalService;

	@Inject
	private LayoutLocalService _layoutLocalService;

	@Inject(
		filter = "mvc.command.name=/layout_page_template_admin/configure_display_page"
	)
	private MVCRenderCommand _mvcRenderCommand;

	@Inject
	private Portal _portal;

}
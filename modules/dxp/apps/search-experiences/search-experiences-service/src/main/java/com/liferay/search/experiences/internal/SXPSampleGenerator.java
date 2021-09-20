/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.internal;

import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.search.experiences.rest.dto.v1_0.SXPBlueprint;
import com.liferay.search.experiences.rest.resource.v1_0.SXPBlueprintResource;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(enabled = false, immediate = true, service = {})
public class SXPSampleGenerator {

	@Activate
	protected void activate(BundleContext bundleContext) throws Exception {
		List<Company> companies = _companyLocalService.getCompanies();

		if (companies.size() != 1) {
			return;
		}

		Company company = companies.get(0);

		User user = _userLocalService.fetchUserByEmailAddress(
			company.getCompanyId(), "test@liferay.com");

		if (user == null) {
			return;
		}

		SXPBlueprintResource.Builder sxpBlueprintResourceBuilder =
			_sxpBlueprintResourceFactory.create();

		SXPBlueprintResource sxpBlueprintResource =
			sxpBlueprintResourceBuilder.user(
				user
			).build();

		sxpBlueprintResource.postSXPBlueprint(new SXPBlueprint());
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	private ModuleServiceLifecycle _moduleServiceLifecycle;

	@Reference
	private SXPBlueprintResource.Factory _sxpBlueprintResourceFactory;

	@Reference
	private UserLocalService _userLocalService;

}
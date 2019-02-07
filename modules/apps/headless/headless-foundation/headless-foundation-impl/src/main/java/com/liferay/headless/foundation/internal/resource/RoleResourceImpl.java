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

package com.liferay.headless.foundation.internal.resource;

import com.liferay.headless.foundation.dto.Role;
import com.liferay.headless.foundation.resource.RoleResource;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.vulcan.context.AcceptLanguage;
import com.liferay.portal.vulcan.context.Pagination;
import com.liferay.portal.vulcan.dto.Page;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.util.List;
import java.util.function.Function;

import javax.ws.rs.core.Context;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/role.properties", scope = ServiceScope.PROTOTYPE,
	service = RoleResource.class
)
public class RoleResourceImpl implements RoleResource {

	@Override
	public Page<Role> getMyUserAccountRolesPage(
			Long myUserAccountId, Pagination pagination)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	public Role getRole(Long rolesId) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Page<Role> getRolesPage(Pagination pagination) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Page<Role> getUserAccountRolesPage(
			Long userAccountId, Pagination pagination)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	protected <T, R> List<R> transform(
		List<T> list, Function<T, R> transformFunction) {

		return TransformUtil.transform(list, transformFunction);
	}

	@Context
	protected AcceptLanguage acceptLanguage;

	@Context
	protected Company company;

}
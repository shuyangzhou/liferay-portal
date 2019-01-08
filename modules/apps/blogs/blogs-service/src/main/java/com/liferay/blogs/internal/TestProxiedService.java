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

package com.liferay.blogs.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.aop.proxy.ProxiedService;
import com.liferay.portal.kernel.util.ProxyUtil;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Preston Crary
 */
@Component(
	immediate = true,
	property = ProxiedService.PROXY_REFERENCE_FIELD + "=_testInterface",
	service = ProxiedService.class
)
public class TestProxiedService implements ProxiedService, TestInterface {

	@Activate
	public void activate() {
		System.out.println("***** TestProxiedService activated *****");
	}

	@Override
	public String testMethod() {
		return StringBundler.concat(
			"***** Test ", PROXY_REFERENCE_FIELD, " is a proxy? ",
			ProxyUtil.isProxyClass(_testInterface.getClass()), " *****");
	}

	private TestInterface _testInterface;

}
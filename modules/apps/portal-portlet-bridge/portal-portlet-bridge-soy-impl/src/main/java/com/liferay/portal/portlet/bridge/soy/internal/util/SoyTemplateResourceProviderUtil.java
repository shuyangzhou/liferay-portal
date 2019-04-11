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

package com.liferay.portal.portlet.bridge.soy.internal.util;

import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.template.soy.util.SoyTemplateResourceProvider;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Tambara
 */
@Component(immediate = true, service = {})
public class SoyTemplateResourceProviderUtil {

	public static TemplateResource getTemplateResource(
		List<Bundle> bundles, String templatePath) {

		return _soyTemplateResourceProvider.getTemplateResource(
			bundles, templatePath);
	}

	@Reference(unbind = "-")
	protected void setSoyTemplateResourceProvider(
		SoyTemplateResourceProvider soyTemplateResourceProvider) {

		_soyTemplateResourceProvider = soyTemplateResourceProvider;
	}

	private static SoyTemplateResourceProvider _soyTemplateResourceProvider;

}
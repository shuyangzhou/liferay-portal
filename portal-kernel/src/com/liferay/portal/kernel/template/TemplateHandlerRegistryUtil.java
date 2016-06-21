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

package com.liferay.portal.kernel.template;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.util.ServiceRetriever;

import java.util.List;

/**
 * @author Juan Fernández
 */
@ProviderType
public class TemplateHandlerRegistryUtil {

	public static long[] getClassNameIds() {
		return _getTemplateHandlerRegistry().getClassNameIds();
	}

	public static TemplateHandler getTemplateHandler(long classNameId) {
		return _getTemplateHandlerRegistry().getTemplateHandler(classNameId);
	}

	public static TemplateHandler getTemplateHandler(String className) {
		return _getTemplateHandlerRegistry().getTemplateHandler(className);
	}

	public static List<TemplateHandler> getTemplateHandlers() {
		return _getTemplateHandlerRegistry().getTemplateHandlers();
	}

	private static TemplateHandlerRegistry _getTemplateHandlerRegistry() {
		return _serviceRetriever.getService();
	}

	private static final ServiceRetriever<TemplateHandlerRegistry>
		_serviceRetriever = new ServiceRetriever<>(
			TemplateHandlerRegistry.class);

}
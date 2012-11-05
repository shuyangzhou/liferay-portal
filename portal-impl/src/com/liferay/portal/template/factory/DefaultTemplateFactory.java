/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.template.factory;

import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.TemplateContextType;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.templateparser.TemplateFactoryContext;

/**
 * @author Tina Tian
 */
public class DefaultTemplateFactory extends BaseTemplateFactory {

	public DefaultTemplateFactory(
		String errorTemplateId, TemplateContextType templateContextType) {

		super(errorTemplateId, templateContextType);
	}

	@Override
	protected TemplateResource getTemplateResource(
			TemplateFactoryContext templateFactoryContext)
		throws Exception {

		return new StringTemplateResource(
			getTemplateId(templateFactoryContext),
			templateFactoryContext.getScript());
	}

}
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

package com.liferay.portal.internal.spring.context;

import com.liferay.portal.spring.context.ApplicationContextConfigurator;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Preston Crary
 */
public class PortalApplicationContextConfigurator
	implements ApplicationContextConfigurator {

	@Override
	public void configure(
		ConfigurableApplicationContext configurableApplicationContext) {

		ConfigurableBeanFactory configurableBeanFactory =
			configurableApplicationContext.getBeanFactory();

		configurableBeanFactory.addBeanPostProcessor(
			new AOPBeanPostProcessor(
				configurableBeanFactory,
				configurableApplicationContext.getClassLoader()));
	}

}
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

import com.liferay.petra.function.UnsafeConsumer;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Preston Crary
 */
public class PortalApplicationContextConfigurator
	implements UnsafeConsumer
		<ConfigurableApplicationContext, RuntimeException> {

	@Override
	public void accept(
		ConfigurableApplicationContext configurableApplicationContext) {

		ConfigurableBeanFactory configurableBeanFactory =
			configurableApplicationContext.getBeanFactory();

		configurableBeanFactory.addBeanPostProcessor(
			new BeanPostProcessor() {

				@Override
				public Object postProcessAfterInitialization(
					Object bean, String beanName) {

					return bean;
				}

				@Override
				public Object postProcessBeforeInitialization(
					Object bean, String beanName) {

					System.out.println("processing bean = " + beanName);

					return bean;
				}

			});
	}

}
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

import java.beans.PropertyDescriptor;

import java.lang.reflect.Constructor;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

import javax.sql.DataSource;

/**
 * @author Preston Crary
 */
public class AOPBeanPostProcessor
	implements SmartInstantiationAwareBeanPostProcessor {

	public AOPBeanPostProcessor(
		ConfigurableBeanFactory configurableBeanFactory,
		ClassLoader classLoader) {

		_configurableBeanFactory = configurableBeanFactory;
		_classLoader = classLoader;
		_dataSource = configurableBeanFactory.getBean(DataSource.class);
	}

	@Override
	public Constructor<?>[] determineCandidateConstructors(
		Class<?> beanClass, String beanName) {

		return null;
	}

	@Override
	public Object getEarlyBeanReference(Object bean, String beanName) {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		return bean;
	}

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) {
		return true;
	}

	@Override
	public Object postProcessBeforeInitialization(
		Object bean, String beanName) {

		System.out.println("processing bean = " + beanName);

		return bean;
	}

	@Override
	public Object postProcessBeforeInstantiation(
		Class<?> beanClass, String beanName) {

		return null;
	}

	@Override
	public PropertyValues postProcessPropertyValues(
		PropertyValues propertyValues, PropertyDescriptor[] propertyDescriptors,
		Object bean, String beanName) {

		return propertyValues;
	}

	@Override
	public Class<?> predictBeanType(Class<?> beanClass, String beanName) {
		return null;
	}

	private final ClassLoader _classLoader;
	private final DataSource _dataSource;
	private final ConfigurableBeanFactory _configurableBeanFactory;

}
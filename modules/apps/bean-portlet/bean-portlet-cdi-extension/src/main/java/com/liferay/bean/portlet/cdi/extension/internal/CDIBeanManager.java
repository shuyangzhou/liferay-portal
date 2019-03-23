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

package com.liferay.bean.portlet.cdi.extension.internal;

import com.liferay.bean.portlet.extension.BeanManager;
import com.liferay.bean.portlet.extension.ManagedBean;

import javax.enterprise.inject.spi.Bean;

/**
 * @author Neil Griffin
 */
public class CDIBeanManager implements BeanManager {

	public CDIBeanManager(javax.enterprise.inject.spi.BeanManager beanManager) {
		_beanManager = beanManager;
	}

	@Override
	public <T> T getBeanInstance(ManagedBean<T> managedBean) {
		Bean bean = (Bean)managedBean.getBean();

		return (T)_beanManager.getReference(
			bean, bean.getBeanClass(),
			_beanManager.createCreationalContext(bean));
	}

	@Override
	public <T> ManagedBean<T> resolveBean(Class<T> beanClass) {
		Bean<?> bean = _beanManager.resolve(_beanManager.getBeans(beanClass));

		return (ManagedBean<T>)new CDIManagedBean(bean);
	}

	private final javax.enterprise.inject.spi.BeanManager _beanManager;

}
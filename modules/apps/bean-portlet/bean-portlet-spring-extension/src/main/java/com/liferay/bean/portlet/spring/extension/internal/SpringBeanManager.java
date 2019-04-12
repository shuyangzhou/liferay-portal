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

package com.liferay.bean.portlet.spring.extension.internal;

import com.liferay.bean.portlet.extension.BeanManager;
import com.liferay.bean.portlet.extension.ManagedBean;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import org.springframework.beans.factory.BeanFactory;

/**
 * @author Neil Griffin
 */
public class SpringBeanManager implements BeanManager {

	public SpringBeanManager(BeanFactory beanFactory) {
		_beanFactory = beanFactory;
	}

	@Override
	public <T> T getBeanInstance(ManagedBean<T> managedBean) {
		T beanInstance = _beanFactory.getBean((Class<T>)managedBean.getBean());

		if (_log.isDebugEnabled()) {
			_log.debug("beanInstance=" + beanInstance);
		}

		return beanInstance;
	}

	@Override
	public <T> ManagedBean<T> resolveBean(Class<T> beanClass) {
		return (ManagedBean<T>)new SpringManagedBean(beanClass);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SpringBeanManager.class);

	private final BeanFactory _beanFactory;

}
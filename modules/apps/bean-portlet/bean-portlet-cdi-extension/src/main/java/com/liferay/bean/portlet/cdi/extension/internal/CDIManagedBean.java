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

import com.liferay.bean.portlet.extension.ManagedBean;

import javax.enterprise.inject.spi.Bean;

/**
 * @author Neil Griffin
 */
public class CDIManagedBean implements ManagedBean<Bean> {

	public CDIManagedBean(Bean<?> bean) {
		_bean = bean;
	}

	@Override
	public Bean getBean() {
		return _bean;
	}

	@Override
	public Class<?> getBeanType() {
		return _bean.getBeanClass();
	}

	private final Bean<?> _bean;

}
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

package com.liferay.portal.dao.orm.hibernate;

import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.spring.aop.MethodReturnedObjectSynchronizerManager.MethodReturnedObjectSynchronizer;

/**
 * @author Mariano Alvaro Saiz
 */
public class MVCCReturnedObjectSynchronizer
	extends MethodReturnedObjectSynchronizer {

	@Override
	public Object updateResult(Object result) {
		return getUpdatedResult(result);
	}

	protected Object getUpdatedResult(Object result) {
		if (result instanceof BaseModel<?> && result instanceof MVCCModel) {
			BaseModel<?> baseModel = (BaseModel<?>)result;
			MVCCModel mvccModel = (MVCCModel)result;

			Object updatedValue = EntityCacheUtil.getResult(
				baseModel.isEntityCacheEnabled(), result.getClass(),
				baseModel.getPrimaryKeyObj());

			if (updatedValue != null) {
				if (mvccModel.getMvccVersion() <
						((MVCCModel)updatedValue).getMvccVersion()) {

					return updatedValue;
				}
			}
		}

		return result;
	}

}
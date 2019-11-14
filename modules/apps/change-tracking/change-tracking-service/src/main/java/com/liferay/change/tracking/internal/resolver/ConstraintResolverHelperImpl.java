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

package com.liferay.change.tracking.internal.resolver;

import com.liferay.change.tracking.constants.CTConstants;
import com.liferay.change.tracking.resolver.helper.ConstraintResolverHelper;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.lang.SafeClosable;
import com.liferay.portal.kernel.change.tracking.CTCollectionThreadLocal;
import com.liferay.portal.kernel.model.change.tracking.CTModel;
import com.liferay.portal.kernel.service.change.tracking.CTService;

/**
 * @author Preston Crary
 */
public class ConstraintResolverHelperImpl<T extends CTModel<T>>
	implements ConstraintResolverHelper<T> {

	public ConstraintResolverHelperImpl(
		CTService<T> ctService, long ctCollectionId) {

		_ctService = ctService;
		_ctCollectionId = ctCollectionId;
	}

	@Override
	public T getCTModel() {
		try (SafeClosable safeClosable =
				CTCollectionThreadLocal.setCTCollectionId(_ctCollectionId)) {

			return _ctService.updateWithUnsafeFunction(
				ctPersistence -> ctPersistence.fetchByPrimaryKey(
					_ctPrimaryKey));
		}
	}

	@Override
	public <R, E extends Throwable> R getInProduction(
			UnsafeSupplier<R, E> unsafeSupplier)
		throws E {

		try (SafeClosable safeClosable =
				CTCollectionThreadLocal.setCTCollectionId(
					CTConstants.CT_COLLECTION_ID_PRODUCTION)) {

			return unsafeSupplier.get();
		}
	}

	@Override
	public T getProductionModel() {
		try (SafeClosable safeClosable =
				CTCollectionThreadLocal.setCTCollectionId(
					CTConstants.CT_COLLECTION_ID_PRODUCTION)) {

			return _ctService.updateWithUnsafeFunction(
				ctPersistence -> ctPersistence.fetchByPrimaryKey(
					_productionPrimaryKey));
		}
	}

	public void setPrimaryKeys(long productionPrimaryKey, long ctPrimaryKey) {
		_productionPrimaryKey = productionPrimaryKey;
		_ctPrimaryKey = ctPrimaryKey;
	}

	private final long _ctCollectionId;
	private long _ctPrimaryKey;
	private final CTService<T> _ctService;
	private long _productionPrimaryKey;

}
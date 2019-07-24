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

package com.liferay.portal.kernel.change.tracking;

import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.portal.kernel.util.ServiceProxyFactory;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Preston Crary
 */
@ProviderType
public class CTCollectionThreadLocal {

	public static long getCTCollectionId() {
		return _ctCollectionId.get();
	}

	public static void removeCTCollectionId() {
		_ctCollectionId.remove();
	}

	public static void setCtCollectionId(long ctCollectionId) {
		_ctCollectionId.set(ctCollectionId);
	}

	private static long _getCTCollectionId() {
		return _ctCollectionIdSupplier.getCTCollectionId();
	}

	private CTCollectionThreadLocal() {
	}

	private static final ThreadLocal<Long> _ctCollectionId =
		new CentralizedThreadLocal<>(
			CTCollectionThreadLocal.class + "._ctCollectionId",
			CTCollectionThreadLocal::_getCTCollectionId);
	private static volatile CTCollectionIdSupplier _ctCollectionIdSupplier =
		ServiceProxyFactory.newServiceTrackedInstance(
			CTCollectionIdSupplier.class, CTCollectionThreadLocal.class,
			"_ctCollectionIdSupplier", true);

}
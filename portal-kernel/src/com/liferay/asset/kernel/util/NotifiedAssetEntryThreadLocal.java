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

package com.liferay.asset.kernel.util;

import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.petra.lang.SafeCloseable;

/**
 * @author Jonathan McCann
 */
public class NotifiedAssetEntryThreadLocal {

	public static boolean isNotifiedAssetEntryIdsModified() {
		return _isNotifiedAssetEntryIdsModified.get();
	}

	public static void setNotifiedAssetEntryIdsModified(
		boolean notifiedAssetEntryIdsModified) {

		_isNotifiedAssetEntryIdsModified.set(notifiedAssetEntryIdsModified);
	}

	public static SafeCloseable setWithSafeCloseable(
		boolean notifiedAssetEntryIdsModified) {

		return _isNotifiedAssetEntryIdsModified.setWithSafeCloseable(
			notifiedAssetEntryIdsModified);
	}

	private static final CentralizedThreadLocal<Boolean>
		_isNotifiedAssetEntryIdsModified = new CentralizedThreadLocal<>(
			NotifiedAssetEntryThreadLocal.class +
				"._isNotifiedAssetEntryIdsModified",
			() -> Boolean.FALSE);

}
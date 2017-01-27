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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Carlos Sierra Andrés
 */
public class CacheResourceBundleLoader implements ResourceBundleLoader {

	public CacheResourceBundleLoader(
		ResourceBundleLoader resourceBundleLoader) {

		_resourceBundleLoader = resourceBundleLoader;
	}

	@Override
	public ResourceBundle loadResourceBundle(String languageId) {
		ResourceBundle resourceBundle = _resourceBundles.get(languageId);

		if (resourceBundle == null) {
			try {
				resourceBundle = _resourceBundleLoader.loadResourceBundle(
					languageId);
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}
			}

			if (resourceBundle == null) {
				_resourceBundles.put(languageId, _nullResourceBundle);
			}
			else {
				_resourceBundles.put(languageId, resourceBundle);
			}
		}
		else if (resourceBundle == _nullResourceBundle) {
			return null;
		}

		return resourceBundle;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CacheResourceBundleLoader.class);

	private static final ResourceBundle _nullResourceBundle =
		new NullResourceBundle();

	private final ResourceBundleLoader _resourceBundleLoader;
	private final Map<String, ResourceBundle> _resourceBundles =
		new ConcurrentHashMap<>();

	private static class NullResourceBundle extends ResourceBundle {

		@Override
		public Enumeration<String> getKeys() {
			throw new UnsupportedOperationException();
		}

		@Override
		protected Object handleGetObject(String key) {
			throw new UnsupportedOperationException();
		}

	}

}
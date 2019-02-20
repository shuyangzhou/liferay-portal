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

package com.liferay.asset.taglib.internal;

import com.liferay.asset.util.AssetHelper;
import com.liferay.item.selector.ItemSelector;
import com.liferay.portal.osgi.web.servlet.taglib.TaglibDependencyResolver;
import com.liferay.portal.osgi.web.servlet.taglib.helper.TaglibDependencyResolverHelper;

import javax.servlet.ServletContext;

/**
 * @author Preston Crary
 */
public class TaglibDependencyResolverUtil implements TaglibDependencyResolver {

	public static AssetHelper getAssetHelper() {
		return _assetHelper;
	}

	public static ItemSelector getItemSelector() {
		return _itemSelector;
	}

	public static ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	public void resolveDependencies(
		TaglibDependencyResolverHelper taglibDependencyResolverHelper) {

		taglibDependencyResolverHelper.resolve(
			AssetHelper.class, this::setAssetHelper);
		taglibDependencyResolverHelper.resolve(
			ItemSelector.class, this::setItemSelector);
		taglibDependencyResolverHelper.resolve(
			ServletContext.class,
			"(osgi.web.symbolicname=com.liferay.asset.taglib)",
			this::setServletContext);
	}

	protected void setAssetHelper(AssetHelper assetHelper) {
		_assetHelper = assetHelper;
	}

	protected void setItemSelector(ItemSelector itemSelector) {
		_itemSelector = itemSelector;
	}

	protected void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	private static AssetHelper _assetHelper;
	private static ItemSelector _itemSelector;
	private static ServletContext _servletContext;

}
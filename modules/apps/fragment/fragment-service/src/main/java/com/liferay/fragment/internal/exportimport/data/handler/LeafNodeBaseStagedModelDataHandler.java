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

package com.liferay.fragment.internal.exportimport.data.handler;

import com.liferay.exportimport.data.handler.base.BaseStagedModelDataHandler;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.model.StagedModel;

/**
 * @author Shuyang Zhou
 */
public abstract class LeafNodeBaseStagedModelDataHandler<T extends StagedModel>
	extends BaseStagedModelDataHandler<T> {

	@Override
	protected void exportAssetCategories(
		PortletDataContext portletDataContext, T stagedModel) {
	}

	@Override
	protected void exportAssetTags(
		PortletDataContext portletDataContext, T stagedModel) {
	}

	@Override
	protected void exportComments(
		PortletDataContext portletDataContext, T stagedModel) {
	}

	@Override
	protected void exportRatings(
		PortletDataContext portletDataContext, T stagedModel) {
	}

	@Override
	protected void importAssetCategories(
		PortletDataContext portletDataContext, T stagedModel) {
	}

	@Override
	protected void importAssetTags(
		PortletDataContext portletDataContext, T stagedModel) {
	}

	@Override
	protected void importComments(
		PortletDataContext portletDataContext, T stagedModel) {
	}

	@Override
	protected void importRatings(
		PortletDataContext portletDataContext, T stagedModel) {
	}

}
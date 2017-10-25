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

package com.liferay.exportimport.staged.model.repository.base;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataException;
import com.liferay.exportimport.staged.model.repository.StagedModelRepository;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.StagedModel;
import com.liferay.portal.kernel.model.TrashedModel;

import java.util.List;

/**
 * @author     Daniel Kocsis
 * @deprecated As of 4.0.0
 */
@Deprecated
public abstract class BaseStagedModelRepository<T extends StagedModel>
	implements StagedModelRepository<T> {

	@Override
	public abstract T addStagedModel(
			PortletDataContext portletDataContext, T stagedModel)
		throws PortalException;

	@Override
	public abstract void deleteStagedModel(
			String uuid, long groupId, String className, String extraData)
		throws PortalException;

	@Override
	public abstract void deleteStagedModel(T stagedModel)
		throws PortalException;

	@Override
	public abstract void deleteStagedModels(
			PortletDataContext portletDataContext)
		throws PortalException;

	/**
	 * @deprecated as of 4.0.0
	 */
	@Deprecated
	@Override
	public T fetchMissingReference(String uuid, long groupId) {
		return null;
	}

	@Override
	public T fetchStagedModelByUuidAndGroupId(String uuid, long groupId) {
		return null;
	}

	@Override
	public abstract List<T> fetchStagedModelsByUuidAndCompanyId(
		String uuid, long companyId);

	@Override
	public abstract ExportActionableDynamicQuery
		getExportActionableDynamicQuery(PortletDataContext portletDataContext);

	/**
	 * @deprecated as of 4.0.0
	 */
	@Deprecated
	@Override
	public void restoreStagedModel(
			PortletDataContext portletDataContext, T stagedModel)
		throws PortletDataException {
	}

	@Override
	public abstract T saveStagedModel(T stagedModel) throws PortalException;

	@Override
	public abstract T updateStagedModel(
			PortletDataContext portletDataContext, T stagedModel)
		throws PortalException;

	protected boolean isStagedModelInTrash(T stagedModel) {
		if (!(stagedModel instanceof TrashedModel)) {
			return false;
		}

		TrashedModel trashedModel = (TrashedModel)stagedModel;

		return trashedModel.isInTrash();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseStagedModelRepository.class);

}
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

package com.liferay.portal.kernel.service.version;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.version.VersionModel;
import com.liferay.portal.kernel.model.version.VersionedModel;

import java.util.List;

/**
 * @author Preston Crary
 */
public interface VersionService
	<E extends VersionedModel<V>, V extends VersionModel<E>> {

	public E create();

	public List<V> delete(E versionedModel);

	public V deleteVersion(V versionModel) throws PortalException;

	public E fetchDraft(E versionedModel);

	public E fetchDraft(long primaryKey);

	public V fetchLatestVersion(E versionedModel);

	public E fetchPublished(E versionedModel);

	public E fetchPublished(long primaryKey);

	public E getDraft(E versionedEntry);

	public E getDraft(long primaryKey) throws PortalException;

	public V getVersion(E versionedModel, int version) throws PortalException;

	public List<V> getVersions(E versionedModel);

	public E publish(E versionedModel) throws PortalException;

	public E revert(E versionedModel, int version) throws PortalException;

	public E update(E versionedModel);

}
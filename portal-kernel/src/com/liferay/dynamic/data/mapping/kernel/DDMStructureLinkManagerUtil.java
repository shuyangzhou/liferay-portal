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

package com.liferay.dynamic.data.mapping.kernel;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ServiceRetriever;

import java.util.List;

/**
 * @author Rafael Praxedes
 */
@ProviderType
public class DDMStructureLinkManagerUtil {

	public static DDMStructureLink addStructureLink(
		long classNameId, long classPK, long structureId) {

		return _getDDMStructureLinkManager().addStructureLink(
			classNameId, classPK, structureId);
	}

	public static void deleteStructureLink(
			long classNameId, long classPK, long structureId)
		throws PortalException {

		_getDDMStructureLinkManager().deleteStructureLink(
			classNameId, classPK, structureId);
	}

	public static void deleteStructureLinks(long classNameId, long classPK) {
		_getDDMStructureLinkManager().deleteStructureLinks(
			classNameId, classPK);
	}

	public static List<DDMStructureLink> getClassNameStructureLinks(
		long classNameId) {

		return _getDDMStructureLinkManager().getClassNameStructureLinks(
			classNameId);
	}

	public static List<DDMStructureLink> getStructureLinks(
		long classNameId, long classPK) {

		return _getDDMStructureLinkManager().getStructureLinks(
			classNameId, classPK);
	}

	private static DDMStructureLinkManager _getDDMStructureLinkManager() {
		return _serviceRetriever.getService();
	}

	private static final ServiceRetriever<DDMStructureLinkManager>
		_serviceRetriever = new ServiceRetriever<>(
			DDMStructureLinkManager.class);

}
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

package com.liferay.portlet.display.template.internal.exportimport.portlet.preferences.processor;

import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataException;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.portlet.preferences.processor.Capability;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portletdisplaytemplate.PortletDisplayTemplateManager;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.template.TemplateHandler;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.display.template.exportimport.portlet.preferences.processor.CustomizedImportCapabilityRegister;
import com.liferay.portlet.display.template.internal.PortletDisplayTemplateUtil;

import java.util.Map;

import javax.portlet.PortletPreferences;

/**
 * @author Mate Thurzo
 */
public class PortletDisplayTemplateImportCapability implements Capability {

	public PortletDisplayTemplateImportCapability(
		CustomizedImportCapabilityRegister importCapabilityRegister,
		Portal portal, PortletLocalService portletLocalService) {

		this.portal = portal;
		_importCapabilityRegister = importCapabilityRegister;
		_portletLocalService = portletLocalService;
	}

	public PortletDisplayTemplateImportCapability(
		Portal portal, PortletLocalService portletLocalService) {

		this(null, portal, portletLocalService);
	}

	@Override
	public PortletPreferences process(
			PortletDataContext portletDataContext,
			PortletPreferences portletPreferences)
		throws PortletDataException {

		try {
			return importDisplayStyle(
				portletDataContext, portletDataContext.getPortletId(),
				portletPreferences);
		}
		catch (Exception e) {
			return portletPreferences;
		}
	}

	protected long getClassNameId(
		PortletDataContext portletDataContext, String portletId) {

		Portlet portlet = _portletLocalService.getPortletById(
			portletDataContext.getCompanyId(), portletId);

		TemplateHandler templateHandler = portlet.getTemplateHandlerInstance();

		if (templateHandler == null) {
			return 0;
		}

		String className = templateHandler.getClassName();

		return portal.getClassNameId(className);
	}

	protected String getDisplayStyle(
		PortletDataContext portletDataContext, String portletId,
		PortletPreferences portletPreferences) {

		try {
			if (_importCapabilityRegister != null) {
				return _importCapabilityRegister.getDisplayStyle(
					portletDataContext, portletId, portletPreferences);
			}

			Portlet portlet = _portletLocalService.getPortletById(
				portletDataContext.getCompanyId(), portletId);

			if (Validator.isNotNull(portlet.getTemplateHandlerInstance())) {
				return portletPreferences.getValue("displayStyle", null);
			}
		}
		catch (Exception e) {
		}

		return null;
	}

	protected long getDisplayStyleGroupId(
		PortletDataContext portletDataContext, String portletId,
		PortletPreferences portletPreferences) {

		try {
			if (_importCapabilityRegister != null) {
				return _importCapabilityRegister.getDisplayStyleGroupId(
					portletDataContext, portletId, portletPreferences);
			}

			Portlet portlet = _portletLocalService.getPortletById(
				portletDataContext.getCompanyId(), portletId);

			if (Validator.isNotNull(portlet.getTemplateHandlerInstance())) {
				return GetterUtil.getLong(
					portletPreferences.getValue("displayStyleGroupId", null));
			}
		}
		catch (Exception e) {
		}

		return 0;
	}

	protected PortletPreferences importDisplayStyle(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		PortletPreferences processedPreferences = portletPreferences;

		String displayStyle = getDisplayStyle(
			portletDataContext, portletId, portletPreferences);

		if (Validator.isNull(displayStyle) ||
			!displayStyle.startsWith(
				PortletDisplayTemplateManager.DISPLAY_STYLE_PREFIX)) {

			return processedPreferences;
		}

		StagedModelDataHandlerUtil.importReferenceStagedModels(
			portletDataContext, DDMTemplate.class);

		long displayStyleGroupId = getDisplayStyleGroupId(
			portletDataContext, portletId, portletPreferences);

		Map<Long, Long> groupIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				Group.class);

		long groupId = MapUtil.getLong(
			groupIds, displayStyleGroupId, displayStyleGroupId);

		DDMTemplate ddmTemplate =
			PortletDisplayTemplateUtil.getPortletDisplayTemplateDDMTemplate(
				groupId, getClassNameId(portletDataContext, portletId),
				displayStyle, false);

		if (ddmTemplate != null) {
			portletPreferences.setValue(
				"displayStyleGroupId",
				String.valueOf(ddmTemplate.getGroupId()));
		}
		else {
			portletPreferences.setValue(
				"displayStyleGroupId", StringPool.BLANK);
		}

		return processedPreferences;
	}

	protected Portal portal;

	private CustomizedImportCapabilityRegister _importCapabilityRegister;
	private PortletLocalService _portletLocalService;

}
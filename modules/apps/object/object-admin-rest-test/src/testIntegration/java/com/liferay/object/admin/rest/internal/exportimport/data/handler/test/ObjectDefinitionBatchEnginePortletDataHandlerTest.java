/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.admin.rest.internal.exportimport.data.handler.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.exportimport.test.rule.ExportImportScopeClassTestRule;
import com.liferay.exportimport.test.util.exportimport.data.handler.BaseBatchEnginePortletDataHandlerTestCase;
import com.liferay.exportimport.vulcan.batch.engine.ExportImportVulcanBatchEngineTaskItemDelegate;
import com.liferay.exportimport.vulcan.batch.engine.ExportImportVulcanBatchEngineTaskItemDelegate.Scope;
import com.liferay.object.admin.rest.resource.v1_0.ObjectDefinitionResource;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.field.builder.TextObjectFieldBuilder;
import com.liferay.object.field.util.ObjectFieldUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.test.util.ObjectDefinitionTestUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;

import java.util.Collections;
import java.util.Date;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * @author Alejandro Tardín
 */
@RunWith(Arquillian.class)
public class ObjectDefinitionBatchEnginePortletDataHandlerTest
	extends BaseBatchEnginePortletDataHandlerTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@ClassRule
	public static final ExportImportScopeClassTestRule
		exportImportScopeClassTestRule = new ExportImportScopeClassTestRule(
			Scope.COMPANY);

	@Override
	protected String addEmptyEntry(long groupId, long userId) throws Exception {
		ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.getOrAddEmptyObjectDefinition(
				ObjectDefinitionTestUtil.getRandomName(),
				_getCompanyId(groupId), userId, 0, true,
				ObjectDefinitionConstants.SCOPE_COMPANY, false);

		return objectDefinition.getExternalReferenceCode();
	}

	@Override
	protected String addEntry(long groupId, Date modifiedDate, long userId)
		throws Exception {

		ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.addCustomObjectDefinition(
				RandomTestUtil.randomString(), userId, 0, null, null, true,
				true, false, false, true, false, false, false, false, null,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				ObjectDefinitionTestUtil.getRandomName(), null, null,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				true, ObjectDefinitionConstants.SCOPE_COMPANY,
				ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT,
				Collections.emptyList(), Collections.emptyList(),
				Collections.emptyList(), new ServiceContext());

		objectDefinition.setModifiedDate(modifiedDate);

		objectDefinition = _objectDefinitionLocalService.updateObjectDefinition(
			objectDefinition);

		return objectDefinition.getExternalReferenceCode();
	}

	@Override
	protected void addStagedModels() throws Exception {
	}

	@Override
	protected void deleteEntry(String externalReferenceCode, long groupId)
		throws Exception {

		_objectDefinitionLocalService.deleteObjectDefinition(
			_fetchObjectDefinition(externalReferenceCode, groupId));
	}

	@Override
	protected Object fetchEntry(String externalReferenceCode, long groupId)
		throws Exception {

		return _fetchObjectDefinition(externalReferenceCode, groupId);
	}

	@Override
	protected long getCreatorUserId(String externalReferenceCode, long groupId)
		throws Exception {

		ObjectDefinition objectDefinition = _fetchObjectDefinition(
			externalReferenceCode, groupId);

		return objectDefinition.getUserId();
	}

	@Override
	protected Object getEntryValue(String externalReferenceCode, long groupId)
		throws Exception {

		ObjectDefinition objectDefinition = _fetchObjectDefinition(
			externalReferenceCode, groupId);

		return objectDefinition.getLabel(
			objectDefinition.getDefaultLanguageId());
	}

	@Override
	protected ExportImportScopeClassTestRule
		getExportImportScopeClassTestRule() {

		return exportImportScopeClassTestRule;
	}

	@Override
	protected ExportImportVulcanBatchEngineTaskItemDelegate<?>
		getExportImportVulcanBatchEngineTaskItemDelegate() {

		return (ExportImportVulcanBatchEngineTaskItemDelegate<?>)
			_objectDefinitionResource;
	}

	@Override
	protected long getPrimaryKey(String externalReferenceCode, long groupId)
		throws Exception {

		ObjectDefinition objectDefinition = _fetchObjectDefinition(
			externalReferenceCode, groupId);

		return objectDefinition.getObjectDefinitionId();
	}

	@Override
	protected int getStatus(String externalReferenceCode, long groupId)
		throws Exception {

		ObjectDefinition objectDefinition = _fetchObjectDefinition(
			externalReferenceCode, groupId);

		return objectDefinition.getStatus();
	}

	@Override
	protected boolean supportsComments() {
		return false;
	}

	@Override
	protected boolean supportsEmptyEntries() {
		return true;
	}

	@Override
	protected boolean supportsPermissions() {
		return true;
	}

	@Override
	protected void updateEntry(String externalReferenceCode, long groupId)
		throws Exception {

		ObjectDefinition objectDefinition = _fetchObjectDefinition(
			externalReferenceCode, groupId);

		objectDefinition =
			_objectDefinitionLocalService.updateCustomObjectDefinition(
				objectDefinition.getExternalReferenceCode(),
				objectDefinition.getObjectDefinitionId(),
				objectDefinition.getAccountEntryRestrictedObjectFieldId(),
				objectDefinition.getDescriptionObjectFieldId(),
				objectDefinition.getObjectFolderId(),
				objectDefinition.getTitleObjectFieldId(),
				objectDefinition.isAccountEntryRestricted(),
				objectDefinition.isActive(), objectDefinition.getClassName(),
				objectDefinition.getDescriptionMap(),
				objectDefinition.isEnableCategorization(),
				objectDefinition.isEnableComments(),
				objectDefinition.isEnableFormContainer(),
				objectDefinition.isEnableFriendlyURLCustomization(),
				objectDefinition.isEnableIndexSearch(),
				objectDefinition.isEnableObjectEntryDraft(),
				objectDefinition.isEnableObjectEntryHistory(),
				objectDefinition.isEnableObjectEntrySchedule(),
				objectDefinition.isEnableObjectEntrySubscription(),
				objectDefinition.isEnableObjectEntryVersioning(),
				objectDefinition.getFriendlyURLSeparator(),
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				objectDefinition.getShortName(),
				objectDefinition.getPanelAppOrder(),
				objectDefinition.getPanelCategoryKey(),
				objectDefinition.isPortlet(),
				objectDefinition.getPluralLabelMap(),
				objectDefinition.getScope(), objectDefinition.getStatus(),
				Collections.emptyList(), Collections.emptyList(),
				Collections.emptyList(), new ServiceContext());

		if (objectDefinition.isApproved()) {
			return;
		}

		ObjectFieldUtil.addCustomObjectField(
			new TextObjectFieldBuilder(
			).userId(
				objectDefinition.getUserId()
			).labelMap(
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString())
			).name(
				"x" + RandomTestUtil.randomString()
			).objectDefinitionId(
				objectDefinition.getObjectDefinitionId()
			).required(
				false
			).build());

		_objectDefinitionLocalService.publishCustomObjectDefinition(
			objectDefinition.getUserId(),
			objectDefinition.getObjectDefinitionId());
	}

	private ObjectDefinition _fetchObjectDefinition(
			String externalReferenceCode, long groupId)
		throws Exception {

		return _objectDefinitionLocalService.
			fetchObjectDefinitionByExternalReferenceCode(
				externalReferenceCode, _getCompanyId(groupId));
	}

	private long _getCompanyId(long groupId) throws Exception {
		Group group = _groupLocalService.getGroup(groupId);

		return group.getCompanyId();
	}

	@Inject
	private GroupLocalService _groupLocalService;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Inject(
		filter = "export.import.vulcan.batch.engine.task.item.delegate=true"
	)
	private ObjectDefinitionResource _objectDefinitionResource;

}
/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.object.internal.exportimport.data.handler.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.exportimport.test.rule.ExportImportScopeClassTestRule;
import com.liferay.exportimport.test.util.exportimport.data.handler.BaseBatchEnginePortletDataHandlerTestCase;
import com.liferay.exportimport.vulcan.batch.engine.ExportImportVulcanBatchEngineTaskItemDelegate;
import com.liferay.exportimport.vulcan.batch.engine.ExportImportVulcanBatchEngineTaskItemDelegate.Scope;
import com.liferay.headless.object.resource.v1_0.ObjectEntryFolderResource;
import com.liferay.object.constants.ObjectEntryFolderConstants;
import com.liferay.object.model.ObjectEntryFolder;
import com.liferay.object.service.ObjectEntryFolderLocalService;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Date;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * @author Alejandro Tardín
 */
@RunWith(Arquillian.class)
public class ObjectEntryFolderBatchEnginePortletDataHandlerTest
	extends BaseBatchEnginePortletDataHandlerTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@ClassRule
	public static final ExportImportScopeClassTestRule
		exportImportScopeClassTestRule = new ExportImportScopeClassTestRule(
			Scope.DEPOT);

	@Override
	protected String addEmptyEntry(long groupId, long userId) throws Exception {
		ObjectEntryFolder objectEntryFolder =
			_objectEntryFolderLocalService.getOrAddEmptyObjectEntryFolder(
				RandomTestUtil.randomString(), groupId, _getCompanyId(groupId),
				userId,
				ServiceContextTestUtil.getServiceContext(groupId, userId));

		return objectEntryFolder.getExternalReferenceCode();
	}

	@Override
	protected String addEntry(long groupId, Date modifiedDate, long userId)
		throws Exception {

		ObjectEntryFolder objectEntryFolder =
			_objectEntryFolderLocalService.addObjectEntryFolder(
				RandomTestUtil.randomString(), groupId, userId,
				ObjectEntryFolderConstants.
					PARENT_OBJECT_ENTRY_FOLDER_ID_DEFAULT,
				RandomTestUtil.randomString(),
				HashMapBuilder.put(
					LocaleUtil.getDefault(), RandomTestUtil.randomString()
				).build(),
				RandomTestUtil.randomString(),
				ServiceContextTestUtil.getServiceContext(groupId, userId));

		objectEntryFolder.setModifiedDate(modifiedDate);

		objectEntryFolder =
			_objectEntryFolderLocalService.updateObjectEntryFolder(
				objectEntryFolder);

		return objectEntryFolder.getExternalReferenceCode();
	}

	@Override
	protected void addStagedModels() throws Exception {
	}

	@Override
	protected void deleteEntry(String externalReferenceCode, long groupId)
		throws Exception {

		_objectEntryFolderLocalService.deleteObjectEntryFolder(
			_fetchObjectEntryFolder(externalReferenceCode, groupId));
	}

	@Override
	protected Object fetchEntry(String externalReferenceCode, long groupId)
		throws Exception {

		return _fetchObjectEntryFolder(externalReferenceCode, groupId);
	}

	@Override
	protected long getCreatorUserId(String externalReferenceCode, long groupId)
		throws Exception {

		ObjectEntryFolder objectEntryFolder = _fetchObjectEntryFolder(
			externalReferenceCode, groupId);

		return objectEntryFolder.getUserId();
	}

	@Override
	protected Object getEntryValue(String externalReferenceCode, long groupId)
		throws Exception {

		ObjectEntryFolder objectEntryFolder = _fetchObjectEntryFolder(
			externalReferenceCode, groupId);

		return objectEntryFolder.getDescription();
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
			_objectEntryFolderResource;
	}

	@Override
	protected long getPrimaryKey(String externalReferenceCode, long groupId)
		throws Exception {

		ObjectEntryFolder objectEntryFolder = _fetchObjectEntryFolder(
			externalReferenceCode, groupId);

		return objectEntryFolder.getObjectEntryFolderId();
	}

	@Override
	protected int getStatus(String externalReferenceCode, long groupId)
		throws Exception {

		ObjectEntryFolder objectEntryFolder = _fetchObjectEntryFolder(
			externalReferenceCode, groupId);

		return objectEntryFolder.getStatus();
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

		ObjectEntryFolder objectEntryFolder = _fetchObjectEntryFolder(
			externalReferenceCode, groupId);

		_objectEntryFolderLocalService.updateObjectEntryFolder(
			objectEntryFolder.getUserId(),
			objectEntryFolder.getObjectEntryFolderId(),
			objectEntryFolder.getParentObjectEntryFolderId(),
			RandomTestUtil.randomString(), objectEntryFolder.getLabelMap(),
			objectEntryFolder.getName(),
			ServiceContextTestUtil.getServiceContext(
				groupId, objectEntryFolder.getUserId()));
	}

	private ObjectEntryFolder _fetchObjectEntryFolder(
			String externalReferenceCode, long groupId)
		throws Exception {

		return _objectEntryFolderLocalService.
			fetchObjectEntryFolderByExternalReferenceCode(
				externalReferenceCode, groupId, _getCompanyId(groupId));
	}

	private long _getCompanyId(long groupId) throws Exception {
		Group group = _groupLocalService.getGroup(groupId);

		return group.getCompanyId();
	}

	@Inject
	private GroupLocalService _groupLocalService;

	@Inject
	private ObjectEntryFolderLocalService _objectEntryFolderLocalService;

	@Inject(
		filter = "export.import.vulcan.batch.engine.task.item.delegate=true"
	)
	private ObjectEntryFolderResource _objectEntryFolderResource;

}
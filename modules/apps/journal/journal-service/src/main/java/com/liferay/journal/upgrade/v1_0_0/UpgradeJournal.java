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

package com.liferay.journal.upgrade.v1_0_0;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.upgrade.util.UpgradeProcessUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatamapping.DDMStructureManager;
import com.liferay.portlet.dynamicdatamapping.DDMTemplateManager;
import com.liferay.portlet.dynamicdatamapping.StorageEngineManager;
import com.liferay.util.ContentUtil;
import com.liferay.util.xml.XMLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Gergely Mathe
 * @author Eudaldo Alonso
 */
public class UpgradeJournal extends UpgradeBaseJournal {

	protected String addBasicWebContentStructureAndTemplate(
			Connection con, long companyId)
		throws Exception {

		long groupId = getCompanyGroupId(con, companyId);

		String defaultLanguageId = UpgradeProcessUtil.getDefaultLanguageId(
			companyId);

		Locale defaultLocale = LocaleUtil.fromLanguageId(defaultLanguageId);

		List<Element> structureElements = getDDMStructures(defaultLocale);

		Element structureElement = structureElements.get(0);

		String name = structureElement.elementText("name");

		String description = structureElement.elementText("description");

		String localizedName = localize(groupId, name, defaultLanguageId);
		String localizedDescription = localize(
			groupId, description, defaultLanguageId);

		Element structureElementDefinitionElement = structureElement.element(
			"definition");

		String definition = structureElementDefinitionElement.getTextTrim();

		Element structureElementLayoutElement = structureElement.element(
			"layout");

		String layout = structureElementLayoutElement.getTextTrim();

		if (hasDDMStructure(con, groupId, name) > 0) {
			return name;
		}

		String ddmStructureUUID = PortalUUIDUtil.generate();

		long ddmStructureId = addDDMStructure(
			con, ddmStructureUUID, increment(), groupId, companyId, name,
			localizedName, localizedDescription, definition, layout,
			StorageEngineManager.STORAGE_TYPE_DEFAULT);

		String ddmTemplateUUID = PortalUUIDUtil.generate();

		Element templateElement = structureElement.element("template");

		String fileName = templateElement.elementText("file-name");
		boolean cacheable = GetterUtil.getBoolean(
			templateElement.elementText("cacheable"));

		addDDMTemplate(
			con, ddmTemplateUUID, increment(), groupId, companyId,
			ddmStructureId, name, localizedName, localizedDescription,
			getContent(fileName), cacheable);

		long stagingGroupId = getStagingGroupId(con, groupId);

		if (stagingGroupId > 0) {
			ddmStructureId = addDDMStructure(
				con, ddmStructureUUID, increment(), stagingGroupId, companyId,
				name, localizedName, localizedDescription, definition, layout,
				StorageEngineManager.STORAGE_TYPE_DEFAULT);

			addDDMTemplate(
				con, ddmTemplateUUID, increment(), stagingGroupId, companyId,
				ddmStructureId, name, localizedName, localizedDescription,
				getContent(fileName), cacheable);
		}

		return name;
	}

	protected long addDDMStructure(
			Connection con, String uuid, long ddmStructureId, long groupId,
			long companyId, String ddmStructureKey, String localizedName,
			String localizedDescription, String definition, String layout,
			String storageType)
		throws Exception {

		Timestamp now = new Timestamp(System.currentTimeMillis());

		StringBundler sb = new StringBundler(6);

		sb.append("insert into DDMStructure (uuid_, structureId, ");
		sb.append("groupId, companyId, userId, userName, createDate, ");
		sb.append("modifiedDate, parentStructureId, classNameId, ");
		sb.append("structureKey, version, name, description, definition, ");
		sb.append("storageType, type_) values (?, ?, ?, ?, ?, ?, ?, ?, ");
		sb.append("?, ?, ?, ?, ?, ?, ?, ?, ?)");

		try (PreparedStatement ps = con.prepareStatement(sb.toString())) {
			ps.setString(1, uuid);
			ps.setLong(2, ddmStructureId);
			ps.setLong(3, groupId);
			ps.setLong(4, companyId);
			ps.setLong(5, getDefaultUserId(con, companyId));
			ps.setString(6, StringPool.BLANK);
			ps.setTimestamp(7, now);
			ps.setTimestamp(8, now);
			ps.setLong(
				9, DDMStructureManager.STRUCTURE_DEFAULT_PARENT_STRUCTURE_ID);
			ps.setLong(
				10,
				PortalUtil.getClassNameId(
					"com.liferay.portlet.journal.model.JournalArticle"));
			ps.setString(11, ddmStructureKey);
			ps.setString(12, DDMStructureManager.STRUCTURE_VERSION_DEFAULT);
			ps.setString(13, localizedName);
			ps.setString(14, localizedDescription);
			ps.setString(15, definition);
			ps.setString(16, storageType);
			ps.setInt(17, DDMStructureManager.STRUCTURE_TYPE_DEFAULT);

			ps.executeUpdate();

			long ddmStructureVersionId = increment();

			addDDMStructureVersion(
				con, ddmStructureVersionId, groupId, companyId,
				getDefaultUserId(con, companyId), StringPool.BLANK, now,
				ddmStructureId,
				DDMStructureManager.STRUCTURE_DEFAULT_PARENT_STRUCTURE_ID,
				localizedName, localizedDescription, definition, storageType,
				DDMStructureManager.STRUCTURE_TYPE_DEFAULT,
				WorkflowConstants.STATUS_APPROVED,
				getDefaultUserId(con, companyId), StringPool.BLANK, now);

			addDDMStructureLayout(
				con, PortalUUIDUtil.generate(), increment(), groupId, companyId,
				getDefaultUserId(con, companyId), StringPool.BLANK, now, now,
				ddmStructureVersionId, layout);

			Map<String, Long> bitwiseValues = getBitwiseValues(
				con, _CLASS_NAME_DDM_STRUCTURE);

			List<String> actionIds = new ArrayList<>();

			actionIds.add(ActionKeys.VIEW);

			long bitwiseValue = getBitwiseValue(bitwiseValues, actionIds);

			addResourcePermission(
				con, companyId, _CLASS_NAME_DDM_STRUCTURE, ddmStructureId,
				getRoleId(con, companyId, RoleConstants.GUEST), bitwiseValue);
			addResourcePermission(
				con, companyId, _CLASS_NAME_DDM_STRUCTURE, ddmStructureId,
				getRoleId(con, companyId, RoleConstants.SITE_MEMBER),
				bitwiseValue);
		}
		catch (Exception e) {
			_log.error("Unable to create the basic web content structure");

			throw e;
		}

		return ddmStructureId;
	}

	protected void addDDMStructureLayout(
			Connection con, String uuid_, long structureLayoutId, long groupId,
			long companyId, long userId, String userName, Timestamp createDate,
			Timestamp modifiedDate, long structureVersionId, String definition)
		throws Exception {

		StringBundler sb = new StringBundler(5);

		sb.append("insert into DDMStructureLayout (uuid_, ");
		sb.append("structureLayoutId, groupId, companyId, userId, ");
		sb.append("userName, createDate, modifiedDate, ");
		sb.append("structureVersionId, definition) values (?, ?, ?, ?, ");
		sb.append("?, ?, ?, ?, ?, ?)");

		try (PreparedStatement ps = con.prepareStatement(sb.toString())) {
			ps.setString(1, uuid_);
			ps.setLong(2, structureLayoutId);
			ps.setLong(3, groupId);
			ps.setLong(4, companyId);
			ps.setLong(5, userId);
			ps.setString(6, userName);
			ps.setTimestamp(7, createDate);
			ps.setTimestamp(8, modifiedDate);
			ps.setLong(9, structureVersionId);
			ps.setString(10, definition);

			ps.executeUpdate();
		}
		catch (Exception e) {
			_log.error(
				"Unable to upgrade dynamic data mapping structure layout " +
					"with structure version ID " + structureVersionId);

			throw e;
		}
	}

	protected void addDDMStructureVersion(
			Connection con, long structureVersionId, long groupId,
			long companyId, long userId, String userName, Timestamp createDate,
			long structureId, long parentStructureId, String name,
			String description, String definition, String storageType, int type,
			int status, long statusByUserId, String statusByUserName,
			Timestamp statusDate)
		throws Exception {

		StringBundler sb = new StringBundler(6);

		sb.append("insert into DDMStructureVersion (structureVersionId, ");
		sb.append("groupId, companyId, userId, userName, createDate, ");
		sb.append("structureId, version, parentStructureId, name, ");
		sb.append("description, definition, storageType, type_, status, ");
		sb.append("statusByUserId, statusByUserName, statusDate) values ");
		sb.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		try (PreparedStatement ps = con.prepareStatement(sb.toString())) {
			ps.setLong(1, structureVersionId);
			ps.setLong(2, groupId);
			ps.setLong(3, companyId);
			ps.setLong(4, userId);
			ps.setString(5, userName);
			ps.setTimestamp(6, createDate);
			ps.setLong(7, structureId);
			ps.setString(8, DDMStructureManager.STRUCTURE_VERSION_DEFAULT);
			ps.setLong(9, parentStructureId);
			ps.setString(10, name);
			ps.setString(11, description);
			ps.setString(12, definition);
			ps.setString(13, storageType);
			ps.setInt(14, type);
			ps.setInt(15, status);
			ps.setLong(16, statusByUserId);
			ps.setString(17, statusByUserName);
			ps.setTimestamp(18, statusDate);

			ps.executeUpdate();
		}
		catch (Exception e) {
			_log.error(
				"Unable to upgrade dynamic data mapping structure version " +
					"with structure ID " + structureId);

			throw e;
		}
	}

	protected long addDDMTemplate(
			Connection con, String uuid, long ddmTemplateId, long groupId,
			long companyId, long ddmStructureId, String templateKey,
			String localizedName, String localizedDescription, String script,
			boolean cacheable)
		throws Exception {

		Timestamp now = new Timestamp(System.currentTimeMillis());

		StringBundler sb = new StringBundler(7);

		sb.append("insert into DDMTemplate (uuid_, templateId, groupId, ");
		sb.append("companyId, userId, userName, createDate, modifiedDate,");
		sb.append("classNameId, classPK, templateKey, version, name, ");
		sb.append("description, type_, mode_, language, script, ");
		sb.append("cacheable, smallImage, smallImageId, smallImageURL) ");
		sb.append("values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
		sb.append("?, ?, ?, ?, ?, ?, ?)");

		try (PreparedStatement ps = con.prepareStatement(sb.toString())) {
			ps.setString(1, uuid);
			ps.setLong(2, ddmTemplateId);
			ps.setLong(3, groupId);
			ps.setLong(4, companyId);
			ps.setLong(5, getDefaultUserId(con, companyId));
			ps.setString(6, StringPool.BLANK);
			ps.setTimestamp(7, now);
			ps.setTimestamp(8, now);
			ps.setLong(9, PortalUtil.getClassNameId(_CLASS_NAME_DDM_STRUCTURE));
			ps.setLong(10, ddmStructureId);
			ps.setString(11, templateKey);
			ps.setString(12, DDMTemplateManager.TEMPLATE_VERSION_DEFAULT);
			ps.setString(13, localizedName);
			ps.setString(14, localizedDescription);
			ps.setString(15, DDMTemplateManager.TEMPLATE_TYPE_DISPLAY);
			ps.setString(16, DDMTemplateManager.TEMPLATE_MODE_CREATE);
			ps.setString(17, TemplateConstants.LANG_TYPE_FTL);
			ps.setString(18, script);
			ps.setBoolean(19, cacheable);
			ps.setBoolean(20, false);
			ps.setLong(21, 0);
			ps.setString(22, StringPool.BLANK);

			ps.executeUpdate();

			addDDMTemplateVersion(
				con, increment(), groupId, companyId,
				getDefaultUserId(con, companyId), StringPool.BLANK, now,
				PortalUtil.getClassNameId(_CLASS_NAME_DDM_STRUCTURE),
				ddmStructureId, ddmTemplateId, localizedName,
				localizedDescription, TemplateConstants.LANG_TYPE_FTL, script,
				WorkflowConstants.STATUS_APPROVED,
				getDefaultUserId(con, companyId), StringPool.BLANK, now);

			Map<String, Long> bitwiseValues = getBitwiseValues(
				con, _CLASS_NAME_DDM_TEMPLATE);

			List<String> actionIds = new ArrayList<>();

			actionIds.add(ActionKeys.VIEW);

			long bitwiseValue = getBitwiseValue(bitwiseValues, actionIds);

			addResourcePermission(
				con, companyId, _CLASS_NAME_DDM_TEMPLATE, ddmTemplateId,
				getRoleId(con, companyId, RoleConstants.GUEST), bitwiseValue);
			addResourcePermission(
				con, companyId, _CLASS_NAME_DDM_TEMPLATE, ddmTemplateId,
				getRoleId(con, companyId, RoleConstants.SITE_MEMBER),
				bitwiseValue);
		}
		catch (Exception e) {
			_log.error("Unable to create the basic web content template");

			throw e;
		}

		return ddmTemplateId;
	}

	protected void addDDMTemplateLink(
			Connection con, long classNameId, long classPK, long templateId)
		throws Exception {

		String sql =
			"insert into DDMTemplateLink (templateLinkId, classNameId, " +
				"classPK, templateId) values (?, ?, ?, ?)";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, increment());
			ps.setLong(2, classNameId);
			ps.setLong(3, classPK);
			ps.setLong(4, templateId);

			ps.executeUpdate();
		}
		catch (Exception e) {
			_log.error("Unable to create template link for journal article");

			throw e;
		}
	}

	protected void addDDMTemplateLinks(Connection con) throws Exception {
		long classNameId = PortalUtil.getClassNameId(_CLASS_NAME_DDM_STRUCTURE);

		StringBundler sb = new StringBundler(6);

		sb.append("select DDMTemplate.templateId, JournalArticle.id_ ");
		sb.append("from JournalArticle inner join DDMTemplate on (");
		sb.append("DDMTemplate.groupId = JournalArticle.groupId and ");
		sb.append("DDMTemplate.templateKey = ");
		sb.append("JournalArticle.ddmTemplateKey and ");
		sb.append("JournalArticle.classNameId != ?)");

		try (PreparedStatement ps = con.prepareStatement(sb.toString())) {
			ps.setLong(1, classNameId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					long templateId = rs.getLong("templateId");
					long id_ = rs.getLong("id_");

					addDDMTemplateLink(con, classNameId, id_, templateId);
				}
			}
		}
	}

	protected void addDDMTemplateVersion(
			Connection con, long templateVersionId, long groupId,
			long companyId, long userId, String userName, Timestamp createDate,
			long classNameId, long classPK, long templateId, String name,
			String description, String language, String script, int status,
			long statusByUserId, String statusByUserName, Timestamp statusDate)
		throws Exception {

		StringBundler sb = new StringBundler(5);

		sb.append("insert into DDMTemplateVersion (templateVersionId, ");
		sb.append("groupId, companyId, userId, userName, createDate, ");
		sb.append("classNameId, classPK, templateId, version, name, ");
		sb.append("description, language, script, status, ");
		sb.append("statusByUserId, statusByUserName, statusDate) values (");
		sb.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		try (PreparedStatement ps = con.prepareStatement(sb.toString())) {
			ps.setLong(1, templateVersionId);
			ps.setLong(2, groupId);
			ps.setLong(3, companyId);
			ps.setLong(4, userId);
			ps.setString(5, userName);
			ps.setTimestamp(6, createDate);
			ps.setLong(7, classNameId);
			ps.setLong(8, classPK);
			ps.setLong(9, templateId);
			ps.setString(10, DDMStructureManager.STRUCTURE_VERSION_DEFAULT);
			ps.setString(11, name);
			ps.setString(12, description);
			ps.setString(13, language);
			ps.setString(14, script);
			ps.setInt(15, status);
			ps.setLong(16, statusByUserId);
			ps.setString(17, statusByUserName);
			ps.setTimestamp(18, statusDate);

			ps.executeUpdate();
		}
		catch (Exception e) {
			_log.error(
				"Unable to upgrade dynamic data mapping template version " +
					"with template ID " + templateId);

			throw e;
		}
	}

	protected String convertStaticContentToDynamic(String content)
		throws Exception {

		Document document = SAXReaderUtil.read(content);

		Document newDocument = SAXReaderUtil.createDocument();

		Element rootElement = document.getRootElement();

		String availableLocales = rootElement.attributeValue(
			"available-locales");
		String defaultLocale = rootElement.attributeValue("default-locale");

		Element newRootElement = SAXReaderUtil.createElement("root");

		newRootElement.addAttribute("available-locales", availableLocales);
		newRootElement.addAttribute("default-locale", defaultLocale);

		newDocument.add(newRootElement);

		Element dynamicElementElement = SAXReaderUtil.createElement(
			"dynamic-element");

		dynamicElementElement.addAttribute("name", "content");
		dynamicElementElement.addAttribute("type", "text_area");
		dynamicElementElement.addAttribute("index-type", "keyword");
		dynamicElementElement.addAttribute("index", String.valueOf(0));

		newRootElement.add(dynamicElementElement);

		List<Element> staticContentElements = rootElement.elements(
			"static-content");

		for (Element staticContentElement : staticContentElements) {
			String languageId = staticContentElement.attributeValue(
				"language-id");
			String text = staticContentElement.getText();

			Element dynamicContentElement = SAXReaderUtil.createElement(
				"dynamic-content");

			dynamicContentElement.addAttribute("language-id", languageId);
			dynamicContentElement.addCDATA(text);

			dynamicElementElement.add(dynamicContentElement);
		}

		return XMLUtil.formatXML(newDocument);
	}

	@Override
	protected void doUpgrade() throws Exception {
		try (Connection con = DataAccess.getUpgradeOptimizedConnection()) {
			updateBasicWebContentStructure(con);

			addDDMTemplateLinks(con);
		}
	}

	protected String getContent(String fileName) {
		Class<?> clazz = getClass();

		return ContentUtil.get(
			clazz.getClassLoader(),
			"com/liferay/journal/upgrade/v1_0_0/dependencies/" + fileName);
	}

	protected List<Element> getDDMStructures(Locale locale)
		throws DocumentException {

		String xml = getContent("basic-web-content-structure.xml");

		xml = StringUtil.replace(xml, "[$LOCALE_DEFAULT$]", locale.toString());

		Document document = SAXReaderUtil.read(xml);

		Element rootElement = document.getRootElement();

		return rootElement.elements("structure");
	}

	protected long getStagingGroupId(Connection con, long groupId)
		throws Exception {

		String sql = "select groupId from Group_ where liveGroupId = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, groupId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("groupId");
				}
			}

			return 0;
		}
	}

	protected int hasDDMStructure(
			Connection con, long groupId, String ddmStructureKey)
		throws Exception {

		String sql =
			"select count(*) from DDMStructure where groupId = ? and " +
				"classNameId = ? and structureKey = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, groupId);
			ps.setLong(
				2,
				PortalUtil.getClassNameId(
					"com.liferay.portlet.journal.model.JournalArticle"));
			ps.setString(3, ddmStructureKey);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt(1);

					return count;
				}
			}

			return 0;
		}
	}

	protected void updateBasicWebContentStructure(Connection con)
		throws Exception {

		String sql = "select companyId from Company";

		try (PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				long companyId = rs.getLong("companyId");

				updateJournalArticles(con, companyId);
			}
		}
	}

	protected void updateJournalArticle(
			Connection con, long id_, String ddmStructureKey,
			String ddmTemplateKey, String content)
		throws Exception {

		String sql =
			"update JournalArticle set ddmStructureKey = ?, ddmTemplateKey " +
				"= ?, content = ? where id_ = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, ddmStructureKey);
			ps.setString(2, ddmTemplateKey);
			ps.setString(3, convertStaticContentToDynamic(content));
			ps.setLong(4, id_);

			ps.executeUpdate();
		}
	}

	protected void updateJournalArticles(Connection con, long companyId)
		throws Exception {

		StringBundler sb = new StringBundler(3);

		sb.append("select id_, content from JournalArticle where companyId = ");
		sb.append(companyId);
		sb.append(" and ddmStructureKey is null or ddmStructureKey like ''");

		try (PreparedStatement ps = con.prepareStatement(sb.toString())) {
			String name = addBasicWebContentStructureAndTemplate(
				con, companyId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					long id_ = rs.getLong("id_");
					String content = rs.getString("content");

					updateJournalArticle(con, id_, name, name, content);
				}
			}
		}
	}

	private static final String _CLASS_NAME_DDM_STRUCTURE =
		"com.liferay.dynamic.data.mapping.model.DDMStructure";

	private static final String _CLASS_NAME_DDM_TEMPLATE =
		"com.liferay.dynamic.data.mapping.model.DDMTemplate";

	private static final Log _log = LogFactoryUtil.getLog(UpgradeJournal.class);

}
<#setting number_format = "0">

<#assign publicLayoutsSize = publicLayouts?size>

<#list 1..maxJournalArticleCount as journalArticleCount>
	<#assign journalArticleResource = dataFactory.newJournalArticleResource(groupId)>

	insert into JournalArticleResource values ('${portalUUIDUtil.generate()}', ${journalArticleResource.resourcePrimKey}, ${journalArticleResource.groupId}, '${journalArticleResource.articleId}');

	<#assign assetEntry = dataFactory.newAssetEntry(groupId, sampleUserId, dataFactory.journalArticleClassNameId, journalArticleResource.resourcePrimKey, true, "text/html", journalArticleTitle)>

	insert into AssetEntry (entryId, groupId, companyId, userId, createDate, modifiedDate, classNameId, classPK, visible, mimeType, title) values (${counter.get()}, ${assetEntry.groupId}, ${companyId}, ${assetEntry.userId}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ${assetEntry.classNameId}, ${assetEntry.classPK}, <#if assetEntry.visible>TRUE<#else>FALSE</#if>, '${assetEntry.mimeType}', '${assetEntry.title}');

	${sampleSQLBuilder.insertResourcePermission("com.liferay.portlet.journal.model.JournalArticle", stringUtil.valueOf(journalArticleResource.resourcePrimKey))}

	${sampleSQLBuilder.insertMBDiscussion(groupId, sampleUserId, dataFactory.journalArticleClassNameId, journalArticleResource.resourcePrimKey, counter.get(), counter.get(), 0)}

	<#assign layout = dataFactory.newLayout(groupId, groupId + "_journal_article_" + journalArticleCount, "", "56,")>

	${writerLayoutCSV.write(layout.friendlyURL + "\n")}

	<#assign publicLayouts = publicLayouts + [layout]>

	<#assign preferences = "<portlet-preferences><preference><name>articleId</name><value>" + journalArticleResource.articleId + "</value></preference><preference><name>enableCommentRatings</name><value>false</value></preference><preference><name>enableComments</name><value>false</value></preference><preference><name>enablePrint</name><value>false</value></preference><preference><name>enableRatings</name><value>false</value></preference><preference><name>enableRelatedAssets</name><value>true</value></preference><preference><name>enableViewCountIncrement</name><value>false</value></preference><preference><name>extensions</name><value>NULL_VALUE</value></preference><preference><name>groupId</name><value>" + groupId + "</value></preference><preference><name>showAvailableLocales</name><value>false</value></preference><preference><name>templateId</name><value></value></preference></portlet-preferences>">

	<#assign portletPreferences = dataFactory.newPortletPreferences(0, layout.plid, "86", preferences)>

	insert into PortletPreferences values (${portletPreferences.portletPreferencesId}, ${portletPreferences.ownerId}, ${portletPreferences.ownerType}, ${portletPreferences.plid}, '${portletPreferences.portletId}', '${portletPreferences.preferences}');

	<#assign portletPreferences = dataFactory.newPortletPreferences(0, layout.plid, "56", preferences)>

	insert into PortletPreferences values (${portletPreferences.portletPreferencesId}, ${portletPreferences.ownerId}, ${portletPreferences.ownerType}, ${portletPreferences.plid}, '${portletPreferences.portletId}', '${portletPreferences.preferences}');

	${sampleSQLBuilder.insertResourcePermission("86", layout.plid + "_LAYOUT_86")}

	insert into JournalContentSearch values (${counter.get()}, ${groupId}, ${companyId}, 0, ${layout.layoutId}, '56', '${journalArticleResource.articleId}');

	<#list 1..maxJournalArticleVersionCount as journalArticleVersionCount>
		<#assign journalArticle = dataFactory.newJournalArticle(journalArticleResource.resourcePrimKey, groupId, companyId, journalArticleResource.articleId, journalArticleVersionCount)>

		<#assign orginalArticleTitle = "Test Journal Article_" + journalArticleCount + "_" + journalArticleVersionCount >

		<#assign journalArticleTitle = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root available-locales=\"en_US\" default-locale=\"en_US\"><Title language-id=\"en_US\">${orginalArticleTitle}</Title></root>">

		<#assign journalArticleContent = "<?xml version=\"1.0\"?><root available-locales=\"en_US\" default-locale=\"en_US\"><static-content language-id=\"en_US\">&lt;p&gt;" + journalArticle.content + "&lt;/p&gt;</static-content></root>">

		insert into JournalArticle values ('${portalUUIDUtil.generate()}', ${journalArticle.id}, ${journalArticle.resourcePrimKey}, ${journalArticle.groupId}, ${journalArticle.companyId}, ${sampleUserId}, '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0, 0, '${journalArticleResource.articleId}', ${journalArticle.version}, '${journalArticleTitle}', 'test journal article', '', '${journalArticleContent}', 'general', '', '', '', CURRENT_TIMESTAMP, null, null, 1, 0, 0, '', 0, 0, '', CURRENT_TIMESTAMP);

		<#if (journalArticleVersionCount = 1)>
			<#assign activityKey = 1>
		<#else>
			<#assign activityKey = 2>
		</#if>

		<#assign socialActivity = dataFactory.newSocialActivity(groupId, companyId, sampleUserId, dataFactory.journalArticleClassNameId, journalArticle.id)>

		insert into SocialActivity values (${socialActivity.activityId}, ${socialActivity.groupId}, ${socialActivity.companyId}, ${socialActivity.userId}, ${stringUtil.valueOf(dateUtil.newTime())}, 0, 0, ${socialActivity.classNameId}, ${socialActivity.classPK}, ${activityKey}, '{"title":"${orginalArticleTitle}"}', 0);
	</#list>
</#list>
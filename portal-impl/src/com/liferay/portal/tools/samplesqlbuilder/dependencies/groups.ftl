<#setting number_format = "0">

<#assign mbMessageCounter = dataFactory.newInteger()>
<#assign wikiPageCounter = dataFactory.newInteger()>

<#assign privateLayouts = []>

<#list dataFactory.groups as group>
	<#assign publicLayouts = [
		dataFactory.addLayout(1, "Welcome", "/welcome", "58,", "47,")
	]>

	${sampleSQLBuilder.insertGroup(group, privateLayouts, publicLayouts)}
</#list>

<#list 1..maxGroupCount as groupCount>
	<#assign groupId = groupCount>

	<#assign group = dataFactory.addGroup(groupId, dataFactory.groupClassName.classNameId, groupId, "Community " + groupCount, "/community" + groupCount, true)>

	<#assign publicLayouts = [
		dataFactory.addLayout(1, "Welcome", "/welcome", "58,", "47,"),
		dataFactory.addLayout(2, "Blogs", "/blogs", "", "33,")
		dataFactory.addLayout(3, "Document Library", "/document_library", "", "20,")
		dataFactory.addLayout(4, "Forums", "/forums", "", "19,")
		dataFactory.addLayout(5, "Wiki", "/wiki", "", "36,")
	]>

	<#assign dynamicDataListDisplayLayouts = []>

	<#assign dynamicDataListDisplayURLPrefix = "dynamic_data_list_display_">
	<#assign dynamicDataListDisplayNamePrefix = "169_INSTANCE_TEST" >

	<#list 1..maxDDLRecordSetCount as ddlRecordSetCount>
		<#assign dynamicDataListDisplayLayouts = dynamicDataListDisplayLayouts + [dataFactory.addLayout(5 + ddlRecordSetCount, "Dynamic Data List Display " + ddlRecordSetCount, "/" + dynamicDataListDisplayURLPrefix + ddlRecordSetCount, "", dynamicDataListDisplayNamePrefix + ddlRecordSetCount)]>
	</#list>

	<#assign journalArticleLayouts = []>

	<#list 1..maxJournalArticleCount as journalArticleCount>
		<#assign journalArticleLayouts = journalArticleLayouts + [dataFactory.addLayout(5 + maxDDLRecordSetCount + journalArticleCount, "Web Content " + journalArticleCount, "/journal_article_" + journalArticleCount, "", "56,")]>

		${writerLayoutCSV.write("journal_article_" + journalArticleCount + "\n")}
	</#list>

	<#assign publicLayouts = publicLayouts + journalArticleLayouts + dynamicDataListDisplayLayouts>

	${sampleSQLBuilder.insertGroup(group, privateLayouts, publicLayouts)}

	${sampleSQLBuilder.insertJournalArticle(groupId, journalArticleLayouts)}

	<#include "users.ftl">

	${sampleSQLBuilder.insertDDLRecordSet(groupId, firstUserId, dynamicDataListDisplayURLPrefix, dynamicDataListDisplayNamePrefix, dynamicDataListDisplayLayouts)}

	<#include "blogs.ftl">

	<#include "dl.ftl">

	<#include "mb.ftl">

	<#include "wiki.ftl">
</#list>
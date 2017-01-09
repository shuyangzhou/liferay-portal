<#include "init.ftl">

<#if stringUtil.equals(language, "ftl")>
	${r"${"}ddlDisplayTemplateHelper.renderRecordFieldValue(${fieldValueVariable}, ${localeVariable})${r"}"}
<#else>
	$ddlDisplayTemplateHelper.renderRecordFieldValue(${fieldValueVariable}, ${localeVariable})
</#if>
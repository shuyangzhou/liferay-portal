<#include "../init.ftl">

<#if (fieldRawValue?is_date)>
	<#assign fieldDateValue = fieldRawValue>
<#else>
	<#assign fieldDateValue = dateUtil.newDate()>
</#if>

<#assign day = fieldDateValue?string("dd")?number>
<#assign month = fieldDateValue?string("MM")?number - 1>
<#assign year = fieldDateValue?string("yyyy")?number>
<#assign yearEnd = fieldDateValue?string("yyyy")?number - 100>
<#assign yearStart = fieldDateValue?string("yyyy")?number + 100>

<@aui["field-wrapper"] helpMessage=fieldStructure.tip label=label>
	<#if required>
		<@aui.validator name="required" />
	</#if>

	<@liferay_ui["input-date"]
		cssClass=cssClass
		dayParam="${namespacedFieldName}Day"
		dayValue=day
		disabled=false
		helpMessage=fieldStructure.tip
		monthParam="${namespacedFieldName}Month"
		monthValue=month
		yearParam="${namespacedFieldName}Year"
		yearRangeEnd=yearStart
		yearRangeStart=yearEnd
		yearValue=year
	/>

	${fieldStructure.children}
</@>
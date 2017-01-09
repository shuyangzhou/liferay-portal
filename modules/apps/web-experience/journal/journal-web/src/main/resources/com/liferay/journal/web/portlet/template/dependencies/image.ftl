<#include "init.ftl">

<#assign variableAltName = name + ".getAttribute(\"alt\")" />

<#if repeatable>
	<#assign variableAltName = "cur_" + variableAltName />
</#if>

<#if stringUtil.equals(language, "ftl")>
${r"<#if"} validator.isNotNull(${variableName})>
	<img alt="${getVariableReferenceCode(variableAltName)}" src="${getVariableReferenceCode(variableName)}" />
${r"</#if>"}
<#else>
#if ($${variableName} && $${variableName} != "")
	<img alt="${getVariableReferenceCode(variableAltName)}" src="${getVariableReferenceCode(variableName)}" />
#end
</#if>
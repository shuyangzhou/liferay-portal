<#assign macro = macroElement.attributeValue("macro")>

<#assign x = macro?last_index_of("#")>

<#assign void = macroNameStack.push(macro?substring(0, x))>

<#if macroElement.getName() == "execute">
	executeScopeVariables = new HashMap<String, String>();

	executeScopeVariables.putAll(commandScopeVariables);

	<#if macroElement.element("var")??>
		<#assign varElements = macroElement.elements("var")>

		<#assign context = "executeScopeVariables">

		<#list varElements as varElement>
			<#include "var_element.ftl">
		</#list>
	</#if>
</#if>

<#if macroElement.getName() == "execute" && macro?substring(x + 1)?starts_with("is")>
	return
</#if>

${seleniumBuilderFileUtil.getVariableName(macro?substring(0, x))}Macro.${macro?substring(x + 1)}(executeScopeVariables)

<#if macroElement.getName() == "execute">
	;
</#if>

<#assign void = macroNameStack.pop()>
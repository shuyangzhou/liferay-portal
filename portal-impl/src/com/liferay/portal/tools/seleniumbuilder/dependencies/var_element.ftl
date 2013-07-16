<#assign varName = varElement.attributeValue("name")>

<#if varElement.attributeValue("value")??>
	<#assign varValue = varElement.attributeValue("value")>
<#elseif varElement.getText()??>
	<#assign varValue = varElement.getText()>
</#if>

${context}.put("${varName}", RuntimeVariables.evaluateVariable("${varValue}", ${context}));
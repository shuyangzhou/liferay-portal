<#assign entityColumns = finder.entityColumns />

<#list entityColumns as entityColumn>
	<#assign entityColumnName = entityColumn.name finderFieldSuffix = "" />

	<#include "persistence_impl_finder_field.ftl">

	<#if entity.isPermissionCheckEnabled(finder) && !entity.isPermissionedModel() && (entityColumn.name != entityColumn.DBName)>
		<#assign entityColumnName = entityColumn.DBName finderFieldSuffix = finderFieldSQLSuffix />

		<#include "persistence_impl_finder_field.ftl">
	</#if>
</#list>
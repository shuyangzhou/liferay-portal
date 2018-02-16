<#if !entityColumn.isPrimitiveType()>
	boolean bind${entityColumn.methodName} = false;

	if (${entityColumn.name} == null) {
		query.append(_FINDER_COLUMN_${finder.name?upper_case}_${entityColumn.name?upper_case}_1${finderFieldSuffix});
	}
	<#if stringUtil.equals(entityColumn.type, "String")>
		else if (${entityColumn.name}.equals("")) {
			query.append(_FINDER_COLUMN_${finder.name?upper_case}_${entityColumn.name?upper_case}_3${finderFieldSuffix});
		}
	</#if>
	else {
		bind${entityColumn.methodName} = true;
</#if>

query.append(_FINDER_COLUMN_${finder.name?upper_case}_${entityColumn.name?upper_case}_2${finderFieldSuffix});

<#if !entityColumn.isPrimitiveType()>
	}
</#if>
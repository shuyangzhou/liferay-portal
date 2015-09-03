<#if !finderCol.isPrimitiveType()>
	boolean bind${finderCol.methodName} = false;

	if (${finderCol.name} == null) {

	<#if finderCol.comparator == "LIKE">
		<#if returnType == "int">
			return 0;
		<#elseif returnType == "list">
			return Collections.emptyList();
		<#elseif returnType == "array">
			return new ${entity.name}[0];
		<#elseif returnType == "entity">
			return null;
		</#if>
	<#else>
		query.append(_FINDER_COLUMN_${finder.name?upper_case}_${finderCol.name?upper_case}_1${finderFieldSuffix});
	</#if>
	}
	<#if finderCol.type == "String">
		else if (${finderCol.name}.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_${finder.name?upper_case}_${finderCol.name?upper_case}_3${finderFieldSuffix});
		}
	</#if>
	else {
		bind${finderCol.methodName} = true;
</#if>

query.append(_FINDER_COLUMN_${finder.name?upper_case}_${finderCol.name?upper_case}_2${finderFieldSuffix});

<#if !finderCol.isPrimitiveType()>
	}
</#if>
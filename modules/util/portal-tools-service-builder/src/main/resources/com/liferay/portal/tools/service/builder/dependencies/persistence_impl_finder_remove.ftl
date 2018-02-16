<#assign entityColumns = finder.entityColumns />

<#-- Case 3: finder.isCollection() && !finder.isUnique() -->

<#if finder.isCollection() && !finder.isUnique()>
	/**
	 * Removes all the ${entity.humanNames} where ${finder.getHumanConditions(false)} from the database.
	 *
	<#list entityColumns as entityColumn>
	 * @param ${entityColumn.name} the ${entityColumn.humanName}
	</#list>
	 */
	@Override
	public void removeBy${finder.name}(

	<#list entityColumns as entityColumn>
		${entityColumn.type} ${entityColumn.name}<#if entityColumn_has_next>,</#if>
	</#list>

	) {
		for (${entity.name} ${entity.varName} : findBy${finder.name}(

		<#list entityColumns as entityColumn>
			${entityColumn.name},
		</#list>

		QueryUtil.ALL_POS, QueryUtil.ALL_POS, null
		)) {
			remove(${entity.varName});
		}
	}
<#else>

<#-- Case 9: !finder.isCollection() || finder.isUnique() -->

	/**
	 * Removes the ${entity.humanName} where ${finder.getHumanConditions(false)} from the database.
	 *
	<#list entityColumns as entityColumn>
	 * @param ${entityColumn.name} the ${entityColumn.humanName}
	</#list>
	 * @return the ${entity.humanName} that was removed
	 */
	@Override
	public ${entity.name} removeBy${finder.name}(

	<#list entityColumns as entityColumn>
		${entityColumn.type} ${entityColumn.name}

		<#if entityColumn_has_next>
			,
		</#if>
	</#list>

	) throws ${noSuchEntity}Exception {
		${entity.name} ${entity.varName} = findBy${finder.name}(

		<#list entityColumns as entityColumn>
			${entityColumn.name}

			<#if entityColumn_has_next>
				,
			</#if>
		</#list>

		);

		return remove(${entity.varName});
	}
</#if>
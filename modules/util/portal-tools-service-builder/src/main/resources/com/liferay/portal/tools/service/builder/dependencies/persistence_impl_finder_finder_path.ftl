<#assign entityColumns = finder.entityColumns />

<#if finder.isCollection()>
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_${finder.name?upper_case} = new FinderPath(
		${entity.name}ModelImpl.ENTITY_CACHE_ENABLED,
		${entity.name}ModelImpl.FINDER_CACHE_ENABLED,
		${entity.name}Impl.class,
		FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
		"findBy${finder.name}",
		new String[] {
			<#list entityColumns as entityColumn>
				${serviceBuilder.getPrimitiveObj("${entityColumn.type}")}.class.getName(),
			</#list>

			Integer.class.getName(), Integer.class.getName(), OrderByComparator.class.getName()
		});

	<#if !finder.hasCustomComparator()>
		public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_${finder.name?upper_case} = new FinderPath(
			${entity.name}ModelImpl.ENTITY_CACHE_ENABLED,
			${entity.name}ModelImpl.FINDER_CACHE_ENABLED,
			${entity.name}Impl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findBy${finder.name}",
			new String[] {
				<#list entityColumns as entityColumn>
					${serviceBuilder.getPrimitiveObj("${entityColumn.type}")}.class.getName()

					<#if entityColumn_has_next>
						,
					</#if>
				</#list>
			}

			<#if columnBitmaskEnabled>
				,

				<#list entityColumns as entityColumn>
					${entity.name}ModelImpl.${entityColumn.name?upper_case}_COLUMN_BITMASK

					<#if entityColumn_has_next>
						|
					</#if>
				</#list>

				<#if entity.entityOrder??>
					<#list entity.entityOrder.entityColumns as entityColumn>
						<#assign pkList = entity.getPKList() />

						<#if !entityColumns?seq_contains(entityColumn) && !pkList?seq_contains(entityColumn)>
							| ${entity.name}ModelImpl.${entityColumn.name?upper_case}_COLUMN_BITMASK
						</#if>
					</#list>
				</#if>
			</#if>

			);
	</#if>
</#if>

<#if !finder.isCollection() || finder.isUnique()>
	public static final FinderPath FINDER_PATH_FETCH_BY_${finder.name?upper_case} = new FinderPath(
		${entity.name}ModelImpl.ENTITY_CACHE_ENABLED,
		${entity.name}ModelImpl.FINDER_CACHE_ENABLED,
		${entity.name}Impl.class,
		FINDER_CLASS_NAME_ENTITY,
		"fetchBy${finder.name}",
		new String[] {
			<#list entityColumns as entityColumn>
				${serviceBuilder.getPrimitiveObj("${entityColumn.type}")}.class.getName()

				<#if entityColumn_has_next>
					,
				</#if>
			</#list>
		}

		<#if columnBitmaskEnabled>
			,

			<#list entityColumns as entityColumn>
				${entity.name}ModelImpl.${entityColumn.name?upper_case}_COLUMN_BITMASK

				<#if entityColumn_has_next>
					|
				</#if>
			</#list>
		</#if>

		);
</#if>

<#if !finder.hasCustomComparator()>
	public static final FinderPath FINDER_PATH_COUNT_BY_${finder.name?upper_case} = new FinderPath(
		${entity.name}ModelImpl.ENTITY_CACHE_ENABLED,
		${entity.name}ModelImpl.FINDER_CACHE_ENABLED,
		Long.class,
		FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
		"countBy${finder.name}",
		new String[] {
			<#list entityColumns as entityColumn>
				${serviceBuilder.getPrimitiveObj("${entityColumn.type}")}.class.getName()

				<#if entityColumn_has_next>
					,
				</#if>
			</#list>
		});
</#if>

<#if finder.hasArrayableOperator() || finder.hasCustomComparator()>
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_${finder.name?upper_case} = new FinderPath(
		${entity.name}ModelImpl.ENTITY_CACHE_ENABLED,
		${entity.name}ModelImpl.FINDER_CACHE_ENABLED,
		Long.class,
		FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
		"countBy${finder.name}",
		new String[] {
			<#list entityColumns as entityColumn>
				${serviceBuilder.getPrimitiveObj("${entityColumn.type}")}.class.getName()

				<#if entityColumn_has_next>
					,
				</#if>
			</#list>
		});
</#if>
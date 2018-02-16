StringBundler query = new StringBundler(${entityColumns?size + 1});

query.append(_SQL_COUNT_${entity.alias?upper_case}_WHERE);

<#include "persistence_impl_finder_cols.ftl">
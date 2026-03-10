# Migration: `findByXxx_PrevAndNext` and `filterFindByXxx_PrevAndNext` Removed

## What Changed

All `findByXxx_PrevAndNext` and `filterFindByXxx_PrevAndNext` methods have been removed from `*Persistence`, `*PersistenceImpl`, and `*Util` classes for entities using Service Builder 7.4+.

These methods returned a 3-element array `[previous, current, next]` representing the neighboring entities in an ordered finder result set.

## Why

PrevAndNext methods accounted for ~595,000 lines of generated code across the codebase while only being used in 3 internal call sites. The same behavior is achievable with existing finder methods.

## How to Migrate

### Approach 1: List Lookup (Recommended)

Replace `findByXxx_PrevAndNext(primaryKey, col1, col2, orderByComparator)` with a list lookup using the corresponding `findByXxx` method:

**Before:**

```java
MyEntity[] array = myEntityPersistence.findByX_Y_PrevAndNext(
    entityId, x, y, orderByComparator);

MyEntity previous = array[0];
MyEntity current = array[1];
MyEntity next = array[2];
```

**After:**

```java
MyEntity current = myEntityPersistence.findByPrimaryKey(entityId);

List<MyEntity> list = myEntityPersistence.findByX_Y(
    x, y, QueryUtil.ALL_POS, QueryUtil.ALL_POS, orderByComparator);

int index = list.indexOf(current);

MyEntity previous = (index > 0) ? list.get(index - 1) : null;
MyEntity next = (index < (list.size() - 1)) ? list.get(index + 1) : null;
```

The same pattern applies to `filterFindByXxx_PrevAndNext` — replace with the corresponding `filterFindByXxx` method.

**Note:** The original `findByXxx_PrevAndNext` threw `NoSuchEntityException` when the primary key was not found. The replacement `findByPrimaryKey` preserves this behavior.

### Approach 2: DSL Queries (Large Data Sets Only)

When the finder matches a large number of rows, loading the entire list into memory is impractical. In this case, use DSL queries to fetch only the previous and next entities directly from the database:

```java
MyEntity current = myEntityPersistence.findByPrimaryKey(entityId);

// Previous: entries ordered before current
List<MyEntity> previousList = myEntityPersistence.dslQuery(
    DSLQueryFactoryUtil.select(
        MyEntityTable.INSTANCE
    ).from(
        MyEntityTable.INSTANCE
    ).where(
        MyEntityTable.INSTANCE.x.eq(x)
        .and(MyEntityTable.INSTANCE.y.eq(y))
        .and(
            MyEntityTable.INSTANCE.orderCol.lt(current.getOrderCol())
            .or(
                MyEntityTable.INSTANCE.orderCol.eq(current.getOrderCol())
                .and(MyEntityTable.INSTANCE.entityId.lt(entityId))))
    ).orderBy(
        MyEntityTable.INSTANCE.orderCol.descending(),
        MyEntityTable.INSTANCE.entityId.descending()
    ).limit(0, 1));

MyEntity previous = previousList.isEmpty() ? null : previousList.get(0);

// Next: entries ordered after current
List<MyEntity> nextList = myEntityPersistence.dslQuery(
    DSLQueryFactoryUtil.select(
        MyEntityTable.INSTANCE
    ).from(
        MyEntityTable.INSTANCE
    ).where(
        MyEntityTable.INSTANCE.x.eq(x)
        .and(MyEntityTable.INSTANCE.y.eq(y))
        .and(
            MyEntityTable.INSTANCE.orderCol.gt(current.getOrderCol())
            .or(
                MyEntityTable.INSTANCE.orderCol.eq(current.getOrderCol())
                .and(MyEntityTable.INSTANCE.entityId.gt(entityId))))
    ).orderBy(
        MyEntityTable.INSTANCE.orderCol.ascending(),
        MyEntityTable.INSTANCE.entityId.ascending()
    ).limit(0, 1));

MyEntity next = nextList.isEmpty() ? null : nextList.get(0);
```

This approach executes two targeted queries that each return at most one row, regardless of how many rows match the finder conditions. The trade-off is that DSL requires compile-time knowledge of the ordering columns — it cannot accept an arbitrary `OrderByComparator` at runtime. Use this approach only when the matching result set is large enough that loading it entirely into memory is a concern.
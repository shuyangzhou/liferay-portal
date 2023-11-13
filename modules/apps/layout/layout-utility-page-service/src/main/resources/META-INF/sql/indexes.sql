create unique index IX_6D96DD70 on LayoutUtilityPageEntry (ctCollectionId, plid);
create index IX_240EF756 on LayoutUtilityPageEntry (externalReferenceCode[$COLUMN_LENGTH:75$]);
create unique index IX_D08C3F1 on LayoutUtilityPageEntry (groupId, type_[$COLUMN_LENGTH:75$], ctCollectionId, name[$COLUMN_LENGTH:75$]);
create index IX_3C8527A6 on LayoutUtilityPageEntry (groupId, type_[$COLUMN_LENGTH:75$], defaultLayoutUtilityPageEntry);
create index IX_997885CD on LayoutUtilityPageEntry (uuid_[$COLUMN_LENGTH:75$]);
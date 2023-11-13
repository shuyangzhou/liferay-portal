create index IX_67813F8 on AssetListEntry (groupId, assetEntryType[$COLUMN_LENGTH:255$], assetEntrySubtype[$COLUMN_LENGTH:255$]);
create unique index IX_366FAE09 on AssetListEntry (groupId, ctCollectionId, assetListEntryKey[$COLUMN_LENGTH:75$]);
create index IX_6E4BA730 on AssetListEntry (groupId, title[$COLUMN_LENGTH:75$], assetEntryType[$COLUMN_LENGTH:255$], assetEntrySubtype[$COLUMN_LENGTH:255$]);
create unique index IX_5B95A9C6 on AssetListEntry (groupId, title[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_4FE08A35 on AssetListEntry (groupId, type_);
create index IX_5B11862A on AssetListEntry (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_FAAE938C on AssetListEntryAssetEntryRel (assetListEntryId, segmentsEntryId, position, ctCollectionId);
create index IX_EA6A8DDB on AssetListEntryAssetEntryRel (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_56302677 on AssetListEntrySegmentsEntryRel (segmentsEntryId, assetListEntryId, ctCollectionId);
create index IX_770BF63 on AssetListEntrySegmentsEntryRel (uuid_[$COLUMN_LENGTH:75$]);

create index IX_BBBAB3D on AssetListEntryUsage (classNameId, key_[$COLUMN_LENGTH:255$], companyId);
create index IX_10BA153A on AssetListEntryUsage (classNameId, key_[$COLUMN_LENGTH:255$], groupId, type_);
create unique index IX_624112AF on AssetListEntryUsage (classNameId, key_[$COLUMN_LENGTH:255$], plid, groupId, containerType, containerKey[$COLUMN_LENGTH:255$], ctCollectionId);
create index IX_8BEB5021 on AssetListEntryUsage (plid, containerType, containerKey[$COLUMN_LENGTH:255$]);
create index IX_561E0151 on AssetListEntryUsage (uuid_[$COLUMN_LENGTH:75$]);
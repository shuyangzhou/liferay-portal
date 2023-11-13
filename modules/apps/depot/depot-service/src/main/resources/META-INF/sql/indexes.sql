create index IX_5B76D798 on DepotAppCustomization (depotEntryId, enabled);
create unique index IX_DA8D9ACC on DepotAppCustomization (depotEntryId, portletId[$COLUMN_LENGTH:75$]);

create unique index IX_884D6226 on DepotEntry (groupId);
create index IX_5B814630 on DepotEntry (uuid_[$COLUMN_LENGTH:75$]);

create index IX_146497CB on DepotEntryGroupRel (depotEntryId);
create index IX_F329B161 on DepotEntryGroupRel (toGroupId, ddmStructuresAvailable);
create unique index IX_65D34444 on DepotEntryGroupRel (toGroupId, depotEntryId);
create index IX_C61C803B on DepotEntryGroupRel (toGroupId, searchable);
create index IX_2B10CBB6 on DepotEntryGroupRel (uuid_[$COLUMN_LENGTH:75$]);
create unique index IX_6ECDD86B on StyleBookEntry (ctCollectionId, headId);
create index IX_957FE3BD on StyleBookEntry (groupId, defaultStyleBookEntry);
create index IX_C95383D1 on StyleBookEntry (groupId, head, defaultStyleBookEntry);
create index IX_77DDEC7F on StyleBookEntry (groupId, head, name[$COLUMN_LENGTH:75$]);
create unique index IX_4B23F11D on StyleBookEntry (groupId, head, styleBookEntryKey[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_F379E6EB on StyleBookEntry (groupId, name[$COLUMN_LENGTH:75$]);
create index IX_9A76A32B on StyleBookEntry (groupId, styleBookEntryKey[$COLUMN_LENGTH:75$]);
create index IX_346515B6 on StyleBookEntry (uuid_[$COLUMN_LENGTH:75$]);

create index IX_E9CC1685 on StyleBookEntryVersion (groupId, defaultStyleBookEntry);
create index IX_B5D7AB23 on StyleBookEntryVersion (groupId, name[$COLUMN_LENGTH:75$]);
create index IX_8E1B79F3 on StyleBookEntryVersion (groupId, styleBookEntryKey[$COLUMN_LENGTH:75$]);
create index IX_3CA4E523 on StyleBookEntryVersion (groupId, version, defaultStyleBookEntry);
create index IX_7B3A245 on StyleBookEntryVersion (groupId, version, name[$COLUMN_LENGTH:75$]);
create unique index IX_5DF6C9D3 on StyleBookEntryVersion (groupId, version, styleBookEntryKey[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_6C02234D on StyleBookEntryVersion (styleBookEntryId);
create index IX_930691EE on StyleBookEntryVersion (uuid_[$COLUMN_LENGTH:75$]);
create unique index IX_ED1779B9 on StyleBookEntryVersion (version, styleBookEntryId, ctCollectionId);
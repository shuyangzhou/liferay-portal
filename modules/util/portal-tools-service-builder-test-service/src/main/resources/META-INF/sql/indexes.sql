create index IX_838D8DFC on BigDecimalEntries_LVEntries (companyId);
create index IX_67100507 on BigDecimalEntries_LVEntries (lvEntryId);

create index IX_867C5A9 on BigDecimalEntry (bigDecimalValue);

create unique index IX_1CF99E19 on CacheDisabledEntry (name[$COLUMN_LENGTH:75$]);

create index IX_4F11FECA on CacheFieldEntry (groupId);

create index IX_EA6A42CC on ERCCompanyEntry (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_84557D43 on ERCCompanyEntry (uuid_[$COLUMN_LENGTH:75$]);

create index IX_10A81FCE on ERCGroupEntry (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_3BA19E45 on ERCGroupEntry (uuid_[$COLUMN_LENGTH:75$]);

create index IX_48865B31 on EagerBlobEntry (uuid_[$COLUMN_LENGTH:75$]);

create index IX_420C1E47 on FinderWhereClauseEntry (name[$COLUMN_LENGTH:75$]);

create unique index IX_70D6DE35 on LVEntry (groupId, head, uniqueGroupKey[$COLUMN_LENGTH:75$]);
create unique index IX_50CAD09D on LVEntry (headId);
create index IX_83B82F26 on LVEntry (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_FC1C4C16 on LVEntryLocalization (headId);
create unique index IX_5233ABD3 on LVEntryLocalization (lvEntryId, languageId[$COLUMN_LENGTH:75$]);

create index IX_8EAC6E7D on LVEntryLocalizationVersion (lvEntryId, languageId[$COLUMN_LENGTH:75$]);
create unique index IX_B05C042B on LVEntryLocalizationVersion (lvEntryId, version, languageId[$COLUMN_LENGTH:75$]);
create index IX_142D1FEF on LVEntryLocalizationVersion (lvEntryLocalizationId);
create unique index IX_EAC6D2F9 on LVEntryLocalizationVersion (version, lvEntryLocalizationId);

create index IX_1A357E79 on LVEntryVersion (groupId, uniqueGroupKey[$COLUMN_LENGTH:75$]);
create unique index IX_D4DF2FAF on LVEntryVersion (groupId, version, uniqueGroupKey[$COLUMN_LENGTH:75$]);
create index IX_1287D6FD on LVEntryVersion (lvEntryId);
create index IX_6038FA7E on LVEntryVersion (uuid_[$COLUMN_LENGTH:75$]);
create unique index IX_4D8E2BAB on LVEntryVersion (version, lvEntryId);

create index IX_94893EAD on LazyBlobEntry (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_2E833843 on LocalizedEntryLocalization (localizedEntryId, languageId[$COLUMN_LENGTH:75$]);

create unique index IX_46C721B9 on NullConvertibleEntry (name[$COLUMN_LENGTH:75$]);

create unique index IX_32712A54 on RedundantIndexEntry (companyId, name[$COLUMN_LENGTH:75$]);

create index IX_DA817981 on RenameFinderColumnEntry (columnToRename[$COLUMN_LENGTH:75$]);

create index IX_6770C47D on VersionedEntry (groupId, head);
create unique index IX_AAA6F330 on VersionedEntry (headId);

create index IX_D2594361 on VersionedEntryVersion (version, groupId);
create unique index IX_B51BCCBB on VersionedEntryVersion (version, versionedEntryId);
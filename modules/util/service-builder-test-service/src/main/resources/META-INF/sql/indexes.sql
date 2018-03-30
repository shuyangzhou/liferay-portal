create index IX_867C5A9 on BigDecimalEntry (bigDecimalValue);

create unique index IX_AAA6F330 on VersionedEntry (headId);

create unique index IX_B51BCCBB on VersionedEntryVersion (versionedEntryId, version);
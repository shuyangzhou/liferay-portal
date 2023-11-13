create index IX_E0154022 on COREntry (companyId, type_[$COLUMN_LENGTH:75$], active_);
create index IX_134EA18B on COREntry (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_44FA9674 on COREntry (status, displayDate);
create index IX_9CB08889 on COREntry (status, expirationDate);
create index IX_DD753A02 on COREntry (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_25C71E83 on COREntryRel (COREntryId, classNameId, classPK);
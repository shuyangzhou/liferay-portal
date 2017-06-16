create index IX_EFF82A87 on FriendlyURLEntry (groupId, companyId, classNameId, classPK);
create index IX_20861768 on FriendlyURLEntry (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_63FD57EA on FriendlyURLEntry (uuid_[$COLUMN_LENGTH:75$], groupId);

create unique index IX_68BE94B1 on FriendlyURLEntryLocalization (friendlyURLEntryId, languageId[$COLUMN_LENGTH:75$]);
create unique index IX_80705FA0 on FriendlyURLEntryLocalization (groupId, companyId, classNameId, urlTitle[$COLUMN_LENGTH:255$]);
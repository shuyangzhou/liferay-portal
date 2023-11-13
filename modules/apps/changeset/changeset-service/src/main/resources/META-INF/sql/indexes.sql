create unique index IX_ABEEE793 on ChangesetCollection (groupId, name[$COLUMN_LENGTH:75$]);
create index IX_EE4B4B0E on ChangesetCollection (groupId, userId);
create index IX_9AC55E11 on ChangesetCollection (name[$COLUMN_LENGTH:75$], companyId);

create unique index IX_EF48912A on ChangesetEntry (classNameId, changesetCollectionId, classPK);
create index IX_4A5B2D2A on ChangesetEntry (classNameId, groupId);
create index IX_CEB6AFA2 on ChangesetEntry (companyId);
create index IX_E00AB6A4 on ChangesetEntry (groupId);
create unique index IX_FF899B2F on SiteFriendlyURL (companyId, friendlyURL[$COLUMN_LENGTH:75$]);
create unique index IX_935A03E8 on SiteFriendlyURL (companyId, groupId, languageId[$COLUMN_LENGTH:75$]);
create index IX_FE4548F1 on SiteFriendlyURL (uuid_[$COLUMN_LENGTH:75$]);
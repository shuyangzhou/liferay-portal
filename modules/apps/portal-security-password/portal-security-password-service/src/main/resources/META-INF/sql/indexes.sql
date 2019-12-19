create index IX_B144143D on HashAlgorithmEntry (uuid_[$COLUMN_LENGTH:75$]);

create index IX_86A4C3B7 on PasswordEntry (userId);
create index IX_83D2DA97 on PasswordEntry (uuid_[$COLUMN_LENGTH:75$]);

create index IX_98DE3A86 on PasswordMeta (hashAlgorithmEntryId);
create index IX_717D8C5C on PasswordMeta (passwordEntryId);
create index IX_7D4673CE on PasswordMeta (uuid_[$COLUMN_LENGTH:75$]);
create index IX_7E9C8FF8 on KBArticle (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_4A49CDD6 on KBArticle (groupId, kbFolderId, urlTitle[$COLUMN_LENGTH:75$]);
create index IX_2B11F674 on KBArticle (groupId, latest, kbFolderId);
create index IX_B0FCBB47 on KBArticle (groupId, latest, parentResourcePrimKey);
create index IX_97C62252 on KBArticle (groupId, main);
create index IX_D91D2879 on KBArticle (groupId, parentResourcePrimKey, main);
create index IX_5FEF5F4F on KBArticle (groupId, resourcePrimKey, latest);
create index IX_8EF92E81 on KBArticle (groupId, resourcePrimKey, main);
create index IX_379FD6BC on KBArticle (groupId, status, kbFolderId, urlTitle[$COLUMN_LENGTH:75$]);
create index IX_994AC32D on KBArticle (groupId, status, latest, parentResourcePrimKey);
create index IX_55A38CF2 on KBArticle (groupId, status, parentResourcePrimKey);
create index IX_49630FA on KBArticle (groupId, status, resourcePrimKey);
create index IX_571C019E on KBArticle (latest, companyId);
create index IX_86BA3247 on KBArticle (latest, parentResourcePrimKey);
create index IX_5A381890 on KBArticle (main, companyId);
create index IX_1DCC5F79 on KBArticle (parentResourcePrimKey, main);
create index IX_A9E2C691 on KBArticle (resourcePrimKey, latest);
create index IX_69C17E43 on KBArticle (resourcePrimKey, main);
create unique index IX_E7D1F9D0 on KBArticle (resourcePrimKey, version, ctCollectionId);
create index IX_FBC2D349 on KBArticle (status, companyId);
create index IX_65DB3C21 on KBArticle (status, displayDate);
create index IX_2B6103F2 on KBArticle (status, parentResourcePrimKey);
create index IX_4E89983C on KBArticle (status, resourcePrimKey);
create index IX_C23FA26F on KBArticle (uuid_[$COLUMN_LENGTH:75$]);

create index IX_47D3AE89 on KBComment (classNameId, classPK, status);
create index IX_FD56A55D on KBComment (classNameId, classPK, userId);
create index IX_E8D43932 on KBComment (classNameId, groupId);
create index IX_828BA082 on KBComment (groupId, status);
create index IX_8E470726 on KBComment (uuid_[$COLUMN_LENGTH:75$]);

create index IX_F32A081D on KBFolder (companyId);
create index IX_E344A9B2 on KBFolder (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_3FA4415C on KBFolder (groupId, parentKBFolderId, name[$COLUMN_LENGTH:75$]);
create index IX_729A89FA on KBFolder (groupId, parentKBFolderId, urlTitle[$COLUMN_LENGTH:75$]);
create index IX_30B67029 on KBFolder (uuid_[$COLUMN_LENGTH:75$]);

create index IX_83D9CC13 on KBTemplate (groupId);
create index IX_9909475D on KBTemplate (uuid_[$COLUMN_LENGTH:75$]);
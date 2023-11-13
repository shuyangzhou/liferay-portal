create index IX_1F90CA2D on BookmarksEntry (companyId);
create index IX_5200100C on BookmarksEntry (groupId, folderId);
create index IX_C78B61AC on BookmarksEntry (groupId, status, folderId, userId);
create index IX_9D9CF70F on BookmarksEntry (groupId, status, userId);
create index IX_276C8C13 on BookmarksEntry (status, companyId);
create index IX_B670BA39 on BookmarksEntry (uuid_[$COLUMN_LENGTH:75$]);

create index IX_2ABA25D7 on BookmarksFolder (companyId);
create index IX_C27C9DBD on BookmarksFolder (status, companyId);
create index IX_D16018A6 on BookmarksFolder (status, groupId, parentFolderId);
create index IX_451E7AE3 on BookmarksFolder (uuid_[$COLUMN_LENGTH:75$]);
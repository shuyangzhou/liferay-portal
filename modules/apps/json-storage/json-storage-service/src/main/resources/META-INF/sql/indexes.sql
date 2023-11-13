create index IX_FA99D9F4 on JSONStorageEntry (classNameId, companyId, index_, type_, valueLong);
create index IX_C2EBA5C7 on JSONStorageEntry (classNameId, companyId, key_[$COLUMN_LENGTH:255$], type_, valueLong);
create unique index IX_7412B525 on JSONStorageEntry (classNameId, index_, key_[$COLUMN_LENGTH:255$], classPK, parentJSONStorageEntryId, ctCollectionId);
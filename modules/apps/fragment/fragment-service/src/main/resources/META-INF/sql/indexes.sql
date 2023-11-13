create unique index IX_7FA4CEC9 on FragmentCollection (groupId, fragmentCollectionKey[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_536510F5 on FragmentCollection (groupId, name[$COLUMN_LENGTH:75$]);
create index IX_8FB7E9C0 on FragmentCollection (uuid_[$COLUMN_LENGTH:75$]);

create index IX_5C61E2DD on FragmentComposition (fragmentCollectionId);
create index IX_11001AAC on FragmentComposition (groupId, fragmentCollectionId, status, name[$COLUMN_LENGTH:75$]);
create unique index IX_3F7591A1 on FragmentComposition (groupId, fragmentCompositionKey[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_70029354 on FragmentComposition (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_A7F77355 on FragmentEntry (ctCollectionId, headId);
create index IX_ADC3EFB9 on FragmentEntry (fragmentCollectionId, head);
create index IX_63A4C952 on FragmentEntry (groupId, fragmentCollectionId, head, name[$COLUMN_LENGTH:75$]);
create index IX_D8AD65B8 on FragmentEntry (groupId, fragmentCollectionId, head, status, name[$COLUMN_LENGTH:75$]);
create index IX_E0C3B930 on FragmentEntry (groupId, fragmentCollectionId, head, type_, status);
create index IX_18F9DFE on FragmentEntry (groupId, fragmentCollectionId, name[$COLUMN_LENGTH:75$]);
create index IX_9EC6FEE4 on FragmentEntry (groupId, fragmentCollectionId, status, name[$COLUMN_LENGTH:75$]);
create index IX_BD1F4C5C on FragmentEntry (groupId, fragmentCollectionId, type_, status);
create index IX_7F3F0EB3 on FragmentEntry (groupId, fragmentEntryKey[$COLUMN_LENGTH:75$]);
create unique index IX_F5386A5 on FragmentEntry (groupId, head, fragmentEntryKey[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_B6178DC1 on FragmentEntry (head, type_);
create index IX_40CE21AD on FragmentEntry (type_);
create index IX_6E7DE18C on FragmentEntry (uuid_[$COLUMN_LENGTH:75$]);

create index IX_67FF823E on FragmentEntryLink (fragmentEntryId, deleted);
create index IX_2FB5437D on FragmentEntryLink (groupId, classNameId, classPK);
create index IX_4A9E751A on FragmentEntryLink (groupId, fragmentEntryId, classNameId, classPK);
create index IX_3D731EF6 on FragmentEntryLink (groupId, plid, deleted);
create index IX_1E535B10 on FragmentEntryLink (groupId, plid, fragmentEntryId);
create index IX_EB818819 on FragmentEntryLink (groupId, plid, originalFragmentEntryLinkId);
create index IX_CFB8093D on FragmentEntryLink (groupId, plid, segmentsExperienceId, deleted);
create index IX_3F876600 on FragmentEntryLink (groupId, plid, segmentsExperienceId, rendererKey[$COLUMN_LENGTH:200$]);
create index IX_EB688B56 on FragmentEntryLink (groupId, segmentsExperienceId, classNameId, classPK);
create index IX_B8E39A66 on FragmentEntryLink (rendererKey[$COLUMN_LENGTH:200$], companyId);
create index IX_17C15BB2 on FragmentEntryLink (uuid_[$COLUMN_LENGTH:75$]);

create index IX_7A6F05CF on FragmentEntryVersion (fragmentCollectionId, version);
create index IX_391FD151 on FragmentEntryVersion (fragmentEntryId);
create index IX_2509F8CA on FragmentEntryVersion (groupId, fragmentCollectionId, name[$COLUMN_LENGTH:75$]);
create index IX_DDCFCDB0 on FragmentEntryVersion (groupId, fragmentCollectionId, status, name[$COLUMN_LENGTH:75$]);
create index IX_5F305710 on FragmentEntryVersion (groupId, fragmentCollectionId, type_, status);
create index IX_3B0C07E on FragmentEntryVersion (groupId, fragmentCollectionId, version, name[$COLUMN_LENGTH:75$]);
create index IX_A4350E58 on FragmentEntryVersion (groupId, fragmentCollectionId, version, status, name[$COLUMN_LENGTH:75$]);
create index IX_97E910F8 on FragmentEntryVersion (groupId, fragmentCollectionId, version, type_, status);
create index IX_519A387F on FragmentEntryVersion (groupId, fragmentEntryKey[$COLUMN_LENGTH:75$]);
create unique index IX_32C340C7 on FragmentEntryVersion (groupId, version, fragmentEntryKey[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_850F2979 on FragmentEntryVersion (type_);
create index IX_B2BEE958 on FragmentEntryVersion (uuid_[$COLUMN_LENGTH:75$]);
create unique index IX_E87ED835 on FragmentEntryVersion (version, fragmentEntryId, ctCollectionId);
create index IX_9CB5E4AF on FragmentEntryVersion (version, type_);
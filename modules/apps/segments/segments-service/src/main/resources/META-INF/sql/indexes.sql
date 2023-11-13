create index IX_175FC150 on SegmentsEntry (companyId);
create unique index IX_DB53F1B1 on SegmentsEntry (groupId, segmentsEntryKey[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_90AB04A7 on SegmentsEntry (source[$COLUMN_LENGTH:75$]);
create index IX_5BFEEA84 on SegmentsEntry (type_[$COLUMN_LENGTH:75$], active_);
create index IX_56AA45CF on SegmentsEntry (type_[$COLUMN_LENGTH:75$], groupId, active_, source[$COLUMN_LENGTH:75$]);
create index IX_8046BADC on SegmentsEntry (uuid_[$COLUMN_LENGTH:75$]);

create index IX_5FBA8532 on SegmentsEntryRel (classNameId, classPK, groupId);
create unique index IX_AAD22503 on SegmentsEntryRel (classNameId, classPK, segmentsEntryId, ctCollectionId);
create index IX_AB286250 on SegmentsEntryRel (segmentsEntryId);

create unique index IX_2876B1F2 on SegmentsEntryRole (roleId, segmentsEntryId, ctCollectionId);

create index IX_EBCFE1C4 on SegmentsExperience (groupId, plid, active_);
create unique index IX_7F495C9B on SegmentsExperience (groupId, plid, ctCollectionId, priority);
create unique index IX_A4991554 on SegmentsExperience (groupId, plid, ctCollectionId, segmentsExperienceKey[$COLUMN_LENGTH:75$]);
create index IX_1ED9E03B on SegmentsExperience (groupId, plid, segmentsEntryId, active_);
create index IX_E90B4ACD on SegmentsExperience (segmentsEntryId);
create index IX_42071D24 on SegmentsExperience (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_5CB505A9 on SegmentsExperiment (groupId, ctCollectionId, segmentsExperienceId, plid);
create unique index IX_9749F869 on SegmentsExperiment (groupId, segmentsExperimentKey[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_127B4FCF on SegmentsExperiment (segmentsExperimentKey[$COLUMN_LENGTH:75$]);
create index IX_2701CFF1 on SegmentsExperiment (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_9EDCFAE5 on SegmentsExperimentRel (segmentsExperienceId, segmentsExperimentId, ctCollectionId);
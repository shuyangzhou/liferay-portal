create index IX_5A1F4BFC on LayoutPageTemplateCollection (groupId, parentLPTCollectionId);
create unique index IX_6259AC7B on LayoutPageTemplateCollection (groupId, type_, ctCollectionId, lptCollectionKey[$COLUMN_LENGTH:75$]);
create unique index IX_D36D1D01 on LayoutPageTemplateCollection (groupId, type_, ctCollectionId, name[$COLUMN_LENGTH:75$]);
create index IX_A17F0EBD on LayoutPageTemplateCollection (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_87D3788E on LayoutPageTemplateEntry (ctCollectionId, plid);
create index IX_A6459477 on LayoutPageTemplateEntry (groupId, classNameId, classTypeId, defaultTemplate);
create index IX_957F6C5D on LayoutPageTemplateEntry (groupId, classNameId, status, classTypeId, defaultTemplate);
create unique index IX_ECAFF217 on LayoutPageTemplateEntry (groupId, ctCollectionId, layoutPageTemplateEntryKey[$COLUMN_LENGTH:75$]);
create index IX_E7CC5585 on LayoutPageTemplateEntry (groupId, layoutPageTemplateCollectionId);
create index IX_30AFAD84 on LayoutPageTemplateEntry (groupId, name[$COLUMN_LENGTH:75$], layoutPageTemplateCollectionId);
create index IX_A4733F6B on LayoutPageTemplateEntry (groupId, status, layoutPageTemplateCollectionId);
create index IX_4C3A286A on LayoutPageTemplateEntry (groupId, status, name[$COLUMN_LENGTH:75$], layoutPageTemplateCollectionId);
create index IX_CD171EDF on LayoutPageTemplateEntry (groupId, type_, classNameId, defaultTemplate);
create index IX_614AC362 on LayoutPageTemplateEntry (groupId, type_, classNameId, name[$COLUMN_LENGTH:75$], classTypeId);
create index IX_E2488048 on LayoutPageTemplateEntry (groupId, type_, classNameId, status, name[$COLUMN_LENGTH:75$], classTypeId);
create index IX_4BCAC4B0 on LayoutPageTemplateEntry (groupId, type_, layoutPageTemplateCollectionId);
create unique index IX_7C97630F on LayoutPageTemplateEntry (groupId, type_, name[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_85E526A1 on LayoutPageTemplateEntry (groupId, type_, status, defaultTemplate);
create index IX_A185457E on LayoutPageTemplateEntry (layoutPrototypeId);
create index IX_2D68D26F on LayoutPageTemplateEntry (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_D822FD2D on LayoutPageTemplateStructure (groupId, plid, ctCollectionId);
create index IX_542ECD0E on LayoutPageTemplateStructure (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_843407A3 on LayoutPageTemplateStructureRel (segmentsExperienceId, layoutPageTemplateStructureId, ctCollectionId);
create index IX_E86D94F5 on LayoutPageTemplateStructureRel (uuid_[$COLUMN_LENGTH:75$]);
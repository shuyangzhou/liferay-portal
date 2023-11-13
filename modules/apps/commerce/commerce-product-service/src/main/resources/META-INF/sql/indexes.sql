create index IX_38280B2E on CChannelAccountEntryRel (accountEntryId);
create unique index IX_EBF9C243 on CChannelAccountEntryRel (classNameId, classPK, type_, accountEntryId, commerceChannelId, ctCollectionId);
create index IX_79DB2231 on CChannelAccountEntryRel (classNameId, classPK, type_, commerceChannelId);

create index IX_4E725857 on CPAttachmentFileEntry (classNameId, classPK, cdnURL[$COLUMN_LENGTH:4000$]);
create index IX_DD114140 on CPAttachmentFileEntry (classNameId, classPK, fileEntryId);
create index IX_B2AFFCE5 on CPAttachmentFileEntry (classNameId, classPK, status, displayDate);
create index IX_60E70397 on CPAttachmentFileEntry (classNameId, classPK, status, type_, galleryEnabled);
create index IX_36552B11 on CPAttachmentFileEntry (classNameId, fileEntryId, groupId);
create index IX_693F40B1 on CPAttachmentFileEntry (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_5B2A1075 on CPAttachmentFileEntry (fileEntryId);
create index IX_A0B4C71A on CPAttachmentFileEntry (status, displayDate);
create index IX_80E4E528 on CPAttachmentFileEntry (uuid_[$COLUMN_LENGTH:75$]);

create index IX_95975FB4 on CPDSpecificationOptionValue (CPDefinitionId, CPOptionCategoryId);
create index IX_173E8E91 on CPDSpecificationOptionValue (CPDefinitionId, CPSpecificationOptionId);
create index IX_4F4EDBA5 on CPDSpecificationOptionValue (CPOptionCategoryId);
create index IX_573BE140 on CPDSpecificationOptionValue (CPSpecificationOptionId);
create index IX_8DA57014 on CPDSpecificationOptionValue (groupId);
create index IX_F5BFF79E on CPDSpecificationOptionValue (uuid_[$COLUMN_LENGTH:75$]);

create index IX_3D5A0021 on CPDefinition (CPTaxCategoryId);
create index IX_F1AEC8A7 on CPDefinition (CProductId, version);
create index IX_217AF702 on CPDefinition (companyId);
create index IX_99C4ED10 on CPDefinition (groupId, subscriptionEnabled);
create index IX_1F4B9C67 on CPDefinition (status, CProductId);
create index IX_A465D100 on CPDefinition (status, displayDate);
create index IX_419350EA on CPDefinition (status, groupId);
create index IX_46B4998E on CPDefinition (uuid_[$COLUMN_LENGTH:75$]);

create index IX_221F7FA6 on CPDefinitionLink (status, displayDate);
create index IX_670DBA17 on CPDefinitionLink (status, expirationDate);
create index IX_BEEF1F4C on CPDefinitionLink (status, type_[$COLUMN_LENGTH:75$], CPDefinitionId);
create index IX_668B3D40 on CPDefinitionLink (status, type_[$COLUMN_LENGTH:75$], CProductId);
create unique index IX_3A90EAC9 on CPDefinitionLink (type_[$COLUMN_LENGTH:75$], CProductId, CPDefinitionId, ctCollectionId);
create index IX_5F842B4 on CPDefinitionLink (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_CB617913 on CPDefinitionLocalization (CPDefinitionId, languageId[$COLUMN_LENGTH:75$], ctCollectionId);

create unique index IX_170B5E88 on CPDefinitionOptionRel (CPDefinitionId, ctCollectionId, CPOptionId);
create unique index IX_A1C589EB on CPDefinitionOptionRel (CPDefinitionId, ctCollectionId, key_[$COLUMN_LENGTH:75$]);
create index IX_BDB8420C on CPDefinitionOptionRel (CPDefinitionId, required);
create index IX_749E99EB on CPDefinitionOptionRel (CPDefinitionId, skuContributor);
create index IX_449BFCFE on CPDefinitionOptionRel (companyId);
create index IX_A65BAB00 on CPDefinitionOptionRel (groupId);
create index IX_B65FFD8A on CPDefinitionOptionRel (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_55A05F1E on CPDefinitionOptionValueRel (CPDefinitionOptionRelId, key_[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_4A77D282 on CPDefinitionOptionValueRel (CPDefinitionOptionRelId, preselected);
create index IX_3EB86274 on CPDefinitionOptionValueRel (CPInstanceUuid[$COLUMN_LENGTH:75$]);
create index IX_44C2E505 on CPDefinitionOptionValueRel (companyId);
create index IX_695AE8C7 on CPDefinitionOptionValueRel (groupId);
create index IX_2434CAD7 on CPDefinitionOptionValueRel (key_[$COLUMN_LENGTH:75$]);
create index IX_8DD20111 on CPDefinitionOptionValueRel (uuid_[$COLUMN_LENGTH:75$]);

create index IX_290BF7BA on CPDisplayLayout (classNameId, classPK);
create unique index IX_7F728A18 on CPDisplayLayout (groupId, classNameId, classPK, ctCollectionId);
create index IX_965CA8C5 on CPDisplayLayout (groupId, layoutPageTemplateEntryUuid[$COLUMN_LENGTH:75$]);
create index IX_381B82DE on CPDisplayLayout (groupId, layoutUuid[$COLUMN_LENGTH:75$]);
create index IX_FEC526EF on CPDisplayLayout (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_31443E86 on CPInstance (CPDefinitionId, CPInstanceUuid[$COLUMN_LENGTH:75$], ctCollectionId);
create unique index IX_2D902FD4 on CPInstance (CPDefinitionId, sku[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_34763899 on CPInstance (CPInstanceUuid[$COLUMN_LENGTH:75$]);
create index IX_1BE250D5 on CPInstance (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_C1F8242 on CPInstance (groupId);
create index IX_9FB1144D on CPInstance (sku[$COLUMN_LENGTH:75$], companyId);
create index IX_C399720F on CPInstance (status, CPDefinitionId, displayDate);
create index IX_7C65903E on CPInstance (status, displayDate);
create index IX_FF605F28 on CPInstance (status, groupId);
create index IX_7F622758 on CPInstance (status, replacementCPInstanceUuid[$COLUMN_LENGTH:75$], replacementCProductId);
create index IX_4654BD4C on CPInstance (uuid_[$COLUMN_LENGTH:75$]);

create index IX_E551D3AA on CPInstanceOptionValueRel (CPDefinitionOptionRelId);
create unique index IX_E45EE045 on CPInstanceOptionValueRel (CPInstanceId, CPDefinitionOptionRelId, CPDefinitionOptionValueRelId, ctCollectionId);
create index IX_1F153982 on CPInstanceOptionValueRel (CPInstanceId, CPDefinitionOptionValueRelId);
create index IX_65D1C04F on CPInstanceOptionValueRel (uuid_[$COLUMN_LENGTH:75$]);

create index IX_4351C7A1 on CPInstanceUOM (CPInstanceId, active_);
create unique index IX_C6BA8E9A on CPInstanceUOM (CPInstanceId, key_[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_611154B9 on CPInstanceUOM (CPInstanceId, primary_);
create index IX_2EED7F68 on CPInstanceUOM (companyId, key_[$COLUMN_LENGTH:75$], sku[$COLUMN_LENGTH:75$]);
create index IX_9DCFEBFC on CPInstanceUOM (companyId, sku[$COLUMN_LENGTH:75$]);
create index IX_ABE6B4BD on CPInstanceUOM (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_7AC67B41 on CPMeasurementUnit (companyId, key_[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_107489F5 on CPMeasurementUnit (companyId, type_, primary_);
create index IX_3DC7E6E4 on CPMeasurementUnit (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_CBEE515B on CPMeasurementUnit (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_3D33A4D2 on CPOption (companyId, key_[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_585FA1B5 on CPOption (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_A64FCE2C on CPOption (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_D9120F4 on CPOptionCategory (companyId, key_[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_ABB730CE on CPOptionCategory (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_3705EB8 on CPOptionValue (CPOptionId, key_[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_C95EFDB3 on CPOptionValue (companyId);
create index IX_3177EE48 on CPOptionValue (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_D7C1A0BF on CPOptionValue (uuid_[$COLUMN_LENGTH:75$]);

create index IX_421ED80 on CPSpecificationOption (CPOptionCategoryId);
create unique index IX_8F980DC9 on CPSpecificationOption (companyId, key_[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_972DFDE3 on CPSpecificationOption (uuid_[$COLUMN_LENGTH:75$]);

create index IX_64046706 on CPTaxCategory (companyId);
create index IX_EC2F31B on CPTaxCategory (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_705EAB92 on CPTaxCategory (uuid_[$COLUMN_LENGTH:75$]);

create index IX_439F910B on CProduct (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_77F5B8F8 on CProduct (groupId);
create index IX_40952982 on CProduct (uuid_[$COLUMN_LENGTH:75$]);

create index IX_7A691498 on CommerceCatalog (accountEntryId);
create index IX_65864AFC on CommerceCatalog (companyId, system_);
create index IX_63DA6FD9 on CommerceCatalog (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_37D36450 on CommerceCatalog (uuid_[$COLUMN_LENGTH:75$]);

create index IX_C2C38B02 on CommerceChannel (accountEntryId);
create index IX_482E401A on CommerceChannel (companyId);
create index IX_B02BCE2F on CommerceChannel (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_E1ECD95 on CommerceChannel (siteGroupId);
create index IX_9E82EA6 on CommerceChannel (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_4469A625 on CommerceChannelRel (commerceChannelId, classNameId, classPK, ctCollectionId);
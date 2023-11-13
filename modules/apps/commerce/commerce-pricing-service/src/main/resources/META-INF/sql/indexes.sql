create unique index IX_DA09B6F3 on CPricingClassCPDefinitionRel (CPDefinitionId, commercePricingClassId, ctCollectionId);

create index IX_176CA5EC on CommercePriceModifier (commercePriceListId);
create index IX_FCACD082 on CommercePriceModifier (companyId, target[$COLUMN_LENGTH:75$]);
create index IX_70709A52 on CommercePriceModifier (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_93813A2F on CommercePriceModifier (status, companyId, groupId);
create index IX_C60214FB on CommercePriceModifier (status, displayDate);
create index IX_E3CDA8A2 on CommercePriceModifier (status, expirationDate);
create index IX_5C17A0C9 on CommercePriceModifier (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_510AD1A9 on CommercePriceModifierRel (classNameId, classPK, commercePriceModifierId, ctCollectionId);

create index IX_B58209D5 on CommercePricingClass (companyId);
create index IX_D2CFD76A on CommercePricingClass (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_33040DE1 on CommercePricingClass (uuid_[$COLUMN_LENGTH:75$]);
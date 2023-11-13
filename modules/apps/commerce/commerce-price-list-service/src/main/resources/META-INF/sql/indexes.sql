create unique index IX_8EF03EDA on CPLCommerceGroupAccountRel (commercePriceListId, commerceAccountGroupId, ctCollectionId);
create index IX_29EF081D on CPLCommerceGroupAccountRel (uuid_[$COLUMN_LENGTH:75$]);

create index IX_CCBB916A on CommercePriceEntry (CPInstanceUuid[$COLUMN_LENGTH:75$], quantity, unitOfMeasureKey[$COLUMN_LENGTH:75$]);
create index IX_CA7A2D0D on CommercePriceEntry (commercePriceListId);
create index IX_5E36B51E on CommercePriceEntry (companyId);
create index IX_9638DD33 on CommercePriceEntry (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_E653D524 on CommercePriceEntry (status, CPInstanceUuid[$COLUMN_LENGTH:75$], commercePriceListId);
create index IX_790F9C1C on CommercePriceEntry (status, displayDate);
create index IX_770DC1E1 on CommercePriceEntry (status, expirationDate);
create index IX_C15BC5AA on CommercePriceEntry (uuid_[$COLUMN_LENGTH:75$]);

create index IX_473B4D8D on CommercePriceList (commerceCurrencyId);
create index IX_2AA1AF56 on CommercePriceList (companyId);
create index IX_34A6436B on CommercePriceList (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_3AE5B429 on CommercePriceList (groupId, catalogBasePriceList);
create index IX_8B683FEB on CommercePriceList (groupId, companyId, status, type_[$COLUMN_LENGTH:75$]);
create index IX_354C658C on CommercePriceList (groupId, type_[$COLUMN_LENGTH:75$], catalogBasePriceList);
create index IX_863045BB on CommercePriceList (parentCommercePriceListId);
create index IX_31913054 on CommercePriceList (status, displayDate);
create index IX_1B0C9BE2 on CommercePriceList (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_D44100F6 on CommercePriceListAccountRel (commercePriceListId, commerceAccountId, ctCollectionId);
create index IX_919FF916 on CommercePriceListAccountRel (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_A19A53CA on CommercePriceListChannelRel (commercePriceListId, commerceChannelId, ctCollectionId);
create index IX_A7045AEC on CommercePriceListChannelRel (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_EB1C0CF8 on CommercePriceListDiscountRel (commercePriceListId, commerceDiscountId, ctCollectionId);
create index IX_4F76A982 on CommercePriceListDiscountRel (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_4EA60BE2 on CommercePriceListOrderTypeRel (commercePriceListId, commerceOrderTypeId, ctCollectionId);
create index IX_C6ECAD11 on CommercePriceListOrderTypeRel (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_4072830C on CommerceTierPriceEntry (commercePriceEntryId, minQuantity, ctCollectionId);
create index IX_F5D5725C on CommerceTierPriceEntry (companyId);
create index IX_305FAD71 on CommerceTierPriceEntry (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_299C7994 on CommerceTierPriceEntry (status, commercePriceEntryId, minQuantity);
create index IX_8A8963DA on CommerceTierPriceEntry (status, displayDate);
create index IX_21C0F963 on CommerceTierPriceEntry (status, expirationDate);
create index IX_71F6D1E8 on CommerceTierPriceEntry (uuid_[$COLUMN_LENGTH:75$]);
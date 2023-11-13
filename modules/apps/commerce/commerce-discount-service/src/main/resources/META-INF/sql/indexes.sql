create unique index IX_9D768AF5 on CDiscountCAccountGroupRel (commerceAccountGroupId, commerceDiscountId);

create index IX_A7A710FC on CommerceDiscount (companyId, active_, couponCode[$COLUMN_LENGTH:75$]);
create index IX_5A1D8CDB on CommerceDiscount (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_90F8C967 on CommerceDiscount (status, companyId, active_, levelType[$COLUMN_LENGTH:75$]);
create index IX_122C15C4 on CommerceDiscount (status, displayDate);
create index IX_2FBF0739 on CommerceDiscount (status, expirationDate);
create index IX_F1A4C552 on CommerceDiscount (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_E082887A on CommerceDiscountAccountRel (commerceDiscountId, commerceAccountId);
create index IX_CEE71686 on CommerceDiscountAccountRel (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_614617A on CommerceDiscountOrderTypeRel (commerceOrderTypeId, commerceDiscountId);
create index IX_CEE22E81 on CommerceDiscountOrderTypeRel (uuid_[$COLUMN_LENGTH:75$]);

create index IX_6B4EEC38 on CommerceDiscountRel (classNameId, classPK);
create index IX_DDFDEF40 on CommerceDiscountRel (classNameId, commerceDiscountId);

create index IX_CB9E6769 on CommerceDiscountRule (commerceDiscountId);

create index IX_70440FFF on CommerceDiscountUsageEntry (commerceDiscountId, commerceOrderId, commerceAccountId);
create unique index IX_17D56F1B on CPDAvailabilityEstimate (CProductId);
create index IX_E560850D on CPDAvailabilityEstimate (commerceAvailabilityEstimateId);
create index IX_109320B4 on CPDAvailabilityEstimate (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_2A31024F on CPDefinitionInventory (CPDefinitionId, ctCollectionId);
create index IX_6102F312 on CPDefinitionInventory (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_4F4C712A on CSOptionAccountEntryRel (commerceChannelId, accountEntryId);
create index IX_64B9CFFC on CSOptionAccountEntryRel (commerceShippingOptionKey[$COLUMN_LENGTH:75$]);

create unique index IX_9DD3ABD3 on CommerceAddressRestriction (countryId, classNameId, classPK);

create index IX_72527224 on CommerceAvailabilityEstimate (companyId);
create index IX_B83AF5B0 on CommerceAvailabilityEstimate (uuid_[$COLUMN_LENGTH:75$]);

create index IX_12131FC1 on CommerceOrder (billingAddressId);
create index IX_6CF56904 on CommerceOrder (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_9C04F6F8 on CommerceOrder (groupId, commercePaymentMethodKey[$COLUMN_LENGTH:75$]);
create index IX_9ACAF78A on CommerceOrder (orderStatus, commerceAccountId, createDate);
create index IX_4F4CAEE4 on CommerceOrder (orderStatus, groupId, commerceAccountId);
create index IX_67E0AF05 on CommerceOrder (orderStatus, groupId, userId);
create index IX_75679B1F on CommerceOrder (orderStatus, userId, createDate);
create index IX_4B11FAD8 on CommerceOrder (shippingAddressId);
create index IX_EFAA753 on CommerceOrder (userId);
create index IX_35A4137B on CommerceOrder (uuid_[$COLUMN_LENGTH:75$]);

create index IX_654BB574 on CommerceOrderItem (CIBookedQuantityId);
create index IX_2E1BB39D on CommerceOrderItem (CPInstanceId);
create index IX_F9E8D927 on CommerceOrderItem (CProductId);
create index IX_415AF3E3 on CommerceOrderItem (commerceOrderId, CPInstanceId);
create index IX_15B37023 on CommerceOrderItem (commerceOrderId, subscription);
create index IX_F0E98FC7 on CommerceOrderItem (customerCommerceOrderItemId);
create index IX_2ED6FAB1 on CommerceOrderItem (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_8E1472FB on CommerceOrderItem (parentCommerceOrderItemId);
create index IX_D1309F28 on CommerceOrderItem (uuid_[$COLUMN_LENGTH:75$]);

create index IX_CEB86C22 on CommerceOrderNote (commerceOrderId, restricted);
create index IX_9646AEB2 on CommerceOrderNote (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_6A627529 on CommerceOrderNote (uuid_[$COLUMN_LENGTH:75$]);

create index IX_CF274005 on CommerceOrderPayment (commerceOrderId);

create index IX_72C90BD4 on CommerceOrderType (companyId, active_);
create index IX_110C106A on CommerceOrderType (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_CAB43113 on CommerceOrderType (status, displayDate);
create index IX_56A0F58A on CommerceOrderType (status, expirationDate);
create index IX_FAD246E1 on CommerceOrderType (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_CBAD3B91 on CommerceOrderTypeRel (commerceOrderTypeId, classNameId, classPK);
create index IX_F8C44DCB on CommerceOrderTypeRel (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_D24B6642 on CommerceOrderTypeRel (uuid_[$COLUMN_LENGTH:75$]);

create index IX_98DF6A2 on CommerceShipment (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_616BDD15 on CommerceShipment (groupId, commerceAddressId);
create index IX_68FBA2B5 on CommerceShipment (groupId, status);
create index IX_67979D19 on CommerceShipment (uuid_[$COLUMN_LENGTH:75$]);

create unique index IX_4FAC36D0 on CommerceShipmentItem (commerceOrderItemId, commerceShipmentId, commerceInventoryWarehouseId);
create index IX_8B13894F on CommerceShipmentItem (externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_DB0BB83C on CommerceShipmentItem (groupId);
create index IX_64EA29C6 on CommerceShipmentItem (uuid_[$COLUMN_LENGTH:75$]);

create index IX_42E5F6EF on CommerceShippingMethod (groupId, active_);
create unique index IX_C4557F93 on CommerceShippingMethod (groupId, engineKey[$COLUMN_LENGTH:75$]);

create unique index IX_D7D137B1 on CommerceSubscriptionEntry (commerceOrderItemId);
create index IX_B99DE058 on CommerceSubscriptionEntry (companyId, groupId, userId);
create index IX_43E6F382 on CommerceSubscriptionEntry (companyId, userId);
create index IX_3961BCA on CommerceSubscriptionEntry (groupId);
create index IX_B496E103 on CommerceSubscriptionEntry (subscriptionStatus);
create index IX_383418D4 on CommerceSubscriptionEntry (uuid_[$COLUMN_LENGTH:75$]);
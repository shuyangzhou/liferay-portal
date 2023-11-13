create index IX_786D171A on Subscription (classNameId, companyId, classPK);
create index IX_C4FAEA47 on Subscription (groupId);
create unique index IX_FCCD4132 on Subscription (userId, classNameId, companyId, classPK, ctCollectionId);
create index IX_1290B81 on Subscription (userId, groupId);
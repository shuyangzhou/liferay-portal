create unique index IX_AFBF7DA on CNTemplateCAccountGroupRel (commerceAccountGroupId, commerceNotificationTemplateId);

create index IX_6E9D8183 on CNotificationAttachment (CNotificationQueueEntryId);
create index IX_9BCE71BD on CNotificationAttachment (uuid_[$COLUMN_LENGTH:75$]);

create index IX_F9149FC on CommerceNotificationQueueEntry (commerceNotificationTemplateId);
create index IX_1CB981DE on CommerceNotificationQueueEntry (sent, groupId, classNameId, classPK);
create index IX_80026CA7 on CommerceNotificationQueueEntry (sentDate);

create index IX_AFC4A180 on CommerceNotificationTemplate (groupId, enabled, type_[$COLUMN_LENGTH:75$]);
create index IX_753B890E on CommerceNotificationTemplate (uuid_[$COLUMN_LENGTH:75$]);
create index IX_2E4A2004 on DispatchLog (dispatchTaskId, status);

create unique index IX_F0C20342 on DispatchTask (companyId, name[$COLUMN_LENGTH:75$]);
create index IX_8E0326B2 on DispatchTask (companyId, type_[$COLUMN_LENGTH:75$]);

create unique index IX_D86DCE63 on DispatchTrigger (companyId, name[$COLUMN_LENGTH:75$]);
create index IX_9BD0BFB1 on DispatchTrigger (companyId, type_[$COLUMN_LENGTH:75$]);
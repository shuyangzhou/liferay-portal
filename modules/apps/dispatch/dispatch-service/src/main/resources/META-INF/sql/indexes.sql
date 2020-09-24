create index IX_36F4EB5F on DispatchLog (dispatchTriggerId, status);

create index IX_111D2CE3 on DispatchTrigger (companyId, jobType[$COLUMN_LENGTH:75$]);
create unique index IX_D86DCE63 on DispatchTrigger (companyId, name[$COLUMN_LENGTH:75$]);
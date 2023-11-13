create index IX_F68F2F64 on WMSLADefinition (active_, wmSLADefinitionId);
create index IX_8872D52F on WMSLADefinition (companyId, active_, processId, name[$COLUMN_LENGTH:75$]);
create index IX_764B37D1 on WMSLADefinition (companyId, active_, processId, status, processVersion[$COLUMN_LENGTH:75$]);
create index IX_73175D43 on WMSLADefinition (companyId, status);
create index IX_B867D369 on WMSLADefinition (uuid_[$COLUMN_LENGTH:75$]);

create index IX_C95794DB on WMSLADefinitionVersion (uuid_[$COLUMN_LENGTH:75$]);
create index IX_A59DFB41 on WMSLADefinitionVersion (wmSLADefinitionId, version[$COLUMN_LENGTH:75$]);
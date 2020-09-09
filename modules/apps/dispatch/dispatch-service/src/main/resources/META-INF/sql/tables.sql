create table DispatchLog (
	mvccVersion LONG default 0 not null,
	dispatchLogId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	dispatchTaskId LONG,
	endDate DATE null,
	error TEXT null,
	output_ TEXT null,
	startDate DATE null,
	status INTEGER
);

create table DispatchTask (
	mvccVersion LONG default 0 not null,
	dispatchTaskId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	active_ BOOLEAN,
	cronExpression VARCHAR(75) null,
	name VARCHAR(75) null,
	system_ BOOLEAN,
	type_ VARCHAR(75) null,
	typeSettings VARCHAR(75) null
);

create table DispatchTrigger (
	mvccVersion LONG default 0 not null,
	dispatchTriggerId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	active_ BOOLEAN,
	cronExpression VARCHAR(75) null,
	endDate DATE null,
	name VARCHAR(75) null,
	startDate DATE null,
	system_ BOOLEAN,
	type_ VARCHAR(75) null,
	typeSettings TEXT null
);
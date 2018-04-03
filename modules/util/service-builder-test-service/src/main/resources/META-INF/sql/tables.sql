create table BigDecimalEntry (
	bigDecimalEntryId LONG not null primary key,
	bigDecimalValue DECIMAL(30, 16) null
);

create table VersionedEntry (
	mvccVersion LONG default 0 not null,
	versionedEntryId LONG not null primary key,
	content VARCHAR(75) null,
	headId LONG
);

create table VersionedEntryVersion (
	versionedEntryVersionId LONG not null primary key,
	version INTEGER,
	versionedEntryId LONG,
	content VARCHAR(75) null
);
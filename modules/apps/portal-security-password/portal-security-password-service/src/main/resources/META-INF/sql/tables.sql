create table HashAlgorithmEntry (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	entryId LONG not null primary key,
	createDate DATE null,
	modifiedDate DATE null,
	name VARCHAR(75) null,
	meta VARCHAR(75) null
);

create table PasswordEntry (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	entryId LONG not null primary key,
	userId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	hash VARCHAR(75) null
);

create table PasswordMeta (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	metaId LONG not null primary key,
	createDate DATE null,
	modifiedDate DATE null,
	passwordEntryId LONG,
	hashAlgorithmEntryId LONG,
	salt VARCHAR(75) null
);
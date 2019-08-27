create table ViewCountEntry (
	viewCountEntryId LONG not null primary key,
	companyId LONG,
	classNameId LONG,
	classPK LONG,
	viewCount LONG
);